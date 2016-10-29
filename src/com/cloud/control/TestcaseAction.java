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
     * 获取统计数据与日志
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
            boolean ave = false; //获取每分钟平均值，默认是每秒值
            boolean readAll = false; //默认false为实时获取点，true表示或者最后300个点（用于关掉浏览器又回来这种第一次获取）
            boolean readLog = true;//是否读取日志
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
     * 获取运行完成输出的汇总报告
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
            String user = request.getParameter("user");
            if (user == null || user.length() == 0)
            {
                log.error("can't get user");
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
            Utils.outPutHTML(response, PET39.doUserInfoAnalysis(user, testcase, task, action, null));	
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
            String testcase = request.getParameter("testcase");//测试场景（用例）  
            
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
        if(loadResult != null && loadResult.getFailedAgents().size() > 0){//执行失败，提示用户哪些agent连接失败
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
System.out.println("------------------统计------------------");	        	
	        	System.out.println(operation.getJson(user.getName(), testcaseTasks.getName(), false, false, true));	        	
	        	if(operation.isStop(user.getName(), testcaseTasks.getName())){//所有任务运行完成
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
