package cn.xuetang.modules.app;

import javax.servlet.http.HttpServletRequest;

import cn.xuetang.common.config.Dict;
import cn.xuetang.common.config.Globals;
import cn.xuetang.common.util.DateUtil;
import cn.xuetang.common.util.DecodeUtil;
import cn.xuetang.common.util.SyncUtil;
import cn.xuetang.modules.app.bean.App_project;
import org.apache.commons.lang.StringUtils;
import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.filter.UserLoginFilter;
import cn.xuetang.modules.app.bean.App_info;

/**
 * @author Wizzer
 * @time 2014-03-29 13:12:58
 */
@IocBean
@At("/private/app/info")
@Filters({@By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class)})
public class App_infoAction extends BaseAction {
    @Inject
    protected Dao dao;
    @Inject
    protected SyncUtil syncUtil;//通知前台服务器初始化参数

    @At("")
    @Ok("->:/private/app/App_info.html")
    public void index(@Param("sys_menu") String sys_menu, HttpServletRequest req) {
        req.setAttribute("pro", daoCtl.list(dao, App_project.class, Cnd.where("1", "=", 1).asc("id")));
        req.setAttribute("type", Globals.DATA_DICT.get(Dict.APP_TYPE));
        req.setAttribute("sys_menu", sys_menu);
    }

    @At
    @Ok("->:/private/app/App_infoAdd.html")
    public void toadd(@Param("pid") int pid, HttpServletRequest req) {
        req.setAttribute("pro", daoCtl.list(dao, App_project.class, Cnd.where("1", "=", 1).asc("id")));
        req.setAttribute("type", Globals.DATA_DICT.get(Dict.APP_TYPE));
        req.setAttribute("pid", pid);

    }

    @At
    @Ok("raw")
    public boolean add(@Param("..") App_info app_info) {
        if (daoCtl.add(dao, app_info)) {
            Globals.InitAppInfo(dao);
            syncUtil.sendMsg("appinfo");
            return true;
        } else
            return false;
    }

    @At
    @Ok("raw")
    public String getkey() {
        return getMyKey();
    }

    public String getMyKey() {
        String key = DecodeUtil.Encrypt("xuetang", DateUtil.getCurDateTime());
        int k = daoCtl.getRowCount(dao, App_info.class, Cnd.where("mykey", "=", key));
        if (k > 0) {
            return getMyKey();
        } else return key;
    }

    @At
    @Ok("raw")
    public String getsecret() {
        return getSecret();
    }

    public String getSecret() {
        String key = DecodeUtil.Encrypt("xuetang", DateUtil.getCurDateTime())+DecodeUtil.Encrypt("xuetang", DateUtil.getCurDateTime());
        int k = daoCtl.getRowCount(dao, App_info.class, Cnd.where("mysecret", "=", key));
        if (k > 0) {
            return getSecret();
        } else return key;
    }

    @At
    @Ok("json")
    public App_info view(@Param("id") int id) {
        return daoCtl.detailById(dao, App_info.class, id);
    }

    @At
    @Ok("->:/private/app/App_infoUpdate.html")
    public App_info toupdate(@Param("id") int id, HttpServletRequest req) {
        req.setAttribute("pro", daoCtl.list(dao, App_project.class, Cnd.where("1", "=", 1).asc("id")));
        req.setAttribute("type", Globals.DATA_DICT.get(Dict.APP_TYPE));
        return daoCtl.detailById(dao, App_info.class, id);//html:obj
    }

    @At
    public boolean update(@Param("..") App_info app_info) {
        if (daoCtl.update(dao, app_info)) {
            Globals.InitAppInfo(dao);
            syncUtil.sendMsg("appinfo");

            return true;
        } else
            return false;
    }

    @At
    public boolean delete(@Param("id") int id) {
        if (daoCtl.deleteById(dao, App_info.class, id)) {
            Globals.InitAppInfo(dao);
            syncUtil.sendMsg("appinfo");

            return true;
        } else
            return false;
    }

    @At
    public boolean deleteIds(@Param("ids") String ids) {
        if (daoCtl.deleteByIds(dao, App_info.class, StringUtils.split(ids, ","))) {
            Globals.InitAppInfo(dao);
            syncUtil.sendMsg("appinfo");

            return true;
        } else
            return false;
    }

    @At
    @Ok("raw")
    public String list(@Param("pid") int pid, @Param("page") int curPage, @Param("rows") int pageSize) {
        Criteria cri = Cnd.cri();
        cri.where().and("pid", "=", pid);
        cri.getOrderBy().desc("id");
        return daoCtl.listPageJson(dao, App_info.class, curPage, pageSize, cri);
    }

}