package com.maoding.org.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.org.dao.OrganizationDao;
import com.maoding.org.dto.PartnerQueryDTO;
import com.maoding.org.entity.InviteEntity;
import com.maoding.org.entity.PartnerEntity;
import com.maoding.project.dto.ProjectPartnerDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/5/8.
 */
@Service("organizationDao")
public class OrganizationDaoImpl extends GenericDao<PartnerEntity> implements OrganizationDao {
    /**
     * 获取项目合伙人列表
     *
     * @param dto 合伙人查找条件
     *            必填字段：fromCompanyId,projectId
     */
    @Override
    public List<ProjectPartnerDTO> getProjectPartnerList(PartnerQueryDTO dto) {
        return this.sqlSession.selectList("GetProjectPartnerMapper.getProjectPartnerList",dto);
    }

    /**
     * 保存邀请记录
     *
     * @param entity 邀请记录
     *               必填字段：companyId,userId,phone,verifyUrl,type
     */
    @Override
    public void insertInvite(InviteEntity entity) {

    }
}
