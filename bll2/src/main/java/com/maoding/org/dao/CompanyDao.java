package com.maoding.org.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.org.dto.CompanyQueryDTO;
import com.maoding.org.dto.CompanyRelationDTO;
import com.maoding.org.dto.CompanyUserDTO;
import com.maoding.org.entity.CompanyEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyDao
 * 类描述：团队(公司）Dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:42:48
 */
public interface CompanyDao extends BaseDao<CompanyEntity> {


    /**
     * 方法描述：获取所有企业（组织切换列表）
     * 作        者：MaoSF
     * 日        期：2016年7月8日-下午2:03:16
     *
     * @param userId
     * @return(id,companyName,companyType,companyShortName,status,filePath)
     */
    public List<CompanyDTO> getCompanyByUserId(String userId);

    /**
     * 方法描述：查找组织架构树所有节点边
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午7:20:10
     *
     * @param map{companyId}
     */
    public List<Map<String, Object>> selectAllCompanyEdges(Map<String, Object> map);

    /***************过滤以挂靠的组织**************/
    /**
     * 方法描述：查询（未挂靠组织的组织），大B搜索小b，传pid=当前公司，小b搜索大B传companyId=当前公司
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午10:49:56
     */
    public List<CompanyDTO> getCompanyFilterbyParam(Map<String, Object> map);

    /**
     * 方法描述：查询（未挂靠组织的组织）条数，大B搜索小b，传pid=当前公司，小b搜索大B传companyId=当前公司
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午10:49:56
     */
    public int getCompanyFilterbyParamCount(Map<String, Object> map);

    /*******************生成树的数据*******************/
    /**
     * 方法描述：查询公司的父公司
     * 作        者：MaoSF
     * 日        期：2016年7月15日-下午2:01:41
     */
    public CompanyEntity getParentCompany(String id);

    public CompanyEntity getParentCompanyByName(String companyName) throws Exception;

    /**
     * 方法描述：查询公司的子公司
     * 作        者：MaoSF
     * 日        期：2016年7月15日-下午2:01:41
     */
    public List<CompanyEntity> getChildrenCompany(String id);

    /**
     * 方法描述：获取所有公司（全部公司注册环信群，手工操作）
     * 作者：MaoSF
     * 日期：2016/8/23
     */
    public List<CompanyEntity> selectAll();

    /**
     * 方法描述：获取所有公司（全部公司注册环信群，手工操作）
     * 作者：MaoSF
     * 日期：2016/8/23
     */
    public List<CompanyEntity> selectAllIm();


    /**
     * 方法描述：  合作设计费统计(收款方向）
     * 作者：MaoSF
     * 日期：2016/8/15
     */
    public List<CompanyEntity> getCompanyListBySearch(String companyName);

    public List<CompanyEntity> getAllCompanyListBySearch(String companyName);

    /**
     * 方法描述：获取查询条件
     * 作者：TangY
     * 日期：2016/7/29
     */
    public List<CompanyEntity> getCompanyForProject(String companyId);


    /**
     * 方法描述：获取我的项目查询条件
     * 作者：TangY
     * 日期：2016/7/29
     */
    public List<CompanyEntity> getCompanyForMyProject(String companyUserId);

    /**
     * 方法描述：获取项目的外部团队
     * 作者：MaoSF
     * 日期：2017/5/8
     */
    List<CompanyEntity> getOuterCooperatorCompany(String companyId, String projectId);

    /**
     * 方法描述：根据公司记录获取公司名
     * 作者：ZCL
     * 日期：2017-5-25
     */
    String getCompanyName(CompanyEntity company);

    /**
     * 方法描述：根据公司ID获取公司名
     * 作者：ZCL
     * 日期：2017-5-25
     */
    String getCompanyName(String id);

    List<String> listCompanyIdByProjectId(String projectId);

    /**
     * 根据甲方公司名获取公司相关甲方ID
     */
    String getCompanyIdByCompanyNameForA(@Param("companyName") String companyName, @Param("relateCompanyId") String relateCompanyId);

    /**
     * 根据乙方公司名称获取公司相关乙方ID
     */
    String getCompanyIdByCompanyNameForB(@Param("companyName") String companyName, @Param("relateCompanyId") String relateCompanyId);

    /**
     * 获取公司具有相关权限的用户ID
     */
    List<String> listUserIdByCompanyIdAndPermissionId(@Param("companyId") String companyId, @Param("permissionId") String permissionId);

    /**
     * 根据公司ID和用户ID获取雇员ID
     */
    String getCompanyUserIdByCompanyIdAndUserId(@Param("companyId") String companyId, @Param("userId") String userId);

    /**
     * 根据相关公司ID获取总公司ID
     */
    String getRootCompanyId(String companyId);

    /**
     * 根据用户名和用户所在公司名获取用户信息
     */
    CompanyUserDTO getCompanyUserByCompanyNameAndUserName(@Param("companyName") String companyName, @Param("userName") String userName);

    /**
     * 获取父组织下具有companyName的其他组织
     */
    List<CompanyEntity> listCompanyByCompanyName(String companyName, String orgPid, String type);

    /**
     * 根据server_type和company_name获取id
     */
    String getCompanyIdByOrgPidAndCompanyId(String orgPid, String companyName);

    List<CompanyRelationDTO> getExpAmountCompanyAndChildren(String orgPid);
    CompanyRelationDTO getOrgType(String orgId);

    /**
     * 描述     查询相关乙方
     * 日期     2018/8/8
     * @author  张成亮
     * @return  符合条件的公司信息列表
     * @param   query 查询条件
     **/
    List<CompanyDTO> listCompanyCooperate(CompanyQueryDTO query);
}
