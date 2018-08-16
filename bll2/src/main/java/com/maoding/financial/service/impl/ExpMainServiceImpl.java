package com.maoding.financial.service.impl;

import com.maoding.commonModule.dto.RelationTypeDTO;
import com.maoding.commonModule.dto.SaveRelationRecordDTO;
import com.maoding.commonModule.service.RelationRecordService;
import com.maoding.companybill.dto.SaveCompanyBillDTO;
import com.maoding.companybill.service.CompanyBalanceService;
import com.maoding.companybill.service.CompanyBillService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.*;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.exception.CustomException;
import com.maoding.financial.dao.ExpAuditDao;
import com.maoding.financial.dao.ExpDetailDao;
import com.maoding.financial.dao.ExpMainDao;
import com.maoding.financial.dto.*;
import com.maoding.financial.entity.ExpAuditEntity;
import com.maoding.financial.entity.ExpDetailEntity;
import com.maoding.financial.entity.ExpMainEntity;
import com.maoding.financial.service.ExpMainService;
import com.maoding.message.dto.SendMessageDTO;
import com.maoding.message.entity.MessageEntity;
import com.maoding.message.service.MessageService;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dto.CompanyRelationDTO;
import com.maoding.org.dto.CompanyUserTableDTO;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.service.CompanyUserService;
import com.maoding.process.dto.ActivitiDTO;
import com.maoding.process.service.ProcessService;
import com.maoding.project.dao.ProjectSkyDriverDao;
import com.maoding.project.entity.ProjectSkyDriveEntity;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.role.service.PermissionService;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpMainServiceImpl
 * 描    述 : 报销主表ServiceImpl
 * 作    者 : LY
 * 日    期 : 2016/7/26-16:07
 */

@Service("expMainService")
public class ExpMainServiceImpl extends GenericService<ExpMainEntity> implements ExpMainService {


    @Autowired
    private ExpMainDao expMainDao;

    @Autowired
    private ExpDetailDao expDetailDao;

    @Autowired
    private ExpAuditDao expAuditDao;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private ProjectSkyDriverDao projectSkyDriverDao;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CompanyBillService companyBillService;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private CompanyBalanceService companyBalanceService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private RelationRecordService relationRecordService;


    /**
     * 方法描述：报销增加或者修改
     * 作   者：LY
     * 日   期：2016/7/26 17:35
     */
    public AjaxMessage saveOrUpdateExpMainAndDetail(ExpMainDTO dto, String userId, String companyId) throws Exception {

        //报销明细
        List<ExpDetailDTO> detailList = dto.getDetailList();
        //是增加true还是修改false操作
        boolean flag = StringUtil.isNullOrEmpty(dto.getId());

        if (StringUtil.isNullOrEmpty(dto.getAuditPerson())) {
            return new AjaxMessage().setCode("1").setInfo("审批人不能为空");
        }
        if (CollectionUtils.isEmpty(detailList)) {
            return new AjaxMessage().setCode("1").setInfo("报销明细不能为空");
        }
        if (!StringUtil.isNullOrEmpty(dto.getRemark()) && dto.getRemark().length() > 255) {
            return new AjaxMessage().setCode("1").setInfo("备注长度过长");
        }
//        for (ExpDetailDTO detailDTO : detailList) {
//            if (detailDTO.getExpAmount() == null) {
//                return new AjaxMessage().setCode("1").setInfo("报销金额不能为空");
//            } else if (StringUtil.isNullOrEmpty(detailDTO.getExpType())) {
//                return new AjaxMessage().setCode("1").setInfo("报销类别不能为空");
//            } else if (StringUtil.isNullOrEmpty(detailDTO.getExpUse())) {
//                return new AjaxMessage().setCode("1").setInfo("用途说明不能为空");
//            }
//        }
        // 验证end

        //保存报销主表
        ExpMainEntity entity = new ExpMainEntity();
        BaseDTO.copyFields(dto, entity);
        entity.setApproveStatus("0");
        if (StringUtil.isNullOrEmpty(dto.getId())) {//插入
            Map<String, Object> map = new HashMap<>();
            map.put("companyId", companyId);
            String expNo = expMainDao.getMaxExpNo(map);
            String yyMMdd = DateUtils.date2Str(DateUtils.yyyyMMdd);
            if ("1001".equals(expNo)) {
                //自动生成一个
                expNo = yyMMdd + "0001";
            } else if (yyMMdd.equals(expNo.substring(0, expNo.length() - 4))) {

            } else {
                expNo = yyMMdd + "0001";
            }
            //判断是否退回后的编辑
            if (!StringUtil.isNullOrEmpty(dto.getPid())) {
                ExpMainEntity exp = expMainDao.selectById(dto.getPid());
                exp.setExpFlag(1);
                entity.setExpFlag(2);
                expMainDao.updateById(exp);
                //新开
                entity.setId(StringUtil.buildUUID());
                //复制原来的附件记录
                Map<String, Object> param = new HashMap<>();
                param.put("targetId", dto.getPid());
                param.put("type", NetFileType.EXPENSE_ATTACH);
                List<ProjectSkyDriveEntity> attachs = projectSkyDriverService.getNetFileByParam(param);
                for (ProjectSkyDriveEntity attach : attachs) {
                    attach.setId(StringUtil.buildUUID());
                    attach.setTargetId(entity.getId());
                    projectSkyDriverDao.insert(attach);
                }
            } else {
                entity.setExpFlag(0);
                entity.setId(dto.getTargetId());
            }
            if (dto.getType() == null) {
                entity.setType(1);
            }
            if(entity.getId()==null){
                entity.setId(StringUtil.buildUUID());
            }
            dto.setExpNo(expNo);
            entity.setExpNo(expNo);
            entity.set4Base(userId, userId, new Date(), new Date());
            entity.setCompanyId(companyId);
            expMainDao.insert(entity);
        } else {//保存
            entity.set4Base(null, userId, null, new Date());
            //版本控制
            entity.setExpDate(DateUtils.getDate());
            int result = expMainDao.updateById(entity);
            if (result == 0) {
                return new AjaxMessage().setCode("0").setInfo("保存失败").setData(dto);
            }
        }
        Integer myTaskType = getMyTaskType(entity);
        //主表Id
        String id = entity.getId();
        //保存报销明细表
        //按照MainId先删除原来明细
        expDetailDao.deleteByMainId(id);
        ExpDetailEntity detailEntity = null;
        int seq = 1;
        for (ExpDetailDTO detailDTO : detailList) {
            detailEntity = new ExpDetailEntity();
            BaseDTO.copyFields(detailDTO, detailEntity);
            detailDTO.setId(StringUtil.buildUUID());
            detailEntity.setId(detailDTO.getId());
            detailEntity.setMainId(id);
            detailEntity.setSeq(seq++);
            if (detailEntity.getExpAllName() != null) {
                String[] allName = detailEntity.getExpAllName().split("-");
                detailEntity.setExpPName(allName[0]);
                detailEntity.setExpName(allName[1]);
            }
            detailEntity.set4Base(userId, userId, new Date(), new Date());
            expDetailDao.insert(detailEntity);
        }

        //保存报销审核表
        ExpAuditEntity auditEntity = new ExpAuditEntity();
        auditEntity.setId(StringUtil.buildUUID());
        auditEntity.setMainId(id);
        auditEntity.setApproveStatus("0");
        auditEntity.setAuditPerson(dto.getAuditPerson());
        auditEntity.set4Base(userId, userId, new Date(), new Date());
        List<ExpAuditEntity> auditEntities = expAuditDao.selectByMainId(id);
        if (!flag) {//修改
            //查询原来为“Y"的Id并把它插入到当前auditEntity的parentId
            if (!CollectionUtils.isEmpty(auditEntities)) {
                auditEntity.setParentId(auditEntities.get(0).getId());
            }
            //把原来所有的is_new改为“N"
            expAuditDao.updateIsNewByMainId(id);
        }
        //每次都新生成记录(包括多次编辑和多次驳回)
        if (null != auditEntities && auditEntities.size() > 0) {
            ExpAuditEntity expAuditEntity = auditEntities.get(0);
            if (auditEntities.get(0).getApproveStatus().equals("0")) {
                String oldAuditPerson = expAuditEntity.getAuditPerson();
                expAuditEntity.setAuditPerson(dto.getAuditPerson());
                expAuditEntity.setIsNew("Y");
                expAuditDao.updateById(expAuditEntity);
                if (!dto.getAuditPerson().equals(oldAuditPerson)) {//如果修改的时候，改变了社会人。则发送任务
                    //忽略原来的任务
                    this.myTaskService.ignoreMyTask(entity.getId(), myTaskType, oldAuditPerson);
                    //指派任务
                    this.myTaskService.saveMyTask(entity.getId(), myTaskType, companyId, dto.getAuditPerson(), true, userId, companyId);
                }
            } else {
                auditEntity.setIsNew("Y");
                expAuditDao.insert(auditEntity);
                //指派任务，此处为。被打回后。重新修改。启动审批
                this.myTaskService.saveMyTask(entity.getId(), myTaskType, companyId, dto.getAuditPerson(), true, userId, companyId);
            }
        } else {
            auditEntity.setIsNew("Y");
            expAuditDao.insert(auditEntity);
            //指派任务 ，新增报销，发送任务
            this.myTaskService.saveMyTask(entity.getId(), myTaskType, companyId, dto.getAuditPerson(), true, userId, companyId);
        }
        dto.setId(entity.getId());
        return new AjaxMessage().setCode("0").setInfo("保存成功").setData(dto);
    }

    @Override
    public AjaxMessage applyProjectCost(ApplyProjectCostDTO dto) throws Exception {
        String userId = dto.getAccountId();
        String companyId = dto.getCurrentCompanyId();
        CompanyUserEntity currentCompanyUser = companyUserDao.getCompanyUserByUserIdAndCompanyId(userId,companyId);
        if(currentCompanyUser==null){
            return AjaxMessage.failed("数据错误");
        }
        //是增加true还是修改false操作
        //保存报销主表
        ExpMainEntity entity = new ExpMainEntity();
        BaseDTO.copyFields(dto, entity);
        entity.setApproveStatus("0");
        if (StringUtil.isNullOrEmpty(dto.getId())) {//插入
            this.saveExpMain(entity,dto,userId,companyId);
        }  else {//保存
            int result = 0;
            ExpMainEntity exp = expMainDao.selectById(dto.getId());
            if(exp!=null && "2".equals(exp.getApproveStatus())){
                if(!StringUtil.isNullOrEmpty(dto.getTargetId()) && dto.getId().equals(dto.getTargetId())){
                    return AjaxMessage.failed("参数错误");
                }
                //判断是否退回后的编辑
                exp.setExpFlag(1);
                entity.setExpFlag(2);
                expMainDao.updateById(exp);
                saveExpMain(entity,dto,userId,companyId);
            }else {
                entity.set4Base(null, userId, null, new Date());
                //版本控制
                entity.setExpDate(DateUtils.getDate());
                result = expMainDao.updateById(entity);
                if (result == 0) {
                    return new AjaxMessage().setCode("0").setInfo("保存失败").setData(dto);
                }
            }
        }
        //主表Id
        String id = entity.getId();
        //保存报销明细表
        this.saveExpDetail(dto ,id);
        return new AjaxMessage().setCode("0").setInfo("保存成功").setData(dto);
    }


    private void saveExpDetail(ApplyProjectCostDTO dto,String mainId) throws Exception {
        //按照MainId先删除原来明细
        String userId = dto.getAccountId();
        expDetailDao.deleteByMainId(mainId);
        ExpDetailEntity detailEntity = null;
        detailEntity = new ExpDetailEntity();
        detailEntity.setId(StringUtil.buildUUID());
        detailEntity.setProjectId(dto.getProjectId());
        detailEntity.setMainId(mainId);
        detailEntity.setSeq(1);
        BigDecimal expAmount = dto.getApplyAmount().multiply(new BigDecimal("10000"));//换成元
        detailEntity.setExpAmount(expAmount);
        detailEntity.set4Base(userId, userId, new Date(), new Date());
        expDetailDao.insert(detailEntity);

        //处理关联项
        SaveRelationRecordDTO relation = new SaveRelationRecordDTO();
        relation.setAccountId(userId);
        relation.setTargetId(mainId);
        RelationTypeDTO relationTypeDTO = new RelationTypeDTO();
        relationTypeDTO.setRelationId(dto.getTargetId());
        relationTypeDTO.setOperateRecordId(detailEntity.getId());
        relationTypeDTO.setRecordType(CopyTargetType.PROJECT_COST_POINT_DETAIL);
        this.relationRecordService.saveRelationRecord(relation);
    }

    private ExpMainEntity saveExpMain(ExpMainEntity entity,ApplyProjectCostDTO dto,String userId,String companyId) throws Exception{
        String expNo = this.getExpNo(companyId);
        entity.setExpFlag(0);
      //  dto.setExpNo(expNo);
        if(StringUtil.isNullOrEmpty(dto.getType())){
            entity.setType(1); //默认为报销费用
        }
        entity.setExpNo(expNo);
        entity.set4Base(userId, userId, new Date(), new Date());
        entity.setCompanyId(companyId);
        entity.initEntity();
        expMainDao.insert(entity);

        String targetType = ProcessTypeConst.PROCESS_TYPE_PROJECT_PAY_APPLY;
//        if(entity.getType()==1){
//            targetType = ProcessTypeConst.PROCESS_TYPE_EXPENSE;
//        }else {
//            targetType= ProcessTypeConst.PROCESS_TYPE_COST_APPLY;
//        }
        // 启动流程
        ActivitiDTO activitiDTO = new ActivitiDTO(entity.getId(),entity.getCompanyUserId(),companyId,userId,dto.getApplyAmount(),targetType);
        activitiDTO.getParam().put("approveUser",dto.getAuditPerson());
        this.processService.startProcessInstance(activitiDTO);
        return entity;
    }


    private String getExpNo(String companyId){
        Map<String, Object> map = new HashMap<>();
        map.put("companyId", companyId);
        String expNo = expMainDao.getMaxExpNo(map);
        String yyMMdd = DateUtils.date2Str(DateUtils.yyyyMMdd);
        if ("1001".equals(expNo)) {
            //自动生成一个
            expNo = yyMMdd + "0001";
        } else if (yyMMdd.equals(expNo.substring(0, expNo.length() - 4))) {

        } else {
            expNo = yyMMdd + "0001";
        }
        return expNo;
    }

    private Integer getMyTaskType(ExpMainEntity entity) {
        if (entity.getType() == 2) {
            return SystemParameters.COST_AUDIT;
        }
        return SystemParameters.EXP_AUDIT;
    }


    /**
     * 方法描述：得到当前公司和当前组织下面人员
     * 作   者：LY
     * 日   期：2016/8/3 17:17
     *
     * @param companyId 公司Id  orgId 组织Id
     */
    public List<CompanyUserTableDTO> getUserList(String companyId, String orgId) throws Exception {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("companyId", companyId);
        if (!companyId.equals(orgId)) {
            mapParams.put("orgId", orgId);
        }
        return companyUserService.getUserByOrgId(mapParams);
    }


    public int getExpMainPageCount(Map<String, Object> param) {
        return expMainDao.getExpMainPageCount(param);
    }


    /**
     * 方法描述：撤回报销
     * 作   者：LY
     * 日   期：2016/7/29 11:01
     *
     * @param id--报销单id type--状态(3撤回)
     */
    public int recallExpMain(String id, String versionNum, String type) throws Exception {
        ExpMainEntity entity = this.expMainDao.selectById(id);
        entity.setId(id);
        entity.setApproveStatus(type);
        if (versionNum != null) {
            entity.setVersionNum(Integer.parseInt(versionNum));
        }
        ExpAuditEntity auditEntity = new ExpAuditEntity();
        auditEntity.setMainId(id);
        auditEntity.setApproveStatus(type);
        if (versionNum != null) {
            auditEntity.setVersionNum(Integer.parseInt(versionNum));
        }
        expAuditDao.updateByMainId(auditEntity);
        deleteMyTask(id, this.getMyTaskType(entity));
        return expMainDao.updateById(entity);
    }


    private void deleteMyTask(String targetId, Integer taskType) {
        setMyTaskStatus(targetId, taskType, null, true);
    }

    private void setMyTaskStatus(String targetId, Integer taskType, String param2, boolean isDeleted) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("taskType", taskType);
        param.put("targetId", targetId);
        param.put("param2", param2);
        if (isDeleted) {
            param.put("param4", "1");
        } else {
            param.put("status", "1");
        }
        myTaskService.updateStatesByTargetId(param);
    }

    /**
     * 方法描述：退回报销
     * 作   者：LY
     * 日   期：2016/7/29 11:01
     *
     * @param dto -- mainId--报销单id  approveStatus--状态(2.退回) auditMessage审批意见
     */
    public int recallExpMain(ExpAuditDTO dto) throws Exception {
        ExpAuditEntity auditEntity = new ExpAuditEntity();
        auditEntity.setMainId(dto.getMainId());
        auditEntity.setApproveStatus(dto.getApproveStatus());
        auditEntity.setAuditMessage(dto.getAuditMessage());
        expAuditDao.updateByMainId(auditEntity);
        ExpMainEntity entity = this.expMainDao.selectById(dto.getMainId());
        entity.setApproveStatus(dto.getApproveStatus());
        //任务设置状态
        setMyTaskStatus(dto.getMainId(), this.getMyTaskType(entity), "2", false);
        //发送消息
        this.sendMessageForType20(dto.getMainId(),dto.getAppOrgId(), entity.getCompanyUserId(), entity.getType(), dto.getAuditPerson(), "2");//推送拒绝的消息
        return expMainDao.updateById(entity);
    }

    private void sendMessageForType20(String mainId, String companyId, String companyUserId, Integer type, String accountId, String approveStatus) throws Exception {
        CompanyUserEntity companyUserEntity = this.companyUserDao.selectById(companyUserId);
        if (companyUserEntity != null) {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setTargetId(mainId);
            if (type == 1 && "1".equals(approveStatus)) {
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_22);
            }
            if (type == 1 && "2".equals(approveStatus)) {
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_20);
            }
            if (type == 1 && "3".equals(approveStatus)) {
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_221);
            }
            if (type == 2 && "1".equals(approveStatus)) {
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_224);
            }
            if (type == 2 && "2".equals(approveStatus)) {
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_223);
            }
            if (type == 2 && "3".equals(approveStatus)) {
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_225);
            }
            messageEntity.setCompanyId(companyUserEntity.getCompanyId());
            messageEntity.setUserId(companyUserEntity.getUserId());
            messageEntity.setSendCompanyId(companyId);
            messageEntity.setCreateBy(accountId);
            this.messageService.sendMessage(messageEntity);
        }
    }

    /**
     * 方法描述：报销详情
     * 作   者：LY
     * 日   期：2016/7/29 10:53
     */
    public ExpMainDTO getExpMainPageDetail(String id) throws Exception {
        ExpMainDTO dto = new ExpMainDTO();
        ExpMainEntity entity = expMainDao.selectById(id);

        BaseDTO.copyFields(entity, dto);
        ExpAuditDTO auditDto = expAuditDao.selectAuditPersonByMainId(id);
        dto.setAuditPerson(auditDto.getAuditPerson());
        dto.setAuditPersonName(auditDto.getAuditPersonName());
        List<ExpDetailDTO> list = expDetailDao.selectDetailDTOByMainId(id);
        dto.setDetailList(list);
        Map<String, Object> map = new HashMap<>();
        map.put("targetId", id);
        map.put("type", NetFileType.EXPENSE_ATTACH);
        List<ProjectSkyDriveEntity> expAttachEntityList = projectSkyDriverService.getNetFileByParam(map);
        dto.setExpAttachDTOList(BaseDTO.copyFields(expAttachEntityList, ExpAttachDTO.class));
        return dto;
    }

    /**
     * 方法描述：删除报销
     * 作   者：LY
     * 日   期：2016/7/29 10:53
     */
    public int deleteExpMain(String id, String versionNum) throws Exception {
        int i = recallExpMain(id, versionNum, "4");
        if (i > 0) {
            // 把相关的任务设置为无效
            this.myTaskService.ignoreMyTask(id);
            this.messageService.deleteMessage(id);
        }
        return i;
    }

    /**
     * 方法描述：同意报销
     * 作   者：LY
     * 日   期：2016/8/1 15:08
     */
    public int agreeExpMain(String id, String versionNum, String currentCompanyId, String currentUserId) throws Exception {
        //处理我的任务
        ExpMainEntity mainEntity = this.expMainDao.selectById(id);
        if (mainEntity != null) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("taskType", SystemParameters.EXP_AUDIT);
            if (mainEntity.getType() == 2) {
                param.put("taskType", SystemParameters.COST_AUDIT);
            }
            this.setMyTaskStatus(id, null, "1", false);
            //给报销人推送消息
            this.sendMessageForType20(id, currentCompanyId, mainEntity.getCompanyUserId(), mainEntity.getType(), currentUserId, "1");
        }
        mainEntity.setApproveStatus("1");
        if (versionNum != null) {
            mainEntity.setVersionNum(Integer.parseInt(versionNum));
        }
        ExpAuditEntity auditEntity = new ExpAuditEntity();
        auditEntity.setMainId(id);
        auditEntity.setApproveStatus("1");
        if (versionNum != null) {
            auditEntity.setVersionNum(Integer.parseInt(versionNum));
        }
        expAuditDao.updateByMainId(auditEntity);
        return expMainDao.updateById(mainEntity);
    }

    /**
     * 方法描述：同意报销并转移审批人
     * 作   者：LY
     * 日   期：2016/8/1 15:08
     *
     * @param id--报销单id auditPerson--新审批人  userId用户Id
     */
    public int agreeAndTransAuditPerExpMain(String id, String userId, String auditPerson, String versionNum, String currentCompanyId) throws Exception {
        //版本控制，只为了方便
        ExpMainEntity entityVersion = this.expMainDao.selectById(id);
        if (entityVersion == null) {
            return 0;
        }
        entityVersion.setApproveStatus("5");
        entityVersion.setVersionNum(Integer.parseInt(versionNum));
        int result = expMainDao.updateById(entityVersion);
        if (result == 0) {
            return result;
        }

        Integer taskType = this.getMyTaskType(entityVersion);
        //处理任务
        this.setMyTaskStatus(id, taskType, "1", false);

        //根据报销单id查询最新审批记录id
        String parentId = null;
        ExpAuditEntity auditEntities = expAuditDao.getLastAuditByMainId(id);
        if (auditEntities!=null) {
            parentId = auditEntities.getId();
        }

        //把当前自己的审批记录改为同意并且is_new为N
        ExpAuditEntity auditEntity = new ExpAuditEntity();
        auditEntity.setMainId(id);
        auditEntity.setApproveStatus("1");
        expAuditDao.transAuditPer(auditEntity);

        //插入新审批人审批记录
        auditEntity = new ExpAuditEntity();
        // approve_date audit_message
        auditEntity.setId(StringUtil.buildUUID());
        auditEntity.setIsNew("Y");
        auditEntity.setMainId(id);
        auditEntity.setParentId(parentId);
        auditEntity.setApproveStatus("5");
        auditEntity.setAuditPerson(auditPerson);
        auditEntity.set4Base(userId, userId, new Date(), new Date());
        expAuditDao.insert(auditEntity);

        //更新报销主表审批状态
        ExpMainEntity entity = this.expMainDao.selectById(id);
        //指派任务
        this.myTaskService.saveMyTask(entity.getId(), taskType, entity.getCompanyId(), auditPerson, userId, entity.getCompanyId(), true);

        //推送消息
        this.sendMessageForType20(id, currentCompanyId, auditPerson, entity.getType(), userId, "3");//同意并转交

        //修改状态
        entity.setApproveStatus("5");
        return expMainDao.updateById(entity);
    }

    /**
     * 财务拨款
     */
    @Override
    public int financialAllocation(String id, String currentCompanyUserId,String currentCompanyId, String accountId, Date d) throws Exception{

        //版本控制，只为了方便
        ExpMainEntity entity = this.expMainDao.selectById(id);
        if(entity == null){
            return 0;
        }
        //判断最低余额
        String amount = this.expMainDao.getTotalAmountByMainId(id);
        if(!companyBalanceService.isCanBeAllocate(entity.getCompanyId(),amount,DateUtils.formatDate(d==null?DateUtils.getDate():d))){
            //抛异常
            throw new CustomException("当前支出的金额不能大于账目余额与最低余额的差值");
        }
        entity.setApproveStatus("6");
        entity.setAllocationDate(new Date());
        entity.setAllocationUserId(currentCompanyUserId);
        entity.setUpdateBy(accountId);
        if (d != null) {
            entity.setAllocationDate(d);
        }
        int i= expMainDao.updateById(entity);
        //财务拨款
        if(i>0){
            i = this.financialAccount(entity);
        }
        if(i>0) {
            //推送抄送消息
            SendMessageDTO m = new SendMessageDTO();
            m.setTargetId(id);
            m.setCurrentCompanyId(currentCompanyId);
            m.setAccountId(accountId);
            int type = entity.getType();
            if (type == 1) {//
                m.setMessageType(SystemParameters.MESSAGE_TYPE_236);
            }
            if (type == 2) {//提交
                m.setMessageType(SystemParameters.MESSAGE_TYPE_237);
            }
            this.messageService.sendMessageForCopy(m);
        }
        return i;
    }

    @Override
    public int financialAllocation(String id, String currentCompanyUserId,String currentCompanyId, String accountId) throws Exception{
        return financialAllocation(id,currentCompanyUserId,currentCompanyId,accountId,null);
    }

    @Override
    public int financialRecallExpMain(FinancialAllocationDTO dto) throws Exception {
        String userId = dto.getAccountId();
        CompanyUserEntity currentUser = this.companyUserDao.getCompanyUserByUserIdAndCompanyId(dto.getAccountId(),dto.getCurrentCompanyId());
        if(currentUser==null){
            return 0;
        }
        ExpMainEntity main = this.expMainDao.selectById(dto.getId());
        if(main==null){
            return 0;
        }
        //获取最新的那条记录
        ExpAuditEntity lastAudit = this.expAuditDao.getLastAuditByMainId(dto.getId());
        //首先增加一条审批记录
        //保存报销审核表
        ExpAuditEntity auditEntity = new ExpAuditEntity();
        auditEntity.setId(StringUtil.buildUUID());
        auditEntity.setMainId(dto.getId());
        auditEntity.setApproveStatus("0");
        auditEntity.setAuditPerson(currentUser.getId());
        auditEntity.set4Base(userId, userId, new Date(), new Date());
        auditEntity.setIsNew("N");
        auditEntity.setApproveStatus("2");
        auditEntity.setAuditMessage(dto.getReason());
        auditEntity.setApproveDate(DateUtils.getDate());
        this.expAuditDao.insert(auditEntity);
        //再增加一条给上一个审批人的记录
        if(lastAudit!=null){
            //把原有的记录给设置N
            lastAudit.setIsNew("N");
            this.expAuditDao.updateById(lastAudit);
            lastAudit.initEntity();//重新插入一条新的记录
            lastAudit.setIsNew("Y");
            lastAudit.setApproveStatus("0");
            this.expAuditDao.insert(lastAudit);
            //激活原有的任务
            Map<String,Object> map = new HashMap<>();
            map.put("targetId",dto.getId());
            map.put("status","0");
            map.put("ignoreStatus","1");
            map.put("handlerId",lastAudit.getAuditPerson());
            map.put("myTaskType",this.getMyTaskType(main));
            this.myTaskService.updateStatesByTargetId(map);
        }
        main.setApproveStatus("5");
        return this.expMainDao.updateById(main);
    }

    private int financialAccount(ExpMainEntity entity) throws Exception{
        SaveCompanyBillDTO billDTO = new SaveCompanyBillDTO();
        billDTO.setCompanyId(entity.getCompanyId());
        billDTO.setFromCompanyId(entity.getCompanyId());
        if(entity.getType()==1){
            billDTO.setFeeType(CompanyBillType.FEE_TYPE_EXPENSE);
        }else {
            billDTO.setFeeType(CompanyBillType.FEE_TYPE_EXP_APPLY);
        }
        billDTO.setPayType(CompanyBillType.DIRECTION_PAYER);
        billDTO.setOperatorId(entity.getAllocationUserId());
        billDTO.setPaymentDate(DateUtils.formatDate(entity.getAllocationDate()));
        billDTO.setTargetId(entity.getId());
        return companyBillService.saveCompanyBill(billDTO);

        //通知抄送人
    }

    /**
     * 方法描述：报销详情与审批记录
     * 作   者：LY
     * 日   期：2016/8/2 14:13
     */
    public Map<String, Object> getExpMainDetail(String id) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<ExpDetailDTO> detailList = expDetailDao.selectDetailDTOByMainId(id);

        map.put("detailList", detailList);
        List<ExpMainDTO> auditList = new ArrayList<>();
        ExpMainDTO expMainDTO = expMainDao.selectByIdWithUserName(id);
        expMainDTO.setApproveStatusName("发起申请");
        auditList.add(expMainDTO);
        List<ExpMainDTO> list = expAuditDao.selectAuditDetailByMainId(id);
        for (ExpMainDTO dto : list) {
            if (!StringUtil.isNullOrEmpty(expMainDTO.getAllocationDate())) {
                dto.setIsNew("N");
            }
            dto.setApproveStatusName(getApproveStatusName(dto.getApproveStatus(), dto.getIsNew()));
        }
        //报销拨款
        if (!StringUtil.isNullOrEmpty(expMainDTO.getAllocationDate())) {
            ExpMainDTO allocation = new ExpMainDTO();
            allocation.setId(expMainDTO.getId());
            allocation.setApproveStatusName("财务拨款");
            allocation.setUserName(expMainDTO.getAllocationUserName());
            allocation.setExpDate(expMainDTO.getAllocationDate());
            allocation.setCompanyName(companyDao.getCompanyName(expMainDTO.getAllocationUserCompanyId()));
            allocation.setIsNew("Y");
            list.add(allocation);
        }

        Map<String, Object> param = new HashMap<>();
        param.put("targetId", id);
        param.put("type", NetFileType.EXPENSE_ATTACH);
        List<ProjectSkyDriveEntity> expAttachEntityList = projectSkyDriverService.getNetFileByParam(param);
        map.put("expAttachEntityList", BaseDTO.copyFields(expAttachEntityList, ExpAttachDTO.class));
        auditList.addAll(list);
        map.put("auditList", auditList);
        map.put("expNo", expMainDTO.getExpNo());
        map.put("versionNum", expMainDTO.getVersionNum());
        map.put("totalExpAmount", detailList.get(0).getTotalExpAmount());
        return map;
    }

    /**
     * 方法描述：状态码得到状态名称
     * 作   者：LY
     * 日   期：2016/8/2 15:42
     *
     * @param approveStatus 状态码
     * @return 审批状态(0:待审核，1:同意，2，退回, 3:撤回, 4:删除, 5.审批中）
     */
    private String getApproveStatusName(String approveStatus, String isNew) {
        if ("0".equals(approveStatus)) {
            return "待审核";
        } else if ("1".equals(approveStatus)) {
            if ("Y".equals(isNew)) {
                return "已同意(完成)";
            } else {
                return "已同意";
            }
        } else if ("2".equals(approveStatus)) {
            return "退回";
        } else if ("3".equals(approveStatus)) {
            return "撤回";
        } else if ("4".equals(approveStatus)) {
            return "删除";
        } else if ("5".equals(approveStatus)) {
            return "审批中";
        }
        return null;
    }


    /**
     * 方法描述：报销汇总数量
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     *
     * @param param 查询条件
     */
    public int getExpMainPageForSummaryCount(Map<String, Object> param) {
        return expMainDao.getExpMainPageForSummaryCount(param);
    }


    //====================================新接口2.0=====================================================

    /**
     * 方法描述：我的报销列表
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     *
     * @param param 查询条件
     */
    @Override
    public List<ExpMainDTO> getExpMainListPage(Map<String, Object> param) {
        if (null != param.get("pageIndex")) {
            int page = (Integer) param.get("pageIndex");
            int pageSize = (Integer) param.get("pageSize");
            param.put("startPage", page * pageSize);
            param.put("endPage", pageSize);
        }
        String approveStatus = (String) param.get("approveStatus");
        if ("1".equals(approveStatus)) {//此处查询已完成的和已拨款的
            param.remove("approveStatus");
            param.put("defaultApproveStatus", "('1','6')");
        }

        List<ExpMainDTO> list = expMainDao.getExpMainPage(param);
        return list;
    }

    @Override
    public int getExpMainListPageCount(Map<String, Object> param) {
        return expMainDao.getExpMainPageCount(param);
    }


    /**
     * 方法描述：报销汇总List
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     *
     * @param param 查询条件
     */
    @Override
    public List<ExpMainDTO> getExpMainListPageForSummary(Map<String, Object> param) {
        if (null != param.get("pageIndex")) {
            int page = (Integer) param.get("pageIndex");
            int pageSize = (Integer) param.get("pageSize");
            param.put("startPage", page * pageSize);
            param.put("endPage", pageSize);
        }

        List<ExpMainDTO> list = expMainDao.getExpMainPageForSummary(param);
        boolean isFinance = this.permissionService.isFinancial((String)param.get("currentCompanyId"),(String)param.get("accountId"));
        for(ExpMainDTO dto:list){
            if(isFinance && "1".equals(dto.getApproveStatus())){
                dto.getRole().setFinancialAllocation(1);
                dto.getRole().setFinancialRecall(1);
            }
        }
        return list;
    }

    /**
     * 方法描述：报销汇总数量
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     *
     * @param param 查询条件
     */
    @Override
    public int getExpMainListPageForSummaryCount(Map<String, Object> param) {
        return expMainDao.getExpMainPageForSummaryCount(param);
    }

    /**
     * 方法描述：报销汇总金额
     * 作   者：ZCL
     * 日   期：2016/7/28 16:34
     *
     * @param param 查询条件
     */
    @Override
    public ExpSummaryDTO getExpMainSummary(Map<String, Object> param) {
        ExpSummaryDTO summary = expMainDao.getExpMainSummary(param);
        List<CompanyRelationDTO> filterCompanyList = expMainDao.listExpFilterCompany(param);
        if (summary != null) {
            summary.setCompanyList(filterCompanyList);
        }
        return summary;
    }

    /**
     * 方法描述：获取最大组织expNo + 1
     * 作   者：ZhujieChen
     * 日   期：2016/12/22
     */
    public Map<String, Object> getMaxExpNo(Map<String, Object> param) throws Exception {

        boolean flag = true;
        while (flag) {
            int num = new Random().nextInt(999) + 1 + 1000;
            String expNo = DateUtils.date2Str(DateUtils.yyyymmddhhmmss);
            expNo = expNo + num;
            param.put("expNo", expNo);
            List<ExpMainEntity> list = expMainDao.selectByParam(param);
            if (list.size() > 0) {
                flag = true;
            } else {
                flag = false;
            }
        }
        param.put("maxExpNo", param.get("expNo"));
        return param;
    }

    /**
     * 方法描述：项目收支明细-台账模块获取费用申请，报销申请
     * 作   者：DongLiu
     * 日   期：2017/12/6 18:36
     */
    @Override
    public StatisticDetailSummaryDTO getExpenditureCount(StatisticDetailQueryDTO param) {
        return expMainDao.getExpenditureCount(param);
    }

    @Override
    public List<LeaveDetailDTO> getLeaveDetailList(LeaveDetailQueryDTO queryDTO) {
        return expMainDao.getLeaveDetailList(queryDTO);
    }

    @Override
    public Integer getLeaveDetailCount(LeaveDetailQueryDTO queryDTO) {
        return expMainDao.getLeaveDetailCount(queryDTO);
    }

    /**
     * 请假任务详情
     */
    @Override
    public Map<String, Object> getLeaveDetail(LeaveDetailQueryDTO queryDTO) {
        Map<String, Object> map = new HashMap<>();
        List<ExpMainDTO> expMainDTOS = new ArrayList<ExpMainDTO>();
        ExpMainDTO expMainDTO = new ExpMainDTO();
        expMainDTO = expMainDao.selectByIdWithUserName(queryDTO.getId());
        expMainDTO.setApproveStatusName("发起申请");
        expMainDTOS.add(expMainDTO);
        List<ExpAuditEntity> expAuditEntities = expAuditDao.selectDetailByMainId(queryDTO.getId());
        for (ExpAuditEntity entity : expAuditEntities) {
            expMainDTO = new ExpMainDTO();
            if ("N".equals(entity.getIsNew())) {
                expMainDTO.setApproveStatusName("审核通过");
            } else if ("Y".equals(entity.getIsNew())) {
                expMainDTO.setApproveStatusName("审核完成");
            }
            expMainDTO.setUserName(entity.getAuditPerson());
            expMainDTO.setExpDate(entity.getApproveDate());
            expMainDTOS.add(expMainDTO);
        }
        map.put("expAuditEntities", expMainDTOS);

        return map;
    }
}

