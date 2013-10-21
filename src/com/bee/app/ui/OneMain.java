package com.bee.app.ui;

import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.db.InfoService;
import com.bee.common.Constants;
import com.bee.common.UIHelper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OneMain extends Fragment implements View.OnClickListener {
	
	private TextView beeNumberView;
	private TextView picNumberView;
	private TextView pointNumberView;
	private Button refreshButton;
	private Button btn_quick;
	private Button to_main_btn;
	private Intent mIntent;
	private FragmentActivity mainPage;
	private AppContext appContext;
	private ProgressDialog refreshDialog;
	private InfoService infoService;
	
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
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mainPage=getActivity();
		appContext=(AppContext)mainPage.getApplicationContext();
		infoService=appContext.getInforService();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View v=inflater.inflate(R.layout.main_one, container, false);
		
		initView(v);
		
		return  v;
    }

	private void initView(View v) {
		// TODO Auto-generated method stub
		beeNumberView=(TextView)v.findViewById(R.id.beeNumber);
		picNumberView=(TextView)v.findViewById(R.id.picNumber);
		pointNumberView=(TextView)v.findViewById(R.id.pointNumber);
		
	//	refreshButton=(Button)v.findViewById(R.id.refreshbtn);
		
	//	refreshButton.setOnClickListener(this);
		
		btn_quick=(Button)v.findViewById(R.id.btn_quick);
		to_main_btn=(Button)v.findViewById(R.id.to_main);

		btn_quick.setOnClickListener(this);
		to_main_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		
		switch (v.getId()) {

		case R.id.to_main:
			/*mIntent = new Intent(this, LoginDialog.class);*/
			mIntent = new Intent(getActivity(), Setting.class);
			startActivity(mIntent);
			break;

	    case R.id.btn_quick:
			
			if (!appContext.isNetworkConnected()) {
				UIHelper.showNetworkSetting(mainPage);
				break;
			}
			
           if(!appContext.isLogin()){
				
				Intent mIntent = new Intent(getActivity(), LoginDialog.class);
				startActivity(mIntent);
				
				break;
			}
			
			showUnitDialog();
			break;	
			
			
/*	   case R.id.refreshbtn:
			
		   refreshDialog=ProgressDialog.show(mainPage, "刷新", "正在读取信息。。。");
			
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

	
	public void showUnitDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mainPage);
		builder.setTitle("请选择上传单位");
		
		if(appContext.mOrgList==null){
			
			Toast.makeText(mainPage, "请更新上传单位参数", Toast.LENGTH_SHORT).show();	
			
			return;
			
		}
		
		ArrayAdapter<CItem> mOrgAdapter = new ArrayAdapter<CItem>(mainPage, android.R.layout.simple_dropdown_item_1line, appContext.mOrgList);
		
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		refreshDialog=ProgressDialog.show(mainPage, "刷新", "正在读取信息。。。");
		
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
	
	

}
