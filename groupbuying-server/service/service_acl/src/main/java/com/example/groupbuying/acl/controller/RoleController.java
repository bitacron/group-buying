package com.example.groupbuying.acl.controller;

import com.example.groupbuying.acl.service.RoleService;
import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.model.acl.Role;
import com.example.groupbuying.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/admin/acl/role")
@Api(tags = "角色接口")
@Slf4j
//@CrossOrigin //跨域
public class RoleController {

    // 注入RoleService
    @Autowired
    private RoleService roleService;

    // 1.条件分页查询角色列表
    @ApiOperation(value = "获取角色分页列表")
    @GetMapping("{current}/{limit}")
    public Result index(
            @ApiParam(name = "current", value = "当前页码", required = true) @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
            @ApiParam(name = "roleQueryVo", value = "查询对象", required = false) RoleQueryVo roleQueryVo) {
        // 1.创建Page对象，传递当前页和每页记录数
        Page<Role> pageParam = new Page<>(current, limit);
        // 2.调用RoleService的selectRolePage方法实现条件分页查询，返回分页对象
        IPage<Role> pageModel = roleService.selectRolePage(pageParam, roleQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取角色")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return Result.ok(role);
    }

    // 2.新增角色
    @ApiOperation(value = "新增角色")
    @PostMapping("save")
    public Result save(@RequestBody Role role) {
        roleService.save(role);
        return Result.ok(null);
    }

    // 3.根据id修改角色
    @ApiOperation(value = "修改角色")
    @PutMapping("update")
    public Result updateById(@RequestBody Role role) {
        roleService.updateById(role);
        return Result.ok(null);
    }

    // 根据id删除角色
    @ApiOperation(value = "删除角色")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        roleService.removeById(id);
        return Result.ok(null);
    }

    // 6.批量删除角色
    @ApiOperation(value = "根据id列表删除角色")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        roleService.removeByIds(idList);
        return Result.ok(null);
    }
}