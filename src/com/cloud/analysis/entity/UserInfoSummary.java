package com.cloud.analysis.entity;

import java.util.ArrayList;

public class UserInfoSummary {	
	//总的事务或任务、Agent汇总
	private ArrayList<UserInfoTaskSummary> successTask = null;
	//失败事务或任务、Agent汇总
	private ArrayList<UserInfoTaskSummary> failedTask = null;
	//运行中错误、失败原因汇总
	private ArrayList<UserInfoErrorSummary> error = null;
	//事务时间超过5秒得到需要显示的汇总
	private ArrayList<UserInfoOver5Summary> over5 = null;
	//请求汇总
	private ArrayList<UserInfoURLSummary> url = null;
	//请求里的失败汇总
	private ArrayList<UserInfoURLSummary> urlFailed = new ArrayList<UserInfoURLSummary>();
	
	public ArrayList<UserInfoTaskSummary> getSuccessTask() {
		return successTask;
	}
	public void setSuccessTask(ArrayList<UserInfoTaskSummary> successTask) {
		this.successTask = successTask;
	}
	public ArrayList<UserInfoTaskSummary> getFailedTask() {
		return failedTask;
	}
	public void setFailedTask(ArrayList<UserInfoTaskSummary> failedTask) {
		this.failedTask = failedTask;
	}
	public ArrayList<UserInfoErrorSummary> getError() {
		return error;
	}
	public void setError(ArrayList<UserInfoErrorSummary> error) {
		this.error = error;
	}
	public ArrayList<UserInfoOver5Summary> getOver5() {
		return over5;
	}
	public void setOver5(ArrayList<UserInfoOver5Summary> over5) {
		this.over5 = over5;
	}
	public ArrayList<UserInfoURLSummary> getUrl() {
		return url;
	}
	public void setUrl(ArrayList<UserInfoURLSummary> url) {
		this.url = url;
	}
	public ArrayList<UserInfoURLSummary> getUrlFailed() {
		return urlFailed;
	}
	public void setUrlFailed(ArrayList<UserInfoURLSummary> urlFailed) {
		this.urlFailed = urlFailed;
	}
	
	private String result = null;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}			
}
