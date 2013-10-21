package com.bee.app.bean;

import java.io.File;
import java.io.Serializable;

import android.widget.TextView;

public class PicBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String path;
	
	private File imgeFile;
	
    private String unitNumber;
	    
	private String unitName;
	
	private String addTime;

	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getUnitNumber() {
		return unitNumber;
	}
	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	private String uid;
	
	private String id;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	private String lat;
	private String lng;
	private String title;
	
	private String status;
	
	private Boolean ischecked;
	
	private String address;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		this.title=title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public void setIschecked(Boolean ischecked) {
		this.ischecked = ischecked;
	}
	
	public Boolean getIschecked() {
		return ischecked;
	}
	public File getImgeFile() {
		return imgeFile;
	}
	public void setImgeFile(File imgeFile) {
		this.imgeFile = imgeFile;
	}

	

}
