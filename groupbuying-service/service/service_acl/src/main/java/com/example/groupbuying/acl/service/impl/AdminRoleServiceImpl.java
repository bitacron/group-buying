package com.example.groupbuying.acl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.groupbuying.acl.mapper.AdminRoleMapper;
import com.example.groupbuying.acl.service.AdminRoleService;
import com.example.groupbuying.model.acl.AdminRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;

/**
 * 用户角色服务实现类
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements AdminRoleService {

}