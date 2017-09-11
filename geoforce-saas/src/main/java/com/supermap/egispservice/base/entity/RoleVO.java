package com.supermap.egispservice.base.entity;


/**
 * 用户选择角色，显示角色的checkbox数据对象
 */
public class RoleVO implements java.io.Serializable {

	private String id;
	private String name;
	private boolean checked;
	public RoleVO() {
	}
	public RoleVO(String id, String name, boolean checked) {
		super();
		this.id = id;
		this.name = name;
		this.checked = checked;
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
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
