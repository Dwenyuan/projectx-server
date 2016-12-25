package com.cloud.utils;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ����һЩȫ�ֵĶ������ֿ���ͨ���ӿ��޸�
 * @author kylinpet
 *
 */
public class UIGlobalConfig {
	//һ�����������ʾ���ٸ��㣬echarts֧�ֿ�������Ȼ��������ʾ����������
	//���ֲ���̫�࣬��ֹ�ڴ��������ǰ̨
	public static int linePoints = 10000;
	//ʱ��仯��Щɢ�����Ƹ���
	public static int changePoints = 100000;
	
	public static int limitRequests = 5000;//һ������һ��IP�Ĳ�������
	public static int limitFileSize = 30000000;
	public static int limitInputLength = 500;
	public static int limitFilesNumber = 50;//Ĭ��ÿ���û��ϴ��Ľű�����
	public static int limitControllerSize = 10000000;//controller�������ļ���С����
	public static int limitDownloadBW = 2*1024*1024/8;//���������صĴ���
	public static int limitRunNumber = 1;//����һ���û����ͬʱ���ж��ٸ���������
	
	//����һ�촥�����ŵĴ���
	public static ConcurrentHashMap<String, Integer> message_Number_map = new ConcurrentHashMap<>();
	
	//����һ���ȡ��֤��Ĵ���
	public static ConcurrentHashMap<String, Integer> code_Number_map = new ConcurrentHashMap<>();
	
	//����һ������Ĵ���
	public static ConcurrentHashMap<String, Integer> request_Number_map = new ConcurrentHashMap<>();
	
	public static HashSet<String> limitIP = new HashSet<String>();
	
	public static int language = 1;//����
}
