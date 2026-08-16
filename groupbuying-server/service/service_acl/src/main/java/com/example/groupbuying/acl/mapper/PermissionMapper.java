package com.example.groupbuying.acl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.groupbuying.model.acl.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 菜单Mpper接口
 */
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {

}