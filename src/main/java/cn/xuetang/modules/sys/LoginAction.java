package cn.xuetang.modules.sys;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.util.OnlineUtil;
import cn.xuetang.shiro.realm.CaptchaUsernamePasswordToken;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 */
@IocBean
@At("/private")
@Filters({ @By(type = GlobalsFilter.class) })
public class LoginAction {
	private final static Log log = Logs.get();
	@Inject
	protected Dao dao;

	@At("/doLogin")
	@Filters(@By(type = cn.xuetang.common.filter.CaptchaFormAuthenticationFilter.class))
	public String login(@Attr("loginToken") CaptchaUsernamePasswordToken token) {
		try {
			Subject subject = SecurityUtils.getSubject();
			ThreadContext.bind(subject);
			subject.login(token);
			return "true";
		} catch (LockedAccountException e) {
			return "用户被禁止登陆。请联系管理员！";
		} catch (AuthenticationException e) {
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@At
	@Ok(">>:/private/login")
	public void logout(HttpSession session) {
		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.logout();
		} catch (SessionException ise) {
			log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
		} catch (Exception e) {
			log.debug("登出发生错误", e);
		}
	}

	@At
	@Ok("vm:template.private.login")
	@RequiresGuest
	public void login() {

	}

	@At
	@Ok("raw")
	@RequiresAuthentication
	public int Online() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.isAuthenticated()) {
			return -2;
		}
		OnlineUtil.addUser(currentUser.getPrincipal().getClass(), String.valueOf(1));
		return OnlineUtil.getOnlineCount(String.valueOf(1));
	}

}
