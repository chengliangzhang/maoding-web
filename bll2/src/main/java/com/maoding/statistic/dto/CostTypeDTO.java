package com.maoding.statistic.dto;

import java.util.ArrayList;
import java.util.List;

public class CostTypeDTO {

    /** id,pid紧供前端匹配组使用 **/
    private String id;
    private String pid;

    private String expTypeKey;

    private String expTypeValue;//展示值

    private Integer level;

    private Integer selected = 1;

    List<CostTypeDTO> childList = new ArrayList<>();//子项

    public CostTypeDTO(){}
    public CostTypeDTO(String expTypeKey,String expTypeValue,Integer level){
        this.expTypeKey = expTypeKey;
        this.expTypeValue = expTypeValue;
        this.level = level;
    }
    public CostTypeDTO(String expTypeKey,String expTypeValue,Integer level,String id,String pid){
        this.expTypeKey = expTypeKey;
        this.expTypeValue = expTypeValue;
        this.level = level;
        this.id = id;
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getExpTypeKey() {
        return expTypeKey;
    }

    public void setExpTypeKey(String expTypeKey) {
        this.expTypeKey = expTypeKey;
    }

    public String getExpTypeValue() {
        return expTypeValue;
    }

    public void setExpTypeValue(String expTypeValue) {
        this.expTypeValue = expTypeValue;
    }

    public List<CostTypeDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<CostTypeDTO> childList) {
        this.childList = childList;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }
}
