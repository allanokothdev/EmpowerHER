package com.chamarw.android.models;

import java.io.Serializable;

public class Category implements Serializable {


    private String id;
    private String title;

    public Category(){ }


    public Category(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Category category = (Category) obj;
        return id.matches(category.getId());
    }
}
