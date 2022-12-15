package com.chamarw.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Listing implements Serializable {

    private String id;
    private String pic;             //PICTURE
    private String certification;   //OWNERSHIP CERTIFICATE
    private String title;           //TITLE
    private String summary;         //SUMMARY
    private int value;              //CHARGES
    private String location;        //LOCATION
    private String publisher;       //PUBLISHER ADDRESS
    private boolean availability;   //AVAILABILITY
    private String token;           //NOTIFICATION TOKEN
    private ArrayList<String> tags; //ARRAY

    public Listing() {
    }


    public Listing(String id, String pic, String certification, String title, String summary, int value, String location, String publisher, boolean availability, String token, ArrayList<String> tags) {
        this.id = id;
        this.pic = pic;
        this.certification = certification;
        this.title = title;
        this.summary = summary;
        this.value = value;
        this.location = location;
        this.publisher = publisher;
        this.availability = availability;
        this.token = token;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Listing listing = (Listing) obj;
        return id.matches(listing.getId());
    }
}
