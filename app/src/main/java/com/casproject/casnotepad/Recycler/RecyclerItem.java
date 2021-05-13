package com.casproject.casnotepad.Recycler;

import android.net.Uri;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class RecyclerItem extends RealmObject {
    @Required
    private String title; // 제목
    private String content; // 내용
    private String URI;
    private int id;

    public RecyclerItem() {
    }

    public RecyclerItem(String title, String content, String URI, int id) {
        this.title = title;
        this.content = content;
        this.URI = URI;
        this.id = id;
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
}
