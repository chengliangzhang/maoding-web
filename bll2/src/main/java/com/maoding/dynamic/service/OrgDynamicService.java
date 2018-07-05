package com.maoding.dynamic.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.dynamic.entity.OrgDynamicEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp21 on 2017/2/23.
 */
public interface OrgDynamicService extends BaseService<OrgDynamicEntity> {


    /**
     * 创建立项动态
     * @param projectId
     * @param companyId
     * @param createPersonId
     * @return
     */
    public AjaxMessage combinationDynamicForProject(String projectId,String companyId,String createPersonId ) throws Exception;


    /**
     * 创建乙方动态
     * @param projectId
     * @param companyId
     * @param createPersonId
     * @return
     */
    public AjaxMessage combinationDynamicForPartyB(String projectId,String companyId,String createPersonId)  throws Exception;


    /**
     * 创建合作方动态
     * @param projectId
     * @param companyId
     * @param partnerId
     * @param taskId
     * @param createPersonId
     * @return
     */
    public  AjaxMessage combinationDynamicForPartner(String projectId,String companyId, String partnerId,String taskId,String createPersonId)  throws Exception;

    /***
     * 创建通知公告动态
     * @param noticeId
     * @param companyId
     * @param createPersonId
     * @return
     */
    public  AjaxMessage combinationDynamicForNotice(String noticeId,String companyId,String createPersonId)  throws Exception;


    /**
     * 查询公司的动态
     * @param paraMap
     * @return
     */
    public Map<String,Object> getOrgDynamicListByCompanyId(Map<String,Object>paraMap)  throws Exception;


    /**
     * 根据类型查询公司的动态(最新五条)
     * @param paraMap
     * @return
     */
    public List<OrgDynamicEntity> getLastOrgDynamicListByCompanyId(Map<String,Object>paraMap);

}
