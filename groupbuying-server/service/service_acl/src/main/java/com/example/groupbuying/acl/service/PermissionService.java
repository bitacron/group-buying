package com.example.groupbuying.acl.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.groupbuying.model.acl.Permission;

import java.util.List;

/**
 * <p>
 * 权限服务接口
 * </p>
 */
public interface PermissionService extends IService<Permission> {

    //获取所有菜单列表
    List<Permission> queryAllMenu();

    //递归删除
    boolean removeChildById(Long id);

    // 根据角色获取菜单
    List<Permission> selectAllMenu(Long roleId);

    // 给角色分配权限
    void saveRolePermissionRelationShip(Long roleId, Long[] permissionIds);
}