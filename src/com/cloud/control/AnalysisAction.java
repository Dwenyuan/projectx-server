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
 * ���������Ļ�ȡ��������������
 * ֻ���Ѿ�ֹͣ���еĲ��Խ��
 * @author kylinpet
 *
 */

@Controller
public class AnalysisAction
{
    public static final Logger log = Logger.getLogger(AnalysisAction.class);
    public PET46 operationImpl = new PET46();                
    
    /**
	 * ��ȡ���е���ʷ�б���˳������
	 * 
	 * ���ֻ�ǻ�ȡĳһ��������Ҫ��id����
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
	    			if(temp.length() > 1){//1Ϊ�Ѿ���ɾ��
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
	 * ��ȡ���е���ʷ���Խ����ͳ������
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
     * ��ȡ�����������Ļ��ܱ���
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
     * ��ȡ�û����������
     * actionֵ���壺
		0��ʾ��ȡ�������(���������û���URL)��1��ʾ�ɹ��û�ʱ��ͼ��2��ʾʧ���û�ʱ��ͼ��3��ʾ���񳬹�5����ʱ��ͼ
		4��ʾĳ��ʧ��ԭ���ÿ��ʧ������5��ʾURLʧ��ÿ��ʧ������ʱ��ͼ��6��ʾURLʱ��ͼ��16��ʾURLÿ��������ɹ���ʧ�ܣ�ͼ
		7��ʾ��ȡ�û�������Ϣ���ж��ٸ���8��ʾ��ȡĳһҳ���û���Ϣ��9��ʾ��ȡ�ض��û���Ϣ��19��ȡĳ���û���������־
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
            String testcase = request.getParameter("testcase");//���Գ�����������  
            String task = request.getParameter("task");  //���񣬶������ʱ��ʾ�����ĸ��������񲻶�����Զ��ȡ��һ��
            //action���������ע��
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
     * ���û�����󣬹���ѡ��ĳ���û�����ȡ���û����е���������ݣ���ѯʧ��ԭ�򣬻����е�ÿ��HTTP�����ʱ��
     * �ϴ��Ĳ��������ڽ����Ѿ����ڣ�ֻ��ԭ�ⲻ��������agent file length
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
            String testcase = request.getParameter("testcase");//���Գ�����������  
            
            String agent = request.getParameter("agent");//���û������ĸ�agent���ѽ������ݴ�������
            long fileStart = 0;
            int dataLength = 0;
            String fileStartStr = request.getParameter("file");//���û��������ݣ����أ����ļ�������ʼ
            if(fileStartStr != null){
            	try{
            		fileStart = Long.valueOf(fileStartStr);
            	}catch(Exception e){}
            }
            String lengthStr = request.getParameter("length");//���û��������ݣ����أ����ļ�������ʼ���ȡ���ٳ���
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
     * �鿴��ʷ��¼���Ѿ�����ֹͣ������ȡ��־�������һҳ�����ݣ���ȡ��������Եõ�ҳ����ÿҳ100����־��
     * ֧�ֹ�������
     * ����json��total��logs�����飬��һҳ����־��
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
            String testcase = request.getParameter("testcase");//���Գ�����������  
            
            //--��־��������--
            int startTime = 0;//��ʼʱ��
            int endTime = 0;//����ʱ��
            String origin = null;//�����������controller��ĳ��agent����дagent���ƣ�
            int logLevel = 0;//��־����
            String userIndex = null;//�û�����
            String description = null;//��־����ģ����ѯ
            
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
     * �鿴ĳһҳ����־���ݣ�ҳ��ͨ��ǰ���ȡ
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
            String testcase = request.getParameter("testcase");//���Գ����������� 
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
     * �û�������鿴ĳ���û���ʧ����־
     * ��Ҫ�ȵ����ȡ�û���������
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
     * �û�������鿴ĳ���û���������־��������debug��ӡ�����û��������������http���ݣ�
     * ��Ҫ�ȵ����ȡ�û���������
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
        String testcase = request.getParameter("testcase");//���Գ�����������  
                                
        //--��־��������--
        boolean notPrintJSCSS = true;//����ӡjs css
        int startTime = 0;//��ʼʱ��
        int totalTime = 0;//�û�����ʱ��            
        String agent = null;//��������ĳ��agent����дagent���ƣ�         
        long userIndex = 0;//�û�����
        
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
