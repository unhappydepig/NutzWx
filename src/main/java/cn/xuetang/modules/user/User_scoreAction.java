package cn.xuetang.modules.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.xuetang.common.util.DateUtil;
import cn.xuetang.modules.app.bean.App_project;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.modules.user.bean.User_score_change;
import org.apache.commons.lang.StringUtils;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.filter.UserLoginFilter;

import cn.xuetang.modules.user.bean.User_score;

/**
 * @author Wizzer
 * @time 2014-04-01 10:11:06
 */
@IocBean
@At("/private/user/score")
@Filters({@By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class)})
public class User_scoreAction extends BaseAction {
    @Inject
    protected Dao dao;
    private final static Log log = Logs.get();

    @At("")
    @Ok("->:/private/user/User_score.html")
    public void index(@Param("sys_menu") String sys_menu, HttpSession session, HttpServletRequest req) {
        Sys_user user = (Sys_user) session.getAttribute("userSession");
        req.setAttribute("pro", daoCtl.list(dao, App_project.class, Cnd.where("id", "in", user.getProlist()).asc("id")));
        req.setAttribute("sys_menu", sys_menu);
    }

    @At("/indexChang")
    @Ok("->:/private/user/User_scoreChange.html")
    public void indexChang(@Param("uid") int uid, HttpServletRequest req) {
        req.setAttribute("uid", uid);
    }

    @At
    @Ok("->:/private/user/User_scoreChangeAdd.html")
    public void toadd(@Param("uid") int uid, HttpServletRequest req) {
        req.setAttribute("uid", uid);
    }

    @At
    @Ok("raw")
    public boolean add(@Param("..") User_score_change user_score, @Param("score") int jfscore) {
        User_score score = daoCtl.detailByCnd(dao, User_score.class, Cnd.where("uid", "=", user_score.getUid()));
        user_score.setScore_pre(score.getScore());
        user_score.setScore_next(score.getScore() + jfscore);
        user_score.setAdd_time(DateUtil.getCurDateTime());
        if (user_score.getUid() > 0) {
            boolean res = daoCtl.add(dao, user_score);
            if (res) {
                score.setB_score(score.getB_score() + jfscore);
                score.setScore(score.getScore() + jfscore);
                daoCtl.update(dao, score);
            }
            return res;
        }
        return false;
    }

    @At
    public boolean deleteIds(@Param("ids") String ids, @Param("uid") int uid) {
        return daoCtl.deleteByIds(dao, User_score_change.class, StringUtils.split(ids, ","));
    }

    @At
    @Ok("raw")
    public String list(@Param("pid") int pid, @Param("loginname") String loginname, @Param("name") String name,
                       @Param("nickname") String nickname, @Param("mobile") String mobile, @Param("page") int curPage, @Param("rows") int pageSize) {
        long a = System.currentTimeMillis();
        String str = "SELECT a.*,b.*,c.* FROM USER_ACCOUNT a,USER_INFO b,USER_SCORE c WHERE a.UID=b.UID and a.UID=c.UID and a.PID=" + pid;
        if (!Strings.isBlank(name)) {
            str += " and b.name like '%" + name + "%'";
        }
        if (!Strings.isBlank(nickname)) {
            str += " and b.nickname like '%" + nickname + "%'";
        }
        if (!Strings.isBlank(mobile)) {
            str += " and b.mobile like '%" + mobile + "%'";
        }
        if (!Strings.isBlank(loginname)) {
            str += " and a.loginname like '%" + loginname + "%'";
        }
        str += " order by c.score desc";
        int count = daoCtl.getIntRowValue(dao, Sqls.create("SELECT COUNT(1) " + str.substring(str.indexOf("FROM"))));
        log.debug("/private/user/infolist Load in " + (System.currentTimeMillis() - a) + "ms");
        return daoCtl.listPageJsonSql(dao, Sqls.create(str), curPage, pageSize, count);
    }

    @At
    @Ok("raw")
    public String listChange(@Param("uid") int uid, @Param("page") int curPage, @Param("rows") int pageSize) {
        Criteria cri = Cnd.cri();
        cri.where().and("uid", "=", uid);
        cri.getOrderBy().desc("id");
        return daoCtl.listPageJson(dao, User_score_change.class, curPage, pageSize, cri);
    }

}