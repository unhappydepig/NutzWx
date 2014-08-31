package cn.xuetang.core;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.nutz.filepool.NutFilePool;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import cn.xuetang.common.config.Globals;
import cn.xuetang.common.task.LoadTask;
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
			appServer.SCHEDULER.shutdown();
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void init(NutConfig config) {
		try {
			AppInfoService appServer = Mvcs.getIoc().get(AppInfoService.class);
			velocityInit(config.getAppRoot());
			appServer.APP_BASE_PATH = Strings.sNull(config.getAppRoot());// 项目路径
			appServer.APP_BASE_NAME = Strings.sNull(config.getServletContext().getContextPath());// 部署名
			appServer.InitSysConfig();// 初始化系统参数
			appServer.InitDataDict();// 初始化数据字典
			appServer.InitAppInfo();// 初始化app接口信息
			appServer.APP_NAME = Strings.sNull(Globals.SYS_CONFIG.get("app_name"));// 项目名称
			appServer.FILE_POOL = new NutFilePool("~/tmp/myfiles", 10);// 创建一个文件夹用于下载
			// 初始化Quartz任务
			appServer.SCHEDULER = StdSchedulerFactory.getDefaultScheduler();
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
