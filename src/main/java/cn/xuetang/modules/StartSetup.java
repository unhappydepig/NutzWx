package cn.xuetang.modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import cn.xuetang.modules.user.bean.PermissionCategory;
import cn.xuetang.service.sys.AppInfoService;

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
			/*dao.drop(Sys_user.class);
			dao.drop(Sys_role.class);
			dao.drop(Permission.class);
			dao.drop(PermissionCategory.class);*/
			if (!dao.exists(Sys_user.class)) {
				dao.create(Sys_user.class, false);
				dao.create(Sys_role.class, false);
				dao.create(Permission.class, false);
				dao.create(PermissionCategory.class, false);
				final Sys_user defaultUser = new Sys_user();
				defaultUser.setLoginname("admin");
				defaultUser.setRealname("admin");
				RandomNumberGenerator rng = new SecureRandomNumberGenerator();
				String salt = rng.nextBytes().toBase64();
				String hashedPasswordBase64 = new Sha256Hash("123", salt, 1024).toBase64();
				defaultUser.setPassword(hashedPasswordBase64);
				defaultUser.setSalt(salt);
				defaultUser.setLoginTime(Times.now());
				// dao.insert(defaultUser);
				/**
				 * 全部权限 *:*:* 添加账号 sys:user.add 更新账号 sys:user.update 删除账号
				 * sys:user.delete 浏览账号 sys:user.view
				 * 
				 * 添加角色 sys:role.add 更新角色 sys:role.update 删除角色 sys:role.delete
				 * 浏览角色 sys:role.view
				 * 
				 * 权限管理 sys:permission 权限分类 sys:permissionCategory
				 */
				List<Permission> pers = new ArrayList<Permission>();
				PermissionCategory perCategory = new PermissionCategory();
				perCategory.setStyle("icon-cog");
				perCategory.setLocked(true);
				perCategory.setName("系统管理");
				perCategory.setPermissions(pers);
				perCategory.setListIndex(1);
				Permission allPerm = new Permission();
				allPerm.setDescription("全部权限");
				allPerm.setLocked(true);
				allPerm.setShow(false);
				allPerm.setName("*:*:*");
				allPerm.setPermissionCategory(perCategory);
				pers.add(allPerm);

				Permission per = new Permission();
				per.setDescription("机构管理");
				per.setLocked(true);
				per.setShow(true);
				per.setName("nutzwx:sys.user.unit");
				per.setUrl("/private/sys/unit");
				per.setPermissionCategory(perCategory);
				pers.add(per);

				per = new Permission();
				per.setUrl("/private/sys/user");
				per.setStyle("fa fa-users");
				per.setShow(true);
				per.setDescription("用户管理");
				per.setLocked(true);
				per.setName("nutzwx:sys.user");
				per.setPermissionCategory(perCategory);
				pers.add(per);

				per = new Permission();
				per.setUrl("/private/sys/role");
				per.setStyle("fa fa-user");
				per.setShow(true);
				per.setDescription("角色管理");
				per.setLocked(true);
				per.setName("nutzwx:sys.role");
				per.setPermissionCategory(perCategory);
				pers.add(per);

				per = new Permission();
				per.setUrl("/private/sys/res");
				per.setStyle("fa fa-user");
				per.setShow(true);
				per.setDescription("资源管理");
				per.setLocked(true);
				per.setName("nutzwx:sys.res");
				per.setPermissionCategory(perCategory);
				pers.add(per);

				per = new Permission();
				per.setUrl("/private/sys/config");
				per.setStyle("fa fa-user");
				per.setShow(true);
				per.setDescription("参数配置");
				per.setLocked(true);
				per.setName("nutzwx:sys.config");
				per.setPermissionCategory(perCategory);
				pers.add(per);

				per = new Permission();
				per.setUrl("/private/sys/dict");
				per.setStyle("fa fa-user");
				per.setShow(true);
				per.setDescription("数字字典");
				per.setLocked(true);
				per.setName("nutzwx:sys.dict");
				per.setPermissionCategory(perCategory);
				pers.add(per);

				per = new Permission();
				per.setUrl("/private/sys/task");
				per.setStyle("fa fa-user");
				per.setShow(true);
				per.setDescription("定时任务");
				per.setLocked(true);
				per.setName("nutzwx:sys.task");
				per.setPermissionCategory(perCategory);
				pers.add(per);

				per = new Permission();
				per.setUrl("/private/sys/safe");
				per.setShow(true);
				per.setDescription("访问控制");
				per.setLocked(true);
				per.setName("nutzwx:sys.safe");
				per.setPermissionCategory(perCategory);
				pers.add(per);

				per = new Permission();
				per.setUrl("/private/sys/user/log");
				per.setShow(true);
				per.setDescription("登陆日志");
				per.setLocked(true);
				per.setName("nutzwx:sys.log");
				per.setPermissionCategory(perCategory);
				pers.add(per);

				dao.insertWith(perCategory, null);

				Sys_role role = new Sys_role();
				role.setName("超级权限");
				role.setDescript("拥有超级管理员的权限");
				List<Permission> permissions = new ArrayList<Permission>();
				permissions.add(allPerm);
				role.setPermissions(permissions);
				List<Sys_user> users = new ArrayList<Sys_user>();
				users.add(defaultUser);
				role.setUsers(users);
				dao.insertWith(role, "users");
				dao.insertRelation(role, "permissions");
				//
				wxManager(dao);

				//
				article(dao);
				//
				memberCenter(dao);
				//
				app(dao);
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

	private void app(Dao dao) {
		List<Permission> pers = new ArrayList<Permission>();
		PermissionCategory perCategory = new PermissionCategory();
		perCategory.setListIndex(2);
		perCategory.setLocked(true);
		perCategory.setName("应用管理");
        perCategory.setStyle("icon-list-alt");
		perCategory.setPermissions(pers);

		Permission allPerm = new Permission();
		allPerm.setUrl("/private/app/project");
		allPerm.setShow(true);
		allPerm.setDescription("项目管理");
		allPerm.setLocked(true);
		allPerm.setName("nutzwx:app.project");
		allPerm.setPermissionCategory(perCategory);
		pers.add(allPerm);

		Permission per = new Permission();
		per.setUrl("/private/app/info");
		per.setShow(true);
		per.setDescription("接口管理");
		per.setLocked(true);
		per.setName("nutzwx:app.api");
		per.setPermissionCategory(perCategory);
		pers.add(per);
		dao.insertWith(perCategory, null);
	}

	private void memberCenter(Dao dao) {

		List<Permission> pers = new ArrayList<Permission>();
		PermissionCategory perCategory = new PermissionCategory();
		perCategory.setListIndex(3);
		perCategory.setLocked(true);
		perCategory.setName("会员中心");
        perCategory.setStyle("icon-edit");
        perCategory.setPermissions(pers);

		Permission allPerm = new Permission();
		allPerm.setUrl("/private/user/info");
		allPerm.setShow(true);
		allPerm.setDescription("会员资料");
		allPerm.setLocked(true);
		allPerm.setName("nutzwx:member.info");
		allPerm.setPermissionCategory(perCategory);
		pers.add(allPerm);

		Permission per = new Permission();
		per.setUrl("/private/user/score");
		per.setShow(true);
		per.setDescription("会员积分");
		per.setLocked(true);
		per.setName("nutzwx:member.score");
		per.setPermissionCategory(perCategory);
		pers.add(per);

		per = new Permission();
		per.setUrl("/private/user/connwx");
		per.setShow(true);
		per.setDescription("微信帐号");
		per.setLocked(true);
		per.setName("nutzwx:member.account");
		per.setPermissionCategory(perCategory);
		pers.add(per);

		dao.insertWith(perCategory, null);
	}

	private void article(Dao dao) {
		List<Permission> pers = new ArrayList<Permission>();
		PermissionCategory perCategory = new PermissionCategory();
		perCategory.setStyle("icon-text-width");
		perCategory.setListIndex(4);
		perCategory.setLocked(true);
		perCategory.setName("内容管理");
		perCategory.setPermissions(pers);

		Permission allPerm = new Permission();
		allPerm.setUrl("/private/wx/content");
		allPerm.setShow(true);
		allPerm.setDescription("文章管理");
		allPerm.setLocked(true);
		allPerm.setName("nutzwx:article.content");
		allPerm.setPermissionCategory(perCategory);
		pers.add(allPerm);

		Permission per = new Permission();
		per.setUrl("/private/wx/channel");
		per.setShow(true);
		per.setDescription("栏目管理");
		per.setLocked(true);
		per.setName("nutzwx:article.channel");
		per.setPermissionCategory(perCategory);
		pers.add(per);
		dao.insertWith(perCategory, null);

	}

	private void wxManager(Dao dao) {
		List<Permission> pers = new ArrayList<Permission>();
		PermissionCategory perCategory = new PermissionCategory();
		perCategory.setStyle("icon-desktop");
		perCategory.setListIndex(5);
		perCategory.setLocked(true);
		perCategory.setName("微信管理");
		perCategory.setPermissions(pers);

		Permission allPerm = new Permission();
		allPerm.setUrl("/private/wx/txt");
		allPerm.setShow(true);
		allPerm.setDescription("微信回复");
		allPerm.setLocked(true);
		allPerm.setName("nutzwx:wx.txt");
		allPerm.setPermissionCategory(perCategory);
		pers.add(allPerm);

		Permission per = new Permission();
		per.setShow(true);
		per.setDescription("客服群发");
		per.setLocked(true);
		per.setName("nutzwx:wx.push.group");
		per.setPermissionCategory(perCategory);
		pers.add(per);

		per = new Permission();
		per.setUrl("/private/wx/push");
		per.setShow(true);
		per.setDescription("高级群发");
		per.setLocked(true);
		per.setName("nutzwx:wx.push.supper");
		per.setPermissionCategory(perCategory);
		pers.add(per);

		per = new Permission();
		per.setUrl("/private/wx/image");
		per.setShow(true);
		per.setDescription("微信相册");
		per.setLocked(true);
		per.setName("nutzwx:wx.image");
		per.setPermissionCategory(perCategory);
		pers.add(per);

		per = new Permission();
		per.setUrl("/private/wx/video");
		per.setShow(true);
		per.setDescription("微信视频");
		per.setLocked(true);
		per.setName("nutzwx:wx.video");
		per.setPermissionCategory(perCategory);
		pers.add(per);

		per = new Permission();
		per.setShow(true);
		per.setDescription("微信菜单");
		per.setLocked(true);
		per.setName("nutzwx:wx.menu");
		per.setPermissionCategory(perCategory);
		pers.add(per);
		dao.insertWith(perCategory, null);
	}

	private void velocityInit(String appPath) throws IOException {
		log.info("Veloctiy引擎初始化...");
		Properties p = new Properties();
		p.setProperty("resource.loader", "file,classloader");
		p.setProperty("file.resource.loader.path", appPath);
		p.setProperty("file", "org.apache.velocity.tools.view.WebappResourceLoader");
		p.setProperty("classloader.resource.loader.class", "cn.xuetang.mvc.VelocityResourceLoader");
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
