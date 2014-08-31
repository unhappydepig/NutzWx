package cn.xuetang.common.filter;

import java.io.OutputStream;
import java.util.Map;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Encoding;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.VoidView;

import cn.xuetang.common.util.ErrorUtil;
import cn.xuetang.common.util.SortHashtable;
import cn.xuetang.common.util.UrlUtil;
import cn.xuetang.modules.app.bean.App_info;
import cn.xuetang.service.AppInfoService;

/**
 * Created by Wizzer on 14-4-1.
 */
@IocBean
public class ApiFilter implements ActionFilter {
    @Inject
    protected AppInfoService appInfoService;
    private final static Log log = Logs.get();
    @Override
    public View match(ActionContext context) {
        String mykey = Strings.sNull(context.getRequest().getParameter("mykey"));
        try{
            JsonFormat jsonFormat=new JsonFormat();
            jsonFormat.setAutoUnicode(true);
            jsonFormat.setCompact(true);
            App_info appInfo = (App_info) appInfoService.APP_INFO.get(mykey);
            String data = Strings.sNull(UrlUtil.readStreamParameterBase64(context.getRequest().getInputStream()));
            Map map= Json.fromJson(Map.class, data);
            String signature=Strings.sNull(map.get("signature"));
            map.remove("signature");
            String str= Json.toJson(SortHashtable.sortByValue(map), jsonFormat);
            String mysignature= Lang.digest("MD5", str.getBytes(), appInfo.getMysecret().getBytes(), 1);
            if(signature.equals(mysignature)){
                context.getRequest().setAttribute("data",data);
                return null;
            }else throw new Exception();
        }catch (Exception e){
            e.printStackTrace();
            context.getResponse().setContentType("text/plain");
            try {
                byte[] data = String.valueOf(ErrorUtil.getErrorMsg(-1)).getBytes(Encoding.UTF8);
                context.getResponse().setHeader("Content-Length", "" + data.length);
                OutputStream out = context.getResponse().getOutputStream();
                Streams.writeAndClose(out, data);
            } catch (Exception ee) {
            }
            return new VoidView();
        }
    }

}
