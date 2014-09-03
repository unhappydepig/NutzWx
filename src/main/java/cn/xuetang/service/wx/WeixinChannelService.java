package cn.xuetang.service.wx;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_channel;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class WeixinChannelService extends BaseService<Weixin_channel> {

	public WeixinChannelService() {
	}

	public WeixinChannelService(Dao dao) {
		super(dao);
	}
}
