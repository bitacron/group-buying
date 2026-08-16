package com.example.groupbuying.product.mapper;

import com.example.groupbuying.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * sku信息 Mapper 接口
 * </p>
 *
 * @author example
 * @since 2024-04-19
 */
@Repository
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    SkuInfo checkStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);

    Integer lockStock(@Param("skuId")Long skuId, @Param("skuNum")Integer skuNum);

    Integer unlockStock(@Param("skuId")Long skuId, @Param("skuNum")Integer skuNum);

    /**
     * 减库存
     *
     * @param skuId 商品skuId
     * @param skuNum 减少数量
     */
    void minusStock(@Param("skuId")Long skuId, @Param("skuNum")Integer skuNum);
}
