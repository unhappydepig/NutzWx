package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_user_role;

@IocBean(fields = { "dao" })
public class SysUserRoleService extends BaseService<Sys_user_role> {

	public SysUserRoleService() {
	}
	
	public SysUserRoleService(Dao dao) {
		super(dao);
	}

}
