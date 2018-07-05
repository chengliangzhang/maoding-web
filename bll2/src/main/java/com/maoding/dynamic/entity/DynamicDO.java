package com.maoding.dynamic.entity;

import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.StringUtil;
import com.maoding.dynamic.dto.DynamicConst;

import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/6/7.
 * 作为升级的动态表记录
 */
public class DynamicDO extends BaseEntity{
    /**
     * 操作者职责编号
     */
    private String companyUserId;
    /**
     * 项目编号
     */
    private String projectId;
    /**
     * 公司编号
     */
    private String companyId;
    /**
     * 被操作的信息类型
     */
    private Integer targetType;
    /**
     * 被操作的信息编号
     */
    private String targetId;
    /**
     * 动态类型编号
     */
    private Integer type;
    /**
     * 操作节点的名称
     */
    private String nodeName;
    /**
     * 操作信息描述
     */
    private String content;
    /**
     * 删除标志
     */
    private Integer deleted;

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content == null) return;
        if (StringUtil.isNullOrEmpty(this.content)){
            this.content = content;
        } else {
            this.content += DynamicConst.SEPARATOR + content;
        }
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
    
    public void setCommon(Integer type,String id,String projectId,String companyId,String userId,String companyUserId){
        if (getCreateBy() == null){
            setCreateBy(userId);
            setCreateDate(new Date());
        } else {
            setUpdateBy(userId);
            setUpdateDate(new Date());
        }
        setCompanyUserId(companyUserId);
        setProjectId(projectId);
        setCompanyId(companyId);
        setTargetType(type);
        setTargetId(id);
    }

    public DynamicDO(){}
    public DynamicDO(Integer type,String id,String projectId,String companyId,String userId,String companyUserId){
        setCommon(type,id,projectId,companyId,userId,companyUserId);
    }
}
