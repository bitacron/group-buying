package com.example.groupbuying.client.activity;

import com.example.groupbuying.model.activity.CouponInfo;
import com.example.groupbuying.model.order.CartInfo;
import com.example.groupbuying.vo.order.CartInfoVo;
import com.example.groupbuying.vo.order.OrderConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@FeignClient("service-activity")
public interface ActivityFeignClient {

    //根据skuId列表获取促销信息
    @PostMapping("/api/activity/inner/findActivity")
    Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList);

    /**
     * 根据skuId获取促销与优惠券信息
     *
     * @param skuId 商品skuId
     * @param userId 用户Id
     * @return 促销与优惠券信息
     */
    @GetMapping("/api/activity/inner/findActivityAndCoupon/{skuId}/{userId}")
    Map<String, Object> findActivityAndCoupon(@PathVariable Long skuId, @PathVariable("userId") Long userId);

    /**
     * 获取购物车满足条件的促销与优惠券信息
     *
     * @param cartInfoList 购物车列表
     * @param userId 用户Id
     * @return 购物车满足条件的促销与优惠券信息
     */
    @PostMapping("/api/activity/inner/findCartActivityAndCoupon/{userId}")
    OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList, @PathVariable("userId") Long userId);

    /**
     * 获取优惠券范围对应的购物车列表
     *
     * @param cartInfoList 购物车列表
     * @param couponId 优惠券Id
     * @return 优惠券范围对应的购物车列表
     */
    @PostMapping(value = "/api/activity/inner/findRangeSkuIdList/{couponId}")
    CouponInfo findRangeSkuIdList(@RequestBody List<CartInfo> cartInfoList, @PathVariable("couponId") Long couponId);
    /**
     * 更新优惠券支付时间
     *
     * @param couponId 优惠券id
     * @param userId 用户id
     * @return 是否更新成功
     */
    @GetMapping(value = "/api/activity/inner/updateCouponInfoUsedTime/{couponId}/{userId}")
    Boolean updateCouponInfoUsedTime(@PathVariable("couponId") Long couponId, @PathVariable("userId") Long userId);

    /**
     * 更新优惠券使用状态
     *
     * @param couponId 优惠券id
     * @param userId 用户id
     * @param orderId 订单id
     * @return 是否更新成功
     */
    @GetMapping(value = "/api/activity/inner/updateCouponInfoUseStatus/{couponId}/{userId}/{orderId}")
    Boolean updateCouponInfoUseStatus(@PathVariable("couponId") Long couponId, @PathVariable("userId") Long userId, @PathVariable("orderId") Long orderId);

    /**
     * 获取购物车对应的促销活动
     *
     * @param cartInfoList 购物车列表
     * @return 购物车对应的促销活动
     */
    @PostMapping(value = "/api/activity/inner/findCartActivityList")
    List<CartInfoVo> findCartActivityList(@RequestBody List<CartInfo> cartInfoList);
}