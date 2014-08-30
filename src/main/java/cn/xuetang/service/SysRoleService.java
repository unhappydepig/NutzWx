package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_role;

@IocBean(fields = { "dao" })
public class SysRoleService extends BaseService<Sys_role> {

	public SysRoleService(Dao dao) {
		super(dao);
	}

}
