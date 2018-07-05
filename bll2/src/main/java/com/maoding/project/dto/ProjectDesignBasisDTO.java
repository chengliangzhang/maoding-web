package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.util.StringUtil;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：DesignBasisDTO
 * 类描述：设计依据DTO
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:48:50
 */
public class ProjectDesignBasisDTO extends BaseDTO {

    private String projectId;
    /**
     * 名称
     */
    private String designBasis;

    /**
     * 收文时间
     */
    private String basisDate;

    /**
     * 电子文件
     */
    private String filePath;

    /**
     * 电子文件名称
     */
    private String fileName;

    /**
     * 状态
     */
    private String status;

    /**
     * 排序
     */
    private int seq;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDesignBasis() {
        return designBasis;
    }

    public void setDesignBasis(String designBasis) {
        this.designBasis = designBasis;
    }

    public String getBasisDate() {
        if(StringUtil.isNullOrEmpty(basisDate)){
            return null;
        }
        return basisDate;
    }

    public void setBasisDate(String basisDate) {
        this.basisDate = basisDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
