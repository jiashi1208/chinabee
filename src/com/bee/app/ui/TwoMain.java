package com.bee.app.ui;

import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.db.InfoService;
import com.bee.common.Constants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class TwoMain extends Fragment implements View.OnClickListener {
	
	private FragmentActivity mainPage;
	private AppContext appContext;
	private FrameLayout picMgr;
	private FrameLayout beeMgr;
	private FrameLayout positionMgr;
	private FrameLayout refreshMgr;
	private Intent mIntent;
	private ProgressDialog mProgressDialog;
	
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){	
			case Constants.REFRESH_SUCCESS:

				mProgressDialog.dismiss();	
				Toast.makeText(mainPage, "刷新完成！", Toast.LENGTH_SHORT).show();
				break;	
			}
			
		}
	};



	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mainPage=getActivity();
		appContext=(AppContext)mainPage.getApplicationContext();
	}
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
	    View v=inflater.inflate(R.layout.main2, container, false);
	    
	    initView(v);
	
        return v;
    }
	
	
	public void initView(View v) {

		picMgr = (FrameLayout) v.findViewById(R.id.picmgr);
		beeMgr = (FrameLayout) v.findViewById(R.id.beemgr);
		positionMgr = (FrameLayout) v.findViewById(R.id.positionmgr);
		refreshMgr = (FrameLayout) v.findViewById(R.id.refreshmgr);

		picMgr.setOnClickListener(this);
		beeMgr.setOnClickListener(this);
		positionMgr.setOnClickListener(this);
		refreshMgr.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.beemgr:
			
			mIntent = new Intent(mainPage, InfoListActivity.class);
			startActivity(mIntent);
			break;		
        case R.id.picmgr:
        	
        	mIntent = new Intent(mainPage, PicListCusorActivity.class);
			startActivity(mIntent);
			break;
			
        case R.id.positionmgr:
        	
        	mIntent = new Intent(mainPage, PointListActivity2.class);
			startActivity(mIntent);
			
			break;
			
        case R.id.refreshmgr:
        	
        	if (!appContext.isNetworkConnected()) {
				Toast.makeText(mainPage, "无法联网，暂时不能更新。", Toast.LENGTH_SHORT).show();
				return;
			}		
        	
/*        	LayoutInflater inflater = getLayoutInflater();
        	View layout = inflater.inflate(R.layout.layout_toast,null);*/
        	
        	mProgressDialog=new ProgressDialog(mainPage);
        	mProgressDialog.setMessage("正在刷新中...");   	
        	mProgressDialog.setProgressStyle(mainPage.RESULT_OK);
        	
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
	

}
