package com.casproject.casnotepad.Realm;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Notepad extends RealmObject {
    @Required
    private String title; // 제목
    private String content; // 내용

    public Notepad(String title, String content) {
        this.title = title;
        this.content = content;
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
}
