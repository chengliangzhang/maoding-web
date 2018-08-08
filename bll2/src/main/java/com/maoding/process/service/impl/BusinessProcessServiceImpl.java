package com.maoding.process.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.ProcessConst;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.core.util.StringUtil;
import com.maoding.exception.CustomException;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.process.dao.*;
import com.maoding.process.dto.*;
import com.maoding.process.entity.*;
import com.maoding.process.service.BusinessProcessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("businessProcessService")
public class BusinessProcessServiceImpl extends NewBaseService implements BusinessProcessService {

    @Autowired
    private ProcessDao processDao;

    @Autowired
    private ProcessNodeDao processNodeDao;

    @Autowired
    private ProcessNodeMemberDao processNodeMemberDao;

    @Autowired
    private ProcessNodeConditionDao processNodeConditionDao;

    @Autowired
    private ProcessOrgRelationDao processOrgRelationDao;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private CompanyDao companyDao;

    private void initDefaultProcessRelation(String companyId,Integer processType){
        ProcessOrgRelationEntity processOrgRelation = new ProcessOrgRelationEntity();
        processOrgRelation.initEntity();
        processOrgRelation.setDeleted(0);//设置为有效状态
        processOrgRelation.setStatus(ProcessConst.START_STATUS);//0：代表没启用，1：代表启用
        processOrgRelation.setCompanyId(companyId);
        processOrgRelation.setCompanyType(ProcessConst.COMPANY_TYPE_ALL);//代表适合所有
        processOrgRelation.setProcessId(processType.toString());//由于系统中默认的流程的id与processType一致。
        processOrgRelationDao.insert(processOrgRelation);
    }

    @Override
    public void initProcess(QueryProcessDTO query) {
        if(StringUtil.isNullOrEmpty(query.getCompanyId())){
            query.setCompanyId(query.getCurrentCompanyId());
        }
        String companyId = query.getCompanyId();
        ProcessCountDTO count = processOrgRelationDao.countProcess(query);

        if(count.getContractReceiveCount()==null || count.getContractReceiveCount()==0){//初始化系统的默认的合同回款流程
            initDefaultProcessRelation(companyId, ProcessConst.PROCESS_TYPE_CONTRACT);
        }
        if(count.getTechnicalReceiveCount()==null || count.getTechnicalReceiveCount()==0){//初始化系统的默认的技术审查费收款流程
            initDefaultProcessRelation(companyId, ProcessConst.PROCESS_TYPE_RECEIVE_TECHNICAL);
        }
        if(count.getTechnicalPayCount()==null || count.getTechnicalPayCount()==0){//初始化系统的默认的技术审查费付款流程
            initDefaultProcessRelation(companyId, ProcessConst.PROCESS_TYPE_PAY_TECHNICAL);
        }
        if(count.getCooperativeReceiveCount()==null || count.getCooperativeReceiveCount()==0){//初始化系统的默认的合作设计费收款流程
            initDefaultProcessRelation(companyId, ProcessConst.PROCESS_TYPE_RECEIVE_COOPERATIVE);
        }
        if(count.getCooperativePayCount()==null || count.getCooperativePayCount()==0){//初始化系统的默认的合作设计费付款流程
            initDefaultProcessRelation(companyId, ProcessConst.PROCESS_TYPE_PAY_COOPERATIVE);
        }
    }

    @Override
    public Map<String, List<ProcessDTO>> getProcessByCompany(QueryProcessDTO query) {
        if(StringUtil.isNullOrEmpty(query.getCompanyId())){
            query.setCompanyId(query.getCurrentCompanyId());
        }
        this.initProcess(query);//处理默认数据
        Map<String, List<ProcessDTO>> map = new HashMap<>();
        List<ProcessDTO> receiveProcessList = new ArrayList<>();
        List<ProcessDTO> payProcessList = new ArrayList<>();
        //todo 查询业务流程数据
        List<ProcessDTO> processList = this.processDao.getProcessByCompany(query);
        //todo 重新封装
        processList.stream().forEach(p->{
            //设置关联组织的名称
            p.setRelationCompanyName(this.getRelationCompanyName(p));
            if(p.getProcessType()==ProcessConst.PROCESS_TYPE_CONTRACT
                    ||p.getProcessType()==ProcessConst.PROCESS_TYPE_RECEIVE_TECHNICAL
                    ||p.getProcessType()==ProcessConst.PROCESS_TYPE_RECEIVE_COOPERATIVE){
                receiveProcessList.add(p);
            }else {
                payProcessList.add(p);
            }
        });
        map.put("receiveProcessList",receiveProcessList);
        map.put("payProcessList",payProcessList);
        return map;
    }

    private void validateSaveProcess(ProcessEditDTO dto){
        if(dto.getProcessType()==null){
            throw new CustomException("类型不能为空");
        }
        if(!StringUtil.isNullOrEmpty(dto.getId())){
            if(StringUtil.isNullOrEmpty(dto.getProcessId())){
                throw new CustomException("参数错误");
            }
        }
    }

    @Override
    public int saveProcess(ProcessEditDTO dto) throws Exception {
        //todo 添加数据校验
        validateSaveProcess(dto);
        //添加判断
        if(StringUtil.isNullOrEmpty(dto.getCompanyId())){
            dto.setCompanyId(dto.getCurrentCompanyId());
        }
        int i = 0;
        ProcessEntity process = (ProcessEntity)BaseDTO.copyFields(dto,ProcessEntity.class);
        if(StringUtil.isNullOrEmpty(dto.getId())){
            process.initEntity();
            process.setDeleted(0);
            if(StringUtil.isNullOrEmpty(dto.getProcessName())){
                process.setProcessName(ProcessConst.PROCESS_NAME_MAP.get(dto.getProcessType().toString()));
            }
            i = processDao.insert(process);
            ProcessOrgRelationEntity processOrgRelation = new ProcessOrgRelationEntity();
            processOrgRelation.initEntity();
            processOrgRelation.setStatus(ProcessConst.STOP_STATUS);
            processOrgRelation.setDeleted(0);
            processOrgRelation.setProcessId(process.getId());
            processOrgRelation.setCompanyId(dto.getCompanyId());
            processOrgRelation.setRelationCompanyId(dto.getRelationCompanyId());
            processOrgRelation.setCompanyType(this.getCompanyType(dto.getRelationCompanyId()));
            processOrgRelationDao.insert(processOrgRelation);
            initProcessNode(process.getId(),dto.getCompanyId(),process.getProcessType());
        }else {
            ProcessOrgRelationEntity processOrgRelation = new ProcessOrgRelationEntity();
            processOrgRelation.setId(dto.getId());
            processOrgRelation.setRelationCompanyId(dto.getRelationCompanyId());
            processOrgRelation.setCompanyType(this.getCompanyType(dto.getRelationCompanyId()));
            processOrgRelationDao.updateById(processOrgRelation);

            process.setId(dto.getProcessId());
            process.setProcessType(null);//编辑不处理类型
            processDao.updateById(process);

            i = 1;//返回成功
        }

        return i;
    }

    @Override
    public List<ProcessNodeDTO> listProcessNode(QueryProcessDTO query) {
        List<ProcessNodeDTO> nodeList = new ArrayList<>();
        List<ProcessNodeEntity> list = processNodeDao.listProcessNode(query.getProcessId());
        List<ProcessNodeConditionEntity> processNodeConditionList = this.processNodeConditionDao.listProcessNodeCondition(query.getProcessId());
        list.stream().forEach(node->{
            ProcessNodeDTO nodeDTO = (ProcessNodeDTO)BaseDTO.copyFields(node,ProcessNodeDTO.class);
            //处理操作人
            nodeDTO.setOperatorName(this.getProcessNodeMemberName(node.getId()));
            //设置每个节点的状态值
            getProcessNodeCondition(nodeDTO,processNodeConditionList);
            nodeList.add(nodeDTO);
        });

        return nodeList;
    }

    @Override
    public int deleteProcessForProjectPay(ProcessEditDTO dto) throws Exception {
        ProcessOrgRelationEntity processOrgRelation = new ProcessOrgRelationEntity();
        processOrgRelation.setId(dto.getId());
        processOrgRelation.setDeleted(1);//设置删除表示
        return processOrgRelationDao.updateById(processOrgRelation);
    }

    @Override
    public int selectedProcessForProjectPay(ProcessEditDTO dto) throws Exception {
        ProcessOrgRelationEntity processOrgRelation = new ProcessOrgRelationEntity();
        processOrgRelation.setId(dto.getId());
        processOrgRelation.setStatus(dto.getStatus());
        return processOrgRelationDao.updateById(processOrgRelation);
    }

    @Override
    public int selectedProcessNodeStatus(ProcessEditDTO dto) throws Exception {
        if(StringUtil.isNullOrEmpty(dto.getStatusType()) || StringUtil.isNullOrEmpty(dto.getNodeId()) || StringUtil.isNullOrEmpty(dto.getProcessId())){
            throw new CustomException("参数错误");
        }
        int i = 0;
        //查询节点的条件
        ProcessNodeConditionEntity condition = this.processNodeConditionDao.getProcessNodeConditionByDataType(dto.getNodeId(),dto.getStatusType().toString());
        if(ProcessConst.IS_SELECTED==dto.getStatus()){
            if(condition==null){//如果系统中不存在，则新增一条记录
                condition = new ProcessNodeConditionEntity();
                condition.initEntity();
                condition.setDeleted(0);
                condition.setDataType(dto.getStatusType().toString());
                condition.setProcessId(dto.getProcessId());
                condition.setNodeId(dto.getNodeId());
                condition.setNodeCondition(ProcessConst.IS_SELECTED.toString());
                i= this.processNodeConditionDao.insert(condition);
            }else {
                condition.setNodeCondition(dto.getStatus().toString());
                i=  this.processNodeConditionDao.updateById(condition);
            }
        }

        //todo 把财务确认发票信息的记录设置为有效。目前没有设置路由。通过代码把记录设置为有效
        if(ProcessConst.CONDITION_INVOICE.equals(dto.getStatusType().toString())){
            ProcessNodeEntity node = this.processNodeDao.getProcessNodeByType(dto.getProcessId(),ProcessConst.NODE_TYPE_FINANCE_INVOICE_CONFIRM);
            if(node!=null){
                node.setDeleted(0);//设置为有效
                this.processNodeDao.updateById(node);
            }
        }
        return i;
    }


    /**
     * 获取关联组织的名称
     */
    private String getRelationCompanyName(ProcessDTO p){
        if(!StringUtil.isNullOrEmpty(p.getCompanyType())){
            if(p.getCompanyType()==ProcessConst.COMPANY_TYPE_ALL){
                return "所有组织";
            }
            if(p.getCompanyType()==ProcessConst.COMPANY_TYPE_SUB){
                return "分支机构";
            }
            if(p.getCompanyType()==ProcessConst.COMPANY_TYPE_ALL){
                return "事业合伙人";
            }
            //todo 查询相应的组织
            if(p.getCompanyType()==ProcessConst.COMPANY_TYPE_SINGLE){
                return companyDao.getCompanyName(p.getRelationCompanyId());
            }
        }

        return "";
    }

    /**
     * 获取关联组织的名称
     */
    private void getProcessNodeCondition(ProcessNodeDTO node,List<ProcessNodeConditionEntity> processNodeConditionList){
        processNodeConditionList.stream().forEach(condition->{
            //如果条件的nodeId不为空，并且是这个节点的条件，并且值是1的情况下：1：代表已选择
            if(!StringUtil.isNullOrEmpty(condition.getNodeId()) && node.getId().equals(condition.getNodeId())){
                Integer status = StringUtil.isNullOrEmpty(condition.getNodeCondition())?0: Integer.parseInt(condition.getNodeCondition());
                //发票字段设置
                if(ProcessConst.CONDITION_INVOICE.equals(condition.getDataType())){
                    node.setInvoiceStatus(status);
                }
                //应付应收的字段设置
                if(ProcessConst.CONDITION_RECEIVE_ABLE.equals(condition.getDataType()) || ProcessConst.CONDITION_PAY_ABLE.equals(condition.getDataType()) ){
                    node.setReceiveOrPayAbleStatus(status);
                }
                //已付已收的字段设置
                if(ProcessConst.CONDITION_RECEIVE.equals(condition.getDataType()) || ProcessConst.CONDITION_PAY.equals(condition.getDataType()) ){
                    node.setReceiveOrPayStatus(status);
                }
                //同步的状态设置
                if(ProcessConst.CONDITION_SYNC.equals(condition.getDataType())){
                    node.setSyncStatus(status);
                }
            }
        });
    }

    /**
     * 初始化流程节点
     */
    private void initProcessNode(String processId,String companyId,Integer processType){
        //查找默认流程
        ProcessEntity defaultProcess = processDao.getDefaultProcessByType(processType);
        if(defaultProcess!=null){
            List<ProcessNodeEntity> processNodeList = this.processNodeDao.listProcessNode(defaultProcess.getId());
            List<ProcessNodeMemberEntity> processNodeMemberList = this.processNodeMemberDao.listProcessNodeMember(defaultProcess.getId());
            List<ProcessNodeConditionEntity> processNodeConditionList = this.processNodeConditionDao.listProcessNodeCondition(defaultProcess.getId());
            //保存每个节点，把默认流程的节点复制到新增的流程中来
            processNodeList.stream().forEach(node->{
                String defaultNodeId = node.getId();
                node.initEntity();
                node.setProcessId(processId);
                this.processNodeDao.insert(node);
                //把每个节点上面的处理人复制到新的流程节点中
                processNodeMemberList.stream().forEach(member->{
                    if(defaultNodeId.equals(member.getNodeId())){
                        member.initEntity();
                        member.setNodeId(node.getId());
                        member.setProcessId(processId);
                        this.processNodeMemberDao.insert(member);
                    }
                });
                //把每个节点上面的条件复制到新的流程节点中
                processNodeConditionList.stream().forEach(condition->{
                    if(defaultNodeId.equals(condition.getNodeId())){
                        condition.initEntity();
                        condition.setNodeId(node.getId());
                        condition.setProcessId(processId);
                        this.processNodeConditionDao.insert(condition);
                    }
                });
            });
        }
    }


    /**
     * 获取流程节点的处理人名称
     */
    private String getProcessNodeMemberName(String nodeId){
        List<String> memberNameList = new ArrayList<>();
        List<ProcessNodeMemberEntity> memberList = processNodeMemberDao.listMemberByNodeId(nodeId);
        //以下代码后续还需要优化
        memberList.stream().forEach(m->{
            if(!StringUtil.isNullOrEmpty(m.getMemberId())){
                memberNameList.add(companyUserDao.getUserName(m.getMemberId()));
            } else if(!StringUtil.isNullOrEmpty(m.getRoleType())){
                if(m.getRoleType().equals(ProjectMemberType.PROJECT_OPERATOR_MANAGER.toString()) ){
                    if(ProjectMemberType.projectMemberRole.get(m.getRoleType())!=null){
                        memberNameList.add(ProjectMemberType.projectMemberRole.get(m.getRoleType()));
                    }
                }
            }
        });
        return StringUtils.join(memberNameList,",");
    }

    /**
     * 获取从前端返回到后台来的组织类型，方便后面查询及业务处理
     */
    private Integer getCompanyType(String relationCompanyId){
        if(!StringUtil.isNullOrEmpty(relationCompanyId)){
            if(relationCompanyId.equals("root")){
                return ProcessConst.COMPANY_TYPE_ALL;
            }
            else if(relationCompanyId.contains("subCompany")){
                return ProcessConst.COMPANY_TYPE_SUB;
            }
            else if(relationCompanyId.contains("partnerId")){
                return ProcessConst.COMPANY_TYPE_PARTNER;
            }else if(relationCompanyId.length()==32){
                return ProcessConst.COMPANY_TYPE_SINGLE;
            }
        }
        return ProcessConst.COMPANY_TYPE_ALL;
    }
}
