package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectSkyDriveDTO
 * 类描述：项目网络磁盘实体
 * 作    者：MaoSF
 * 日    期：2016年12月18日-上午10:13:28
 */
public class ProjectSkyDriveDTO extends BaseDTO {


    /**
     * 项目id
     */
    private String projectId;

    /**
     * 父目录id
     */
    private String pid;

    /**
     * 文件/文件夹的文件路径id-id
     */
    private String skyDrivePath;

    /**
     * 文件/文件夹名
     */
    private String fileName;

    /**
     * 若是文件，文件在fastdfs中的路径
     */
    private String filePath;

    /**
     * 文件的组
     */
    private String fileGroup;

    /**
     * 文件所属公司
     */
    private String companyId;

    /**
     *类型：文件目录：0，文件：1
     */
    private  int type;

    /**
     *文件大小
     */
    private long fileSize;

    /**
     * 文件类型
     */
    private String fileExtName;
    /**
     *0，自定义，1，默认 文件、文件夹
     */
    private int isCustomize;

    private Integer param4;

    private MultipartFile multipartFile;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getSkyDrivePath() {
        return skyDrivePath;
    }

    public void setSkyDrivePath(String skyDrivePath) {
        this.skyDrivePath = skyDrivePath == null ? null : skyDrivePath.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getFileGroup() {
        return fileGroup;
    }

    public void setFileGroup(String fileGroup) {
        this.fileGroup = fileGroup;
    }


    public int getIsCustomize() {
        return isCustomize;
    }

    public void setIsCustomize(int isCustomize) {
        this.isCustomize = isCustomize;
    }


    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    public Integer getParam4() {
        return param4;
    }

    public void setParam4(Integer param4) {
        this.param4 = param4;
    }
}