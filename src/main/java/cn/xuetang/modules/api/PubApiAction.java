package cn.xuetang.modules.api;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.filter.ApiFilter;
import cn.xuetang.service.sys.SysDictService;

/**
 * Created by Wizzer on 14-4-9.
 */
@IocBean
@At("/api/pub")
@Filters({ @By(type = ApiFilter.class) })
public class PubApiAction {
	@Inject
	private SysDictService sysDictService;

	@At
	@Ok("raw")
	public String getDivision(@Param("zipcode") String zipcode) {
		return sysDictService.getList(zipcode);
	}
}
