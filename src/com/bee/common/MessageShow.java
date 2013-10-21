package com.bee.common;

import android.content.Context;
import android.widget.Toast;

public class MessageShow {
	
	   public static void netshow(Context mContext){
		   Toast.makeText(mContext, "无法连接网络，请检查网络设置，稍后再试", Toast.LENGTH_SHORT).show();
	   }

}
