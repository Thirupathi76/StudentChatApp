package com.demos.studentchatapp;

/**
 * Created by welcome on 2/2/2018.
 */

public class ChatBean {

    private String name;
    private String message;
    private String date;
    private String branch;
    private String key;

    public ChatBean(String name, String message, String date, String key) {

        this.name = name;
        this.message = message;
        this.date = date;
        this.key = key;
    }

    public ChatBean(){

    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
