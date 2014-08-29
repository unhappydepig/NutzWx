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
@Table("user_score_change")
public class User_score_change 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT USER_SCORE_CHANGE_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int uid;
    @Column
    private int score_pre;
    @Column
    private int score_next;
	@Column
	private String text;
    @Column
    private String day;
	@Column
	private String add_time;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public int getUid()
	{
		return uid;
	}
	public void setUid(int uid)
	{
		this.uid=uid;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text=text;
	}
	public String getAdd_time()
	{
		return add_time;
	}
	public void setAdd_time(String add_time)
	{
		this.add_time=add_time;
	}

    public int getScore_pre() {
        return score_pre;
    }

    public void setScore_pre(int score_pre) {
        this.score_pre = score_pre;
    }

    public int getScore_next() {
        return score_next;
    }

    public void setScore_next(int score_next) {
        this.score_next = score_next;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}