package cn.xuetang.modules.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.xuetang.modules.app.bean.App_project;
import cn.xuetang.modules.sys.bean.Sys_user;
import org.apache.commons.lang.StringUtils;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.filter.UserLoginFilter;

import cn.xuetang.modules.user.bean.User_conn_wx;

/**
 * @author Wizzer
 * @time 2014-04-01 10:11:06
 */
@IocBean
@At("/private/user/connwx")
@Filters({@By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class)})
public class User_conn_wxAction extends BaseAction {
    @Inject
    protected Dao dao;

    @At
    @Ok("raw")
    public String getNikename(@Param("openid") String[] openid) {
        return Json.toJson(daoCtl.list(dao, User_conn_wx.class, Cnd.where("openid", "in", openid)));
    }

    @At("")
    @Ok("->:/private/user/User_conn_wx.html")
    public void index(@Param("sys_menu") String sys_menu, HttpSession session, HttpServletRequest req) {
        Sys_user user = (Sys_user) session.getAttribute("userSession");
        req.setAttribute("pro", daoCtl.list(dao, App_project.class, Cnd.where("id", "in", user.getProlist()).asc("id")));
        req.setAttribute("sys_menu", sys_menu);
    }

    @At
    @Ok("json")
    public User_conn_wx view(@Param("id") int id) {
        return daoCtl.detailById(dao, User_conn_wx.class, id);
    }

    @At
    @Ok("raw")
    public String list(@Param("pid")int pid,@Param("openid")String openid,@Param("sex") String sex, @Param("province") String province, @Param("city") String city, @Param("nickname") String nickname, @Param("page") int curPage, @Param("rows") int pageSize) {
        Criteria cri = Cnd.cri();
        cri.where().and("pid", "=", pid);
        if (!Strings.isBlank(sex) && !"all".equals(Strings.sNull(sex))) {
            cri.where().and("wx_sex", "=", sex);

        }
        if(!Strings.isBlank(openid)){
            cri.where().and("openid", "=", openid);

        }
        if(!Strings.isBlank(province)){
            cri.where().and("wx_province", "like", "%"+province+"%");

        }
        if(!Strings.isBlank(city)){
            cri.where().and("wx_city", "like", "%"+city+"%");

        }
        if(!Strings.isBlank(nickname)){
            cri.where().and("wx_nickname", "like", "%"+nickname+"%");

        }
        cri.getOrderBy().desc("id");
        return daoCtl.listPageJson(dao, User_conn_wx.class, curPage, pageSize, cri);
    }

}