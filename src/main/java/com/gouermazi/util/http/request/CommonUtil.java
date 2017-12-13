package com.gouermazi.util.http.request;


import com.alibaba.fastjson.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

//import net.sf.json.JSONObject;

/**
* 类名: CommonUtil 
* 描述: 请求https和http通用工具类 
* 开发人员： jieFan 
*/

public class CommonUtil {
//	private static Logger log = LoggerFactory.getLogger(CommonUtil.class);
	  // 凭证获取（GET）
	  public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	  
	  /**
	   *发送https请求 
	   */
	  public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
	    JSONObject jsonObject = null;
	    try {
	      // 创建SSLContext对象，并使用我们指定的信任管理器初始化
	      TrustManager[] tm = { new MyX509TrustManager() };
	      SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
	      sslContext.init(null, tm, new java.security.SecureRandom());
	      // 从上述SSLContext对象中得到SSLSocketFactory对象
	      SSLSocketFactory ssf = sslContext.getSocketFactory();
	      URL url = new URL(requestUrl);
	      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	      conn.setSSLSocketFactory(ssf);
	      conn.setDoOutput(true);
	      conn.setDoInput(true);
	      conn.setUseCaches(false);
	      // 设置请求方式（GET/POST）
	      conn.setRequestMethod(requestMethod);
	      // 当outputStr不为null时向输出流写数据
	      if (null != outputStr) {
	        OutputStream outputStream = conn.getOutputStream();
	        // 注意编码格式
	        outputStream.write(outputStr.getBytes("UTF-8"));
	        outputStream.close();
	      }
	      // 从输入流读取返回内容
	      InputStream inputStream = conn.getInputStream();
	      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	      String str = null;
	      StringBuffer buffer = new StringBuffer();
	      while ((str = bufferedReader.readLine()) != null) {
	        buffer.append(str);
	      }
	      // 释放资源
	      bufferedReader.close();
	      inputStreamReader.close();
	      inputStream.close();
	      inputStream = null;
	      conn.disconnect();
	      jsonObject = JSONObject.parseObject(buffer.toString());
	    } catch (ConnectException ce) {
//	      log.error("连接超时：{}", ce);
	      System.out.println("连接超时：{}"+ce);
	    } catch (Exception e) {
//	      log.error("https请求异常：{}", e);
 		  System.out.println("https请求异常：{}"+e);
	    }
	    return jsonObject;
	  }
	  
	  /**
	   *发送http请求 
	   */
	  public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr){
		  JSONObject jsonObject = null;
		    try {
		      URL url = new URL(requestUrl);
		      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		      conn.setDoOutput(true);
		      conn.setDoInput(true);
		      conn.setUseCaches(false);
		      // 设置请求方式（GET/POST）
		      conn.setRequestMethod(requestMethod);
		      // 当outputStr不为null时向输出流写数据
		      if (null != outputStr) {
		        OutputStream outputStream = conn.getOutputStream();
		        // 注意编码格式
		        outputStream.write(outputStr.getBytes("UTF-8"));
		        outputStream.close();
		      }
		      // 从输入流读取返回内容
		      InputStream inputStream = conn.getInputStream();
		      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		      String str = null;
		      StringBuffer buffer = new StringBuffer();
		      while ((str = bufferedReader.readLine()) != null) {
		        buffer.append(str);
		      }
		      // 释放资源
		      bufferedReader.close();
		      inputStreamReader.close();
		      inputStream.close();
		      inputStream = null;
		      conn.disconnect();
		      jsonObject = JSONObject.parseObject(buffer.toString());
		    } catch (ConnectException ce) {
                System.out.println("连接超时：{}"+ce);
		    } catch (Exception e) {
                System.out.println("http请求异常：{}"+e);
		    }
		    return jsonObject;
	  }
}
