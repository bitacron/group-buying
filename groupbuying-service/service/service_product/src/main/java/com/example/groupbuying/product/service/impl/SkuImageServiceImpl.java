package com.example.groupbuying.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.groupbuying.model.product.SkuImage;
import com.example.groupbuying.product.mapper.SkuImageMapper;
import com.example.groupbuying.product.service.SkuImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品图片 服务实现类
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage> implements SkuImageService {

    // 根据id查询商品图片列表
    @Override
    public List<SkuImage> getImageListBySkuId(Long id) {
        LambdaQueryWrapper<SkuImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuImage::getSkuId, id);
        List<SkuImage> skuImageList = baseMapper.selectList(queryWrapper);
        return skuImageList;
    }
}
