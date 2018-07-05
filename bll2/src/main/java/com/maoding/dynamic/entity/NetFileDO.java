package com.maoding.dynamic.entity;

import com.maoding.core.base.entity.BaseEntity;

/**
 * Created by Wuwq on 2017/05/31.
 */
public class NetFileDO extends BaseEntity {

    /**
     * 组织ID
     */
    private String companyId;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 父目录id
     */
    private String pid;
    /**
     * 类型参考 NetFileType
     */
    private Integer type;
    /**
     * 0，自定义，1，默认
     */
    private Integer isCustomize;
    /**
     * 文件/文件夹的文件路径id-id
     */
    private String skyDrivePath;
    /**
     * 文件/文件夹名
     */
    private String fileName;
    /**
     * 扩展名
     */
    private String fileExtName;
    /**
     * 上传的分组
     */
    private String fileGroup;
    /**
     * 若是文件，文件在fastdfs中的路径
     */
    private String filePath;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 预留字段4(排序字段)
     */
    private Integer param4;
    /**
     * 关联任务的id
     */
    private String taskId;
    /**
     * 关联Id
     **/
    private String targetId;
    /**
     * 状态（0：有效，1：无效（删除））
     */
    private String status;

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsCustomize() {
        return isCustomize;
    }

    public void setIsCustomize(Integer isCustomize) {
        this.isCustomize = isCustomize;
    }

    public String getSkyDrivePath() {
        return skyDrivePath;
    }

    public void setSkyDrivePath(String skyDrivePath) {
        this.skyDrivePath = skyDrivePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    public String getFileGroup() {
        return fileGroup;
    }

    public void setFileGroup(String fileGroup) {
        this.fileGroup = fileGroup;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getParam4() {
        return param4;
    }

    public void setParam4(Integer param4) {
        this.param4 = param4;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
