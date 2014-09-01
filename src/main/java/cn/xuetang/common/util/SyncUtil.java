package cn.xuetang.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * 前台负载均衡部署时，同步更新全局参数 Created by Wizzer on 14-5-21.
 */
public class SyncUtil {
	public static void sendMsg(String type,String urls,String key) {
		/*String urls = Strings.sNull(AppInfoService.SYS_CONFIG.get("sync_url"));
		String key = Strings.sNull(AppInfoService.SYS_CONFIG.get("sync_key"));*/
		String[] url = StringUtils.split(urls, ",");
		for (int i = 0; i < url.length; i++) {
			SendUtil sendUtil = new SendUtil(type, key, url[i]);
			sendUtil.start();
		}
	}
}
