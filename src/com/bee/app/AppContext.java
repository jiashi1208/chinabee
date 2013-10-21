package com.bee.app;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.baidu.location.LocationClient;

import com.bee.app.api.ApiClient;
import com.bee.app.bean.BeeSource;
import com.bee.app.bean.PicBean;
import com.bee.app.bean.Picture;
import com.bee.app.bean.UserPoint;
import com.bee.app.db.InfoService;
import com.bee.app.ui.CItem;
import com.bee.common.Constants;
import com.bee.common.HttpPostUtil;
import com.bee.common.WebserviceUtil;
import com.bee.log.LogUtil;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class AppContext extends Application {

	private static final Integer UPLOAD_SUCCESS = 1;
	private InfoService infoService;
	public LocationClient mLocationClient = null;
	public static String TAG = "chinabee";
	
	public TextView mPositionText;
	
	public String mPhoneNumber;
	
	public ArrayList<CItem> mOrgList;
	
	public String rawURL;
	
	public ArrayList<CItem> materialTypeList=new ArrayList<CItem>();
	
	public ArrayList<CItem> beeTypeList=new ArrayList<CItem>();
	
	public ArrayList<CItem> guiGeList =new ArrayList<CItem>();
	
	private boolean login = false;	//登录状态
	public String loginUid = "";	//登录用户的id
	
	public String mLocation;
	public double mLongitude;
	public double mLatitude;
	
	public JSONObject   danWeiObject;
	
	public JSONObject   beeTypeObject;
	
	public JSONObject   guiGeObject;
	
	public JSONObject   materialTypeObject;
	
	private SharedPreferences settings;
	private String phone;
	private FileOutputStream fos;
	
	private Uri uri;
	public String orgId_choosed;
	public String orgValue_choosed;
	public String mSessionId;
	public String mCookies;
	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		 super.onCreate();
		 
		 LogUtil.logoff=false;
		 LogUtil.level=Log.DEBUG;
		 
		 LogUtil.saveDirName = "/sdcard/chinabeelog/";
		 
		
	     infoService=new InfoService(this);
		 
		 settings=getSharedPreferences(Constants.SETTINGS_NAME,MODE_PRIVATE);
		 
		 rawURL=settings.getString("url", "");
		 
		 if(rawURL==null||rawURL.equals("")){
			 
			 Editor editor=settings.edit();
			 editor.putString("url", Constants.URL_INIT);	 
			 editor.commit(); 
			 rawURL=Constants.URL_INIT;
		 }
	
		 asyncFresh(this);//从网络加载

	}


	public InfoService getInforService() {
		return infoService;
	}
	
    public String getLocalNumber() {
        TelephonyManager tManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String number = tManager.getLine1Number();
        return number;
    }
    
    
    public ArrayList<CItem> getGuigeList(){
    	return guiGeList;
    }
    
	public void setGuigeList(ArrayList<CItem> list) {
		guiGeList=list;
	}

	public ArrayList<CItem> getMaterialTypeList() {
		return materialTypeList;
	}

	public void setMaterialTypeList(ArrayList<CItem> list) {
		materialTypeList=list;
	}

	public ArrayList<CItem> getBeeTypeList() {
		return beeTypeList;
	}

	public void setBeeTypeList(ArrayList<CItem> list){
		beeTypeList=list;
	}
	
	/**
	 * 检测网络是否可用
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
	
	public boolean isLogin() {
		// TODO Auto-generated method stub
		String userId=settings.getString("uid", "");
		if(userId!=null&&(!userId.equals(""))){
			
			return true;
		} else{
			return false;
		}	 
	}
	
	public void setLoginStatus(Boolean status){
		
		login=status;
	}
	
	
	public void asyncFresh(final AppContext appContext){
		
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				SharedPreferences sp=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
				String url=sp.getString("url", "");
	
				if (url != null) {

					String webserviceUrl =url + "/TypeInfo.asmx";
					
					try {
						materialTypeList = WebserviceUtil.getIno(
								"a35bb0f7-e557-4158-8f75-9f9430225f8f", webserviceUrl);
						

						guiGeList = WebserviceUtil.getIno(
								"6c00d7a4-b827-476d-b72c-44920a93e2ab", webserviceUrl);
						beeTypeList = WebserviceUtil.getIno(
								"53639f9c-3532-4805-9ce8-d524cd6da073", webserviceUrl);
						
						settings=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
						
						
						String phoneNumber=settings.getString("phoneNumber", "");
						
						if(phoneNumber!=null){
							
							mOrgList=WebserviceUtil.getORG("7b77c756-bad9-441b-9904-7076973eb68c",phoneNumber, webserviceUrl);
							if (mOrgList != null
									&& mOrgList.size() > 0) {
							}	
						}
						
						if (materialTypeList != null
								&& materialTypeList.size() > 0) {

							saveBeeType();
						}
						
						if(guiGeList!=null&&guiGeList.size()>0){
						saveGuige();
						}
						
						if(beeTypeList!=null&&beeTypeList.size()>0){
						saveMaterial();
						}				 

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}		
				}	
		}.start();
		
	}
	
	
	public void asyncFresh(){
		

		// TODO Auto-generated method stub
		
		SharedPreferences sp=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
		String url=sp.getString("url", "");

		if (url != null) {

			String webserviceUrl =url + "/TypeInfo.asmx";
			
			try {
				materialTypeList = WebserviceUtil.getIno(
						"a35bb0f7-e557-4158-8f75-9f9430225f8f", webserviceUrl);
				

				guiGeList = WebserviceUtil.getIno(
						"6c00d7a4-b827-476d-b72c-44920a93e2ab", webserviceUrl);
				beeTypeList = WebserviceUtil.getIno(
						"53639f9c-3532-4805-9ce8-d524cd6da073", webserviceUrl);
				
				settings=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
				
				
				String phoneNumber=settings.getString("phoneNumber", "");
				
				if(phoneNumber!=null){
					
					mOrgList=WebserviceUtil.getORG("7b77c756-bad9-441b-9904-7076973eb68c",phoneNumber, webserviceUrl);
					if (mOrgList != null
							&& mOrgList.size() > 0) {
					}	
				}
				
				if (materialTypeList != null
						&& materialTypeList.size() > 0) {

					saveBeeType();
				}
				
				if(guiGeList!=null&&guiGeList.size()>0){
				saveGuige();
				}
				
				if(beeTypeList!=null&&beeTypeList.size()>0){
				saveMaterial();
				}				 

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
		
		
	}
	
	//刷新单位
	
	public void asyncFreshforOrg(){
		
		SharedPreferences sp=getSharedPreferences(Constants.SETTINGS_NAME, MODE_PRIVATE);
		String url=sp.getString("url", "");
		
		String webserviceUrl=null;

		if (url != null) {

			webserviceUrl =url + "/TypeInfo.asmx";
		}
		
		String phoneNumber=settings.getString("phoneNumber", "");
		
		if(phoneNumber!=null){
			
			try {
				
				mOrgList=WebserviceUtil.getORG("7b77c756-bad9-441b-9904-7076973eb68c",phoneNumber, webserviceUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	
	
	
	
    protected void saveOrgList() {
		// TODO Auto-generated method stub
		
	}


	public void saveMaterial(){
    	
    	Properties pros=new Properties();	
    	for (int i = 0; i < materialTypeList.size(); i++) {

			pros.setProperty(materialTypeList.get(i).GetID(), materialTypeList.get(i).GetValue());
		}
    	try {
			FileOutputStream fos=this.openFileOutput("material",this.MODE_PRIVATE);
			pros.store(fos, "");
			fos.flush();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try {
				if(fos!=null){
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    		
    }
    
    public void saveGuige(){
    	
    	Properties pros=new Properties();	
    	for (int i = 0; i < guiGeList.size(); i++) {

			pros.setProperty(guiGeList.get(i).GetID(), guiGeList.get(i).GetValue());
		}
    	try {
			FileOutputStream fos=this.openFileOutput("guige",this.MODE_PRIVATE);
			
			pros.store(fos, "");
			fos.flush();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try {
				if(fos!=null){
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 	 	
    }
    
    public void saveBeeType(){
    	
    	Properties pros=new Properties();	
    	for (int i = 0; i < beeTypeList.size(); i++) {

			pros.setProperty(beeTypeList.get(i).GetID(), beeTypeList.get(i).GetValue());
		}
    	try {
			FileOutputStream fos=this.openFileOutput("beetype",this.MODE_PRIVATE);
			pros.store(fos, "");
			fos.flush();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try {
				if(fos!=null){
					fos.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	 	
    }
    
    
     public boolean SaveBeeSource(BeeSource beeSource){ //判断是都有ID,有ID就更新。
    	 
		long rowId;
		
		
		if(beeSource.getId()!=null&&beeSource.getId()!=""){
			
			return infoService.updateBeeSource(beeSource);
			
			
		}else{
			
			rowId = infoService.add(beeSource);
			
			
			Log.d(TAG,"SaveBeeSource  result "+ rowId);

			if (rowId != -1) {
				
				beeSource.setId(String.valueOf(rowId));

				return true;
			}
		}
		
		return false;
    	 
     }
     
  /*   public Boolean upLoadBeeSource(BeeSource beeSource){
    	 
    	 
    	String phone=settings.getString("phoneNumber", "");
    	
    	String rawUrl=settings.getString("url", "");
    	
		String memo = "SYR%s;DD%s;BH%s;ZL%s;SL%s;GG%s;SLIAO%s;QMKS%s;QMJS%s;SGRQ%s;SGDW%s;ND%s;lat%s;lng%s;CMJG%s;YYQK%s;FZ%s;ORGID%s";
		memo = String.format(memo, beeSource.getA2(), beeSource.getA4(),
				beeSource.getA1(), beeSource.getA5(), beeSource.getA7(),
				beeSource.getA8(), beeSource.getA6(), beeSource.getA12(),
				beeSource.getA13(), beeSource.getA14(), beeSource.getA15(),
				beeSource.getA9(), 0, 0, beeSource.getA10(),
				beeSource.getA11(), beeSource.getA16(),
				beeSource.getUnitNumber());
		
		JSONObject sms = new JSONObject();
		try {
			
			sms.put("b", memo);
			sms.put("m", phone);
			sms.put("u", settings.getString("uid", ""));
			
			JSONArray data = new JSONArray();
	        data.put(sms);
	        String url = rawUrl +"/api.aspx";
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("data", data.toString()));
	        HttpUtil.getHttpClient();

	        final String result = HttpUtil.doPost(url, params);
	        
	        if (result.equals("处理完成")) {
	        	beeSource.setStatus(Constants.UPLOAD_SUCCESS);
	        	updateBeeSource(beeSource);
	        	return true;            
	        }
	        		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 return false;
     
   //    return ApiClient.uploadBeanSource(beeSource);	 
     
     }
     */
     
     
     public Boolean upLoadBeeSource(ArrayList<BeeSource> beelist) throws AppException{
    	 
    	String phone=settings.getString("phoneNumber", "");
     	
     	String rawUrl=settings.getString("url", "");
     	
     	String userId=settings.getString("uid", "");
     	
     	String url = rawUrl +"/api.aspx";
    	 
    	 
    	return  ApiClient.uploadBeeSource(this,beelist,url, phone,userId);
    	 
    	
     }

     private boolean uploadFile(String fileName,int unit){
    	 
    	    String rawUrl=settings.getString("url", "");
    	    String userId=settings.getString("uid", "");
    	    
	        String result="0";
	        String urlStr =rawUrl + "/upload.aspx";

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
	            if(unit!=-1){
	            	u.addTextParameter("ORGID", Constants.unitMap.get(unit));
	            }
	            //上传单位
	            
	            //上传经纬度
	            u.addTextParameter("lng", String.valueOf(mLongitude));
	            u.addTextParameter("lat", String.valueOf(mLatitude));
	            u.addTextParameter("picName", fileName);
	            
	            byte[] b = u.send();
	            result = new String(b);
	            if(result.equals("1")){
	            	return true;
	            	
	            }else if(result.equals("0")){
	            	
	            	return false;
	            }
	        }catch(Exception e){
	            e.printStackTrace();
	            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	            dialog.setTitle("发送失败");
	            dialog.setMessage("发送失败");
	            dialog.show();
	        }finally{
	        }
	        return false;
	    }
     
     
	public boolean savePic(PicBean picBean, Bitmap bitmap) {

		File file = new File(Constants.PIC_PATH);
		if (!file.exists()) {
			file.mkdirs();// 创建文件夹
		}
		String fileName = Constants.PIC_PATH + picBean.getTitle() + ".jpg";

		String filePath = null;
		Uri uri;

		try {
			fos = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			uri = Uri.fromFile(new File(fileName));
			filePath = uri.getPath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		picBean.setPath(filePath);

		long rowId = infoService.savePic(picBean);

		if (rowId != -1) {

			picBean.setId(String.valueOf(rowId));

			return true;
		}
		return false;

	}
        
        public boolean uploadPic(PicBean picBean){
        	
        	String rawUrl=settings.getString("url", "");
    	    String userId=settings.getString("uid", "");
    	    
	        String result="0";
	        String hostPath =rawUrl + "/upload.aspx";
	        File file = new File(picBean.getPath());
	        try{
	            HttpPostUtil u = new HttpPostUtil(hostPath);
	            byte[] buffer;
	            buffer = HttpPostUtil.getBytesFromFile(file);
	            u.addFileParameter("img", buffer);

	            if(userId!=null&&userId!=""){
	                u.addTextParameter("u", userId);
	            }
	            
	            //上传经纬度
	            u.addTextParameter("lng",picBean.getLng());
	            u.addTextParameter("lat", picBean.getLat());
	            u.addTextParameter("picName", picBean.getTitle());
	            
	            byte[] b = u.send();
	            result = new String(b);
	            if(result.equals("1")){
	            	return true;
	            	
	            }else if(result.equals("0")){
	            	
	            	return false;
	            }
	        }catch(Exception e){
	            e.printStackTrace();
	            return false;
	        }
	        return true;
        }
     
        
        public boolean uploadPosition(UserPoint userPoint) throws AppException{
        	
        	String rawUrl=settings.getString("url", "");
    	    String userId=settings.getString("uid", "");
    	    
    	    
    	    String phone=settings.getString("phoneNumber", ""); //add
    	    
    	    return ApiClient.uploadPosition(this, rawUrl, userPoint,userId,phone);
        }
         
        
        public boolean modifyPassword(String new_password) throws AppException{
        	
        	String rawUrl=settings.getString("url", "");
    	    String userId=settings.getString("uid", "");
    	    String sessionId=this.mSessionId;
        	
    	    return ApiClient.modifyPassword(this, rawUrl, userId,new_password,sessionId);
        }
        
      public int modifyPassword2(String new_password) throws AppException{
        	
        	String rawUrl=settings.getString("url", "");
    	    String userId=settings.getString("uid", "");
    	    String sessionId=this.mSessionId;
        	
    	    return ApiClient.modifyPassword2(this, rawUrl, userId,new_password,sessionId);
        }
        
	public boolean saveUserPoint(UserPoint userPoint) {// 判断是否有ID，以免重复保存

		if (userPoint.getId() != null && userPoint.getId() != "") {

			return infoService.updateUserPoint(userPoint);
		}

		if (userPoint != null) {
			long rowId = infoService.addUserPoint(userPoint.getLat(),
					userPoint.getLng(), userPoint.getName(),
					userPoint.getStatus(),userPoint.getAddress());

			if (rowId != -1) {
				userPoint.setId(String.valueOf(rowId));
				return true;
			}

		}
		return false;
	}
        
        
        public void updateUserPoint(UserPoint userPoint,String status){
        	
        	infoService.updateUserPoint(userPoint.getId(), status); 	
        }
        
       public void updateUserPoint(UserPoint userPoint){
        	
        	infoService.updateUserPoint(userPoint); 	
        }
        
        
        public void updatePic(PicBean picBean,String status){

        	infoService.updatePic(picBean.getId(), status);
        }
        
        public void updatePic(PicBean picBean){

        	infoService.updatePic(picBean);
        }
        
        public void updatePic(Picture picture,String status){

        	infoService.updatePic(picture.getId(), status);
        }
        
        
       public Boolean updateBeeSource(BeeSource beeSource){
        	
        	return infoService.updateBeeSource(beeSource);        	
        }
       
       
       
	public ArrayList<CItem> loadType(String filename) {

		ArrayList<CItem> typeList = new ArrayList<CItem>();

		Properties pros = new Properties();
		try {
			FileInputStream fis = this.openFileInput(filename);
			pros.load(fis);

			if (pros.size() < 1) {
				return typeList;
			}

			Set<Entry<Object, Object>> sets = pros.entrySet();

			Iterator<Entry<Object, Object>> iterator = sets.iterator();

			while (iterator.hasNext()) {
				Entry<Object, Object> item = iterator.next();

				typeList.add(new CItem(item.getKey().toString(), item
						.getValue().toString()));

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return typeList;

	}
	
	
	public ArrayList<CItem> loadTypeFromAssets(String filename) {

		ArrayList<CItem> typeList = new ArrayList<CItem>();

		Properties pros = new Properties();
		try {
			
			InputStream is = this.getAssets().open(filename);
			pros.load(is);

			if (pros.size() < 1) {
				return typeList;
			}

			Set<Entry<Object, Object>> sets = pros.entrySet();

			Iterator<Entry<Object, Object>> iterator = sets.iterator();

			while (iterator.hasNext()) {
				Entry<Object, Object> item = iterator.next();

				typeList.add(new CItem(item.getKey().toString(), item
						.getValue().toString()));

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return typeList;

	}
	
}
