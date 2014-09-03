package cn.xuetang.common.shiro.realm;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.util.ByteSource;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Lang;
import org.nutz.mvc.Mvcs;

import cn.xuetang.common.exception.IncorrectCaptchaException;
import cn.xuetang.modules.sys.bean.Sys_user;

import com.octo.captcha.service.image.ImageCaptchaService;

public class NutDaoRealm extends AbstractNutAuthoRealm {

	private ImageCaptchaService imageCaptchaService;
	public NutDaoRealm() {
		setAuthenticationTokenClass(CaptchaUsernamePasswordToken.class);
	}
	protected ImageCaptchaService getImageCaptchaService() {
		if (Lang.isEmpty(imageCaptchaService)) {
			Ioc ioc = Mvcs.getIoc();
			imageCaptchaService = ioc.get(ImageCaptchaService.class);
		}
		return imageCaptchaService;
	}
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws DisabledAccountException {
		CaptchaUsernamePasswordToken authcToken = (CaptchaUsernamePasswordToken) token;
		boolean isCaptchaBlank = StringUtils.isBlank(authcToken.getCaptcha());
		if (isCaptchaBlank) {
			throw Lang.makeThrow(IncorrectCaptchaException.class, "common.error.account.captcha");
		}
		String sessionID = SecurityUtils.getSubject().getSession(true).getId().toString();
		boolean isRight = getImageCaptchaService().validateResponseForID(sessionID, StringUtils.upperCase(authcToken.getCaptcha()));
		if (!isRight) {
			throw Lang.makeThrow(IncorrectCaptchaException.class, "common.error.account.captcha");
		}
		String accountName = authcToken.getUsername();
		if (StringUtils.isBlank(accountName)) {
			throw Lang.makeThrow(AuthenticationException.class, "common.error.account.null");
		}
		Sys_user user = getUserService().fetchByName(authcToken.getUsername());
		if (Lang.isEmpty(user)) {
			throw Lang.makeThrow(UnknownAccountException.class, "common.error.account.not.found");
		}
		if (user.isLocked()) {
			throw Lang.makeThrow(LockedAccountException.class, "common.error.account.locked");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		ByteSource salt = ByteSource.Util.bytes(user.getSalt());
		info.setCredentialsSalt(salt);
		Mvcs.getHttpSession().setAttribute(org.nutz.web.Webs.ME, user);
		return info;
	}
}
