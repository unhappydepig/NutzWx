package cn.xuetang.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.service.IdEntityService;

import cn.xuetang.common.dao.DBObject;

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

	public List<T> listByCnd(Condition cnd) {
		return dao().query(getEntityClass(), cnd);
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

	public T detailByName(String colname, String name) {
		T t;
		try {
			t = dao().fetch(getEntityClass(), Cnd.where(colname, "=", name));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return t;
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

	/**
	 * 通过查询条件获得Hashtable
	 * 
	 * @param sql
	 * @return
	 */
	public Hashtable<String, String> getHTable(Sql sql) {
		final Hashtable<String, String> htable = new Hashtable<String, String>();
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
					htable.put(key, value);
				}
				return null;
			}
		});
		dao().execute(sql);
		return htable;
	}

	public int delete(String table, Condition cnd) {
		return dao().clear(table, cnd);
	}

	/**
	 * 通过查询条件获得Hashtable
	 * 
	 * @param sql
	 * @return
	 */
	public HashMap<String, String> getHashMap(Sql sql) {
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

	public String listPageJsonSql(Sql sql, int curPage, int pageSize, int count) {
		Pager pager = dao().createPager(curPage, pageSize);
		pager.setRecordCount(count);// 记录数需手动设置
		sql.setPager(pager);
		sql.setCallback(Sqls.callback.records());
		dao().execute(sql);
		Map<String, Object> jsonobj = new HashMap<String, Object>();
		jsonobj.put("total", pager.getRecordCount());
		jsonobj.put("rows", sql.getList(Map.class));
		return Json.toJson(jsonobj);
	}

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

	public List<T> list(Sql sql) {
		Entity<T> entity = dao().getEntity(getEntityClass());
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);
		return sql.getList(getEntityClass());
	}

	public QueryResult listPageSql(Sql sql, int curPage, int pageSize, int count) {
		Pager pager = dao().createPager(curPage, pageSize);
		pager.setRecordCount(count);// 记录数需手动设置
		sql.setPager(pager);
		sql.setCallback(Sqls.callback.records());
		dao().execute(sql);
		return new QueryResult(sql.getList(Map.class), pager);
	}

	public QueryResult listPage(int curPage, int pageSize, Condition cnd) {
		Pager pager = dao().createPager(curPage, pageSize);
		List<T> list = dao().query(getEntityClass(), cnd, pager);
		pager.setRecordCount(dao().count(getEntityClass(), cnd));// 记录数需手动设置
		return new QueryResult(list, pager);
	}
	
    public QueryResult listPagerSql(Sql sql, Pager pager) {
        sql.setPager(pager);
        sql.setCallback(Sqls.callback.records());
        dao().execute(sql);
        return new QueryResult(sql.getList(Map.class), pager);
    }
    public List<Map> listMap(Sql sql) {
        sql.setCallback(Sqls.callback.records());
        dao().execute(sql);
        return sql.getList(Map.class);
    }
}
