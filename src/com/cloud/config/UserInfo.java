package com.cloud.config;

public class UserInfo {
	private String name = null;
	private int taskNum = 1;//能运行的任务数，可以同时运行多个
	private long space = 10 * 1000000000;//一个用户允许使用的磁盘空间
	
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
