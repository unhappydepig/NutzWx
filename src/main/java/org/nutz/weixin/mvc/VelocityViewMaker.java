package org.nutz.weixin.mvc;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;

public class VelocityViewMaker implements ViewMaker {

    @Override
    public View make(Ioc ioc, String type, String value) {
        if ("vm".equalsIgnoreCase(type)) {
            return new VelocityLayoutView(ioc.get(cn.xuetang.shiro.web.support.velocity.Permission.class, "shiro_permission"),value);
        }
        return null;
    }

}
