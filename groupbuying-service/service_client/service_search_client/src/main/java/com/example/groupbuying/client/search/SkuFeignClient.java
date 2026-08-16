package com.example.groupbuying.client.search;

import com.example.groupbuying.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "service-search")
public interface SkuFeignClient {
    // 获取爆品商品
    @GetMapping("/api/search/sku/inner/findHotSkuList")
    List<SkuEs> findHotSkuList();

    /**
     * 更新商品incrHotScore
     * @param skuId 商品skuId
     * @return 更新结果
     */
    @GetMapping("/api/search/sku/inner/incrHotScore/{skuId}")
    Boolean incrHotScore(@PathVariable("skuId") Long skuId);

}
