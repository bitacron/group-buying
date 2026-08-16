package com.example.groupbuying.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.groupbuying.enums.PaymentType;
import com.example.groupbuying.model.order.PaymentInfo;
import com.example.groupbuying.payment.service.PaymentService;
import com.example.groupbuying.payment.service.WeiXinService;
import com.example.groupbuying.payment.util.ConstantPropertiesUtils;
import com.example.groupbuying.payment.util.HttpClient;
import com.example.groupbuying.vo.user.UserLoginVo;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WeiXinServiceImpl implements WeiXinService {

    @Resource
    private PaymentService paymentService;

	@Resource
	private RedisTemplate<Object, Object> redisTemplate;

	/**
	 * 根据订单号下单，调用微信支付系统生成预付单，生成支付链接
	 *
	 * @param orderNo 订单号
	 * @return 支付链接
	 */
	@Override
	public Map<String, String> createJsapi(String orderNo) {

//		Map payMap = (Map) redisTemplate.opsForValue().get(orderNo);
//		if(null != payMap) return payMap;
		// 1.向payment_info支付记录表中添加记录，目前支付状态：正在支付中
		PaymentInfo paymentInfo = paymentService.getPaymentInfoByOrderNo(orderNo, PaymentType.WEIXIN);
		if(null == paymentInfo) {
			paymentInfo = paymentService.savePaymentInfo(orderNo, PaymentType.WEIXIN);
		}

		// 2.封装微信支付系统接口需要参数
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("appid", ConstantPropertiesUtils.APPID);
		paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
		paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
		paramMap.put("body", paymentInfo.getSubject());
		paramMap.put("out_trade_no", paymentInfo.getOrderNo());
		int totalFee = paymentInfo.getTotalAmount().multiply(new BigDecimal(100)).intValue();
		paramMap.put("total_fee", String.valueOf(totalFee));
		paramMap.put("spbill_create_ip", "127.0.0.1");
		paramMap.put("notify_url", ConstantPropertiesUtils.NOTIFYURL);
		paramMap.put("trade_type", "JSAPI");
		// openId
		UserLoginVo userLoginVo = (UserLoginVo) redisTemplate.opsForValue().get("user:login:" + paymentInfo.getUserId());
		if(null != userLoginVo && !StringUtils.isEmpty(userLoginVo.getOpenId())) {
			paramMap.put("openid", userLoginVo.getOpenId());
		} else {
			paramMap.put("openid", "oD7av4igt-00GI8PqsIlg5FROYnI");
		}

		// 3.使用HTTPClient调用微信支付系统接口，根据URL访问第三方接口并且传递参数
		HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
		try {
			// client设置参数。xml格式
			client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
			client.setHttps(true);
			client.post();
			// 4.返回第三方的数据
			String xml = client.getContent();
			Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
			log.info("微信下单返回结果：{}", JSON.toJSONString(resultMap));

			// 5.再次封装参数-包含预付单标识 prepay_id
			Map<String, String> parameterMap = new HashMap<>();
			String prepayId = String.valueOf(resultMap.get("prepay_id"));
			String packages = "prepay_id=" + prepayId;
			parameterMap.put("appId", ConstantPropertiesUtils.APPID);
			parameterMap.put("nonceStr", resultMap.get("nonce_str"));
			parameterMap.put("package", packages);
			parameterMap.put("signType", "MD5");
			parameterMap.put("timeStamp", String.valueOf(new Date().getTime()));
			String sign = WXPayUtil.generateSignature(parameterMap, ConstantPropertiesUtils.PARTNERKEY);

			//返回结果
			Map<String, String> result = new HashMap<>();
			result.put("timeStamp", parameterMap.get("timeStamp"));
			result.put("nonceStr", parameterMap.get("nonceStr"));
			result.put("signType", "MD5");
			result.put("paySign", sign);
			result.put("package", packages);
			if(null != resultMap.get("result_code")) {
				//微信支付二维码2小时过期，可采取2小时未支付取消订单
				redisTemplate.opsForValue().set(orderNo, result, 120, TimeUnit.MINUTES);
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, String> queryPayStatus(String orderNo, String paymentType) {
		//1、封装参数
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("appid", ConstantPropertiesUtils.APPID);
		paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
		paramMap.put("out_trade_no", orderNo);
		paramMap.put("nonce_str", WXPayUtil.generateNonceStr());

		//2、设置请求
		HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
		try {
			client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
			client.setHttps(true);
			client.post();
			//3、返回第三方的数据
			String xml = client.getContent();

			return WXPayUtil.xmlToMap(xml);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}