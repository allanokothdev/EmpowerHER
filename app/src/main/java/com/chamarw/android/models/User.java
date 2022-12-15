package com.chamarw.android.models;

import java.io.Serializable;

public class User implements Serializable {

    private String id;   //USER ID
    private String address;   //USER ADDRESS
    private String pic;   //USER PIC
    private String name;   //USER NAME
    private String token;   //USER TOKEN

    public User() {
    }

    public User(String id, String address, String pic, String name, String token) {
        this.id = id;
        this.address = address;
        this.pic = pic;
        this.name = name;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        User user = (User) obj;
        assert user != null;
        return id.matches(user.getId());
    }
}
