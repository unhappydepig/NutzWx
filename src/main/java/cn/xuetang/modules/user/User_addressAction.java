package cn.xuetang.modules.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.nutz.dao.Cnd;
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
import cn.xuetang.modules.user.bean.User_address;
import cn.xuetang.service.UserAddressService;

/**
 * @author Wizzer
 * @time 2014-04-01 10:11:06
 * 
 */
@IocBean
@At("/private/user/user_address")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class User_addressAction {

	@Inject
	protected UserAddressService userAddressService;

	@At("/index")
	@Ok("->:/private/user/User_address.html")
	public void index() {

	}

	@At
	@Ok("->:/private/user/User_addressAdd.html")
	public void toadd() {

	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") User_address user_address) {
		userAddressService.insert(user_address);
		return true;
	}

	// @At
	// @Ok("raw")
	// public int add(@Param("..") User_address user_address) {
	// return daoCtl.addT(dao,user_address).getId();
	// }

	@At
	@Ok("json")
	public User_address view(@Param("id") int id) {
		return userAddressService.fetch(id);
	}

	@At
	@Ok("->:/private/user/User_addressUpdate.html")
	public User_address toupdate(@Param("id") int id) {
		return userAddressService.fetch(id);// html:obj
	}

	@At
	public boolean update(@Param("..") User_address user_address) {
		return userAddressService.update(user_address);
	}

	@At
	public boolean delete(@Param("id") int id) {
		return userAddressService.delete(id)>0;
	}

	@At
	public boolean deleteIds(@Param("ids") String[] ids) {
		return userAddressService.deleteByIds(ids);
	}

	@At
	@Ok("raw")
	public String list(@Param("page") int curPage, @Param("rows") int pageSize) {
		return userAddressService.listPageJson(curPage, pageSize);
	}

}