package com.example.groupbuying.mq.constant;

public class MqConst {
    /**
     * 消息补偿
     */
    public static final String MQ_KEY_PREFIX = "groupbuying.mq:list";
    public static final int RETRY_COUNT = 3;

    /**
     * 商品上下架
     */
    public static final String EXCHANGE_GOODS_DIRECT = "groupbuying.goods.direct";
    public static final String ROUTING_GOODS_UPPER = "groupbuying.goods.upper";
    public static final String ROUTING_GOODS_LOWER = "groupbuying.goods.lower";
    //队列
    public static final String QUEUE_GOODS_UPPER  = "groupbuying.goods.upper";
    public static final String QUEUE_GOODS_LOWER  = "groupbuying.goods.lower";

    /**
     * 团长上下线
     */
    public static final String EXCHANGE_LEADER_DIRECT = "groupbuying.leader.direct";
    public static final String ROUTING_LEADER_UPPER = "groupbuying.leader.upper";
    public static final String ROUTING_LEADER_LOWER = "groupbuying.leader.lower";
    //队列
    public static final String QUEUE_LEADER_UPPER  = "groupbuying.leader.upper";
    public static final String QUEUE_LEADER_LOWER  = "groupbuying.leader.lower";

    //订单
    public static final String EXCHANGE_ORDER_DIRECT = "groupbuying.order.direct";
    public static final String ROUTING_ROLLBACK_STOCK = "groupbuying.rollback.stock";
    public static final String ROUTING_MINUS_STOCK = "groupbuying.minus.stock";

    public static final String ROUTING_DELETE_CART = "groupbuying.delete.cart";
    //解锁普通商品库存
    public static final String QUEUE_ROLLBACK_STOCK = "groupbuying.rollback.stock";
    public static final String QUEUE_SECKILL_ROLLBACK_STOCK = "groupbuying.seckill.rollback.stock";
    public static final String QUEUE_MINUS_STOCK = "groupbuying.minus.stock";
    public static final String QUEUE_DELETE_CART = "groupbuying.delete.cart";

    //支付
    public static final String EXCHANGE_PAY_DIRECT = "groupbuying.pay.direct";
    public static final String ROUTING_PAY_SUCCESS = "groupbuying.pay.success";
    public static final String QUEUE_ORDER_PAY  = "groupbuying.order.pay";
    public static final String QUEUE_LEADER_BILL  = "groupbuying.leader.bill";

    //取消订单
    public static final String EXCHANGE_CANCEL_ORDER_DIRECT = "groupbuying.cancel.order.direct";
    public static final String ROUTING_CANCEL_ORDER = "groupbuying.cancel.order";
    //延迟取消订单队列
    public static final String QUEUE_CANCEL_ORDER  = "groupbuying.cancel.order";

    /**
     * 定时任务
     */
    public static final String EXCHANGE_DIRECT_TASK = "groupbuying.exchange.direct.task";
    public static final String ROUTING_TASK_23 = "groupbuying.task.23";
    //队列
    public static final String QUEUE_TASK_23  = "groupbuying.queue.task.23";
}