package cn.xuetang.modules.sys.bean;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */
@Table("sys_user")
@TableIndexes({ @Index(name = "user_name", fields = { "loginname" }, unique = true) })
public class Sys_user {
	@Id
	private long userid;
	@Column
	private String loginname;
	@Column
	private String realname;
	@Column
	private String unitid;
	@Column
	private String password;
	@Column
	private String salt;
	@Column
	private String descript;
	@Column
	private String pozition;
	@Column
	private String address;
	@Column
	private String telephone;
	@Column
	private String mobile;
	@Column
	private String email;
	@Column
	private int location;
	@Column
	private String style;
	@Column
	private int logintype;
	@ColDefine(type = ColType.TIMESTAMP)
	@Column("login_time")
	private Date loginTime;
	@Column
	private String loginip;
	@Column
	private int logincount;
	@Column
	private String loginresid;
	@ManyMany(target = Sys_role.class, relation = "system_user_role", from = "userid", to = "roleid")
	private List<Sys_role> roles;
	@Column("is_locked")
	private boolean locked;

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	private String unitname;

	public List<Sys_role> getRoles() {
		return roles;
	}

	public void setRoles(List<Sys_role> roles) {
		this.roles = roles;
	}

	// 是否超级管理员角色
	private boolean sysrole;

	private List<Integer> rolelist;
	private List<Integer> prolist;
	private List<String> reslist;

	private Hashtable<String, String> btnmap;

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getPozition() {
		return pozition;
	}

	public void setPozition(String pozition) {
		this.pozition = pozition;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getLogintype() {
		return logintype;
	}

	public void setLogintype(int logintype) {
		this.logintype = logintype;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginip() {
		return loginip;
	}

	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}

	public int getLogincount() {
		return logincount;
	}

	public void setLogincount(int logincount) {
		this.logincount = logincount;
	}

	public String getLoginresid() {
		return loginresid;
	}

	public void setLoginresid(String loginresid) {
		this.loginresid = loginresid;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public List<Integer> getRolelist() {
		return rolelist;
	}

	public void setRolelist(List<Integer> rolelist) {
		this.rolelist = rolelist;
	}

	public List<String> getReslist() {
		return reslist;
	}

	public void setReslist(List<String> reslist) {
		this.reslist = reslist;
	}

	public boolean getSysrole() {
		return sysrole;
	}

	public void setSysrole(boolean sysrole) {
		this.sysrole = sysrole;
	}

	public Hashtable<String, String> getBtnmap() {
		return btnmap;
	}

	public void setBtnmap(Hashtable<String, String> btnmap) {
		this.btnmap = btnmap;
	}

	public List<Integer> getProlist() {
		return prolist;
	}

	public void setProlist(List<Integer> prolist) {
		this.prolist = prolist;
	}
}
