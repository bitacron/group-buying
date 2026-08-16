package com.example.groupbuying.cart.receiver;

import com.example.groupbuying.cart.service.CartInfoService;
import com.example.groupbuying.mq.constant.MqConst;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class CartReceiver {

    @Resource
    private CartInfoService cartInfoService;


    /**
     * 通过RabbitMQ，异步删除购物车选项
     *
     * @param userId 用户Id
     * @param message 消息
     * @param channel 信道
     * @throws IOException IO异常
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_DELETE_CART, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_ORDER_DIRECT),
            key = {MqConst.ROUTING_DELETE_CART}
    ))
    public void deleteCart(Long userId, Message message, Channel channel) throws IOException {
        if (null != userId){
            // 获取用户id和skuIdList
            cartInfoService.deleteCartCheck(userId);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}