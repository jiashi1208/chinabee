package com.bee.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Character.UnicodeBlock;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.AppException;
import com.bee.app.api.ApiClient;
import com.bee.app.bean.BeeSource;
import com.bee.app.bean.PicBean;

import com.bee.app.db.InfoService;
import com.bee.base.BaseUi;
import com.bee.common.Constants;
import com.bee.common.FileUtils;
import com.bee.common.HttpPostUtil;
import com.bee.common.ImageUtils;
import com.bee.common.StringUtils;
import com.bee.common.UIHelper;
import com.bee.log.GlobalExceptionHandler;
import com.bee.log.GlobalExceptionHandler.UncaughtException;
import com.bee.log.LogUtil;

import android.graphics.BitmapFactory;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class CamActivity0814 extends BaseUi implements View.OnClickListener {
    private ImageView imageView;
    private String filePath;
    private String userId;
    private Uri uri;
    private Bitmap b;
    private TextView picName;
	private ImageButton camImgBtn;
	private Button saveButton;
	private Bitmap bitmap;
	private Button uploadButton;
	private AppContext appContext;
	private String url;
	
//	private String mLocation;
	private String mLatitude;
	private String mLongitude;
	
	private ProgressDialog progressUpload;
	
	private String mLBSAddress;
	
	public static final int IMAGE_CAPTURE=1;
	private static final String TAG = "chinabee";
	
	private String theLarge;
	
	private String theThumbnail;
	
	private File imgFile;
	
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (!Thread.currentThread().isInterrupted()){
                switch (msg.what){
                    case 1:
                    	
                    	mProgressDialog.dismiss();
                        Toast.makeText(CamActivity0814.this, "图片上传成功！", Toast.LENGTH_SHORT).show();                     
                        mIntent.setAction(Constants.QUICK_POINT_ADD);
            			startActivity(mIntent);		
            			CamActivity0814.this.finish();   
                        
                        break;
                    case 0:
                    	mProgressDialog.dismiss();
                    	Toast.makeText(CamActivity0814.this, "图片上传失败,请稍候再试！", Toast.LENGTH_SHORT).show();
                        break;
                    case -1:
                      /*  String error = msg.getData().getString("error");
                        Toast.makeText(CamActivity0814.this, error, 1).show();*/
                    	 mProgressDialog.dismiss();
                    	((AppException)msg.obj).makeToast(CamActivity0814.this);
                        break;
                    case Constants.POSITION_SUCCESS:
                    	
                    	mPlace.setText(mLBSAddress);
                    	if(mClient!=null&&mClient.isStarted())
                    	         mClient.stop();	
                    	break;

                }
            }
        }
    };
    
    
	private Button mPlace;
	private SharedPreferences settings;
	private InfoService infoService;
	private GlobalExceptionHandler globalExceptionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.camera);
        
        
        globalExceptionHandler =  LogUtil.processGlobalException(getApplication(),false);
        globalExceptionHandler.setUncatchExceptionListener(new UncaughtException() {

			@Override
			public void uncatchException(Thread thread, Throwable ex) {
				System.out.println("我们出现了不能处理的错误");
				Toast.makeText(CamActivity0814.this, "出现错误了。。。", Toast.LENGTH_SHORT).show();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
		});
        
        
        
        
        initView();
        initLBS();
          
        appContext=(AppContext)getApplication();
        
        infoService=appContext.getInforService();
        
		settings = getSharedPreferences(Constants.SETTINGS_NAME,
				MODE_PRIVATE);

		url = settings.getString("url", "");
        
        
        userId=settings.getString("uid", "");

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {             // 检测sd是否可用        
            Toast.makeText(CamActivity0814.this, "存储卡不可用，拍照无法保存", Toast.LENGTH_SHORT).show();
        }
      //  mClient.start();  
        setListener();
    }

    private void setListener() {
		// TODO Auto-generated method stub
    	
    	mClient.registerLocationListener(new BDLocationListener() {

			public void onReceivePoi(BDLocation mBDLocation) {

			}
			public void onReceiveLocation(BDLocation mBDLocation) {
				/*saveButton.setEnabled(true);
				uploadButton.setEnabled(true);
				nextButton.setEnabled(true);*/
				
				mLBSAddress = mBDLocation.getAddrStr();
				/*appContext.mLocation = mBDLocation.getAddrStr();
				appContext.mLatitude = mBDLocation.getLatitude();
				appContext.mLongitude = mBDLocation.getLongitude();*/
				
				//mLocation = mBDLocation.getAddrStr();
				mLatitude = String.valueOf(mBDLocation.getLatitude());
				mLongitude =  String.valueOf(mBDLocation.getLongitude());

				mHandler.sendEmptyMessage(Constants.POSITION_SUCCESS);
			}
		});
    	
    	mPlace.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
					
					mPlace.setText("正在定位...");
					if (!mClient.isStarted()) {
						   mClient.start();
					}
					/*saveButton.setEnabled(false);
					uploadButton.setEnabled(false);
					nextButton.setEnabled(false);*/
					mClient.requestLocation();
			}
		});
		
	}

	@Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        
		if(resultCode != RESULT_OK) return;
		
        if (resultCode == Activity.RESULT_OK) {
            destoryBimap();
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {         // 检测sd是否可用
            	Toast.makeText(CamActivity0814.this, "存储卡不可用，拍照无法保存", Toast.LENGTH_SHORT).show();
            	return;
            }
            
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            camImgBtn.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
        }
    }

    private void destoryBimap() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

	private int whichUnit;
	private LocationClientOption mOption;
	private LocationClient mClient;
	private Intent mIntent;
	private Button nextButton;
	private String action;
	private BeeSource beeSource;
	private Button skipButton;
	private PicBean picBean;
	private ProgressDialog mProgressDialog;
	private File imgeFile;
    
    private String uploadFile(String fileName){
        String result="0";
        String urlStr =url + "/upload.aspx";

        Log.v("url", urlStr);
        File file = new File(fileName);
        try{
            HttpPostUtil u = new HttpPostUtil(urlStr);
            byte[] buffer;
            buffer = HttpPostUtil.getBytesFromFile(file);
            u.addFileParameter("img", buffer);

            if(userId!=null&&userId!=""){
                u.addTextParameter("u", userId);
            }
            //上传单位
            if(whichUnit!=-1){
            	u.addTextParameter("ORGID", Constants.unitMap.get(whichUnit));
            }
            //上传单位
            
            //上传经纬度
            u.addTextParameter("lng", String.valueOf(appContext.mLongitude));
            u.addTextParameter("lat", String.valueOf(appContext.mLatitude));
            u.addTextParameter("picName", fileName);
            
            byte[] bytes = u.send();
            result = new String(bytes);

        }catch(Exception e){
            e.printStackTrace();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("发送失败");
            dialog.setMessage("发送失败");
            dialog.show();
        }finally{
        }
        return result;
    }
    
    
	public  void showUnitDialog(){
		
		whichUnit=-1;		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("请选择上传单位");
		builder.setItems(R.array.unitlist, new DialogInterface.OnClickListener() {
				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub		
				    whichUnit = which;
					progressUpload=ProgressDialog.show(CamActivity0814.this, "上传", "正在上传，请等待。。。");

					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							String result=uploadFile(filePath);
	                         progressUpload.dismiss();                       
	                         Message msg = new Message();
	                                   
	                         msg.what = Integer.valueOf(result);
	                         handler.sendMessage(msg);

	                         filePath=null;
							
							
						}

					}.start();		
			}
		});
	 builder.show();

	}
	
	private void initLBS() {
		mOption = new LocationClientOption();
		mOption.setOpenGps(true);
		mOption.setCoorType("bd09ll");
		mOption.setAddrType("all");
		mOption.setScanSpan(100);
		mClient = new LocationClient(getApplicationContext(), mOption);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		action=getIntent().getAction();
		if(action!=null&&action.equals(Constants.PIC_ADD)){
			
			uploadButton.setVisibility(View.GONE);
			mClient.start(); //启动定位
			
		}else if(action!=null&&action.equals(Constants.QUICK_PIC_ADD)){
			
			mClient.start(); //启动定位
			
			saveButton.setVisibility(View.GONE);
			uploadButton.setVisibility(View.GONE);
			
			nextButton.setVisibility(View.VISIBLE);	
			skipButton.setVisibility(View.VISIBLE);
			
			
			
		}else if(action!=null&&action.equals(Constants.PIC_EDIT)){
			
			uploadButton.setVisibility(View.GONE);
			Bundle bundle=getIntent().getExtras();
			picBean=(PicBean)bundle.get("picBean");
			picName.setText(picBean.getTitle());
			
			bitmap=BitmapFactory.decodeFile(picBean.getPath());			
			imageView.setImageBitmap(bitmap);
			imageView.setVisibility(View.VISIBLE);
			camImgBtn.setVisibility(View.GONE);
			mPlace.setText(picBean.getAddress());
			mPlace.setClickable(false);
			mLatitude=picBean.getLat();
			mLongitude=picBean.getLng();
		}
		
	}
   
	
	public void initView(){
		
		mPlace = (Button) findViewById(R.id.newsfeedpublish_poi_place);
		
		imageView = (ImageView) this.findViewById(R.id.preview1);
        picName= (TextView) this.findViewById(R.id.picName);

        saveButton = (Button) this.findViewById(R.id.button_save);
        saveButton.setOnClickListener(this);
        camImgBtn=(ImageButton)this.findViewById(R.id.camimgbtn);       
        uploadButton = (Button) this.findViewById(R.id.button_upload);

        camImgBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub			
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
 
                startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
			}
		});
        
        nextButton=(Button)this.findViewById(R.id.button_next);
		nextButton.setOnClickListener(this);
		
		skipButton=(Button)this.findViewById(R.id.button_skip);	
		skipButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub	
		switch(v.getId()){
		
		case R.id.button_next:		
			if (!appContext.isNetworkConnected()) {
				
				Toast.makeText(CamActivity0814.this, "无法联网，请检查网络状态", Toast.LENGTH_SHORT).show();
				UIHelper.showNetworkSetting(this);
				break;
			}
			
			if(!appContext.isLogin()){
	        	   Intent mIntent=new Intent(this,LoginDialog.class);
	        	   startActivity(mIntent);
	        	   break;
	           }
			
			if(!validate()){
				break;
			}
					
			if(!savePic2SDCard(bitmap)){	//保存到sdcard 获取到path
				Toast.makeText(this, "保存到SDCard失败", Toast.LENGTH_SHORT).show();
				break;	
			}
			
			picBean=getPicBean();
			picBean.setStatus(Constants.NO_UPLOAD);
			if(!savePic2DB(picBean)){		
				Toast.makeText(this, "保存到数据库失败", Toast.LENGTH_SHORT).show();
				break;
			}
			
          if(appContext.mOrgList==null){
				
				Toast.makeText(CamActivity0814.this, "请更新上传单位参数", Toast.LENGTH_SHORT).show();		
				break;
			}
			
			mIntent=new Intent();
	
			/* ArrayAdapter<CItem> mOrgAdapter = new ArrayAdapter<CItem>(this, android.R.layout.simple_dropdown_item_1line, appContext.mOrgList);
			
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
		   builder.setTitle("请选择上传单位");
		   builder.setAdapter(mOrgAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							mProgressDialog=ProgressDialog.show(CamActivity0814.this, "上传", "正在上传图片信息。。。");
							
							mProgressDialog.setCancelable(true);
							picBean.setUnitNumber(appContext.mOrgList.get(which).GetID());
							//添加
							picBean.setUnitName(appContext.mOrgList.get(which).GetValue());
							
							
							new Thread(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
												
									String rawUrl=settings.getString("url", "");
						    	    String userId=settings.getString("uid", "");
						    	    
						    	    String phone=settings.getString("phoneNumber", "");
						    	    
						    	    
							        String result="0";
							        String picUrl =rawUrl + "/upload.aspx";		
							        int what = 0;
							        Message msg =new Message();
							        
									try {
										
										if(ApiClient.uploadPic(appContext, picUrl, picBean,userId,phone)){
											
											appContext.updatePic(picBean, Constants.UPLOAD_SUCCESS);
											picBean.setStatus(Constants.UPLOAD_SUCCESS);
											appContext.updatePic(picBean);
											
											what = 1;
											msg.what = 1;
											
										}else{
											
											what = 0;
											msg.what = 0;
										}
										
									} catch (AppException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										msg.what = -1;
										msg.obj = e;
									}
									
									mHandler.sendMessage(msg);

								}	
							}.start();
							
							}
						});
			
			
		   builder.show();*/
			
           mProgressDialog=ProgressDialog.show(CamActivity0814.this, "上传", "正在上传图片信息。。。");
			
			mProgressDialog.setCancelable(true);
			picBean.setUnitNumber(appContext.orgId_choosed);
			//添加
			picBean.setUnitName(appContext.orgValue_choosed);
			
			
			new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
								
					String rawUrl=settings.getString("url", "");
		    	    String userId=settings.getString("uid", "");
		    	    
		    	    String phone=settings.getString("phoneNumber", "");
		    	    
		    	    
			        String result="0";
			        String picUrl =rawUrl + "/upload.aspx";		
			        int what = 0;
			        Message msg =new Message();
			        
					try {
						
						if(ApiClient.uploadPic(appContext, picUrl, picBean,userId,phone)){
							
							appContext.updatePic(picBean, Constants.UPLOAD_SUCCESS);
							picBean.setStatus(Constants.UPLOAD_SUCCESS);
							appContext.updatePic(picBean);
							
							what = 1;
							msg.what = 1;
							
						}else{
							
							what = 0;
							msg.what = 0;
						}
						
					} catch (AppException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msg.what = -1;
						msg.obj = e;
					}
					
					mHandler.sendMessage(msg);

				}	
			}.start();

			break;
		
		case R.id.button_save:
					
			if(!validate()){		
				break;
			}
			
			if(!savePic2SDCard(bitmap)){	//保存到sdcard 获取到path
				Toast.makeText(this, "保存到SDCard失败", Toast.LENGTH_SHORT).show();
				break;	
			}
			
			picBean=getPicBean();
			picBean.setStatus(Constants.NO_UPLOAD);
			if(!savePic2DB(picBean)){		
				Toast.makeText(this, "保存到数据库失败", Toast.LENGTH_SHORT).show();
				break;
			}
			
			Toast.makeText(CamActivity0814.this, "保存成功", Toast.LENGTH_SHORT).show();	

			/*imageView.setVisibility(View.INVISIBLE);
			picName.setText("");

			camImgBtn.setVisibility(View.VISIBLE);*/

			/*if (action.equals(Constants.PIC_ADD)) {*/

				setResult(RESULT_OK);
				/*mIntent = new Intent(this, PicListCusorActivity.class);
				startActivity(mIntent);*/
				finish();
				
			/*}*/
		    break;
		    
		case R.id.button_skip:
			
			mIntent=new Intent();
			mIntent.setAction(Constants.QUICK_POINT_ADD);
			startActivity(mIntent);		
			CamActivity0814.this.finish();	
			break;
		}
		
	}
	
	
	private PicBean getPicBean() {
		// TODO Auto-generated method stub
		    if(picBean==null){
			   
			  picBean=new PicBean();
		    }
			picBean.setLat(String.valueOf(mLatitude));
			picBean.setLng(String.valueOf(mLongitude));
			picBean.setTitle(picName.getText().toString());
			picBean.setPath(filePath);
			picBean.setImgeFile(imgeFile);
			picBean.setUid(settings.getString("uid", ""));
			picBean.setAddress(mPlace.getText().toString());
			/*picBean.setStatus("0");	*/
		return picBean;
	}
		
	public boolean validate(){
		
		if (bitmap == null || bitmap.equals("")) {

			Toast.makeText(this, "您尚未取景，没有可上传的照片", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (picName.getText().toString().equals("")
				|| picName.getText().toString().trim().equals("")
				|| picName.getText().toString().trim().length() == 0) {

			Toast.makeText(CamActivity0814.this, "图片名称不能为空", Toast.LENGTH_SHORT)
					.show();
			return false;

		}

		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用

			Toast.makeText(CamActivity0814.this, "存储卡不可用", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		
		if(mLatitude==null||mLongitude==null)
		{
			Toast.makeText(this, "尚未获取经纬度", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		/*if(mPlace.getText().toString()==null||mPlace.getText().toString().equals("")){
			
			Toast.makeText(this, "尚未获取具体位置信息", Toast.LENGTH_SHORT).show();
			return false;
		   
		}*/
		
		return true;
	}
	
	 public boolean savePic2SDCard(Bitmap bitmap){
		 
         String name=picName.getText().toString().trim()+ ".jpg";
         FileOutputStream fos = null;
         File file = new File(Constants.PIC_PATH);
         if (!file.exists()) {
             file.mkdirs();
         }
         String fileName = Constants.PIC_PATH + name;
         
         imgeFile = new File(fileName);

         try {
        	 fos = new FileOutputStream(fileName);
             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
             uri = Uri.fromFile(new File(fileName));
             filePath = uri.getPath();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
             return false;
         } finally {
             try {
            	 fos.flush();
            	 fos.close();
             } catch (IOException e) {
                 e.printStackTrace();
                 return false;
             }
         }
		 return true;
	 }
	 
	public boolean savePic2DB(PicBean picBean) {

		if (picBean.getId() != null && picBean.getId() != "") {

			return infoService.updatePic(picBean);
		} else {

			long rowId = infoService.savePic(picBean);

			if (rowId > 0) {

				picBean.setId(String.valueOf(rowId));
				return true;
			}

		}
		return false;
	}
	
	
	
	
}
