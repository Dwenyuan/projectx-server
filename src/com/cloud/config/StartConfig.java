package com.cloud.config;

public class StartConfig {
	//0立即开始，1休眠相应秒数后开始，2特定日期时间开始
	private int startIndex = 0;//选择哪个
	private String sleepTime = null;//设置开始时间前休眠的时间
	private String startTime = null;//设置开始时间，选择第一个时用
	
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public String getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	
}
