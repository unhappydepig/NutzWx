package cn.xuetang.service.wx;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_content_txt;
import cn.xuetang.service.BaseService;

@IocBean(fields = { "dao" })
public class WeixinContentTXTService extends BaseService<Weixin_content_txt> {

	public WeixinContentTXTService(){}
	public WeixinContentTXTService(Dao dao){
		super(dao);
	}
}
