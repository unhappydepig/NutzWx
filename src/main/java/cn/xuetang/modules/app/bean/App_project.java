package cn.xuetang.modules.app.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;
/**
* @author Wizzer
* @time   2014-04-03 20:39:13
*/
@Table("app_project")
public class App_project 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT APP_PROJECT_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String name;
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
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
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