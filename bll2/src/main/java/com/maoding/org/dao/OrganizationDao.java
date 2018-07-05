package com.maoding.org.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.dto.PartnerQueryDTO;
import com.maoding.org.entity.InviteEntity;
import com.maoding.org.entity.PartnerEntity;
import com.maoding.project.dto.ProjectPartnerDTO;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/5/6.
 */
public interface OrganizationDao extends BaseDao<PartnerEntity> {
    /**
     * 获取项目合伙人列表
     */
    List<ProjectPartnerDTO> getProjectPartnerList(PartnerQueryDTO dto);

    /**
     * 保存邀请记录
     */
    void insertInvite(InviteEntity entity);
}
