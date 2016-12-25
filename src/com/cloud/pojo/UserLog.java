package com.cloud.pojo;

import java.io.ByteArrayOutputStream;

import com.cloud.utils.Utils;

public class UserLog {
	private String user;
	private long time;
	//做了什么操作，0表示是异常操作的日志，下面的errorType会有值
	//1表示登陆，2表示点击运行，3运行通过，开始运行，4停止运行
	//5上传脚本，6上传参数, 7是查看历史结果, 8点击停止，9点击快速停止
	private int action;
	//1表示是操作次数超过限制的攻击，2上传文件过大，3上传文件个数过多
	private long errorType;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public long getErrorType() {
		return errorType;
	}
	public void setErrorType(long errorType) {
		this.errorType = errorType;
	}		
	
	public byte[] toBytes() throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream(user.length()+24);
		byte[] temp = new byte[8];
		Utils.intToBytes(time, 8, temp, 0);
		out.write(temp);
		out.write(action);		
		Utils.intToBytes(errorType, 8, temp, 0);
		out.write(temp);
		out.write(user.getBytes("utf-8"));
		byte[] data = out.toByteArray();
		out.close();
		return data;
	}
}
