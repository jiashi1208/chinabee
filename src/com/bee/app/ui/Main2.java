package com.bee.app.ui;

import com.bee.R;
import com.bee.R.layout;

import com.bee.app.AppContext;
import com.bee.common.Constants;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main2 extends Activity implements OnClickListener {

	private static final String TAG = "chinabee";
	private FrameLayout picMgr;
	private FrameLayout beeMgr;
	private FrameLayout positionMgr;
	private FrameLayout refreshMgr;
	private Intent mIntent;
	private AppContext appContext;
	
	
	
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			
			
			case Constants.REFRESH_SUCCESS:

				mProgressDialog.dismiss();
				
				/*Toast.makeText(Main2.this, "刷新完成！", Toast.LENGTH_SHORT).show();*/
				
				showToast("刷新完成！");

				break;
				
			}
			
		}
		
		
		
	};
	private ProgressDialog mProgressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG,"Main2 onCreate ");
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main2);
		
		appContext=(AppContext)getApplication();
		initView();
	}
	
	
	public void initView(){
		
		picMgr=	(FrameLayout)this.findViewById(R.id.picmgr);
		beeMgr=	(FrameLayout)this.findViewById(R.id.beemgr);
		positionMgr=	(FrameLayout)this.findViewById(R.id.positionmgr);
		refreshMgr=	(FrameLayout)this.findViewById(R.id.refreshmgr);
		
		picMgr.setOnClickListener(this);
		beeMgr.setOnClickListener(this);
		positionMgr.setOnClickListener(this);
		refreshMgr.setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.beemgr:
			
			mIntent = new Intent(this, InfoListActivity.class);
			startActivity(mIntent);
			break;
			
        case R.id.picmgr:
        	
        	mIntent = new Intent(this, PicListCusorActivity.class);
			startActivity(mIntent);
			break;
			
        case R.id.positionmgr:
        	
        	mIntent = new Intent(this, PointListActivity2.class);
			startActivity(mIntent);
			
			break;
			
        case R.id.refreshmgr:
        	
        	if (!appContext.isNetworkConnected()) {
				Toast.makeText(this, "无法联网，暂时不能更新。", Toast.LENGTH_SHORT).show();
				return;
			}		
			/*Toast.makeText(this, "后台更新中，请稍后刷新", Toast.LENGTH_SHORT).show();*/
        	
        	LayoutInflater inflater = getLayoutInflater();
        	View layout = inflater.inflate(R.layout.layout_toast,null
        	                               /*(ViewGroup) findViewById(R.id.toast_layout_root)*/);

        	/*TextView text = (TextView) layout.findViewById(R.id.message);
        	text.setText("正在刷新,请稍等。");

        	Toast toast = new Toast(getApplicationContext());
        	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        	toast.setDuration(Toast.LENGTH_LONG);
        	toast.setView(layout);
        	toast.show();*/
        	
        	mProgressDialog=new ProgressDialog(this);
        	mProgressDialog.setMessage("正在刷新中...");
        	
        	mProgressDialog.setProgressStyle(RESULT_OK);
        	
        	mProgressDialog.show();
        	
        	new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					appContext.asyncFresh();
							
					mHandler.sendEmptyMessage(Constants.REFRESH_SUCCESS);
					
									
				}
        			
        	}.start();
        	 	
			
        	
        	break;
        

		}
		
	}
	
	
	public void showToast(String message){
		
		LayoutInflater inflater = getLayoutInflater();
    	View layout = inflater.inflate(R.layout.layout_toast,null);

    	TextView text = (TextView) layout.findViewById(R.id.message);
    	text.setText(message);

    	Toast toast = new Toast(getApplicationContext());
    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    	toast.setDuration(Toast.LENGTH_SHORT);
    	toast.setView(layout);
    	toast.show();	
	}


}
