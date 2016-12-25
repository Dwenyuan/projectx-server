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
			if(!codeOK){//�ύ����֤�����				
				response.getOutputStream().write(Utils.resources.getString("codeError").getBytes("utf-8"));
				return;
			}
			
			String phone = request.getParameter("phone").trim();
			String password = request.getParameter("password");
						
			File user = new File(Utils.webInfoPath+"workspace/"+phone);
			if(!user.exists()){
				response.getOutputStream().write(Utils.resources.getString("userError").getBytes("utf-8"));//�û������������
				return;
			}
			
			String myPassword = new String(Utils.getDataFromFile(Utils.webInfoPath+"workspace/"+phone+"/1"));
			if(!myPassword.equals(Utils.getMD5(password))){
				response.getOutputStream().write(Utils.resources.getString("userError").getBytes("utf-8"));//�û������������
				return;
			}
			
			UserLog log = new UserLog();
			log.setUser(phone);								
			log.setAction(1);
			Utils.saveLogToFile(log);
			
			request.getSession().setAttribute("user", phone);//��½�ɹ�
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
			if(!codeOK){//���Ͷ����ύ����֤�����				
				response.getOutputStream().write(Utils.resources.getString("codeError").getBytes("utf-8"));
				return;
			}
			
			String phone = request.getParameter("phone").trim();
			String password = request.getParameter("password");
			String password2 = request.getParameter("password2");
			if(password == null || password2 == null && password.length()==0 || !password.equals(password2)){
				response.getOutputStream().write(Utils.resources.getString("passwordError").getBytes("utf-8"));//�������
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
					if(number >= 100){//һ���ȡ��֤�볬��100��
						response.setStatus(500);
						response.getOutputStream().write(Utils.resources.getString("overLimit").getBytes("utf-8"));//���Ĳ����������ƴ������������ٳ��ԣ�����ϵQQ
						return;
					}
					UIGlobalConfig.code_Number_map.put(clientIP, 1 + number);
				}				
			}
			
			// ������Ӧ�����͸�ʽΪͼƬ��ʽ
	        response.setContentType("image/png");
	        //��ֹͼ�񻺴档
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
	 * �����ֻ���֤��
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
			
			if(!codeOK){//���Ͷ����ύ����֤�����				
				response.getOutputStream().write(Utils.resources.getString("codeError").getBytes("utf-8"));
				return;
			}
			
			String phone = request.getParameter("phone").trim();			
			if(phone == null || phone.length() != 11 || new File(Utils.webInfoPath+"workspace/"+phone).exists()){
				response.getOutputStream().write(Utils.resources.getString("userExist").getBytes("utf-8"));//�û��Ѿ�����
				return;
			}
			
			String clientIP = request.getRemoteAddr();
			if(clientIP != null){
				Integer number = UIGlobalConfig.message_Number_map.get(clientIP);
				if(number == null){
					UIGlobalConfig.message_Number_map.put(clientIP, 1);
				}
				else{
					if(number >= 10){//һ�췢�Ͷ��ų���10��
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
