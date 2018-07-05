package com.maoding.org.entity;


import com.maoding.core.base.entity.BaseEntity;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyAttachEntity
 * 类描述：组织附件表
 * 作    者：wangrb
 * 日    期：2015年11月18日-下午5:28:09
 */
public class CompanyAttachEntity extends BaseEntity{
	

	/**
	 * 公司id
	 */
    private String companyId;

    /**
     * 附件名
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 附件路径
     */
    private String filePath;

    private String fileGroup;

    /**
     * 序号
     */
    private int seq;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getFileGroup() {
        return fileGroup;
    }

    public void setFileGroup(String fileGroup) {
        this.fileGroup = fileGroup;
    }
}