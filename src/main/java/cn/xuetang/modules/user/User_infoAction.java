package cn.xuetang.modules.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.config.Dict;
import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.filter.UserLoginFilter;
import cn.xuetang.common.util.DateUtil;
import cn.xuetang.common.util.DecodeUtil;
import cn.xuetang.modules.app.bean.App_project;
import cn.xuetang.modules.sys.bean.Sys_dict;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.modules.user.bean.User_account;
import cn.xuetang.modules.user.bean.User_conn_wx;
import cn.xuetang.modules.user.bean.User_info;
import cn.xuetang.modules.user.bean.User_score;
import cn.xuetang.service.AppInfoService;

/**
 * @author Wizzer
 * @time 2014-04-01 10:11:06
 */
@IocBean
@At("/private/user/info")
@Filters({@By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class)})
public class User_infoAction extends BaseAction {
    @Inject
    protected Dao dao;
    private final static Log log = Logs.get();

	@Inject
	private AppInfoService appInfoService;
	
    @At
    @Ok("vm:template.private.user.User_info")
    public void index(@Param("sys_menu") String sys_menu, HttpSession session, HttpServletRequest req) {
        Sys_user user = (Sys_user) session.getAttribute("userSession");
        req.setAttribute("pro", daoCtl.list(dao, App_project.class, Cnd.where("id", "in", user.getProlist()).asc("id")));
        req.setAttribute("sys_menu", sys_menu);
    }

    @At
    @Ok("vm:template.private.user.User_infoUpdate")
    public User_info toupdate(@Param("uid") int uid, HttpServletRequest req) {
        req.setAttribute("acc", daoCtl.detailById(dao, User_account.class, uid));
        req.setAttribute("jf", daoCtl.detailById(dao, User_score.class, uid));
        req.setAttribute("wx", daoCtl.detailByCnd(dao, User_conn_wx.class, Cnd.where("uid", "=", uid)));
        req.setAttribute("level", appInfoService.DATA_DICT.get(Dict.USER_LEVEL));
        return daoCtl.detailById(dao, User_info.class, uid);//html:obj
    }

    @At
    @Ok("vm:template.private.user.User_infoView")
    public User_info view(@Param("uid") int uid, HttpServletRequest req) {
        req.setAttribute("acc", daoCtl.detailById(dao, User_account.class, uid));
        req.setAttribute("jf", daoCtl.detailById(dao, User_score.class, uid));
        req.setAttribute("wx", daoCtl.detailByCnd(dao, User_conn_wx.class, Cnd.where("uid", "=", uid)));
        User_info info = daoCtl.detailById(dao, User_info.class, uid);
		Map<String, String> dict = (HashMap) appInfoService.DATA_DICT.get(Dict.DIVSION);
        Map<String, String> level = (HashMap) appInfoService.DATA_DICT.get(Dict.USER_LEVEL);
        info.setProvince(Strings.sNull(dict.get(info.getProvince())));
        info.setCity(Strings.sNull(dict.get(info.getCity())));
        info.setArea(Strings.sNull(dict.get(info.getArea())));
        info.setUser_level(Strings.sNull(level.get(info.getUser_level())));
        return info;//html:obj
    }

    @At
    public String getCity(@Param("zipcode") String zipcode) {
        List<Sys_dict> list;
        if (!Strings.isBlank(zipcode)) {
            Sys_dict dict = daoCtl.detailByCnd(dao, Sys_dict.class, Cnd.where("dkey", "=", zipcode));
            list = daoCtl.list(dao, Sys_dict.class, Cnd.where("id", "like", dict.getId() + "____"));
        } else {
            list = daoCtl.list(dao, Sys_dict.class, Cnd.where("id", "like", Dict.DIVSION+"____"));
        }
        return Json.toJson(list);
    }

    @At
    public boolean update(@Param("::acc.") User_account user_account, @Param("::info.") User_info user_info) {
        boolean res = daoCtl.update(dao, user_info);
        Chain chain;
        if (!Strings.isBlank(user_account.getPassword())) {
            String salt = DecodeUtil.getSalt(6);
            String pass = Lang.digest("MD5", user_account.getPassword().getBytes(), salt.getBytes(), 3);
            chain = Chain.make("password", pass).add("salt", salt).add("status", user_account.getStatus());
        } else {
            chain = Chain.make("status", user_account.getStatus());
        }
        daoCtl.update(dao, User_account.class, chain, Cnd.where("uid", "=", user_account.getUid()));

        return res;
    }

    @At
    public boolean delete(@Param("uid") int uid) {
        return daoCtl.deleteById(dao, User_info.class, uid);
    }

    @At
    public boolean deleteIds(@Param("ids") String ids) {
        return daoCtl.deleteByIds(dao, User_info.class, StringUtils.split(ids, ","));
    }

    @At
    @Ok("raw")
    public String list(@Param("pid") int pid, @Param("name") String name, @Param("loginname") String loginname, @Param("email") String email, @Param("nickname") String nickname,
                       @Param("ageStart") int ageStart, @Param("ageEnd") int ageEnd,
                       @Param("page") int curPage, @Param("sex") String sex, @Param("rows") int pageSize) {
        long a = System.currentTimeMillis();
        String str = "SELECT a.*,b.* FROM USER_ACCOUNT a,USER_INFO b WHERE a.UID=b.UID and a.PID=" + pid;
        if (!Strings.isBlank(name)) {
            str += " and b.name like '%" + name + "%'";
        }
        if (!Strings.isBlank(nickname)) {
            str += " and b.nickname like '%" + nickname + "%'";
        }
        if (!Strings.isBlank(loginname)) {
            str += " and a.loginname like '%" + loginname + "%'";
        }
        if (!Strings.isBlank(email)) {
            str += " and a.email like '%" + email + "%'";
        }
        if (!Strings.isBlank(sex) && !"all".equals(Strings.sNull(sex))) {
            str += " and b.sex=" + sex;
        }
        if (ageStart > 0) {
            int curyear = NumberUtils.toInt(DateUtil.getTime("yyyy"));
            str += " and b.birth_year <=" + (curyear - ageStart);
        }
        if (ageEnd > 0) {
            int curyear = NumberUtils.toInt(DateUtil.getTime("yyyy"));
            str += " and b.birth_year >=" + (curyear - ageEnd);
        }
        str += " order by a.uid desc";
        int count = daoCtl.getIntRowValue(dao, Sqls.create("SELECT COUNT(1) " + str.substring(str.indexOf("FROM"))));
        log.debug("/private/user/infolist Load in " + (System.currentTimeMillis() - a) + "ms");
        return daoCtl.listPageJsonSql(dao, Sqls.create(str), curPage, pageSize, count);
    }

}