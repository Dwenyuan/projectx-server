package com.cloud.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cloud.pojo.UserLog;

public class Utils {
	private static Logger logger = Logger.getLogger(Utils.class);
	public static Random r = new Random(System.currentTimeMillis());
	public static RandomAccessFile userLogFile;
	public static Object forSyn = new Object();
	
	public static ResourceBundle resources;	
	
	public static String webInfoPath = "./";
    static
    {
    	try{
    		String languageFile = "language";
        	switch(UIGlobalConfig.language){
        	case 1:
        		Locale.setDefault(Locale.CHINA);
        		languageFile = "language_zh-CN";
        		break;
        	case 2:
        		Locale.setDefault(Locale.JAPAN);
        		languageFile = "language_ja";
        		break;
        	case 3:
        		Locale.setDefault(Locale.FRANCE);
        		languageFile = "language_fr";
        		break;
        	case 4:
        		Locale.setDefault(Locale.GERMANY);
        		languageFile = "language_de";
        		break;
        	case 5:
        		Locale.setDefault(Locale.ITALY);
        		languageFile = "language_it";
        		break;
        	case 6:        		
        		Locale.setDefault(new Locale("ru", "RU"));
        		languageFile = "language_ru";
        		break;
        	case 7:
        		Locale.setDefault(new Locale("iw", "IL"));
        		languageFile = "language_iw";
        		break;
        	case 8:
        		Locale.setDefault(new Locale("ar", "EG"));
        		languageFile = "language_ar";
       		 	break;
        	default:
        		Locale.setDefault(Locale.US);
        	}
        	resources = ResourceBundle.getBundle(languageFile,
                            Locale.getDefault());
		}catch(Exception e){
			logger.error("", e);		
		}
    	
        webInfoPath = new Utils().getClass().getResource("").getPath();
        // һ������classes+��������ȥ�������ڵ�·���󲿷֣��õ�WEB-INFĿ¼
        int webINFLoc = webInfoPath.indexOf("WEB-INF");
        if (webINFLoc > 0)
        {
            webInfoPath = webInfoPath.substring(1, webINFLoc);
            webInfoPath = webInfoPath.replace("%20", " ");// �ļ�Ŀ¼�ո���%20ת����������tomcatʶ�𲻳�
            if (System.getProperty("os.name").toLowerCase().indexOf("window") < 0)
            {
                webInfoPath = "/" + webInfoPath;
            }
            logger.warn("webInfoPath=" + webInfoPath);
        }
        else
        {
        	webInfoPath = "./";
        	logger.warn("can't get webInfoPath");
        }
    }
	
    /**
	 * ��һ�����������ַ���
	 * @param key
	 * @param param
	 * @return
	 */
	public static String getInfoString(String key, String param) {
		String text = resources.getString(key);
		text = MessageFormat.format(text,
				new Object[] {new String(param) });
		return text;
	}
    
    public static String getUser(HttpServletRequest request){
    	Object userObj = request.getSession().getAttribute("user");
    	String user = null;
    	if(userObj != null){
    		user = (String)userObj;
    	}
    	else{
    		user = request.getParameter("userCode");
    	}
    	return user;
    }
    
	/**
	 * �ӽ��յ�Http���ȡjson����
	 * @param request
	 * @return
	 * @throws Exception
	 */
    public static String getDataFromHTTP(HttpServletRequest request) throws Exception
    {
        InputStream inputStream = request.getInputStream();
        // ȡHTTP����������
        int size = request.getContentLength();
        return new String(getDataFromHTTP(size, inputStream), "utf-8");
    }
    
    private static byte[] getDataFromHTTP(int size, InputStream inputStream) throws Exception{
    	if(size < 0){//��chunked
        	ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
        	byte[] buffer = new byte[1024];
        	int len = inputStream.read(buffer);
        	while(len != -1){
        		out.write(buffer, 0, len);
        		if(out.size() > UIGlobalConfig.limitFileSize){//����
            		throw new Exception("Size Too Big");
            	}
        		len = inputStream.read(buffer);
        	}
        	byte[] dataByte = out.toByteArray();
        	out.close();
        	return dataByte;
        }
        else{//����content-length
	        // ���ڴ�Ž��������
        	if(size > UIGlobalConfig.limitFileSize){//����200M
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
    
    /**
     * ��ȡ�ļ�������������ؽű�������������������
     * @param filename
     * @param response
     */
    public static void dataToBrowser(File filename, HttpServletResponse response, boolean limit){
    	FileInputStream in = null;
		try{		
			OutputStream out = response.getOutputStream();
			in = new FileInputStream(filename);
			response.setContentLength((int)filename.length());
			int len = 0;
			byte[] buffer = new byte[2048];
			while((len = in.read(buffer)) != -1){
				out.write(buffer, 0, len);		
				if(limit){
					doLimit(len);
				}
			}
			out.flush();
		}catch(Exception e){
			logger.error("", e);
		}
		finally{
			try{
				in.close();
			}catch(Exception e){}
		}
    }
    
    static long[] total_time = new long[2];
    
    /**
     * ���ؽű���CSVռ�õĴ���������ƣ���ֹ������������������
     * @param size
     * @param total_time
     * @param limitBW
     */
    public static void doLimit(int size){
    	total_time[0] += size;
		if(total_time[0]>=UIGlobalConfig.limitDownloadBW){//�������Ƶĳ���
			long nowTime = System.nanoTime();
			long timeleft = nowTime - total_time[1];
//System.err.print("timeleft="+timeleft+"  "+total_time[1]+"  "+total_time[0]);				
			if(timeleft<48000000l){//ÿ50����ͳ��һ��
				try{
					Thread.sleep((50000000l - timeleft)/1000000);
				}catch(Exception e){}
				total_time[1] = System.nanoTime();
			}
			else{
				total_time[1] = nowTime;
			}
			total_time[0] = 0;				
		}
	}
    
    public static void outPut500(HttpServletResponse response, Exception e){    	
		try{
			response.setStatus(500);
			response.setContentType("text/plain; charset=utf-8");
			response.getOutputStream().write(("action failed. because "+e).getBytes("utf-8"));
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
    
    public static void outPut200(HttpServletRequest request, HttpServletResponse response, String json){    	
		try{
			response.setContentType("application/json; charset=utf-8"); 
			response.setHeader("Cache-Control", "no-cache");
			setTomcatIP(request, response);
			if(json.length() > 10000){//����10K��ѹ������
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
    
    public static void outPutHTML(HttpServletRequest request, HttpServletResponse response, String html){    	
		try{
			response.setContentType("text/html; charset=utf-8"); 
			response.setHeader("Cache-Control", "no-cache");
			setTomcatIP(request, response);
			byte[] htmlData = html.getBytes("utf-8");
			if(htmlData.length > 10000){//����10K��ѹ������
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
    
    private static void setTomcatIP(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	String ip = request.getLocalAddr();    	
    	response.setHeader("X-Label", getMD5(ip));
    }
    
    public static String getMD5(String value) throws Exception{
    	// ����һ��MD5���ܼ���ժҪ
        MessageDigest md = MessageDigest.getInstance("MD5");
        // ����md5����
        md.update(value.getBytes());
        // digest()���ȷ������md5 hashֵ������ֵΪ8Ϊ�ַ�������Ϊmd5 hashֵ��16λ��hexֵ��ʵ���Ͼ���8λ���ַ�
        // BigInteger������8λ���ַ���ת����16λhexֵ�����ַ�������ʾ���õ��ַ�����ʽ��hashֵ
    	String md5 = new BigInteger(1, md.digest()).toString(16);
    	return md5;
    }
    
    /**
     * Զ�̵�������ϵͳ�ӿ���Ҫͨ�������
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
     * Զ�̵�������ϵͳ�ӿ���Ҫͨ�������
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
        httpURLConnection.setReadTimeout(timeout);//120�뻹û�����������˳�
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
    
   	public static byte[] getDataFromFile(String filename){
   		try{
   			FileInputStream file_in =new FileInputStream(filename);
   			int length = file_in.available();
   			byte[] data = new byte[length];
   			file_in.read(data);
   			file_in.close();
   	        return data;
   		}catch(Exception e){
   			logger.error("", e);
   		}	
   		return null;
   	}
   	
   	/**
   	 * ֻ��ȡǰ�沿���ֽڣ���ȡ��������������ʱ
   	 * @param filename
   	 * @param readLength
   	 * @return
   	 */
   	public static byte[] getDataFromFile(String filename, int readLength){
   		try{
   			FileInputStream file_in =new FileInputStream(filename);   
   			int length = file_in.available();
   			byte[] data = new byte[length>readLength?readLength:length];
   			file_in.read(data);
   			file_in.close();
   	        return data;
   		}catch(Exception e){
   			logger.error("", e);
   		}	
   		return null;
   	}
	
	/**
	 * �������ݵ��ļ�
	 * @param filename
	 * @param data
	 */
	public static void saveDataToFile(String filename,byte[] data){		
		try{
			FileOutputStream file_out = null;
			file_out =new FileOutputStream(filename);								
			file_out.write(data);			
			file_out.flush();
			file_out.close();			
		}catch(Exception e){
			logger.error("", e);
		}
	}
	
	public static void saveLogToFile(UserLog log){
		log.setTime(System.currentTimeMillis());
		synchronized(forSyn){
			try{
				if(userLogFile == null){
					// ��һ����������ļ���������д��ʽ
					userLogFile = new RandomAccessFile(Utils.webInfoPath+"action.log", "rw");
		            // �ļ����ȣ��ֽ���
		            long fileLength = userLogFile.length();
		            //��д�ļ�ָ���Ƶ��ļ�β��
		            userLogFile.seek(fileLength);	            
				}
				byte[] length = new byte[2];
				byte[] data = log.toBytes();
				intToBytes(data.length, 2, length, 0);
				userLogFile.write(length);
				userLogFile.write(data);
			}catch(Exception e){
				
			}
		}
	}
	
	/**
     * ��һ����ֵ��ɶ�����
     * @param val  ��ֵ
     * @param num  ��ֵ����(ռ�õ�byte��)
     * bΪ���ֽ����飬offΪ���������λ��
     */
    public static void intToBytes(long val, int num,byte[] b,int off) {
    	//byte[] b = new byte[num];
        for(int i=0; i<num; i++)
            b[ off + num - i - 1] = (byte)((val & (0xFFL << (i * 8))) >> (i * 8));
        //return b;
    }
}
