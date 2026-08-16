package com.example.groupbuying.client.order;

import com.example.groupbuying.model.order.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-order")
public interface OrderFeignClient {
    /**
     * 获取订单详情
     *
     * @param orderNo 订单orderNo
     * @return 订单详情
     */
    @GetMapping("/api/order/inner/getOrderInfoByOrderNo/{orderNo}")
    OrderInfo getOrderInfoByOrderNo(@PathVariable("orderNo") String orderNo);

}