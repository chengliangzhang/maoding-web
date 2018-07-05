package com.maoding.org.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.org.dao.OrgAuthAuditDao;
import com.maoding.org.entity.OrgAuthAuditDO;
import org.springframework.stereotype.Service;

/**
 * Created by Chengliang.zhang on 2017/7/12.
 */
@Service("orgAuthAuditDao")
public class OrgAuthAuditDaoImpl extends GenericDao<OrgAuthAuditDO> implements OrgAuthAuditDao {
    @Override
    public void updateStatusByOrgId(String orgId) {
        sqlSession.update("OrgAuthAuditDOMapper.updateStatusByOrgId",orgId);
    }
}
