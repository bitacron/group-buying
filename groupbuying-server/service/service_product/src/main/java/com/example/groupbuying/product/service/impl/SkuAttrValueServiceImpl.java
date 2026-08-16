package com.example.groupbuying.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.groupbuying.model.product.SkuAttrValue;
import com.example.groupbuying.product.mapper.SkuAttrValueMapper;
import com.example.groupbuying.product.service.SkuAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * spu属性值 服务实现类
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue> implements SkuAttrValueService {

    // 根据id查询商品属性信息
    @Override
    public List<SkuAttrValue> getAttrValueListBySkuId(Long id) {
        LambdaQueryWrapper<SkuAttrValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuAttrValue::getSkuId, id);
        List<SkuAttrValue> skuAttrValueList = baseMapper.selectList(queryWrapper);
        return skuAttrValueList;
    }
}
