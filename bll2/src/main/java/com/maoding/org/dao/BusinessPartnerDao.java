package com.maoding.org.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.entity.BusinessPartnerEntity;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：BusinessPartnerDao
 * 类描述：合作伙伴Dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:42:48
 */
public interface BusinessPartnerDao extends BaseDao<BusinessPartnerEntity>{

    String getNickName(String companyId);
    List<BusinessPartnerEntity> getListByNameMask(String nickNameMask);
}
