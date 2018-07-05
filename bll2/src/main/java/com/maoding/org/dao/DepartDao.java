package com.maoding.org.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.dto.DepartRoleDTO;
import com.maoding.org.dto.UserDepartDTO;
import com.maoding.org.entity.DepartEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DepartDao
 * 类描述：部门 Dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:42:48
 */
public interface DepartDao extends BaseDao<DepartEntity>{
	
	/**
	 * 方法描述：根据companyId查询Departs（部门）
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 * @param paraMap（companyId（公司ID）,departLevel（部门层级）,pid（部门父ID）,departType（部门type）type:0:部门，1：分支机构）
	 * @return
	 */
	public List<DepartEntity> getDepartByCompanyId(Map<String,Object>paraMap);

	/**
	 * 方法描述：根据companyId和userId查询Departs（部门）包含公司
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 * @param paraMap（companyId（公司ID）,userId（用户Id））
	 * @return
	 */
	public List<DepartEntity> getDepartByUserIdContentCompany(Map<String,Object>paraMap);
	
	/**
	 * 方法描述：根据部门名称和父id查找部门（用于添加部门，验证是否在同级下面是否已经存在）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-上午11:22:23
	 * @param paraMap(companyId（公司ID）,departDepart（部门名称）,pid（部门父ID）
	 * @return
	 */
	public DepartEntity getByDepartNameAndPid(Map<String,Object>paraMap);
	
	/**
	 * 方法描述：根据部门路径（父级部分的id拼接）查找子部门（可包含当前部门）【用于修改部门，修改父部门时使用】
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-上午11:48:56
	 * @param departPath
	 * @return
	 */
	public List<DepartEntity> getDepartsByDepartPath(String departPath);
	
	/**
	 * 方法描述：查询部门列表
	 * 作        者：MaoSF
	 * 日        期：2016年6月16日-上午11:05:07
	 * @param param{companyIds}
	 * @return
	 */
	public List<DepartEntity> selectDepartNodesByCompanyIds(Map<String,Object> param);
	/**
	 * 方法描述：查询部门边列表
	 * 作        者：MaoSF
	 * 日        期：2016年6月16日-上午11:05:43
	 * @param param{companyIds}
	 * @return
	 */
	public List<Map<String, Object>>selectDepartEdgesByCompanyIds(Map<String,Object> param);
	
	/**
	 * 方法描述：获取用户所在组织的部门信息（param:companyId,必填，departType=1:查询独立部门）
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午5:07:48
	 * @param param
	 * @return
	 */
	public List<UserDepartDTO> selectUserDpartByParam(Map<String,Object> param);
	
	/**
	 * 方法描述：获取公司部门数
	 * 作        者：MaoSF
	 * 日        期：2016年7月14日-下午8:21:40
	 * @param companyId
	 * @return
	 */
	public int getDepartByCompanyIdCount(String companyId);

	/**
	 * 方法描述：根据departPath删除（批量删除，删除自己及所有子部门）逻辑删除
	 * 作        者：MaoSF
	 * 日        期：2016年7月14日-下午8:21:40
	 * @param departPath
	 * @return
	 */
	public int deleteByDepartPath(String departPath);


	/**
	 * 方法描述：获取具有某些角色的部门（当前人在当前组织下的部门）
	 * 作者：MaoSF
	 * 日期：2016/8/16
	 * @param:
	 * @return:
	 */
	public List<DepartRoleDTO> getDepartByRole(Map<String,Object>paraMap);

	/**
	 * 方法描述：获取所有组织的角色（在当前公司下）
	 * 作者：MaoSF
	 * 日期：2016/8/16
	 * @param:
	 * @return:
	 */
	public List<DepartRoleDTO> getOrgRole(Map<String,Object>paraMap);


	/**
	 *selectDepartByParam
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 * @param paraMap userId
	 * @return
	 */
	public List<DepartEntity> selectDepartByParam(Map<String,Object>paraMap);
	/**
	 *selectOneDepartCompanyId
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 * @return
	 */
	public List<DepartEntity> selectStairDepartCompanyId(Map<String, Object> params);

	/**
	 *查询没有创建群组的一级部门群
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 * @return
	 */
	public List<DepartEntity> selectNotCreateGroupDepart(Map<String, Object> paraMap);
	/**
	 *查询创建群组的一级部门群
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 * @return
	 */
	public List<DepartEntity> selectCreateGroupDepart(Map<String, Object> paraMap);

}
