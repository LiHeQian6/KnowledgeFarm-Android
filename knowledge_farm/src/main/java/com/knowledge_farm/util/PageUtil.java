package com.knowledge_farm.util;

import java.util.List;

/**
 * Description:  <br /> 
 * Project: itclass <br />
 * ClassName: Page <br />
 * Copyright: Copyright (c) 2011 onest<br />
 * @author wangwei
 * @version 1.0 2013-4-18上午9:19:53
 *
 */
public class PageUtil<T> {
	private List<T> list;
	private int currentPageNum;
	private int pageSize;
	private int prePageNum;
	private int nextPageNum;
	private int totalPageNum;
	private int totalCount;
	
	public PageUtil(){}
	
	public PageUtil(int pageNum, int pageSize){
		this.currentPageNum=pageNum;
		this.pageSize=pageSize;
	}
	
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public int getCurrentPageNum() {
		return currentPageNum;
	}
	public void setCurrentPageNum(int currentPageNum) {
		this.currentPageNum = currentPageNum;
	}
	public int getTotalPageNum() {
		return totalPageNum;
	}
	public void setTotalPageNum(int totalPageNum) {
		totalPageNum = totalPageNum;
	}
	public int getPrePageNum() {
		return prePageNum;
	}
	public void setPrePageNum(int prePageNum) {
		this.prePageNum = prePageNum;
	}
	public int getNextPageNum() {
		return nextPageNum;
	}
	public void setNextPageNum(int nextPageNum) {
		this.nextPageNum = nextPageNum;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		if(totalCount != 0) {
			if(totalCount%pageSize==0)
				totalPageNum=totalCount/pageSize;
			else
				totalPageNum=totalCount/pageSize+1;
			
			if(currentPageNum>1)
				prePageNum=currentPageNum-1;
			else
				prePageNum=1;
			
			if(currentPageNum<totalPageNum)
				nextPageNum=currentPageNum+1;
			else
				nextPageNum=totalPageNum;
		}else {
			prePageNum = 1;
			nextPageNum = 1;
			totalPageNum = 1;
		}
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}

