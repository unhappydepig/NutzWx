package cn.xuetang.service;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.user.bean.User_account;

@IocBean(fields = { "dao" })
public class UserAccountService extends BaseService<User_account> {

	public UserAccountService() {
	}
	
	public UserAccountService(Dao dao) {
		super(dao);
	}

	public User_account getUserAccountByUid(int uid) {
		return dao().fetch(getEntityClass(), Cnd.where("uid", "=", uid));
	}

	public boolean deleteUserAccountByUid(int uid) {
		return dao().clear(getEntityClass(), Cnd.where("uid", "=", uid)) > 0;
	}

	public boolean deleteByIds(String[] ids) {
		return dao().clear(getEntityClass(), Cnd.where("uid", "in", ids)) > 0;
	}

	public String listPageJson(int curPage, int pageSize) {
		Criteria cri = Cnd.cri();
		cri.getOrderBy().desc("uid");
		return super.listPageJson(curPage, pageSize, cri);
	}

}
