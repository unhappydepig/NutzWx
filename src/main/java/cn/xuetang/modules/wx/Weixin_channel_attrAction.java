package cn.xuetang.modules.wx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.config.Dict;
import cn.xuetang.modules.wx.bean.Weixin_channel_attr;
import cn.xuetang.service.AppInfoService;
import cn.xuetang.service.WeixinChannelAttrService;

/**
 * @author Wizzer
 * @time 2014-05-18 09:41:37
 */
@IocBean
@At("/private/wx/channel/attr")
public class Weixin_channel_attrAction {
	@Inject
	private AppInfoService appInfoService;

	private WeixinChannelAttrService weixinChannelAttrService;

	@At
	@Ok("vm:template.private.wx.Weixin_channelAttrAdd")
	public void toadd(@Param("classid") String classid, HttpServletRequest req) {
		Map<String, String> map = (Map) appInfoService.DATA_DICT.get(Dict.FORM_TYPE);
		req.setAttribute("formmap", map);
		req.setAttribute("classid", classid);
	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") Weixin_channel_attr channel_attr) {
		Weixin_channel_attr attr = weixinChannelAttrService.fetch(Cnd.where("classid", "=", channel_attr.getClassid()).desc("attr_code"));
		String code = "attr_1";
		if (attr != null) {
			code = "attr_" + (NumberUtils.toInt(Strings.sNull(attr.getAttr_code()).replace("attr_", "")) + 1);

		}
		channel_attr.setAttr_code(code);
		return weixinChannelAttrService.insert(channel_attr);
	}

	@At
	@Ok("vm:template.private.wx.Weixin_channelAttrUpdate")
	public Weixin_channel_attr toupdate(@Param("id") int id, HttpServletRequest req) {
		Map<String, String> map = (Map) appInfoService.DATA_DICT.get(Dict.FORM_TYPE);
		req.setAttribute("formmap", map);
		return weixinChannelAttrService.fetch(id);// html:obj
	}

	@At
	public boolean update(@Param("..") Weixin_channel_attr weixin_channel_attr) {
		return weixinChannelAttrService.update(weixin_channel_attr);
	}

	@At
	public boolean delete(@Param("id") int id) {
		return weixinChannelAttrService.delete(id) > 0;
	}

	@At
	public boolean deleteIds(@Param("ids") Integer[] ids) {
		return weixinChannelAttrService.delete(Cnd.where("id", "in", ids)) > 0;
	}

	@At
	@Ok("raw")
	public String list(@Param("classid") String classid, @Param("page") int curPage, @Param("rows") int pageSize) {
		Criteria cri = Cnd.cri();
		cri.where().and("classid", "=", classid);
		cri.getOrderBy().desc("id");
		return weixinChannelAttrService.listPageJson(curPage, pageSize, cri);
	}

}