package com.bee.app.api;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bee.app.AppContext;
import com.bee.app.AppException;
import com.bee.app.bean.BeeSource;
import com.bee.app.bean.PicBean;
import com.bee.app.bean.Update;
import com.bee.app.bean.UserPoint;
import com.bee.app.utils.MD5;
import com.bee.common.Constants;


public class ApiClient {
	
	public static final String UTF_8 = "UTF-8";

	
	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;


	
	public static HttpClient getHttpClient() {        
        HttpClient httpClient = new HttpClient();
        
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间 
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}
	
	
	public static DefaultHttpClient getHttpClient2() {        
	       /* HttpClient httpClient = new HttpClient();*/
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		//	httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
	        // 设置 默认的超时重试处理策略
		//	httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			// 设置 连接超时时间
			/*httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION)*/;
			// 设置 读数据超时时间 
			/*httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);*/
			// 设置 字符集
		//	httpClient.getParams().setContentCharset(UTF_8);
			return httpClient;
		}
	
	
	public static PostMethod getHttpPost(String url/*, String cookie, String userAgent*/) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		/*httpPost.setRequestHeader("Host", Constants.URL_INIT);*/
		httpPost.setRequestHeader("Connection","Keep-Alive");
		/*httpPost.setRequestHeader("Cookie", cookie);*/
		/*httpPost.setRequestHeader("User-Agent", userAgent);*/
		return httpPost;
	}
	
	
	public static GetMethod getHttpGet(String url/*, String cookie, String userAgent*/) {
		GetMethod httpGet = new GetMethod(url);
		
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		/*httpGet.setRequestHeader("Host", URLs.HOST);*/
		httpGet.setRequestHeader("Connection","Keep-Alive");
	/*	httpGet.setRequestHeader("Cookie", cookie);*/
		/*httpGet.setRequestHeader("User-Agent", userAgent);*/
		return httpGet;
	}
	
	public static GetMethod getHttpGet(String url, String cookie, String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		
		httpGet.setRequestHeader("cookie",cookie);
		
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		/*httpGet.setRequestHeader("Host", URLs.HOST);*/
		httpGet.setRequestHeader("Connection","Keep-Alive");
	/*	httpGet.setRequestHeader("Cookie", cookie);*/
		/*httpGet.setRequestHeader("User-Agent", userAgent);*/
		return httpGet;
	}
	
	public static GetMethod getHttpGet2(String url/*, String cookie, String userAgent*/) {
		GetMethod httpGet = new GetMethod(url);
		
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		/*httpGet.setRequestHeader("Host", URLs.HOST);*/
		httpGet.setRequestHeader("Connection","Keep-Alive");
	/*	httpGet.setRequestHeader("Cookie", cookie);*/
		/*httpGet.setRequestHeader("User-Agent", userAgent);*/
		return httpGet;
	}
	
	
	/**
	 * 上传图片
	 * @param 
	 * @return
	 * @throws AppException
	 */
	public static boolean uploadPic(AppContext appContext,String picUrl, PicBean picBean,String uid,String phone) throws AppException {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("lat", picBean.getLat());
		params.put("lng", picBean.getLng());
		params.put("u", uid);
		params.put("picName", picBean.getTitle());
		
		//add		
		params.put("ORGID", picBean.getUnitNumber());
		
		params.put("mobile",phone);
		
		params.put("address", picBean.getAddress());
		
		//add
		
					
		Map<String, File> files = new HashMap<String, File>();
		if(picBean.getPath() != null)
			files.put("img", picBean.getImgeFile());
		
		try{
			return http_post(appContext, picUrl, params, files);		
		}catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
			throw AppException.network(e);
		}
	}
	
	
	/**
	 * 上传蜜源信息
	 * @param 
	 * @return
	 * @throws AppException
	 */
	/*public static boolean uploadBeanSource(AppContext appContext,String BeaSourceUrl, BeeSource beeSource) throws AppException {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("lat", picBean.getLat());
		params.put("lng", picBean.getLng());
		params.put("u", picBean.getUid());
		params.put("picName", picBean.getTitle());
		
					
		Map<String, File> files = new HashMap<String, File>();
		if(picBean.getPath() != null)
			files.put("img", picBean.getImgeFile());
		
		try{
			return http_post(appContext, picUrl, params, files);		
		}catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
			throw AppException.network(e);
		}
	}*/
	
	
	
	
	
	
	
	
	
	/**
	 * 上传位置
	 * @param 
	 * @return
	 * @throws AppException
	 */
	public static boolean uploadPosition(AppContext appContext,String pointUrl, UserPoint userPoint,String userId,String mobile) throws AppException {
		
		try {
			String url = pointUrl + "/userPoint.aspx?u=" + userId
					+ "&lat=" + userPoint.getLat() + "&lng="
					+ userPoint.getLng() + "&name="
					+ URLEncoder.encode(userPoint.getName(), "UTF-8")
					+"&mobile="+mobile//add
			        +"&ORGID="+userPoint.getUnitNumber()
			        +"&address="+URLEncoder.encode(userPoint.getAddress(),"UTF-8");
			
			InputStream is = http_get(appContext, url);
			InputStreamReader inReader=new InputStreamReader(is);
			BufferedReader buffer=new BufferedReader(inReader);
			
			String strLine=null;
			String result="";
			while((strLine=buffer.readLine())!=null)
			{
				result+=strLine;
			}
		    if(result.equals("发送成功")){
		    	return true;
		    	
		    }else{
		    	
		    	return false;
		    }
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return false;
	}
	
		
	/**
	 * post请求URL
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException 
	 * @throws IOException 
	 * @throws  
	 */
	private static Boolean http_post(AppContext appContext, String url, Map<String, Object> params, Map<String,File> files) throws AppException, IOException {

		String result=_post(appContext, url, params, files);
		
		if(result.equals("1")||result.equals("处理完成")){
        	return true;  	
        }else if(result.equals("0")){	
        	return false;
        }
		return false;
	}
	
	/**
	 * 公用post方法
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	private static String _post(AppContext appContext, String url, Map<String, Object> params, Map<String,File> files) throws AppException {

		/*String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);*/
		
		HttpClient httpClient = null;
		PostMethod httpPost = null;
		
		//post表单参数处理
		int length = (params == null ? 0 : params.size()) + (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
        if(params != null)
        for(String name : params.keySet()){
        	parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
        	//System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
        }
        if(files != null)
        for(String file : files.keySet()){
        	try {
				parts[i++] = new FilePart(file, files.get(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        	//System.out.println("post_key_file==> "+file);
        }
		
		String responseBody = "";
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpPost = getHttpPost(url/*, cookie, userAgent*/);	        
		        httpPost.setRequestEntity(new MultipartRequestEntity(parts,httpPost.getParams()));		        
		        int statusCode = httpClient.executeMethod(httpPost);
		        if(statusCode != HttpStatus.SC_OK) 
		        {
		        	throw AppException.http(statusCode);
		        }
		        else if(statusCode == HttpStatus.SC_OK) 
		        {
		            /*Cookie[] cookies = httpClient.getState().getCookies();
		            String tmpcookies = "";
		            for (Cookie ck : cookies) {
		                tmpcookies += ck.toString()+";";
		            }*/
		            //保存cookie   
	        		/*if(appContext != null && tmpcookies != ""){
	        			appContext.setProperty("cookie", tmpcookies);
	        			appCookie = tmpcookies;
	        		}*/
		        }
		     	responseBody = httpPost.getResponseBodyAsString();
		     	
		        //System.out.println("XMLDATA=====>"+responseBody);
		     	break;	     	
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
        
      /*  responseBody = responseBody.replaceAll("\\p{Cntrl}", "");*/
		/*if(responseBody.contains("result") && responseBody.contains("errorCode") && appContext.containsProperty("user.uid")){
			try {
				Result res = Result.parse(new ByteArrayInputStream(responseBody.getBytes()));	
				if(res.getErrorCode() == 0){
					appContext.Logout();
					appContext.getUnLoginHandler().sendEmptyMessage(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
        return new ByteArrayInputStream(responseBody.getBytes());*/
        
        
        return responseBody;
	}
	
	/**
	 * 检查版本更新
	 * @param url
	 * @return
	 */
	public static Update checkVersion(AppContext appContext) throws AppException {
		try{
			return Update.parse(http_get(appContext, /*URLs.UPDATE_VERSION*/"http://223.4.144.25:30018/version.xml"));		
		}catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
			throw AppException.network(e);
		}
	}
	
	
	/**
	 * get请求URL
	 * @param url
	 * @throws AppException 
	 */
	private static InputStream http_get(AppContext appContext, String url) throws AppException {	
		//System.out.println("get_url==> "+url);
/*		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);*/
		
		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
		//		httpGet = getHttpGet(url/*, cookie, userAgent*/);
				httpGet = getHttpGet(url, appContext.mCookies, "");
			
				/*httpGet = getHttpGet(url, cookie, userAgent);*/
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				//System.out.println("XMLDATA=====>"+responseBody);
				break;				
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
		
		responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		/*if(responseBody.contains("result") && responseBody.contains("errorCode") && appContext.containsProperty("user.uid")){
			try {
				Result res = Result.parse(new ByteArrayInputStream(responseBody.getBytes()));	
				if(res.getErrorCode() == 0){
					appContext.Logout();
					appContext.getUnLoginHandler().sendEmptyMessage(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}*/
		return new ByteArrayInputStream(responseBody.getBytes());
	}


	public static Boolean uploadBeeSource(AppContext appContext,
			ArrayList<BeeSource> beelist, String url, String phone,String userId) throws AppException {
		// TODO Auto-generated method stub
		
		
		JSONArray data = new JSONArray();
		for(int i=0;i<beelist.size();i++){
			
		    String memo = "SYR%s;DD%s;BH%s;ZL%s;SL%s;GG%s;SLIAO%s;QMKS%s;QMJS%s;SGRQ%s;SGDW%s;ND%s;lat%s;lng%s;CMJG%s;YYQK%s;FZ%s;ORGID%s";
			memo = String.format(memo, beelist.get(i).getA2(), beelist.get(i).getA4(), beelist.get(i).getA1(), beelist.get(i).getA5(), beelist.get(i).getA7(), 
					beelist.get(i).getA8(), beelist.get(i).getA6(), beelist.get(i).getA12(), beelist.get(i).getA13(), beelist.get(i).getA14(), 
				   beelist.get(i).getA15(), beelist.get(i).getA9(), 0, 0, beelist.get(i).getA10(), beelist.get(i).getA11(),beelist.get(i).getA16(),beelist.get(i).getUnitNumber()/*appContext.mOrgList.get(whichUnit).GetID()*//*Constants.unitMap.get(whichUnit)*/);
			
			JSONObject sms = new JSONObject();
			
			try {
				sms.put("b", memo);
				sms.put("m", phone);
				sms.put("u", userId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
			    data.put(sms);
		}
		
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("data", data);
		
		try {
			return http_post(appContext, url, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}


	public static boolean modifyPassword(AppContext appContext, String rawUrl, String userId,
			String new_password,String sessionId) throws AppException{
		// TODO Auto-generated method stub

		
		try {			
			String url = rawUrl + "/modifyPWD.aspx?u=" + userId
					+ "&passwd=" + new_password
					+"&sid="+sessionId;
					
			
			InputStream is = http_get(appContext, url);
			InputStreamReader inReader=new InputStreamReader(is);
			BufferedReader buffer=new BufferedReader(inReader);
			
			String strLine=null;
			String result="";
			while((strLine=buffer.readLine())!=null)
			{
				result+=strLine;
			}
		    if(result.equals("1")){
		    	return true;
		    	
		    }else{
		    	
		    	return false;
		    }
					
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return false;
	
	}
	
		
	//扩展,返回服务端数据
	
	public static int modifyPassword2(AppContext appContext, String rawUrl, String userId,
			String new_password,String sessionId) throws AppException{
		// TODO Auto-generated method stub

		String e_new_password="";
		try {
			e_new_password=MD5.getMD5(new_password, "utf-8");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
			
		try {					
			/*String url = rawUrl + "/modifyPWD.aspx?u=" + userId
					+ "&passwd=" + new_password
					+"&sid="+sessionId;*/
			
			String url = rawUrl + "/modifyPWD.aspx?u=" + userId
					+ "&passwd=" + e_new_password
					+"&sid="+sessionId;
					
			
			InputStream is = http_get(appContext, url);
			InputStreamReader inReader=new InputStreamReader(is);
			BufferedReader buffer=new BufferedReader(inReader);
			
			String strLine=null;
			String result="";
			while((strLine=buffer.readLine())!=null)
			{
				result+=strLine;
			}
			
		    if(result.equals("1")){
		    	return 1;
		    	
		    }else if(result.equals("2")){
		    	
		    	return 2;
		    }else{
		    	
		    	return 0;
		    }
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return 0;
	
	}
	
	
	
	public static boolean modifyPassword2(AppContext appContext, String rawUrl, String userId,
			String new_password) throws AppException{
		// TODO Auto-generated method stub
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("userId", userId);
		
		params.put("passwd", new_password);
		

		
		try {
			
			
			String url = rawUrl + "/modifyPWD.aspx";
						
			return http_post(appContext,url,params,null);
				
		} catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
			throw AppException.network(e);
		}
	}
}
