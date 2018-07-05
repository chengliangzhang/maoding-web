package com.maoding.org.entity;


import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.StringUtil;

public class CompanyEntity extends BaseEntity implements java.io.Serializable{
    /**
     * 企业与上级之间的关系类型
     */
    private String relationType;

	/**
	 * 企业名称
	 */
    private String companyName;

	/**
	 * 专业类别
	 */
    private String majorType;

	/**
	 * 技术资质
	 */
    private String certificate;

	/**
	 * 擅长领域
	 */
    private String mainField;

	/**
	 * 是否认证(0.否，1.是，2申请认证)
	 */
    private String isAuthentication;

	/**
	 * 经办人
	 */
    private String operatorName;

	/**
	 * 认证不通过原因
	 */
    private String rejectReason;

	/**
	 * 公司类型(0=小B，1＝超级大B,2=大B分支机构)
	 */
    private String companyType;

	/**
	 * 企业邮箱
	 */
    private String companyEmail;

	/**
	 * 企业简称
	 */
    private String companyShortName;

	/**
	 * 企业传真
	 */
    private String companyFax;

	/**
	 * 服务类型
	 */
    private String serverType;

	/**
	 * 企业所属省
	 */
    private String province;

	/**
	 * 企业所属市
	 */
    private String city;

    /**
     * 县或区或镇
     */
    private String county;

	/**
	 * 法人代表
	 */
    private String legalRepresentative;

	/**
	 * 联系电话
	 */
    private String companyPhone;

	/**
	 * 企业地址
	 */
    private String companyAddress;

	/**
	 * 企业状态（生效0，1不生效）
	 */
    private String status;

	/**
	 * 团队排序
	 */
    private Integer groupIndex;

	/**是否首页展示(0展示，1不展示)
	 * 
	 */
    private String indexShow;

	/**
	 * 工商营业执照号码
	 */
    private String businessLicenseNumber;

	/**
	 * 组织机构代码证号码
	 */
    private String organizationCodeNumber;

	/**
	 * 微官网地址
	 */
    private String microUrl;

	/**
	 * 微官网模板
	 */
    private String microTemplate;

	/**
	 * 企业群ID
	 */
    private String groupId;

	/**
	 * 企业简介
	 */
    private String companyComment;


    /**
     * 别名（此字段不是对应数据表的字段）
     */
    private String aliasName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getMajorType() {
        return majorType;
    }

    public void setMajorType(String majorType) {
        this.majorType = majorType == null ? null : majorType.trim();
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate == null ? null : certificate.trim();
    }

    public String getMainField() {
        return mainField;
    }

    public void setMainField(String mainField) {
        this.mainField = mainField == null ? null : mainField.trim();
    }

    public String getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(String isAuthentication) {
        this.isAuthentication = isAuthentication == null ? null : isAuthentication.trim();
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason == null ? null : rejectReason.trim();
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType == null ? null : companyType.trim();
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail == null ? null : companyEmail.trim();
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName == null ? null : companyShortName.trim();
    }

    public String getCompanyFax() {
        return companyFax;
    }

    public void setCompanyFax(String companyFax) {
        this.companyFax = companyFax == null ? null : companyFax.trim();
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType == null ? null : serverType.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative == null ? null : legalRepresentative.trim();
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone == null ? null : companyPhone.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    public String getIndexShow() {
        return indexShow;
    }

    public void setIndexShow(String indexShow) {
        this.indexShow = indexShow == null ? null : indexShow.trim();
    }

    public String getBusinessLicenseNumber() {
        return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber == null ? null : businessLicenseNumber.trim();
    }

    public String getOrganizationCodeNumber() {
        return organizationCodeNumber;
    }

    public void setOrganizationCodeNumber(String organizationCodeNumber) {
        this.organizationCodeNumber = organizationCodeNumber == null ? null : organizationCodeNumber.trim();
    }

    public String getMicroUrl() {
        return microUrl;
    }

    public void setMicroUrl(String microUrl) {
        this.microUrl = microUrl == null ? null : microUrl.trim();
    }

    public String getMicroTemplate() {
        return microTemplate;
    }

    public void setMicroTemplate(String microTemplate) {
        this.microTemplate = microTemplate == null ? null : microTemplate.trim();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public String getCompanyComment() {
        return companyComment;
    }

    public void setCompanyComment(String companyComment) {
        this.companyComment = companyComment == null ? null : companyComment.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAliasName() {
        if(StringUtil.isNullOrEmpty(aliasName)){
            aliasName = companyName;
        }
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}