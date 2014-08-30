package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_user;

@IocBean(fields = { "dao" })
public class SysUserService extends BaseService<Sys_user> {

	public SysUserService(Dao dao) {
		super(dao);
	}

}
