package com.cloud.analysis.entity;

public class UserAnalysisEntity {
	private int row = 0;//����
	private int start = 0;//ʱ��
	private long user = 0;//�û�����
	private String agent = null;//����agent
	private int success = 0;//�ɹ���ʧ��
	private String failed = null;//ʧ��ԭ��
	private int time = -1;//�����Ựʱ��
	private int diff = 0;//��¼�ƱȽ�
	private int urls = 0;//�ܵ����������ͽ��սڵ���	
	
	//���ڶ�ȡ����WEBInfo���õ����ƿ��յı��
	private int len = 0;//WEBInfo���ݳ���
	private long file = 0;//WEBInfo������webInfo�ļ�������
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public long getUser() {
		return user;
	}
	public void setUser(long user) {
		this.user = user;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public String getFailed() {
		return failed;
	}
	public void setFailed(String failed) {
		this.failed = failed;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getDiff() {
		return diff;
	}
	public void setDiff(int diff) {
		this.diff = diff;
	}
	public int getUrls() {
		return urls;
	}
	public void setUrls(int urls) {
		this.urls = urls;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public long getFile() {
		return file;
	}
	public void setFile(long file) {
		this.file = file;
	}		
}
