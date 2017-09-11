package com.supermap.egispservice.base.entity;


/**
 * ztree
 */
public class ZtreeVO implements java.io.Serializable {

	private String id;
	private String name;
	private String pid;
	private boolean checked;
	public ZtreeVO() {
	}
	public ZtreeVO(String id, String name, String pid, boolean checked) {
		super();
		this.id = id;
		this.name = name;
		this.pid = pid;
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
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	
}
