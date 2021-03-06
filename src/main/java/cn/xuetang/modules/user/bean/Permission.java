package cn.xuetang.modules.user.bean;

import java.io.Serializable;
import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import cn.xuetang.modules.sys.bean.Sys_role;

@Table("sys_permission")
@TableIndexes({ @Index(name = "permission_name_id", fields = { "name" }, unique = true) })
public class Permission implements Serializable {
	private static final long serialVersionUID = -8140799124476746216L;

	@Id
	private Long id;

	@Column
	@ColDefine(type = ColType.VARCHAR, width = 200)
	private String name;

	@Column
	@ColDefine(type = ColType.VARCHAR, width = 500)
	private String description;

	@ManyMany(target = Sys_role.class, relation = "sys_role_permission", from = "permissionid", to = "roleid")
	private List<Sys_role> roles;

	@Column("permission_category_id")
	private String permissionCategoryId;

	@One(target = PermissionCategory.class, field = "permissionCategoryId")
	private PermissionCategory permissionCategory;

	@Column("is_locked")
	@ColDefine(type = ColType.BOOLEAN)
	private boolean locked;

	@Column("is_show")
	@ColDefine(type = ColType.BOOLEAN)
	private boolean show;

	@Column("action_url")
	private String url;

	@Column("page_style")
	private String style;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getPermissionCategoryId() {
		return permissionCategoryId;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void setPermissionCategoryId(String permissionCategoryId) {
		this.permissionCategoryId = permissionCategoryId;
	}

	public PermissionCategory getPermissionCategory() {
		return permissionCategory;
	}

	public void setPermissionCategory(PermissionCategory permissionCategory) {
		this.permissionCategory = permissionCategory;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Sys_role> getRoles() {
		return roles;
	}

	public void setRoles(List<Sys_role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permission other = (Permission) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}