package com.example.groupbuying.activity.mapper;

import com.example.groupbuying.model.activity.CouponInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 优惠券信息 Mapper 接口
 * </p>
 *
 * @author example
 * @since 2024-04-24
 */
@Repository
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {

    /**
     * sku优惠券
     *
     * @param skuId 商品skuId
     * @param categoryId 分类id
     * @param userId 用户Id
     * @return sku优惠券
     */
    List<CouponInfo> selectCouponInfoList(@Param("skuId") Long skuId, @Param("categoryId") Long categoryId, @Param("userId") Long userId);
    /**
     * 获取用户全部优惠券
     *
     * @param userId 用户Id
     * @return 用户全部优惠券
     */
    List<CouponInfo> selectCartCouponInfoList(@Param("userId")Long userId);
}
