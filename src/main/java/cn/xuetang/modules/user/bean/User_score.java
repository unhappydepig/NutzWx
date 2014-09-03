package cn.xuetang.modules.user.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author Wizzer
 * @time 2014-04-05 00:45:11
 */
@Table("user_score")
public class User_score {
	@Id(auto = false)
	private int uid;
	@Column
	private int score;
	@Column
	private int p_score;
	@Column
	private int b_score;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getP_score() {
		return p_score;
	}

	public void setP_score(int p_score) {
		this.p_score = p_score;
	}

	public int getB_score() {
		return b_score;
	}

	public void setB_score(int b_score) {
		this.b_score = b_score;
	}

}