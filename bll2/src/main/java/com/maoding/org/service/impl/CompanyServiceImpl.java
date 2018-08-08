package com.maoding.org.service.impl;

import com.maoding.conllaboration.service.CollaborationService;
import com.maoding.conllaboration.service.CompanyDiskService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.bean.Sms;
import com.maoding.core.constant.CompanyType;
import com.maoding.core.constant.NetFileType;
import com.maoding.core.constant.ProjectCostConst;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.*;
import com.maoding.exception.CustomException;
import com.maoding.hxIm.constDefine.ImGroupType;
import com.maoding.hxIm.service.ImService;
import com.maoding.org.dao.*;
import com.maoding.org.dto.*;
import com.maoding.org.entity.*;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.TeamOperaterService;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.dao.ProjectSkyDriverDao;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.entity.ProjectSkyDriveEntity;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.role.dao.RolePermissionDao;
import com.maoding.role.dao.RoleUserDao;
import com.maoding.role.dto.SaveRoleUserDTO;
import com.maoding.role.entity.RolePermissionEntity;
import com.maoding.role.entity.RoleUserEntity;
import com.maoding.role.service.RoleUserService;
import com.maoding.system.dao.DataDictionaryDao;
import com.maoding.system.entity.DataDictionaryEntity;
import com.maoding.user.dao.AccountDao;
import com.maoding.user.entity.AccountEntity;
import com.maoding.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyServiceImpl
 * 类描述：团队（公司）ServiceImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午4:24:38
 */
@Service("companyService")
public class CompanyServiceImpl extends GenericService<CompanyEntity> implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private DepartDao departDao;

    @Autowired
    private OrgDao orgDao;

    @Autowired
    private CompanyRelationDao companyRelationDao;

    @Autowired
    private CompanyRelationAuditDao companyRelationAuditDao;

    @Autowired
    private SubCompanyDao subCompanyDao;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private TeamOperaterDao teamOperaterDao;

    @Autowired
    private BusinessPartnerDao businessPartnerDao;

    @Autowired
    private DataDictionaryDao dataDictionaryDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleUserDao roleUserDao;

    @Autowired
    private OrgUserDao orgUserDao;

    @Autowired
    private SmsSender smsSender;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private TeamOperaterService teamOperaterService;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private CollaborationService collaborationService;

    @Autowired
    private CompanyInviteDao companyInviteDao;

    @Autowired
    private CompanyDiskService companyDiskService;

    @Autowired
    private ProjectSkyDriverDao projectSkyDriverDao;

    @Autowired
    private OrgAuthDao orgAuthDao;

    @Autowired
    private OrgAuthAuditDao orgAuthAuditDao;

    @Autowired
    private CompanyUserServiceImpl companyUserService;

    @Value("${server.url}")
    protected String serverUrl;

    @Value("${android.url}")
    protected String androidUrl;

    @Value("${ios.url}")
    protected String iosUrl;


    @Value("${fastdfs.url}")
    protected String fastdfsUrl;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private ImService imService;

    /**
     * 方法描述：验证公司信息
     * 作者：MaoSF
     * 日期：2016/8/19
     *
     * @param:
     * @return:
     */
    public AjaxMessage validateSaveOrUpdateCompany(CompanyDTO dto) throws Exception {

        if (StringUtil.isNullOrEmpty(dto.getCompanyName())) {
            return new AjaxMessage().setCode("1").setInfo("公司名不能为空");
        }
     /*   if (StringUtil.isNullOrEmpty(dto.getCompanyShortName())) {
            return new AjaxMessage().setCode("1").setInfo("公司简称不能为空");
        }
        CompanyEntity company = companyDao.getCompanyByCompanyName(dto.getCompanyName());
        CompanyEntity company2 = companyDao.getCompanyByCompanyShortName(dto.getCompanyShortName());
        if (StringUtil.isNullOrEmpty(dto.getId())) {
            if (company != null) {
                return new AjaxMessage().setCode("1").setInfo(dto.getCompanyName() + ":公司名已经被注册过");
            }
            if (company2 != null) {
                return new AjaxMessage().setCode("1").setInfo(dto.getCompanyShortName() + ":公司简称已经被占用");
            }
        } else {
            if (company != null && !dto.getId().equals(company.getId())) {
                if (dto.getCompanyName().equals(company.getCompanyName())) {
                    return new AjaxMessage().setCode("1").setInfo(dto.getCompanyName() + ":公司名已经被注册过");
                }
            }
            if (company2 != null && !dto.getId().equals(company2.getId())) {
                if (dto.getCompanyShortName().equals(company2.getCompanyShortName())) {
                    return new AjaxMessage().setCode("1").setInfo(dto.getCompanyShortName() + ":公司简称已经被占用");
                }
            }
        }*/
        return new AjaxMessage().setCode("0");
    }


    /**
     * 保存公司(创建组织)
     *
     * @param company
     * @throws Exception
     */
    @Override
    public String saveCompany(CompanyEntity company, String userName, String orgManagerId, String currUserId) throws Exception {
        String companyId = StringUtil.buildUUID();
        company.setId(companyId);
        company.setCreateBy(currUserId);
        this.companyDao.insert(company);

        OrgAuthEntity orgAuthentication = new OrgAuthEntity();
        orgAuthentication.setId(companyId);
        orgAuthentication.setAuthenticationStatus(0);
        orgAuthentication.setApplyDate(new Date());
        orgAuthentication.setExpiryDate(DateUtils.getDateAfter2(orgAuthentication.getApplyDate(), 30));
        this.orgAuthDao.insert(orgAuthentication);

        //复杂权限
        this.rolePermissionDao.deleteByCompanyId(company.getId());
        List<RolePermissionEntity> initData = this.rolePermissionDao.getAllDefaultPermission();
        if (!CollectionUtils.isEmpty(initData)) {
            for (RolePermissionEntity rolePermission : initData) {
                rolePermission.setId(StringUtil.buildUUID());
                rolePermission.setCompanyId(company.getId());
                this.rolePermissionDao.insert(rolePermission);
            }
        }

        //保存OrgEntity,组织基础表
        OrgEntity org = new OrgEntity();
        org.setId(companyId);//基础表id和公司表id一致
        org.setOrgType("0");
        org.setOrgStatus("0");
        org.setCreateBy(currUserId);
        orgDao.insert(org);

        //创建团队管理员
        TeamOperaterEntity teamOperaterEntity = new TeamOperaterEntity();
        teamOperaterEntity.setUserId(orgManagerId);
        teamOperaterEntity.setCompanyId(companyId);
        teamOperaterEntity.setCreateBy(currUserId);
        teamOperaterService.saveSystemManage(teamOperaterEntity);

        //添加公司成员信息
        CompanyUserEntity companyUserEntity = new CompanyUserEntity();
        String companyUserId = StringUtil.buildUUID();
        companyUserEntity.setId(companyUserId);
        companyUserEntity.setCompanyId(companyId);
        companyUserEntity.setUserId(orgManagerId);
        companyUserEntity.setAuditStatus("1");//自己创建直接通过
        companyUserEntity.setRelationType("1");
        companyUserEntity.setCreateBy(currUserId);
        companyUserEntity.setUserName(userName);
        companyUserDao.insert(companyUserEntity);

        //保存OrgEntity,组织基础表
        OrgEntity orgUserEntity = new OrgEntity();
        orgUserEntity.setId(companyUserId);//基础表id和人员表id一致
        orgUserEntity.setOrgType("4");
        orgUserEntity.setOrgStatus("0");
        orgUserEntity.setCreateBy(currUserId);
        orgDao.insert(orgUserEntity);

        //人加入组织中间表
        OrgUserEntity orgUser = new OrgUserEntity();
        orgUser.setId(StringUtil.buildUUID());
        orgUser.setOrgId(companyId);
        orgUser.setCompanyId(companyId);
        orgUser.setCuId(companyUserId);
        orgUser.setUserId(orgManagerId);
        orgUser.setCreateBy(currUserId);
        orgUserDao.insert(orgUser);

        //生成二维码
        projectSkyDriverService.createCompanyQrcode(companyId);

        //第五步，创建群组
        this.createImGroup(company.getId(), orgManagerId, company.getCompanyName());

        //通知协同
        this.collaborationService.pushSyncCMD_CU(companyId);

        this.companyDiskService.initDisk(companyId);

        return companyId;
    }


    @Override
    public AjaxMessage saveOrUpdateCompany(CompanyDTO dto) throws Exception {
        //简称=全称
        dto.setCompanyShortName(dto.getCompanyName());

        AjaxMessage ajaxMessage = validateSaveOrUpdateCompany(dto);
        if (!"0".equals(ajaxMessage.getCode())) {
            return ajaxMessage;
        }
        CompanyEntity entity = null;
        if (StringUtil.isNullOrEmpty(dto.getId())) {
            entity = new CompanyEntity();
            BaseDTO.copyFields(dto, entity);

            AccountEntity account = this.accountDao.selectById(dto.getAccountId());
            if (account == null) {
                return AjaxMessage.failed("操作失败");
            }
            String id = this.saveCompany(entity, dto.getAccountId(), account.getUserName(), dto.getAccountId());
            dto.setId(id);
        } else {
            entity = new CompanyEntity();
            BaseDTO.copyFields(dto, entity);
            entity.setUpdateBy(dto.getAccountId());
            companyDao.updateById(entity);

            //修改群组
            this.updateImGroup(entity.getId(), entity.getCompanyShortName());
        }
        return new AjaxMessage().setCode("0").setInfo("保存成功").setData(dto);
    }

    /**
     * 方法描述：添加分支机构校验方法
     * 作者：MaoSF
     * 日期：2016/11/29
     */
    public AjaxMessage validateCompany(SubCompanyDTO dto) throws Exception {

        if (StringUtil.isNullOrEmpty(dto.getCompanyName())) {
            return new AjaxMessage().setCode("1").setInfo("团队名称不能为空");
        }
        List<CompanyEntity> list = companyDao.listCompanyByCompanyName(dto.getCompanyName(), dto.getCompanyId(), dto.getType() + "");
        if (!CollectionUtils.isEmpty(list)) {
            return AjaxMessage.failed("当前组织下已存在该组织名的" + (dto.getType() == 2 ? "分公司" : "事业合伙人"));
        }
//        if (StringUtil.isNullOrEmpty(dto.getCompanyShortName())) {
//            return new AjaxMessage().setCode("1").setInfo("团队简称不能为空");
//        }

      /*  if (StringUtil.isNullOrEmpty(dto.getProvince())) {
            return new AjaxMessage().setCode("1").setInfo("团队地址不能为空");
        }

        if (this.getCompanyDtoByCompanyName(dto.getCompanyName()) != null) {
            return new AjaxMessage().setCode("1").setInfo(dto.getCompanyName() + ":团队名称已经存在");
        }
        if (companyDao.getCompanyByCompanyShortName(dto.getCompanyShortName()) != null) {
            return new AjaxMessage().setCode("1").setInfo(dto.getCompanyShortName() + ":公司简称已经被占用");
        }*/

        if (StringUtil.isNullOrEmpty(dto.getAccountId())) {
            if (StringUtil.isNullOrEmpty(dto.getCellphone())) {
                return new AjaxMessage().setCode("1").setInfo("负责人手机号码不能为空");
            }
            if (StringUtil.isNullOrEmpty(dto.getUserName())) {
                return new AjaxMessage().setCode("1").setInfo("负责人不能为空");
            }
        }

        return new AjaxMessage().setCode("0");
    }


    /**
     * 方法描述：增加分支机构
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     */
    @Override
    public AjaxMessage createSubCompany(SubCompanyDTO dto) throws Exception {
        //简称=全称
        dto.setCompanyShortName(dto.getCompanyName());
        AjaxMessage ajax = validateCompany(dto);
        if ("1".equals(ajax.getCode())) {//如果信息有误，直接返回
            return ajax;
        }
        //第一步，获取当前用户名
        String accountName = "";
        CompanyUserEntity companyUser = companyUserDao.getCompanyUserByUserIdAndCompanyId(dto.getAccountId(), dto.getCompanyId());
        if (companyUser != null) {
            accountName = companyUser.getUserName();
        }
        //获取父公司名称
        String pcompanyName = "";
        CompanyEntity pcompany = companyDao.selectById(dto.getCompanyId());
        if (pcompany != null) {
            pcompanyName = pcompany.getCompanyName();
        }
        String msg = "";
        //第二步，判断手机号码是否存在，如果不存在，则创建账号
        AccountEntity account = accountDao.getAccountByCellphoneOrEmail(dto.getCellphone());
        msg = StringUtil.format(SystemParameters.CREATE_SUB_COMPANY_MSG_2, accountName, pcompanyName,
                dto.getCompanyName());
        if (account == null) {
            //创建账号
            account = this.accountService.createAccount(dto.getUserName(), MD5Helper.getMD5For32("123456"), dto.getCellphone());
            msg = StringUtil.format(SystemParameters.CREATE_SUB_COMPANY_MSG_1, accountName, pcompanyName,
                    dto.getCompanyName(), dto.getCellphone(), "123456", serverUrl);

        } else if (!"0".equals(account.getStatus())) {//存在更新，设置为有效，
            account.setStatus("0");
            account.setActiveTime(DateUtils.date2Str(DateUtils.datetimeFormat));
            accountDao.updateById(account);
            //短信内容
        }
        //第三步，创建团队（分支机构首先是个团队（公司））
        CompanyEntity company = new CompanyEntity();
        BaseDTO.copyFields(dto, company);
        //保存公司信息
        String id = this.saveCompany(company, account.getUserName(), account.getId(), dto.getAccountId());
        //第四步，把当前的公司和创建的分支机构，挂上分支机构的关系
        String subOrgId = this.createCompanyRelation(dto.getAccountId(), company.getId(), dto.getCompanyId(), dto.getType() + "", dto.getRoleType());
        if (dto.getType() == 2) {//邀请分公司
            SubCompanyEntity subCompany = new SubCompanyEntity();
            subCompany.setId(subOrgId);//此id设置和org的一样
            subCompany.setNickName(null);
            subCompany.setCreateBy(dto.getAccountId());
            subCompanyDao.insert(subCompany);
        } else if (dto.getType() == 3) {//邀请事业合伙人
            BusinessPartnerEntity businessPartner = new BusinessPartnerEntity();
            businessPartner.setId(subOrgId);//此id设置和org的一样
            businessPartner.setNickName(null);
            businessPartner.setCreateBy(dto.getAccountId());
            businessPartner.setCompanyId(company.getId());
            businessPartner.setType(3);
            businessPartnerDao.insert(businessPartner);
        }
        //第五步，发送短信通知负责人
        this.sendMsg(dto.getCellphone(), msg);
        //第六步，返回信息
        dto.setId(id);
        OrgTreeDTO tree = new OrgTreeDTO();
        tree.setRealId(id);
        tree.setId(id);
        tree.setCompanyId(id);
        tree.setText(dto.getCompanyShortName());
        if (dto.getType() == 2) {
            tree.setType("subCompany");
        } else {
            tree.setType("partner");
            tree.setIsCurrentSubCompany(1);
        }
        tree.setTreeEntity(company);
        tree.setRelationType(dto.getRoleType());
        return new AjaxMessage().setCode("0").setInfo("保存成功").setData(tree);
    }


    /**
     * 方法描述：创建公司直接的关联
     * 作者：MaoSF
     * 日期：2016/11/29
     *
     * @param: orgType:2分支机构，3合作伙伴
     */
    private String createCompanyRelation(String createBy, String orgId, String orgPid, String orgType, String partnerTypeId) throws Exception {
        OrgEntity subOrg = new OrgEntity();
        String subOrgId = StringUtil.buildUUID();
        subOrg.setId(subOrgId);
        subOrg.setOrgStatus("0");
        subOrg.setOrgType(orgType);//分支机构
        subOrg.setCreateBy(createBy);
        orgDao.insert(subOrg);

        CompanyRelationEntity relationEntity = new CompanyRelationEntity();
        relationEntity.setId(subOrgId);//此id设置和org的一样
        relationEntity.setOrgPid(orgPid);
        relationEntity.setOrgId(orgId);
        relationEntity.setCreateBy(createBy);
        relationEntity.setTypeId(partnerTypeId);
        companyRelationDao.insert(relationEntity);

        //审核表中也保存一份数据
        CompanyRelationAuditEntity relationAuditEntity = new CompanyRelationAuditEntity();
        relationAuditEntity.setId(subOrgId);//此id设置和org的一样
        relationAuditEntity.setOrgPid(orgPid);
        relationAuditEntity.setOrgId(orgId);
        relationAuditEntity.setCreateBy(createBy);
        relationAuditEntity.setAuditStatus("0");//已通过状态
        relationAuditEntity.setType(orgType);
        companyRelationAuditDao.insert(relationAuditEntity);

        return subOrgId;
    }

    private String createCompanyRelation(String createBy, String orgId, String orgPid, String orgType) throws Exception {
        return createCompanyRelation(createBy, orgId, orgPid, orgType, null);
    }


    /**
     * 方法描述：修改分支机构
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     */
    @Override
    public AjaxMessage updateSubCompany(SubCompanyDTO dto) throws Exception {
        //简称=全称
        dto.setCompanyShortName(dto.getCompanyName());
        CompanyDTO companyDTO = new CompanyDTO();
        BaseDTO.copyFields(dto, companyDTO);
        AjaxMessage ajaxMessage = this.validateSaveOrUpdateCompany(companyDTO);

        if (!"0".equals(ajaxMessage.getCode())) {
            return ajaxMessage;
        }

        CompanyEntity companyEntity = companyDao.selectById(dto.getId());
        BaseDTO.copyFields(dto, companyEntity);
        companyDao.updateById(companyEntity);

        //修改群组
        this.updateImGroup(companyEntity.getId(), companyEntity.getCompanyShortName());

        //返回数据
        OrgTreeDTO tree = new OrgTreeDTO();
        tree.setRealId(dto.getId());
        tree.setId(dto.getId());
        tree.setCompanyId(dto.getId());
        tree.setText(dto.getCompanyShortName());
        tree.setTreeEntity(companyEntity);
        tree.setType("subCompany");
        return new AjaxMessage().setCode("0").setInfo("保存成功").setData(tree);
    }


    /**
     * 方法描述：删除分支机构
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     */
    @Override
    public AjaxMessage deleteSubCompany(String orgPid, String orgId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orgPid", orgPid);
        map.put("orgId", orgId);
        List<CompanyRelationDTO> list = companyRelationDao.getCompanyRelationByParam(map);
        CompanyRelationDTO dto = null;
        if (list != null && list.size() == 1) {
            dto = list.get(0);
            companyRelationDao.deleteById(dto.getId());
            companyRelationAuditDao.deleteById(dto.getId());
            subCompanyDao.deleteById(dto.getId());
            orgDao.deleteById(dto.getId());
            return new AjaxMessage().setCode("0").setInfo("删除成功");
        }
        return new AjaxMessage().setCode("1").setInfo("删除失败");
    }


    /**
     * 方法描述：添加分支机构校验方法
     * 作者：MaoSF
     * 日期：2016/11/29
     */
    public AjaxMessage validatecreateBusinessPartner(BusinessPartnerDTO dto) throws Exception {

        if (StringUtil.isNullOrEmpty(dto.getCompanyName())) {
            return new AjaxMessage().setCode("1").setInfo("团队名称不能为空");
        }

        if (StringUtil.isNullOrEmpty(dto.getUserId())) {
            if (StringUtil.isNullOrEmpty(dto.getCellphone())) {
                return new AjaxMessage().setCode("1").setInfo(" 负责人手机号码不能为空");
            }
            if (StringUtil.isNullOrEmpty(dto.getUserName())) {
                return new AjaxMessage().setCode("1").setInfo(" 负责人不能为空");
            }
            if (StringUtil.isNullOrEmpty(dto.getAdminPassword())) {
                return new AjaxMessage().setCode("1").setInfo(" 密码不能为空");
            }
        }
        return new AjaxMessage().setCode("0");
    }

    /**
     * 方法描述：增加合伙人
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     */
    @Override
    public AjaxMessage createBusinessPartner(BusinessPartnerDTO dto) throws Exception {
        //简称=全称
        dto.setCompanyShortName(dto.getCompanyName());
        AjaxMessage ajax = validatecreateBusinessPartner(dto);
        if ("1".equals(ajax.getCode())) {//如果信息有误，直接返回
            return ajax;
        }

        CompanyInviteEntity companyInviteEntity = this.companyInviteDao.selectById(dto.getInviteId());
        if (companyInviteEntity == null) {
            return AjaxMessage.failed("申请失败");
        }

        //第一步.判断手机号码是否存在，如果不存在，则创建账号
        AccountEntity account = accountDao.getAccountByCellphoneOrEmail(dto.getCellphone());

        if (account == null) {
            account = this.accountService.createAccount(dto.getUserName(), MD5Helper.getMD5For32(dto.getAdminPassword()), dto.getCellphone());
        } else if (!"0".equals(account.getStatus())) {//存在更新，设置为有效，
            account.setStatus("0");
            account.setActiveTime(DateUtils.date2Str(DateUtils.datetimeFormat));
            accountDao.updateById(account);
        }

        //第二步，创建团队（分支机构首先是个团队（公司））
        CompanyEntity company = new CompanyEntity();
        BaseDTO.copyFields(dto, company);
        String id = this.saveCompany(company, account.getUserName(), account.getId(), dto.getAccountId());

        //第三步，把当前的公司和创建的合作伙伴，挂上合作伙伴的关系
        String subOrgId = null;
        if ("1".equals(companyInviteEntity.getType())) {//邀请分公司
            subOrgId = this.createCompanyRelation(dto.getAccountId(), company.getId(), companyInviteEntity.getCompanyId(), CompanyType.SUB_COMPANY_TYPE);
            SubCompanyEntity subCompany = new SubCompanyEntity();
            subCompany.setId(subOrgId);//此id设置和org的一样
            subCompany.setNickName(null);
            subCompany.setCreateBy(dto.getAccountId());
            subCompanyDao.insert(subCompany);
        } else if ("2".equals(companyInviteEntity.getType())) {//邀请事业合伙人
            subOrgId = this.createCompanyRelation(dto.getAccountId(), company.getId(), companyInviteEntity.getCompanyId(), CompanyType.PARTNER_COMPANY_TYPE);
            BusinessPartnerEntity businessPartner = new BusinessPartnerEntity();
            businessPartner.setId(subOrgId);//此id设置和org的一样
            businessPartner.setNickName(null);
            businessPartner.setCreateBy(dto.getAccountId());
            businessPartner.setCompanyId(company.getId());
            businessPartner.setType(3);
            businessPartnerDao.insert(businessPartner);
        } else {//外部合作伙伴产生生效数据
            PartnerEntity partnerEntity = new PartnerEntity();
            partnerEntity.setCompanyId(id);
            partnerEntity.setId(companyInviteEntity.getId());
            organizationDao.updateById(partnerEntity);
        }

        //第四步，删除邀请信息
        this.companyInviteDao.deleteById(dto.getInviteId());

        return new AjaxMessage().setCode("0").setInfo("保存成功");
    }

    /**
     * 方法描述：删除事业合伙人
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     */
    @Override
    public AjaxMessage deleteBusinessPartner(String orgPid, String orgId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orgPid", orgPid);
        map.put("orgId", orgId);
        List<CompanyRelationDTO> list = companyRelationDao.getCompanyRelationByParam(map);
        CompanyRelationDTO dto = null;
        if (list != null && list.size() == 1) {
            dto = list.get(0);
            companyRelationDao.deleteById(dto.getId());
            companyRelationAuditDao.deleteById(dto.getId());
            businessPartnerDao.deleteById(dto.getId());
            orgDao.deleteById(dto.getId());
            return new AjaxMessage().setCode("0").setInfo("删除成功");
        }
        return new AjaxMessage().setCode("1").setInfo("删除失败");
    }

    /**
     * 方法描述：修改合伙人
     * 作        者：MaoSF
     * 日        期：2016年7月9日-上午11:15:52
     */
    @Override
    public AjaxMessage updateBusinessPartner(BusinessPartnerDTO dto) throws Exception {
        //简称=全称
        dto.setCompanyShortName(dto.getCompanyName());
        CompanyDTO companyDTO = new CompanyDTO();
        BaseDTO.copyFields(dto, companyDTO);
        AjaxMessage ajaxMessage = this.validateSaveOrUpdateCompany(companyDTO);
        if (!"0".equals(ajaxMessage.getCode())) {
            return ajaxMessage;
        }

        CompanyEntity companyEntity = companyDao.selectById(dto.getId());
        BaseDTO.copyFields(dto, companyEntity);
        companyDao.updateById(companyEntity);

        //修改群组
        this.updateImGroup(companyEntity.getId(), companyEntity.getCompanyShortName());

        //返回数据
        OrgTreeDTO tree = new OrgTreeDTO();
        tree.setRealId(dto.getId());
        tree.setId(dto.getId());
        tree.setCompanyId(dto.getId());
        tree.setText(dto.getCompanyShortName());
        tree.setTreeEntity(companyEntity);
        tree.setType("partner");
        return new AjaxMessage().setCode("0").setInfo("保存成功").setData(tree);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public CompanyDTO getCompanyById(String id) throws Exception {

        CompanyEntity company = this.selectById(id);
        if (company == null) {
            return null;
        }
        CompanyDTO dto = new CompanyDTO();
        BaseDTO.copyFields(company, dto);

        //查询服务类型
        if (!StringUtil.isNullOrEmpty(company.getServerType())) {
            Map map = new HashMap();
            map.put("idList", company.getServerType().split(","));
            List<DataDictionaryEntity> list = dataDictionaryDao.getDataByParemeter(map);
            for (DataDictionaryEntity d : list) {
                map = new HashMap();
                map.put("id", d.getId());
                map.put("name", d.getName());
                dto.getServerTypeList().add(map);
            }
        }

        //获取公司logo
        Map<String, Object> param = new HashMap<>();
        param.put("companyId", id);
        param.put("type", NetFileType.COMPANY_LOGO_ATTACH);
        param.put("status", "0");
        List<ProjectSkyDriveEntity> list = projectSkyDriverDao.getNetFileByParam(param);
        if (!list.isEmpty()) {
            dto.setFilePath(list.get(0).getFileGroup() + "/" + list.get(0).getFilePath());
        }

        //查询公司二维码
        param.put("type", NetFileType.COMPANY_QR_CODE_ATTACH);
        param.put("status", "0");
        List<ProjectSkyDriveEntity> list2 = projectSkyDriverDao.getNetFileByParam(param);
        if (!list2.isEmpty()) {
            dto.setQrcodePath(list2.get(0).getFilePath());
        } else {//若无，则生成二维码
            dto.setQrcodePath(this.projectSkyDriverService.createCompanyQrcode(id));
        }

        return dto;
    }

    @Override
    public List<CompanyDTO> getCompanyByUserId(String userId) {
        return companyDao.getCompanyByUserId(userId);
    }

    private OrgTreeDTO entityToTree(CompanyEntity c,String companyId,String selectId){
        //查询别名
        String nickName = this.businessPartnerDao.getNickName(c.getId());
        OrgTreeDTO tree = new OrgTreeDTO();
        tree.setText(c.getCompanyShortName());
        if (!StringUtil.isNullOrEmpty(nickName)) {
            tree.setText(nickName);
        }
        tree.setRelationType(c.getRelationType());
        tree.setType("company");
        tree.setId(c.getId());
        tree.setCompanyId(c.getId());//当前节点是公司，companyId=id
        tree.setRealId(c.getId());
        tree.setTreeEntity(c);
        if (CompanyType.SUB_COMPANY_TYPE.equals(c.getCompanyType())) {//如果是分支机构
            tree.setType("subCompany");
        }
        if (CompanyType.PARTNER_COMPANY_TYPE.equals(c.getCompanyType())) {//如果是分支机构
            tree.setType("partner");
        }
        //如果是当前公司，则展示
        if ((companyId!=null && c.getId() != null && c.getId().equals(companyId)) || (selectId != null && c.getId().equals(selectId))) {
            Map<String, Object> tmapMap = new HashMap<String, Object>();
            tmapMap.put("opened", true);
            tmapMap.put("selected", true);
            tree.setState(tmapMap);
        }
        return tree;
    }

    private OrgTreeDTO departToTree(DepartEntity d,String selectId){
        OrgTreeDTO tree = new OrgTreeDTO();
        tree.setText(d.getDepartName());
        tree.setId(d.getId());
        tree.setCompanyId(d.getCompanyId());//当前节点是部门，companyId为当前节点的companyId
        tree.setRealId(d.getId());
        tree.setType("depart");
        tree.setTreeEntity(d);
        if (selectId != null && d.getId().equals(selectId)) {
            Map<String, Object> tmapMap = new HashMap<>();
            tmapMap.put("opened", true);
            tmapMap.put("selected", true);
            tree.setState(tmapMap);
        }
        return tree;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OrgTreeDTO getOrgTree(Map<String, Object> map) throws Exception {
        String companyId = (String) map.get("companyId");
        String rootId = companyId;

        //把每个节点都保存到nodeMap中
        Map<String, OrgTreeDTO> nodeMap = new HashMap<String, OrgTreeDTO>();

        //所有的公司节点
        List<CompanyEntity> companyNodes = new ArrayList<CompanyEntity>();
        List<Map<String, Object>> companyEdges = new ArrayList<Map<String, Object>>();
        String selectId = (String) map.get("selectId");//被选择的节点id

        //type不为空则查询当前公司组织树
        if (map.get("type") != null && "1".equals(map.get("type"))) {
            CompanyEntity company = companyDao.selectById(companyId);
            if (company != null) {
                companyNodes.add(company);
            }
        } else {
            if (map.get("type") != null) {
                rootId = companyId;//查询当前组织下面的所有子公司，合作伙伴
            } else {
                //先获取根节点
                rootId = getRootCompanyId(companyId);
            }
            //查询根节点
            if (null != this.selectById(rootId)) {
                companyNodes.add(this.selectById(rootId));
            }
            //查询子公司
            if (null != this.getAllChildrenCompany(rootId)) {
                companyNodes.addAll(this.getAllChildrenCompany(rootId));
            }
            List<String> companyIds = new ArrayList<String>();
            for (CompanyEntity c : companyNodes) {
                companyIds.add(c.getId());
            }
            map.clear();
            if (null != companyIds && 0 < companyIds.size()) {
                map.put("companyIds", companyIds);
            }
            companyEdges = companyDao.selectAllCompanyEdges(map);
        }

        List<String> companyIds = new ArrayList<String>();
        for (CompanyEntity c : companyNodes) {
            companyIds.add(c.getId());
            OrgTreeDTO tree = entityToTree(c,companyId,selectId);
            nodeMap.put(c.getId(), tree);
        }

        //把部门 添加到组织树上
        this.getDepartToTree(companyIds,selectId,nodeMap);
        //把分支机构和合作伙伴加上
        this.getSubCompanyAndPartnerToTree(companyEdges,selectId,nodeMap,companyId);
        //返回树
        return nodeMap.get(rootId);
    }


    private void getDepartToTree(List<String> companyIds,String selectId,Map<String, OrgTreeDTO> nodeMap){
        Map<String, Object> param = new HashMap<>();
        if (!CollectionUtils.isEmpty(companyIds)) {
            param.put("companyIds", companyIds.toArray());
            List<DepartEntity> departNodes = departDao.selectDepartNodesByCompanyIds(param);
            for (DepartEntity d : departNodes) {
                OrgTreeDTO tree = this.departToTree(d,selectId);
                nodeMap.put(d.getId(), tree);
            }

            List<Map<String, Object>> departEdges = departDao.selectDepartEdgesByCompanyIds(param);
            for (Map<String, Object> e : departEdges) {
                String from = (String) e.get("from");
                String to = (String) e.get("to");
                String too = (String) e.get("too");

                OrgTreeDTO fromNode = nodeMap.get(from);//部门id
                OrgTreeDTO toNode = nodeMap.get(to);//部门的公司
                OrgTreeDTO tooNode = nodeMap.get(too);//部门pid

                if (tooNode != null) {
                    //部门上级为公司ID
                    if (too.equals(to)) {
                        if (toNode != null) {
                            toNode.getChildren().add(fromNode);
                        }
                        //部门上级为部门
                    } else {
                        tooNode.getChildren().add(fromNode);
                    }
                }
            }
        }
    }

    private void getSubCompanyAndPartnerToTree(List<Map<String, Object>> companyEdges,String selectId,Map<String, OrgTreeDTO> nodeMap, String companyId){
        getSubCompanyAndPartnerToTree(companyEdges,selectId,nodeMap,companyId,false);
    }

    /**
     * 把分公司和事业合伙人添加到组织树上
     */
    private void getSubCompanyAndPartnerToTree(List<Map<String, Object>> companyEdges,String selectId,Map<String, OrgTreeDTO> nodeMap, String companyId,boolean isDisabled){
        for (Map<String, Object> e : companyEdges) {
            String from = (String) e.get("from");
            String to = (String) e.get("to");
            String type = (String) e.get("type");
            OrgTreeDTO toNode = nodeMap.get(to);
            if (toNode != null) {
                //分支机构关系
                if (CompanyType.SUB_COMPANY_TYPE.equals(type)) {
                    if (toNode.getChildById(to + "subCompanyId") == null) {
                        OrgTreeDTO subCompanyTree = new OrgTreeDTO();
                        subCompanyTree.setText("分支机构");
                        subCompanyTree.setType("subCompanyContainer");
                        subCompanyTree.setId(toNode.getId() + "subCompanyId");
                        subCompanyTree.setRealId(toNode.getId() + "subCompanyId");
                        if(isDisabled){
                            subCompanyTree.getState().put("disabled",true);
                        }
                   //
                        toNode.getChildren().add(subCompanyTree);
                        nodeMap.put(toNode.getId() + "subCompanyId", subCompanyTree);
                    }
                    if (companyId!=null && to.equals(companyId)) {
                        nodeMap.get(from).setIsCurrentSubCompany(1);
                    }
                    toNode.getChildById(to + "subCompanyId").getChildren().add(nodeMap.get(from));
                    //合作伙伴关系
                } else if (CompanyType.PARTNER_COMPANY_TYPE.equals(type)) {
                    if (toNode.getChildById(to + "partnerId") == null) {
                        //事业合伙人的节点
                        OrgTreeDTO partnerTree = new OrgTreeDTO();
                        partnerTree = new OrgTreeDTO();
                        partnerTree.setText("事业合伙人");
                        partnerTree.setType("partnerContainer");
                        partnerTree.setId(toNode.getId() + "partnerId");
                        partnerTree.setRealId(toNode.getId() + "partnerId");
                        if(isDisabled){
                            partnerTree.getState().put("disabled",true);
                        }
                        nodeMap.put(toNode.getId() + "partnerId", partnerTree);
                        toNode.getChildren().add(partnerTree);
                    }
                    if (companyId!=null && to.equals(companyId)) {
                        nodeMap.get(from).setIsCurrentSubCompany(1);
                    }
                    toNode.getChildById(to + "partnerId").getChildren().add(nodeMap.get(from));
                }
            }
        }
    }

    /**
     * 方法描述：获取单层组织架构树信息
     * 作        者：Zhangchengliang
     * 日        期：2017/4/14
     */
    @Override
    public OrgTreeDTO getOrgTreeSimple(Map<String, Object> map) throws Exception {
        return getOrgTreeBase(map);
    }

    private OrgTreeDTO getOrgTreeBase(Map<String, Object> map) throws Exception {
        String notIncludeDepart = (String)map.get("notIncludeDepart");
        boolean isDisabled = false;
        if(map.containsKey("isDisabled")){
            isDisabled  = (boolean)map.get("isDisabled");
        }

        String companyId = (String) map.get("companyId");
        String rootId = companyId;
        //把每个节点都保存到nodeMap中
        Map<String, OrgTreeDTO> nodeMap = new HashMap<>();
        //所有的公司节点
        List<CompanyEntity> companyNodes = new ArrayList<CompanyEntity>();
        List<Map<String, Object>> companyEdges = new ArrayList<Map<String, Object>>();
        String selectId = (String) map.get("selectId");//被选择的节点id

        CompanyEntity company = companyDao.selectById(companyId);
        if (company == null) {
            return null;
        }
        companyNodes.add(company);
        List<CompanyEntity> subcompanies = companyDao.getChildrenCompany(companyId);
        if (subcompanies != null) {
            companyNodes.addAll(subcompanies);
        }
        List<String> companyIds = new ArrayList<>();
        for (CompanyEntity c : companyNodes) {
            companyIds.add(c.getId());
            OrgTreeDTO tree = this.entityToTree(c,companyId,selectId);
            nodeMap.put(c.getId(), tree);
        }

        //把部门 添加到组织树上
        if(StringUtil.isNullOrEmpty(notIncludeDepart)){
            this.getDepartToTree(companyIds,selectId,nodeMap);
        }

        //把分支机构和合作伙伴加上
        Map<String, Object> map_edges = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add(companyId);
        map_edges.put("companyIds", list);
        companyEdges = companyDao.selectAllCompanyEdges(map_edges);

        this.getSubCompanyAndPartnerToTree(companyEdges,selectId,nodeMap,companyId,isDisabled);
        //判断是否总公司，是否包含子公司/合伙人的子公司/合伙人，并设置id返回，id为1代表是总公司并有子公司的子公司，id为null代表没有
        if (companyId.equals(getRootCompanyId(companyId))) { //是总公司
            Map<String, Object> map_query = new HashMap<>();
            List<Map<String, Object>> cc = null;
            companyIds.remove(company.getId());
            if (!CollectionUtils.isEmpty(companyIds)) {
                map_query.put("companyIds", companyIds);
                cc = companyDao.selectAllCompanyEdges(map_query);
            }

            if ((cc == null) || (cc.size() == 0)) {
                OrgTreeDTO t = nodeMap.get(rootId);
                t.setPid((t.getPid() == null) ? "!" : t.getPid() + "!");
            }
        }

        return nodeMap.get(rootId);
    }

    /**
     * 方法描述：获取组织架构树信息（消息通告选择发送组织  使用）
     * 作        者：MaoSF
     * 日        期：2016年7月9日-下午6:42:43
     */
    @Override
    public OrgTreeDTO getOrgTreeForNotice(Map<String, Object> map) throws Exception {
        String companyId = (String) map.get("companyId");
        String rootId = companyId;
        Map<String, OrgTreeDTO> nodeMap = this.getTreeMap(map);
        //重新组合一下数据
        OrgTreeDTO tree = new OrgTreeDTO();
        tree.setText(nodeMap.get(rootId).getText());
        tree.setId("root");
        tree.setCompanyId("root");//当前节点是部门，companyId为当前节点的companyId
        tree.setRealId("root");
        tree.setType("company");

        nodeMap.get(rootId).setText(nodeMap.get(rootId).getText() + "本部");
        tree.getChildren().add(nodeMap.get(rootId));
        if (nodeMap.get(rootId).getChildById(rootId + "subCompanyId") != null) {
            tree.getChildren().add(nodeMap.get(rootId).getChildById(rootId + "subCompanyId"));
        }
        if (nodeMap.get(rootId).getChildById(rootId + "partnerId") != null) {
            tree.getChildren().add(nodeMap.get(rootId).getChildById(rootId + "partnerId"));
        }
        nodeMap.get(rootId).getChildren().remove(nodeMap.get(rootId).getChildById(rootId + "subCompanyId"));
        nodeMap.get(rootId).getChildren().remove(nodeMap.get(rootId).getChildById(rootId + "partnerId"));
        return tree;
    }

    private Map<String, OrgTreeDTO> getTreeMap(Map<String,Object> map) {
        String notIncludeDepart = (String)map.get("notIncludeDepart");
        boolean isDisabled = map.get("isDisabled")==null?false:true;
        String companyId = (String) map.get("companyId");
        String rootId = companyId;
        //把每个节点都保存到nodeMap中
        Map<String, OrgTreeDTO> nodeMap = new HashMap<>();
        //所有的公司节点
        List<CompanyEntity> companyNodes = new ArrayList<>();
        List<Map<String, Object>> companyEdges = new ArrayList<>();
        String selectId = (String) map.get("selectId");//被选择的节点id
        //查询根节点
        companyNodes.add(this.selectById(rootId));
        //查询子公司
        companyNodes.addAll(this.getAllChildrenCompany(rootId));
        List<String> companyIds2 = new ArrayList<String>();
        for (CompanyEntity c : companyNodes) {
            companyIds2.add(c.getId());
        }
        map.clear();
        map.put("companyIds", companyIds2);
        companyEdges = companyDao.selectAllCompanyEdges(map);
        for (CompanyEntity c : companyNodes) {
            //查询别名
            OrgTreeDTO tree = this.entityToTree(c,companyId,selectId);
            //如果是当前公司，则展示
            if ((c.getId() != null && c.getId().equals(companyId)) || (selectId != null && c.getId().equals(selectId))) {
                Map<String, Object> tmapMap = new HashMap<String, Object>();
                tmapMap.put("opened", true);
                tree.setState(tmapMap);
            }
            nodeMap.put(c.getId(), tree);
        }

        if(StringUtil.isNullOrEmpty(notIncludeDepart)){
            //只查询自己的部门
            List<String> companyIds = new ArrayList<String>();
            companyIds.add(companyId);
            this.getDepartToTree(companyIds,selectId,nodeMap);
        }

        //把分支机构和合作伙伴加上
        this.getSubCompanyAndPartnerToTree(companyEdges,selectId,nodeMap,companyId,isDisabled);
        return nodeMap;
    }

    @Override
    public List<CompanyDTO> getCompanyFilterbyParam(Map<String, Object> param) throws Exception {
        if (null != param.get("pageNumber")) {
            int page = (Integer) param.get("pageNumber");
            int pageSize = (Integer) param.get("pageSize");
            param.put("startPage", page * pageSize);
            param.put("endPage", pageSize);
        }
        return companyDao.getCompanyFilterbyParam(param);
    }

    @Override
    public int getCompanyFilterbyParamCount(Map<String, Object> map) throws Exception {
        return companyDao.getCompanyFilterbyParamCount(map);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Map getCompanyFilterbyParamPage(Map<String, Object> map)
            throws Exception {
        if (null != map.get("pageNumber")) {
            int page = (Integer) map.get("pageNumber");
            int pageSize = (Integer) map.get("pageSize");
            map.put("startPage", page * pageSize);
            map.put("endPage", pageSize);
        }

        Map returnMap = new HashMap();
        returnMap.put("data", this.getCompanyFilterbyParam(map));
        returnMap.put("total", this.getCompanyFilterbyParamCount(map));
        return returnMap;
    }


    @Override
    public AjaxMessage disbandCompany(String companyId) throws Exception {
        //查询团队所有的一级部门，对一级部门群组进行删除
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        //查询部门，如果是一级部门，则删除群组成员
        List<DepartEntity> departEntityList = departDao.selectDepartByParam(params);
        for (DepartEntity departEntity : departEntityList) {
            if ("1".equals(departEntity.getDepartLevel())) {
                //删除部门群组
                this.removeImGroup(departEntity.getId(), ImGroupType.DEPARTMENT);
            }
        }
        // 修改公司的状态：为不可用状态
        CompanyEntity entity = new CompanyEntity();
        entity.setId(companyId);
        entity.setStatus("1");
        int i = companyDao.updateById(entity);
        //删除与之有关的组织关系
        companyRelationDao.deleteCompanyRelationByOrgId(companyId);
        companyRelationAuditDao.deleteCompanyRelationByOrgId(companyId);
        //修改员工的默认企业
        accountDao.updatedeAllfaultCompanyId(companyId);
        //删除公司群组
        this.removeImGroup(companyId, ImGroupType.COMPANY);

        if (i == 1) {
            return new AjaxMessage().setCode("0").setInfo("解散当前组织成功");
        }
        return new AjaxMessage().setCode("1").setInfo("解散当前组织失败");
    }


    @Override
    public List<CompanyEntity> getAllParentCompanyList(String id) throws Exception {
        List<CompanyEntity> list = new ArrayList<>();
        CompanyEntity companyEntity = companyDao.getParentCompany(id);
        if (companyEntity == null) {
            return list;
        } else {
            list.add(companyEntity);
            getAllParentCompanyList(companyEntity.getId());
        }

        return list;
    }

    @Override
    public List<CompanyEntity> getAllChildrenCompany(String id) {
        List<CompanyEntity> list = new ArrayList<>();
        getChildrenCompany2(id, list);
        return list;
    }

    @Override
    public List<CompanyEntity> getAllOrg(String id) throws Exception {
        String rootId = this.getRootCompanyId(id);
        List<CompanyEntity> list = this.getAllChildrenCompany(rootId);
        list.add(0, this.companyDao.selectById(rootId));
        return list;
    }


    /**
     * 获取某节点公司的根节点id
     */
    public String getRootCompanyId(String id) {
        CompanyEntity companyEntity = companyDao.getParentCompany(id);
        if (companyEntity == null) {
            return id;
        } else {
            return getRootCompanyId(companyEntity.getId());
        }
    }

    /**
     * 方法描述：查询线上公司
     * 作者：MaoSF
     * 日期：2016/11/11
     */
    public CompanyDTO getLineCompanyByCompanyId(String id) throws Exception {
        CompanyEntity companyEntity = companyDao.getParentCompany(id);
        if (null != companyEntity) {
            CompanyDTO companyDTO = new CompanyDTO();
            BaseDTO.copyFields(companyEntity, companyDTO);
            return companyDTO;
        }
        return null;
    }


    /**
     * 方法描述：发送短信【此方法不是接口】
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午8:05:44
     */
    public void sendMsg(String cellphone, String msg) {
        Sms sms = new Sms();
        sms.addMobile(cellphone);
        sms.setMsg(msg);
        smsSender.send(sms);
    }

    /**
     * 方法描述：获取常用的合作伙伴
     * 作者：MaoSF
     * 日期：2016/8/26
     */
    @Override
    public List<CompanyDTO> getUsedCooperationPartners(String companyId) throws Exception {
        String root = this.getRootCompanyId(companyId);
        List<CompanyEntity> childrenCompany = new ArrayList<CompanyEntity>();
        CompanyEntity rootCompany = this.selectById(root);
        childrenCompany.add(rootCompany);
        childrenCompany.addAll(this.getAllChildrenCompany(root));
        if (!CollectionUtils.isEmpty(childrenCompany)) {
            for (CompanyEntity c : childrenCompany) {
                if (companyId.equals(c.getId())) {
                    childrenCompany.remove(c);
                    break;
                }
            }
            return BaseDTO.copyFields(childrenCompany, CompanyDTO.class);
        }
        return new ArrayList<CompanyDTO>();
    }

    @Override
    public List<CompanyEntity> getAllCompany() throws Exception {
        return companyDao.selectAll();
    }

    @Override
    public List<CompanyEntity> getAllCompanyIm() throws Exception {
        return companyDao.selectAllIm();
    }

    /**
     * 方法描述：添加权限，系统管理员角色（初始话云端数据）
     * 作者：MaoSF
     * 日期：2016/11/7
     */
    @Override
    public AjaxMessage initCompanyRole() throws Exception {
        //1.初始化有公司的默认角色的权限
        //1.1查询所有公司
        List<CompanyEntity> companyList = companyDao.selectAll();
        //1.2查询所有默认角色
        List<RolePermissionEntity> initData = this.rolePermissionDao.getAllDefaultPermission();
        //1.3依次初始化公司默认角色
        if (!CollectionUtils.isEmpty(companyList)) {
            for (CompanyEntity entity : companyList) {
                if (!CollectionUtils.isEmpty(initData)) {
                    this.rolePermissionDao.deleteByCompanyId(entity.getId());
                    for (RolePermissionEntity rolePermission : initData) {
                        rolePermission.setId(StringUtil.buildUUID());
                        rolePermission.setCompanyId(entity.getId());
                        this.rolePermissionDao.insert(rolePermission);
                    }
                }
            }
        }

        //2.初始话所有管理员的角色（添加系统管理员角色）
        List<TeamOperaterEntity> teamOperaterList = teamOperaterDao.getAllTeamOperater();
        if (!CollectionUtils.isEmpty(teamOperaterList)) {
            for (TeamOperaterEntity teamOperater : teamOperaterList) {
                //删除当前人的系统管理员角色
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("roleId", SystemParameters.ADMIN_MANAGER_ROLE_ID);
                param.put("userId", teamOperater.getUserId());
                param.put("orgId", teamOperater.getCompanyId());
                param.put("companyId", teamOperater.getCompanyId());
                this.roleUserDao.deleteUserRole(param);
                //添加系统管理员角色
                RoleUserEntity roleUser = new RoleUserEntity();
                roleUser.setId(StringUtil.buildUUID());
                roleUser.setRoleId(SystemParameters.ADMIN_MANAGER_ROLE_ID);
                roleUser.setCompanyId(teamOperater.getCompanyId());
                roleUser.setUserId(teamOperater.getUserId());
                roleUser.setOrgId(teamOperater.getCompanyId());
                roleUserDao.insert(roleUser);
            }
        }
        return null;
    }

    /**
     * 方法描述：系统管理员权限（初始话云端数据）2016-11-11
     * 作者：MaoSF
     * 日期：2016/11/11
     */
    @Override
    public AjaxMessage initRolePermission() throws Exception {
        //2.初始话所有管理员的角色（添加系统管理员角色）
        List<TeamOperaterEntity> teamOperaterList = teamOperaterDao.getAllTeamOperater();
        if (!CollectionUtils.isEmpty(teamOperaterList)) {
            for (TeamOperaterEntity teamOperater : teamOperaterList) {
                //添加系统管理员角色
                SaveRoleUserDTO saveRoleUserDTO = new SaveRoleUserDTO();
                saveRoleUserDTO.setRoleId(SystemParameters.ADMIN_MANAGER_ROLE_ID);
                List<String> users = new ArrayList<String>();
                users.add(teamOperater.getUserId());
                saveRoleUserDTO.setUserIds(users);
                saveRoleUserDTO.setCurrentCompanyId(teamOperater.getCompanyId());
                this.roleUserService.saveOrUserRole(saveRoleUserDTO);
            }
        }
        return null;
    }


    /**
     * 方法描述：创建群组
     * 作者：MaoSF
     * 日期：2016/11/29
     */
    private void createImGroup(String orgId, String admin, String companyShortName) throws Exception {
        this.imService.createImGroup(orgId, admin, companyShortName, ImGroupType.COMPANY);
    }

    /**
     * 方法描述：修改群组信息
     * 作者：MaoSF
     * 日期：2016/11/29
     */
    private void updateImGroup(String orgId, String name) throws Exception {
        this.imService.updateImGroup(orgId, name, ImGroupType.COMPANY);
    }

    /**
     * 方法描述：删除群组信息
     * 作者：MaoSF
     * 日期：2016/11/29
     */
    private void removeImGroup(String orgId, Integer groupType) throws Exception {
        this.imService.deleteImGroup(orgId, groupType);
    }


    //==============================================新接口2.0===============================================================


    /**
     * 递归获取子公司
     */
    public void getChildrenCompany2(String id, List<CompanyEntity> list) {
        List<CompanyEntity> cList = companyDao.getChildrenCompany(id);
        if (cList != null && cList.size() > 0) {
            list.addAll(cList);
            for (int i = 0; i < cList.size(); i++) {
                getChildrenCompany2(cList.get(i).getId(), list);
            }
        }
    }

    /**
     * 方法描述：根据当前id 查找所有子公司和部门
     * 作        者：TangY
     * 日        期：2016年7月15日-下午2:01:41
     */
    @Override
    public List<CompanyDTO> getCompanysAndDepartsByCompanyId(String id) throws Exception {
        List<CompanyEntity> list = new ArrayList<CompanyEntity>();
        getChildrenCompany2(id, list);
        //查询本公司
        CompanyEntity entity = companyDao.selectById(id);
        List<CompanyDTO> companyDTOList = new ArrayList<CompanyDTO>();
        CompanyDTO dto = new CompanyDTO();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("companyId", id);
        //本公司的部门列表
        dto.setDepartList(BaseDTO.copyFields(departDao.getDepartByCompanyId(param), DepartEntity.class));
        BaseDTO.copyFields(entity, dto);
        companyDTOList.addAll(BaseDTO.copyFields(list, CompanyDTO.class));
        companyDTOList.add(0, dto);
        return companyDTOList;
    }

    //=========================================================新接口2.0================================================
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Map getCompanyFilterPage(Map<String, Object> map)
            throws Exception {
        if (null != map.get("pageIndex")) {
            int page = (Integer) map.get("pageIndex");
            int pageSize = (Integer) map.get("pageSize");
            map.put("startPage", page * pageSize);
            map.put("endPage", pageSize);
        }

        Map returnMap = new HashMap();
        returnMap.put("data", this.getCompanyFilterbyParam(map));
        returnMap.put("total", this.getCompanyFilterbyParamCount(map));
        return returnMap;
    }

    @Override
    public List<CompanyEntity> getCompanyListBySearch(String companyName) {
        return companyDao.getCompanyListBySearch(companyName);
    }


    @Override
    public List<CompanyEntity> getCompanyForMyProject(String companyUserId) {
        return companyDao.getCompanyForMyProject(companyUserId);
    }

    /**
     * 方法描述：查询公司的子公司
     * 作        者：MaoSF
     * 日        期：2016年7月15日-下午2:01:41
     */
    public List<CompanyEntity> getChildrenCompany(String id) {
        return companyDao.getChildrenCompany(id);
    }

    @Override
    public List<CompanyDataDTO> listCompanyAndChildren(String id) throws Exception {
        CompanyEntity c = this.companyDao.selectById(id);
        List<CompanyEntity> list = this.getAllChildrenCompany(id);
        list.add(0, c);
        return BaseDTO.copyFields(list, CompanyDataDTO.class);
    }

    @Override
    public List<CompanyRelationDTO> getExpAmountCompanyAndChildren(String id) throws Exception {
        List<CompanyRelationDTO> companyRelationList = new ArrayList<>();
        CompanyRelationDTO relation = this.companyDao.getOrgType(id);
        if(relation==null || CompanyType.ROLE_TYPE_3.equals(relation.getTypeId()) || CompanyType.ROLE_TYPE_2.equals(relation.getTypeId())){//理论上不可能为null
            if(relation!=null){
                companyRelationList.add(relation);
            }
            return companyRelationList;
        }
        companyRelationList.add(relation);
        getExpAmountCompanyAndChildren2(id,companyRelationList);
        return companyRelationList;
    }

    /**
     * 递归获取费用录入子公司
     */
    public void getExpAmountCompanyAndChildren2(String id, List<CompanyRelationDTO> list) {
        List<CompanyRelationDTO> cList = companyDao.getExpAmountCompanyAndChildren(id);
        for(CompanyRelationDTO dto:cList){
            if(!StringUtil.isNullOrEmpty(dto.getTypeId()) && (CompanyType.ROLE_TYPE_3.equals(dto.getTypeId()) || CompanyType.ROLE_TYPE_2.equals(dto.getTypeId()))){
                list.add(dto);
                getExpAmountCompanyAndChildren2(dto.getOrgId(),list);
            }
        }
    }

    @Override
    public OrgTreeDTO getRelationTypeIsThree(String id) throws Exception {
        if(!id.equals(this.getRootCompanyId(id))){
           throw new CustomException("无权限操作");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("companyId",id);
        map.put("notIncludeDepart","1");
        map.put("isDisabled",true);
        return this.getOrgTreeBase(map);
//        List<CompanyRelationDTO> companyRelationList = this.getExpAmountCompanyAndChildren(id);
//        if(CollectionUtils.isEmpty(companyRelationList) ||  CompanyType.ROLE_TYPE_3.equals(companyRelationList.get(0).getTypeId())){
//            return null;
//        }
//        List<Map<String, Object>> companyEdges = new ArrayList<>();
//        List<String> companyIds = new ArrayList<>();
//        //把每个节点都保存到nodeMap中
//        Map<String, OrgTreeDTO> nodeMap = new HashMap<String, OrgTreeDTO>();
//        for(CompanyRelationDTO r:companyRelationList){
//            companyIds.add(r.getOrgId());
//            CompanyEntity c = new CompanyEntity();
//            c.setId(r.getOrgId());
//            c.setCompanyName(r.getCompanyName());
//            c.setCompanyShortName(r.getCompanyName());
//            c.setCompanyType(r.getCompanyType());
//            nodeMap.put(c.getId(), entityToTree(c,id,null));
//
//            if(!r.getOrgId().equals(id)){
//                Map<String, Object> edge = new HashMap<>();
//                edge.put("from",r.getOrgId());
//                edge.put("to",r.getOrgPid());
//                edge.put("type",r.getCompanyType());
//                companyEdges.add(edge);
//            }
//        }
//        if(!CollectionUtils.isEmpty(companyEdges)){
//            this.getSubCompanyAndPartnerToTree(companyEdges,null,nodeMap,id);
//        }
//        return nodeMap.get(id);
    }

    /**
     * 方法描述：返回选择团队列表（分支机构，合作伙伴，所有公司），任务转发给其他公司，团队选择
     * 作者：MaoSF
     * 日期：2017/1/5
     */
    @Override
    public List<CompanyDataDTO> getCompanyForSelect(String id, String projectId) throws Exception {
        List<CompanyEntity> list = companyDao.getChildrenCompany(id);
        List<CompanyDataDTO> allCompanyList = BaseDTO.copyFields(list, CompanyDataDTO.class);

        //查询父公司
        CompanyEntity parentCompany = this.companyDao.getParentCompany(id);
        if (parentCompany != null) {
            CompanyDataDTO parentDataDTO = new CompanyDataDTO();
            BaseDTO.copyFields(parentCompany, parentDataDTO);
            allCompanyList.add(0, parentDataDTO);

            String rootId = this.getRootCompanyId(id);
            if (!id.equals(rootId) && !parentCompany.getId().equals(rootId)) {
                CompanyEntity rootCompany = this.companyDao.selectById(rootId);
                CompanyDataDTO rootDTO = new CompanyDataDTO();
                BaseDTO.copyFields(rootCompany, rootDTO);
                allCompanyList.add(1, rootDTO);
            }

            //查询父级的子节点
            list = companyDao.getChildrenCompany(parentCompany.getId());
            List<CompanyDataDTO> allCompany = BaseDTO.copyFields(list, CompanyDataDTO.class);
            if (!CollectionUtils.isEmpty(allCompany)) {
                for (CompanyDataDTO dataDTO : allCompany) {
                    if (dataDTO.getId().equals(id)) {
                        allCompany.remove(dataDTO);
                        break;
                    }
                }
            }
            allCompanyList.addAll(allCompany);
        }

        //查询自己及父公司以及rootId的公司
        CompanyEntity companyEntity = this.companyDao.selectById(id);
        CompanyDataDTO ownCompany = new CompanyDataDTO();
        BaseDTO.copyFields(companyEntity, ownCompany);
        allCompanyList.add(0, ownCompany);

        //查询外部合作合伙
        List<CompanyEntity> outerCooperatorCompanyList = this.companyDao.getOuterCooperatorCompany(id, projectId);
        if (!CollectionUtils.isEmpty(outerCooperatorCompanyList)) {
            for (CompanyEntity company : outerCooperatorCompanyList) {
                for (CompanyDataDTO dataDTO : allCompanyList) {//去重，如果是外部合作关系，又是事业合伙人，则取事业和人数据，此情况几乎不会存在
                    if (dataDTO.getId().equals(company.getId())) {
                        continue;
                    }
                }
                CompanyDataDTO outerCompany = new CompanyDataDTO();
                BaseDTO.copyFields(company, outerCompany);
                outerCompany.setIsOuterCooperator(1);
                allCompanyList.add(outerCompany);
            }
        }

        //返回数据
        Map<String, Object> returnData = new HashMap<String, Object>();
        returnData.put("allCompanyList", allCompanyList);
        return allCompanyList;
    }


    /**
     * 方法描述：事业合伙人申请（确认身份后，请求数据）
     * 作者：MaoSF
     * 日期：2017/4/1
     *
     * @param:
     * @return:
     */
    @Override
    public Map<String, Object> getCompanyPrincipal(Map<String, Object> map) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String cellphone = (String) map.get("cellphone");
        if (StringUtil.isNullOrEmpty(cellphone)) {
            resultMap.put("companyList", new ArrayList<>());
            return resultMap;
        }
        AccountEntity accountEntity = this.accountDao.getAccountByCellphoneOrEmail(cellphone);
        if (accountEntity == null) {
            resultMap.put("companyList", new ArrayList<>());
            return resultMap;
        }

        CompanyInviteEntity companyInviteEntity = this.companyInviteDao.selectById(map.get("id"));
        List<CompanyDTO> companyList = this.companyDao.getCompanyByUserId(accountEntity.getId());
        List<CompanyDataDTO> companyDataDTOs = new ArrayList<>();
        List<CompanyDataDTO> issueCompanyList = null;
        if ("3".equals(companyInviteEntity.getType())) {//如果是项目外部团队邀请
            //获取可以签发的团队信息
            issueCompanyList = this.getCompanyForSelect(companyInviteEntity.getCompanyId(), companyInviteEntity.getProjectId());
            ProjectEntity projectEntity = projectDao.selectById(companyInviteEntity.getProjectId());
            resultMap.put("projectName", projectEntity.getProjectName());
        }
        for (CompanyDTO companyDTO : companyList) {
            //查询是否是企业负责人
            CompanyDataDTO dto = new CompanyDataDTO();
            BaseDTO.copyFields(companyDTO, dto);
            map.clear();
            map.put("permissionId", SystemParameters.ORG_MANAGER_PERMISSION_ID);//企业负责人权限id
            map.put("companyId", dto.getId());
            map.put("userId", accountEntity.getId());
            List<CompanyUserTableDTO> companyUserList = this.companyUserDao.getCompanyUserByPermissionId(map);
            if (CollectionUtils.isEmpty(companyUserList)) {//如果不是企业负责人，则不返回
                continue;
            }

            dto.setSystemManager(companyUserList.get(0).getUserName());

            if (!StringUtil.isNullOrEmpty(companyDTO.getFilePath())) {
                dto.setFilePath(this.fastdfsUrl + companyDTO.getFileGroup() + "/" + companyDTO.getFilePath());
            }


            if (companyInviteEntity != null && companyInviteEntity.getCompanyId().equals(dto.getId())) {
                dto.setMemo("不能申请自己");
                dto.setFlag(1);
            } else {
                if ("3".equals(companyInviteEntity.getType())) {//如果是项目外部团队邀请
                    setMemoForGetCompanyPrincipal3(dto, issueCompanyList);
                } else {
                    //查询是否已经存在父公司
                    map.clear();
                    map.put("orgId", dto.getId());
                    //理论上存在0 or 1条信息
                    List<CompanyRelationDTO> relationList = this.companyRelationDao.getCompanyRelationByParam(map);
                    if (!CollectionUtils.isEmpty(relationList)) {
                        if ("2".equals(relationList.get(0).getCompanyType())) {
                            dto.setMemo("已成为" + relationList.get(0).getpCompanyName() + "的分支机构");
                        } else {
                            dto.setMemo("已成为" + relationList.get(0).getpCompanyName() + "的事业合伙人");
                        }
                        dto.setFlag(1);
                    }
                }
            }
            companyDataDTOs.add(dto);
        }
        resultMap.put("companyList", companyDataDTOs);
        resultMap.put("userId", accountEntity.getId());
        return resultMap;
    }


    /**
     * 方法描述：外部团队邀请，设置memo，前端不可邀请的提示语
     * 作者：MaoSF
     * 日期：2017/5/8
     */
    private void setMemoForGetCompanyPrincipal3(CompanyDataDTO dto, List<CompanyDataDTO> issueCompanyList) {
        if (!CollectionUtils.isEmpty(issueCompanyList)) {
            for (CompanyDataDTO dataDTO : issueCompanyList) {
                if (dto.getId().equals(dataDTO.getId())) {
                    if (dataDTO.getIsOuterCooperator() == 1) {
                        dto.setMemo(dataDTO.getCompanyName() + "已经是该项目的外部合作组织");
                    } else {
                        dto.setMemo(dataDTO.getCompanyName() + "已经在本组织体系中");
                    }
                    dto.setFlag(1);
                    break;
                }
            }
        }
    }

    /**
     * 方法描述：点击邀请信息请求数据
     * 作        者：chenzhujie
     * 日        期：2017/2/27
     */
    @Override
    public AjaxMessage getCompanyByInviteUrl(String id) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        CompanyInviteEntity companyInviteEntity = companyInviteDao.selectById(id);

        if (companyInviteEntity != null) {
            String companyId = companyInviteEntity.getCompanyId();
            CompanyEntity companyEntity = companyDao.selectById(companyId);
            if (companyEntity != null) {
                resultMap.put("id", companyId);
                resultMap.put("companyName", companyEntity.getCompanyName());
                //查询公司logo
                resultMap.put("filePath", this.projectSkyDriverService.getCompanyLogo(companyId));
                //查询企业管理员
                CompanyUserTableDTO orgManager = this.companyUserService.getOrgManager(companyId);
                if (orgManager != null) {
                    resultMap.put("systemManager", orgManager.getUserName());

                }
                String cellphone = companyInviteEntity.getInviteCellphone();
                cellphone = cellphone.substring(0, 3) + "*****" + cellphone.substring(8);
                resultMap.put("cellphone", cellphone);
                if (!StringUtil.isNullOrEmpty(companyInviteEntity.getProjectId())) {
                    ProjectEntity project = this.projectDao.selectById(companyInviteEntity.getProjectId());
                    resultMap.put("projectName", (project == null ? "" : project.getProjectName()));
                }

                return AjaxMessage.succeed(resultMap);
            }
        }
        return AjaxMessage.failed("请求失败");
    }

    /**
     * 方法描述：邀请事业合伙人,分公司，外部合作
     * 作者：MaoSF
     * 日期：2017/4/1
     */
    @Override
    public AjaxMessage inviteParent(InviteParentDTO dto) throws Exception {
        CompanyUserEntity companyUser = this.companyUserDao.getCompanyUserByUserIdAndCompanyId(dto.getAccountId(), dto.getCurrentCompanyId());
        CompanyEntity companyEntity = this.companyDao.selectById(dto.getCurrentCompanyId());
        if (companyUser == null || companyEntity == null) {
            return AjaxMessage.failed("操作失败");
        }
        CompanyInviteEntity companyInviteEntity = new CompanyInviteEntity();
        if ("3".equals(dto.getType())) {//外部合作组织

            if (dto.getProjectId() == null || "".equals(dto.getProjectId())) {//验证数据,项目ID必传
                return AjaxMessage.failed("操作失败");
            }
            companyInviteEntity.setProjectId(dto.getProjectId());
        }

        companyInviteEntity.setCompanyId(dto.getCurrentCompanyId());
        companyInviteEntity.setCreateBy(dto.getAccountId());
        companyInviteEntity.setType(dto.getType());

        for (String cellphone : dto.getCellphoneList()) {

            companyInviteEntity.setInviteCellphone(cellphone);
            companyInviteEntity.setId(StringUtil.buildUUID());
            String url = this.serverUrl + "/na/bPartner/invited/" + companyInviteEntity.getId();//点击的url地址
            companyInviteEntity.setUrl(url);

            this.companyInviteDao.insert(companyInviteEntity);

            //发送信息
            if ("1".equals(dto.getType())) {
                this.sendMsg(cellphone, StringUtil.format(SystemParameters.INVITE_PARENT_MSG, companyUser.getUserName(), companyEntity.getCompanyName(), url));
            }
            if ("2".equals(dto.getType())) {
                this.sendMsg(cellphone, StringUtil.format(SystemParameters.INVITE_PARENT_MSG2, companyUser.getUserName(), companyEntity.getCompanyName(), url));
            }
            if ("3".equals(dto.getType())) {

                //插入外部合作组织记录
                PartnerEntity partnerEntity = new PartnerEntity();
                partnerEntity.setFromCompanyId(dto.getCurrentCompanyId());
                partnerEntity.setFromUserId(dto.getAccountId());
                partnerEntity.setCreateBy(dto.getAccountId());
                partnerEntity.setCreateDate(new Date());
                partnerEntity.setType(Integer.valueOf(dto.getType()));//合伙人类别=3
                partnerEntity.setNickName(companyUser.getUserName());
                partnerEntity.setProjectId(dto.getProjectId());
                partnerEntity.setPhone(cellphone);
                partnerEntity.setId(companyInviteEntity.getId());//此ID与companyInviteEntity的ID同步
                organizationDao.insert(partnerEntity);

                //发送信息
                this.sendMsg(cellphone, StringUtil.format(SystemParameters.INVITE_PARENT_MSG3, companyUser.getUserName(), companyEntity.getCompanyName(), url));
            }
        }

        return AjaxMessage.succeed("邀请发送成功").setInfo("邀请发送成功");
    }

    /**
     * 方法描述：邀请事业合伙人身份验证
     * 作者：MaoSF
     * 日期：2017/4/1
     *
     * @param map
     * @param:map(id,cellphone),id为url地址中携带的id
     * @return:
     */
    @Override
    public AjaxMessage verifyIdentityForParent(Map<String, Object> map) throws Exception {
        CompanyInviteEntity companyInviteEntity = companyInviteDao.selectById(map.get("id"));

        if (companyInviteEntity != null) {
            if (companyInviteEntity.getInviteCellphone().equals(map.get("cellphone"))) {
                return AjaxMessage.succeed(null);
            }
            return AjaxMessage.failed("系统验证失败，请检查您输入的手机号码");
        }

        return AjaxMessage.failed("邀请信息已失效");
    }

    @Override
    public CompanyInviteEntity getCompanyInviteById(String id) throws Exception {
        return this.companyInviteDao.selectById(id);
    }

    /**
     * 方法描述：设置事业合伙人的别名
     * 作者：MaoSF
     * 日期：2017/4/18
     */
    @Override
    public AjaxMessage setBusinessPartnerNickName(BusinessPartnerDTO dto) throws Exception {
        CompanyRelationDTO dto1 = this.companyRelationDao.getCompanyRelationByOrgId(dto.getCompanyId());
        if (dto1 != null) {
            CompanyEntity companyEntity = new CompanyEntity();
            companyEntity.setId(dto.getCompanyId());
            companyEntity.setCompanyShortName(dto.getCompanyName());
            companyEntity.setCompanyName(dto.getCompanyName());
            companyDao.updateById(companyEntity);
            if ("3".equals(dto1.getCompanyType())) {
                BusinessPartnerEntity businessPartner = new BusinessPartnerEntity();
                businessPartner.setId(dto1.getId());
                businessPartner.setNickName(dto.getCompanyName());
                this.businessPartnerDao.updateById(businessPartner);
            }
            if ("2".equals(dto1.getCompanyType())) {
                SubCompanyEntity subCompany = new SubCompanyEntity();
                subCompany.setId(dto1.getId());
                subCompany.setNickName(dto.getCompanyName());
                this.subCompanyDao.updateById(subCompany);
            }
            if (!StringUtils.isEmpty(dto.getRelationTypeId())) {
                CompanyRelationEntity companyRelationEntity = new CompanyRelationEntity();
                companyRelationEntity.setId(dto1.getId());
                companyRelationEntity.setTypeId(dto.getRelationTypeId());
                companyRelationDao.updateById(companyRelationEntity);
            }
            return AjaxMessage.succeed("操作成功");
        }
        return AjaxMessage.failed("操作失败");
    }


    /**
     * 方法：设置免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param orgId      组织ID
     * @param expiryDate 免费到期日期
     */
    @Override
    public void setExpiryDate(String orgId, Date expiryDate) {
        setExpiryDate(orgId, expiryDate, null);
    }

    /**
     * 方法：设置免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param orgId          组织ID
     * @param expiryDate     免费到期日期
     * @param operatorUserId 操作者ID
     */
    @Override
    public void setExpiryDate(String orgId, Date expiryDate, String operatorUserId) {
        if ((orgId == null) || (expiryDate == null)) throw new NullPointerException("setExpiryDate 参数错误");

        OrgAuthEntity entity = new OrgAuthEntity();
        entity.setId(orgId);
        entity.setExpiryDate(expiryDate);
        entity.setUpdateBy(operatorUserId);
        entity.setUpdateDate(new Date());
        orgAuthDao.updateById(entity);
    }

    /**
     * 方法：延长免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    @Override
    public void extendExpiryDate(String orgId, Integer days) {
        extendExpiryDate(orgId, days, null);
    }

    /**
     * 方法：延长免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param orgId          组织ID
     * @param days           延期时长
     * @param operatorUserId 操作者ID
     */
    @Override
    public void extendExpiryDate(String orgId, Integer days, String operatorUserId) {
        if ((orgId == null) || (days == null)) throw new NullPointerException("extendExpiryDate 参数错误");
        OrgAuthEntity entity = orgAuthDao.selectById(orgId);
        if (entity == null) throw new IllegalArgumentException("extendExpiryDate 无法创建OrgAuthEntity");

        Date expiryDate = (entity.getExpiryDate() != null) ? DateUtils.str2Date(DateUtils.getDateAfter(entity.getExpiryDate(), days), DateUtils.date_sdf) : null;
        if ((expiryDate == null) && (entity.getApplyDate() != null))
            expiryDate = DateUtils.str2Date(DateUtils.getDateAfter(entity.getApplyDate(), days), DateUtils.date_sdf);
        if ((expiryDate == null) && (entity.getCreateDate() != null))
            expiryDate = DateUtils.str2Date(DateUtils.getDateAfter(entity.getCreateDate(), days), DateUtils.date_sdf);
        if (expiryDate == null)
            expiryDate = DateUtils.str2Date(DateUtils.getDateAfter(new Date(), days), DateUtils.date_sdf);
        setExpiryDate(orgId, expiryDate, operatorUserId);
    }

    /**
     * 方法：提交审核
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    @Override
    public void applyAuthentication(OrgAuthenticationDTO authentication) {
        if (authentication == null) throw new IllegalArgumentException("applyAuthentication 参数错误");
        //更新审核记录，如果在审核表内没有找到记录创建一条
        OrgAuthEntity entity = (authentication.getId() != null) ? orgAuthDao.selectById(authentication.getId()) : null;
        Boolean isNew = false;
        if (entity == null) {
            entity = new OrgAuthEntity();
            isNew = true;
        }
        BeanUtilsEx.copyProperties(authentication, entity);
        entity.setApplyDate(new Date());
        entity.setAuthenticationStatus(1);
        entity.setRejectReason("");
        entity.setDeleted(0);

        if (isNew) {
            if (entity.getId() == null) entity.setId(StringUtil.buildUUID());
            entity.setCreateDate(entity.getApplyDate());
            orgAuthDao.insert(entity);
        } else {
            entity.setUpdateDate(entity.getApplyDate());
            orgAuthDao.updateById(entity);
        }
    }

    /**
     * 方法：处理审核
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param authorizeResult 审核结果
     */
    @Override
    public OrgAuthenticationDTO authorizeAuthentication(OrgAuthorizeResultDTO authorizeResult) {
        if ((authorizeResult == null) || (authorizeResult.getOrgId() == null))
            throw new IllegalArgumentException("authorizeAuthentication 参数错误");

        if ((authorizeResult.getStatus() != 1) && (authorizeResult.getRejectType() == null))
            throw new IllegalArgumentException("不通过审核原因不能为空");

        //保存当次审核结果
        OrgAuthEntity entity = orgAuthDao.selectById(authorizeResult.getOrgId());
        if (entity == null) throw new IllegalArgumentException("authorizeAuthentication 参数错误");

        BeanUtilsEx.copyProperties(authorizeResult, entity);
        entity.setUpdateDate(new Date());
        orgAuthDao.updateById(entity);

        //更新认证信息历史
        orgAuthAuditDao.updateStatusByOrgId(authorizeResult.getOrgId()); //更新以往数据的isNew字段，设置为0

        OrgAuthAuditDO auditDO = new OrgAuthAuditDO();
        BeanUtilsEx.copyProperties(authorizeResult, auditDO);
        if (entity.getUpdateDate() != null) {
            auditDO.setApproveDate(LocalDateTime.ofInstant(entity.getUpdateDate().toInstant(), ZoneId.systemDefault()));
        }
        auditDO.setAuditMessage(authorizeResult.getRejectType().toString());
        auditDO.setIsNew(1);
        if (entity.getApplyDate() != null) {
            auditDO.setSubmitDate(LocalDateTime.ofInstant(entity.getApplyDate().toInstant(), ZoneId.systemDefault()));
        }
        auditDO.setCreateDate(entity.getUpdateDate());
        auditDO.setCreateBy(entity.getUpdateBy());
        if (auditDO.getId() == null) auditDO.setId(StringUtil.buildUUID());
        orgAuthAuditDao.insert(auditDO);

        OrgAuthenticationDataDTO data = orgAuthDao.getOrgAuthenticationInfo(entity.getId());
        return createAuthenticationByEntity(data);
    }

    /**
     * 方法：列出申请审核记录
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param query 查询过滤条件
     */
    @Override
    public List<OrgAuthenticationDTO> listAuthentication(OrgAuthenticationQueryDTO query) {
        if (query == null) {
            throw new IllegalArgumentException("listAuthentication 参数错误");
        }
        List<OrgAuthenticationDataDTO> dataList = orgAuthDao.listOrgAuthenticationInfo(query);
        List<OrgAuthenticationDTO> dtoList = new ArrayList<>();
        for (OrgAuthenticationDataDTO data : dataList) {
            OrgAuthenticationDTO dto = createAuthenticationByEntity(data);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public OrgAuthenticationPageDTO getAuthenticationPage(OrgAuthenticationQueryDTO query) {
        if (query == null) {
            throw new IllegalArgumentException("getAuthenticationPage 参数错误");
        }
        OrgAuthenticationDataPageDTO dataPage = orgAuthDao.getOrgAuthenticationInfoPage(query);
        OrgAuthenticationPageDTO result = new OrgAuthenticationPageDTO();
        result.setTotal(dataPage.getTotal());
        if ((result.getTotal() > 0) && (dataPage.getList() != null)) {
            List<OrgAuthenticationDTO> dtoList = new ArrayList<>();
            for (OrgAuthenticationDataDTO data : dataPage.getList()) {
                OrgAuthenticationDTO dto = createAuthenticationByEntity(data);
                dtoList.add(dto);
            }
            result.setList(dtoList);
        }
        return result;
    }

    /**
     * 方法：获取注册信息
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    @Override
    public OrgAuthenticationDTO getAuthenticationById(String orgId) {
        if (orgId == null) {
            throw new IllegalArgumentException("getAuthenticationById 参数错误");
        }
        OrgAuthenticationDataDTO dto = orgAuthDao.getOrgAuthenticationInfo(orgId);
        return createAuthenticationByEntity(dto);
    }

    @Override
    public String getOrgTypeId(String companyId) {
        if (StringUtil.isNullOrEmpty(companyId)) {
            return null;
        }
        CompanyRelationDTO relation = this.companyRelationDao.getCompanyRelationByOrgId(companyId);
        if (relation != null) {
            return relation.getTypeId();
        }
        return null;
    }

    @Override
    public String getFinancialHandleCompanyId(String companyId) {
        CompanyRelationDTO relation = this.companyRelationDao.getCompanyRelationByOrgId(companyId);
        if (relation == null || StringUtil.isNullOrEmpty(relation.getTypeId()) || !"3".equals(relation.getTypeId())) {
            return companyId;
        } else {
            return getFinancialHandleCompanyId(relation.getOrgPid());
        }
    }

    @Override
    public String getExpensesDetailLedgerCompany(String companyId,String flag) throws Exception {
        List<String> companyIds = new ArrayList<>();
        if(flag.equals("root")){
            companyIds.add(companyId);
            List<CompanyEntity> list = getAllChildrenCompany(companyId);
            list.stream().forEach(c->{
                companyIds.add(c.getId());
            });
        }else {
            Map<String,Object> map = new HashMap<>();
            map.put("companyId",companyId);
            Map<String,OrgTreeDTO> treeMap = this.getTreeMap(map);
            OrgTreeDTO tree = treeMap.get(flag);
             this.getCompanyIds(tree,companyIds);
        }
        if(CollectionUtils.isEmpty(companyIds)){
            return "noData";//此处为了防止传递null,数据库中过滤到查询
        }
        return org.apache.commons.lang3.StringUtils.join(companyIds,",");
    }

    @Override
    public List<String> getExpensesDetailLedgerCompanyList(String companyId, String flag) throws Exception {
        List<String> companyIds = new ArrayList<>();
        if(flag.equals("root")){
            companyIds.add(companyId);
            List<CompanyEntity> list = getAllChildrenCompany(companyId);
            list.stream().forEach(c->{
                companyIds.add(c.getId());
            });
        }else {
            Map<String,Object> map = new HashMap<>();
            map.put("companyId",flag.substring(0,32));//前32位为组织id。
            Map<String,OrgTreeDTO> treeMap = this.getTreeMap(map);
            OrgTreeDTO tree = treeMap.get(flag);
            this.getCompanyIds(tree,companyIds);
        }
        if(CollectionUtils.isEmpty(companyIds)){
            companyIds.add( "noData");//此处为了防止传递null,数据库中过滤到查询
        }
        return companyIds;
    }

    void getCompanyIds(OrgTreeDTO tree,List<String> companyIds){
        if(tree==null){
            return ;
        }
        for(OrgTreeDTO child:tree.getChildren()){
            companyIds.add(child.getCompanyId());
            getCompanyIds(child,companyIds);
        }
    }


    private OrgAuthenticationDTO createAuthenticationByEntity(OrgAuthenticationDataDTO data) {
        if (data == null) {
            throw new IllegalArgumentException("createAuthenticationByEntity 参数错误");
        }
        OrgAuthenticationDTO authentication = new OrgAuthenticationDTO();
        BeanUtilsEx.copyProperties(data, authentication);
        List<ProjectSkyDriveEntity> fileList = data.getAttachList();
        //复制附件文件名
        for (ProjectSkyDriveEntity file : fileList) {
            if (file.getType() == NetFileType.CERTIFICATE_ATTACH) {
                authentication.setSealPhoto(getFilePath(file));
            } else if (file.getType() == NetFileType.BUSINESS_LICENSE_ATTACH) {
                authentication.setBusinessLicensePhoto(getFilePath(file));
            } else if (file.getType() == NetFileType.LEGAL_REPRESENTATIVE_ATTACH) {
                authentication.setLegalRepresentativePhoto(getFilePath(file));
            } else if (file.getType() == NetFileType.OPERATOR_ATTACH) {
                authentication.setOperatorPhoto(getFilePath(file));
            }
        }
        return authentication;
    }

    private String getFilePath(ProjectSkyDriveEntity entity) {
        return this.fastdfsUrl + entity.getFileGroup() + "/" + entity.getFilePath();
    }

    /**
     * 描述     查询组织信息
     *
     * @param query 组织查询条件
     * @return 组织信息
     * @author 张成亮
     **/
    @Override
    public List<CompanyDTO> listCompany(CompanyQueryDTO query) {
        List<CompanyDTO> result = null;
        if (isPayQuery(query)) {
            if (isContract(query.getFeeType()) && isCreatorQuery(query)){
                result = companyDao.listCompanyA(query);
            } else if (isCheck(query.getFeeType()) && !isCreatorQuery(query)) {
                result = new ArrayList<>();
                result.add(getCreator(query));
            } else if (isCooperate(query.getFeeType())){
                result = companyDao.listCompanyCooperate(query);
            }
        } else {
            if (isCheck(query.getFeeType()) && isCreatorQuery(query)){
                result = companyDao.listCompanyCooperate(query);
            } else if (isCooperate(query.getFeeType())) {
                result = companyDao.listCompanyCooperate(query);
            }
        }
        return result;
    }

    //是否合同回款
    private boolean isContract(Integer type){
        return (ProjectCostConst.FEE_TYPE_CONTRACT == type);
    }

    //是否技术审查费
    private boolean isCheck(Integer type){
        return (ProjectCostConst.FEE_TYPE_CHECK == type);
    }

    //是否合作设计费
    private boolean isCooperate(Integer type){
        return (ProjectCostConst.FEE_TYPE_COOPERATE == type);
    }

    //是否付款查询
    private boolean isPayQuery(CompanyQueryDTO query){
        return StringUtils.isEmpty(query.getIsPay()) || StringUtils.isSame("0",query.getIsPay());
    }

    //是否立项方发起的查询
    private boolean isCreatorQuery(CompanyQueryDTO query){
        TraceUtils.check(query != null);
        TraceUtils.check(!StringUtils.isEmpty(query.getProjectId()),log,"!projectId不能为空");
        ProjectEntity project = projectDao.selectById(query.getProjectId());
        TraceUtils.check(project != null);
        TraceUtils.check(!StringUtils.isEmpty(query.getCurrentCompanyId()),log,"!currentCompanyId不能为空");
        return StringUtils.isSame(query.getCurrentCompanyId(),project.getCompanyId());
    }

    //获取项目立项方信息
    private CompanyDTO getCreator(CompanyQueryDTO query){
        TraceUtils.check(query != null);
        TraceUtils.check(!StringUtils.isEmpty(query.getProjectId()),log,"!projectId不能为空");
        ProjectEntity project = projectDao.selectById(query.getProjectId());
        TraceUtils.check(project != null);
        CompanyEntity company = companyDao.selectById(project.getId());
        TraceUtils.check(!StringUtils.isEmpty(query.getCurrentCompanyId()),log,"!currentCompanyId不能为空");
        return BeanUtils.createFrom(company,CompanyDTO.class);
    }

    //获取项目信息
    private ProjectEntity getProjectInfo(String projectId){
        return projectDao.selectById(projectId);
    }
}
