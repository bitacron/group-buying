package com.example.groupbuying.activity.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.groupbuying.model.activity.ActivityInfo;
import com.example.groupbuying.model.activity.ActivityRule;
import com.example.groupbuying.model.order.CartInfo;
import com.example.groupbuying.model.product.SkuInfo;
import com.example.groupbuying.vo.activity.ActivityRuleVo;
import com.example.groupbuying.vo.order.CartInfoVo;
import com.example.groupbuying.vo.order.OrderConfirmVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动表 服务类
 * </p>
 *
 * @author example
 * @since 2024-04-24
 */
public interface ActivityInfoService extends IService<ActivityInfo> {
    /**
     * 分页查询
     * @param pageParam 查询参数
     * @return 活动信息
     */
    IPage<ActivityInfo> selectPageActivityInfo(Page<ActivityInfo> pageParam);


    //1.根据活动id获取活动规则数据
    Map<String, Object> findActivityRuleList(Long activityId);

    //2.在活动里面添加规则数据
    void saveActivityRule(ActivityRuleVo activityRuleVo);

    //3.根据关键字查询匹配sku信息
    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    /**
     * 根据skuId列表获取促销信息
     *
     * @param skuIdList skuId列表
     * @return 取促销信息
     */
    Map<Long, List<String>> findActivity(List<Long> skuIdList);

    /**
     * 根据skuId获取促销与优惠券信息
     *
     * @param skuId skuId
     * @param userId 用户Id
     * @return 取促销信息
     */
    Map<String, Object> findActivityAndCoupon(Long skuId, Long userId);

    /**
     * 根据skuId获取促销规则信息
     *
     * @param skuId skuId
     * @return 促销规则信息
     */
    List<ActivityRule> findActivityRuleBySkuId(Long skuId);

    /**
     * 获取购物车满足条件的促销与优惠券信息
     *
     * @param cartInfoList 购物车列表
     * @param userId 用户Id
     * @return 购物车满足条件的促销与优惠券信息
     */
    OrderConfirmVo findCartActivityAndCoupon(List<CartInfo> cartInfoList, Long userId);
    /**
     * 获取购物车对应的促销活动
     *
     * @param cartInfoList 购物车列表
     * @return 购物车对应的促销活动
     */
    List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoList);
}
