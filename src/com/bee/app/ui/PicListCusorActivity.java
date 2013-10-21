package com.bee.app.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.AppException;
import com.bee.app.adapter.PictureAdapter;
import com.bee.app.adapter.PictureGridAdapter;
import com.bee.app.api.ApiClient;
import com.bee.app.bean.BeeSource;
import com.bee.app.bean.PicBean;
import com.bee.app.bean.Picture;
import com.bee.app.bean.UserPoint;
import com.bee.app.db.DataBaseHelper;
import com.bee.app.db.InfoService;

import com.bee.base.BaseUi;
import com.bee.common.Constants;
import com.bee.common.FileUtil;
import com.bee.common.HttpPostUtil;
import com.bee.common.UIHelper;
import com.bee.log.GlobalExceptionHandler;
import com.bee.log.GlobalExceptionHandler.UncaughtException;
import com.bee.log.LogUtil;

public class PicListCusorActivity extends BaseUi implements View.OnClickListener{
    private static final String TAG = "chinabee";
	private GridView gridView;
    private String[] imagePaths;
	private List<PicBean> picBeans;
	private PictureGridAdapter adapter;
	
	private int checkNum;
	private Button deleteBtn;
	private Button uploadBtn;
	
	private ArrayList<PicBean> urllist2=new ArrayList<PicBean>();
	
	private RelativeLayout noInfo;
	private LinearLayout picList;
	
	
	private static final int UPLOAD_NOSDCARD = 0;
    private static final int UPLOAD_UPDATE = 1;
    private static final int DOWN_OVER = 2;
	
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case 1:
				mProgressDialog.dismiss();
				Toast.makeText(PicListCusorActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
				fillData();
				break;
			}
			super.handleMessage(msg);
		}

	};
	protected int progress;
	
	private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case UPLOAD_UPDATE:
				
				progress =(int)(((float)mFinishUploadNumber / mUploadTotalNumber) * 100);
				mProgress.setProgress(progress);
				uploadDialog.setTitle("正在上传第"+mCurrentNumber+"张图片");
				mProgressText.setText("已上传 "+mFinishUploadNumber+ "/" +mUploadTotalNumber);
				break;
			case DOWN_OVER:
				if (uploadDialog != null) {
					uploadDialog.dismiss();
				}
				Toast.makeText(PicListCusorActivity.this, "上传完成！", 3000).show();
				fillData();
				dataChanged();
				break;
			case UPLOAD_NOSDCARD:
				break;
				
			case -1:
				if (uploadDialog != null) {
					uploadDialog.dismiss();
				}
				((AppException)msg.obj).makeToast(PicListCusorActivity.this);
				fillData();
				dataChanged();
				
				break;
			}
			}
    		
    	};
		
	private ProgressDialog mProgressDialog;
	private int whichUnit;
	private AppContext appContext;
	private String rawUrl;
	private CheckBox selectChk;
	private TextView addBtn;
	private Intent mIntent;
	private InfoService infoService;
	private ProgressBar mProgress;
	private TextView mProgressText;
	private AlertDialog uploadDialog;
	protected int mUploadTotalNumber;
	protected int mFinishUploadNumber;
	protected int mCurrentNumber;
	private SharedPreferences settings;
	private RelativeLayout rl_pd_btn;
	private RelativeLayout rl_sd_btn;
	private TextView btn_pd;
	private TextView btn_sd;
	private TextView tv_tip_pd;
	private TextView tv_tip_sd;
	public int flowType;
	private TextView head_title;
	private TextView tv_show;
	protected ProgressDialog uploadProgress;
	protected boolean isCancel=false;
	private GlobalExceptionHandler globalExceptionHandler;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 LogUtil.trace(Log.ERROR, TAG, "onCreate...",false);
		 globalExceptionHandler =  LogUtil.processGlobalException(getApplication(),false);
	        globalExceptionHandler.setUncatchExceptionListener(new UncaughtException() {
				

				@Override
				public void uncatchException(Thread thread, Throwable ex) {
					System.out.println("我们出现了不能处理的错误");
					Toast.makeText(PicListCusorActivity.this, "出现错误了。。。", Toast.LENGTH_SHORT).show();
					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(0);
				}
			});
		
		
		
		Log.e(TAG,"PicListCursorActivity oncreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_piclist);	
		initView();
		appContext=(AppContext)this.getApplication();	
		infoService=appContext.getInforService();
		picBeans = new ArrayList<PicBean>();
		
		settings = getSharedPreferences(Constants.SETTINGS_NAME,
				MODE_PRIVATE);
		
		
        //add
		PdOnClickListener localPdOnClickListener = new PdOnClickListener();
		rl_pd_btn.setOnClickListener(localPdOnClickListener);
		SdOnClickListener localSdOnClickListener = new SdOnClickListener();
        rl_sd_btn.setOnClickListener(localSdOnClickListener); 
        //add
        
        registerForContextMenu(gridView);
    }

    private void initView() {
		// TODO Auto-generated method stub
		
    	gridView = (GridView) findViewById(R.id.gridview);
    	
        head_title=(TextView)findViewById(R.id.pointlist_head_title);
 		
 		head_title.setText("图片列表");
 		
 		tv_show = (TextView) findViewById(R.id.tv);

		deleteBtn=(Button)this.findViewById(R.id.button_delete);
		uploadBtn=(Button)this.findViewById(R.id.button_upload);
		addBtn=(TextView)this.findViewById(R.id.button_add);
		addBtn.setOnClickListener(this);
		
		picList =(LinearLayout)findViewById(R.id.piclist);
		noInfo=(RelativeLayout)findViewById(R.id.noinfo);
		deleteBtn.setOnClickListener(this);
		uploadBtn.setOnClickListener(this);
		
		//区分上传 和未上传
    	this.rl_pd_btn = ((RelativeLayout)findViewById(R.id.order_btn_pd));
        this.rl_sd_btn = ((RelativeLayout)findViewById(R.id.order_btn_sd));
        this.btn_pd = ((TextView)findViewById(R.id.order_tv_pd));
        this.btn_sd = ((TextView)findViewById(R.id.order_tv_sd));
        this.tv_tip_pd = ((TextView)findViewById(R.id.order_tv_tip_pd));
        this.tv_tip_sd = ((TextView)findViewById(R.id.order_tv_tip_sd));
         
       //区分上传 和未上传
				
        selectChk=(CheckBox)findViewById(R.id.selectchk);
		
		selectChk.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
				if(selectChk.isChecked()){
					
					checkNum=picBeans.size();
					for (int i = 0; i < picBeans.size(); i++) {
						picBeans.get(i).setIschecked(true);						
						//增加
						urllist2.add(picBeans.get(i));
						//增加
					}
					// 刷新listview和TextView的显示
					adapter.notifyDataSetChanged();
					dataChanged();
				}else{
					
					checkNum=0;
					for (int i = 0; i < picBeans.size(); i++) {

						picBeans.get(i).setIschecked(false);
					}
					urllist2.clear();
					// 刷新listview和TextView的显示
					adapter.notifyDataSetChanged();
					dataChanged();
				}
					
				}
				
			});
		
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				ImageView checkbox=(ImageView)view.findViewById(R.id.checkbox);
				ToggleButton  toggleButton=(ToggleButton)view.findViewById(R.id.toggle_button);
				toggleButton.toggle();
				if(toggleButton.isChecked()){
					checkbox.setImageResource(R.drawable.checkbox_selected);					
					picBeans.get(position).setIschecked(true);
					urllist2.add(picBeans.get(position));	
					checkNum++;
					
				}else{
					checkbox.setImageResource(R.drawable.checkbox_disselected);				
					picBeans.get(position).setIschecked(false);	
					urllist2.remove(picBeans.get(position));
					checkNum--;
				}
				dataChanged();		
				Log.d("itemclick","urllist "+urllist2.toString());
			}
		});
	}

	private void fillData(){
    	
    	picBeans.clear();
    	urllist2.clear();
    	checkNum=0;   	
    	Cursor picCursor=infoService.queryPic(flowType);
    	
    	if(picCursor.getCount()<1){
    		
    		picList.setVisibility(View.GONE);
        	noInfo.setVisibility(View.VISIBLE);	
    		return;
    	}
    	
    	imagePaths = new String[picCursor.getCount()];	
    	picList.setVisibility(View.VISIBLE);
    	noInfo.setVisibility(View.GONE); 
    	int i=0;
		if (picCursor.moveToFirst()) {

			do {
				int idIndex = picCursor.getColumnIndex("id");
				String id=picCursor.getString(idIndex);

				int pathIndex = picCursor.getColumnIndex("path");		
				String path=picCursor.getString(pathIndex);
				
				imagePaths[i]=path;
				i++;
				
				int titleIndex=picCursor.getColumnIndex("title");
				String title=picCursor.getString(titleIndex);
				
				int latIndex = picCursor.getColumnIndex("lat");

				int lngIndex = picCursor.getColumnIndex("lng");
				
				int statusIndex = picCursor.getColumnIndex("status");
				String status=picCursor.getString(statusIndex);
				
				
				int unitIndex = picCursor.getColumnIndex("unit");
				String unitName=picCursor.getString(unitIndex);
				
				int addressIndex = picCursor.getColumnIndex("address");
				String address=picCursor.getString(addressIndex);
			//add	
				int addTimeIndex = picCursor.getColumnIndex("addTime");
				String addTime=picCursor.getString(addTimeIndex);	
				//add
				
				String lat=picCursor.getString(latIndex);
				String lng=picCursor.getString(lngIndex);
				
				Log.d("piclist","Path   "+path);
				
				PicBean picBean=new PicBean();
				picBean.setTitle(title);
				picBean.setPath(path);
				picBean.setLat(lat);
				picBean.setLng(lng);
				picBean.setStatus(status);
				picBean.setId(id);
				picBean.setIschecked(false);
				picBean.setImgeFile(new File(path));
				picBean.setUnitName(unitName);
				picBean.setAddress(address);
				picBean.setAddTime(addTime);
			    picBeans.add(picBean);
			    
			} while (picCursor.moveToNext());

		}
        adapter = new PictureGridAdapter(picBeans, this);
        
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        
    }
   
    
   private void deletePic(ArrayList<PicBean> urllist2){
    	
    	for(int i=0;i<urllist2.size();i++){
    		 //从数据库删除
    		infoService.deletePic(urllist2.get(i).getId());	
    		//从本地SDCARD删除
    		File file=new File(urllist2.get(i).getPath());
    		if(file.exists()){
    			file.delete();
    		}
    	}
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {	
		case R.id.nextButton:			
			mIntent=new Intent();
			mIntent.setAction(Constants.QUICK_POINT_ADD);
			startActivity(mIntent);			
			break;					
		case R.id.button_add:
			mIntent=new Intent();
			mIntent.setAction(Constants.PIC_ADD);
			startActivityForResult(mIntent, Constants.REQUEST_ADD);
			/*startActivity(mIntent);	*/
			break;
		case R.id.gridpic:
			
			break;			
		case R.id.listpic:
			
			break;			
		case R.id.button_delete:
			
			if(urllist2.size()<1){
				Toast.makeText(this, "请选择需要删除的选项。", Toast.LENGTH_SHORT).show();
				return;
			} 
	
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setMessage("确定删除吗？");
			builder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//从数据库 和从本地
					deletePic(urllist2);
					
			    	fillData();
			    	dataChanged();
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
			
			if(urllist2.size()==0) return;
			
			if (!appContext.isNetworkConnected()) {
				Toast.makeText(this, "无法联网，请检查网络状态", Toast.LENGTH_SHORT).show();
				UIHelper.showNetworkSetting(this);
				return;
			}
			
			if(!appContext.isLogin()){
	        	   mIntent=new Intent(this,LoginDialog.class);
	        	   startActivity(mIntent);
	        	   return;
	           }
			
			if(urllist2 == null || urllist2.size() <= 0) {
				
				Toast.makeText(this, "请选择需要上传的图片", Toast.LENGTH_SHORT).show();
				return;		
			}		
			
            if(appContext.mOrgList==null){
				
				Toast.makeText(PicListCusorActivity.this, "请更新上传单位参数", Toast.LENGTH_SHORT).show();
				break;
			}
            
			showUnitDialog();	
			break;
		default:
			break;
		}		
	}

    
    
 public String uploadFile(PicBean picBean){
    	
    	String result=null;
    	
    	String urlStr =appContext.rawURL + "/upload.aspx";
    	try {
			HttpPostUtil u = new HttpPostUtil(urlStr);
			File file = new File(picBean.getPath());
			byte[] buffer;
			
			buffer = HttpPostUtil.getBytesFromFile(file);
			u.addFileParameter("img", buffer);
			u.addTextParameter("lat", picBean.getLat());
			u.addTextParameter("lng", picBean.getLng());
			
			if(appContext.loginUid!=null&&appContext.loginUid!=""){
                u.addTextParameter("u", appContext.loginUid);
            }
            //上传单位
            if(whichUnit!=-1){
            	u.addTextParameter("ORGID", Constants.unitMap.get(whichUnit));
            }
            //上传单位
			
			byte[] b = u.send();
	        result = new String(b);
	        if(result.equals("1")){
	        	
	        	appContext.updatePic(picBean, Constants.UPLOAD_SUCCESS);
	        }
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return result;
    }
     
	public  void showUnitDialog2(){
		
		whichUnit=-1;		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("请选择上传单位");
		builder.setItems(R.array.unitlist, new DialogInterface.OnClickListener() {
				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub		
				    whichUnit = which;
					mProgressDialog=ProgressDialog.show(PicListCusorActivity.this, "上传", "正在上传，请等待。。。");

					new Thread() {
						@Override
						public void run() {
							for(int i=0;i<urllist2.size();i++){								
							uploadFile(urllist2.get(i));
								
							}
							urllist2.clear();
							Message msg = new Message();
							msg.what = 1;
							handler.sendMessage(msg);
						}

					}.start();		
			}
		});
	 builder.show();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isCancel=false;
		
		Log.d(TAG,"onresume isCancel    "+isCancel);
	    
		
		
		Log.e(TAG,"PicListCursorActivity resume");
		
		String status=Environment.getExternalStorageState();
		if(!status.equals(Environment.MEDIA_MOUNTED)){
			
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setMessage("存储卡暂时不可用！");
			builder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					 PicListCusorActivity.this.finish();
				}
			});
			builder.setCancelable(false);
			
			builder.create().show();
			
		}else{
			
			fillData();
		}	
	}
	
	/**
	 * 显示上传对话框
	 */
	private void showUploadDialog(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("正在上传图片");
		
		final LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
		
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {	
			

			@Override
			public void onClick(DialogInterface dialog, int which) {
				isCancel=true;
				dialog.dismiss();
				fillData();
				dataChanged();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isCancel=true;
				dialog.dismiss();
				fillData();
				dataChanged();
				
			}
		});
		uploadDialog = builder.create();
		uploadDialog.setCanceledOnTouchOutside(false);
		uploadDialog.show();
		
		
			
		new Thread("upload pic") {
			@Override
			public void run() {
				
				Message msg =new Message();
				
				String rawUrl=settings.getString("url", "");
			    String userId=settings.getString("uid", "");
		        String picUrl =rawUrl + "/upload.aspx";	
		        String phone=settings.getString("phoneNumber", "");

				mUploadTotalNumber = urllist2.size();
				mFinishUploadNumber = 0;
				mCurrentNumber=0;
				try {			 
				int i=0;	
			    while(i<urllist2.size())	{	 //终止线程
			    	
			    	Log.d(TAG,"while while while while isCancel    "+isCancel);
					mCurrentNumber++;
					mHandler.sendEmptyMessage(UPLOAD_UPDATE);
						
						if(ApiClient.uploadPic(appContext, picUrl, urllist2.get(i),userId,phone)){
							Log.d(TAG,"urllist2.get(i) "+urllist2.get(i).toString());
							urllist2.get(i).setStatus(Constants.UPLOAD_SUCCESS);
							appContext.updatePic(urllist2.get(i));
							mFinishUploadNumber++;	
						}	
						i++;						
					}	
				} catch (AppException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
					mHandler.sendMessage(msg);
					return;
					
				}finally{

					urllist2.clear();
				}

				mHandler.sendEmptyMessage(DOWN_OVER);
			}

		}.start();
		
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
		 PicBean picBean = ((PicBean)picBeans.get(itemInfo.position));
		 switch (item.getItemId()) {
	        case R.id.viewInfo:   
	        	
			if (flowType == 0) {
				Intent mIntent = new Intent();
				mIntent.setAction(Constants.PIC_EDIT);
				Bundle bundle = new Bundle();
				bundle.putSerializable("picBean", picBean);
				mIntent.putExtras(bundle);
				startActivity(mIntent);
			} else if (flowType == 1) {
				
				Intent mIntent = new Intent(this,PicInfoView.class);
		
				Bundle bundle = new Bundle();
				bundle.putSerializable("picBean", picBean);
				mIntent.putExtras(bundle);
				startActivity(mIntent);

			}
	          return true;
	    }
		
		return super.onContextItemSelected(item);
	}
	
	private class PdOnClickListener implements View.OnClickListener {

		public void onClick(View paramView) {
		    flowType = 0;
			rl_pd_btn.setBackgroundResource(R.drawable.pd_btn_bg_on);
			rl_sd_btn.setBackgroundResource(R.drawable.sd_btn_bg_off);
			btn_pd.setTextColor(getResources().getColor(R.color.white));
			btn_sd.setTextColor(getResources().getColor(R.color.help_item));
		//	idSet.clear();
			tv_tip_sd.setVisibility(View.GONE);
			fillData();
			dataChanged();
			checkNum=0;  //初始化
			selectChk.setChecked(false);		
			uploadBtn.setVisibility(View.VISIBLE);
		//	registerForContextMenu(gridView);
		}
	}

	private class SdOnClickListener implements View.OnClickListener {

		public void onClick(View paramView) {
			flowType = 1;
			checkNum=0;  //初始化
		//	idSet.clear();
			rl_pd_btn.setBackgroundResource(R.drawable.pd_btn_bg_off);
			rl_sd_btn.setBackgroundResource(R.drawable.sd_btn_bg_on);
			btn_sd.setTextColor(getResources().getColor(R.color.white));
			btn_pd.setTextColor(getResources().getColor(R.color.help_item));
			
			tv_tip_pd.setVisibility(View.GONE);	
		    fillData();
		    dataChanged();
		    selectChk.setChecked(false);		    
		    uploadBtn.setVisibility(View.GONE);
		//    unregisterForContextMenu(gridView);
		}
	}	
	
	// 刷新listview和TextView的显示
		private void dataChanged() {
			/*if(adapter!=null){		
				adapter.notifyDataSetChanged();
			}*/
			tv_show.setText("已选中" + checkNum + "项");
			if(checkNum==0){
				
				selectChk.setChecked(false);
			}
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
						for(PicBean picBean:urllist2){
							
							picBean.setUnitNumber(appContext.mOrgList.get(whichUnit).GetID());
							picBean.setUnitName(appContext.mOrgList.get(whichUnit).GetValue());
						}
						
						showUploadDialog();
						
					}
				});
				
			 builder.create().show();	
			}

		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			Log.d(TAG,"onDestroy");
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
