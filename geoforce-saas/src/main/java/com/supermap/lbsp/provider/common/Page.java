package com.supermap.lbsp.provider.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象. 包含当前页数据及分页信息如总记录数.
 */

public class Page implements Serializable {
	private static final long serialVersionUID = -1289317936728849804L;

	private static int DEFAULT_PAGE_SIZE = 10;

	private int pageSize = DEFAULT_PAGE_SIZE; // 每页的记录数
	private int currentPageNum; // 当前页
	private int totalNum; // 总记录数
	private int pageNum; // 总页数
	private int pageClick = 0;
	private List result = new ArrayList(); // 当前页中存放的记录,类型一般为List
	

	/**
	 * 构造方法，只构造空页.
	 */

	
	public Page() {
		this(1, 0, DEFAULT_PAGE_SIZE, new ArrayList());
		
	}

	public Page(int currentPageNum, int pageSize) {
		this(currentPageNum, 100000, pageSize, new ArrayList());
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
		this.currentPageNum = 1;
		this.totalNum = 0;
		this.result = new ArrayList();
	}

	/**
	 * 默认构造方法.
	 * 
	 * @param currentPageNum
	 *            本页数
	 * @param totalSize
	 *            数据库中总记录条数
	 * @param pageSize
	 *            本页容量
	 * @param data
	 *            本页包含的数据
	 */
	public Page(int currentPageNum, int totalSize, int pageSize, List data) {
		this.pageSize = pageSize;
		this.currentPageNum = currentPageNum < 1 ? 1 : currentPageNum;
		this.totalNum = totalSize;
		this.result = data;
	}

	/**
	 * 取总记录数.
	 */
	public int getTotalNum() {
		return this.totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	/**
	 * 取总页数.
	 */
	public int getPageNum() {
		if (totalNum % pageSize == 0)
			this.pageNum = totalNum / pageSize;
		else
			this.pageNum = totalNum / pageSize + 1;

		return pageNum;	
	}
	/**
	 * 取每页数据容量.
	 */
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 取当前页中的记录.
	 */
	public List getResult() {
		return result;
	}

	public void setResult(List result) {
		if (null != result)
			this.result = result;
	}

	/**
	 * 取该页当前页码,页码从1开始.
	 */
	public int getCurrentPageNum() {
		return currentPageNum;
	}

	public void setCurrentPageNum(int currentPageNum) {
		this.currentPageNum = currentPageNum < 1 ? 1 : currentPageNum;
		if(this.pageNum != 0)
			this.currentPageNum = currentPageNum > this.pageNum ? this.pageNum : currentPageNum;
	}

	/**
	 * 该页是否有下一页.
	 */
	public boolean hasNextPage() {
		return this.getCurrentPageNum() < this.getPageNum();
	}

	/**
	 * 该页是否有上一页.
	 */
	public boolean hasPreviousPage() {
		return this.getCurrentPageNum() > 1;
	}

	/**
	 * 该页是否有下一页；返回1表示有，返回0表示无
	 */
	public int getNextPage() {
		if (this.hasNextPage()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 该页是否有上一页；返回1表示有，返回0表示无
	 */
	public int getPreviousPage() {
		if (this.hasPreviousPage()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 获取任一页第一条数据在数据集的位置.
	 * 
	 * @param PageNum
	 *            从1开始的页号
	 * @param pageSize
	 *            每页记录条数
	 * @return 该页第一条数据
	 */
	public int getStartLineNum() {
		if (getPageNum() < getCurrentPageNum()) {
			setCurrentPageNum(getPageNum());
		}
		return (currentPageNum - 1) * pageSize;
	}

	public int getPageClick() {
		return pageClick;
	}

	public void setPageClick(int pageClick) {
		this.pageClick = pageClick;
	}

}