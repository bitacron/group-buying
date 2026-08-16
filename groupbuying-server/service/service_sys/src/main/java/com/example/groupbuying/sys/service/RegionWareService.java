package com.example.groupbuying.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.groupbuying.model.sys.RegionWare;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.groupbuying.vo.sys.RegionWareQueryVo;

/**
 * <p>
 * 城市仓库关联表 服务类
 * </p>
 *
 * @author example
 * @since 2024-04-17
 */
public interface RegionWareService extends IService<RegionWare> {

    // 开通区域列表
    IPage<RegionWare> selectPageRegionWare(Page<RegionWare> pageParam, RegionWareQueryVo regionWareQueryVo);

    // 添加开通区域
    void saveRegionWare(RegionWare regionWare);

    //取消开通区域
    void updateStatus(Long id, Integer status);
}
