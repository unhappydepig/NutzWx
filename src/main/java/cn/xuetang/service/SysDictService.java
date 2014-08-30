package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.service.EntityService;

import cn.xuetang.modules.sys.bean.Sys_dict;

@IocBean(fields = { "dao" })
public class SysDictService extends EntityService<Sys_dict> {

	public SysDictService(Dao dao) {
		super(dao);
	}

}
