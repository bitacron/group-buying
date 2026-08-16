package com.example.groupbuying.acl.service.impl;

import com.example.groupbuying.acl.service.RolePermissionService;
import com.example.groupbuying.acl.utils.PermissionHelper;
import com.example.groupbuying.acl.mapper.PermissionMapper;
import com.example.groupbuying.acl.service.PermissionService;
import com.example.groupbuying.model.acl.Permission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.groupbuying.model.acl.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限服务实现类
 * </p>
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

	//获取所有菜单
	@Override
	public List<Permission> queryAllMenu() {
		// 1.获取全部权限数据
		List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));

		// 2.把权限数据构建成树形结构数据
		List<Permission> result = PermissionHelper.build(allPermissionList);
		return result;
	}

	//递归删除菜单
	@Override
	public boolean removeChildById(Long id) {
		// 1.创建当前菜单id，封装所有要删除菜单id
		List<Long> idList = new ArrayList<>();
		// 递归找当前菜单下面的子菜单
		this.selectChildListById(id, idList);
		idList.add(id);
		baseMapper.deleteBatchIds(idList);
		return true;
	}

	/**
	 *	递归获取子节点
	 * @param id
	 * @param idList
	 */
	private void selectChildListById(Long id, List<Long> idList) {
		// 根据当前菜单id查询下面的子菜单
		// select * from permission where pid = 2;
		List<Permission> childList = baseMapper.selectList(new QueryWrapper<Permission>().eq("pid", id).select("id"));
		// 查询是否还有子菜单，有着话递归查询
		childList.stream().forEach(item -> {
			idList.add(item.getId());
			this.selectChildListById(item.getId(), idList);
		});
	}

	@Autowired
	private RolePermissionService rolePermissionService;

	@Override
	public List<Permission> selectAllMenu(Long roleId) {
		List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));
		//根据角色id获取角色权限
		List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id",roleId));
		//转换给角色id与角色权限对应Map对象
		for (int i = 0; i < allPermissionList.size(); i++) {
			Permission permission = allPermissionList.get(i);
			for (int m = 0; m < rolePermissionList.size(); m++) {
				RolePermission rolePermission = rolePermissionList.get(m);
				if(rolePermission.getPermissionId().equals(permission.getId())) {
					permission.setSelect(true);
				}
			}
		}
		List<Permission> permissionList = buildPermission(allPermissionList);
		return permissionList;
	}

	//把返回所有菜单list集合进行封装的方法
	public static List<Permission> buildPermission(List<Permission> permissionList) {
		//创建list集合，用于数据最终封装
		List<Permission> finalNode = new ArrayList<>();
		//把所有菜单list集合遍历，得到顶层菜单 pid=0菜单，设置level是1
		for(Permission permissionNode : permissionList) {
			//得到顶层菜单 pid=1菜单
			if(permissionNode.getPid().equals(0L)) {
				//设置顶层菜单的level是1
				permissionNode.setLevel(1);
				//根据顶层菜单，向里面进行查询子菜单，封装到finalNode里面
				finalNode.add(selectChildren(permissionNode,permissionList));
			}
		}
		return finalNode;
	}
	private static Permission selectChildren(Permission permissionNode, List<Permission> permissionList) {
		//1 因为向一层菜单里面放二层菜单，二层里面还要放三层，把对象初始化
		permissionNode.setChildren(new ArrayList<Permission>());
		//2 遍历所有菜单list集合，进行判断比较，比较id和pid值是否相同
		for(Permission it : permissionList) {
			//判断 id和pid值是否相同
			if(permissionNode.getId().equals(it.getPid())) {
				//把父菜单的level值+1
				int level = permissionNode.getLevel()+1;
				it.setLevel(level);
				//如果children为空，进行初始化操作
				if(permissionNode.getChildren() == null) {
					permissionNode.setChildren(new ArrayList<Permission>());
				}
				//把查询出来的子菜单放到父菜单里面
				permissionNode.getChildren().add(selectChildren(it,permissionList));
			}
		}
		return permissionNode;
	}

	@Override
	public void saveRolePermissionRelationShip(Long roleId, Long[] permissionIds) {
		//roleId角色id
		//permissionId菜单id 数组形式
		//1 创建list集合，用于封装添加数据
		List<RolePermission> rolePermissionList = new ArrayList<>();
		//遍历所有菜单数组
		for(Long perId : permissionIds) {
			//RolePermission对象
			RolePermission rolePermission = new RolePermission();
			rolePermission.setRoleId(roleId);
			rolePermission.setPermissionId(perId);
			//封装到list集合
			rolePermissionList.add(rolePermission);
		}
		//添加到角色菜单关系表
		rolePermissionService.saveBatch(rolePermissionList);
	}
}