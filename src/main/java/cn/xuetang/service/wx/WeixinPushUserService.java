package cn.xuetang.service.wx;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_push_user;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class WeixinPushUserService extends BaseService<Weixin_push_user> {

	public WeixinPushUserService() {
	}

	public WeixinPushUserService(Dao dao) {
		super(dao);
	}
}
