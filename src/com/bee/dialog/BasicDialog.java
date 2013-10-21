package com.bee.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class BasicDialog {

	private Dialog mDialog;
	private TextView mTextMessage;

	public BasicDialog(Context context, Bundle params) {
		mDialog = new Dialog(context);

		mDialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
		
		Window window = mDialog.getWindow();
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = 0;
		window.setAttributes(wl);
//		window.setGravity(Gravity.CENTER);
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.setLayout(200, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	public void show() {
		mDialog.show();
	}

}
