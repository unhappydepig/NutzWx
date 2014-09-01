package cn.xuetang.modules.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.util.DateUtil;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.modules.user.bean.User_score;
import cn.xuetang.modules.user.bean.User_score_change;
import cn.xuetang.service.AppProjectService;
import cn.xuetang.service.UserAccountService;
import cn.xuetang.service.UserScoreChangeService;
import cn.xuetang.service.UserScoreService;

/**
 * @author Wizzer
 * @time 2014-04-01 10:11:06
 */
@IocBean
@At("/private/user/score")
public class User_scoreAction {
	private final static Log log = Logs.get();

	@Inject
	private UserAccountService userAccountService;
	@Inject
	private UserScoreChangeService userScoreChangeService;
	@Inject
	private UserScoreService userScoreService;
	@Inject
	private AppProjectService appProjectService;

	@At("")
	@Ok("vm:template.private.user.User_score")
	public void index(@Param("sys_menu") String sys_menu, HttpSession session, HttpServletRequest req) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		req.setAttribute("pro", appProjectService.listByCnd(Cnd.where("id", "in", user.getProlist()).asc("id")));
		req.setAttribute("sys_menu", sys_menu);
	}

	@At("/indexChang")
	@Ok("vm:template.private.user.User_scoreChange")
	public void indexChang(@Param("uid") int uid, HttpServletRequest req) {
		req.setAttribute("uid", uid);
	}

	@At
	@Ok("vm:template.private.user.User_scoreChangeAdd")
	public void toadd(@Param("uid") int uid, HttpServletRequest req) {
		req.setAttribute("uid", uid);
	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") User_score_change user_score, @Param("score") int jfscore) {
		User_score score = userScoreService.fetch(Cnd.where("uid", "=", user_score.getUid()));
		user_score.setScore_pre(score.getScore());
		user_score.setScore_next(score.getScore() + jfscore);
		user_score.setAdd_time(DateUtil.getCurDateTime());
		if (user_score.getUid() > 0) {
			boolean res = userScoreChangeService.insert(user_score);
			if (res) {
				score.setB_score(score.getB_score() + jfscore);
				score.setScore(score.getScore() + jfscore);
				userScoreService.update(score);
			}
			return res;
		}
		return false;
	}

	@At
	public boolean deleteIds(@Param("ids") String[] ids) {
		return userScoreChangeService.deleteByIds(ids);
	}

	@At
	@Ok("raw")
	public String list(@Param("pid") int pid, @Param("loginname") String loginname, @Param("name") String name, @Param("nickname") String nickname, @Param("mobile") String mobile, @Param("page") int curPage, @Param("rows") int pageSize) {
		long a = System.currentTimeMillis();
		String str = "SELECT a.*,b.*,c.* FROM USER_ACCOUNT a,USER_INFO b,USER_SCORE c WHERE a.UID=b.UID and a.UID=c.UID and a.PID=" + pid;
		if (StringUtils.isNotBlank(name)) {
			str += " and b.name like '%" + name + "%'";
		}
		if (StringUtils.isNotBlank(nickname)) {
			str += " and b.nickname like '%" + nickname + "%'";
		}
		if (StringUtils.isNotBlank(mobile)) {
			str += " and b.mobile like '%" + mobile + "%'";
		}
		if (StringUtils.isNotBlank(loginname)) {
			str += " and a.loginname like '%" + loginname + "%'";
		}
		str += " order by c.score desc";
		int count = userAccountService.getIntRowValue(Sqls.create("SELECT COUNT(1) " + str.substring(str.indexOf("FROM"))));
		log.debug("/private/user/infolist Load in " + (System.currentTimeMillis() - a) + "ms");
		return userAccountService.listPageJsonSql(Sqls.create(str), curPage, pageSize, count);
	}

	@At
	@Ok("raw")
	public String listChange(@Param("uid") int uid, @Param("page") int curPage, @Param("rows") int pageSize) {
		Criteria cri = Cnd.cri();
		cri.where().and("uid", "=", uid);
		cri.getOrderBy().desc("id");
		return userScoreChangeService.listPageJson(curPage, pageSize, cri);
	}

}