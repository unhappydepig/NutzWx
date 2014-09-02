package cn.xuetang.common.task.job;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.xuetang.common.util.DateUtil;
import cn.xuetang.common.util.EmojiFilter;
import cn.xuetang.common.util.UrlUtil;
import cn.xuetang.modules.app.bean.App_info;
import cn.xuetang.modules.user.bean.User_conn_wx;
import cn.xuetang.service.AppInfoService;
import cn.xuetang.service.user.UserConnWXService;

/**
 * 获取微信用户资料
 * Created by Wizzer on 14-4-2.
 */
@IocBean
public class UserInfoJob implements Job {
    @Inject
    private AppInfoService appInfoService;
    @Inject
    private UserConnWXService userConnWXService; 
    
    private final static Log log = Logs.get();
    private static String task_code = "";
    private static String param_value = "";
    private static int task_interval = 0;
    private static int task_id = 0;
    private static int task_threadnum = 1;
    private static int pageSize = 20;
    private ExecutorService pool;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jdm = context.getJobDetail().getJobDataMap();
            param_value = Strings.sNull(jdm.get("param_value"));
            task_code = Strings.sNull(jdm.get("task_code"));
            task_interval = NumberUtils.toInt(Strings.sNull(jdm.get("task_interval"))) > 0 ? NumberUtils.toInt(Strings.sNull(jdm.get("task_interval"))) : 1000;
            task_id = NumberUtils.toInt(Strings.sNull(jdm.get("task_id")));
            task_threadnum = NumberUtils.toInt(Strings.sNull(jdm.get("task_threadnum"))) > 0 ? NumberUtils.toInt(Strings.sNull(jdm.get("task_threadnum"))) : 1;
            log.info("PushDataJob param_value:" + param_value + " task_code:" + task_code + " task_interval:" + task_interval + " task_threadnum:" + task_threadnum + " task_id:" + task_id);
            pageSize = NumberUtils.toInt(param_value) > 0 ? NumberUtils.toInt(param_value) : 20;
            init();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void init() {
        pool = Executors.newFixedThreadPool(task_threadnum);
        List<App_info> infoList = appInfoService.listByCnd(Cnd.where("1", "=", 1));
        for (App_info info : infoList) {
            List<User_conn_wx> list = userConnWXService.listPage(1, pageSize, Cnd.where("status", "=", 0).and("appid", "=", info.getId()).asc("id")).getList(User_conn_wx.class);
            String access_token = appInfoService.getGloalsAccessToken(info);
            for (User_conn_wx wx : list) {
                pool.execute(new UserThread(access_token, wx));
            }
        }
    }

    class UserThread extends Thread {
        private String access_token;
        private User_conn_wx wx;

        public UserThread(String access_token, User_conn_wx wx) {
            this.access_token = access_token;
            this.wx = wx;
        }

        public void run() {
            try {
                Thread.sleep(task_interval);
                String res = UrlUtil.getOneHtml("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid=" + wx.getOpenid() + "&lang=zh_CN", "UTF-8");
                Map<String, Object> map = Json.fromJson(Map.class, res);
                if (map.get("errcode") == null) {
                    log.info("nickname:" + Strings.sNull(map.get("nickname")));
                    wx.setWx_nickname(EmojiFilter.filterEmoji(Strings.sNull(map.get("nickname"))));
                    wx.setWx_sex(NumberUtils.toInt(Strings.sNull(map.get("sex"))));
                    wx.setWx_city(Strings.sNull(map.get("city")));
                    wx.setWx_country(Strings.sNull(map.get("country")));
                    wx.setWx_province(Strings.sNull(map.get("province")));
                    wx.setWx_headimgurl(Strings.sNull(map.get("headimgurl")));
                    wx.setWx_time(DateUtil.long2DateStr(NumberUtils.toLong(Strings.sNull(map.get("subscribe_time")))));
                    wx.setStatus(1);
                    userConnWXService.update(wx);
                } else {
                    wx.setStatus(2);
                    userConnWXService.update(wx);
                    log.info("UserInfoJob.getInfo:" + map.get("errmsg"));
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
}
