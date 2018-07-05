package com.maoding.conllaboration.entity;


import com.maoding.core.base.entity.BaseEntity;

/**
 * Created by Wuwq on 2017/2/9.
 * 组织磁盘空间
 */
public class CompanyDiskEntity extends BaseEntity implements java.io.Serializable {

    /**
     * 组织Id（Id主键也是存companyId)
     */
    private String companyId;
    /**
     * 总容量
     */
    private Long totalSize;
    /**
     * 协同文档是否云端存储
     */
    private Boolean corpOnCloud;
    /**
     * 协同占用容量
     */
    private Long corpSize;
    /**
     * 文档库占用容量
     */
    private Long docmgrSize;
    /**
     * 其他占用容量
     */
    private Long otherSize;
    /**
     * 剩余容量
     */
    private Long freeSize;
    /**
     * 版本控制(乐观锁）
     */
    private Long upVersion;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Boolean getCorpOnCloud() {
        return corpOnCloud;
    }

    public void setCorpOnCloud(Boolean corpOnCloud) {
        this.corpOnCloud = corpOnCloud;
    }

    public Long getCorpSize() {
        return corpSize;
    }

    public void setCorpSize(Long corpSize) {
        this.corpSize = corpSize;
    }

    public Long getDocmgrSize() {
        return docmgrSize;
    }

    public void setDocmgrSize(Long docmgrSize) {
        this.docmgrSize = docmgrSize;
    }

    public Long getOtherSize() {
        return otherSize;
    }

    public void setOtherSize(Long otherSize) {
        this.otherSize = otherSize;
    }

    public Long getFreeSize() {
        return freeSize;
    }

    public void setFreeSize(Long freeSize) {
        this.freeSize = freeSize;
    }

    /**
     * 重新计算剩余空间
     */
    public Long recalcFreeSize() {
        //判断协同文档是否云端存储
        if (corpOnCloud)
            freeSize = totalSize - corpSize - docmgrSize - otherSize;
        else
            freeSize = totalSize - docmgrSize - otherSize;

        return freeSize;
    }

    public Long getUpVersion() {
        return upVersion;
    }

    public void setUpVersion(Long upVersion) {
        this.upVersion = upVersion;
    }
}
