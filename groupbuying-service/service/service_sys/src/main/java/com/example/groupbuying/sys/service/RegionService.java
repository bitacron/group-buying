package com.example.groupbuying.sys.service;

import com.example.groupbuying.model.sys.Region;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地区表 服务类
 * </p>
 *
 * @author example
 * @since 2024-04-17
 */
public interface RegionService extends IService<Region> {
    // 根据关键字获取地区列表
    List<Region> getRegionByKeyword(String keyword);
}
