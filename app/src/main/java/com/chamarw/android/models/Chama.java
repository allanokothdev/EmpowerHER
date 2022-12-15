package com.chamarw.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Chama implements Serializable {

    private String id;              //ID
    private String pic;             //PICTURE
    private String title;           //TITLE
    private String summary;         //DESCRIPTION
    private String publisher;       //ADMIN
    private int savings;            //SAVINGS
    private int goal;               //GOAL
    private ArrayList<String> tags; //ARRAY
    private boolean open;           //GOAL

    public Chama() {
    }

    public Chama(String id, String pic, String title, String summary, String publisher, int savings, int goal, ArrayList<String> tags, boolean open) {
        this.id = id;
        this.pic = pic;
        this.title = title;
        this.summary = summary;
        this.publisher = publisher;
        this.savings = savings;
        this.goal = goal;
        this.tags = tags;
        this.open = open;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getSavings() {
        return savings;
    }

    public void setSavings(int savings) {
        this.savings = savings;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Chama chama = (Chama) obj;
        return id.matches(chama.getId());
    }
}
