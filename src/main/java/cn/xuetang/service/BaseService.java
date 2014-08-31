package cn.xuetang.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.service.IdEntityService;

public class BaseService<T> extends IdEntityService<T> {

	private final static Log log = Logs.get();

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
}
