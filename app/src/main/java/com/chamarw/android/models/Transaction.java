package com.chamarw.android.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Transaction implements Serializable {

    private String id;
    private String title;
    private String summary;
    private String timestamp;
    private String sender;
    private String receiver;
    private String value;
    private ArrayList<String> tags;
    private String token;
    private String link;

    public Transaction() {
    }

    public Transaction(String id, String title, String summary, String timestamp, String sender, String receiver, String value, ArrayList<String> tags, String token, String link) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
        this.value = value;
        this.tags = tags;
        this.token = token;
        this.link = link;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj){
        Transaction transaction = (Transaction) obj;
        return id.matches(transaction.getId());
    }
}
