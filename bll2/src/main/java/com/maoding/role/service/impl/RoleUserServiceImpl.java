package com.maoding.role.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.StringUtil;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.DepartDao;
import com.maoding.org.dto.UserDepartDTO;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.role.dao.RoleDao;
import com.maoding.role.dao.RolePermissionDao;
import com.maoding.role.dao.RoleUserDao;
import com.maoding.role.dao.UserPermissionDao;
import com.maoding.role.dto.RoleUserDTO;
import com.maoding.role.dto.SaveRoleUserDTO;
import com.maoding.role.dto.UserOrgRoleDTO;
import com.maoding.role.entity.RoleEntity;
import com.maoding.role.entity.RolePermissionEntity;
import com.maoding.role.entity.RoleUserEntity;
import com.maoding.role.entity.UserPermissionEntity;
import com.maoding.role.service.RoleUserService;
import com.maoding.role.service.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：DepartServiceImpl
 * 类描述：团队（公司）ServiceImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午4:24:38
 */
@Service("roleUserService")
public class RoleUserServiceImpl extends GenericService<RoleUserEntity>  implements RoleUserService{
	
    @Autowired
    private RoleUserDao  roleUserDao;
    
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private CompanyDao companyDao;
    
    @Autowired
    private DepartDao departDao;

	@Autowired
	private RolePermissionDao rolePermissionDao;

	@Autowired
	private UserPermissionDao userPermissionDao;

    @Autowired
    private UserPermissionService userPermissionService;
    
	@Override
	public List<UserOrgRoleDTO> getOrgUserRoles(Map<String, Object> param) {
		
		String companyId = (String)param.get("companyId");
		String userId = (String)param.get("userId");
		
		List<UserOrgRoleDTO> returnData = new ArrayList<UserOrgRoleDTO>(); 
		CompanyEntity company = companyDao.selectById(companyId);
		
		//获取所在公司的权限
		List<RoleEntity> userRoles=roleDao.getUserRolesByOrgId(userId, companyId);
		UserOrgRoleDTO orgRole = new UserOrgRoleDTO();
		orgRole.setOrgId(companyId);
		orgRole.setOrgName(company.getCompanyShortName());
		orgRole.setRoleList(userRoles);
		returnData.add(orgRole);
		
		//获取user所在当前公司的独立部门
		List<UserDepartDTO> orgUserList = departDao.selectUserDpartByParam(param);
		for(int i=0 ;i<orgUserList.size();i++){
			orgRole = new UserOrgRoleDTO();
			orgRole.setOrgId(orgUserList.get(i).getDepartId());
			orgRole.setOrgName(orgUserList.get(i).getDepartName());
			
			 //获取所在部门的权限
			 userRoles=roleDao.getUserRolesByOrgId(userId, orgUserList.get(i).getDepartId());
			 orgRole.setRoleList(userRoles);
			 returnData.add(orgRole);
		}
		return returnData;
	}

	@Override
	public Object saveOrUserRoles(RoleUserDTO dto) {
		
		 //添加权限的用户id
		String userId=dto.getUserId();
		String companyId=dto.getCompanyId();
		String createBy=dto.getAccountId();
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("companyId", companyId);
		roleUserDao.deleteUserRole(map);//先删除权限
		
		for(int i=0;i<dto.getOrgRoles().size();i++){
			UserOrgRoleDTO orgRole = dto.getOrgRoles().get(i);
			List<RoleEntity> roles = orgRole.getRoleList();
			for(int j=0;j<roles.size();j++){
				RoleUserEntity roleUser =new RoleUserEntity();
				roleUser.setId(StringUtil.buildUUID());
				roleUser.setCompanyId(companyId);
				roleUser.setOrgId(orgRole.getId());
				roleUser.setRoleId(roles.get(j).getId());
				roleUser.setUserId(userId);
				roleUser.setCreateBy(createBy);
				roleUserDao.insert(roleUser);
			}
		}
		return dto;
	}

	public AjaxMessage saveOrgManager(SaveRoleUserDTO dto) throws Exception{
		String roleId = dto.getRoleId();
		String companyId = dto.getCurrentCompanyId();
		List<RolePermissionEntity> permissionList = rolePermissionDao.getPermissionByRole(roleId,companyId);//保存用户权限的关系（2016-11-10权限改动添加代码）
		if(!CollectionUtils.isEmpty(dto.getUserIds())){
			//1.删除原来所有的企业负责人的权限
			Map<String,Object> mapss = new HashMap<String,Object>();
			//删除权限
			for(RolePermissionEntity rolePermission:permissionList){
				mapss.put("companyId",dto.getCurrentCompanyId());
				mapss.put("permissionId",rolePermission.getPermissionId());
				this.userPermissionDao.deleteByPermissionId(mapss);
			}
			//先删除权限
			Map<String,Object> map = new HashMap<>();
			map.put("companyId",dto.getCurrentCompanyId());
			map.put("roleId",dto.getRoleId());
			this.roleUserDao.deleteUserRole(map);
			//保存被选择人的权限
			for(String userId:dto.getUserIds()){
				//添加
				RoleUserEntity roleUserEntity = new RoleUserEntity();
				String id = StringUtil.buildUUID();
				roleUserEntity.setId(id);
				roleUserEntity.setOrgId(companyId);
				roleUserEntity.setCompanyId(companyId);
				roleUserEntity.setUserId(userId);
				roleUserEntity.setRoleId(roleId);
				this.roleUserDao.insert(roleUserEntity);

				//此处可能保存重复的权限
				for(RolePermissionEntity rolePermission:permissionList) {
					//添加数据
					UserPermissionEntity userPermission = new UserPermissionEntity();
					userPermission.setPermissionId(rolePermission.getPermissionId());
					userPermission.setCompanyId(companyId);
					userPermission.setUserId(userId);
                    userPermissionService.saveUserPermission(userPermission);
				}
			}
		}
		return new AjaxMessage().setCode("0").setInfo("保存成功").setData(null);

	}

	/**
	 * 方法描述：保存用户权限
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午5:23:51
	 *
	 * @param dto
	 * @return
	 */
	@Override
	public AjaxMessage saveOrUserRole(SaveRoleUserDTO dto) throws Exception {

		//如果是企业负责人
		if(SystemParameters.ORG_MANAGER_ROLE_ID.equals(dto.getRoleId())){

			return this.saveOrgManager(dto);
		}
		if(!CollectionUtils.isEmpty(dto.getUserIds())){
			String roleId = dto.getRoleId();
			String companyId = dto.getCurrentCompanyId();
			List<RolePermissionEntity> permissionList = rolePermissionDao.getPermissionByRole(roleId,companyId);//保存用户权限的关系（2016-11-10权限改动添加代码）
			for(String userId:dto.getUserIds()){
				//先删除权限
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("companyId",companyId);
				map.put("roleId",roleId);
				map.put("userId",userId);
				this.roleUserDao.deleteUserRole(map);
				List<String> listStr = dto.getDeleteUserIds();
				//删除角色
				for(String str : listStr){
					Map<String,Object> mapss = new HashMap<>();
					mapss.put("companyId",companyId);
					mapss.put("roleId",roleId);
					mapss.put("userId",str);
                    this.roleUserDao.deleteUserRole(mapss);

					//删除权限
					for(RolePermissionEntity rolePermission:permissionList){
						mapss.put("permissionId",rolePermission.getPermissionId());
						this.userPermissionDao.deleteByPermissionId(mapss);
					}
				}

				//添加
				RoleUserEntity roleUserEntity = new RoleUserEntity();
				String id = StringUtil.buildUUID();
				roleUserEntity.setId(id);
				roleUserEntity.setOrgId(companyId);
				roleUserEntity.setCompanyId(companyId);
				roleUserEntity.setUserId(userId);
				roleUserEntity.setRoleId(roleId);
				this.roleUserDao.insert(roleUserEntity);

				//保存用户权限的关系（2016-11-10权限改动添加代码）
				//此处可能保存重复的权限
				for(RolePermissionEntity rolePermission:permissionList){

					//先删除已有数据
					List<String> permissionLists = new ArrayList<String>();
					permissionLists.add(rolePermission.getPermissionId());
					map.put("permissionList",permissionLists);
					userPermissionDao.deleteByUserIdAndPermission(map);
					for(String str : listStr){
						Map<String,Object> mapss = new HashMap<>();
						mapss.put("userId",str);
						mapss.put("permissionList",permissionLists);
                        userPermissionDao.deleteByUserIdAndPermission(mapss);
					}

					//添加数据
					UserPermissionEntity userPermission =new UserPermissionEntity();
					//userPermission.setId(StringUtil.buildUUID());
					userPermission.setPermissionId(rolePermission.getPermissionId());
					userPermission.setCompanyId(companyId);
					userPermission.setUserId(userId);
					userPermissionService.saveUserPermission(userPermission);
				}

				//-----------------end-----------------
			}
		}

		return new AjaxMessage().setCode("0").setInfo("保存成功").setData(null);
	}

	/**
	 * 方法描述：删除用户角色（根据userId，companyId，roleId）
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 *
	 * @param param
	 * @param:
	 * @return:
	 */
	@Override
	public AjaxMessage deleteRoleUser(Map<String,Object> param) throws Exception {
		this.roleUserDao.deleteUserRole(param);
		deletePermissionByRole(param); //（2016-11-10权限改动添加代码）
		return  new AjaxMessage().setCode("0").setInfo("删除成功").setData(null);
	}

	private void deletePermissionByRole(Map<String,Object> param) {
		//删除uer对应角色所拥有的权限（2016-11-10权限改动添加代码）
		String companyId = (String)param.get("companyId");
		String roleId = (String)param.get("roleId");
		String userId = (String)param.get("userId");

		List<RoleEntity> roleList = roleDao.getUserRolesByOrgId(userId,companyId);

		if(!CollectionUtils.isEmpty(roleList)){
			List<String> roleIds = new ArrayList<String>();
			for(RoleEntity roleEntity:roleList){
				roleIds.add(roleEntity.getId());
			}
			param.put("roleIds",roleIds);
		}
		List<String> permissionList = this.userPermissionDao.getPermissionByUserAndRole(param);
		if(!CollectionUtils.isEmpty(permissionList)){
			param.put("permissionList",permissionList);
			this.userPermissionDao.deleteByUserIdAndPermission(param);
		}

		//------------end------------------
	}


	/**
	 * 方法描述：初始化用户角色对应的权限 2011-11-11 （数据迁移使用）
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 *
	 * @param:
	 * @return:
	 */
	@Override
	public AjaxMessage initRoleUserPermission() throws Exception {
		List<RoleUserEntity> list = this.roleUserDao.selectAllRoleUser();
		if(!CollectionUtils.isEmpty(list)){
			for(RoleUserEntity roleUser:list){
				List<RolePermissionEntity> permissionList = rolePermissionDao.getPermissionByRole(roleUser.getRoleId(),roleUser.getCompanyId());//保存用户权限的关系（2016-11-11权限改动添加代码）
					//保存用户权限的关系（2016-11-11权限改动添加代码）
					//此处可能保存重复的权限
					for(RolePermissionEntity rolePermission:permissionList){
						UserPermissionEntity userPermission =new UserPermissionEntity();
						//userPermission.setId(StringUtil.buildUUID());
						userPermission.setPermissionId(rolePermission.getPermissionId());
						userPermission.setCompanyId(roleUser.getCompanyId());
						userPermission.setUserId(roleUser.getUserId());
						userPermissionService.saveUserPermission(userPermission);
					}
					//-----------------end-----------------
			}
		}
		return null;
	}

}
