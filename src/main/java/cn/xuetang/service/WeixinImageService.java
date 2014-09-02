package cn.xuetang.service;

import org.nutz.dao.Dao;

import cn.xuetang.modules.wx.bean.Weixin_image;

public class WeixinImageService extends BaseService<Weixin_image> {

	public WeixinImageService(){}
	public WeixinImageService(Dao dao){
		super(dao);
	}
}
