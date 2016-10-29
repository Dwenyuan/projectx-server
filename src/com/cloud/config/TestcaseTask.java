package com.cloud.config;

import java.util.List;

public class TestcaseTask {
	private String name = null;
	//脚本
	private String scripter = null;
	//参数文件
	private String paraFile = null;
	
	//脚本对应的执行器
	private List<LoadAgentConfig> loadAgent = null;
	//施压模型
	private Model model = null;
	
	//开始执行
	private StartConfig startConfig = null;
	//结束执行
	private StopConfig stopConfig = null;
	//运行次数
	private IteratorTimes iteratorTimes = null;
	//集合点
	private Rendezvous rendezvous = null;		
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScripter() {
		return scripter;
	}
	public void setScripter(String scripter) {
		this.scripter = scripter;
	}
	
	public String getParaFile() {
		return paraFile;
	}
	public void setParaFile(String paraFile) {
		this.paraFile = paraFile;
	}
	public List<LoadAgentConfig> getLoadAgent() {
		return loadAgent;
	}
	public void setLoadAgent(List<LoadAgentConfig> loadAgent) {
		this.loadAgent = loadAgent;
	}
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public StartConfig getStartConfig() {
		return startConfig;
	}
	public void setStartConfig(StartConfig startConfig) {
		this.startConfig = startConfig;
	}
	public StopConfig getStopConfig() {
		return stopConfig;
	}
	public void setStopConfig(StopConfig stopConfig) {
		this.stopConfig = stopConfig;
	}
	public IteratorTimes getIteratorTimes() {
		return iteratorTimes;
	}
	public void setIteratorTimes(IteratorTimes iteratorTimes) {
		this.iteratorTimes = iteratorTimes;
	}
	public Rendezvous getRendezvous() {
		return rendezvous;
	}
	public void setRendezvous(Rendezvous rendezvous) {
		this.rendezvous = rendezvous;
	}		
}
