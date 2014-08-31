package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.app.bean.App_project;

@IocBean(fields = { "dao" })
public class AppProjectService extends BaseService<App_project> {

	public AppProjectService(Dao dao) {
		super(dao);
	}
}
