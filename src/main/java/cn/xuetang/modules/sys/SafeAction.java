package cn.xuetang.modules.sys;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.util.StringUtil;
import cn.xuetang.modules.sys.bean.Sys_safeconfig;
import cn.xuetang.service.sys.SysSafeConfigService;

/**
 * @author Wizzer.cn
 * @time 2012-9-14 上午11:45:52 安全管理模块
 * 
 */
@IocBean
@At("/private/sys/safe")
public class SafeAction {
	@Inject
	private SysSafeConfigService sysSafeConfigService;

	/***
	 * 查询全部
	 * */
	@At
	@Ok("vm:template.private.sys.safe")
	public void list(HttpServletRequest req) {
		int sel = 0;
		Sys_safeconfig ip_safe = sysSafeConfigService.detailByName("type", "1");// 允许
		Sys_safeconfig ip_refuse = sysSafeConfigService.detailByName("type", "0");// 拒绝
		if (ip_safe.getState() == 0) {
			sel = 1;
		} else if (ip_refuse.getState() == 0) {
			sel = 0;
		}
		req.setAttribute("sel", sel);
		req.setAttribute("ip_safe", ip_safe);
		req.setAttribute("ip_refuse", ip_refuse);
	}

	/**
	 * 更新
	 * */
	@At
	@Ok("raw")
	public boolean update(@Param("safetype") int cursafetype, @Param("ip_safe") String ip_safe, @Param("ip_refuse") String ip_refuse) {
		if (cursafetype == 0) {
			sysSafeConfigService.update(Chain.make("note", StringUtil.getMysqlSaveString(ip_refuse, "''")).add("state", "0"), Cnd.where("type", "=", "0"));
			sysSafeConfigService.update(Chain.make("note", StringUtil.getMysqlSaveString(ip_safe, "''")).add("state", "1"), Cnd.where("type", "=", "1"));
		} else {
			sysSafeConfigService.update(Chain.make("note", StringUtil.getMysqlSaveString(ip_refuse, "''")).add("state", "1"), Cnd.where("type", "=", "0"));
			sysSafeConfigService.update(Chain.make("note", StringUtil.getMysqlSaveString(ip_safe, "''")).add("state", "0"), Cnd.where("type", "=", "1"));
		}
		return true;
	}
}