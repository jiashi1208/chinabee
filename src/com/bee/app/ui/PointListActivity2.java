package com.bee.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.AppException;
import com.bee.app.adapter.ListViewBeeAdapter2;
import com.bee.app.adapter.ListViewPointAdapter;
import com.bee.app.api.ApiClient;
import com.bee.app.bean.BeeSource;
import com.bee.app.bean.PicBean;
import com.bee.app.bean.UserPoint;
import com.bee.app.db.InfoService;
import com.bee.common.Constants;
import com.bee.common.HttpUtil;
import com.bee.common.UIHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PointListActivity2 extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private List<UserPoint> list;
    ArrayList<HashMap<String,String>> list2 = new ArrayList<HashMap<String,String>>();
    
    private ListViewPointAdapter adapter;

    private UserPoint _infoBvo;
    private InfoService infoService;
    private String userId;
  
    private ProgressDialog progressUpload;
    
    private static final int DOWN_OVER = 2;
    
    private static final int UPLOAD_UPDATE = 1;
	private static final String TAG = "chinabee";

	protected int progress;
    
    
	private Handler handler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case UPLOAD_UPDATE:
				
				progress =(int)(((float)mFinishUploadNumber / mUploadTotalNumber) * 100);
				mProgress.setProgress(progress);
				uploadDialog.setTitle("正在上传第"+mCurrentNumber+"条位置信息");
				mProgressText.setText("已上传 "+mFinishUploadNumber+ "/" +mUploadTotalNumber);
				break;
			case DOWN_OVER:
				if (uploadDialog != null) {

					uploadDialog.dismiss();
				}
				Toast.makeText(PointListActivity2.this, "上传完成！", Toast.LENGTH_SHORT).show();
				checkNum=0;
				dataChanged();
				fillData();
				break;
				
			case -1:
				if(uploadDialog!=null){
					
					uploadDialog.dismiss();
				}
				
				((AppException)msg.obj).makeToast(PointListActivity2.this);
				fillData();
				dataChanged();
				break;
			}
			}
    		
    	};
    
	private ProgressDialog mProgressDialog;
	private Button bt_selectall;
	private Button bt_cancel;
	private Button deleteBtn;
	private Button uploadBtn;
	private ListView pointListView;
	
	private int checkNum;
	private HashSet<String> idSet;
	private AsyncTask<HashSet<String>, Integer, Void> deleteWork;
	private LinearLayout pointListLayout;
	private RelativeLayout noInfo;
	private ProgressDialog uploadProgress;
	private int whichUnit;
	private AppContext appContext;
	private CheckBox selectChk;
	private String uerId;
	private TextView addBtn;
	private Intent mIntent;
	private SharedPreferences settings;
	private String rawUrl;
	private UserPoint userPoint;
	private ProgressBar mProgress;
	private TextView mProgressText;
	private AlertDialog uploadDialog;
	protected int mUploadTotalNumber;
	protected int mFinishUploadNumber;
	protected int mCurrentNumber;
	private RelativeLayout rl_pd_btn;
	private RelativeLayout rl_sd_btn;
	private TextView btn_pd;
	private TextView btn_sd;
	private TextView tv_tip_pd;
	private TextView tv_tip_sd;
	public int flowType=0;
	private TextView head_title;
	private TextView tv_show;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_pointlist);
        appContext=(AppContext)this.getApplication();       
        initView();
        //add
		PdOnClickListener localPdOnClickListener = new PdOnClickListener();
		rl_pd_btn.setOnClickListener(localPdOnClickListener);
		SdOnClickListener localSdOnClickListener = new SdOnClickListener();
        rl_sd_btn.setOnClickListener(localSdOnClickListener);
        
        //add
        
        Log.d(TAG,"PointListActivity onCreate");
        
        infoService=appContext.getInforService();
        
        settings=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
		
		rawUrl=settings.getString("url", "");
		
		userId=settings.getString("uid", "");
        
        
        idSet = new HashSet<String>();  //记录被选择的ID
        
        adapter = new ListViewPointAdapter(list2,this);
        pointListView.setAdapter(adapter);
        
        registerForContextMenu(pointListView); 
  
}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.button_add:

			mIntent = new Intent();
			mIntent.setAction(Constants.POINT_ADD);
			startActivityForResult(mIntent, Constants.REQUEST_ADD);
			
		//	startActivity(mIntent);

			break;
		
		case R.id.button_delete:
			
			if (checkNum == 0) {

				Toast.makeText(this, "请选择需要删除的选项。", Toast.LENGTH_SHORT).show();
				return;

			}
			
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setMessage("确定删除吗？");
			builder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					checkNum=0;
					new DeleteWork().execute(idSet);
					
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			} );
			
			builder.create().show();
			break;
			
		case R.id.button_upload:
			
			if(idSet.size()==0) return;
			
			if (!appContext.isNetworkConnected()) {
				Toast.makeText(this, "无法联网，请检查网络状态", Toast.LENGTH_SHORT).show();
				
				UIHelper.showNetworkSetting(this);
				return;
			}
		
			if(!appContext.isLogin()){

	        	   Intent mIntent=new Intent(this,LoginDialog.class);
	        	   startActivity(mIntent);
	        	   return;
	           }
			
			
           if(appContext.mOrgList==null){
				
				Toast.makeText(PointListActivity2.this, "请更新上传单位参数", Toast.LENGTH_SHORT).show();
				break;
			}
			
			showUnitDialog();
			
			/*progressUpload=ProgressDialog.show(PointListActivity2.this, "上传", "正在上传，请等待。。。");
			progressUpload.setCancelable(true);
			
			new uploadWork().execute(idSet);*/
			
		//	showUploadDialog();	
		}		
	}
	
	// 刷新TextView的显示
	private void dataChanged() {/*
		if(flowType==0){
			
			tv_tip_sd.setVisibility(View.GONE);
			uploadBtn.setVisibility(View.VISIBLE);
			
			if(checkNum>0){
				
				tv_tip_pd.setVisibility(View.VISIBLE);
				tv_tip_pd.setText(checkNum+"");
			}else{
				
				tv_tip_pd.setVisibility(View.GONE);
				
			}
		}else if(flowType==1){
			
			tv_tip_pd.setVisibility(View.GONE);
			uploadBtn.setVisibility(View.GONE);
			if (checkNum > 0) {

				tv_tip_sd.setVisibility(View.VISIBLE);
				tv_tip_sd.setText(checkNum+"");
			}else{
				tv_tip_sd.setVisibility(View.GONE);
				
			}	
		}
		
	*/
		//checkNum=0;
		tv_show.setText("已选中" + checkNum + "项");
	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		CheckBox checkbox=(CheckBox)view.findViewById(R.id.checkbox);
		checkbox.toggle();
		if(checkbox.isChecked()){
			checkNum++;
			list2.get(position).put("flag", "true");
			idSet.add(list2.get(position).get("id"));

		}else {
			checkNum--;
			list2.get(position).put("flag", "false");
			idSet.remove(list2.get(position).get("id"));
		}
		dataChanged();	
	}
	
    private void fillData(){
    	checkNum=0;
    	list=null;
        list=infoService.getUserPointList(flowType);
        if(list.size()==0){
        	pointListLayout.setVisibility(View.GONE);
        	noInfo.setVisibility(View.VISIBLE);
        	return;
        }else{
        	
        	pointListLayout.setVisibility(View.VISIBLE);
        	noInfo.setVisibility(View.GONE);
        }
        list2.clear();
        for(int i=0;i<list.size();i++)
        {
            UserPoint userPoint=list.get(i);
            HashMap<String,String> item2 = new HashMap<String,String>();
            item2.put( "date",/*"坐标获取时间: "+*/userPoint.getAddTime());
            item2.put( "pointname","名称: "+userPoint.getName());
            item2.put("flag", "false");
            
            if(userPoint.getStatus().equals(Constants.NO_UPLOAD)){
           	 item2.put("status","未上传");
           	 
           }else if (userPoint.getStatus().equals(Constants.UPLOAD_SUCCESS)) {
           	
           	item2.put("status","已上传给");
           }
            
            item2.put("unit", userPoint.getUnitName());
            item2.put("id", userPoint.getId());
            list2.add(item2);
        }
        adapter.notifyDataSetChanged();
    } 
    
    
    private class DeleteWork extends  AsyncTask<HashSet<String>, Integer, Void>{

	@Override
	protected Void doInBackground(HashSet<String>... params) {
		// TODO Auto-generated method stub
		HashSet<String> idSet=params[0];
		for(String id:idSet){
			infoService.deleteUserPoint((Integer.valueOf(id)));		
		} 
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		checkNum=0;
		fillData();
		dataChanged();
	}		
 
 }
	
    public  void showUnitDialog2(){
		
    	whichUnit=-1;		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("请选择上传单位");
		
		ArrayAdapter<CItem> mOrgAdapter = new ArrayAdapter<CItem>(this, android.R.layout.simple_dropdown_item_1line, appContext.mOrgList);

		builder.setAdapter(mOrgAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				whichUnit = which;
				showUploadDialog();
				
				}
			});

	}
    
    public  void showUnitDialog(){
		
		whichUnit=-1;		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("请选择上传单位");
		
		ArrayAdapter<CItem> mOrgAdapter = new ArrayAdapter<CItem>(this, android.R.layout.simple_dropdown_item_1line, appContext.mOrgList);
		builder.setAdapter(mOrgAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				whichUnit=which;			
				showUploadDialog();
				
			}
		});
	
		
	 builder.create().show();	
	}
    
    
    
	
	public void initView(){
		
		pointListView=(ListView)findViewById(R.id.pointlistview);
        pointListView.setOnItemClickListener(this);
        addBtn=(TextView)this.findViewById(R.id.button_add);
        addBtn.setOnClickListener(this);
        
        tv_show = (TextView) findViewById(R.id.tv);
        
        deleteBtn=(Button)this.findViewById(R.id.button_delete);
        deleteBtn.setOnClickListener(this);
        uploadBtn=(Button)this.findViewById(R.id.button_upload);
        uploadBtn.setOnClickListener(this);
        
        pointListLayout =(LinearLayout)findViewById(R.id.pointlist);
		noInfo=(RelativeLayout)findViewById(R.id.noinfo);
		
		pointListLayout.setVisibility(View.VISIBLE);
    	noInfo.setVisibility(View.GONE);
    	
        head_title=(TextView)findViewById(R.id.pointlist_head_title);
 		
 		head_title.setText("位置列表");
    	
    	
      //区分上传 和未上传
    	this.rl_pd_btn = ((RelativeLayout)findViewById(R.id.order_btn_pd));
        this.rl_sd_btn = ((RelativeLayout)findViewById(R.id.order_btn_sd));
        this.btn_pd = ((TextView)findViewById(R.id.order_tv_pd));
        this.btn_sd = ((TextView)findViewById(R.id.order_tv_sd));
         
       //区分上传 和未上传
    	
		selectChk=(CheckBox)findViewById(R.id.selectchk);
		
		selectChk.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (selectChk.isChecked()) {

							for (int i = 0; i < list.size(); i++) {
								list2.get(i).put("flag", "true");
								idSet.add(list2.get(i).get("id"));
							}
							checkNum = list.size();
							dataChanged();
							adapter.notifyDataSetChanged();

						} else {

							for (int i = 0; i < list.size(); i++) {

								list2.get(i).put("flag", "false");
								checkNum = 0;
							}
							idSet.clear();
							dataChanged();
							adapter.notifyDataSetChanged();
						}

					}

				});	
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
		Log.d(TAG,"PointListActivity resume");
		checkNum=0;
        fillData();
        dataChanged();
        

	}
	
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		
		 MenuInflater inflater = getMenuInflater(); 
		 inflater.inflate(R.menu.beelist_menu, menu);
		 
		 super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		AdapterContextMenuInfo itemInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		
		 userPoint=((UserPoint)list.get(itemInfo.position));
		 switch (item.getItemId()) {
	        case R.id.viewInfo:      	
			if (flowType == 0) {

				Intent mIntent = new Intent();
				mIntent.setAction(Constants.POINT_EDIT);
				Bundle bundle = new Bundle();
				bundle.putSerializable("userPoint", userPoint);
				mIntent.putExtras(bundle);
				startActivity(mIntent);
				
			} else if (flowType == 1) {
				
				Intent mIntent = new Intent(this,UserPointView.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("userPoint", userPoint);
				mIntent.putExtras(bundle);
				startActivity(mIntent);

			}
	          return true;
	    }
		
		return super.onContextItemSelected(item);
	}
	
	
	/**
	 * 显示上传对话框
	 */
	private void showUploadDialog(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("正在上传位置");
		
		final LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
		
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
		uploadDialog = builder.create();
		uploadDialog.setCanceledOnTouchOutside(false);
		uploadDialog.show();	
				
		new Thread() {
			@Override
			public void run() {
				
				Message msg =new Message();

				mUploadTotalNumber = idSet.size();
				mFinishUploadNumber = 0;
				mCurrentNumber=0;
				
				
				ArrayList<UserPoint> pointList=infoService.findPointsByIds(idSet);
				
                for(UserPoint userPoint:pointList){
					
                	userPoint.setUnitNumber(appContext.mOrgList.get(whichUnit).GetID());
                	userPoint.setUnitName(appContext.mOrgList.get(whichUnit).GetValue());
				}
				
				try {
				for (int i = 0; i < pointList.size(); i++) {
					mCurrentNumber++;
					handler.sendEmptyMessage(UPLOAD_UPDATE);
						
						if(appContext.uploadPosition(pointList.get(i))){						
							pointList.get(i).setStatus(Constants.UPLOAD_SUCCESS);
				    		appContext.updateUserPoint(pointList.get(i));							
							mFinishUploadNumber++;
							
						}						
					}
					
				} catch (AppException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
					handler.sendMessage(msg);
					return;
					
				}finally{

					idSet.clear();
				}

				handler.sendEmptyMessage(DOWN_OVER);
			}

		}.start();
		
	}
	
	
	private class PdOnClickListener implements View.OnClickListener {

		public void onClick(View paramView) {
		    flowType = 0;
			rl_pd_btn.setBackgroundResource(R.drawable.pd_btn_bg_on);
			rl_sd_btn.setBackgroundResource(R.drawable.sd_btn_bg_off);
			btn_pd.setTextColor(getResources().getColor(R.color.white));
			btn_sd.setTextColor(getResources().getColor(R.color.help_item));
			idSet.clear();
			fillData();
			dataChanged();
			selectChk.setChecked(false);		
			uploadBtn.setVisibility(View.VISIBLE);
		}
	}

	private class SdOnClickListener implements View.OnClickListener {

		public void onClick(View paramView) {
			flowType = 1;
			idSet.clear();
			rl_pd_btn.setBackgroundResource(R.drawable.pd_btn_bg_off);
			rl_sd_btn.setBackgroundResource(R.drawable.sd_btn_bg_on);
			btn_sd.setTextColor(getResources().getColor(R.color.white));
			btn_pd.setTextColor(getResources().getColor(R.color.help_item));
		    fillData();
		    dataChanged();
		    selectChk.setChecked(false);	    
		    uploadBtn.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		/*super.onActivityResult(requestCode, resultCode, data);*/
		flowType=0;
		rl_pd_btn.setBackgroundResource(R.drawable.pd_btn_bg_on);
		rl_sd_btn.setBackgroundResource(R.drawable.sd_btn_bg_off);
		btn_pd.setTextColor(getResources().getColor(R.color.white));
		btn_sd.setTextColor(getResources().getColor(R.color.help_item));
		
		uploadBtn.setVisibility(View.VISIBLE);
		
		Log.d(TAG,"PointList onActivityResult");
	}
	
	

}

