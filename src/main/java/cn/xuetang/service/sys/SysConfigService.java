package cn.xuetang.service.sys;

import cn.xuetang.service.BaseService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_config;

@IocBean(fields = { "dao" })
public class SysConfigService extends BaseService<Sys_config> {

	public SysConfigService() {
	}
	
	public SysConfigService(Dao dao) {
		super(dao);
	}

}
