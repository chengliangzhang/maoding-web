package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */
public class QueryProjectDTO extends BaseDTO {
    //沿用原有定义
    String companyUserId;
    String relationCompany;
    Integer status;
    String buildType;
    List<String> buildTypeList;
    List<String> buildTypeNames;
    Integer pageIndex;
    Integer pageSize;
    Integer type;

    //如果存在pageIndex和pageSize,计算出startLine和maxCount,也可以直接使用
    Integer startLine; //起始记录序数
    Integer maxCount;  //返回记录个数

    //原projectName，因用于部分项目名查找，故改名以示区别
    String projectNameMask;
    //设计组织
    String orgMask;
    List<String> orgIds;
    //起始日期和终止日期，所查记录范围为 startTime <= 立项日期 < 终止日期+1
    String startTime;
    String endTime;
    //立项人编号
    String createBy;
    //排序字段名及排序方式
    String orderField; //排序字段名，目前只能为null或createDate，如果为null，则按默认方式排序，否则按direction方式排序
    Integer direction; //排序方式，1为升序，其他为降序，仅在orderField不为空时起作用

    String companyId;
    String constructCompany;
    String companyBid;

    String startSignDate;
    String endSignDate;
    String province;
    String city;
    String county;
    String keyWord;
    String orderSign;
    List<String> memberProjects;
    String memberStatusType;//判断是否查询条件memberProjects为空

    boolean isNeedSearchBuildType;

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getRelationCompany() {
        return relationCompany;
    }

    public void setRelationCompany(String relationCompany) {
        this.relationCompany = relationCompany;
    }

    public String getProjectNameMask() {
        return projectNameMask;
    }

    public void setProjectNameMask(String projectNameMask) {
        this.projectNameMask = projectNameMask;
    }

    public String getOrgMask() {
        return orgMask;
    }

    public void setOrgMask(String orgMask) {
        this.orgMask = orgMask;
    }

    public List<String> getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(List<String> orgIds) {
        this.orgIds = orgIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getConstructCompany() {
        return constructCompany;
    }

    public void setConstructCompany(String constructCompany) {
        this.constructCompany = constructCompany;
    }

    public String getCompanyBid() {
        return companyBid;
    }

    public void setCompanyBid(String companyBid) {
        this.companyBid = companyBid;
    }

    public String getStartSignDate() {
        return startSignDate;
    }

    public void setStartSignDate(String startSignDate) {
        this.startSignDate = startSignDate;
    }

    public String getEndSignDate() {
        return endSignDate;
    }

    public void setEndSignDate(String endSignDate) {
        this.endSignDate = endSignDate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getOrderSign() {
        return orderSign;
    }

    public void setOrderSign(String orderSign) {
        this.orderSign = orderSign;
    }

    public List<String> getBuildTypeList() {
        return buildTypeList;
    }

    public void setBuildTypeList(List<String> buildTypeList) {
        this.buildTypeList = buildTypeList;
    }

    public List<String> getMemberProjects() {
        return memberProjects;
    }

    public void setMemberProjects(List<String> memberProjects) {
        this.memberProjects = memberProjects;
    }

    public String getMemberStatusType() {
        return memberStatusType;
    }

    public void setMemberStatusType(String memberStatusType) {
        this.memberStatusType = memberStatusType;
    }

    public boolean isNeedSearchBuildType() {
        return isNeedSearchBuildType;
    }

    public void setNeedSearchBuildType(boolean needSearchBuildType) {
        isNeedSearchBuildType = needSearchBuildType;
    }

    public List<String> getBuildTypeNames() {
        return buildTypeNames;
    }

    public void setBuildTypeNames(List<String> buildTypeNames) {
        this.buildTypeNames = buildTypeNames;
    }
}
