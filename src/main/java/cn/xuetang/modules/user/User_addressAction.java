package cn.xuetang.modules.user;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.modules.user.bean.User_address;
import cn.xuetang.service.UserAddressService;

/**
 * @author Wizzer
 * @time 2014-04-01 10:11:06
 * 
 */
@IocBean
@At("/private/user/user_address")
public class User_addressAction {

	@Inject
	protected UserAddressService userAddressService;

	@At("/index")
	@Ok("vm:template.private.user.User_address")
	public void index() {

	}

	@At
	@Ok("vm:template.private.user.User_addressAdd")
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
	@Ok("vm:template.private.user.User_addressUpdate")
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