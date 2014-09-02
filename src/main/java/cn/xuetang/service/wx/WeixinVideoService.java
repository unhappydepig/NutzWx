package cn.xuetang.service.wx;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_video;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class WeixinVideoService extends BaseService<Weixin_video> {

	public WeixinVideoService() {
	}

	public WeixinVideoService(Dao dao) {
		super(dao);
	}
}
