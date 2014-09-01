package cn.xuetang.common.filter;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.View;
import org.nutz.mvc.view.DefaultViewMaker;
import org.nutz.mvc.view.ServerRedirectView;
import org.nutz.mvc.view.VoidView;

/**
 * 在入口方法中应用Shiro注解来进行安全过滤
 * 
 * @author wendal
 * 
 */
public class ShiroActionFilter extends GlobalsFilter {
	private static Log log = Logs.get();

	public View match(final ActionContext actionContext) {
		try {
			super.match(actionContext);
			ShiroAnnotationsAuthorizingMethodInterceptor.DEFAULT_AUTH.assertAuthorized(new MethodInvocation() {
				public Object proceed() throws Throwable {
					throw Lang.noImplement();
				}

				public Object getThis() {
					return actionContext.getModule();
				}

				public Method getMethod() {
					return actionContext.getMethod();
				}

				public Object[] getArguments() {
					return actionContext.getMethodArgs();
				}
			});
		} catch (UnauthenticatedException e) {
			e.printStackTrace();
			return whenAuthFail(actionContext, e);
		} catch (UnauthorizedException e) {
			e.printStackTrace();
			return permissionFail(actionContext, e);
		} catch (AuthorizationException e) {
			e.printStackTrace();
			return permissionFail(actionContext, e);
		}
		return null;
	}

	private View view = new ServerRedirectView("/");
	private View NOT_PERMISSION = new ServerRedirectView("/admin/common/forbit.rk");

	public ShiroActionFilter() {

	}

	public ShiroActionFilter(String view) {
		if (view.contains(":")) {
			String[] vs = view.split(":", 2);
			this.view = new DefaultViewMaker().make(null, vs[0], vs[1]);
		} else {
			this.view = new ServerRedirectView(view);
		}
	}

	private View whenAuthFail(ActionContext ctx, AuthorizationException e) {
		HttpServletRequest localHttpServletRequest = ctx.getRequest();
		HttpServletResponse localHttpServletResponse = ctx.getResponse();
		String str = localHttpServletRequest.getHeader("X-Requested-With");
		if (StringUtils.isNotBlank(str) && (str.equalsIgnoreCase("XMLHttpRequest"))) {
			localHttpServletResponse.addHeader("loginStatus", "accessDenied");
			try {
				localHttpServletResponse.sendError(403);
			} catch (IOException exception) {
				log.error(exception.getMessage());
			}
			return new VoidView();
		}
		return view;
	}

	private View permissionFail(ActionContext ctx, AuthorizationException e) {
		HttpServletRequest localHttpServletRequest = ctx.getRequest();
		HttpServletResponse localHttpServletResponse = ctx.getResponse();
		String str = localHttpServletRequest.getHeader("X-Requested-With");
		if (StringUtils.isNotBlank(str) && (str.equalsIgnoreCase("XMLHttpRequest"))) {
			localHttpServletResponse.addHeader("loginStatus", "unauthorized");
			try {
				localHttpServletResponse.sendError(403);
			} catch (IOException exception) {
				log.error(exception.getMessage());
			}
			return new VoidView();
		}
		return NOT_PERMISSION;
	}
}
