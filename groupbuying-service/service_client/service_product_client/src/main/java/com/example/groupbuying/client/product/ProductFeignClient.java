package com.example.groupbuying.client.product;

import com.example.groupbuying.model.product.Category;
import com.example.groupbuying.model.product.SkuInfo;
import com.example.groupbuying.vo.product.SkuInfoVo;
import com.example.groupbuying.vo.product.SkuStockLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "service-product")
public interface ProductFeignClient {

    @GetMapping("/api/product/inner/getCategory/{categoryId}")
    Category getCategory(@PathVariable("categoryId") Long categoryId);

    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId);

    // 根据skuId批量获取sku信息，活动使用
    @PostMapping("/api/product/inner/findSkuInfoList")
    List<SkuInfo> findSkuInfoList(@RequestBody List<Long> skuIdList);

    //根据关键字获取sku列表，活动使用
    @GetMapping("/api/product/inner/findSkuInfoByKeyword/{keyword}")
    List<SkuInfo> findSkuInfoByKeyword(@PathVariable("keyword") String keyword);

    // 批量获取分类信息
    @PostMapping("/api/product/inner/findCategoryList")
    List<Category> findCategoryList(@RequestBody List<Long> categoryIdList);

    @GetMapping("/api/product/inner/findAllCategoryList")
    List<Category> findAllCategoryList();

    @GetMapping("/api/product/inner/findNewPersonSkuInfoList")
    List<SkuInfo> findNewPersonSkuInfoList();

    @GetMapping("/api/product/inner/getSkuInfoVo/{skuId}")
    SkuInfoVo getSkuInfoVo(@PathVariable("skuId") Long skuId);

    @PostMapping("/api/product/inner/checkAndLock/{orderNo}")
    Boolean checkAndLock(@RequestBody List<SkuStockLockVo> skuStockLockVoList, @PathVariable String orderNo);
}