package com.bee.app.bean;

import java.io.Serializable;

public class UserPoint implements Serializable {
    private String id;
    private String lat;
    private String lng;
    private String addTime;
    private String name;
    private String status;
    
    private String unitNumber;
    private String unitName;
    
    private String address;
    
    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	private String userId;

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "UserPointBvo{" +
                "id=" + id +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", addTime='" + addTime + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
