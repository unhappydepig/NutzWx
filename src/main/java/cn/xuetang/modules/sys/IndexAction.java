package cn.xuetang.modules.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.web.Webs;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.service.sys.SysResourceService;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 */
@IocBean
@At("/private")
@RequiresAuthentication
public class IndexAction extends BaseAction {
	@Inject
	protected Dao dao;

	@Inject
	private SysResourceService sysResourceService;
	
	@At
	public void dolock(HttpServletRequest req, HttpSession session) {
		session.setAttribute("validate", "openLockWindow();");
	}

	@At
	@Ok("vm:template.private.lock")
	public void lock() {

	}

	@At
	@Ok("raw")
	public boolean reload(@Param("resid") String resid, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if (daoCtl.update(dao, Sys_user.class, Chain.make("loginresid", resid), Cnd.where("userid", "=", user.getUserid()))) {
			user.setLoginresid(resid);
			session.setAttribute("userSession", user);
			return true;
		}
		return false;
	}

	@At
	@Ok("raw")
	public String dounlock(@Param("password") String password, HttpServletRequest req, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if (!Lang.digest("MD5", Strings.sNull(password).getBytes(), Strings.sNull(user.getSalt()).getBytes(), 3).equals(user.getPassword())) {
			return "密码不正确，请输入当前登陆用户密码！";
		} else {
			session.setAttribute("validate", "");
			return "true";
		}

	}

	@At
	@Ok("vm:template.private.index")
	public Object index(@Attr(Webs.ME) Sys_user user,HttpServletRequest req) {
		/*Sql sql = Sqls.create("select * from sys_role where id in(select roleid from sys_user_role where userid=@userid)");
		sql.params().set("userid", user.getUserid());
		List<Map> rolelist = daoCtl.list(dao, sql);
		// 判断是否为系统管理员角色
		List<Integer> rolelist1 = new ArrayList<Integer>();
		List<Integer> plist = new ArrayList<Integer>();
		for (Map map : rolelist) {
			rolelist1.add(NumberUtils.toInt(Strings.sNull(map.get("id"))));
			int pid = NumberUtils.toInt(Strings.sNull(map.get("pid")));
			if (!plist.contains(pid))
				plist.add(pid);
		}
		if (rolelist1.contains(2)) {
			user.setSysrole(true);
		} else {
			user.setSysrole(false);
		}
		user.setRolelist(rolelist1);
		user.setProlist(plist);
		// 将用户所属角色塞入内存
		Sql sql1 = Sqls.create("select distinct resourceid from sys_role_resource where ( roleid in(select roleid from sys_user_role where userid=@userid) or roleid=1) and resourceid not in(select id from sys_resource where state=1)");
		sql1.params().set("userid", user.getUserid());
		user.setReslist(daoCtl.getStrRowValues(dao, sql1));
		// 获取用户一级资源菜单
		List<Sys_resource> moduleslist = daoCtl.list(dao, Sys_resource.class, Cnd.where("id", "like", "____").and("id", "in", user.getReslist()).asc("location"));
		req.setAttribute("moduleslist", moduleslist);
		String resid = Strings.sNull(user.getLoginresid());
		if ("".equals(resid)) {
			for (Sys_resource res : moduleslist) {
				resid = res.getId();
				break;
			}
		}
		// 获取用户二级资源菜单
		List<Sys_resource> modulessublist = daoCtl.list(dao, Sys_resource.class, Cnd.where("id", "like", resid + "____").and("id", "in", user.getReslist()).asc("location"));
		req.setAttribute("modulessublist", modulessublist);
		req.setAttribute("resid", resid);
		// 获取用户资源button HashMap
		List<List<String>> reslist = daoCtl.getMulRowValue(dao, Sqls.create("SELECT a.url,b.button FROM sys_resource a,sys_role_resource b WHERE a.ID=b.RESOURCEID " + " AND (b.button<>'' or b.button is not null) AND ( b.roleid IN(SELECT roleid FROM sys_user_role WHERE userid=" + user.getUserid() + ") OR roleid=1) " + " AND b.resourceid NOT IN(SELECT id FROM sys_resource WHERE state=1)"));
		Hashtable<String, String> btnmap = new Hashtable<String, String>();
		for (List<String> obj : reslist) {
			String key = Strings.sNull(obj.get(0));
			String value = Strings.sNull(btnmap.get(key)) + Strings.sNull(obj.get(1));
			btnmap.put(key, value);
		}
		user.setBtnmap(btnmap);*/
		req.setAttribute("validate", true);
		return sysResourceService.listByCnd(null);
	}

	@At
	@Ok("vm:template.private.left")
	public Object left(@Attr(Webs.ME) Sys_user user,@Param("sys_menuid") String sys_menuid, HttpServletRequest req) {
		return sysResourceService.listByCnd(null);
	}

	@At
	@Ok("vm:template.private.welcome")
	public void welcome() {

	}

}
