package com.example.groupbuying.home.service.impl;

import com.example.groupbuying.client.search.SkuFeignClient;
import com.example.groupbuying.client.activity.ActivityFeignClient;
import com.example.groupbuying.enums.SkuType;
import com.example.groupbuying.client.product.ProductFeignClient;
import com.example.groupbuying.home.service.ItemService;
import com.example.groupbuying.vo.activity.SeckillSkuVo;
import com.example.groupbuying.vo.product.SkuInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ItemServiceImpl implements ItemService {

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private ActivityFeignClient activityFeignClient;

    @Resource
    private SkuFeignClient searchFeignClient;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 获取sku详细信息
     *
     * @param skuId 商品skuId
     * @param userId 用户Id
     * @return 商品详情
     */
    @Override
    public Map<String, Object> item(Long skuId, Long userId) {
        Map<String, Object> result = new HashMap<>();

        // 通过skuId 查询skuInfo
        // 通过CompletableFuture异步计算获取。supplyAsync可以支持返回值
        CompletableFuture<SkuInfoVo> skuInfoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            // 通过Feign远程调用获取sku基本信息的数据
            SkuInfoVo skuInfoVo = productFeignClient.getSkuInfoVo(skuId);
            result.put("skuInfoVo", skuInfoVo);
            return skuInfoVo;
        }, threadPoolExecutor);

        //TODO 如果商品是秒杀商品，获取秒杀信息

        // 通过CompletableFuture异步计算获取。runAsync方法不支持返回值
        CompletableFuture<Void> activityCompletableFuture = CompletableFuture.runAsync(() -> {
            //sku对应的促销与优惠券信息
            Map<String, Object> activityAndCouponMap = activityFeignClient.findActivityAndCoupon(skuId, userId);
            result.putAll(activityAndCouponMap);
        },threadPoolExecutor);

        // 通过CompletableFuture异步更新商品热度
        CompletableFuture<Void> hotCompletableFuture = CompletableFuture.runAsync(() -> {
            searchFeignClient.incrHotScore(skuId);
        },threadPoolExecutor);

        //  任务组合：多个任务都完成后，再往后执行
        CompletableFuture.allOf(
                skuInfoCompletableFuture,
//                seckillSkuCompletableFuture,
                activityCompletableFuture,
                hotCompletableFuture
        ).join();
        return result;
    }
}