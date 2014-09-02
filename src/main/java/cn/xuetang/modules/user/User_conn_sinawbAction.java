package cn.xuetang.modules.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.modules.user.bean.User_conn_sinawb;
import cn.xuetang.service.user.UserConnSinawbService;

/**
 * @author Wizzer
 * @time 2014-04-01 10:11:06
 * 
 */
@IocBean
@At("/private/user/user_conn_sinawb")
public class User_conn_sinawbAction {

	@Inject
	private UserConnSinawbService userConnSinawbService;

	@At("/index")
	@Ok("vm:template.private.user.User_conn_sinawb")
	public void index(HttpSession session, HttpServletRequest req) {

	}

	@At
	@Ok("vm:template.private.user.User_conn_sinawbAdd")
	public void toadd() {

	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") User_conn_sinawb user_conn_sinawb) {
		return userConnSinawbService.insert(user_conn_sinawb);
	}

	// @At
	// @Ok("raw")
	// public int add(@Param("..") User_conn_sinawb user_conn_sinawb) {
	// return daoCtl.addT(dao,user_conn_sinawb).getId();
	// }

	@At
	@Ok("json")
	public User_conn_sinawb view(@Param("id") int id) {
		return userConnSinawbService.fetch(id);
	}

	@At
	@Ok("vm:template.private.user.User_conn_sinawbUpdate")
	public User_conn_sinawb toupdate(@Param("id") int id, HttpServletRequest req) {
		return userConnSinawbService.fetch(id);// html:obj
	}

	@At
	public boolean update(@Param("..") User_conn_sinawb user_conn_sinawb) {
		return userConnSinawbService.update(user_conn_sinawb);
	}

	@At
	public boolean delete(@Param("id") int id) {
		return userConnSinawbService.delete(id) > 0;
	}

	@At
	public boolean deleteIds(@Param("ids") String[] ids) {
		return userConnSinawbService.deleteByIds(ids);
	}

	@At
	@Ok("raw")
	public String list(@Param("page") int curPage, @Param("rows") int pageSize) {
		Criteria cri = Cnd.cri();
		cri.where().and("1", "=", 1);
		cri.getOrderBy().desc("id");
		return userConnSinawbService.listPageJson(curPage, pageSize, cri);
	}

}