package com.cloud.config;

public class UserInfo {
	private String name = null;
	private int taskNum = 1;//�����е�������������ͬʱ���ж��
	private long space = 10 * 1000000000;//һ���û�����ʹ�õĴ��̿ռ�
	
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
