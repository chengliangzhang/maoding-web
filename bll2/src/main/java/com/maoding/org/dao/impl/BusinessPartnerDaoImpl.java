package com.maoding.org.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.org.dao.BusinessPartnerDao;
import com.maoding.org.entity.BusinessPartnerEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：BusinessPartnerDaoDaoImpl
 * 类描述：合作伙伴DaoImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:44:06
 */
@Service("businessPartnerDao")
public class BusinessPartnerDaoImpl extends GenericDao<BusinessPartnerEntity> implements BusinessPartnerDao {


    @Override
    public String getNickName(String companyId) {
        return this.sqlSession.selectOne("BusinessPartnerEntityMapper.getNickName",companyId);
    }

    @Override
    public List<BusinessPartnerEntity> getListByNameMask(String nickNameMask) {
        return this.sqlSession.selectList("BusinessPartnerEntityMapper.getEntityByNameMask", nickNameMask);
    }

}
