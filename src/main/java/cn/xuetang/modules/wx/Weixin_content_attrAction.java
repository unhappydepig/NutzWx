package cn.xuetang.modules.wx;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SqlExpressionGroup;
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

import cn.xuetang.modules.wx.bean.Weixin_content_attr;

/**
 * @author Wizzer
 * @time 2014-05-18 09:41:37
 * 
 */
@IocBean
@At("/private/wx/weixin_content_attr")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Weixin_content_attrAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/wx/Weixin_content_attr.html")
	public void index(@Param("sys_menu") String sys_menu,HttpServletRequest req) {
		req.setAttribute("sys_menu",sys_menu);
	}
	
	@At
	@Ok("->:/private/wx/Weixin_content_attrAdd.html")
	public void toadd() {
	
	}
	
	@At
	@Ok("raw")
	public boolean add(@Param("..") Weixin_content_attr weixin_content_attr) {
		return daoCtl.add(dao,weixin_content_attr);
	}
	
	//@At
	//@Ok("raw")
	//public String add(@Param("..") Weixin_content_attr weixin_content_attr) {
	//	return daoCtl.addT(dao,weixin_content_attr).getId();
	//}
	
	//@At
	//@Ok("json")
	//public Weixin_content_attr view(@Param("id") String id) {
		//return daoCtl.detailByName(dao,Weixin_content_attr.class, id);
	//}
	
	//@At
	//@Ok("->:/private/wx/Weixin_content_attrUpdate.html")
	//public Weixin_content_attr toupdate(@Param("id") String id, HttpServletRequest req) {
		//return daoCtl.detailByName(dao, Weixin_content_attr.class, id);//html:obj
	//}
	
	@At
	public boolean update(@Param("..") Weixin_content_attr weixin_content_attr) {
		return daoCtl.update(dao, weixin_content_attr);
	}
	
	//@At
	//public boolean delete(@Param("id") String id) {
		//return daoCtl.deleteById(dao, Weixin_content_attr.class, id);
	//}
	
	@At
	public boolean deleteIds(@Param("ids") String[] ids) {
		return daoCtl.delete(dao, Weixin_content_attr.class, Cnd.where("id", "in", ids)) > 0;
	}
	
	@At
	@Ok("raw")
	public String list(@Param("page") int curPage, @Param("rows") int pageSize){
		Criteria cri = Cnd.cri();
		cri.where().and("1","=",1);
		cri.getOrderBy().desc("");
		return daoCtl.listPageJson(dao, Weixin_content_attr.class, curPage, pageSize, cri);
	}

}