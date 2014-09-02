package cn.xuetang.service.sys;

import cn.xuetang.service.BaseService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_safeconfig;

@IocBean(fields = { "dao" })
public class SysSafeConfigService extends BaseService<Sys_safeconfig> {

	
	public SysSafeConfigService() {
	}
	
	public SysSafeConfigService(Dao dao) {
		super(dao);
	}

}
