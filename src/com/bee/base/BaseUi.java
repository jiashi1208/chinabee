package com.bee.base;

import org.json.JSONObject;

import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.db.InfoService;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class BaseUi extends Activity {
	
	protected boolean showLoadBar = false;
	protected BaseTaskPool taskPool;
	protected BaseHandler handler;	
	protected AppContext app;

	
	
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
	private ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// async task handler
		this.handler = new BaseHandler(this);
		// init task pool
		this.taskPool = new BaseTaskPool(this);

		// init application
		this.app = (AppContext) this.getApplicationContext();

		IntentFilter filter = new IntentFilter();

		filter.addAction("finish");

		registerReceiver(mFinishReceiver, filter);
	}	
	
	public void showLoadBar () {
		this.findViewById(R.id.main_load_bar).setVisibility(View.VISIBLE);
		this.findViewById(R.id.main_load_bar).bringToFront();
		showLoadBar = true;
	}
	
	public void hideLoadBar () {
		if (showLoadBar) {
			this.findViewById(R.id.main_load_bar).setVisibility(View.GONE);
			showLoadBar = false;
		}
	}
	
	
	public void doTaskAsync (int taskId, String taskUrl) {
		//showLoadBar();
		showProgressDialog(taskId);
		taskPool.addTask(taskId, taskUrl, new BaseTask(){
			@Override
			public void onComplete (String httpResult) {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
			}
			@Override
			public void onError (String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}			
/*			@Override
			public void onComplete ( ) {
			//	sendMessage(BaseTask.TASK_COMPLETE, this.getId(), "");
			}*/
		}, 0);
	}
	
	public Context getContext () {
		return this;
	}
	
	public void sendMessage (int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}
	
	public void sendMessage (int what, String data) {
		Bundle b = new Bundle();
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}
	
	public void sendMessage (int what, int taskId, String data) {
		Bundle b = new Bundle();
		b.putInt("task", taskId);
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}
	
	public void onNetworkError (int taskId) {
		toast(C.err.network);
	}
	
	public void toast (String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
    public void onTaskComplete (int taskId, BaseMessage message) {
		
	}
    
	public void onTaskComplete (int taskId) {
		
	}
	
	public InfoService getInfoService(){
		
		
		
		return app.getInforService();
		
	}
	
	public BaseHandler getHandler(){
		return this.handler;
	}
	
	public void showConfirmDialog(String msg){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(
				android.R.drawable.ic_dialog_alert).setTitle("提示信息");
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
		builder.show();	
	}
	
	public void showProgressDialog(int TaskId){
		String msg="请等待";
		mProgressDialog=new ProgressDialog(this,AlertDialog.THEME_HOLO_LIGHT);
		if(TaskId==1002){
			msg="正在登陆，请等待";
		}
	    mProgressDialog.setMessage(msg);
		mProgressDialog.show();	
	}
	
	public void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	public void onTaskComplete() {
		// TODO Auto-generated method stub
		
	}
		
}
