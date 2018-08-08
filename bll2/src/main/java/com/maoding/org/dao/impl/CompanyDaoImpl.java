package com.maoding.org.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.org.dto.CompanyQueryDTO;
import com.maoding.org.dto.CompanyRelationDTO;
import com.maoding.org.dto.CompanyUserDTO;
import com.maoding.org.entity.CompanyEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyDaoImpl
 * 类描述：类描述：团队(公司）DaoImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:44:06
 */
@Service("companyDao")
public class CompanyDaoImpl extends GenericDao<CompanyEntity> implements CompanyDao {


    @Override
    public List<CompanyDTO> getCompanyByUserId(String userId) {
        return this.sqlSession.selectList("GetCompanyByUserIdMapper.getCompanyByUserId", userId);
    }

    /**
     * 方法描述：获取所有公司（全部公司注册环信群，手工操作）
     * 作者：MaoSF
     * 日期：2016/8/23
     */
    @Override
    public List<CompanyEntity> selectAll() {
        return this.sqlSession.selectList("CompanyEntityMapper.selectAll");
    }

    /**
     * 方法描述：获取所有公司（全部公司注册环信群，手工操作）
     * 作者：MaoSF
     * 日期：2016/8/23
     */
    @Override
    public List<CompanyEntity> selectAllIm() {
        return this.sqlSession.selectList("CompanyEntityMapper.selectAllIm");
    }

    @Override
    public List<Map<String, Object>> selectAllCompanyEdges(Map<String, Object> map) {
        return this.sqlSession.selectList("QueryOrgTreeEntityMapper.selectAllCompanyEdges", map);
    }

    @Override
    public List<CompanyDTO> getCompanyFilterbyParam(Map<String, Object> map) {
        return this.sqlSession.selectList("GetCompanyFilterbyParamMapper.getCompanyFilterbyParam", map);
    }

    @Override
    public int getCompanyFilterbyParamCount(Map<String, Object> map) {
        Object count = this.sqlSession.selectOne("GetCompanyFilterbyParamMapper.getCompanyFilterbyParamCount", map);
        return count == null ? 0 : Integer.parseInt(count.toString());
    }

    @Override
    public CompanyEntity getParentCompany(String id)  {
        return this.sqlSession.selectOne("QueryOrgTreeEntityMapper.getParentCompany", id);
    }

    @Override
    public CompanyEntity getParentCompanyByName(String companyName) throws Exception {
        return this.sqlSession.selectOne("CompanyMapper.getParentCompanyByName", companyName);
    }

    @Override
    public List<CompanyEntity> getChildrenCompany(String id) {
        return this.sqlSession.selectList("QueryOrgTreeEntityMapper.getChildrenCompany", id);
    }

    /**
     * 方法描述：  合作设计费统计(收款方向）
     * 作者：MaoSF
     * 日期：2016/8/15
     */
    @Override
    public List<CompanyEntity> getCompanyListBySearch(String companyName) {
        return this.sqlSession.selectList("CompanyEntityMapper.getCompanyBySearch", companyName);
    }

    @Override

    public List<CompanyEntity> getAllCompanyListBySearch(String companyName) {
        return this.sqlSession.selectList("CompanyEntityMapper.getAllCompanyBySearch", companyName);
    }


    @Override
    public List<CompanyEntity> getCompanyForProject(String companyId) {
        return this.sqlSession.selectList("CompanyEntityMapper.getCompanyForProject", companyId);
    }

    @Override
    public List<CompanyEntity> getCompanyForMyProject(String companyUserId) {
        return this.sqlSession.selectList("CompanyEntityMapper.getCompanyForMyProject", companyUserId);
    }

    /**
     * 方法描述：获取项目的外部团队
     * 作者：MaoSF
     * 日期：2017/5/8
     */
    @Override
    public List<CompanyEntity> getOuterCooperatorCompany(String companyId, String projectId) {
        Map<String, Object> map = new HashMap<>();
        map.put("companyId", companyId);
        map.put("projectId", projectId);
        return this.sqlSession.selectList("CompanyEntityMapper.getOuterCooperatorCompany", map);
    }

    /**
     * 方法描述：根据公司记录获取公司名
     * 作者：ZCL
     * 日期：2017-5-25
     */
    @Override
    public String getCompanyName(CompanyEntity company) {
        return (company != null) ? company.getCompanyName() : "";
    }

    /**
     * 方法描述：根据公司ID获取公司名
     * 作者：ZCL
     * 日期：2017-5-25
     */
    @Override
    public String getCompanyName(String id) {
        return (id != null) ? getCompanyName(selectById(id)) : "";
    }


    @Override
    public List<String> listCompanyIdByProjectId(String projectId) {
        return this.sqlSession.selectList("CompanyEntityMapper.listCompanyIdByProjectId", projectId);
    }

    /**
     * 根据甲方公司名获取公司相关甲方ID
     *
     * @param companyName
     * @param relateCompanyId
     */
    @Override
    public String getCompanyIdByCompanyNameForA(@Param("companyName") String companyName, @Param("relateCompanyId") String relateCompanyId) {
        Map<String, String> map = new HashMap<>();
        map.put("companyName", companyName);
        map.put("relateCompanyId", relateCompanyId);
        return this.sqlSession.selectOne("CompanyMapper.getCompanyIdByCompanyNameForA", map);
    }

    /**
     * 根据乙方公司名称获取公司相关乙方ID
     *
     * @param companyName
     * @param relateCompanyId
     */
    @Override
    public String getCompanyIdByCompanyNameForB(@Param("companyName") String companyName, @Param("relateCompanyId") String relateCompanyId) {
        Map<String, String> map = new HashMap<>();
        map.put("companyName", companyName);
        map.put("relateCompanyId", relateCompanyId);
        return this.sqlSession.selectOne("CompanyMapper.getCompanyIdByCompanyNameForB", map);
    }

    /**
     * 获取公司具有相关权限的用户ID
     *
     * @param companyId
     * @param permissionId
     */
    @Override
    public List<String> listUserIdByCompanyIdAndPermissionId(@Param("companyId") String companyId, @Param("permissionId") String permissionId) {
        Map<String, String> map = new HashMap<>();
        map.put("companyId", companyId);
        map.put("permissionId", permissionId);
        return this.sqlSession.selectList("CompanyMapper.listUserIdByCompanyIdAndPermissionId", map);
    }

    /**
     * 根据公司ID和用户ID获取雇员ID
     *
     * @param companyId
     * @param userId
     */
    @Override
    public String getCompanyUserIdByCompanyIdAndUserId(@Param("companyId") String companyId, @Param("userId") String userId) {
        if ((companyId == null) || (userId == null)) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("companyId", companyId);
        map.put("userId", userId);
        return this.sqlSession.selectOne("CompanyMapper.getCompanyUserIdByCompanyIdAndUserId", map);
    }

    /**
     * 根据相关公司ID获取总公司ID
     *
     * @param companyId
     */
    @Override
    public String getRootCompanyId(String companyId) {
        return this.sqlSession.selectOne("CompanyMapper.getRootCompanyId", companyId);
    }

    /**
     * 根据用户名和用户所在公司名获取用户信息
     *
     * @param companyName
     * @param userName
     */
    @Override
    public CompanyUserDTO getCompanyUserByCompanyNameAndUserName(@Param("companyName") String companyName, @Param("userName") String userName) {
        Map<String, String> map = new HashMap<>();
        map.put("companyName", companyName);
        map.put("userName", userName);
        return this.sqlSession.selectOne("CompanyMapper.getCompanyUserByCompanyNameAndUserName", map);
    }

    @Override
    public List<CompanyEntity> listCompanyByCompanyName(String companyName, String orgPid, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("orgPid", orgPid);
        map.put("type", type);
        map.put("companyName", companyName);
        return this.sqlSession.selectList("CompanyEntityMapper.listCompanyByCompanyName", map);
    }

    @Override
    public String getCompanyIdByOrgPidAndCompanyId(String orgPid, String companyName) {
        Map<String, String> map = new HashMap<>();
        map.put("orgPid", orgPid);
        map.put("companyName", companyName);
        return this.sqlSession.selectOne("CompanyMapper.getCompanyIdByOrgPidAndCompanyId", map);
    }

    @Override
    public List<CompanyRelationDTO> getExpAmountCompanyAndChildren(String orgPid) {
        return this.sqlSession.selectList("QueryCompanyRelationEntityMapper.getExpAmountCompanyAndChildren", orgPid);
    }

    public CompanyRelationDTO getOrgType(String orgId) {
        return this.sqlSession.selectOne("QueryCompanyRelationEntityMapper.getOrgType", orgId);
    }

    /**
     * 描述     查询相关甲方
     * 日期     2018/8/8
     *
     * @param query 查询条件
     * @return 符合条件的公司信息列表
     * @author 张成亮
     **/
    @Override
    public List<CompanyDTO> listCompanyA(CompanyQueryDTO query) {
        return sqlSession.selectList("CompanyMapper.listCompanyA", query);
    }

    /**
     * 描述     查询相关合作方
     * 日期     2018/8/8
     *
     * @param query 查询条件
     * @return 符合条件的公司信息列表
     * @author 张成亮
     **/
    @Override
    public List<CompanyDTO> listCompanyCooperate(CompanyQueryDTO query) {
        return sqlSession.selectList("CompanyMapper.listCompanyCooperate", query);
    }
}
