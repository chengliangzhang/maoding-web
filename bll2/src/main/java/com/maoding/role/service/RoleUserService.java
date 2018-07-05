package com.maoding.role.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.role.dto.RoleUserDTO;
import com.maoding.role.dto.SaveRoleUserDTO;
import com.maoding.role.dto.UserOrgRoleDTO;
import com.maoding.role.entity.RoleUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RoleService
 * 类描述：用户角色权限Service
 * 作    者：MaoSF
 * 日    期：2016年7月11日-下午3:28:54
 */
public interface RoleUserService  extends BaseService<RoleUserEntity>{

	/**
	 * 方法描述：获取当前用户所在组织（包含独立部门）的所有权限
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午3:33:39
	 * @param param（companyId,departType=1）
	 * @return
	 */
	public List<UserOrgRoleDTO> getOrgUserRoles(Map<String,Object> param);
	
	/**
	 * 方法描述：保存用户权限
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午5:23:51
	 */
	public Object saveOrUserRoles(RoleUserDTO dto);

	/**
	 * 方法描述：保存用户权限
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午5:23:51
	 */
	public AjaxMessage saveOrUserRole(SaveRoleUserDTO dto)  throws Exception;

	/**
	 * 方法描述：删除用户角色（根据userId，companyId，roleId）
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 */
	public AjaxMessage deleteRoleUser(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：初始化用户角色对应的权限 2011-11-11 （数据迁移使用）
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 */
	public AjaxMessage initRoleUserPermission() throws Exception;

	/**
	 * 方法描述：企业负责人，系统管理员 移交调用接口
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 */
	AjaxMessage saveOrgManager(SaveRoleUserDTO dto) throws Exception;

}
