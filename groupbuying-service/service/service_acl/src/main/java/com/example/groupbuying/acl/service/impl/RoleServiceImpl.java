package com.example.groupbuying.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.groupbuying.acl.mapper.RoleMapper;
import com.example.groupbuying.acl.service.AdminRoleService;
import com.example.groupbuying.acl.service.RoleService;
import com.example.groupbuying.model.acl.AdminRole;
import com.example.groupbuying.model.acl.Role;
import com.example.groupbuying.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

	// 1.角色分页列表
	@Override
	public IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {
		//获取条件值：角色名称
		String roleName = roleQueryVo.getRoleName();
		//创建条件构造器对象
		LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
		//判断条件值是否为空
		if(!StringUtils.isEmpty(roleName)) {
			//封装条件
			wrapper.like(Role::getRoleName,roleName);
		}
		//调用mapper方法实现条件分页查询
		IPage<Role> pageModel = baseMapper.selectPage(pageParam, wrapper);
		return pageModel;
	}

	@Autowired
	private AdminRoleService adminRoleService;
	/**
	 * 根据用户获取角色数据
	 *
	 * @param adminId
	 * @return
	 */
	@Override
	public Map<String, Object> findRoleByUserId(Long adminId) {
		// 1.查询所有的角色
		List<Role> allRolesList =baseMapper.selectList(null);
		// 2.根据用户id查询用户分配角色列表
		// 2.1根据用户id查询用户角色关系表admin_role，查询用户分配角色id列表
		List<AdminRole> adminRoleList = adminRoleService.list(new QueryWrapper<AdminRole>().eq("admin_id", adminId).select("role_id"));
		// 2.2根据第一步返回集合，获取所有角色id的列表List<Long>
		List<Long> roleIdsList = adminRoleList.stream().map(c->c.getRoleId()).collect(Collectors.toList());
		// 2.3创建一个新的List集合，用于存储用户配置角色
		List<Role> assignRoles = new ArrayList<Role>();
		// 2.4遍历所有角色列表，得到每个角色，判断所有角色里面是否半酣已经分配的角色id，对角色进行分类
		for (Role role : allRolesList) {
			//已分配
			if(roleIdsList.contains(role.getId())) {
				assignRoles.add(role);
			}
		}
		// 3.封装到Map
		Map<String, Object> roleMap = new HashMap<>();
		roleMap.put("assignRoles", assignRoles);
		roleMap.put("allRolesList", allRolesList);
		return roleMap;
	}

	/**
	 * 分配角色
	 *
	 * @param adminId
	 * @param roleIds
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveUserRoleRelationShip(Long adminId, Long[] roleIds) {
		// 1.删除用户已经分配过的角色数据(根据用户id删除admin_role表里面对应数据)
		adminRoleService.remove(new QueryWrapper<AdminRole>().eq("admin_id", adminId));

		// 2.重新分配新的角色(遍历多个角色id，得到每个角色id，拿着每个角色id+用户id添加用户关系表)
		List<AdminRole> userRoleList = new ArrayList<>();
		for(Long roleId : roleIds) {
			if(StringUtils.isEmpty(roleId)) continue;
			AdminRole userRole = new AdminRole();
			userRole.setAdminId(adminId);
			userRole.setRoleId(roleId);
			userRoleList.add(userRole);
		}
		adminRoleService.saveBatch(userRoleList);
	}


}