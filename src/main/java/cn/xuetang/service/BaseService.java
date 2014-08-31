package cn.xuetang.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.service.IdEntityService;

public class BaseService<T> extends IdEntityService<T> {

	private final static Log log = Logs.get();

	public BaseService() {
	}

	public BaseService(Dao dao) {
		super(dao);
	}

	public boolean insert(T t) {
		try {
			dao().insert(t);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean update(T t) {
		return dao().update(t) > 0;
	}

	public String listPageJson(int curPage, int pageSize, Condition cnd) {
		Map<String, Object> jsonobj = new HashMap<String, Object>();
		Pager pager = dao().createPager(curPage, pageSize);
		List<T> list = dao().query(getEntityClass(), cnd, pager);
		pager.setRecordCount(dao().count(getEntityClass(), cnd));// 记录数需手动设置
		jsonobj.put("total", pager.getRecordCount());
		jsonobj.put("rows", list);
		return Json.toJson(jsonobj);
	}

	public boolean deleteByIds(String[] ids) {
		dao().clear(getEntityClass(), Cnd.where("id", "in", ids));
		return true;
	}

	public int delete(Condition cnd) {
		return dao().clear(getEntityClass(), cnd);
	}

	public int getRowCount(Condition cnd) {
		return dao().count(getEntityClass(), cnd);
	}

	public T detailByName(String id) {
		try {
			return dao().fetch(getEntityClass(), id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean deleteByName(String name) {
		return dao().delete(getEntityClass(), name) == 1;
	}

	public int getIntRowValue(Sql sql) {
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
	public String getSubMenuId(Sql sql, final String val) {
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

	public boolean exeUpdateBySql(Sql sql) {
		try {
			dao().execute(sql);
			return true;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	public boolean updateSortRow(String tableName, String[] ids, String rowName, int initvalue) {
		try {
			Sql[] sqls = new Sql[ids.length];
			for (int i = 0; i < ids.length; i++) {
				sqls[i] = Sqls.create("update " + tableName + " set " + rowName + "=" + (i + initvalue) + " where id=" + ids[i]);
			}
			dao().execute(sqls);
			return true;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

}
