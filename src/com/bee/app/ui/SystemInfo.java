package com.bee.app.ui;

public class SystemInfo {
    private Integer id;
    private String url;
    private String userId;
    private String lat;
    private String lng;
    private String loginName;
	private String phoneNumber;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SystemInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", userId='" + userId + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", loginName='" + loginName + '\'' +
                '}';
    }

	public void setPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		this.phoneNumber=phoneNumber;
	}
	
	public String getPhoneNumber(){
		return phoneNumber;
	}
}
