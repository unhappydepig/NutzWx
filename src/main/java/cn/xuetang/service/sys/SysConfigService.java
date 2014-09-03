package cn.xuetang.service.sys;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_config;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class SysConfigService extends BaseService<Sys_config> {

	public SysConfigService() {
	}
	
	public SysConfigService(Dao dao) {
		super(dao);
	}

}
