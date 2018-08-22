package com.maoding.org.dao;

import com.maoding.commonModule.dto.QueryCopyRecordDTO;
import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.dto.*;
import com.maoding.org.entity.CompanyUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyDao
 * 类描述：团队(公司）Dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:42:48
 */
public interface CompanyUserDao extends BaseDao<CompanyUserEntity>{

	/**
	 * 方法描述：根据userId和companyId查找人员（一个人在一个公司最多只存在一条记录)【用于添加人员】
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public CompanyUserEntity getCompanyUserByUserIdAndCompanyId(String userId, String companyId);

	CompanyUserLiteDTO getCompanyUserLiteDTO(String accountId, String companyId);

	/**
	 * 方法描述：查找当前公司所有人员
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 * @param companyId
	 * @return
	 */
	public List<CompanyUserEntity> getCompanyUserByCompanyId(String companyId);


	/**
	 * 方法描述： 根据参数查找当前公司所有人员
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 * @param param
	 * @return
	 */
	public List<CompanyUserEntity> getCompanyUserByParam(Map <String,Object> param);
	/**
	 * 方法描述：根据id查询数据
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 * @param id
	 * @return
	 */
	public CompanyUserTableDTO getCompanyUserById(String id) throws Exception;

	/**
	 * 方法描述：组织人员条数（分页查询）（以前的，攒无使用，递归查询子节点中人的数）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	public int getCompanyUserByOrgIdCount(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：组织人员查询（分页查询）(admin项目）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param(orgId,keyword,startPage,endPage)【orgId必传，startPage,endPage分页，不传，则查询所有】
	 * @return
	 * @throws Exception
	 */
	public List<CompanyUserTableDTO> getCompanyUserByOrgIdOfAdmin(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：组织人员查询（分页查询）（work项目）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param(orgId,keyword,startPage,endPage)【orgId必传，startPage,endPage分页，不传，则查询所有】
	 * @return
	 * @throws Exception
	 */
	public List<CompanyUserTableDTO> getCompanyUserByOrgIdOfWork(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：组织人员查询
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param(orgId)【orgId组织Id，companyId 公司Id】
	 */
	public List<CompanyUserTableDTO> getUserByOrgId (Map<String,Object> param) throws Exception;
	

	/**
	 * 方法描述：组织人员条数（分页查询）（work项目）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	public int getCompanyUserByOrgIdCountOfWork(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：组织人员条数（分页查询）（admin项目）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	public int getCompanyUserByOrgIdCountOfAdmin(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：组织人员部门及权限
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param userId
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public List<UserDepartDTO> getCompanyUserDepartRole(String userId,String companyId);

	/**
	 * 方法描述：企业未激活人员查询（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param(companyId,startPage,endPage)【companyId必传，startPage,endPage分页，不传，则查询所有】
	 * @return
	 * @throws Exception
	 */
	public List<CompanyUserTableDTO> getCompanyUserOfNotActive(Map<String,Object> param) throws Exception;
	
	/**
	 * 方法描述：：企业未激活人员条数（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param (companyId)【companyId必传】
	 * @return
	 * @throws Exception
	 */
	public int getCompanyUserOfNotActiveCount(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：查询部门用户
	 * 作        者：MaoSF
	 * 日        期：2016年4月23日-下午6:02:15
	 * @param param
	 * @return
	 */
    public List<CompanyUserEntity> getUserByDepartId(Map<String, Object> param);

	/**
	 * 根据userId查询CompanyuserEntity
	 * @param userId
	 * @return
     */
	public List<CompanyUserEntity> getCompanyUserId(String userId);



	/**
	 * 方法描述：根据角色id查询公司人员
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 * @param:
	 * @return:
	 */
	List<CompanyUserTableDTO> getCompanyUserByRoleId(Map<String, Object> param);

	/**
	 * 方法描述：根据权限id查询公司人员
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 * @param:
	 * @return:
	 */
	List<CompanyUserTableDTO> getCompanyUserByPermissionId(Map<String, Object> param);

	/**
	 * 方法描述：根据orgList查询人员的账号id
	 * 作者：MaoSF
	 * 日期：2016/12/8
	 * @param:
	 * @return:
	 */
	List<String> getUserByOrgIdForNotice(Map<String, Object> param);

	
	/**
	 * 方法描述：根据关键字查询公司成员
	 * 作者：MaoSF
	 * 日期：2017/3/2
	 */
	List<Map<String,String>> getUserByKeyWord(Map<String, Object> param);

	/**
	 * 方法描述：获取所有人员（用于数据迁移处理）
	 * 作者：MaoSF
	 * 日期：2017/4/13
	 * @param:
	 * @return:
	 */
	List<CompanyUserEntity> getAllCompanyUser();

	/**
	 * 获取字段值
     */
	String getCompanyUserId(CompanyUserEntity companyUser);
	String getUserId(CompanyUserEntity companyUser);
	String getCompanyId(CompanyUserEntity companyUser);
	String getUserName(CompanyUserEntity companyUser);

	String getUserNameByCompanyIdAndUserId(String companyId,String userId);

	String getUserName(String companyUserId);
	/**
	 * 方法描述：查询员工简单信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 */
	CompanyUserAppDTO getCompanyUserDataById(String companyUserId);


	/**
	 * 方法描述：获取抄送人的信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 */
	List<CompanyUserDataDTO> getCopyUser(QueryCopyRecordDTO query);
}
