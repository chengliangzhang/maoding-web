package com.maoding.project.dto;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/3 17:59
 * 描    述 :
 */
public class ProjectWorkingHourTableDTO {
    private String id;
    private String projectNo;
    private String projectName;
    private Integer num;
    private String hours;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
