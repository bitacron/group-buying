package com.example.groupbuying.cart.controller;

import com.example.groupbuying.cart.service.CartInfoService;
import com.example.groupbuying.client.activity.ActivityFeignClient;
import com.example.groupbuying.common.auth.AuthContextHolder;
import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.model.order.CartInfo;
import com.example.groupbuying.vo.order.OrderConfirmVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author example
 * @since 2024-04-24
 */
@Api(value = "Cart购物车信息管理", tags = "购物车信息管理")
@RestController
@RequestMapping("/api/cart")
//@CrossOrigin
public class CartApiController {

    @Resource
    private CartInfoService cartInfoService;

    @Resource
    private ActivityFeignClient activityFeignClient;

    /**
     * 添加购物车
     *
     * @param skuId 商品skuId
     * @param skuNum 商品数量
     * @return 添加结果
     */
    @GetMapping("addToCart/{skuId}/{skuNum}")
    public Result<String> addToCart(@PathVariable("skuId") Long skuId,
                            @PathVariable("skuNum") Integer skuNum) {
        // 如何获取userId
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.addToCart(skuId, userId, skuNum);
        return Result.ok("success");
    }

    /**
     * 删除购物车
     *
     * @param skuId 商品skuId
     * @param request servlet请求
     * @return 删除结果
     */
    @DeleteMapping("deleteCart/{skuId}")
    public Result<String> deleteCart(@PathVariable("skuId") Long skuId,
                             HttpServletRequest request) {
        // 如何获取userId
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteCart(skuId, userId);
        return Result.ok("success");
    }

    /**
     * 清空购物车
     *
     * @param request servlet请求
     * @return 清空结果
     */
    @ApiOperation(value="清空购物车")
    @DeleteMapping("deleteAllCart")
    public Result<String> deleteAllCart(HttpServletRequest request){
        // 如何获取userId
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteAllCart(userId);
        return Result.ok("success");
    }
    /**
     * 批量删除购物车
     *
     * @param skuIdList 商品skuId集合
     * @param request servlet请求
     * @return 批量删除结果
     */
    @ApiOperation(value="批量删除购物车")
    @PostMapping("batchDeleteCart")
    public Result<String> batchDeleteCart(@RequestBody List<Long> skuIdList, HttpServletRequest request){
        // 如何获取userId
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteCartCheck(skuIdList, userId);
        return Result.ok("success");
    }
    /**
     * 查询购物车列表
     *
     * @param request 请求参数
     * @return 购物车列表
     */
    @GetMapping("cartList")
    public Result<List<CartInfo>> cartList(HttpServletRequest request) {
        // 获取用户Id
        Long userId = AuthContextHolder.getUserId();
        List<CartInfo> cartInfoList = cartInfoService.getCartList(userId);
        return Result.ok(cartInfoList);
    }

    /**
     * 查询带优惠卷的购物车
     *
     * @param request 请求参数
     * @return 购物车列表
     */
    @GetMapping("activityCartList")
    public Result<OrderConfirmVo> activityCartList(HttpServletRequest request) {
        // 获取用户Id
        Long userId = AuthContextHolder.getUserId();
        List<CartInfo> cartInfoList = cartInfoService.getCartList(userId);
        OrderConfirmVo orderTradeVo = activityFeignClient.findCartActivityAndCoupon(cartInfoList, userId);
        return Result.ok(orderTradeVo);
    }
    /**
     * 更新选中状态
     *
     * @param skuId 商品skuId
     * @param isChecked 是否选中
     * @return 是否成功
     */
    @GetMapping("checkCart/{skuId}/{isChecked}")
    public Result<String> checkCart(@PathVariable(value = "skuId") Long skuId,
                            @PathVariable(value = "isChecked") Integer isChecked) {
        // 获取用户Id
        Long userId = AuthContextHolder.getUserId();
        // 调用更新方法
        cartInfoService.checkCart(userId, isChecked, skuId);
        return Result.ok("success");
    }

    @GetMapping("checkAllCart/{isChecked}")
    public Result<String> checkAllCart(@PathVariable(value = "isChecked") Integer isChecked) {
        // 获取用户Id
        Long userId = AuthContextHolder.getUserId();
        // 调用更新方法
        cartInfoService.checkAllCart(userId, isChecked);
        return Result.ok("success");
    }

    @ApiOperation(value="批量选择购物车")
    @PostMapping("batchCheckCart/{isChecked}")
    public Result<String> batchCheckCart(@RequestBody List<Long> skuIdList, @PathVariable(value = "isChecked") Integer isChecked){
        // 如何获取userId
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.batchCheckCart(skuIdList, userId, isChecked);
        return Result.ok("success");
    }
    /**
     * 根据用户Id 查询购物车列表
     *
     * @param userId 用户Id
     * @return 购物车列表
     */
    @GetMapping("inner/getCartCheckedList/{userId}")
    public List<CartInfo> getCartCheckedList(@PathVariable("userId") Long userId) {
        return cartInfoService.getCartCheckedList(userId);
    }
}

