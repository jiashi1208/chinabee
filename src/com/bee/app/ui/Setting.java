package com.bee.app.ui;

import com.bee.R;
import com.bee.R.layout;
import com.bee.R.menu;
import com.bee.app.AppContext;
import com.bee.common.Constants;
import com.bee.common.UpdateManager;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class Setting extends PreferenceActivity {

	private Preference mynetwork;
	private Preference account;
	private Preference update;
	private AppContext appContext;
	private SharedPreferences settings;
	private String userId;
	private Preference myPassWD;
	private String curVersionName;
	private int curVersionCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		
		ListView localListView = getListView();
		localListView.setBackgroundColor(0);
		localListView.setCacheColorHint(0);
		((ViewGroup) localListView.getParent()).removeView(localListView);
		ViewGroup localViewGroup = (ViewGroup) getLayoutInflater().inflate(
				R.layout.setting, null);
		((ViewGroup) localViewGroup.findViewById(R.id.setting_content))
				.addView(localListView, -1, -1);

		setContentView(localViewGroup);
		
		appContext=(AppContext)getApplication();
		settings=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
		
		mynetwork = (Preference) findPreference("network");
		mynetwork.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(Setting.this, SystemSettingActivity.class);
				Setting.this.startActivity(intent);
				return true;
			}
		});
		
		
		
		myPassWD = (Preference) findPreference("passwd");
		myPassWD.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(Setting.this, PasswordModifyActivity.class);
				Setting.this.startActivity(intent);
				return true;
			}
		});
		
		
		
		
		
		account = (Preference) findPreference("account");
		account.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				
				if(!appContext.isLogin()){
					
					Intent intent = new Intent(Setting.this, LoginDialog.class);
					Setting.this.startActivity(intent);
				}else{
					
					AlertDialog.Builder builder=new AlertDialog.Builder(Setting.this);
					builder.setMessage("是否注销？");
					builder.setPositiveButton("确定", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							Toast.makeText(Setting.this, "已经注销登录", Toast.LENGTH_SHORT).show();
							SharedPreferences.Editor editor = settings.edit();
		 					editor.putString("uid", "");	 					
		 					editor.commit();
		 					account.setTitle("用户登录"); 					
		 					account.setSummary("");
		 					myPassWD.setEnabled(false);
		 					appContext.mOrgList=null;
						}
					});
					builder.setNegativeButton("取消", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					} );
					
					builder.create().show();	
				}
				return true;
			}
		});
		
		
		
		// 版本更新
		update = (Preference) findPreference("update");
		update.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UpdateManager.getUpdateManager().checkAppUpdate(Setting.this,
						true);
				return true;
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
			curVersionName = info.versionName;
	    	curVersionCode = info.versionCode;
	    	update.setSummary("当前版本为："+curVersionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		
		if(!appContext.isLogin()){
			account.setTitle("用户登录");
			
			myPassWD.setEnabled(false);
     	 
        }else{      	
        	account.setTitle("注销登录");
        	
        	String userName=settings.getString("username", "");
        	account.setSummary("当前登录用户:"+userName);
        	myPassWD.setEnabled(true);
        }	
	}
	
	public void back(View paramView) {
		finish();
	}

}
