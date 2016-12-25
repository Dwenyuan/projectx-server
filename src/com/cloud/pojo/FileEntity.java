package com.cloud.pojo;

/**
 * 文件类型的参数(脚本，参数，测试模型配置)
 * @author Administrator
 *
 */
public class FileEntity {
	private String id = null;
	private String name = null;
	private String create = null;
	private String modify = null;
	private String des = null;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreate() {
		return create;
	}
	public void setCreate(String create) {
		this.create = create;
	}
	public String getModify() {
		return modify;
	}
	public void setModify(String modify) {
		this.modify = modify;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	
	
}
