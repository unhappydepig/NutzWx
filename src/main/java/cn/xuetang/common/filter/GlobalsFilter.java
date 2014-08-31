package cn.xuetang.common.filter;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

import cn.xuetang.service.AppInfoService;

/**
 * @author Wizzer.cn
 * @time   2012-9-13 上午10:54:04
 *
 */
public class GlobalsFilter implements ActionFilter{

	@Override
	public View match(ActionContext context) {
		context.getRequest().setAttribute("app_name", AppInfoService.APP_NAME);
		context.getRequest().setAttribute("app_base_name", AppInfoService.APP_BASE_NAME);
		context.getRequest().setAttribute("app_base_path", AppInfoService.APP_BASE_PATH);
		return null;
	}

}
