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
@Table("user_conn_sinawb")
public class User_conn_sinawb 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT USER_CONN_SINAWB_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int pid;
	@Column
	private int appid;
	@Column
	private int uid;
	@Column
	private long sina_uid;
	@Column
	private String nickname;
	@Column
	private String first_login;
	@Column
	private String access_token;
	@Column
	private String token_secret;
	@Column
	private int expires_in;
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
	public long getSina_uid()
	{
		return sina_uid;
	}
	public void setSina_uid(long sina_uid)
	{
		this.sina_uid=sina_uid;
	}
	public String getNickname()
	{
		return nickname;
	}
	public void setNickname(String nickname)
	{
		this.nickname=nickname;
	}
	public String getFirst_login()
	{
		return first_login;
	}
	public void setFirst_login(String first_login)
	{
		this.first_login=first_login;
	}
	public String getAccess_token()
	{
		return access_token;
	}
	public void setAccess_token(String access_token)
	{
		this.access_token=access_token;
	}
	public String getToken_secret()
	{
		return token_secret;
	}
	public void setToken_secret(String token_secret)
	{
		this.token_secret=token_secret;
	}
	public int getExpires_in()
	{
		return expires_in;
	}
	public void setExpires_in(int expires_in)
	{
		this.expires_in=expires_in;
	}

}