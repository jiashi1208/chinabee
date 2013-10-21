package com.bee.app.ui;

import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.db.InfoService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;

public class CurrentTaskActivity extends Activity {
	
	private ProgressDialog refreshDialog;
	
	Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			refreshDialog.dismiss();
			
			 Bundle data=msg.getData();
			 String beeNumber=data.getString("bee");	
			 String picNumber=data.getString("pic");
			 String pointNumber=data.getString("point");
			 
			 beeNumberView.setText(beeNumber);
			 picNumberView.setText(picNumber);
			 pointNumberView.setText(pointNumber);
		}
		
		
	};

	private AppContext appContext;

	private InfoService infoService;

	private TextView beeNumberView;

	private TextView picNumberView;

	private TextView pointNumberView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_currentask);
		
		initView();
		
		appContext=(AppContext)this.getApplication();
		infoService=appContext.getInforService();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();	
		refreshDialog=ProgressDialog.show(this, "刷新", "正在读取信息。。。");
		
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
   
				String beeNumber=infoService.getBeeSourceNumber_NoSend();
				String picNumner=infoService.getPicNumber_NoSend();
				String pointNumber=infoService.getUserPointNumber_NoSend();
				
			    Message msg=new Message();
			    Bundle data=new Bundle();
			    data.putString("bee", beeNumber);
			    data.putString("pic", picNumner);
			    data.putString("point", pointNumber);
			    msg.setData(data); 		    
				mHandler.sendMessage(msg);
			}			
		}.start();	
		
	}
	
	
	public void initView(){
		
		beeNumberView=(TextView)this.findViewById(R.id.beeNumber);
		picNumberView=(TextView)this.findViewById(R.id.picNumber);
		pointNumberView=(TextView)this.findViewById(R.id.pointNumber);
		
	}

}
