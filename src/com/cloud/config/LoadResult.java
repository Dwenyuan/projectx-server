package com.cloud.config;

import java.util.Vector;

import com.cloud.utils.Utils;
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
	//ʧ��ԭ��
	private String error = null;
	
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

	public void setErrorType(int type, String value){
		if(type == 1){
			error = Utils.getInfoString("limitRun", value);
		}
		else if(type == 2){
			error = Utils.resources.getString("testcaseName");
		}
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	
}
