package com.bee.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import com.android.internal.util.*;
import com.bee.app.AppContext;
import com.bee.app.AppException;
import com.bee.app.api.ApiClient;
import com.bee.app.ui.XmlUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

public class HttpUtil {
	
	static public int WAP_INT = 1;
	static public int NET_INT = 2;
	static public int WIFI_INT = 3;
	static public int NONET_INT = 4;
	
	static private Uri APN_URI = null;

    public static String changeUrl(String _url){
        if(_url!=null&&_url.trim()!=""){
            if(_url.endsWith("/")){
                _url=_url.substring(0,_url.length()-1)+":30018/";
            }
            else{
                _url+=":30018";
            }
        }
        return _url;
    }

    public static HttpGet getHttpGet(String url){
        HttpGet request = new HttpGet(url);
        return request;
    }

    public static HttpPost getHttpPost(String url){
        Log.d("url", url);
        HttpPost request = new HttpPost(url);
        return request;
    }

    public static HttpResponse getHttpResponse(HttpGet request)throws ClientProtocolException,IOException{
        HttpResponse response = new DefaultHttpClient().execute(request);
        return response;
    }

    public static HttpResponse getHttpResponse(HttpPost request)throws ClientProtocolException,IOException{
        HttpResponse response = new DefaultHttpClient().execute(request);
        return response;
    }

    /**
     * query by normal value params
     * @param url
     * @return
     */
    public static String queryStringForPost(String url){
        if(url.indexOf("http://")<0){
            url="http://"+url;
        }
        
        String result=null;
        try{
        	HttpPost httpPost = new HttpPost(url);
            result = null;
            HttpResponse response = HttpUtil.getHttpResponse(httpPost); //获取登陆信息。
            
           /* org.apache.commons.httpclient.HttpClient mHttpClient = ApiClient.getHttpClient();*/

            if(response.getStatusLine().getStatusCode() == 200){
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
            result = "";
            return result;
        }catch(IOException e){
            e.printStackTrace();
            result = "";
            return result;
        }
        return null;
    }
    
    
    public static String login(String url,Context mContext) throws AppException{
        if(url.indexOf("http://")<0){
            url="http://"+url;
        }
        
        String result=null;
        org.apache.commons.httpclient.HttpClient mHttpClient=null;
        String responseBody = "";
      
        
        GetMethod mHttpGet=null;
        
        try{
        	
            result = null;
                  
            mHttpClient = ApiClient.getHttpClient();
            
            mHttpGet = ApiClient.getHttpGet(url);
            
            
            int statusCode = mHttpClient.executeMethod(mHttpGet);
            
            if(statusCode != HttpStatus.SC_OK) 
	        {
	        	throw AppException.http(statusCode);
	        	
	        }else if(statusCode == HttpStatus.SC_OK){
	        	
	        	responseBody = mHttpGet.getResponseBodyAsString();
	        	
	        	if(responseBody.equals("-1")){
	        		
	        		return "";
	        		
	        	}else{
	        		
	        		Header mSessionResponseHeader = mHttpGet.getResponseHeader("Content-Type");
		        	
		        	String mCookieResponseHeader = mHttpGet.getResponseHeader("Set-Cookie").getValue();
		        //	String mCookieResponseHeader = mHttpGet.getResponseHeader("Cookie").getValue();
		        	
		        	String SessionId = mCookieResponseHeader.substring(mCookieResponseHeader.indexOf("NET_SessionId=") + "NET_SessionId=".length(), mCookieResponseHeader.indexOf(";"));  
		            System.out.println(SessionId);  
		            
		            AppContext appContext = (AppContext)mContext.getApplicationContext();
		            appContext.mSessionId=SessionId;
		            
		            //sj
		            
		          //获得登陆后的 Cookie
		            Cookie[] cookies=mHttpClient.getState().getCookies();
		            String tmpcookies= "";
		            for(Cookie c:cookies){
		                tmpcookies += c.toString()+";";
		            }
		            
		            appContext.mCookies=tmpcookies;
		            
		            //sj
	        	}
	        	
	        
	        	
	        	
	        }

        }catch (HttpException e) {
			/*time++;
			if(time < RETRY_TIME) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {} 
				continue;
			}*/
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			e.printStackTrace();
			throw AppException.http(e);
		} catch (IOException e) {
			/*time++;
			if(time < RETRY_TIME) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {} 
				continue;
			}*/
			// 发生网络异常
			e.printStackTrace();
			throw AppException.network(e);
		} finally {
			// 释放连接
			mHttpGet.releaseConnection();
			httpClient = null;
		}
        
        return responseBody;
    }
    
    
    
    
    
    
    
    

    public static String queryStringForGet(String url){
        HttpGet request = HttpUtil.getHttpGet(url);
        String result = null;
        try{
            HttpResponse response = HttpUtil.getHttpResponse(request);
            if(response.getStatusLine().getStatusCode() == 200){
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
            result = "网络异常！";
            return result;
        }catch(IOException e){
            e.printStackTrace();
            result = "网络异常！";
            return result;
        }
        return null;
    }

    public static String queryStringForPost(HttpPost request){
        String result = null;
        try{
            HttpResponse response = HttpUtil.getHttpResponse(request);
            if(response.getStatusLine().getStatusCode() == 200){
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
            result = "网络异常！";
            return result;
        }catch(IOException e){
            e.printStackTrace();
            result = "网络异常！";
            return result;
        }
        return null;
    }

    public static void getAllXML(String url) throws XmlPullParserException,
            IOException, URISyntaxException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new InputStreamReader(getUrlData(url)));
        XmlUtils.beginDocument(parser, "results");
        int eventType = parser.getEventType();
        do {
            XmlUtils.nextElement(parser);
            parser.next();
            eventType = parser.getEventType();
            if (eventType == XmlPullParser.TEXT) {
                Log.d("test", parser.getText());
                System.out.println(parser.getText());
            }
        } while (eventType != XmlPullParser.END_DOCUMENT);
    }

    public static InputStream getUrlData(String url) throws URISyntaxException,
            ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet method = new HttpGet(new URI(url));
        HttpResponse res = client.execute(method);
        return res.getEntity().getContent();
    }
    
    static public int getNetType (Context ctx) {
		// has network
		ConnectivityManager conn = null;
		try {
			conn = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (conn == null) {
			return HttpUtil.NONET_INT;
		}
		NetworkInfo info = conn.getActiveNetworkInfo();
		boolean available = info.isAvailable();
		if (!available){
			return HttpUtil.NONET_INT;
		}
		// check use wifi
		String type = info.getTypeName();
		if (type.equals("WIFI")) {
			return HttpUtil.WIFI_INT;
		}
		// check use wap
		APN_URI = Uri.parse("content://telephony/carriers/preferapn");
		Cursor uriCursor = ctx.getContentResolver().query(APN_URI, null, null, null, null);
		if (uriCursor != null && uriCursor.moveToFirst()) {
			String proxy = uriCursor.getString(uriCursor.getColumnIndex("proxy"));
			String port = uriCursor.getString(uriCursor.getColumnIndex("port"));
			String apn = uriCursor.getString(uriCursor.getColumnIndex("apn"));
			if (proxy != null && port != null && apn != null && apn.equals("cmwap") && port.equals("80") &&
				(proxy.equals("10.0.0.172") || proxy.equals("010.000.000.172"))) {
				return HttpUtil.WAP_INT;
			}
		}
		return HttpUtil.NET_INT;
	}


	private  static BasicHttpParams httpParams;
	private static DefaultHttpClient httpClient;
    
    
    public static HttpClient getHttpClient() {

        httpParams = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpParams, 120 * 1000);

        HttpConnectionParams.setSoTimeout(httpParams, 120 * 1000);

        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

        HttpClientParams.setRedirecting(httpParams, true);
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
        HttpProtocolParams.setUserAgent(httpParams, userAgent);

        httpClient = new DefaultHttpClient(httpParams);

        return httpClient;
    }
    
    public static String doPost(String url, List<NameValuePair> params) {

        HttpPost httpRequest = new HttpPost(url);
        System.out.println("url:" + url);
        String strResult = "doPostError";

        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(httpResponse.getEntity());

            } else {
                strResult = "Error Response: "
                        + httpResponse.getStatusLine().toString();
            }
        } catch (ClientProtocolException e) {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        } catch (IOException e) {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        } catch (Exception e) {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        }

        Log.v("strResult", strResult);

        return strResult;
    }
    

}
