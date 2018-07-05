package com.maoding.user.entity;

import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.StringUtil;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：UserEntity
 * 类描述：个人基本信息
 * 作    者：MaoSF
 * 日    期：2016年7月6日-下午4:46:50
 */
public class UserEntity extends BaseEntity implements java.io.Serializable{
	

	/**
	 * 人员姓名
	 */
    private String userName;

	/**
	 * 手机号
	 */
    private String cellphone;

	/**
	 * 邮箱
	 */
    private String email;

	/**
	 * 性别
	 */
    private String sex;

	/**
	 * 出生日期
	 */
    private String birthday;

	/**
	 * 婚姻状况
	 */
    private String marriageStatus;

	/**
	 * 身份证号
	 */
    private String idCard;

	/**
	 * 籍贯
	 */
    private String nativePlace;

	/**
	 * 档案所在地
	 */
    private String fileStorage;

	/**
	 * 户口所在地
	 */
    private String residencyLocation;

	/**
	 * 现居住地址
	 */
    private String address;

	/**
	 * 毕业学校
	 */
    private String graduationSchool;

	/**
	 * 所学专业
	 */
    private String major;

	/**
	 * 最高学历
	 */
    private String lastDegree;

	/**
	 * 最高学位
	 */
    private String highestDegree;

	/**
	 * 参加工作时间
	 */
    private String workBeginDate;

	/**
	 * 毕业时间
	 */
    private String graduationDate;

	/**
	 * 社保参保省市
	 */
    private String insuranceAddress;

	/**
	 * 社保号
	 */
    private String insuranceNo;

	/**
	 * 公积金账号
	 */
    private String accumulationFund;

	/**
	 * 发薪银行
	 */
    private String bankInfoNo;

	/**银行账号
	 * 
	 */
    private String bankAccount;

	/**
	 * 紧急联系人
	 */
    private String emergencyContact;

	/**
	 * 紧急联系人电话
	 */
    private String emergencyPhone;

	/**
	 * 英语水平
	 */
    private String englishLevel;

	/**
	 * 民族
	 */
    private String nation;

	/**
	 * 政治面貌
	 */
    private String politicalStatus;

	/**
	 * 爱好及特长
	 */
    private String hobby;

	/**
	 * 人事信息状态
	 */
    private String status;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone == null ? null : cellphone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getBirthday() {
        if(StringUtil.isNullOrEmpty(birthday)){
            return null;
        }
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus == null ? null : marriageStatus.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace == null ? null : nativePlace.trim();
    }

    public String getFileStorage() {
        return fileStorage;
    }

    public void setFileStorage(String fileStorage) {
        this.fileStorage = fileStorage == null ? null : fileStorage.trim();
    }

    public String getResidencyLocation() {
        return residencyLocation;
    }

    public void setResidencyLocation(String residencyLocation) {
        this.residencyLocation = residencyLocation == null ? null : residencyLocation.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getGraduationSchool() {
        return graduationSchool;
    }

    public void setGraduationSchool(String graduationSchool) {
        this.graduationSchool = graduationSchool == null ? null : graduationSchool.trim();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major == null ? null : major.trim();
    }

    public String getLastDegree() {
        return lastDegree;
    }

    public void setLastDegree(String lastDegree) {
        this.lastDegree = lastDegree == null ? null : lastDegree.trim();
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree == null ? null : highestDegree.trim();
    }

    public String getWorkBeginDate() {
        return workBeginDate;
    }

    public void setWorkBeginDate(String workBeginDate) {
        this.workBeginDate = workBeginDate;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
    }

    public String getInsuranceAddress() {
        return insuranceAddress;
    }

    public void setInsuranceAddress(String insuranceAddress) {
        this.insuranceAddress = insuranceAddress == null ? null : insuranceAddress.trim();
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo == null ? null : insuranceNo.trim();
    }

    public String getAccumulationFund() {
        return accumulationFund;
    }

    public void setAccumulationFund(String accumulationFund) {
        this.accumulationFund = accumulationFund == null ? null : accumulationFund.trim();
    }

    public String getBankInfoNo() {
        return bankInfoNo;
    }

    public void setBankInfoNo(String bankInfoNo) {
        this.bankInfoNo = bankInfoNo == null ? null : bankInfoNo.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact == null ? null : emergencyContact.trim();
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone == null ? null : emergencyPhone.trim();
    }

    public String getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(String englishLevel) {
        this.englishLevel = englishLevel == null ? null : englishLevel.trim();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus == null ? null : politicalStatus.trim();
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby == null ? null : hobby.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

}