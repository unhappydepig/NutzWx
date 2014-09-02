package cn.xuetang.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.xuetang.modules.wx.bean.Weixin_content_txt;

@IocBean(fields = { "dao" })
public class WeixinContentTXTService extends BaseService<Weixin_content_txt> {

	public WeixinContentTXTService(){}
	public WeixinContentTXTService(Dao dao){
		super(dao);
	}
}
