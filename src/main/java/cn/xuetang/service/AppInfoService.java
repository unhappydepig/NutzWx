package cn.xuetang.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.filepool.FilePool;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Scheduler;

import cn.xuetang.common.config.Dict;
import cn.xuetang.common.dao.DBObject;
import cn.xuetang.modules.app.bean.App_info;
import cn.xuetang.modules.sys.bean.Sys_config;

@IocBean(fields = { "dao" })
public class AppInfoService extends BaseService<App_info> {

	private final static Log log = Logs.get();
	// 虚拟目录路径
	public String APP_BASE_PATH = "";
	// 虚拟目录名称
	public String APP_BASE_NAME = "";
	// 应用中文名
	public String APP_NAME = "";
	// 系统配置
	public Map<String, String> SYS_CONFIG = new HashMap<>();
	// 数据字典，根据ID分别初始化
	public Map<String, Object> DATA_DICT = new HashMap<String, Object>();
	// 应用信息，用于通过mykey验证来源，放在内存里为了提高响应速度
	public Map<String, Object> APP_INFO = new HashMap<String, Object>();;
	// 定时任务实例
	public Scheduler SCHEDULER;
	// 文件池
	public FilePool FILE_POOL;

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
			APP_INFO.clear();
			List<App_info> appInfoList = dao().query(getEntityClass(), Cnd.orderBy().asc("ID"));
			for (App_info appInfo : appInfoList) {
				APP_INFO.put(appInfo.getMykey(), appInfo);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void InitSysConfig() {
		try {
			List<Sys_config> configList = dao().query(Sys_config.class, Cnd.orderBy().asc("ID"));
			for (Sys_config sysConfig : configList) {
				SYS_CONFIG.put(sysConfig.getCname(), sysConfig.getCvalue());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void InitDataDict() {
		try {
			DATA_DICT.put(Dict.APP_TYPE, getHashMap(Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.APP_TYPE + "____'")));
			DATA_DICT.put(Dict.DIVSION, getHashMap(Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.DIVSION + "_%'")));
			DATA_DICT.put(Dict.FORM_TYPE, getHashMap(Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.FORM_TYPE + "_%' order by location asc,id asc")));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public <sys_dict> HashMap<String, String> getHashMap(Sql sql) {
		final HashMap<String, String> hashMap = new HashMap<String, String>();
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				String key = "", value = "";
				while (rs.next()) {
					ResultSetMetaData rsmd = rs.getMetaData();
					if (rsmd.getColumnType(1) == 2005) {
						key = Strings.sNull(DBObject.getClobBody(rs, rsmd.getColumnName(1)));
					} else {
						key = Strings.sNull(rs.getString(1));
					}
					if (rsmd.getColumnType(2) == 2005) {
						value = Strings.sNull(DBObject.getClobBody(rs, rsmd.getColumnName(2)));
					} else {
						value = Strings.sNull(rs.getString(2));
					}
					hashMap.put(key, value);
				}
				return null;
			}
		});
		dao().execute(sql);
		return hashMap;
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
		return DATA_DICT.get(type);
	}
}
