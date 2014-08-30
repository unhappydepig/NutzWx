package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_safeconfig;

@IocBean(fields = { "dao" })
public class SysSafeConfigService extends BaseService<Sys_safeconfig> {

	public SysSafeConfigService(Dao dao) {
		super(dao);
	}

}
