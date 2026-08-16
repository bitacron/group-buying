package com.example.groupbuying.payment.service;

import com.example.groupbuying.enums.PaymentType;
import com.example.groupbuying.model.order.PaymentInfo;
import java.util.Map;

public interface PaymentService {

    /**
     * 保存支付记录
     *
     * @param orderNo 订单号
     * @param paymentType 支付类型（1：微信 2：支付宝）
     * @return 支付记录信息
     */
    PaymentInfo savePaymentInfo(String orderNo, PaymentType paymentType);
    /**
     * 根据orderNo查询支付记录
     *
     * @param orderNo 订单号
     * @param paymentType 支付类型（1：微信 2：支付宝）
     * @return 支付记录信息
     */
    PaymentInfo getPaymentInfoByOrderNo(String orderNo, PaymentType paymentType);

    /**
     * 支付成功，更改支付状态、更改订单状态状态并扣减库存
     *
     * @param orderNo 订单号
     * @param paymentType 支付类型（1：微信 2：支付宝）
     * @param paramMap 微信支付系统返回的订单支付状态
     */
    void paySuccess(String orderNo,PaymentType paymentType, Map<String,String> paramMap);
}