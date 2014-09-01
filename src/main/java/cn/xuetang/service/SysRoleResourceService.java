package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_role_resource;

@IocBean(fields = { "dao" })
public class SysRoleResourceService extends BaseService<Sys_role_resource> {

	public SysRoleResourceService() {
	}
	
	public SysRoleResourceService(Dao dao) {
		super(dao);
	}
}
