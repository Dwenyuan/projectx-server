package com.cloud.analysis.entity;

public class UserAnalysisEntity {
	private int row = 0;//行数
	private int start = 0;//时间
	private long user = 0;//用户索引
	private String agent = null;//归属agent
	private int success = 0;//成功或失败
	private String failed = null;//失败原因
	private int time = -1;//整个会话时间
	private int diff = 0;//与录制比较
	private int urls = 0;//总的请求数或发送接收节点数	
	
	//用于读取整个WEBInfo，得到类似快照的表格
	private int len = 0;//WEBInfo数据长度
	private long file = 0;//WEBInfo数据在webInfo文件的索引
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public long getUser() {
		return user;
	}
	public void setUser(long user) {
		this.user = user;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public String getFailed() {
		return failed;
	}
	public void setFailed(String failed) {
		this.failed = failed;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getDiff() {
		return diff;
	}
	public void setDiff(int diff) {
		this.diff = diff;
	}
	public int getUrls() {
		return urls;
	}
	public void setUrls(int urls) {
		this.urls = urls;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public long getFile() {
		return file;
	}
	public void setFile(long file) {
		this.file = file;
	}		
}
