package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 文档库重命名DTO
 */
public class ProjectSkyDriveRenameDTO extends BaseDTO {
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}