package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_video;

@IocBean(fields = { "dao" })
public class WeixinVideoService extends BaseService<Weixin_video> {

	public WeixinVideoService() {
	}

	public WeixinVideoService(Dao dao) {
		super(dao);
	}
}
