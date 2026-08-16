package com.example.groupbuying.product.service;

import com.example.groupbuying.model.product.SkuPoster;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品海报表 服务类
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
public interface SkuPosterService extends IService<SkuPoster> {

    // 根据id查询商品海报列表
    List<SkuPoster> getPosterListBySkuId(Long id);
}
