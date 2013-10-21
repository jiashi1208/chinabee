package com.bee.app.ui;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.db.InfoService;
import com.bee.common.WebserviceUtil;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;


public class SplashActivity extends Activity {
	
	protected BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {

	           if("finish".equals(intent.getAction())) {

	              Log.e("#########", "I am " + getLocalClassName()

	                     + ",now finishing myself...");
	              finish();
	       }
	    }
	};

	private AppContext app;

	private ArrayList<CItem> materialTypeList;

	private ArrayList<CItem> guiGeList;

	private ArrayList<CItem> beeTypeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		final View view=View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);
		AlphaAnimation animation=new AlphaAnimation(0.3f,1.0f);
		animation.setDuration(2000);
		view.setAnimation(animation);
		app=(AppContext)this.getApplicationContext();
			
		animation.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationStart(Animation animation) {}			
		});
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("finish");
		registerReceiver(mFinishReceiver, filter);		
	}

	protected void redirectTo() {
		// TODO Auto-generated method stub
		Intent mIntent=new Intent(SplashActivity.this,MainPage2.class);
	    startActivity(mIntent);
		finish();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	

}
