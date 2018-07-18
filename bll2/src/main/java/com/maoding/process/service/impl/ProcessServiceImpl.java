package com.maoding.process.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.ProcessConst;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.core.util.StringUtil;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.process.dao.ProcessDao;
import com.maoding.process.dao.ProcessNodeDao;
import com.maoding.process.dao.ProcessNodeMemberDao;
import com.maoding.process.dao.ProcessOrgRelationDao;
import com.maoding.process.dto.*;
import com.maoding.process.entity.ProcessEntity;
import com.maoding.process.entity.ProcessNodeEntity;
import com.maoding.process.entity.ProcessNodeMemberEntity;
import com.maoding.process.entity.ProcessOrgRelationEntity;
import com.maoding.process.service.ProcessService;
import com.maoding.project.dto.ProjectProcessNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service("processService")
public class ProcessServiceImpl extends NewBaseService implements ProcessService {

    @Autowired
    private ProcessDao processDao;

    @Autowired
    private ProcessNodeDao processNodeDao;

    @Autowired
    private ProcessNodeMemberDao processNodeMemberDao;

    @Autowired
    private ProcessOrgRelationDao processOrgRelationDao;

    @Autowired
    private CompanyUserDao companyUserDao;

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

    @Override
    public int saveProcess(ProcessEditDTO dto) throws Exception {

        //todo 添加数据校验

        //添加判断
        if(StringUtil.isNullOrEmpty(dto.getCompanyId())){
            dto.setCompanyId(dto.getCurrentCompanyId());
        }
        ProcessEntity process = (ProcessEntity)BaseDTO.copyFields(dto,ProcessEntity.class);
        process.initEntity();
        process.setDeleted(0);
        if(StringUtil.isNullOrEmpty(dto.getProcessName())){
            process.setProcessName(ProcessConst.PROCESS_NAME_MAP.get(dto.getProcessType().toString()));
        }
        int i = processDao.insert(process);
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
        return i;
    }

    @Override
    public List<ProcessNodeDTO> listProcessNode(QueryProcessDTO query) {
        List<ProcessNodeDTO> nodeList = new ArrayList<>();
        List<ProcessNodeEntity> list = processNodeDao.listProcessNode(query.getProcessId());
        list.stream().forEach(node->{
            ProcessNodeDTO nodeDTO = (ProcessNodeDTO)BaseDTO.copyFields(node,ProcessNodeDTO.class);
            //处理操作人
            nodeDTO.setOperatorName(this.getProcessNodeMemberName(node.getId()));
            nodeList.add(nodeDTO);
        });

        return nodeList;
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
                return "";
            }
        }

        return "";
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
            processNodeList.stream().forEach(node->{
                String defaultNodeId = node.getId();
                node.initEntity();
                node.setProcessId(processId);
                this.processNodeDao.insert(node);
                processNodeMemberList.stream().forEach(member->{
                    if(defaultNodeId.equals(member.getNodeId())){
                        member.initEntity();
                        member.setNodeId(node.getId());
                        member.setProcessId(processId);
                        this.processNodeMemberDao.insert(member);
                    }
                });
            });
        }
    }


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
