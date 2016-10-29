package com.cloud.analysis.entity;

public class OneUserEntity {
	private String name;//名称或url
	private String method = null;
	private String des = null;
	private String response = null;
	private String recordC = null;//录制响应值
	private int nTCP = 0;//是否创建tcp
	private int dns = 0;
	private int tcp = 0;
	private int first = 0;
	private int left = 0;
	private int diff = 0;
	private int recordT = 0;//录制时间
	private int bytes = 0;
	private int recordB = 0;//录制字节
	private String host = null;
	private String belong = null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getRecordC() {
		return recordC;
	}
	public void setRecordC(String recordC) {
		this.recordC = recordC;
	}
	
	public int getDns() {
		return dns;
	}
	public void setDns(int dns) {
		this.dns = dns;
	}
	public int getTcp() {
		return tcp;
	}
	public void setTcp(int tcp) {
		this.tcp = tcp;
	}
	public int getFirst() {
		return first;
	}
	public void setFirst(int first) {
		this.first = first;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}	
	public int getnTCP() {
		return nTCP;
	}
	public void setnTCP(int nTCP) {
		this.nTCP = nTCP;
	}
	public int getDiff() {
		return diff;
	}
	public void setDiff(int diff) {
		this.diff = diff;
	}
	public int getRecordT() {
		return recordT;
	}
	public void setRecordT(int recordT) {
		this.recordT = recordT;
	}
	public int getBytes() {
		return bytes;
	}
	public void setBytes(int bytes) {
		this.bytes = bytes;
	}
	public int getRecordB() {
		return recordB;
	}
	public void setRecordB(int recordB) {
		this.recordB = recordB;
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
}
