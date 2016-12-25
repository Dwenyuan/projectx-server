package com.cloud.analysis.entity;

import java.util.ArrayList;

public class UserInfoSummary {	
	//�ܵ����������Agent����
	private ArrayList<UserInfoTaskSummary> successTask = null;
	//ʧ�����������Agent����
	private ArrayList<UserInfoTaskSummary> failedTask = null;
	//�����д���ʧ��ԭ�����
	private ArrayList<UserInfoErrorSummary> error = null;
	//����ʱ�䳬��5��õ���Ҫ��ʾ�Ļ���
	private ArrayList<UserInfoOver5Summary> over5 = null;
	//�������
	private ArrayList<UserInfoURLSummary> url = null;
	//�������ʧ�ܻ���
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
