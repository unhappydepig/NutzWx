package cn.xuetang.modules.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.modules.user.bean.User_conn_wx;
import cn.xuetang.service.AppProjectService;
import cn.xuetang.service.UserConnWXService;

/**
 * @author Wizzer
 * @time 2014-04-01 10:11:06
 */
@IocBean
@At("/private/user/connwx")
public class User_conn_wxAction {
	@Inject
	private AppProjectService appProjectService;
	@Inject
	private UserConnWXService userConnWXService;

	@At
	@Ok("raw")
	public String getNikename(@Param("openid") String[] openid) {
		return Json.toJson(userConnWXService.listByCnd(Cnd.where("openid", "in", openid)));
	}

	@At("")
	@Ok("vm:template.private.user.User_conn_wx")
	public void index(@Param("sys_menu") String sys_menu, HttpSession session, HttpServletRequest req) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		req.setAttribute("pro", appProjectService.listByCnd(Cnd.where("id", "in", user.getProlist()).asc("id")));
		req.setAttribute("sys_menu", sys_menu);
	}

	@At
	@Ok("json")
	public User_conn_wx view(@Param("id") int id) {
		return userConnWXService.fetch(id);
	}

	@At
	@Ok("raw")
	public String list(@Param("pid") int pid, @Param("openid") String openid, @Param("sex") String sex, @Param("province") String province, @Param("city") String city, @Param("nickname") String nickname, @Param("page") int curPage, @Param("rows") int pageSize) {
		Criteria cri = Cnd.cri();
		cri.where().and("pid", "=", pid);
		if (StringUtils.isNotBlank(sex) && !"all".equals(Strings.sNull(sex))) {
			cri.where().and("wx_sex", "=", sex);

		}
		if (StringUtils.isNotBlank(openid)) {
			cri.where().and("openid", "=", openid);
		}
		if (StringUtils.isNotBlank(province)) {
			cri.where().and("wx_province", "like", "%" + province + "%");

		}
		if (StringUtils.isNotBlank(city)) {
			cri.where().and("wx_city", "like", "%" + city + "%");

		}
		if (StringUtils.isNotBlank(nickname)) {
			cri.where().and("wx_nickname", "like", "%" + nickname + "%");
		}
		cri.getOrderBy().desc("id");
		return userConnWXService.listPageJson(curPage, pageSize, cri);
	}
}