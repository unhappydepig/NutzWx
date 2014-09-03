package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
/**
* @author Wizzer
* @time   2014-05-18 09:41:37
*/
@Table("weixin_content_attr")
public class Weixin_content_attr 
{
	@Id
	private int gid;
	@Column
	private String attr_code;
	@Column
	private String attr_value;
		public int getGid()
	{
		return gid;
	}
	public void setGid(int gid)
	{
		this.gid=gid;
	}
	public String getAttr_code()
	{
		return attr_code;
	}
	public void setAttr_code(String attr_code)
	{
		this.attr_code=attr_code;
	}
	public String getAttr_value()
	{
		return attr_value;
	}
	public void setAttr_value(String attr_value)
	{
		this.attr_value=attr_value;
	}

}