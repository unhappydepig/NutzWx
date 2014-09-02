package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
/**
* @author Wizzer
* @time   2014-04-03 20:38:34
*/
@Table("weixin_content")
public class Weixin_content 
{
	@Id
	private int id;
	@Column
	private int pid;
	@Column
	private String channel_id;
	@Column
	private String title;
	@Column
	private String link;
	@Column
	private String title_color;
	@Column
	private String is_bold;
	@Column
	private String short_title;
	@Column
	private String author;
	@Column
	private String origin;
	@Column
	private String origin_url;
	@Column
	private String description;
	@Column
	private String media_type;
	@Column
	private String media_path;
	@Column
	private String title_img;
	@Column
	private String type;
	@Column
	private int top_level;
	@Column
	private String pub_time;
	@Column
	private long add_userid;
	@Column
	private String add_time;
	@Column
	private int send_type;
	@Column
	private int status;
	@Column
	private int views;
    private String txt;
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
	public String getChannel_id()
	{
		return channel_id;
	}
	public void setChannel_id(String channel_id)
	{
		this.channel_id=channel_id;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title=title;
	}
	public String getLink()
	{
		return link;
	}
	public void setLink(String link)
	{
		this.link=link;
	}
	public String getTitle_color()
	{
		return title_color;
	}
	public void setTitle_color(String title_color)
	{
		this.title_color=title_color;
	}
	public String getIs_bold()
	{
		return is_bold;
	}
	public void setIs_bold(String is_bold)
	{
		this.is_bold=is_bold;
	}
	public String getShort_title()
	{
		return short_title;
	}
	public void setShort_title(String short_title)
	{
		this.short_title=short_title;
	}
	public String getAuthor()
	{
		return author;
	}
	public void setAuthor(String author)
	{
		this.author=author;
	}
	public String getOrigin()
	{
		return origin;
	}
	public void setOrigin(String origin)
	{
		this.origin=origin;
	}
	public String getOrigin_url()
	{
		return origin_url;
	}
	public void setOrigin_url(String origin_url)
	{
		this.origin_url=origin_url;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getMedia_type()
	{
		return media_type;
	}
	public void setMedia_type(String media_type)
	{
		this.media_type=media_type;
	}
	public String getMedia_path()
	{
		return media_path;
	}
	public void setMedia_path(String media_path)
	{
		this.media_path=media_path;
	}
	public String getTitle_img()
	{
		return title_img;
	}
	public void setTitle_img(String title_img)
	{
		this.title_img=title_img;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type=type;
	}
	public int getTop_level()
	{
		return top_level;
	}
	public void setTop_level(int top_level)
	{
		this.top_level=top_level;
	}
	public String getPub_time()
	{
		return pub_time;
	}
	public void setPub_time(String pub_time)
	{
		this.pub_time=pub_time;
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
	public int getSend_type()
	{
		return send_type;
	}
	public void setSend_type(int send_type)
	{
		this.send_type=send_type;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status=status;
	}
	public int getViews()
	{
		return views;
	}
	public void setViews(int views)
	{
		this.views=views;
	}

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}