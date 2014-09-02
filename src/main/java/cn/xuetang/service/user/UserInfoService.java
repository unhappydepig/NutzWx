package cn.xuetang.service.user;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.user.bean.User_info;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class UserInfoService extends BaseService<User_info> {

	public UserInfoService() {
	}
	
	public UserInfoService(Dao dao) {
		super(dao);
	}
}
