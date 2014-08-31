package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_channel;

@IocBean(fields = { "dao" })
public class WeixinChannelService extends BaseService<Weixin_channel> {

	public WeixinChannelService(Dao dao) {
		super(dao);
	}
}
