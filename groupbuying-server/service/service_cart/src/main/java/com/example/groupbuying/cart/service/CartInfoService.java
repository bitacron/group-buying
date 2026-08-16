package com.example.groupbuying.cart.service;

import com.example.groupbuying.model.order.CartInfo;

import java.util.List;

public interface CartInfoService {

    /**
     * 添加购物车
     *
     * @param skuId 商品skuId
     * @param userId 用户Id
     * @param skuNum 商品数量
     */
    void addToCart(Long skuId, Long userId, Integer skuNum);

    /**
     * 删除购物车
     *
     * @param skuId 商品skuId
     * @param userId 用户Id
     */
    void deleteCart(Long skuId, Long userId);

    /**
     * 清空购物车
     *
     * @param userId 用户Id
     */
    void deleteAllCart(Long userId);

    /**
     * 批量删除购物车
     *
     * @param skuIdList 商品skuId集合
     * @param userId 用户Id
     */
    void deleteCartCheck(List<Long> skuIdList, Long userId);

    /**
     * 通过用户Id 查询购物车列表
     *
     * @param userId 用户id
     * @return 购物车列表
     */
    List<CartInfo> getCartList(Long userId);

    /**
     * 更新选中状态
     *
     * @param userId 用户id
     * @param skuId 商品skuId
     * @param isChecked 是否选中
     */
    void checkCart(Long userId, Integer isChecked, Long skuId);

    void checkAllCart(Long userId, Integer isChecked);

    void batchCheckCart(List<Long> skuIdList, Long userId, Integer isChecked);

    /**
     * 根据用户Id 查询购物车列表
     *
     * @param userId 用户Id
     * @return 购物车列表
     */
    List<CartInfo> getCartCheckedList(Long userId);

    void deleteCartCheck(Long userId);
}