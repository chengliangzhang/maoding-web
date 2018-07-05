package com.maoding.task.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.task.dto.TransferTaskDesignerDTO;
import com.maoding.task.entity.ProjectManagerEntity;

import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectManagerService
 * 类描述：项目经营人、负责人
 * 作    者：MaoSF
 * 日    期：2016年12月28日-下午5:28:54
 */
public interface ProjectManagerService {

    AjaxMessage updateProjectManager(Map<String,Object>param) throws Exception;

    /**
     * 设置助理
     */
    AjaxMessage updateProjectAssistant(Map<String,Object>param) throws Exception;

    /**
     * 方法描述：移交设计负责人
     * 作者：MaoSF
     * 日期：2017/3/22
     * @param:
     * @return:
     */
    AjaxMessage transferTaskDesinger(TransferTaskDesignerDTO dto) throws Exception;

    /**
     * 方法描述：删除经营负责人和设计负责人
     * 作者：MaoSF
     * 日期：2017/4/12
     */
    AjaxMessage deleteProjectManage(String projectId, String companyId) throws Exception;

}
