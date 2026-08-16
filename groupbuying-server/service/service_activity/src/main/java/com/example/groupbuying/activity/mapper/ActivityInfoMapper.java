package com.example.groupbuying.activity.mapper;

import com.example.groupbuying.model.activity.ActivityInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.groupbuying.model.activity.ActivityRule;
import com.example.groupbuying.model.activity.ActivitySku;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author example
 * @since 2024-04-24
 */
@Repository
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {

    List<Long> selectExistSkuIdList(@Param("skuIdList")List<Long> skuIdList);

    List<ActivityRule> findActivityRule(Long skuId);

    /**
     * 根据skuIdList查询对应的活动列表
     *
     * @param skuIdList skuId集合
     * @return 活动列表
     */
    List<ActivitySku> selectCartActivityList(@Param("skuIdList") List<Long> skuIdList);
}
