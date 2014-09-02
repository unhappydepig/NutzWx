package cn.xuetang.service.user;

import cn.xuetang.service.BaseService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.user.bean.User_conn_sinawb;

@IocBean(fields = { "dao" })
public class UserConnSinawbService extends BaseService<User_conn_sinawb> {
	public UserConnSinawbService() {
	}

	public UserConnSinawbService(Dao dao) {
		super(dao);
	}
}
