package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_content_attr;

@IocBean(fields = { "dao" })
public class WeixinContentAttrService extends BaseService<Weixin_content_attr> {

	public WeixinContentAttrService() {
	}

	public WeixinContentAttrService(Dao dao) {
		super(dao);
	}

}
