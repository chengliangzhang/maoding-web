package com.maoding.system.dto;


import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DataDictionaryEntity
 * 类描述：数据字典
 * 作    者：wangrb
 * 日    期：2015年11月26日-下午2:21:01
 */
public class DataDictionaryDataDTO  {

    private String id;
	
    private String name;

    private String code;

    private String pid;

    private String memo;

    private Integer seq;

    List<DataDictionaryDataDTO> childList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DataDictionaryDataDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<DataDictionaryDataDTO> childList) {
        this.childList = childList;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}