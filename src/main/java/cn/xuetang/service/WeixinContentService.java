package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_content;
@IocBean(fields = { "dao" })
public class WeixinContentService extends BaseService<Weixin_content> {

	private WeixinContentService() {
	}

	private WeixinContentService(Dao dao) {
		super(dao);
	}

}
