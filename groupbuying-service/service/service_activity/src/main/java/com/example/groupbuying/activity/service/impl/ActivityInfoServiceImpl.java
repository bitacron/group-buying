package com.example.groupbuying.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.groupbuying.activity.mapper.ActivityRuleMapper;
import com.example.groupbuying.activity.mapper.ActivitySkuMapper;
import com.example.groupbuying.activity.service.CouponInfoService;
import com.example.groupbuying.client.product.ProductFeignClient;
import com.example.groupbuying.enums.ActivityType;
import com.example.groupbuying.model.activity.ActivityInfo;
import com.example.groupbuying.activity.mapper.ActivityInfoMapper;
import com.example.groupbuying.activity.service.ActivityInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.groupbuying.model.activity.ActivityRule;
import com.example.groupbuying.model.activity.ActivitySku;
import com.example.groupbuying.model.activity.CouponInfo;
import com.example.groupbuying.model.order.CartInfo;
import com.example.groupbuying.model.product.SkuInfo;
import com.example.groupbuying.vo.activity.ActivityRuleVo;
import com.example.groupbuying.vo.order.CartInfoVo;
import com.example.groupbuying.vo.order.OrderConfirmVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author example
 * @since 2024-04-24
 */

@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {

    @Autowired
    private ActivityInfoMapper activityInfoMapper;

    @Autowired
    private ActivityRuleMapper activityRuleMapper;

    @Autowired
    private ActivitySkuMapper activitySkuMapper;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private CouponInfoService couponInfoService;

    //优惠活动列表方法
    @Override
    public IPage<ActivityInfo> selectPageActivityInfo(Page<ActivityInfo> pageParam) {
        QueryWrapper<ActivityInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        IPage<ActivityInfo> page = activityInfoMapper.selectPage(pageParam, queryWrapper);
        //page.toString()
        page.getRecords().forEach(item -> {
            item.setActivityTypeString(item.getActivityType().getComment());
        });
        return page;
    }

    //1.根据活动id获取活动规则数据
    @Override
    public Map<String, Object> findActivityRuleList(Long activityId) {
        Map<String, Object> result = new HashMap<>();
        // 1.根据活动id查询，查询规则列表activity_rule表
        QueryWrapper<ActivityRule> queryWrapper = new QueryWrapper<ActivityRule>();
        queryWrapper.eq("activity_id",activityId);
        List<ActivityRule> activityRuleList = activityRuleMapper.selectList(queryWrapper);
        result.put("activityRuleList", activityRuleList);
        // 2.根据活动id查询，查询规则商品列表activity_sku表
        QueryWrapper<ActivitySku> activitySkuQueryWrapper = new QueryWrapper<ActivitySku>();
        activitySkuQueryWrapper.eq("activity_id",activityId);
        List<ActivitySku> activitySkuList = activitySkuMapper.selectList(activitySkuQueryWrapper);
        List<Long> skuIdList = activitySkuList.stream().map(ActivitySku::getSkuId).collect(Collectors.toList());
        //通过远程调用service-product模块接口，根据skuId列表得到商品信息
        List<SkuInfo> skuInfoList = new ArrayList<>();
        if (!skuIdList.isEmpty()) {
            skuInfoList = productFeignClient.findSkuInfoList(skuIdList);
        }
        result.put("skuInfoList", skuInfoList);
        return result;
    }

    //2.在活动里面添加规则数据
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveActivityRule(ActivityRuleVo activityRuleVo) {
        // 第一步 根据活动id删除之前规则数据
        activityRuleMapper.delete(new QueryWrapper<ActivityRule>().eq("activity_id",activityRuleVo.getActivityId()));
        activitySkuMapper.delete(new QueryWrapper<ActivitySku>().eq("activity_id",activityRuleVo.getActivityId()));
        // 第二步 获取规则列表数组
        List<ActivityRule> activityRuleList = activityRuleVo.getActivityRuleList();
        ActivityInfo activityInfo = baseMapper.selectById(activityRuleVo.getActivityId());
        for(ActivityRule activityRule : activityRuleList) {
            activityRule.setActivityId(activityRuleVo.getActivityId());
            activityRule.setActivityType(activityInfo.getActivityType());
            activityRuleMapper.insert(activityRule);
        }
        // 第三部 获取规则范围数据
        List<ActivitySku> activitySkuList = activityRuleVo.getActivitySkuList();
        for(ActivitySku activitySku : activitySkuList) {
            activitySku.setActivityId(activityRuleVo.getActivityId());
            activitySkuMapper.insert(activitySku);
        }
    }

    //3.根据关键字查询匹配sku信息
    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        // 第一步 根据关键字查询sku匹配内容列表
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoByKeyword(keyword);
        // 判断：根据关键字查找不到集合，直接返回空
        if (skuInfoList.size() == 0) {
            return skuInfoList;
        }
        List<Long> skuIdList = skuInfoList.stream().map(SkuInfo::getId).collect(Collectors.toList());
        // 第二步 判断添加商品之前是否参加过活动，如果之前参加过，活动正在进行中，排除商品
        List<SkuInfo> notExistSkuInfoList = new ArrayList<>();
        // 查询两张表activity_info和activity_sku判断，编写sql语句实现
        List<Long> existSkuIdList = activityInfoMapper.selectExistSkuIdList(skuIdList);
        String existSkuIdString = "," + StringUtils.join(existSkuIdList.toArray(), ",") + ",";
        for(SkuInfo skuInfo : skuInfoList) {
            if(existSkuIdString.indexOf(","+skuInfo.getId()+",") == -1) {
                notExistSkuInfoList.add(skuInfo);
            }
        }
        return notExistSkuInfoList;
    }

    /**
     * 根据skuId列表获取促销信息
     *
     * @param skuIdList skuId列表
     * @return 取促销信息
     */
    @Override
    public Map<Long, List<String>> findActivity(List<Long> skuIdList) {
        Map<Long, List<String>> result = new HashMap<>();
        //skuIdList遍历，得到每个skuId
        skuIdList.forEach(skuId -> {
            //根据skuId进行查询，查询sku对应活动里面规则列表
            List<ActivityRule> activityRuleList = baseMapper.findActivityRule(skuId);
            //数据封装，规则名称
            if(!CollectionUtils.isEmpty(activityRuleList)) {
                List<String> ruleList = new ArrayList<>();
                //把规则名称处理
                for (ActivityRule activityRule:activityRuleList) {
                    ruleList.add(this.getRuleDesc(activityRule));
                }
                result.put(skuId,ruleList);
            }
        });
        return result;
    }

    //构造规则名称的方法
    private String getRuleDesc(ActivityRule activityRule) {
        ActivityType activityType = activityRule.getActivityType();
        StringBuilder ruleDesc = new StringBuilder();
        if (activityType == ActivityType.FULL_REDUCTION) {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionAmount())
                    .append("元减")
                    .append(activityRule.getBenefitAmount())
                    .append("元");
        } else {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionNum())
                    .append("元打")
                    .append(activityRule.getBenefitDiscount())
                    .append("折");
        }
        return ruleDesc.toString();
    }

    @Override
    public Map<String, Object> findActivityAndCoupon(Long skuId, Long userId) {
        // 根据skuId获取sku营销活动。一个sku只能有一个促销活动，一个活动有多个活动规则（如满赠，满100送10，满500送50）
        List<ActivityRule> activityRuleList = this.findActivityRuleBySkuId(skuId);

        // 根据skuId和userId获取优惠券信息
        List<CouponInfo> couponInfoList = couponInfoService.findCouponInfo(skuId, userId);

        Map<String, Object> map = new HashMap<>();
        map.put("activityRuleList", activityRuleList);
        map.put("couponInfoList", couponInfoList);
        return map;
    }
    //根据skuId获取活动规则数据
    @Override
    public List<ActivityRule> findActivityRuleBySkuId(Long skuId) {
        List<ActivityRule> activityRuleList = baseMapper.findActivityRule(skuId);
        if(!CollectionUtils.isEmpty(activityRuleList)) {
            for (ActivityRule activityRule : activityRuleList) {
                String ruleDesc = this.getRuleDesc(activityRule);
                activityRule.setRuleDesc(ruleDesc);
            }
        }
        return activityRuleList;
    }

    /**
     * 获取购物车满足条件的促销与优惠券信息
     *
     * @param cartInfoList 购物车列表
     * @param userId       用户Id
     * @return 购物车满足条件的促销与优惠券信息
     */
    @Override
    public OrderConfirmVo findCartActivityAndCoupon(List<CartInfo> cartInfoList, Long userId) {
        // 1.获取购物车中每个购物项参与活动，根据活动规则进行分组
        // 一个活动规则对应多个商品
        List<CartInfoVo> carInfoVoList = this.findCartActivityList(cartInfoList);

        // 2.计算商品参与活动之后的金额
        BigDecimal activityReduceAmount = carInfoVoList.stream()
                .filter(carInfoVo -> null != carInfoVo.getActivityRule())
                .map(carInfoVo -> carInfoVo.getActivityRule().getReduceAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 3.购物车可使用的优惠券列表
        List<CouponInfo> couponInfoList = couponInfoService.findCartCouponInfo(cartInfoList, userId);

        // 4.计算商品使用优惠券之后金额，一次只能使用一张优惠券
        BigDecimal couponReduceAmount = new BigDecimal(0);
        if(!CollectionUtils.isEmpty(couponInfoList)) {
            couponReduceAmount = couponInfoList.stream()
                    .filter(couponInfo -> couponInfo.getIsOptimal() == 1)
                    .map(CouponInfo::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        // 5.计算没有参与活动，没有使用优惠券的原始金额
        BigDecimal originalTotalAmount = cartInfoList.stream()
                .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .map(cartInfo -> cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 6.计算参与活动后，使用优惠券后的最终金额
        BigDecimal totalAmount = originalTotalAmount.subtract(activityReduceAmount).subtract(couponReduceAmount);

        // 7.封装数据到OrderConfirmVo，返回
        OrderConfirmVo orderTradeVo = new OrderConfirmVo();
        orderTradeVo.setCarInfoVoList(carInfoVoList);
        orderTradeVo.setActivityReduceAmount(activityReduceAmount);
        orderTradeVo.setCouponInfoList(couponInfoList);
        orderTradeVo.setCouponReduceAmount(couponReduceAmount);
        orderTradeVo.setOriginalTotalAmount(originalTotalAmount);
        orderTradeVo.setTotalAmount(totalAmount);
        return orderTradeVo;
    }

    /**
     * 获取购物车对应的促销活动
     *
     * @param cartInfoList 购物车列表
     * @return 购物车对应的促销活动
     */
    @Override
    public List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoList) {
        List<CartInfoVo> carInfoVoList = new ArrayList<>();

        // 第一步：把购物车里面相同活动的购物项汇总一起
        // 获取skuId列表
        List<Long> skuIdList = cartInfoList.stream().map(CartInfo::getSkuId).collect(Collectors.toList());
        // 获取skuId列表对应的全部促销规则
        List<ActivitySku> activitySkuList = baseMapper.selectCartActivityList(skuIdList);
        // 根据活动分组，取活动对应的skuId列表，即把购物车里面相同活动的购物项汇总一起，凑单使用。
        // key:活动Id；value:每组活动中的skuId集合数据
        Map<Long, Set<Long>> activityIdToSkuIdListMap = activitySkuList.stream().collect(Collectors.groupingBy(ActivitySku::getActivityId, Collectors.mapping(ActivitySku::getSkuId, Collectors.toSet())));

        //第二步：获取活动对应的促销规则
        //获取购物车对应的活动id
        Set<Long> activityIdSet = activitySkuList.stream().map(ActivitySku::getActivityId).collect(Collectors.toSet());
        // key:活动id；value：活动里面规则列表的数据
        Map<Long, List<ActivityRule>> activityIdToActivityRuleListMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(activityIdSet)) {
            LambdaQueryWrapper<ActivityRule> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByDesc(ActivityRule::getConditionAmount, ActivityRule::getConditionNum);
            queryWrapper.in(ActivityRule::getActivityId, activityIdSet);
            List<ActivityRule> activityRuleList = activityRuleMapper.selectList(queryWrapper);
            // 按活动Id分组，获取活动对应的规则
            activityIdToActivityRuleListMap = activityRuleList.stream().collect(Collectors.groupingBy(ActivityRule::getActivityId));
        }

        //第三步：根据活动汇总购物项，相同活动的购物项为一组显示在页面，并且计算最优优惠金额
        //记录有活动的购物项skuId
        Set<Long> activitySkuIdSet = new HashSet<>();
        if(!CollectionUtils.isEmpty(activityIdToSkuIdListMap)) {
            for (Map.Entry<Long, Set<Long>> entry : activityIdToSkuIdListMap.entrySet()) {
                Long activityId = entry.getKey();
                //当前活动对应的购物项skuId列表
                Set<Long> currentActivitySkuIdSet = entry.getValue();
                //当前活动对应的购物项列表
                List<CartInfo> currentActivityCartInfoList = cartInfoList.stream().filter(cartInfo -> currentActivitySkuIdSet.contains(cartInfo.getSkuId())).collect(Collectors.toList());

                //当前活动的总金额
                BigDecimal activityTotalAmount = this.computeTotalAmount(currentActivityCartInfoList);
                //当前活动的购物项总个数
                Integer activityTotalNum = this.computeCartNum(currentActivityCartInfoList);
                //计算当前活动对应的最优规则
                //活动当前活动对应的规则
                List<ActivityRule> currentActivityRuleList = activityIdToActivityRuleListMap.get(activityId);
                ActivityType activityType = currentActivityRuleList.get(0).getActivityType();
                ActivityRule optimalActivityRule = null;
                // 判断活动类型：满减和满量打折
                if (activityType == ActivityType.FULL_REDUCTION) {
                    optimalActivityRule = this.computeFullReduction(activityTotalAmount, currentActivityRuleList);
                } else {
                    optimalActivityRule = this.computeFullDiscount(activityTotalNum, activityTotalAmount, currentActivityRuleList);
                }

                //同一活动对应的购物项列表与对应优化规则
                CartInfoVo carInfoVo = new CartInfoVo();
                carInfoVo.setCartInfoList(currentActivityCartInfoList);
                carInfoVo.setActivityRule(optimalActivityRule);
                carInfoVoList.add(carInfoVo);
                //记录
                activitySkuIdSet.addAll(currentActivitySkuIdSet);
            }
        }

        //第四步：无活动的购物项，每一项一组
        // 获取没有参加互动的skuId。把参加活动的skuId移出，剩下的就是。
        skuIdList.removeAll(activitySkuIdSet);
        if(!CollectionUtils.isEmpty(skuIdList)) {
            //获取skuId对应的购物项
            Map<Long, CartInfo> skuIdToCartInfoMap = cartInfoList.stream().collect(Collectors.toMap(CartInfo::getSkuId, CartInfo->CartInfo));
            for(Long skuId : skuIdList) {
                CartInfoVo carInfoVo = new CartInfoVo();
                carInfoVo.setActivityRule(null);
                List<CartInfo> currentCartInfoList = new ArrayList<>();
                currentCartInfoList.add(skuIdToCartInfoMap.get(skuId));
                carInfoVo.setCartInfoList(currentCartInfoList);
                carInfoVoList.add(carInfoVo);
            }
        }
        return carInfoVoList;
    }


    /**
     * 计算满量打折最优规则
     *
     * @param totalNum 总数量
     * @param activityRuleList 该活动规则skuActivityRuleList数据，已经按照优惠折扣从大到小排序了
     * @return 满量打折最优规则
     */
    private ActivityRule computeFullDiscount(Integer totalNum, BigDecimal totalAmount, List<ActivityRule> activityRuleList) {
        ActivityRule optimalActivityRule = null;
        //该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
        for (ActivityRule activityRule : activityRuleList) {
            //如果订单项购买个数大于等于满减件数，则优化打折
            if (totalNum >= activityRule.getConditionNum()) {
                BigDecimal skuDiscountTotalAmount = totalAmount.multiply(activityRule.getBenefitDiscount().divide(new BigDecimal("10")));
                BigDecimal reduceAmount = totalAmount.subtract(skuDiscountTotalAmount);
                activityRule.setReduceAmount(reduceAmount);
                optimalActivityRule = activityRule;
                break;
            }
        }
        if(null == optimalActivityRule) {
            //如果没有满足条件的取最小满足条件的一项
            optimalActivityRule = activityRuleList.get(activityRuleList.size()-1);
            optimalActivityRule.setReduceAmount(new BigDecimal("0"));
            optimalActivityRule.setSelectType(1);

            String ruleDesc = "满" +
                    optimalActivityRule.getConditionNum() +
                    "元打" +
                    optimalActivityRule.getBenefitDiscount() +
                    "折，还差" +
                    (totalNum - optimalActivityRule.getConditionNum()) +
                    "件";
            optimalActivityRule.setRuleDesc(ruleDesc);
        } else {
            String ruleDesc = "满" +
                    optimalActivityRule.getConditionNum() +
                    "元打" +
                    optimalActivityRule.getBenefitDiscount() +
                    "折，已减" +
                    optimalActivityRule.getReduceAmount() +
                    "元";
            optimalActivityRule.setRuleDesc(ruleDesc);
            optimalActivityRule.setSelectType(2);
        }
        return optimalActivityRule;
    }

    /**
     * 计算满减最优规则
     *
     * @param totalAmount 总金额
     * @param activityRuleList 该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
     * @return 满减最优规则
     */
    private ActivityRule computeFullReduction(BigDecimal totalAmount, List<ActivityRule> activityRuleList) {
        ActivityRule optimalActivityRule = null;
        //该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
        for (ActivityRule activityRule : activityRuleList) {
            //如果订单项金额大于等于满减金额，则优惠金额
            if (totalAmount.compareTo(activityRule.getConditionAmount()) > -1) {
                //优惠后减少金额
                activityRule.setReduceAmount(activityRule.getBenefitAmount());
                optimalActivityRule = activityRule;
                break;
            }
        }
        if(null == optimalActivityRule) {
            //如果没有满足条件的取最小满足条件的一项
            optimalActivityRule = activityRuleList.get(activityRuleList.size()-1);
            optimalActivityRule.setReduceAmount(new BigDecimal("0"));
            optimalActivityRule.setSelectType(1);

            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionAmount())
                    .append("元减")
                    .append(optimalActivityRule.getBenefitAmount())
                    .append("元，还差")
                    .append(totalAmount.subtract(optimalActivityRule.getConditionAmount()))
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
        } else {
            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionAmount())
                    .append("元减")
                    .append(optimalActivityRule.getBenefitAmount())
                    .append("元，已减")
                    .append(optimalActivityRule.getReduceAmount())
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
            optimalActivityRule.setSelectType(2);
        }
        return optimalActivityRule;
    }

    /**
     * 计算购物项的总金额
     *
     * @param cartInfoList 购物车列表
     * @return 购物项的总金额
     */
    private BigDecimal computeTotalAmount(List<CartInfo> cartInfoList) {
        BigDecimal total = new BigDecimal("0");
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if(cartInfo.getIsChecked().intValue() == 1) {
                BigDecimal itemTotal = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                total = total.add(itemTotal);
            }
        }
        return total;
    }

    /**
     * 计算购物项的总数量
     *
     * @param cartInfoList 购物车列表
     * @return 购物项的总数量
     */
    private int computeCartNum(List<CartInfo> cartInfoList) {
        int total = 0;
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if(cartInfo.getIsChecked() == 1) {
                total += cartInfo.getSkuNum();
            }
        }
        return total;
    }
}
