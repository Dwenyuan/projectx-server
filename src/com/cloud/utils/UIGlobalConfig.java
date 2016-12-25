package com.cloud.utils;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保存一些全局的东西，又可以通过接口修改
 * @author kylinpet
 *
 */
public class UIGlobalConfig {
	//一个界面最多显示多少个点，echarts支持可视区域，然后拖拉显示完所有数据
	//但又不能太多，防止内存溢出，或前台
	public static int linePoints = 10000;
	//时间变化这些散点限制个数
	public static int changePoints = 100000;
	
	public static int limitRequests = 5000;//一天限制一个IP的操作个数
	public static int limitFileSize = 30000000;
	public static int limitInputLength = 500;
	public static int limitFilesNumber = 50;//默认每个用户上传的脚本个数
	public static int limitControllerSize = 10000000;//controller的配置文件大小限制
	public static int limitDownloadBW = 2*1024*1024/8;//限制总下载的带宽
	public static int limitRunNumber = 1;//限制一个用户最多同时运行多少个测试用例
	
	//限制一天触发短信的次数
	public static ConcurrentHashMap<String, Integer> message_Number_map = new ConcurrentHashMap<>();
	
	//限制一天获取验证码的次数
	public static ConcurrentHashMap<String, Integer> code_Number_map = new ConcurrentHashMap<>();
	
	//限制一天操作的次数
	public static ConcurrentHashMap<String, Integer> request_Number_map = new ConcurrentHashMap<>();
	
	public static HashSet<String> limitIP = new HashSet<String>();
	
	public static int language = 1;//语言
}
