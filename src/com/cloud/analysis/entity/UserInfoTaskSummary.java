package com.cloud.analysis.entity;

public class UserInfoTaskSummary {
	private String name = null;//在打开首页时赋值
	private int type = 0;//0表示是任务，1表示是agent，2表示是自定义事务, 3是agent的自定义事务
	private long total = 0;//总数
	private long success = 0;
	private long error = 0;
	private int ave = 0;
	private int max = 0;
	private int min = Integer.MAX_VALUE;
	private int record = 0;//录制时的参照值，原始值
	private int[] percent = new int[10];//90% 10  80% 20   70%  30   60  40   50  50

	//1表示错误码，2表示超时，3表示TCP建立失败，4表示断言失败，5表示上传文件失败, 6服务器主动关闭tcp,7建立IP协议栈的TCP失败, 8TLS建立失败但TCP成功, 9调用java
	private int[] failed = new int[9];
	//请求总数、http只算发送，rtsp也是，其他为发送与接收
	private float aveRequests = 0;
	public int scripter = 0;//脚本参照值
	private int maxRequests = 0;
	private int minRequests = Integer.MAX_VALUE;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getSuccess() {
		return success;
	}
	public void setSuccess(long success) {
		this.success = success;
	}
	public long getError() {
		return error;
	}
	public void setError(long error) {
		this.error = error;
	}
	public int getAve() {
		return ave;
	}
	public void setAve(int ave) {
		this.ave = ave;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getRecord() {
		return record;
	}
	public void setRecord(int record) {
		this.record = record;
	}
	public int[] getPercent() {
		return percent;
	}
	public void setPercent(int[] percent) {
		this.percent = percent;
	}
	public int[] getFailed() {
		return failed;
	}
	public void setFailed(int[] failed) {
		this.failed = failed;
	}
	public float getAveRequests() {
		return aveRequests;
	}
	public void setAveRequests(float aveRequests) {
		this.aveRequests = aveRequests;
	}
	public int getScripter() {
		return scripter;
	}
	public void setScripter(int scripter) {
		this.scripter = scripter;
	}
	public int getMaxRequests() {
		return maxRequests;
	}
	public void setMaxRequests(int maxRequests) {
		this.maxRequests = maxRequests;
	}
	public int getMinRequests() {
		return minRequests;
	}
	public void setMinRequests(int minRequests) {
		this.minRequests = minRequests;
	}		
}
