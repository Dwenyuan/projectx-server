package com.cloud.config;

public class StopConfig {	
	private int stopIndex = 0;//ѡ���ĸ�
	private String stopUser = null;//���ٸ��û�ʧ��ʱֹͣ��ѡ��ڶ���ʱ��
	private String stopPercent = null;//�ٷ�֮���ٸ��û�ʧ��ʱֹͣ��ѡ�������ʱ��
	private String runTime = null;//���ж೤ʱ���ֹͣ
	private String stopTime = null;//���ý���ʱ�䣬ѡ����ĸ�ʱ��
	
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
