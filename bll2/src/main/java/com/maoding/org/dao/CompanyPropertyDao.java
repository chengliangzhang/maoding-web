package com.maoding.org.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.entity.CompanyPropertyEntity;
import com.maoding.project.dto.CustomProjectPropertyDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/8/17.
 * 项目自定义字段模板数据库
 */
public interface CompanyPropertyDao extends BaseDao<CompanyPropertyEntity> {
    /** 获取默认的项目自定义属性 */
    List<CustomProjectPropertyDTO> listDefaultProperty(String companyId);
    /** 获取公司自定义的项目自定义属性 */
    List<CustomProjectPropertyDTO> listCustomProperty(String companyId);
    /** 获取卯丁默认的项目自定义属性 */
    List<CompanyPropertyEntity> listCommonDefaultProperty();
    /** 插入默认的项目自定义属性 */
    int insertDefaultProperty(@Param("companyId") String companyId, @Param("entityList") List<CompanyPropertyEntity> entityList);
    /** 删除记录 */
    int fakeDeleteById(String id);
    /** 获取单位名称列表 */
    List<String> listUnitName();
    /** 获取公司自定义的所有项目属性 */
    List<CustomProjectPropertyDTO> listCompanyProperty(String companyId);
}
