package com.example.groupbuying.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.groupbuying.model.product.Attr;
import com.example.groupbuying.product.mapper.AttrMapper;
import com.example.groupbuying.product.service.AttrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, Attr> implements AttrService {

    @Override
    public List<Attr> getAttrListByGroupId(Long attrGroupId) {
        LambdaQueryWrapper<Attr> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Attr::getAttrGroupId, attrGroupId);
        List<Attr> attrList = baseMapper.selectList(queryWrapper);
        return attrList;
    }
}
