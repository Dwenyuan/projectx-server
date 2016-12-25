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
 * 登陆拦截
 * 操作次数频繁限制与日志打印
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
			//如果前面有proxy负载均衡，则不需要限制
			if(UIGlobalConfig.limitRequests > 0 && url.indexOf("/getData") < 0){//需要统计一个用户一天的操作次数不能超过限制，否则认为攻击，不处理
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
		
		
		if(url.indexOf("/user/") < 0){//登陆，注销，或者验证码之类的都不需要判断用户是否登陆，否则都需要判断用户已经登陆
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
		// 已通过验证，用户访问继续
		arg2.doFilter(arg0, arg1);
		return;
	}
	/**
     * 该会话user参数不为null表示已经登陆
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
				// 打开一个随机访问文件流，按读写方式
	            randomFile = new RandomAccessFile(Utils.webInfoPath+"ip.txt", "rw");
	            // 文件长度，字节数
	            long fileLength = randomFile.length();
	            //将写文件指针移到文件尾。
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
