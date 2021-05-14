package com.casproject.casnotepad.Recycler;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class RecyclerItem extends RealmObject {
    @Required
    private String title; // 제목
    private String content; // 내용
    private String URI; // URI
    private int id; // id
    private String date;

    public RecyclerItem() {
    }

    public RecyclerItem(String title, String content, String URI, int id, String date) {
        this.title = title;
        this.content = content;
        this.URI = URI;
        this.id = id;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
