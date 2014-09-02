package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
/**
* @author Wizzer
* @time   2014-04-03 20:38:34
*/
@Table("weixin_content_txt")
public class Weixin_content_txt 
{
	@Id
	private int id;
	@Column
	private String txt;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getTxt()
	{
		return txt;
	}
	public void setTxt(String txt)
	{
		this.txt=txt;
	}

}