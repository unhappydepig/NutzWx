package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
/**
* @author Wizzer
* @time   2014-04-08 22:16:05
*/
@Table("weixin_video")
public class Weixin_video 
{
	@Id
	private int id;
	@Column
	private int pid;
	@Column
	private int appid;
	@Column
	private String openid;
	@Column
	private String mediaid;
	@Column
	private String thumbmediaid;
	@Column
	private String video_url;
	@Column
	private String pic_url;
	@Column
	private String txt;
	@Column
	private int status;
	@Column
	private int nice;
	@Column
	private String post_time;
    @Column
    private int tvid;
    @Column
    private int comment;
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
	public String getOpenid()
	{
		return openid;
	}
	public void setOpenid(String openid)
	{
		this.openid=openid;
	}
	public String getMediaid()
	{
		return mediaid;
	}
	public void setMediaid(String mediaid)
	{
		this.mediaid=mediaid;
	}
	public String getThumbmediaid()
	{
		return thumbmediaid;
	}
	public void setThumbmediaid(String thumbmediaid)
	{
		this.thumbmediaid=thumbmediaid;
	}
	public String getVideo_url()
	{
		return video_url;
	}
	public void setVideo_url(String video_url)
	{
		this.video_url=video_url;
	}
	public String getPic_url()
	{
		return pic_url;
	}
	public void setPic_url(String pic_url)
	{
		this.pic_url=pic_url;
	}
	public String getTxt()
	{
		return txt;
	}
	public void setTxt(String txt)
	{
		this.txt=txt;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status=status;
	}
	public int getNice()
	{
		return nice;
	}
	public void setNice(int nice)
	{
		this.nice=nice;
	}
	public String getPost_time()
	{
		return post_time;
	}
	public void setPost_time(String post_time)
	{
		this.post_time=post_time;
	}

    public int getTvid() {
        return tvid;
    }

    public void setTvid(int tvid) {
        this.tvid = tvid;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}