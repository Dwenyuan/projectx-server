package com.cloud.config;

import java.util.List;

public class TestcaseTask {
	private String name = null;
	//�ű�
	private String scripterID = null;
	private String scripterName = null;
	//�����ļ�
	private String paraID = null;
	private String paraName = null;
	
	//�ű���Ӧ��ִ����
	private List<LoadAgentConfig> loadAgent = null;
	//ʩѹģ��
	private Model model = null;
	
	//��ʼִ��
	private StartConfig startConfig = null;
	//����ִ��
	private StopConfig stopConfig = null;
	//���д���
	private IteratorTimes iteratorTimes = null;
	//���ϵ�
	private Rendezvous rendezvous = null;		
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	public String getScripterID() {
		return scripterID;
	}
	public void setScripterID(String scripterID) {
		this.scripterID = scripterID;
	}
	public String getScripterName() {
		return scripterName;
	}
	public void setScripterName(String scripterName) {
		this.scripterName = scripterName;
	}
	public String getParaID() {
		return paraID;
	}
	public void setParaID(String paraID) {
		this.paraID = paraID;
	}
	public String getParaName() {
		return paraName;
	}
	public void setParaName(String paraName) {
		this.paraName = paraName;
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
