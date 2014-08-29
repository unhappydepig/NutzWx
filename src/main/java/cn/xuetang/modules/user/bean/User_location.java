package cn.xuetang.modules.user.bean;

import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2014-04-05 00:45:11
*/
@Table("user_location")
public class User_location 
{
	@Column
	@Name
    private String openid;
	@Column
	private int pid;
	@Column
	private int appid;
	@Column
	private int uid;
	@Column
	private String location_x;
    @Column
    private String location_y;
    @Column
    private String location_jd;
	@Column
	private String baidu_x;
	@Column
	private String baidu_y;
	@Column
	private String post_time;
	@Column
	private String lable;
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
	public String getOpenid()
	{
		return openid;
	}
	public void setOpenid(String openid)
	{
		this.openid=openid;
	}
	public String getLocation_x()
	{
		return location_x;
	}
	public void setLocation_x(String location_x)
	{
		this.location_x=location_x;
	}
	public String getLocation_y()
	{
		return location_y;
	}
	public void setLocation_y(String location_y)
	{
		this.location_y=location_y;
	}

    public String getLocation_jd() {
        return location_jd;
    }

    public void setLocation_jd(String location_jd) {
        this.location_jd = location_jd;
    }

    public String getBaidu_x()
	{
		return baidu_x;
	}
	public void setBaidu_x(String baidu_x)
	{
		this.baidu_x=baidu_x;
	}
	public String getBaidu_y()
	{
		return baidu_y;
	}
	public void setBaidu_y(String baidu_y)
	{
		this.baidu_y=baidu_y;
	}
	public String getPost_time()
	{
		return post_time;
	}
	public void setPost_time(String post_time)
	{
		this.post_time=post_time;
	}
	public String getLable()
	{
		return lable;
	}
	public void setLable(String lable)
	{
		this.lable=lable;
	}

}