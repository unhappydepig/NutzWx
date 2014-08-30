package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_user_log;

@IocBean(fields = { "dao" })
public class SysUserLogService extends BaseService<Sys_user_log> {

	public SysUserLogService(Dao dao) {
		super(dao);
	}

}
