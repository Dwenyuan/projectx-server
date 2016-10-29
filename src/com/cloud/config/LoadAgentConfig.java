package com.cloud.config;

public class LoadAgentConfig {	
	private String address = null;
	private String name = null;
	private String ip = null;
	private int port = 1998;
	//虚拟IP的起始于结束
	private String startIP = null;
	private String endIP = null;
	private String error = null;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getStartIP() {
		return startIP;
	}
	public void setStartIP(String startIP) {
		this.startIP = startIP;
	}
	public String getEndIP() {
		return endIP;
	}
	public void setEndIP(String endIP) {
		this.endIP = endIP;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	
}
