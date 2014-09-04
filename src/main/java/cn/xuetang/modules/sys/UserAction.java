package cn.xuetang.modules.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.web.Webs;

import cn.xuetang.common.util.SortHashtable;
import cn.xuetang.modules.sys.bean.Sys_role;
import cn.xuetang.modules.sys.bean.Sys_unit;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.service.sys.AppInfoService;
import cn.xuetang.service.sys.SysRoleService;
import cn.xuetang.service.sys.SysUnitService;
import cn.xuetang.service.sys.SysUserService;

/**
 * @author Wizzer.cn
 * @time 2012-9-21 下午1:30:15
 */
@IocBean
@At("/private/sys/user")
public class UserAction{

	@Inject
	private SysRoleService sysRoleService;
	@Inject
	private SysUnitService sysUnitService;
	@Inject
	private SysUserService sysUserService;
	@Inject
	private AppInfoService appInfoService;

	@At("")
	@Ok("vm:template.private.sys.user")
	public void user(@Attr(Webs.ME) Sys_user user,HttpServletRequest req) {
		String unitid = user.getUnitid();
		req.setAttribute("unitid", unitid);
	}

	@At
	@Ok("raw")
	public String search(@Param("username") String username, @Param("userid") int userid) {
		if (!Strings.isBlank(username)) {
			return Json.toJson(sysUserService.listByCnd(Cnd.where("realname", "like", "%" + username + "%")));
		} else if (userid > 0) {
			return Json.toJson(sysUserService.listByCnd(Cnd.where("userid", "=", userid)));
		}
		return "";
	}

	@At
	@Ok("raw")
	public String tree(@Attr(Webs.ME) Sys_user user,@Param("id") String id, HttpSession session) throws Exception {
		id = Strings.sNull(id);
		List<Object> array = new ArrayList<Object>();
		if ("".equals(id)) {
			Map<String, Object> jsonroot = new HashMap<String, Object>();
			jsonroot.put("id", "");
			jsonroot.put("pId", "0");
			jsonroot.put("name", "机构列表");
			jsonroot.put("url", "javascript:list(\"\")");
			jsonroot.put("target", "_self");
			jsonroot.put("icon", appInfoService.getAPP_BASE_NAME() + "/images/icons/icon042a1.gif");
			array.add(jsonroot);
		}
		Criteria cri = Cnd.cri();
		if (user.isSystem()) // 判断是否为系统管理员角色
		{
			cri.where().and("id", "like", id + "____");
			cri.getOrderBy().asc("location");
			cri.getOrderBy().asc("id");
		} else {
			if ("".equals(id)) {
				cri.where().and("id", "=", user.getUnitid());
				cri.getOrderBy().asc("location");
				cri.getOrderBy().asc("id");
			} else {
				cri.where().and("id", "like", id + "____");
				cri.getOrderBy().asc("location");
				cri.getOrderBy().asc("id");
			}
		}
		List<Sys_unit> unitlist = sysUnitService.listByCnd(cri);
		int i = 0;
		for (Sys_unit u : unitlist) {
			String pid = u.getId().substring(0, u.getId().length() - 4);
			int num = sysUnitService.getRowCount(Cnd.wrap("id like '" + u.getId() + "____'"));
			if (i == 0 || "".equals(pid))
				pid = "0";
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("id", u.getId());
			obj.put("pId", pid);
			obj.put("name", u.getName());
			obj.put("url", "javascript:list(\"" + u.getId() + "\")");
			obj.put("target", "_self");
			obj.put("isParent", num > 0 ? true : false);
			array.add(obj);
			i++;
		}
		return Json.toJson(array);
	}

	@At
	@Ok("raw")
	public String getuname(@Param("unitid") String unitid) {
		if (StringUtils.isBlank(unitid))
			return "所有机构";
		return sysUnitService.detailByName(unitid).getName();
	}

	@At
	@Ok("raw")
	public String list(@Attr(Webs.ME) Sys_user user,@Param("unitid") String unitid, @Param("page") int curPage, @Param("rows") int pageSize, @Param("SearchUserName") String SearchUserName, @Param("lock") String lock, HttpSession session) {
		Criteria cri = Cnd.cri();
		if (StringUtils.isNotBlank(unitid)) {
			cri.where().and("unitid", "like", unitid + "%");
		} else {
			if (user.isSystem()) { // 判断是否为系统管理员角色
				cri.where().and("1", "=", 1);
			} else {
				cri.where().and("unitid", "like", user.getUnitid() + "%");
			}
		}
		if ("1".equals(lock)) {
			cri.where().and("state", "=", 1);
		}
		if (StringUtils.isNotBlank(SearchUserName)) {
			SqlExpressionGroup exp = Cnd.exps("loginname", "LIKE", "%" + SearchUserName + "%").or("realname", "LIKE", "%" + SearchUserName + "%");
			cri.where().and(exp);
		}
		cri.getOrderBy().asc("loginname");
		return sysUserService.listPageJson(curPage, pageSize, cri);
	}

	@SuppressWarnings("rawtypes")
	@At
	@Ok("vm:template.private.sys.userAdd")
	public void toadd(@Attr(Webs.ME) Sys_user user,@Param("unitid") String unitid1, HttpSession session, HttpServletRequest req) {
		Sys_unit unit = sysUnitService.detailByName(unitid1);
		req.setAttribute("unit", unit);
		Condition sql;
		Condition sqlunit;
		Condition sqlrole;
		List<Sys_role> list = null;
		List<Sys_role> rolelist = null;
		List<Sys_unit> unitlist = null;
		int sysrole = 0;
		if (user.getRolelist().contains(2)) // 判断是否为系统管理员角色
		{
			sqlunit = Cnd.wrap("id in (select unitid from sys_role ) order by location,id asc");
			sqlrole = Cnd.wrap("unitid is null or unitid='' order by location,id asc");
			sysrole = 1;
		} else {
			sqlunit = Cnd.wrap("id in (select unitid from sys_role where unitid like '" + user.getUnitid() + "%') order by location,id asc");
			sqlrole = Cnd.wrap("(unitid is null or unitid='') and id in (select roleid from sys_user_role where userid='" + user.getUserid() + "') order by location,id asc");
		}
		unitlist = sysUnitService.listByCnd(sqlunit);
		req.setAttribute("sysrole", sysrole);
		Hashtable<String, String> hm = new Hashtable<String, String>();
		List<Object> array = new ArrayList<Object>();
		Map<String, Object> jsonroot = new HashMap<String, Object>();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "角色列表");
		jsonroot.put("checked", false);
		jsonroot.put("nocheck", true);
		jsonroot.put("open", true);
		jsonroot.put("icon", appInfoService.getAPP_BASE_NAME() + "/images/icons/icon042a1.gif");
		if (!Lang.isEmpty(sqlrole)) {
			rolelist = sysRoleService.listByCnd(sqlrole);
			for (int j = 0; j < rolelist.size(); j++) {
				Sys_role roleobj = rolelist.get(j);
				Map<String, Object> jsonobj = new HashMap<String, Object>();
				jsonobj.put("id", String.valueOf(roleobj.getId()));
				jsonobj.put("pId", "");
				jsonobj.put("name", roleobj.getName());
				if (roleobj.getId() == 1) {
					jsonobj.put("checked", true);
				} else {
					jsonobj.put("checked", false);
				}
				jsonobj.put("url", "javascript:list(\"" + roleobj.getId() + "\")");
				jsonobj.put("target", "_self");
				array.add(jsonobj);

			}

		}
		array.add(jsonroot);
		for (int i = 0; i < unitlist.size(); i++) { // 得到单位列表
			Sys_unit unitobj = unitlist.get(i);
			String unitid = unitobj.getId();
			sql = Cnd.wrap("unitid='" + unitid + "' order by id");
			list = sysRoleService.listByCnd(sql);
			String tunit = "";
			int t = 1;
			hm.put(unitid, unitid);
			for (int k = unitid.length(); k > 4; k = k - 4) { // 计算单位的上级（递归）
				t = k / 4 - 1;
				if (t != 0) {
					tunit = unitid.substring(0, unitid.length() - 4 * t);
				} else {
					tunit = unitid;
				}
				hm.put(tunit, tunit);
			}
			String name = "";
			for (int j = 0; j < list.size(); j++) { // 得到单位列表下的角色
				Sys_role roleobj = list.get(j);
				name = roleobj.getName();
				Map<String, Object> jsonobj = new HashMap<String, Object>();
				jsonobj.put("id", String.valueOf(roleobj.getId()));
				jsonobj.put("pId", unitid);
				jsonobj.put("name", name);
				jsonobj.put("checked", false);
				jsonobj.put("url", "javascript:list(\"" + roleobj.getId() + "\")");
				jsonobj.put("target", "_self");
				array.add(jsonobj);
			}
		}
		String temp = "";

		Map.Entry[] set = SortHashtable.getSortedHashtableByKey(hm);
		for (int i = 0; i < set.length; i++) {
			String key = set[i].getKey().toString();
			Sys_unit su = sysUnitService.detailByName(String.valueOf(key));
			if (su != null) {
				String tempUid = String.valueOf(key);
				String tempName = su.getName();
				if (tempUid.length() == 4) {
					temp = "0";
				} else {
					temp = tempUid.substring(0, tempUid.length() - 4);
				}
				Map<String, Object> jsonobj = new HashMap<String, Object>();
				jsonobj.put("id", tempUid);
				jsonobj.put("pId", temp);
				jsonobj.put("name", tempName.length() > 12 ? tempName.substring(0, 12) + ".." : tempName);
				jsonobj.put("isParent", true);
				jsonobj.put("open", true);
				jsonobj.put("nocheck", true);
				jsonobj.put("icon", appInfoService.getAPP_BASE_NAME() + "/images/icons/icon042a1.gif");
				array.add(jsonobj);
			}
		}
		req.setAttribute("str", Json.toJson(array));
	}

	@At
	@Ok("raw")
	public boolean ajaxname(@Param("loginname") String loginname) {
		int res = sysUserService.getRowCount(Cnd.where("loginname", "=", loginname));
		if (res > 0) {
			return true;
		}
		return false;
	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") Sys_user user, @Param("checkids") String checkids, HttpServletRequest req) {
		try {
			String[] ids = StringUtils.split(checkids, ",");
			String salt ="";// DecodeUtil.getSalt(6);
			user.setPassword(Lang.digest("MD5", Strings.sNull(user.getPassword()).getBytes(), Strings.sNull(salt).getBytes(), 3));
			user.setSalt(salt);
			user.setLoginTime(Times.now());
			if (sysUserService.insert(user)) {
				/*for (int i = 0; i < ids.length && (!"".equals(ids[0])); i++) {
					Sys_user_role syr = new Sys_user_role();
					syr.setUserid(user.getUserid());
					syr.setRoleid(NumberUtils.toInt(Strings.sNull(ids[i])));
					sysUserRoleService.insert(syr);
				}*/
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@At
	@Ok("vm:template.private.sys.userUpdate")
	public void toupdate(@Attr(Webs.ME) Sys_user user,@Param("userid") long userid, HttpSession session, HttpServletRequest req) {
		/*Sys_user obj = sysUserService.fetch(userid);
		Sys_unit unit = sysUnitService.detailByName(obj.getUnitid());
		req.setAttribute("obj", obj);
		req.setAttribute("unit", unit);
		obj.setPassword(Lang.digest("MD5", Strings.sNull(obj.getPassword()).getBytes(), Strings.sNull(obj.getSalt()).getBytes(), 3));
		boolean self = false;
		if (user.getLoginname().equals(obj.getLoginname())) {
			self = true;
			req.setAttribute("self", self);
		}
		Hashtable<String, String> hashrole = new Hashtable<String, String>();
		Sql sql = Sqls.create("select roleid,'wiz' from sys_user_role where userid = '" + obj.getUserid() + "'");
		hashrole = sysUserRoleService.getHTable(sql);
		LinkedList<Sys_role> list = null;
		LinkedList<Sys_role> rolelist = null;
		LinkedList<Sys_unit> unitlist = null;
		int sysrole = 0;
		Condition unitc;
		Condition rolec;
		if (user.getSysrole()) // 判断是否为系统管理员角色
		{
			unitc = Cnd.wrap("id in (select unitid from sys_role ) order by location,id asc");
			rolec = Cnd.wrap("unitid is null or unitid='' order by location,id asc");
			sysrole = 1;
		} else {
			unitc = Cnd.wrap("id in (select unitid from sys_role where unitid like '" + user.getUnitid() + "%') order by location,id asc");
			rolec = Cnd.wrap("(unitid is null or unitid='') and id in (select roleid from sys_user_role where userid='" + user.getUserid() + "') order by location,id asc");
		}
		unitlist = (LinkedList) sysUnitService.listByCnd(unitc);
		req.setAttribute("sysrole", sysrole);
		Hashtable<String, String> hm = new Hashtable<String, String>();
		List array = new ArrayList();
		Map<String, Object> jsonroot = new HashMap<String, Object>();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "角色列表");
		jsonroot.put("checked", false);
		jsonroot.put("nocheck", true);
		jsonroot.put("open", true);
		jsonroot.put("icon", appInfoService.getAPP_BASE_NAME() + "/images/icons/icon042a1.gif");
		if (!Lang.isEmpty(rolec)) {
			rolelist = (LinkedList<Sys_role>) sysRoleService.listByCnd(rolec);
			for (int j = 0; j < rolelist.size(); j++) {
				Sys_role roleobj = rolelist.get(j);
				Map<String, Object> jsonobj = new HashMap<String, Object>();
				jsonobj.put("id", String.valueOf(roleobj.getId()));
				jsonobj.put("pId", "");
				jsonobj.put("name", roleobj.getName());
				if (StringUtils.isNotBlank(Strings.sNull(hashrole.get(String.valueOf(roleobj.getId()))))) {
					if (self) {
						jsonobj.put("chkDisabled", true);
					}
					jsonobj.put("checked", true);
				} else {
					jsonobj.put("checked", false);
				}
				array.add(jsonobj);

			}
		}
		array.add(jsonroot);
		for (int i = 0; i < unitlist.size(); i++) { // 得到单位列表
			Sys_unit unitobj = unitlist.get(i);
			String unitid = unitobj.getId();
			Condition c = Cnd.wrap("unitid='" + unitid + "' order by id");
			list = (LinkedList) sysRoleService.listByCnd(c);
			String tunit = "";
			int t = 1;
			hm.put(unitid, unitid);
			for (int k = unitid.length(); k > 4; k = k - 4) { // 计算单位的上级（递归）
				t = k / 4 - 1;
				if (t != 0) {
					tunit = unitid.substring(0, unitid.length() - 4 * t);
				} else {
					tunit = unitid;
				}
				hm.put(tunit, tunit);
			}
			String name = "";
			for (int j = 0; j < list.size(); j++) { // 得到单位列表下的角色
				Sys_role roleobj = list.get(j);
				name = roleobj.getName();
				Map<String, Object> jsonobj = new HashMap<String, Object>();
				jsonobj.put("id", String.valueOf(roleobj.getId()));
				jsonobj.put("pId", unitid);
				jsonobj.put("name", name);
				if (!"".equals(Strings.sNull(hashrole.get(String.valueOf(roleobj.getId()))))) {
					jsonobj.put("checked", true);
				} else {
					jsonobj.put("checked", false);
				}
				array.add(jsonobj);
			}
		}
		String temp = "";
		Map.Entry[] set = SortHashtable.getSortedHashtableByKey(hm);
		for (int i = 0; i < set.length; i++) {
			String key = set[i].getKey().toString();
			Sys_unit su = sysUnitService.detailByName(String.valueOf(key));
			if (su != null) {
				String tempUid = String.valueOf(key);
				String tempName = su.getName();
				if (tempUid.length() == 4) {
					temp = "0";
				} else {
					temp = tempUid.substring(0, tempUid.length() - 4);
				}
				Map<String, Object> jsonobj = new HashMap<String, Object>();
				jsonobj.put("id", tempUid);
				jsonobj.put("pId", temp);
				jsonobj.put("name", tempName.length() > 12 ? tempName.substring(0, 12) + ".." : tempName);
				jsonobj.put("isParent", true);
				jsonobj.put("open", true);
				jsonobj.put("nocheck", true);
				jsonobj.put("icon", appInfoService.getAPP_BASE_NAME() + "/images/icons/icon042a1.gif");
				array.add(jsonobj);
			}
		}
		req.setAttribute("str", Json.toJson(array));*/
	}

	@At
	public boolean update(@Param("..") Sys_user user, @Param("checkids") String checkids[], @Param("oldloginname") String oldloginname) {
		boolean result = false;
		try {
			String salt ="";// DecodeUtil.getSalt(6);
			if (!Strings.isBlank(user.getPassword())) {
				user.setPassword(Lang.digest("MD5", Strings.sNull(user.getPassword()).getBytes(), Strings.sNull(salt).getBytes(), 3));
				user.setSalt(salt);
			}
			user.setLoginTime(Times.now());
			result = sysUserService.update(user);
			if (result) {
				/*sysUserRoleService.delete("sys_user_role", Cnd.where("userid", "=", user.getUserid()));
				for (int i = 0; i < checkids.length && (!"".equals(checkids[0])); i++) {
					Sys_user_role syr = new Sys_user_role();
					syr.setUserid(user.getUserid());
					syr.setRoleid(NumberUtils.toInt(Strings.sNull(checkids[i])));
					sysUserRoleService.insert(syr);
				}*/
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@At
	public boolean delete(@Param("ids") String[] ids) {
		boolean result = false;
		result = sysUserService.deleteByIds(ids);
		if (result) {
			Sql sql = Sqls.create("delete from sys_user_role where userid=@userid");
			for (int i = 0; i < ids.length; i++) {
				sql.params().set("userid", NumberUtils.toLong(Strings.sNull(ids[i])));
				sql.addBatch();
			}
			sysUserService.exeUpdateBySql(sql);
		}
		return result;

	}

	@At
	public boolean lock(@Param("ids") String[] ids) {
		boolean result = true;
		for (int i = 0; i < ids.length; i++) {
			sysUserService.update(Chain.make("state", 1), Cnd.where("userid", "=", NumberUtils.toLong(Strings.sNull(ids[i]))));
		}
		return result;
	}

	@At
	public boolean unlock(@Param("ids") String[] ids) {
		boolean result = true;
		for (int i = 0; i < ids.length; i++) {
			sysUserService.update(Chain.make("state", 0), Cnd.where("userid", "=", NumberUtils.toLong(Strings.sNull(ids[i]))));
		}
		return result;

	}

	/**
	 * 个人信息
	 */
	@At
	@Ok("vm:template.private.sys.userInfo")
	public Sys_user info(@Attr(Webs.ME) Sys_user user) {
		return sysUserService.fetch(user.getUserid());
	}

	/**
	 * 更新个人信息
	 */
	@At
	@Ok("raw")
	public String updateInfo(@Param("..") Sys_user user, @Param("userid") Long userid, @Param("password2") String pass, @Param("oldpassword") String oldpassword) {
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        String salt = rng.nextBytes().toBase64();
        String hashedPasswordBase64 = new Sha256Hash(oldpassword, salt, 1024).toBase64();
        Sys_user olduser = sysUserService.fetch(userid);
		boolean relogin = false;
		if (Lang.digest("MD5", Strings.sNull(oldpassword).getBytes(), Strings.sNull(olduser.getSalt()).getBytes(), 3).equals(olduser.getPassword())) {
			if (!"".equals(pass)) {
				relogin = true;
				olduser.setPassword(Lang.digest("MD5", Strings.sNull(pass).getBytes(), salt.getBytes(), 3));
				olduser.setSalt(salt);
			}
			if (!olduser.getLoginname().equals(user.getLoginname())) {
				relogin = true;
			}
			olduser.setLoginname(user.getLoginname());
			olduser.setRealname(user.getRealname());
			olduser.setDescript(user.getDescript());
			olduser.setPozition(user.getPozition());
			olduser.setAddress(user.getAddress());
			olduser.setTelephone(user.getTelephone());
			olduser.setMobile(user.getMobile());
			olduser.setEmail(user.getEmail());
			boolean up = sysUserService.update(olduser);
			if (up && relogin) {
				return "relogin";
			} else if (up) {
				return "true";
			} else {
				return "false";
			}
		} else {
			return "error";
		}
	}
}