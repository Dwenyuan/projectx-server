package com.cloud.analysis.entity;

import java.util.ArrayList;

public class UserInfoErrorSummary {
	private String task = null;
	private int type = 1;
	private String name = null;
	
	private long total = 0;
	private int urls = 0;//对应的URL个数，通过索引聚合一样的URL
	private ArrayList<String[]> agent = null;//对应的agent个数
	
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getUrls() {
		return urls;
	}
	public void setUrls(int urls) {
		this.urls = urls;
	}
	public ArrayList<String[]> getAgent() {
		return agent;
	}
	public void setAgent(ArrayList<String[]> agent) {
		this.agent = agent;
	}	
}
