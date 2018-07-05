package com.maoding.org.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.entity.OrgAuthAuditDO;

/**
 * Created by Chengliang.zhang on 2017/7/12.
 */
public interface OrgAuthAuditDao extends BaseDao<OrgAuthAuditDO> {
    void updateStatusByOrgId(String orgId);
}
