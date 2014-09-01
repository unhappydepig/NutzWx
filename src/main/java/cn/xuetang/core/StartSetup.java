package cn.xuetang.core;

import java.io.IOException;
import java.util.Properties;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.velocity.app.Velocity;
import org.nutz.dao.Dao;
import org.nutz.filepool.NutFilePool;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import cn.xuetang.common.task.LoadTask;
import cn.xuetang.modules.sys.bean.Sys_role;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.modules.user.bean.Permission;
import cn.xuetang.service.AppInfoService;

/**
 * 类描述： 创建人：Wizzer 联系方式：www.wizzer.cn 创建时间：2013-11-26 下午2:11:13
 * 
 * @version
 */

public class StartSetup implements Setup {
	private final Log log = Logs.get();

	@Override
	public void destroy(NutConfig config) {
		AppInfoService appServer = Mvcs.getIoc().get(AppInfoService.class);
		try {
			appServer.getSCHEDULER().shutdown();
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void init(NutConfig config) {
		try {
			Ioc ioc = Mvcs.getIoc();
			Dao dao = ioc.get(Dao.class);
			dao.create(Sys_user.class, false);
			dao.create(Sys_role.class, false);
			dao.create(Permission.class, false);
			if (!dao.exists(Sys_user.class)) {
				Sys_user defaultUser = new Sys_user();
				defaultUser.setLoginname("superadmin");
				RandomNumberGenerator rng = new SecureRandomNumberGenerator();
				String salt = rng.nextBytes().toBase64();
				String hashedPasswordBase64 = new Sha256Hash("123", salt, 1024).toBase64();
				defaultUser.setPassword(hashedPasswordBase64);
				defaultUser.setSalt(salt);
				defaultUser.setLoginTime(Times.now());
				dao.insert(defaultUser);
			}
			AppInfoService appServer = ioc.get(AppInfoService.class);
			velocityInit(config.getAppRoot());
			appServer.setAPP_BASE_PATH(Strings.sNull(config.getAppRoot()));// 项目路径
			appServer.setAPP_BASE_NAME(Strings.sNull(config.getServletContext().getContextPath()));// 部署名
			appServer.InitSysConfig();// 初始化系统参数
			appServer.InitDataDict();// 初始化数据字典
			appServer.InitAppInfo();// 初始化app接口信息
			appServer.setAPP_NAME(Strings.sNull(appServer.getSYS_CONFIG().get("app_name")));// 项目名称
			appServer.setFILE_POOL(new NutFilePool("~/tmp/myfiles", 10));// 创建一个文件夹用于下载
			// 初始化Quartz任务
			appServer.setSCHEDULER(StdSchedulerFactory.getDefaultScheduler());
			new Thread(config.getIoc().get(LoadTask.class)).start();// 定时任务
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void velocityInit(String appPath) throws IOException {
		log.info("Veloctiy引擎初始化...");
		Properties p = new Properties();
		p.setProperty("resource.loader", "file,classloader");
		p.setProperty("file.resource.loader.path", appPath);
		p.setProperty("file", "org.apache.velocity.tools.view.WebappResourceLoader");
		p.setProperty("classloader.resource.loader.class", "org.nutz.weixin.mvc.VelocityResourceLoader");
		p.setProperty("classloader.resource.loader.path", appPath);
		p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		p.setProperty("velocimacro.library.autoreload", "false");
		p.setProperty("classloader.resource.loader.root", appPath);
		p.setProperty("velocimarco.library.autoreload", "true");
		p.setProperty("runtime.log.error.stacktrace", "false");
		p.setProperty("runtime.log.warn.stacktrace", "false");
		p.setProperty("runtime.log.info.stacktrace", "false");
		p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
		p.setProperty("runtime.log.logsystem.log4j.category", "velocity_log");
		Velocity.init(p);
		log.info("Veloctiy引擎初始化完成。");
	}
}
