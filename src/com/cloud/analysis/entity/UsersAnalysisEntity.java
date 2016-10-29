package com.cloud.analysis.entity;

import java.util.List;

public class UsersAnalysisEntity {
	private long total = 0;
	private int page = 0;
	private String des;	
	private List<UserAnalysisEntity> list = null;
	
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}		
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public List<UserAnalysisEntity> getList() {
		return list;
	}
	public void setList(List<UserAnalysisEntity> list) {
		this.list = list;
	}
	
	private String result = null;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}	
}
