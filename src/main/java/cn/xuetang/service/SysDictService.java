package cn.xuetang.service;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.service.EntityService;

import cn.xuetang.common.config.Dict;
import cn.xuetang.common.util.ErrorUtil;
import cn.xuetang.modules.sys.bean.Sys_dict;

@IocBean(fields = { "dao" })
public class SysDictService extends EntityService<Sys_dict> {

	public SysDictService(Dao dao) {
		super(dao);
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
}