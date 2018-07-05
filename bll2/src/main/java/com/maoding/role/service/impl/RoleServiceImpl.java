package com.maoding.role.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtil;
import com.maoding.org.service.CompanyService;
import com.maoding.role.dao.PermissionDao;
import com.maoding.role.dao.RoleDao;
import com.maoding.role.dao.RolePermissionDao;
import com.maoding.role.dao.RoleUserDao;
import com.maoding.role.dto.PermissionDTO;
import com.maoding.role.dto.RoleDTO;
import com.maoding.role.entity.RoleEntity;
import com.maoding.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RoleService
 * 类描述：角色权限Service
 * 作    者：MaoSF
 * 日    期：2016年7月11日-下午3:28:54
 */
@Service("roleService")
public class RoleServiceImpl  extends GenericService<RoleEntity>  implements RoleService{

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private RoleUserDao roleUserDao;

	@Autowired
	private RolePermissionDao rolePermissionDao;

	@Autowired
	private PermissionDao permissionDao;

	@Autowired
	private CompanyService companyService;

	/**
	 * 方法描述：保存自定义角色
	 * 作者：MaoSF
	 * 日期：2016/11/2:
	 */
	@Override
	public AjaxMessage saveRole(RoleDTO dto) throws Exception {
		RoleEntity roleEntity = new RoleEntity();
		BaseDTO.copyFields(dto,roleEntity);
		if(StringUtil.isNullOrEmpty(dto.getId())){
			dto.setId(StringUtil.buildUUID());
			roleEntity.setId(dto.getId());
			roleDao.insert(roleEntity);
		}else {
			roleDao.updateById(roleEntity);
		}

		return new AjaxMessage().setCode("0").setInfo("保存成功").setData(dto);
	}

	/**
	 * 方法描述：删除权限
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 */
	@Override
	public AjaxMessage deleteRole(String id, String companyId) throws Exception {
		//1.删除角色
		this.roleDao.deleteById(id);

		//2.删除角色与权限的关联
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("roleId", id);
		map.put("companyId", companyId);
		this.rolePermissionDao.deleteByRoleId(map);//先删除权限与角色之间的关系

		//3.删除角色与人员之间的关联

		//删除所有的角色
		map.clear();
		map.put("companyId", companyId);
		map.put("roleId", id);
		this.roleUserDao.deleteUserRole(map);

		return new AjaxMessage().setCode("0").setInfo("删除成功成功");
	}

	@Override
	public List<RoleEntity> getCompanyRole(String companyId) throws Exception{
		return roleDao.getCompanyRole(companyId);
	}
	@Override
	public List<RoleDTO> getCompanyRoleDTO(String companyId) throws Exception{
		return roleDao.getCompanyRoleDTO(companyId);
	}

	/**
	 * 方法描述：根据组织获取用户的角色
	 * 作者：MaoSF
	 * 日期：2016/8/16
	 */
	@Override
	public List<RoleEntity> getRoleByUser(String userId, String orgId) throws Exception{
		return roleDao.getUserRolesByOrgId(userId,orgId);
	}


	/**
	 * 方法描述：获取当前公司角色-权限-人员总览数据
	 * 作者：MaoSF
	 * 日期：2016/11/15
	 */
	@Override
	public List<RoleDTO> getRolePermissionUser(String companyId) throws Exception {
		List<RoleDTO> roleList = this.getCompanyRoleDTO(companyId);
		String typeId = this.companyService.getOrgTypeId(companyId);
		if(!CollectionUtils.isEmpty(roleList)){
			for(RoleDTO role:roleList){
				//获取所有的权限
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("companyId",companyId);
				map.put("roleId",role.getId());
				if(!StringUtil.isNullOrEmpty(typeId)){
					map.put("typeId",typeId);
				}
				List<PermissionDTO> permissionList = permissionDao.getPermissionByRole2(map);
				role.setPermissionList(permissionList);
			}
		}
		return roleList;
	}

	@Override
	public List<RoleDTO> getRolePermissionByType(String type) throws Exception {
		List<RoleDTO> roleList = this.roleDao.getRolePermissionByType(type);
		return roleList;
	}

	/**
	 * 方法描述：获取当前公司角色-权限-人员权限
	 * 作者：MaoSF
	 * 日期：2016/11/15
	 */
	@Override
	public List<RoleDTO> getRolePermissionByUser(String companyId, String userId) throws Exception {
		List<RoleDTO> roleList = this.getCompanyRoleDTO(companyId);
		//List<RoleDTO> newRoleList = new ArrayList<RoleDTO>();
		String typeId = this.companyService.getOrgTypeId(companyId);
		if(!CollectionUtils.isEmpty(roleList)){
			for(RoleDTO role:roleList){
				//获取userId的所有的权限
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("companyId",companyId);
				map.put("roleId",role.getId());
				map.put("userId",userId);
				if(!StringUtil.isNullOrEmpty(typeId)){
					map.put("typeId",typeId);
				}
				List<PermissionDTO> permissionList = permissionDao.getPermissionByRoleAndUser(map);
				if(!CollectionUtils.isEmpty(permissionList)){
					role.setPermissionList(permissionList);
					//newRoleList.add(role);
				}

			}
		}
		return roleList;
	}
	/**
	 * 方法描述：
	 * 作者：MaoSF
	 * 日期：2016/11/15
	 */
	@Override
	public List<RoleDTO> getRolePermissionByUserId(String companyId, String userId) throws Exception {
		List<RoleDTO> roleList = this.getCompanyRoleDTO(companyId);
		List<RoleDTO> newRoleList = new ArrayList<>();
		String typeId = this.companyService.getOrgTypeId(companyId);
		if(!CollectionUtils.isEmpty(roleList)){
			for(RoleDTO role:roleList){
				//获取userId的所有的权限
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("companyId",companyId);
				map.put("roleId",role.getId());
				map.put("userId",userId);
				if(!StringUtil.isNullOrEmpty(typeId)){
					map.put("typeId",typeId);
				}
				List<PermissionDTO> permissionList = permissionDao.getPermissionByRoleAndUserForApp(map);
				if(!CollectionUtils.isEmpty(permissionList)){
					role.setPermissionList(permissionList);
					newRoleList.add(role);
				}
			}
		}
		return newRoleList;
	}
}
