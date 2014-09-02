package cn.xuetang.modules.sys;

import org.apache.commons.lang.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.service.sys.SysUserLogService;

/**
 * 类描述： 创建人：Wizzer 联系方式：www.wizzer.cn 创建时间：2013-12-2 下午6:00:07
 * 
 * @version
 */
@IocBean
@At("/private/sys/user")
public class UserLogAction {

	@Inject
	private SysUserLogService sysUserLogService;

	@At
	@Ok("vm:template.private.sys.userLog")
	public void log() {

	}

	@At
	@Ok("raw")
	public String loglist(@Param("page") int curPage, @Param("rows") int pageSize, @Param("SearchUserName") String SearchUserName) {
		Criteria cri = Cnd.cri();
		if (StringUtils.isNotBlank(SearchUserName)) {
			SqlExpressionGroup exp = Cnd.exps("LOGINNAME", "LIKE", "%" + SearchUserName + "%").or("REALNAME", "LIKE", "%" + SearchUserName + "%");
			cri.where().and(exp);
		}
		cri.getOrderBy().desc("LOGINTIME");
		return sysUserLogService.listPageJson(curPage, pageSize, cri);
	}
}
