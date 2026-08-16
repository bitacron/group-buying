package com.example.groupbuying.payment.api;

import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.enums.PaymentType;
import com.example.groupbuying.payment.service.PaymentService;
import com.example.groupbuying.payment.service.WeiXinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 微信支付 API
 * </p>
 */
@Api(tags = "微信支付接口")
@RestController
@RequestMapping("/api/payment/weixin")
@Slf4j
public class WeiXinController {

    @Resource
    private WeiXinService weiXinService;

    @Resource
    private PaymentService paymentService;

    @ApiOperation(value = "下单-小程序支付")
    @GetMapping("/createJsapi/{orderNo}")
    public Result<Map<String, String>> createJsapi(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {
        Map<String, String> jsapi = weiXinService.createJsapi(orderNo);
        return Result.ok(jsapi);
    }

    @ApiOperation(value = "查询支付状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result<String> queryPayStatus(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {
        // 1.调用微信支付系统，查询订单支付状态
        Map<String, String> resultMap = weiXinService.queryPayStatus(orderNo, PaymentType.WEIXIN.name());
        // 2.支付系统返回null，支付失败
        if (resultMap == null) {
            return Result.fail("支付出错");
        }
        // 3.支付系统返回值，支付成功
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {
            // 支付成功，更改支付状态、更改订单状态状态并扣减库存，处理支付结果
            String out_trade_no = resultMap.get("out_trade_no");
            paymentService.paySuccess(out_trade_no, PaymentType.WEIXIN, resultMap);
            return Result.ok("支付成功");
        }
        // 4.支付中，等待
        return Result.ok("支付中");
    }
}