package com.supermap.egispweb.pojo;

import java.util.List;

/**
 * 查询类接口返回分页信息类
* @ClassName: PageBean
* @author WangShuang
* @date 2013-3-13 下午05:25:52
 */
public class PageBean {

	private Integer page;//当前页
	private Integer rows;//每页记录数
	private Long record;//总记录数
	private Long total;//总页数  
	private List list;
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Long getRecord() {
		return record;
	}
	public void setRecord(Long record) {
		this.record = record;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	
}
