package cn.xuetang.modules.wx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.util.DateUtil;
import cn.xuetang.common.util.WeixinUtil;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.modules.wx.bean.Weixin_push_content;
import cn.xuetang.service.AppInfoService;

/**
 * @author Wizzer
 * @time 2014-04-28 15:23:18
 */
@IocBean
@At("/private/wx/push/content")
public class Weixin_push_contentAction extends BaseAction {
	@Inject
	protected Dao dao;
	@Inject
	private AppInfoService appInfoService;
	@At("")
	@Ok("vm:template.private.wx.Weixin_push_content")
	public void index(@Param("contentids") String contentids, @Param("sys_menu") String sys_menu, @Param("pid") int pid, @Param("appid") int appid, HttpServletRequest req) {
		req.setAttribute("pid", pid);
		req.setAttribute("appid", appid);
		req.setAttribute("sys_menu", sys_menu);
		req.setAttribute("contentids", contentids);
	}

	@At
	@Ok("vm:template.private.wx.Weixin_push_contentAdd")
	public void toadd(@Param("pid") int pid, @Param("appid") int appid, HttpServletRequest req) {
		req.setAttribute("pid", pid);
		req.setAttribute("appid", appid);
	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") Weixin_push_content weixin_push_content, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		weixin_push_content.setOp_time(DateUtil.getCurDateTime());
		weixin_push_content.setOp_userid(user.getUserid());
		Weixin_push_content content = daoCtl.addT(dao, weixin_push_content);
		if (Strings.isBlank(weixin_push_content.getContent_source_url())) {
			String domain = Strings.sNull(appInfoService.getSYS_CONFIG().get("app_domain"));
			content.setContent_source_url(domain + "/api/wx/news/pushview?id=" + content.getId());
			daoCtl.update(dao, content);
		}
		return content != null;
	}

	@At
	public String getAccessToken(@Param("appid") int appid) {
		return WeixinUtil.getGloalsAccessToken(dao, appid);
	}

	@At
	@Ok("vm:template.private.wx.Weixin_push_contentUpdate")
	public Weixin_push_content toupdate(@Param("id") int id, HttpServletRequest req) {
		Weixin_push_content content = daoCtl.detailById(dao, Weixin_push_content.class, id);
		req.setAttribute("pid", content.getPid());
		return content;// html:obj
	}

	@At
	public boolean update(@Param("..") Weixin_push_content weixin_push_content, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		weixin_push_content.setOp_time(DateUtil.getCurDateTime());
		weixin_push_content.setOp_userid(user.getUserid());
		if (Strings.isBlank(weixin_push_content.getContent_source_url())) {
			String domain = Strings.sNull(appInfoService.getSYS_CONFIG().get("app_domain"));
			weixin_push_content.setContent_source_url(domain + "/api/wx/news/pushview?id=" + weixin_push_content.getId());
		}
		return daoCtl.update(dao, weixin_push_content);
	}

	@At
	public boolean deleteIds(@Param("ids") Integer[] ids) {
		return daoCtl.delete(dao, Weixin_push_content.class, Cnd.where("id", "in", ids)) > 0;
	}

	@At
	@Ok("raw")
	public String list(@Param("pid") int pid, @Param("appid") int appid, @Param("page") int curPage, @Param("rows") int pageSize) {
		Criteria cri = Cnd.cri();
		cri.where().and("pid", "=", pid);
		cri.where().and("appid", "=", appid);
		cri.where().and("status", "=", 0);
		cri.getOrderBy().desc("id");
		return daoCtl.listPageJson(dao, Weixin_push_content.class, curPage, pageSize, cri);
	}

}