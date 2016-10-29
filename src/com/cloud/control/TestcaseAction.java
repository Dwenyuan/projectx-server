package com.cloud.control;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloud.config.IteratorTimes;
import com.cloud.config.LoadAgentConfig;
import com.cloud.config.LoadResult;
import com.cloud.config.Model;
import com.cloud.config.Rendezvous;
import com.cloud.config.StartConfig;
import com.cloud.config.StopConfig;
import com.cloud.config.TestcaseTask;
import com.cloud.config.TestcaseTasks;
import com.cloud.config.UserInfo;
import com.cloud.utils.Utils;
import kylinPET.PET39;

@Controller
public class TestcaseAction
{
    public static final Logger log = Logger.getLogger(TestcaseAction.class);
    public PET39 PET39 = new PET39();

    @RequestMapping(value = "/test")
    public void test(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = request.getParameter("user");
            if (user == null || user.length() == 0)
            {
                log.error("can't get user");
                return;
            }
                        
            UserInfo userInfo = new UserInfo();
            userInfo.setName(user);
            test(userInfo, false);
        }
        catch (Exception e)
        {
            log.error("", e);            
            return;
        }
    }
    
    /**
     * ��ȡͳ����������־
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getData")
    public void getData(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = request.getParameter("user");
            if (user == null || user.length() == 0)
            {
                log.error("can't get user");
                return;
            }
            String testcase = request.getParameter("testcase");  
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
            Utils.outPut200(response, getData(user, testcase, ave, readAll, readLog));	
        }
        catch (Exception e)
        {
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
            String user = request.getParameter("user");
            if (user == null || user.length() == 0)
            {
                log.error("can't get user");
                return;
            }
            String testcase = request.getParameter("testcase");                          
            Utils.outPutHTML(response, PET39.getReportSummary(user, testcase));	
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
            String user = request.getParameter("user");
            if (user == null || user.length() == 0)
            {
                log.error("can't get user");
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
            Utils.outPutHTML(response, PET39.doUserInfoAnalysis(user, testcase, task, action, null));	
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
    @RequestMapping(value = "/getOneUser")
    public void getOneUser(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	
            String user = request.getParameter("user");
            if (user == null || user.length() == 0)
            {
                log.error("can't get user");
                return;
            }
            String testcase = request.getParameter("testcase");//���Գ�����������  
            
            String agent = request.getParameter("agent");
            long fileStart = 0;
            int dataLength = 0;
            String fileStartStr = request.getParameter("file");
            if(fileStartStr != null){
            	try{
            		fileStart = Long.valueOf(fileStartStr);
            	}catch(Exception e){}
            }
            String lengthStr = request.getParameter("length");
            if(lengthStr != null){
            	try{
            		dataLength = Integer.valueOf(lengthStr);
            	}catch(Exception e){}
            }            
            Utils.outPutHTML(response, PET39.getOneUser(user, testcase, agent, fileStart, dataLength));	
        }
        catch (Exception e)
        {
            log.error("", e);            
            Utils.outPut500(response, e);
        }
    }
    
    private String getData(String user, String testcase, boolean ave, boolean readAll, boolean readLog){    	
    	return PET39.getJson(user, testcase, false, false, true);
    }
    
    
    
    private void addTask(List<TestcaseTask> taskList, String ip, String name, String area, String task){
    	TestcaseTask task1 = new TestcaseTask();
        taskList.add(task1);
        task1.setName(task);
        if(area.equalsIgnoreCase("beijing")){
        	task1.setScripter("Example31.spet");
        }
        else{
        	task1.setScripter("Example314.spet");
        }
        
        Model model = new Model();
        model.setTotalUsers("20");
        model.setCaps("2");
        task1.setModel(model);
        
        StartConfig startConfig = new StartConfig();         
        task1.setStartConfig(startConfig);
        
        StopConfig stopConfig = new StopConfig();
        task1.setStopConfig(stopConfig);
        
        IteratorTimes iteratorTimes = new IteratorTimes();
        task1.setIteratorTimes(iteratorTimes);
        
        Rendezvous rendezvous = new Rendezvous();
        task1.setRendezvous(rendezvous);
        List<LoadAgentConfig> loadAgent = new ArrayList<LoadAgentConfig>();
        task1.setLoadAgent(loadAgent);
        LoadAgentConfig agent = new LoadAgentConfig();
        agent.setIp(ip);
        agent.setName(name);
        agent.setAddress(area);            
        loadAgent.add(agent);
        
        if(area.equalsIgnoreCase("beijing")){
        /*LoadAgentConfig agent2 = new LoadAgentConfig();
        agent2.setIp("192.168.1.100");
        agent2.setName("agenthz");
        agent2.setAddress("beijing");     
        agent2.setPort(1998);
        loadAgent.add(agent2);*/
        
       /* LoadAgentConfig agent3 = new LoadAgentConfig();
        agent3.setIp("192.168.1.100");
        agent3.setName("agentgz");
        agent3.setAddress("gz");     
        agent3.setPort(2000);
        loadAgent.add(agent3);*/
        }
    }
    
    private void test(UserInfo user, boolean print){
    	TestcaseTasks testcaseTasks = new TestcaseTasks();
    	testcaseTasks.setName("abc");
        testcaseTasks.setWebDetail(1);
        
        List<TestcaseTask> taskList = new ArrayList<TestcaseTask>(); 
        testcaseTasks.setTestcaseTasks(taskList);                                        
        
        addTask(taskList, "127.0.0.1", "local", "beijing", "test");
        //addTask(taskList, "192.168.1.100", "agent2", "shanghai", "tttt2");
        
        PET39 operation = new PET39();
        LoadResult loadResult = operation.loadConfigAndRun(user, testcaseTasks);
        if(loadResult != null && loadResult.getFailedAgents().size() > 0){//ִ��ʧ�ܣ���ʾ�û���Щagent����ʧ��
        	for(LoadAgentConfig config:loadResult.getFailedAgents()){
        		log.error(("Agent '"+config.getName()+"' and ip = "+config.getIp()+", area = "+config.getAddress()+" "+config.getError()));      
        	}
        }
        if(!print){
        	return;
        }
        try{        	
        	int second = 0;
	        while(true){
	        	Thread.sleep(1000);
	        	second++;
	        	/*if(second == 10){
	        		operation.stop(user.getName(), testcaseTasks.getName());
	        	}*/
System.out.println("------------------ͳ��------------------");	        	
	        	System.out.println(operation.getJson(user.getName(), testcaseTasks.getName(), false, false, true));	        	
	        	if(operation.isStop(user.getName(), testcaseTasks.getName())){//���������������
	        		break;
	        	}
	        }
	        System.out.println("--------------report------------");
			System.out.println(operation.getReportSummary(user.getName(), testcaseTasks.getName()));
			System.out.println("--------------summary------------");
	        System.out.println(operation.doUserInfoAnalysis(user.getName(), testcaseTasks.getName(), "test", 0, null));
	        System.out.println("--------------summary------------");
	        System.out.println(operation.doUserInfoAnalysis(user.getName(), testcaseTasks.getName(), "tttt2", 0, null));
	        System.out.println("--------------UserInfo------------");
	        String users = operation.doUserInfoAnalysis(user.getName(), testcaseTasks.getName(), "test", 8, null);
	        System.out.println(users);
	        Pattern p = Pattern.compile("\"file\":(\\d+),\"len\":(\\d+)");
	        Matcher m = p.matcher(users);
	        System.out.println("--------------UserInfo--One----------");
	        int i = 0;
	        while(m.find()){
	        	if(i == 0 || i==5){
	        	System.out.println(operation.getOneUser(user.getName(), testcaseTasks.getName(), "local", Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2))));
	        	}		        	  
	        	i++;
	        }	        	             
        }catch(Exception e){
        	e.printStackTrace();
        }
        log.info("success!");
    }           
    
    public static void main(String[] args) throws Exception{
    	  UserInfo userInfo = new UserInfo();
          userInfo.setName("lin");
          TestcaseAction test = new TestcaseAction();
          test.test(userInfo, true);
          //test.testAnalysis();
	}
}
