package com.maoding.org.service;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.org.dto.OrgAuthenticationDTO;
import com.maoding.org.dto.OrgAuthenticationQueryDTO;
import com.maoding.org.dto.OrgAuthorizeResultDTO;
import com.maoding.org.dto.PartnerQueryDTO;
import com.maoding.org.entity.InviteEntity;
import com.maoding.org.entity.PartnerEntity;
import com.maoding.project.dto.ProjectPartnerDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by wrb on 2017/5/8.
 */
public interface OrganizationService extends BaseDao<PartnerEntity> {
    /**
     * 获取项目合伙人列表
     */
    List<ProjectPartnerDTO> getProjectPartnerList(PartnerQueryDTO dto);

    /**
     * 保存邀请记录
     */
    void insertInvite(InviteEntity entity);

    /**
     * 方法描述：外部合作－解除关系
     * 作者：wrb
     * 日期：2017/5/9
     * @param:
     * @return:
     */
    AjaxMessage relieveRelationship(String id);

    /**
     * 方法描述：重新发送短信通知
     * 作者：wrb
     * 日期：2017/5/9
     * @param:
     * @return:
     */
    AjaxMessage resendSMS(String id,String currentUserId,String currentCompanyId);
}
