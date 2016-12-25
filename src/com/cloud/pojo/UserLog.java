package com.cloud.pojo;

import java.io.ByteArrayOutputStream;

import com.cloud.utils.Utils;

public class UserLog {
	private String user;
	private long time;
	//����ʲô������0��ʾ���쳣��������־�������errorType����ֵ
	//1��ʾ��½��2��ʾ������У�3����ͨ������ʼ���У�4ֹͣ����
	//5�ϴ��ű���6�ϴ�����, 7�ǲ鿴��ʷ���, 8���ֹͣ��9�������ֹͣ
	private int action;
	//1��ʾ�ǲ��������������ƵĹ�����2�ϴ��ļ�����3�ϴ��ļ���������
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
