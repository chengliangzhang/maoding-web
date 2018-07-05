package com.maoding.org.dto;

import com.maoding.core.util.StringUtil;

/**
 * Created by Idccapp22 on 2017/1/5.
 */
public class CompanyDataDTO {

    /**
     *组织id
     */
    private String id;
    /**
     *组织名称
     */
    private String companyName;
    /**
     *组织简称
     */
    private String companyShortName;
    /**
     *组织类型
     */
    private String companyType;

    /**
     *公司
     */
    private String filePath;

    /**
     * 企业管理员名
     */
    private String systemManager;


    private int flag;//是否已经存在事业合伙人或许分支机构（0：无，1：是）

    private String memo;

    /**
     * 是否是外部合作组织（1：是）
     */
    private int isOuterCooperator;

    /**
     * 别名
     */
    private String aliasName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        if(!StringUtil.isNullOrEmpty(this.aliasName)){
            companyName = aliasName;
        }
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSystemManager() {
        return systemManager;
    }

    public void setSystemManager(String systemManager) {
        this.systemManager = systemManager;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public int getIsOuterCooperator() {
        return isOuterCooperator;
    }

    public void setIsOuterCooperator(int isOuterCooperator) {
        this.isOuterCooperator = isOuterCooperator;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
