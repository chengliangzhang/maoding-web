package com.maoding.project.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.project.dto.ProjectConstructDTO;
import com.maoding.project.entity.ProjectConstructEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectConstructService
 * 类描述：Service
 * 作    者：LY
 * 日    期：2016年7月20日- 09:38:54
 */
public interface ProjectConstructService extends BaseService<ProjectConstructEntity> {

    /**
     * 方法描述：根据当前组织查找客户管理列表
     * 作   者：LY
     * 日   期：2016/7/22 10:04
     *
    */
     List<ProjectConstructDTO> getConstructByCompanyId(String companyId)  throws Exception;

    /**
     * 方法描述：根据当前组织查找客户管理列表（模糊查询）
     * 日   期：2016/7/22 10:04
     *
     */
     List<ProjectConstructDTO> getConstructByParam(Map<String,Object> map)  throws Exception;

    /**
     * 方法描述：新增或者修改建设单位
     * 作        者：LY
     * 日        期：2016年7月20日-下午17:58:09
     */
     AjaxMessage saveOrUpdateProjectConstruct(ProjectConstructDTO projectConstructDTO) throws Exception;
    
    /**
     * 方法描述：根据id获取建设单位的详细信息
     * 作者：MaoSF
     * 日期：2016/7/28
     */
     ProjectConstructDTO getProjectConstructById(String id) throws Exception;

    /**
     * 方法描述：根据id获取建设单位的详细信息
     * 作者：MaoSF
     * 日期：2016/7/28
     */
     ProjectConstructDTO getProjectConstructByIdAndOtherDetail(String id,String companyId,String projectId) throws Exception;
    
    /**
     * 方法描述：获取5个常用的建设单位
     * 作者：MaoSF
     * 日期：2016/8/26
     */
     List<ProjectConstructDTO> getUsedConstructByCompanyId(String companyId) throws Exception;
}
