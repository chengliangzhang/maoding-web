package com.maoding.hxIm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maoding.core.base.entity.BaseEntity;

/**
 * IM群
 */
public class ImGroupEntity extends BaseEntity {

    /** 环信群ID */
    private String groupNo;

    /** 群名 */
    private String groupName;

    /** 群类型：0-公司群 1-部门群 2-自定义群 3-项目群 */
    private Integer groupType;

    /** 群主ID，逗号分隔 */
    private String groupOwner;

    /** 群状态，0-待创建 1-已启用 2-已禁用 */
    private Integer groupStatus;

    /** 群头像，从grouppath开始 */
    private String groupImg;

    /** 根组织ID */
    private String rootOrgId;

    /** 组织ID */
    private String orgId;

    /**
     * 项目群组（项目id）
     */
    private String nodeId;

    /** 置顶 */
    private Boolean topHold;

    /** 最后操作队列号 */
    private Long lastQueueNo;

    /** 版本控制(乐观锁） */
    @JsonIgnore
    private Long upVersion;

    /** 已删除 **/
    private Boolean deleted;

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public Integer getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(Integer groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getGroupImg() {
        return groupImg;
    }

    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    public String getRootOrgId() {
        return rootOrgId;
    }

    public void setRootOrgId(String rootOrgId) {
        this.rootOrgId = rootOrgId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Boolean getTopHold() {
        return topHold;
    }

    public void setTopHold(Boolean topHold) {
        this.topHold = topHold;
    }

    public Long getLastQueueNo() {
        return lastQueueNo;
    }

    public void setLastQueueNo(Long lastQueueNo) {
        this.lastQueueNo = lastQueueNo;
    }

    public Long getUpVersion() {
        return upVersion;
    }

    public void setUpVersion(Long upVersion) {
        this.upVersion = upVersion;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
