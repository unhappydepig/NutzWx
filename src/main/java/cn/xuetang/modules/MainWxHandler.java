package cn.xuetang.modules;

import cn.xuetang.common.config.Globals;
import cn.xuetang.common.dao.ObjectCtl;
import cn.xuetang.modules.app.bean.App_info;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.impl.AbstractWxHandler;
import org.nutz.weixin.impl.BasicWxHandler;
import org.nutz.weixin.util.Wxs;

/**
 * Created by Wizzer on 14-8-11.
 */
@IocBean(name = "wxHandler")
public class MainWxHandler extends AbstractWxHandler {
    private final static Log log = Logs.get();
    protected static ObjectCtl daoCtl = new ObjectCtl();
    @Inject
    protected Dao dao;

    public boolean check(String signature, String timestamp, String nonce, String key) {
        App_info appInfo = (App_info) Globals.APP_INFO.get(key);
        return appInfo != null && Wxs.check(appInfo.getToken(), signature, timestamp, nonce);

    }

    // 用户发送的是文本的时候调用这个方法
    public WxOutMsg text(WxInMsg msg) {
        log.info("key::::"+msg.getExtkey());
        if ("god".equals(msg.getContent())) // 用户输入god,我就惊叹一下嘛!
            return Wxs.respText(null, "Oh my God!" + msg.getExtkey());
        else
            return Wxs.respText(null, "Out of my way!" + msg.getExtkey()); // 否则,滚开!
    }

}
