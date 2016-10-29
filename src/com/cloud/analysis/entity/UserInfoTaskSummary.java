package com.cloud.analysis.entity;

public class UserInfoTaskSummary {
	private String name = null;//�ڴ���ҳʱ��ֵ
	private int type = 0;//0��ʾ������1��ʾ��agent��2��ʾ���Զ�������, 3��agent���Զ�������
	private long total = 0;//����
	private long success = 0;
	private long error = 0;
	private int ave = 0;
	private int max = 0;
	private int min = Integer.MAX_VALUE;
	private int record = 0;//¼��ʱ�Ĳ���ֵ��ԭʼֵ
	private int[] percent = new int[10];//90% 10  80% 20   70%  30   60  40   50  50

	//1��ʾ�����룬2��ʾ��ʱ��3��ʾTCP����ʧ�ܣ�4��ʾ����ʧ�ܣ�5��ʾ�ϴ��ļ�ʧ��, 6�����������ر�tcp,7����IPЭ��ջ��TCPʧ��, 8TLS����ʧ�ܵ�TCP�ɹ�, 9����java
	private int[] failed = new int[9];
	//����������httpֻ�㷢�ͣ�rtspҲ�ǣ�����Ϊ���������
	private float aveRequests = 0;
	public int scripter = 0;//�ű�����ֵ
	private int maxRequests = 0;
	private int minRequests = Integer.MAX_VALUE;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public long getError() {
		return error;
	}
	public void setError(long error) {
		this.error = error;
	}
	public int getAve() {
		return ave;
	}
	public void setAve(int ave) {
		this.ave = ave;
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
	public int getRecord() {
		return record;
	}
	public void setRecord(int record) {
		this.record = record;
	}
	public int[] getPercent() {
		return percent;
	}
	public void setPercent(int[] percent) {
		this.percent = percent;
	}
	public int[] getFailed() {
		return failed;
	}
	public void setFailed(int[] failed) {
		this.failed = failed;
	}
	public float getAveRequests() {
		return aveRequests;
	}
	public void setAveRequests(float aveRequests) {
		this.aveRequests = aveRequests;
	}
	public int getScripter() {
		return scripter;
	}
	public void setScripter(int scripter) {
		this.scripter = scripter;
	}
	public int getMaxRequests() {
		return maxRequests;
	}
	public void setMaxRequests(int maxRequests) {
		this.maxRequests = maxRequests;
	}
	public int getMinRequests() {
		return minRequests;
	}
	public void setMinRequests(int minRequests) {
		this.minRequests = minRequests;
	}		
}
