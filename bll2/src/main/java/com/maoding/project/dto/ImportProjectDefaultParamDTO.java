package com.maoding.project.dto;

import com.maoding.user.dto.CurrentUserDTO;

/**
 * Created by Chengliang.zhang on 2017/7/26.
 */
public class ImportProjectDefaultParamDTO extends CurrentUserDTO{
    /** 立项组织ID */
    String creatorOrgId;

    public String getCreatorOrgId() {
        return creatorOrgId;
    }

    public void setCreatorOrgId(String creatorOrgId) {
        this.creatorOrgId = creatorOrgId;
    }
}
