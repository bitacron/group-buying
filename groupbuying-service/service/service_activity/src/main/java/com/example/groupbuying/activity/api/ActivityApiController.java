package com.example.groupbuying.activity.api;

import com.example.groupbuying.activity.service.ActivityInfoService;
import com.example.groupbuying.activity.service.CouponInfoService;
import com.example.groupbuying.model.activity.CouponInfo;
import com.example.groupbuying.model.order.CartInfo;
import com.example.groupbuying.vo.order.CartInfoVo;
import com.example.groupbuying.vo.order.OrderConfirmVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "促销与优惠券接口")
@RestController
@RequestMapping("/api/activity")
@Slf4j
public class ActivityApiController {

    @Resource
    private ActivityInfoService activityInfoService;
    @Resource
    private CouponInfoService couponInfoService;

    /**
     * 根据skuId列表获取促销信息
     *
     * @param skuIdList skuId列表
     * @return 促销信息
     */
    @ApiOperation(value = "根据skuId列表获取促销信息")
    @PostMapping("inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList) {
        return activityInfoService.findActivity(skuIdList);
    }

    /**
     * 根据skuId获取促销与优惠券信息
     *
     * @param skuId skuId
     * @param userId 用户Id
     * @return 促销信息
     */
    @ApiOperation(value = "根据skuId获取促销与优惠券信息")
    @GetMapping("inner/findActivityAndCoupon/{skuId}/{userId}")
    public Map<String, Object> findActivityAndCoupon(@PathVariable Long skuId, @PathVariable("userId") Long userId) {
        return activityInfoService.findActivityAndCoupon(skuId, userId);
    }

    /**
     * 获取购物车满足条件的促销与优惠券信息
     *
     * @param cartInfoList 购物车列表
     * @param userId 用户Id
     * @return 购物车满足条件的促销与优惠券信息
     */
    @ApiOperation(value = "获取购物车满足条件的促销与优惠券信息")
    @PostMapping("inner/findCartActivityAndCoupon/{userId}")
    public OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList, @PathVariable("userId") Long userId) {
        return activityInfoService.findCartActivityAndCoupon(cartInfoList, userId);
    }

    /**
     * 获取购物车对应的促销活动
     *
     * @param cartInfoList 购物车列表
     * @return 购物车对应的促销活动
     */
    @ApiOperation(value = "获取购物车对应的促销活动")
    @PostMapping(value = "/inner/findCartActivityList")
    List<CartInfoVo> findCartActivityList(@RequestBody List<CartInfo> cartInfoList){
        return activityInfoService.findCartActivityList(cartInfoList);
    }

    /**
     * 获取优惠券范围对应的购物车列表
     *
     * @param cartInfoList 购物车列表
     * @param couponId 优惠券Id
     * @return 优惠券范围对应的购物车列表
     */
    @PostMapping(value = "inner/findRangeSkuIdList/{couponId}")
    CouponInfo findRangeSkuIdList(@RequestBody List<CartInfo> cartInfoList, @PathVariable("couponId") Long couponId) {
        return couponInfoService.findRangeSkuIdList(cartInfoList, couponId);
    }

    @ApiOperation(value = "更新优惠券使用状态")
    @GetMapping(value = "inner/updateCouponInfoUseStatus/{couponId}/{userId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable("couponId") Long couponId, @PathVariable("userId") Long userId, @PathVariable("orderId") Long orderId) {
        return couponInfoService.updateCouponInfoUseStatus(couponId, userId, orderId);
    }

    /**
     * 更新优惠券支付时间
     *
     * @param couponId 优惠券id
     * @param userId 用户id
     * @return 是否更新成功
     */
    @ApiOperation(value = "更新优惠券支付时间")
    @GetMapping(value = "inner/updateCouponInfoUsedTime/{couponId}/{userId}")
    public Boolean updateCouponInfoUsedTime(@PathVariable("couponId") Long couponId, @PathVariable("userId") Long userId) {
        return couponInfoService.updateCouponInfoUsedTime(couponId, userId);
    }
}