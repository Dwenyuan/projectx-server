package com.cloud.control;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.cloud.pojo.FileEntity;
import com.cloud.pojo.UserLog;
import com.cloud.utils.Utils;

import kylinPET.PET46;


/**
 * 测试用例的获取数据与结果、分析
 * 只对已经停止运行的测试结果
 * @author kylinpet
 *
 */

@Controller
public class AnalysisAction
{
    public static final Logger log = Logger.getLogger(AnalysisAction.class);
    public PET46 operationImpl = new PET46();                
    
    /**
	 * 获取运行的历史列表，按顺序排序
	 * 
	 * 如果只是获取某一个，则需要传id参数
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "list")
    public void list(HttpServletRequest request,HttpServletResponse response)
    {
		try{
			String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            
			File f = new File(Utils.webInfoPath+"workspace/"+user+"/ResultData");
	    	if(f.exists()){	    		
	    		File[] files = f.listFiles();	    		
	    		files = ScripterAction.sortFiles(files);
	    		
	    		ArrayList<FileEntity> list = new ArrayList<>(files.length);
	    		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
	    		for(int i = 0; i < files.length; i++){
	    			File temp = files[i];
	    			if(temp.length() > 1){//1为已经被删除
	    				FileEntity fileEntity = new FileEntity();
	    				list.add(fileEntity);
	    					    				
	    				String name = temp.getName();	    				
	    				fileEntity.setId(name);
	    				//fileEntity.setCreate(formatter.format(new Date(temp.lastModified())));
	    				fileEntity.setModify(formatter.format(new Date(temp.lastModified())));
	    				fileEntity.setName(name);	    				  			
	    			}
	    		}
	    		Utils.outPut200(request, response, JSON.toJSONString(list));
	    	}
		} catch(Exception e){
			log.error("", e);
			Utils.outPut500(response, e);
		}
    }
    
	/**
	 * 获取运行的历史测试结果的统计数据
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getHistory")
    public void getHistory(HttpServletRequest request,HttpServletResponse response)
    {
		try{
			String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            
            UserLog log = new UserLog();
			log.setUser(user);								
			log.setAction(7);						
			Utils.saveLogToFile(log);
			
            String testcase = request.getParameter("testcase"); 
            Utils.outPut200(request, response, operationImpl.doAnalysis(user, testcase));	    	
		} catch(Exception e){
			log.error("", e);
			Utils.outPut500(response, e);
		}
    }
	
    /**
     * 获取运行完成输出的汇总报告
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getReport")
    public void getReport(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));                
                return;
            }
            String testcase = request.getParameter("testcase");                          
            Utils.outPutHTML(request, response, operationImpl.getReportSummary(user, testcase));	
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }
    
    /**
     * 获取用户详情的数据
     * action值含义：
		0表示获取任务汇总(包括事务、用户、URL)，1表示成功用户时间图、2表示失败用户时间图、3表示事务超过5毫秒时间图
		4表示某种失败原因的每秒失败数、5表示URL失败每秒失败数的时间图、6表示URL时间图、16表示URL每秒个数（成功与失败）图
		7表示读取用户汇总信息，有多少个、8表示读取某一页的用户信息、9表示读取特定用户信息、19获取某个用户的运行日志
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getUserDetail")
    public void getUserDetail(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            String testcase = request.getParameter("testcase");//测试场景（用例）  
            String task = request.getParameter("task");  //任务，多个任务时表示或者哪个任务，任务不对则永远获取第一个
            //action含义见上面注释
            int action = 0;
            String actionStr = request.getParameter("action");  
            if(actionStr != null){
            	try{
            		action = Integer.valueOf(actionStr);
            	}catch(Exception e){}
            }                        
            Utils.outPut200(request, response, operationImpl.doUserInfoAnalysis(user, testcase, task, action, null));	
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }
    
    /**
     * 打开用户详情后，过滤选中某个用户，获取该用户运行的详情的数据，查询失败原因，或运行的每个HTTP请求的时间
     * 上传的参数都是在界面已经存在，只需原封不动带上来agent file length
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getOneUser")
    public void getOneUser(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            String testcase = request.getParameter("testcase");//测试场景（用例）  
            
            String agent = request.getParameter("agent");//该用户属于哪个agent（把界面数据带上来）
            long fileStart = 0;
            int dataLength = 0;
            String fileStartStr = request.getParameter("file");//该用户界面数据（隐藏）的文件索引开始
            if(fileStartStr != null){
            	try{
            		fileStart = Long.valueOf(fileStartStr);
            	}catch(Exception e){}
            }
            String lengthStr = request.getParameter("length");//该用户界面数据（隐藏）的文件索引开始后读取多少长度
            if(lengthStr != null){
            	try{
            		dataLength = Integer.valueOf(lengthStr);
            	}catch(Exception e){}
            }            
            Utils.outPut200(request, response, operationImpl.getOneUser(user, testcase, agent, fileStart, dataLength));	
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }
    
    /**
     * 查看历史记录（已经运行停止），获取日志总数与第一页的数据，获取总数后可以得到页数（每页100个日志）
     * 支持过滤条件
     * 返回json带total与logs（数组，第一页的日志）
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getLog")
    public void getLog(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            String testcase = request.getParameter("testcase");//测试场景（用例）  
            
            //--日志过滤条件--
            int startTime = 0;//开始时间
            int endTime = 0;//结束时间
            String origin = null;//归属，如归属controller或某个agent（则写agent名称）
            int logLevel = 0;//日志级别
            String userIndex = null;//用户索引
            String description = null;//日志内容模糊查询
            
            String startStr = request.getParameter("start");
            if(startStr != null){
            	try{
            		startTime = Integer.valueOf(startStr);
            	}catch(Exception e){}
            }
            String endStr = request.getParameter("end");
            if(endStr != null){
            	try{
            		endTime = Integer.valueOf(endStr);
            	}catch(Exception e){}
            }
            String logStr = request.getParameter("log");
            if(logStr != null){
            	try{
            		logLevel = Integer.valueOf(logStr);
            	}catch(Exception e){}
            }
            String originStr = request.getParameter("origin");
            if(originStr != null){
            	origin = originStr;
            }   
            String userStr = request.getParameter("user");
            if(userStr != null){
            	userIndex = userStr;
            }  
            String desStr = request.getParameter("des");
            if(desStr != null){
            	description = desStr;
            }  
            
            Utils.outPut200(request, response, operationImpl.getLogsByFirstPage(user, testcase, 
            		startTime, endTime, origin, logLevel, userIndex, description));
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }
    
    /**
     * 查看某一页的日志数据，页数通过前面获取
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getPageLog")
    public void getPageLog(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            String testcase = request.getParameter("testcase");//测试场景（用例） 
            int pageNumber = 2;
            String pageStr = request.getParameter("page");
            if(pageStr != null){
            	try{
            		pageNumber = Integer.valueOf(pageStr);
            	}catch(Exception e){}
            }
            Utils.outPut200(request, response, operationImpl.getLogsByPage(user, testcase, pageNumber));
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }

    /**
     * 用户详情里，查看某个用户的失败日志
     * 需要先点击获取用户详情数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getUserFailLog")
    public void getUserFailLog(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	                        
            Utils.outPutHTML(request, response, getUserLog(request, 0));
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }
    
    /**
     * 用户详情里，查看某个用户的运行日志（开启了debug打印虚拟用户与服务器交互的http数据）
     * 需要先点击获取用户详情数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getUserLog")
    public void getUserLog(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	                        
            Utils.outPut200(request, response, getUserLog(request, 1));
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }
    
    private String getUserLog(HttpServletRequest request, int failLog) throws Exception{
    	String user = Utils.getUser(request);
        if (user == null || user.length() == 0)
        {            
            return Utils.resources.getString("userNotExist");
        }
        String testcase = request.getParameter("testcase");//测试场景（用例）  
                                
        //--日志过滤条件--
        boolean notPrintJSCSS = true;//不打印js css
        int startTime = 0;//开始时间
        int totalTime = 0;//用户运行时间            
        String agent = null;//归属，如某个agent（则写agent名称）         
        long userIndex = 0;//用户索引
        
        String startStr = request.getParameter("start");
        if(startStr != null){
        	try{
        		startTime = Integer.valueOf(startStr);
        	}catch(Exception e){}
        }
        String timeStr = request.getParameter("time");
        if(timeStr != null){
        	try{
        		totalTime = Integer.valueOf(timeStr);
        	}catch(Exception e){}
        }
        agent = request.getParameter("agent");

        String userStr = request.getParameter("user");
        if(userStr != null){
        	try{
        		userIndex = Long.valueOf(userStr);
        	}catch(Exception e){}
        } 
        String printStr = request.getParameter("print");
        if(printStr != null){
        	try{
        		notPrintJSCSS = Boolean.valueOf(printStr);
        	}catch(Exception e){}
        } 
        
        if(failLog==0){
        	return operationImpl.getUserFailedLog(userStr, testcase, userIndex, agent, startTime, totalTime);
        }
        else{
        	return operationImpl.getUserLog(userStr, testcase, userIndex, agent, startTime, totalTime, notPrintJSCSS);
        }
    }            
}
