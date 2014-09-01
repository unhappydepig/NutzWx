package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.user.bean.User_score;

@IocBean(fields = { "dao" })
public class UserScoreService extends BaseService<User_score> {

	
	public UserScoreService(){
	}
	public UserScoreService(Dao dao){
		super(dao);
	}
}
