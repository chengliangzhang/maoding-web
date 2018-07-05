package com.maoding.project.service;

import com.maoding.core.bean.AjaxMessage;
import com.maoding.project.dto.ProjectProcessDTO;
import com.maoding.task.entity.ProjectTaskEntity;

import java.util.List;

/**
 * Created by Wuwq on 2016/10/27.
 */
public interface ProjectProcessService {



    /**新建或更新流程*/
    public AjaxMessage saveOrUpdateProcess(ProjectProcessDTO dto) throws Exception;

    /**新建或更新流程*/
    public AjaxMessage saveOrUpdateProcessNew(ProjectProcessDTO dto) throws Exception;

    /**获取指定任务下的流程*/
    public AjaxMessage getProcessesByTask(String taskManageId) throws Exception;


    /**
     * 方法描述：根据任务id删除流程
     * 作者：MaoSF
     * 日期：2017/3/3
     */
    public AjaxMessage deleteProcessByTaskId(String taskId) throws Exception;


    /**
     * 方法描述：设校审节点完成
     * 作者：MaoSF
     * 日期：2017/6/8
     */
    AjaxMessage completeProjectProcessNode(String projectId,String companyId,String nodeId, String taskId,String accountId) throws Exception;

    /**
     * 方法描述：保存流程节点
     * 作者：MaoSF
     * 日期：2017/6/22
     */
    String saveProjectProcessNode(String projectId,int seq,int nodeSeq,String taskId,String companyUserId) throws Exception;
    
    /**
     * 方法描述：删除节点
     * 作者：MaoSF
     * 日期：2017/6/22
     */
    void deleteProcessNodeById(String id) throws Exception;


    void saveProjectProcessForNewTask(ProjectTaskEntity task, List<String> companyUserList,int memberType,String currentCompanyUserId) throws Exception;

    /**
     * 设计分解任务的时候，保存当前人为设计人，不推送任务，不推送消息
     */
    void saveProjectProcessForDesignTask(ProjectTaskEntity task,int memberType,String currentCompanyUserId) throws Exception;
}
