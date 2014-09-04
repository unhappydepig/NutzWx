package cn.xuetang.service.sys;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import cn.xuetang.modules.sys.bean.Sys_role;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.service.BaseService;

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

	public boolean checkPwd(Sys_user user, String newPwd) {
		if (StringUtils.isBlank(newPwd)) {
			return false;
		}
		String hashedPasswordBase64 = new Sha256Hash(newPwd, user.getSalt(), 1024).toBase64();
		if (Lang.equals(user.getPassword(), hashedPasswordBase64)) {
			return true;
		}
		return false;
	}
}
