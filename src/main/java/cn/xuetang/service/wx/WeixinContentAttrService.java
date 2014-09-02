package cn.xuetang.service.wx;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_content_attr;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class WeixinContentAttrService extends BaseService<Weixin_content_attr> {

	public WeixinContentAttrService() {
	}

	public WeixinContentAttrService(Dao dao) {
		super(dao);
	}

}
