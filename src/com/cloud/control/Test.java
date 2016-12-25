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

import com.alibaba.fastjson.JSON;
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

import kylinPET.PET46;

@Controller
public class Test {
	public static final Logger log = Logger.getLogger(Test.class);
	
	@RequestMapping(value = "/test")
    public void test(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {        	        	
            String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
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
	
	private void addTask(List<TestcaseTask> taskList, String ip, String name, String area, String task){
    	TestcaseTask task1 = new TestcaseTask();
        taskList.add(task1);
        task1.setName(task);
        if(area.equalsIgnoreCase("beijing")){
        	task1.setScripterID("Example31.spet");
        }
        else{
        	task1.setScripterID("Example314.spet");
        }
        
        Model model = new Model();
        model.setTotalUsers("10");
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
        agent3.setAddress("hangzhou");     
        agent3.setPort(2000);
        loadAgent.add(agent3);*/
        }
    }
    
    private void test(UserInfo user, boolean print){
    	TestcaseTasks testcaseTasks = new TestcaseTasks();
    	testcaseTasks.setA("abc");
        testcaseTasks.setWebDetail(1);
        testcaseTasks.setLogLevel(1);//记录用户交互日志
        List<TestcaseTask> taskList = new ArrayList<TestcaseTask>(); 
        testcaseTasks.setTestcaseTasks(taskList);                                        
        
        addTask(taskList, "127.0.0.1", "local", "beijing", "test");
        //addTask(taskList, "192.168.1.100", "agent2", "shanghai", "tttt2");taskList.get(0).getLoadAgent().get(0).setPort(2000);
        
        PET46 operation = new PET46();
        System.out.println(JSON.toJSONString(testcaseTasks));
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
	        	System.out.println(operation.getJson(null, user.getName(), testcaseTasks.getA(), false, false, true));	        	
	        	if(operation.isStop(user.getName(), testcaseTasks.getA())){//所有任务运行完成
	        		break;
	        	}
	        }
	        printUserInfo(operation, user.getName(), testcaseTasks.getA());
        }catch(Exception e){
        	e.printStackTrace();
        }
        log.info("success!");
    }
    
    private static void printUserInfo(PET46 operation, String user, String testcaseName){
    	System.out.println("--------------report------------");
		System.out.println(operation.getReportSummary(user, testcaseName));
		System.out.println("--------------summary------------");
        System.out.println(operation.doUserInfoAnalysis(user, testcaseName, "test", 0, null));	        
        System.out.println("--------------UserInfo------------");
        String users = operation.doUserInfoAnalysis(user, testcaseName, "test", 8, null);
        System.out.println(users);
        Pattern p = Pattern.compile("\"file\":(\\d+),\"len\":(\\d+)");
        Matcher m = p.matcher(users);
        System.out.println("--------------UserInfo--One----------");
        int i = 0;
        while(m.find()){
        	if(i == 0 || i==5){
        	System.out.println(operation.getOneUser(user, testcaseName, "local@beijing", Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2))));
        	}		        	  
        	i++;
        }	        	             
        System.out.println("--------------UserInfo--One--Log----------");
        //System.out.println(operation.getUserFailedLog(user, testcaseName, 1, "local@beijing", 0, 100));
        //System.out.println("--------------summary------------");
        //System.out.println(operation.doUserInfoAnalysis(user, testcaseName, "tttt2", 0, null));
    }
    
    private void testAnalysis() throws Exception{
    	//abc_2016_10_29_14_34_43正常
    	//自己构造abc_2016_10_22_16_19_33  abc_2016_10_29_15_46_23
    	doAnalysis(user, "abc_2016_10_29_15_46_23");
    	
    }
    
    private String doAnalysis(String user, String path) throws Exception{
    	PET46 operation = new PET46();    	
    	System.out.println(operation.doAnalysis(user, path));
    	printUserInfo(operation, user, path);
    	System.out.println("-----------------------------------");
    	//System.out.println(operation.getLogsByFirstPage(user, path, 0, 0, null, 0, null, null));
    	System.out.println("-----------------------------------");
    	//System.out.println(operation.getLogsByPage(user, path, 8));    	
    	//printLine(operation, user, path, "Total Task");
    	//printLine(operation, user, path, "test");
    	//printLine(operation, user, path, "tttt2");
    	System.out.println(operation.getTimePoints(user, path, "test", 100, 2, 7, 20, 0, true, false));
    	System.out.println(operation.getTimePoints(user, path, "test-aa", 100, 0, 0, 0, 0, true, false));
    	System.out.println(operation.getTimePoints(user, path, "test", 100, 0, 0, 0, 0, true, true));
    	System.out.println(operation.getTimePoints(user, path, "test", 100, 0, 0, 0, 0, false, false));
    	return "";
    }    
    
    private void printLine(PET46 operation, String user, String path, String task) throws Exception{
    	System.out.println("--------------Line------"+task+"---------------");
    	for(int i = 1; i < 15; i++){
    		System.out.println(new String(operation.getLine(user, path, task, i, 0, 0, -1, -1), "utf-8"));
    	}
    	System.out.println(new String(operation.getLine(user, path, task, 21, 0, 0, -1, -1), "utf-8"));
    	System.out.println(new String(operation.getLine(user, path, task, 101, 0, 0, -1, -1), "utf-8"));
    	System.out.println("--------------Line------"+task+"----End-----------");
    }
    static String user = "lin";
    public static void main(String[] args) throws Exception{
    	  UserInfo userInfo = new UserInfo();
          userInfo.setName(user);
          Test test = new Test();
          test.test(userInfo, true);
          //test.testAnalysis();
	}
}
