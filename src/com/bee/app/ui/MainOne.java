package com.bee.app.ui;

import java.util.ArrayList;

import org.json.JSONObject;

import com.bee.R;
import com.bee.R.layout;

import com.bee.app.AppContext;
import com.bee.app.AppException;
import com.bee.app.bean.BeeSource;
import com.bee.app.db.InfoService;
import com.bee.common.Constants;
import com.bee.common.UIHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainOne extends Activity  implements View.OnClickListener{

	private static final String TAG = "chinabee";
	private TextView beeNumberView;
	private TextView picNumberView;
	private TextView pointNumberView;
	private AppContext appContext;
	private InfoService infoService;
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
	private Button btn_quick;
	private Button to_main_btn;
	private Intent mIntent;
	private Button refreshButton;
	private int whichUnit;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG,"MainOne onCreate ");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_one);
		
		initView();
		
		appContext=(AppContext)this.getApplication();
		infoService=appContext.getInforService();
		
		
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

	private void initView() {
		// TODO Auto-generated method stub
		beeNumberView=(TextView)this.findViewById(R.id.beeNumber);
		picNumberView=(TextView)this.findViewById(R.id.picNumber);
		pointNumberView=(TextView)this.findViewById(R.id.pointNumber);
		
	//	refreshButton=(Button)findViewById(R.id.refreshbtn);
		
	//	refreshButton.setOnClickListener(this);
		
		btn_quick=(Button)findViewById(R.id.btn_quick);
		to_main_btn=(Button)findViewById(R.id.to_main);

		btn_quick.setOnClickListener(this);
		to_main_btn.setOnClickListener(this);
		
		
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();	
		
		Log.d(TAG,"MainOne onResume ");
		
		Log.e(TAG,"MainOne onResume********************** ");
		
		/*refreshDialog=ProgressDialog.show(this, "刷新", "正在读取信息。。。");
		
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
		*/
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {

		case R.id.to_main:
			/*mIntent = new Intent(this, LoginDialog.class);*/
			mIntent = new Intent(this, Setting.class);
			startActivity(mIntent);
			break;

		case R.id.btn_quick:
			
			if (!appContext.isNetworkConnected()) {
				UIHelper.showNetworkSetting(this);
				break;
			}
			
			if(!appContext.isLogin()){
				
				Intent intent = new Intent(this, LoginDialog.class);
				startActivity(intent);
				
				break;
			}
			
			showUnitDialog();
			break;	
			
			
	 /*  case R.id.refreshbtn:
			
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
			
			break;*/
		}
		
	
		
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setCancelable(true).setTitle("提示信息")
					.setMessage("您确定要退出应用吗？");
			DialogInterface.OnClickListener l = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == DialogInterface.BUTTON_POSITIVE) {
						/* quit(); */
						getApplicationContext().sendBroadcast(
								new Intent("finish"));
						MainOne.this.finish();
					} else if (which == DialogInterface.BUTTON_NEGATIVE) {
						dialog.dismiss();
					}
				}
			};
			builder.setPositiveButton("退出", l);
			builder.setNegativeButton("取消", l);
			builder.show();
		}
		return false;
	}
	
	public void showUnitDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请选择上传单位");
		
		if(appContext.mOrgList==null){
			
			Toast.makeText(this, "请更新上传单位参数", Toast.LENGTH_SHORT).show();	
			
			return;
			
		}
		
		ArrayAdapter<CItem> mOrgAdapter = new ArrayAdapter<CItem>(this, android.R.layout.simple_dropdown_item_1line, appContext.mOrgList);
		
		builder.setAdapter(mOrgAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						appContext.orgId_choosed=appContext.mOrgList.get(which).GetID();
						appContext.orgValue_choosed=appContext.mOrgList.get(which).GetValue();

						mIntent = new Intent();
						mIntent.setAction(Constants.QUICK_BEE_ADD);
						startActivity(mIntent);
						
					}
				});
		builder.show();	
	}

}
