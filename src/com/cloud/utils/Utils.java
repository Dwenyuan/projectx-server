package com.cloud.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class Utils {
	private static Logger logger = Logger.getLogger(Utils.class);
	public static Random r = new Random(System.currentTimeMillis());
	
	/**
	 * 从接收的Http里获取json内容
	 * @param request
	 * @return
	 * @throws Exception
	 */
    public static String getDataFromHTTP(HttpServletRequest request) throws Exception
    {
        InputStream inputStream = request.getInputStream();
        // 取HTTP请求流长度
        int size = request.getContentLength();
        return new String(getDataFromHTTP(size, inputStream), "utf-8");
    }
    
    private static byte[] getDataFromHTTP(int size, InputStream inputStream) throws Exception{
    	if(size < 0){//是chunked
        	ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
        	byte[] buffer = new byte[1024];
        	int len = inputStream.read(buffer);
        	while(len != -1){
        		out.write(buffer, 0, len);
        		if(out.size() > 250000000){//限制200M
            		throw new Exception("Size Too Big");
            	}
        		len = inputStream.read(buffer);
        	}
        	byte[] dataByte = out.toByteArray();
        	out.close();
        	return dataByte;
        }
        else{//存在content-length
	        // 用于存放结果的数组
        	if(size > 250000000){//限制200M
        		throw new Exception("Size Too Big");
        	}
	        byte[] dataByte = new byte[size];
	        int offset = 0;
	        while (size > 0)
	        {
	            int len = inputStream.read(dataByte, offset, size > 1024 ? 1024 : size);
	            offset += len;
	            size -= len;
	        }
	        return dataByte;
        }        
    }
    
    public static void outPut500(HttpServletResponse response, Exception e){    	
		try{
			response.setStatus(500);
			response.getOutputStream().write(("action failed. because "+e).getBytes());
		}catch(Exception ee){
			
		}
    }
    
    public static void outPut500(HttpServletResponse response, String jsonStr){    	
		try{
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(500);
			response.getOutputStream().write(jsonStr.getBytes("utf-8"));
		}catch(Exception ee){
			logger.error("",ee);
		}
    }
    
    public static void outPut200(HttpServletResponse response, String json){    	
		try{
			response.setContentType("application/json; charset=utf-8"); 
			response.setHeader("Cache-Control", "no-cache");
			if(json.length() > 10000){//大于10K，压缩传输
				response.setHeader("Content-Encoding", "gzip");

				GZIPOutputStream gps = new GZIPOutputStream(response.getOutputStream());
				gps.write(json.getBytes("utf-8"));
				gps.close();
			}
			else{
				response.getOutputStream().write(json.getBytes("utf-8"));
			}
		}catch(Exception e){
			logger.error("", e);
		}
    }    
    
    public static void outPutHTML(HttpServletResponse response, String html){    	
		try{
			response.setContentType("text/html; charset=utf-8"); 
			response.setHeader("Cache-Control", "no-cache");
			byte[] htmlData = html.getBytes("utf-8");
			if(htmlData.length > 10000){//大于10K，压缩传输
				response.setHeader("Content-Encoding", "gzip");

				GZIPOutputStream gps = new GZIPOutputStream(response.getOutputStream());
				gps.write(htmlData);
				gps.close();
			}
			else{
				response.getOutputStream().write(htmlData);
			}
		}catch(Exception e){
			logger.error("", e);
		}
    }    
    
    /**
     * 远程调用其他系统接口需要通过服务层
     * @param url
     * @param content
     * @return
     * @throws Exception
     */
    public static String doPost(String url, String content) throws Exception {
        URL localURL = new URL(url);
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        byte[] contentBytes = content.getBytes("utf-8");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(contentBytes.length));
        
        OutputStream outputStream = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        try {
            outputStream = httpURLConnection.getOutputStream();            
            outputStream.write(contentBytes);
            outputStream.flush();
            
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }

        return resultBuffer.toString();
    }	
    
    /**
     * 远程调用其他系统接口需要通过服务层
     * @param url
     * @return
     * @throws Exception
     */
    public static byte[] doGet(String url, String cookie, int timeout) throws Exception {
    	if(logger.isInfoEnabled()){
    		logger.info("url="+url);   
    	}
        URL localURL = new URL(url);
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        httpURLConnection.setDoOutput(false);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        if(cookie != null){
        	httpURLConnection.setRequestProperty("Cookie", cookie);
        }
        httpURLConnection.setReadTimeout(timeout);//120秒还没读到数据则退出
        InputStream inputStream = null;
        byte[] picData = null;
        
        try {            
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            
            int size = httpURLConnection.getContentLength();
            inputStream = httpURLConnection.getInputStream();
            
            picData = getDataFromHTTP(size, inputStream);           
        } catch (Exception e)
	    {
	    	logger.error("", e);
	    }finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if(httpURLConnection != null){
            	httpURLConnection.disconnect();
            }
        }

        return picData;
    }                
}
