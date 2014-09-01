package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.user.bean.User_info;

@IocBean(fields = { "dao" })
public class UserInfoService extends BaseService<User_info> {

	public UserInfoService(Dao dao) {
		super(dao);
	}
}
