package com.cloud.config;

public class Rendezvous {
	//0表示禁用，1表示启用但按照controller配置，2表示启用按scripter配置
	private int rendezvousEnable = 0;
	private int policy = 0;
	private int policyUsers_Percent = 0;
	
	public int getRendezvousEnable() {
		return rendezvousEnable;
	}
	public void setRendezvousEnable(int rendezvousEnable) {
		this.rendezvousEnable = rendezvousEnable;
	}
	public int getPolicy() {
		return policy;
	}
	public void setPolicy(int policy) {
		this.policy = policy;
	}
	public int getPolicyUsers_Percent() {
		return policyUsers_Percent;
	}
	public void setPolicyUsers_Percent(int policyUsers_Percent) {
		this.policyUsers_Percent = policyUsers_Percent;
	}
	
	
}
