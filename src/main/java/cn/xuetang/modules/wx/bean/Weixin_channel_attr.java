package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2014-05-18 09:41:37
*/
@Table("weixin_channel_attr")
public class Weixin_channel_attr 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT WEIXIN_CHANNEL_ATTR_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String classid;
	@Column
	private String attr_name;
	@Column
	private String attr_code;
	@Column
	private String attr_type;
	@Column
	private String attr_default;
	@Column
	private int attr_must;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getClassid()
	{
		return classid;
	}
	public void setClassid(String classid)
	{
		this.classid=classid;
	}
	public String getAttr_name()
	{
		return attr_name;
	}
	public void setAttr_name(String attr_name)
	{
		this.attr_name=attr_name;
	}
	public String getAttr_code()
	{
		return attr_code;
	}
	public void setAttr_code(String attr_code)
	{
		this.attr_code=attr_code;
	}
	public String getAttr_type()
	{
		return attr_type;
	}
	public void setAttr_type(String attr_type)
	{
		this.attr_type=attr_type;
	}
	public String getAttr_default()
	{
		return attr_default;
	}
	public void setAttr_default(String attr_default)
	{
		this.attr_default=attr_default;
	}
	public int getAttr_must()
	{
		return attr_must;
	}
	public void setAttr_must(int attr_must)
	{
		this.attr_must=attr_must;
	}

}