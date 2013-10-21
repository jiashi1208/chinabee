package com.bee.common;

import java.util.HashMap;

public class Constants {
	
/*	public static final HashMap<Integer,String> unitMap=new HashMap<Integer,String>(){
		

	};*/
	
	public static final HashMap<Integer,String> unitMap;
	
	public static final String SETTINGS_NAME="settings";
	
	public static final int REQUEST_ADD = 0;
	
	public static final int FLAG_ADD = 1;

	public static final String BEE_EDIT = "com.bee.BEE_EDIT";
	
	public static final String POINT_EDIT ="com.bee.POINT_EDIT";
	
	public static final String PIC_EDIT ="com.bee.PIC_EDIT";
	
	public static final String BEE_ADD =  "com.bee.BEE_ADD";
	
	public static final String PIC_ADD =  "com.bee.PIC_ADD";
	
	public static final String QUICK_BEE_ADD =  "com.bee.QUICK_BEE_ADD";
	
	public static final String POINT_ADD =  "com.bee.POINT_ADD";
	
	public static final String QUICK_PIC_ADD="com.bee.QUICK_PIC_ADD";
	public static final String QUICK_POINT_ADD = "com.bee.QUICK_POINT_ADD";
	
	public final static String UPLOAD_SUCCESS="1";
	public final static String UPLOAD_FAIL="0";
	
    public final static String NO_UPLOAD="0";
    
    public final static int REFRESH_SUCCESS=1;
    
    public final static int POSITION_SUCCESS=3;
    
    public final static String HTTP_PORT=":30018";
    
    public final static String URL_INIT="http://223.4.144.25:30018";
	
	static
    {
		unitMap = new HashMap<Integer, String>();
		unitMap.put(0, "f7e311e4-b43c-4a90-bc90-7eda63e4ace9");
		unitMap.put(1, "fe10fd6d-e1f1-463a-b1ef-608203a679c6");
		unitMap.put(2, "3c2e83c3-4ec5-4981-be81-ea09d778bac8");
    }
	
	public final static String PIC_PATH="/sdcard/chinabee/";
	

}
