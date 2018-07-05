package com.maoding.notice.dto;

import com.maoding.core.base.dto.BaseDTO;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：NoticeOrgEntity
 * 类描述：通知公告-组织中间实体
 * 作    者：MaoSF
 * 日    期：2016年11月30日-下午3:10:45
 */
public class NoticeOrgDTO extends BaseDTO{

    private String noticeId;

    private String orgId;

    private String orgName;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId == null ? null : noticeId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}