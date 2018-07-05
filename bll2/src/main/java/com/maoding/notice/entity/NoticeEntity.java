package com.maoding.notice.entity;

import com.maoding.core.base.entity.BaseEntity;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：NoticeEntity
 * 类描述：通知公告实体
 * 作    者：MaoSF
 * 日    期：2016年11月30日-下午3:10:45
 */
public class NoticeEntity extends BaseEntity{

    private String noticeTitle;

    private String noticeNo;

    private String noticeStatus;

    private String noticeIsPush;

    private String companyId;

    private String noticePublisher;

    private String noticePublishdate;

    private String noticeContent;

    private Integer noticeType;



    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle == null ? null : noticeTitle.trim();
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo == null ? null : noticeNo.trim();
    }

    public String getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(String noticeStatus) {
        this.noticeStatus = noticeStatus == null ? null : noticeStatus.trim();
    }

    public String getNoticeIsPush() {
        return noticeIsPush;
    }

    public void setNoticeIsPush(String noticeIsPush) {
        this.noticeIsPush = noticeIsPush == null ? null : noticeIsPush.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getNoticePublisher() {
        return noticePublisher;
    }

    public void setNoticePublisher(String noticePublisher) {
        this.noticePublisher = noticePublisher == null ? null : noticePublisher.trim();
    }

    public String getNoticePublishdate() {
        return noticePublishdate;
    }

    public void setNoticePublishdate(String noticePublishdate) {
        this.noticePublishdate = noticePublishdate;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent == null ? null : noticeContent.trim();
    }

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }
}