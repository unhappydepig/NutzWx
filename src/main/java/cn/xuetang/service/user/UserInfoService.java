package cn.xuetang.service.user;

import cn.xuetang.service.BaseService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.user.bean.User_info;

@IocBean(fields = { "dao" })
public class UserInfoService extends BaseService<User_info> {

	public UserInfoService() {
	}
	
	public UserInfoService(Dao dao) {
		super(dao);
	}
}
