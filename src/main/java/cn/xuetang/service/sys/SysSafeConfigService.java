package cn.xuetang.service.sys;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_safeconfig;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class SysSafeConfigService extends BaseService<Sys_safeconfig> {

	
	public SysSafeConfigService() {
	}
	
	public SysSafeConfigService(Dao dao) {
		super(dao);
	}

}
