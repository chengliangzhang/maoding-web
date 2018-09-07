package com.maoding.financial.service.impl;

import com.maoding.attach.dto.FileDataDTO;
import com.maoding.commonModule.dto.QueryCopyRecordDTO;
import com.maoding.commonModule.dto.SaveCopyRecordDTO;
import com.maoding.commonModule.service.CopyRecordService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.CopyTargetType;
import com.maoding.core.constant.NetFileType;
import com.maoding.core.constant.ProcessTypeConst;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.StringUtils;
import com.maoding.financial.dao.ExpAuditDao;
import com.maoding.financial.dao.ExpMainDao;
import com.maoding.financial.dao.LeaveDetailDao;
import com.maoding.financial.dto.AuditDTO;
import com.maoding.financial.dto.AuditEditDTO;
import com.maoding.financial.dto.LeaveDTO;
import com.maoding.financial.dto.SaveLeaveDTO;
import com.maoding.financial.entity.ExpMainEntity;
import com.maoding.financial.entity.LeaveDetailEntity;
import com.maoding.financial.service.ExpMainService;
import com.maoding.financial.service.LeaveService;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.process.dto.ActivitiDTO;
import com.maoding.process.service.ProcessService;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.user.service.UserAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("leaveService")
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private ExpMainDao expMainDao;

    @Autowired
    private LeaveDetailDao leaveDetailDao;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private ExpAuditDao expAuditDao;

    @Autowired
    private UserAttachService userAttachService;

    @Autowired
    private ExpMainService expMainService;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private CopyRecordService copyRecordService;

    @Autowired
    private ProcessService processService;

    AjaxMessage validateSaveLeave(SaveLeaveDTO dto){
//        if (StringUtil.isNullOrEmpty(dto.getAuditPerson())) {
//            return ResponseBean.responseError("审批人不能为空");
//        }
        if (!StringUtil.isNullOrEmpty(dto.getRemark()) && dto.getRemark().length() > 255) {
            return AjaxMessage.failed("备注长度过长");
        }
        if (StringUtil.isNullOrEmpty(dto.getLeaveStartTime()) || StringUtil.isNullOrEmpty(dto.getLeaveEndTime())) {
            return AjaxMessage.failed("时间不能为空");
        }
        return null;
    }

    @Override
    public AjaxMessage saveLeave(SaveLeaveDTO dto) throws Exception {
        if (StringUtils.isEmpty(dto.getCompanyUserId())){
            dto.setCompanyUserId(dto.getCurrentCompanyUserId());
        }

        AjaxMessage ajaxMessage = validateSaveLeave(dto);
        if(ajaxMessage!=null){
            return ajaxMessage;
        }
        String companyId = dto.getAppOrgId();
        String userId = dto.getAccountId();
        if(StringUtil.isNullOrEmpty(dto.getCompanyId())){
            dto.setCompanyId(companyId);
        }
        boolean flag = false;
        //保存报销主表
        ExpMainEntity entity = new ExpMainEntity();
        BaseDTO.copyFields(dto, entity);
        entity.setApproveStatus("0");
        if (StringUtil.isNullOrEmpty(dto.getId())) {//插入
            saveExpMain(entity,dto);
            flag = true;
        } else {//保存
            ExpMainEntity exp = expMainDao.selectById(dto.getId());
            if(exp!=null && ("2".equals(exp.getApproveStatus()) || "3".equals(exp.getApproveStatus()))){
                exp.setExpFlag(1);
                entity.setExpFlag(2);
                expMainDao.updateById(exp);
                //新增
                saveExpMain(entity,dto);
                //复制原来的附件记录
                projectSkyDriverService.copyFileToNewObject(entity.getId(),dto.getId(), NetFileType.EXPENSE_ATTACH,dto.getDeleteAttachList());
                flag = true;
            }else {
                entity.set4Base(null, userId, null, new Date());
                //版本控制
                int result = expMainDao.updateById(entity);
                //保存报销明细表
                this.saveLeaveDetail(dto,entity.getId());
                if (result == 0) {
                    return AjaxMessage.failed("保存失败");
                }
            }
        }
        //主表Id
        String id = entity.getId();
        //处理图片
        if(!CollectionUtils.isEmpty(dto.getDeleteAttachList())){
            projectSkyDriverService.deleteSysDrive(dto.getDeleteAttachList(),dto.getAccountId(),id);
        }
        //    Integer myTaskType = this.getMyTaskType(entity);


        //处理抄送
        SaveCopyRecordDTO copyDTO = new SaveCopyRecordDTO();
        copyDTO.setCompanyUserList(dto.getCcCompanyUserList());
        copyDTO.setSendCompanyUserId(companyUserDao.getUserNameByCompanyIdAndUserId(dto.getAppOrgId(),dto.getAccountId()));
        copyDTO.setOperateRecordId(id);
        copyDTO.setTargetId(id);
        copyDTO.setRecordType(CopyTargetType.EXP_MAIN);
        copyRecordService.saveCopyRecode(copyDTO);
//        for(String companyUserId:dto.getCcCompanyUserList()){
//            this.expMainService.sendMessageForAudit(entity.getId(),companyId,companyUserId,entity.getType(),dto.getAccountId(),null,"7");//抄送
//        }
        return AjaxMessage.succeed("保存成功");
    }


    /**
     * 方法描述：报销详情与审批记录
     * 作   者：LY
     * 日   期：2016/8/2 14:13
     */
    @Override
    public LeaveDTO getLeaveDetail(String id) throws Exception {
        LeaveDTO result = leaveDetailDao.getLeaveById(id);
        if(result==null){
            return new LeaveDTO();
        }
        Map<String,Object> param = new HashMap<>();
        param.put("id",id);
        List<AuditDTO> auditList = expAuditDao.selectAuditByMainId(param);
        result.setAuditList(auditList);
        if(!CollectionUtils.isEmpty(auditList)){
            if("2".equals(result.getApproveStatus())){//如果是退回，把退回原因提取到最外层，以便前端展示
                result.setCallbackReason(auditList.get(auditList.size()-1).getAuditMessage());
            }
            //获取审核人
            AuditDTO audit = auditList.get(0);
            result.setAuditPersonName(audit.getUserName());
            result.setAuditPerson(audit.getCompanyUserId());

            //copy一份最后审批人到到你跟前审批人上
            AuditDTO currentAuditPerson = new AuditDTO();
            BaseDTO.copyFields(auditList.get(auditList.size()-1),currentAuditPerson);
            result.setCurrentAuditPerson(currentAuditPerson);
        }
        //发起申请的记录
        AuditDTO applyDTO = new AuditDTO();
        applyDTO.setUserName(result.getUserName());
        applyDTO.setCompanyUserId(result.getCompanyUserId());
        applyDTO.setApproveDate(result.getExpDate());
        if("3".equals(result.getApproveStatus())){//撤销
            applyDTO.setApproveStatus(result.getApproveStatus());
        }else {
            applyDTO.setApproveStatus(null);//此处设置为null，为了防止ApproveStatusName 在dto的get方法中发生改变
        }
        applyDTO.setApproveStatusName("发起申请");
        applyDTO.setFileFullPath(userAttachService.getHeadImgNotFullPath(result.getAccountId()));
        auditList.add(0,applyDTO);

        param.clear();
        param.put("targetId", id);
        param.put("type", NetFileType.EXPENSE_ATTACH);
        List<FileDataDTO> expAttachList = this.projectSkyDriverService.getAttachDataList(param);
        result.setAttachList(expAttachList);

        //获取抄送人
        result.setCcCompanyUserList(copyRecordService.getCopyRecode(new QueryCopyRecordDTO(id)));
        //返回流程标识，给前端控制是否要给审批人，以及按钮显示的控制
        Map<String,Object> processData = processService.getCurrentTaskUser(new AuditEditDTO(id,null,null),auditList,result.getLeaveTime());
        result.setProcessFlag(processData.get("processFlag"));
        result.setProcessType(processData.get("processType"));
        result.setConditionList(processData.get("conditionList"));
        return result;
    }

    private ExpMainEntity saveExpMain(ExpMainEntity entity, SaveLeaveDTO dto) throws Exception{
        String companyId = dto.getAppOrgId();
        String userId = dto.getAccountId();
        String expNo = this.getExpNo(companyId);
        entity.setExpFlag(0);
        if(StringUtil.isNullOrEmpty(dto.getTargetId())){
            entity.setId(StringUtil.buildUUID());
        }else {
            entity.setId(dto.getTargetId());
        }
        dto.setExpNo(expNo);
        if(StringUtil.isNullOrEmpty(dto.getType())){
            entity.setType(1); //默认为报销费用
        }
        entity.setExpNo(expNo);
        entity.set4Base(userId, userId, new Date(), new Date());
        entity.setCompanyId(companyId);
        expMainDao.insert(entity);
        this.saveLeaveDetail(dto,entity.getId());
        // 启动流程
        String targetType = null;
        if(entity.getType()==3){
            targetType = ProcessTypeConst.PROCESS_TYPE_LEAVE;
        }else {
            targetType= ProcessTypeConst.PROCESS_TYPE_ON_BUSINESS;
        }
        ActivitiDTO activitiDTO = new ActivitiDTO(entity.getId(),entity.getCompanyUserId(),companyId,userId,dto.getLeaveTime(),targetType);
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


    private void saveLeaveDetail(SaveLeaveDTO dto,String mainId) throws Exception {
        //查询是否存在明细，目前此处是1对1关联
        LeaveDetailEntity leaveDetail = this.leaveDetailDao.getLeaveDetailByMainId(mainId);
        if(leaveDetail==null){
            leaveDetail = new LeaveDetailEntity();
            BaseDTO.copyFields(dto,leaveDetail);
            leaveDetail.initEntity();
            leaveDetail.setMainId(mainId);
            leaveDetail.setTimeUnit(1);//默认为天
            leaveDetail.setCreateBy(dto.getAccountId());
            if(dto.getType()==4){
                leaveDetail.setLeaveType("10");//出差
            }
            this.leaveDetailDao.insert(leaveDetail);
        }else {
            String id = leaveDetail.getId();
            BaseDTO.copyFields(dto,leaveDetail);
            leaveDetail.setId(id);
            leaveDetail.setMainId(mainId);
            leaveDetail.setUpdateBy(dto.getAccountId());
            this.leaveDetailDao.updateById(leaveDetail);
        }
    }


    private Integer getMyTaskType(ExpMainEntity entity){
        if(entity.getType()==3){
            return SystemParameters.LEAVE_AUDIT;
        }
        if(entity.getType()==4){
            return SystemParameters.EVECTION_AUDIT;
        }
        return SystemParameters.EXP_AUDIT;
    }
}
