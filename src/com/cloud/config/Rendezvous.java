package com.cloud.config;

public class Rendezvous {
	//0��ʾ���ã�1��ʾ���õ�����controller���ã�2��ʾ���ð�scripter����
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
