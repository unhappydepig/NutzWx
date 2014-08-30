package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_task;

@IocBean(fields = { "dao" })
public class SysTaskService extends BaseService<Sys_task> {

	public SysTaskService(Dao dao) {
		super(dao);
	}

}
