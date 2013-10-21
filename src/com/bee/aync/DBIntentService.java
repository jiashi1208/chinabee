package com.bee.aync;

import com.bee.app.db.InfoService;

import android.app.IntentService;
import android.content.Intent;

public class DBIntentService extends IntentService {

	private InfoService infoService;

	public DBIntentService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
		infoService=new InfoService(this);

	}

}
