package com.cloud.utils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import kylinPET.PET46;

/**
 * 对一些缓存清空防止无限大
 * 如user_task_map
 * @author kylinpet
 *
 */
public class ClearCacheTask extends Thread{
	public static final Logger log = Logger.getLogger(ClearCacheTask.class);
	
	public void run(){
		this.setName("ClearCacheTask");
		boolean hasClear = false;
		while(true){
			try{
				Thread.sleep(60000);
				Date date = new Date();
				if (!hasClear && 2 == date.getHours())//晚上凌晨2点，执行以下清除操作
				{
					hasClear = true;
					
					UIGlobalConfig.message_Number_map.clear();
					UIGlobalConfig.code_Number_map.clear();
					UIGlobalConfig.request_Number_map.clear();
					
					PET46 operationImpl = new PET46();
					operationImpl.clearTestcaseCache();
				}
				else if(3 == date.getHours()){
					hasClear = false;
				}
			}catch(Exception e){
				log.error("", e);     
			}
		}
	}	
}
