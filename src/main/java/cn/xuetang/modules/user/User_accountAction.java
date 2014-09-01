package cn.xuetang.modules.user;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.modules.user.bean.User_account;
import cn.xuetang.service.UserAccountService;

/**
 * @author Wizzer
 * @time 2014-04-01 10:11:06
 * 
 */
@IocBean
@At("/private/user/user_account")
public class User_accountAction {

	@Inject
	protected UserAccountService userAccountService;

	@At("/index")
	@Ok("vm:template.private.user.User_account")
	public void index() {

	}

	@At
	@Ok("vm:template.private.user.User_accountAdd")
	public void toadd() {

	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") User_account user_account) {
		userAccountService.insert(user_account);
		return true;
	}

	// @At
	// @Ok("raw")
	// public int add(@Param("..") User_account user_account) {
	// return daoCtl.addT(dao,user_account).getUid();
	// }

	@At
	@Ok("json")
	public User_account view(@Param("uid") int uid) {
		return userAccountService.getUserAccountByUid(uid);
	}

	@At
	@Ok("vm:template.private.user.User_accountUpdate")
	public User_account toupdate(@Param("uid") int uid) {
		return userAccountService.getUserAccountByUid(uid);
	}

	@At
	public boolean update(@Param("..") User_account user_account) {
		return userAccountService.update(user_account);
	}

	@At
	public boolean delete(@Param("uid") int uid) {
		return userAccountService.deleteUserAccountByUid(uid);
	}

	@At
	public boolean deleteIds(@Param("ids") String[] ids) {
		return userAccountService.deleteByIds(ids);
	}

	@At
	@Ok("raw")
	public String list(@Param("page") int curPage, @Param("rows") int pageSize) {
		return userAccountService.listPageJson(curPage, pageSize);
	}

}