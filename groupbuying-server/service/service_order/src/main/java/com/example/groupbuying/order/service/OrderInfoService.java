package com.example.groupbuying.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.groupbuying.model.order.OrderInfo;
import com.example.groupbuying.vo.order.OrderConfirmVo;
import com.example.groupbuying.vo.order.OrderSubmitVo;
import com.example.groupbuying.vo.order.OrderUserQueryVo;

public interface OrderInfoService extends IService<OrderInfo> {
    /**
     * 确认订单
     *
     * @return 订单确认信息
     */
    OrderConfirmVo confirmOrder();

    /**
     * 生成订单
     *
     * @param orderParamVo 订单参数
     * @return 订单id
     */
    Long submitOrder(OrderSubmitVo orderParamVo);

    /**
     * 获取订单详情
     *
     * @param orderId 订单id
     * @return 订单详情
     */
    OrderInfo getOrderInfoById(Long orderId);
    /**
     * 获取订单详情
     *
     * @param orderNo 订单orderNo
     * @return 订单详情
     */
    OrderInfo getOrderInfoByOrderNo(String orderNo);

    /**
     * 更改订单状态、通知扣减库存
     *
     * @param orderNo 订单orderNo
     */
    void orderPay(String orderNo);
    /**
     * 订单分页查询-搜索条件
     *
     * @param pageParam 分页查询信息
     * @param orderUserQueryVo 查询条件-订单类型（待付款、待发货、待提货等）等
     * @return 订单信息
     */
    IPage<OrderInfo> findUserOrderPage(Page<OrderInfo> pageParam, OrderUserQueryVo orderUserQueryVo);
}