package com.cloud.control;

import java.io.IOException;
import java.io.RandomAccessFile;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cloud.pojo.UserLog;
import com.cloud.utils.UIGlobalConfig;
import com.cloud.utils.Utils;

/**
 * ��½����
 * ��������Ƶ����������־��ӡ
 * @author kylinpet
 *
 */
public class LoginFilter implements Filter
{
	public RandomAccessFile randomFile;
	
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException
	{
		HttpServletResponse response = (HttpServletResponse) arg1;
		HttpServletRequest request = (HttpServletRequest) arg0;
		String url = request.getRequestURI(); 		
		
		try{
			//���ǰ����proxy���ؾ��⣬����Ҫ����
			if(UIGlobalConfig.limitRequests > 0 && url.indexOf("/getData") < 0){//��Ҫͳ��һ���û�һ��Ĳ����������ܳ������ƣ�������Ϊ������������
				String ip = request.getRemoteAddr();
				if(UIGlobalConfig.limitIP.contains(ip)){
					return;
				}
				Integer number = UIGlobalConfig.request_Number_map.get(ip);
				if(number == 0){
					UIGlobalConfig.request_Number_map.put(ip, 1);
				}
				else{
					if(number >= UIGlobalConfig.limitRequests){
						saveIPToFile(ip);
						HttpSession session = request.getSession(false);
						if(session != null){
							Object userObj = session.getAttribute("user");
							if(userObj != null){
								UserLog log = new UserLog();
								log.setUser((String)userObj);								
								log.setErrorType(1);
								Utils.saveLogToFile(log);
							}
						}
						return;
					}
					UIGlobalConfig.request_Number_map.put(ip, number+1);
				}
			}
		}catch(Exception e){
			
		}
		
		
		if(url.indexOf("/user/") < 0){//��½��ע����������֤��֮��Ķ�����Ҫ�ж��û��Ƿ��½��������Ҫ�ж��û��Ѿ���½
			HttpSession session = request.getSession(false);
    		if(session == null || !isLogin(session)){
        		response.sendRedirect("login.jsp");  
    			return;    
        	}
		}
		else if (url.indexOf("logout") >= 0)
		{
			HttpSession session = request.getSession(false);
			if(session != null){
				session.setAttribute("user", null);
			}			
			response.sendRedirect("login.jsp");  
			return;
		}		
		// ��ͨ����֤���û����ʼ���
		arg2.doFilter(arg0, arg1);
		return;
	}
	/**
     * �ûỰuser������Ϊnull��ʾ�Ѿ���½
     * @param session
     * @return
     */
    private boolean isLogin(HttpSession session){    	
    	if (session.getAttribute("user") != null) {
			return true;
		} else {
			return false;
		}
    }
	public void init(FilterConfig arg0) throws ServletException
	{

	}

	private synchronized void saveIPToFile(String ip){
		try{
			if(randomFile == null){
				// ��һ����������ļ���������д��ʽ
	            randomFile = new RandomAccessFile(Utils.webInfoPath+"ip.txt", "rw");
	            // �ļ����ȣ��ֽ���
	            long fileLength = randomFile.length();
	            //��д�ļ�ָ���Ƶ��ļ�β��
	            randomFile.seek(fileLength);	            
			}
			randomFile.writeBytes(ip+"\r\n");
		}catch(Exception e){
			
		}
	}
	
	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy()
	{
		try{
			if(randomFile != null){
				randomFile.close();
			}
			if(Utils.userLogFile != null){
				Utils.userLogFile.close();
			}
		}catch(Exception e){
			
		}
	}
}
