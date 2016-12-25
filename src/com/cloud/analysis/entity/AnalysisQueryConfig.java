package com.cloud.analysis.entity;

public class AnalysisQueryConfig {	
	private String agent;
	private int startTime;
	private int endTime;
	private int largerThan;
	private int smallerThan;
	private int requestStart;
	private int requestEnd;
	private int success = -1;//-1表示全部，0表示成功，1表示失败
	private String failedReceive = null;//错误响应码
	private int faileType = -1;//失败类型
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public int getLargerThan() {
		return largerThan;
	}
	public void setLargerThan(int largerThan) {
		this.largerThan = largerThan;
	}
	public int getSmallerThan() {
		return smallerThan;
	}
	public void setSmallerThan(int smallerThan) {
		this.smallerThan = smallerThan;
	}
	public int getRequestStart() {
		return requestStart;
	}
	public void setRequestStart(int requestStart) {
		this.requestStart = requestStart;
	}
	public int getRequestEnd() {
		return requestEnd;
	}
	public void setRequestEnd(int requestEnd) {
		this.requestEnd = requestEnd;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public String getFailedReceive() {
		return failedReceive;
	}
	public void setFailedReceive(String failedReceive) {
		this.failedReceive = failedReceive;
	}
	public int getFaileType() {
		return faileType;
	}
	public void setFaileType(int faileType) {
		this.faileType = faileType;
	}

	
}
