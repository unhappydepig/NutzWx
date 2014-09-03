package cn.xuetang.modules.sys;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

@IocBean
public class CaptchaModule {

	@At
	@Ok("captcha:captcha")
	@Filters
	public void captcha() {
	}
}
