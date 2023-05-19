package com.example.lostandfoundmap.model;

public class Item {
    private String postType;
    private String poster;
    private String phoneNo;
    private String description;
    private String location;
    private String date;
    private double latitude;
    private double longitude;

    public Item(String postType, String poster, String phoneNo, String description, String location, String date, double latitude, double longitude) {
        this.postType = postType;
        this.poster = poster;
        this.phoneNo = phoneNo;
        this.description = description;
        this.location = "NOT RELEVANT";
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Item() {
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        //ignore the input and always set the location to "SEE MAP" (stops me from having to change much more code this way)
        this.location = "SEE MAP";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
