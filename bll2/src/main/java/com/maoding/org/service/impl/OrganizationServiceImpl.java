package com.maoding.org.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.bean.Sms;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.StringUtil;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyInviteDao;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dao.OrganizationDao;
import com.maoding.org.dto.PartnerQueryDTO;
import com.maoding.org.entity.*;
import com.maoding.org.service.OrganizationService;
import com.maoding.project.dto.ProjectPartnerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wrb on 2017/5/8.
 */
@Service("organizationService")
public class OrganizationServiceImpl extends GenericService<PartnerEntity> implements OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private CompanyInviteDao companyInviteDao;

    @Autowired
    private SmsSender smsSender;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Override
    public List<ProjectPartnerDTO> getProjectPartnerList(PartnerQueryDTO dto) {
        return organizationDao.getProjectPartnerList(dto);
    }

    @Override
    public void insertInvite(InviteEntity entity) {
        organizationDao.insertInvite(entity);
    }

    @Override
    public AjaxMessage relieveRelationship(String id) {
        int res = 0;
        res = organizationDao.deleteById(id);
        if(res>0){
            return AjaxMessage.succeed("解除成功");
        }else {
            return AjaxMessage.error("解除失败");
        }
    }

    @Override
    public AjaxMessage resendSMS(String id,String currentUserId,String currentCompanyId) {

        CompanyInviteEntity companyInviteEntity = companyInviteDao.selectById(id);

        if(null!=companyInviteEntity){

            CompanyEntity companyEntity = companyDao.selectById(companyInviteEntity.getCompanyId());
            CompanyUserEntity companyUser = companyUserDao.getCompanyUserByUserIdAndCompanyId(currentUserId,currentCompanyId);
            if(companyUser==null || companyEntity==null){
                return AjaxMessage.failed("操作失败");
            }
            //发送信息
            Sms sms = new Sms();
            sms.addMobile(companyInviteEntity.getInviteCellphone());
            sms.setMsg(StringUtil.format(SystemParameters.INVITE_PARENT_MSG3,companyUser.getUserName(),companyEntity.getCompanyName(),companyInviteEntity.getUrl()));
            smsSender.send(sms);
        }

        return AjaxMessage.succeed("发送成功");
    }
}
