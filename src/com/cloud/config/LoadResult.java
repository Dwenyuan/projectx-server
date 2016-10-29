package com.cloud.config;

import java.util.Vector;
/**
 * 加载配置返回结果
 * @author Administrator
 *
 */
public class LoadResult {
	//执行是否成功
	private boolean result = false;
	//连接agent失败有哪些，可能多个用户共用执行器，导致一起配置和执行时，有些agent已经被别人先执行。或者配置后，agent停止运行了
	//则加载失败，通知用户，让用户重新配置
	//支持同步，因为连接多个agent是并行的
	private Vector<LoadAgentConfig> failedAgents = new Vector<LoadAgentConfig>();
	//1表示任务在运行中，需要先停止
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
