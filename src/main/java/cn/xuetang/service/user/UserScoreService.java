package cn.xuetang.service.user;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.user.bean.User_score;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class UserScoreService extends BaseService<User_score> {

	
	public UserScoreService(){
	}
	public UserScoreService(Dao dao){
		super(dao);
	}
}
