package com.example.smartshopping.Model;

import com.google.firebase.database.ServerValue;

public class Comment {
    private String content,uimg,uname,pid,date;


    public Comment() {
    }

    public Comment(String content, String uimg, String uname, String pid, String date) {
        this.content = content;
        this.uimg = uimg;
        this.uname = uname;
        this.pid = pid;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
