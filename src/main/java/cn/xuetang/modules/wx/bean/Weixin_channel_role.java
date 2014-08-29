package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
/**
* @author Wizzer
* @time   2014-04-05 13:38:45
*/
@Table("weixin_channel_role")
public class Weixin_channel_role 
{
	@Column
	private int pid;
	@Column
	private int role_id;
	@Column
	private String channel_id;
		public int getPid()
	{
		return pid;
	}
	public void setPid(int pid)
	{
		this.pid=pid;
	}
	public int getRole_id()
	{
		return role_id;
	}
	public void setRole_id(int role_id)
	{
		this.role_id=role_id;
	}
	public String getChannel_id()
	{
		return channel_id;
	}
	public void setChannel_id(String channel_id)
	{
		this.channel_id=channel_id;
	}

}