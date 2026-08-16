package com.example.groupbuying.acl.service;

import com.example.groupbuying.model.acl.Admin;
import com.example.groupbuying.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户服务接口
 * </p>
 */
public interface AdminService extends IService<Admin> {

	/**
	 * 用户分页列表
	 * @param pageParam
	 * @param adminQueryVo
	 * @return
	 */
	IPage<Admin> selectUserPage(Page<Admin> pageParam, AdminQueryVo adminQueryVo);

}