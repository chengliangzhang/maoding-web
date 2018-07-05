package com.maoding.financial.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.financial.dao.ExpCategoryDao;
import com.maoding.financial.dao.ExpCategoryRelationDao;
import com.maoding.financial.dto.ExpCategoryDataDTO;
import com.maoding.financial.dto.ExpCountDTO;
import com.maoding.financial.dto.QueryExpCategoryDTO;
import com.maoding.financial.dto.SaveExpCategoryShowStatusDTO;
import com.maoding.financial.entity.ExpCategoryEntity;
import com.maoding.financial.entity.ExpCategoryRelationEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpCategoryDaoImpl
 * 描    述 : 报销类别DaoImpl
 * 作    者 : MaoSF
 * 日    期 : 2016/10/09-15:52
 */

@Service("expCategoryRelationDao")
public class ExpCategoryRelationDaoImpl extends GenericDao<ExpCategoryRelationEntity> implements ExpCategoryRelationDao {


    @Override
    public int getSelectedCategory(String companyId) {
        return this.sqlSession.selectOne("ExpCategoryRelationEntityMapper.getSelectedCategory",companyId);
    }

    @Override
    public int insertCategoryFromRootCompany(QueryExpCategoryDTO dto) {
        return this.sqlSession.insert("ExpCategoryRelationEntityMapper.insertCategoryFromRootCompany",dto);
    }

    @Override
    public int insertBatch(SaveExpCategoryShowStatusDTO dto) {
        return this.sqlSession.insert("ExpCategoryRelationEntityMapper.insertBatch",dto);
    }

    @Override
    public int deleteCategoryByCompany(SaveExpCategoryShowStatusDTO dto) {
        return this.sqlSession.delete("ExpCategoryRelationEntityMapper.deleteCategoryByCompany",dto);
    }
}
