package cn.xuetang.service.sys;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.app.bean.App_project;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class AppProjectService extends BaseService<App_project> {

	public AppProjectService() {
	}

	public AppProjectService(Dao dao) {
		super(dao);
	}

	public List<App_project> list() {
		return dao().query(getEntityClass(), Cnd.orderBy().asc("id"));
	}
}
