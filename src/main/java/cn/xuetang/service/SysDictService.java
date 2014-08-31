package cn.xuetang.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;

import cn.xuetang.common.config.Dict;
import cn.xuetang.common.util.ErrorUtil;
import cn.xuetang.modules.sys.bean.Sys_dict;

@IocBean(fields = { "dao" })
public class SysDictService extends BaseService<Sys_dict> {

	public SysDictService(Dao dao) {
		super(dao);
	}

	public List<Sys_dict> listByLike(String id) {
		Criteria cri = Cnd.cri();
		cri.where().and("id", "like", id + "%");
		cri.getOrderBy().asc("location");
		return dao().query(getEntityClass(), cri);
	}

	public List<Sys_dict> listByLikeId(String id, List<Object> array) {
		if (Strings.isBlank(id)) {
			Map<String, Object> jsonroot = new HashMap<String, Object>();
			jsonroot.put("id", "");
			jsonroot.put("pId", "0");
			jsonroot.put("name", "数据字典");
			jsonroot.put("url", "javascript:list(\"\")");
			jsonroot.put("target", "_self");
			jsonroot.put("icon", AppInfoService.APP_BASE_NAME + "/images/icons/icon042a1.gif");
			array.add(jsonroot);
			return dao().query(getEntityClass(), Cnd.where("length(id)", "=", 4).asc("location"));
		} else {
			return dao().query(getEntityClass(), Cnd.where("id", "like", id + "____").asc("location"));
		}
	}

	public String getList(String zipcode) {
		if (Strings.isBlank(zipcode)) {
			List<Sys_dict> list = dao().query(getEntityClass(), Cnd.where("id", "like", Dict.DIVSION + "____"));
			return Json.toJson(list);
		} else {
			Sys_dict dict = dao().fetch(getEntityClass(), Cnd.where("dkey", "=", zipcode));
			if (dict != null) {
				List<Sys_dict> list = dao().query(getEntityClass(), Cnd.where("id", "like", dict.getId() + "____"));
				return Json.toJson(list);
			}
			return ErrorUtil.getErrorMsg(3);
		}
	}

	/**
	 * 取一个字段的一个值，对于多个字段或多个值，不适用此方法
	 * 
	 * @param dao
	 * @param sql
	 * @return
	 */
	public int getIntRowValue(Sys_dict sys_dict) {
		Sql sql = Sqls.create("select max(location)+1 from Sys_dict where id like  @id");
		sql.params().set("id", sys_dict.getId() + "_%");
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				if (rs.next()) {
					return rs.getInt(1);
				}
				return 0;
			}
		});
		dao().execute(sql);
		return sql.getInt();
	}

	/**
	 * 根据列名得到下一级栏目的下一个值
	 * 
	 * @param dao
	 * @param tableName
	 * @param cloName
	 * @param value
	 * @return
	 */
	public String getSubMenuId(String tableName, String cloName, String value) {
		final String val = value;
		Sql sql = Sqls.create("select " + cloName + " from " + tableName + " where " + cloName + " like '" + value + "____' order by " + cloName + " desc");
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				String rsvalue = val + "0001";
				if (rs != null && rs.next()) {
					rsvalue = rs.getString(1);
					int newvalue = NumberUtils.toInt(rsvalue.substring(rsvalue.length() - 4)) + 1;
					rsvalue = rsvalue.substring(0, rsvalue.length() - 4) + new java.text.DecimalFormat("0000").format(newvalue);
				}
				return rsvalue;
			}
		});
		dao().execute(sql);
		return sql.getString();
	}

	public void updateSqls(String[] ids, String id) {
		Sql[] sqls = new Sql[ids.length];
		for (int i = 0; i < ids.length; i++) {
			Sql sql = Sqls.create("update Sys_dict set location =" + (i) + " where id like '" + id + "%' and id=" + ids[i]);
			sqls[i] = sql;
		}
		dao().execute(sqls);
	}
}