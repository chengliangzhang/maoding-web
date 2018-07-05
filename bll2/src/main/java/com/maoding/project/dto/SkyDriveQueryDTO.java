package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/7/2 20:48
 * 描    述 :
 */
public class SkyDriveQueryDTO extends BaseDTO {
    /** 编号过滤条件 */
    List<String> fileIdList;

    public List<String> getFileIdList() {
        return fileIdList;
    }

    public void setFileIdList(List<String> fileIdList) {
        this.fileIdList = fileIdList;
    }
}
