package com.example.groupbuying.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.groupbuying.common.auth.AuthContextHolder;
import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.model.order.OrderInfo;
import com.example.groupbuying.order.service.OrderInfoService;
import com.example.groupbuying.vo.order.OrderConfirmVo;
import com.example.groupbuying.vo.order.OrderSubmitVo;
import com.example.groupbuying.vo.order.OrderUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(value = "Order管理", tags = "Order管理")
@RestController
@RequestMapping(value="/api/order")
public class OrderApiController {
	
	@Resource
	private OrderInfoService orderService;

	@ApiOperation("确认订单")
	@GetMapping("auth/confirmOrder")
	public Result<OrderConfirmVo> confirm() {
		OrderConfirmVo orderConfirmVo = orderService.confirmOrder();
		return Result.ok(orderConfirmVo);
	}

	@ApiOperation("生成订单")
	@PostMapping("auth/submitOrder")
	public Result<Long> submitOrder(@RequestBody OrderSubmitVo orderParamVo, HttpServletRequest request) {
		// 获取到用户Id
		Long userId = AuthContextHolder.getUserId();
		Long orderId = orderService.submitOrder(orderParamVo);
		return Result.ok(orderId);
	}

	@ApiOperation("获取订单详情")
	@GetMapping("auth/getOrderInfoById/{orderId}")
	public Result<OrderInfo> getOrderInfoById(@PathVariable("orderId") Long orderId){
		OrderInfo orderInfoById = orderService.getOrderInfoById(orderId);
		return Result.ok(orderInfoById);
	}
	/**
	 * 获取订单详情
	 *
	 * @param orderNo 订单orderNo
	 * @return 订单详情
	 */
	@ApiOperation("根据orderNo获取订单详情")
	@GetMapping("inner/getOrderInfoByOrderNo/{orderNo}")
	public OrderInfo getOrderInfoByOrderNo(@PathVariable("orderNo") String orderNo){
		return orderService.getOrderInfoByOrderNo(orderNo);
	}

	@ApiOperation(value = "获取用户订单分页列表")
	@GetMapping("auth/findUserOrderPage/{page}/{limit}")
	public Result<IPage<OrderInfo>> findUserOrderPage(
			@ApiParam(name = "page", value = "当前页码", required = true)
			@PathVariable Long page,

			@ApiParam(name = "limit", value = "每页记录数", required = true)
			@PathVariable Long limit,

			@ApiParam(name = "orderVo", value = "查询对象", required = false)
			OrderUserQueryVo orderUserQueryVo) {
		Long userId = AuthContextHolder.getUserId();
		orderUserQueryVo.setUserId(userId);
		Page<OrderInfo> pageParam = new Page<>(page, limit);
		IPage<OrderInfo> pageModel = orderService.findUserOrderPage(pageParam, orderUserQueryVo);
		return Result.ok(pageModel);
	}
}