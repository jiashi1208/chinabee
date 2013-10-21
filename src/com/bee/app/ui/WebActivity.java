package com.bee.app.ui;

import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.db.InfoService;
import com.bee.base.BaseUi;
import com.bee.common.Constants;
import com.bee.common.HttpUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-21
 * Time: 上午9:02
 * To change this template use File | Settings | File Templates.
 */
public class WebActivity extends BaseUi {
    private WebView webView;
//    private static final String URL = "http://www.google.com";
	private SharedPreferences settings;
	private AppContext appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 取消标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 进行全屏

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.map);
        
        appContext=(AppContext)getApplication();
        
        if(appContext.rawURL==null||appContext.equals("")){
        	Toast.makeText(this, "尚未设置服务器网络。", Toast.LENGTH_SHORT).show();
        	return;
        }
        
        // 实例化WebView
        webView = (WebView) this.findViewById(R.id.wv_oauth);
        /**
         * 调用loadUrl()方法进行加载内容
         */
        Bundle extras = this.getIntent().getExtras();
        String url="";
        InfoService infoService=new InfoService(this);
        SystemInfo systemInfo=infoService.getSystemInfo();
        if(systemInfo!=null){
            url=appContext.rawURL +"/map.aspx";
            if (extras != null) {
                url+= "?lat="+ extras.getString("lat");
                url+= "&lng="+ extras.getString("lng");
            }
            webView.loadUrl(url);
        }

        System.out.println("web view url:"+url);

        /**
         * 设置WebView的属性，此时可以去执行JavaScript脚本
         */
        webView.getSettings().setJavaScriptEnabled(true);
    }

}
