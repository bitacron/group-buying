package com.example.groupbuying.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.groupbuying.common.exception.CustomException;
import com.example.groupbuying.common.result.ResultCodeEnum;
import com.example.groupbuying.model.sys.RegionWare;
import com.example.groupbuying.sys.mapper.RegionWareMapper;
import com.example.groupbuying.sys.service.RegionWareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.groupbuying.vo.sys.RegionWareQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * <p>
 * 城市仓库关联表 服务实现类
 * </p>
 *
 * @author example
 * @since 2024-04-17
 */
@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare> implements RegionWareService {

    @Resource
    private RegionWareMapper regionWareMapper;

    // 开通区域列表
    @Override
    public IPage<RegionWare> selectPageRegionWare(Page<RegionWare> pageParam, RegionWareQueryVo regionWareQueryVo) {
        // 1.获取查询条件值
        String keyword = regionWareQueryVo.getKeyword();
        // 2.判断条件值是否为空，不为空则封装条件
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(keyword)) {
            // 封装条件 关键词keyWord与 区域名称 或 仓库名称 中的一个模糊查询
            wrapper.like(RegionWare::getRegionName,keyword)
                    .or().like(RegionWare::getWareName,keyword);
        }
        IPage<RegionWare> regionWarePage = baseMapper.selectPage(pageParam, wrapper);
        return regionWarePage;
    }

    // 添加开通区域
    @Override
    public void saveRegionWare(RegionWare regionWare) {
        LambdaQueryWrapper<RegionWare> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(RegionWare::getRegionId, regionWare.getRegionId());
        Integer count = regionWareMapper.selectCount(queryWrapper);
        // 如果此次添加的区域数据已经存在了，抛出异常
        if(count > 0) {
            throw new CustomException(ResultCodeEnum.REGION_OPEN);
        }
        baseMapper.insert(regionWare);
    }

    //取消开通区域
    @Override
    public void updateStatus(Long id, Integer status) {
        RegionWare regionWare = baseMapper.selectById(id);
        regionWare.setStatus(status);
        baseMapper.updateById(regionWare);
    }
}
