package com.cloud.config;

public class UserInfo {
	private String name = null;
	private int taskNum = 1;//能运行的任务数，可以同时运行多个
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(int taskNum) {
		this.taskNum = taskNum;
	}
	
	
}
