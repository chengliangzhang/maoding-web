package com.maoding.project.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.dto.ProjectConstructDTO;
import com.maoding.project.entity.ProjectConstructEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectConstructDao
 * 类描述：建设单位Dao
 * 作    者：LY
 * 日    期：2016年7月10日-下午2:19:05
 */
public interface ProjectConstructDao extends BaseDao<ProjectConstructEntity> {


    /**
     * 方法描述：根据当前组织ID查找
     * 作        者：wangrb
     * 日        期：2015年11月26日-下午5:01:44
     * @param companyId
     * @return
     */
    public List<ProjectConstructDTO> getConstructByCompanyId(String companyId);

    /**
     * 方法描述：根据当前组织ID查找
     * 作        者：wangrb
     * 日        期：2015年11月26日-下午5:01:44
     * @param paramMap
     * @return
     */
    public ProjectConstructDTO getConstructByName(Map<String,Object> paramMap);

    /**
     * 方法描述：根据当前组织ID查找
     * 作        者：wangrb
     * 日        期：2015年11月26日-下午5:01:44
     * @param param
     * @return
     */
    public List<ProjectConstructDTO> getConstructByParam(Map<String,Object> param);

    /**
     * 方法描述：获取5个常用的建设单位
     * 作者：MaoSF
     * 日期：2016/8/26
     * @param:
     * @return:
     */
    public List<ProjectConstructEntity> selectUsedConstructByCompanyId(String companyId);

}