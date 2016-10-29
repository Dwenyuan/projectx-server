package com.cloud.config;

public class StopConfig {	
	private int stopIndex = 0;//选择哪个
	private String stopUser = null;//多少个用户失败时停止，选择第二个时用
	private String stopPercent = null;//百分之多少个用户失败时停止，选择第三个时用
	private String runTime = null;//呼叫多长时间就停止
	private String stopTime = null;//设置结束时间，选择第四个时用
	
	public int getStopIndex() {
		return stopIndex;
	}
	public void setStopIndex(int stopIndex) {
		this.stopIndex = stopIndex;
	}
	public String getStopUser() {
		return stopUser;
	}
	public void setStopUser(String stopUser) {
		this.stopUser = stopUser;
	}
	public String getStopPercent() {
		return stopPercent;
	}
	public void setStopPercent(String stopPercent) {
		this.stopPercent = stopPercent;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getStopTime() {
		return stopTime;
	}
	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}	
}
