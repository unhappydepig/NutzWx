package cn.xuetang.service.sys;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_unit;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class SysUnitService extends BaseService<Sys_unit> {

	
	public SysUnitService() {
	}	
	public SysUnitService(Dao dao) {
		super(dao);
	}
}
