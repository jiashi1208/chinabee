package com.bee.network;

public class NetworkOperate {
	
	   /* public String doGet(String url, Map params) {

    建立HTTPGet对象 

   String paramStr = "";

   Iterator iter = params.entrySet().iterator();
   while (iter.hasNext()) {
       Map.Entry entry = (Map.Entry) iter.next();
       Object key = entry.getKey();
       Object val = entry.getValue();
       paramStr += paramStr = "&" + key + "=" + val;
   }

   if (!paramStr.equals("")) {
       paramStr = paramStr.replaceFirst("&", "?");
       url += paramStr;
   }
   HttpGet httpRequest = new HttpGet(url);

   String strResult = "doGetError";

   try {

        发送请求并等待响应 
       HttpResponse httpResponse = httpClient.execute(httpRequest);
        若状态码为200 ok 
       if (httpResponse.getStatusLine().getStatusCode() == 200) {
            读返回数据 
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

public String doPost(String url, List<NameValuePair> params) {

    建立HTTPPost对象 
   HttpPost httpRequest = new HttpPost(url);
   System.out.println("url:"+url);
   String strResult = "doPostError";

   try {
        添加请求参数到请求对象 
       httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        发送请求并等待响应 
       HttpResponse httpResponse = httpClient.execute(httpRequest);
        若状态码为200 ok 
       if (httpResponse.getStatusLine().getStatusCode() == 200) {
            读返回数据 
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

public HttpClient getHttpClient() {

   // 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）

   this.httpParams = new BasicHttpParams();

   // 设置连接超时和 Socket 超时，以及 Socket 缓存大小

   HttpConnectionParams.setConnectionTimeout(httpParams, 120 * 1000);

   HttpConnectionParams.setSoTimeout(httpParams, 120 * 1000);

   HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

   // 设置重定向，缺省为 true

   HttpClientParams.setRedirecting(httpParams, true);

   // 设置 user agent

   String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
   HttpProtocolParams.setUserAgent(httpParams, userAgent);

   // 创建一个 HttpClient 实例

   // 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient

   // 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient

   httpClient = new DefaultHttpClient(httpParams);

   return httpClient;
}
*/

}
