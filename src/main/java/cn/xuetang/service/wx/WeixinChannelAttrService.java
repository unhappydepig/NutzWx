package cn.xuetang.service.wx;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_channel_attr;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class WeixinChannelAttrService extends BaseService<Weixin_channel_attr> {

	public WeixinChannelAttrService() {
	}	
	
	public WeixinChannelAttrService(Dao dao) {
		super(dao);
	}
}
