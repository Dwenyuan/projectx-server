package com.cloud.analysis.entity;

import java.util.HashMap;

public class UserInfoURLSummary {
	private String task = null;
	private int type = 0;//0��ʾ�Ƿ��ͣ�1��ʾ�ǽ��գ�2��ʾ������	
	private String name = null;//URL������
	
	//����3�����Թ�����
	private String host = null;//��������http rtsp��
	private String belong = "";//��������
	private int httpType = 100;
		
	private String method = null;
	private String rCode = null;//¼����Ӧ�룬��http��rtsp��		
	private int rTime = 0;//¼��ʱ�䣬����ֵ������Ϊ0ʱ������Ϊ��һ��ֵ
	private int rBytes = 0;//¼��ʱ���ֽڣ�����ֵ		
	
	private long total = 0;//����
	private long success = 0;
	private long failed = 0;
		
	private long ave = 0;
	private long aveBytes = 0;
	private int max = 0;
	private int min = Integer.MAX_VALUE;
	private int[] percent = new int[10];//90% 10  80% 20   70%  30   60  40   50  50

	private HashMap<String, Integer> failedType = new HashMap<String, Integer>();

	//�������û������ťչ������ϸ��ʱ�仨����
	private long dnsTime = 0;
	private long tcp = 0;
	private long first = 0;
	private long left = 0;
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getBelong() {
		return belong;
	}
	public void setBelong(String belong) {
		this.belong = belong;
	}
	
	public int getHttpType() {
		return httpType;
	}
	public void setHttpType(int httpType) {
		this.httpType = httpType;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getrCode() {
		return rCode;
	}
	public void setrCode(String rCode) {
		this.rCode = rCode;
	}
	public int getrTime() {
		return rTime;
	}
	public void setrTime(int rTime) {
		this.rTime = rTime;
	}
	public int getrBytes() {
		return rBytes;
	}
	public void setrBytes(int rBytes) {
		this.rBytes = rBytes;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getSuccess() {
		return success;
	}
	public void setSuccess(long success) {
		this.success = success;
	}
	public long getFailed() {
		return failed;
	}
	public void setFailed(long failed) {
		this.failed = failed;
	}
	public long getAve() {
		return ave;
	}
	public void setAve(long ave) {
		this.ave = ave;
	}
	public long getAveBytes() {
		return aveBytes;
	}
	public void setAveBytes(long aveBytes) {
		this.aveBytes = aveBytes;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int[] getPercent() {
		return percent;
	}
	public void setPercent(int[] percent) {
		this.percent = percent;
	}
	public HashMap<String, Integer> getFailedType() {
		return failedType;
	}
	public void setFailedType(HashMap<String, Integer> failedType) {
		this.failedType = failedType;
	}
	public long getDnsTime() {
		return dnsTime;
	}
	public void setDnsTime(long dnsTime) {
		this.dnsTime = dnsTime;
	}
	public long getTcp() {
		return tcp;
	}
	public void setTcp(long tcp) {
		this.tcp = tcp;
	}
	public long getFirst() {
		return first;
	}
	public void setFirst(long first) {
		this.first = first;
	}
	public long getLeft() {
		return left;
	}
	public void setLeft(long left) {
		this.left = left;
	}			
}
