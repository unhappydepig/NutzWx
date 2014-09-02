package cn.xuetang.service.sys;

import java.util.ArrayList;
import java.util.List;

import cn.xuetang.service.BaseService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import cn.xuetang.modules.sys.bean.Sys_role;
import cn.xuetang.modules.sys.bean.Sys_user;

@IocBean(fields = { "dao" })
public class SysUserService extends BaseService<Sys_user> {

	public SysUserService() {
	}

	public SysUserService(Dao dao) {
		super(dao);
	}

	public Sys_user fetchByName(String name) {
		return fetch(Cnd.where("loginname", "=", name));
	}

	public Sys_user fetchByOpenID(String openid) {
		Sys_user user = fetch(Cnd.where("openid", "=", openid));
		if (!Lang.isEmpty(user)) {
			dao().fetchLinks(user, "servers");
			dao().fetchLinks(user, "roles");
		}
		return user;
	}
	public List<String> getRoleNameList(Sys_user user) {
		dao().fetchLinks(user, "roles");
		List<String> roleNameList = new ArrayList<String>();
		for (Sys_role role : user.getRoles()) {
			roleNameList.add(role.getName());
		}
		return roleNameList;
	}
}
