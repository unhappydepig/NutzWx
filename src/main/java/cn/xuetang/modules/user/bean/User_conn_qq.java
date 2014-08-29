package cn.xuetang.modules.user.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2014-04-05 00:45:11
*/
@Table("user_conn_qq")
public class User_conn_qq 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT USER_CONN_QQ_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int pid;
	@Column
	private int appid;
	@Column
	private int uid;
	@Column
	private String conuin;
	@Column
	private String conuinsecret;
	@Column
	private String conopenid;
	@Column
	private int conisfeed;
	@Column
	private int conispublishfeed;
	@Column
	private int conispublishht;
	@Column
	private int conisregister;
	@Column
	private int conisqzoneavatar;
	@Column
	private int conisqqshow;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public int getPid()
	{
		return pid;
	}
	public void setPid(int pid)
	{
		this.pid=pid;
	}
	public int getAppid()
	{
		return appid;
	}
	public void setAppid(int appid)
	{
		this.appid=appid;
	}
	public int getUid()
	{
		return uid;
	}
	public void setUid(int uid)
	{
		this.uid=uid;
	}
	public String getConuin()
	{
		return conuin;
	}
	public void setConuin(String conuin)
	{
		this.conuin=conuin;
	}
	public String getConuinsecret()
	{
		return conuinsecret;
	}
	public void setConuinsecret(String conuinsecret)
	{
		this.conuinsecret=conuinsecret;
	}
	public String getConopenid()
	{
		return conopenid;
	}
	public void setConopenid(String conopenid)
	{
		this.conopenid=conopenid;
	}
	public int getConisfeed()
	{
		return conisfeed;
	}
	public void setConisfeed(int conisfeed)
	{
		this.conisfeed=conisfeed;
	}
	public int getConispublishfeed()
	{
		return conispublishfeed;
	}
	public void setConispublishfeed(int conispublishfeed)
	{
		this.conispublishfeed=conispublishfeed;
	}
	public int getConispublishht()
	{
		return conispublishht;
	}
	public void setConispublishht(int conispublishht)
	{
		this.conispublishht=conispublishht;
	}
	public int getConisregister()
	{
		return conisregister;
	}
	public void setConisregister(int conisregister)
	{
		this.conisregister=conisregister;
	}
	public int getConisqzoneavatar()
	{
		return conisqzoneavatar;
	}
	public void setConisqzoneavatar(int conisqzoneavatar)
	{
		this.conisqzoneavatar=conisqzoneavatar;
	}
	public int getConisqqshow()
	{
		return conisqqshow;
	}
	public void setConisqqshow(int conisqqshow)
	{
		this.conisqqshow=conisqqshow;
	}

}