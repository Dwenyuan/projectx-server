package com.cloud.config;

import java.util.List;

public class TestcaseTasks {
	private List<TestcaseTask> testcaseTasks = null;
	//多少个点
	private int manyToOne = 60;
	//禁用IP，系统虚拟IP，根据虚拟IP
	private int ipSpoof = 0;
	//0表示不启用，1表示每一个用户打印一个，N表示N个用户只打印一个。考虑性能问题
	private int webDetail = 0;
	
	private int saveMode = 0;//用户详情保存策略
	
	private int logLevel = 0;
	
	private String name = "new";//场景名称
	
	private List<Monitor> monitor = null;//监控
	
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
