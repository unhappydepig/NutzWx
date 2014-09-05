package cn.xuetang.modules.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.util.SyncUtil;
import cn.xuetang.modules.sys.bean.Sys_dict;
import cn.xuetang.service.sys.AppInfoService;
import cn.xuetang.service.sys.SysDictService;

/**
 * @author Wizzer
 * @time 2014-03-26 12:30:22
 */
@IocBean
@At("/private/sys/dict")
public class DictAction {
	@Inject
	private SysDictService sysDictService;

	@Inject
	private AppInfoService appInfoService;

	@At
	@Ok("vm:template.private.sys.dict")
	public void index() {

	}

	@At
	@Ok("vm:template.private.sys.dictAdd")
	public void toadd(@Param("id") String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
			req.setAttribute("dict", sysDictService.detailByName(id));
		}
		req.setAttribute("treeid", id);

	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") Sys_dict sys_dict, @Param("treeid") String treeid) {
		int location = sysDictService.getIntRowValue(sys_dict);
		sys_dict.setLocation(location);
		sys_dict.setId(sysDictService.getSubMenuId("sys_dict", "id", Strings.sNull(treeid)));
		if (sysDictService.insert(sys_dict)) {
			appInfoService.InitDataDict();
			String urls = Strings.sNull(appInfoService.getSYS_CONFIG().get("sync_url"));
			String key = Strings.sNull(appInfoService.getSYS_CONFIG().get("sync_key"));
			SyncUtil.sendMsg("datadict",urls,key);
			return true;
		} else
			return false;
	}

	@At
	@Ok("vm:template.private.sys.dictUpdate")
	public Sys_dict toupdate(@Param("id") String id, HttpServletRequest req) {
		if (StringUtils.isNotBlank(id) && id.length() > 4) {
			req.setAttribute("dict", sysDictService.detailByName(id.substring(0, 4)));
		}
		return sysDictService.detailByName(id);// html:obj
	}

	@At
	public boolean update(@Param("..") Sys_dict sys_dict) {
		if (sysDictService.update(sys_dict)) {
			appInfoService.InitDataDict();
			String urls = Strings.sNull(appInfoService.getSYS_CONFIG().get("sync_url"));
			String key = Strings.sNull(appInfoService.getSYS_CONFIG().get("sync_key"));
			SyncUtil.sendMsg("datadict",urls,key);
			return true;
		} else
			return false;
	}

	@At
	public boolean delete(@Param("id") String id) {
		if (sysDictService.deleteByName(id)) {
			appInfoService.InitDataDict();
			String urls = Strings.sNull(appInfoService.getSYS_CONFIG().get("sync_url"));
			String key = Strings.sNull(appInfoService.getSYS_CONFIG().get("sync_key"));
			SyncUtil.sendMsg("datadict",urls,key);
			return true;
		} else
			return false;
	}

	@At
	public boolean deleteIds(@Param("ids") String ids) {
		String[] id = StringUtils.split(ids, ",");
		try {
			for (String d : id) {
				sysDictService.delete(Cnd.where("id", "like", d + "%"));
			}
			appInfoService.InitDataDict();
			String urls = Strings.sNull(appInfoService.getSYS_CONFIG().get("sync_url"));
			String key = Strings.sNull(appInfoService.getSYS_CONFIG().get("sync_key"));
			SyncUtil.sendMsg("datadict",urls,key);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@At
	public boolean getdict(@Param("id") String id, @Param("tag") int tag) {
		return true;
	}

	@At
	@Ok("raw")
	public String list(@Param("page") int curPage, @Param("rows") int pageSize, @Param("treeid") String treeid) {
		Criteria cri = Cnd.cri();
		cri.where().and("id", "like", Strings.sNull(treeid) + "____");
		cri.getOrderBy().asc("location");
		return sysDictService.listPageJson(curPage, pageSize, cri);
	}

	@At
	@Ok("vm:template.private.sys.dictSort")
	public void toSort(@Param("id") String id, HttpServletRequest req) throws Exception {
		List<Object> array = new ArrayList<Object>();
		List<Sys_dict> list = sysDictService.listByLike(id);
		Map<String, Object> jsonroot = new HashMap<String, Object>();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "数据字典");
		jsonroot.put("open", true);
		jsonroot.put("childOuter", false);
		jsonroot.put("icon", appInfoService.getAPP_BASE_NAME() + "/images/icons/icon042a1.gif");
		array.add(jsonroot);
		for (Sys_dict obj : list) {
			Map<String, Object> jsonobj = new HashMap<String, Object>();
			jsonobj.put("id", obj.getId());
			String p = obj.getId().substring(0, obj.getId().length() - 4);
			jsonobj.put("pId", "".equals(p) ? "0" : p);
			String name = obj.getDval();
			jsonobj.put("name", name);
			jsonobj.put("childOuter", false);
			if (obj.getId().length() < 12) {
				jsonobj.put("open", true);
			} else {
				jsonobj.put("open", false);
			}
			array.add(jsonobj);
		}
		req.setAttribute("str", Json.toJson(array));
		req.setAttribute("id", id);
	}

	/**
	 * 确认排序
	 */
	@At
	@Ok("raw")
	public boolean sort(@Param("id") String id, @Param("checkids") String[] ids, HttpSession session, HttpServletRequest req) {
		boolean res = false;
		try {
			sysDictService.updateSqls(ids, id);
			res = true;
		} catch (Exception e) {
			res = false;
		}
		return res;

	}

	/**
	 * 获取栏目树形结构
	 * 
	 * @param id
	 * @return
	 */
	@At
	@Ok("raw")
	public String treelist(@Param("id") String id) {
		List<Object> array = new ArrayList<Object>();
		List<Sys_dict> list = sysDictService.listByLikeId(id, array);
		for (int i = 0; i < list.size(); i++) {
			Sys_dict ch = list.get(i);
			boolean sign = false;
			String pid = ch.getId().substring(0, ch.getId().length() - 4);
			if (i == 0 || "".equals(pid))
				pid = "0";
			int num = sysDictService.getRowCount(Cnd.where("id", "like", ch.getId() + "____"));
			if (num > 0)
				sign = true;
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("id", ch.getId());
			obj.put("pId", pid);
			obj.put("isParent", sign);
			obj.put("name", ch.getDval());
			obj.put("url", "javascript:list(\"" + ch.getId() + "\")");
			obj.put("target", "_self");
			array.add(obj);
		}
		return Json.toJson(array);
	}
}