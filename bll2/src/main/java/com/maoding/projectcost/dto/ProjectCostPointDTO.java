package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostPointDTO
 * 类描述：费用节点表（记录费用在哪个节点上产生的，费用的描述，金额）
 * 项目费用收款节点表
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostPointDTO extends BaseDTO {

    /**
     * 总金额id
     */
    private String costId;
    /**
     * 项目id
     */
    private String projectId;

    /**
     * 回款节点描述
     */
    private String feeDescription;

    /**
     * 回款比例
     */
    private String feeProportion;
    /**
     * 回款金额
     */
    private BigDecimal fee;


    /**
     * 类型type（type=1:合同总金额,type=2:技术审查费，type=3：合作设计费，type=4：其他费用（付款），type=5:其他费用收款）
     */
    private String type;

    /**
     * 标示：1：正式协议，2：补充协议
     */
    private String flag;

    /**
     * 预留字段1
     */
    private int seq;

    /**
     * 父节点id
     */
    private String pid;



    private String taskId;

    private int deleteFlag;

    private String status;
    /**
     * 节点明细
     */
    private List<ProjectCostPointDTO> projectCostDetailList= new ArrayList<ProjectCostPointDTO>();


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(String feeDescription) {
        this.feeDescription = feeDescription;
    }

    public String getFeeProportion() {
        return feeProportion;
    }

    public void setFeeProportion(String feeProportion) {
        this.feeProportion = feeProportion;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<ProjectCostPointDTO> getProjectCostDetailList() {
        return projectCostDetailList;
    }

    public void setProjectCostDetailList(List<ProjectCostPointDTO> projectCostDetailList) {
        this.projectCostDetailList = projectCostDetailList;
    }

    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
