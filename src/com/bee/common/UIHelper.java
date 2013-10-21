package com.bee.common;

import com.bee.app.AppContext;
import com.bee.app.ui.LoginDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

// 应用程序UI工具包：封装UI相关的一些操作
public class UIHelper {
		
	public static void ToastMessage(Context cont, int msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	
	
	public static void ToastMessage(Context cont, String msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 用户登录或注销
	 * 
	 * @param activity
	 */
	public static void loginOrLogout(Activity activity) {
		AppContext ac = (AppContext) activity.getApplication();
		if (ac.isLogin()) {
			ToastMessage(activity, "已退出登录");
		} else {
			showLoginDialog(activity);
		}
	}
	
	/**
	 * 显示登录页面
	 * 
	 * @param activity
	 */
	public static void showLoginDialog(Context context) {
		Intent intent = new Intent(context, LoginDialog.class);
		/*if (context instanceof Main)
			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_MAIN);
		else if (context instanceof Setting)
			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_SETTING);
		else
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
		context.startActivity(intent);
	}


	/**
	 * 点击返回监听事件
	 * 
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}
	
	/**
	 * 显示设置网络界面
	 * 
	 * @param activity
	 * @return
	 */
	public static void showNetworkSetting(final Context context) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context).
                setIcon(android.R.drawable.ic_dialog_alert).setCancelable(true).setTitle("网络状态").
                setMessage("本应用需要使用网络，请设置您的网络。");
        DialogInterface.OnClickListener l = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE) {
              
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);

                    context.startActivity(intent);
                  /*  finish();*/
                }else if(which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog.dismiss();
                }
            }};
        builder.setPositiveButton("设置", l);
        builder.setNegativeButton(android.R.string.cancel, l);
        builder.show();
	}


}
