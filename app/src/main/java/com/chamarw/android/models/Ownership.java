package com.chamarw.android.models;

import java.io.Serializable;

public class Ownership implements Serializable {

    private String address;
    private String pic;
    private int savings;
    private int goal;

    public Ownership() {
    }

    public Ownership(String address, String pic, int savings, int goal) {
        this.address = address;
        this.pic = pic;
        this.savings = savings;
        this.goal = goal;
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

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Ownership ownership = (Ownership) obj;
        return address.matches(ownership.getAddress());
    }
}
