package com.maoding.project.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.util.Date;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectConstructEntity
 * 类描述：建设单位实体
 * 作    者：LY
 * 日    期：2016年7月19日-下午4:04:50
 */
public class ProjectConstructEntity extends BaseEntity {


    /**
     * 建设单位名称
     */
    private String companyName;

    /**
     * 联系人
     */
    private String contractPerson;

    /**
     * 联系电话
     */
    private String contractPhone;

    /**
     * 通讯地址
     */
    private String contractAddress;

    /**
     * 联系邮箱
     */
    private String contractEmail;

    /**
     * 创建者企业id
     */
    private String companyId;

    /**
     * 省份
     */
    private String contractProvince;

    /**
     * 市
     */
    private String contractCity;

    /**
     * 传真
     */
    private String contractFax;

    /**
     * 官网地址
     */
    private String webUrl;

    /**
     * 备注
     */
    private String remark;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getContractPerson() {
        return contractPerson;
    }

    public void setContractPerson(String contractPerson) {
        this.contractPerson = contractPerson == null ? null : contractPerson.trim();
    }

    public String getContractPhone() {
        return contractPhone;
    }

    public void setContractPhone(String contractPhone) {
        this.contractPhone = contractPhone == null ? null : contractPhone.trim();
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress == null ? null : contractAddress.trim();
    }

    public String getContractEmail() {
        return contractEmail;
    }

    public void setContractEmail(String contractEmail) {
        this.contractEmail = contractEmail == null ? null : contractEmail.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getContractProvince() {
        return contractProvince;
    }

    public void setContractProvince(String contractProvince) {
        this.contractProvince = contractProvince == null ? null : contractProvince.trim();
    }

    public String getContractCity() {
        return contractCity;
    }

    public void setContractCity(String contractCity) {
        this.contractCity = contractCity == null ? null : contractCity.trim();
    }

    public String getContractFax() {
        return contractFax;
    }

    public void setContractFax(String contractFax) {
        this.contractFax = contractFax == null ? null : contractFax.trim();
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl == null ? null : webUrl.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}