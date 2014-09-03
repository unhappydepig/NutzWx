package cn.xuetang.modules.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.xuetang.modules.sys.bean.Sys_resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.web.Webs;

import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.modules.user.bean.PermissionCategory;
import cn.xuetang.service.sys.SysResourceService;
import cn.xuetang.service.sys.SysUserService;
import cn.xuetang.service.user.PermissionCategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 */
@IocBean
@At("/private")
@RequiresAuthentication
public class IndexAction {
	@Inject
	protected SysUserService sysUserService;

	@Inject
	private PermissionCategoryService permissionCategoryService;

	@At
	@Ok("raw")
	public boolean reload(@Attr(Webs.ME) Sys_user user, @Param("resid") String resid, HttpSession session) {
		sysUserService.update(Chain.make("loginresid", resid), Cnd.where("userid", "=", user.getUserid()));
		user.setLoginresid(resid);
		return true;
	}

	@At
	@Ok("vm:template.private.index")
	public List<PermissionCategory> index(@Attr(Webs.ME) Sys_user user) {
		return permissionCategoryService.list();
	}

}
