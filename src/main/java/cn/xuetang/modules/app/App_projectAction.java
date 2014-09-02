package cn.xuetang.modules.app;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.modules.app.bean.App_project;
import cn.xuetang.service.sys.AppProjectService;

/**
 * @author Wizzer
 * @time 2014-03-29 13:12:58
 * 
 */
@IocBean
@At("/private/app/project")
public class App_projectAction {
	@Inject
	private AppProjectService appProjectService;

	@At("")
	@Ok("vm:template.private.app.App_project")
	public void index(@Param("sys_menu") String sys_menu, HttpServletRequest req) {
		req.setAttribute("sys_menu", sys_menu);
	}

	@At
	@Ok("vm:template.private.app.App_projectAdd")
	public void toadd() {

	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") App_project app_project) {
		return appProjectService.insert(app_project);
	}

	// @At
	// @Ok("raw")
	// public int add(@Param("..") App_project app_project) {
	// return daoCtl.addT(dao,app_project).getId();
	// }

	@At
	@Ok("json")
	public App_project view(@Param("id") int id) {
		return appProjectService.fetch(id);
	}

	@At
	@Ok("vm:template.private.app.App_projectUpdate")
	public App_project toupdate(@Param("id") int id, HttpServletRequest req) {
		return appProjectService.fetch(id);// html:obj
	}

	@At
	public boolean update(@Param("..") App_project app_project) {
		return appProjectService.update(app_project);
	}

	@At
	public boolean delete(@Param("id") int id) {
		return appProjectService.delete(id) > 0;
	}

	@At
	public boolean deleteIds(@Param("ids") String[] ids) {
		return appProjectService.deleteByIds(ids);
	}

	@At
	@Ok("raw")
	public String list(@Param("page") int curPage, @Param("rows") int pageSize) {
		Criteria cri = Cnd.cri();
		cri.getOrderBy().desc("id");
		return appProjectService.listPageJson(curPage, pageSize, cri);
	}

}