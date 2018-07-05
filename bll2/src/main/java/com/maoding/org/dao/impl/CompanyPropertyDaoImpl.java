package com.maoding.org.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.org.dao.CompanyPropertyDao;
import com.maoding.org.entity.CompanyPropertyEntity;
import com.maoding.project.dto.CustomProjectPropertyDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chengliang.zhang on 2017/8/17.
 */
@Service("CompanyPropertyDao")
public class CompanyPropertyDaoImpl extends GenericDao<CompanyPropertyEntity> implements CompanyPropertyDao {
    /**
     * 获取默认的项目自定义属性
     *
     * @param companyId 组织ID
     */
    @Override
    public List<CustomProjectPropertyDTO> listDefaultProperty(String companyId) {
        return sqlSession.selectList("CompanyPropertyEntityMapper.listDefaultProperty",companyId);
    }

    /**
     * 获取公司自定义的项目自定义属性
     *
     * @param companyId 组织ID
     */
    @Override
    public List<CustomProjectPropertyDTO> listCustomProperty(String companyId) {
        return sqlSession.selectList("CompanyPropertyEntityMapper.listCustomProperty",companyId);
    }

    /**
     * 获取卯丁默认的项目自定义属性
     */
    @Override
    public List<CompanyPropertyEntity> listCommonDefaultProperty() {
        return sqlSession.selectList("CompanyPropertyEntityMapper.listCommonDefaultProperty");
    }

    /**
     * 插入默认的项目自定义属性
     *
     * @param companyId 新添加的自定义属性模板记录的组织id
     * @param entityList 默认的自定义属性（组织被忽略)
     */
    @Override
    public int insertDefaultProperty(@Param("companyId") String companyId, @Param("entityList") List<CompanyPropertyEntity> entityList) {
        Map<String,Object> params = new HashMap<>();
        params.put("companyId",companyId);
        params.put("entityList",entityList);
        return sqlSession.insert("CompanyPropertyEntityMapper.insertDefaultProperty",params);
    }

    /**
     * 删除记录
     *
     * @param id
     */
    @Override
    public int fakeDeleteById(String id) {
        return sqlSession.update("CompanyPropertyEntityMapper.fakeDeleteById",id);
    }

    /**
     * 获取单位名称列表
     */
    @Override
    public List<String> listUnitName() {
        return sqlSession.selectList("CompanyPropertyEntityMapper.listUnitName");
    }

    /**
     * 获取公司自定义的所有项目属性
     *
     * @param companyId
     */
    @Override
    public List<CustomProjectPropertyDTO> listCompanyProperty(String companyId) {
        return sqlSession.selectList("CompanyPropertyEntityMapper.listCompanyProperty",companyId);
    }
}
