package cn.xuetang.modules.app;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.config.Dict;
import cn.xuetang.common.util.DateUtil;
import cn.xuetang.common.util.DecodeUtil;
import cn.xuetang.common.util.SyncUtil;
import cn.xuetang.modules.app.bean.App_info;
import cn.xuetang.service.AppInfoService;

/**
 * @author Wizzer
 * @time 2014-03-29 13:12:58
 */
@IocBean
@At("/private/app/info")
public class App_infoAction {
	@Inject
	private AppInfoService appInfoService;

	@At("")
	@Ok("vm:template.private.app.App_info")
	public void index(@Param("sys_menu") String sys_menu, HttpServletRequest req) {
		req.setAttribute("pro", appInfoService.list());
		req.setAttribute("type", appInfoService.DATA_DICT.get(Dict.APP_TYPE));
		req.setAttribute("sys_menu", sys_menu);
	}

	@At
	@Ok("vm:template.private.app.App_infoAdd")
	public void toadd(@Param("pid") int pid, HttpServletRequest req) {
		req.setAttribute("pro", appInfoService.list());
		req.setAttribute("type", appInfoService.DATA_DICT.get(Dict.APP_TYPE));
		req.setAttribute("pid", pid);

	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") App_info app_info) {
		if (appInfoService.insert(app_info)) {
			appInfoService.InitAppInfo();
			SyncUtil.sendMsg("appinfo");
			return true;
		} else
			return false;
	}

	@At
	@Ok("raw")
	public String getkey() {
		return getMyKey();
	}

	public String getMyKey() {
		String key = DecodeUtil.Encrypt("xuetang", DateUtil.getCurDateTime());
		int k = appInfoService.getRowCount(key);
		if (k > 0) {
			return getMyKey();
		} else
			return key;
	}

	@At
	@Ok("raw")
	public String getsecret() {
		return getSecret();
	}

	public String getSecret() {
		String key = DecodeUtil.Encrypt("xuetang", DateUtil.getCurDateTime()) + DecodeUtil.Encrypt("xuetang", DateUtil.getCurDateTime());
		int k = appInfoService.getRowCountBySecret(key);
		if (k > 0) {
			return getSecret();
		} else
			return key;
	}

	@At
	@Ok("json")
	public App_info view(@Param("id") int id) {
		return appInfoService.fetch(id);
	}

	@At
	@Ok("vm:template.private.app.App_infoUpdate")
	public App_info toupdate(@Param("id") int id, HttpServletRequest req) {
		req.setAttribute("pro", appInfoService.list());
		req.setAttribute("type", appInfoService.getType(Dict.APP_TYPE));
		return appInfoService.fetch(id);// html:obj
	}

	@At
	public boolean update(@Param("..") App_info app_info) {
		if (appInfoService.update(app_info)) {
			appInfoService.InitAppInfo();
			SyncUtil.sendMsg("appinfo");
			return true;
		} else
			return false;
	}

	@At
	public boolean delete(@Param("id") int id) {
		if (appInfoService.delete(id) > 0) {
			appInfoService.InitAppInfo();
			SyncUtil.sendMsg("appinfo");
			return true;
		} else
			return false;
	}

	@At
	public boolean deleteIds(@Param("ids") String[] ids) {
		if (appInfoService.deleteByIds(ids)) {
			appInfoService.InitAppInfo();
			SyncUtil.sendMsg("appinfo");
			return true;
		} else
			return false;
	}

	@At
	@Ok("raw")
	public String list(@Param("pid") int pid, @Param("page") int curPage, @Param("rows") int pageSize) {
		return appInfoService.listPageJson(curPage, pageSize);
	}
}