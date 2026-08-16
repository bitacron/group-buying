package com.example.groupbuying.product.service;

import com.example.groupbuying.model.product.SkuAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * spu属性值 服务类
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
public interface SkuAttrValueService extends IService<SkuAttrValue> {

    // 根据id查询商品属性信息
    List<SkuAttrValue> getAttrValueListBySkuId(Long id);
}
