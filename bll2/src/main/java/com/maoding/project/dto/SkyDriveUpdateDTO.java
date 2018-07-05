package com.maoding.project.dto;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/7/2 21:00
 * 描    述 :
 */
public class SkyDriveUpdateDTO extends ProjectSkyDriveDTO {
    /** 更改字段 */
    ProjectSkyDriveDTO data;
    /** 过滤条件 */
    SkyDriveQueryDTO query;

    public ProjectSkyDriveDTO getData() {
        return data;
    }

    public void setData(ProjectSkyDriveDTO data) {
        this.data = data;
    }

    public SkyDriveQueryDTO getQuery() {
        return query;
    }

    public void setQuery(SkyDriveQueryDTO query) {
        this.query = query;
    }
}
