package com.bee.app.bean;

public class ImageInfo {
	
	public String imageMsg;		//菜单标题
	public int imageId;			//logo图片资源
	
	public int bgId;			//背景图片资源

	public ImageInfo(String msg, int imageId,int bgId) {
		
		this.imageId = imageId;
		this.imageMsg = msg;
		this.bgId=bgId;
	}

}
