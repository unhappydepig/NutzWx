package cn.xuetang.modules.wx;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.modules.wx.bean.Weixin_content_attr;
import cn.xuetang.service.wx.WeixinContentAttrService;

/**
 * @author Wizzer
 * @time 2014-05-18 09:41:37
 * 
 */
@IocBean
@At("/private/wx/weixin_content_attr")
public class Weixin_content_attrAction {
	@Inject
	protected WeixinContentAttrService weixinContentAttrService;

	@At
	@Ok("vm:template.private.wx.Weixin_content_attr")
	public void index(@Param("sys_menu") String sys_menu, HttpServletRequest req) {
		req.setAttribute("sys_menu", sys_menu);
	}

	@At
	@Ok("vm:template.private.wx.Weixin_content_attrAdd")
	public void toadd() {

	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") Weixin_content_attr weixin_content_attr) {
		return weixinContentAttrService.insert(weixin_content_attr);
	}

	// @At
	// @Ok("raw")
	// public String add(@Param("..") Weixin_content_attr weixin_content_attr) {
	// return daoCtl.addT(dao,weixin_content_attr).getId();
	// }

	// @At
	// @Ok("json")
	// public Weixin_content_attr view(@Param("id") String id) {
	// return daoCtl.detailByName(dao,Weixin_content_attr.class, id);
	// }

	// @At
	// @Ok("vm:template.private.wx.Weixin_content_attrUpdate")
	// public Weixin_content_attr toupdate(@Param("id") String id,
	// HttpServletRequest req) {
	// return daoCtl.detailByName(dao, Weixin_content_attr.class, id);//html:obj
	// }

	@At
	public boolean update(@Param("..") Weixin_content_attr weixin_content_attr) {
		return weixinContentAttrService.update(weixin_content_attr);
	}

	// @At
	// public boolean delete(@Param("id") String id) {
	// return daoCtl.deleteById(dao, Weixin_content_attr.class, id);
	// }

	@At
	public boolean deleteIds(@Param("ids") String[] ids) {
		return weixinContentAttrService.delete(Cnd.where("id", "in", ids)) > 0;
	}

	@At
	@Ok("raw")
	public String list(@Param("page") int curPage, @Param("rows") int pageSize) {
		Criteria cri = Cnd.cri();
		cri.where().and("1", "=", 1);
		cri.getOrderBy().desc("");
		return weixinContentAttrService.listPageJson(curPage, pageSize, cri);
	}

}