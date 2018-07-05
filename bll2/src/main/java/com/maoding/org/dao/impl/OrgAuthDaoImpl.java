package com.maoding.org.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.org.dao.OrgAuthDao;
import com.maoding.org.dto.OrgAuthenticationDataDTO;
import com.maoding.org.dto.OrgAuthenticationDataPageDTO;
import com.maoding.org.dto.OrgAuthenticationQueryDTO;
import com.maoding.org.entity.OrgAuthEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：OrgAuthDao
 * 类描述：组织审核dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:44:06
 */
@Service("orgAuthDao")
public class OrgAuthDaoImpl extends GenericDao<OrgAuthEntity> implements OrgAuthDao {

    @Override
    public OrgAuthenticationDataDTO getOrgAuthenticationInfo(String id) {
        return this.sqlSession.selectOne("GetOrgAuthMapper.getOrgAuthenticationInfo",id);
    }

    /**
     * 方法：根据查询条件查找审核信息列表
     * 作者：ZCL
     * 日期：2017/7/11
     *
     * @param query 查询条件
     */
    @Override
    public List<OrgAuthenticationDataDTO> listOrgAuthenticationInfo(OrgAuthenticationQueryDTO query) {
        return this.sqlSession.selectList("GetOrgAuthMapper.listOrgAuthenticationInfo",query);
    }

    @Override
    public OrgAuthenticationDataPageDTO getOrgAuthenticationInfoPage(OrgAuthenticationQueryDTO query) {
        List<OrgAuthenticationDataDTO> list = listOrgAuthenticationInfo(query);
        OrgAuthenticationDataPageDTO page = new OrgAuthenticationDataPageDTO();
        page.setTotal(sqlSession.selectOne("CommonMapper.getLastQueryCount"));
        if (page.getTotal() > 0) {
            page.setList(list);
        }
        return page;
    }
}
