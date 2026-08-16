package com.example.groupbuying.order.service.impl;

import com.example.groupbuying.model.order.OrderItem;
import com.example.groupbuying.order.mapper.OrderItemMapper;
import com.example.groupbuying.order.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单项信息 服务实现类
 * </p>
 *
 * @author example
 * @since 2025-11-10
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
