package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Name;
/**
* @author Wizzer
* @time   2014-04-03 20:38:34
*/
@Table("weixin_channel")
public class Weixin_channel 
{
	@Column
	@Name
	private String id;
	@Column
	private int pid;
	@Column
	private String name;
	@Column
	private int status;
    @Column
    private int views;
	@Column
	private long add_userid;
	@Column
	private String add_time;
	@Column
	private int location;
		public String getId()
	{
		return id;
	}
	public void setId(String id)
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
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status=status;
	}
	public long getAdd_userid()
	{
		return add_userid;
	}
	public void setAdd_userid(long add_userid)
	{
		this.add_userid=add_userid;
	}
	public String getAdd_time()
	{
		return add_time;
	}
	public void setAdd_time(String add_time)
	{
		this.add_time=add_time;
	}
	public int getLocation()
	{
		return location;
	}
	public void setLocation(int location)
	{
		this.location=location;
	}

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}