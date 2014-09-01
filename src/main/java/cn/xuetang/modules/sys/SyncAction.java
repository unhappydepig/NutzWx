package cn.xuetang.modules.sys;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.service.AppInfoService;

/**
 * Created by Wizzer on 14-5-21.
 */
@IocBean
@At("/private/sys/sync")
public class SyncAction {

	@Inject
	private AppInfoService appInfoService;

	@At("/config")
	@Ok("raw")
	public String config(@Param("key") String key, @Param("type") String type, HttpServletRequest req) {
		String mykey = Strings.sNull(appInfoService.getSYS_CONFIG().get("sync_key"));
		if (mykey.equals(key)) {
			if ("datadict".equals(type)) {
				appInfoService.InitDataDict();
			} else if ("appinfo".equals(type)) {
				appInfoService.InitAppInfo();
			} else if ("sysconfig".equals(type)) {
				appInfoService.InitSysConfig();
			}
		}
		return "sucess";
	}
}
