package com.cloud.control;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.cloud.pojo.FileEntity;
import com.cloud.pojo.UserLog;
import com.cloud.utils.UIGlobalConfig;
import com.cloud.utils.Utils;

/**
 * 脚本与参数文件的上传、获取所有列表、删除、修改
 * @author kylinpet
 *
 */
@Controller
@RequestMapping("scripter")
public class ScripterAction {
	public static final Logger log = Logger.getLogger(ScripterAction.class);
	
	/**
	 * 删除某个文件，或多个文件
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
            
            boolean isScripter = true;
            String type = request.getParameter("type");
            if(type != null && type.equalsIgnoreCase("para")){
            	isScripter = false;
            }
            
            String idList = request.getParameter("id");//多个ID以逗号分隔
            StringTokenizer st = new StringTokenizer(idList, ",;");
            while(st.hasMoreTokens()){
            	String id = st.nextToken();
	            File f = new File(Utils.webInfoPath+"workspace/"+user+"/scripter/"+(isScripter?(id+".spet"):("para/"+id+".csv")));
	            if(f.exists()){            	
	            	saveFileInfo(f.getParent()+"/"+id+".txt", "", null);//删除掉描述文件，表示文件已经删除            	
	            	File newF = new File(f.getParent()+"/"+id+".bak");
	            	f.renameTo(newF);
	            	
	            	File infoF = new File(f.getParent()+"/"+id+".txt");
	            	infoF.deleteOnExit();
	            }
            }
		} catch(Exception e){
			log.error("", e);
			Utils.outPut500(response, e);
		}
    }
	
	/**
	 * 修改备注或名字
	 * @param request
	 * @param response
	 * 脚本和参数 
	 * id
	 */
	@RequestMapping(value = "modify")
    public void modify(HttpServletRequest request,HttpServletResponse response)
    {
		try{
			String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            String id = request.getParameter("id");
            
            String filename = request.getParameter("filename");
            String des = request.getParameter("des");
            boolean isScripter = true;
            String type = request.getParameter("type");
            if(type != null && type.equalsIgnoreCase("para")){
            	isScripter = false;
            }
            
            File f = new File(Utils.webInfoPath+"workspace/"+user+"/scripter/"+(isScripter?(id+".txt"):("para/"+id+".txt")));
            if(f.exists()){  
            	saveFileInfo(f.getAbsolutePath(), filename, des);
            	Utils.outPut200(request, response, "{\"id\":\""+id+"\"}");
            	return;
            }
            Utils.outPut500(response, Utils.resources.getString("userNotExist"));
		} catch(Exception e){
			log.error("", e);
			Utils.outPut500(response, e);
		}
    }
	
	/**
	 * 获取脚本列表，按时间顺序排序
	 * 
	 * 如果只是获取某一个，则需要传id参数
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
            
            String oneID = request.getParameter("id");//默认为空则查询所有文件，否则查询一个文件
            
            boolean isScripter = true;
            String type = request.getParameter("type");
            if(type != null && type.equalsIgnoreCase("para")){
            	isScripter = false;
            }
            
			File f = new File(Utils.webInfoPath+"workspace/"+user+"/scripter"+(isScripter?"":"/para"));
	    	if(f.exists()){
	    		FileFilter filter = createFileFilter(oneID);
	    		File[] files = f.listFiles(filter);
	    		
	    		files = sortFiles(files);
	    		
	    		ArrayList<FileEntity> list = new ArrayList<>(files.length);
	    		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
	    		for(int i = 0; i < files.length; i++){
	    			File temp = files[i];
	    			if(temp.length() > 1){//1为已经被删除
	    				FileEntity fileEntity = new FileEntity();
	    				list.add(fileEntity);
	    				
	    				String id = temp.getName();
	    				id = id.substring(0, id.length() - 4);//去掉txt
	    				fileEntity.setId(id);
	    				fileEntity.setCreate(formatter.format(new Date(Long.valueOf(id))));
	    				fileEntity.setModify(formatter.format(new Date(temp.lastModified())));
	    				String info = new String(Utils.getDataFromFile(temp.getAbsolutePath()), "utf-8");
	    				int loc = info.indexOf("`");
	    				if(loc > 0){
	    					fileEntity.setName(info.substring(0, loc));
	    					if(loc + 1 < info.length()){
	    						fileEntity.setDes(info.substring(loc+1));
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
	 * 读取某个脚本文件或者参数文件
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
            
            boolean isScripter = true;
            String type = request.getParameter("type");
            if(type != null && type.equalsIgnoreCase("para")){
            	isScripter = false;
            }
            
            File f = new File(Utils.webInfoPath+"workspace/"+user+"/scripter/"+(isScripter?(id+".spet"):("para/"+id+".csv")));                        
            if(!f.exists()){            		            		            	            
            	Utils.outPut500(response, "can't find config "+id);
            	return;
            }
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型   
            response.setContentType("multipart/form-data");   
            //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)   
            response.setHeader("Content-Disposition", "attachment;fileName="+(isScripter?(id+".spet"):("para/"+id+".csv")));   
			Utils.dataToBrowser(f, response, true);
		} catch(Exception e){
			log.error("", e);
			Utils.outPut500(response, e);
		}
    }
	
	/**
	 * 上传脚本或参数文件
	 * @param file
	 * @param request
	 * @param response
	 * 
	 * url需附带3个参数 filename，des(描述),type(默认为脚本，如果type=para,则表示上传的是参数文件)
	 */
    @RequestMapping(value = "upload")
    public void upload(@RequestParam("file") MultipartFile file,
    		HttpServletRequest request,HttpServletResponse response)
    {
    	try{
    		String user = Utils.getUser(request);
            if (user == null || user.length() == 0)
            {
                Utils.outPut500(response, Utils.resources.getString("userNotExist"));
                return;
            }
            
            String filename = request.getParameter("filename");
            String des = request.getParameter("des");
            boolean isScripter = true;
            String type = request.getParameter("type");
            if(type != null && type.equalsIgnoreCase("para")){
            	isScripter = false;
            }
                        
	    	//通过日期，基本可以保证唯一，但为了防止两个人同时创建增加了后面的random，相同的概率基本就为0
	    	String id = String.valueOf(System.currentTimeMillis());	
	    	saveFile(file, user, id, filename, des, isScripter);
	    	Utils.outPut200(request, response, "{\"id\":\""+id+"\"}");	        
        } catch(Exception e){
			log.error("", e);
			Utils.outPut500(response, e);
		}
    }
    
    private int getFileNumber(String user, boolean isScripter){
    	try{
	    	File f = new File(Utils.webInfoPath+"workspace/"+user+"/scripter"+(isScripter?"":"/para"));
	    	if(f.exists()){
	    		FileFilter filter = createFileFilter(null);	             
	    		return f.listFiles(filter).length;
	    	}
    	}catch(Exception e){}
    	return 0;
    }
    
    
    private FileFilter createFileFilter(final String id){
    	FileFilter filter = new FileFilter() {    			
			public boolean accept(File pathname) {
				if(id == null || id.length() == 0){
					return pathname.isFile() && pathname.getName().endsWith("txt");	
				}
				else{
					return pathname.isFile() && pathname.getName().endsWith(id+".txt");
				}
            }
		};
		return filter;
    }
    
    /**
     * 保存文件到磁盘
     * @param file
     * @param user
     * @param id
     * @param filename
     * @param des
     * @param isScripter
     * @throws Exception
     */
    private void saveFile(MultipartFile file, String user, String id, String filename,
    		String des, boolean isScripter) throws Exception{    	    	
		int fileSize = (int)file.getSize();
		int limitFileSize = this.getLimitFileSize(user);
		if(fileSize > limitFileSize){
			UserLog log = new UserLog();
			log.setUser(user);								
			log.setErrorType(2);
			Utils.saveLogToFile(log);
			throw new Exception("file too big, limit "+(limitFileSize/10000000l)+"MB");
		}
		
		int limitInput = this.getLimitInput(user);
		if((des != null && des.length() > limitInput) || (filename != null && filename.length() > limitInput)){
			throw new Exception("input limit " + limitInput);
		}
		
		int limitFile = getLimitFile(user); 
		//判断用户是否允许更多文件		
		if(getFileNumber(user, isScripter) >= limitFile){
			UserLog log = new UserLog();
			log.setUser(user);								
			log.setErrorType(3);
			Utils.saveLogToFile(log);
			throw new Exception(Utils.getInfoString("limitFile", String.valueOf(limitFile)));
		}
		byte[] buffer = new byte[2048];		
		InputStream in = file.getInputStream();
		File userF = new File(Utils.webInfoPath+"workspace/"+user);
		if(!userF.exists()){
			userF.mkdir();
		}
		File f = new File(userF.getAbsolutePath()+"/scripter");
		if(!f.exists()){
			f.mkdir();
		}
		if(!isScripter){//参数文件
			f = new File(f.getAbsolutePath()+"/para");
			if(!f.exists()){
				f.mkdir();
			}
		}
		saveFileInfo(f.getAbsolutePath()+"/"+id+".txt", filename, des);
		
		UserLog userLog = new UserLog();
		userLog.setUser(user);								
		userLog.setAction(isScripter?5:6);
		Utils.saveLogToFile(userLog);
		
		FileOutputStream out = new FileOutputStream(f.getAbsolutePath()+"/"+id+(isScripter?".spet":".csv"));
		try{		
			int offset = 0;
			int len = 0;
			while((len = in.read(buffer)) != -1){
				out.write(buffer, 0, len);
				offset += len;
				if(offset >= fileSize){
					break;
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
	}
    
    /**
     * 保存文件描述，包括文件名与描述
     * @param file
     * @param filename
     * @param des
     */
    private void saveFileInfo(String file, String filename,
    		String des){
    	FileOutputStream out = null;
		try{	
			out = new FileOutputStream(file);
			if(filename != null && filename.length() > 0){
				out.write(filename.getBytes("utf-8"));
			}
			out.write('`');
			if(des != null && des.length() > 0){
				out.write(des.getBytes("utf-8"));
			}			
		}catch(Exception e){
			log.error("", e);
		}
		finally{
			try{				
				out.close();
			}catch(Exception e){}
		}
    }
    
    /**
     * 对文件按时间顺序排序，时间大的在前
     * @param files
     * @return
     */
    public static File[] sortFiles(File[] files){
    	//按修改时间排序
		Arrays.sort(files, new Comparator<File>(){  
			public int compare(File f1, File f2) {  
				long diff = f1.lastModified()-f2.lastModified();  
				if(diff>0)  
					return 1;  
			    else if(diff==0)  
			    	return 0;  
			    else  
			    	return -1;  
			}  
		});
		return files;
    }
    
    public static int getLimitFile(String user){
    	File f = new File(Utils.webInfoPath+"workspace/"+user+"/limit.ini");
    	if(!f.exists()){
    		return UIGlobalConfig.limitFilesNumber;
    	}    	
    	String config = new String(Utils.getDataFromFile(f.getAbsolutePath()));
    	int loc = config.indexOf("file");
    	if(loc > 0){
    		try{
    			int endLoc = config.indexOf("\r\n", loc+5);
    			return Integer.valueOf(config.substring(loc+5, endLoc).trim());
    		}catch(Exception e){}
    	}
    	return UIGlobalConfig.limitFilesNumber;
    }
    
    private int getLimitFileSize(String user){
    	File f = new File(Utils.webInfoPath+"workspace/"+user+"/limit.ini");
    	if(!f.exists()){
    		return UIGlobalConfig.limitFileSize;
    	}    	
    	String config = new String(Utils.getDataFromFile(f.getAbsolutePath()));
    	int loc = config.indexOf("fileSize");
    	if(loc > 0){
    		try{
    			int endLoc = config.indexOf("\r\n", loc+9);
    			return Integer.valueOf(config.substring(loc+9, endLoc).trim());
    		}catch(Exception e){}
    	}
    	return UIGlobalConfig.limitFileSize;
    }
    
    private int getLimitInput(String user){
    	File f = new File(Utils.webInfoPath+"workspace/"+user+"/limit.ini");
    	if(!f.exists()){
    		return UIGlobalConfig.limitInputLength;
    	}    	
    	String config = new String(Utils.getDataFromFile(f.getAbsolutePath()));
    	int loc = config.indexOf("input");
    	if(loc > 0){
    		try{
    			int endLoc = config.indexOf("\r\n", loc+6);
    			return Integer.valueOf(config.substring(loc+6, endLoc).trim());
    		}catch(Exception e){}
    	}
    	return UIGlobalConfig.limitInputLength;
    }
}
