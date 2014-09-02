package cn.xuetang.modules.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.util.DateUtil;
import cn.xuetang.common.util.WeixinUtil;
import cn.xuetang.modules.sys.bean.Sys_dict;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.modules.user.bean.User_conn_wx;
import cn.xuetang.modules.wx.bean.Weixin_push;
import cn.xuetang.modules.wx.bean.Weixin_push_content;
import cn.xuetang.modules.wx.bean.Weixin_push_user;
import cn.xuetang.service.AppInfoService;
import cn.xuetang.service.AppProjectService;
import cn.xuetang.service.WeixinPushContentService;
import cn.xuetang.service.WeixinPushService;
import cn.xuetang.service.WeixinPushUserService;
import cn.xuetang.service.sys.SysDictService;
import cn.xuetang.service.user.UserConnWXService;

/**
 * @author Wizzer
 * @time 2014-04-28 15:23:18
 */
@IocBean
@At("/private/wx/push")
public class Weixin_pushAction {
	@Inject
	private AppProjectService appProjectService;
	@Inject
	private AppInfoService appInfoService;
	@Inject
	private SysDictService sysDictService;
	@Inject
	private UserConnWXService userConnWXService;
	@Inject
	private WeixinPushService weixinPushService;
	@Inject
	private WeixinPushContentService weixinPushContentService;
	@Inject
	private WeixinPushUserService weixinPushUserService;
	private final static Log log = Logs.get();

	@At("")
	@Ok("vm:template.private.wx.Weixin_push")
	public void index(@Param("sys_menu") String sys_menu, HttpServletRequest req, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if (user.getSysrole()) {
			req.setAttribute("pro", appProjectService.listByCnd(Cnd.where("1", "=", 1).asc("id")));

		} else {
			req.setAttribute("pro", appProjectService.listByCnd(Cnd.where("id", "in", user.getProlist()).asc("id")));
		}
		req.setAttribute("sys_menu", sys_menu);
	}

	@At
	@Ok("vm:template.private.wx.Weixin_pushAdd")
	public void toadd(@Param("pid") int pid, HttpServletRequest req) {
		req.setAttribute("applist", appInfoService.listByCnd(Cnd.where("pid", "=", pid).and("app_type", "=", "01")));
		req.setAttribute("pid", pid);
		Pager pager = new Pager();
		pager.setPageNumber(1);
		pager.setPageSize(8);

	}

	@At
	@Ok("raw")
	public String getOneCity(@Param("zipcode") String zipcode) {
		if (!Strings.isBlank(zipcode)) {
			Sys_dict dict = sysDictService.fetch(Cnd.where("dkey", "=", zipcode));
			return dict.getDval();
		}
		return "";
	}

	@At
	public String getCity(@Param("zipcode") String zipcode) {
		if (!Strings.isBlank(zipcode)) {
			return Json.toJson(userConnWXService.list(Sqls.create("SELECT WX_CITY from User_conn_wx where WX_PROVINCE='" + zipcode + "' group by WX_CITY ORDER BY WX_CITY")));
		} else {
			return Json.toJson(userConnWXService.list(Sqls.create("SELECT WX_PROVINCE from User_conn_wx group by WX_PROVINCE ORDER BY WX_PROVINCE")));
		}
	}

	@At
	@Ok("raw")
	public boolean add(@Param("image_media_id") String image_media_id, @Param("voice_media_id") String voice_media_id, @Param("video_media_id") String video_media_id, @Param("..") Weixin_push weixin_push, @Param("contentids") Integer[] contentids, @Param("openids") String openid) {
		if ("voice".equals(Strings.sNull(weixin_push.getType()))) {
			weixin_push.setMedia_id(voice_media_id);
		} else if ("mpvideo".equals(Strings.sNull(weixin_push.getType()))) {
			weixin_push.setMedia_id(video_media_id);
		} else if ("image".equals(Strings.sNull(weixin_push.getType()))) {
			weixin_push.setMedia_id(image_media_id);
		}
		int push_num = 0;
		if (weixinPushService.insert(weixin_push)) {
			if ("mpnews".equals(Strings.sNull(weixin_push.getType()))) {
				weixinPushContentService.update(Chain.make("pushid", weixin_push.getId()).add("status", 1), Cnd.where("id", "in", contentids));
			}
			int type = weixin_push.getPush_type();
			if (type == 0) {// 全部用户
				Pager pager = new Pager();
				pager.setPageSize(10000);
				int count = userConnWXService.getIntRowValue(Sqls.create("SELECT COUNT(*) FROM user_conn_wx where appid=" + weixin_push.getAppid()));
				pager.setRecordCount(count);
				push_num = count;
				for (int p = 1; p <= pager.getPageCount(); p++) {
					pager.setPageNumber(p);
					Sql sql = Sqls.create("select openid from User_conn_wx where appid=@appid order by openid asc");
					sql.params().set("appid", weixin_push.getAppid());
					QueryResult queryResult = userConnWXService.listPagerSql(sql, pager);
					StringBuilder sb = new StringBuilder();
					int total = 0;
					for (Map map : queryResult.getList(Map.class)) {
						sb.append(Strings.sNull(map.get("openid")) + ",");
						total++;
					}
					Weixin_push_user user = new Weixin_push_user();
					user.setPagenum(p);
					user.setTotal(total);
					user.setPushid(weixin_push.getId());
					user.setOpenids(sb.toString());
					weixinPushUserService.insert(user);
				}
			} else if (type == 1) {// 节目用户

			} else if (type == 2) {// 选择范围
				List<String> openidList = new ArrayList<String>();
				List<String> openidListBB = new ArrayList<String>();
				if (weixin_push.getPush_sex() > 0 || !Strings.isBlank(weixin_push.getPush_age())) {
					Criteria cri = Cnd.cri();
					if (weixin_push.getPush_sex() > 0) {
						cri.where().and("WX_SEX", "=", weixin_push.getPush_sex());
					}
					List<User_conn_wx> list = userConnWXService.listByCnd(cri);
					for (User_conn_wx info : list) {
						if (!openidListBB.contains(info.getOpenid()))
							openidListBB.add(info.getOpenid());
					}
				}
				if (!Strings.isBlank(weixin_push.getPush_province())) {
					String s = "SELECT openid FROM user_conn_wx where 1=1 ";
					if (!Strings.isBlank(weixin_push.getPush_province())) {
						s += " and WX_PROVINCE='" + weixin_push.getPush_province() + "'";
					}
					if (!Strings.isBlank(weixin_push.getPush_city())) {
						s += " and WX_CITY='" + weixin_push.getPush_city() + "'";
					}
					List<Map> list = userConnWXService.listMap(Sqls.create(s));
					for (Map map : list) {
						if (!openidList.contains(Strings.sNull(map.get("openid")))) {
							openidList.add(Strings.sNull(map.get("openid")));
						}
					}
					if (openidListBB.size() > 0)
						openidList.retainAll(openidListBB);
				} else {
					openidList = openidListBB;
				}
				Pager pager = new Pager();
				pager.setRecordCount(openidList.size());
				pager.setPageSize(10000);
				for (int p = 0; p < pager.getPageCount(); p++) {
					int k = 0;
					StringBuilder sb = new StringBuilder();
					for (int j = 0; j < pager.getRecordCount(); j++) {
						int pageIndex = ((j + 1) + (pager.getPageSize() - 1)) / pager.getPageSize();
						if (pageIndex == (p + 1)) {
							sb.append(openidList.get(j) + ",");
							k++;
						}
						if ((j + 1) == ((j + 1) * pager.getPageSize())) {
							break;
						}
					}
					Weixin_push_user user = new Weixin_push_user();
					user.setPagenum(p + 1);
					user.setTotal(k);
					user.setPushid(weixin_push.getId());
					user.setOpenids(sb.toString());
					weixinPushUserService.insert(user);
				}
				push_num = openidList.size();
			} else if (type == 3) {// 用户列表
				String[] openids = StringUtils.split(openid, ",");
				if (openids != null && openids.length > 0) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < openids.length; i++) {
						sb.append(openids[i] + ",");
					}
					Weixin_push_user user = new Weixin_push_user();
					user.setPagenum(1);
					user.setTotal(openids.length);
					user.setPushid(weixin_push.getId());
					user.setOpenids(sb.toString());
					weixinPushUserService.insert(user);
				}
				push_num = openids.length;
			}
			weixin_push.setPush_num(push_num);
			if ("mpnews".equals(Strings.sNull(weixin_push.getType()))) {
				List<Weixin_push_content> contentList = weixinPushContentService.listByCnd(Cnd.where("pushid", "=", weixin_push.getId()));
				List<Object> list = new ArrayList<Object>();
				for (Weixin_push_content content : contentList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("thumb_media_id", content.getThumb_media_id());
					map.put("author", content.getAuthor());
					map.put("title", content.getTitle());
					map.put("content_source_url", content.getContent_source_url());
					map.put("content", content.getContent());
					map.put("digest", content.getDigest());
					list.add(map);
				}

				String res = WeixinUtil.PushNews(appInfoService.getGloalsAccessToken(weixin_push.getAppid()), list);
				log.debug("res::::::::::::::::::::" + res);
				Map<String, Object> re = Json.fromJson(Map.class, res);
				int errcode = NumberUtils.toInt(Strings.sNull(re.get("errcode")));
				if (errcode > 0) {
					weixin_push.setErrcode(errcode);
					weixin_push.setErrmsg(Strings.sNull(re.get("errmsg")));
					weixin_push.setStatus(2);// 创建失败
					weixin_push.setCreated_at(DateUtil.getCurDateTime());
					weixin_push.setMedia_id("");
				} else {
					weixin_push.setMedia_id(Strings.sNull(re.get("media_id")));
					weixin_push.setCreated_at(DateUtil.getCurDateTime());
					weixin_push.setStatus(1);// 创建成功

				}
				weixinPushService.update(weixin_push);
				if (!Strings.isBlank(weixin_push.getMedia_id())) {
					sendMedia(weixin_push, "mpnews");
				}
			} else if ("text".equals(Strings.sNull(weixin_push.getType()))) {
				weixin_push.setStatus(1);// 创建成功
				weixin_push.setCreated_at(DateUtil.getCurDateTime());
				weixinPushService.update(weixin_push);
				sendText(weixin_push);
			} else if ("voice".equals(Strings.sNull(weixin_push.getType()))) {
				weixin_push.setStatus(1);// 创建成功
				weixin_push.setCreated_at(DateUtil.getCurDateTime());
				weixinPushService.update(weixin_push);
				sendMedia(weixin_push, "voice");
			} else if ("image".equals(Strings.sNull(weixin_push.getType()))) {
				weixin_push.setStatus(1);// 创建成功
				weixin_push.setCreated_at(DateUtil.getCurDateTime());
				weixinPushService.update(weixin_push);
				;
				sendMedia(weixin_push, "image");
			} else if ("mpvideo".equals(Strings.sNull(weixin_push.getType()))) {
				weixin_push.setCreated_at(DateUtil.getCurDateTime());
				String res = WeixinUtil.PushVideo(appInfoService.getGloalsAccessToken(weixin_push.getAppid()), weixin_push.getMedia_id(), weixin_push.getPush_title(), weixin_push.getPush_description());
				log.debug("res::::::::::::::::::::" + res);
				Map<String, Object> re = Json.fromJson(Map.class, res);
				int errcode = NumberUtils.toInt(Strings.sNull(re.get("errcode")));
				if (errcode > 0) {
					weixin_push.setErrcode(errcode);
					weixin_push.setErrmsg(Strings.sNull(re.get("errmsg")));
					weixin_push.setStatus(2);// 创建失败
					weixin_push.setMedia_id("");
					weixin_push.setCreated_at(DateUtil.getCurDateTime());

				} else {
					weixin_push.setMedia_id(Strings.sNull(re.get("media_id")));
					weixin_push.setStatus(1);// 创建成功
					weixin_push.setCreated_at(DateUtil.getCurDateTime());

				}
				weixinPushService.update(weixin_push);
				;
				if (!Strings.isBlank(weixin_push.getMedia_id())) {
					sendVideo(weixin_push);
				}
			}
		}
		return weixin_push != null;
	}

	private void sendText(Weixin_push push) {
		if (push.getPush_num() > 0) {
			List<Weixin_push_user> userList = weixinPushUserService.listByCnd(Cnd.where("pushid", "=", push.getId()));
			for (Weixin_push_user user : userList) {
				Map<String, Object> obj = new HashMap<String, Object>();
				Map<String, Object> content = new HashMap<String, Object>();
				content.put("content", push.getPush_text());
				obj.put("msgtype", "text");
				obj.put("text", content);
				obj.put("touser", StringUtils.split(Strings.sNull(user.getOpenids()), ","));
				String res = WeixinUtil.PushUser(appInfoService.getGloalsAccessToken(push.getAppid()), obj);
				log.debug("sendText:" + res);
				Map<String, Object> re = Json.fromJson(Map.class, res);
				int errcode = NumberUtils.toInt(Strings.sNull(re.get("errcode")));
				if (errcode > 0) {
					user.setErrcode(errcode);
					user.setErrmsg(Strings.sNull(re.get("errmsg")));
					user.setStatus(2);// 提交失败
				} else {
					user.setMsg_id(NumberUtils.toLong(Strings.sNull(re.get("msg_id"))));
					user.setStatus(1);// 提交成功
				}
				weixinPushUserService.update(user);
			}
		}
	}

	private void sendMedia(Weixin_push push, String type) {
		if (push.getPush_num() > 0 && !Strings.isBlank(push.getMedia_id())) {
			List<Weixin_push_user> userList = weixinPushUserService.listByCnd(Cnd.where("pushid", "=", push.getId()));
			for (Weixin_push_user user : userList) {
				Map<String, Object> obj = new HashMap<String, Object>();
				Map<String, Object> mdeia = new HashMap<String, Object>();
				mdeia.put("media_id", push.getMedia_id());
				obj.put("msgtype", type);
				obj.put(type, mdeia);
				obj.put("touser", StringUtils.split(Strings.sNull(user.getOpenids()), ","));
				String res = WeixinUtil.PushUser(appInfoService.getGloalsAccessToken(push.getAppid()), obj);
				log.debug(res);
				Map<String, Object> re = Json.fromJson(Map.class, res);
				int errcode = NumberUtils.toInt(Strings.sNull(re.get("errcode")));
				if (errcode > 0) {
					user.setErrcode(errcode);
					user.setErrmsg(Strings.sNull(re.get("errmsg")));
					user.setStatus(2);// 提交失败
					// 提交失败，还原图文资料状态
					weixinPushContentService.update(Chain.make("status", 0), Cnd.where("pushid", "=", push.getId()));
				} else {
					user.setMsg_id(NumberUtils.toLong(Strings.sNull(re.get("msg_id"))));
					user.setStatus(1);// 提交成功
				}
				weixinPushUserService.update(user);
			}
		}
	}

	private void sendVideo(Weixin_push push) {
		if (push.getPush_num() > 0 && !Strings.isBlank(push.getMedia_id())) {
			List<Weixin_push_user> userList = weixinPushUserService.listByCnd(Cnd.where("pushid", "=", push.getId()));
			for (Weixin_push_user user : userList) {
				Map<String, Object> obj = new HashMap<String, Object>();
				Map<String, Object> mdeia = new HashMap<String, Object>();
				mdeia.put("media_id", push.getMedia_id());
				mdeia.put("title", push.getPush_title());
				mdeia.put("description", push.getPush_description());
				obj.put("msgtype", "mpvideo");
				obj.put("mpvideo", mdeia);
				obj.put("touser", StringUtils.split(Strings.sNull(user.getOpenids()), ","));
				String res = WeixinUtil.PushUser(appInfoService.getGloalsAccessToken(push.getAppid()), obj);
				log.debug(res);
				Map<String, Object> re = Json.fromJson(Map.class, res);
				int errcode = NumberUtils.toInt(Strings.sNull(re.get("errcode")));
				if (errcode > 0) {
					user.setErrcode(errcode);
					user.setErrmsg(Strings.sNull(re.get("errmsg")));
					user.setStatus(2);// 提交失败
				} else {
					user.setMsg_id(NumberUtils.toLong(Strings.sNull(re.get("msg_id"))));
					user.setStatus(1);// 提交成功
				}
				weixinPushUserService.update(user);
			}
		}
	}

	@At
	@Ok("vm:template.private.wx.Weixin_pushView")
	public Weixin_push view(@Param("id") int id, HttpServletRequest req) {
		Weixin_push push = weixinPushService.fetch(id);
		req.setAttribute("applist", appInfoService.listByCnd(Cnd.where("pid", "=", push.getPid()).and("app_type", "=", "01")));
		req.setAttribute("list", weixinPushUserService.listByCnd(Cnd.where("pushid", "=", id)));
		return push;// html:obj
	}

	@At
	public boolean deleteIds(@Param("ids") Integer[] ids) {
		// daoCtl.delete(dao, Weixin_push_content.class, Cnd.where("pushid",
		// "in", ids));
		// 删除push时，还原图文资料
		weixinPushContentService.update(Chain.make("status", 0), Cnd.where("pushid", "in", ids));
		weixinPushUserService.delete(Cnd.where("pushid", "in", ids));
		return weixinPushService.delete(Cnd.where("id", "in", ids)) > 0;
	}

	@At
	@Ok("raw")
	public String list(@Param("pid") int pid, @Param("push_name") String push_name, @Param("push_type") String push_type, @Param("type") String type, @Param("page") int curPage, @Param("rows") int pageSize) {
		Criteria cri = Cnd.cri();
		cri.where().and("pid", "=", pid);
		if (!Strings.isBlank(push_name)) {
			cri.where().and("pid", "=", pid);
		}
		if (!"all".equals(Strings.sNull(push_type))) {
			cri.where().and("push_type", "=", NumberUtils.toInt(Strings.sNull(push_type)));
		}
		if (!"all".equals(Strings.sNull(type))) {
			cri.where().and("type", "=", type);
		}
		cri.getOrderBy().desc("id");
		return weixinPushService.listPageJson(curPage, pageSize, cri);
	}

}