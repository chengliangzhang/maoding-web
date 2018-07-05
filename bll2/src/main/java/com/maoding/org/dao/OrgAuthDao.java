package com.maoding.org.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.dto.OrgAuthenticationDataDTO;
import com.maoding.org.dto.OrgAuthenticationDataPageDTO;
import com.maoding.org.dto.OrgAuthenticationQueryDTO;
import com.maoding.org.entity.OrgAuthEntity;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：OrgAuthDao
 * 类描述：组织审核dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:42:48
 */
public interface OrgAuthDao extends BaseDao<OrgAuthEntity>{
    /**
     * 方法：根据组织ID获取审核信息
     * 作者：MSF
     * 日期：2017/7/11
     */
    OrgAuthenticationDataDTO getOrgAuthenticationInfo(String id);

    /**
     * 方法：根据查询条件查找审核信息列表
     * 作者：ZCL
     * 日期：2017/7/11
     */
    List<OrgAuthenticationDataDTO> listOrgAuthenticationInfo(OrgAuthenticationQueryDTO query);

    /**
     * 方法：根据查询条件分页查找审核信息列表，并返回全部记录数
     * 作者：ZCL
     * 日期：2017/7/11
     */
    OrgAuthenticationDataPageDTO getOrgAuthenticationInfoPage(OrgAuthenticationQueryDTO query);
}
