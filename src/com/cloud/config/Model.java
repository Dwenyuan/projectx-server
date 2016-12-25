package com.cloud.config;

public class Model {
	private int isCaps = 1;//是否每秒并发caps模型，还是每秒限定用户数模型 
	private int modelType = 0;//选择什么类型的索引值
	private String totalUsers = "2";//总用户数
	private String caps = "1";//initial（递增或递减）或者caps（平稳），或者是min（浪涌、震荡）
	private String limit = "1";//者max或者min（递增或递减），max（浪涌、震荡）
	private String keepTime = "1";//保持时间（Alter Frequency）
	private String step = "1";//Increase Value递增步值或decrease Value递减步值，或者wave and peak 的极值
	private int chooseType = 0;//递增或递减类型索引，或者是wave and peak类型索引
	
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
