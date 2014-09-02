package cn.xuetang.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.filepool.FilePool;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Scheduler;

import cn.xuetang.common.config.Dict;
import cn.xuetang.common.util.WeixinUtil;
import cn.xuetang.modules.app.bean.App_info;
import cn.xuetang.modules.sys.bean.Sys_config;

@IocBean(fields = { "dao" })
public class AppInfoService extends BaseService<App_info> {

	private final static Log log = Logs.get();
	private App app = new App();

	private class App {
		// 虚拟目录路径
		private String APP_BASE_PATH = "";
		// 虚拟目录名称
		private String APP_BASE_NAME = "";
		// 应用中文名
		private String APP_NAME = "";
		// 系统配置
		private Map<String, String> SYS_CONFIG = new HashMap<>();
		// 数据字典，根据ID分别初始化
		private Map<String, Object> DATA_DICT = new HashMap<String, Object>();
		// 应用信息，用于通过mykey验证来源，放在内存里为了提高响应速度
		private Map<String, Object> APP_INFO = new HashMap<String, Object>();;
		// 定时任务实例
		private Scheduler SCHEDULER;
		// 文件池
		private FilePool FILE_POOL;

		public void appInfoClean() {
			APP_INFO.clear();
		}

		public void appInfoPut(String key, Object obj) {
			APP_INFO.put(key, obj);
		}

		public void sysConfigPut(String key, String obj) {
			SYS_CONFIG.put(key, obj);
		}

		public void dataDictPut(String key, Object obj) {
			DATA_DICT.put(key, obj);
		}

		public Object dataDictGet(String key) {
			return DATA_DICT.get(key);
		}

	}
	public void setAPP_BASE_PATH(String aPP_BASE_PATH) {
		app.APP_BASE_PATH = aPP_BASE_PATH;
	}

	public void setAPP_BASE_NAME(String aPP_BASE_NAME) {
		app.APP_BASE_NAME = aPP_BASE_NAME;
	}

	public void setAPP_NAME(String aPP_NAME) {
		app.APP_NAME = aPP_NAME;
	}

	public void setSYS_CONFIG(Map<String, String> sYS_CONFIG) {
		app.SYS_CONFIG = sYS_CONFIG;
	}

	public void setDATA_DICT(Map<String, Object> dATA_DICT) {
		app.DATA_DICT = dATA_DICT;
	}

	public void setAPP_INFO(Map<String, Object> aPP_INFO) {
		app.APP_INFO = aPP_INFO;
	}

	public void setSCHEDULER(Scheduler sCHEDULER) {
		app.SCHEDULER = sCHEDULER;
	}

	public void setFILE_POOL(FilePool fILE_POOL) {
		app.FILE_POOL = fILE_POOL;
	}
	public String getAPP_BASE_PATH() {
		return app.APP_BASE_PATH;
	}

	public String getAPP_BASE_NAME() {
		return app.APP_BASE_NAME;
	}

	public String getAPP_NAME() {
		return app.APP_NAME;
	}

	public Map<String, String> getSYS_CONFIG() {
		return app.SYS_CONFIG;
	}

	public Map<String, Object> getDATA_DICT() {
		return app.DATA_DICT;
	}

	public Map<String, Object> getAPP_INFO() {
		return app.APP_INFO;
	}

	public Scheduler getSCHEDULER() {
		return app.SCHEDULER;
	}

	public FilePool getFILE_POOL() {
		return app.FILE_POOL;
	}
	public AppInfoService() {
	}

	public AppInfoService(Dao dao) {
		super(dao);
	}

	public List<App_info> list() {
		return dao().query(getEntityClass(), Cnd.orderBy().asc("id"), null);
	}

	public void InitAppInfo() {
		try {
			app.appInfoClean();
			List<App_info> appInfoList = dao().query(getEntityClass(), Cnd.orderBy().asc("ID"));
			for (App_info appInfo : appInfoList) {
				app.appInfoPut(appInfo.getMykey(), appInfo);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void InitSysConfig() {
		try {
			List<Sys_config> configList = dao().query(Sys_config.class, Cnd.orderBy().asc("ID"));
			for (Sys_config sysConfig : configList) {
				app.sysConfigPut(sysConfig.getCname(), sysConfig.getCvalue());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void InitDataDict() {
		try {
			app.dataDictPut(Dict.APP_TYPE, getHashMap(Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.APP_TYPE + "____'")));
			app.dataDictPut(Dict.DIVSION, getHashMap(Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.DIVSION + "_%'")));
			app.dataDictPut(Dict.FORM_TYPE, getHashMap(Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.FORM_TYPE + "_%' order by location asc,id asc")));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public int getRowCount(String key) {
		return dao().count(getEntityClass(), Cnd.where("mykey", "=", key));
	}

	public int getRowCountBySecret(String key) {
		return dao().count(getEntityClass(), Cnd.where("mysecret", "=", key));
	}

	public String listPageJson(int curPage, int pageSize) {
		return super.listPageJson(curPage, pageSize, Cnd.orderBy().asc("id"));
	}

	public Object getType(String type) {
		return app.dataDictGet(type);
	}
	
	public String getGloalsAccessToken(App_info appInfo) {
		String access_token = "";
		boolean resetAccesstoken = false;
		long now = System.currentTimeMillis();
		if (!Strings.isBlank(appInfo.getAccess_token())) {
			long ftime = NumberUtils.toLong(Strings.sNull(appInfo.getAccess_time()));
			if ((now - ftime) > 7150 * 1000) {
				resetAccesstoken = true;
			} else {
				access_token = Strings.sNull(appInfo.getAccess_token());
			}
		} else
			resetAccesstoken = true;
		if (resetAccesstoken) {
			access_token = WeixinUtil.getAccess_token(appInfo);
			dao().update(getEntityClass(),Chain.make("access_time", String.valueOf(now)).add("access_token", access_token), Cnd.where("id", "=", appInfo.getId()));
		}
		return access_token;
	}
	/**
	 * 获取全局access_token，超过7200ms重新获取，否则返回上次值
	 * 
	 * @param dao
	 * @param appid
	 * @return
	 */
	public String getGloalsAccessToken(int appid) {
		return getGloalsAccessToken(appid);
	}
}
