package com.maoding.hxIm.dto;

import java.io.Serializable;
import java.util.Map;

/** APP后台兼容过来的SendMessageDTO */
public class ImSendMessageDTO implements Serializable {
    /** 发送者 "user1" ，卯丁助手："123f9ef123c140fd9b6c7a5123c68e1a"*/
    private String fromUser;

    /** 接收者数组["user2","user3"] */
    private String[] toUsers;

    /** 信息类型，内容 "type":"txt","msg":"发送内容" */
    private Map<String, Object> msgMap;

    /** 收对象（users：接收主体为个人，chatgroups：接收主体为群组）*/
    private String targetType;

    private Map<String,Object> ext;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String[] getToUsers() {
        return toUsers;
    }

    public void setToUsers(String[] toUsers) {
        this.toUsers = toUsers;
    }

    public Map<String, Object> getMsgMap() {
        return msgMap;
    }

    public void setMsgMap(Map<String, Object> msgMap) {
        this.msgMap = msgMap;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }
}
