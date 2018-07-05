package com.maoding.role.controller;


import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.RoleConst;
import com.maoding.core.util.BeanUtilsEx;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.org.dto.DepartDTO;
import com.maoding.org.dto.DepartRoleDTO;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.CompanyUserService;
import com.maoding.org.service.DepartService;
import com.maoding.org.service.TeamOperaterService;
import com.maoding.role.dto.*;
import com.maoding.role.service.*;
import com.maoding.system.service.SystemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("iWork/role")
public class RoleController extends BaseController{
	
	@Autowired
	private RoleUserService roleUserService; 
    
	@Autowired
	private RoleService roleService;

	@Autowired
	private DepartService departService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private UserPermissionService userPermissionService;

	@Autowired
	private RolePermissionService rolePermissionService;

	@Autowired
	private CompanyUserService companyUserService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private TeamOperaterService teamOperaterService;

	@ModelAttribute
	public void before(){
		this.currentUserId = this.getFromSession("userId",String.class);
		this.currentCompanyId =this.getFromSession("companyId",String.class);
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
		return "work/views/organization/rolePermissionsDistribution";
	}

	/**
	 * 跳转到员工角色权限分配页面(2.0)
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/permissionSettings")
	public String permissionSettings(ModelMap model) throws Exception {
		systemService.getCurrUserInfoOfWork(model, this.getSession());
		return "views/admin/backstageMana";
	}
	/**
	 * 跳转到后台－认证界面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/permissionSettings/{forwordType}")
	public String centerBy(RedirectAttributes model, @PathVariable String forwordType) throws Exception {
		String dataAction;
		switch (forwordType) {
			case "1":
				dataAction="permissionSettings";
				break;
			case "2":
				dataAction="enterpriseCertification";
				break;
			default:
				dataAction="permissionSettings";
				break;
		}
		model.addFlashAttribute("forwardType", dataAction);
		return "redirect:/iWork/role/permissionSettings";
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
	public AjaxMessage getOrgUserRoles(@RequestBody Map<String,Object> param) throws Exception{
		String   companyId = this.currentCompanyId;
		param.put("companyId", companyId);
		param.put("departType", "1");
		return this.ajaxResponseSuccess().setData(roleUserService.getOrgUserRoles(param));
	}
   

	/**
	 * 方法描述：在角色中添加成员
	 * 作者：MaoSF
	 * 日期：2016/11/2
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
	 */
	@RequestMapping(value = "/roleUser/{roleId}/{userId}",method= RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = {RoleConst.SYS_ROLE_PERMISSION})
	public AjaxMessage deleteRoleUser(@PathVariable("roleId") String roleId , @PathVariable("userId") String userId) throws Exception{
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyId",this.currentCompanyId);
		param.put("roleId",roleId);
		param.put("userId",userId);
		return roleUserService.deleteRoleUser(param);
	}

	
	/**方法描述：获取当前公司角色
	 * 作        者：TangY
	 * 日        期：2015年12月3日-下午3:20:20
	 * @return
	 */
	@RequestMapping("/companyRole")
	@ResponseBody
	public AjaxMessage getCompanyRole() throws Exception{
		String   companyId=this.currentCompanyId;
		return ajaxResponseSuccess().setData(roleService.getCompanyRole(companyId));
	}

	/**
	 * 方法描述：权限判断，是否含有此权限
	 * 作者：MaoSF
	 * 日期：2016/8/15
	 * @param:
	 * @return:
	 */
	@RequestMapping("/isHasRole")
	@ResponseBody
	public AjaxMessage isHasRole(@RequestBody Map<String,Object> roles){
		List roleList = (List)roles.get("roleList");
		boolean isRole = this.hasRoles(roleList);
		return ajaxResponseSuccess().setData(isRole);
	}


	/**
	 * 方法描述：权限判断，是否含有此权限
	 * 作者：MaoSF
	 * 日期：2016/8/15
	 * @param:
	 * @return:
	 */
	@RequestMapping("/isHasOrgRole")
	@ResponseBody
	public AjaxMessage isHasOrgRole(@RequestBody Map<String,Object> map){
		boolean isRole = false;
		String orgId = (String)map.get("orgId");
		List roleList = (List)map.get("roleList");
		if(this.currentCompanyId.equals(orgId))
		{
			isRole = this.hasRoles(roleList);
		}else {
			map.put("userId",this.currentUserId);
			map.put("companyId",this.currentCompanyId);
			List<DepartRoleDTO> list = departService.getDepartByRole(map);
			if(list!=null && list.size()>0){
				isRole=true;
			}
		}
		return ajaxResponseSuccess().setData(isRole);
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
	//==================================================admin====================================================================
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
	@RequestMapping("/rolePermissionsPageAdmin/{userId}")
	public String rolePermissionsPageAdmin(ModelMap model, @PathVariable("userId") String userId) throws Exception {
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
		return "admin/views/role/permissionsConfiguration";
	}


	/**
	 * 方法描述：查询当前所选人在当前组织（包含独立部门）所有的权限(总览所在团队，部门（组织）下的所有权限)
	 * 作        者：MaoSF
	 * 日        期：2016年6月2日-下午6:10:08
	 * @param param（userId:必须传）
	 * @return
	 */
	@RequestMapping("/getOrgUserRolesAdmin")
	@ResponseBody
	public Object getOrgUserRolesAdmin(@RequestBody Map<String,Object> param) throws Exception{
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
	@RequestMapping(value = "/getCompanyRoleAdmin",method= RequestMethod.GET)
	@ResponseBody
	public Object getCompanyRoleAdmin() throws Exception{
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

	/**
	 * 方法描述：保存角色权限
	 * 作者：MaoSF
	 * 日期：2016/11/2
	 * @param:
	 * @return:
	 */
	@RequestMapping(value = "/rolePermission/{roleId}",method= RequestMethod.POST)
	@ResponseBody
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
	@RequestMapping(value = {"/companyRole/{id}"},method= RequestMethod.GET)
	@ResponseBody
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
	@RequestMapping(value = "/saveRoleUserAdmin",method= RequestMethod.POST)
	@ResponseBody
	public AjaxMessage saveRoleUserAdmin(@RequestBody SaveRoleUserDTO dto) throws Exception{
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
	@RequestMapping(value = "/deleteRoleUserAdmin/{roleId}/{userId}",method= RequestMethod.GET)
	@ResponseBody
	public AjaxMessage deleteRoleUserAdmin(@PathVariable("roleId") String roleId , @PathVariable("userId") String userId) throws Exception{
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyId",this.currentCompanyId);
		param.put("roleId",roleId);
		param.put("userId",userId);
		return roleUserService.deleteRoleUser(param);
	}


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
		Map<String,Object> data = new HashMap<>();
		data.put("roleList",roleService.getRolePermissionUser(this.currentCompanyId));
		data.put("systemManager",teamOperaterService.getSystemManager(this.currentCompanyId));
		return ajaxResponseSuccess().setData(data);
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
	@RequestMapping(value = "/saveUserPermissionAdmin/{userId}",method= RequestMethod.POST)
	@ResponseBody
	public AjaxMessage saveUserPermissionAdmin(@RequestBody SaveUserPermissionDTO dto) throws Exception{
		dto.setCurrentCompanyId(this.currentCompanyId);
		dto.setAccountId(this.currentUserId);
		return userPermissionService.saveUserPermission(dto);
	}

	/**
	 * 方法描述：查询人员所在的公司,部门职位及权限
	 * 作    者 : ZhangChengliang
	 * 日    期 : 2017/6/22
	 */
	@RequestMapping(value = "/getCompanyDepartAndPermission",method= RequestMethod.POST)
	@ResponseBody
	public AjaxMessage getCompanyDepartAndPermission(@RequestBody PermissionQueryDTO query) throws Exception{
		String userId = query.getAccountId();
		//所在的所有公司
		List<CompanyDTO> orgList = companyService.getCompanyByUserId(userId);
		List<PermissionForOrgAndUserDTO> list = new ArrayList<>();
		for(CompanyDTO companyDTO : orgList){
			PermissionForOrgAndUserDTO dto = new PermissionForOrgAndUserDTO();
			dto.setCompanyName(companyDTO.getCompanyName());
			dto.setCompanyId(companyDTO.getId());
			dto.setCompanyShortName(companyDTO.getCompanyShortName());
			dto.setUserId(userId);
			Map<String,Object> map = new HashMap<>();
			BeanUtilsEx.copyProperties(dto,map);
			List<DepartDTO> departList =  departService.getDepartByUserIdContentCompany(map);
			List<RoleDTO> roleDtoList =   roleService.getRolePermissionByUserId(companyDTO.getId(),userId);
			dto.setDepartList(departList);
			dto.setRoleList(roleDtoList);
			list.add(dto);
		}

		return AjaxMessage.succeed(list);
	}

	/**
	 * 方法描述：不同组织类型的权限列表展示
	 * 作    者 : MaoSF
	 * 日    期 : 2017/6/22
	 */
	@RequestMapping(value = "/getRolePermissionByType",method= RequestMethod.POST)
	@ResponseBody
	public AjaxMessage getRolePermissionByType(@RequestBody PermissionQueryDTO query) throws Exception{
		return AjaxMessage.succeed(roleService.getRolePermissionByType(query.getType()));
	}

	/**
	 * 方法描述：财务设置板块的权限
	 * 作    者 : MaoSF
	 * 日    期 : 2017/6/22
	 */
	@RequestMapping(value = "/getPermissionOperator",method= RequestMethod.POST)
	@ResponseBody
	public AjaxMessage getPermissionOperator(@RequestBody Map<String,Object> map) throws Exception{
		map.put("companyId",this.currentCompanyId);
		map.put("accountId",this.currentUserId);
		return AjaxMessage.succeed(permissionService.getPermissionOperator(map));
	}

}
