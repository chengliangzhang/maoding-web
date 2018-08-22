package com.maoding.projectcost.dto;

import com.maoding.attach.dto.FileDataDTO;
import com.maoding.core.constant.ProjectCostConst;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostDTO
 * 类描述：项目费用总记录表（项目中某款项的总金额，包含支付方，和收款方）
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostDataDTO  extends ProjectCooperatorCostDTO{


    /**
     * 当前记录创建的组织
     */
    private String operateCompanyId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 付款方公司id
     */
    private String fromCompanyId;

    /**
     * 收款方公司id
     */
    private String toCompanyId;

    /**
     * 收款方、付款方名称
     */
    private String relationCompanyName;

    /**
     * 类型1:合同总金额，2：技术审查费,3合作设计费付款
     */
    private String type;

    private String typeName;

    /**
     * 备注
     */
    private String remark;

    private List<FileDataDTO> attachList = new ArrayList<>();

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
    }

    public String getToCompanyId() {
        return toCompanyId;
    }

    public void setToCompanyId(String toCompanyId) {
        this.toCompanyId = toCompanyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRelationCompanyName() {
        return relationCompanyName;
    }

    public void setRelationCompanyName(String relationCompanyName) {
        this.relationCompanyName = relationCompanyName;
    }

    public List<FileDataDTO> getAttachList() {
        return attachList;
    }

    public void setAttachList(List<FileDataDTO> attachList) {
        this.attachList = attachList;
    }

    public String getTypeName() {
        typeName = ProjectCostConst.COST_TYPE_MAP.get(type);
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOperateCompanyId() {
        return operateCompanyId;
    }

    public void setOperateCompanyId(String operateCompanyId) {
        this.operateCompanyId = operateCompanyId;
    }
}
