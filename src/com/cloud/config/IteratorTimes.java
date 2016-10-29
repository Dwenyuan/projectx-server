package com.cloud.config;

public class IteratorTimes {
	private int onlineIndex = 0;//0表示运行完就结束，1表示一直循环，2表示运行相应个数
	private String loopTimes = null;//跌代运行多少次
	
	public int getOnlineIndex() {
		return onlineIndex;
	}
	public void setOnlineIndex(int onlineIndex) {
		this.onlineIndex = onlineIndex;
	}
	public String getLoopTimes() {
		return loopTimes;
	}
	public void setLoopTimes(String loopTimes) {
		this.loopTimes = loopTimes;
	}
	
	
}
