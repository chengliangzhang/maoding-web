package com.maoding.project.service;

import com.maoding.core.bean.AjaxMessage;
import com.maoding.project.dto.ProjectTaskResponsibleDTO;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectService
 * 类描述：Service
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:28:54
 */
public interface ProjectTaskResponsibleService {

      /**
     * 方法描述：保存任务负责人（新增）
     * 作者：MaoSF
     * 日期：2017/5/20
     * @param:
     * @return:
     */
    AjaxMessage insertTaskResponsible(String projectId,String companyId,String companyUserId,String taskId,String nodeId,String accountId,String currentCompanyId) throws Exception;

    /**
     * 方法描述：保存任务负责人（新增）-- 发布任务给本组织，默认设计负责人为任务负责人
     * 作者：MaoSF
     * 日期：2017/5/20
     */
    AjaxMessage insertTaskResponsibleForPublishTask(String projectId,String companyId,String taskId,String accountId,String currentCompanyId) throws Exception;


    /**
     * 方法描述：保存设计负责人（新增)(用于处理未发布版本)
     * 作者：MaoSF
     * 日期：2017/5/20
     * @param:
     * @return:
     */
    AjaxMessage insertTaskResponsible(String projectId,String companyId,String companyUserId,String taskId,String accountId) throws Exception;



    /**
     * 任务负责人移交
     * @param projectTaskResponsibleDTO
     * @return
     * @throws Exception
     */
    AjaxMessage transferTaskResponse(ProjectTaskResponsibleDTO projectTaskResponsibleDTO) throws Exception;

    /**
     * 任务负责人移交
     * @param projectTaskResponsibleDTO
     * @return
     * @throws Exception
     */
    AjaxMessage transferTaskResponse(ProjectTaskResponsibleDTO projectTaskResponsibleDTO,boolean isPublish) throws Exception;

}
