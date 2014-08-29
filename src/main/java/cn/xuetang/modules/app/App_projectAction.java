package cn.xuetang.modules.app;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param; 

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.filter.UserLoginFilter;
import cn.xuetang.modules.app.bean.App_project;

/**
 * @author Wizzer
 * @time 2014-03-29 13:12:58
 * 
 */
@IocBean
@At("/private/app/project")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class App_projectAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/app/App_project.html")
    public void index(@Param("sys_menu")String sys_menu,HttpServletRequest req) {
        req.setAttribute("sys_menu",sys_menu);
    }
	
	@At
	@Ok("->:/private/app/App_projectAdd.html")
	public void toadd() {
	
	}
	
	@At
	@Ok("raw")
	public boolean add(@Param("..") App_project app_project) {
		return daoCtl.add(dao,app_project);
	}
	
	//@At
	//@Ok("raw")
	//public int add(@Param("..") App_project app_project) {
	//	return daoCtl.addT(dao,app_project).getId();
	//}
	
	@At
	@Ok("json")
	public App_project view(@Param("id") int id) {
		return daoCtl.detailById(dao,App_project.class, id);
	}
	
	@At
	@Ok("->:/private/app/App_projectUpdate.html")
	public App_project toupdate(@Param("id") int id, HttpServletRequest req) {
		return daoCtl.detailById(dao, App_project.class, id);//html:obj
	}
	
	@At
	public boolean update(@Param("..") App_project app_project) {
		return daoCtl.update(dao, app_project);
	}
	
	@At
	public boolean delete(@Param("id") int id) {
		return daoCtl.deleteById(dao, App_project.class, id);
	}
	
	@At
	public boolean deleteIds(@Param("ids") String ids) {
		return daoCtl.deleteByIds(dao, App_project.class, StringUtils.split(ids, ","));
	}
	
	@At
	@Ok("raw")
	public String list(@Param("page") int curPage, @Param("rows") int pageSize){
		Criteria cri = Cnd.cri();
		cri.where().and("1","=",1);
		cri.getOrderBy().desc("id");
		return daoCtl.listPageJson(dao, App_project.class, curPage, pageSize, cri);
	}

}