package com.example.groupbuying.cart.service.impl;

import com.example.groupbuying.cart.service.CartInfoService;
import com.example.groupbuying.client.product.ProductFeignClient;
import com.example.groupbuying.common.constant.RedisConst;
import com.example.groupbuying.common.exception.CustomException;
import com.example.groupbuying.common.result.ResultCodeEnum;
import com.example.groupbuying.enums.SkuType;
import com.example.groupbuying.model.order.CartInfo;
import com.example.groupbuying.model.product.SkuInfo;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CartInfoServiceImpl implements CartInfoService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ProductFeignClient productFeignClient;

    private String getCartKey(Long userId) {
        //定义key user:userId:cart
        return RedisConst.USER_KEY_PREFIX + userId + RedisConst.USER_CART_KEY_SUFFIX;
    }
    /**
     * 添加购物车
     *
     * @param skuId 商品skuId
     * @param userId 用户Id
     * @param skuNum 商品数量
     */
    @Override
    public void addToCart(Long skuId, Long userId, Integer skuNum) {
        // 定义key user:userId:cart
        String cartKey = getCartKey(userId);
        //获取缓存对象
        BoundHashOperations<String, String, CartInfo> hashOperations = redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = null;
        // 如果购物车中有该商品并且没有失效，则更新个数并选中 && cartInfo.getStatus().intValue() == 1
        if (hashOperations.hasKey(skuId.toString())) {
            cartInfo = hashOperations.get(skuId.toString());
            int currentSkuNum = cartInfo.getSkuNum() + skuNum;
            if(currentSkuNum < 1) {
                return;
            }

            //获取用户当前已经购买的sku个数，sku限量，每天不能超买
            //  添加购物车数量
            cartInfo.setSkuNum(currentSkuNum);
            //当天购买数量
            cartInfo.setCurrentBuyNum(currentSkuNum);
            //大于限购个数，不能更新个数
            if(currentSkuNum >= cartInfo.getPerLimit()) {
                throw new CustomException(ResultCodeEnum.SKU_LIMIT_ERROR);
            }
            cartInfo.setIsChecked(1);
            cartInfo.setUpdateTime(new Date());
        } else {
            //第一次添加只能添加一个
            skuNum = 1;

            // 当购物车中没用该商品的时候，则直接添加到购物车！insert
            cartInfo = new CartInfo();
            // 购物车数据是从商品详情得到 {skuInfo}
            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
            if(null == skuInfo) {
                throw new CustomException(ResultCodeEnum.DATA_ERROR);
            }
            cartInfo.setSkuId(skuId);
            cartInfo.setCategoryId(skuInfo.getCategoryId());
            cartInfo.setSkuType(skuInfo.getSkuType());
            cartInfo.setIsNewPerson(skuInfo.getIsNewPerson());
            cartInfo.setUserId(userId);
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setCurrentBuyNum(skuNum);
            cartInfo.setSkuType(SkuType.COMMON.getCode());
            cartInfo.setPerLimit(skuInfo.getPerLimit());
            cartInfo.setImgUrl(skuInfo.getImgUrl());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setWareId(skuInfo.getWareId());
            cartInfo.setIsChecked(1);
            cartInfo.setStatus(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
        }

        // 更新缓存
        hashOperations.put(skuId.toString(), cartInfo);
        // 设置过期时间
        this.setCartKeyExpire(cartKey);
    }

    //  设置key 的过期时间！
    private void setCartKeyExpire(String cartKey) {
        redisTemplate.expire(cartKey, RedisConst.USER_CART_EXPIRE, TimeUnit.SECONDS);
    }
    /**
     * 删除购物车
     *
     * @param skuId 商品skuId
     * @param userId 用户Id
     */
    @Override
    public void deleteCart(Long skuId, Long userId) {
        BoundHashOperations<String, String, CartInfo> boundHashOps = this.redisTemplate.boundHashOps(this.getCartKey(userId));
        //  判断购物车中是否有该商品！
        if (boundHashOps.hasKey(skuId.toString())){
            boundHashOps.delete(skuId.toString());
        }
    }
    /**
     * 清空购物车
     *
     * @param userId 用户Id
     */
    @Override
    public void deleteAllCart(Long userId) {
        String cartKey = getCartKey(userId);
        //获取缓存对象
        BoundHashOperations<String, String, CartInfo> hashOperations = redisTemplate.boundHashOps(cartKey);
        hashOperations.values().forEach(cartInfo -> {
            hashOperations.delete(cartInfo.getSkuId().toString());
        });
    }
    /**
     * 批量删除购物车
     *
     * @param skuIdList 商品skuId集合
     * @param userId 用户Id
     */
    @Override
    public void deleteCartCheck(List<Long> skuIdList, Long userId) {
        String cartKey = getCartKey(userId);
        //获取缓存对象
        BoundHashOperations<String, String, CartInfo> hashOperations = redisTemplate.boundHashOps(cartKey);
        skuIdList.forEach(skuId -> {
            hashOperations.delete(skuId.toString());
        });
    }

    /**
     * 通过用户Id 查询购物车列表
     *
     * @param userId 用户id
     * @return 购物车列表
     */
    @Override
    public List<CartInfo> getCartList(Long userId) {
        // 一个返回的集合对象
        List<CartInfo> cartInfoList = new ArrayList<>();
        if (Objects.isNull(userId)) return cartInfoList;

        // 定义key user:userId:cart
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> hashOperations = redisTemplate.boundHashOps(cartKey);
        // 获取数据
        cartInfoList = hashOperations.values();
        if (!CollectionUtils.isEmpty(cartInfoList)) {
            // 购物车列表显示有顺序：按照商品的更新时间 降序
            cartInfoList.sort(new Comparator<CartInfo>() {
                @Override
                public int compare(CartInfo o1, CartInfo o2) {
                    // str1 = ab str2 = ac;
                    return o2.getCreateTime().compareTo(o1.getCreateTime());
                }
            });
        }
        return cartInfoList;
    }

    /**
     * 更新选中状态
     *
     * @param userId 用户id
     * @param skuId 商品skuId
     * @param isChecked 是否选中
     */
    @Override
    public void checkCart(Long userId, Integer isChecked, Long skuId) {
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> boundHashOps = this.redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = boundHashOps.get(skuId.toString());
        if(null != cartInfo) {
            cartInfo.setIsChecked(isChecked);
            boundHashOps.put(skuId.toString(), cartInfo);
            this.setCartKeyExpire(cartKey);
        }
    }

    @Override
    public void checkAllCart(Long userId, Integer isChecked) {
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> boundHashOps = this.redisTemplate.boundHashOps(cartKey);
        Objects.requireNonNull(boundHashOps.values()).forEach(cartInfo -> {
            cartInfo.setIsChecked(isChecked);
            boundHashOps.put(cartInfo.getSkuId().toString(), cartInfo);
        });
        this.setCartKeyExpire(cartKey);
    }

    @Override
    public void batchCheckCart(List<Long> skuIdList, Long userId, Integer isChecked) {
        String cartKey = getCartKey(userId);
        //获取缓存对象
        BoundHashOperations<String, String, CartInfo> hashOperations = redisTemplate.boundHashOps(cartKey);
        skuIdList.forEach(skuId -> {
            CartInfo cartInfo = hashOperations.get(skuId.toString());
            assert cartInfo != null;
            cartInfo.setIsChecked(isChecked);
            hashOperations.put(cartInfo.getSkuId().toString(), cartInfo);
        });
    }

    /**
     * 根据用户Id 查询购物车列表
     *
     * @param userId 用户Id
     * @return 购物车列表
     */
    @Override
    public List<CartInfo> getCartCheckedList(Long userId) {
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> boundHashOps = this.redisTemplate.boundHashOps(cartKey);
        // isChecked = 1 购物项选中
        List<CartInfo> cartInfoCheckList = boundHashOps.values().stream().filter((cartInfo) -> {
            return cartInfo.getIsChecked() == 1;
        }).collect(Collectors.toList());
        return cartInfoCheckList;
    }
    /**
     * 根据userId删除选中购物车记录
     *
     * @param userId 用户Id
     */
    @Override
    public void deleteCartCheck(Long userId) {
        // 根据UserId查询选中购物车记录
        List<CartInfo> cartInfoList = this.getCartCheckedList(userId);
        // 处理数据，获取skuId集合
        List<Long> skuIdList = cartInfoList.stream().map(CartInfo::getSkuId).collect(Collectors.toList());
        // 构建Redis的key。hash类型 key field-value
        String cartKey = getCartKey(userId);
        // 获取缓存对象。根据key查询filed-value结构
        BoundHashOperations<String, String, CartInfo> hashOperations = redisTemplate.boundHashOps(cartKey);
        // 根据filed（skuId）删除redis数据
        skuIdList.forEach(skuId -> {
            hashOperations.delete(skuId.toString());
        });
    }
}
