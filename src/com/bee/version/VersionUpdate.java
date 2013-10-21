package com.bee.version;
/*package com.test2.version;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

import com.test2.HttpUtil;
import com.test2.util.DownLoadManager;
import com.test2.util.UpdataInfoParser;

public class VersionUpdate {
	
	 
	    * 从服务器中下载APK
	    
	    protected void downLoadApk() {
	        final ProgressDialog pd;    //进度条对话框
	        pd = new ProgressDialog(this);
	        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        pd.setMessage("正在下载更新");
	        pd.show();
	        new Thread(){
	            @Override
	            public void run() {
	                try {
	                    File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
	                    sleep(3000);
	                    installApk(file);
	                    pd.dismiss(); //结束掉进度条对话框
	                } catch (Exception e) {
	                    Message msg = new Message();
	                    msg.what = 3;
	                    handler2.sendMessage(msg);
	                    e.printStackTrace();
	                    pd.dismiss(); //结束掉进度条对话框
	                }
	            }}.start();
	    }
	    

	    //安装apk
	    protected void installApk(File file) {
	        Intent intent = new Intent();
	        //执行动作
	        intent.setAction(Intent.ACTION_VIEW);
	        //执行的数据类型
	        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
	        startActivity(intent);
	    }
	    
	    
	      * 获取当前程序的版本号
	      
	    private String getVersionName() throws Exception{
	        //获取packagemanager的实例
	        PackageManager packageManager = getPackageManager();
	        //getPackageName()是你当前类的包名，0代表是获取版本信息
	        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
	        return packInfo.versionName;
	    }
	    
	    public class CheckVersionTask implements Runnable{//验证版本

	        public void run() {
	            try {
	                if(systemInfo!=null){
	                    //从资源文件获取服务器 地址
	                    String path = systemInfo.getUrl();
	                    path=HttpUtil.changeUrl(path)+"/version.xml";
	                    //包装成url的对象
	                    URL url = new URL(path);
	                    HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
	                    conn.setConnectTimeout(5000);
	                    InputStream is =conn.getInputStream();
	                    info =  UpdataInfoParser.getUpdataInfo(is);
	                    String versionname=getVersionName().trim();
	                    if(info.getVersion().equals(versionname)){
	                        Log.i("","版本号相同无需升级");
	    //                    LoginMain();
	                    }else{
	                        Log.i("","版本号不同 ,提示用户升级 ");
	                        Message msg = new Message();
	                        msg.what = 1;
	                        handler2.sendMessage(msg);
	                    }
	                }
	            } catch (Exception e) {
	                // 待处理
	                Message msg = new Message();
	                msg.what = 2;
	                handler2.sendMessage(msg);
	                e.printStackTrace();
	            }
	        }
	    }



}
*/