package cn.xuetang.modules.wx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.xuetang.common.config.Globals;
import cn.xuetang.common.file.FileType;
import cn.xuetang.common.util.DateUtil;
import cn.xuetang.common.util.DecodeUtil;
import cn.xuetang.common.util.StringUtil;
import cn.xuetang.modules.app.bean.App_info;
import cn.xuetang.modules.app.bean.App_project;
import cn.xuetang.modules.sys.bean.Sys_user;
import cn.xuetang.modules.wx.bean.*;
import org.apache.commons.lang.StringUtils;

import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
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

import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wizzer
 * @time 2014-04-01 11:49:27
 */
@IocBean
@At("/private/wx/content")
@Filters({@By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class)})
public class Weixin_contentAction extends BaseAction {
    @Inject
    protected Dao dao;
    @Inject
    protected UploadAdaptor upload;
    private final static Log log = Logs.get();

    @At("")
    @Ok("->:/private/wx/Weixin_content.html")
    public void index(@Param("sys_menu") String sys_menu, HttpSession session, HttpServletRequest req) {
        Sys_user user = (Sys_user) session.getAttribute("userSession");
        if (user.getSysrole()) {
            req.setAttribute("pro", daoCtl.list(dao, App_project.class, Cnd.where("1", "=", 1).asc("id")));

        } else {
            req.setAttribute("pro", daoCtl.list(dao, App_project.class, Cnd.where("id", "in", user.getProlist()).asc("id")));
        }
        req.setAttribute("sys_menu", sys_menu);
    }

    @At
    @Ok("raw")
    public boolean del(@Param("ids") String[] ids) {
        return daoCtl.update(dao, Weixin_content.class, Chain.make("status", -1), Cnd.where("id", "in", ids));

    }

    @At
    @Ok("raw")
    public boolean setStatus(@Param("type") String type, @Param("appid") int appid, @Param("status") int status, @Param("ids") String[] ids) {
        try {
            for (int i = 0; i < ids.length; i++) {
                int id = NumberUtils.toInt(ids[i]);
                if ("pub".equals(Strings.sNull(type))) {
                    daoCtl.update(dao, Weixin_content.class, Chain.make("status", 1).add("send_type", 0), Cnd.where("id", "=", id));
                } else if ("push".equals(Strings.sNull(type))) {
                    daoCtl.update(dao, Weixin_content.class, Chain.make("status", 1).add("send_type", 1), Cnd.where("id", "=", id));

                } else if ("recall".equals(type)) {
                    daoCtl.update(dao, Weixin_content.class, Chain.make("status", 0).add("send_type", 0), Cnd.where("id", "=", id));
                    daoCtl.exeUpdateBySql(dao, Sqls.create("update Weixin_content_push set status=1 where id=" + id));

                }

            }
        } catch (Exception e) {

            return false;
        }
        return true;

    }

    @At
    @Ok("->:/private/wx/Weixin_contentPush.html")
    public void topush(@Param("pid") int pid, HttpServletRequest req) {
        req.setAttribute("applist", daoCtl.list(dao, App_info.class, Cnd.where("pid", "=", pid).and("app_type", "=", "01")));
    }


    @At
    @Ok("->:/private/wx/Weixin_contentEdit.html")
    public void edit(@Param("id") int id, @Param("pid") int pid, @Param("channel_id") String channel_id, HttpServletRequest req) {
        if (id > 0) {
            req.setAttribute("content", daoCtl.detailById(dao, Weixin_content.class, id));
            Weixin_content_txt txt = daoCtl.detailById(dao, Weixin_content_txt.class, id);
            if (txt != null) {
                req.setAttribute("ctxt", txt);
            }
            req.setAttribute("id", id);
            Map<String, String> attrMap = daoCtl.getHashMap(dao, Sqls.create("select attr_code,attr_value from Weixin_content_attr where gid=" + id));
            req.setAttribute("attrMap", attrMap);
        }
        req.setAttribute("pid", pid);
        req.setAttribute("channel_id", channel_id);
        req.setAttribute("allow_size", upload.getContext().getMaxFileSize());
        req.setAttribute("allow_images", fileTypeExts(FileType.getSuffixname(upload, "images")));
        req.setAttribute("allow_video", fileTypeExts(FileType.getSuffixname(upload, "video")));
        req.setAttribute("allow_other", fileTypeExts(FileType.getSuffixname(upload, "other")));
        req.setAttribute("timenow", DateUtil.getCurDateTime());
        req.setAttribute("file_username", Globals.SYS_CONFIG.get("file_username"));
        req.setAttribute("file_password", DecodeUtil.Encrypt(Globals.SYS_CONFIG.get("file_password"), "file"));
        req.setAttribute("file_uploadurl", Globals.SYS_CONFIG.get("file_uploadurl"));
        req.setAttribute("file_domain", Globals.SYS_CONFIG.get("file_domain"));
        List<Weixin_channel_attr> attrList = daoCtl.list(dao, Weixin_channel_attr.class, Cnd.where("classid", "=", channel_id).asc("attr_code"));
        req.setAttribute("attrList", attrList);
        req.setAttribute("StringUtil", new StringUtil());

    }

    @At
    @Ok("raw")
    public int doSave(@Param("::attr.") final Map<String, String> map2, @Param("status") final int status, @Param("appid") final int appid, @Param("type") final int type, @Param("::content.") final Weixin_content content1, @Param("::contenttxt.") final Weixin_content_txt txt1, final HttpSession session, final HttpServletRequest req) {
        final Sys_user user = (Sys_user) session.getAttribute("userSession");
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
                        Weixin_content c = null;
                        try {
                            c = daoCtl.addT(dao, content);
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
                                daoCtl.exeUpdateBySql(dao, sqlattr);
                            }
                            Weixin_content_txt t = txt1;
                            t.setId(c.getId());
                            daoCtl.add(dao, t);
//                            if (type == 1) {
//                                Weixin_content_push push = new Weixin_content_push();
//                                push.setId(c.getId());
//                                push.setAppid(appid);
//                                daoCtl.add(dao, push);
//                            }
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
                        daoCtl.update(dao, content);
                        daoCtl.delete(dao, "Weixin_content_attr", Cnd.where("gid", "=", content.getId()));
                        if (map != null) {
                            Sql sqlattr = Sqls.create("insert into Weixin_content_attr(gid,attr_code,attr_value) values(@a,@b,@c)");
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                sqlattr.params().set("a", content.getId());
                                sqlattr.params().set("b", entry.getKey());
                                sqlattr.params().set("c", entry.getValue());
                                sqlattr.addBatch();
                            }
                            daoCtl.exeUpdateBySql(dao, sqlattr);
                        }
                        Weixin_content_txt t = txt1;
                        t.setId(content.getId());
                        daoCtl.update(dao, t);

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
    @Ok("->:/private/wx/Weixin_contentUpdate.html")
    public Weixin_content toupdate(@Param("id") int id, HttpServletRequest req) {
        return daoCtl.detailById(dao, Weixin_content.class, id);//html:obj
    }

    @At
    public boolean update(@Param("..") Weixin_content weixin_content) {
        return daoCtl.update(dao, weixin_content);
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
            } else cri.where().and("status", ">", -1);
        } else cri.where().and("status", ">", -1);
        if (queryOrderBy == 0) {
            cri.getOrderBy().desc("id");
        } else if (queryOrderBy == 1) {
            cri.getOrderBy().asc("id");
        } else if (queryOrderBy == 2) {
            cri.getOrderBy().desc("pub_time");
        } else if (queryOrderBy == 3) {
            cri.getOrderBy().asc("pub_time");
        }

        return daoCtl.listPageJson(dao, Weixin_content.class, curPage, pageSize, cri);
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
            jsonroot.put("icon", Globals.APP_BASE_NAME
                    + "/images/icons/icon042a1.gif");
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

        List<Weixin_channel> list = daoCtl.list(dao, Weixin_channel.class, sql);
        for (int i = 0; i < list.size(); i++) {
            Weixin_channel ch = list.get(i);
            boolean sign = false;
            String pid = ch.getId().substring(0, ch.getId().length() - 4);
            if (i == 0 || "".equals(pid))
                pid = "0";
            sql = Sqls.create("select count(*) from weixin_channel where pid=@s and id like @c");
            sql.params().set("s", ch.getPid());
            sql.params().set("c", ch.getId() + "____");
            int num = daoCtl.getIntRowValue(dao, sql);
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