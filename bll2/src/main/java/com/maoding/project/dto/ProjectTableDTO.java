package com.maoding.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.projectmember.dto.ProjectMemberDTO;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectSaveDTO
 * 类描述：经营列表DTO
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:35:50
 */
public class ProjectTableDTO extends BaseDTO {
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 乙方
     */
    private String companyBid;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * 项目编号
     */
    private String projectNo;

    /**
     * 合同签订日期
     */
    private String signDate;


    /**
     * 立项日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date projectCreateDate;

    /**
     * 合同总金额
     */
    private BigDecimal totalContractAmount;

    /**
     * 组织Id
     */
    private String companyId;

    /**
     * 项目承接人
     */
    private String companyName;

    /**
     * 设计人（接包人）
     */
    private String designCompanyName;

    /**
     * 发包人
     */
    private String fromCompanyName;

    /**
     * 乙方
     */
    private String partyB;

    /**
     * 甲方（建设单位）
     */
    private String partyA;

    private String parentProjectid;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;

    private String createBy;

    /**
     * 是否存在外部合作（0，不存在，1，存在）
     */
    private Integer outerCooperatorFlag;

    /**
     * 任务超时标示（0：不超时，1：超时）
     */
    private Integer overtimeFlag;

    private List<ProjectDesignContentShow> designContentList = new ArrayList<ProjectDesignContentShow>();

    private List<ProjectMemberDTO> projectMemberEntities = new ArrayList<>();
    /**
     * 经营负责人名称
     */
    private String busPersonInCharge;

    private String busPersonInChargeUserId;
    /**
     * 经营负责人助理
     */
    private String busPersonInChargeAssistant;

    private String busPersonInChargeAssistantUserId;
    /**
     * 设计负责人名称
     */
    private String designPersonInCharge;
    private String designPersonInChargeUserId;
    /**
     * 设计负责人助理
     */
    private String designPersonInChargeAssistant;
    private String designPersonInChargeAssistantUserId;
    /**
     * 关注项目ID
     */
    private String attentionId;

    /**
     * 是否是我的项目（0，存在，1，不存在）
     */
    private String isMyProject;

    private String companyUserId;

    private Date updateDate;

    private String address;

    private String helpCompanyUserName;

    private String buildName;

    private String buildType;

    private boolean childId;

    private String constructCompany;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCompanyBid() {
        return companyBid;
    }

    public void setCompanyBid(String companyBid) {
        this.companyBid = companyBid;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public BigDecimal getTotalContractAmount() {
        return totalContractAmount;
    }

    public void setTotalContractAmount(BigDecimal totalContractAmount) {
        this.totalContractAmount = totalContractAmount;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getParentProjectid() {
        return parentProjectid;
    }

    public void setParentProjectid(String parentProjectid) {
        this.parentProjectid = parentProjectid;
    }

    public String getDesignCompanyName() {
        return designCompanyName;
    }

    public void setDesignCompanyName(String designCompanyName) {
        this.designCompanyName = designCompanyName;
    }

    public String getPartyB() {
        return partyB;
    }

    public void setPartyB(String partyB) {
        this.partyB = partyB;
    }

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getFromCompanyName() {
        return fromCompanyName;
    }

    public void setFromCompanyName(String fromCompanyName) {
        this.fromCompanyName = fromCompanyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getAttentionId() {
        return attentionId;
    }

    public void setAttentionId(String attentionId) {
        this.attentionId = attentionId;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getIsMyProject() {
        return isMyProject;
    }

    public void setIsMyProject(String isMyProject) {
        this.isMyProject = isMyProject;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDesignPersonInCharge() {
        return designPersonInCharge;
    }

    public void setDesignPersonInCharge(String designPersonInCharge) {
        this.designPersonInCharge = designPersonInCharge;
    }

    public String getBusPersonInCharge() {
        return busPersonInCharge;
    }

    public void setBusPersonInCharge(String busPersonInCharge) {
        this.busPersonInCharge = busPersonInCharge;
    }

    public List<ProjectDesignContentShow> getDesignContentList() {
        return designContentList;
    }

    public void setDesignContentList(List<ProjectDesignContentShow> designContentList) {
        this.designContentList = designContentList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getOuterCooperatorFlag() {
        return outerCooperatorFlag;
    }

    public void setOuterCooperatorFlag(Integer outerCooperatorFlag) {
        this.outerCooperatorFlag = outerCooperatorFlag;
    }

    public Integer getOvertimeFlag() {
        return overtimeFlag;
    }

    public void setOvertimeFlag(Integer overtimeFlag) {
        this.overtimeFlag = overtimeFlag;
    }

    public String getHelpCompanyUserName() {
        return helpCompanyUserName;
    }

    public void setHelpCompanyUserName(String helpCompanyUserName) {
        this.helpCompanyUserName = helpCompanyUserName;
    }

    public Date getProjectCreateDate() {
        return projectCreateDate;
    }

    public void setProjectCreateDate(Date projectCreateDate) {
        this.projectCreateDate = projectCreateDate;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public boolean isChildId() {
        return childId;
    }

    public void setChildId(boolean childId) {
        this.childId = childId;
    }

    public String getConstructCompany() {
        return constructCompany;
    }

    public void setConstructCompany(String constructCompany) {
        this.constructCompany = constructCompany;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    //0：立项人，1：经营负责人，2：设计负责人,3,任务负责人,4.设计，5，校对，6，审核，经营助理：7，设计助理
    public List<ProjectMemberDTO> getProjectMemberEntities() {
        return projectMemberEntities;
    }

    public void setProjectMemberEntities(List<ProjectMemberDTO> projectMemberEntities) {
        for (ProjectMemberDTO entity : projectMemberEntities) {
            if (1 == entity.getMemberType()) {
                busPersonInCharge = entity.getCompanyUserName();
                busPersonInChargeUserId = entity.getCompanyUserId();
            } else if (2 == entity.getMemberType()) {
                designPersonInCharge = entity.getCompanyUserName();
                designPersonInChargeUserId = entity.getCompanyUserId();
            } else if (7 == entity.getMemberType()) {
                busPersonInChargeAssistant = entity.getCompanyUserName();
                busPersonInChargeAssistantUserId = entity.getCompanyUserId();
            } else if (8 == entity.getMemberType()) {
                designPersonInChargeAssistant = entity.getCompanyUserName();
                designPersonInChargeAssistantUserId = entity.getCompanyUserId();
            }
        }
        this.projectMemberEntities = projectMemberEntities;
    }

    public String getBusPersonInChargeAssistant() {
        return busPersonInChargeAssistant;
    }

    public void setBusPersonInChargeAssistant(String busPersonInChargeAssistant) {
        this.busPersonInChargeAssistant = busPersonInChargeAssistant;
    }

    public String getDesignPersonInChargeAssistant() {
        return designPersonInChargeAssistant;
    }

    public void setDesignPersonInChargeAssistant(String designPersonInChargeAssistant) {
        this.designPersonInChargeAssistant = designPersonInChargeAssistant;
    }

    public String getBusPersonInChargeUserId() {
        return busPersonInChargeUserId;
    }

    public void setBusPersonInChargeUserId(String busPersonInChargeUserId) {
        this.busPersonInChargeUserId = busPersonInChargeUserId;
    }

    public String getBusPersonInChargeAssistantUserId() {
        return busPersonInChargeAssistantUserId;
    }

    public void setBusPersonInChargeAssistantUserId(String busPersonInChargeAssistantUserId) {
        this.busPersonInChargeAssistantUserId = busPersonInChargeAssistantUserId;
    }

    public String getDesignPersonInChargeUserId() {
        return designPersonInChargeUserId;
    }

    public void setDesignPersonInChargeUserId(String designPersonInChargeUserId) {
        this.designPersonInChargeUserId = designPersonInChargeUserId;
    }

    public String getDesignPersonInChargeAssistantUserId() {
        return designPersonInChargeAssistantUserId;
    }

    public void setDesignPersonInChargeAssistantUserId(String designPersonInChargeAssistantUserId) {
        this.designPersonInChargeAssistantUserId = designPersonInChargeAssistantUserId;
    }
}
