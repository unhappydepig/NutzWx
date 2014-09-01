package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.user.bean.User_score_change;


@IocBean(fields = { "dao" })
public class UserScoreChangeService extends BaseService<User_score_change> {

	public UserScoreChangeService(Dao dao){
		super(dao);
	}
}
