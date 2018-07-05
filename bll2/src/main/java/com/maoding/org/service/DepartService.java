package com.maoding.org.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.org.dto.DepartDTO;
import com.maoding.org.dto.DepartRoleDTO;
import com.maoding.org.entity.DepartEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：departService
 * 类描述：部门           Service
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:22:43
 */
public interface DepartService extends BaseService<DepartEntity>{

	/**
	 * 方法描述：根据companyId查询Departs（部门）
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 * @param paraMap（companyId（公司ID）,departLevel（部门层级）,pid（部门父ID）,departType（部门type）,type:0:部门，1：分支机构）
	 */
	List<DepartDTO> getDepartByCompanyId(Map<String,Object>paraMap)throws Exception;

	/**
	 * 方法描述：使用递归查询公司部门
	 * 作者：MaoSF
	 * 日期：2016/9/18
	 */
	List<DepartDTO> getDepartByCompanyId(Map<String,Object> param,List<DepartDTO> departDTOList) throws Exception;

	/**
	 * 方法描述：根据companyId和userId查询Departs（部门）包含公司
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 * @param paraMap（companyId（公司ID）,userId（用户Id））
	 */
	List<DepartDTO> getDepartByUserIdContentCompany(Map<String,Object>paraMap) throws Exception;
	
	/**
	 * 方法描述：增加修改部门
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-上午11:15:52
	 */
	AjaxMessage saveOrUpdateDepart(DepartDTO dto)throws Exception;


	/**
	 * 方法描述：删除部门（递归删除）【删除部门及所有的子部门和人员】
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-上午11:15:52
	 */
	AjaxMessage deleteDepartById(String id,String accountId)throws Exception;
	
	/**
	 * 方法描述：获取公司部门数
	 * 作        者：MaoSF
	 * 日        期：2016年7月14日-下午8:21:40
	 */
	int getDepartByCompanyIdCount(String companyId)throws Exception;
	
	/**
	 * 方法描述：获取具有某些角色的部门（当前人在当前组织下的部门）
	 * 作者：MaoSF
	 * 日期：2016/8/16
	 */
	List<DepartRoleDTO> getDepartByRole(Map<String,Object>paraMap);

	/**
	 * 方法描述：获取所有组织的角色（在当前公司下）
	 * 作者：MaoSF
	 * 日期：2016/8/16
	 */
	List<DepartRoleDTO> getOrgRole(Map<String,Object>paraMap);

	/**
	 * 方法描述：查询没有创建群组的一级部门群
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 */
	List<DepartEntity> selectNotCreateGroupDepart(Map<String,Object>paraMap)throws Exception;

	/**
	 * 方法描述：查询所有子部门
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 */
    List<DepartEntity> selectChildDepartEntity(String departPath)throws Exception;
	/**
	 * 方法描述：查询创建群组的一级部门群
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 */
	List<DepartEntity> selectCreateGroupDepart(Map<String, Object> params)throws Exception;
}
