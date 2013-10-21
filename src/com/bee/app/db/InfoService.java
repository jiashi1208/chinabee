package com.bee.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


import com.bee.app.bean.BeeSource;
import com.bee.app.bean.PicBean;

import com.bee.app.bean.UserPoint;
import com.bee.app.ui.SystemInfo;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


public class InfoService {
    private DataBaseHelper dbHelper;
    private SQLiteDatabase db;
    
    public void updateUrl(String url){
        ContentValues cv = new ContentValues();
        cv.put("url", url);
        if(getSystemInfo()==null){
            this.db.insert("system_config", null, cv);
        }
        else{
            this.db.update("system_config", cv, "id=1", null);
        }
    }

    public void updateUserId(String userId){
        ContentValues cv = new ContentValues();
        cv.put("userId", userId);
        if(getSystemInfo()==null){
            this.db.insert("system_config", null, cv);
        }
        else{
            this.db.update("system_config", cv, "id=1", null);
        }
    }
    
    
    public void updatePhoneNumber(String phoneNumber){
    	
    	ContentValues cv=new ContentValues();
    	cv.put("phoneNumber", phoneNumber);
    	
    	if(getSystemInfo()==null){
            this.db.insert("system_config", null, cv);
        }
        else{
            this.db.update("system_config", cv, "id=1", null);
        }
    }

    public void updateLoginName(String loginName){
        ContentValues cv = new ContentValues();
        cv.put("loginName", loginName);
        if(getSystemInfo()==null){
            this.db.insert("system_config", null, cv);
        }
        else{
            this.db.update("system_config", cv, "id=1", null);
        }
    }

    public void updateLatLng(String lat,String lng){
        ContentValues cv = new ContentValues();
        cv.put("lat", lat);
        cv.put("lng", lng);
        if(getSystemInfo()==null){
            this.db.insert("system_config", null, cv);
        }
        else{
            this.db.update("system_config", cv, "id=1", null);
        }
    }
    
    
    public SystemInfo getSystemInfo(){
        try{
            Cursor cur = this.db.rawQuery("SELECT id,url,userId,lat,lng,loginName FROM system_config where id=?", new String[]{String.valueOf(1)});
            if (cur != null) {
                while (cur.moveToNext()) {
                    SystemInfo systemInfo=new SystemInfo();
                    systemInfo.setId(cur.getInt(0));
                    systemInfo.setUrl(cur.getString(1));
                    systemInfo.setUserId(cur.getString(2));
                    systemInfo.setLat(cur.getString(3));
                    systemInfo.setLng(cur.getString(4));
                    systemInfo.setLoginName(cur.getString(5));
                    if (!cur.isClosed()) {
                        cur.close();
                    }
                    return systemInfo;
                }
            }
        }
        catch (SQLiteException ex){
            ex.printStackTrace();

            Cursor cur = this.db.rawQuery("SELECT id,url,userId,lat,lng,loginName,phoneNumber FROM system_config where id=?", new String[]{String.valueOf(1)});
            if (cur != null) {
                while (cur.moveToNext()) {
                    SystemInfo systemInfo=new SystemInfo();
                    systemInfo.setId(cur.getInt(0));
                    systemInfo.setUrl(cur.getString(1));
                    systemInfo.setUserId(cur.getString(2));
                    systemInfo.setLat(cur.getString(3));
                    systemInfo.setLng(cur.getString(4));
                    systemInfo.setLoginName(cur.getString(5));
                    systemInfo.setPhoneNumber(cur.getString(6));
                    if (!cur.isClosed()) {
                        cur.close();
                    }
                    return systemInfo;
                }
            }
        }
        return null;
    }

    public InfoService(Context context) {
        dbHelper=new DataBaseHelper(context) ;
        db=dbHelper.getWritableDatabase();
    }

    public long add(BeeSource beeSource){
        ContentValues cv = new ContentValues();
        cv.put("a1", beeSource.getA1());
        cv.put("a2", beeSource.getA2());
        cv.put("a4", beeSource.getA4());
        cv.put("a5", beeSource.getA5());
        cv.put("a6", beeSource.getA6());
        cv.put("a7", beeSource.getA7());
        cv.put("a8", beeSource.getA8());
        cv.put("a9", beeSource.getA9());
        cv.put("a10", beeSource.getA10());
        cv.put("a11", beeSource.getA11());
        cv.put("a12", beeSource.getA12());
        cv.put("a13", beeSource.getA13());
        cv.put("a14", beeSource.getA14());
        cv.put("a15", beeSource.getA15());
        cv.put("a16", beeSource.getA16());
/*        cv.put("addTime", DateFormat.format("MM/dd/yy h:mmaa", Calendar.getInstance(Locale.CHINA)).toString());*/
        cv.put("addTime",  DateFormat.getDateTimeInstance().format(new Date()));
      /*  cv.put("editTime", DateFormat.format("MM/dd/yy h:mmaa", Calendar.getInstance(Locale.CHINA)).toString());*/
        cv.put("editTime",  DateFormat.getDateTimeInstance().format(new Date()));
        cv.put("status", beeSource.getStatus());
        return db.insert(DataBaseHelper.TABLE_BEESOURCE, null, cv);
    }

    public void deleteInfo(int id){
        this.db.delete(DataBaseHelper.TABLE_BEESOURCE, "id =" + id, null);
    }

    public long addUserPoint(String lat,String lng,String name,String status,String address){

        ContentValues cv = new ContentValues();
        cv.put("lat", lat);
        cv.put("lng", lng);
        cv.put("placeName", name);
        cv.put("address", address);
      /*  cv.put("addTime", DateFormat.format("yyyy/MM/dd hh:mm:ss", Calendar.getInstance(Locale.CHINA)).toString());*/
        
        cv.put("addTime",  DateFormat.getDateTimeInstance().format(new Date()));
        cv.put("status", status);
        return db.insert(DataBaseHelper.TABLE_POINT, null, cv);
    }

    public void updateUserPoint(String id,String status){

        ContentValues cv = new ContentValues();
        cv.put("status", status);
        this.db.update(DataBaseHelper.TABLE_POINT, cv, "id " + "=" + id, null);
    }
    
    

    public void deleteUserPoint(int id){

        this.db.delete(DataBaseHelper.TABLE_POINT, "id =" + id, null);
    }

    public UserPoint getUserPoint(int id){

        Cursor cur = this.db.rawQuery("SELECT lat,lng,status,addTime,placeName FROM "+DataBaseHelper.TABLE_POINT+" where id=?", new String[]{String.valueOf(id)});
        if (cur != null) {
            while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
                UserPoint userPoint=new UserPoint();
                userPoint.setId(String.valueOf(id));
                userPoint.setLat(cur.getString(0));
                userPoint.setLng(cur.getString(1));
                userPoint.setStatus(cur.getString(21));
                userPoint.setAddTime(cur.getString(3));
                userPoint.setName(cur.getString(4));
                if (!cur.isClosed()) {
                    cur.close();
                }

                return userPoint;
            }
        }
        if (!cur.isClosed()) {
            cur.close();
        }

        return null;
    }

    public List<UserPoint> getUserPointList(){

        List<UserPoint> list=new ArrayList<UserPoint>();
        Cursor cur = this.db.rawQuery("SELECT id,lat,lng,status,addTime,placeName FROM "+DataBaseHelper.TABLE_POINT+" ORDER BY id DESC", null);
        if (cur != null) {
            while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
                UserPoint userPoint=new UserPoint();
                userPoint.setId(cur.getString(0));
                userPoint.setLat(cur.getString(1));
                userPoint.setLng(cur.getString(2));
                userPoint.setStatus(cur.getString(3));
                userPoint.setAddTime(cur.getString(4));
                userPoint.setName(cur.getString(5));
                list.add(userPoint);
            }
        }
        if (!cur.isClosed()) {
            cur.close();
        }
        return list;
    }
    
    
    
    
    public List<UserPoint> getUserPointList(int flowType){

        List<UserPoint> list=new ArrayList<UserPoint>();
        Cursor cur = this.db.rawQuery("SELECT id,lat,lng,status,addTime,placeName,unit,address FROM "+DataBaseHelper.TABLE_POINT+" Where status="+flowType+" ORDER BY id DESC", null);
        if (cur != null) {
            while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
                UserPoint userPoint=new UserPoint();
                userPoint.setId(cur.getString(0));
                userPoint.setLat(cur.getString(1));
                userPoint.setLng(cur.getString(2));
                userPoint.setStatus(cur.getString(3));
                userPoint.setAddTime(cur.getString(4));
                userPoint.setName(cur.getString(5));
                
                userPoint.setAddress(cur.getString(7));
                //add
                userPoint.setUnitName(cur.getString(6));
                list.add(userPoint);
            }
        }
        if (!cur.isClosed()) {
            cur.close();
        }
        return list;
    }

    public int update(BeeSource beeSource,String id){
        ContentValues cv = new ContentValues();
        cv.put("a1", beeSource.getA1());
        cv.put("a2", beeSource.getA2());
        cv.put("a4", beeSource.getA4());
        cv.put("a5", beeSource.getA5());
        cv.put("a6", beeSource.getA6());
        cv.put("a7", beeSource.getA7());
        cv.put("a8", beeSource.getA8());
        cv.put("a9", beeSource.getA9());
        cv.put("a10", beeSource.getA10());
        cv.put("a11", beeSource.getA11());
        cv.put("a12", beeSource.getA12());
        cv.put("a13", beeSource.getA13());
        cv.put("a14", beeSource.getA14());
        cv.put("a15", beeSource.getA15());
        cv.put("a16", beeSource.getA16());
        
        cv.put("unit", beeSource.getUnitName());
        if(beeSource.getStatus()==null){
        	
            cv.put("status", 0);
        }
        else{
            cv.put("status", beeSource.getStatus());
        }
       /* cv.put("editTime", DateFormat.format("yyyy/MM/dd hh:mm:ss", Calendar.getInstance(Locale.CHINA)).toString());*/
        cv.put("editTime",  DateFormat.getDateTimeInstance().format(new Date()));
        return db.update(DataBaseHelper.TABLE_BEESOURCE, cv, "id " + "=" + id, null);
        
        
    }

    public void delete(int id){
        this.db.delete(DataBaseHelper.TABLE_BEESOURCE, "id =" + id, null);
    }

    public void send(int id){
        ContentValues cv = new ContentValues();
        cv.put("status", 1);
        this.db.update(DataBaseHelper.TABLE_BEESOURCE, cv, "id " + "=" + id, null);
    }

    public BeeSource find(String id){
        Cursor cur = this.db.rawQuery("SELECT a1, a2, a4, a5, a6, a7, a8, a9,a10,a11,a12,a13,a14,a15,status,addTime,editTime,a16 FROM "
                + DataBaseHelper.TABLE_BEESOURCE+" where id=?", new String[]{String.valueOf(id)});
        if (cur != null) {
            while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
                BeeSource infoBvo=new BeeSource();
                infoBvo.setId(id);
                infoBvo.setA1(cur.getString(0));
                infoBvo.setA2(cur.getString(1));
                infoBvo.setA4(cur.getString(2));
                infoBvo.setA5(cur.getString(3));
                infoBvo.setA6(cur.getString(4));
                infoBvo.setA7(cur.getString(5));
                infoBvo.setA8(cur.getString(6));
                infoBvo.setA9(cur.getString(7));
                infoBvo.setA10(cur.getString(8));
                infoBvo.setA11(cur.getString(9));
                infoBvo.setA12(cur.getString(10));
                infoBvo.setA13(cur.getString(11));
                infoBvo.setA14(cur.getString(12));
                infoBvo.setA15(cur.getString(13));
                infoBvo.setStatus(cur.getString(14));
                infoBvo.setAddTime(cur.getString(15));
                infoBvo.setEditTime(cur.getString(16));
                infoBvo.setA16(cur.getString(17));
                if (!cur.isClosed()) {
                    cur.close();
                }
                return infoBvo;
            }
        }
        if (!cur.isClosed()) {
            cur.close();
        }
        return null;
    }

    public ArrayList<BeeSource> findAll(){
        ArrayList<BeeSource> list=new ArrayList<BeeSource>();
        Cursor cur = this.db.rawQuery("SELECT a1, a2, a4, a5, a6, a7, a8, a9,a10,a11,a12,a13,a14,a15,status,id,addTime,editTime,a16,unit FROM "
                + DataBaseHelper.TABLE_BEESOURCE+" ORDER BY id DESC", null);
        if (cur != null) {
            while (cur.moveToNext()) { //直到返回false说明表中到了数据末尾
                BeeSource beeSource=new BeeSource();
                beeSource.setA1(cur.getString(0));
                beeSource.setA2(cur.getString(1));
                beeSource.setA4(cur.getString(2));
                beeSource.setA5(cur.getString(3));
                beeSource.setA6(cur.getString(4));
                beeSource.setA7(cur.getString(5));
                beeSource.setA8(cur.getString(6));
                beeSource.setA9(cur.getString(7));
                beeSource.setA10(cur.getString(8));
                beeSource.setA11(cur.getString(9));
                beeSource.setA12(cur.getString(10));
                beeSource.setA13(cur.getString(11));
                beeSource.setA14(cur.getString(12));
                beeSource.setA15(cur.getString(13));
                beeSource.setStatus(cur.getString(14));
                beeSource.setId(String.valueOf(cur.getInt(15)));
                beeSource.setAddTime(cur.getString(16));
                beeSource.setEditTime(cur.getString(17));
                beeSource.setA16(cur.getString(18));
                //添加
                beeSource.setUnitName(cur.getString(19));
                list.add(beeSource);
            }
        }
        if (!cur.isClosed()) {
            cur.close();
        }
        return list;
    }
    
    
    public ArrayList<BeeSource> findAll(int flowType){
        ArrayList<BeeSource> list=new ArrayList<BeeSource>();
        Cursor cur = this.db.rawQuery("SELECT a1, a2, a4, a5, a6, a7, a8, a9,a10,a11,a12,a13,a14,a15,status,id,addTime,editTime,a16,unit FROM "
                + DataBaseHelper.TABLE_BEESOURCE+" where status="+flowType+" ORDER BY id DESC", null);
        if (cur != null) {
            while (cur.moveToNext()) { //直到返回false说明表中到了数据末尾
                BeeSource beeSource=new BeeSource();
                beeSource.setA1(cur.getString(0));
                beeSource.setA2(cur.getString(1));
                beeSource.setA4(cur.getString(2));
                beeSource.setA5(cur.getString(3));
                beeSource.setA6(cur.getString(4));
                beeSource.setA7(cur.getString(5));
                beeSource.setA8(cur.getString(6));
                beeSource.setA9(cur.getString(7));
                beeSource.setA10(cur.getString(8));
                beeSource.setA11(cur.getString(9));
                beeSource.setA12(cur.getString(10));
                beeSource.setA13(cur.getString(11));
                beeSource.setA14(cur.getString(12));
                beeSource.setA15(cur.getString(13));
                beeSource.setStatus(cur.getString(14));
                beeSource.setId(String.valueOf(cur.getInt(15)));
                beeSource.setAddTime(cur.getString(16));
                beeSource.setEditTime(cur.getString(17));
                beeSource.setA16(cur.getString(18));
                //添加
                beeSource.setUnitName(cur.getString(19));
                list.add(beeSource);
            }
        }
        if (!cur.isClosed()) {
            cur.close();
        }
        return list;
    }
    
    
    
    
    public ArrayList<BeeSource> findByIds(HashSet<String> ids){
        ArrayList<BeeSource> list=new ArrayList<BeeSource>();
        
        Cursor cur=null;
        for(String id:ids){
        	
        	cur=db.rawQuery("SELECT a1, a2, a4, a5, a6, a7, a8, a9,a10,a11,a12,a13,a14,a15,status,id,addTime,editTime,a16 FROM "
                    + DataBaseHelper.TABLE_BEESOURCE+" where id="+id, null);   
        if (cur != null) {
            while (cur.moveToNext()) {//直到返回false说明表中到了数据末尾
                BeeSource infoBvo=new BeeSource();
                infoBvo.setA1(cur.getString(0));
                infoBvo.setA2(cur.getString(1));
                infoBvo.setA4(cur.getString(2));
                infoBvo.setA5(cur.getString(3));
                infoBvo.setA6(cur.getString(4));
                infoBvo.setA7(cur.getString(5));
                infoBvo.setA8(cur.getString(6));
                infoBvo.setA9(cur.getString(7));
                infoBvo.setA10(cur.getString(8));
                infoBvo.setA11(cur.getString(9));
                infoBvo.setA12(cur.getString(10));
                infoBvo.setA13(cur.getString(11));
                infoBvo.setA14(cur.getString(12));
                infoBvo.setA15(cur.getString(13));
                infoBvo.setStatus(cur.getString(14));
                infoBvo.setId(String.valueOf(cur.getInt(15)));
                infoBvo.setAddTime(cur.getString(16));
                infoBvo.setEditTime(cur.getString(17));
                infoBvo.setA16(cur.getString(18));
                list.add(infoBvo);
            }
        }    
        }    
        if (!cur.isClosed()) {
            cur.close();
        }
        return list;
    }
    
    public ArrayList<UserPoint> findPointsByIds(HashSet<String> ids){
    	
    	ArrayList<UserPoint> pointList=new ArrayList<UserPoint>();  	
    	Cursor cur=null;
    	
    	for(String id:ids){
    		
    		String sql="";
    		sql="SELECT id,lat,lng,status,addTime,placeName,address FROM "+DataBaseHelper.TABLE_POINT+" where id="+id;
    		
    		cur=db.rawQuery(sql, null);
    		
    		if(cur!=null){
    			if(cur.moveToNext()){
    		       UserPoint userPoint=new UserPoint();
    		       userPoint.setId(cur.getString(0));
    		       userPoint.setAddTime(cur.getString(4));
    		       userPoint.setLat(cur.getString(1));
    		       userPoint.setLng(cur.getString(2));
    		       userPoint.setName(cur.getString(5));
    		       userPoint.setStatus(cur.getString(3)); 
    		       userPoint.setAddress(cur.getString(6)); 
    		       pointList.add(userPoint);
    				
    			}			
    		}	
    	}
    	
        if (cur!=null&&!cur.isClosed()) {
            cur.close();
        }
    	
    	return pointList;  	
    }
    
    
    public String getBeeSourceNumber_NoSend(){
    	
    	String sql;
    	sql="select count(*) from "+DataBaseHelper.TABLE_BEESOURCE+" where status=0";
    	Cursor cur = db.rawQuery(sql, null);
    	
    	if(cur!=null){	
    		if(cur.moveToFirst()){
    			
    			return cur.getString(0);
    		}
    	}
    	
    	return "0";
    }
    
    
 public String getUserPointNumber_NoSend(){
    	
    	String sql;
    	sql="select count(*) from "+DataBaseHelper.TABLE_POINT+" where status=0";
    	Cursor cur = db.rawQuery(sql, null);
    	
    	if(cur!=null){	
    		if(cur.moveToFirst()){
    			
    			return cur.getString(0);
    		}
    	}
    	
    	return "0";
    } 

	public long  savePic(PicBean picDB){
		
		 ContentValues values=new ContentValues();
		 values.put("path", picDB.getPath());
		 values.put("title", picDB.getTitle());
		 values.put("lat", picDB.getLat());
		 values.put("lng", picDB.getLng());
		 values.put("status", picDB.getStatus());
		 values.put("address", picDB.getAddress());		 
		 values.put("addTime",  DateFormat.getDateTimeInstance().format(new Date()));
       return  db.insert(DataBaseHelper.TABLE_PIC, null, values);		
	}
	
	public void deletePic(String id){
		
		db.delete(DataBaseHelper.TABLE_PIC, "id =" + id, null);
		
	}
	
	public Cursor queryPic(){
		 return db.query(DataBaseHelper.TABLE_PIC, null, null, null, null, null, null);
	}
	
	public Cursor queryPic(int flowType){
		
	/*	ContentValues values=new ContentValues();
		values.put("status", flowType);*/
		return db.query(DataBaseHelper.TABLE_PIC, null, "status="+flowType, null, null, null, " id DESC");
	}
	
	
	public String getPicNumber_NoSend() {

		String sql;
		sql = "select count(*) from "+DataBaseHelper.TABLE_PIC+" where status=0";
		Cursor cur = db.rawQuery(sql, null);

		if (cur != null) {
			if (cur.moveToFirst()) {

				return cur.getString(0);
			}
		}
		return "0";
	} 
	
	public void updatePic(String id,String status){

        ContentValues cv = new ContentValues();
        cv.put("status", status);
        this.db.update(DataBaseHelper.TABLE_PIC, cv, "id " + "=" + id, null);
    }

	public void updateBeeSource(String id, String status) {
		// TODO Auto-generated method stub
		
		 ContentValues cv = new ContentValues();
	     cv.put("status", status);
	     db.update(DataBaseHelper.TABLE_BEESOURCE, cv, "id " + "=" + id, null);
		
	}
	
	public Boolean updateBeeSource(BeeSource beeSource) {
		// TODO Auto-generated method stub
		
		 ContentValues cv = new ContentValues();
	     cv.put("status", beeSource.getStatus());
	     
	     cv.put("a1", beeSource.getA1());
	        cv.put("a2", beeSource.getA2());
	        cv.put("a4", beeSource.getA4());
	        cv.put("a5", beeSource.getA5());
	        cv.put("a6", beeSource.getA6());
	        cv.put("a7", beeSource.getA7());
	        cv.put("a8", beeSource.getA8());
	        cv.put("a9", beeSource.getA9());
	        cv.put("a10", beeSource.getA10());
	        cv.put("a11", beeSource.getA11());
	        cv.put("a12", beeSource.getA12());
	        cv.put("a13", beeSource.getA13());
	        cv.put("a14", beeSource.getA14());
	        cv.put("a15", beeSource.getA15());
	        cv.put("a16", beeSource.getA16());
	        
	        cv.put("unit", beeSource.getUnitName());
	        
	      /*  cv.put("addTime",  DateFormat.format("yyyy/MM/dd hh:mm:ss", Calendar.getInstance(Locale.CHINA)).toString());*/
	       // cv.put("addTime",  DateFormat.getDateTimeInstance().format(new Date()));
	       /* cv.put("editTime", DateFormat.format("yyyy/MM/dd hh:mm:ss", Calendar.getInstance(Locale.CHINA)).toString());*/
	        
	        cv.put("editTime",  DateFormat.getDateTimeInstance().format(new Date()));
	        cv.put("status", beeSource.getStatus());
	          
	       int rowId=db.update(DataBaseHelper.TABLE_BEESOURCE, cv, "id " + "=" + beeSource.getId(), null);
	       if(rowId==-1){    	   
	    	   return false;
	       }       
	       return true;	
	}

	public boolean updateUserPoint(UserPoint userPoint) {
		// TODO Auto-generated method stub
		
		    ContentValues cv = new ContentValues();
	        cv.put("status", userPoint.getStatus());
	        
	        cv.put("lat", userPoint.getLat());
	        cv.put("lng", userPoint.getLng());
	        /*cv.put("lat", userPoint.getAddTime());*/
	        cv.put("placeName", userPoint.getName());
	        cv.put("unit", userPoint.getUnitName());
	        
	  //      cv.put("unit", userPoint.getAddress());
	        
	        int rowId=this.db.update(DataBaseHelper.TABLE_POINT, cv, "id " + "=" + userPoint.getId(), null);
		
	        if(rowId==-1){
	        	
		    	   return false;
		    }       
		       return true;	
	}

	public boolean updatePic(PicBean picBean) {
		// TODO Auto-generated method stub

		ContentValues cv = new ContentValues();
        cv.put("status", picBean.getStatus());
        cv.put("title", picBean.getTitle());
        cv.put("lat", picBean.getLat());
        cv.put("lng", picBean.getLng());
      /*  cv.put("address", picBean.getAddress());*/
        cv.put("unit", picBean.getUnitName());
        
        int rowId=this.db.update(DataBaseHelper.TABLE_PIC, cv, "id " + "=" + picBean.getId(), null);
        if(rowId==-1){
        	
	    	   return false;
	    }       
	       return true;
	}
	
}
