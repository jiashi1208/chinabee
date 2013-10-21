package com.bee.app.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataBaseHelper extends SQLiteOpenHelper {
    public final static int VERSION = 1;
    public final static String TABLE_BEESOURCE = "beesource";
    public final static String TABLE_PIC = "beepic";
    
    public final static String TABLE_POINT = "beepoint";
    public final static String ID = "id";
    public static final String DATABASE_NAME = "bee3.db";
    public DataBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
        String str_sql="CREATE TABLE IF NOT EXISTS "+TABLE_PIC+"("+
       	       ID+" INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT NOT NULL,path TEXT NOT NULL,lat TEXT NOT NULL,lng TEXT NOT NULL,status TEXT NOT NULL,address TEXT NOT NULL,unit TEXT,addTime TEXT);";
    	
        db.execSQL(str_sql);
        
        str_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_BEESOURCE + "(" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,status TEXT NOT NULL,a1 TEXT, a2 TEXT, a4 TEXT, a5 TEXT, a6 TEXT, a7 TEXT, a8 TEXT, a9 TEXT,a10 TEXT,a11 TEXT,a12 TEXT,a13 TEXT,a14 TEXT,a15 TEXT,addTime TEXT,editTime TEXT, a16 TEXT,unit TEXT);";
        db.execSQL(str_sql);
        
        
        str_sql = "CREATE TABLE IF NOT EXISTS "+TABLE_POINT +"(" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,lat TEXT,lng TEXT,addTime TEXT,status TEXT NOT NULL,placeName TEXT,unit TEXT,address TEXT);";
        db.execSQL(str_sql);
        
        str_sql = "CREATE TABLE IF NOT EXISTS "+"system_config"+" (" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,url TEXT,userId TEXT,lat TEXT,lng TEXT,loginName TEXT,phoneNumber TEXT);";
        	       
        db.execSQL(str_sql);
        
/*        str_sql = "CREATE TABLE IF NOT EXISTS "+"material"+" (" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,typeID TEXT,type TEXT";
        db.execSQL(str_sql);*/
        
       /* str_sql = "CREATE TABLE IF NOT EXISTS "+"material"+" (" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,typeID TEXT,type TEXT";
        db.execSQL(str_sql);*/
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	
    	db.execSQL("DROP TABLE IF EXISTS beepic");
    	db.execSQL("DROP TABLE IF EXISTS beesource");
    	db.execSQL("DROP TABLE IF EXISTS beepoint");
    	db.execSQL("DROP TABLE IF EXISTS system_config");
    	db.execSQL("DROP TABLE IF EXISTS material");
		onCreate(db);

    }
}