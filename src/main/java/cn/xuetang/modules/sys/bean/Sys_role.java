package cn.xuetang.modules.sys.bean;

import java.util.List;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;

import cn.xuetang.core.bean.Permission;
import cn.xuetang.core.bean.User;

/**
 * @author Wizzer.cn
 * @time 2012-9-20 下午1:33:32
 * 
 */
@Table("sys_role")
public class Sys_role {
	@Column
	@Id
	@Prev({ @SQL(db = DB.ORACLE, value = "SELECT SYS_ROLE_S.nextval FROM dual") })
	private int id;
	@Column
	private String name;
	@Column
	private String unitid;
	@Column
	private String descript;
	@Column
	private int pid;
	@Column
	private int location;
	@ManyMany(target = Sys_user.class, relation = "system_user_role", from = "roleid", to = "userid")
	private List<Sys_user> users;
	@ManyMany(target = Permission.class, relation = "system_role_permission", from = "roleid", to = "permissionid")
	private List<Permission> permissions;
	
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public int getId() {
		return id;
	}

	public List<Sys_user> getUsers() {
		return users;
	}

	public void setUsers(List<Sys_user> users) {
		this.users = users;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

}
