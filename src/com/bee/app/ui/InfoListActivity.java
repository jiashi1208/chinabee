package com.bee.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;

import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.bee.R;
import com.bee.app.AppContext;
import com.bee.app.AppException;

import com.bee.app.adapter.ListViewBeeAdapter2;
import com.bee.app.bean.BeeSource;
import com.bee.app.db.InfoService;
import com.bee.common.Constants;
import com.bee.common.UIHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InfoListActivity extends Activity  implements View.OnClickListener,AdapterView.OnItemClickListener{

    private ArrayList<BeeSource> list;
    ArrayList<HashMap<String,String>> list2 = new ArrayList<HashMap<String,String>>();
    private ListViewBeeAdapter2 adapter;
	protected static final String TAG = "chinabee";
		
    private BeeSource beeSource;
    private InfoService infoService;
    
    protected BroadcastReceiver mFinishReceiver;
	private ListView beeListView;
	
	private int checkNum;     // 记录选中的条目数量
	private TextView tv_show; // 用于显示选中的条目数量
	private Button deleteBtn;
	private Button uploadBtn;
	private HashSet<String> idSet;
	
	private AsyncTask<HashSet<String>, Integer, Void> deleteWork;
	private LinearLayout beelist;
	private RelativeLayout noinfo;
	private BasicHttpParams httpParams;
	private DefaultHttpClient httpClient;
	private ProgressDialog uploadProgress;
	private AppContext appContext;
	private int whichUnit;
	private HashMap<Integer, String> unitMap;
	private String rawUrl;
	private CheckBox selectChk;
	private SharedPreferences settings;
	private TextView addBtn;
	private Intent mIntent;
	private String phone;
	private RelativeLayout rl_pd_btn;
	private RelativeLayout rl_sd_btn;
	private TextView btn_pd;
	private TextView btn_sd;
	private TextView tv_tip_pd;
	private TextView tv_tip_sd;
	public int flowType;
	private TextView head_title;
	
	
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){			
			case 1:		
				if(uploadProgress!=null){
					uploadProgress.dismiss();
				}	
				Toast.makeText(InfoListActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
				fillData();
				dataChanged();
				break;
				
			case 0:
				
				if(uploadProgress!=null){
					uploadProgress.dismiss();
				}
				Toast.makeText(InfoListActivity.this, "上传失败", Toast.LENGTH_SHORT).show();				
			   break;
			   
			case -1:				
				if(uploadProgress!=null){
					uploadProgress.dismiss();
				}
             	((AppException)msg.obj).makeToast(InfoListActivity.this);	
             	dataChanged();
			}

		}	
	};

    private void fillData(){ //查询数据库
    	/*list.clear();*/
    	checkNum=0;
        /*list=infoService.findAll();*/	
    	list=infoService.findAll(flowType); //按照status查询蜜源列表
    	
        if(list.size()==0||list==null){
        	
         	beelist.setVisibility(View.GONE);
        	noinfo.setVisibility(View.VISIBLE); 
        	return;    	
        }else{
        	
        	beelist.setVisibility(View.VISIBLE);
        	noinfo.setVisibility(View.GONE); 
        
        
        list2.clear();
        for(int i=0;i<list.size();i++)
        {
            BeeSource beeSource=list.get(i);
            HashMap<String,String> item = new HashMap<String,String>();
            item.put( "sourceid",beeSource.getA1());
            item.put( "edittime",beeSource.getEditTime());
            if(beeSource.getStatus().equals("0")){
            	
            	item.put("status", "未上传");
            }else{
            	
            	item.put("status", "已上传给");
            }
            
            item.put("flag", "false");
            item.put("id", String.valueOf(beeSource.getId()));
            
            item.put("unitname", beeSource.getUnitName());       
            list2.add(item);      
        }
        
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_beelist);
        
        initView();       
        uploadBtn.setOnClickListener(this);
        
        appContext=(AppContext)getApplication();
        
        infoService=appContext.getInforService();
        
        registerForContextMenu(beeListView); //需要注册
        
		settings = getSharedPreferences(Constants.SETTINGS_NAME,
				MODE_PRIVATE);

		rawUrl = settings.getString("url", "");
        
        deleteWork = new AsyncTask<HashSet<String>, Integer, Void>() {

			@Override
			protected Void doInBackground(HashSet<String>... params) {
				// TODO Auto-generated method stub
				HashSet<String> idSet=params[0];
				for(String id:idSet){
					infoService.deleteInfo(Integer.valueOf(id));		
				} 		
				fillData();
				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				Log.d(TAG,"delete Work,onPostExecute");
				dataChanged();
			}	
	
        };
        idSet = new HashSet<String>();//记录被选择的ID
        adapter = new ListViewBeeAdapter2(list2,this);          
        beeListView.setAdapter(adapter);
		IntentFilter filter = new IntentFilter();
		filter.addAction("finish");	
		mFinishReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				if ("finish".equals(intent.getAction())) {

					Log.e("#########", "I am " + getLocalClassName()

					+ ",now finishing myself...");

					finish();
				}

			}
		};
		registerReceiver(mFinishReceiver, filter);
				
		beeListView.setOnItemClickListener(this);
		
		// add
		PdOnClickListener localPdOnClickListener = new PdOnClickListener();
		rl_pd_btn.setOnClickListener(localPdOnClickListener);
		SdOnClickListener localSdOnClickListener = new SdOnClickListener();
		rl_sd_btn.setOnClickListener(localSdOnClickListener);
		// add
    }

  

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {	
		
		case R.id.button_add:
			
			mIntent=new Intent();
			mIntent.setAction(Constants.BEE_ADD);
			startActivityForResult(mIntent, Constants.REQUEST_ADD);
			/*startActivity(mIntent);*/
			
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
			
			if(idSet.size()==0){
				Toast.makeText(InfoListActivity.this, "还未选择需要上传的蜜源。", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (!appContext.isNetworkConnected()) {
				/*Toast.makeText(InfoListActivity.this, "无法联网，请检查网络状态", Toast.LENGTH_SHORT).show();*/
				UIHelper.showNetworkSetting(this);
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
			
            if(appContext.mOrgList==null){
				
				Toast.makeText(InfoListActivity.this, "请更新上传单位参数", Toast.LENGTH_SHORT).show();
				break;
			}
			
	        showUnitDialog();	
			break;
		
		}
	}

	
	// 刷新listview和TextView的显示
	private void dataChanged() {   //页面刷新

		if(adapter!=null){		
			adapter.notifyDataSetChanged();

		}	
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
		//fillData();
		dataChanged();
	}
	
	
	 private class DeleteWork extends  AsyncTask<HashSet<String>, Integer, Void>{

			@Override
			protected Void doInBackground(HashSet<String>... params) {
				// TODO Auto-generated method stub
				HashSet<String> idSet=params[0];
				for(String id:idSet){
					infoService.deleteInfo(((Integer.valueOf(id))));		
				} 						
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				fillData();
				dataChanged();
			}		
		    }
	 
	 private class uploadWork extends  AsyncTask<HashSet<String>, Integer, String>{

			private JSONObject sms;


			@Override
			protected String doInBackground(HashSet<String>... params) {
				// TODO Auto-generated method stub
				HashSet<String> idSet=params[0];	
				ArrayList<BeeSource> beelist=infoService.findByIds(idSet);
				idSet.clear();
				
				JSONArray data = new JSONArray();
				for(int i=0;i<beelist.size();i++){
					
				    String memo = "SYR%s;DD%s;BH%s;ZL%s;SL%s;GG%s;SLIAO%s;QMKS%s;QMJS%s;SGRQ%s;SGDW%s;ND%s;lat%s;lng%s;CMJG%s;YYQK%s;FZ%s;ORGID%s";
					memo = String.format(memo, beelist.get(i).getA2(), beelist.get(i).getA4(), beelist.get(i).getA1(), beelist.get(i).getA5(), beelist.get(i).getA7(), 
							beelist.get(i).getA8(), beelist.get(i).getA6(), beelist.get(i).getA12(), beelist.get(i).getA13(), beelist.get(i).getA14(), 
						   beelist.get(i).getA15(), beelist.get(i).getA9(), 0, 0, beelist.get(i).getA10(), beelist.get(i).getA11(),beelist.get(i).getA16(),appContext.mOrgList.get(whichUnit).GetID()/*Constants.unitMap.get(whichUnit)*/);
					
					sms = new JSONObject();
					
					try {
						sms.put("b", memo);
						sms.put("m", phone);
						sms.put("u", settings.getString("uid", ""));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
					    data.put(sms);
				}
				
				String url =rawUrl + "/api.aspx";
			    List<NameValuePair> params2 = new ArrayList<NameValuePair>();
			    params2.add(new BasicNameValuePair("data", data.toString()));
			    getHttpClient();
			    final String result = doPost(url, params2);
			    
			    if(result.equals("处理完成")){
			    	
			    	for(int i=0;i<beelist.size();i++){
			    		//添加
			    		beelist.get(i).setUnitName(appContext.mOrgList.get(whichUnit).GetValue());
			    		//添加 0815
			    		
			    		beelist.get(i).setStatus(Constants.UPLOAD_SUCCESS);
			    		infoService.update(beelist.get(i), beelist.get(i).getId());
			    	}   	
			    }	       
				return result;
			}
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				uploadProgress.cancel();
				
				if(result.equals("处理完成")){
					Toast.makeText(InfoListActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
				}else{
					
					Toast.makeText(InfoListActivity.this, "上传失败，请重新上传。", Toast.LENGTH_SHORT).show();
				}
				fillData();
				dataChanged();
			}		
		    }
	
	
	 public HttpClient getHttpClient() {

	        // 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）

	        this.httpParams = new BasicHttpParams();

	        // 设置连接超时和 Socket 超时，以及 Socket 缓存大小

	        HttpConnectionParams.setConnectionTimeout(httpParams, 120 * 1000);

	        HttpConnectionParams.setSoTimeout(httpParams, 120 * 1000);

	        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

	        // 设置重定向，缺省为 true

	        HttpClientParams.setRedirecting(httpParams, true);

	        // 设置 user agent

	        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
	        HttpProtocolParams.setUserAgent(httpParams, userAgent);

	        // 创建一个 HttpClient 实例

	        // 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient

	        // 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient

	        httpClient = new DefaultHttpClient(httpParams);

	        return httpClient;
	    }	   
	 
	 
	     public String doPost(String url, List<NameValuePair> params) {

	        /* 建立HTTPPost对象 */
	        HttpPost httpRequest = new HttpPost(url);
	        System.out.println("url:" + url);
	        String strResult = "doPostError";

	        try {
	            /* 添加请求参数到请求对象 */
	            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	            /* 发送请求并等待响应 */
	            HttpResponse httpResponse = httpClient.execute(httpRequest);
	            /* 若状态码为200 ok */
	            if (httpResponse.getStatusLine().getStatusCode() == 200) {
	                /* 读返回数据 */
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
	 
	 
     public  void showUnitDialog(){
			
			whichUnit=-1;		
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle("请选择上传单位");
			
			ArrayAdapter<CItem> mOrgAdapter = new ArrayAdapter<CItem>(this, android.R.layout.simple_dropdown_item_1line, appContext.mOrgList);
			/*ArrayAdapter<CItem> mOrgAdapter = new ArrayAdapter<CItem>(this, android.R.layout.select_dialog_singlechoice, appContext.mOrgList);*/
			builder.setAdapter(mOrgAdapter, new DialogInterface.OnClickListener() {
			/*builder.setSingleChoiceItems(mOrgAdapter, 0, new DialogInterface.OnClickListener() {*/

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub		
					whichUnit = which;
					
					uploadProgress=ProgressDialog.show(InfoListActivity.this, "上传中", "正在上传蜜源信息...");
					uploadProgress.setCancelable(true);
													
					new Thread() {								
						@Override
						public void run() {
						
							ArrayList<BeeSource> beelist=infoService.findByIds(idSet);
							for(BeeSource beeSource:beelist){
								
								beeSource.setUnitNumber(appContext.mOrgList.get(whichUnit).GetID());
								beeSource.setUnitName(appContext.mOrgList.get(whichUnit).GetValue());
							}
							idSet.clear();
							
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
			
	  /*   builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				fillData();
				Log.d(TAG,"Alert Dialog press Cancell");
				
			}
		});*/
			
		 builder.show();	
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
		
		 beeSource=((BeeSource)list.get(itemInfo.position));
		 switch (item.getItemId()) {
	        case R.id.viewInfo:
	        	
	          if(flowType==0){
	        	  
	        	  Intent mIntent= new Intent();
		          mIntent.setAction(Constants.BEE_EDIT);
	              Bundle bundle = new Bundle();
	              bundle.putSerializable("beeSource", beeSource);
	              mIntent.putExtras(bundle);
	              startActivity(mIntent);
	        	  
	        	  
	          }else if(flowType==1){
	        	  
	        	  Intent mIntent= new Intent(this,BeeSourceView.class);
	        	  
	              Bundle bundle = new Bundle();
	              bundle.putSerializable("beeSource", beeSource);
	              mIntent.putExtras(bundle);
	              startActivity(mIntent);
	          }    
	          return true;
	    }
		
		return super.onContextItemSelected(item);
	}
	
	
	
     
     public void initView(){
    	 
    	beeListView=(ListView)findViewById(R.id.beelistview);
        tv_show = (TextView) findViewById(R.id.tv);
 		beelist =(LinearLayout)findViewById(R.id.beelist);
 		noinfo=(RelativeLayout)findViewById(R.id.noinfo);
 		
 		head_title=(TextView)findViewById(R.id.pointlist_head_title);
 		
 		head_title.setText("蜜源列表");

 		
        selectChk=(CheckBox)findViewById(R.id.selectchk);
 		
 		selectChk.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

 			@Override
 			public void onCheckedChanged(CompoundButton buttonView,
 					boolean isChecked) {
 				// TODO Auto-generated method stub
 				
 				if(selectChk.isChecked()){
 					
 					for (int i = 0; i < list.size(); i++) {
 						list2.get(i).put("flag", "true");
 						idSet.add(list2.get(i).get("id"));
 					}
 					checkNum = list.size();
 					dataChanged();
 					
 				}else{
 					
 					for (int i = 0; i < list.size(); i++) {
 						if (list2.get(i).get("flag").equals("true")) {
 							list2.get(i).put("flag", "false");
 							checkNum--;
 						}
 					}
 					idSet.clear();
 					dataChanged();
 				}
 					
 				}
 				
 			});

         deleteBtn=(Button)this.findViewById(R.id.button_delete);       
         addBtn=(TextView)this.findViewById(R.id.button_add);
         addBtn.setOnClickListener(this);
         deleteBtn.setOnClickListener(this);
         uploadBtn=(Button)this.findViewById(R.id.button_upload);
         
         
       //区分上传 和未上传
     	 this.rl_pd_btn = ((RelativeLayout)findViewById(R.id.order_btn_pd));
         this.rl_sd_btn = ((RelativeLayout)findViewById(R.id.order_btn_sd));
         this.btn_pd = ((TextView)findViewById(R.id.order_tv_pd));
         this.btn_sd = ((TextView)findViewById(R.id.order_tv_sd));
         this.tv_tip_pd = ((TextView)findViewById(R.id.order_tv_tip_pd));
         this.tv_tip_sd = ((TextView)findViewById(R.id.order_tv_tip_sd));
          
        //区分上传 和未上传
    	 	 
     }

     
     
 	@Override
 	protected void onResume() {
 		// TODO Auto-generated method stub
 		super.onResume();
 		
 		checkNum=0;
 	/*	flowType=0;*/	
		Log.d(TAG,"InfoListActivity onResume");     
        fillData();
        dataChanged();
        
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
		//	registerForContextMenu(beeListView);
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
		//    unregisterForContextMenu(beeListView);
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
		
		Log.d(TAG,"infoList onActivityResult");
	}
	
}
