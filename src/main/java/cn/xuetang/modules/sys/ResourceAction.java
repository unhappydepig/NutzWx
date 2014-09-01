package cn.xuetang.modules.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import cn.xuetang.modules.sys.bean.Sys_resource;
import cn.xuetang.service.AppInfoService;
import cn.xuetang.service.SysResourceService;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */
@IocBean
@At("/private/sys/res")
public class ResourceAction{
	@Inject
	protected SysResourceService sysResourceService;

	@Inject
	private AppInfoService appInfoService;

	@At("")
	@Ok("vm:template.private.sys.resource")
	public void user() {
	}

	@At
	@Ok("json")
	public List<Sys_resource> list(@Param("subtype") int subtype) {
		return sysResourceService.list(subtype);
	}

	/**
	 * 查询全部
	 * */
	@At
	@Ok("raw")
	public String listAll(@Param("id") String id, @Param("subtype") int subtype) {
		return Json.toJson(getJSON(id, subtype));
	}

	private List<Object> getJSON(String id, int subtype) {
		Criteria cri = Cnd.cri();
		if (null == id || "".equals(id)) {
			cri.where().and("id", "like", "____");
		} else {
			cri.where().and("id", "like", id + "____");
		}
		if (subtype >= 0) {
			cri.where().and("subtype", "=", subtype);
		}
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Sys_resource> list = sysResourceService.listByCnd(cri);
		List<Object> array = new ArrayList<Object>();
		for (int i = 0; i < list.size(); i++) {
			Sys_resource res = list.get(i);
			Map<String, Object> jsonobj = new HashMap<String, Object>();
			String pid = res.getId().substring(0, res.getId().length() - 4);
			if (i == 0 || Strings.isBlank(pid))
				pid = "0";
			int num = sysResourceService.getRowCount(Cnd.where("id", "like", res.getId() + "____"));
			jsonobj.put("id", res.getId());
			jsonobj.put("name", res.getName());
			jsonobj.put("descript", res.getDescript());
			jsonobj.put("url", res.getUrl());
			jsonobj.put("_parentId", pid);
			String bts = Strings.sNull(res.getButton());
			String[] bt;
			String temp = "";
			if (StringUtils.isNotBlank(bts)) {
				bt = StringUtils.split(bts, ";");
				for (int j = 0; j < bt.length; j++)
					temp += bt[j].substring(0, bt[j].indexOf("/")) + ";";
			}
			jsonobj.put("bts", temp);
			if (num > 0) {
				jsonobj.put("children", getJSON(res.getId(), subtype));
			}
			array.add(jsonobj);
		}
		return array;
	}

	/***
	 * 修改前查找
	 * */
	@At
	@Ok("vm:template.private.sys.resourceUpdate")
	public void toupdate(@Param("id") String id, HttpServletRequest req) {
		Sys_resource res = sysResourceService.detailByName(id);
		req.setAttribute("obj", res);
	}

	/****
	 * 修改
	 * */
	@At
	@Ok("raw")
	public String updateSave(@Param("..") Sys_resource res, @Param("button2") String button2, HttpServletRequest req) {
		res.setButton(button2);
		return sysResourceService.update(res) == true ? res.getId() : "";
	}

	/****
	 * 新建菜单，查找单位。
	 * */
	@At
	@Ok("vm:template.private.sys.resourceAdd")
	public void toAdd() {

	}

	/***
	 * 新建资源
	 * */
	@At
	@Ok("raw")
	public boolean addSave(@Param("..") Sys_resource res, @Param("button2") String button2) {
		int location = sysResourceService.getIntRowValue(res);
		res.setLocation(location);
		res.setId(sysResourceService.getSubMenuId("Sys_resource", "id", res.getId()));
		res.setButton(button2);
		return sysResourceService.insert(res);
	}

	/**
	 * 删除
	 * */
	@At
	@Ok("raw")
	public boolean del(@Param("id") String[] ids) {
		try {
			sysResourceService.delSqls(ids);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 转到排序页面
	 * */
	@At
	@Ok("vm:template.private.sys.resourceSort")
	public void toSort(HttpServletRequest req) throws Exception {
		List<Object> array = new ArrayList<Object>();
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Sys_resource> list = sysResourceService.listByCnd(cri);
		Map<String, Object> jsonroot = new HashMap<String, Object>();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "资源列表");
		jsonroot.put("open", true);
		jsonroot.put("childOuter", false);
		jsonroot.put("icon", appInfoService.getAPP_BASE_NAME() + "/images/icons/icon042a1.gif");
		array.add(jsonroot);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> jsonobj = new HashMap<String, Object>();
			Sys_resource obj = list.get(i);
			jsonobj.put("id", obj.getId());
			String p = obj.getId().substring(0, obj.getId().length() - 4);
			jsonobj.put("pId", p == "" ? "0" : p);
			String name = obj.getName();
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
	}

	/***
	 * 确认排序
	 * */
	@At
	@Ok("raw")
	public boolean sort(@Param("checkids") String[] checkids) {
		boolean rs = sysResourceService.updateSortRow(checkids, 0);
		return rs;
	}
}