package com.example.groupbuying.home.service;

import java.util.Map;

public interface ItemService {

    /**
     * 获取sku详细信息
     *
     * @param skuId 商品skuId
     * @param userId 用户Id
     * @return 商品详情
     */
    Map<String, Object> item(Long skuId, Long userId);
}