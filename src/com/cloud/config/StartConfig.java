package com.cloud.config;

public class StartConfig {
	//0������ʼ��1������Ӧ������ʼ��2�ض�����ʱ�俪ʼ
	private int startIndex = 0;//ѡ���ĸ�
	private String sleepTime = null;//���ÿ�ʼʱ��ǰ���ߵ�ʱ��
	private String startTime = null;//���ÿ�ʼʱ�䣬ѡ���һ��ʱ��
	
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
