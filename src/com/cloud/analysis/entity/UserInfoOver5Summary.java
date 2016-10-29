package com.cloud.analysis.entity;

import java.util.ArrayList;

public class UserInfoOver5Summary {
	private String task = null;
	private String name = null;
	
	private long total = 0;	
	private ArrayList<String[]> agent = null;//对应的agent个数
	
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
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
	public ArrayList<String[]> getAgent() {
		return agent;
	}
	public void setAgent(ArrayList<String[]> agent) {
		this.agent = agent;
	}		
}
