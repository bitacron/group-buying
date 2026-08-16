package com.example.groupbuying.sys.controller;


import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.sys.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 地区表 前端控制器
 * </p>
 *
 * @author example
 * @since 2024-04-17
 */
@Api(tags = "Region区域接口")
@RestController
@RequestMapping("/admin/sys/region")
//@CrossOrigin
public class RegionController {
    @Resource
    private RegionService regionService;

    @ApiOperation(value = "根据关键字获取地区列表")
    @GetMapping("findRegionByKeyword/{keyword}")
    public Result findRegionByKeyword(@PathVariable("keyword") String keyword) {
        return Result.ok(regionService.getRegionByKeyword(keyword));
    }
}

