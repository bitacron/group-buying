package com.example.groupbuying.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.groupbuying.model.activity.CouponInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.groupbuying.model.order.CartInfo;
import com.example.groupbuying.vo.activity.CouponRuleVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author example
 * @since 2024-04-24
 */
public interface CouponInfoService extends IService<CouponInfo> {

    //获取分页列表
    IPage<CouponInfo> selectPageCouponInfo(Page<CouponInfo> pageParam);

    //根据id获取优惠券
    CouponInfo getCouponInfo(String id);

    //根据优惠卷id获取优惠券规则列表
    Map<String, Object> findCouponRuleList(Long couponId);

    //新增优惠券规则
    void saveCouponRule(CouponRuleVo couponRuleVo);

    //根据关键字获取sku列表，活动使用
    List<CouponInfo> findCouponByKeyword(String keyword);

    List<CouponInfo> findCouponInfo(Long skuId, Long userId);

    /**
     * 获取购物车中可以使用的优惠券的列表
     *
     * @param cartInfoList 购物车列表
     * @param userId 用户id
     * @return 购物车中可可以使用的优惠券的列表
     */
    List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId);
    /**
     * 获取优惠券范围对应的购物车列表
     *
     * @param cartInfoList 购物车列表
     * @param couponId 优惠券Id
     * @return 优惠券范围对应的购物车列表
     */
    CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId);

    /**
     * 更新优惠券使用状态
     *
     * @param couponId 优惠券id
     * @param userId 用户id
     * @param orderId 订单id
     * @return 是否更新成功
     */
    boolean updateCouponInfoUseStatus(Long couponId, Long userId, Long orderId);
    /**
     * 更新优惠券支付时间
     *
     * @param couponId 优惠券id
     * @param userId 用户id
     */
    boolean updateCouponInfoUsedTime(Long couponId, Long userId);
}
