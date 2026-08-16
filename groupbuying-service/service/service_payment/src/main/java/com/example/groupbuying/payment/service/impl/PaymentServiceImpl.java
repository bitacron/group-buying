package com.example.groupbuying.payment.service.impl;

import com.example.groupbuying.client.order.OrderFeignClient;
import com.example.groupbuying.common.exception.CustomException;
import com.example.groupbuying.common.result.ResultCodeEnum;
import com.example.groupbuying.enums.PaymentStatus;
import com.example.groupbuying.enums.PaymentType;
import com.example.groupbuying.model.order.OrderInfo;
import com.example.groupbuying.model.order.PaymentInfo;
import com.example.groupbuying.mq.constant.MqConst;
import com.example.groupbuying.mq.service.RabbitService;
import com.example.groupbuying.payment.mapper.PaymentInfoMapper;
import com.example.groupbuying.payment.service.PaymentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentInfoMapper paymentInfoMapper;

    @Resource
    private OrderFeignClient orderFeignClient;

    @Resource
    private RabbitService rabbitService;

    /**
     * 保存支付记录
     *
     * @param orderNo 订单号
     * @param paymentType 支付类型（1：微信 2：支付宝）
     * @return 支付记录信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentInfo savePaymentInfo(String orderNo, PaymentType paymentType) {
        // 远程调用，根据orderNo获取order信息
        OrderInfo order = orderFeignClient.getOrderInfoByOrderNo(orderNo);
        if(null == order) {
            throw new CustomException(ResultCodeEnum.DATA_ERROR);
        }
        // 保存交易记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(order.getId());
        paymentInfo.setPaymentType(paymentType);
        paymentInfo.setUserId(order.getUserId());
        paymentInfo.setOrderNo(order.getOrderNo());
        paymentInfo.setPaymentStatus(PaymentStatus.UNPAID);
        String subject = "userId:" + order.getUserId() + "下订单";
        paymentInfo.setSubject(subject);
        paymentInfo.setTotalAmount(new BigDecimal("0.01")); // 测试时支付0.01元
//        paymentInfo.setTotalAmount(order.getTotalAmount());
        // 落库添加记录
        paymentInfoMapper.insert(paymentInfo);
        return paymentInfo;
    }

    /**
     * 根据orderNo查询支付记录
     *
     * @param orderNo 订单号
     * @param paymentType 支付类型（1：微信 2：支付宝）
     * @return 支付记录信息
     */
    @Override
    public PaymentInfo getPaymentInfoByOrderNo(String orderNo, PaymentType paymentType) {
        LambdaQueryWrapper<PaymentInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentInfo::getOrderNo, orderNo);
        queryWrapper.eq(PaymentInfo::getPaymentType, paymentType);
        return paymentInfoMapper.selectOne(queryWrapper);
    }

    /**
     * 支付成功，更改支付状态、更改订单状态状态并扣减库存
     *
     * @param orderNo 订单号
     * @param paymentType 支付类型（1：微信 2：支付宝）
     * @param paramMap 微信支付系统返回的订单支付状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(String orderNo, PaymentType paymentType, Map<String,String> paramMap) {
        // 1.查询订单支付表状态，是不是已经支付
        LambdaQueryWrapper<PaymentInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentInfo::getOrderNo, orderNo);
        queryWrapper.eq(PaymentInfo::getPaymentType, paymentType);
        PaymentInfo paymentInfo = paymentInfoMapper.selectOne(queryWrapper);
        if (paymentInfo.getPaymentStatus() != PaymentStatus.UNPAID) {
            return;
        }
        // 2.如果支付记录表中支付状态为未支付，更新
        PaymentInfo paymentInfoUpd = new PaymentInfo();
        paymentInfoUpd.setPaymentStatus(PaymentStatus.PAID);
        String tradeNo = paymentType == PaymentType.WEIXIN ? paramMap.get("ransaction_id") : paramMap.get("trade_no");
        paymentInfoUpd.setTradeNo(tradeNo);
        paymentInfoUpd.setCallbackTime(new Date());
        paymentInfoUpd.setCallbackContent(paramMap.toString());
        paymentInfoMapper.update(paymentInfoUpd, new LambdaQueryWrapper<PaymentInfo>().eq(PaymentInfo::getOrderNo, orderNo));

        // 3.通过RabbitMQ实现发送消息，修改订单记录为已支付，库存扣减
        rabbitService.sendMessage(MqConst.EXCHANGE_PAY_DIRECT, MqConst.ROUTING_PAY_SUCCESS, orderNo);
    }
}