package cn.xuetang.modules.user;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.filter.UserLoginFilter;
import cn.xuetang.modules.user.bean.User_conn_sinawb;

/**
 * @author Wizzer
 * @time 2014-04-01 10:11:06
 * 
 */
@IocBean
@At("/private/user/user_conn_sinawb")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class User_conn_sinawbAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("/index")
	@Ok("vm:template.private.user.User_conn_sinawb")
	public void index(HttpSession session,HttpServletRequest req) {
	
	}
	
	@At
	@Ok("vm:template.private.user.User_conn_sinawbAdd")
	public void toadd() {
	
	}
	
	@At
	@Ok("raw")
	public boolean add(@Param("..") User_conn_sinawb user_conn_sinawb) {
		return daoCtl.add(dao,user_conn_sinawb);
	}
	
	//@At
	//@Ok("raw")
	//public int add(@Param("..") User_conn_sinawb user_conn_sinawb) {
	//	return daoCtl.addT(dao,user_conn_sinawb).getId();
	//}
	
	@At
	@Ok("json")
	public User_conn_sinawb view(@Param("id") int id) {
		return daoCtl.detailById(dao,User_conn_sinawb.class, id);
	}
	
	@At
	@Ok("vm:template.private.user.User_conn_sinawbUpdate")
	public User_conn_sinawb toupdate(@Param("id") int id, HttpServletRequest req) {
		return daoCtl.detailById(dao, User_conn_sinawb.class, id);//html:obj
	}
	
	@At
	public boolean update(@Param("..") User_conn_sinawb user_conn_sinawb) {
		return daoCtl.update(dao, user_conn_sinawb);
	}
	
	@At
	public boolean delete(@Param("id") int id) {
		return daoCtl.deleteById(dao, User_conn_sinawb.class, id);
	}
	
	@At
	public boolean deleteIds(@Param("ids") String ids) {
		return daoCtl.deleteByIds(dao, User_conn_sinawb.class, StringUtils.split(ids,","));
	}
	
	@At
	@Ok("raw")
	public String list(@Param("page") int curPage, @Param("rows") int pageSize){
		Criteria cri = Cnd.cri();
		cri.where().and("1","=",1);
		cri.getOrderBy().desc("id");
		return daoCtl.listPageJson(dao, User_conn_sinawb.class, curPage, pageSize, cri);
	}

}