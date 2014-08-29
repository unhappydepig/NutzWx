package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.DB;
import org.nutz.repo.Base64;

/**
* @author Wizzer
* @time   2014-04-05 00:46:13
*/
@Table("weixin_txt")
public class Weixin_txt 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT WEIXIN_TXT_S.nextval FROM dual")
	})
	private long id;
	@Column
	private int pid;
	@Column
	private int appid;
	@Column
	private String openid;
	@Column
	private String createtime;
	@Column
	private String msgtype;
	@Column
	private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
	public String getOpenid()
	{
		return openid;
	}
	public void setOpenid(String openid)
	{
		this.openid=openid;
	}
	public String getCreatetime()
	{
		return createtime;
	}
	public void setCreatetime(String createtime)
	{
		this.createtime=createtime;
	}
	public String getMsgtype()
	{
		return msgtype;
	}
	public void setMsgtype(String msgtype)
	{
		this.msgtype=msgtype;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content=content;
	}

}