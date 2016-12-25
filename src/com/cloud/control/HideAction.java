package com.cloud.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloud.utils.UIGlobalConfig;

@Controller
@RequestMapping("ClOudmAnaGe")
public class HideAction {
	public static final Logger log = Logger.getLogger(HideAction.class);
	
	@RequestMapping(value = "/clearCache")
    public void clearCache(HttpServletRequest request, HttpServletResponse response)
    {
		try{
			if(!isOK(request, response)){
				return;
			}
			UIGlobalConfig.code_Number_map.clear();
			UIGlobalConfig.message_Number_map.clear();
		}catch(Exception e){
			log.error("", e);
		}
    }
	
	private boolean isOK(HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(request.getParameter("KeY").equals("KyLInClouD")){
			return true;
		}
		response.getOutputStream().write("Error".getBytes());
		return false;
	}
}
