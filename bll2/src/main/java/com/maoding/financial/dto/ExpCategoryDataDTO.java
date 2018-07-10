package com.maoding.financial.dto;



import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ExpCategoryDataDTO
 * 作    者：wangrb
 * 日    期：2015年11月26日-下午2:21:01
 */
public class ExpCategoryDataDTO {

    private String id;
	
    private String name;

    private String code;

    private String pid;

    private Integer showStatus;

    private Integer categoryType;

    private String expTypeMemo;

    private String dataSource;

    private boolean disabled;

    private String isDefaulted;

    List<ExpCategoryDataDTO> childList = new ArrayList<>();

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

    public List<ExpCategoryDataDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<ExpCategoryDataDTO> childList) {
        this.childList = childList;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public String getExpTypeMemo() {
        return expTypeMemo;
    }

    public void setExpTypeMemo(String expTypeMemo) {
        this.expTypeMemo = expTypeMemo;
    }

    public String getDataSource() {
        if(this.categoryType!=null){
            switch (categoryType){
                case 0:return "项目收支管理系统数据";
                case 1:return "费用报销系统数据";
                case 2:return "人工输入";
                case 3:return "人工输入";
            }
        }
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getIsDefaulted() {
        return isDefaulted;
    }

    public void setIsDefaulted(String isDefaulted) {
        this.isDefaulted = isDefaulted;
    }
}