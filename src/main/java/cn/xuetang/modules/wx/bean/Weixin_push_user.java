package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2014-04-30 13:59:23
*/
@Table("weixin_push_user")
public class Weixin_push_user 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT WEIXIN_PUSH_USER_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int pushid;
	@Column
	private String openids;
	@Column
	private int total;
	@Column
	private int pagenum;
	@Column
	private int status;
	@Column
	private int errcode;
	@Column
	private String errmsg;
	@Column
	private long msg_id;
	@Column
	private String res_status;
	@Column
	private int res_totalcount;
	@Column
	private int res_filtercount;
	@Column
	private int res_sentcount;
	@Column
	private int res_errorcount;
	@Column
	private String res_createtime;
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
	public String getOpenids()
	{
		return openids;
	}
	public void setOpenids(String openids)
	{
		this.openids=openids;
	}
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total=total;
	}
	public int getPagenum()
	{
		return pagenum;
	}
	public void setPagenum(int pagenum)
	{
		this.pagenum=pagenum;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status=status;
	}
	public int getErrcode()
	{
		return errcode;
	}
	public void setErrcode(int errcode)
	{
		this.errcode=errcode;
	}
	public String getErrmsg()
	{
		return errmsg;
	}
	public void setErrmsg(String errmsg)
	{
		this.errmsg=errmsg;
	}
	public long getMsg_id()
	{
		return msg_id;
	}
	public void setMsg_id(long msg_id)
	{
		this.msg_id=msg_id;
	}
	public String getRes_status()
	{
		return res_status;
	}
	public void setRes_status(String res_status)
	{
		this.res_status=res_status;
	}
	public int getRes_totalcount()
	{
		return res_totalcount;
	}
	public void setRes_totalcount(int res_totalcount)
	{
		this.res_totalcount=res_totalcount;
	}
	public int getRes_filtercount()
	{
		return res_filtercount;
	}
	public void setRes_filtercount(int res_filtercount)
	{
		this.res_filtercount=res_filtercount;
	}
	public int getRes_sentcount()
	{
		return res_sentcount;
	}
	public void setRes_sentcount(int res_sentcount)
	{
		this.res_sentcount=res_sentcount;
	}
	public int getRes_errorcount()
	{
		return res_errorcount;
	}
	public void setRes_errorcount(int res_errorcount)
	{
		this.res_errorcount=res_errorcount;
	}
	public String getRes_createtime()
	{
		return res_createtime;
	}
	public void setRes_createtime(String res_createtime)
	{
		this.res_createtime=res_createtime;
	}

}