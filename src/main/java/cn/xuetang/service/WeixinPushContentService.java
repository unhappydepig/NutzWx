package cn.xuetang.service;

import org.nutz.dao.Dao;

import cn.xuetang.modules.wx.bean.Weixin_push_content;

public class WeixinPushContentService extends BaseService<Weixin_push_content> {

	public WeixinPushContentService() {
	}

	public WeixinPushContentService(Dao dao) {
		super(dao);
	}
}
