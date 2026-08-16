package com.example.groupbuying.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.groupbuying.activity.mapper.CouponRangeMapper;
import com.example.groupbuying.activity.mapper.CouponUseMapper;
import com.example.groupbuying.client.product.ProductFeignClient;
import com.example.groupbuying.enums.CouponRangeType;
import com.example.groupbuying.enums.CouponStatus;
import com.example.groupbuying.model.activity.CouponInfo;
import com.example.groupbuying.activity.mapper.CouponInfoMapper;
import com.example.groupbuying.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.groupbuying.model.activity.CouponRange;
import com.example.groupbuying.model.activity.CouponUse;
import com.example.groupbuying.model.base.BaseEntity;
import com.example.groupbuying.model.order.CartInfo;
import com.example.groupbuying.model.product.Category;
import com.example.groupbuying.model.product.SkuInfo;
import com.example.groupbuying.vo.activity.CouponRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author example
 * @since 2024-04-24
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

    @Autowired
    private CouponInfoMapper couponInfoMapper;

    @Autowired
    private CouponRangeMapper couponRangeMapper;

    @Autowired
    private CouponUseMapper couponUseMapper;

    @Autowired
    private ProductFeignClient productFeignClient;
    //优惠卷分页查询
    @Override
    public IPage selectPageCouponInfo(Page<CouponInfo> pageParam) {
        //  构造排序条件
        QueryWrapper<CouponInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        IPage<CouponInfo> page = baseMapper.selectPage(pageParam, queryWrapper);
        page.getRecords().stream().forEach(item -> {
            item.setCouponTypeString(item.getCouponType().getComment());
            if(null != item.getRangeType()) {
                item.setRangeTypeString(item.getRangeType().getComment());
            }
        });
        // 返回数据集合
        return page;
    }

    //根据id获取优惠券
    @Override
    public CouponInfo getCouponInfo(String id) {
        CouponInfo couponInfo = this.getById(id);
        couponInfo.setCouponTypeString(couponInfo.getCouponType().getComment());
        if(null != couponInfo.getRangeType()) {
            couponInfo.setRangeTypeString(couponInfo.getRangeType().getComment());
        }
        return couponInfo;
    }

    //根据优惠卷id获取优惠券规则列表
    @Override
    public Map<String, Object> findCouponRuleList(Long couponId) {
        Map<String, Object> result = new HashMap<>();
        // 第一步 根据优惠券id查询优惠券基本信息 coupon_info表
        CouponInfo couponInfo = baseMapper.selectById(couponId);
        // 第二步 根据优惠券id查询coupon_range 查询里面对于range_id
        QueryWrapper couponRangeQueryWrapper = new QueryWrapper<CouponRange>();
        couponRangeQueryWrapper.eq("coupon_id",couponId);
        List<CouponRange> activitySkuList = couponRangeMapper.selectList(couponRangeQueryWrapper);

        List<Long> rangeIdList = activitySkuList.stream().map(CouponRange::getRangeId).collect(Collectors.toList());
        // 分别判断封装不同数据
        if(!CollectionUtils.isEmpty(rangeIdList)) {
            if(couponInfo.getRangeType() == CouponRangeType.SKU) {
                List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(rangeIdList);
                result.put("skuInfoList", skuInfoList);

            } else if (couponInfo.getRangeType() == CouponRangeType.CATEGORY) {
                List<Category> categoryList = productFeignClient.findCategoryList(rangeIdList);
                result.put("categoryList", categoryList);

            } else {
                //通用
            }
        }
        return result;
    }

    //新增优惠券规则
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCouponRule(CouponRuleVo couponRuleVo) {
        /*
        优惠券couponInfo 与 couponRange 要一起操作：先删除couponRange ，更新couponInfo ，再新增couponRange ！
         */
        QueryWrapper<CouponRange> couponRangeQueryWrapper = new QueryWrapper<>();
        couponRangeQueryWrapper.eq("coupon_id",couponRuleVo.getCouponId());
        couponRangeMapper.delete(couponRangeQueryWrapper);

        //  更新数据
        CouponInfo couponInfo = this.getById(couponRuleVo.getCouponId());
        // couponInfo.setCouponType();
        couponInfo.setRangeType(couponRuleVo.getRangeType());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setAmount(couponRuleVo.getAmount());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setRangeDesc(couponRuleVo.getRangeDesc());

        couponInfoMapper.updateById(couponInfo);

        //  插入优惠券的规则 couponRangeList
        List<CouponRange> couponRangeList = couponRuleVo.getCouponRangeList();
        for (CouponRange couponRange : couponRangeList) {
            couponRange.setCouponId(couponRuleVo.getCouponId());
            //  插入数据
            couponRangeMapper.insert(couponRange);
        }
    }

    //根据关键字获取sku列表，活动使用
    @Override
    public List<CouponInfo> findCouponByKeyword(String keyword) {
        //  模糊查询
        QueryWrapper<CouponInfo> couponInfoQueryWrapper = new QueryWrapper<>();
        couponInfoQueryWrapper.like("coupon_name",keyword);
        return couponInfoMapper.selectList(couponInfoQueryWrapper);
    }

    @Override
    public List<CouponInfo> findCouponInfo(Long skuId, Long userId) {
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if(null == skuInfo) return new ArrayList<>();
        return couponInfoMapper.selectCouponInfoList(skuInfo.getId(), skuInfo.getCategoryId(), userId);
    }

    /**
     * 获取购物车中可以使用的优惠券的列表
     *
     * @param cartInfoList 购物车列表
     * @param userId       用户id
     * @return 购物车中可可以使用的优惠券的列表
     */
    @Override
    public List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId) {
        // 1.获取用户可以使用的全部优惠券：coupon_use、coupon_info
        List<CouponInfo> userAllCouponInfoList = baseMapper.selectCartCouponInfoList(userId);
        if(CollectionUtils.isEmpty(userAllCouponInfoList)) {
            return Collections.emptyList();
        }

        // 2.从第一步返回的List中，获取所有优惠券id列表
        List<Long> couponIdList = userAllCouponInfoList.stream().map(BaseEntity::getId).collect(Collectors.toList());

        // 3.查询优惠券对应的范围 coupon_range
        List<CouponRange> couponRangesList = couponRangeMapper.selectList(new LambdaQueryWrapper<CouponRange>().in(CouponRange::getCouponId, couponIdList));

        // 4.获取优惠券id对应的skuId列表 优惠券id进行分组，得到map集合。
        Map<Long, List<Long>> couponIdToSkuIdMap = this.findCouponIdToSkuIdMap(cartInfoList, couponRangesList);

        // 5.遍历全部优惠券集合，判断优惠券类型。全场通用、sku和分类
        //优惠后减少金额
        BigDecimal reduceAmount = new BigDecimal("0");
        //记录最优优惠券
        CouponInfo optimalCouponInfo = null;
        for(CouponInfo couponInfo : userAllCouponInfoList) {
            if(CouponRangeType.ALL == couponInfo.getRangeType()) {
                //全场通用
                //判断是否满足优惠使用门槛
                //计算购物车商品的总价
                BigDecimal totalAmount = computeTotalAmount(cartInfoList);
                if(totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0){
                    couponInfo.setIsSelect(1);
                }
            } else {
                //优惠券id对应的满足使用范围的购物项skuId列表
                List<Long> skuIdList = couponIdToSkuIdMap.get(couponInfo.getId());
                //当前满足使用范围的购物项
                List<CartInfo> currentCartInfoList = cartInfoList.stream().filter(cartInfo -> skuIdList.contains(cartInfo.getSkuId())).collect(Collectors.toList());
                BigDecimal totalAmount = computeTotalAmount(currentCartInfoList);
                if(totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0){
                    couponInfo.setIsSelect(1);
                }
            }
            if (couponInfo.getIsSelect() == 1 && couponInfo.getAmount().subtract(reduceAmount).doubleValue() > 0) {
                reduceAmount = couponInfo.getAmount();
                optimalCouponInfo = couponInfo;
            }
        }
        if(null != optimalCouponInfo) {
            optimalCouponInfo.setIsOptimal(1);
        }

        // 6.返回集合
        return userAllCouponInfoList;
    }

    private BigDecimal computeTotalAmount(List<CartInfo> cartInfoList) {
        BigDecimal total = new BigDecimal("0");
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if(cartInfo.getIsChecked() == 1) {
                BigDecimal itemTotal = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                total = total.add(itemTotal);
            }
        }
        return total;
    }

    /**
     * 获取优惠券范围对应的购物车列表
     *
     * @param cartInfoList 购物车列表
     * @param couponId 优惠券Id
     * @return 优惠券范围对应的购物车列表
     */
    @Override
    public CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId) {
        CouponInfo couponInfo = this.getById(couponId);
        if(null == couponInfo || couponInfo.getCouponStatus() == 2) return null;

        //查询优惠券对应的范围
        List<CouponRange> couponRangesList = couponRangeMapper.selectList(new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId, couponId));
        //获取优惠券id对应的满足使用范围的购物项skuId列表
        Map<Long, List<Long>> couponIdToSkuIdMap = this.findCouponIdToSkuIdMap(cartInfoList, couponRangesList);
        List<Long> skuIdList = couponIdToSkuIdMap.entrySet().iterator().next().getValue();
        couponInfo.setSkuIdList(skuIdList);
        return couponInfo;
    }

    /**
     * 获取优惠券id对应的满足使用范围的购物项skuId列表
     * 说明：一个优惠券可能有多个购物项满足它的使用范围，那么多个购物项可以拼单使用这个优惠券
     *
     * @param cartInfoList 购物车列表
     * @param couponRangesList 优惠券范围列表
     * @return 优惠券id对应的满足使用范围的购物项skuId列表
     */
    private Map<Long, List<Long>> findCouponIdToSkuIdMap(List<CartInfo> cartInfoList, List<CouponRange> couponRangesList) {
        Map<Long, List<Long>> couponIdToSkuIdMap = new HashMap<>();
        // couponRangesList数据处理。根据优惠券id进行分组，获取优惠券id对应的范围列表
        Map<Long, List<CouponRange>> couponIdToCouponRangeListMap = couponRangesList.stream().collect(Collectors.groupingBy(CouponRange::getCouponId));
        for (Map.Entry<Long, List<CouponRange>> entry : couponIdToCouponRangeListMap.entrySet()) {
            Long couponId = entry.getKey();
            List<CouponRange> couponRangeList = entry.getValue();

            // 创建集合set
            Set<Long> skuIdSet = new HashSet<>();
            for (CartInfo cartInfo : cartInfoList) {
                for (CouponRange couponRange : couponRangeList) {
                    if (CouponRangeType.SKU == couponRange.getRangeType() && couponRange.getRangeId() == cartInfo.getSkuId().intValue()) {
                        // SKU
                        skuIdSet.add(cartInfo.getSkuId());
                    } else if (CouponRangeType.CATEGORY == couponRange.getRangeType() && couponRange.getRangeId() == cartInfo.getCategoryId().intValue()) {
                        // 分类
                        skuIdSet.add(cartInfo.getSkuId());
                    }  // 全场通用不需要处理。

                }
            }
            couponIdToSkuIdMap.put(couponId, new ArrayList<>(skuIdSet));
        }
        return couponIdToSkuIdMap;
    }

    /**
     * 更新优惠券使用状态
     *
     * @param couponId 优惠券id
     * @param userId   用户id
     * @param orderId  订单id
     * @return 是否更新成功
     */
    @Override
    public boolean updateCouponInfoUseStatus(Long couponId, Long userId, Long orderId) {
        CouponUse couponUse = new CouponUse();
        couponUse.setOrderId(orderId);
        couponUse.setCouponStatus(CouponStatus.USED);
        couponUse.setUsingTime(new Date());

        QueryWrapper<CouponUse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("coupon_id", couponId);
        queryWrapper.eq("user_id", userId);
        int update = couponUseMapper.update(couponUse, queryWrapper);
        return update > 0;
    }

    /**
     * 更新优惠券支付时间
     *
     * @param couponId 优惠券id
     * @param userId   用户id
     * @return 是否更新成功
     */
    @Override
    public boolean updateCouponInfoUsedTime(Long couponId, Long userId) {
        CouponUse couponUse = new CouponUse();
        Date date = new Date();
        couponUse.setUsedTime(date);
        couponUse.setUsingTime(date);

        QueryWrapper<CouponUse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("coupon_id", couponId);
        queryWrapper.eq("user_id", userId);
        int update = couponUseMapper.update(couponUse, queryWrapper);
        return update > 0;
    }
}
