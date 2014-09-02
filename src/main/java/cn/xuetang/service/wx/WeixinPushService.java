package cn.xuetang.service.wx;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_push;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class WeixinPushService extends BaseService<Weixin_push> {

	public WeixinPushService() {
	}

	public WeixinPushService(Dao dao) {
		super(dao);
	}
}
