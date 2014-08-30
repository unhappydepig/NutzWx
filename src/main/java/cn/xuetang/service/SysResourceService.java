package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_resource;

@IocBean(fields = { "dao" })
public class SysResourceService extends BaseService<Sys_resource> {

	public SysResourceService(Dao dao) {
		super(dao);
	}

}
