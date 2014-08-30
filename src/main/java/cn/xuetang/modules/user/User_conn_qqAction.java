package cn.xuetang.modules.user;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.filter.UserLoginFilter;
import cn.xuetang.modules.user.bean.User_conn_qq;
import cn.xuetang.service.UserConnQQService;

/**
 * @author Wizzer
 * @time 2014-04-01 10:11:06
 * 
 */
@IocBean
@At("/private/user/user_conn_qq")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class User_conn_qqAction {

	@Inject
	private UserConnQQService userConnQQService;

	@At("/index")
	@Ok("->:/private/user/User_conn_qq.html")
	public void index() {

	}

	@At
	@Ok("->:/private/user/User_conn_qqAdd.html")
	public void toadd() {

	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") User_conn_qq user_conn_qq) {
		userConnQQService.insert(user_conn_qq);
		return true;
	}

	// @At
	// @Ok("raw")
	// public int add(@Param("..") User_conn_qq user_conn_qq) {
	// return daoCtl.addT(dao,user_conn_qq).getId();
	// }

	@At
	@Ok("json")
	public User_conn_qq view(@Param("id") int id) {
		return userConnQQService.fetch(id);
	}

	@At
	@Ok("->:/private/user/User_conn_qqUpdate.html")
	public User_conn_qq toupdate(@Param("id") int id, HttpServletRequest req) {
		return userConnQQService.fetch(id);// html:obj
	}

	@At
	public boolean update(@Param("..") User_conn_qq user_conn_qq) {
		return userConnQQService.update(user_conn_qq);
	}

	@At
	public boolean delete(@Param("id") int id) {
		return userConnQQService.delete(id) > 0;
	}

	@At
	public boolean deleteIds(@Param("ids") String[] ids) {
		return userConnQQService.deleteByIds(ids);
	}

	@At
	@Ok("raw")
	public String list(@Param("page") int curPage, @Param("rows") int pageSize) {
		return userConnQQService.listPageJson(curPage, pageSize);
	}

}