package cn.xuetang.modules.app.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2014-04-03 20:39:13
*/
@Table("app_info")
public class App_info 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT APP_INFO_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int pid;
	@Column
	private String name;
    @Column
    private String token;
	@Column
	private String app_name;
	@Column
	private String app_type;
	@Column
	private String app_key;
	@Column
	private String app_secret;
	@Column
	private String mykey;
    @Column
    private String mysecret;
	@Column
	private String txt;
    @Column
    private String access_time;
    @Column
    private String access_token;
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
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApp_name()
	{
		return app_name;
	}
	public void setApp_name(String app_name)
	{
		this.app_name=app_name;
	}
	public String getApp_type()
	{
		return app_type;
	}
	public void setApp_type(String app_type)
	{
		this.app_type=app_type;
	}
	public String getApp_key()
	{
		return app_key;
	}
	public void setApp_key(String app_key)
	{
		this.app_key=app_key;
	}
	public String getApp_secret()
	{
		return app_secret;
	}
	public void setApp_secret(String app_secret)
	{
		this.app_secret=app_secret;
	}
	public String getMykey()
	{
		return mykey;
	}
	public void setMykey(String mykey)
	{
		this.mykey=mykey;
	}
	public String getTxt()
	{
		return txt;
	}
	public void setTxt(String txt)
	{
		this.txt=txt;
	}

    public String getAccess_time() {
        return access_time;
    }

    public void setAccess_time(String access_time) {
        this.access_time = access_time;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getMysecret() {
        return mysecret;
    }

    public void setMysecret(String mysecret) {
        this.mysecret = mysecret;
    }
}