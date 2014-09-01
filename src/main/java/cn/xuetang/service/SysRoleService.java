package cn.xuetang.service;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_role;
import cn.xuetang.modules.user.bean.Permission;

@IocBean(fields = { "dao" })
public class SysRoleService extends BaseService<Sys_role> {

	
	public SysRoleService() {
	}
	
	public SysRoleService(Dao dao) {
		super(dao);
	}

	public List<String> getPermissionNameList(Sys_role role) {
		dao().fetchLinks(role, "permissions");
		List<String> permissionNameList = new ArrayList<String>();
		for (Permission permission : role.getPermissions()) {
			permissionNameList.add(permission.getName());
		}
		return permissionNameList;
	}
}
