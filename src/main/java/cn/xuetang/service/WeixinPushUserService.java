package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_push_user;

@IocBean(fields = { "dao" })
public class WeixinPushUserService extends BaseService<Weixin_push_user> {

	public WeixinPushUserService() {
	}

	public WeixinPushUserService(Dao dao) {
		super(dao);
	}
}
