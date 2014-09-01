package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.user.bean.User_conn_wx;

@IocBean(fields = { "dao" })
public class UserConnWXService extends BaseService<User_conn_wx> {

	public UserConnWXService(Dao dao){
		super(dao);
	}
}
