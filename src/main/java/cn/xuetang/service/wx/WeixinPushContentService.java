package cn.xuetang.service.wx;

import org.nutz.dao.Dao;

import cn.xuetang.modules.wx.bean.Weixin_push_content;
import cn.xuetang.service.BaseService;

public class WeixinPushContentService extends BaseService<Weixin_push_content> {

	public WeixinPushContentService() {
	}

	public WeixinPushContentService(Dao dao) {
		super(dao);
	}
}
