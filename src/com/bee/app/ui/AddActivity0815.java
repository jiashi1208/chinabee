package com.bee.app.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;


import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.AppException;
import com.bee.app.bean.BeeSource;
import com.bee.app.db.InfoService;

import com.bee.base.BaseUi;
import com.bee.base.C;
import com.bee.common.CompareID;
import com.bee.common.Constants;
import com.bee.common.HttpUtil;
import com.bee.common.StatueChecker;
import com.bee.common.UIHelper;

//import com.test2.util.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class AddActivity0815 extends BaseUi implements View.OnClickListener {
	
	
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			
			case 1:
				
				mProgressDialog.dismiss();
				Toast.makeText(AddActivity0815.this, "上传成功", Toast.LENGTH_SHORT).show();
				
				mIntent = new Intent();
				/*mIntent.putExtra("bee", beeSource);*/
				mIntent.setAction(Constants.QUICK_PIC_ADD);
				startActivity(mIntent);
				AddActivity0815.this.finish();
	
				break;
				
			case 0:
				
				mProgressDialog.dismiss();
				Toast.makeText(AddActivity0815.this, "上传失败", Toast.LENGTH_SHORT).show();
				
			   break;
			   
			case -1:
				
				 mProgressDialog.dismiss();
             	((AppException)msg.obj).makeToast(AddActivity0815.this);
			
			
			}

		}	
	};
	
	//验证输入是否合法
	private  class Validate {

		public boolean validateInput() {
			String _a1 = a1.getText().toString().trim();
			if (_a1.equals("")) {
				toast("蜜源追溯码不能为空");
				return false;
			} else if ((!_a1.matches("[0-9]{9}"))            //正则表达式
					/*|| Integer.valueOf(_a1)<100000000*/  //080808080必须是有效的
					|| Integer.valueOf(_a1) > 999999999) {
				toast("蜜源追溯码必须为9位数字");
				return false;
			} else if (a2.getText().toString().trim().equals("")) {
				toast("饲养人不能为空");
				return false;
			}else if (a4.getText().toString().trim().equals("")) {
				toast("地点不能为空");
				return false;
			} else if (_a5.trim().equals("")) {
				toast("原料种类不能为空");
				return false;
			} else if (_a16.trim().equals("")) {
				toast("蜂种不能为空");
				return false;
			} else if (a7.getText().toString().trim().equals("")) {
				toast("数量不能为空");
				return false;
			} else if (a6.getText().toString().trim().equals("")) {
				toast("饲料不能为空");
				return false;
			} else if (_a8.trim().equals("")) {
				toast("规格不能为空");
				return false;
			}  else if (a9.getText().toString().trim().equals("")) {
				toast("浓度不能为空");
				return false;
			} else if (Float.parseFloat(a9.getText().toString()) > 1000) {
				toast("浓度不能大于等于1000");
				return false;
			} else if (a10.getText().toString().trim().equals("")) {
				toast("摇蜜间隔不能为空");
				return false;

			} else if (a11.getText().toString().trim().equals("")) {
				toast("用药情况不能为空");
				return false;
			} else if (a12.getText().toString().trim().equals("")) {
				toast("首次采收日期不能为空");
				return false;
			} else if (a13.getText().toString().trim().equals("")) {
				toast("末次采收日期不能为空");
				return false;
			} else if (a14.getText().toString().trim().equals("")) {
				toast("收购日期不能为空");
				return false;
			} else if (a15.getText().toString().trim().equals("")) {
				toast("收购单位不能为空");
				return false;
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date1 = sdf.parse(a12.getText().toString().trim());
					Date date2 = sdf.parse(a13.getText().toString().trim());
					Date date3 = sdf.parse(a14.getText().toString().trim());
					Date date0 = new Date();
					if (date1.getTime() > date0.getTime()) {
						toast("首次采收日期不能大于当前日期");
						return false;
					}
					if (date2.getTime() > date0.getTime()) {
						toast("末次采收日期不能大于当前日期");
						return false;
					}
					if (date3.getTime() > date0.getTime()) {
						toast("收购日期不能大于当前日期");
						return false;
					}
					if (date1.getTime() > date2.getTime()) {
						toast("首次采收日期不能大于末次采收日期");
						return false;
					}
					if (date2.getTime() > date3.getTime()) {
						toast("末次采收日期不能大于收购日期");
						return false;
				}
				} catch (ParseException pe) {
					pe.printStackTrace();
				}
			}
			return true;
		}
	}

	private static final String TAG = "chinabee";
	private EditText a1, a2,

			a4, a6, a7, a9, a10, a11, a12, a13, a14, a15;

	private Spinner a5, a8, a16;  //a5 原料
	                              //a8 规格   a16 蜂种

	private HttpClient httpClient;
	private HttpParams httpParams;
	private String _a5 = "";
	private String _a8 = "";
	private String _a16 = "";
	private String userId;
	private String id;
	private String url;
	
	private InfoService infoService;
	private JSONObject sms;

	private Button saveButton;
	private Button uploadButton;
	
	private AppContext appContext;// 全局Context
	private int whichUnit=-1;
	
	private ProgressDialog progressUpload;
	private String rawUrl;
	private SharedPreferences settings;
	private int flag;
	private String phone;
	private Button nextButton;
	private Intent mIntent;
	private BeeSource beeSource;
	private ArrayList<CItem> materialTypeList =new ArrayList<CItem>();
	private ArrayList<CItem> guiGeList =new ArrayList<CItem>();
	private ArrayList<CItem> beeTypeList =new ArrayList<CItem>();
	private String action;
	private Button skipButton;
	protected ProgressDialog mProgressDialog;
	private ArrayList<CItem> beeList=new ArrayList<CItem>();
	private ArrayList<CItem> materialList=new ArrayList<CItem>();

	public String getLocalNumber() {
		TelephonyManager tManager = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);
		String number = tManager.getLine1Number();
		return number;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add2);
		appContext=(AppContext)getApplication();
		initView();	
		
		//从本地加载参数信息，并排序。
		
		beeList=appContext.loadType("beetype");
		if(beeList.size()<1){
			
			beeList=appContext.loadTypeFromAssets("beetype");
		}		
		Collections.sort(beeList,new CompareID());
		
		materialList=appContext.loadType("material");
		if(materialList.size()<1){
			
			materialList=appContext.loadTypeFromAssets("material");
		}
		Collections.sort(materialList,new CompareID());
		
		guiGeList=appContext.loadType("guige");
		if(guiGeList.size()<1){
			
			guiGeList=appContext.loadTypeFromAssets("guige");
		}      
		Collections.sort(guiGeList,new CompareID());
		
		//从本地加载参数信息，并排序。
		
		fillSpanData();

		//fillSpanData(); //暂时不能放在onresume里面
		infoService=appContext.getInforService();
		settings=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
		
		rawUrl=settings.getString("url", "");
		
		userId=settings.getString("uid", "");
		
	}

		
	 public void upload(JSONObject sms) {
	        JSONArray data = new JSONArray();
	        data.put(sms);
	        String url = rawUrl + /*"/Default.aspx"*/"/api.aspx";
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("data", data.toString()));
	        getHttpClient();

	        final String result = doPost(url, params);

	        BeeSource beeSource = new BeeSource();
	        beeSource.setA1(a1.getText().toString());
	        beeSource.setA2(a2.getText().toString());
	        beeSource.setA4(a4.getText().toString());
	        beeSource.setA5(_a5);
	        beeSource.setA6(a6.getText().toString());
	        beeSource.setA7(a7.getText().toString());
	        beeSource.setA8(_a8);
	        beeSource.setA9(a9.getText().toString());
	        beeSource.setA10(a10.getText().toString());
	        beeSource.setA11(a11.getText().toString());
	        beeSource.setA12(a12.getText().toString());
	        beeSource.setA13(a13.getText().toString());
	        beeSource.setA14(a14.getText().toString());
	        beeSource.setA15(a15.getText().toString());
	        beeSource.setA16(_a16);

	        if ( result.equals("处理完成")) {
	            beeSource.setStatus(Constants.UPLOAD_SUCCESS);
	            
	        }
	        if (this.id == null) {
	            infoService.add(beeSource);
	        } else {
	            infoService.update(beeSource, this.id);
	        }
	                
	        AddActivity0815.this.runOnUiThread(new Runnable() {
                public void run() {
                	progressUpload.dismiss();
        	        
                	Toast.makeText(AddActivity0815.this, result, Toast.LENGTH_SHORT).show();
                }
            });
}
	 
	    public HttpClient getHttpClient() {

	        this.httpParams = new BasicHttpParams();

	        HttpConnectionParams.setConnectionTimeout(httpParams, 120 * 1000);

	        HttpConnectionParams.setSoTimeout(httpParams, 120 * 1000);

	        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

	        HttpClientParams.setRedirecting(httpParams, true);
	        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
	        HttpProtocolParams.setUserAgent(httpParams, userAgent);

	        httpClient = new DefaultHttpClient(httpParams);

	        return httpClient;
	    }
	    
	    public String doPost(String url, List<NameValuePair> params) {

	        HttpPost httpRequest = new HttpPost(url);
	        System.out.println("url:" + url);
	        String strResult = "doPostError";

	        try {
	            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	            HttpResponse httpResponse = httpClient.execute(httpRequest);
	            if (httpResponse.getStatusLine().getStatusCode() == 200) {
	                strResult = EntityUtils.toString(httpResponse.getEntity());

	            } else {
	                strResult = "Error Response: "
	                        + httpResponse.getStatusLine().toString();
	            }
	        } catch (ClientProtocolException e) {
	            strResult = e.getMessage().toString();
	            e.printStackTrace();
	        } catch (IOException e) {
	            strResult = e.getMessage().toString();
	            e.printStackTrace();
	        } catch (Exception e) {
	            strResult = e.getMessage().toString();
	            e.printStackTrace();
	        }

	        Log.v("strResult", strResult);

	        return strResult;
	    }

		@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.button_next:
			
			if (!appContext.isNetworkConnected()) {
				
				UIHelper.showNetworkSetting(this);
				
				break;
			}
			
			if(!appContext.isLogin()){
	        	   Intent mIntent=new Intent(this,LoginDialog.class);
	        	   startActivity(mIntent);
	        	   break;
	           }
				
			if (!(new Validate().validateInput())) { //可以改进成form bean
				break;
			}	
			
			//later 改进 :判断是否改动,改动就保存，没改动，就不保存。
			beeSource=getBeeSource();
			beeSource.setStatus(Constants.NO_UPLOAD);	
			if(!appContext.SaveBeeSource(beeSource)){
				
				toast("保存失败");
				return;
			}
			
			/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择上传单位");
			
			if(appContext.mOrgList==null){
				
				Toast.makeText(AddActivity0815.this, "请更新上传单位参数", Toast.LENGTH_SHORT).show();		
				break;
			}
			
			ArrayAdapter<CItem> mOrgAdapter = new ArrayAdapter<CItem>(this, android.R.layout.simple_dropdown_item_1line, appContext.mOrgList);
			
			builder.setAdapter(mOrgAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

								beeSource.setUnitNumber(appContext.mOrgList.get(which).GetID());
								//添加
								beeSource.setUnitName(appContext.mOrgList.get(which).GetValue());		
								mProgressDialog=ProgressDialog.show(AddActivity0815.this, "上传", "正在上传蜜源信息。。。");
								mProgressDialog.setCancelable(true);
								
								new Thread() {								
									@Override
									public void run() {
										ArrayList<BeeSource> beelist = new ArrayList<BeeSource>();
										beelist.add(beeSource);
										
										Message msg =new Message();
										int what = 0;
										try {
											if(appContext.upLoadBeeSource(beelist)){
												
												for(int i=0;i<beelist.size();i++){
										    		beelist.get(i).setStatus(Constants.UPLOAD_SUCCESS);
										    		appContext.updateBeeSource(beelist.get(i));
										    	}									
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
										
									}}.start();
							
						}
					});
			builder.show();*/
			
			
			beeSource.setUnitNumber(appContext.orgId_choosed);
			//添加
			beeSource.setUnitName(appContext.orgValue_choosed);		
			mProgressDialog=ProgressDialog.show(AddActivity0815.this, "上传", "正在上传蜜源信息。。。");
			mProgressDialog.setCancelable(true);
			
			new Thread() {								
				@Override
				public void run() {
					ArrayList<BeeSource> beelist = new ArrayList<BeeSource>();
					beelist.add(beeSource);
					
					Message msg =new Message();
					int what = 0;
					try {
						if(appContext.upLoadBeeSource(beelist)){
							
							for(int i=0;i<beelist.size();i++){
					    		beelist.get(i).setStatus(Constants.UPLOAD_SUCCESS);
					    		appContext.updateBeeSource(beelist.get(i));
					    	}									
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
					
				}}.start();
					
			break;

		case R.id.button_save:
			if (!(new Validate().validateInput())) {
				break;
			}
			
			BeeSource beeSource=getBeeSource();
			
			Boolean isSuccessSave=false;
			
		try { 
			
            if(action!=null&&action.equals(Constants.BEE_ADD)){
            	
            	beeSource.setStatus(Constants.NO_UPLOAD);
            	
            	isSuccessSave=appContext.SaveBeeSource(beeSource);
            	
            }else if(action!=null&&action.equals(Constants.BEE_EDIT)){
            	
            	beeSource.setStatus(Constants.NO_UPLOAD);
            	isSuccessSave=appContext.updateBeeSource(beeSource);
            }
            
				if (isSuccessSave) {

					toast("保存或编辑成功");
					this.finish();
					Intent intent = new Intent(AddActivity0815.this,
							InfoListActivity.class);
					startActivity(intent);

				}else {
					
					toast("保存或编辑失败");
					
				}
									
			} catch (Exception ex) {
				
				ex.printStackTrace();
				toast("保存或者编辑出现异常");
			}
			break;
/*		case R.id.button_upload:
			
			if (!appContext.isNetworkConnected()) {
				
				Toast.makeText(this, "无法联网，请检查网络状态", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!(new Validate().validateInput())) {
				return;
			}
			
           if(!appContext.isLogin()){       	   

        	   Intent mIntent=new Intent(this,LoginDialog.class);
        	   startActivity(mIntent);
        	   return;
           }   
           
           phone=settings.getString("phoneNumber", "");
           
           if(phone==null||phone.equals("")){
        	   Toast.makeText(this, "无法获取该用户手机号，暂时不能上传。", Toast.LENGTH_SHORT).show();
        	   return;
           }
            showUnitDialog();
			break;*/
			
			
		case R.id.button_skip:
			
			mIntent = new Intent();
			mIntent.setAction(Constants.QUICK_PIC_ADD);
			startActivity(mIntent);
			AddActivity0815.this.finish();		
			break;
		}
	}

		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
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

					String memo = "SYR%s;DD%s;BH%s;ZL%s;SL%s;GG%s;SLIAO%s;QMKS%s;QMJS%s;SGRQ%s;SGDW%s;ND%s;lat%s;lng%s;CMJG%s;YYQK%s;FZ%s;ORGID%s";
					memo = String.format(memo, a2.getText().toString(), a4.getText()
							.toString(), a1.getText().toString(), _a5, a7.getText()
							.toString(), _a8, a6.getText().toString(), a12.getText()
							.toString(), a13.getText().toString(), a14.getText()
							.toString(), a15.getText().toString(), a9.getText()
							.toString(), 0, 0, a10.getText().toString(), a11.getText()
							.toString(), _a16,Constants.unitMap.get(whichUnit));

					sms = new JSONObject();
					try {
						sms.put("b", memo);
						sms.put("m", phone);
						sms.put("u", settings.getString("uid", ""));
						/*sms.put("ORGID",Constants.unitMap.get(whichUnit));*/
						
						progressUpload=ProgressDialog.show(AddActivity0815.this, "上传", "正在上传，请等待。。。");

						new Thread() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								upload(sms);
							}

						}.start();

					} catch (Exception ex) {
						ex.printStackTrace();
						showConfirmDialog("上传失败");
					}	
				}
			});
			
		 builder.show();

		}	
		
		
		public void initView(){
			
			a1 = (EditText) findViewById(R.id.a1);
			a2 = (EditText) findViewById(R.id.a2);
			a4 = (EditText) findViewById(R.id.a4);

			a6 = (EditText) findViewById(R.id.a6);
			a7 = (EditText) findViewById(R.id.a7);

			a9 = (EditText) findViewById(R.id.a9);
			a10 = (EditText) findViewById(R.id.a10);
			a11 = (EditText) findViewById(R.id.a11);
			a12 = (EditText) findViewById(R.id.a12);
			a13 = (EditText) findViewById(R.id.a13);
			a14 = (EditText) findViewById(R.id.a14);
			a15 = (EditText) findViewById(R.id.a15);
			a1.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
			a7.setInputType(EditorInfo.TYPE_CLASS_PHONE);
			a9.setInputType(EditorInfo.TYPE_CLASS_PHONE);
			a10.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
			DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false,
					true);
			a7.setKeyListener(numericOnlyListener);
			a9.setKeyListener(numericOnlyListener);
			a5 = (Spinner) findViewById(R.id.a5);
			a8 = (Spinner) findViewById(R.id.a8);
			a16 = (Spinner) findViewById(R.id.a16);
			saveButton=(Button)this.findViewById(R.id.button_save);
			uploadButton=(Button)this.findViewById(R.id.button_upload);
			saveButton.setOnClickListener(this);
			uploadButton.setOnClickListener(this);		
			
			nextButton=(Button)this.findViewById(R.id.button_next);
			nextButton.setOnClickListener(this);
			
			skipButton=(Button)this.findViewById(R.id.button_skip);
			
			skipButton.setOnClickListener(this);
			
			
			a12.setFocusable(false);
			a12.setFocusableInTouchMode(false);
			a12.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					final Calendar cd = Calendar.getInstance();
					Date date = new Date();
					if (a12.getText().toString().trim() != "") {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						try {
							date = sdf.parse(a12.getText().toString().trim());
						} catch (ParseException e) {
							e.printStackTrace(); 
						}
					}
					cd.setTime(date);
					new DatePickerDialog(AddActivity0815.this,
							new DatePickerDialog.OnDateSetListener() {
								public void onDateSet(DatePicker view, int year,
										int monthOfYear, int dayOfMonth) {
									a12.setText(year + "-" + (monthOfYear + 1)
											+ "-" + dayOfMonth);
								}
							}, cd.get(Calendar.YEAR), cd.get(Calendar.MONTH), cd
									.get(Calendar.DAY_OF_MONTH)).show();
				}
			});

			a13.setFocusable(false);
			a13.setFocusableInTouchMode(false);
			a13.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					final Calendar cd2 = Calendar.getInstance();
					Date date2 = new Date();
					if (a13.getText().toString().trim() != "") {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						try {
							date2 = sdf.parse(a13.getText().toString().trim());
						} catch (ParseException e) {
							e.printStackTrace(); 
						}
					}
					cd2.setTime(date2);
					new DatePickerDialog(AddActivity0815.this,
							new DatePickerDialog.OnDateSetListener() {
								public void onDateSet(DatePicker view, int year,
										int monthOfYear, int dayOfMonth) {
									a13.setText(year + "-" + (monthOfYear + 1)
											+ "-" + dayOfMonth);
								}
							}, cd2.get(Calendar.YEAR), cd2.get(Calendar.MONTH), cd2
									.get(Calendar.DAY_OF_MONTH)).show();
				}
			});

			a14.setFocusable(false);
			a14.setFocusableInTouchMode(false);
			a14.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					final Calendar cd3 = Calendar.getInstance();
					Date date3 = new Date();
					if (a14.getText().toString().trim() != "") {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						try {
							date3 = sdf.parse(a14.getText().toString().trim());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					cd3.setTime(date3);
					new DatePickerDialog(AddActivity0815.this,
							new DatePickerDialog.OnDateSetListener() {
								public void onDateSet(DatePicker view, int year,
										int monthOfYear, int dayOfMonth) {
									a14.setText(year + "-" + (monthOfYear + 1)
											+ "-" + dayOfMonth);
								}
							}, cd3.get(Calendar.YEAR), cd3.get(Calendar.MONTH), cd3
									.get(Calendar.DAY_OF_MONTH)).show();
				}
			});
		}
		
		public void fillSpanData(){
			
			ArrayAdapter<CItem> myaAdapter = new ArrayAdapter<CItem>(this, android.R.layout.simple_dropdown_item_1line, materialList/*appContext.getMaterialTypeList()*/);
            a5.setAdapter(myaAdapter);
            a5.setPrompt( "请选择原料种类：" );

            a5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    _a5 = ((CItem) a5.getSelectedItem()).GetID();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
            


            ArrayAdapter<CItem> myaAdapter3 = new ArrayAdapter<CItem>(this, android.R.layout.simple_dropdown_item_1line, beeList /*appContext.getBeeTypeList()*/);
            a16.setAdapter(myaAdapter3);
            a16.setPrompt( "请选择蜂种：" );

            a16.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                	_a16 =((CItem)a16.getItemAtPosition(position)).GetID();           	
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });

            ArrayAdapter<CItem> myaAdapter2 = new ArrayAdapter<CItem>(this, android.R.layout.simple_dropdown_item_1line, guiGeList/*appContext.getGuigeList()*/);
            a8.setAdapter(myaAdapter2);
            a8.setPrompt( "请选择规格：" );
            a8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    _a8 = ((CItem) a8.getSelectedItem()).GetID();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
		}

		@Override
		protected void onResume() {
			
			super.onResume();
			
			Log.d(TAG,"AddActivity  onResume");
			// TODO Auto-generated method stub
			action=getIntent().getAction();

			if(action!=null&&action.equals(Constants.BEE_EDIT)){
				
				fillEdit();
				uploadButton.setVisibility(View.GONE);
				
			}else if(action!=null&&action.equals(Constants.BEE_ADD)){
				
				uploadButton.setVisibility(View.GONE);
				
			}else if(action!=null&&action.equals(Constants.QUICK_BEE_ADD)){
				
				uploadButton.setVisibility(View.GONE);
				saveButton.setVisibility(View.GONE);
				nextButton.setVisibility(View.VISIBLE);
				skipButton.setVisibility(View.VISIBLE);		
			}
			
			
		}
		
		public void fillEdit(){
			
			Bundle extras = getIntent().getExtras();
	        if (extras != null) {
	            beeSource = (BeeSource) extras.get("beeSource");
	            
	            a1.setText(beeSource.getA1());
	            a2.setText(beeSource.getA2());
	            a4.setText(beeSource.getA4());
	            a6.setText(beeSource.getA6());
	            a7.setText(beeSource.getA7());


	            if (beeSource.getA5() != null && beeSource.getA5() != "") {
	                try {
	                    for(int i=0;i<materialList.size();i++){
	                        if(materialList.get(i).GetID().equals(beeSource.getA5()))  {
	            	   
	                            a5.setSelection(i);
	                            Log.d(TAG,"setSelection "+ materialList.get(i).GetID());
	                        }
	                    }
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	            }

	            if (beeSource.getA8() != null && beeSource.getA8() != "") {
	                for(int i=0;i<guiGeList.size();i++){
	                    if(guiGeList.get(i).GetID().equals(beeSource.getA8()))  {
	                        a8.setSelection(i);
	                        Log.d(TAG,"setSelection "+ guiGeList.get(i).GetID());
	                    }
	                }
	            }

	            if (beeSource.getA16() != null && beeSource.getA16() != "") {
	                for(int i=0;i<beeList.size();i++){
	                    if(beeList.get(i).GetID().equals(beeSource.getA16()))  {
	                        a16.setSelection(i);
	                        Log.d(TAG,"setSelection "+ beeList.get(i).GetID());
	                    }
	                }
	            }
	            
	            a9.setText(beeSource.getA9());
	            a10.setText(beeSource.getA10());
	            a11.setText(beeSource.getA11());
	            a12.setText(beeSource.getA12());
	            a13.setText(beeSource.getA13());
	            a14.setText(beeSource.getA14());
	            a15.setText(beeSource.getA15());
	            this.id = beeSource.getId();
	        }
		}

			public BeeSource getBeeSource(){
				
				//add
				  if(beeSource==null){
					  
					  beeSource = new BeeSource();
				  }
				//add		
				beeSource.setA1(a1.getText().toString());
				beeSource.setA2(a2.getText().toString());
				beeSource.setA4(a4.getText().toString());
				beeSource.setA5(_a5);
				beeSource.setA6(a6.getText().toString());
				beeSource.setA7(a7.getText().toString());
				beeSource.setA8(_a8);
				beeSource.setA9(a9.getText().toString());
				beeSource.setA10(a10.getText().toString());
				beeSource.setA11(a11.getText().toString());
				beeSource.setA12(a12.getText().toString());
				beeSource.setA13(a13.getText().toString());
				beeSource.setA14(a14.getText().toString());
				beeSource.setA15(a15.getText().toString());
				beeSource.setA16(_a16);
				/*if(id!=null){
	   			   beeSource.setId(id);		 //??
				}*/
				return beeSource;
			}
}
