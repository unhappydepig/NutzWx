package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_channel_role;

@IocBean(fields = { "dao" })
public class WeixinChannelRoleService extends BaseService<Weixin_channel_role> {

	public WeixinChannelRoleService(Dao dao) {
		super(dao);
	}
}
