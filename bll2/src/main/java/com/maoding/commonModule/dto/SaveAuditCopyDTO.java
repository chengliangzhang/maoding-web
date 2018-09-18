package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveAuditCopyDTO extends BaseDTO {

    /**
     * 所属记录的id（目前是 processType的id）
     */
    private String targetId;

    /**
     * targetId关联记录的类型（1= processType表中的记录）
     */
    private String targetType;

    /**
     * 通知方式
     */
    private Integer noticeType;

    /**
     * 抄送人员.组列表
     */
    List<AuditCopyDTO> copyList = new ArrayList<>();

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public List<AuditCopyDTO> getCopyList() {
        return copyList;
    }

    public void setCopyList(List<AuditCopyDTO> copyList) {
        this.copyList = copyList;
    }

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }
}
