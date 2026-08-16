package com.example.groupbuying.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.groupbuying.client.cart.CartFeignClient;
import com.example.groupbuying.client.activity.ActivityFeignClient;
import com.example.groupbuying.client.product.ProductFeignClient;
import com.example.groupbuying.client.user.UserFeignClient;
import com.example.groupbuying.common.auth.AuthContextHolder;
import com.example.groupbuying.common.constant.RedisConst;
import com.example.groupbuying.common.exception.CustomException;
import com.example.groupbuying.common.result.ResultCodeEnum;
import com.example.groupbuying.common.utils.DateUtil;
import com.example.groupbuying.enums.*;
import com.example.groupbuying.model.activity.ActivityRule;
import com.example.groupbuying.model.activity.CouponInfo;
import com.example.groupbuying.model.order.CartInfo;
import com.example.groupbuying.model.order.OrderInfo;
import com.example.groupbuying.model.order.OrderItem;
import com.example.groupbuying.mq.constant.MqConst;
import com.example.groupbuying.mq.service.RabbitService;
import com.example.groupbuying.order.mapper.OrderDeliverMapper;
import com.example.groupbuying.order.mapper.OrderInfoMapper;
import com.example.groupbuying.order.mapper.OrderItemMapper;
import com.example.groupbuying.order.service.OrderInfoService;
import com.example.groupbuying.order.service.OrderItemService;
import com.example.groupbuying.order.service.OrderLogService;
import com.example.groupbuying.order.service.OrderSetService;
import com.example.groupbuying.vo.order.CartInfoVo;
import com.example.groupbuying.vo.order.OrderConfirmVo;
import com.example.groupbuying.vo.order.OrderSubmitVo;
import com.example.groupbuying.vo.order.OrderUserQueryVo;
import com.example.groupbuying.vo.product.SkuStockLockVo;
import com.example.groupbuying.vo.user.LeaderAddressVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

	@Resource
	private OrderInfoMapper orderInfoMapper;
	@Resource
	private OrderItemMapper orderItemMapper;

	@Resource
	private OrderItemService orderItemService;

	@Resource
	private CartFeignClient cartFeignClient;

	@Resource
	private OrderLogService orderLogService;

	@Resource
	private OrderSetService orderSetService;

	@Resource
	private ActivityFeignClient activityFeignClient;

//	@Resource
//	private SeckillFeignClient seckillFeignClient;

	@Resource
	private UserFeignClient userFeignClient;

	@Resource
	private OrderDeliverMapper orderDeliverMapper;

	@Resource
	private ProductFeignClient productFeignClient;

	@Resource
	private RabbitService rabbitService;

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public OrderConfirmVo confirmOrder() {
		// 获取用户Id
		Long userId = AuthContextHolder.getUserId();

		//获取用户地址
		LeaderAddressVo leaderAddressVo = userFeignClient.getUserAddressByUserId(userId);

		// 先得到用户想要购买的商品！（勾选的）
		List<CartInfo> cartInfoList = cartFeignClient.getCartCheckedList(userId);

		// 防重：生成一个唯一标识，保存到redis中一份
		String orderNo = System.currentTimeMillis()+""; // IdWorker.getTimeId();
		redisTemplate.opsForValue().set(RedisConst.ORDER_REPEAT + orderNo, orderNo, 24, TimeUnit.HOURS);

		//获取购物车满足条件的促销与优惠券信息
		OrderConfirmVo orderTradeVo = activityFeignClient.findCartActivityAndCoupon(cartInfoList, userId);
		orderTradeVo.setLeaderAddressVo(leaderAddressVo);
		orderTradeVo.setOrderNo(orderNo);
		return orderTradeVo;
	}

	/**
	 * 生成订单
	 *
	 * @param orderParamVo 订单参数
	 * @return 订单id
	 */
	@Override
	@Transactional
	public Long submitOrder(OrderSubmitVo orderParamVo) {
		// 第一步：设置给哪个用户生成订单
		Long userId = AuthContextHolder.getUserId();
		orderParamVo.setUserId(userId);

		// 第二步：防重，通过redis+lua脚本进行判断。lua脚本保证原子性操作
		// 1.获取传递过来的订单orderNo
		String orderNo = orderParamVo.getOrderNo();
		if (StringUtils.isEmpty(orderNo)){
			throw new CustomException(ResultCodeEnum.ILLEGAL_REQUEST);
		}
		// 2.拿着orderNo到redis进行查询
		String script = "if(redis.call('get', KEYS[1]) == ARGV[1]) then return redis.call('del', KEYS[1]) else return 0 end";
		Boolean flag = (Boolean) redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Collections.singletonList(RedisConst.ORDER_REPEAT + orderNo), orderNo);
		// 3.如果redis有相同orderNo，表示正常提交订单，将redis的orderNo删除
		// 4.如果redis无相同orderNo，表示重复提交订单，不能再往后进行操作
		if (!flag){
			throw new CustomException(ResultCodeEnum.REPEAT_SUBMIT);
		}

		// 第三步：验证库存，并锁定库存
		// 比如仓库有10个西红柿，想买2个西红柿。验证库存，查询仓库里面是否有充足的西红柿。
		// 库存充足，库存锁定2西红柿。（目前并没有真正减库存，用户支付购买后真正减库存）锁定期间不能在被其他用户购买，一定时间后，锁定过期，则解除锁定
		//普通商品
//		List<Long> skuIdList = orderSubmitVo.getSkuIdList();
		// 1.远程调用service-cart模块，获取当前用户购物车商品（选中的购物项
		List<CartInfo> cartInfoList = cartFeignClient.getCartCheckedList(AuthContextHolder.getUserId());
		// 2.购物车有很多商品，商品不同类型，重点处理普通类型商品
		List<CartInfo> commonSkuList = cartInfoList.stream().filter(cartInfo -> cartInfo.getSkuType() == SkuType.COMMON.getCode()).collect(Collectors.toList());
		// 3. 把获取的购物车里面普通类型商品list集合转换
		if(!CollectionUtils.isEmpty(commonSkuList)) {
			List<SkuStockLockVo> commonStockLockVoList = commonSkuList.stream().map(item -> {
				SkuStockLockVo skuStockLockVo = new SkuStockLockVo();
				skuStockLockVo.setSkuId(item.getSkuId());
				skuStockLockVo.setSkuNum(item.getSkuNum());
				return skuStockLockVo;
			}).collect(Collectors.toList());
			// 4.调用service-product模块实现锁定锁定商品。验证库存并锁定库存，保证具有原子性。
			Boolean isLockSuccess = productFeignClient.checkAndLock(commonStockLockVoList, orderParamVo.getOrderNo());
			if (!isLockSuccess){
				throw new CustomException(ResultCodeEnum.ORDER_STOCK_FALL);
			}
		}

		//2.2秒杀商品
//		List<CartInfo> seckillSkuList = cartInfoList.stream().filter(cartInfo -> cartInfo.getSkuType() == SkuType.SECKILL.getCode()).collect(Collectors.toList());
//		if(!CollectionUtils.isEmpty(seckillSkuList)) {
//			List<SkuStockLockVo> seckillStockLockVoList = seckillSkuList.stream().map(item -> {
//				SkuStockLockVo skuStockLockVo = new SkuStockLockVo();
//				skuStockLockVo.setSkuId(item.getSkuId());
//				skuStockLockVo.setSkuNum(item.getSkuNum());
//				return skuStockLockVo;
//			}).collect(Collectors.toList());
//			//是否锁定
//			Boolean isLockSeckill = seckillFeignClient.checkAndMinusStock(seckillStockLockVoList, orderParamVo.getOrderNo());
//			if (!isLockSeckill){
//				throw new CustomException(ResultCodeEnum.ORDER_STOCK_FALL);
//			}
//		}

		// 第四步：下单。向2张表添加数据。order_info和order_item
		Long orderId;
		try {
			orderId = this.saveOrder(orderParamVo, cartInfoList);

			// 订单正常创建成功的情况下，发送消息定时关单
//			int normalOrderOvertime = orderSetService.getNormalOrderOvertime();
//			rabbitService.sendDelayMessage(MqConst.EXCHANGE_ORDER_DIRECT, MqConst.ROUTING_CANCEL_ORDER, orderParamVo.getOrderNo(), normalOrderOvertime);
		} catch (Exception e) {
//			e.printStackTrace();
			// 出现异常立马解锁库存 标记订单时无效订单
//			rabbitService.sendMessage(MqConst.EXCHANGE_ORDER_DIRECT, MqConst.ROUTING_ROLLBACK_STOCK, orderParamVo.getOrderNo());
			throw new CustomException(ResultCodeEnum.CREATE_ORDER_FAIL);
		}

		// 5.异步删除购物车中对应的记录。不应该影响下单的整体流程
		rabbitService.sendMessage(MqConst.EXCHANGE_ORDER_DIRECT, MqConst.ROUTING_DELETE_CART, orderParamVo.getUserId());

		//说明：商品价格在此不做校验，我们在购物车里面已经校验，商品价格只会在停售时间更改
		// 第五步：返回订单id
		return orderId;
	}

	@Transactional(rollbackFor = {Exception.class})
	public Long saveOrder(OrderSubmitVo orderSubmitVo, List<CartInfo> cartInfoList) {
		Long userId = AuthContextHolder.getUserId();
		if(CollectionUtils.isEmpty(cartInfoList)) {
			throw new CustomException(ResultCodeEnum.DATA_ERROR);
		}
		LeaderAddressVo leaderAddressVo = userFeignClient.getUserAddressByUserId(userId);
		if(null == leaderAddressVo) {
			throw new CustomException(ResultCodeEnum.DATA_ERROR);
		}

		//计算购物项分摊的优惠减少金额，按比例分摊，退款时按实际支付金额退款
		Map<String, BigDecimal> activitySplitAmountMap = this.computeActivitySplitAmount(cartInfoList);
		Map<String, BigDecimal> couponInfoSplitAmountMap = this.computeCouponInfoSplitAmount(cartInfoList, orderSubmitVo.getCouponId());
		//sku对应的订单明细
		List<OrderItem> orderItemList = new ArrayList<>();
		// 保存订单明细
		for (CartInfo cartInfo : cartInfoList) {
			OrderItem orderItem = new OrderItem();
			orderItem.setId(null);
			orderItem.setCategoryId(cartInfo.getCategoryId());
			if(cartInfo.getSkuType() == SkuType.COMMON.getCode()) {
				orderItem.setSkuType(SkuType.COMMON);
			} else {
				orderItem.setSkuType(SkuType.SECKILL);
			}
			orderItem.setSkuId(cartInfo.getSkuId());
			orderItem.setSkuName(cartInfo.getSkuName());
			orderItem.setSkuPrice(cartInfo.getCartPrice());
			orderItem.setImgUrl(cartInfo.getImgUrl());
			orderItem.setSkuNum(cartInfo.getSkuNum());
			orderItem.setLeaderId(orderSubmitVo.getLeaderId());

			// 促销活动分摊金额
			BigDecimal splitActivityAmount = activitySplitAmountMap.get("activity:"+orderItem.getSkuId());
			if(null == splitActivityAmount) {
				splitActivityAmount = new BigDecimal(0);
			}
			orderItem.setSplitActivityAmount(splitActivityAmount);

			// 优惠券分摊金额
			BigDecimal splitCouponAmount = couponInfoSplitAmountMap.get("coupon:"+orderItem.getSkuId());
			if(null == splitCouponAmount) {
				splitCouponAmount = new BigDecimal(0);
			}
			orderItem.setSplitCouponAmount(splitCouponAmount);

			// 优惠后的总金额
			BigDecimal skuTotalAmount = orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum()));
			BigDecimal splitTotalAmount = skuTotalAmount.subtract(splitActivityAmount).subtract(splitCouponAmount);
			orderItem.setSplitTotalAmount(splitTotalAmount);
			orderItemList.add(orderItem);
		}

		//保存订单
		OrderInfo order = new OrderInfo();
		order.setUserId(userId);
//		private String nickName;
		order.setOrderNo(orderSubmitVo.getOrderNo());
		order.setOrderStatus(OrderStatus.UNPAID);
		order.setProcessStatus(ProcessStatus.UNPAID);
		order.setCouponId(orderSubmitVo.getCouponId());
		order.setLeaderId(orderSubmitVo.getLeaderId());
		order.setLeaderName(leaderAddressVo.getLeaderName());
		order.setLeaderPhone(leaderAddressVo.getLeaderPhone());
		order.setTakeName(leaderAddressVo.getTakeName());
		order.setReceiverName(orderSubmitVo.getReceiverName());
		order.setReceiverPhone(orderSubmitVo.getReceiverPhone());
		order.setReceiverProvince(leaderAddressVo.getProvince());
		order.setReceiverCity(leaderAddressVo.getCity());
		order.setReceiverDistrict(leaderAddressVo.getDistrict());
		order.setReceiverAddress(leaderAddressVo.getDetailAddress());
		order.setWareId(cartInfoList.get(0).getWareId());

		//计算订单金额
		BigDecimal originalTotalAmount = this.computeTotalAmount(cartInfoList);
		BigDecimal activityAmount = activitySplitAmountMap.get("activity:total");
		if(null == activityAmount) activityAmount = new BigDecimal(0);
		BigDecimal couponAmount = couponInfoSplitAmountMap.get("coupon:total");
		if(null == couponAmount) couponAmount = new BigDecimal(0);
		BigDecimal totalAmount = originalTotalAmount.subtract(activityAmount).subtract(couponAmount);
		//计算订单金额
		order.setOriginalTotalAmount(originalTotalAmount);
		order.setActivityAmount(activityAmount);
		order.setCouponAmount(couponAmount);
		order.setTotalAmount(totalAmount);

		//计算团长佣金
//		BigDecimal profitRate = orderSetService.getProfitRate();
		BigDecimal profitRate = new BigDecimal(0);
		BigDecimal commissionAmount = order.getTotalAmount().multiply(profitRate);
		order.setCommissionAmount(commissionAmount);

		orderInfoMapper.insert(order);

		//保存订单项
		for(OrderItem orderItem : orderItemList) {
			orderItem.setOrderId(order.getId());
		}
		orderItemService.saveBatch(orderItemList);

		//更新优惠券使用状态
		if(null != order.getCouponId()) {
			activityFeignClient.updateCouponInfoUseStatus(order.getCouponId(), userId, order.getId());
		}

		// 下单成功，记录用户商品购买个数
		// redis的hash类型 key(userId) - field(skuId)-value(skuNum)
		String orderSkuKey = RedisConst.ORDER_SKU_MAP + orderSubmitVo.getUserId();
		BoundHashOperations<String, String, Integer> hashOperations = redisTemplate.boundHashOps(orderSkuKey);
		cartInfoList.forEach(cartInfo -> {
			if(Boolean.TRUE.equals(hashOperations.hasKey(cartInfo.getSkuId().toString()))) {
				Integer orderSkuNum = hashOperations.get(cartInfo.getSkuId().toString()) + cartInfo.getSkuNum();
				hashOperations.put(cartInfo.getSkuId().toString(), orderSkuNum);
			}
		});
		redisTemplate.expire(orderSkuKey, DateUtil.getCurrentExpireTimes(), TimeUnit.SECONDS);

		//发送消息
		return order.getId();
	}

	private BigDecimal computeTotalAmount(List<CartInfo> cartInfoList) {
		BigDecimal total = new BigDecimal(0);
		for (CartInfo cartInfo : cartInfoList) {
			BigDecimal itemTotal = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
			total = total.add(itemTotal);
		}
		return total;
	}

	/**
	 * 计算购物项分摊的优惠减少金额
	 * 打折：按折扣分担
	 * 现金：按比例分摊
	 *
	 * @param cartInfoParamList 购物项列表
	 * @return 优惠减少金额
	 */
	private Map<String, BigDecimal> computeActivitySplitAmount(List<CartInfo> cartInfoParamList) {
		Map<String, BigDecimal> activitySplitAmountMap = new HashMap<>();

		//促销活动相关信息
		List<CartInfoVo> cartInfoVoList = activityFeignClient.findCartActivityList(cartInfoParamList);

		//活动总金额
		BigDecimal activityReduceAmount = new BigDecimal(0);
		if(!CollectionUtils.isEmpty(cartInfoVoList)) {
			for(CartInfoVo cartInfoVo : cartInfoVoList) {
				ActivityRule activityRule = cartInfoVo.getActivityRule();
				List<CartInfo> cartInfoList = cartInfoVo.getCartInfoList();
				if(null != activityRule) {
					//优惠金额， 按比例分摊
					BigDecimal reduceAmount = activityRule.getReduceAmount();
					activityReduceAmount = activityReduceAmount.add(reduceAmount);
					if(cartInfoList.size() == 1) {
						activitySplitAmountMap.put("activity:"+cartInfoList.get(0).getSkuId(), reduceAmount);
					} else {
						//总金额
						BigDecimal originalTotalAmount = new BigDecimal(0);
						for(CartInfo cartInfo : cartInfoList) {
							BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
							originalTotalAmount = originalTotalAmount.add(skuTotalAmount);
						}
						//记录除最后一项是所有分摊金额， 最后一项=总的 - skuPartReduceAmount
						BigDecimal skuPartReduceAmount = new BigDecimal(0);
						if (activityRule.getActivityType() == ActivityType.FULL_REDUCTION) {
							for(int i=0, len=cartInfoList.size(); i<len; i++) {
								CartInfo cartInfo = cartInfoList.get(i);
								if(i < len -1) {
									BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
									//sku分摊金额
									BigDecimal skuReduceAmount = skuTotalAmount.divide(originalTotalAmount, 2, RoundingMode.HALF_UP).multiply(reduceAmount);
									activitySplitAmountMap.put("activity:"+cartInfo.getSkuId(), skuReduceAmount);

									skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
								} else {
									BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
									activitySplitAmountMap.put("activity:"+cartInfo.getSkuId(), skuReduceAmount);
								}
							}
						} else {
							for(int i=0, len=cartInfoList.size(); i<len; i++) {
								CartInfo cartInfo = cartInfoList.get(i);
								if(i < len -1) {
									BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));

									//sku分摊金额
									BigDecimal skuDiscountTotalAmount = skuTotalAmount.multiply(activityRule.getBenefitDiscount().divide(new BigDecimal("10")));
									BigDecimal skuReduceAmount = skuTotalAmount.subtract(skuDiscountTotalAmount);
									activitySplitAmountMap.put("activity:"+cartInfo.getSkuId(), skuReduceAmount);

									skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
								} else {
									BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
									activitySplitAmountMap.put("activity:"+cartInfo.getSkuId(), skuReduceAmount);
								}
							}
						}
					}
				}
			}
		}
		activitySplitAmountMap.put("activity:total", activityReduceAmount);
		return activitySplitAmountMap;
	}

	private Map<String, BigDecimal> computeCouponInfoSplitAmount(List<CartInfo> cartInfoList, Long couponId) {
		Map<String, BigDecimal> couponInfoSplitAmountMap = new HashMap<>();

		if(null == couponId) return couponInfoSplitAmountMap;
		CouponInfo couponInfo = activityFeignClient.findRangeSkuIdList(cartInfoList, couponId);

		if(null != couponInfo) {
			//sku对应的订单明细
			Map<Long, CartInfo> skuIdToCartInfoMap = new HashMap<>();
			for (CartInfo cartInfo : cartInfoList) {
				skuIdToCartInfoMap.put(cartInfo.getSkuId(), cartInfo);
			}
			//优惠券对应的skuId列表
			List<Long> skuIdList = couponInfo.getSkuIdList();
			if(CollectionUtils.isEmpty(skuIdList)) {
				return couponInfoSplitAmountMap;
			}
			//优惠券优化总金额
			BigDecimal reduceAmount = couponInfo.getAmount();
			if(skuIdList.size() == 1) {
				//sku的优化金额
				couponInfoSplitAmountMap.put("coupon:"+skuIdToCartInfoMap.get(skuIdList.get(0)).getSkuId(), reduceAmount);
			} else {
				//总金额
				BigDecimal originalTotalAmount = new BigDecimal(0);
				for (Long skuId : skuIdList) {
					CartInfo cartInfo = skuIdToCartInfoMap.get(skuId);
					BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
					originalTotalAmount = originalTotalAmount.add(skuTotalAmount);
				}
				//记录除最后一项是所有分摊金额， 最后一项=总的 - skuPartReduceAmount
				BigDecimal skuPartReduceAmount = new BigDecimal(0);
				if (couponInfo.getCouponType() == CouponType.CASH || couponInfo.getCouponType() == CouponType.FULL_REDUCTION) {
					for(int i=0, len=skuIdList.size(); i<len; i++) {
						CartInfo cartInfo = skuIdToCartInfoMap.get(skuIdList.get(i));
						if(i < len -1) {
							BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
							//sku分摊金额
							BigDecimal skuReduceAmount = skuTotalAmount.divide(originalTotalAmount, 2, RoundingMode.HALF_UP).multiply(reduceAmount);
							couponInfoSplitAmountMap.put("coupon:"+cartInfo.getSkuId(), skuReduceAmount);

							skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
						} else {
							BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
							couponInfoSplitAmountMap.put("coupon:"+cartInfo.getSkuId(), skuReduceAmount);
						}
					}
				}
			}
			couponInfoSplitAmountMap.put("coupon:total", couponInfo.getAmount());
		}
		return couponInfoSplitAmountMap;
	}

	/**
	 * 获取订单详情
	 *
	 * @param orderId 订单id
	 * @return 订单详情
	 */
	@Override
	public OrderInfo getOrderInfoById(Long orderId) {
		// 根据orderId，查询订单基本信息
		OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
		orderInfo.getParam().put("orderStatusName", orderInfo.getOrderStatus().getComment());
		// 根据orderId，查询订单所有的订单项List
		List<OrderItem> orderItemList = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderInfo.getId()));
		// 封装
		orderInfo.setOrderItemList(orderItemList);
		return orderInfo;
	}

	/**
	 * 获取订单详情
	 *
	 * @param orderNo 订单orderNo
	 * @return 订单详情
	 */
	@Override
	public OrderInfo getOrderInfoByOrderNo(String orderNo) {
		QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(OrderInfo::getOrderNo, orderNo);
		OrderInfo orderInfo = baseMapper.selectOne(queryWrapper);
		orderInfo.getParam().put("orderStatusName", orderInfo.getOrderStatus().getComment());
		List<OrderItem> orderItemList = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderInfo.getId()));
		orderInfo.setOrderItemList(orderItemList);
		return orderInfo;
	}

	/**
	 * 更改订单状态、通知扣减库存
	 *
	 * @param orderNo 订单orderNo
	 */
	@Override
	public void orderPay(String orderNo) {
		OrderInfo orderInfo = this.getOrderInfoByOrderNo(orderNo);
		if (Objects.isNull(orderInfo) || orderInfo.getOrderStatus() != OrderStatus.UNPAID) {
			return;
		}

		// 更改订单状态为待发货
		this.updateOrderStatus(orderInfo.getId(), ProcessStatus.WAITING_DELEVER);

		// 通过RabbitMQ发送消息通知product模块，扣减库存
		rabbitService.sendMessage(MqConst.EXCHANGE_ORDER_DIRECT, MqConst.ROUTING_MINUS_STOCK, orderNo);
	}

	private void updateOrderStatus(Long orderId, ProcessStatus processStatus) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setId(orderId);
		orderInfo.setProcessStatus(processStatus);
		orderInfo.setOrderStatus(processStatus.getOrderStatus());
		if(processStatus == ProcessStatus.WAITING_DELEVER) {
			orderInfo.setPaymentTime(new Date());
		} else if(processStatus == ProcessStatus.WAITING_LEADER_TAKE) {
			orderInfo.setDeliveryTime(new Date());
		} else if(processStatus == ProcessStatus.WAITING_USER_TAKE) {
			orderInfo.setTakeTime(new Date());
		}
		orderInfoMapper.updateById(orderInfo);
	}

	/**
	 * 订单分页查询-搜索条件
	 *
	 * @param pageParam        分页查询信息
	 * @param orderUserQueryVo 查询条件-订单类型（待付款、待发货、待提货等）等
	 * @return 订单信息
	 */
	@Override
	public IPage<OrderInfo> findUserOrderPage(Page<OrderInfo> pageParam, OrderUserQueryVo orderUserQueryVo) {
		LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(OrderInfo::getUserId,orderUserQueryVo.getUserId());
		wrapper.eq(OrderInfo::getOrderStatus,orderUserQueryVo.getOrderStatus());
		IPage<OrderInfo> pageModel = baseMapper.selectPage(pageParam, wrapper);

		//获取每个订单，把每个订单里面订单项查询封装
		List<OrderInfo> orderInfoList = pageModel.getRecords();
		for(OrderInfo orderInfo : orderInfoList) {
			//根据订单id查询里面所有订单项列表
			List<OrderItem> orderItemList = orderItemMapper.selectList(
					new LambdaQueryWrapper<OrderItem>()
							.eq(OrderItem::getOrderId, orderInfo.getId())
			);
			//把订单项集合封装到每个订单里面
			orderInfo.setOrderItemList(orderItemList);
			//封装订单状态名称
			orderInfo.getParam().put("orderStatusName",orderInfo.getOrderStatus().getComment());
		}
		return pageModel;
	}
}