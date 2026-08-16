package com.example.groupbuying.payment.service;

import java.util.Map;

public interface WeiXinService {

	/**
	 * 根据订单号下单，调用微信支付系统生成预付单，生成支付链接
	 *
	 * @param orderNo 订单号
	 * @return 支付链接
	 */
	Map<String, String> createJsapi(String orderNo);
	
	/**
	 * 根据订单号去微信第三方查询支付状态
	 * @param orderNo 订单号
	 * @return 支付状态
	 */
	Map<String, String> queryPayStatus(String orderNo, String paymentType);

}