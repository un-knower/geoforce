package com.supermap.egispweb.pojo;

/**
 * @author Administrator
 * zTree树形结构
 * 		id:   节点ID
 * 		pId：   父节点ID
 * 		name：节点名称
 * 		open：是否展开，true展开，false折叠
 * 		activeType: 车辆行为类型
 */
public class JsonZTree {
	private String id;
	private String pId;
	private String name;
	private String ename;
	private String code;
	private boolean open;
	private boolean checked;
	private boolean isParent;
	private String activeType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isParent() {
		return isParent;
	}
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	public String getActiveType() {
		return activeType;
	}
	public void setActiveType(String activeType) {
		this.activeType = activeType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
