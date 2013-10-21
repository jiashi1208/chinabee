package com.bee.base;

public class C {
	
	
	public static final class err {
		public static final String network			= "网络错误";
		public static final String message			= "消息错误";
		public static final String jsonFormat		= "消息格式错误";
	}
	
	
	public static final class task {
		public static final int index				= 1001;
		public static final int login				= 1002;
		public static final int logout				= 1003;
		public static final int faceView			= 1004;
		public static final int faceList			= 1005;
		public static final int blogList			= 1006;
		public static final int blogView			= 1007;
		public static final int blogCreate			= 1008;
		public static final int commentList			= 1009;
		public static final int commentCreate		= 1010;
		public static final int customerView		= 1011;
		public static final int customerEdit		= 1012;
		public static final int fansAdd				= 1013;
		public static final int fansDel				= 1014;
		public static final int notice				= 1015;
		public static final int beeInfoAdd          = 1016;
	}
	
	public static final class api {
		public static final String base				= "http://223.4.144.25:30018";
		public static final String index			= "/index/index";
	//	public static final String login			= "/index/login";
		public static final String login			="/login.aspx";
		public static final String logout			= "/index/logout";
		public static final String faceView 		= "/image/faceView";
		public static final String faceList 		= "/image/faceList";
		public static final String blogList			= "/blog/blogList";
		public static final String blogView			= "/blog/blogView";
		public static final String blogCreate		= "/blog/blogCreate";
		public static final String commentList		= "/comment/commentList";
		public static final String commentCreate	= "/comment/commentCreate";
		public static final String customerView		= "/customer/customerView";
		public static final String customerEdit		= "/customer/customerEdit";
		public static final String fansAdd			= "/customer/fansAdd";
		public static final String fansDel			= "/customer/fansDel";
		public static final String notice			= "/notify/notice";
	}

}
