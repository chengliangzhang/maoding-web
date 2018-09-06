package com.maoding.projectcost.service.impl;

import com.maoding.commonModule.entity.RelationRecordEntity;
import com.maoding.commonModule.service.RelationRecordService;
import com.maoding.companybill.dto.SaveCompanyBillDTO;
import com.maoding.companybill.service.CompanyBalanceService;
import com.maoding.companybill.service.CompanyBillService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.CompanyBillType;
import com.maoding.core.constant.ProcessTypeConst;
import com.maoding.core.constant.ProjectCostConst;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.*;
import com.maoding.dynamic.dao.ZInfoDAO;
import com.maoding.dynamic.service.DynamicService;
import com.maoding.enterprise.dto.EnterpriseDTO;
import com.maoding.enterprise.service.EnterpriseService;
import com.maoding.exception.CustomException;
import com.maoding.financial.dao.ExpMainDao;
import com.maoding.financial.dto.ApplyProjectCostDTO;
import com.maoding.financial.dto.ExpMainDTO;
import com.maoding.financial.service.ExpMainService;
import com.maoding.invoice.dto.InvoiceEditDTO;
import com.maoding.invoice.service.InvoiceService;
import com.maoding.message.entity.MessageEntity;
import com.maoding.message.service.MessageService;
import com.maoding.mytask.entity.MyTaskEntity;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dto.CompanyRelationDTO;
import com.maoding.org.dto.CompanyUserTableDTO;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.CompanyUserService;
import com.maoding.process.dto.ActivitiDTO;
import com.maoding.process.service.ProcessService;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.dto.ProjectSimpleDTO;
import com.maoding.project.dto.QueryProjectDTO;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.projectcost.dao.*;
import com.maoding.projectcost.dto.*;
import com.maoding.projectcost.entity.*;
import com.maoding.projectcost.service.ProjectCostService;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.projectmember.service.ProjectMemberService;
import com.maoding.role.service.PermissionService;
import com.maoding.projectcost.dto.ProjectCostQueryDTO;
import com.maoding.task.dao.ProjectTaskDao;
import com.maoding.task.dao.ProjectTaskRelationDao;
import com.maoding.task.entity.ProjectTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostService
 * 类描述：费用service
 * 作    者：MaoSF
 * 日    期：2016年7月19日-下午5:28:54
 */
@Service("projectCostService")
public class ProjectCostServiceImpl extends GenericService<ProjectCostEntity> implements ProjectCostService {
    @Autowired
    private ProjectCostDao projectCostDao;

    @Autowired
    private ProjectCostPaymentDetailDao projectCostPaymentDetailDao;

    @Autowired
    private ProjectCostOperaterDao projectCostOperaterDao;

    @Autowired
    private ProjectCostPointDao projectCostPointDao;

    @Autowired
    private ProjectCostPointDetailDao projectCostPointDetailDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ProjectTaskRelationDao projectTaskRelationDao;

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private ProjectTaskDao projectTaskDao;

    @Autowired
    private ZInfoDAO zInfoDAO;

    @Autowired
    private CompanyBillService companyBillService;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private CompanyBalanceService companyBalanceService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ExpMainService expMainService;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private RelationRecordService relationRecordService;

    @Autowired
    private ExpMainDao expMainDao;

    /**
     * 方法描述：设置合同总金额/技术审查费
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    @Override
    public AjaxMessage saveOrUpdateProjectCost(ProjectCostEditDTO projectCostDto) throws Exception {
        ProjectCostEntity entity = new ProjectCostEntity();
        BaseDTO.copyFields(projectCostDto, entity);
        //类型1:合同总金额，2：技术审查费,3合作设计费付款 (字符串)，
        entity.setOperateCompanyId(projectCostDto.getCurrentCompanyId());
        if(projectCostDto.getPayType()==CompanyBillType.DIRECTION_PAYEE){//如果是收款方
            entity.setToCompanyId(projectCostDto.getCurrentCompanyId());
        }else {
            entity.setFromCompanyId(projectCostDto.getCurrentCompanyId());
        }
//        if ("2".equals(projectCostDto.getType())) {
//            ProjectEntity project = projectDao.selectById(projectCostDto.getProjectId());
//            entity.setFromCompanyId(project.getCompanyId());
//            entity.setToCompanyId(project.getCompanyBid());
//        }
//
//        if ("1".equals(projectCostDto.getType())) {
//            ProjectEntity project = projectDao.selectById(projectCostDto.getProjectId());
//            entity.setToCompanyId(project.getCompanyId());
//        }
        //新增
        if (StringUtil.isNullOrEmpty(projectCostDto.getId())) {
            if(StringUtil.isNullOrEmpty(projectCostDto.getFlag())){
                entity.setFlag("1");
            }
            entity.setId(StringUtil.buildUUID());
            entity.setCreateBy(projectCostDto.getAccountId());
            projectCostDao.insert(entity);
            //添加项目动态
            dynamicService.addDynamic(null,entity,projectCostDto.getCurrentCompanyId(),projectCostDto.getAccountId());
        } else {
            updateCostFee(projectCostDto);
        }
        //处理附件
        projectCostDto.setId(entity.getId());
        projectSkyDriverService.saveProjectFeeContractAttach(projectCostDto);
        return AjaxMessage.succeed("操作成功");
    }

    private void updateCostFee(ProjectCostEditDTO projectCostDto) throws Exception {
        ProjectCostEntity entity = this.selectById(projectCostDto.getId());
        ProjectCostEntity origin = new ProjectCostEntity();
        BeanUtilsEx.copyProperties(entity,origin);
        entity.setFee(projectCostDto.getFee());
        this.projectCostDao.updateById(entity);
        //添加项目动态
        dynamicService.addDynamic(origin,entity,projectCostDto.getCurrentCompanyId(),projectCostDto.getAccountId());
        //更新子节点的fee

        if ("1".equals(projectCostDto.getType())  && projectCostDto.getFee()!=null) {
            this.updateContractFee(projectCostDto.getProjectId(), projectCostDto.getFee(),entity.getFlag());
        }

        if ("2".equals(projectCostDto.getType()) && projectCostDto.getFee()!=null) {
            this.updateTechnicalFee(projectCostDto.getProjectId(), projectCostDto.getFee(),entity.getFlag());
        }

        if ("3".equals(projectCostDto.getType())  && projectCostDto.getFee()!=null) {
            this.updateDesignFee(projectCostDto.getId(), projectCostDto.getFee(),entity.getFlag());
        }

    }

    private void updateContractFee(String projectId, BigDecimal amount,String flag) throws Exception {
        //1.查询子节点
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("projectId", projectId);
        map.put("type", "1");
        map.put("flag", flag);
        List<ProjectCostPointDTO> list = projectCostPointDao.selectByParam(map);
        if (!CollectionUtils.isEmpty(list)) {
            for (ProjectCostPointDTO dto : list) {
                ProjectCostPointEntity entity = new ProjectCostPointEntity();
                entity.setId(dto.getId());
                if (!StringUtil.isNullOrEmpty(dto.getFeeProportion())) {
                    setProjectCostFee(dto, amount);
                }
            }
        }
    }


    private void updateTechnicalFee(String projectId, BigDecimal amount,String flag) throws Exception {
        //1.查询子节点
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("projectId", projectId);
        map.put("type", "2");
        map.put("pidIsNull", "1");//标示，只查父节点
        map.put("flag", flag);//标示
        List<ProjectCostPointDTO> list = projectCostPointDao.selectByParam(map);
        if (!CollectionUtils.isEmpty(list)) {
            for (ProjectCostPointDTO dto : list) {
                if (!StringUtil.isNullOrEmpty(dto.getFeeProportion())) {
                    setProjectCostFee(dto, amount);
                    //查询子节点
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("type", dto.getType());
                    param.put("pid", dto.getId());
                    List<ProjectCostPointDTO> childList = this.projectCostPointDao.selectByParam(param);
                    for (ProjectCostPointDTO dto1 : childList) {
                        if (!StringUtil.isNullOrEmpty(dto.getFeeProportion())) {
                            setProjectCostFee(dto1, dto.getFee());
                        }
                    }
                }
            }
        }
    }

    private void updateDesignFee(String costId, BigDecimal amount,String flag) throws Exception {
        //1.查询子节点
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("costId", costId);
        map.put("type", "3");
        map.put("pidIsNull", "1");//标示，只查父节点
        map.put("flag", flag);//标示
        List<ProjectCostPointDTO> list = projectCostPointDao.selectByParam(map);
        if (!CollectionUtils.isEmpty(list)) {
            for (ProjectCostPointDTO dto : list) {
                if (!StringUtil.isNullOrEmpty(dto.getFeeProportion())) {
                    setProjectCostFee(dto, amount);
                    //查询子节点
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("type", dto.getType());
                    param.put("pid", dto.getId());
                    List<ProjectCostPointDTO> childList = this.projectCostPointDao.selectByParam(param);
                    for (ProjectCostPointDTO dto1 : childList) {
                        if (!StringUtil.isNullOrEmpty(dto1.getFeeProportion())) {
                            setProjectCostFee(dto1, dto.getFee());
                        }
                    }
                }
            }
        }
    }

    private void setProjectCostFee(ProjectCostPointDTO dto, BigDecimal amount) {
        if(amount!=null){
            ProjectCostPointEntity entity = new ProjectCostPointEntity();
            entity.setId(dto.getId());
            double proportion = Double.parseDouble(dto.getFeeProportion());
            BigDecimal decimalProprotion = new BigDecimal(proportion / 100);
            entity.setFee(amount.multiply(decimalProprotion));
            dto.setFee(amount.multiply(decimalProprotion));
            this.projectCostPointDao.updateById(entity);
        }
    }

    /**
     * 方法描述：添加修改费用节点（如果是合作设计费。costId必须传递）  //类型1:合同总金额，2：技术审查费,3合作设计费付款 (字符串)，4.其他费用（付款），5.其他费用（收款）
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    @Override
    public AjaxMessage saveOrUpdateProjectCostPoint(ProjectCostPointDTO projectCostPointDTO) throws Exception {

        //漏验证
        AjaxMessage ajaxMessage = this.validateTechnicalFee(projectCostPointDTO);
        if ("1".equals(ajaxMessage.getCode())) {
            return ajaxMessage;
        }
        ProjectCostPointEntity entity = new ProjectCostPointEntity();
        BaseDTO.copyFields(projectCostPointDTO, entity);
        ProjectCostEntity projectCost = this.projectCostDao.selectById(projectCostPointDTO.getCostId());
        //新增
        if (StringUtil.isNullOrEmpty(projectCostPointDTO.getId())) {
            if(StringUtil.isNullOrEmpty(projectCostPointDTO.getFlag())){
                entity.setFlag("1");
            }
            entity.setId(StringUtil.buildUUID());
            entity.setCreateBy(projectCostPointDTO.getAccountId());
            projectCostPointDao.insert(entity);

            //添加项目动态
            dynamicService.addDynamic(null,entity,projectCostPointDTO.getCurrentCompanyId(),projectCostPointDTO.getAccountId());
        } else {
            ProjectCostPointEntity origin = projectCostPointDao.selectById(entity.getId());//保留修改前的数据
            projectCostPointDao.updateById(entity);
            //添加项目动态
            dynamicService.addDynamic(origin,entity,projectCostPointDTO.getCurrentCompanyId(),projectCostPointDTO.getAccountId());
            //查询子节点
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("type", projectCostPointDTO.getType());
            param.put("pid", projectCostPointDTO.getId());
            List<ProjectCostPointDTO> childList = this.projectCostPointDao.selectByParam(param);
            for (ProjectCostPointDTO dto1 : childList) {
                if (!StringUtil.isNullOrEmpty(dto1.getFeeProportion())) {
                    setProjectCostFee(dto1, projectCostPointDTO.getFee());
                }
            }
        }
        return AjaxMessage.succeed("操作成功");
    }

    //4.其他费用（付款），5.其他费用（收款）
    @Override
    public AjaxMessage saveOtherProjectCostPoint(ProjectCostPointDTO projectCostPointDTO) throws Exception {
        ProjectCostPointEntity entity = new ProjectCostPointEntity();
        BaseDTO.copyFields(projectCostPointDTO, entity);
        //新增
        if (StringUtil.isNullOrEmpty(projectCostPointDTO.getId())) {
            //无需做总金额校验
            //查看是否存在ProjectCost数据。此数据仅此用于关联一个costId，用于区分该数据属于哪个公司
            //查询总费用
            String costId=null;
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("projectId",projectCostPointDTO.getProjectId());
            if("4".equals(projectCostPointDTO.getType())){
                //查询是否存在
                map.put("type","4");
                map.put("fromCompanyId",projectCostPointDTO.getCurrentCompanyId());

            }else {
                map.put("type","5");
                map.put("toCompanyId",projectCostPointDTO.getCurrentCompanyId());
            }
            List<ProjectCostDTO> totalCost = this.projectCostDao.selectByParam(map);//理论上只会存在一条
            if(!CollectionUtils.isEmpty(totalCost)){
                costId = totalCost.get(0).getId();
            }else {//如果不存在
                costId = this.saveProjectCostForOther(projectCostPointDTO);
            }
            //后台数据统一，flag=1：代表正式合同（实际上其他费用没有什么正式合同与附加协议）
            if(StringUtil.isNullOrEmpty(projectCostPointDTO.getFlag())){
                entity.setFlag("1");
            }
            entity.setCostId(costId);
            entity.setId(StringUtil.buildUUID());
            entity.setCreateBy(projectCostPointDTO.getAccountId());
            projectCostPointDao.insert(entity);
            //添加项目动态
            dynamicService.addDynamic(null,entity,projectCostPointDTO.getCurrentCompanyId(),projectCostPointDTO.getAccountId());
        } else {
            ProjectCostPointEntity origin = projectCostPointDao.selectById(entity.getId()); //保留更改前的数据
            projectCostPointDao.updateById(entity);
            //添加项目动态
            dynamicService.addDynamic(origin,entity,projectCostPointDTO.getCurrentCompanyId(),projectCostPointDTO.getAccountId());
        }

        return AjaxMessage.succeed("操作成功");
    }

    private String saveProjectCostForOther(ProjectCostPointDTO projectCostPointDTO){
       String costId = StringUtil.buildUUID();
        ProjectCostEntity costEntity = new ProjectCostEntity();
        costEntity.setId(costId);
        costEntity.setProjectId(projectCostPointDTO.getProjectId());
        if("4".equals(projectCostPointDTO.getType())){
            costEntity.setFromCompanyId(projectCostPointDTO.getCurrentCompanyId());
        }else {
            costEntity.setToCompanyId(projectCostPointDTO.getCurrentCompanyId());
        }
        costEntity.setType(projectCostPointDTO.getType());
        this.projectCostDao.insert(costEntity);

        return costId;
    }

    private AjaxMessage validateReturnMoneyDetail(ProjectCostPointDetailDTO dto) throws Exception {

        ProjectCostPointEntity pointEntity = this.projectCostPointDao.selectById(dto.getPointId());
        if (pointEntity == null) {
            return AjaxMessage.failed("操作失败");
        }
        if (null == pointEntity.getFee() || pointEntity.getFee().compareTo(new BigDecimal("0"))==0) {
            return AjaxMessage.failed("请先设置总金额");
        }

        //验证发起收款的金额不能大于节点的金额
        String typeMemo=getTypeMemo(pointEntity.getType());
        ProjectCostPointDetailEntity detailEntity = null;
        if (!StringUtil.isNullOrEmpty(dto.getFee())) {
            BigDecimal originalFee = new BigDecimal("0");
            double sumReturnFee = this.projectCostPointDetailDao.getSumFee(pointEntity.getId());
            if (!StringUtil.isNullOrEmpty(dto.getId())) {
                detailEntity = this.projectCostPointDetailDao.selectById(dto.getId());
                if(null!=detailEntity){
                    originalFee = detailEntity.getFee();
                }
            }
            if (CommonUtil.doubleCompare(dto.getFee().doubleValue() + sumReturnFee - originalFee.doubleValue(), pointEntity.getFee().doubleValue()) > 0) {
                return AjaxMessage.failed(typeMemo+"大于"+ StringUtil.getRealData(pointEntity.getFee()));
            }
        }

        //如果是修改，则验证修改的金额不能小于已经收款的金额
        if(detailEntity!=null && !StringUtil.isNullOrEmpty(dto.getFee())){
            double sumPaymentFee= this.projectCostPaymentDetailDao.getSumFee(detailEntity.getId());
            if (CommonUtil.doubleCompare(sumPaymentFee, dto.getFee().doubleValue()) > 0) {
                //new BigDecimal(sumPaymentFee+"")因为double会出现精度问题。所以先变成字符串
                return AjaxMessage.failed(typeMemo+"小于"+ StringUtil.getRealData(new BigDecimal(sumPaymentFee+"")));
            }
        }

        return null;
    }

    private String getTypeMemo(String type){
        switch (type){
            case "1":
                return "发起回款总金额不能";
            case "2":
                return "发起收款总金额不能";
            case "3":
                return "发起收款总金额不能";
            case "4":
                return "发起付款总金额不能";
            case "5":
                return "发起收款总金额不能";
        }
        return "";
    }

    /**
     * 方法描述：其他费用收款付款
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    @Override
    public AjaxMessage saveOtherCostDetail(ProjectCostPointDetailDTO projectCostPointDetailDTO) throws Exception {
        ProjectCostPointDetailEntity entity = this.projectCostPointDetailDao.selectById(projectCostPointDetailDTO.getId());
        //保留原有数据
        ProjectCostPointDetailEntity origin = new ProjectCostPointDetailEntity();
        BeanUtilsEx.copyProperties(entity,origin);
        entity.setFee(entity.getFee());
        projectCostPointDetailDTO.setFee(entity.getFee());
        projectCostPointDetailDao.updateById(entity);
        //保存项目动态
        dynamicService.addDynamic(origin,entity,projectCostPointDetailDTO.getCurrentCompanyId(),projectCostPointDetailDTO.getAccountId());
        return AjaxMessage.succeed("操作成功");
    }


    private void sendMyTaskForReturnMoney(String costDetailId, ProjectCostPointDetailDTO dto) throws Exception {
        String type = "";
        String companyId = "";
        //推送任务 其他费用付款收款:|| "4".equals(pointEntity.getType()) || "5".equals(pointEntity.getType()) 不需要
        if("1".equals(dto.getIsInvoice())){//代表开发票
            //增加财务对开票信息的确认
            type = "2";
            this.myTaskService.saveMyTask(costDetailId,SystemParameters.INVOICE_FINN_IN_FOR_PAID ,dto.getCurrentCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
        }else {
            ProjectCostPointEntity pointEntity = this.projectCostPointDao.selectById(dto.getPointId());
            if ("1".equals(pointEntity.getType()))//合同回款，
            {
                type = "2";
                this.myTaskService.saveMyTask(costDetailId,SystemParameters.CONTRACT_FEE_PAYMENT_CONFIRM ,dto.getCurrentCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
            }
            if ("2".equals(pointEntity.getType())) {//技术审查费
                type = "1";
                //给立项组织发起确认信息
                ProjectEntity projectEntity = this.projectDao.selectById(pointEntity.getProjectId());
                this.myTaskService.saveMyTask(costDetailId, SystemParameters.TECHNICAL_REVIEW_FEE_OPERATOR_MANAGER, projectEntity.getCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
            }
            if ("3".equals(pointEntity.getType())) {//合作设计费
                type = "1";
                //给发包人发起确认信息
                ProjectCostEntity costEntity = this.projectCostDao.selectById(pointEntity.getCostId());
                this.myTaskService.saveMyTask(costDetailId, SystemParameters.COOPERATIVE_DESIGN_FEE_ORG_MANAGER, costEntity.getFromCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
            }

            if ("4".equals(pointEntity.getType()))//其他费用付款
            {
                type = "2";
                this.myTaskService.saveMyTask(costDetailId, SystemParameters.OTHER_FEE_FOR_PAY, dto.getCurrentCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
            }
            if ("5".equals(pointEntity.getType()))//其他费用收款
            {
                type = "2";
                this.myTaskService.saveMyTask(costDetailId, SystemParameters.OTHER_FEE_FOR_PAID, dto.getCurrentCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
            }
        }
    }

    private void sendMyTask(String costDetailId, ProjectCostPointDetailDTO dto) throws Exception {
        if("1".equals(dto.getIsInvoice())){//代表开发票
            //增加财务对开票信息的确认
            this.myTaskService.saveMyTask(costDetailId,SystemParameters.INVOICE_FINN_IN_FOR_PAID ,dto.getCurrentCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
        }else {
            ProjectCostPointEntity pointEntity = this.projectCostPointDao.selectById(dto.getPointId());
            ProjectCostEntity costEntity = this.projectCostDao.selectById(pointEntity.getCostId());
            if ("1".equals(costEntity.getType()))//合同回款，
            {
                this.myTaskService.saveMyTask(costDetailId,SystemParameters.CONTRACT_FEE_PAYMENT_CONFIRM ,dto.getCurrentCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
            }
            if ("2".equals(costEntity.getType())) {//技术审查费
                int taskType = 0;
                if(dto.getCurrentCompanyId().equals(costEntity.getFromCompanyId())){//付款组织
                    taskType = SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAY_2;
                }
                if(dto.getCurrentCompanyId().equals(costEntity.getToCompanyId())){
                    taskType = SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAID_2;
                }
                if(taskType>0){
                    this.myTaskService.saveMyTask(costDetailId, taskType, dto.getCurrentCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
                }
            }
            if ("3".equals(costEntity.getType())) {//合作设计费
                int taskType = 0;
                if(dto.getCurrentCompanyId().equals(costEntity.getFromCompanyId())){//付款组织
                    taskType = SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAY_2;
                }
                if(dto.getCurrentCompanyId().equals(costEntity.getToCompanyId())){
                    taskType = SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAID_2;
                }
                //给发包人发起确认信息
                this.myTaskService.saveMyTask(costDetailId, taskType, dto.getCurrentCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
            }

            if ("4".equals(costEntity.getType()))//其他费用付款
            {
                this.myTaskService.saveMyTask(costDetailId, SystemParameters.OTHER_FEE_FOR_PAY, dto.getCurrentCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
            }
            if ("5".equals(costEntity.getType()))//其他费用收款
            {
                this.myTaskService.saveMyTask(costDetailId, SystemParameters.OTHER_FEE_FOR_PAID, dto.getCurrentCompanyId(),dto.getAccountId(),dto.getCurrentCompanyId());
            }
        }
    }

    /**
     * 方法描述：发起收款
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    @Override
    public AjaxMessage saveOrUpdateReturnMoneyDetail(ProjectCostPointDetailDTO projectCostPointDetailDTO) throws Exception {
        AjaxMessage ajaxMessage = validateReturnMoneyDetail(projectCostPointDetailDTO);
        if (ajaxMessage != null) {
            return ajaxMessage;
        }
        //todo 查询是否是内部组织
        ProjectCostEntity p = this.projectCostDao.getProjectCostByPointId(projectCostPointDetailDTO.getPointId());
        ProjectCostDTO costDTO = new ProjectCostDTO();
        BaseDTO.copyFields(p,costDTO);
        if(costDTO == null){
            return AjaxMessage.error("数据错误");
        }
        //todo 根据流程状态，是否处理发票信息
        String invoiceId = null;
        if("1".equals(projectCostPointDetailDTO.getIsInvoice())){ //如果开票
            invoiceId = this.saveInvoice(projectCostPointDetailDTO);
        }
        ProjectCostPointDetailEntity entity = null;
        projectCostPointDetailDTO.setInvoice(invoiceId);
        //新增(发起收收款)
        if (StringUtil.isNullOrEmpty(projectCostPointDetailDTO.getId())) {
            boolean isInnerCompany = this.isInnerCompany(costDTO);
            if(projectCostPointDetailDTO.getCurrentCompanyId().equals(costDTO.getFromCompanyId())){//如果是付款方新增数据
                entity = this.savePointDetailForPay(projectCostPointDetailDTO,costDTO,isInnerCompany);
            }else{ //如果是内部组织，以收款方做为主体方，进行发起收款，同时通知付款方，可以进行付款申请的动作

                entity = this.saveProjectCostPointDetailEntity(projectCostPointDetailDTO,costDTO,isInnerCompany);
                if(isInnerCompany){ //推送消息,等对方确认后，方可财务处理
                    //need todo 推送消息
                    this.sendMessageToOperatorAndOrgAdminForTaskType(p,entity.getId(),entity.getPointId(),307,projectCostPointDetailDTO.getAccountId(),projectCostPointDetailDTO.getCurrentCompanyId());

                    if("1".equals(projectCostPointDetailDTO.getIsInvoice())){//必须要此判断，否则会进入推送财务收款的任务
                        this.sendMyTask(entity.getId(), projectCostPointDetailDTO);
                    }
                }else {
                    //推送任务
                    this.sendMyTask(entity.getId(), projectCostPointDetailDTO);
                }

            }
            //保存操作
            this.saveOperator(entity.getId(),projectCostPointDetailDTO);
            //添加项目动态
            dynamicService.addDynamic(null,entity,projectCostPointDetailDTO.getCurrentCompanyId(),projectCostPointDetailDTO.getAccountId());
        } else {
            this.updateProjectCostPointDetailEntity(projectCostPointDetailDTO);
        }
        return AjaxMessage.succeed("操作成功");
    }

    private ProjectCostPointDetailEntity savePointDetailForPay(ProjectCostPointDetailDTO projectCostPointDetailDTO,ProjectCostDTO costDTO,boolean isInnerCompany) throws Exception{
        ProjectCostPointDetailEntity entity = null;
        ActivitiDTO activitiDTO = new ActivitiDTO(null,null,projectCostPointDetailDTO.getCurrentCompanyId(),null,projectCostPointDetailDTO.getFee(), ProcessTypeConst.PROCESS_TYPE_PROJECT_PAY_APPLY);
        activitiDTO.getParam().put("approveUser",projectCostPointDetailDTO.getAuditPerson());
        boolean isNeedStartProcess = processService.isNeedStartProcess(activitiDTO);
        if(isNeedStartProcess){
            projectCostPointDetailDTO.setFeeStatus(ProjectCostConst.FEE_STATUS_START);
            entity = this.saveProjectCostPointDetailEntity(projectCostPointDetailDTO,costDTO,isInnerCompany);
            projectCostPointDetailDTO.setId(entity.getId());
            this.startProcessForProjectFeeApply(projectCostPointDetailDTO);
        }else {
            projectCostPointDetailDTO.setFeeStatus(ProjectCostConst.FEE_STATUS_APPROVE);
            entity = this.saveProjectCostPointDetailEntity(projectCostPointDetailDTO,costDTO,isInnerCompany);
            this.sendMyTask(entity.getId(), projectCostPointDetailDTO);
        }
        return entity;
    }

    @Override
    public AjaxMessage applyProjectCostPayFee(ProjectCostPointDetailDTO projectCostPointDetailDTO) throws Exception {
        ActivitiDTO activitiDTO = new ActivitiDTO(null,projectCostPointDetailDTO.getCurrentCompanyUserId(),projectCostPointDetailDTO.getCurrentCompanyId(),null,projectCostPointDetailDTO.getFee(), ProcessTypeConst.PROCESS_TYPE_PROJECT_PAY_APPLY);
        activitiDTO.getParam().put("approveUser",projectCostPointDetailDTO.getAuditPerson());
        if(processService.isNeedStartProcess(activitiDTO)){
            projectCostPointDetailDTO.setFeeStatus(ProjectCostConst.FEE_STATUS_APPROVE_ING);//申请中状态
            this.startProcessForProjectFeeApply(projectCostPointDetailDTO);
        }else {
            //更改费用的状态
            projectCostPointDetailDTO.setFeeStatus(ProjectCostConst.FEE_STATUS_APPROVE);//如果没有走流程，费用直接通过状态
            this.sendMyTask(projectCostPointDetailDTO.getId(), projectCostPointDetailDTO);
        }
        this.updateProjectCostPointDetailEntity(projectCostPointDetailDTO);
        return AjaxMessage.succeed("操作成功");
    }

    /**
     * 保存操作记录
     */
    private void saveOperator(String costDetailId,ProjectCostPointDetailDTO dto) {
        //保存操作
        CompanyUserEntity userEntity = this.companyUserDao.getCompanyUserByUserIdAndCompanyId(dto.getAccountId(), dto.getCurrentCompanyId());
        ProjectCostOperaterEntity operaterEntity = new ProjectCostOperaterEntity();
        if (userEntity != null) {
            operaterEntity.setId(StringUtil.buildUUID());
            operaterEntity.setCostDetailId(costDetailId);
            operaterEntity.setCompanyUserId(userEntity.getId());
            operaterEntity.setType("1");//统一用1，发起回款，发起收款，发起付款
            this.projectCostOperaterDao.insert(operaterEntity);
        }
    }

    private String saveInvoice(ProjectCostPointDetailDTO projectCostPointDetailDTO) throws Exception{
        InvoiceEditDTO invoice = new InvoiceEditDTO();
        BeanUtils.copyProperties(projectCostPointDetailDTO,invoice);
        invoice.setId(projectCostPointDetailDTO.getInvoice());
        invoice.setCompanyId(projectCostPointDetailDTO.getCurrentCompanyId());
        String invoiceId = invoiceService.saveInvoice(invoice);
        return invoiceId;
    }
    private ProjectCostPointDetailEntity saveProjectCostPointDetailEntity(ProjectCostPointDetailDTO projectCostPointDetailDTO,ProjectCostDTO costDTO,boolean isInnerCompany) throws Exception {
        //todo 是否处理发票信息
        ProjectCostPointDetailEntity entity = new ProjectCostPointDetailEntity();
        BaseDTO.copyFields(projectCostPointDetailDTO, entity);
        //新增(发起收收款)
        if (StringUtil.isNullOrEmpty(projectCostPointDetailDTO.getId())) {
            String id = StringUtil.buildUUID();
            entity.setId(id);
            entity.setCompanyId(projectCostPointDetailDTO.getCurrentCompanyId());//设置金额发起的主体方公司
            if(entity.getFeeStatus()==null){
                entity.setFeeStatus(ProjectCostConst.FEE_STATUS_START);//发起的状态，还没有进入审批阶段
            }
            if("1".equals(projectCostPointDetailDTO.getIsInvoice())) { //如果开票，对应收款方而言，则不进入应收状态
                entity.setPaidFeeStatus(ProjectCostConst.FEE_STATUS_START);
            }else {
                entity.setPaidFeeStatus(ProjectCostConst.FEE_STATUS_APPROVE);//如果没有开票，则直接进入应付的状态
            }
            //todo 查询是否是内部组织
            if(isInnerCompany){
                entity.setRelationCompanyId(costDTO.getFromCompanyId());//如果是内部组织，则发起方为收款组织，关联组织为付款方
            }else {
                entity.setRelationCompanyId(null);//为了防止projectCostPointDetailDTO前端或许其他地方设置了RelationCompanyId值
            }
            projectCostPointDetailDao.insert(entity);
        }
        projectCostPointDetailDTO.setId(entity.getId());
        return entity;
    }


    private ProjectCostPointDetailEntity updateProjectCostPointDetailEntity(ProjectCostPointDetailDTO projectCostPointDetailDTO) throws Exception {
        //todo 根据流程状态，是否处理发票信息
        ProjectCostPointDetailEntity entity = new ProjectCostPointDetailEntity();
        BaseDTO.copyFields(projectCostPointDetailDTO, entity);
        ProjectCostPointDetailEntity origin = projectCostPointDetailDao.selectById(projectCostPointDetailDTO.getId());//保留原有数据
        projectCostPointDetailDao.updateById(entity);
        //添加项目动态
        dynamicService.addDynamic(origin,entity,projectCostPointDetailDTO.getCurrentCompanyId(),projectCostPointDetailDTO.getAccountId());
        //createPaymentTask(projectCostPointDetailDTO.getId(),projectCostPointDetailDTO.getAccountId(),projectCostPointDetailDTO.getCurrentCompanyId());
        return entity;
    }


    /**
     * 费用申请启动流程
     */
    private AjaxMessage startProcessForProjectFeeApply(ProjectCostPointDetailDTO dto) throws Exception{
        ApplyProjectCostDTO applyProjectCost = new ApplyProjectCostDTO();
        applyProjectCost.setAccountId(dto.getAccountId());
        applyProjectCost.setCurrentCompanyId(dto.getCurrentCompanyId());
        applyProjectCost.setTargetId(dto.getId());
        applyProjectCost.setCompanyUserId(dto.getCurrentCompanyUserId());
        applyProjectCost.setApplyAmount(dto.getFee());
        applyProjectCost.setProjectId(dto.getProjectId());
        applyProjectCost.setAuditPerson(dto.getAuditPerson());
        expMainService.applyProjectCost(applyProjectCost);
        return AjaxMessage.succeed("操作成功");
    }


    private ProjectCostDTO getProjectCostDTO(ProjectCostPointDetailDTO projectCostPointDetailDTO) throws Exception{
        ProjectCostEntity p = this.projectCostDao.getProjectCostByPointId(projectCostPointDetailDTO.getPointId());
        ProjectCostDTO dto = new ProjectCostDTO();
        BaseDTO.copyFields(p,dto);

        return dto;
    }

    private boolean isInnerCompany(ProjectCostEntity p){
        ProjectCostDTO dto = new ProjectCostDTO();
        BeanUtils.copyProperties(p,dto);
        return isInnerCompany(dto);
    }
    private boolean isInnerCompany(ProjectCostDTO p){
        if(p!=null && ("2".equals(p.getType()) || "3".equals(p.getType()))){
            CompanyRelationDTO fromRootCompany =  companyDao.getOrgType(p.getFromCompanyId());
            CompanyRelationDTO toRootCompany =  companyDao.getOrgType(p.getToCompanyId());
            if(fromRootCompany!=null && StringUtil.isNullOrEmpty(fromRootCompany.getOrgPid())){
                fromRootCompany.setOrgPid(fromRootCompany.getOrgId());//如果为空，默认为自己
            }
            if(toRootCompany!=null && StringUtil.isNullOrEmpty(toRootCompany.getOrgPid())){
                toRootCompany.setOrgPid(toRootCompany.getOrgId());//如果为空，默认为自己
            }
            if(fromRootCompany!=null && toRootCompany!=null
                    && fromRootCompany.getOrgPid().equals(toRootCompany.getOrgPid())
                    && (
                    (StringUtil.isNullOrEmpty(fromRootCompany.getTypeId()) && "3".equals(toRootCompany.getTypeId()))
                            || (StringUtil.isNullOrEmpty(toRootCompany.getTypeId()) && "3".equals(fromRootCompany.getTypeId()))
                            || ("3".equals(fromRootCompany.getTypeId()) && "3".equals(toRootCompany.getTypeId()))
            )
                    ) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法描述：查询合同回款(map:projectId)
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    @Override
    public AjaxMessage getContractInfo(Map<String, Object> map) throws Exception {
        String accountId = (String)map.get("accountId");
        map.put("type", "1");
        map.put("flag", "1");
        String cpyId = (StringUtil.isNullOrEmpty(map.get("companyId"))) ?
                this.projectDao.selectById(map.get("projectId")).getCompanyId() :
                (String) map.get("companyId");

        map.put("companyId",cpyId);
        String companyUserId = (String)map.get("companyUserId");
        String isManager = "0";
        String isFinancial = "0";
        if(null!=companyUserId && !"".equals(companyUserId)){
            isManager = this.getManagerFlag((String) map.get("projectId"), cpyId,companyUserId);
        }
        if(permissionService.isFinancialReceive(cpyId,accountId)){
            isFinancial = "1";
        }
        Map<String, Object> result = this.getReviewFeeInfo(map, "1");
        result.put("isManager", isManager);
        result.put("isFinancial",isFinancial);
        return AjaxMessage.succeed("查询成功").setData(result);
    }

    private void setCurrentTaskRealCompanyId(Map<String, Object> map){
        if(map.containsKey("myTaskId") && !StringUtil.isNullOrEmpty(map.get("myTaskId"))){
            String companyId = this.projectCostPointDao.getCostFeeCompanyByTaskId((String)map.get("myTaskId"));
            if(!StringUtil.isNullOrEmpty(companyId)){
                map.put("companyId",companyId);
            }
        }
    }
    /**
     * 方法描述：查询技术审查费(map:projectId)
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    public AjaxMessage getTechicalReviewFeeInfo(Map<String, Object> map) throws Exception {
        this.setCurrentTaskRealCompanyId(map);
        map.put("pidIsNull", "1");//标示，只查父节点
        map.put("flag", "1");//标示,查询正式合同记录
        Map<String, Object> result = this.getReviewFeeInfo(map, "2");

        String fromStatistic = (String) map.get("fromStatistic");//从统计过来查询无需要查询相关的操作权限

        if(fromStatistic==null || "".equals(fromStatistic)){

            ProjectEntity projectEntity = this.projectDao.selectById((String) map.get("projectId"));

            if (projectEntity != null) {
                result.put("projectName", projectEntity.getProjectName());
                CompanyEntity companyEntity = this.companyDao.selectById(projectEntity.getCompanyId());
                if (companyEntity != null) {
                    result.put("companyName", companyEntity.getAliasName());
                }
                if (!StringUtil.isNullOrEmpty(projectEntity.getCompanyBid())) {
                    CompanyEntity companyBEntity = this.companyDao.selectById(projectEntity.getCompanyBid());
                    if (companyBEntity != null) {
                        result.put("companyBName", companyBEntity.getAliasName());
                    }
                }

                if (projectEntity.getCompanyId().equals((String) map.get("companyId"))) {//1为立项方，0：为乙方
                    result.put("isSetUpProject", "1");
                } else {
                    result.put("isSetUpProject", "0");
                }

                String isManager = "0";
                String currentCompanyId = (String) map.get("currentCompanyId");
                String companyUserId = (String) map.get("companyUserId");
                if (!StringUtil.isNullOrEmpty(projectEntity.getCompanyBid()) && !projectEntity.getCompanyBid().equals(projectEntity.getCompanyId())) {//存在乙方
                    if(currentCompanyId.equals(projectEntity.getCompanyBid())){//当前是组织是乙方
                        isManager = this.getManagerFlag(projectEntity.getId(), projectEntity.getCompanyBid(),companyUserId);

                    }else{//当前是立项组织
                        isManager = this.getManagerFlag(projectEntity.getId(), currentCompanyId,companyUserId);
                    }
                }
                result.put("isManager", isManager);
            }
        }

        return AjaxMessage.succeed("查询成功").setData(result);
    }

    private Map<String, Object> getReviewFeeInfo(Map<String, Object> map, String type) throws Exception {
        map.put("type", type);
        List<ProjectCostPointDTO> projectCostPointDTOS = new ArrayList<>();
        //查询总费用
        List<ProjectCostDTO> totalCost = this.projectCostDao.selectByParam(map);//理论上只会存在一条
        if(!CollectionUtils.isEmpty(totalCost)){//根据costId查询相对应的节点数据
            map.put("costId",totalCost.get(0).getId());
            //查询是否是内部组织
            map.put("isInnerCompany",this.isInnerCompany(totalCost.get(0)));
            projectCostPointDTOS = this.projectCostPointDao.selectByParam(map);
        }
        List<ProjectCostPointDataDTO> projectCostPointDataList = new ArrayList<>();
        ProjectCostTotalDTO totalDTO = new ProjectCostTotalDTO();
        Map<String, Object> result = new HashMap<String, Object>();

        if (!CollectionUtils.isEmpty(totalCost)) {
            result.put("totalCost", totalCost.get(0).getFee());
            result.put("costId", totalCost.get(0).getId());
            if (type.equals("3")) {
                String companyId = (String) map.get("companyId");
                String projectCompanyId = (String) map.get("projectCompanyId");
                if (projectCompanyId.equals((String) map.get("fromCompanyId")) && !projectCompanyId.equals(companyId)) {
                    result.put("isSetUpProject", "1");
                } else {
                    result.put("isSetUpProject", "0");
                }
                CompanyEntity companyEntity = null;
                if (companyId.equals((String) map.get("fromCompanyId"))) {
                    companyEntity = this.companyDao.selectById(map.get("toCompanyId"));
                } else {
                    companyEntity = this.companyDao.selectById(map.get("fromCompanyId"));
                }
                if (companyEntity != null) {
                    result.put("companyName", companyEntity.getAliasName());
                }

            }
            for (ProjectCostPointDTO dto : projectCostPointDTOS) {
                this.getProjectCostPointData(dto, totalDTO, projectCostPointDataList,map,totalCost.get(0).getFee());
            }
        }
        //使用新方法计算总计百分比
        if(!CollectionUtils.isEmpty(totalCost)){
            BigDecimal f = totalCost.get(0).getFee();
            if ((f != null) && CommonUtil.doubleCompare(f.doubleValue(),new BigDecimal(0).doubleValue()) > 0) {
                BigDecimal p = totalDTO.getFee().multiply(new BigDecimal(100));
                p = p.divide(totalCost.get(0).getFee(), 2, RoundingMode.HALF_UP);
                totalDTO.setFeeProportion(p.doubleValue());
            }
        }
        result.put("pointList", projectCostPointDataList);
        result.put("total", totalDTO);
        result.put("currentFeeOrgId",map.get("companyId"));
        return result;
    }


    private Map<String, Object> getReviewFeeInfo(Map<String, Object> map, ProjectCostDTO cost) throws Exception {
        boolean isInnerCompany = this.isInnerCompany(cost);
        String companyId = (String) map.get("companyId");
        map.put("costId",cost.getId());
        map.put("fromCompanyId",cost.getFromCompanyId());
        map.put("toCompanyId",cost.getToCompanyId());
        map.put("isInnerCompany",isInnerCompany);

        //查询收款节点
        Map<String,Object> costPointParam = new HashMap<>();
        costPointParam.put("pidIsNull", "1");//标示，只查父节点
        costPointParam.put("flag", "1");//标示,查询正式合同记录
        costPointParam.put("costId",cost.getId());
        List<ProjectCostPointDTO> projectCostPointDTOS = this.projectCostPointDao.selectByParam(costPointParam);
        //处理返回数据
        List<ProjectCostPointDataDTO> projectCostPointDataList = new ArrayList<>();
        ProjectCostTotalDTO totalDTO = new ProjectCostTotalDTO();
        for (ProjectCostPointDTO dto : projectCostPointDTOS) {
            this.getProjectCostPointData(dto, totalDTO, projectCostPointDataList,map,cost.getFee());
        }
        //使用新方法计算总计百分比
        BigDecimal f = cost.getFee();
        if ((f != null) && CommonUtil.doubleCompare(f.doubleValue(),new BigDecimal(0).doubleValue()) > 0) {
            BigDecimal p = totalDTO.getFee().multiply(new BigDecimal(100));
            p = p.divide(cost.getFee(), 2, RoundingMode.HALF_UP);
            totalDTO.setFeeProportion(p.doubleValue());
        }

        Map<String,String> relationCompany = this.getRelationCompanyNameMap(cost,companyId);
        Map<String, Object> result = new HashMap<>();
        result.put("totalCost", cost.getFee());//总金额
        result.put("costId", cost.getId());
        result.put("type",cost.getType());
        result.put("operateCompanyId",cost.getOperateCompanyId());
        result.put("isSetUpProject", this.getIsSetUpProject(companyId, (String)map.get("projectCompanyId")));
        result.put("relationCompany",relationCompany);
        result.put("companyName", relationCompany==null?"":relationCompany.get("companyName"));
        result.put("pointList", projectCostPointDataList);
        result.put("total", totalDTO);
        result.put("currentFeeOrgId",map.get("companyId"));
        result.put("startReceiveFlag",this.getStartReceiveFlag(map));//发起回款
        result.put("startPayFlag",this.getStartPayFlag(map,cost,isInnerCompany)); //发起付款申请（外部）
        result.put("startPayFlagForInner",this.getStartPayFlagForInner(map,cost,isInnerCompany));  //发起付款申请（内部）
        //查询是否是内部组织
        result.put("isInnerCompany",isInnerCompany);
        //获取附件
        result.put("attachList",projectSkyDriverService.getAttachListByTargetId(cost.getId()));
        return result;
    }

    /**
     * 发起收款的标识 1：可以发起收款，2：不可以发起收款
     */
    /** 使用getStartReceiveFlag(String currentCompanyId,ProjectCostDataDTO costData,String isManager)代替 **/
    @Deprecated
    private String getStartReceiveFlag(Map<String, Object> map){
        if(((String)map.get("companyId")).equals((String)map.get("toCompanyId")) && "1".equals(map.get("isManager"))){
            return "1";
        }
        return "0";
    }

    /**
     * 发起收款的标识 1：可以发起收款，2：不可以发起收款
     */
    private String getStartReceiveFlag(String currentCompanyId,ProjectCostDataDTO costData,String isManager){
        if((currentCompanyId).equals(costData.getToCompanyId()) && "1".equals(isManager)){
            return "1";
        }
        return "0";
    }

    /** 使用getStartPayFlag(String currentCompanyId,ProjectCostDataDTO costData,String isManager,boolean isInnerCompany)代替 **/
    @Deprecated
    private String getStartPayFlag(Map<String, Object> map,ProjectCostDTO cost,boolean isInnerCompany){
        if(((String)map.get("companyId")).equals((String)map.get("fromCompanyId")) && "1".equals(map.get("isManager")) && !isInnerCompany){
            return "1";
        }
        return "0";
    }

    private String getStartPayFlag(String currentCompanyId,ProjectCostDataDTO costData,String isManager,boolean isInnerCompany){
        if(currentCompanyId.equals(costData.getFromCompanyId()) && "1".equals(isManager) && !isInnerCompany){
            return "1";
        }
        return "0";
    }

    /** 使用getStartPayFlagForInner(String currentCompanyId,ProjectCostDataDTO costData,String isManager,boolean isInnerCompany)代替 **/
    @Deprecated
    private String getStartPayFlagForInner(Map<String, Object> map,ProjectCostDTO cost,boolean isInnerCompany){
        if(((String)map.get("companyId")).equals((String)map.get("fromCompanyId")) && "1".equals(map.get("isManager")) && isInnerCompany){
            return "1";
        }
        return "0";
    }

    private String getStartPayFlagForInner(String currentCompanyId,ProjectCostDataDTO costData,String isManager,boolean isInnerCompany){
        if(currentCompanyId.equals(costData.getFromCompanyId()) && "1".equals(isManager) && isInnerCompany){
            return "1";
        }
        return "0";
    }

    /**
     * 获取立项方组织的名称
     */
    /** 使用String getRelationCompanyName代替 **/
    @Deprecated
    private Map<String,String> getRelationCompanyNameMap(ProjectCostDTO cost,String currentCompanyId){
        CompanyEntity companyEntity = null;
        String relationCompanyId = null;
        Map<String,String> relationCompany = new HashMap<>();
        if (currentCompanyId.equals(cost.getFromCompanyId())) {
            relationCompanyId = cost.getToCompanyId();
        } else {
            relationCompanyId = cost.getFromCompanyId();
        }
        if(StringUtil.isNullOrEmpty(relationCompanyId)){
            return null;
        }
        relationCompany.put("companyId",relationCompanyId);
        companyEntity = this.companyDao.selectById(relationCompanyId);
        if (companyEntity != null) {

            relationCompany.put("companyName",companyEntity.getAliasName());
            return  relationCompany;
        }else {
            //从enterprise中查询
             EnterpriseDTO enterprise= enterpriseService.getEnterpriseById(relationCompanyId);
            if(enterprise!=null){
                relationCompany.put("companyName",enterprise.getCorpname());
                relationCompany.put("taxNumber",enterprise.getTaxNumber());
                return relationCompany;
            }
        }
        relationCompany.put("companyName",relationCompanyId);
        return relationCompany;
    }

    /**
     * 获取立项方组织的名称
     */
    private String getRelationCompanyName(ProjectCostDTO cost,String currentCompanyId){
        CompanyEntity companyEntity = null;
        String relationCompanyId = null;
        if (currentCompanyId.equals(cost.getFromCompanyId())) {
            relationCompanyId = cost.getToCompanyId();
        } else {
            relationCompanyId = cost.getFromCompanyId();
        }
        companyEntity = this.companyDao.selectById(relationCompanyId);
        if (companyEntity != null) {
            return  companyEntity.getAliasName();
        }else {
            //从enterprise中查询
            String name = enterpriseService.getEnterpriseName(relationCompanyId);
            if(name!=null){
                return name;
            }
        }
        return relationCompanyId;
    }

    /**
     * 当前组织是否是立项方
     */
    private String getIsSetUpProject(String currentCompanyId,String projectCompanyId ){
        if (projectCompanyId.equals(currentCompanyId)) {
           return "1";//是立项方
        } else {
           return "0";//不是立项方
        }
    }

    /**
     * 方法描述：合作设计费
     * 作者：chenzhujie
     * 日期：2017/3/1
     *
     * @param map(projectId,companyId:当前公司)
     */
    @Override
    public AjaxMessage getCooperativeDesignFeeInfo(Map<String, Object> map) throws Exception {
        this.setCurrentTaskRealCompanyId(map);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        //首先查询关系
        List<Map<String, String>> relationList = projectTaskRelationDao.getProjectRelation(map);
        //1.查询项目所在组织id
        ProjectEntity projectEntity = this.projectDao.selectById(map.get("projectId"));
        if (projectEntity == null) {
            return AjaxMessage.failed("查询失败");
        }

        //2.查询每个表的明细
        for (Map<String, String> map1 : relationList) {
            Map<String, Object> param = new HashMap<String, Object>();
         //   param.put("taskIdList", map1.get("taskId").split(","));
            param.put("pidIsNull", "1");//标示，只查父节点
            param.put("projectId", map.get("projectId"));
            param.put("fromCompanyId", map1.get("fromCompanyId"));
            param.put("toCompanyId", map1.get("toCompanyId"));
            param.put("projectCompanyId", projectEntity.getCompanyId());
            param.put("companyId", map.get("companyId"));
            param.put("currentCompanyId", map.get("currentCompanyId"));
            param.put("companyUserId", map.get("companyUserId"));
            param.put("flag", "1");
            param.put("type", "3");
            Map<String, Object> result = this.getReviewFeeInfo(param, "3");
            result.put("fromCompanyId", map1.get("fromCompanyId"));
            result.put("toCompanyId", map1.get("toCompanyId"));

            String companyUserId = (String) map.get("companyUserId");
            if(null!=companyUserId && !"".equals(companyUserId)){

                result.put("isManager", this.getManagerFlag(projectEntity.getId(), (String)map1.get("fromCompanyId"),companyUserId));

                //收款方
                result.put("isManager2", this.getManagerFlag(projectEntity.getId(), (String)map1.get("toCompanyId"),companyUserId));
            }
            param.clear();
            param.put("toCompanyId",map1.get("toCompanyId"));
            param.put("fromCompanyId",map1.get("fromCompanyId"));
            param.put("projectId",map.get("projectId"));
            ProjectTaskEntity taskEntity = projectTaskDao.getAllTaskNameByToCompanyId(param);
            if(null!=taskEntity){
                result.put("taskName",taskEntity.getTaskName());
            }
            resultList.add(result);
        }

        //合作关系单独请求
        return AjaxMessage.succeed("查询成功").setData(resultList);
    }

    @Override
    public Map<String, Object> listProjectCost(Map<String, Object> map) throws Exception {
        //做拦截
        this.setCurrentTaskRealCompanyId(map);
        Map<String, Object> resultData = new HashMap<>();
        List<Map<String, Object>> resultList =  new ArrayList<>();
        String companyId = (String)map.get("companyId");
        String currentCompanyId =  (String)map.get("currentCompanyId");
        String companyUserId = (String)map.get("companyUserId");
        String accountId = (String)map.get("accountId");
        String payType = (String)map.get("payType");
        ProjectEntity project = this.projectDao.selectById(map.get("projectId"));
        if(project==null){
            return resultData;
        }
        map.put("projectCompanyId",project.getCompanyId());

        //获取要返回到前端的部分数据
        String isManager = this.getManagerFlag(project.getId(), companyId,companyUserId);
        String isFinancial = getIsFinancial(currentCompanyId,accountId,payType);
        map.put("isManager", isManager);//便于传递到 getReviewFeeInfo 处理其他业务
        resultData.put("isManager", isManager);
        resultData.put("isFinancial", isFinancial);
        map.put("isFinancial",isFinancial);
        //先查询主记录
        List<ProjectCostDTO> costList = this.getProjectCostList(map);
        for(ProjectCostDTO cost:costList){
            Map<String,Object> result = this.getReviewFeeInfo(map,cost);
            result.putAll(resultData);
            resultList.add(result);
        }
        resultData.put("costList",resultList);
        return resultData;
    }


    private List<ProjectCostDTO> getProjectCostList(Map<String, Object> map){
        String costId = (String)map.get("costId");
        Map<String, Object> costParam = new HashMap<>();
        costParam.put("projectId",map.get("projectId"));
        if(StringUtil.isNullOrEmpty(costId)){
            String currentCompanyId = (String)map.get("companyId");
            Integer payType =Integer.parseInt((String)map.get("payType"));
            if(CompanyBillType.DIRECTION_PAYEE == payType){
                costParam.put("toCompanyId",currentCompanyId);
            }else {
                costParam.put("fromCompanyId",currentCompanyId);
            }
        }else {
            costParam.put("costId",costId);
        }
        return this.projectCostDao.selectByParam(costParam);
    }

    /**
     * 当前是否是财务 1=是，0=不是
     */
    private String getIsFinancial(String companyId,String accountId,String payType){
        String isFinancial = "0";
        if("1".equals(payType) && permissionService.isFinancialReceive(companyId,accountId)){
            isFinancial = "1";
        }else {
            if(permissionService.isFinancial(companyId,accountId)){
                isFinancial = "1";
            }
        }
        return isFinancial;
    }

    /**
     * 当前是否是经营负责人 1=是，0=不是
     */
    private String getManagerFlag(String projectId,String companyId,String companyUserId) throws Exception{
        ProjectMemberEntity managerEntity = this.projectMemberService.getOperatorManager(projectId,companyId);
        if(managerEntity!=null && managerEntity.getCompanyUserId().equals(companyUserId)){
            return "1";
        }
        managerEntity = this.projectMemberService.getOperatorAssistant(projectId,companyId);
        if(managerEntity!=null && managerEntity.getCompanyUserId().equals(companyUserId)){
            return "1";
        }
        return "0";
    }

    /**
     * 收款节点处理
     */
    private ProjectCostPointDataDTO getProjectCostPointData(ProjectCostPointDTO dto, ProjectCostTotalDTO totalDTO, List<ProjectCostPointDataDTO> projectCostPointDataList, Map<String,Object> map,BigDecimal totalCost) throws Exception {

        ProjectCostPointDataDTO dataDTO = new ProjectCostPointDataDTO();
        BaseDTO.copyFields(dto, dataDTO);
        projectCostPointDataList.add(dataDTO);
        dataDTO.setUnpaid(new BigDecimal(0));//首先默认为0

        dataDTO.setParentFee(totalCost);
        //查询子节点
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("type", dto.getType());
        param.put("pid", dataDTO.getId());
        List<ProjectCostPointDTO> childList = this.projectCostPointDao.selectByParam(param);
        if (!CollectionUtils.isEmpty(childList)) {
            dataDTO.setIsHasChild(1);//有子节点
            BigDecimal paidFee = new BigDecimal("0");
            for (ProjectCostPointDTO dto1 : childList) {
                ProjectCostPointDataDTO dataDTO1 = new ProjectCostPointDataDTO();
                BaseDTO.copyFields(dto1, dataDTO1);
                //把父任务的值设置上去，用于前端判断
                dataDTO1.setParentFee(dataDTO.getFee());
                projectCostPointDataList.add(dataDTO1);
                //获取发起收款（发起回款）
                this.getPointDetailData(dataDTO1, map,totalDTO);
                if(dataDTO1.getDeleteFlag()==1){//只要有到款则不可被删除
                    dataDTO.setDeleteFlag(1);
                }
                if(dataDTO1.getPaidFee()!=null){
                    paidFee = paidFee.add(dataDTO1.getPaidFee());
                }
            }
            if (dataDTO.getFee()!=null) {
                dataDTO.setUnpaid(dataDTO.getFee().subtract(paidFee));
            }
        } else {
            //获取发起收款（发起回款）
            this.getPointDetailData(dataDTO, map,totalDTO);
        }


        //处理合计
        if (!StringUtil.isNullOrEmpty(dto.getFeeProportion())) {
            totalDTO.setFeeProportion(totalDTO.getFeeProportion() + Double.parseDouble(dto.getFeeProportion()));
        }

        if (null != dto.getFee()) {
            totalDTO.setFee(totalDTO.getFee().add(dto.getFee()));
        }
        if (null != dataDTO.getUnpaid() && null != totalDTO.getUnpaid()) {
            totalDTO.setUnpaid(totalDTO.getUnpaid().add(dataDTO.getUnpaid()));
        }
        return dataDTO;
    }

    /**
     * 方法描述：获取发起收款明细
     * 作者：MaoSF
     * 日期：2017/4/25
     */
    private void getPointDetailData(ProjectCostPointDataDTO dto, Map<String,Object> map,ProjectCostTotalDTO totalDTO) throws Exception {
        //查询明细
        dto.setUnpaid(new BigDecimal("0"));
        dto.setBackFee(new BigDecimal("0"));
        //获取当前人所再的组织
        List<ProjectCostPointDetailDataDTO> pointDetailDataList = this.projectCostPointDetailDao.getCostPointDetail(dto.getId(),(String)map.get("companyId"));
        for(ProjectCostPointDetailDataDTO pointDetailDataDTO:pointDetailDataList){
            this.getPaymentDetailData(pointDetailDataDTO,map);
            //累积发起收款的金额
            dto.setBackFee(dto.getBackFee().add(pointDetailDataDTO.getFee()));

            //只要有到款，则不可被删除
            if(pointDetailDataDTO.getPayFee().compareTo(new BigDecimal("0"))>0 || pointDetailDataDTO.getPaidFee().compareTo(new BigDecimal("0"))>0){
                pointDetailDataDTO.setDeleteFlag(1);//不可被删除
            }
            //累积发起收款
            if(!"2".equals(pointDetailDataDTO.getFeeStatus())) {//2代表已经退回的状态，退回的不做计算
                totalDTO.setBackMoney(totalDTO.getBackMoney().add(pointDetailDataDTO.getFee()));
            }
            //累计已经审批通过的金额
            if("1".equals(pointDetailDataDTO.getFeeStatus())){
                totalDTO.setApproveBackMoneyApprove(totalDTO.getApproveBackMoneyApprove().add(pointDetailDataDTO.getFee()));
            }
            //累计财务已经发票确认过的金额
            if("1".equals(pointDetailDataDTO.getPaidFeeStatus())){
                totalDTO.setInvoiceConfirmFee(totalDTO.getInvoiceConfirmFee().add(pointDetailDataDTO.getFee()));
            }

            //累积总到款
            totalDTO.setToTheMoney(totalDTO.getToTheMoney().add(pointDetailDataDTO.getPaidFee()));

            //累积总付款
            totalDTO.setPayTheMoney(totalDTO.getPayTheMoney().add(pointDetailDataDTO.getPayFee()));

            //累积经营负责人付款（到款）的金额
            totalDTO.setPaymentFee(totalDTO.getPaymentFee().add(pointDetailDataDTO.getPaymentFee()));
            //查询操作人
            this.getOperatorForCostPointDetail(pointDetailDataDTO, dto.getType(), map);

            //处理费用状态
            if("2".equals((String)map.get("payType"))){  //如果是付款
                ExpMainDTO auditEntity =  expMainDao.getExpMainByRelationId(pointDetailDataDTO.getId());
                if(auditEntity == null){//如果没有发起申请
                    pointDetailDataDTO.setAuditStatus("4");//没有经过审批
                }else {
                    pointDetailDataDTO.setAuditStatus(auditEntity.getApproveStatus());
                }
            }
        }

        dto.setPointDetailList(pointDetailDataList);
    }

    /**
     * 方法描述：处理操作人
     * 作者：MaoSF
     * 日期：2017/3/6
     */
    private void getOperatorForCostPointDetail(ProjectCostPointDetailDataDTO detailDataDTO, String type, Map<String,Object> param) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("costDetailId", detailDataDTO.getId());
        //设置权限
        detailDataDTO.setRoleMap(handleOperateRole(detailDataDTO.getId(), type, param));
        //handleNeedRoleToHandle(detailDataDTO,companyUserId);//对权限做补偿
        List<ProjectCostOperaterDTO> operaterDTOS = this.projectCostOperaterDao.getCostOperator(map);
        if ("1".equals(type) || "4".equals(type) || "5".equals(type)) {//合同回款
            for (ProjectCostOperaterDTO dto : operaterDTOS) {
                if ("2".equals(dto.getType()) || "1".equals(dto.getType())) {
                    detailDataDTO.setUserName(dto.getUserName());
                }
            }
        }

        if ("2".equals(type) || "3".equals(type)) {//技术审查费

            for (ProjectCostOperaterDTO dto : operaterDTOS) {
                if ("1".equals(dto.getType())) {
                    detailDataDTO.setUserName(dto.getUserName());
                }
                if ("3".equals(dto.getType())) {
                    detailDataDTO.setUserName2(dto.getUserName());
                }
                if(StringUtil.isNullOrEmpty( detailDataDTO.getUserName2())){
                    if("4".equals(dto.getType())){
                        detailDataDTO.setUserName2(dto.getUserName());
                    }
                }

            }
        }
    }

    /**
     * 方法描述：获取收款详情
     * 作者：MaoSF
     * 日期：2017/4/25
     * @param:
     * @return:
     */
    private void getPaymentDetailData(ProjectCostPointDetailDataDTO dto, Map<String, Object> param) throws Exception {
        //查询明细
        Map<String, Object> map = new HashMap<>();
        map.put("pointDetailId", dto.getId());
        List<ProjectCostPaymentDetailDTO> detailDTOS = this.projectCostPaymentDetailDao.selectByPointDetailId(map);
        List<ProjectCostPaymentDetailDataDTO> detailDataList = new ArrayList<>();
        BigDecimal paidFee = new BigDecimal("0");
        BigDecimal payFee = new BigDecimal("0");
        BigDecimal paymentFee = new BigDecimal("0");
        for (ProjectCostPaymentDetailDTO detailDataDTO : detailDTOS) {
            ProjectCostPaymentDetailDataDTO detailDataDTO1 = new ProjectCostPaymentDetailDataDTO();
            BaseDTO.copyFields(detailDataDTO, detailDataDTO1);
            //查询操作人
            this.getOperatorForCostDetail(detailDataDTO1, dto.getType(), param);
            //统计到款
            if(!StringUtil.isNullOrEmpty(detailDataDTO.getPaidDate())){
                paidFee = paidFee.add(detailDataDTO.getFee());
            }
            //统计付款
            if(!StringUtil.isNullOrEmpty(detailDataDTO.getPayDate())){
                payFee = payFee.add(detailDataDTO.getFee());
            }

            //累积明细金额
            paymentFee = paymentFee.add(detailDataDTO.getFee());

            detailDataList.add(detailDataDTO1);
        }
        //未付金额
        dto.setPayFee(payFee);
        //未收金额
        dto.setPaidFee(paidFee);
        //总收款（付款）明细
        dto.setPaymentFee(paymentFee);
        dto.setPaymentList(detailDataList);

    }


    /**
     * 方法描述：处理操作人
     * 作者：MaoSF
     * 日期：2017/3/6
     *
     * @param:
     * @return:
     */
    private void getOperatorForCostDetail(ProjectCostPaymentDetailDataDTO detailDataDTO, String type, Map<String, Object> paraMap) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("costDetailId", detailDataDTO.getId());

        //设置权限
        detailDataDTO.setRoleMap(handleOperateRole(detailDataDTO.getId(), type, paraMap));
       // handleNeedRoleToHandle(detailDataDTO,companyUserId);//对权限做补偿
        List<ProjectCostOperaterDTO> operaterDTOS = this.projectCostOperaterDao.getCostOperator(map);
        if ("1".equals(type) || "4".equals(type) || "5".equals(type)) {//合同回款
            for (ProjectCostOperaterDTO dto : operaterDTOS) {
                if ("2".equals(dto.getType())) {
                    detailDataDTO.setUserName(dto.getUserName());
                }
            }
        }

        if ("2".equals(type) || "3".equals(type)) {//技术审查费

            for (ProjectCostOperaterDTO dto : operaterDTOS) {
                if ("1".equals(dto.getType())) {
                    detailDataDTO.setUserName(dto.getUserName());
                }
                if ("3".equals(dto.getType())) {
                    detailDataDTO.setUserName2(dto.getUserName());
                }
                if(StringUtil.isNullOrEmpty( detailDataDTO.getUserName2())){
                    if("4".equals(dto.getType())){
                        detailDataDTO.setUserName2(dto.getUserName());
                    }
                }

            }
        }
    }

    private void handleNeedRoleToHandle(ProjectCostPaymentDetailDataDTO detailDataDTO, String companyUserId) throws Exception{
        CompanyUserEntity companyUserEntity = this.companyUserDao.selectById(companyUserId);
        if(companyUserEntity==null){
            return;
        }
        String companyId = companyUserEntity.getCompanyId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("targetId", detailDataDTO.getId());
        map.put("companyId", companyId);
        List<MyTaskEntity> myTaskList = this.myTaskService.getMyTaskByParamter(map);

        if(!CollectionUtils.isEmpty(myTaskList)){//理论上只会存在一条有效数据
            MyTaskEntity entity = myTaskList.get(0);
            if(companyId.equals(entity.getCompanyId())) {
                if (entity.getTaskType() == 5 || entity.getTaskType() == 7) {
                    map.clear();
                    map.put("permissionId", "50");
                    map.put("companyId", companyId);
                    map.put("userId", companyUserEntity.getUserId());
                    List<CompanyUserTableDTO> companyUserList = this.companyUserDao.getCompanyUserByPermissionId(map);
                    if (!CollectionUtils.isEmpty(companyUserList)){
                        detailDataDTO.getRoleMap().put("flag" + entity.getTaskType(), entity.getId());
                    }else {
                        detailDataDTO.getRoleMap().remove("flag" + entity.getTaskType());
                    }
                }

                if(entity.getTaskType()==8 || entity.getTaskType()==9 || entity.getTaskType()==10 ||  (entity.getTaskType()>15 &&  entity.getTaskType()<22)){
                    if(isMyTask(entity,companyUserEntity.getUserId())){
                        detailDataDTO.getRoleMap().put("flag" + entity.getTaskType(), entity.getId());
                    }else {
                        detailDataDTO.getRoleMap().remove("flag" + entity.getTaskType());
                    }
                }
            }
        }
    }

//    private void handleNeedRoleToHandle(ProjectCostPointDetailDataDTO detailDataDTO, String companyUserId) throws Exception{
//        CompanyUserEntity companyUserEntity = this.companyUserDao.selectById(companyUserId);
//        if(companyUserEntity==null){
//            return;
//        }
//        String companyId = companyUserEntity.getCompanyId();
//        Map<String, Object> map = new HashMap<>();
//        map.put("targetId", detailDataDTO.getId());
//        map.put("companyId", companyId);
//        List<MyTaskEntity> myTaskList = this.myTaskService.getMyTaskByParamter(map);
//        if(companyUserEntity==null){
//            return;
//        }
//        if(!CollectionUtils.isEmpty(myTaskList)){//理论上只会存在一条有效数据
//            MyTaskEntity entity = myTaskList.get(0);
//            if(companyId.equals(entity.getCompanyId())) {
//                if (entity.getTaskType() == 5 || entity.getTaskType() == 7) {
//                    map.clear();
//                    map.put("permissionId", "50");
//                    map.put("companyId", companyId);
//                    map.put("userId", companyUserEntity.getUserId());
//                    List<CompanyUserTableDTO> companyUserList = this.companyUserDao.getCompanyUserByPermissionId(map);
//                    if (!CollectionUtils.isEmpty(companyUserList)){
//                        detailDataDTO.getRoleMap().put("flag" + entity.getTaskType(), entity.getId());
//                    }else {
//                        detailDataDTO.getRoleMap().remove("flag" + entity.getTaskType());
//                    }
//                }
//                if(entity.getTaskType()==10 || (entity.getTaskType()>15 &&  entity.getTaskType()<22)){
//
//                    if(isMyTask(entity,companyUserEntity.getUserId())){
//                        detailDataDTO.getRoleMap().put("flag" + entity.getTaskType(), entity.getId());
//                    }else {
//                        detailDataDTO.getRoleMap().remove("flag" + entity.getTaskType());
//                    }
//                }
//            }
//        }
//    }

    private boolean isMyTask(MyTaskEntity myTask,String accountId){
        switch (myTask.getTaskType()){
            case SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAY:
            case SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAY:
            case SystemParameters.OTHER_FEE_FOR_PAY:
            case SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAY_2:
            case SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAY_2:
                if (permissionService.isFinancial(myTask.getCompanyId(),accountId)) {
                    return true;
                }
            case SystemParameters.CONTRACT_FEE_PAYMENT_CONFIRM:
            case SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAID:
            case SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAID:
            case SystemParameters.OTHER_FEE_FOR_PAID:
            case SystemParameters.INVOICE_FINN_IN_FOR_PAID:
            case SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAID_2:
            case SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAID_2:
                if (permissionService.isFinancialReceive(myTask.getCompanyId(),accountId)) {
                    return true;
                }
            default:return false;
        }
    }

    private Map<String, Object> handleOperateRole(String costDetailId, ProjectCostQueryDTO queryDTO) throws Exception {
        Map<String, Object> roleMap = new HashMap<>();
        String accountId = queryDTO.getAccountId();
        String companyId = queryDTO.getAppOrgId();
        if(permissionService.isFinancialReceive(companyId,accountId) || permissionService.isFinancial(companyId,accountId)){
            Map<String, Object> map = new HashMap<>();
            map.put("targetId", costDetailId);
            map.put("companyId", companyId);
            List<MyTaskEntity> myTaskList = this.myTaskService.getMyTaskByParam(map);
            if(!CollectionUtils.isEmpty(myTaskList)){
                MyTaskEntity task = myTaskList.get(0);//理论上只会存在一条
                if(task.getTaskType()==29){
                    roleMap.put("invoiceConfirm",task.getId());//发票确认
                }else {
                    roleMap.put("financialForFee",task.getId());//财务到账，付款
                }
            }
        }
        return roleMap;
    }

    /** 在app内已经使用handleOperateRole(String costDetailId, ProjectCostQueryDTO queryDTO)代替 **/
    @Deprecated
    private Map<String, Object> handleOperateRole(String costDetailId, String type, Map<String,Object> param) throws Exception {
        Map<String, Object> roleMap = new HashMap<>();
        String companyUserId = (String) param.get("companyUserId");
        String accountId = (String) param.get("accountId");
        String companyId = (String) param.get("currentCompanyId");
        String isFinancial = (param.containsKey("isFinancial")?(String)param.get("isFinancial"):"0");
       // if(permissionService.isFinancialReceive(companyId,accountId) || permissionService.isFinancial(companyId,accountId)){
        if("1".equals(isFinancial)){//如果是财务
            Map<String, Object> map = new HashMap<>();
            map.put("targetId", costDetailId);
            map.put("companyId", companyId);
            List<MyTaskEntity> myTaskList = this.myTaskService.getMyTaskByParamter(map);
            if(!CollectionUtils.isEmpty(myTaskList)){
                for(MyTaskEntity task:myTaskList){
                    if(task.getTaskType()==29){
                        roleMap.put("invoiceConfirm",task.getId());//发票确认
                    }else {
                        roleMap.put("financialForFee",task.getId());//财务到账，付款
                    }
                }
               // MyTaskEntity task = myTaskList.get(0);//理论上只会存在一条
            }
        }

        return roleMap;
    }


    /**
     * 方法描述：查询合同回款(map:projectId)map.put("type"="4"：付款，5：收款);
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    @Override
    public AjaxMessage getOtherFee(Map<String, Object> map) throws Exception {
        String cpyId = (String) map.get("companyId");//当前组织
        this.setCurrentTaskRealCompanyId(map);
        String companyId= (String) map.get("companyId");//当前费用所属的组织
        String type = map.get("type").toString();
        String accountId = (String) map.get("accountId");
        String isManager = "0";
        String isFinancal = "0";
        String companyUserId = (String)map.get("companyUserId");
        if(null!=companyUserId && !"".equals(companyUserId)){
            isManager = this.getManagerFlag((String) map.get("projectId"), cpyId,companyUserId);
        }
        if("4".equals(type)){
            //查询是否存在
            map.put("type","4");
            map.put("fromCompanyId",companyId);
            if(permissionService.isFinancial(cpyId,accountId)){
                isFinancal = "1";
            }
        }else {
            map.put("type","5");
            map.put("toCompanyId",companyId);
            if(permissionService.isFinancialReceive(cpyId,accountId)){
                isFinancal = "1";
            }
        }
        Map<String, Object> result = this.getReviewFeeInfo(map,type);
        result.put("isManager",isManager);
        result.put("isFinancal",isFinancal);
        return AjaxMessage.succeed("查询成功").setData(result);
    }

    /**
     * 方法描述：删除费用（目前界面上没有删除操作。用于删除签发的任务时候，如果不存在签发的记录，则合作设计费删除）
     * 作者：MaoSF
     * 日期：2017/3/2
     */
    @Override
    public AjaxMessage deleteProjectCost(String id, String accountId) throws Exception {

        Map<String,Object> map = new HashMap<>();
        map.put("costId",id);
        List<ProjectCostPointDTO> projectCostPointList = this.projectCostPointDao.selectByParam(map);
        for(ProjectCostPointDTO pointDTO : projectCostPointList){
            this.deleteProjectCostPoint(pointDTO.getId(),accountId);
        }

        ProjectCostEntity costEntity = new ProjectCostEntity();
        costEntity.setId(id);
        costEntity.setUpdateBy(accountId);
        costEntity.setStatus("1");
        this.projectCostDao.updateById(costEntity);
        return AjaxMessage.succeed("删除成功");
    }

    /**
     * 方法描述：删除费用节点
     * 作者：MaoSF
     * 日期：2017/3/2
     */
    @Override
    public AjaxMessage deleteProjectCostPoint(String id, String companyUserId) throws Exception {
        ProjectCostPointEntity entity = this.projectCostPointDao.selectById(id);
        if (entity != null) {//有可能不是签发节点，所以entity可能为null
            //添加项目动态
            dynamicService.addDynamic(entity,(ProjectCostPointEntity)null,companyUserId);
            List<ProjectCostPointDetailEntity> list = this.projectCostPointDetailDao.getCostPointDetailByPointId(id);
            this.projectCostPointDao.updateByPid(id);
            entity.setStatus("1");
            this.projectCostPointDao.updateById(entity);
            //忽略任务
            for (ProjectCostPointDetailEntity entity1 : list) {
                deleteProjectCostPointDetail(entity1.getId(),companyUserId,false);
                this.myTaskService.ignoreMyTask(entity1.getId());
                messageService.deleteMessage(entity1.getId());
            }
            messageService.deleteMessage(id);
        }
        return AjaxMessage.succeed("删除成功");
    }

    /**
     * 方法描述：删除发起收款明细节点
     * 作者：MaoSF
     * 日期：2017/3/2
     */
    @Override
    public AjaxMessage deleteProjectCostPointDetail(String id,String companyUserId) throws Exception {
        return deleteProjectCostPointDetail(id,companyUserId,true);
    }

    @Override
    public AjaxMessage deleteProjectCostPointDetail(String id, String companyUserId, Boolean isAddDynamic) throws Exception {
        if(isAddDynamic) {//此函数在删除费用节点时也会调用，此时不应写入动态
            ProjectCostPointDetailEntity origin = projectCostPointDetailDao.selectById(id);
            dynamicService.addDynamic(origin,(ProjectCostPointDetailEntity)null,companyUserId);
        }
        String accountId = zInfoDAO.getUserIdByCompanyUserId(companyUserId); //获取userId
        //逻辑删除（发起收款）
        ProjectCostPointDetailEntity pointDetailEntity = new ProjectCostPointDetailEntity();
        pointDetailEntity.setId(id);
        pointDetailEntity.setUpdateBy(accountId);
        pointDetailEntity.setStatus("1");//逻辑删除的标示
        this.projectCostPointDetailDao.updateById(pointDetailEntity);

        //逻辑删除收款的明细及相关个人任务
        List<ProjectCostPaymentDetailEntity> list = this.projectCostPaymentDetailDao.listPaymentByDetailId(id);
        for (ProjectCostPaymentDetailEntity entity1 : list) {
            this.myTaskService.ignoreMyTask(entity1.getId());
            messageService.deleteMessage(entity1.getId());
            //删除组织账目
            companyBillService.deleteCompanyBill(entity1.getId());
        }
        ProjectCostPaymentDetailEntity paymentDetailEntity = new ProjectCostPaymentDetailEntity();
        paymentDetailEntity.setUpdateBy(accountId);
        paymentDetailEntity.setStatus("1");//逻辑删除的标示
        paymentDetailEntity.setPointDetailId(id);
        this.projectCostPaymentDetailDao.updateCostPaymentDetailByPointDetailId(paymentDetailEntity);
        // this.projectCostOperaterDao.deleteByCostDetailId(id);

        //忽略明细款项相关个人任务
        this.myTaskService.ignoreMyTask(id);
        //删除消息
        this.messageService.deleteMessage(id);
        return AjaxMessage.succeed("删除成功");
    }

    /**
     * 方法描述：删除收款明细节点
     * 作者：MaoSF
     * 日期：2017/4/27
     */
    @Override
    public AjaxMessage deleteProjectCostPaymentDetail(String id, String companyUserId) throws Exception {
        //逻辑删除收款的明细
        ProjectCostPaymentDetailEntity paymentDetailEntity = this.projectCostPaymentDetailDao.selectById(id);
        CompanyUserEntity u = companyUserDao.selectById(companyUserId);
        if(paymentDetailEntity==null || u==null){
            return AjaxMessage.failed("删除失败");
        }
        //保存项目动态
        dynamicService.addDynamic(paymentDetailEntity,null,companyUserId);

        paymentDetailEntity.setUpdateBy(u.getUserId());
        paymentDetailEntity.setStatus("1");//逻辑删除的标示
        this.projectCostPaymentDetailDao.updateById(paymentDetailEntity);

        //删除账目
        companyBillService.deleteCompanyBill(id);

        //this.projectCostOperaterDao.deleteByCostDetailId(id);

        //忽略任务
        this.myTaskService.ignoreMyTask(id);
        //删除消息
        this.messageService.deleteMessage(id);

        //处理是否触发重新发起任务
        this.handleSendMyTaskForChangeProjectCostPayment(paymentDetailEntity,u.getUserId(),u.getCompanyId());

        return AjaxMessage.succeed("删除成功");
    }

    /**
     * 方法描述：处理删除收款明细，是否重新触发任务发送给（合同回款-财务人员，技术审查费--
     * 作者：MaoSF
     * 日期：2017/4/27
     */
    private void handleSendMyTaskForChangeProjectCostPayment(ProjectCostPaymentDetailEntity paymentDetailEntity,String accountId,String currentCompanyId) throws Exception{
        if (paymentDetailEntity == null){
            return;
        }
        createPaymentTask(paymentDetailEntity.getPointDetailId(),accountId,currentCompanyId);
    }


    /**
     * 方法描述：查找是否存在确认付款/确认付款任务，如果没有且需要确认付款/到款，添加一条任务
     * 作者：ZCL
     * 日期：2017/5/4
     */
    private void createPaymentTask(String detailId,String accountId,String currentCompanyId) throws Exception{
        if (detailId == null){
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("targetId", detailId);
        List<MyTaskEntity> myTaskList = this.myTaskService.getMyTaskByParamter(map);
        ProjectCostPointDetailEntity pointDetailEntity = this.projectCostPointDetailDao.selectById(detailId);
        double paid = projectCostPaymentDetailDao.getSumFee(detailId);
        if(CollectionUtils.isEmpty(myTaskList)){
            if(pointDetailEntity!=null) {
                if ((CommonUtil.doubleCompare(paid,pointDetailEntity.getFee().doubleValue())) < 0) {
                    ProjectCostEntity costEntity = this.projectCostDao.getProjectCostByPointId(pointDetailEntity.getPointId());
                    if (costEntity != null) {
                        //合同回款
                        if ("1".equals(costEntity.getType())) {
                            ProjectEntity projectEntity = this.projectDao.selectById(costEntity.getProjectId());
                            this.myTaskService.saveMyTask(detailId, SystemParameters.CONTRACT_FEE_PAYMENT_CONFIRM, projectEntity.getCompanyId(),accountId,currentCompanyId);
                        }
                        //技术审查费
                        if ("2".equals(costEntity.getType())) {
                            //给立项组织发起确认信息
                            ProjectEntity projectEntity = this.projectDao.selectById(costEntity.getProjectId());
                            this.myTaskService.saveMyTask(detailId, SystemParameters.TECHNICAL_REVIEW_FEE_OPERATOR_MANAGER, projectEntity.getCompanyId(),accountId,currentCompanyId);
                        }
                        //合作设计费
                        if ("3".equals(costEntity.getType())) {
                            //给发包人发起确认信息
                            this.myTaskService.saveMyTask(detailId, SystemParameters.COOPERATIVE_DESIGN_FEE_ORG_MANAGER, costEntity.getFromCompanyId(),accountId,currentCompanyId);
                        }
                        //其他费用付款
                        if ("4".equals(costEntity.getType())) {
                            this.myTaskService.saveMyTask(detailId, SystemParameters.OTHER_FEE_FOR_PAY, costEntity.getFromCompanyId(),accountId,currentCompanyId);
                        }
                        //其他费用收款
                        if ("5".equals(costEntity.getType())) {
                            this.myTaskService.saveMyTask(detailId, SystemParameters.OTHER_FEE_FOR_PAID, costEntity.getToCompanyId(),accountId,currentCompanyId);
                        }
                    }
                }
            }
        }else {
            if ((CommonUtil.doubleCompare(paid,pointDetailEntity.getFee().doubleValue())) >= 0) {
                //此处为经营负责人，把付款或许收款金额修改比原来大的时候调用
                //把任务设置为完成
                //理论上，该种任务只会存在一条
                for(MyTaskEntity myTaskEntity:myTaskList){
                    if(myTaskEntity.getTaskType()==SystemParameters.TECHNICAL_REVIEW_FEE_OPERATOR_MANAGER || myTaskEntity.getTaskType()==SystemParameters.COOPERATIVE_DESIGN_FEE_ORG_MANAGER){
                        myTaskEntity.setStatus("1");
                        this.myTaskService.updateById(myTaskEntity);
                    }
                }
            }
        }
    }


    /**
     * 方法描述：合同乙方更改技术审查费
     * 作者：MaoSF
     * 日期：2017/3/2
     * @param:flag(1:重新添加，2.全部删除，4.先删除后添加）此处3，在原有的代码中处理
     */
    @Override
    public AjaxMessage handPartBChange(String projectId, String accountId, int flag) throws Exception {

        //以下内容与 2018-07-16 屏蔽。原因：取消 添加乙方与技术审查费的关联关系
//        if (flag != 0) {
//            if (flag == 1) {
//                //saveTechnicalReviewFeePoint(projectId, "2");
//            }
//            if (flag == 2) {
//                deleteTechnicalFee(projectId,"2");
//                deletePoint(projectId, "2",accountId);
//            }
//            if (flag == 4) {
//                deleteTechnicalFee(projectId,"2");
//                deletePoint(projectId, "2",accountId);
//               // saveTechnicalReviewFeePoint(projectId, "2");
//            }
//        }
        return null;
    }


    private void deleteTechnicalFee(String projectId,String type){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("projectId",projectId);
        map.put("type",type);
        List<ProjectCostDTO> list = this.projectCostDao.selectByParam(map);
        for(ProjectCostDTO dto:list){
            ProjectCostEntity projectCost = new ProjectCostEntity();
            projectCost.setId(dto.getId());
            projectCost.setStatus("1");
            this.projectCostDao.updateById(projectCost);
        }
    }
    private void deletePoint(String projectId, String type,String accountId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("projectId", projectId);
        map.put("type", type);
        List<ProjectCostPointEntity> list = projectCostPointDao.selectByType(map);
        for (ProjectCostPointEntity entity : list) {
            deleteProjectCostPoint(entity.getId(),accountId);
        }
    }


    /***************************=============技术审查==============**************/

    /**
     * 方法描述：验证合作设计费
     * 作者：MaoSF
     * 日期：2017/3/12
     */
    @Override
    public AjaxMessage validateTechnicalFee(ProjectCostPointDTO projectCostPointDTO) throws Exception {
        if(StringUtil.isNullOrEmpty(projectCostPointDTO.getFee())){
            return AjaxMessage.succeed(null);
        }
        ProjectCostPointEntity pointEntity= null;
        String pointPid = projectCostPointDTO.getPid();
        double fee = 0;
        //如果是修改，则先查询出原来的数据
        if (!StringUtil.isNullOrEmpty(projectCostPointDTO.getId())) {
            pointEntity = this.projectCostPointDao.selectById(projectCostPointDTO.getId());
            pointPid = pointEntity.getPid();
            fee =  pointEntity.getFee().doubleValue();
        }

        String typeMemo="总金额不能大于";
        double total = 0;
        //查询子节点的总金额
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("projectId", projectCostPointDTO.getProjectId());
        map.put("type", projectCostPointDTO.getType());
        if ("3".equals(projectCostPointDTO.getType())) {
            map.put("costId", projectCostPointDTO.getCostId());
        }

        if (!StringUtil.isNullOrEmpty(pointPid)) {
            ProjectCostPointEntity projectCostPoint = this.projectCostPointDao.selectById(pointPid);
            if (projectCostPoint.getFee() != null) {
                total = projectCostPoint.getFee().doubleValue();
            }
            map.put("pid", pointPid);
        } else {
            //如果是技术审查费，其他费用，第一级是不需要验证总金额 //type = 1,3暂时不做限制
            if ("1".equals(projectCostPointDTO.getType()) || "2".equals(projectCostPointDTO.getType()) || "3".equals(projectCostPointDTO.getType()) || "4".equals(projectCostPointDTO.getType()) || "5".equals(projectCostPointDTO.getType())) {
                //判断不能小于设置的子节点的总金额
                if(pointEntity!=null){
                    double pointTotalFee = this.projectCostPointDetailDao.getSumFee(pointEntity.getId());
                    if(projectCostPointDTO.getFee().doubleValue()<pointTotalFee){
                        return AjaxMessage.failed("金额不能小于" + pointTotalFee);
                    }
                }
                return AjaxMessage.succeed(null);
            }
            //查询总金额
            if ("3".equals(projectCostPointDTO.getType())) {
                ProjectCostEntity costEntity = this.projectCostDao.selectById(projectCostPointDTO.getCostId());
                if (costEntity.getFee() != null) {
                    total = costEntity.getFee().doubleValue();
                }
                map.put("costId", costEntity.getId());
                map.put("pidIsNull", "1");
            } else {//如果是合同回款
                List<ProjectCostDTO> costDTOs = this.projectCostDao.selectByParam(map);
                if (!CollectionUtils.isEmpty(costDTOs)) {
                    if (null != costDTOs.get(0).getFee()) {
                        total = costDTOs.get(0).getFee().doubleValue();
                    }
                }
            }
        }

        if(total==0){
            return AjaxMessage.failed("请先设置总金额");
        }
        //判断不能大于设置的总金额
        Double totalFee = this.projectCostPointDao.getTotalFee(map);
        if (!StringUtil.isNullOrEmpty(projectCostPointDTO.getFee())) {
            Double d = (totalFee + projectCostPointDTO.getFee().doubleValue() - fee);
            if (CommonUtil.doubleCompare(d,total) > 0) {
                return AjaxMessage.failed(typeMemo + total);
            }
        }



        return AjaxMessage.succeed(null);
    }

    /**
     * 方法描述：验证收款(如果是新增的话，originalFee默认为0)
     * 作者：MaoSF
     * 日期：2017/4/26
     */
    public AjaxMessage validateSaveCostPaymentDetail(ProjectCostPaymentDetailDTO dto,BigDecimal pointFee,BigDecimal originalFee,String feeType) throws Exception{

        if(pointFee==null){
            return AjaxMessage.failed("操作失败");
        }

        if(dto.getFee()!=null){//当财务到款，付款是不需要传递fee的
            double sumFee = this.projectCostPaymentDetailDao.getSumFee(dto.getPointDetailId());
            if (CommonUtil.doubleCompare((sumFee + dto.getFee().doubleValue()-originalFee.doubleValue()) ,pointFee.doubleValue()) > 0) {

                String errorMsg = "";
                if("1".equals(feeType) || "5".equals(feeType)){
                    errorMsg = "收款总金额不能大于";
                }
                if("2".equals(feeType) || "3".equals(feeType) || "4".equals(feeType)){
                    errorMsg = "付款总金额不能大于";
                }
                return AjaxMessage.failed(errorMsg+StringUtil.getRealData(pointFee));
            }
        }

        return null;
    }

    @Override
    public AjaxMessage saveCostPaymentDetail(ProjectCostPaymentDetailDTO dto) throws Exception {

        Integer operateFlag = dto.getOperateFlag();
        boolean isInsert = false;
        boolean isSaveAdverseFinancial = false;
        BigDecimal originalFee = new BigDecimal("0");
        if(!StringUtil.isNullOrEmpty(dto.getId())) {//存在ID修改
            ProjectCostPaymentDetailEntity paymentDetail = this.projectCostPaymentDetailDao.selectById(dto.getId());
            if(paymentDetail!=null){
                originalFee = paymentDetail.getFee();
                //防止在任务调用方，没有传递pointDetailId
                dto.setPointDetailId(paymentDetail.getPointDetailId());
            }
        }
        ProjectCostPointDetailEntity pointDetail = this.projectCostPointDetailDao.selectById(dto.getPointDetailId());
        if(pointDetail==null){
            return AjaxMessage.failed("操作失败");
        }
        ProjectCostPointEntity costPoint = this.projectCostPointDao.selectById(pointDetail.getPointId());
        if(pointDetail==null){
            return AjaxMessage.failed("操作失败");
        }
        ProjectCostEntity cost = projectCostDao.selectById(costPoint.getCostId());
        if(cost==null){
            return AjaxMessage.failed("操作失败");
        }
        //验证
        AjaxMessage ajax = this.validateSaveCostPaymentDetail(dto,pointDetail.getFee(),originalFee,costPoint.getType());
        if(ajax != null){
            return ajax;
        }
        int res = 0;
        ProjectCostPaymentDetailEntity entity = new ProjectCostPaymentDetailEntity();
        BaseDTO.copyFields(dto, entity);
        if(!StringUtil.isNullOrEmpty(dto.getId())){//存在ID修改
            ProjectCostPaymentDetailEntity origin = projectCostPaymentDetailDao.selectById(entity.getId()); //保存原有数据
            entity.setUpdateBy(dto.getAccountId());
            //保存项目动态
            dynamicService.addDynamic(origin,entity,dto.getCurrentCompanyId(),dto.getAccountId());
            //保存操作
            Boolean isReceive = operateFlag==1?true:false;
            String payDate = null;
            if(!StringUtil.isNullOrEmpty(dto.getPaidDate())){
                payDate = dto.getPaidDate();
            }
            if(!StringUtil.isNullOrEmpty(dto.getPayDate())){
                payDate = dto.getPayDate();
            }

            isSaveAdverseFinancial = handleAdverseFinancialAccount(entity.getId());
            if(isSaveAdverseFinancial || !isReceive){
                //判断余额付款方的余额
                validateBalance(cost.getFromCompanyId(),origin.getFee(),payDate);
            }
            isInsert = this.saveProjectCostOperater(entity.getId(),operateFlag,dto.getCurrentCompanyUserId(),dto.getFee(),dto.getAccountId());
            if(isInsert){
                this.financialAccount(costPoint,dto,isReceive,entity.getId(),dto.getFee());
            }
            if(isSaveAdverseFinancial){
                isReceive = !isReceive;
                isInsert = this.saveProjectCostOperater(entity.getId(),operateFlag,dto.getCurrentCompanyUserId(),dto.getFee(),dto.getAccountId());
                if(isInsert){
                    this.financialAccount(costPoint,dto,isReceive,entity.getId(),dto.getFee());
                }
            }
            if(isSaveAdverseFinancial){ //当前财务如果是 技术审查费/合作设计费 双方的共同财务，则同时把付款信息默认记录到系统
                if(!StringUtil.isNullOrEmpty(entity.getPayDate()) && StringUtil.isNullOrEmpty(entity.getPaidDate())){
                    entity.setPaidDate(entity.getPayDate());
                }else if(!StringUtil.isNullOrEmpty(entity.getPaidDate()) && StringUtil.isNullOrEmpty(entity.getPayDate())){
                    entity.setPayDate(entity.getPaidDate());
                }
            }
            res = projectCostPaymentDetailDao.updateById(entity);
        }else{//添加
            //如果是合同回款，其他费用收款
            boolean isReceive = operateFlag==1?true:false;
            boolean isInnerCompany = this.isInnerCompany(cost);
            String paidDate = dto.getDateStr()==null?DateUtils.date2Str(DateUtils.date_sdf): dto.getDateStr();
            if(isInnerCompany){
                entity.setPaidDate(paidDate);
                entity.setPayDate(paidDate);
            }else {
                if(isReceive){
                    entity.setPaidDate(paidDate);
                }else {
                    entity.setPayDate(paidDate);
                    //判断余额付款方的余额
                    validateBalance(cost.getFromCompanyId(),dto.getFee(),dto.getPayDate());
                }
            }
            entity.setId(StringUtil.buildUUID());
            entity.setCreateBy(dto.getAccountId());
            entity.setProjectId(costPoint.getProjectId());
            res = projectCostPaymentDetailDao.insert(entity);
            //保存项目日志
            dynamicService.addDynamic(null,entity,dto.getCurrentCompanyId(),dto.getAccountId());
            //保存操作
            isInsert = this.saveProjectCostOperater(entity.getId(),operateFlag,dto.getCurrentCompanyUserId(),dto.getFee(),dto.getAccountId());
            if(operateFlag<3){
                if(isInsert){
                    this.financialAccount(costPoint,dto,isReceive,entity.getId(),dto.getFee());
                }
            }
            //如果是内部组织，把对方的财务也保存一份数据
            if(operateFlag<3 && isInnerCompany){
                operateFlag = operateFlag==1?2:1;//此处取反
                isInsert = this.saveProjectCostOperater(entity.getId(),operateFlag,dto.getCurrentCompanyUserId(),dto.getFee(),dto.getAccountId());
                if(isInsert){
                    this.financialAccount(costPoint,dto,!isReceive,entity.getId(),dto.getFee());
                }
            }
        }
        //财务到款，付款给企业负责人和经营负责人推送消息
        this.sendMessage(cost,pointDetail,entity,dto,isSaveAdverseFinancial);
        if(res>0){
            Map<String,Object> map = new HashMap<>();
            map.put("costId",costPoint.getCostId());
            map.put("paymentDetailId",entity.getId());
            map.put("pointDetailId",pointDetail.getId());
            map.put("pointId",costPoint.getId());
            return AjaxMessage.succeed("操作成功").setData(map);
        }else{
            return AjaxMessage.error("操作失败");
        }
    }

    /**
     * 财务处理发票信息（发票确认）
     */
    @Override
    public AjaxMessage saveCostPointDetailForInvoice(InvoiceEditDTO dto) throws Exception {
        ProjectCostPointDetailEntity pointDetail = this.projectCostPointDetailDao.selectById(dto.getPointDetailId());
        ProjectCostEntity costEntity = this.projectCostDao.getProjectCostByPointId(pointDetail.getPointId());
        if(pointDetail==null || costEntity==null){
            return AjaxMessage.error("参数错误");
        }
        //处理PaidFeeStatus = 1
        pointDetail.setPaidFeeStatus(ProjectCostConst.FEE_STATUS_APPROVE);
        this.projectCostPointDetailDao.updateById(pointDetail);
        //处理发票信息
        String invoice = pointDetail.getInvoice();
        dto.setId(invoice);
        invoiceService.saveInvoice(dto);
        //处理任务
        ProjectCostPointDetailDTO pointDetailDTO = new ProjectCostPointDetailDTO();
        BeanUtils.copyProperties(pointDetail,pointDetailDTO);//把基本参数复制进去即可
        pointDetailDTO.setPointId(pointDetail.getPointId());
        pointDetailDTO.setCurrentCompanyId(dto.getCurrentCompanyId());
        pointDetailDTO.setAccountId(dto.getAccountId());
        if(!this.isInnerCompany(costEntity)){//如果不是内部组织，推送任务
            this.sendMyTask(dto.getPointDetailId(),pointDetailDTO);
        }
        return AjaxMessage.succeed("操作成功");
    }

    private void validateBalance(String companyId,BigDecimal fee,String payDate) throws Exception{
        fee = fee.multiply(new BigDecimal("10000"));
        if(!companyBalanceService.isCanBeAllocate(companyId,fee.toString(),payDate)){
            //抛异常
            throw new CustomException("当前支出的金额不能大于账目余额与最低余额的差值");
        }
    }

    /**
     * 方法描述：修改付款或到款明细
     * 作者：wrb
     * 日期：2017/4/26
     */
    @Override
    public AjaxMessage updateCostPaymentDetail(ProjectCostPaymentDetailDTO dto) throws Exception {
        BigDecimal originalFee = new BigDecimal("0");
        ProjectCostPaymentDetailEntity origin = null;
        if(!StringUtil.isNullOrEmpty(dto.getId())) {//存在ID修改
            ProjectCostPaymentDetailEntity paymentDetail = this.projectCostPaymentDetailDao.selectById(dto.getId());
            if(paymentDetail!=null){
                //保存原有数据
                origin = new ProjectCostPaymentDetailEntity();
                BeanUtilsEx.copyProperties(paymentDetail,origin);

                originalFee = paymentDetail.getFee();
                //防止在任务调用方，没有传递pointDetailId
                dto.setPointDetailId(paymentDetail.getPointDetailId());
            }
        }
        ProjectCostPointDetailEntity pointDetail = this.projectCostPointDetailDao.selectById(dto.getPointDetailId());
        if(pointDetail==null){
            return AjaxMessage.failed("操作失败");
        }
        ProjectCostPointEntity costPoint = this.projectCostPointDao.selectById(pointDetail.getPointId());
        if(pointDetail==null){
            return AjaxMessage.failed("操作失败");
        }
        //验证
        AjaxMessage ajax = this.validateSaveCostPaymentDetail(dto,pointDetail.getFee(),originalFee,costPoint.getType());
        if(ajax != null){
            return ajax;
        }

        ProjectCostPaymentDetailEntity entity = new ProjectCostPaymentDetailEntity();
        BaseDTO.copyFields(dto, entity);
        entity.setUpdateBy(dto.getAccountId());
        projectCostPaymentDetailDao.updateById(entity);

        //保存项目日志
        dynamicService.addDynamic(origin,entity,dto.getCurrentCompanyId(),dto.getAccountId());
        //处理任务
        handleSendMyTaskForChangeProjectCostPayment(entity,dto.getAccountId(),dto.getCurrentCompanyId());

        return AjaxMessage.succeed("操作成功");
    }


    private boolean saveProjectCostOperater(String paymentDetailId,Integer operateFlag,String companyUserId,BigDecimal fee,String accountId){
        ProjectCostOperaterEntity costOperaterEntity = new ProjectCostOperaterEntity();
        costOperaterEntity.setId(StringUtil.buildUUID());
        costOperaterEntity.setCompanyUserId(companyUserId);
        costOperaterEntity.setCostDetailId(paymentDetailId);//记录到款的数据的id
        costOperaterEntity.setCreateBy(accountId);
        if(operateFlag==3){//为了兼容经营负责人，所有使用Boolean
            costOperaterEntity.setType("3");//经营负责人付款确认
        }else {
            if(operateFlag==1){
                costOperaterEntity.setType("5");//到款类型
            }else {
                costOperaterEntity.setType("6");//财务付款
            }
        }
        //理论上，这种操作只会是一次，为了防止数据库中的数据错误，检查再处理
        List<ProjectCostOperaterEntity> list = this.projectCostOperaterDao.selectByType(costOperaterEntity);
        if(CollectionUtils.isEmpty(list)){
            this.projectCostOperaterDao.insert(costOperaterEntity);
            return true;
        }else {
            costOperaterEntity = list.get(0);
            costOperaterEntity.setCompanyUserId(companyUserId);
            this.projectCostOperaterDao.updateById(costOperaterEntity);
            return false;
        }
    }

    private void financialAccount(ProjectCostPointEntity costPoint,ProjectCostPaymentDetailDTO dto,boolean isReceive,String paymentDetailId,BigDecimal fee) throws Exception{
        SaveCompanyBillDTO billDTO = new SaveCompanyBillDTO();
        String paymentDate = StringUtil.isNullOrEmpty(dto.getPaidDate())?dto.getPayDate():dto.getPaidDate();
        if(dto.getDateStr()!=null){
            paymentDate = dto.getDateStr();
        }
        ProjectCostEntity cost = projectCostDao.selectById(costPoint.getCostId());
        billDTO.setFromCompanyId(cost.getFromCompanyId());
        billDTO.setToCompanyId(cost.getToCompanyId());
        billDTO.setFee(fee);
        ProjectCostPaymentDetailEntity payment = null;
        if(fee==null){
            payment = this.projectCostPaymentDetailDao.selectById(paymentDetailId);
            billDTO.setFee(payment.getFee());
        }

        if(isReceive){
            billDTO.setPayType(CompanyBillType.DIRECTION_PAYEE);
            billDTO.setCompanyId(cost.getToCompanyId());
        }else {
            billDTO.setPayType(CompanyBillType.DIRECTION_PAYER);
            billDTO.setCompanyId(cost.getFromCompanyId());
        }
        billDTO.setFeeType(Integer.valueOf(cost.getType()));
        if("5".equals(cost.getType())){ //如果类型是5，则属于其他费用
            billDTO.setFeeType(CompanyBillType.FEE_TYPE_OTHER);
        }

        billDTO.setBillDescription(costPoint.getFeeDescription());
        billDTO.setProjectId(cost.getProjectId());
        billDTO.setOperatorId(dto.getCurrentCompanyUserId());
        billDTO.setPaymentDate(paymentDate);
        billDTO.setTargetId(paymentDetailId);
        companyBillService.saveCompanyBill(billDTO);
    }

    boolean handleAdverseFinancialAccount(String paymentId) throws Exception{
        Map<String,Object> param = new HashMap<>();
        param.put("targetId",paymentId);
        param.put("ignoreStatus","1");//忽略status的状态值，因为getMyTaskByParamter中默认status = 0
        List<MyTaskEntity> taskList = this.myTaskService.getMyTaskByParamter(param);
        if(!CollectionUtils.isEmpty(taskList) && taskList.size()<2){
            return true;
        }
        return false;
    }

    private void sendMessage(ProjectCostEntity cost,ProjectCostPointDetailEntity pointDetail,ProjectCostPaymentDetailEntity paymentDetail,ProjectCostPaymentDetailDTO dto,boolean isSaveAdverseFinancial) throws Exception{
        //财务到款，付款给企业负责人和经营负责人推送消息
        sendMessageToOperatorAndOrgAdminForTaskType(cost,paymentDetail.getId(),pointDetail.getPointId(),dto.getTaskType(),dto.getAccountId(),dto.getCurrentCompanyId());
        if(isSaveAdverseFinancial){
            int type = dto.getTaskType();
            if(dto.getTaskType()==SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAY){
                type = SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAID;
            }
            if(dto.getTaskType()==SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAID){
                type = SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAY;
            }
            if(dto.getTaskType()==SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAY){
                type = SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAID;
            }
            if(dto.getTaskType()==SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAID){
                type = SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAY;
            }
            sendMessageToOperatorAndOrgAdminForTaskType(cost,paymentDetail.getId(),pointDetail.getPointId(),type,dto.getAccountId(),dto.getCurrentCompanyId());
        }
    }
    /**
     * 方法描述：财务到款确认，给企业负责人和经营负责人推送信息
     * 作者：MaoSF
     * 日期：2017/3/17
     */
    private void sendMessageToOperatorAndOrgAdminForTaskType(ProjectCostEntity cost ,String paymentDetailId,String pointId,int taskType,String accountId,String currentCompanyId) throws Exception{
        int type = 0;
        String companyId = null;
        String projectId = cost.getProjectId();
        if(taskType==8){
            type = SystemParameters.MESSAGE_TYPE_13;
            companyId = cost.getToCompanyId();
        }
        if(taskType==9){
            type = SystemParameters.MESSAGE_TYPE_16;
            companyId = cost.getToCompanyId();
        }
        if(taskType==10){
            type = SystemParameters.MESSAGE_TYPE_18;
            companyId = cost.getToCompanyId();
        }
        if(taskType==16 || taskType ==30){
            type = SystemParameters.MESSAGE_TYPE_27;
            companyId = cost.getFromCompanyId();
        }
        if(taskType==17 || taskType ==31){
            type = SystemParameters.MESSAGE_TYPE_28;
            companyId = cost.getToCompanyId();
        }
        if(taskType==18 || taskType ==32){
            type = SystemParameters.MESSAGE_TYPE_29;
            companyId = cost.getFromCompanyId();
        }
        if(taskType==19 || taskType ==33){
            type = SystemParameters.MESSAGE_TYPE_30;
            companyId = cost.getToCompanyId();
        }
        if(taskType==20){
            type = SystemParameters.MESSAGE_TYPE_33;
            companyId = cost.getFromCompanyId();
        }
        if(taskType==21){
            type = SystemParameters.MESSAGE_TYPE_34;
            companyId = cost.getToCompanyId();
        }
        if(taskType==307){//特定307，经营负责人收到内部组织的付款申请的信息
            type = SystemParameters.MESSAGE_TYPE_307;
            companyId = cost.getFromCompanyId();
        }
        if(type==0){
            return;//如果不是以上内容，只直接返回
        }
        ProjectMemberEntity managerEntity = this.projectMemberService.getOperatorManager(projectId,companyId);
        ProjectMemberEntity assist = this.projectMemberService.getOperatorAssistant(projectId,companyId);
        CompanyUserTableDTO orgManager = null;
        if(taskType!=307){
            orgManager = this.companyUserService.getOrgManager(companyId);
        }
        List<String> userIdList = new ArrayList<>();
        if (managerEntity != null) {
            if (!userIdList.contains(managerEntity.getAccountId())) {
                userIdList.add(managerEntity.getAccountId());
            }
        }
        if (assist != null) {
            if (!userIdList.contains(assist.getAccountId())) {
                userIdList.add(assist.getAccountId());
            }
        }
        if (orgManager != null) {
            if (!userIdList.contains(orgManager.getUserId())) {
                userIdList.add(orgManager.getUserId());
            }
        }
        List<String> userList = userIdList.stream()
                .filter(line -> !accountId.equals(line))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(userList)) {
            for (String userId : userList) {
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setProjectId(projectId);
                messageEntity.setCompanyId(companyId);
                messageEntity.setTargetId(paymentDetailId);
                messageEntity.setParam1(pointId);
                messageEntity.setUserId(userId);
                messageEntity.setMessageType(type);
                messageEntity.setCreateBy(accountId);
                messageEntity.setCreateDate(new Date());
                messageEntity.setSendCompanyId(currentCompanyId);
                this.messageService.sendMessage(messageEntity);
            }
        }
    }

    /**
     * 方法描述：合作设计费（技术审查费）详情
     * 作者：MaoSF
     * 日期：2017/3/9
     *
     * @param:map(pointDetailId,taskType)
     * @return:
     */
    @Override
    public ProjectCostPointDataForMyTaskDTO getProjectCostPointDetailForMyTask(String paymentDetailId,String pointDetailId,int taskType,String companyId) throws Exception {
        if(StringUtil.isNullOrEmpty(paymentDetailId) && StringUtil.isNullOrEmpty(pointDetailId)){
            return null;
        }
        ProjectCostPointDataForMyTaskDTO dataDTO = this.projectCostPointDao.getCostPointData(pointDetailId,paymentDetailId,companyId);
        if(dataDTO==null){
            return dataDTO;
        }
        BigDecimal paidFee = new BigDecimal("0");
        dataDTO.setUserName(getOperatorForCostDetail2(dataDTO.getType(),dataDTO.getPointDetailId()));
        //查询明细
        Map<String, Object> map = new HashMap<>();
        map.put("pointDetailId",dataDTO.getPointDetailId());
        List<ProjectCostPaymentDetailDTO> detailDTOS = this.projectCostPaymentDetailDao.selectByPointDetailId(map);
        for(ProjectCostPaymentDetailDTO detail:detailDTOS){
            if(taskType==4 || taskType==5 || taskType==6 || taskType==7){//经营负责人填写付款金额
                paidFee = paidFee.add(detail.getFee());
            }
            else if(taskType==10 || taskType==17 || taskType==19 || taskType==21){//财务到款
                if(!StringUtil.isNullOrEmpty(detail.getPaidDate())){
                    paidFee = paidFee.add(detail.getFee());
                }
            }else {//财务付款
                if(!StringUtil.isNullOrEmpty(detail.getPayDate())){
                    paidFee = paidFee.add(detail.getFee());
                }
            }
        }
        if(dataDTO.getPointDetailFee()!=null){
            dataDTO.setUnpaid(dataDTO.getPointDetailFee().subtract(paidFee));
        }
        dataDTO.setPaidFee(paidFee);
        return dataDTO;
    }

    /**
     * 方法描述：处理操作人
     * 作者：MaoSF
     * 日期：2017/3/6
     * @param:
     * @return:
     */
    private String getOperatorForCostDetail2(String type,String costDetailId) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("costDetailId",costDetailId);
        List<ProjectCostOperaterDTO> operaterDTOS = this.projectCostOperaterDao.getCostOperator(map);
        if("1".equals(type)  || "4".equals(type) || "5".equals(type)){//合同回款
            for(ProjectCostOperaterDTO dto:operaterDTOS){
                if("2".equals(dto.getType()) || "1".equals(dto.getType())){
                    return dto.getUserName();
                }
            }
        }
        if("2".equals(type) || "3".equals(type)){//技术审查费
            for(ProjectCostOperaterDTO dto:operaterDTOS){
                if("1".equals(dto.getType())){
                    return dto.getUserName();
                }
            }
        }
        return null;
    }

    /**
     * 方法描述：保存费用(合作设计费），先设置一条空的数据
     * 作者：MaoSF
     * 日期：2017/3/7
     */
    public void saveProjectCost(ProjectTaskEntity task, String currentCompanyId) throws Exception {
        //查询是否存在总金额
        //查询总费用
        //以下内容与 2018-07-16 屏蔽。原因：取消 任务签发与合作设计费的关联关系
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("projectId", task.getProjectId());
//        map.put("fromCompanyId", currentCompanyId);
//        map.put("toCompanyId", task.getCompanyId());
//        map.put("type", "3"); //代表合作设计费类型
//        map.put("flag", "1"); //代表正式合同
//        List<ProjectCostDTO> totalCost = this.projectCostDao.selectByParam(map);//理论上只会存在一条
//        String costId = null;
//        if (!CollectionUtils.isEmpty(totalCost)) {
//            costId = totalCost.get(0).getId();
//        } else {
//            costId = StringUtil.buildUUID();
//            ProjectCostEntity costEntity = new ProjectCostEntity();
//            costEntity.setId(costId);
//            costEntity.setProjectId(task.getProjectId());
//            costEntity.setFromCompanyId(currentCompanyId);
//            costEntity.setToCompanyId(task.getCompanyId());
//            costEntity.setType("3");
//            costEntity.setFlag("1");
//            this.projectCostDao.insert(costEntity);
//        }
    }

    /**
     * 描述     获取组织内各项目的收付款汇总列表
     * 日期     2018/8/10
     *
     * @param query 查询条件
     *              startDate 起始日期
     * @return ProjectCostSummaryDTO列表
     * @author 张成亮
     **/
    @Override
    public List<ProjectCostSummaryDTO> listProjectCostSummary(ProjectCostSummaryQueryDTO query) {
        //建立参与的项目的列表
        QueryProjectDTO projectQuery = BeanUtils.createFrom(query,QueryProjectDTO.class);
        List<ProjectSimpleDTO> projectList = projectDao.listProject(projectQuery);
        List<ProjectCostSummaryDTO> list = BeanUtils.createListFrom(projectList,ProjectCostSummaryDTO.class);

        //更新合同及到款信息、技术审查费及到款信息等具体信息，更新累计到账和累计付款信息
        updateProjectCostSummaryList(list,query);

        return list;
    }

    /**
     * 描述     分页获取组织内各项目的收付款汇总列表
     * 日期     2018/8/10
     *
     * @param query 查询条件
     *              startDate 起始日期
     * @return ProjectCostSummaryDTO列表
     * @author 张成亮
     **/
    @Override
    public CorePageDTO<ProjectCostSummaryDTO> listPageProjectCostSummary(ProjectCostSummaryQueryDTO query) {

        //获取项目分页显示信息
        QueryProjectDTO projectQuery = BeanUtils.createFrom(query,QueryProjectDTO.class);
        List<ProjectSimpleDTO> projectList = projectDao.listProject(projectQuery);
        int count = projectDao.getLastQueryCount();
        List<ProjectCostSummaryDTO> list = BeanUtils.createListFrom(projectList,ProjectCostSummaryDTO.class);

        //更新合同及到款信息、技术审查费及到款信息等具体信息，更新累计到账和累计付款信息
        updateProjectCostSummaryList(list,query);

        //建立分页返回信息
        CorePageDTO<ProjectCostSummaryDTO> page = new CorePageDTO<>();
        page.setTotal(count);
        page.setPageSize(DigitUtils.parseInt(query.getPageSize()));
        page.setPageIndex(DigitUtils.parseInt(query.getPageIndex()));
        page.setData(list);
        return page;
    }

    /**
     * 描述       分别累加到账和付款项，并更新项目费用表
     * 日期       2018/8/13
     * @author   张成亮
     **/
    private List<ProjectCostSummaryDTO> updateProjectCostSummaryList(List<ProjectCostSummaryDTO> summaryList){
        final int roundFor10k = 6;

        for(ProjectCostSummaryDTO summary : summaryList) {
            double gain = 0;
            gain += summary.getContractReal();
            gain += summary.getTechnicalReal();
            gain += summary.getCooperateGainReal();
            summary.setGainRealSummary(DigitUtils.round(gain,roundFor10k));

            double pay = 0;
            pay += summary.getCooperatePayReal();
            pay += summary.getPayExpense();
            pay += summary.getPayOther();
            summary.setPayRealSummary(DigitUtils.round(pay,roundFor10k));
        }
        return summaryList;
    }

    /**
     * 描述       更新合同及到款信息、技术审查费及到款信息等具体信息，更新累计到账和累计付款信息
     * 日期       2018/8/13
     * @author   张成亮
     **/
    private List<ProjectCostSummaryDTO> updateProjectCostSummaryList(List<ProjectCostSummaryDTO> summaryList,ProjectCostSummaryQueryDTO query){
        summaryList.forEach(project->{
            ProjectCostQueryDTO costQuery = new ProjectCostQueryDTO(query.getProjectId(),query.getCompanyId());

            //添加计划款项信息
            CostAmountDTO planAmount = projectCostDao.getCostAmountPlan(costQuery);
            if (planAmount != null) {
                //合同回款
                project.setContract(DigitUtils.parseDouble(planAmount.getContract()));
                //技术审查费收入
                project.setTechnical(DigitUtils.parseDouble(planAmount.getTechnicalGain()));
                //技术审查费支出
                //未定义
                //合作设计费收入
                project.setCooperateGain(DigitUtils.parseDouble(planAmount.getCooperateGain()));
                //合作设计费支出
                project.setCooperatePay(DigitUtils.parseDouble(planAmount.getCooperatePay()));
            }

            //添加实际款项信息
            CostAmountDTO realAmount = projectCostDao.getCostAmountReal(costQuery);
            if (planAmount != null) {
                //合同回款
                project.setContractReal(DigitUtils.parseDouble(realAmount.getContract()));
                //技术审查费收入
                project.setTechnicalReal(DigitUtils.parseDouble(realAmount.getTechnicalGain()));
                //技术审查费支出
                //未定义
                //合作设计费收入
                project.setCooperateGainReal(DigitUtils.parseDouble(realAmount.getCooperateGain()));
                //合作设计费支出
                project.setCooperatePayReal(DigitUtils.parseDouble(realAmount.getCooperatePay()));
            }
        });

        //在exp表内查询报销及费用信息，添加报销费用信息和费用信息
        List<ProjectExpSingleSummaryDTO> singleSummaryList = projectCostDao.listProjectExpSummary(query);
        if (ObjectUtils.isNotEmpty(singleSummaryList)){
            for(ProjectExpSingleSummaryDTO singleSummary : singleSummaryList) {
                for (ProjectCostSummaryDTO summary : summaryList) {
                    if (StringUtils.isSame(summary.getId(), singleSummary.getId())) {
                        summary.setPayExpense(singleSummary.getExpense());
                        summary.setPayOther(singleSummary.getCost());
                        break;
                    }
                }
            }
        }

        //计算累计到账和累计付款
        updateProjectCostSummaryList(summaryList);

        return summaryList;
    }

    /**
     * 描述       在Cost类的表内查询费用信息，并更新项目费用表
     * 日期       2018/8/13
     * @author   张成亮
     **/
    @Deprecated
    /** 在updateProjectCostSummaryList内实现 **/
    private List<ProjectCostSummaryDTO> updateProjectCostSummaryList(List<ProjectCostSummaryDTO> summaryList,ProjectCostSummaryQueryDTO query,int feeType){
        final String IS_DETAIL = "1";
        final String IS_NOT_DETAIL = "0";

        query.setCostType(feeType);
        query.setIsDetail(IS_DETAIL);
        List<ProjectCostSingleSummaryDTO> singleSummaryList = projectCostDao.listProjectCostSummary(query);
        if (ObjectUtils.isNotEmpty(singleSummaryList)){
            for(ProjectCostSingleSummaryDTO singleSummary : singleSummaryList) {
                for (ProjectCostSummaryDTO summary : summaryList) {
                    if (StringUtils.isSame(summary.getId(), singleSummary.getId())) {
                        if (feeType == ProjectCostConst.FEE_TYPE_CONTRACT) {
                            summary.setContract(singleSummary.getPlan());
                            summary.setContractReal(singleSummary.getReal());
                        } else if (feeType == ProjectCostConst.FEE_TYPE_TECHNICAL) {
                            summary.setTechnical(singleSummary.getPlan());
                            summary.setTechnicalReal(singleSummary.getReal());
                        } else if (feeType == ProjectCostConst.FEE_TYPE_COOPERATE_GAIN) {
                            summary.setCooperateGain(singleSummary.getPlan());
                            summary.setCooperateGainReal(singleSummary.getReal());
                        } else if (feeType == ProjectCostConst.FEE_TYPE_COOPERATE_PAY) {
                            summary.setCooperatePay(singleSummary.getPlan());
                            summary.setCooperatePayReal(singleSummary.getReal());
                        }
                        break;
                    }
                }
            }
        } else {
            query.setIsDetail(IS_NOT_DETAIL);
            singleSummaryList = projectCostDao.listProjectCostSummary(query);
            if (ObjectUtils.isNotEmpty(singleSummaryList)) {
                for(ProjectCostSingleSummaryDTO singleSummary : singleSummaryList) {
                    for (ProjectCostSummaryDTO summary : summaryList) {
                        if (StringUtils.isSame(summary.getId(), singleSummary.getId())) {
                            if (feeType == ProjectCostConst.FEE_TYPE_CONTRACT) {
                                summary.setContract(singleSummary.getPlan());
                            } else if (feeType == ProjectCostConst.FEE_TYPE_TECHNICAL) {
                                summary.setTechnical(singleSummary.getPlan());
                            } else if (feeType == ProjectCostConst.FEE_TYPE_COOPERATE_GAIN) {
                                summary.setCooperateGain(singleSummary.getPlan());
                            } else if (feeType == ProjectCostConst.FEE_TYPE_COOPERATE_PAY) {
                                summary.setCooperatePay(singleSummary.getPlan());
                            }
                            break;
                        }
                    }
                }
            }
        }
        return summaryList;
    }


    @Override
    public AjaxMessage completeProjectFeeApply(ProjectCostPointDetailDTO projectCostPointDetailDTO) throws Exception {
        RelationRecordEntity relationRecord = relationRecordService.getRelationRecord(projectCostPointDetailDTO.getMainId());
        if(relationRecord!=null){
            String pointDetailId = relationRecord.getRelationId();
            projectCostPointDetailDTO.setId(pointDetailId);
            ProjectCostPointDetailEntity pointDetailEntity = this.projectCostPointDetailDao.selectById(pointDetailId);
            ProjectCostEntity projectCost = this.projectCostDao.getProjectCostByPointId(pointDetailEntity.getPointId());
            projectCostPointDetailDTO.setCurrentCompanyId(projectCost.getFromCompanyId());//此处设置未付款组织的id
            projectCostPointDetailDTO.setAccountId(pointDetailEntity.getCreateBy());
            projectCostPointDetailDTO.setPointId(pointDetailEntity.getPointId());
            pointDetailEntity.setFeeStatus(projectCostPointDetailDTO.getFeeStatus());
            this.projectCostPointDetailDao.updateById(pointDetailEntity);
            if(projectCostPointDetailDTO.getFeeStatus()==ProjectCostConst.FEE_STATUS_APPROVE){
                this.sendMyTask(pointDetailId,projectCostPointDetailDTO);
            }
        }
        return null;
    }


    @Override
    public Map<String, Object> getProjectCostPaymentDetailByPointDetailIdForPay(ProjectCostQueryDTO queryDTO) throws Exception {
        if(StringUtil.isNullOrEmpty(queryDTO.getCompanyId())){
            queryDTO.setCompanyId(queryDTO.getCurrentCompanyId());
        }
        //查看详情
        ProjectCostPointDetailEntity pointDetailEntity = this.projectCostPointDetailDao.selectById(queryDTO.getPointDetailId());
        //审批记录
        Map<String,Object> auditInfo = expMainService.getAuditInfoByRelationId(queryDTO.getPointDetailId(),queryDTO.getCurrentCompanyUserId());
        //封装返回信息
        Map<String,Object> result = new HashMap<>();
        result.put("pointDetailInfo",pointDetailEntity);
        result.putAll(auditInfo);
        return result;
    }


    private Map<String,Object> getEditRole(String currentCompanyId,ProjectCostDataDTO costData,ProjectCostPointDetailInfoDTO pointDetailInfo,String isManager) {
        ProjectCostDTO cost = new ProjectCostDTO();
        BeanUtils.copyProperties(costData,cost);
        boolean  isInnerCompany = this.isInnerCompany(cost);
        Map<String,Object> result = new HashMap<>();
        result.put("startReceiveFlag",this.getStartReceiveFlag(currentCompanyId,costData,isManager));//发起回款,发起付款
        result.put("startPayFlag",this.getStartPayFlag(currentCompanyId,costData,isManager,isInnerCompany)); //发起付款申请（外部）
        if(pointDetailInfo!=null && pointDetailInfo.getFeeStatus()!=null && pointDetailInfo.getFeeStatus()==0){
            result.put("startPayFlagForInner",this.getStartPayFlagForInner(currentCompanyId,costData,isManager,isInnerCompany));  //发起付款申请（内部）
        }
        return result;
    }

    private ProjectCostDataDTO getProjectCost(ProjectCostQueryDTO queryDTO,ProjectCostDTO cost,boolean isSelectAttach) throws Exception{
        if(cost==null){
            ProjectCostEntity costEntity = this.projectCostDao.selectById(queryDTO.getCostId());
            cost = new ProjectCostDTO();
            BeanUtils.copyProperties(costEntity,cost);
        }
        queryDTO.setCostId(cost.getId());
        ProjectCooperatorCostDTO costDetail = this.projectCostDao.getProjectAmountFeeByCostId(queryDTO);
        ProjectCostDataDTO result = new ProjectCostDataDTO();
        if(costDetail!=null){
            BeanUtils.copyProperties(costDetail,result);
        }
        result.setRelationCompanyName(this.getRelationCompanyName(cost,queryDTO.getAppOrgId()));
        result.setCostId(cost.getId());
        result.setPlanFee(cost.getFee());
        result.setOperateCompanyId(cost.getOperateCompanyId());
        result.setToCompanyId(cost.getToCompanyId());
        result.setFromCompanyId(cost.getFromCompanyId());
        result.setType(cost.getType());
        result.setProjectName(this.projectDao.getProjectName(cost.getProjectId()));

        if(isSelectAttach){
            //获取附件
            result.setAttachList(projectSkyDriverService.getAttachListByTargetId(cost.getId()));
        }
        return result;
    }


    @Override
    public ProjectCostDataDTO getProjectCostByMainId(String mainId) throws Exception {
        ProjectCostDataDTO result = new ProjectCostDataDTO();
        RelationRecordEntity relationRecord = relationRecordService.getRelationRecord(mainId);
        if(relationRecord!=null){
            String pointDetailId = relationRecord.getRelationId();
            ProjectCostPointDetailEntity pointDetailEntity = this.projectCostPointDetailDao.selectById(pointDetailId);
            ProjectCostEntity costEntity = this.projectCostDao.getProjectCostByPointId(pointDetailEntity.getPointId());
            ProjectCostDTO cost = new ProjectCostDTO();
            BeanUtils.copyProperties(costEntity,cost);

            BeanUtils.copyProperties(costEntity,result);
            result.setCostId(cost.getId());
            result.setPlanFee(cost.getFee());
            result.setOperateCompanyId(cost.getOperateCompanyId());
            result.setToCompanyId(cost.getToCompanyId());
            result.setFromCompanyId(cost.getFromCompanyId());
            result.setType(cost.getType());
            result.setRelationCompanyName(this.getRelationCompanyName(cost,costEntity.getFromCompanyId()));
            result.setProjectName(this.projectDao.getProjectName(cost.getProjectId()));
            result.setTypeName(ProjectCostConst.COST_TYPE_MAP.get(cost.getType()));
            result.setDetailFee(pointDetailEntity.getFee());
        }
        return result;
    }
}
