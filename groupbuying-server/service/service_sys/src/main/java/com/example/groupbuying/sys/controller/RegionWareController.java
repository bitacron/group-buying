package com.example.groupbuying.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.model.sys.RegionWare;
import com.example.groupbuying.sys.service.RegionWareService;
import com.example.groupbuying.vo.sys.RegionWareQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 城市仓库关联表 前端控制器
 * </p>
 *
 * @author example
 * @since 2024-04-17
 */
@Api(value = "RegionWare仓库区域管理", tags = "RegionWare仓库区域管理")
@RestController
@RequestMapping(value="/admin/sys/regionWare")
//@CrossOrigin
@SuppressWarnings({"unchecked", "rawtypes"})
public class RegionWareController {
    @Resource
    private RegionWareService regionWareService;

    // 开通区域列表
    @ApiOperation(value = "获取开通区域列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
            @ApiParam(name = "regionWareVo", value = "查询对象", required = false) RegionWareQueryVo regionWareQueryVo) {
        Page<RegionWare> pageParam = new Page<>(page, limit);
        IPage<RegionWare> pageModel = regionWareService.selectPageRegionWare(pageParam, regionWareQueryVo);
        return Result.ok(pageModel);
    }

    // 添加开通区域
    @ApiOperation(value = "新增开通区域")
    @PostMapping("save")
    public Result save(@RequestBody RegionWare regionWare) {
        regionWareService.saveRegionWare(regionWare);
        return Result.ok(null);
    }

    // 删除开通区域
    @ApiOperation(value = "删除开通区域")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        regionWareService.removeById(id);
        return Result.ok(null);
    }

    // 取消开通区域
    @ApiOperation(value = "取消开通区域")
    @PostMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        regionWareService.updateStatus(id, status);
        return Result.ok(null);
    }
}

