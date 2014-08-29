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
@Table("user_account")
public class User_account 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT USER_ACCOUNT_S.nextval FROM dual")
	})
	private int uid;
	@Column
	private int pid;
	@Column
	private String loginname;
	@Column
	private String email;
	@Column
	private int email_status;
	@Column
	private String password;
	@Column
	private String salt;
	@Column
	private int status;
    @Column
    private String reg_type;
	@Column
	private String reg_time;
	@Column
	private String reg_ip;
	@Column
	private String login_time;
	@Column
	private String login_ip;
	@Column
	private String login_type;
		public int getUid()
	{
		return uid;
	}
	public void setUid(int uid)
	{
		this.uid=uid;
	}
	public int getPid()
	{
		return pid;
	}
	public void setPid(int pid)
	{
		this.pid=pid;
	}
	public String getLoginname()
	{
		return loginname;
	}
	public void setLoginname(String loginname)
	{
		this.loginname=loginname;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	public int getEmail_status()
	{
		return email_status;
	}
	public void setEmail_status(int email_status)
	{
		this.email_status=email_status;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password=password;
	}
	public String getSalt()
	{
		return salt;
	}
	public void setSalt(String salt)
	{
		this.salt=salt;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status=status;
	}
	public String getReg_time()
	{
		return reg_time;
	}
	public void setReg_time(String reg_time)
	{
		this.reg_time=reg_time;
	}
	public String getReg_ip()
	{
		return reg_ip;
	}
	public void setReg_ip(String reg_ip)
	{
		this.reg_ip=reg_ip;
	}
	public String getLogin_time()
	{
		return login_time;
	}
	public void setLogin_time(String login_time)
	{
		this.login_time=login_time;
	}
	public String getLogin_ip()
	{
		return login_ip;
	}
	public void setLogin_ip(String login_ip)
	{
		this.login_ip=login_ip;
	}
	public String getLogin_type()
	{
		return login_type;
	}
	public void setLogin_type(String login_type)
	{
		this.login_type=login_type;
	}

    public String getReg_type() {
        return reg_type;
    }

    public void setReg_type(String reg_type) {
        this.reg_type = reg_type;
    }
}