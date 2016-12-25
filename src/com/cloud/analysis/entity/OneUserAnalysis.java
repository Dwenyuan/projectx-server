package com.cloud.analysis.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class OneUserAnalysis {
	private int record = 0;//�����ű���¼��ʱ��
	private int urls = 0;//�ܵ�������	
	//private int total = 0;//ִ��ʱ��
	private HashMap<String, Integer> host = null;
	//�����Ӧ��ÿ�����͵�ͳ��
	private HashMap<String, HashMap<String, URLDetailEntity>> type = null;		
	private ArrayList<OneUserEntity> list = null;

	public int getRecord() {
		return record;
	}

	public void setRecord(int record) {
		this.record = record;
	}

	public int getUrls() {
		return urls;
	}

	public void setUrls(int urls) {
		this.urls = urls;
	}
/*
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}*/

	public ArrayList<OneUserEntity> getList() {
		return list;
	}

	public void setList(ArrayList<OneUserEntity> list) {
		this.list = list;
	}

	public HashMap<String, Integer> getHost() {
		return host;
	}

	public void setHost(HashMap<String, Integer> host) {
		this.host = host;
	}

	public HashMap<String, HashMap<String, URLDetailEntity>> getType() {
		return type;
	}

	public void setType(HashMap<String, HashMap<String, URLDetailEntity>> type) {
		this.type = type;
	}		
}
