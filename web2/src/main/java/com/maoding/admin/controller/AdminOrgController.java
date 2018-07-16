package com.maoding.admin.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.bean.Sms;
import com.maoding.core.constant.NetFileType;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.*;
import com.maoding.org.dto.*;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.entity.DepartEntity;
import com.maoding.org.entity.TeamOperaterEntity;
import com.maoding.org.service.*;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.role.entity.RoleEntity;
import com.maoding.role.service.RoleService;
import com.maoding.system.entity.DataDictionaryEntity;
import com.maoding.system.service.DataDictionaryService;
import com.maoding.system.service.SystemService;
import com.maoding.user.dto.ShareInvateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：OrgController
 * 类描述：组织controller
 * 作    者：MaoSF
 * 日    期：2016年7月8日-下午3:12:45
 */
@Controller
@RequestMapping("iAdmin/org")
public class AdminOrgController extends BaseController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyRelationService companyRelationService;

	@Autowired
	private CompanyRelationAuditService companyRelationAuditService;

	@Autowired
	private CompanyUserService companyUserService;


	@Autowired
	private TeamOperaterService teamOperaterService;

	@Autowired
	private DataDictionaryService dataDictionaryService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private CompanyUserAuditService companyUserAuditService;

	@Autowired
	private SmsSender smsSender;

	@Autowired
	private JedisUtils jedisUtils;

	@Autowired
	private RoleService roleService;

	private CompanyDTO currentCompanyDTO = null;

	@Autowired
	private DepartService departService;

	@Autowired
	private ProjectSkyDriverService projectSkyDriverService;

	@Value("${android.url}")
	protected String androidUrl;

	@Value("${ios.url}")
	protected String iosUrl;



	private final String th0="240X250"; //内部管理，log
	private final String th1="300X45"; //内部管理，log
	private final String th2="150X180";//公司团队log 最大尺寸150X180
	private final String th3="200X35";//精英团队图标logo尺寸：最大200 x 35

	@Value("${company}")
	private String companyUrl;

	@RequestMapping("/adminOrgRelation")
	public String adminBusHome(ModelMap model) throws Exception {
		//获取当前用户基本信息
		systemService.getCurrUserInfoOfAdmin(model,this.getSession());
		return "admin/views/organization/adminOrganizationRelation";
	}
	@RequestMapping("/adminOrgFramework")
	public String adminOrganizationFramework(ModelMap model) throws Exception {
		systemService.getCurrUserInfoOfAdmin(model, this.getSession());
		return "admin/views/organization/adminOrganizationFramework";
	}


	/**
	 * 分支机构邀请加盟
	 * @param model
	 * @return
	 * @throws Exception
     */
	@RequestMapping("/branchCompanyInvitation")
	public String branchCompanyInvitation(ModelMap model) throws Exception {
		systemService.getCurrUserInfoOfAdmin(model, this.getSession());
		return "admin/views/organization/branchCompanyInvitation";
	}
	/**
	 * 分支机构申请加盟
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/branchCompanyApplication")
	public String branchCompanyApplication(ModelMap model) throws Exception {
		systemService.getCurrUserInfoOfAdmin(model, this.getSession());
		return "admin/views/organization/branchCompanyApplication";
	}
	/**
	 * 事业合伙人邀请加盟
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/businessPartnerInvitation")
	public String businessPartnerInvitation(ModelMap model) throws Exception {
		systemService.getCurrUserInfoOfAdmin(model, this.getSession());
		return "admin/views/organization/businessPartnerInvitation";
	}
	/**
	 * 事业合伙人申请加盟
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/businessPartnerApplication")
	public String businessPartnerApplication(ModelMap model) throws Exception {
		systemService.getCurrUserInfoOfAdmin(model, this.getSession());
		return "admin/views/organization/businessPartnerApplication";
	}

	@ModelAttribute
	public void before() throws Exception{
		this.currentUserId = getFromSession("userId",String.class);
		this.currentCompanyId = getFromSession("companyId",String.class);
		this.currentCompanyDTO = this.getFromSession("company", CompanyDTO.class);
		if(StringUtil.isNullOrEmpty(this.currentUserId) || StringUtil.isNullOrEmpty(this.currentCompanyId)){
			throw new LoginException();
		}
	}
	
	/**
	 * 批量导入 界面
	 * @param companyId
	 * @param model
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = {"/bulkImport","/bulkImport/{companyId}"}, method = RequestMethod.GET)
	public String bulkImport(@PathVariable String companyId, ModelMap model) throws Exception {
		if(companyId==null || "".equals(companyId)){
			companyId = this.currentCompanyId;
		}
		systemService.getCurrUserInfoOfAdmin(model, this.getSession());
		CompanyDTO companyDTO = companyService.getCompanyById(companyId);
		model.addAttribute("companyDTO",companyDTO);
		model.addAttribute("companyId",companyId);
		return "admin/views/organization/uploadUserFileSuccess";
	}

	@RequestMapping("/adminRoleAssignments")
	public String adminRoleAssignments() {
		return "admin/views/organization/adminRoleAssignments";
	}
	//iAdmin/org/adminTeamInfo

    @RequestMapping("/adminTeamInfo")
	public String adminTeamInfo(ModelMap model) throws Exception {
		//当前组织信息 
//		CompanyDTO company = (CompanyDTO) this.getSession().getAttribute("company");
//		List<DataDictionaryEntity> serverTypeList = dataDictionaryService.getSubDataByCode("server-type");
//		model.addAttribute("companyInfo",company);
//		model.addAttribute("serverTypeList",serverTypeList);
//		model.addAttribute("fastdfsUrl",fastdfsUrl);

		systemService.getCurrUserInfoOfAdmin(model, this.getSession());
		return "views/teamInfo/adminTeamInfo";
	}

	/****************************公司信息***********************************/


	/**
	 * 方法描述：根据id查找公司信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午3:35:00
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/teamInfo","/teamInfo/{id}"}, method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getCompanyInfo(@PathVariable("id") String id) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		//当前组织信息 
		List<DataDictionaryEntity> serverTypeList = dataDictionaryService.getSubDataByCode("server-type");
		map.put("companyInfo",companyService.getCompanyById(this.currentCompanyId));
		map.put("serverTypeList",serverTypeList);
		map.put("fastdfsUrl",fastdfsUrl);
		return new AjaxMessage().setCode("0").setData(map);
	}

	/**
	 * 方法描述：根据id查找公司信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午3:35:00
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCompanyById/{id}", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getCompanyById(@PathVariable("id") String id) throws Exception{
		CompanyDTO dto = companyService.getCompanyById(id);
		return this.ajaxResponseSuccess(dto);
	}

	/**
	 * 方法描述：增加与修改公司信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午3:35:00
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/saveOrUpdateCompany","/saveOrUpdateCompany/{id}"}, method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage saveOrUpdateCompany(@RequestBody CompanyDTO dto) throws Exception{
		dto.setAccountId(this.currentUserId);
		AjaxMessage ajax = companyService.saveOrUpdateCompany(dto);
		if("0".equals(ajax.getCode())){
			this.getSession().setAttribute("adminCompany", ajax.getData());
		}
		return ajax;
	}

	/**
	 * 方法描述：获取当前用户所在的组织（组织切换）
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午3:14:31
	 * @return
	 */
	@RequestMapping(value = "/getCompanyByUserId", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getCompanyByUserId() throws Exception{
		String userId =this.currentUserId;
		List<CompanyDTO> list = companyService.getCompanyByUserId(userId);
		return this.ajaxResponseSuccess(list);
	}


/*******************************团队成员信息**************************************/
	/**
	 * 方法描述：添加修改（团队成员）
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:25:03
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/saveCompanyUser","/saveCompanyUser/{id}"}, method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage saveCompanyUser(@RequestBody CompanyUserTableDTO dto) throws Exception{
		String userId =this.currentUserId;
		CompanyDTO company =companyService.getCompanyById(this.currentCompanyId);
		dto.setCompanyName(company.getCompanyName());
		dto.setAccountId(userId);
		AjaxMessage ajaxMessage = companyUserService.saveCompanyUser(dto);
		//下面代码，把角色保存到redis中，若抛出异常，不做处理
		try{
			ListTranscoder<RoleEntity> listTranscoder = new ListTranscoder<>();
			List<RoleEntity> roleList =roleService.getRoleByUser(dto.getUserId(),dto.getCompanyId());
			//存入redis中
			String keyString = dto.getUserId()+"-"+dto.getCompanyId();
			byte[] key = keyString.getBytes();
			jedisUtils.delete(key);//先删除
			jedisUtils.setBytes(key, listTranscoder.serialize(roleList),0);
		}catch (Exception e){
			e.printStackTrace();
		}
		return ajaxMessage;
	}


	/**
	 * 方法描述：团队成员列表信息【组织界面-团队人员列表，人员选择列表】分页
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:25:03
	 * @param orgId
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOrgUser", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getOrgUser(String orgId,int pageSize,int pageIndex) throws Exception{
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgId", orgId);
		param.put("pageSize", pageSize);
		param.put("pageNumber", pageIndex);
		param.put("auditStatus", "1");
		if(null == param.get("orgId")){
			param.put("orgId", this.currentCompanyId);
		}
		List<CompanyUserTableDTO> data = companyUserService.getCompanyUserByOrgIdOfAdmin(param);
		int totalNumber = companyUserService.getCompanyUserByOrgIdCountOfAdmin(param);
		param.clear();
		param.put("data",data);
		param.put("total", totalNumber);
		return this.ajaxResponseSuccess().setData(param);
	}

	/**
	 * 方法描述：团队成员列表信息【组织界面-团队人员列表，人员选择列表】不分页
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:25:03
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOrgUserNoPage/{orgId}", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getOrgUserNoPage(@PathVariable String orgId) throws Exception{
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("orgId", orgId);
		/*param.put("pageSize", pageSize);
		param.put("pageNumber", pageIndex);*/
		param.put("auditStatus", "1");
		if(null == param.get("orgId")){
			param.put("orgId", this.currentCompanyId);
		}
		List<CompanyUserTableDTO> data = companyUserService.getCompanyUserByOrgIdOfAdmin(param);
		return this.ajaxResponseSuccess().setData(data);
	}

	/**
	 * 方法描述：离职，删除人员
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:25:03
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveCompanyUser/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public AjaxMessage quit(@PathVariable String id) throws Exception{
		return companyUserService.quit(id);
	}

	/**
	 * 方法描述：待审核成员列表信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:25:03
	 * @param orgId
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPendingAudiOrgUser", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getPendingAudiOrgUser(String orgId,int pageSize,int pageIndex) throws Exception{
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("companyId", orgId);
		param.put("pageSize", pageSize);
		param.put("pageNumber", pageIndex);
		if(StringUtil.isNullOrEmpty(orgId)){
			param.put("companyId", this.currentCompanyId);
		}
		Map returnData =companyUserAuditService.getCompanyUserAudit(param);
		return this.ajaxResponseSuccess().setData(returnData);
	}

	/**
	 * 方法描述：审核申请加入组织的人员【 同意或拒绝】(dto:id,auditStatus,companyId)
	 * 作        者：MaoSF
	 * 日        期：2016年3月18日-上午9:43:08
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/audiOrgUser/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Object audiOrgUser(@RequestBody  ShareInvateDTO dto)throws Exception{
		dto.setAccountId(this.currentUserId);
		return companyUserAuditService.auditShareInvate(dto);
	}

	/**
	 * 方法描述：批量导入
	 * 作        者：MaoSF
	 * 日        期：2016年7月14日-下午4:53:03
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/uploadUserFile")
	@ResponseBody
	public AjaxMessage uploadUserFile(MultipartFile file,String companyId)throws Exception{
		String userId =this.getFromSession("userId",String.class);
		List<List<Object>> list=new ArrayList<List<Object>>();
		String filename=file.getOriginalFilename();
		String sourceExtendName = filename.substring(filename.lastIndexOf('.') + 1);//原始文件的扩展名(不包含“.”)
		if(!(sourceExtendName.equals("xlsx") || sourceExtendName.equals("xls"))){
			return ajaxResponseError("文件上传仅支持以下类型：xlsx、xls");
		}
		if(StringUtil.isNullOrEmpty(companyId))
		{
			companyId =this.currentCompanyId;
		}

		list=ExcelUtils.readOneSheetFromExcel(file.getInputStream());
		List<ImportFileCompanyUserDTO> cList=new ArrayList<ImportFileCompanyUserDTO>();
		List<ImportFileCompanyUserDTO> errorList=new ArrayList<ImportFileCompanyUserDTO>();
		if(list !=null && list.size()>4){
			if(list.get(3).size()<8 || !"部门".equals(((String)list.get(3).get(0)).trim())
					|| !"姓名*".equals(((String)list.get(3).get(1)).trim())
					|| !"手机号码*".equals(((String)list.get(3).get(2)).trim())
					//|| !"性别".equals(((String)list.get(3).get(3)).trim())
					|| !"职位".equals(((String)list.get(3).get(3)).trim())
					|| !"办公电话".equals(((String)list.get(3).get(4)).trim())
					|| !"邮箱".equals(((String)list.get(3).get(5)).trim())
					|| !"入职时间".equals(((String)list.get(3).get(6)).trim())
					|| !"备注".equals(((String)list.get(3).get(7)).trim()))
			{
				return ajaxResponseError("模板错误");
			}
			Map<String,String> cellphoneMap = new HashMap<String,String>();
			for(int i=4;i<list.size();i++){
				if ("".equals(((String)list.get(i).get(0)).trim())
						&& "".equals(((String)list.get(i).get(1)).trim())
						&& "".equals(((String)list.get(i).get(2)).trim())) continue;
				String error="第"+(i+1)+"行";
				boolean isValidate=true;
				ImportFileCompanyUserDTO c=new ImportFileCompanyUserDTO();
				cellphoneMap.put((String)list.get(i).get(2), (i+1)+"");
				c.setDepartName(((String)list.get(i).get(0)).trim());
				c.setUserName(((String)list.get(i).get(1)).trim());
				c.setCellphone(((String)list.get(i).get(2)).trim());
				//c.setSex(((String)list.get(i).get(3)).trim());
				c.setServerStation(((String)list.get(i).get(3)).trim());
				c.setPhone(((String)list.get(i).get(4)).trim());
				c.setEmail(((String)list.get(i).get(5)).trim());
				if(null !=list.get(i).get(6) && !"".equals(list.get(i).get(6))){
					boolean dateFlag=true;
					Object d = list.get(i).get(6);
					int  checkFlag=0;
					if(!(d instanceof Date)) {
						dateFlag = DateUtils.isDate(d.toString());
						checkFlag=1;
					}
                    else{
                    	String date=DateUtils.date2Str(((Date)d),DateUtils.date_sdf);
						dateFlag = DateUtils.isDate(date);
						checkFlag=2;
					}
					if(dateFlag==false){
						if(checkFlag==1) {
							error += "入职时间:" + (String) d + "数据错误;";
						}
						else{
							error += "入职时间:" + (String) DateUtils.date2Str(((Date)d),DateUtils.date_sdf) + "数据错误;";
						}
						isValidate=false;
					}else{
						Date date=DateUtils.isValidDate2(d);
						c.setEntryTime(DateUtils.date2Str(date,DateUtils.date_sdf));
					}
				}
				c.setRemark(((String)list.get(i).get(7)).trim());
				c.setAccountId(userId);
				c.setCompanyId(companyId);
				if((String)list.get(i).get(1)==null || "".equals(((String)list.get(i).get(1)).trim())){
					error+="姓名不能为空;";
					isValidate=false;
				}
				if((String)list.get(i).get(2)==null || "".equals(((String)list.get(i).get(2)).trim())){
					error+="手机号码不能为空;";
					isValidate=false;
				}else if(!isMobileNO((String)list.get(i).get(2))){
					error+="手机号码："+(String)list.get(i).get(2)+"数据错误;";
					isValidate=false;
				}
				if(!StringUtil.isNullOrEmpty(c.getDepartName())){
					if(c.getDepartName().indexOf("//")>-1 || "/".equals(c.getDepartName().substring(0,1))
							|| "/".equals(c.getDepartName().substring(c.getDepartName().length()-1))){
						error+="部门："+(String)list.get(i).get(0)+"数据错误;";
						isValidate=false;
					}else{
						String[] departs = c.getDepartName().split("/");
						for(String departName: departs){
							if(StringUtil.isNullOrEmpty(departName)){
								error+="部门："+(String)list.get(i).get(0)+"数据错误;";
								isValidate=false;
							}
						}
					}
				}
				/*if(cellphoneMap.containsKey((String)list.get(i).get(2))){
					error+="手机号码："+(String)list.get(i).get(2)+"和"+cellphoneMap.get((String)list.get(i).get(2))+"行重复";
					isValidate=false;
				}*/
				if(isValidate){
					cList.add(c);
				}else{
					c.setRemark(error);
					errorList.add(c);
				}
			}
			//验证无错误后，
			if(cList.size()>0)
			{
				Map<String,Object> returnMap=new HashMap<String,Object>();
				Object map=companyUserService.addUserFile(cList);
				//添加到公司群中
				companyUserService.handleCompanyIm(cList);
				//创建没有创建的一级部门群
				handleDepartIm(companyId);
				//添加人员到一级部门群
				handleAddUserToDepartIm(companyId);
				int count=errorList.size();
				if(null !=((Map)map).get("exist")){
					count=count+((List)((Map)map).get("exist")).size();
					errorList.addAll((List)((Map)map).get("exist"));
				}
				returnMap.put("error", errorList);
				returnMap.put("errorSize", count);
				returnMap.put("successSize", (cList.size()-count));
				returnMap.put("count", cList.size());
				//处理添加一级部门群及添加用户到群组当中
			//	handleCreateOneLevelDepartGroupAndAddUser(companyId);
				if(count==0){
					return ajaxResponseSuccess("导入成功"+(cList.size()-count)+"条,失败"+count+"条");
				}
				return ajaxResponseSuccess("导入成功"+(cList.size()-count)+"条,失败"+count+"条").setData(returnMap);
			}else{
				Map<String,Object> returnMap=new HashMap<String,Object>();
				returnMap.put("error", errorList);
				returnMap.put("errorSize", errorList.size());
				returnMap.put("successSize", 0);
				returnMap.put("count", cList.size());
				return ajaxResponseSuccess("导入失败").setData(returnMap);
			}
		}

		return ajaxResponseError("模板错误");
	}

	private void handleAddUserToDepartIm(String companyId)throws Exception {
		CompanyEntity c = companyService.selectById(companyId);
		//查询创建群组的一级部门群
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyId", companyId);
		List<DepartEntity> list = departService.selectCreateGroupDepart(params);
		for (DepartEntity departEntity : list) {
			params.clear();
			params.put("departId", departEntity.getId());
			String groupId = null;
			//查询部门用户
			List<CompanyUserEntity> userList = companyUserService.getUserByDepartId(params);
			//查询所有子部门
			List<DepartEntity> listChild = departService.selectChildDepartEntity(departEntity.getId() + "-");
			for (DepartEntity de : listChild) {
				params.clear();
				params.put("departId", de.getId());
				List<CompanyUserEntity> newUserList = new ArrayList<>();
				newUserList = companyUserService.getUserByDepartId(params);
				for (CompanyUserEntity cue : newUserList) {
					userList.add(cue);
				}
			}
			if (userList.size() > 0) {
				String userId = userList.get(0).getUserId();
				groupId = companyUserService.selectOneLevelDepartGroupId(departEntity.getId(), departEntity.getDepartName(), userId);
			}
			if (null != groupId && userList.size() > 1) {
				for (int j = 1; j < userList.size(); j++) {
					System.out.println("userList.get(j).getUserId()-->" + userList.get(j).getUserId() + "===" + groupId);
					companyUserService.addUserToGroup(userList.get(j).getUserId(), groupId, departEntity.getId());
				}

			}
		}
	}

	private void handleDepartIm(String companyId)throws Exception {


		CompanyEntity c = companyService.selectById(companyId);
		//查询没有创建群组的一级部门群
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyId",companyId);
		List<DepartEntity> list = departService.selectNotCreateGroupDepart(params);
		for(DepartEntity departEntity : list){
			params.clear();
			params.put("departId",departEntity.getId());
			String groupId = null;
			//查询部门用户
			List<CompanyUserEntity> userList =companyUserService.getUserByDepartId(params);
			//查询所有子部门
			List<DepartEntity> listChild =  departService.selectChildDepartEntity(departEntity.getId()+"-");
			for(DepartEntity de : listChild){
				params.clear();
				params.put("departId",de.getId());
				List<CompanyUserEntity> newUserList = new ArrayList<>();
				newUserList=companyUserService.getUserByDepartId(params);
				for(CompanyUserEntity cue : newUserList){
					userList.add(cue);
				}
			}
			if(userList.size()>0){
				String userId = userList.get(0).getUserId();
				groupId = companyUserService.addOneLevelDepartGroup(departEntity.getId(),departEntity.getDepartName(),userId);
			}
		}



	}


	/**
	 * 方法描述：处理添加一级部门群及添加用户到群组当中
	 * 作        者：ChenZJ
	 * 日        期：2016年10月18日-下午8:34:56
	 * @return
	 */
	private void handleCreateOneLevelDepartGroupAndAddUser(String companyId)throws Exception {
		//查询没有创建群组的一级部门群
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyId",companyId);
		List<DepartEntity> list = departService.selectNotCreateGroupDepart(params);
		for(DepartEntity departEntity : list){
			params.clear();
			params.put("departId",departEntity.getId());
			String groupId = null;
			//查询部门用户
			List<CompanyUserEntity> userList =companyUserService.getUserByDepartId(params);
			//查询所有子部门
			List<DepartEntity> listChild =  departService.selectChildDepartEntity(departEntity.getId()+"-");
			for(DepartEntity de : listChild){
				params.clear();
				params.put("departId",de.getId());
				List<CompanyUserEntity> newUserList = new ArrayList<>();
				newUserList=companyUserService.getUserByDepartId(params);
				for(CompanyUserEntity cue : newUserList){
					userList.add(cue);
				}
			}
			if(userList.size()>0){
				String userId = userList.get(0).getUserId();
				System.out.println("departEntity.getId()-->"+departEntity.getId()+"==="+departEntity.getDepartName()+"====userId==>"+userId);
				groupId = companyUserService.addOneLevelDepartGroup(departEntity.getId(),departEntity.getDepartName(),userId);
			}
			if(null != groupId && userList.size()>1){
				for(int j = 1 ; j < userList.size();j++){
					System.out.println("userList.get(j).getUserId()-->"+userList.get(j).getUserId()+"==="+groupId);
					companyUserService.addUserToGroup(userList.get(j).getUserId(),groupId,departEntity.getId());
				}

			}
		}
		//处理已经创建的一级部门群
		handleOneLevelDepartGroupAndAddUser(companyId);


	}

	private void handleOneLevelDepartGroupAndAddUser(String companyId)throws Exception {
		//查询创建群组的一级部门群
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyId",companyId);
		List<DepartEntity> list = departService.selectCreateGroupDepart(params);
		for(DepartEntity departEntity : list){
			params.clear();
			params.put("departId",departEntity.getId());
			String groupId = null;
			//查询部门用户
			List<CompanyUserEntity> userList =companyUserService.getUserByDepartId(params);
			//查询所有子部门
			List<DepartEntity> listChild =  departService.selectChildDepartEntity(departEntity.getId()+"-");
			for(DepartEntity de : listChild){
				params.clear();
				params.put("departId",de.getId());
				List<CompanyUserEntity> newUserList = new ArrayList<>();
				newUserList=companyUserService.getUserByDepartId(params);
				for(CompanyUserEntity cue : newUserList){
					userList.add(cue);
				}
			}
			if(userList.size()>0){
				String userId = userList.get(0).getUserId();
				groupId = companyUserService.selectOneLevelDepartGroupId(departEntity.getId(),departEntity.getDepartName(),userId);
			}
			if(null != groupId && userList.size()>1){
				for(int j = 1 ; j < userList.size();j++){
					System.out.println("userList.get(j).getUserId()-->"+userList.get(j).getUserId()+"==="+groupId);
					companyUserService.addUserToGroup(userList.get(j).getUserId(),groupId,departEntity.getId());
				}

			}
		}
	}

	/**
	 * 方法描述：手机号码验证
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午8:34:56
	 * @param mobiles
	 * @return
	 */
	public  boolean isMobileNO(String mobiles){

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();

	}

/*******************************部门信息*******************************************/
	/**
	 * 方法描述：增加，修改部门
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-上午11:57:10
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/saveOrUpdateDepart","/saveOrUpdateDepart/{id}"}, method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage saveOrUpdateDepart(@RequestBody DepartDTO dto) throws Exception{
		String userId =this.getFromSession("userId",String.class);
		dto.setAccountId(userId);
		return departService.saveOrUpdateDepart(dto);
	}

	/**
	 * 方法描述：获取当前公司的所有部门【用到的界面：添加、修改人员，添加、修改部门】
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午12:00:13
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ={"/getDepartByCompanyId","/getDepartByCompanyId/{companyId}"} , method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getDepartByCompanyId(@PathVariable("companyId") String companyId) throws Exception{
		if(StringUtil.isNullOrEmpty(companyId))
		{
			companyId = this.currentCompanyId;
		}
		CompanyDTO company = companyService.getCompanyById(companyId);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyId", companyId);
		param.put("type", "0");
		List<DepartDTO> list = departService.getDepartByCompanyId(param,null);
		DepartDTO dto = new DepartDTO();
		dto.setId(companyId);
		dto.setDepartName(company.getCompanyName());
		list.add(0, dto);
		return this.ajaxResponseSuccess().setData(list);
	}


	/**
	 * 方法描述：删除部门
	 * 作        者：MaoSF
	 * 日        期：2016年7月12日-上午6:08:52
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/depart/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public AjaxMessage deleteDepart(@PathVariable("id") String id) throws Exception{
		return departService.deleteDepartById(id,this.getFromSession("userId",String.class));
	}


	/***********************添加修改分支机构*************************/
	/**
	 * 方法描述：添加修改分支机构
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-上午11:57:10
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/subCompany","/subCompany/{id}"}, method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage createSubCompany(@RequestBody SubCompanyDTO dto) throws Exception{
		if(StringUtil.isNullOrEmpty(dto.getId())){
			String companyId=this.currentCompanyId;
			dto.setCompanyId(companyId);
			dto.setAccountId(this.currentUserId);
			dto.setClearlyAdminPassword(dto.getAdminPassword());
			dto.setAdminPassword(MD5Helper.getMD5For32(dto.getAdminPassword()));
			return companyService.createSubCompany(dto);
		}else{
			return companyService.updateSubCompany(dto);
		}

	}

	/**
	 * 方法描述：删除分支机构
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-上午11:57:10
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/subCompany/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public AjaxMessage deleteSubCompany(@PathVariable("id") String id) throws Exception{
		String companyId=this.currentCompanyId;
		return companyService.deleteSubCompany(companyId,id);
	}



	/***********************创建事业合伙人*************************/
	/**
	 * 方法描述：创建事业合伙人
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-上午11:57:10
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/businessPartner","/businessPartner/{id}"}, method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage createBusinessPartner(@RequestBody BusinessPartnerDTO dto) throws Exception{
		if(StringUtil.isNullOrEmpty(dto.getId())) {
			String companyId = this.currentCompanyId;
			dto.setCompanyId(companyId);
			dto.setAccountId(this.getFromSession("userId", String.class));
			dto.setClearlyAdminPassword(dto.getAdminPassword());
			dto.setAdminPassword(MD5Helper.getMD5For32(dto.getAdminPassword()));
			return companyService.createBusinessPartner(dto);
		}else{
			return companyService.updateBusinessPartner(dto);
		}
	}


	/**
	 * 方法描述：删除事业合伙人
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-上午11:57:10
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/businessPartner/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public AjaxMessage deleteBusinessPartner(@PathVariable("id") String id) throws Exception{
		String companyId=this.currentCompanyId;
		return companyService.deleteSubCompany(companyId,id);
	}
	/***************组织架构树***************/
	/**
	 * 方法描述：组织架构树(数据）
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-上午11:58:59
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOrgTree", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage getOrgTree() throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("companyId", currentCompanyId);
		map.put("type", "2");//查询以当前公司为root的所有公司，部门信息
		OrgTreeDTO dataMap = companyService.getOrgTree(map);
		return this.ajaxResponseSuccess("操作成功").setData(dataMap);
	}



	/**
	 * 方法描述：组织架构树(数据）--搜索界面
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-上午11:58:59
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOrgTreeForSearch", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getOrgTreeForSearch() throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("companyId", this.currentCompanyId);
		map.put("type", "1");//只查询当前公司
		OrgTreeDTO dataMap = companyService.getOrgTree(map);
		return this.ajaxResponseSuccess("操作成功").setData(dataMap);
	}


	/********************移交管理员******************/

	/**
	 * 方法描述：移交管理员
	 * 作        者：MaoSF
	 * 日        期：2016年3月18日-上午9:43:08
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/transferSys", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage transferSys(@RequestBody TeamOperaterDTO dto) throws Exception{
		String newAdminPassword = dto.getNewAdminPassword();
		dto.setAccountId(this.getFromSession("userId", String.class));
		dto.setCompanyId(this.currentCompanyId);
		dto.setNewAdminPassword(MD5Helper.getMD5For32(dto.getNewAdminPassword()));
		if("1".equals(dto.getType())){
			return teamOperaterService.transferSys(dto,newAdminPassword);
		}
		if("2".equals(dto.getType())){
			return teamOperaterService.transferOrgManager(dto,newAdminPassword);
		}
		return AjaxMessage.failed("操作失败");

	}

	/******************事业合伙人*********************/

	/**
	 * 方法描述：查询正在审核和审核通过的合作伙伴
	 * 作        者：MaoSF
	 * 日        期：2016年6月8日-上午11:07:13
	 * @param map(type：2，分支机构：3，合作伙伴为)
	 * @return
	 */
	@RequestMapping(value = {"/selectInvitedPartner", "/selectInvitedPartner/{type}"} ,method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage selectInvitedPartner(@RequestBody Map<String, Object>map)throws Exception{
		//Map<String,Object> map = new HashMap<String,Object>();
		//map.put("companyStatus", companyStatus);
		map.put("cid", this.currentCompanyId);
		map.put("isAudit","1");//表示只查询待审核的且只是自己审核的数据
		map.put("auditStatus", 2);
        List<CompanyRelationAuditDTO> companyRelationAuditList = companyRelationAuditService.getCompanyRelationAuditByParam(map);
        map.clear();
        map.put("toAuditCompanyList",companyRelationAuditList);
        map.put("toAuditCompanyCount",this.selToAuditCompanyCount());
		return this.ajaxResponseSuccess("操作成功").setData(map);
	}
	/**
	 * 方法描述：查询正在审核和审核通过的合作伙伴
	 * 作        者：MaoSF
	 * 日        期：2016年6月8日-上午11:07:13
	 * @param {type 2，分支机构：3，合作伙伴为}
	 * @return
	 */
	@RequestMapping(value = "/getToAuditCompanyCount", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getToAuditCompanyCount() throws Exception{
        int count = this.selToAuditCompanyCount();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("companyId", this.currentCompanyId);
		int empCount = companyUserAuditService.getCompanyUserNumAudit(map);
		map.clear();
		map.put("toAuditNum",count);
		map.put("toAuditEmpNum",empCount);
		return this.ajaxResponseSuccess("查询成功").setData(map);
	}

	/**
	 * 待审核团队 数量统计
	 * @return
	 * @throws Exception
     */
	private int selToAuditCompanyCount()throws Exception{
        int count = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("cid", this.currentCompanyId);
        map.put("isAudit","1");//表示只查询待审核的且只是自己审核的数据
        map.put("auditStatus", 2);
        map.put("type",2);
        count = companyRelationAuditService.getCompanyRelationAuditByParamCount(map);
        map.clear();
        map.put("cid", this.currentCompanyId);
        map.put("isAudit","1");//表示只查询待审核的且只是自己审核的数据
        map.put("auditStatus", 2);
        map.put("type",3);
        count += companyRelationAuditService.getCompanyRelationAuditByParamCount(map);
        return count;
    }
	private int selToAuditEmpCount()throws Exception{
		int count = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("cid", this.currentCompanyId);
		map.put("isAudit","1");//表示只查询待审核的且只是自己审核的数据
		map.put("auditStatus", 2);
		map.put("type",2);
		count = companyRelationAuditService.getCompanyRelationAuditByParamCount(map);
		map.clear();
		map.put("cid", this.currentCompanyId);
		map.put("isAudit","1");//表示只查询待审核的且只是自己审核的数据
		map.put("auditStatus", 2);
		map.put("type",3);
		count += companyRelationAuditService.getCompanyRelationAuditByParamCount(map);
		return count;
	}

	/**
	 * 方法描述：搜索未挂靠的公司（大B搜索小b，小b搜索大B）
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午10:59:23
	 * @param (keyword)
	 */
	@RequestMapping(value = "/getFilterCompany", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getFilterCompany(String keyword,int pageSize,int pageIndex)throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("keyword", keyword);
		map.put("pageSize", pageSize);
		map.put("pageNumber", pageIndex);
		map.put("orgId", this.currentCompanyId);
		return this.ajaxResponseSuccess("操作成功").setData(companyService.getCompanyFilterbyParamPage(map));
	}

	/**
	 * 方法描述：组织与组织之间的关系的操作（companyStatus:取消，同意，拒绝，解除）
	 * 作        者：MaoSF
	 * 日        期：2016年2月26日-下午4:04:43
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/processingApplicationOrInvitation" , method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage processingApplicationOrInvitation(@RequestBody CompanyRelationAuditDTO dto)throws Exception{
		return companyRelationAuditService.processingApplicationOrInvitation(dto);
	}


	/**
	 * 方法描述：申请成为分支机构,合作伙伴(当前公司的id为orgId，选择公司的id为orgPid）
	 * 邀请分支机构,合作伙伴(当前公司的id为orgPid，选择公司的id为orgId）
	 * 作        者：MaoSF
	 * 日        期：2016年3月21日-上午10:38:08
	 * @param id
	 * @param type（1：申请成为分支机构，2：邀请分支机构，3：申请成为合作伙伴，4：邀请合作伙伴）
	 * @return
	 */
	@RequestMapping(value = "/applicationOrInvitationCompany/{id}/{type}", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage applicationOrInvitationCompany(@PathVariable("id") String id,@PathVariable("type") int type)throws Exception{
		String userId=getFromSession("userId", String.class);
		String companyId=this.currentCompanyId;
		CompanyRelationAuditDTO dto = new CompanyRelationAuditDTO();
		//申请成为分支机构
		if(type ==1){
			dto.setAccountId(userId);
			dto.setOrgId(companyId);
			dto.setOrgPid(id);
			dto.setType("2");
			dto.setRelationType("0");
		}
		//邀请分支机构
		if(type ==2){
			dto.setAccountId(userId);
			dto.setOrgId(id);
			dto.setOrgPid(companyId);
			dto.setType("2");
			dto.setRelationType("1");
		}
		//申请成为合作伙伴
		if(type ==3){
			dto.setAccountId(userId);
			dto.setOrgId(companyId);
			dto.setOrgPid(id);
			dto.setType("3");
			dto.setRelationType("0");
		}
		//邀请合作伙伴
		if(type ==4){
			dto.setAccountId(userId);
			dto.setOrgId(id);
			dto.setOrgPid(companyId);
			dto.setType("3");
			dto.setRelationType("1");
		}

		return companyRelationAuditService.applicationOrInvitation(dto);
	}

	/**
	 * 方法描述：申请成为分支机构,合作伙伴(当前公司的id为orgId，选择公司的id为orgPid）
	 * 邀请分支机构,合作伙伴(当前公司的id为orgPid，选择公司的id为orgId）
	 * 作        者：MaoSF
	 * 日        期：2016年3月21日-上午10:38:08
	 * @param id
	 * @param type（1：申请成为分支机构，2：邀请分支机构，3：申请成为合作伙伴，4：邀请合作伙伴）
	 * @return
	 */
	@RequestMapping(value = "/applicationSubCompany/{id}/{type}", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage applicationSubCompany(@PathVariable("id") String id,@PathVariable("type") int type)throws Exception{
		String userId=getFromSession("userId", String.class);
		String companyId=this.currentCompanyId;
		CompanyRelationAuditDTO dto = new CompanyRelationAuditDTO();
		//邀请分支机构
		dto.setAccountId(userId);
		dto.setOrgId(companyId);
		dto.setOrgPid(id);
		dto.setType("2");
		dto.setRelationType("0");
		return companyRelationAuditService.applicationOrInvitation(dto);
	}

	/**
	 * 方法描述：申请成为分支机构,合作伙伴(当前公司的id为orgId，选择公司的id为orgPid）
	 * 邀请分支机构,合作伙伴(当前公司的id为orgPid，选择公司的id为orgId）
	 * 作        者：MaoSF
	 * 日        期：2016年3月21日-上午10:38:08
	 * @param id
	 * @param type（1：申请成为分支机构，2：邀请分支机构，3：申请成为合作伙伴，4：邀请合作伙伴）
	 * @return
	 */
	@RequestMapping(value = "/invateSubCompany/{id}/{type}", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage invateSubCompany(@PathVariable("id") String id,@PathVariable("type") int type)throws Exception{
		String userId=getFromSession("userId", String.class);
		String companyId=this.currentCompanyId;
		CompanyRelationAuditDTO dto = new CompanyRelationAuditDTO();
		//邀请分支机构
		dto.setAccountId(userId);
		dto.setOrgId(id);
		dto.setOrgPid(companyId);
		dto.setType("2");
		dto.setRelationType("1");
		return companyRelationAuditService.applicationOrInvitation(dto);
	}

	/**
	 * 方法描述：申请成为分支机构,合作伙伴(当前公司的id为orgId，选择公司的id为orgPid）
	 * 邀请分支机构,合作伙伴(当前公司的id为orgPid，选择公司的id为orgId）
	 * 作        者：MaoSF
	 * 日        期：2016年3月21日-上午10:38:08
	 * @param id
	 * @param type（1：申请成为分支机构，2：邀请分支机构，3：申请成为合作伙伴，4：邀请合作伙伴）
	 * @return
	 */
	@RequestMapping(value = "/applicationBusinessPartner/{id}/{type}", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage applicationBusinessPartner(@PathVariable("id") String id,@PathVariable("type") int type)throws Exception{
		String userId=getFromSession("userId", String.class);
		String companyId=this.currentCompanyId;
		CompanyRelationAuditDTO dto = new CompanyRelationAuditDTO();
		//申请成为合作伙伴
		dto.setAccountId(userId);
		dto.setOrgId(companyId);
		dto.setOrgPid(id);
		dto.setType("3");
		dto.setRelationType("0");

		return companyRelationAuditService.applicationOrInvitation(dto);
	}

	/**
	 * 方法描述：申请成为分支机构,合作伙伴(当前公司的id为orgId，选择公司的id为orgPid）
	 * 邀请分支机构,合作伙伴(当前公司的id为orgPid，选择公司的id为orgId）
	 * 作        者：MaoSF
	 * 日        期：2016年3月21日-上午10:38:08
	 * @param id
	 * @param type（1：申请成为分支机构，2：邀请分支机构，3：申请成为合作伙伴，4：邀请合作伙伴）
	 * @return
	 */
	@RequestMapping(value = "/invateBusinessPartner/{id}/{type}", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage invateBusinessPartner(@PathVariable("id") String id,@PathVariable("type") int type)throws Exception{
		String userId=getFromSession("userId", String.class);
		String companyId=this.currentCompanyId;
		CompanyRelationAuditDTO dto = new CompanyRelationAuditDTO();
		//邀请合作伙伴
		dto.setAccountId(userId);
		dto.setOrgId(id);
		dto.setOrgPid(companyId);
		dto.setType("3");
		dto.setRelationType("1");

		return companyRelationAuditService.applicationOrInvitation(dto);
	}


	/**
	 * 方法描述：修改管理员密码
	 * 作        者：TangY
	 * 日        期：2016年7月12日-上午4:53:31
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changAdminPassword", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage changAdminPassword(@RequestBody Map<String,Object> paraMap)throws Exception{
		String userId=this.currentUserId;
		String companyId=this.currentCompanyId;
		TeamOperaterEntity teamEntity= teamOperaterService.getTeamOperaterByParam(companyId, userId);
		if(paraMap.get("newPassword").equals(paraMap.get("oldPassword"))){
			return ajaxResponseError("新密码不能和旧密码一致");
		}
		if(!teamEntity.getAdminPassword().equals(MD5Helper.getMD5For32(paraMap.get("oldPassword").toString()))){
			return ajaxResponseError("旧密码错误");
		}
		teamEntity.setAdminPassword(MD5Helper.getMD5For32(paraMap.get("newPassword").toString()));
		teamOperaterService.updateById(teamEntity);
		return ajaxResponseSuccess("密码修改成功！");
	}


	/**
	 * 方法描述：本组织的 部门数，企业人数，未激活人数，合作伙伴数
	 * 作        者：MaoSF
	 * 日        期：2016年7月15日-上午10:49:50
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCompanyAllCount", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getCompanyAllCount() throws Exception{
		Map<String,Object> param = new HashMap<String,Object>();
		String companyId = this.currentCompanyId;
		//查询当前组织部门数
		param.put("companyId", companyId);
		int departCount = departService.getDepartByCompanyIdCount(companyId);
		//查询当前 公司未激活人数
		int companyUserNotActiveCount = companyUserService.getCompanyUserOfNotActiveCount(param);
		//查询当前组织人数
		param.clear();
		param.put("orgId", companyId);
		param.put("auditStatus", "1");
		int companyUserCount = companyUserService.getCompanyUserByOrgIdCount(param);
		//事业合伙人个数
		param.clear();
		param.put("orgPid", companyId);
		param.put("companyStatus", "0");
		param.put("orgType","3");
		int companyPartnerCount = companyRelationService.getCompanyRelationByParamCount(param);
		//查询分支机构个数
		param.clear();
		param.put("orgPid", companyId);
		param.put("companyStatus", "0");
		param.put("orgType","2");
		int subcompanyCount = companyRelationService.getCompanyRelationByParamCount(param);

		param.clear();
		param.put("departCount", departCount);
		param.put("companyUserNotActiveCount", companyUserNotActiveCount);
		param.put("companyUserCount", companyUserCount);
		param.put("companyPartnerCount", companyPartnerCount);
		param.put("subcompanyCount", subcompanyCount);
		return this.ajaxResponseSuccess().setData(param);
	}

	/**
	 * 方法描述：未激活人数列表
	 * 作        者：MaoSF
	 * 日        期：2016年7月15日-下午12:03:49
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCompanyUserNotActive", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage getCompanyUserNotActive(int pageSize,int pageIndex) throws Exception{
		Map<String,Object> param = new HashMap<String,Object>();
		String companyId = this.currentCompanyId;
		//查询当前组织部门数
		param.put("companyId", companyId);
		param.put("pageSize", pageSize);
		param.put("pageNumber", pageIndex);
		//查询当前 公司未激活人数
		int companyUserNotActiveCount = companyUserService.getCompanyUserOfNotActiveCount(param);
		List<CompanyUserTableDTO> data = companyUserService.getCompanyUserOfNotActive(param);
		param.clear();
		param.put("data",data);
		param.put("total", companyUserNotActiveCount);
		return this.ajaxResponseSuccess().setData(param);
	}


	/**
	 * 方法描述：未激活人列表，【发送消息】,单个发送
	 * 作        者：MaoSF
	 * 日        期：2016年7月15日-下午1:37:35
	 * @param cellphone
	 * @return
	 */
	@RequestMapping(value = "/msgSend/{cellphone}", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage msgSend(@PathVariable("cellphone") String cellphone) throws Exception{
		//验证手机号是否注册，在账号输入框失去焦点时验证，在此不做验证
		String accountName="";
		CompanyUserEntity userEntity = companyUserService.getCompanyUserByUserIdAndCompanyId(this.currentUserId,this.currentCompanyId);
		if(userEntity!=null){
			accountName = userEntity.getUserName();
		}
		CompanyDTO company = currentCompanyDTO;
		if(StringUtil.isNumeric(cellphone)&&cellphone.length()==11){
			Sms sms=new Sms();
			sms.addMobile(cellphone);
			sms.setMsg(StringUtil.format(SystemParameters.ADD_COMPANY_USER_MSG_1, accountName,company.getCompanyName(),this.serverUrl));
			log.debug("短信发送结果::{}",smsSender.send(sms));
			return this.ajaxResponseSuccess("成功");
		}else{
			return ajaxResponseError("发送失败");
		}
	}


	/**
	 * 方法描述：未激活人列表，【发送消息】,单个发送
	 * 作        者：MaoSF
	 * 日        期：2016年7月15日-下午1:37:35
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/smsGroupSends", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage smsGroupSends() throws Exception{

		String accountName="";
		CompanyUserEntity userEntity = companyUserService.getCompanyUserByUserIdAndCompanyId(this.currentUserId,this.currentCompanyId);
		if(userEntity!=null){
			accountName = userEntity.getUserName();
		}

		CompanyDTO company = currentCompanyDTO;
		Map<String,Object> param = new HashMap<String,Object>();
		String companyId = this.currentCompanyId;
		param.put("companyId", companyId);
		//查询当前 公司未激活人数
		List<CompanyUserTableDTO> data = companyUserService.getCompanyUserOfNotActive(param);
		for(int i = 0 ;data!=null &&i<data.size();i++){
			String cellphone = data.get(i).getCellphone();
			if(StringUtil.isNumeric(cellphone)&&cellphone.length()==11){
				Sms sms=new Sms();
				sms.addMobile(cellphone);
				sms.setMsg(StringUtil.format(SystemParameters.ADD_COMPANY_USER_MSG_1, accountName,company.getCompanyName(),this.serverUrl));
				log.debug("短信发送结果::{}",smsSender.send(sms));
			}
		}
		return this.ajaxResponseSuccess("发送成功");
	}

	/**
	 * 方法描述：解散团队
	 * 作        者：MaoSF
	 * 日        期：2016年4月26日-下午2:24:31
	 * @return
	 */
	@RequestMapping(value="/disbandCompany/{adminPassword}", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage disbandCompany(@PathVariable("adminPassword") String adminPassword) throws Exception{
		String companyId=this.currentCompanyId;
		String userId=this.getFromSession("userId", String.class);
		TeamOperaterEntity teamOper= teamOperaterService.getTeamOperaterByParam(companyId, userId);
		if(teamOper.getAdminPassword().equals(MD5Helper.getMD5For32(adminPassword))){
			return companyService.disbandCompany(companyId);
		}
		else{
			return this.ajaxResponseError("管理员密码有误，请重新输入！");
		}

	}


	/**
	 * 方法描述：组织成员排序（上移，下移）
	 * 作者：MaoSF
	 * 日期：2016/8/5
	 * @param:[map]
	 * @return:com.maoding.core.bean.AjaxMessage
	 */
	@RequestMapping(value="/orderCompanyUser", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage orderCompanyUser(@RequestBody OrderCompanyUserDTO dto) throws Exception{
		CompanyUserTableDTO companyUserDTO1 = dto.getCompanyUser1();
		CompanyUserTableDTO companyUserDTO2 = dto.getCompanyUser2();
		String orgId = dto.getOrgId();
		return companyUserService.orderCompanyUser(companyUserDTO1,companyUserDTO2,orgId);
	}

	@RequestMapping("file/downloadQrcodeImg")
	public void downloadQrcodeImg(HttpServletResponse response) throws Exception {

		String filePath = this.projectSkyDriverService.getCompanyFileByType(this.currentCompanyId, NetFileType.COMPANY_QR_CODE_ATTACH);
		if (!StringUtil.isNullOrEmpty(filePath)) {
			String fileName = this.currentCompanyDTO.getCompanyName()+".jpg";
		//	uploadService.downLoadFileByFdfs(response,filePath,fileName);
		}

	}
    //=========================================新接口2.0=============================================
	/**
	 * 方法描述：团队成员列表信息【组织界面-团队人员列表，人员选择列表】分页 (v2.0)
	 * 作        者：tanyyi
	 * 日        期：2016年12月14日
	 * @param param orgId 组织Id pageSize pageIndex
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOrgUserList", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage getOrgUserList(@RequestBody Map<String,Object> param) throws Exception{
		param.put("auditStatus", "1");
		if(null == param.get("orgId")){
			param.put("orgId", this.currentCompanyId);
		}
		List<CompanyUserTableDTO> data = companyUserService.getCompanyUserListByOrgIdOfAdmin(param);
		int totalNumber = companyUserService.getCompanyUserListByOrgIdCountOfAdmin(param);
		param.clear();
		param.put("data",data);
		param.put("total", totalNumber);
		return this.ajaxResponseSuccess().setData(param);
	}
}
