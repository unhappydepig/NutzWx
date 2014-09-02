package cn.xuetang.service.wx;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_content;
import cn.xuetang.service.BaseService;
@IocBean(fields = { "dao" })
public class WeixinContentService extends BaseService<Weixin_content> {

	private WeixinContentService() {
	}

	private WeixinContentService(Dao dao) {
		super(dao);
	}

}
