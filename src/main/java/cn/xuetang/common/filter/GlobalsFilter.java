package cn.xuetang.common.filter;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

import cn.xuetang.service.sys.AppInfoService;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */
@IocBean
public class GlobalsFilter implements ActionFilter {
	@Inject
	protected AppInfoService appInfoService;

	@Override
	public View match(ActionContext context) {
		HttpServletRequest req = context.getRequest();
		req.setAttribute("app_name", appInfoService.getAPP_NAME());
		req.setAttribute("app_base_name", appInfoService.getAPP_BASE_NAME());
		req.setAttribute("app_base_path", appInfoService.getAPP_BASE_PATH());
		return null;
	}

}
