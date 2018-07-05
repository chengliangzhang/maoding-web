package com.maoding.user.dto;

import com.maoding.core.base.dto.BaseDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Idccapp21 on 2016/7/28.
 */
public class UploadAttachDTO extends BaseDTO {

    private MultipartFile uploadFile;
    private String attachType;

    /**
     * 上传logo的公司Id
     */
    private String companyId;

    private String projectId;

    private int seq;

    public String getAttachType() {
        return attachType;
    }

    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }


    public MultipartFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(MultipartFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
