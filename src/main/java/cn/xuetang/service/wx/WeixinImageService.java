package cn.xuetang.service.wx;

import org.nutz.dao.Dao;

import cn.xuetang.modules.wx.bean.Weixin_image;
import cn.xuetang.service.BaseService;

public class WeixinImageService extends BaseService<Weixin_image> {

	public WeixinImageService(){}
	public WeixinImageService(Dao dao){
		super(dao);
	}
}
