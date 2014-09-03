package cn.xuetang.service.sys;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_user_log;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class SysUserLogService extends BaseService<Sys_user_log> {

	public SysUserLogService() {
	}
	
	public SysUserLogService(Dao dao) {
		super(dao);
	}

}
