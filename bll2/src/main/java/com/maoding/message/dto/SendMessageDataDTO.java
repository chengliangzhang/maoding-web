package com.maoding.message.dto;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendMessageDataDTO {

    private String title;
    private String receiver;
    private List<String> receiverList;
    private String content;
    private String messageType;
    private Map<String,Object> otherContent = new HashMap<>();

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public List<String> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<String> receiverList) {
        this.receiverList = receiverList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getOtherContent() {
        return otherContent;
    }

    public void setOtherContent(Map<String, Object> otherContent) {
        this.otherContent = otherContent;
    }
}