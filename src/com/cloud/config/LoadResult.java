package com.cloud.config;

import java.util.Vector;
/**
 * �������÷��ؽ��
 * @author Administrator
 *
 */
public class LoadResult {
	//ִ���Ƿ�ɹ�
	private boolean result = false;
	//����agentʧ������Щ�����ܶ���û�����ִ����������һ�����ú�ִ��ʱ����Щagent�Ѿ���������ִ�С��������ú�agentֹͣ������
	//�����ʧ�ܣ�֪ͨ�û������û���������
	//֧��ͬ������Ϊ���Ӷ��agent�ǲ��е�
	private Vector<LoadAgentConfig> failedAgents = new Vector<LoadAgentConfig>();
	//1��ʾ�����������У���Ҫ��ֹͣ
	private int errorType = 0;
	
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	
	public Vector<LoadAgentConfig> getFailedAgents() {
		return failedAgents;
	}
	public void setFailedAgents(Vector<LoadAgentConfig> failedAgents) {
		this.failedAgents = failedAgents;
	}	
	public void addFail(LoadAgentConfig config){
		failedAgents.add(config);
	}
	public int getErrorType() {
		return errorType;
	}
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}
	
}
