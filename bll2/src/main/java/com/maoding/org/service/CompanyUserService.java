package com.maoding.org.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.org.dto.CompanyUserDTO;
import com.maoding.org.dto.CompanyUserLiteDTO;
import com.maoding.org.dto.CompanyUserTableDTO;
import com.maoding.org.dto.ImportFileCompanyUserDTO;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyUserService
 * 类描述：团队（公司）用户  Service
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:22:43
 */
public interface CompanyUserService extends BaseService<CompanyUserEntity>{

	/**
	 * 方法描述：根据userId和companyId查找人员（一个人在一个公司只存在一条记录）
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public CompanyUserEntity getCompanyUserByUserIdAndCompanyId(String userId,String companyId) throws Exception;
	/**
	 * 方法描述：根据accountId和companyId查找人员（一个人在一个公司只存在一条记录）
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 * @param accountId
	 * @param companyId
	 * @return
	 */
	public CompanyUserLiteDTO getCompanyUserLiteDTO(String accountId, String companyId);
	/**
	 * 方法描述：查找当前公司所有人员
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 * @param companyId
	 * @return
	 */
	public List<CompanyUserEntity> getCompanyUserByCompanyId(String companyId);


	/**
	 * 方法描述：根据id查询数据
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 * @param id
	 * @return
	 */
	public CompanyUserTableDTO getCompanyUserById(String id) throws Exception;
	
	/**
	 * 方法描述：组织人员条数（分页查询）（暂无使用）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	public int getCompanyUserByOrgIdCount(Map<String,Object> param) throws Exception;


	/**
	 * 方法描述：组织人员查询（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param(orgId,keyword,startPage,endPage)【orgId必传，startPage,endPage分页，不传，则查询所有】
	 * @return
	 * @throws Exception
	 */
	public List<CompanyUserTableDTO> getCompanyUserByOrgIdOfAdmin(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：组织人员条数（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	public int getCompanyUserByOrgIdCountOfAdmin(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：组织人员查询
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param(orgId)【orgId组织Id，companyId 公司Id】
	 */
	public List<CompanyUserTableDTO> getUserByOrgId (Map<String,Object> param) throws Exception;

	/** 方法描述：企业未激活人员查询（分页查询）
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
	 * 方法描述：保存团队人员（新增，更改）
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:22:53
	 * @param dto
	 * @return
	 */
	public AjaxMessage saveCompanyUser(CompanyUserTableDTO dto) throws Exception;

	/**
	 * 方法描述：添加/移除用户到团队群,
	 * 作者：MaoSF
	 * 日期：2016/11/29
	 * @param:orgId(组织id，公司或许部门），userId，auditStatus：成员的状态（1有效，4：离职）
	 * @return:
	 */
	public void sendCompanyUserMessageFun(String orgId ,String userId,String auditStatus) throws Exception;
	
	/**
	 * 方法描述：批量导入团队人员信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午6:55:01
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public Object addUserFile(List<ImportFileCompanyUserDTO> list)throws Exception;
	
	/**
	 * 方法描述：离职处理
	 * 作        者：MaoSF
	 * 日        期：2016年3月23日-下午6:35:02
	 * @param id
	 * @return
	 */
	public AjaxMessage quit(String id) throws Exception;

	/**
	 * 方法描述：
	 * 作者：MaoSF
	 * 日期：2016/8/5
	 * @param:
	 * @return:
	 */
	public AjaxMessage orderCompanyUser(CompanyUserTableDTO dto1,CompanyUserTableDTO dto2,String orgId)throws Exception;

	public String addOneLevelDepartGroup(String id,String departName,String userId) throws Exception;
	/**
	 * 方法描述：查询部门用户
	 * 作        者：MaoSF
	 * 日        期：2016年4月23日-下午6:02:15
	 * @param param
	 * @return
	 */
	public List<CompanyUserEntity> getUserByDepartId(Map<String,Object> param);

	public void addUserToGroup(String userId, String groupId,String departId)  throws Exception;
	/**
	 * 方法描述：查询一级部门群groupId
	 * 作        者：MaoSF
	 * 日        期：2016年4月23日-下午6:02:15
	 * @param id
	 * @return
	 */
	public String selectOneLevelDepartGroupId(String id, String departName, String userId);



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
	 * 获取通知公告的接收人员
	 * @param pubMap
	 * @return
	 */
	public  List<CompanyUserDTO>  selectCompanyUserId(Map<String,Object>pubMap) throws Exception;



	/**
	 * 方法描述：批量导入处理添加成功员工，添加到群组当中
	 * 作者：Chenzhujie
	 * 日期：2016/11/21
	 * @param:
	 * @return:
	 */
	public void handleCompanyIm(List<ImportFileCompanyUserDTO> cList)throws  Exception;


	//==================================================2.0============================================
	/**
	 * 方法描述：组织人员查询（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param@return
	 * @throws Exception
	 */
	public List<CompanyUserTableDTO> getCompanyUserListByOrgIdOfAdmin(Map<String, Object> param) throws Exception;

	/**
	 * 方法描述：组织人员条数（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	public int getCompanyUserListByOrgIdCountOfAdmin(Map<String,Object> param) throws Exception;


	/**
	 * 方法描述：企业未激活人员查询（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param(companyId,startPage,endPage)【companyId必传，startPage,endPage分页，不传，则查询所有】
	 * @return
	 * @throws Exception
	 */
	public List<CompanyUserTableDTO> getComUserOfNotActive(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：：企业未激活人员条数（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param (companyId)【companyId必传】
	 * @return
	 * @throws Exception
	 */
	public int getComUserOfNotActiveCount(Map<String,Object> param) throws Exception;




	/**
	 * 方法描述：组织人员查询（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param(orgId,keyword,startPage,endPage)【orgId必传，startPage,endPage分页，不传，则查询所有】
	 * @return
	 * @throws Exception
	 */
	public List<CompanyUserTableDTO> getComUserByOrgIdOfWork(Map<String,Object> param) throws Exception;


	/**
	 * 方法描述：组织人员条数（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	public int getComUserByOrgIdCountOfWork(Map<String,Object> param) throws Exception;



	/**
	 * 方法描述：组织人员查询（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param(orgId,keyword,startPage,endPage)【orgId必传，startPage,endPage分页，不传，则查询所有】
	 * @return
	 * @throws Exception
	 */
	public List<CompanyUserTableDTO> getComUserByOrgIdOfAdmin(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：组织人员条数（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	public int getComUserByOrgIdCountOfAdmin(Map<String,Object> param) throws Exception;


	/**
	 * 方法描述：根据关键字查询公司成员
	 * 作者：MaoSF
	 * 日期：2017/3/2
	 * @param:
	 * @return:
	 */
	List<Map<String,String>> getUserByKeyWord(Map<String, Object> param);

	/**
	 * 方法描述：查询人员是否在该组织
	 * 作者：wrb
	 * 日期：2017/6/30
	 * @param:
	 * @return:
	 */
	public boolean isUserInOrg(Map <String,Object> param);

	/**
	 * 组织经营总监
	 */
	List<CompanyUserTableDTO> getOperatorManager(String companyId);

	/**
	 * 财务出账
	 */
	List<CompanyUserTableDTO> getFinancialManager(String companyId);

	/**
	 * 财务收款组
	 */
	List<CompanyUserTableDTO> getFinancialManagerForReceive(String companyId);

	/**
	 * 组织项目总监
	 */
	List<CompanyUserTableDTO> getDesignManager(String companyId);

	/**
	 * 企业负责人
	 */
	CompanyUserTableDTO getOrgManager(String companyId);

}
