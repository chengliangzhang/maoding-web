package com.maoding.financial.dto;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.project.entity.ProjectSkyDriveEntity;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/22 15:06
 * 描    述 : 请假、出差DTO
 */
@Alias("LeaveDetailDTO")
public class LeaveDetailDTO extends BaseDTO implements Serializable {
    private String mainId;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date applyDate;//申请时间
    private String applyName;//申请人
    private String typeName;//请假类型名称
    private String type;//请假类型
    private String comment;//请假事由
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date startTime;//开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date endTime;//结束时间
    private String leaveTime;//请假时长
    private String auditPerson;//审批人
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date approveDate;//审批时间
    private String address;//出差地址
    private String projectName;//关联项目
    private List<ProjectSkyDriveEntity> projectSkyDriveEntity = Lists.newArrayList();

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getTypeName() {
        Integer type = Integer.parseInt(getType());
        if (1 == type) {
            typeName = "年假";
        } else if (2 == type) {
            typeName = "事假";
        } else if (3 == type) {
            typeName = "病假";
        } else if (4 == type) {
            typeName = "调休假";
        } else if (5 == type) {
            typeName = "婚假";
        } else if (6 == type) {
            typeName = "产假";
        } else if (7 == type) {
            typeName = "陪产假";
        } else if (8 == type) {
            typeName = "丧假";
        } else if (9 == type) {
            typeName = "其他";
        } else if (10 == type) {
            typeName = "出差";
        } else {
            typeName = "其他";
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ProjectSkyDriveEntity> getProjectSkyDriveEntity() {
        return projectSkyDriveEntity;
    }

    public void setProjectSkyDriveEntity(List<ProjectSkyDriveEntity> projectSkyDriveEntity) {
        this.projectSkyDriveEntity = projectSkyDriveEntity;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
