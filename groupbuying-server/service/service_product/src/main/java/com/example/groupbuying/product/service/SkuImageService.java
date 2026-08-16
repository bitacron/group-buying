package com.example.groupbuying.product.service;

import com.example.groupbuying.model.product.SkuImage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品图片 服务类
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
public interface SkuImageService extends IService<SkuImage> {

    // 根据id查询商品图片列表
    List<SkuImage> getImageListBySkuId(Long id);
}
