package com.example.groupbuying.acl.controller;

import com.example.groupbuying.acl.service.AdminService;
import com.example.groupbuying.acl.service.RoleService;
import com.example.groupbuying.common.result.Result;
import com.example.groupbuying.common.utils.MD5;
import com.example.groupbuying.model.acl.Admin;
import com.example.groupbuying.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 用户管理 前端控制器
 */
@RestController
@RequestMapping("/admin/acl/user")
@Api(tags = "用户管理")
//@CrossOrigin //跨域
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    // 1.用户列表
    @ApiOperation(value = "获取管理用户分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
            @ApiParam(name = "adminQueryVo", value = "查询对象", required = false) AdminQueryVo adminQueryVo) {
        Page<Admin> pageParam = new Page<>(page, limit);
        IPage<Admin> pageModel = adminService.selectUserPage(pageParam, adminQueryVo);
        return Result.ok(pageModel);
    }

    // 2.id获取用户
    @ApiOperation(value = "获取管理用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Admin user = adminService.getById(id);
        return Result.ok(user);
    }

    // 3.添加用户
    @ApiOperation(value = "新增管理用户")
    @PostMapping("save")
    public Result save(@RequestBody Admin user) {
        //对密码进行MD5处理
        user.setPassword(MD5.encrypt(user.getPassword()));
        adminService.save(user);
        return Result.ok(null);
    }

    // 4.修改用户
    @ApiOperation(value = "修改管理用户")
    @PutMapping("update")
    public Result updateById(@RequestBody Admin user) {
        adminService.updateById(user);
        return Result.ok(null);
    }

    // 5.id删除用户
    @ApiOperation(value = "删除管理用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        adminService.removeById(id);
        return Result.ok(null);
    }

    // 6.根据id列表批量删除用户
    @ApiOperation(value = "根据id列表删除管理用户")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        adminService.removeByIds(idList);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{adminId}")
    public Result toAssign(@PathVariable Long adminId) {
        Map<String, Object> roleMap = roleService.findRoleByUserId(adminId);
        return Result.ok(roleMap);
    }

    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam Long adminId, @RequestParam Long[] roleId) {
        roleService.saveUserRoleRelationShip(adminId, roleId);
        return Result.ok(null);
    }
}