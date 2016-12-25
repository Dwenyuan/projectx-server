package com.cloud.control;

import java.io.File;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloud.pojo.UserLog;
import com.cloud.utils.CreateImageCode;
import com.cloud.utils.UIGlobalConfig;
import com.cloud.utils.Utils;

@Controller
@RequestMapping("user")
public class UserManageAction {
	public static final Logger log = Logger.getLogger(UserManageAction.class);
	
	@RequestMapping(value = "/login")
    public void login(HttpServletRequest request, HttpServletResponse response)
    {
		try{
			String code = request.getParameter("code");
			boolean codeOK = false;
			if(code != null){
				Object tempCode = request.getSession().getAttribute("code");			
				if(tempCode != null && code.equals((String)tempCode)){
					codeOK = true;
				}
			}
			if(!codeOK){//提交的验证码错误				
				response.getOutputStream().write(Utils.resources.getString("codeError").getBytes("utf-8"));
				return;
			}
			
			String phone = request.getParameter("phone").trim();
			String password = request.getParameter("password");
						
			File user = new File(Utils.webInfoPath+"workspace/"+phone);
			if(!user.exists()){
				response.getOutputStream().write(Utils.resources.getString("userError").getBytes("utf-8"));//用户名或密码错误
				return;
			}
			
			String myPassword = new String(Utils.getDataFromFile(Utils.webInfoPath+"workspace/"+phone+"/1"));
			if(!myPassword.equals(Utils.getMD5(password))){
				response.getOutputStream().write(Utils.resources.getString("userError").getBytes("utf-8"));//用户名或密码错误
				return;
			}
			
			UserLog log = new UserLog();
			log.setUser(phone);								
			log.setAction(1);
			Utils.saveLogToFile(log);
			
			request.getSession().setAttribute("user", phone);//登陆成功
		}catch (Exception e)
		{
			log.error("", e);      
			Utils.outPut500(response, e);            
		}
    }
		
	@RequestMapping(value = "/register")
    public void register(HttpServletRequest request, HttpServletResponse response)
    {
		try{
			String code = request.getParameter("code");
			boolean codeOK = false;
			if(code != null){
				Object tempCode = request.getSession().getAttribute("pcode");			
				if(tempCode != null && code.equals((String)tempCode)){
					codeOK = true;
				}
			}
			if(!codeOK){//发送短信提交的验证码错误				
				response.getOutputStream().write(Utils.resources.getString("codeError").getBytes("utf-8"));
				return;
			}
			
			String phone = request.getParameter("phone").trim();
			String password = request.getParameter("password");
			String password2 = request.getParameter("password2");
			if(password == null || password2 == null && password.length()==0 || !password.equals(password2)){
				response.getOutputStream().write(Utils.resources.getString("passwordError").getBytes("utf-8"));//密码错误
				return;
			}
			
			File user = new File(Utils.webInfoPath+"workspace/"+phone);
			if(!user.exists()){
				user.mkdir();
			}
			Utils.saveDataToFile(Utils.webInfoPath+"workspace/"+phone+"/1", Utils.getMD5(password).getBytes());
			
			String name = request.getParameter("name");			
			String company = request.getParameter("company");
			Utils.saveDataToFile(Utils.webInfoPath+"workspace/"+phone+"/info.txt", 
					(((name==null)?"":name)+":"+((company==null)?"":company)).getBytes("utf-8"));
		}catch (Exception e)
		{
			log.error("", e);      
			Utils.outPut500(response, e);            
		}
    }
	
	@RequestMapping(value = "/getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response)
    {
		try{
			String clientIP = request.getRemoteAddr();
			if(clientIP != null){
				Integer number = UIGlobalConfig.code_Number_map.get(clientIP);
				if(number == null){
					UIGlobalConfig.code_Number_map.put(clientIP, 1);
				}
				else{
					if(number >= 100){//一天获取验证码超过100次
						response.setStatus(500);
						response.getOutputStream().write(Utils.resources.getString("overLimit").getBytes("utf-8"));//您的操作超过限制次数，请明天再尝试，或联系QQ
						return;
					}
					UIGlobalConfig.code_Number_map.put(clientIP, 1 + number);
				}				
			}
			
			// 设置响应的类型格式为图片格式
	        response.setContentType("image/png");
	        //禁止图像缓存。
	        response.setHeader("Pragma", "no-cache");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setDateHeader("Expires", 0);
	                
	        CreateImageCode vCode = new CreateImageCode(100,30,4,6);
	        request.getSession().setAttribute("code", vCode.getCode());
	        vCode.write(response.getOutputStream());
		}
        catch (Exception e)
        {
            log.error("", e);      
            Utils.outPut500(response, e);            
        }
    }
	
	/**
	 * 发送手机验证码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sendCode")
    public void sendCode(HttpServletRequest request, HttpServletResponse response)
    {
		try{
			String code = request.getParameter("code");
			boolean codeOK = false;
			if(code != null){
				Object tempCode = request.getSession().getAttribute("code");			
				if(tempCode != null && code.equals((String)tempCode)){
					codeOK = true;
				}
			}
			
			if(!codeOK){//发送短信提交的验证码错误				
				response.getOutputStream().write(Utils.resources.getString("codeError").getBytes("utf-8"));
				return;
			}
			
			String phone = request.getParameter("phone").trim();			
			if(phone == null || phone.length() != 11 || new File(Utils.webInfoPath+"workspace/"+phone).exists()){
				response.getOutputStream().write(Utils.resources.getString("userExist").getBytes("utf-8"));//用户已经存在
				return;
			}
			
			String clientIP = request.getRemoteAddr();
			if(clientIP != null){
				Integer number = UIGlobalConfig.message_Number_map.get(clientIP);
				if(number == null){
					UIGlobalConfig.message_Number_map.put(clientIP, 1);
				}
				else{
					if(number >= 10){//一天发送短信超过10次
						response.setStatus(500);
						response.getOutputStream().write(Utils.resources.getString("overLimit").getBytes("utf-8"));
						return;
					}
					UIGlobalConfig.message_Number_map.put(clientIP, 1 + number);
				}				
			}
			
			Random r = new Random(System.currentTimeMillis());
			StringBuilder sb = new StringBuilder();
			while(sb.length()<5){
				sb.append(r.nextInt(10));
			}
			String pCode = sb.toString();
			if(pCode.length() > 5){
				pCode = pCode.substring(0, 5);
			}
			request.getSession().setAttribute("pcode", pCode);
			
		}
        catch (Exception e)
        {
            log.error("", e);      
            Utils.outPut500(response, e);            
        }
    }
	
	
}
