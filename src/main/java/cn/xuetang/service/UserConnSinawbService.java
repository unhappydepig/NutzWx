package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.user.bean.User_conn_sinawb;

@IocBean(fields = { "dao" })
public class UserConnSinawbService extends BaseService<User_conn_sinawb> {

	public UserConnSinawbService(Dao dao) {
		super(dao);
	}
}
