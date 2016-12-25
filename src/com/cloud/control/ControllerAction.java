package com.cloud.control;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.cloud.config.LoadAgentConfig;
import com.cloud.config.LoadResult;
import com.cloud.config.TestcaseTasks;
import com.cloud.config.UserInfo;
import com.cloud.pojo.FileEntity;
import com.cloud.pojo.UserLog;
import com.cloud.utils.UIGlobalConfig;
import com.cloud.utils.Utils;

import kylinPET.PET46;

/**
 * controller�Ĳ������ã���ӡ�ɾ�����޸ģ���ȡ��������
 * ���м�ʵʱ��ȡ��������־
 * @author kylinpet
 *
 */
@Controller
@RequestMapping("controller")
public class ControllerAction {
	public static final Logger log = Logger.getLogger(ControllerAction.class);
	public PET46 operationImpl = new PET46();            
	
	/**
	 * ɾ���������ã�֧��ɾ�����
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "delete")
    public void delete(HttpServletRequest request,HttpServletResponse response)
    {
		try{
			String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
                        
            String idList = request.getParameter("id");//���ID�Զ��ŷָ�
            StringTokenizer st = new StringTokenizer(idList, ",;");
            while(st.hasMoreTokens()){
            	String id = st.nextToken();
	            File f = new File(Utils.webInfoPath+"workspace/"+user+"/controller/"+id+".cpet");
	            if(f.exists()){            		            		            	            
	            	File newF = new File(f.getParent()+"/"+id+".bak");
	            	f.renameTo(newF);	            	
	            }
            }
		} catch(Exception e){
			log.error("", e);
			Utils.outPut500(response, e);
		}
    }
	
	/**
	 * �������ã������½����޸�
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "save")
    public void save(HttpServletRequest request,HttpServletResponse response)
    {
		try{			
			String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            
            int limitFile = ScripterAction.getLimitFile(user); 
    		//�ж��û��Ƿ���������ļ�		
    		if(getFileNumber(user) >= limitFile){   
    			UserLog log = new UserLog();
    			log.setUser(user);								
    			log.setErrorType(3);
    			Utils.saveLogToFile(log);
    			throw new Exception(Utils.getInfoString("limitFile", String.valueOf(limitFile)));
    		}
            
            String id = request.getParameter("id");
            if(id == null){
            	id = String.valueOf(System.currentTimeMillis());
            }                                                
            
            InputStream in = request.getInputStream();
            byte[] buffer = new byte[2048];		
            FileOutputStream out = new FileOutputStream(Utils.webInfoPath+"workspace/"+user+"/controller/"+id+".cpet");
    		try{		
    			int offset = 0;
    			int len = 0;
    			while((len = in.read(buffer)) != -1){
    				out.write(buffer, 0, len);
    				offset += len;
    				if(offset >= UIGlobalConfig.limitControllerSize){
    					UserLog log = new UserLog();
    					log.setUser(user);								
    					log.setErrorType(2);
    					Utils.saveLogToFile(log);
    					Utils.outPut500(response, "file size limit "+(UIGlobalConfig.limitControllerSize/1000000)+"MB");
    					return;
    				}
    			}
    		}catch(Exception e){
    			log.error("", e);
    		}
    		finally{
    			try{
    				out.close();
    			}catch(Exception e){}
    		}	
            
    		Utils.outPut200(request, response, "{\"id\":\""+id+"\"}");
		} catch(Exception e){
			log.error("", e);
			Utils.outPut500(response, e);
		}
    }
	
	/**
	 * ��ȡĳ�����ã����ز�������
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "read")
    public void read(HttpServletRequest request,HttpServletResponse response)
    {
		try{
			String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
                        
            String id = request.getParameter("id");
            File f = new File(Utils.webInfoPath+"workspace/"+user+"/controller/"+id+".cpet");
            if(!f.exists()){            		            		            	            
            	Utils.outPut500(response, "can't find file "+id);
            	return;
            }
            response.setContentType("application/json; charset=utf-8"); 
			response.setHeader("Cache-Control", "no-cache");
			Utils.dataToBrowser(f, response, false);
		} catch(Exception e){
			log.error("", e);
			Utils.outPut500(response, e);
		}
    }
	
	/**
	 * ��ȡcontroller�Ĳ��������б���ʱ��˳������
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
            
			File f = new File(Utils.webInfoPath+"workspace/"+user+"/controller");
	    	if(f.exists()){
	    		FileFilter filter = createFileFilter();
	    		File[] files = f.listFiles(filter);
	    		
	    		files = ScripterAction.sortFiles(files);
	    		
	    		ArrayList<FileEntity> list = new ArrayList<>(files.length);
	    		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
	    		for(int i = 0; i < files.length; i++){
	    			File temp = files[i];
	    			if(temp.length() > 1){//1Ϊ�Ѿ���ɾ��
	    				FileEntity fileEntity = new FileEntity();
	    				list.add(fileEntity);
	    				
	    				String id = temp.getName();
	    				id = id.substring(0, id.length() - 4);//ȥ��txt
	    				fileEntity.setId(id);
	    				fileEntity.setCreate(formatter.format(new Date(Long.valueOf(id))));
	    				fileEntity.setModify(formatter.format(new Date(temp.lastModified())));
	    				
	    				String prefixData = new String(Utils.getDataFromFile(temp.getAbsolutePath(), 80), "utf-8");//��ȡ80�ֽھ��Ѿ�����������
	    				int loc = prefixData.indexOf("\"a\"");
	    				if(loc > 0){
	    					int endLoc = prefixData.indexOf("\",", loc + 5);
	    					if(endLoc > 0){
	    						fileEntity.setName(prefixData.substring(loc+5, endLoc));
	    					}
	    				}	    				
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
     * ��ʼ����
     * @param request
     * @param response
     */
    @RequestMapping(value = "/start")
    public void startTestcase(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	        	
            String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            
            String testcaseID = request.getParameter("id");//���е����ã�����ǰ�ȵ���controller/save�������ã���̨���سɹ���id
            File f = new File(Utils.webInfoPath+"workspace/"+user+"/controller/"+testcaseID+".cpet");
            if(!f.exists()){            		            		            	            
            	Utils.outPut500(response, "can't find file "+testcaseID);
            	return;
            }
            
            UserLog log = new UserLog();
			log.setUser(user);								
			log.setAction(2);
			Utils.saveLogToFile(log);
            
            TestcaseTasks testcaseTasks = JSON.parseObject(new String(Utils.getDataFromFile(f.getAbsolutePath()), "utf-8"), 
            		TestcaseTasks.class);            
            
            UserInfo userInfo = new UserInfo();
            userInfo.setName(user);
            userInfo.setTaskNum(getLimitRunNumber(user));
            
            LoadResult loadResult = operationImpl.loadConfigAndRun(userInfo, testcaseTasks);
            if(loadResult != null && loadResult.getFailedAgents().size() > 0){//ִ��ʧ�ܣ���ʾ�û���Щagent����ʧ��
            	StringBuilder sb = new StringBuilder(loadResult.getFailedAgents().size()*80);
            	for(LoadAgentConfig config:loadResult.getFailedAgents()){ 
            		if(sb.length() > 0){
            			sb.append("\r\n");
            		}
            		sb.append("Agent '").append(config.getName()).append("' and ip = ").append(config.getIp());
            		sb.append(", area = ").append(config.getAddress()).append(" ").append(config.getError());            		
            	}
            	Utils.outPut500(response, sb.toString());
            	return;
            }
            else if(loadResult != null && loadResult.getError() != null){
            	Utils.outPut500(response, loadResult.getError());
            	return;
            }
            
            log = new UserLog();
			log.setUser(user);								
			log.setAction(3);
			log.setErrorType(testcaseTasks.getTestcaseTasks().get(0).getLoadAgent().size());//���ж��ٸ�agent
			Utils.saveLogToFile(log);
            
            Utils.outPut200(request, response, testcaseTasks.getA());	 //�ɹ����ز�������  
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }
    
    /**
     * ��ȡͳ����������־
     * ÿ�����һ��
     * @param request
     * @param response
     * 
     * @param testcase ���Գ�������  (����������еĲ��ԣ���ȡ���300���㣬�����������ģ���Ҫ ����+���ڣ����ȡ���еĵ�)
     * @param ave ��ΪTRUE ���� ÿ����ƽ��ֵ ����ÿ����1���㣩
     * @param readAll ΪTRUE����ȡ����300���㣬ΪFALSE ��ȡ��ǰ��
     * ʱ
     */
    @RequestMapping(value = "/getData")
    public void getData(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            String testcase = request.getParameter("testcase"); //�����еĲ����������� 
            boolean ave = false; //��ȡÿ����ƽ��ֵ��Ĭ����ÿ��ֵ
            boolean readAll = false; //Ĭ��falseΪʵʱ��ȡ�㣬true��ʾ�������300���㣨���ڹص�������ֻ������ֵ�һ�λ�ȡ��
            boolean readLog = true;//�Ƿ��ȡ��־
            String aveString = request.getParameter("ave");
            if(aveString != null){
            	try{
            		ave = Boolean.valueOf(aveString);
            	}catch(Exception e){}
            }
            String readAllString = request.getParameter("readAll");
            if(readAllString != null){
            	try{
            		readAll = Boolean.valueOf(readAllString);
            	}catch(Exception e){}
            }
            String readLogString = request.getParameter("readLog");
            if(readLogString != null){
            	try{
            		readLog = Boolean.valueOf(readLogString);
            	}catch(Exception e){}
            }            
            Utils.outPut200(request, response, operationImpl.getJson(request.getSession().getId(), user, testcase, ave, readAll, readLog));	
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }
    
    /**
     * ��ȡͳ����������־
     * ÿ�����һ��
     * @param request
     * @param response
     */
    @RequestMapping(value = "/stop")
    public void stop(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            String testcase = request.getParameter("testcase"); //�����еĲ����������� 
            
            UserLog log = new UserLog();
			log.setUser(user);								
			log.setAction(8);			
			Utils.saveLogToFile(log);
            
            operationImpl.stop(user, testcase);
            Utils.outPut200(request, response, "ok");	
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }
    
    @RequestMapping(value = "/quickStop")
    public void quickStop(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            String testcase = request.getParameter("testcase"); //�����еĲ�����������
            
            UserLog log = new UserLog();
			log.setUser(user);								
			log.setAction(9);			
			Utils.saveLogToFile(log);
            
            operationImpl.quickStop(user, testcase);
            Utils.outPut200(request, response, "ok");	
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }        
	
    /**
     * ��ȡ��ǰ�������еĲ��Գ���
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getRunning")
    public void getRunning(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            
            Utils.outPut200(request, response, JSON.toJSONString(operationImpl.getRunningTestcase(user)));	
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }
    
	private int getFileNumber(String user){
    	try{
	    	File f = new File(Utils.webInfoPath+"workspace/"+user+"/controller");
	    	if(f.exists()){
	    		FileFilter filter = createFileFilter();	             
	    		return f.listFiles(filter).length;
	    	}
    	}catch(Exception e){}
    	return 0;
    }
    
    
    private FileFilter createFileFilter(){
    	FileFilter filter = new FileFilter() {    			
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getName().endsWith("cpet");	
            }
		};
		return filter;
    }
    
    /**
     * ��ȡ�û�ͬʱ���и���
     * @param user
     * @return
     */
    private int getLimitRunNumber(String user){
    	File f = new File(Utils.webInfoPath+"workspace/"+user+"/limit.ini");
    	if(!f.exists()){
    		return UIGlobalConfig.limitRunNumber;
    	}    	
    	String config = new String(Utils.getDataFromFile(f.getAbsolutePath()));
    	int loc = config.indexOf("run");
    	if(loc > 0){
    		try{
    			int endLoc = config.indexOf("\r\n", loc+4);
    			return Integer.valueOf(config.substring(loc+4, endLoc).trim());
    		}catch(Exception e){}
    	}
    	return UIGlobalConfig.limitRunNumber;
    }
}
