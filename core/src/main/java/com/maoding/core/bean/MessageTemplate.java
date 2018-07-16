package com.maoding.core.bean;

public class MessageTemplate {

    private String content;

    private int type;

    public MessageTemplate(String content,int type){
        this.content = content;
        this.type = type;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
