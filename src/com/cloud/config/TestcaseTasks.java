package com.cloud.config;

import java.util.List;

public class TestcaseTasks {
	private List<TestcaseTask> testcaseTasks = null;
	//���ٸ���
	private int manyToOne = 60;
	//����IP��ϵͳ����IP����������IP
	private int ipSpoof = 0;
	//0��ʾ�����ã�1��ʾÿһ���û���ӡһ����N��ʾN���û�ֻ��ӡһ����������������
	private int webDetail = 0;
	
	private int saveMode = 0;//�û����鱣�����
	
	private int logLevel = 0;
	
	private String name = "new";//��������
	
	private List<Monitor> monitor = null;//���
	
	public List<TestcaseTask> getTestcaseTasks() {
		return testcaseTasks;
	}

	public void setTestcaseTasks(List<TestcaseTask> testcaseTasks) {
		this.testcaseTasks = testcaseTasks;
	}

	public int getManyToOne() {
		return manyToOne;
	}

	public void setManyToOne(int manyToOne) {
		this.manyToOne = manyToOne;
	}

	public int getIpSpoof() {
		return ipSpoof;
	}

	public void setIpSpoof(int ipSpoof) {
		this.ipSpoof = ipSpoof;
	}

	public int getWebDetail() {
		return webDetail;
	}

	public void setWebDetail(int webDetail) {
		this.webDetail = webDetail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	public List<Monitor> getMonitor() {
		return monitor;
	}

	public void setMonitor(List<Monitor> monitor) {
		this.monitor = monitor;
	}

	public int getSaveMode() {
		return saveMode;
	}

	public void setSaveMode(int saveMode) {
		this.saveMode = saveMode;
	}				
}
