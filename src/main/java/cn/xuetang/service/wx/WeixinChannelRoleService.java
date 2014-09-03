package cn.xuetang.service.wx;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_channel_role;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class WeixinChannelRoleService extends BaseService<Weixin_channel_role> {

	
	public WeixinChannelRoleService() {
	}
	
	public WeixinChannelRoleService(Dao dao) {
		super(dao);
	}
}
