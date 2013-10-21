package com.bee.app.ui;

import com.bee.R;
import com.bee.R.layout;

import com.bee.app.AppContext;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

public class Main2_bak extends Activity implements OnClickListener {

	private static final String TAG = "chinabee";
	private FrameLayout picMgr;
	private FrameLayout beeMgr;
	private FrameLayout positionMgr;
	private FrameLayout refreshMgr;
	private Intent mIntent;
	private AppContext appContext;


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
			Toast.makeText(this, "后台更新中，请稍后刷新", Toast.LENGTH_SHORT).show();
			appContext.asyncFresh(appContext);
        	
        	break;
        

		}
		
	}


}
