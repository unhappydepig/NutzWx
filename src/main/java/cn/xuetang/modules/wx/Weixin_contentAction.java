package cn.xuetang.modules.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;
import org.nutz.web.Webs;

import cn.xuetang.common.file.FileType;
import cn.xuetang.common.util.DateUtil;
import cn.xuetang.common.util.DecodeUtil;
import cn.xuetang.common.util.StringUtil;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.modules.wx.bean.Weixin_channel;
import cn.xuetang.modules.wx.bean.Weixin_channel_attr;
import cn.xuetang.modules.wx.bean.Weixin_content;
import cn.xuetang.modules.wx.bean.Weixin_content_txt;
import cn.xuetang.service.AppInfoService;
import cn.xuetang.service.AppProjectService;
import cn.xuetang.service.WeixinContentAttrService;
import cn.xuetang.service.WeixinContentService;
import cn.xuetang.service.WeixinContentTXTService;
import cn.xuetang.service.wx.WeixinChannelAttrService;
import cn.xuetang.service.wx.WeixinChannelService;

/**
 * @author Wizzer
 * @time 2014-04-01 11:49:27
 */
@IocBean
@At("/private/wx/content")
public class Weixin_contentAction{
	@Inject
	private WeixinChannelService weixinChannelService;
	@Inject
	private WeixinChannelAttrService weixinChannelAttrService;
	@Inject
	private WeixinContentAttrService weixinContentAttrService;
	@Inject
	private AppProjectService appProjectService;
	@Inject
	private WeixinContentTXTService weixinContentTXTService;
	@Inject
	private WeixinContentService weixinContentService; 
	@Inject
	private UploadAdaptor upload;
	private final static Log log = Logs.get();
	@Inject
	private AppInfoService appInfoService;

	@At("")
	@Ok("vm:template.private.wx.Weixin_content")
	public void index(@Attr(Webs.ME)Sys_user user,@Param("sys_menu") String sys_menu, HttpSession session, HttpServletRequest req) {
		if (user.getSysrole()) {
			req.setAttribute("pro", appProjectService.listByCnd(Cnd.where("1", "=", 1).asc("id")));

		} else {
			req.setAttribute("pro", appProjectService.listByCnd(Cnd.where("id", "in", user.getProlist()).asc("id")));
		}
		req.setAttribute("sys_menu", sys_menu);
	}

	@At
	@Ok("raw")
	public boolean del(@Param("ids") String[] ids) {
		weixinContentService.update(Chain.make("status", -1), Cnd.where("id", "in", ids));
		return true;
	}

	@At
	@Ok("raw")
	public boolean setStatus(@Param("type") String type, @Param("appid") int appid, @Param("status") int status, @Param("ids") String[] ids) {
		try {
			for (int i = 0; i < ids.length; i++) {
				int id = NumberUtils.toInt(ids[i]);
				if ("pub".equals(Strings.sNull(type))) {
					weixinContentService.update(Chain.make("status", 1).add("send_type", 0), Cnd.where("id", "=", id));
				} else if ("push".equals(Strings.sNull(type))) {
					weixinContentService.update(Chain.make("status", 1).add("send_type", 1), Cnd.where("id", "=", id));

				} else if ("recall".equals(type)) {
					weixinContentService.update(Chain.make("status", 0).add("send_type", 0), Cnd.where("id", "=", id));
					weixinContentService.exeUpdateBySql(Sqls.create("update Weixin_content_push set status=1 where id=" + id));
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	@At
	@Ok("vm:template.private.wx.Weixin_contentPush")
	public void topush(@Param("pid") int pid, HttpServletRequest req) {
		req.setAttribute("applist", appInfoService.listByCnd(Cnd.where("pid", "=", pid).and("app_type", "=", "01")));
	}

	@At
	@Ok("vm:template.private.wx.Weixin_contentEdit")
	public void edit(@Param("id") int id, @Param("pid") int pid, @Param("channel_id") String channel_id, HttpServletRequest req) {
		if (id > 0) {
			req.setAttribute("content", weixinContentService.fetch(id));
			Weixin_content_txt txt = weixinContentTXTService.fetch(id);
			if (txt != null) {
				req.setAttribute("ctxt", txt);
			}
			req.setAttribute("id", id);
			Map<String, String> attrMap = weixinContentAttrService.getHashMap(Sqls.create("select attr_code,attr_value from Weixin_content_attr where gid=" + id));
			req.setAttribute("attrMap", attrMap);
		}
		req.setAttribute("pid", pid);
		req.setAttribute("channel_id", channel_id);
		req.setAttribute("allow_size", upload.getContext().getMaxFileSize());
		req.setAttribute("allow_images", fileTypeExts(FileType.getSuffixname("images")));
		req.setAttribute("allow_video", fileTypeExts(FileType.getSuffixname("video")));
		req.setAttribute("allow_other", fileTypeExts(FileType.getSuffixname("other")));
		req.setAttribute("timenow", DateUtil.getCurDateTime());
		req.setAttribute("file_username", appInfoService.getSYS_CONFIG().get("file_username"));
		req.setAttribute("file_password", DecodeUtil.Encrypt(appInfoService.getSYS_CONFIG().get("file_password"), "file"));
		req.setAttribute("file_uploadurl", appInfoService.getSYS_CONFIG().get("file_uploadurl"));
		req.setAttribute("file_domain", appInfoService.getSYS_CONFIG().get("file_domain"));
		List<Weixin_channel_attr> attrList = weixinChannelAttrService.listByCnd(Cnd.where("classid", "=", channel_id).asc("attr_code"));
		req.setAttribute("attrList", attrList);
		req.setAttribute("StringUtil", new StringUtil());

	}

	@At
	@Ok("raw")
	public int doSave(final @Attr(Webs.ME)Sys_user user,@Param("::attr.") final Map<String, String> map2, @Param("status") final int status, @Param("appid") final int appid, @Param("type") final int type, @Param("::content.") final Weixin_content content1, @Param("::contenttxt.") final Weixin_content_txt txt1, final HttpSession session, final HttpServletRequest req) {
		try {
			final ThreadLocal<Integer> re = new ThreadLocal<Integer>();
			Trans.exec(new Atom() {
				public void run() {
					Map<String, String> map = map2;
					if (content1.getId() < 1) {
						Weixin_content content = content1;
						content.setAdd_time(DateUtil.getCurDateTime());
						content.setAdd_userid(user.getUserid());
						content.setStatus(status);
						content.setSend_type(type);
						Weixin_content c = content;
						try {
							weixinContentService.insert(content);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (c != null) {
							if (map != null) {
								Sql sqlattr = Sqls.create("insert into Weixin_content_attr(gid,attr_code,attr_value) values(@a,@b,@c)");
								for (Map.Entry<String, String> entry : map.entrySet()) {
									sqlattr.params().set("a", c.getId());
									sqlattr.params().set("b", entry.getKey());
									sqlattr.params().set("c", entry.getValue());
									sqlattr.addBatch();
								}
								weixinContentAttrService.exeUpdateBySql(sqlattr);
							}
							Weixin_content_txt t = txt1;
							t.setId(c.getId());
							weixinContentTXTService.insert(t);
							// if (type == 1) {
							// Weixin_content_push push = new
							// Weixin_content_push();
							// push.setId(c.getId());
							// push.setAppid(appid);
							// daoCtl.add(dao, push);
							// }
							re.set(c.getId());
						} else {
							throw new RuntimeException();
						}
					} else {
						Weixin_content content = content1;
						content.setAdd_time(DateUtil.getCurDateTime());
						content.setAdd_userid(user.getUserid());
						content.setStatus(status);
						content.setSend_type(type);
						weixinContentService.update(content);
						weixinContentAttrService.delete("Weixin_content_attr", Cnd.where("gid", "=", content.getId()));
						if (map != null) {
							Sql sqlattr = Sqls.create("insert into Weixin_content_attr(gid,attr_code,attr_value) values(@a,@b,@c)");
							for (Map.Entry<String, String> entry : map.entrySet()) {
								sqlattr.params().set("a", content.getId());
								sqlattr.params().set("b", entry.getKey());
								sqlattr.params().set("c", entry.getValue());
								sqlattr.addBatch();
							}
							weixinContentAttrService.exeUpdateBySql(sqlattr);
						}
						Weixin_content_txt t = txt1;
						t.setId(content.getId());
						weixinContentTXTService.update(t);
						re.set(content.getId());
					}
				}
			});
			return re.get();
		} catch (Exception e) {
			log.debug(e.getMessage());
			return 0;
		}
	}

	/**
	 * 转换文件类型格式
	 * 
	 * @param str
	 * @return
	 */
	private String fileTypeExts(String str) {
		String[] temp = str.split(",");
		String rt = "";
		for (int i = 0; i < temp.length; i++) {
			rt += "*." + temp[i] + ";";
		}
		return rt;
	}

	@At
	@Ok("vm:template.private.wx.Weixin_contentUpdate")
	public Weixin_content toupdate(@Param("id") int id, HttpServletRequest req) {
		return weixinContentService.fetch(id);// html:obj
	}

	@At
	public boolean update(@Param("..") Weixin_content weixin_content) {
		return weixinContentService.update(weixin_content);
	}

	@At
	@Ok("raw")
	public String list(@Param("queryStatus") String queryStatus, @Param("queryPubtimeStart") String queryPubtimeStart, @Param("queryPubtimeEnd") String queryPubtimeEnd, @Param("queryTitle") String queryTitle, @Param("queryOrderBy") int queryOrderBy, @Param("channel_id") String channel_id, @Param("pid") int pid, @Param("page") int curPage, @Param("rows") int pageSize) {
		Criteria cri = Cnd.cri();
		cri.where().and("pid", "=", pid);
		if (!Strings.isBlank(channel_id)) {
			cri.where().and("channel_id", "=", channel_id);
		}
		if (!Strings.isBlank(queryTitle)) {
			cri.where().and("title", "like", "%" + queryTitle + "%");
		}
		if (!Strings.isBlank(queryPubtimeStart)) {
			cri.where().and("pub_time", ">=", queryPubtimeStart + " 00:00:00");
		}
		if (!Strings.isBlank(queryPubtimeEnd)) {
			cri.where().and("pub_time", "<=", queryPubtimeEnd + " 00:00:00");
		}
		if (!Strings.isBlank(queryStatus)) {
			if ("draft".equals(queryStatus)) {
				cri.where().and("status", "=", 0);
			} else if ("pub".equals(queryStatus)) {
				cri.where().and("status", "=", 1);
			} else if ("del".equals(queryStatus)) {
				cri.where().and("status", "=", -1);
			} else
				cri.where().and("status", ">", -1);
		} else
			cri.where().and("status", ">", -1);
		if (queryOrderBy == 0) {
			cri.getOrderBy().desc("id");
		} else if (queryOrderBy == 1) {
			cri.getOrderBy().asc("id");
		} else if (queryOrderBy == 2) {
			cri.getOrderBy().desc("pub_time");
		} else if (queryOrderBy == 3) {
			cri.getOrderBy().asc("pub_time");
		}
		return weixinContentService.listPageJson(curPage, pageSize, cri);
	}

	@At
	@Ok("raw")
	public String treelist(@Param("id") String channel_id, @Param("pid") int proid, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Sql sql;

		List<Object> array = new ArrayList<Object>();
		if (Strings.isBlank(channel_id)) {
			Map<String, String> jsonroot = new HashMap<String, String>();
			jsonroot.put("id", "");
			jsonroot.put("pId", "0");
			jsonroot.put("name", "栏目列表");
			jsonroot.put("url", "javascript:changeChannel(\"\")");
			jsonroot.put("target", "_self");
			jsonroot.put("icon", appInfoService.getAPP_BASE_NAME() + "/images/icons/icon042a1.gif");
			array.add(jsonroot);
			if (user.getRolelist().contains(2)) {
				sql = Sqls.create("select * from weixin_channel where pid=@s and id like @c order by location asc");
			} else {
				sql = Sqls.create("select * from weixin_channel where pid=@s and id like @c and id in $d order by location asc");
				sql.vars().set("d", Cnd.wrap("(select channel_id from weixin_channel_role where role_id in " + StringUtil.getIdsplit(user.getRolelist()) + ")"));
			}
			sql.params().set("s", proid);
			sql.params().set("c", "____");
		} else {
			if (user.getRolelist().contains(2)) {
				sql = Sqls.create("select * from weixin_channel where pid=@s and id like @c order by location asc");
			} else {
				sql = Sqls.create("select * from weixin_channel where pid=@s and id like @c and id in $d order by location asc");
				sql.vars().set("d", Cnd.wrap("(select channel_id from weixin_channel_role where role_id in " + StringUtil.getIdsplit(user.getRolelist()) + ")"));
			}
			sql.params().set("s", proid);
			sql.params().set("c", channel_id + "____");

		}

		List<Weixin_channel> list = weixinChannelService.list(sql);
		for (int i = 0; i < list.size(); i++) {
			Weixin_channel ch = list.get(i);
			boolean sign = false;
			String pid = ch.getId().substring(0, ch.getId().length() - 4);
			if (i == 0 || "".equals(pid))
				pid = "0";
			sql = Sqls.create("select count(*) from weixin_channel where pid=@s and id like @c");
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
			obj.put("url", "javascript:changeChannel(\"" + ch.getId() + "\")");
			obj.put("target", "_self");
			array.add(obj);
		}
		return Json.toJson(array);
	}
}