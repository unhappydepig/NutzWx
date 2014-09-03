package cn.xuetang.modules.sys;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

import cn.xuetang.common.config.Message;
import cn.xuetang.common.exception.IncorrectCaptchaException;
import cn.xuetang.common.shiro.realm.CaptchaUsernamePasswordToken;
import cn.xuetang.common.util.OnlineUtil;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 */
@IocBean
@At("/private")
public class LoginAction {

	private final static Log log = Logs.get();

	@At("/doLogin")
	@Filters(@By(type = cn.xuetang.common.filter.CaptchaFormAuthenticationFilter.class))
	@Ok("json")
	public Message login(@Attr("loginToken") CaptchaUsernamePasswordToken token, HttpServletRequest req) {
		try {
			Subject subject = SecurityUtils.getSubject();
			ThreadContext.bind(subject);
			subject.login(token);
			return Message.success("common.success", req);
		} catch (LockedAccountException e) {
			return Message.error(e.getMessage(), req);
		} catch (UnknownAccountException e) {
			return Message.error(e.getMessage(), req);
		} catch (IncorrectCaptchaException e) {
			return Message.error(e.getMessage(), req);
		}catch(IncorrectCredentialsException e){
			return Message.error("common.error.login.password", req);
		} catch (AuthenticationException e) {
			return Message.error(e.getMessage(), req);
		} catch (Exception e) {
			return Message.error("common.error.login", req);
		}
	}

	@At
	@Ok(">>:/private/login")
	@RequiresAuthentication
	public void logout() {
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

}
