package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2014-04-28 15:23:18
*/
@Table("weixin_push_content")
public class Weixin_push_content 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT WEIXIN_PUSH_CONTENT_S.nextval FROM dual")
	})
	private int id;
    @Column
    private int pid;
    @Column
    private int appid;
	@Column
	private int pushid;
	@Column
	private String title;
	@Column
	private String author;
	@Column
	private String picurl;
	@Column
	private String thumb_media_id;
	@Column
	private String content_source_url;
	@Column
	private String content;
	@Column
	private String digest;
    @Column
    private int status;
    @Column
    private int views;
	@Column
	private long op_userid;
	@Column
	private String op_time;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public int getPushid()
	{
		return pushid;
	}
	public void setPushid(int pushid)
	{
		this.pushid=pushid;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title=title;
	}
	public String getAuthor()
	{
		return author;
	}
	public void setAuthor(String author)
	{
		this.author=author;
	}
	public String getPicurl()
	{
		return picurl;
	}
	public void setPicurl(String picurl)
	{
		this.picurl=picurl;
	}
	public String getThumb_media_id()
	{
		return thumb_media_id;
	}
	public void setThumb_media_id(String thumb_media_id)
	{
		this.thumb_media_id=thumb_media_id;
	}
	public String getContent_source_url()
	{
		return content_source_url;
	}
	public void setContent_source_url(String content_source_url)
	{
		this.content_source_url=content_source_url;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content=content;
	}
	public String getDigest()
	{
		return digest;
	}
	public void setDigest(String digest)
	{
		this.digest=digest;
	}
	public long getOp_userid()
	{
		return op_userid;
	}
	public void setOp_userid(long op_userid)
	{
		this.op_userid=op_userid;
	}
	public String getOp_time()
	{
		return op_time;
	}
	public void setOp_time(String op_time)
	{
		this.op_time=op_time;
	}

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}