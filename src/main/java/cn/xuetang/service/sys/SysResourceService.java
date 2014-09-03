package cn.xuetang.service.sys;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import cn.xuetang.modules.sys.bean.Sys_resource;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class SysResourceService extends BaseService<Sys_resource> {

	public SysResourceService() {
	}

	public SysResourceService(Dao dao) {
		super(dao);
	}

	public List<Sys_resource> list(int subtype) {
		Criteria cri = Cnd.cri();
		cri.where().and("subtype", "=", subtype);
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		return dao().query(getEntityClass(), cri);
	}

	public List<Sys_resource> listByCnd(Criteria cnd) {
		return dao().query(getEntityClass(), cnd);
	}

	public int getIntRowValue(Sys_resource res) {
		Sql sql = Sqls.create("select max(location)+1 from Sys_resource where id like  @id");
		sql.params().set("id", res.getId() + "_%");
		return getIntRowValue(sql);
	}

	public String getSubMenuId(String tableName, String cloName, String value) {
		Sql sql = Sqls.create("select " + cloName + " from " + tableName + " where " + cloName + " like '" + value + "____' order by " + cloName + " desc");
		return super.getSubMenuId(sql, value);
	}

	public void delSqls(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			exeUpdateBySql(Sqls.create("delete from Sys_resource where id like '" + Strings.sNull(ids[i]) + "%'"));
			exeUpdateBySql(Sqls.create("delete from Sys_role_resource where resourceid like '" + Strings.sNull(ids[i]) + "%'"));
		}
	}

	public boolean updateSortRow(String[] ids, int initvalue) {
		return super.updateSortRow("Sys_resource", ids, "location", initvalue);
	}
}
