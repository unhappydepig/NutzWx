package cn.xuetang.service.sys;

import java.util.List;

import cn.xuetang.service.BaseService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.sys.bean.Sys_task;

@IocBean(fields = { "dao" })
public class SysTaskService extends BaseService<Sys_task> {

	public SysTaskService() {
	}

	public SysTaskService(Dao dao) {
		super(dao);
	}

	public List<Sys_task> list() {
		return dao().query(getEntityClass(), Cnd.where("is_enable", "=", 0));
	}

}
