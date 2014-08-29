package cn.xuetang.modules.api;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.config.Dict;
import cn.xuetang.common.filter.ApiFilter;
import cn.xuetang.common.util.ErrorUtil;
import cn.xuetang.modules.sys.bean.Sys_dict;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import java.util.List;

/**
 * Created by Wizzer on 14-4-9.
 */
@IocBean
@At("/api/pub")
@Filters({ @By(type = ApiFilter.class) })
public class PubApiAction extends BaseAction {
    @Inject
    protected Dao dao;
    private final static Log log = Logs.get();

    @At
    @Ok("raw")
    public String getDivision(@Param("zipcode") String zipcode) {
        if (Strings.isBlank(zipcode)) {
            List<Sys_dict> list = daoCtl.list(dao, Sys_dict.class, Cnd.where("id", "like", Dict.DIVSION+"____"));
            return Json.toJson(list);
        } else {
            Sys_dict dict = daoCtl.detailByCnd(dao, Sys_dict.class, Cnd.where("dkey", "=", zipcode));
            if (dict != null) {
                List<Sys_dict> list = daoCtl.list(dao, Sys_dict.class, Cnd.where("id", "like", dict.getId() + "____"));
                return Json.toJson(list);
            }
            return ErrorUtil.getErrorMsg(3);
        }
    }
}
