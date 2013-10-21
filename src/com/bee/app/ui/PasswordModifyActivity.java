package com.bee.app.ui;

import com.bee.R;
import com.bee.R.layout;
import com.bee.R.menu;
import com.bee.app.AppContext;
import com.bee.app.AppException;
import com.bee.common.Constants;
import com.bee.common.UIHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PasswordModifyActivity extends Activity implements View.OnClickListener {

	private EditText passwd_new_view;
	private EditText passwd_now_view;
	private Button button_save;
	private AppContext appContext;
	private SharedPreferences settings;
	private ProgressDialog modify_password_progress;
	private String rawUrl;
	
	private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (!Thread.currentThread().isInterrupted()){
                switch (msg.what){
                    case 1:
                    	
                    	modify_password_progress.dismiss();
                        Toast.makeText(PasswordModifyActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();                     
                        Editor editor=settings.edit();
           			    editor.putString("password", passwd_new_view.getText().toString());
           			    editor.commit(); 
                        
            			PasswordModifyActivity.this.finish();   
                        
                        break;
                    case 0:
                    	modify_password_progress.dismiss();
                    	Toast.makeText(PasswordModifyActivity.this, "密码修改失败！", Toast.LENGTH_SHORT).show();
                    
                        break;
                    case 2:
                    	modify_password_progress.dismiss();
                    	Toast.makeText(PasswordModifyActivity.this, "Session失效，请重新登录！", Toast.LENGTH_SHORT).show();
                    	break;
                    case -1:

                    	modify_password_progress.dismiss();
                    	((AppException)msg.obj).makeToast(PasswordModifyActivity.this);
                        break;

                }
            }
        }
    };
	private ImageButton modify_password_back_btn;
	private EditText passwd_new2_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_password_modify);
		
		appContext=(AppContext)this.getApplicationContext();
		settings=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
		
		rawUrl=settings.getString("url", "");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		passwd_new_view=(EditText)findViewById(R.id.new_password_edittext);
		
		passwd_new2_view=(EditText)findViewById(R.id.new_password2_edittext);
		
		passwd_now_view=(EditText)findViewById(R.id.old_password_edittext);
		
		modify_password_back_btn=(ImageButton)findViewById(R.id.modify_password_back_btn);
		
		button_save=(Button)findViewById(R.id.confirm_btn);
		
		button_save.setOnClickListener(this);	
		
		modify_password_back_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			
		case R.id.confirm_btn:
			
			if (!appContext.isNetworkConnected()) {
				Toast.makeText(PasswordModifyActivity.this, "无法联网，请检查网络状态", Toast.LENGTH_SHORT).show();
				UIHelper.showNetworkSetting(this);
				break;
			}
			
			if(!appContext.isLogin()){
	        	   Intent mIntent=new Intent(this,LoginDialog.class);
	        	   startActivity(mIntent);
	        	   break;
	           }
			
			if((passwd_now_view.getText().toString()=="")){
				Toast.makeText(this, " 请输入当前密码", Toast.LENGTH_SHORT).show();
				break;
			}
			
			if(passwd_new_view.getText().toString()==""){
				Toast.makeText(this, " 请输入新密码", Toast.LENGTH_SHORT).show();
				break;
			}
			
			if(passwd_new2_view.getText().toString()==""){
				Toast.makeText(this, " 请再输入一次新密码", Toast.LENGTH_SHORT).show();
				break;
			}
			
			if(!(passwd_now_view.getText().toString().equals(settings.getString("password", ""))))
			{
				Toast.makeText(this, " 当前密码输入不正确！", Toast.LENGTH_SHORT).show();
				break;
			}
			
			if(!(passwd_new_view.getText().toString().equals(passwd_new2_view.getText().toString())))
			{
				Toast.makeText(this, " 两次输入新密码不一致！", Toast.LENGTH_SHORT).show();
				break;
			}
			
			modify_password_progress=ProgressDialog.show(this,"修改" , "正在修改密码。。。");
			modify_password_progress.setCancelable(true);
			
			/*new Thread() {
				@Override
				
				public void run() {
					// TODO Auto-generated method stub
					Message msg =new Message();
				     try {				    	 
				    	if( appContext.modifyPassword(passwd_new_view.getText().toString())){
				 				 		
							msg.what = 1;
				    		
				    	}else{				    		
							msg.what = 0;				    	 
				    	}
					} catch (AppException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msg.what = -1;
						msg.obj = e;					
					}			     
				    mHandler.sendMessage(msg);
				}
			}.start();*/
			
			new Thread() {
				@Override
				
				public void run() {
					// TODO Auto-generated method stub
					Message msg =new Message();
				     try {	
				    	 
				    	int response=appContext.modifyPassword2(passwd_new_view.getText().toString());
				    	if( response==1){
				 				 		
							msg.what = 1;
				    		
				    	}else if(response==2){
				    		
				    		msg.what = 2;
				    		
				    	}
				    	
				    	else{				    		
							msg.what = 0;				    	 
				    	}
					} catch (AppException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msg.what = -1;
						msg.obj = e;					
					}			     
				    mHandler.sendMessage(msg);
				}
			}.start();
			
			break;	
			
		case R.id.modify_password_back_btn:
			
			finish();
			
			break;
		}
	}

}
