package cn.xuetang.modules.user.bean;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * @author 科技㊣²º¹³ 2014年4月19日 上午8:54:23 http://www.rekoe.com QQ:5382211
 */
@Table("permission_category")
@TableIndexes({ @Index(name = "permission_listIndex", fields = { "listIndex" }, unique = true) })
public class PermissionCategory {

	@Name
	@Prev(els = { @EL("uuid()") })
	private String id;
	@Column
	private String name;
	@Many(target = Permission.class, field = "permissionCategoryId")
	private List<Permission> permissions;
	@Column("is_locked")
	@ColDefine(type = ColType.BOOLEAN)
	private boolean locked;

	@Column("list_index")
	private int listIndex;

	public int getListIndex() {
		return listIndex;
	}

	public void setListIndex(int listIndex) {
		this.listIndex = listIndex;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
