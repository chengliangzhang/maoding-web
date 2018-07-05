package com.maoding.admin.controller;


import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.RoleConst;
import com.maoding.core.util.StringUtil;
import com.maoding.org.service.CompanyUserService;
import com.maoding.role.dto.*;
import com.maoding.role.service.*;
import com.maoding.system.service.SystemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("iAdmin/role")
public class AdminRoleController extends BaseController{
	
	@Autowired
	private RoleUserService roleUserService; 
    
	@Autowired
	private RoleService roleService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private RolePermissionService rolePermissionService;

	@Autowired
	private UserPermissionService userPermissionService;

	@Autowired
	private CompanyUserService companyUserService;

	/**
	 * 跳转到角色权限配置页面
	 * @param model
	 * @return
	 * @throws Exception
     */
	@RequestMapping("/roleAuthorization")
	public String roleAuthorizationPage(ModelMap model) throws Exception {
		systemService.getCurrUserInfoOfAdmin(model, this.getSession());
		return "admin/views/role/roleAuthorizationConfiguration";
	}
	/**
	 * 跳转到员工角色权限分配页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/rolePermissions")
	public String rolePermissionsPage(ModelMap model) throws Exception {
		systemService.getCurrUserInfoOfAdmin(model, this.getSession());
		return "admin/views/role/rolePermissionsDistribution";
	}

	/**
	 * 跳转到员工角色权限分配页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/rolePermissions/{userId}")
	public String rolePermissionsPage(ModelMap model, @PathVariable("userId") String userId) throws Exception {
		systemService.getCurrUserInfoOfWork(model, this.getSession());
		model.put("editUserId",userId);
		return "admin/views/role/rolePermissionsDistribution";
	}

	/**
	 * 跳转到员工角色权限分配页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/rolePermissionsConfig")
	public String rolePermissionsConfig(ModelMap model) throws Exception {
		systemService.getCurrUserInfoOfAdmin(model, this.getSession());
		return "views/admin/roleAuthorization";
	}

	@ModelAttribute
	public void before() throws Exception{
		this.currentUserId = getFromSession("userId",String.class);
		this.currentCompanyId = getFromSession("adminCompanyId",String.class);
		if(StringUtil.isNullOrEmpty(this.currentUserId) || StringUtil.isNullOrEmpty(this.currentCompanyId)){
			throw new LoginException();
		}
	}
	
	/**
	 * 方法描述：查询当前所选人在当前组织（包含独立部门）所有的权限(总览所在团队，部门（组织）下的所有权限)
	 * 作        者：MaoSF
	 * 日        期：2016年6月2日-下午6:10:08
	 * @param param（userId:必须传）
	 * @return
	 */
	@RequestMapping("/getOrgUserRoles")
	@ResponseBody
	public Object getOrgUserRoles(@RequestBody Map<String,Object> param) throws Exception{
		String   companyId=currentCompanyId;
		param.put("companyId", companyId);
		param.put("departType", "1");
		return this.ajaxResponseSuccess().setData(roleUserService.getOrgUserRoles(param));
	}
   
	/**方法描述：保存人员角色
	 * 作        者：TangY
	 * 日        期：2015年12月3日-下午3:20:20
	 * @param dto
	 * @return
	 */
	@RequestMapping("/saveUserRole")
	@ResponseBody
    @RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
	public Object saveUserRole(@RequestBody RoleUserDTO dto) throws Exception{
			 String   companyId=currentCompanyId;
			 //当前登录用户Id
			 String   userId=this.currentUserId;
			 dto.setCompanyId(companyId);
			 dto.setAccountId(userId);
			 roleUserService.saveOrUserRoles(dto);
		    return ajaxResponseSuccess().setData(dto);
	}
	
	/**方法描述：获取当前公司角色
	 * 作        者：TangY
	 * 日        期：2015年12月3日-下午3:20:20
	 * @return
	 */
	@RequestMapping(value = "/companyRole",method= RequestMethod.GET)
	@ResponseBody
	public Object getCompanyRole() throws Exception{
		String   companyId=currentCompanyId;
		return ajaxResponseSuccess().setData(roleService.getCompanyRole(companyId));
	}


	/**
	 * 方法描述：获取所有权限
	 * 作者：MaoSF
	 * 日期：2016/11/2
	 * @param:
	 * @return:
	 */
	@RequestMapping(value = "/allPermission",method= RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getAllPermission(){

		return ajaxResponseSuccess().setData(permissionService.getAllPermission());
	}

//	/**
//	 * 方法描述：根据角色获取所有权限
//	 * 作者：MaoSF
//	 * 日期：2016/11/2
//	 * @param:
//	 * @return:
//	 */
//	@RequestMapping(value = "/rolePermission/{roleId}",method= RequestMethod.GET)
//	@ResponseBody
//    @RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
//	public AjaxMessage getPermissionByRoleId(@PathVariable("roleId") String roleId){
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("roleId",roleId);
//		map.put("companyId",this.currentCompanyId);
//		return ajaxResponseSuccess().setData(permissionService.getPermissionByRole(map));
//	}

	/**
	 * 方法描述：保存角色权限
	 * 作者：MaoSF
	 * 日期：2016/11/2
	 * @param:
	 * @return:
	 */
	@RequestMapping(value = "/rolePermission/{roleId}",method= RequestMethod.POST)
	@ResponseBody
    @RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
	public AjaxMessage saveRolePermission(@RequestBody SaveRolePermissionDTO dto) throws Exception{
		dto.setCurrentCompanyId(this.currentCompanyId);
		return rolePermissionService.saveRolePermission(dto);
	}

	/**方法描述：添加自定义角色
	 * 作        者：TangY
	 * 日        期：2015年12月3日-下午3:20:20
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = {"/companyRole","/companyRole/{id}"},method= RequestMethod.POST)
	@ResponseBody
    @RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
	public Object saveRole(@RequestBody RoleDTO dto) throws Exception{
		String   companyId=currentCompanyId;
		//当前登录用户Id
		String   userId=this.currentUserId;
		dto.setCompanyId(companyId);
		dto.setAccountId(userId);
		roleService.saveRole(dto);
		return ajaxResponseSuccess().setData(dto);
	}

	/**方法描述：删除自定义角色
	 * 作        者：MaoSF
	 * 日        期：2015年12月3日-下午3:20:20
	 * @return
	 */
	@RequestMapping(value = {"/companyRole/{id}"},method= RequestMethod.DELETE)
	@ResponseBody
    @RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
	public Object deletRole(@PathVariable("id") String id) throws Exception{

		return roleService.deleteRole(id,this.currentCompanyId);
	}

	/**
	 * 方法描述：根据角色获取人员
	 * 作者：MaoSF
	 * 日期：2016/11/2
	 * @param:
	 * @return:
	 */
	@RequestMapping(value = "/companyUser/{roleId}",method= RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getCompanyUserByRoleId(@PathVariable("roleId") String roleId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("roleId",roleId);
		map.put("companyId",this.currentCompanyId);
		return ajaxResponseSuccess().setData(companyUserService.getCompanyUserByRoleId(map));
	}

	/**
	 * 方法描述：在角色中添加成员
	 * 作者：MaoSF
	 * 日期：2016/11/2
	 * @param:
	 * @return:
	 */
	@RequestMapping(value = "/roleUser",method= RequestMethod.POST)
	@ResponseBody
    @RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
	public AjaxMessage saveRoleUser(@RequestBody SaveRoleUserDTO dto) throws Exception{
		dto.setCurrentCompanyId(this.currentCompanyId);
		return roleUserService.saveOrUserRole(dto);
	}

	/**
	 * 方法描述：在角色中删除成员
	 * 作者：MaoSF
	 * 日期：2016/11/2
	 * @param:param（userId,roleId)
	 * @return:
	 */
	@RequestMapping(value = "/roleUser/{roleId}/{userId}",method= RequestMethod.DELETE)
	@ResponseBody
    @RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
	public AjaxMessage deleteRoleUser(@PathVariable("roleId") String roleId , @PathVariable("userId") String userId) throws Exception{
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyId",this.currentCompanyId);
		param.put("roleId",roleId);
		param.put("userId",userId);
		return roleUserService.deleteRoleUser(param);
	}

//	/**
//	 * 方法描述：根据用户获取所有权限
//	 * 作者：MaoSF
//	 * 日期：2016/11/2
//	 * @param:
//	 * @return:
//	 */
//	@RequestMapping(value = "/userPermission/{userId}",method= RequestMethod.GET)
//	@ResponseBody
//	public AjaxMessage getPermissionByUserId(@PathVariable("userId") String userId) throws Exception{
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("userId",userId);
//		map.put("companyId",this.currentCompanyId);
//		List<PermissionDTO> permissionList = permissionService.getPermissionByUserId(map);
//		CompanyUserEntity companyUser = companyUserService.getCompanyUserByUserIdAndCompanyId(userId,currentCompanyId);
//		List<RoleEntity> roleList= roleService.getRoleByUser(userId,this.currentCompanyId);
//		map.clear();
//		map.put("permissionList",permissionList);
//		map.put("companyUser",companyUser);
//		map.put("roleList",roleList);
//		return ajaxResponseSuccess().setData(map);
//	}


	/**
	 * 方法描述：获取公司角色-权限-人员总览
	 * 作者：MaoSF
	 * 日期：2016/11/2
	 * @param:
	 * @return:
	 */
	@RequestMapping(value = "/getRoleUserPermission",method= RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getRoleUserPermission() throws Exception{
		return ajaxResponseSuccess().setData(roleService.getRolePermissionUser(this.currentCompanyId));
	}

	/**
	 * 方法描述：保存用户权限(从权限中选择人员保存)
	 * 作者：MaoSF
	 * 日期：2016/11/16
	 * @param:
	 * @return:
	 */
	@RequestMapping(value = "/saveUserPermission/{permissionId}",method= RequestMethod.POST)
	@ResponseBody
    @RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
	public AjaxMessage saveUserPermission(@RequestBody SaveUserPermission2DTO dto) throws Exception{
		dto.setCurrentCompanyId(this.currentCompanyId);
		dto.setAccountId(this.currentUserId);
		return userPermissionService.saveUserPermission2(dto);
	}

	/**
	 * 方法描述：删除用户权限(从权限中删除人员)
	 * 作者：MaoSF
	 * 日期：2016/11/16
	 * @param:
	 * @return:
	 */
	@RequestMapping(value = "/deleteUserPermission/{permissionId}/{userId}",method= RequestMethod.GET)
	@ResponseBody
    @RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
	public AjaxMessage deleteUserPermission(@PathVariable("permissionId") String permissionId,@PathVariable("userId") String userId) throws Exception{
		//如果已经是最后一个成员，提示错误
//		List<PermissionDTO> oldPermissionList = permissionService.listPermissionByCompanyIdAndPermissionId(this.currentCompanyId,permissionId);
//		if ((oldPermissionList == null) || (oldPermissionList.size() <= 1)) return new AjaxMessage().setCode("1").setInfo("删除失败，不能把所有人员删除");

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId",userId);
		map.put("companyId",this.currentCompanyId);
		map.put("accountId",this.currentUserId);
		List<String> permissionList = new ArrayList<String>();
		permissionList.add(permissionId);
		map.put("permissionList",permissionList);
		return userPermissionService.deleteUserPermission2(map);
	}

	/**
	 * 方法描述：查询人员的权限（2016-11-17）新版本
	 * 作者：MaoSF
	 * 日期：2016/11/17
	 * @param:
	 * @return:
	 */
	@RequestMapping(value = "/getRolePermissionByUser/{userId}",method= RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getRolePermissionByUser(@PathVariable("userId") String userId) throws Exception{
		return ajaxResponseSuccess().setData(roleService.getRolePermissionByUser(this.currentCompanyId,userId));
	}


    /**
     * 方法描述：批量删除用户权限
     * 作者：MaoSF
     * 日期：2016/11/16
     * @param:
     * @return:
     */
    @RequestMapping(value = "/deleteUserPermission",method= RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
    public AjaxMessage deleteUserPermission(@RequestBody SaveUserPermissionDTO dto) throws Exception{
        dto.setCurrentCompanyId(this.currentCompanyId);
        return userPermissionService.deleteUserPermissionOfBatch(dto);
    }

	/**
	 * 方法描述：保存用户权限
	 * 作者：MaoSF
	 * 日期：2016/11/2
	 * @param:
	 * @return:
	 */
	@RequestMapping(value = "/userPermission/{userId}",method= RequestMethod.POST)
	@ResponseBody
    @RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
	public AjaxMessage saveUserPermission(@RequestBody SaveUserPermissionDTO dto) throws Exception{
		dto.setCurrentCompanyId(this.currentCompanyId);
		dto.setAccountId(this.currentUserId);
		return userPermissionService.saveUserPermission(dto);
	}

}
