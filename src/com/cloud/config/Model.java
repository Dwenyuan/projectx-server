package com.cloud.config;

public class Model {
	private int isCaps = 1;//�Ƿ�ÿ�벢��capsģ�ͣ�����ÿ���޶��û���ģ�� 
	private int modelType = 0;//ѡ��ʲô���͵�����ֵ
	private String totalUsers = "2";//���û���
	private String caps = "1";//initial��������ݼ�������caps��ƽ�ȣ���������min����ӿ���𵴣�
	private String limit = "1";//��max����min��������ݼ�����max����ӿ���𵴣�
	private String keepTime = "1";//����ʱ�䣨Alter Frequency��
	private String step = "1";//Increase Value������ֵ��decrease Value�ݼ���ֵ������wave and peak �ļ�ֵ
	private int chooseType = 0;//������ݼ�����������������wave and peak��������
	
	public int getIsCaps() {
		return isCaps;
	}
	public void setIsCaps(int isCaps) {
		this.isCaps = isCaps;
	}
	public int getModelType() {
		return modelType;
	}
	public void setModelType(int modelType) {
		this.modelType = modelType;
	}
	public String getTotalUsers() {
		return totalUsers;
	}
	public void setTotalUsers(String totalUsers) {
		this.totalUsers = totalUsers;
	}
	public String getCaps() {
		return caps;
	}
	public void setCaps(String caps) {
		this.caps = caps;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getKeepTime() {
		return keepTime;
	}
	public void setKeepTime(String keepTime) {
		this.keepTime = keepTime;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public int getChooseType() {
		return chooseType;
	}
	public void setChooseType(int chooseType) {
		this.chooseType = chooseType;
	}
	
	
}
