package com.maoding.project.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.entity.ProjectProcessNodeEntity;

import java.util.List;
import java.util.Map;


/**
 * Created by Wuwq on 2016/10/27.
 */
public interface ProjectProcessNodeDao extends BaseDao<ProjectProcessNodeEntity> {

    List<ProjectProcessNodeEntity> selectByProcessId(String id);

    List<ProjectProcessNodeEntity> selectBySeq(Map<String,Object> paraMap);

    ProjectProcessNodeEntity selectLastByProcessId(String processId);


    List<ProjectProcessNodeEntity> selectNodeByTaskId(String taskId,String companyUserId);


    /**
     * 方法描述：获取节点（taskId，projectId）此方法兼容selectNodeByTaskManageId
     * 作者：MaoSF
     * 日期：2017/5/20
     *
     * @param paraMap
     * @param:
     * @return:
     */
    public List<ProjectProcessNodeEntity> getProcessNodeByParam(Map<String, Object> paraMap);
    /**
     * 方法描述：根据processId,companyUserId,seq,查询节点（一个节点，一个companyUserId只会产生一条数据）
     * 作者：MaoSF
     * 日期：2017/2/24
     */
    ProjectProcessNodeEntity getNodeByCompanyUserAndSeq(String processId, String companyUserId, int seq);


    /**
     * 方法描述：设置完成时间
     * 作者：MaoSF
     * 日期：2017/3/11
     */
    int updateProcessNodeComplete(String id,String taskId,String companyUserId);

    /**
     * 方法描述：设置完成时间
     * 作者：MaoSF
     * 日期：2017/3/11
     * @param:
     * @return:
     */
    int updateProcessNodeComplete(String id);

    /**
     * 方法描述：设置完成时间(生产任务已完成，设计分解的任务自动完成)
     * 作者：MaoSF
     * 日期：2017/3/11
     * @param:
     * @return:
     */
    int updateProcessNodeCompleteForDesignTask(String taskId,String companyUserId);

    /**
     * 分解的所有设计任务已经完成，对应的父任务的设计任务完成
     */
    int updateCompleteForParentTask(String taskId,String companyUserId);
}
