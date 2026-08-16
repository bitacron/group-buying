package com.example.groupbuying.sys.controller;


import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.sys.service.WareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 仓库表 前端控制器
 * </p>
 *
 * @author example
 * @since 2024-04-17
 */
@Api(value = "Ware仓库管理", tags = "Ware仓库管理")
@RestController
@RequestMapping(value="/admin/sys/ware")
//@CrossOrigin
public class WareController {

    @Resource
    private WareService wareService;

    @ApiOperation(value = "获取全部仓库")
    @GetMapping("findAllList")
    public Result findAllList() {
        return Result.ok(wareService.list());
    }
    // 根据关键字分页查询仓库列表
    // 根据id查询仓库信息
    // 新增仓库
    // 修改仓库
    // 删除仓库
    // 批量删除仓库

}

