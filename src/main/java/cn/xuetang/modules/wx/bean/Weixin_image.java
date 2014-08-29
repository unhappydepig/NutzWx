package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2014-04-08 22:16:05
*/
@Table("weixin_image")
public class Weixin_image 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT WEIXIN_IMAGE_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int pid;
	@Column
	private int appid;
	@Column
	private String openid;
	@Column
	private String picurl;
	@Column
	private String image_url;
	@Column
	private String image_thumb;
	@Column
	private String image_water;
	@Column
	private String txt;
	@Column
	private int status;
	@Column
	private int nice;
	@Column
	private String face_id;
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
	public String getPicurl()
	{
		return picurl;
	}
	public void setPicurl(String picurl)
	{
		this.picurl=picurl;
	}
	public String getImage_url()
	{
		return image_url;
	}
	public void setImage_url(String image_url)
	{
		this.image_url=image_url;
	}
	public String getImage_thumb()
	{
		return image_thumb;
	}
	public void setImage_thumb(String image_thumb)
	{
		this.image_thumb=image_thumb;
	}
	public String getImage_water()
	{
		return image_water;
	}
	public void setImage_water(String image_water)
	{
		this.image_water=image_water;
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
	public String getFace_id()
	{
		return face_id;
	}
	public void setFace_id(String face_id)
	{
		this.face_id=face_id;
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