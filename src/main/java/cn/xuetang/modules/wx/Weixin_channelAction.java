package cn.xuetang.modules.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.config.Dict;
import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.filter.UserLoginFilter;
import cn.xuetang.common.util.DateUtil;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.modules.wx.bean.Weixin_channel;
import cn.xuetang.service.AppInfoService;
import cn.xuetang.service.AppProjectService;
import cn.xuetang.service.WeixinChannelService;

/**
 * @author Wizzer
 * @time 2014-03-30 11:28:46
 */
@IocBean
@At("/private/wx/channel")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Weixin_channelAction{
	@Inject
	protected AppProjectService appProjectService;
	@Inject
	private WeixinChannelService weixinChannelService;
	@Inject
	private AppInfoService appInfoService;

	@At
	@Ok("vm:template.private.wx.Weixin_channel")
	public void index(@Param("sys_menu") String sys_menu, HttpServletRequest req, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Map<String, String> map = (Map) appInfoService.DATA_DICT.get(Dict.FORM_TYPE);
		req.setAttribute("formmap", map);
		if (user.getSysrole()) {
			req.setAttribute("pro", appProjectService.listByCnd(Cnd.orderBy().asc("id")));

		} else {
			req.setAttribute("pro", appProjectService.listByCnd(Cnd.where("id", "in", user.getProlist()).asc("id")));
		}
		req.setAttribute("sys_menu", sys_menu);
	}

	@At
	@Ok("vm:template.private.wx.Weixin_channelAdd")
	public void toadd(@Param("pid") String pid, HttpServletRequest req, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if (user.getSysrole()) {
			req.setAttribute("pro", appProjectService.listByCnd(Cnd.orderBy().asc("id")));
		} else {
			req.setAttribute("pro", appProjectService.listByCnd(Cnd.where("id", "in", user.getProlist()).asc("id")));
		}
		req.setAttribute("pid", pid);
	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") Weixin_channel wx, @Param("id") String id, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		int location = 0;
		if (!Strings.isBlank(wx.getId())) {
			Sql sql = Sqls.create("select max(location)+1 from Weixin_channel where id like  @id");
			sql.params().set("id", wx.getId() + "_%");
			location = weixinChannelService.getIntRowValue(sql);
		}
		wx.setAdd_userid(user.getUserid());
		wx.setAdd_time(DateUtil.getCurDateTime());
		wx.setLocation(location);
		wx.setId(weixinChannelService.getSubMenuId("Weixin_channel", "id", Strings.sNull(id)));
		return weixinChannelService.insert(wx);
	}

	@At
	@Ok("vm:template.private.wx.Weixin_channelUpdate")
	public Weixin_channel toupdate(@Param("id") String id, HttpServletRequest req) {
		return weixinChannelService.fetch(Cnd.where("id", "=", id));// html:obj
	}

	@At
	public boolean update(@Param("..") Weixin_channel wx, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		wx.setAdd_userid(user.getUserid());
		wx.setAdd_time(DateUtil.getCurDateTime());
		return weixinChannelService.update(wx);
	}

	@At
	public boolean delete(@Param("id") String id) {
		return weixinChannelService.delete(Cnd.where("id", "=", id)) > 0;
	}

	@At
	public boolean deleteIds(@Param("ids") String[] ids) {
		try {
			for (int i = 0; i < ids.length; i++) {
				weixinChannelService.exeUpdateBySql(Sqls.create("delete from Weixin_channel where id like '" + Strings.sNull(ids[i]) + "%'"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@At
	@Ok("raw")
	public String list(@Param("page") int curPage, @Param("rows") int pageSize, @Param("pid") int pid) {
		Criteria cri = Cnd.cri();
		cri.where().and("pid", "=", pid);
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		return weixinChannelService.listPageJson(curPage, pageSize, cri);
	}

	@At
	@Ok("raw")
	public String treelist(@Param("id") String id, @Param("pid") int proid, HttpSession session) {
		Sql sql;
		List<Object> array = new ArrayList<Object>();
		if (Strings.isBlank(id)) {
			Map<String, String> jsonroot = new HashMap<String, String>();
			jsonroot.put("id", "");
			jsonroot.put("pId", "0");
			jsonroot.put("name", "栏目列表");
			jsonroot.put("url", "javascript:changeClass(\"\")");
			jsonroot.put("target", "_self");
			jsonroot.put("icon", AppInfoService.APP_BASE_NAME + "/images/icons/icon042a1.gif");
			array.add(jsonroot);
			sql = Sqls.create("select * from Weixin_channel where pid=@s and id like @c order by location asc");
			sql.params().set("s", proid);
			sql.params().set("c", "____");
		} else {
			sql = Sqls.create("select * from Weixin_channel where pid=@s and id like @c order by location asc");
			sql.params().set("s", proid);
			sql.params().set("c", id + "____");

		}

		List<Weixin_channel> list = weixinChannelService.list(sql);
		for (int i = 0; i < list.size(); i++) {
			Weixin_channel ch = list.get(i);
			boolean sign = false;
			String pid = ch.getId().substring(0, ch.getId().length() - 4);
			if (i == 0 || Strings.isBlank(pid))
				pid = "0";
			sql = Sqls.create("select count(*) from Weixin_channel where pid=@s and id like @c");
			sql.params().set("s", ch.getPid());
			sql.params().set("c", ch.getId() + "____");
			int num = weixinChannelService.getIntRowValue(sql);
			if (num > 0)
				sign = true;
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("id", ch.getId());
			obj.put("pId", pid);
			obj.put("isParent", sign);
			obj.put("name", ch.getName());
			obj.put("url", "javascript:changeClass(\"" + ch.getId() + "\")");
			obj.put("target", "_self");
			array.add(obj);
		}
		return Json.toJson(array);
	}

	/**
	 * 查询全部
	 */
	@At
	@Ok("raw")
	public String listAll(@Param("id") String id, @Param("pid") int pid) {
		return Json.toJson(getJSON(id, pid));
	}

	private List<Object> getJSON(String id, int proid) {
		Criteria cri = Cnd.cri();
		if (null == id || "".equals(id)) {
			cri.where().and("id", "like", "____");
		} else {
			cri.where().and("id", "like", id + "____");
		}
		cri.where().and("pid", "=", proid);
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Weixin_channel> list = weixinChannelService.listByCnd(cri);
		List<Object> array = new ArrayList<Object>();
		for (int i = 0; i < list.size(); i++) {
			Weixin_channel res = list.get(i);
			Map<String, Object> jsonobj = new HashMap<String, Object>();
			String pid = res.getId().substring(0, res.getId().length() - 4);
			if (i == 0 || "".equals(pid))
				pid = "0";
			int num = weixinChannelService.getRowCount(Cnd.where("id", "like", res.getId() + "____"));
			jsonobj.put("id", res.getId());
			jsonobj.put("name", res.getName());
			jsonobj.put("status", res.getStatus());
			jsonobj.put("_parentId", pid);
			if (num > 0) {
				jsonobj.put("children", getJSON(res.getId(), proid));
			}
			array.add(jsonobj);
		}
		return array;
	}

	/**
	 * 转到排序页面
	 */
	@At
	@Ok("vm:template.private.wx.Weixin_channelSort")
	public void toSort(HttpServletRequest req, @Param("pid") int pid) throws Exception {
		List<Object> array = new ArrayList<Object>();
		Criteria cri = Cnd.cri();
		cri.where().and("pid", "=", pid);
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Weixin_channel> list = weixinChannelService.listByCnd(cri);
		Map<String, Object> jsonroot = new HashMap<String, Object>();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "栏目列表");
		jsonroot.put("open", true);
		jsonroot.put("childOuter", false);
		jsonroot.put("icon", AppInfoService.APP_BASE_NAME + "/images/icons/icon042a1.gif");
		array.add(jsonroot);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> jsonobj = new HashMap<String, Object>();
			Weixin_channel obj = list.get(i);
			jsonobj.put("id", obj.getId());
			String p = obj.getId().substring(0, obj.getId().length() - 4);
			jsonobj.put("pId", "".equals(p) ? "0" : p);
			String name = obj.getName();
			jsonobj.put("name", name);
			jsonobj.put("childOuter", false);
			if (obj.getId().length() < 12) {
				jsonobj.put("open", true);
			} else {
				jsonobj.put("open", false);
			}
			array.add(jsonobj);
		}
		req.setAttribute("str", Json.toJson(array));
	}

	/**
	 * 确认排序
	 */
	@At
	@Ok("raw")
	public boolean sort(@Param("checkids") String[] checkids) {
		boolean rs = weixinChannelService.updateSortRow("Weixin_channel", checkids, "location", 0);
		return rs;

	}
}