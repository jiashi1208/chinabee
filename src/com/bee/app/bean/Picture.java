package com.bee.app.bean;

public class Picture {
    private String title;
    private String imagepath;
    
    private Boolean ischecked;
    
    private String lat;
    private String lng;
    private String id;
    
    private String status;

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIschecked() {
		return ischecked;
	}

	public void setIschecked(Boolean ischecked) {
		this.ischecked = ischecked;
	}

	public Picture() {
        super();
    }

    public Picture(String title, String imagepath,Boolean ischecked ) {
        super();
        this.title = title;
        this.imagepath = imagepath;
        this.ischecked=ischecked;
        
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagepath;
    }

    public void setImageId(String imagePath) {
        this.imagepath = imagePath;
    }

}
