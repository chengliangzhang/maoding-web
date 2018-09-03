package com.maoding.project.dto;

import com.maoding.project.entity.ProjectSkyDriveEntity;

public class NetFileDTO extends ProjectSkyDriveEntity {

    private String crtFilePath;

    public String getCrtFilePath() {
        return crtFilePath;
    }

    public void setCrtFilePath(String crtFilePath) {
        this.crtFilePath = crtFilePath;
    }
}
