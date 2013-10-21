package com.bee.app.ui;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.AppException;

import com.bee.app.db.InfoService;
import com.bee.app.utils.MD5;
import com.bee.common.Constants;
import com.bee.common.HttpUtil;
import com.bee.common.StringUtils;
import com.bee.common.UIHelper;
import com.bee.common.WebserviceUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class LoginDialog extends Activity implements View.OnClickListener {

	private ViewSwitcher mViewSwitcher;
	private View loginLoading;
	private AutoCompleteTextView mAccount;
	private EditText mPwd;
	private CheckBox chb_rememberMe;
	private ImageButton btn_close;
	private Button btn_login;
	
	private AnimationDrawable loadingAnimation;
	private SharedPreferences settings;
	private AppContext appContext;
	private InfoService infoService;
	private String rawUrl=null;
	private Button setting_btn;
	private RelativeLayout needloginLayout;
	private RelativeLayout hasloginLayout;
	private TextView accountView;
	private Button setting_btn2;
	private Button loginout_btn;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_dialog);
		
		appContext=(AppContext)this.getApplicationContext();
		initView();
		settings=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
		
		infoService =appContext.getInforService();
		
		userId=settings.getString("uid", "");
		String userName=settings.getString("username", "");

        if(userId!=null&&(!userId.equals(""))){
        	
        	needloginLayout.setVisibility(View.GONE);
        	hasloginLayout.setVisibility(View.VISIBLE);
        	infoService=appContext.getInforService();
        	accountView.setText(userName);
        }
        
        rawUrl=appContext.rawURL;
        
        if (settings.getBoolean("remember", false)) {
        	chb_rememberMe.setChecked(true);
        	mAccount.setText(settings.getString("username", ""));
        	mPwd.setText(settings.getString("password", ""));
        	
		}
             
     // remember checkbox
        chb_rememberMe.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
     			@Override
     			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
     				SharedPreferences.Editor editor = settings.edit();
     				if (chb_rememberMe.isChecked()) {
     					editor.putBoolean("remember", true);
     					editor.putString("username", mAccount.getText().toString());
     					editor.putString("password", mPwd.getText().toString());
     				} else {
     					editor.putBoolean("remember", false);
     					editor.putString("username", "");
     					editor.putString("password", "");
     				}
     				editor.commit();
     			}
     		});         
	}

/*	protected void login(final String account, final String pwd, final boolean isRememberMe) {
		// TODO Auto-generated method stub
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					    SharedPreferences.Editor editor = settings.edit();
					    Bundle bundle=msg.getData();
					    String userId=bundle.getString("userId");
					    
					    String phoneNumber=bundle.getString("phoneNumber");
					    editor.putString("uid",userId);
					    editor.putString("phoneNumber", phoneNumber);
					    editor.commit();
					        
					//    appContext.loginUid=userId;
					//    appContext.mPhoneNumber=phoneNumber;
					    
					    if(userId!=null&&(!userId.equals(""))){
					    	
					    	appContext.setLoginStatus(true);
					    }
					    
					  //添加手机号码,UID,loginName到数据库
					    infoService.updateUserId(userId); 
					    infoService.updateLoginName(mAccount.getText().toString());
					    infoService.updatePhoneNumber(phoneNumber);
					    				    
						UIHelper.ToastMessage(LoginDialog.this, R.string.msg_login_success);
						finish();
				}else if(msg.what == 0){
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					UIHelper.ToastMessage(LoginDialog.this, getString(R.string.msg_login_fail));
				}
			}
		};
			
		new Thread(){

			

			@Override
			public void run() {
				// TODO Auto-generated method stub
					Message msg=new Message();
		            String url = rawUrl+ "/login.aspx?u=" + account + "&p=" + pwd;
		            String userId;
		            String phoneNumber;
		            userId=HttpUtil.queryStringForPost(url);
		   try {
					userId = HttpUtil.login(url);

					Bundle bundle = new Bundle();
				
				if(userId!=null&&(!userId.equals(""))) {
					
					bundle.putString("userId", userId);
					String phoneGetUrl = rawUrl
							+ "/query.aspx?u=" + userId;
					phoneNumber=HttpUtil.queryStringForPost(phoneGetUrl);
					if(phoneNumber!=null&&(!phoneNumber.equals(""))){
						bundle.putString("phoneNumber", phoneNumber);
						String webserviceUrl =rawUrl + "/TypeInfo.asmx";
						try {
							
							appContext.mOrgList=WebserviceUtil.getORG("7b77c756-bad9-441b-9904-7076973eb68c",phoneNumber, webserviceUrl);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
	            	msg.what=1;
	            	msg.setData(bundle);
	            }else{
	            	msg.what=0;
	            }  
				
				} catch (AppException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				handler.sendMessage(msg);
			}	
		}.start();
	}*/
	
	
	
	protected void login(final String account, final String pwd, final boolean isRememberMe) {
		// TODO Auto-generated method stub
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					    SharedPreferences.Editor editor = settings.edit();
					    Bundle bundle=msg.getData();
					    String userId=bundle.getString("userId");
					    
					    String phoneNumber=bundle.getString("phoneNumber");
					    editor.putString("uid",userId);
					    editor.putString("phoneNumber", phoneNumber);
					    editor.commit();
					        
					//    appContext.loginUid=userId;
					//    appContext.mPhoneNumber=phoneNumber;
					    
					    if(userId!=null&&(!userId.equals(""))){					    	
					    	appContext.setLoginStatus(true);
					    }
					    
					  //添加手机号码,UID,loginName到数据库
					    infoService.updateUserId(userId); 
					    infoService.updateLoginName(mAccount.getText().toString());
					    infoService.updatePhoneNumber(phoneNumber);
					    				    
						UIHelper.ToastMessage(LoginDialog.this, R.string.msg_login_success);
						finish();
				}else if(msg.what == 0){
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					UIHelper.ToastMessage(LoginDialog.this, getString(R.string.msg_login_fail));
				}else if(msg.what == -1){
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					((AppException)msg.obj).makeToast(LoginDialog.this);
				}
			}
		};
			
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
					Message msg=new Message();
					String e_pwd="";
					try {
						e_pwd=MD5.getMD5(pwd, "utf-8");
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
		          //  String url = rawUrl+ "/login.aspx?u=" + account + "&p=" + pwd;
					String url = rawUrl+ "/login.aspx?u=" + account + "&p=" + e_pwd;
		            String userId;
		            String phoneNumber;
		          /*  userId=HttpUtil.queryStringForPost(url);*/
		   try {
					userId = HttpUtil.login(url,LoginDialog.this);

					Bundle bundle = new Bundle();
				
				if(userId!=null&&(!userId.equals(""))) {
					
					bundle.putString("userId", userId);
					String phoneGetUrl = rawUrl
							+ "/query.aspx?u=" + userId;
					phoneNumber=HttpUtil.queryStringForPost(phoneGetUrl);
					if(phoneNumber!=null&&(!phoneNumber.equals(""))){
						bundle.putString("phoneNumber", phoneNumber);
						String webserviceUrl =rawUrl + "/TypeInfo.asmx";
						try {
							
							appContext.mOrgList=WebserviceUtil.getORG("7b77c756-bad9-441b-9904-7076973eb68c",phoneNumber, webserviceUrl);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
	            	msg.what=1;
	            	msg.setData(bundle);
	            }else{
	            	msg.what=0;
	            }  
				
				} catch (AppException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
			    	msg.what = -1;
			    	msg.obj = e1;
					
				}
				handler.sendMessage(msg);
			}	
		}.start();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){	
		    case R.id.setting_btn :
		    case R.id.setting_btn2:
		    	
		    	Intent mIntent = new Intent(this,SystemSettingActivity.class);
				mIntent.setAction("android.intent.action.VIEW");

			if (appContext.rawURL != null) {

				Uri content_url = Uri.parse(appContext.rawURL);
				mIntent.setData(content_url);
				startActivity(mIntent);
			}
			break;
			
		    case R.id.login_btn_login:
		    	
				// TODO Auto-generated method stub
		    	
		    	try{
				String account = mAccount.getText().toString();
				String pwd = mPwd.getText().toString();
				boolean isRememberMe = chb_rememberMe.isChecked(); //判断输入
				if(StringUtils.isEmpty(account)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_login_email_null));
					return;
				}
				if(StringUtils.isEmpty(pwd)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_login_pwd_null));
					return;
				}
				
				 
				 if(isRememberMe){
			        	SharedPreferences.Editor editor = settings.edit();
			        	editor.putBoolean("remember", true);
	 					editor.putString("username", mAccount.getText().toString());
	 					editor.putString("password", mPwd.getText().toString());
	 					editor.commit();
			        	
			        }
				 
				 if (!appContext.isNetworkConnected()) {
						Toast.makeText(this, "无法联网，请检查网络状态", Toast.LENGTH_SHORT).show();
						return;
					}
				
				btn_close.setVisibility(View.GONE);
		        loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
		        loadingAnimation.start();
		        mViewSwitcher.showNext();
		            
		        
		        login(account, pwd, isRememberMe);
		        
		    	}catch(Exception e){
		    		
		    		Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT);
		    	}
		        			
			    break;
			    
		    case R.id.loginout_btn:
		    	
		    	needloginLayout.setVisibility(View.VISIBLE);
	        	hasloginLayout.setVisibility(View.GONE);
		    	Editor editor=settings.edit();
		    	editor.putString("phoneNumber", "");
		    	editor.putString("uid", "");
		    	editor.commit();
		    	break; 		
		}
		
	}
	
	public void initView() {

		mViewSwitcher = (ViewSwitcher) findViewById(R.id.logindialog_view_switcher);
		loginLoading = (View) findViewById(R.id.login_loading);
		mAccount = (AutoCompleteTextView) findViewById(R.id.login_account);
		mPwd = (EditText) findViewById(R.id.login_password);
		chb_rememberMe = (CheckBox) findViewById(R.id.login_checkbox_rememberMe);
		needloginLayout=(RelativeLayout)this.findViewById(R.id.needlogin);
    /*    hasloginLayout=(RelativeLayout)this.findViewById(R.id.haslogin);*/
        accountView=(TextView)this.findViewById(R.id.account);
		/*setting_btn = (Button) findViewById(R.id.setting_btn);
		setting_btn.setOnClickListener(this);*/
		
		/*setting_btn2 = (Button) findViewById(R.id.setting_btn2);
		setting_btn2.setOnClickListener(this);*/
		
		btn_close = (ImageButton)findViewById(R.id.login_close_button);
        btn_close.setOnClickListener(UIHelper.finish(this)); 
		
        btn_login = (Button)findViewById(R.id.login_btn_login);
        btn_login.setOnClickListener(this);
        
      /*  loginout_btn=(Button)findViewById(R.id.loginout_btn);
        loginout_btn.setOnClickListener(this);*/
		
	}
}
