package com.chamarw.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Ownership implements Serializable {

    private String address;
    private String pic;
    private int totalSavings;
    private int totalGoal;

    public Ownership() {
    }

    public Ownership(String address, String pic, int totalSavings, int totalGoal) {
        this.address = address;
        this.pic = pic;
        this.totalSavings = totalSavings;
        this.totalGoal = totalGoal;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(int totalSavings) {
        this.totalSavings = totalSavings;
    }

    public int getTotalGoal() {
        return totalGoal;
    }

    public void setTotalGoal(int totalGoal) {
        this.totalGoal = totalGoal;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Ownership ownership = (Ownership) obj;
        return address.matches(ownership.getAddress());
    }
}
