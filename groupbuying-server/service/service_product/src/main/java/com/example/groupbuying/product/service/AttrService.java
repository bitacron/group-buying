package com.example.groupbuying.product.service;

import com.example.groupbuying.model.product.Attr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
public interface AttrService extends IService<Attr> {
    //根据属性分组id 获取属性列表
    List<Attr> getAttrListByGroupId(Long attrGroupId);
}
