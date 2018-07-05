package com.maoding.dynamic.service.impl;

import com.maoding.core.constant.NetFileType;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.core.util.BeanUtilsEx;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.dynamic.dao.DynamicDAO;
import com.maoding.dynamic.dao.ZInfoDAO;
import com.maoding.dynamic.dto.*;
import com.maoding.dynamic.entity.DynamicDO;
import com.maoding.dynamic.entity.NetFileDO;
import com.maoding.dynamic.service.DynamicService;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.entity.ProjectProcessNodeEntity;
import com.maoding.projectcost.entity.ProjectCostEntity;
import com.maoding.projectcost.entity.ProjectCostPaymentDetailEntity;
import com.maoding.projectcost.entity.ProjectCostPointDetailEntity;
import com.maoding.projectcost.entity.ProjectCostPointEntity;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.task.dto.TaskWithFullNameDTO;
import com.maoding.task.entity.ProjectProcessTimeEntity;
import com.maoding.task.entity.ProjectTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Chengliang.zhang on 2017/6/7.
 */
@Service("dynamicService")
public class DynamicServiceImpl implements DynamicService {
    @Autowired
    private DynamicDAO dynamicDAO;

    @Autowired
    private ZInfoDAO zInfoDAO;

    @Autowired
    private CompanyUserDao companyUserDao;
    /**
     * 获取动态
     *
     * @param query
     */
    @Override
    public ProjectDynamicDTO getProjectDynamic(QueryDynamicDTO query) {
        if ((query.getStartLine() == null) && (query.getPageIndex() != null) && (query.getPageSize() != null)){
            query.setStartLine(query.getPageIndex()*query.getPageSize());
        }
        List<DynamicDTO> dataList = dynamicDAO.listDynamic(query);
        Integer total = zInfoDAO.getLastQueryCount();

        ProjectDynamicDTO dto = new ProjectDynamicDTO();
        dto.setTotal(total);
        if (dataList.size() > 0){
            for (DynamicDTO data : dataList){
                if (data.getTemplate() != null) {
                    String[] params = (data.getExtra() != null) ? data.getExtra().split(DynamicConst.SEPARATOR,3) : null;
                    data.setContent(StringUtil.format(data.getTemplate(),
                            ((data.getOperatorName()!=null)?data.getOperatorName():""),
                            ((data.getNodeName()!=null)?data.getNodeName():""),
                            ((params!=null && params.length>0)?params[0]:""),
                            ((params!=null && params.length>1)?params[1]:""),
                            ((params!=null && params.length>2)?params[2]:"")));
                } else {
                    data.setContent(data.getOperatorName() + DynamicConst.SEPARATOR +
                            data.getNodeName() + DynamicConst.SEPARATOR + data.getExtra());
                }
            }
            dto.setDynamicList(dataList);
        }
        return dto;
    }

    /**
     * 
     */
    /**
     * 通用的添加动态方法
     *
     * @param origin 原始数据
     * @param target 修改后的数据
     * @param projectId 操作者针对的项目的编号
     * @param companyId 操作者所在的公司的编号
     * @param userId 操作者的用户编号
     * @param companyUserId 操作者的雇员编号
     */
    @Override
    public <T> void addDynamic(T origin, T target, String projectId, String companyId, String userId, String companyUserId) {
        if ((origin == null) && (target == null)) return;
        //补填缺失参数
        //项目编号
        if ((projectId == null) && (target != null)) projectId = (String)BeanUtilsEx.getProperty(target,"projectId");
        if ((projectId == null) && (origin != null)) projectId = (String)BeanUtilsEx.getProperty(origin,"projectId");

        //操作者公司编号和用户编号
        if (((companyId == null) || (userId == null)) && (companyUserId != null)) {
            CompanyUserEntity cue = companyUserDao.selectById(companyUserId);
            if (cue != null) {
                companyId = cue.getCompanyId();
                userId = cue.getUserId();
            }
        }
        if ((companyId == null) && (target != null)) companyId = (String)BeanUtilsEx.getProperty(target,"currentCompanyId");
        if ((companyId == null) && (origin != null)) companyId = (String)BeanUtilsEx.getProperty(origin,"currentCompanyId");
        if ((companyId == null) && (target != null)) companyId = (String)BeanUtilsEx.getProperty(target,"companyId");
        if ((companyId == null) && (origin != null)) companyId = (String)BeanUtilsEx.getProperty(origin,"companyId");

        if ((userId == null) && (target != null)) userId = (String)BeanUtilsEx.getProperty(target,"accountId");
        if ((userId == null) && (target != null)) userId = (String)BeanUtilsEx.getProperty(target,"userId");
        if ((userId == null) && (target != null)) userId = (String)BeanUtilsEx.getProperty(target,"updateBy");
        if ((userId == null) && (target != null)) userId = (String)BeanUtilsEx.getProperty(target,"createBy");
        if ((userId == null) && (origin != null)) userId = (String)BeanUtilsEx.getProperty(origin,"accountId");
        if ((userId == null) && (origin != null)) userId = (String)BeanUtilsEx.getProperty(origin,"userId");
        if ((userId == null) && (origin != null)) userId = (String)BeanUtilsEx.getProperty(origin,"updateBy");
        if ((userId == null) && (origin != null)) userId = (String)BeanUtilsEx.getProperty(origin,"createBy");

        //操作者雇员编号
        if ((companyUserId == null) && (companyId != null) && (userId != null)){
            companyUserId = zInfoDAO.getCompanyUserIdByCompanyIdAndUserId(companyId,userId);
        }
        if ((companyUserId == null) && (target != null)) companyUserId = (String)BeanUtilsEx.getProperty(target,"currentCompanyUserId");
        if ((companyUserId == null) && (target != null)) companyUserId = (String)BeanUtilsEx.getProperty(target,"companyUserId");
        if ((companyUserId == null) && (origin != null)) companyUserId = (String)BeanUtilsEx.getProperty(origin,"currentCompanyUserId");
        if ((companyUserId == null) && (origin != null)) companyUserId = (String)BeanUtilsEx.getProperty(origin,"companyUserId");

        //调用相应创建日志方法
        if ((origin instanceof TaskWithFullNameDTO) || (target instanceof TaskWithFullNameDTO)){
            addDynamic(createDynamicFrom((TaskWithFullNameDTO)origin,(TaskWithFullNameDTO)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof ZCostDTO) || (target instanceof ZCostDTO)){
            addDynamic(createDynamicFrom((ZCostDTO)origin,(ZCostDTO)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof ZProjectDTO) || (target instanceof ZProjectDTO)){
            addDynamic(createDynamicFrom((ZProjectDTO)origin,(ZProjectDTO)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof ProjectCostEntity) || (target instanceof ProjectCostEntity)){
            addDynamic(createDynamicFrom((ProjectCostEntity)origin,(ProjectCostEntity)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof ProjectCostPointEntity) || (target instanceof ProjectCostPointEntity)){
            addDynamic(createDynamicFrom((ProjectCostPointEntity)origin,(ProjectCostPointEntity)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof ProjectCostPointDetailEntity) || (target instanceof ProjectCostPointDetailEntity)){
            addDynamic(createDynamicFrom((ProjectCostPointDetailEntity)origin,(ProjectCostPointDetailEntity)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof ProjectCostPaymentDetailEntity) || (target instanceof ProjectCostPaymentDetailEntity)){
            addDynamic(createDynamicFrom((ProjectCostPaymentDetailEntity)origin,(ProjectCostPaymentDetailEntity)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof ProjectTaskEntity) || (target instanceof ProjectTaskEntity)){
            addDynamic(createDynamicFrom((ProjectTaskEntity)origin,(ProjectTaskEntity)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof ProjectEntity) || (target instanceof ProjectEntity)){
            addDynamic(createDynamicFrom((ProjectEntity)origin,(ProjectEntity)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof ProjectMemberEntity) || (target instanceof ProjectMemberEntity)){
            addDynamic(createDynamicFrom((ProjectMemberEntity)origin,(ProjectMemberEntity)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof ProjectProcessNodeEntity) || (target instanceof ProjectProcessNodeEntity)){
            addDynamic(createDynamicFrom((ProjectProcessNodeEntity)origin,(ProjectProcessNodeEntity)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof ProjectProcessTimeEntity) || (target instanceof ProjectProcessTimeEntity)){
            addDynamic(createDynamicFrom((ProjectProcessTimeEntity)origin,(ProjectProcessTimeEntity)target,projectId,companyId,userId,companyUserId));
        } else if ((origin instanceof NetFileDO) || (target instanceof NetFileDO)){
            addDynamic(createDynamicFrom((NetFileDO)origin,(NetFileDO)target,projectId,companyId,userId,companyUserId));
        }
    }
    /**
     * 更改时间
     */
    private DynamicDO createDynamicFrom(ProjectProcessTimeEntity origin,ProjectProcessTimeEntity target,String projectId,String companyId,String userId,String companyUserId) {
        if ((origin == null) && (target == null)) return null;
        //转换数据格式
        TaskWithFullNameDTO originTask = zInfoDAO.getTaskByTime(origin);
        TaskWithFullNameDTO targetTask = zInfoDAO.getTaskByTime(target);
        return createDynamicFrom(originTask,targetTask,projectId,companyId,userId,companyUserId);
    }
    /**
     * 更改设校审
     */
    private DynamicDO createDynamicFrom(ProjectProcessNodeEntity origin,ProjectProcessNodeEntity target,String projectId,String companyId,String userId,String companyUserId){
        if ((origin == null) && (target == null)) return null;
        //转换数据格式
        ZProcessNodeDTO originNode = zInfoDAO.getProcessNodeByProcessNode(origin);
        ZProcessNodeDTO targetNode = zInfoDAO.getProcessNodeByProcessNode(target);
        //补充缺失参数
        if (StringUtil.isNullOrEmpty(projectId) && (targetNode != null)) projectId = targetNode.getProjectId();
        if (StringUtil.isNullOrEmpty(projectId) && (originNode != null)) projectId = originNode.getProjectId();

        //设置通用字段
        String id = (target != null) ? target.getId() : null;
        if ((id == null) && (origin != null)) id = origin.getId();

        DynamicDO dyn = new DynamicDO();
        dyn.set4Base(userId,null,new Date(),null);
        dyn.setCompanyUserId(companyUserId);
        dyn.setProjectId(projectId);
        dyn.setCompanyId(companyId);
        dyn.setTargetType(DynamicConst.TARGET_TYPE_PROCESS_NODE);
        dyn.setTargetId(id);

        //设置特有字段

        String nodeName = (targetNode != null) ? targetNode.getNodeName() : null;
        if ((nodeName == null) && (originNode != null)) nodeName = originNode.getNodeName();

        String taskName = (targetNode != null) ? targetNode.getTaskFullName() : null;
        if ((taskName == null) && (originNode != null)) taskName = originNode.getTaskFullName();

        if ((originNode != null) && (targetNode != null)){
            if ((originNode.getCompleteDate() == null) && (targetNode.getCompleteDate() != null)) {
                dyn.setType(DynamicConst.DYNAMIC_TYPE_FINISH_TASK);
            } else if ((originNode.getCompleteDate() != null) && (targetNode.getCompleteDate() == null)) {
                dyn.setType(DynamicConst.DYNAMIC_TYPE_REACTIVE_TASK);
            }else {
                dyn.setType(DynamicConst.DYNAMIC_TYPE_FINISH_TASK);
            }
            dyn.setNodeName(nodeName);
            dyn.setContent(taskName);
        }
        return dyn;
    }
    /**
     * 更改项目负责人
     */
    private DynamicDO createDynamicFrom(ProjectMemberEntity origin,ProjectMemberEntity target,String projectId,String companyId,String userId,String companyUserId){
        if ((origin == null) && (target == null)) return null;

        //设置通用字段
        String id = (target != null) ? target.getId() : null;
        if ((id == null) && (origin != null)) id = origin.getId();

        DynamicDO dyn = new DynamicDO();
        dyn.setCommon(DynamicConst.TARGET_TYPE_PROJECT_MEMBER,id,projectId,companyId,userId,companyUserId);

        //设置特有字段
        Integer type = (target != null) ? target.getMemberType() : null;
        if ((type == null) && (origin != null)) type = origin.getMemberType();

        String roleName = "负责人";
        if(Objects.equals(type, ProjectMemberType.PROJECT_OPERATOR_MANAGER)){
            roleName = "经营负责人";
        } else if(Objects.equals(type, ProjectMemberType.PROJECT_DESIGNER_MANAGER)){
            roleName = "设计负责人";
        } else if (Objects.equals(type, ProjectMemberType.PROJECT_TASK_RESPONSIBLE)){ //如果修改任务负责人，通过task修改实现
            // roleName = "任务负责人";
            return null;
        }
        String originResponseId = (origin != null) ? origin.getCompanyUserId() : null;
        String originResponseName = (originResponseId != null) ? zInfoDAO.getUserNameByCompanyUserId(originResponseId) : null;
        String targetResponseId = (target != null) ? target.getCompanyUserId() : null;
        String targetResponseName = (targetResponseId != null) ? zInfoDAO.getUserNameByCompanyUserId(targetResponseId) : null;

        if (StringUtil.isEmpty(originResponseId) && !StringUtil.isEmpty(targetResponseId)){
            dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_RESPONSIBLE);
            dyn.setNodeName(roleName);
            dyn.setContent(targetResponseName);
        } else if (!StringUtil.isEmpty(originResponseId) && StringUtil.isEmpty(targetResponseId)){
            dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_RESPONSIBLE);
            dyn.setNodeName(originResponseName);
            dyn.setContent(roleName);
        } else if (!StringUtil.isSame(originResponseId,targetResponseId)){
            dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_RESPONSIBLE);
            dyn.setNodeName(roleName);
            dyn.setContent(originResponseName);
            dyn.setContent(targetResponseName);
        }
        return dyn;
    }

    /**
     * 更改项目合并属性
     */
    private List<DynamicDO> createDynamicFrom(ProjectEntity origin,ProjectEntity target,String projectId,String companyId,String userId,String companyUserId){
        if ((origin == null) && (target == null)) return null;
        //补充缺失参数
        if (StringUtil.isNullOrEmpty(projectId) && (target != null)) projectId = target.getId();
        if (StringUtil.isNullOrEmpty(projectId) && (origin != null)) projectId = origin.getId();

        if (StringUtil.isNullOrEmpty(companyUserId) && (companyId != null) && (userId != null)){
            companyUserId = zInfoDAO.getCompanyUserIdByCompanyIdAndUserId(companyId,userId);
        }

        List<DynamicDO> list = new ArrayList<>();
        if (origin == null){
            DynamicDO dyn = new DynamicDO();
            dyn.setNodeName(target.getProjectName());
            dyn.setType(DynamicConst.DYNAMIC_TYPE_ADD_PROJECT);
            dyn.setContent("");
            list.add(dyn);
        } else if (target == null) {
            DynamicDO dyn = new DynamicDO();
            dyn.setNodeName(origin.getProjectName());
            dyn.setType(DynamicConst.DYNAMIC_TYPE_DEL_PROJECT);
            dyn.setContent("");
            list.add(dyn);
        } else {
            addDynamicForProjectNo(list, origin, target);
            addDynamicForProjectName(list, origin, target);
            addDynamicForProjectAddress(list, origin, target);
            addDynamicForEstimation(list, origin, target);
            addDynamicForACompany(list, origin, target);
            addDynamicForBCompany(list, origin, target);
            //addDynamicForProjectType(list,origin,target); //ProjectType目前没有起作用
            addDynamicForBaseArea(list, origin, target);
            //addDynamicForBuiltType(list,origin,target);
            addDynamicForCapacityArea(list, origin, target);
            addDynamicForTotalArea(list, origin, target);
            addDynamicForIncreasingArea(list, origin, target);
            addDynamicForGreeningRate(list, origin, target);
            addDynamicForBuiltHeight(list, origin, target);
            addDynamicForBuiltFloor(list, origin, target);
            addDynamicForProjectStatus(list, origin, target);
            addDynamicForVolumeRatio(list, origin, target);
            addDynamicForTotalAmount(list, origin, target);
            addDynamicForContract(list, origin, target);
            addDynamicForBasis(list, origin, target);
            //addDynamicForRange(list,origin,target);
            addDynamicForCoverage(list, origin, target);
        }

        for (DynamicDO dyn : list){
            dyn.setCommon(DynamicConst.TARGET_TYPE_PROJECT,projectId,projectId,companyId,userId,companyUserId);
        }
        return list;
    }
    private List<DynamicDO> createDynamicFrom(ZProjectDTO origin,ZProjectDTO target,String projectId,String companyId,String userId,String companyUserId){
        if ((origin == null) && (target == null)) return null;
        //补充缺失参数
        if (StringUtil.isNullOrEmpty(projectId) && (target != null)) projectId = target.getId();
        if (StringUtil.isNullOrEmpty(projectId) && (origin != null)) projectId = origin.getId();

        List<DynamicDO> list = new ArrayList<>();
        addDynamicForRanges(list,origin,target);
        addDynamicForFunctions(list,origin,target);
        addDynamicForSignDate(list,origin,target);

        for (DynamicDO dyn : list){
            dyn.setCommon(DynamicConst.TARGET_TYPE_PROJECT,projectId,projectId,companyId,userId,companyUserId);
        }
        return list;
    }

    //项目编号
    private void addDynamicForProjectNo(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target) {
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getProjectNo())) ?
                origin.getProjectNo() : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getProjectNo())) ?
                target.getProjectNo() : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("项目编号");
            list.add(dyn);
        }
    }

    private void addDynamicForCoverage(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target) {
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getCoverage())) ?
                origin.getCoverage() + "%" : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getCoverage())) ?
                target.getCoverage() + "%" : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("覆盖率");
            list.add(dyn);
        }
    }
    private void addDynamicForEstimation(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && (origin.getInvestmentEstimation() != null)) ?
                String.valueOf(origin.getInvestmentEstimation().setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue()) + "万" : null;
        String ts = ((target != null) && (target.getInvestmentEstimation() != null)) ?
                String.valueOf(target.getInvestmentEstimation().setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue()) + "万" : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("投资估算");
            list.add(dyn);
        }
    }
    private void addDynamicForACompany(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

         String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getConstructCompany())) ?
                zInfoDAO.getContractCompanyNameByCompanyId(origin.getConstructCompany()) : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getConstructCompany())) ?
                zInfoDAO.getContractCompanyNameByCompanyId(target.getConstructCompany()) : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("甲方");
            list.add(dyn);
        }
    }
    private void addDynamicForBCompany(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getCompanyBid())) ?
                zInfoDAO.getCompanyNameByCompanyId(origin.getCompanyBid()) : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getCompanyBid())) ?
                zInfoDAO.getCompanyNameByCompanyId(target.getCompanyBid()) : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("乙方");
            list.add(dyn);
        }
    }
    private void addDynamicForProjectType(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getProjectType())) ?
                origin.getProjectType() : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getProjectType())) ?
                target.getProjectType() : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("项目类型");
            list.add(dyn);
        }
    }
    private void addDynamicForBaseArea(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getBaseArea())) ?
                origin.getBaseArea() + "平方米" : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getBaseArea())) ?
                target.getBaseArea() + "平方米" : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("基地面积");
            list.add(dyn);
        }
    }

    private void addDynamicForCapacityArea(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getCapacityArea())) ?
                origin.getCapacityArea() + "平方米" : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getCapacityArea())) ?
                target.getCapacityArea() + "平方米" : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("计容建筑面积");
            list.add(dyn);
        }
    }
    private void addDynamicForTotalArea(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getTotalConstructionArea())) ?
                origin.getTotalConstructionArea() + "平方米" : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getTotalConstructionArea())) ?
                target.getTotalConstructionArea() + "平方米" : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("总建筑面积");
            list.add(dyn);
        }
    }
    private void addDynamicForIncreasingArea(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getIncreasingArea())) ?
                origin.getIncreasingArea() + "平方米" : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getIncreasingArea())) ?
                target.getIncreasingArea() + "平方米" : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("核增面积");
            list.add(dyn);
        }
    }
    private void addDynamicForGreeningRate(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getGreeningRate())) ?
                origin.getGreeningRate() + "%" : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getGreeningRate())) ?
                target.getGreeningRate() + "%" : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("绿化率");
            list.add(dyn);
        }
    }
    private void addDynamicForBuiltHeight(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getBuiltHeight())) ?
                origin.getBuiltHeight() + "米" : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getBuiltHeight())) ?
                target.getBuiltHeight() + "米" : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("建筑高度");
            list.add(dyn);
        }
    }
    private void addDynamicForBuiltFloor(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = zInfoDAO.getProjectBuiltFloorByProject(origin);
        String ts = zInfoDAO.getProjectBuiltFloorByProject(target);

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("建筑层数");
            list.add(dyn);
        }
    }
    private void addDynamicForProjectStatus(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getStatus())) ?
                zInfoDAO.getProjectStatusNameByStatus(origin.getStatus()) : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getStatus())) ?
                zInfoDAO.getProjectStatusNameByStatus(target.getStatus()) : null;


        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("项目状态");
            list.add(dyn);
        }
    }
    private void addDynamicForVolumeRatio(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getVolumeRatio())) ?
                origin.getVolumeRatio() + "%" : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getVolumeRatio())) ?
                target.getVolumeRatio() + "%" : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("容积率");
            list.add(dyn);
        }
    }
    private void addDynamicForTotalAmount(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && (origin.getTotalContractAmount() != null)) ?
                String.valueOf(origin.getTotalContractAmount().setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue()) + "万" : null;
        String ts = ((target != null) && (target.getTotalContractAmount() != null)) ?
                String.valueOf(target.getTotalContractAmount().setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue()) + "万" : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("合同总金额");
            list.add(dyn);
        }
    }
    private void addDynamicForContract(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getContractAttachment())) ?
                origin.getContractAttachment() : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getContractAttachment())) ?
                target.getContractAttachment() : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("合同扫描件");
            list.add(dyn);
        }
    }
    private void addDynamicForBasis(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

//        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getDesignBasis())) ?
//                origin.getDesignBasis() : null;
//        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getDesignBasis())) ?
//                target.getDesignBasis() : null;

//        DynamicDO dyn = createDynamicFrom(ds,ts);
//        if (dyn != null) {
//            dyn.setNodeName("设计依据");
//            list.add(dyn);
//        }
    }
    private void addDynamicForRange(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target){
        if ((list == null) || ((origin == null) && (target == null))) return;

//        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getDesignRange())) ?
//                origin.getDesignRange() : null;
//        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getDesignRange())) ?
//                target.getDesignRange() : null;
//
//        DynamicDO dyn = createDynamicFrom(ds,ts);
//        if (dyn != null) {
//            dyn.setNodeName("设计范围");
//            list.add(dyn);
//        }
    }

    //项目名称
    private void addDynamicForProjectName(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target) {
        if ((list == null) || ((origin == null) && (target == null))) return;

        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getProjectName())) ?
                origin.getProjectName() : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getProjectName())) ?
                target.getProjectName() : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("项目名称");
            list.add(dyn);
        }
    }
    //项目地点
    private void addDynamicForProjectAddress(List<DynamicDO> list, ProjectEntity origin, ProjectEntity target) {
        if ((list == null) || ((origin == null) && (target == null))) return;

        //建立动态对象
        String ds = zInfoDAO.getProjectAddressByProject(origin);
        String ts = zInfoDAO.getProjectAddressByProject(target);

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("项目地点");
            list.add(dyn);
        }
    }

    /**
     * 更改设计范围
     */
    private void addDynamicForRanges(List<DynamicDO> list,ZProjectDTO origin,ZProjectDTO target){
        if ((list == null) || ((origin == null) && (target == null))) return;
        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getRanges())) ?
                origin.getRanges() : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getRanges())) ?
                target.getRanges() : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("设计范围");
            list.add(dyn);
        }
    }
    /**
     * 更改功能分类
     */
    private void addDynamicForFunctions(List<DynamicDO> list,ZProjectDTO origin,ZProjectDTO target){
        if ((list == null) || ((origin == null) && (target == null))) return;
        String ds = ((origin != null) && !StringUtil.isNullOrEmpty(origin.getFunctions())) ?
                origin.getFunctions() : null;
        String ts = ((target != null) && !StringUtil.isNullOrEmpty(target.getFunctions())) ?
                target.getFunctions() : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("功能分类");
            list.add(dyn);
        }
    }
    /**
     * 更改合同签署日期
     */
    private void addDynamicForSignDate(List<DynamicDO> list,ZProjectDTO origin,ZProjectDTO target){
        if ((list == null) || ((origin == null) && (target == null))) return;
        String ds = ((origin != null) && (origin.getSignDate() != null)) ?
                DateUtils.date2Str(origin.getSignDate(),DateUtils.date_sdf2) : null;
        String ts = ((target != null) && (target.getSignDate() != null)) ?
                DateUtils.date2Str(target.getSignDate(),DateUtils.date_sdf2) : null;

        DynamicDO dyn = createDynamicFrom(ds,ts);
        if (dyn != null) {
            dyn.setNodeName("合同签订日期");
            list.add(dyn);
        }
    }
    /**
     * 更改合同总金额
     */
    private DynamicDO createDynamicFrom(ProjectCostEntity origin,ProjectCostEntity target,String projectId,String companyId,String userId,String companyUserId){
        if ((origin == null) && (target == null)) return null;
        String pid = (projectId != null) ? projectId :
                ((target != null) && (target.getProjectId() != null)) ? target.getProjectId() :
                (origin != null) ? origin.getProjectId() : null;
        String tid = ((target != null) && (target.getId() != null)) ? target.getId() :
                (origin != null) ? origin.getId() : null;
        String type = ((target != null) && (target.getType() != null)) ? target.getType() :
                (origin != null) ? origin.getType() : null;
        String typeName = getFeeTypeString(type);
        String ds = ((origin != null) && (origin.getFee() != null)) ? String.valueOf(origin.getFee().doubleValue()) : "0";
        String ts = ((target != null) && (target.getFee() != null)) ? String.valueOf(target.getFee().doubleValue()) : "0";

        DynamicDO dyn = new DynamicDO();
        dyn.set4Base(userId,null,new Date(),null);
        dyn.setCompanyUserId(companyUserId);
        dyn.setProjectId(pid);
        dyn.setCompanyId(companyId);
        dyn.setTargetType(DynamicConst.TARGET_TYPE_COST);
        dyn.setTargetId(tid);
        dyn.setNodeName(typeName);
        if (origin == null){
            dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_CONTRACT_COST);
            dyn.setContent(ts);
        } else if (target == null){
            dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_CONTRACT_COST);
            dyn.setContent(ds);
        } else {
            if (isSame(ds,ts)) return null;
            dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_CONTRACT_COST);
            dyn.setContent(ds);
            dyn.setContent(ts);
        }
        return dyn;
    }

    private String getFeeTypeString(String type){
        if (Integer.parseInt(type) == 1) {
            return "合同回款";
        } else if (Integer.parseInt(type) == 2){
            return "技术审查费";
        } else if (Integer.parseInt(type) == 3){
            return "合作设计费";
        } else {
            return "其他";
        }
    }


    /**
     * 更改费用
     */
    private DynamicDO createDynamicFrom(ZCostDTO origin,ZCostDTO target,String projectId,String companyId,String userId,String companyUserId){
        if ((origin == null) && (target == null)) return null;

        //获取一致性数据
        Integer type = (target != null) ? target.getPointType() : null;
        if ((type == null) && (origin != null)) type = origin.getPointType();

        String typeName = (target != null) ? target.getPointTypeName() : null;
        if (StringUtil.isNullOrEmpty(typeName) && (origin != null)) typeName = origin.getPointTypeName();

        String id = (target != null) ? target.getId() : null;
        if (StringUtil.isNullOrEmpty(id) && (origin != null)) id = origin.getId();

        Integer idType = (target != null) ? target.getIdType() : null;
        if ((idType == null) && (origin != null)) idType = origin.getIdType();

        BigDecimal paidCost = (target != null) ? target.getPaidCost() : null;
        if ((paidCost == null) && (origin != null)) paidCost = origin.getPaidCost();

        //修正错误参数
        if ((target != null) && (target.getPayDate() != null) && (target.getPaidDate() == null)){
            if ((origin != null) && (origin.getPayDate() != null) && (origin.getPaidDate() != null)) {
                target = null;
            } else if ((origin != null) && (origin.getPayDate() == null)) {
                origin = null;
            }
        }

        DynamicDO dyn = new DynamicDO();
        dyn.set4Base(userId,null,new Date(),null);
        dyn.setCompanyUserId(companyUserId);
        dyn.setProjectId(projectId);
        dyn.setCompanyId(companyId);
        dyn.setTargetType(idType);
        dyn.setTargetId(id);
        dyn.setNodeName(typeName);
        if (origin == null){
            if (Objects.equals(idType, DynamicConst.TARGET_TYPE_COST_POINT)){
                if (Objects.equals(type,4)) {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_POINT_COST_PAY);
                } else if (Objects.equals(type,2) || Objects.equals(type,3) || Objects.equals(type,5)){
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_POINT_COST_RECEIPT);
                } else{
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_POINT_COST);
                }
            } else if (Objects.equals(idType, DynamicConst.TARGET_TYPE_COST_DETAIL)){
                if (Objects.equals(type,4)) {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_DETAIL_COST_PAY);
                } else if (Objects.equals(type,2) || Objects.equals(type,3) || Objects.equals(type,5)){
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_DETAIL_COST_RECEIPT);
                } else{
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_DETAIL_COST);
                }
            } else if (Objects.equals(idType, DynamicConst.TARGET_TYPE_COST_PAY)){
                if ((target.getPayDate() == null) && (target.getPaidDate() == null)){
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_DETAIL_COST_PAY);
                    if (paidCost != null)
                        target.setFullName(target.getFullName() + " 付款金额：" + String.valueOf(paidCost.setScale(20, 6).doubleValue()) + "万");
                } else if ((target.getPayDate() != null) && (target.getPaidDate() == null)) {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_PAY_COST_PAY);
                    if (paidCost != null)
                        target.setFullName(target.getFullName() + " 付款金额：" + String.valueOf(paidCost.setScale(20, 6).doubleValue()) + "万"
                                + " 付款时间：" + DateUtils.date2Str(target.getPayDate(), DateUtils.date_sdf2));
                } else {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_PAY_COST);
                }
            } else {
                dyn.setType(idType);
            }
            dyn.setContent(target.getFullName());
        } else if (target == null){
            if (Objects.equals(idType, DynamicConst.TARGET_TYPE_COST_POINT)){
                if (Objects.equals(type,4)) {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_POINT_COST_PAY);
                } else if (Objects.equals(type,2) || Objects.equals(type,3) || Objects.equals(type,5)){
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_POINT_COST_RECEIPT);
                } else{
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_POINT_COST);
                }
            } else if (Objects.equals(idType, DynamicConst.TARGET_TYPE_COST_DETAIL)){
                if (Objects.equals(type,4)) {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_DETAIL_COST_PAY);
                } else if (Objects.equals(type,2) || Objects.equals(type,3) || Objects.equals(type,5)){
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_DETAIL_COST_RECEIPT);
                } else{
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_DETAIL_COST);
                }
            } else if (Objects.equals(idType, DynamicConst.TARGET_TYPE_COST_PAY)){
                if ((origin.getPayDate() == null) && (origin.getPaidDate() == null)){
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_DETAIL_COST_PAY);
                } else if ((origin.getPayDate() != null) && (origin.getPaidDate() == null)) {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_PAY_COST_PAY);
                } else {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_PAY_COST);
                }
            } else {
                dyn.setType(idType);
            }
            dyn.setContent(origin.getFullName());
        } else {
            //if (isSame(origin.getFullName(),target.getFullName())) return null;
            if (Objects.equals(idType, DynamicConst.TARGET_TYPE_COST_POINT)){
                if (Objects.equals(type,4)) {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_POINT_COST_PAY);
                } else if (Objects.equals(type,2) || Objects.equals(type,3) || Objects.equals(type,5)){
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_POINT_COST_RECEIPT);
                } else{
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_POINT_COST);
                }
                dyn.setContent(origin.getFullName());
                dyn.setContent(target.getFullName());
            } else if (Objects.equals(idType, DynamicConst.TARGET_TYPE_COST_DETAIL)){
                if (Objects.equals(type,4)) {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_DETAIL_COST_PAY);
                } else if (Objects.equals(type,2) || Objects.equals(type,3) || Objects.equals(type,5)){
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_DETAIL_COST_RECEIPT);
                } else{
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_DETAIL_COST);
                }
                dyn.setContent(origin.getFullName());
                dyn.setContent(target.getFullName());
            } else if (Objects.equals(idType, DynamicConst.TARGET_TYPE_COST_PAY)){
                if ((origin.getPayDate() == target.getPayDate()) && (origin.getPaidDate() == target.getPaidDate())){
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_DETAIL_COST_PAY);
                    dyn.setContent(origin.getFullName());
                    dyn.setContent(target.getFullName());
                } else if ((origin.getPayDate() == null) && (target.getPayDate() != null)) {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_PAY_COST_PAY);
                    dyn.setContent(target.getFullName());
                } else if ((origin.getPaidDate() == null) && (target.getPaidDate() != null)) {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_PAY_COST);
                    dyn.setContent(target.getFullName());
                } else {
                    dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_PAY_COST);
                    dyn.setContent(origin.getFullName());
                    dyn.setContent(target.getFullName());
                }
            } else {
                dyn.setType(idType);
                dyn.setContent(origin.getFullName());
                dyn.setContent(target.getFullName());
            }
        }
        return dyn;
    }
    /**
     * 更改合同节点
     */
    private DynamicDO createDynamicFrom(ProjectCostPointEntity origin,ProjectCostPointEntity target,String projectId,String companyId,String userId,String companyUserId){
        if ((origin == null) && (target == null)) return null;
        //获取类内部缺失参数
        if ((origin != null) && (target != null)){
            if (target.getId() == null) target.setId(origin.getId());
            if (target.getFeeDescription() == null) target.setFeeDescription(origin.getFeeDescription());
            if (target.getFeeProportion() == null) target.setFeeProportion(origin.getFeeProportion());
            if (target.getFee() == null) target.setFee(origin.getFee());
            if (target.getType() == null) target.setType(origin.getType());
        }
        //获取缺失参数
        if (StringUtil.isNullOrEmpty(projectId) && (target != null)) projectId = target.getProjectId();
        if (StringUtil.isNullOrEmpty(projectId) && (origin != null)) projectId = origin.getProjectId();

        if (StringUtil.isNullOrEmpty(userId) && (target != null)) projectId = target.getUpdateBy();
        if (StringUtil.isNullOrEmpty(userId) && (target != null)) projectId = target.getCreateBy();
        if (StringUtil.isNullOrEmpty(userId) && (origin != null)) projectId = origin.getUpdateBy();
        if (StringUtil.isNullOrEmpty(userId) && (origin != null)) projectId = origin.getCreateBy();

        if (StringUtil.isNullOrEmpty(companyUserId) && (companyId != null) && (userId != null)){
            companyUserId = zInfoDAO.getCompanyUserIdByCompanyIdAndUserId(companyId,userId);
        }

        //生成动态数据
        ZCostDTO originCost = (origin != null) ? zInfoDAO.getCostByPoint(origin) : null;
        ZCostDTO targetCost = (target != null) ? zInfoDAO.getCostByPoint(target) : null;
        return createDynamicFrom(originCost,targetCost,projectId,companyId,userId,companyUserId);
    }
    /**
     * 更改回款节点
     */
    private DynamicDO createDynamicFrom(ProjectCostPointDetailEntity origin,ProjectCostPointDetailEntity target,String projectId,String companyId,String userId,String companyUserId){
        if ((origin == null) && (target == null)) return null;
        //获取类内部缺失参数
        if ((origin != null) && (target != null)){
            if (target.getId() == null) target.setId(origin.getId());
            if (target.getPointId() == null) target.setPointId(origin.getPointId());
        }
        //获取缺失参数
        if (StringUtil.isNullOrEmpty(projectId) && (target != null)) projectId = target.getProjectId();
        if (StringUtil.isNullOrEmpty(projectId) && (origin != null)) projectId = origin.getProjectId();

        if (StringUtil.isNullOrEmpty(userId) && (target != null)) projectId = target.getUpdateBy();
        if (StringUtil.isNullOrEmpty(userId) && (target != null)) projectId = target.getCreateBy();
        if (StringUtil.isNullOrEmpty(userId) && (origin != null)) projectId = origin.getUpdateBy();
        if (StringUtil.isNullOrEmpty(userId) && (origin != null)) projectId = origin.getCreateBy();

        if (StringUtil.isNullOrEmpty(companyUserId) && (companyId != null) && (userId != null)){
            companyUserId = zInfoDAO.getCompanyUserIdByCompanyIdAndUserId(companyId,userId);
        }

        //生成动态数据
        ZCostDTO originCost = (origin != null) ? zInfoDAO.getCostByDetail(origin) : null;
        ZCostDTO targetCost = (target != null) ? zInfoDAO.getCostByDetail(target) : null;
        return createDynamicFrom(originCost,targetCost,projectId,companyId,userId,companyUserId);
    }
    /**
     * 确认收款
     */
    private DynamicDO createDynamicFrom(ProjectCostPaymentDetailEntity origin,ProjectCostPaymentDetailEntity target,String projectId,String companyId,String userId,String companyUserId){
        if ((origin == null) && (target == null)) return null;
        //获取类内部缺失参数
        if ((origin != null) && (target != null)){
            if (target.getId() == null) target.setId(origin.getId());
            if (target.getPointDetailId() == null) target.setPointDetailId(origin.getPointDetailId());
            if (target.getFee() == null) target.setFee(origin.getFee());
        }
        //获取缺失参数
        if (StringUtil.isNullOrEmpty(projectId) && (target != null)) projectId = target.getProjectId();
        if (StringUtil.isNullOrEmpty(projectId) && (origin != null)) projectId = origin.getProjectId();

        if (StringUtil.isNullOrEmpty(userId) && (target != null)) projectId = target.getUpdateBy();
        if (StringUtil.isNullOrEmpty(userId) && (target != null)) projectId = target.getCreateBy();
        if (StringUtil.isNullOrEmpty(userId) && (origin != null)) projectId = origin.getUpdateBy();
        if (StringUtil.isNullOrEmpty(userId) && (origin != null)) projectId = origin.getCreateBy();

        if (StringUtil.isNullOrEmpty(companyUserId) && (companyId != null) && (userId != null)){
            companyUserId = zInfoDAO.getCompanyUserIdByCompanyIdAndUserId(companyId,userId);
        }

        //生成动态数据
        ZCostDTO originCost = (origin != null) ? zInfoDAO.getCostByPay(origin) : null;
        ZCostDTO targetCost = (target != null) ? zInfoDAO.getCostByPay(target) : null;
        return createDynamicFrom(originCost,targetCost,projectId,companyId,userId,companyUserId);
    }
    /**
     * 任务更改
     */
    private DynamicDO createDynamicFrom(ProjectTaskEntity origin,ProjectTaskEntity target,String projectId,String companyId,String userId,String companyUserId){
        if ((origin == null) && (target == null)) return null;
        //获取缺失参数
        if (StringUtil.isNullOrEmpty(projectId) && (target != null)) projectId = target.getProjectId();
        if (StringUtil.isNullOrEmpty(projectId) && (origin != null)) projectId = origin.getProjectId();

        if (StringUtil.isNullOrEmpty(companyId) && (target != null)) companyId = target.getCompanyId();
        if (StringUtil.isNullOrEmpty(companyId) && (origin != null)) companyId = origin.getCompanyId();

        if (StringUtil.isNullOrEmpty(userId) && (target != null)) userId = (target.getUpdateBy() != null) ? target.getUpdateBy() : target.getCreateBy();
        if (StringUtil.isNullOrEmpty(userId) && (origin != null)) userId = (origin.getUpdateBy() != null) ? origin.getUpdateBy() : origin.getCreateBy();

        if (StringUtil.isNullOrEmpty(companyUserId) && (companyId != null) && (userId != null)){
            companyUserId = zInfoDAO.getCompanyUserIdByCompanyIdAndUserId(companyId,userId);
        }

        TaskWithFullNameDTO originTask = zInfoDAO.getTaskByTask(origin);
        TaskWithFullNameDTO targetTask = zInfoDAO.getTaskByTask(target);
        return createDynamicFrom(originTask,targetTask,projectId,companyId,userId,companyUserId);
    }

    /**
     * 更改任务相关结果
     */
    private DynamicDO createDynamicFrom(TaskWithFullNameDTO origin, TaskWithFullNameDTO target, String projectId, String companyId, String userId, String companyUserId){
        if ((origin == null) && (target == null)) return null;

        //设置通用字段
        DynamicDO dyn = new DynamicDO();
        dyn.set4Base(userId,null,new Date(),null);
        dyn.setCompanyUserId(companyUserId);
        dyn.setProjectId(projectId);
        dyn.setCompanyId(companyId);
        dyn.setTargetType(DynamicConst.TARGET_TYPE_PROJECT_TASK);
        dyn.setTargetId(((target != null) && (target.getId() != null)) ?
                target.getId() : ((origin != null) ? origin.getId() : null));

        //设置特有字段
        if (origin == null) { //添加
            dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_OBJECT);
            dyn.setNodeName(target.getTypeName());
            dyn.setContent(target.getTaskFullName());
        } else if (target == null) { //删除
            dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_OBJECT);
            dyn.setNodeName(origin.getTypeName());
            dyn.setContent(origin.getTaskFullName());
        } else {
            Integer type = target.getTaskType();
            String typeName = target.getTypeName();
            String ds = origin.getTaskName();
            String ts = target.getTaskName();
            dyn.setNodeName(typeName);
            if (!isSame(origin.getLeaderName(),target.getLeaderName())){
                ds += " 负责人：" + origin.getLeaderName();
                ts += " 负责人：" + target.getLeaderName();
            }
            if (!isSame(origin.getTaskPeriod(),target.getTaskPeriod())){
                String periodName = (type == 1) ? "合同时间" : "计划进度";
                if (!StringUtil.isNullOrEmpty(origin.getTaskPeriod()))
                    ds += " " + periodName + "：" + origin.getTaskPeriod();
                if (!StringUtil.isNullOrEmpty(target.getTaskPeriod()))
                    ts += " " + periodName + "：" + target.getTaskPeriod();
            }
            if (!isSame(origin.getMembers(),target.getMembers())){
                ds += " 参与人员：" + origin.getMembers();
                ts += " 参与人员：" + target.getMembers();
            }
            if (!isSame(origin.getToCompanyName(),target.getToCompanyName())){
                ds += origin.getToCompanyName();
                ts += target.getToCompanyName();
            }
            if (!isSame(ds,ts)) {
                dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_OBJECT);
                dyn.setContent(ds);
                dyn.setContent(ts);
            } else if ((origin.getCompleteDate() == null) && (target.getCompleteDate() != null)){
                dyn.setType(DynamicConst.DYNAMIC_TYPE_FINISH_TASK);
                dyn.setNodeName("设计");
                dyn.setContent(origin.getTaskFullName());
            } else if ((origin.getCompleteDate() != null) && (target.getCompleteDate() == null)){
                dyn.setType(DynamicConst.DYNAMIC_TYPE_REACTIVE_TASK);
                dyn.setNodeName("设计");
                dyn.setContent(origin.getTaskFullName());
            } else { //任务修改前和修改后完全一样
                return null;
            }
        }
        return dyn;
    }

    /**
     * 更改文档相关结果
     */
    private DynamicDO createDynamicFrom(NetFileDO origin, NetFileDO target, String projectId, String companyId, String userId, String companyUserId) {
        if ((origin == null) && (target == null)) return null;

        String id = (target != null) ? target.getId() : null;
        if ((id == null) && (origin != null)) id = origin.getId();

        DynamicDO dyn = new DynamicDO();
        dyn.setCommon(DynamicConst.TARGET_TYPE_SKY_DRIVE,id,projectId,companyId,userId,companyUserId);

        if (origin == null) {
            dyn.setNodeName(target.getFileName());
            dyn.setType((Objects.equals(target.getType(), NetFileType.FILE)) ?
                    DynamicConst.DYNAMIC_TYPE_UPLOAD_FILE :
                    DynamicConst.DYNAMIC_TYPE_CREATE_DIRECTORY);
        } else if (target == null){
            dyn.setNodeName(origin.getFileName());
            dyn.setType((Objects.equals(origin.getType(), NetFileType.FILE)) ?
                    DynamicConst.DYNAMIC_TYPE_DELETE_FILE :
                    DynamicConst.DYNAMIC_TYPE_DELETE_DIRECTORY);
        } else {
            dyn.setNodeName(origin.getFileName());
            dyn.setType((Objects.equals(origin.getType(), NetFileType.FILE)) ?
                    DynamicConst.DYNAMIC_TYPE_UPDATE_FILE :
                    DynamicConst.DYNAMIC_TYPE_UPDATE_DIRECTORY);
            dyn.setContent(target.getFileName());
        }
        return dyn;
    }


    //通用的生成动态函数，用于项目基本信息变化
    private DynamicDO createDynamicFrom(String originString, String targetString){
        return createDynamicFrom(originString,targetString,originString,targetString);
    }
    private DynamicDO createDynamicFrom(String originString, String targetString, String originDisplayString, String targetDisplayString){
        DynamicDO dyn = null;
        if (StringUtil.isNullOrEmpty(originString) && !StringUtil.isNullOrEmpty(targetString)) { //创建
            dyn = new DynamicDO();
            dyn.setType(DynamicConst.DYNAMIC_TYPE_CREATE_OBJECT);
            dyn.setContent(targetDisplayString);
        } else if (!StringUtil.isNullOrEmpty(originString) && StringUtil.isNullOrEmpty(targetString)) { //删除
            dyn = new DynamicDO();
            dyn.setType(DynamicConst.DYNAMIC_TYPE_DELETE_OBJECT);
            dyn.setContent(originDisplayString);
        } else if (!StringUtil.isNullOrEmpty(originString) && !StringUtil.isNullOrEmpty(targetString) && !isSame(originString,targetString)) { //更改
            dyn = new DynamicDO();
            dyn.setType(DynamicConst.DYNAMIC_TYPE_UPDATE_OBJECT);
            dyn.setContent(originDisplayString);
            dyn.setContent(targetDisplayString);
        }
        return dyn;
    }

    //判断两个字符串是否相同，视null和""为相同字符串
    private Boolean isSame(String a,String b){
        return (StringUtil.isNullOrEmpty(a) && StringUtil.isNullOrEmpty(b)) ||
                ((a != null) && (b != null) && (a.equals(b)));
    }

    /**
     * 记录项目动态
     *
     * @param dynamic 项目动态记录
     */
    @Override
    public void addDynamic(DynamicDO dynamic) {
        if (dynamic == null) return;
        if (dynamic.getId() == null) dynamic.setId(StringUtil.buildUUID());
        dynamicDAO.insert(dynamic);
    }
    /**
     * 连续记录多个项目动态
     *
     * @param dynamicList 项目动态记录列表
     */
    @Override
    public void addDynamic(List<DynamicDO> dynamicList) {
        dynamicList.forEach(this::addDynamic);
    }
    /**
     * 处理难以获取某些参数的情况，保持兼容性
     */
    @Override
    public <T> void addDynamic(T origin, T target, String projectId, String companyId, String userId) {
        addDynamic(origin,target,projectId,companyId,userId,null);
    }

    @Override
    public <T> void addDynamic(T origin, T target, String companyId, String userId) {
        addDynamic(origin,target,null,companyId,userId,null);
    }

    @Override
    public <T> void addDynamic(T origin, T target, String companyUserId) {
        addDynamic(origin,target,null,null,null,companyUserId);
    }

    @Override
    public <T> void addDynamic(T origin, T target) {
        addDynamic(origin,target,null,null,null,null);
    }

    /**
     * 方便使用的添加删除方法
     * @param target 要添加的对象
     * @param projectId 操作项目ID
     * @param companyId 操作者公司ID
     * @param userId 操作者用户ID
     * @param companyUserId 操作者雇员ID
     */
    @Override
    public <T> void addCreateDynamic(T target, String projectId, String companyId, String userId, String companyUserId) {
        addDynamic(null,target,projectId,companyId,userId,companyUserId);
    }
    @Override
    public <T> void addCreateDynamic(T target, String projectId, String companyId, String userId) {
        addDynamic(null,target,projectId,companyId,userId,null);
    }
    @Override
    public <T> void addCreateDynamic(T target, String companyId, String userId) {
        addDynamic(null,target,null,companyId,userId,null);
    }
    @Override
    public <T> void addCreateDynamic(T target, String companyUserId) {
        addDynamic(null,target,null,null,null,companyUserId);
    }
    @Override
    public <T> void addCreateDynamic(T target) {
        addDynamic(null,target,null,null,null,null);
    }
    @Override
    public <T> void addDeleteDynamic(T origin, String projectId, String companyId, String userId, String companyUserId) {
        addDynamic(origin,null,projectId,companyId,userId,companyUserId);
    }
    @Override
    public <T> void addDeleteDynamic(T origin, String projectId, String companyId, String userId) {
        addDynamic(origin,null,projectId,companyId,userId,null);
    }

    @Override
    public <T> void addDeleteDynamic(T origin, String companyId, String userId) {
        addDynamic(origin,null,null,companyId,userId,null);
    }

    @Override
    public <T> void addDeleteDynamic(T origin, String companyUserId) {
        addDynamic(origin,null,null,null,null,companyUserId);
    }

    @Override
    public <T> void addDeleteDynamic(T origin) {
        addDynamic(origin,null,null,null,null,null);
    }
}
