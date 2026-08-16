package com.example.groupbuying.acl.service.impl;

import com.example.groupbuying.acl.mapper.AdminMapper;
import com.example.groupbuying.acl.service.AdminService;
import com.example.groupbuying.acl.service.RoleService;
import com.example.groupbuying.model.acl.Admin;
import com.example.groupbuying.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
/**
 * <p>
 * 用户角色服务实现类
 * </p>
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

	@Autowired
	private AdminMapper userMapper;

	@Autowired
	private RoleService roleService;

	@Override
	public IPage<Admin> selectUserPage(Page<Admin> pageParam, AdminQueryVo adminQueryVo) {
		//获取用户名称条件值
		String username = adminQueryVo.getUsername();
		String name = adminQueryVo.getName();
		//创建条件构造器
		LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
		if(!StringUtils.isEmpty(username)) {
			//封装条件
			wrapper.like(Admin::getUsername, username);
		}
		if(!StringUtils.isEmpty(name)) {
			//封装条件
			wrapper.like(Admin::getName, name);
		}
		//调用mapper方法
		IPage<Admin> pageModel = baseMapper.selectPage(pageParam, wrapper);
		return pageModel;
	}
}