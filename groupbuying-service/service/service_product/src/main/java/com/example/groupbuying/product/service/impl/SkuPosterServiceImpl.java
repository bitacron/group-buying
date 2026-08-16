package com.example.groupbuying.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.groupbuying.model.product.SkuPoster;
import com.example.groupbuying.product.mapper.SkuPosterMapper;
import com.example.groupbuying.product.service.SkuPosterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品海报表 服务实现类
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
@Service
public class SkuPosterServiceImpl extends ServiceImpl<SkuPosterMapper, SkuPoster> implements SkuPosterService {

    // 根据id查询商品海报列表
    @Override
    public List<SkuPoster> getPosterListBySkuId(Long id) {
        LambdaQueryWrapper<SkuPoster> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuPoster::getSkuId, id);
        List<SkuPoster> skuPosterList = baseMapper.selectList(queryWrapper);
        return skuPosterList;
    }
}
