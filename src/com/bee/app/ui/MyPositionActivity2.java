package com.bee.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.AppException;
import com.bee.app.adapter.ListViewPosAdapter;
import com.bee.app.bean.BeeSource;

import com.bee.app.bean.PicBean;
import com.bee.app.bean.Point;
import com.bee.app.bean.Position;
import com.bee.app.bean.UserPoint;
import com.bee.app.db.InfoService;
import com.bee.base.BaseUi;
import com.bee.common.Constants;
import com.bee.common.HttpPostUtil;
import com.bee.common.HttpUtil;
import com.bee.common.UIHelper;

import com.bee.location.*;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.json.JSONObject;

public class MyPositionActivity2 extends BaseUi implements View.OnClickListener {
	
    private LocationManager locationManager;
    private Button mPointCatchBtn;
    private Button postBtn;
    private Button mButtonCurrent;
    private Button saveButton;
    private TextView mPositionText;
    private String userId;
    private String url;
    
    public final static int UPLOAD_SUCCESS=1;
    public final static int NO_UPLOAD=0;
 
    
    private BDLocation currentLocation=new BDLocation();
    private EditText placeName;

    private LocationManager lm;

    // 每次侦听的最大持续时间
    private static final long LISTEN_TIME = 60000;

	private static final String TAG = "chinabee";

	protected static final int SAVE = 1;

	protected static final int UPLOAD = 2;
	private static final int CATCH = 0;
	protected static final int ONE_UPLOAD = 3;
	protected static final int FAIL = 4;

    private GpsLocation gpsLocation;

    private LocationUtil locationUtil = new LocationUtil();
    private InfoService infoService;

	private LocationManager mLocationManager;
	
	private Point point;
	
    public MyLocationListenner myListener = new MyLocationListenner();
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			   switch(msg.what){
			   case CATCH:
				   if (currentLocation != null) {

						mPositionText.setText("已获取到位置信息（经度:"
								+ String.valueOf(currentLocation.getLongitude())
								+ ",纬度:"
								+ String.valueOf(currentLocation.getLatitude()) + "）");
						postBtn.setVisibility(View.VISIBLE);
						saveButton.setVisibility(View.VISIBLE);
					}
				   break;
			   case SAVE:	   
				   toast("保存成功");
				   mPositionText.setText("点击获取位置信息");
				   placeName.setText("");
				   latView.setText("");
				   lngView.setText("");
				   break;
				   
			   case UPLOAD:		
				   
				   toast(msg.getData().getString("result"));
				   mProgressDialog.cancel();
				   mPositionText.setText("点击获取位置信息");
				   placeName.setText("");
				   latView.setText("");
				   lngView.setText("");
				   break;
				   
			   case ONE_UPLOAD:
				  
				   mProgressDialog.dismiss();
				   Toast.makeText(MyPositionActivity2.this, "上传成功", Toast.LENGTH_SHORT).show();
				   Intent mIntent=new Intent(MyPositionActivity2.this,MainPage2.class);
				   startActivity(mIntent);
				   break;
				   
			   case FAIL:
				   mProgressDialog.cancel();
				   toast("操作失败");
				   break;
				   
			   case -1:
				   mProgressDialog.dismiss();
				   ((AppException)msg.obj).makeToast(MyPositionActivity2.this);
			   }
			   
	   
		}
		
	};
	private LocationClient mLocationClient;
	private ListViewPosAdapter lvNewsAdapter;
	private ProgressBar mHeadProgress;
	private ImageButton mCatch_ImgButton;
	private ProgressDialog mProgressDialog;
	private int whichUnit;
	private AppContext appContext;
	private TextView latView;
	private TextView lngView;
	private Button uploadButton;
	private String action;
	private Button nextButton;
	private Intent mIntent;
	private BeeSource beeSource;
	private String filePath;
	private SharedPreferences settings;
	private PicBean picBean;
	private UserPoint userPoint;
	private Button skipButton;
	private Button mButtonCatch;
	private LinearLayout pos_info;
	private WebView viewmap;
	private String rawUrl;
	private ProgressBar progressbar;
	private FrameLayout framelayout;
	public String mLatitude;
	public String mLongitude;
	private TextView mPosition_name;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_position);
        initView();
        
        appContext=(AppContext)this.getApplicationContext();
        infoService=appContext.getInforService();
        
        settings=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
		
		rawUrl=settings.getString("url", "");
        
		//baidu
		mLocationClient = new LocationClient(this);
		setLocationOption();
		mLocationClient.registerLocationListener(myListener);	
		//baidu
		settings = getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);

		url = settings.getString("url", "");

		userId = settings.getString("uid", "");
            
        mButtonCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	mButtonCurrent.setEnabled(false);
            	mButtonCatch.setEnabled(true);
            	pos_info.setVisibility(View.GONE );
            	viewmap.setVisibility(View.VISIBLE);
            	
            	framelayout.setVisibility(View.VISIBLE);
            	
            	
                if(currentLocation==null){
                	toast("尚未获取位置信息");
                	return;
                }
                
                
                
                viewmap.setWebViewClient(new WebViewClient(){

					@Override
					public void onPageFinished(WebView view, String url) {
						// TODO Auto-generated method stub
						
						Log.d(TAG,"onPageFinished ");
						progressbar.setVisibility(View.GONE);
						viewmap.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadResource(WebView view, String url) {
						// TODO Auto-generated method stub
						Log.d(TAG,"onLoadResource ");
					}

					@Override
					public void onPageStarted(WebView view, String url,
							Bitmap favicon) {
						// TODO Auto-generated method stub
						Log.d(TAG,"onPageStarted ");
						progressbar.setVisibility(View.VISIBLE);
						viewmap.setVisibility(View.GONE);
					} 	   	
                });
                      
                viewmap.getSettings().setJavaScriptEnabled(true);
                
                url=rawUrl +"/map.aspx";
    
                url+= "?lat="+ currentLocation.getLatitude();
                url+= "&lng="+ currentLocation.getLongitude();
                
                viewmap.loadUrl(url);
                //webview加载
                }
            });
        
        
        mButtonCatch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				mButtonCurrent.setEnabled(true);
            	mButtonCatch.setEnabled(false);
            	
            	pos_info.setVisibility(View.VISIBLE );
            	viewmap.setVisibility(View.GONE);
            	framelayout.setVisibility(View.GONE);
				
			}

        });
        
           
        mCatch_ImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	 mLocationClient.start();
            	 mHeadProgress.setVisibility(View.VISIBLE);
            	 mLocationClient.requestLocation();
            	 mPositionText.setText("正在获取位置信息。。。");
            	 latView.setText("");
            	 lngView.setText("");
            	 placeName.setText("");
            	 mPosition_name.setText("");
            }
        }); 
    }

    
    protected void SavePosition(String status) {
		// TODO Auto-generated method stub
		if (point != null) {
			infoService.addUserPoint(point.getLat(), point.getLng(),
					placeName.getText().toString(), status,mPosition_name.getText().toString());

			Message msg = new Message();
			msg.what = MyPositionActivity2.SAVE;
			mHandler.sendMessage(msg);
		}
	}   
    
    protected void uploadPosition(String placeName){
        if (point==null){
        	
        	 return;
        	
        }

		String _url;
		try {
			_url = url + "/userPoint.aspx?u=" + userId
					+ "&lat=" + point.getLat() + "&lng="
					+ point.getLng() + "&name="
					+ URLEncoder.encode(placeName, "UTF-8")
					//添加单位
					+"&ORGID="+Constants.unitMap.get(whichUnit);
			
			        //添加单位

			Log.e("userPointUrl:", _url);
			String result = HttpUtil.queryStringForPost(_url);
			if(result.equals("发送成功")){
				
				  SavePosition(Constants.UPLOAD_SUCCESS);
				
			}else{
				  SavePosition(Constants.NO_UPLOAD);
			}
			Message msg=new Message();
			msg.what=MyPositionActivity2.UPLOAD;
			Bundle bundle=new Bundle();
			bundle.putString("result", result);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  		
    }
  
     @Override
 	protected void onResume() {
    	 
    	 super.onResume();	
    	 
    //	mButtonCurrent.setEnabled(true);
 		mButtonCatch.setEnabled(false);
    	 
    	action=getIntent().getAction();

		 if(action!=null&&action.equals(Constants.POINT_ADD)){
				uploadButton.setVisibility(View.GONE);
				
		}else if(action!=null&&action.equals(Constants.QUICK_POINT_ADD)){
				
				uploadButton.setVisibility(View.GONE);
				saveButton.setVisibility(View.GONE);
				nextButton.setVisibility(View.VISIBLE);
				
				skipButton.setVisibility(View.VISIBLE);
				
			}else if(action!=null&&action.equals(Constants.POINT_EDIT)){
				
				uploadButton.setVisibility(View.GONE);
				mCatch_ImgButton.setVisibility(View.GONE);
				mPositionText.setVisibility(View.GONE);
				userPoint=(UserPoint)getIntent().getExtras().get("userPoint");
				latView.setText(userPoint.getLat());
				lngView.setText(userPoint.getLng());
				currentLocation.setLatitude(Double.valueOf(userPoint.getLat()));
				currentLocation.setLongitude(Double.valueOf(userPoint.getLng()));
				mLatitude=userPoint.getLat();
				mLongitude=userPoint.getLng();
				placeName.setText(userPoint.getName());
				mPosition_name.setText(userPoint.getAddress());
				
			}
     }
     
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onPause();
	//	gpsLocation.stopGps();
		mLocationClient.stop();
		currentLocation=null;
		point=null;
	}
	
	public void toast (String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	//设置相关参数
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);				//打开gps
		option.setCoorType("bd09II");		//设置坐标类型
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);	
		option.setAddrType("all");
        option.setScanSpan(3000);		
		option.setPriority(LocationClientOption.GpsFirst);        //不设置，默认是gps优先
		option.disableCache(true);		
		mLocationClient.setLocOption(option);
	}
	
	
	private class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation mBDLocation) {
			if (mBDLocation == null)
			 return ;
			
			  mPositionText.setText("点击获取位置信息");
			  
			  mLatitude = String.valueOf(mBDLocation.getLatitude());
			  mLongitude =  String.valueOf(mBDLocation.getLongitude());
			  
			  Log.d(TAG,"before mLatitude :"+mLatitude);
			  Log.d(TAG,"before mLongitude :"+mLongitude);
			  
			  latView.setText(mLatitude);
			  lngView.setText(mLongitude);
			  mPosition_name.setText(mBDLocation.getAddrStr());
			  mHeadProgress.setVisibility(View.GONE);
			  MyPositionActivity2.this.currentLocation=mBDLocation;					
			  /*new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					point = locationUtil.change(
							String.valueOf(currentLocation.getLatitude()),
							String.valueOf(currentLocation.getLongitude()));
					
					Log.d(TAG,"after mLatitude :"+mLatitude);
					Log.d(TAG,"after mLongitude :"+mLongitude);
				}
			}.start();	 */ 
		}
		
		public void onReceivePoi(BDLocation poiLocation) {
			
			
			if (poiLocation == null){
				return ; 
			}
		}
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){		
		case R.id.button_next:
			
            if (!appContext.isNetworkConnected()) {
				
				/*Toast.makeText(MyPositionActivity2.this, "无法联网，请检查网络状态", Toast.LENGTH_SHORT).show();*/
				UIHelper.showNetworkSetting(this);
				break;
			}
			
			if (!validate()) {
				break;
			}
			
		    if (!appContext.isLogin()) {
			Intent mIntent = new Intent(this, LoginDialog.class);
			startActivity(mIntent);
			break;
		   }
		    userPoint=getUserPoint();
		    userPoint.setStatus(Constants.NO_UPLOAD);
		    
		    settings = getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);

			userId = settings.getString("uid", "");
			
		    userPoint.setUserId(userId);
		    
		    
		    
		    if (!appContext.saveUserPoint(userPoint)){
		    	
		    	toast("保存失败");
				return;
		    }
		    
           if(appContext.mOrgList==null){
				
				Toast.makeText(MyPositionActivity2.this, "请更新上传单位参数", Toast.LENGTH_SHORT).show();		
				break;
			}
           
           /*ArrayAdapter<CItem> mOrgAdapter = new ArrayAdapter<CItem>(this, android.R.layout.simple_dropdown_item_1line, appContext.mOrgList);
			
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
		   builder.setTitle("请选择上传单位");
		   builder.setAdapter(mOrgAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							mProgressDialog=ProgressDialog.show(MyPositionActivity2.this, "上传", "正在上传位置信息，请等待。。。");	
							mProgressDialog.setCancelable(true);
							
							userPoint.setUnitNumber(appContext.mOrgList.get(which).GetID());
							//添加
							userPoint.setUnitName(appContext.mOrgList.get(which).GetValue());
							
							new Thread() {
								@Override
								public void run() {
									Message msg=new Message();
									try {
										if (appContext.uploadPosition(userPoint)) {
											
											appContext.updateUserPoint(userPoint,
													Constants.UPLOAD_SUCCESS);
											userPoint.setStatus(Constants.UPLOAD_SUCCESS);
		                                    appContext.updateUserPoint(userPoint);
											
											msg.what=ONE_UPLOAD;
										}else{
											
											msg.what=FAIL;
											
										}
									} catch (AppException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										msg.what=-1;
										msg.obj=e;
									}
								
									mHandler.sendMessage(msg);
								
								}
								}.start();
							
							
							}
						});
                     
           builder.show();*/
           
           mProgressDialog=ProgressDialog.show(MyPositionActivity2.this, "上传", "正在上传位置信息，请等待。。。");	
			mProgressDialog.setCancelable(true);
			
			userPoint.setUnitNumber(appContext.orgId_choosed);
			//添加
			userPoint.setUnitName(appContext.orgValue_choosed);
			
			new Thread() {
				@Override
				public void run() {
					Message msg=new Message();
					try {
						if (appContext.uploadPosition(userPoint)) {
							
							appContext.updateUserPoint(userPoint,
									Constants.UPLOAD_SUCCESS);
							userPoint.setStatus(Constants.UPLOAD_SUCCESS);
                           appContext.updateUserPoint(userPoint);
							
							msg.what=ONE_UPLOAD;
						}else{
							
							msg.what=FAIL;
							
						}
					} catch (AppException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msg.what=-1;
						msg.obj=e;
					}
				
					mHandler.sendMessage(msg);
				
				}
				}.start();
		
			break;	
		case R.id.button_save: 
			
            if(!validate())
            {
            	break;
            }
            
            if (currentLocation != null) {
            	
            	UserPoint userPoint=getUserPoint();
            	userPoint.setStatus(Constants.NO_UPLOAD);
            	if(!appContext.saveUserPoint(userPoint)){
            		
            		Toast.makeText(MyPositionActivity2.this, "保存位置信息失败",Toast.LENGTH_SHORT).show();
            		break;
            	}else {
            		
                	Toast.makeText(MyPositionActivity2.this, "保存完成！",Toast.LENGTH_SHORT).show();
            	}

/*              startActivity(new Intent(this,PointListActivity2.class));*/
            	
            	setResult(RESULT_OK);
            	MyPositionActivity2.this.finish();
            	
            } 
	    break;
	    
        case R.id.button_skip:
			
			mIntent=new Intent(this,MainPage2.class);
	
			startActivity(mIntent);		
			MyPositionActivity2.this.finish();	
			break;
		}
	    
		}
	
	
	public void initView(){
		
        mButtonCurrent = (Button) findViewById(R.id.button_current);
        
        mButtonCatch = (Button) findViewById(R.id.button_catch);
        uploadButton = (Button) findViewById(R.id.button_upload);
        saveButton = (Button) findViewById(R.id.button_save);
        saveButton.setOnClickListener(this);
        mCatch_ImgButton=(ImageButton)findViewById(R.id.catch_imgbutton);
        mPositionText = (TextView) findViewById(R.id.labelText);
        placeName = (EditText) findViewById(R.id.placeName);

        mHeadProgress = (ProgressBar) findViewById(R.id.pos_head_progress);
        latView=(TextView)findViewById(R.id.latview);
        lngView=(TextView)findViewById(R.id.lngview);
        
        mPosition_name=(TextView)findViewById(R.id.position_name);
        
        nextButton=(Button)this.findViewById(R.id.button_next);
		nextButton.setOnClickListener(this);
		
		skipButton=(Button)this.findViewById(R.id.button_skip);	
		skipButton.setOnClickListener(this);
		
		mButtonCurrent.setEnabled(true);
		mButtonCatch.setEnabled(true);

		
		pos_info=(LinearLayout)findViewById(R.id.pos_info);
		
		
		viewmap=(WebView)findViewById(R.id.viewmap);
		
		framelayout=(FrameLayout)findViewById(R.id.web);
		
		progressbar=(ProgressBar)findViewById(R.id.progressBar);
		
	}
	 
	 
	 
	 public boolean validate(){
		 
		 
		/*if (currentLocation == null) {
			Toast.makeText(this, "尚未获取位置信息", Toast.LENGTH_SHORT).show();
			return false;
		}*/  //暂时注释掉
		 
		 if(mLatitude==null||mLongitude==null)
			{
				Toast.makeText(this, "尚未获取经纬度", Toast.LENGTH_SHORT).show();
				return false;
			}
		
		if (placeName.getText().toString().equals("")
				|| placeName.getText().toString().trim().equals("")
				|| placeName.getText().toString().trim().length() == 0) {
			
			Toast.makeText(this, "位置名称不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		 return true;
	 }
	
	public UserPoint getUserPoint(){
		//add
		if(userPoint==null){
			
			userPoint=new UserPoint();
		}
		//add
		 
		 userPoint.setName(placeName.getText().toString());
		 userPoint.setLat(String.valueOf(latView.getText().toString()/*currentLocation.getLatitude())*/));
		 userPoint.setLng(String.valueOf(lngView.getText().toString()/*currentLocation.getLongitude())*/));
		 userPoint.setUserId(userId);
		 userPoint.setAddress(mPosition_name.getText().toString());
		 return userPoint;	
	}
	
	
	public boolean savePosition(UserPoint userPoint){
		long rowId=-1;
		if(userPoint!=null){
		   rowId=infoService.addUserPoint(userPoint.getLat(), userPoint.getLng(), userPoint.getName(), userPoint.getStatus(),userPoint.getAddress());	
		}
		
		if(rowId==-1){
			return false;
		}	
		userPoint.setId(String.valueOf(rowId));
		return true;
	}
		
}