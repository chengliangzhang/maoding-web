package com.maoding.org.service.impl;

import com.maoding.conllaboration.service.CollaborationService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.bean.Sms;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.JedisUtils;
import com.maoding.core.util.ListTranscoder;
import com.maoding.core.util.SecretUtil;
import com.maoding.core.util.StringUtil;
import com.maoding.hxIm.constDefine.ImGroupType;
import com.maoding.hxIm.dao.ImGroupDao;
import com.maoding.hxIm.entity.ImGroupEntity;
import com.maoding.hxIm.service.ImService;
import com.maoding.org.dao.*;
import com.maoding.org.dto.*;
import com.maoding.org.entity.*;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.CompanyUserService;
import com.maoding.role.dao.RoleDao;
import com.maoding.role.dao.RoleUserDao;
import com.maoding.role.dao.UserPermissionDao;
import com.maoding.role.entity.RoleEntity;
import com.maoding.role.entity.RoleUserEntity;
import com.maoding.user.dao.AccountDao;
import com.maoding.user.dao.UserDao;
import com.maoding.user.entity.AccountEntity;
import com.maoding.user.entity.UserEntity;
import com.maoding.user.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyUserServiceImpl
 * 类描述：团队（公司）人员 ServiceImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午4:24:38
 */
@Service("companyUserService")
public class CompanyUserServiceImpl extends GenericService<CompanyUserEntity>  implements CompanyUserService{
	private final Logger log=LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SmsSender smsSender;
	
	@Autowired
    private CompanyUserDao companyUserDao;

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleUserDao roleUserDao;
	
	@Autowired
	private OrgDao orgDao;
	
	@Autowired
	private OrgUserDao orgUserDao;

	@Autowired
	private CompanyUserAuditDao companyUserAuditDao;
	
	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private DepartDao departDao;

	@Autowired
	private JedisUtils jedisUtils;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserPermissionDao userPermissionDao;

	@Autowired
	private ImGroupDao imGroupDao;

	@Autowired
	private CollaborationService collaborationService;

	@Autowired
	private ImService imService;

	@Value("${server.url}")
	protected String serverUrl;

	@Value("${android.url}")
	protected String androidUrl;

	@Value("${ios.url}")
	protected String iosUrl;
	
	@Override
	public CompanyUserEntity getCompanyUserByUserIdAndCompanyId(String userId, String companyId) throws Exception {
		return companyUserDao.getCompanyUserByUserIdAndCompanyId(userId, companyId);
	}
	@Override
	public CompanyUserLiteDTO getCompanyUserLiteDTO(String accountId, String companyId) {
		return companyUserDao.getCompanyUserLiteDTO(accountId, companyId);
	}
	/**
	 * 方法描述：查找当前公司所有人员
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 * @param companyId
	 * @return
	 */
	public List<CompanyUserEntity> getCompanyUserByCompanyId(String companyId){
		return companyUserDao.getCompanyUserByCompanyId(companyId);
	}

	/**
	 * 方法描述：根据id查询数据
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 *
	 * @param id
	 * @return
	 */
	@Override
	public CompanyUserTableDTO getCompanyUserById(String id) throws Exception {
		if (id == null) return null;
		CompanyUserTableDTO dto = companyUserDao.getCompanyUserById(id);
		List<UserDepartDTO> departList = companyUserDao.getCompanyUserDepartRole(dto.getUserId(),dto.getCompanyId());
		dto.setDepartList(departList);
		return dto;
	}


	@Override
	public int getCompanyUserByOrgIdCount(Map<String, Object> param)
			throws Exception {
		return companyUserDao.getCompanyUserByOrgIdCount(param);
	}

	/**
	 * 方法描述：组织人员查询（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param@return
	 * @throws Exception
	 */
	@Override
	public List<CompanyUserTableDTO> getCompanyUserByOrgIdOfAdmin(Map<String, Object> param) throws Exception {
		if(null!=param.get("pageNumber")){
			int page=(Integer)param.get("pageNumber");
			int pageSize=(Integer) param.get("pageSize");
			param.put("startPage",page*pageSize);
			param.put("endPage",pageSize);
		}
		List<CompanyUserTableDTO> list = companyUserDao.getCompanyUserByOrgIdOfAdmin(param);

		for(CompanyUserTableDTO c:list){
			List<UserDepartDTO> departList = companyUserDao.getCompanyUserDepartRole(c.getUserId(),c.getCompanyId());
			c.setDepartList(departList);
		}
		return list;
	}

	/**
	 * 方法描述：组织人员条数（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getCompanyUserByOrgIdCountOfAdmin(Map<String, Object> param) throws Exception {
		return companyUserDao.getCompanyUserByOrgIdCountOfAdmin(param);
	}


	/**
	 * 方法描述：组织人员查询
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param(orgId)【orgId组织Id，companyId 公司Id】
	 */
	public List<CompanyUserTableDTO> getUserByOrgId (Map<String,Object> param) throws Exception{
		return companyUserDao.getUserByOrgId(param);
	}


	@Override
	public List<CompanyUserTableDTO> getCompanyUserOfNotActive(
			Map<String, Object> param) throws Exception {
		
		if(null!=param.get("pageNumber")){
			int page=(Integer)param.get("pageNumber");
			int pageSize=(Integer) param.get("pageSize");
			param.put("startPage",page*pageSize);
			param.put("endPage",pageSize);
		}

		List<CompanyUserTableDTO> list = companyUserDao.getCompanyUserOfNotActive(param);
		for(CompanyUserTableDTO c:list){
			List<UserDepartDTO> departList = companyUserDao.getCompanyUserDepartRole(c.getUserId(),c.getCompanyId());
			c.setDepartList(departList);
		}

		return list;
	}

	@Override
	public int getCompanyUserOfNotActiveCount(Map<String, Object> param)
			throws Exception {
		return companyUserDao.getCompanyUserOfNotActiveCount(param);
	}
	
	/**
	 * 方法描述：验证添加人员（saveCompanyUser）
	 * 作        者：MaoSF
	 * 日        期：2016年7月14日-上午11:24:22
	 * @param dto
	 * @return
	 */
	public AjaxMessage validateSaveCompanyUser(CompanyUserTableDTO dto){
		if (StringUtil.isNullOrEmpty(dto.getCompanyId())){
			return new AjaxMessage().setCode("1").setInfo("请选择团队");
		}
		CompanyEntity company = companyDao.selectById(dto.getCompanyId());
		if(company==null){
			return new AjaxMessage().setCode("1").setInfo("请选择团队");
		}
		if (StringUtil.isNullOrEmpty(dto.getCompanyId())){
			return new AjaxMessage().setCode("1").setInfo("请选择团队");
		}

		if(StringUtil.isNullOrEmpty(dto.getCellphone())){
			return new AjaxMessage().setCode("1").setInfo("手机号不能为空");
		}
		if(StringUtil.isNullOrEmpty(dto.getUserName())){
			return new AjaxMessage().setCode("1").setInfo("姓名不能为空");
		}
		if(null !=dto.getDepartList() && dto.getDepartList().size()>0){
			for(int i = 0;i<dto.getDepartList().size();i++){
				if(StringUtil.isNullOrEmpty(dto.getDepartList().get(i).getDepartId())){
					return new AjaxMessage().setCode("1").setInfo("部门不能为空");
				}
			}
		}
		return new AjaxMessage().setCode("0");
	}

	@Override
	public AjaxMessage saveCompanyUser(CompanyUserTableDTO dto) throws Exception{

		AjaxMessage ajax = validateSaveCompanyUser(dto);
		if(!"0".equals(ajax.getCode())){
			return ajax;
		}

		AccountEntity account= null;
		String cellphone=dto.getCellphone();
		CompanyUserEntity cUser =null;
		/************如果没有存公司，首先胖的是否已经注册，若没有注册，先注册，然后加入公司，如果注册，直接加入公司*************/
		boolean isSave = false;
		if(dto.getId()==null){//新增人
			account=accountDao.getAccountByCellphoneOrEmail(cellphone);
			boolean isNewUser=false;
			//没有人员，先注册
			if(account==null){
				account = this.register(dto);
				isNewUser= true;
			}//账号已经存在

            //查询该账户是否存在公司中
			cUser = this.getCompanyUserByUserIdAndCompanyId(account.getId(), dto.getCompanyId());
            String comUserId="";
			if(cUser !=null){//判断是否已经存在公司
				comUserId=cUser.getId();
				BaseDTO.copyFields(dto, cUser);
				cUser.setId(comUserId);
				cUser.setCreateBy(dto.getAccountId());
				cUser.setUserId(account.getId());
				cUser.setAuditStatus("1");
				companyUserDao.updateById(cUser);
			}
			//入股不存在该公司
			if(cUser ==null){
				/****************添加公司人员*****************/
				cUser = new CompanyUserEntity();
				BaseDTO.copyFields(dto, cUser);
				cUser.setId(StringUtil.buildUUID());
				cUser.setUserId(account.getId());
				cUser.setAuditStatus("1");
				cUser.setRelationType("2");//受邀请
				cUser.setCreateBy(dto.getAccountId());
				this.saveCompanyUser(cUser,dto.getCellphone());
				isSave = true;
			}
			//处理部门
			this.handleDepartForAdd(dto,cUser);
			//新增的时候才发送短信
			if(isSave){
				this.sendMsg(isNewUser,dto.getCompanyName(),dto.getCellphone(),dto.getAccountId(),dto.getCompanyId());
			}
		}else{
			/****************修改公司人员信息*****************/
			cUser = new CompanyUserEntity();
			BaseDTO.copyFields(dto, cUser);
			cUser.setCreateBy(dto.getAccountId());
			companyUserDao.updateById(cUser);
			//处理部门
			this.handleDepartForEdit(dto,cUser);
		}
        //处理人员在群组的信息
		if(isSave){
			this.sendCompanyUserMessageFun(cUser.getCompanyId(),cUser.getUserId(),cUser.getAuditStatus());
		}
		//删除没有成员的一级部门群
		this.handleDeleteNoMemberGroup(dto.getCompanyId());
		//通知协同
		this.collaborationService.pushSyncCMD_CU(dto.getCompanyId());
		/***************2016/08/19修改（把部门权限取消）end*****************/

		return new AjaxMessage().setCode("0").setInfo("保存成功").setData(this.getCompanyUserById(cUser.getId()));
	}

    /**
     * 方法描述：添加/移除用户到团队群,
     * 作者：MaoSF
     * 日期：2016/11/29
     * @param:orgId(组织id，公司或许部门），userId，auditStatus：成员的状态（1有效，4：离职）
     * @return:
     */
	public void sendCompanyUserMessageFun(String orgId ,String userId,String auditStatus) throws Exception{
        /************添加到团队群，暂时屏蔽，后期添加*********/
		if ("1".equals(auditStatus)) {
			imService.addMembers(orgId,userId);
		} else if ("4".equals(auditStatus)) {
			imService.deleteMembers(orgId,userId);
		}
    }

    /**
     * 方法描述：删除没有成员的一级部门群
     * 作者：MaoSF
     * 日期：2016/11/29
     * @param:
     * @return:
     */
	private void handleDeleteNoMemberGroup(String companyId) throws Exception{
		Map<String,Object> params = new HashMap<>();
		params.put("companyId",companyId);
		List<DepartEntity> departEntityList = departDao.selectStairDepartCompanyId(params);
		//List<DepartEntity> departEntityList = departDao.selectDepartByParam(params);
		for(DepartEntity departEntity : departEntityList){
			Map<String,Object> mapPass = new HashMap<>();
			mapPass.put("orgId",departEntity.getId());
			List<OrgUserEntity> orgUserEntityList = orgUserDao.selectByParam(mapPass);
			if(orgUserEntityList.size()==0){
				this.removeImGroup(departEntity.getId());
			}
		}
	}


	/**
	 * 方法描述：删除组织群组
	 * 作者：MaoSF
	 * 日期：2016/11/29
	 * @param:
	 * @return:
	 */
	private void removeImGroup(String orgId) throws Exception{
		this.imService.deleteImGroup(orgId, ImGroupType.DEPARTMENT);
	}

	/**
	 * 方法描述：修改人员，部门处理
	 * 作者：MaoSF
	 * 日期：2016/9/20
	 * @param:
	 * @return:
	 */
	private void handleDepartForEdit(CompanyUserTableDTO dto,CompanyUserEntity cUser) throws Exception{
		//添加部门与成员之间的关系
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyId", dto.getCompanyId());
		param.put("cuId", cUser.getId());
		List<OrgUserEntity> orgUserEntityList = orgUserDao.selectByParam(param);
		String notDeleteIds ="";
		if(dto.getDepartList() !=null && dto.getDepartList().size()>0){
			for(int j=0;j<dto.getDepartList().size();j++){
				UserDepartDTO userDepartDTO=dto.getDepartList().get(j);
				OrgUserEntity orgUser = new OrgUserEntity();
				orgUser.setCompanyId(cUser.getCompanyId());
				orgUser.setUserId(cUser.getUserId());
				orgUser.setCuId(cUser.getId());
				orgUser.setOrgId(userDepartDTO.getDepartId());
				orgUser.setServerStation(userDepartDTO.getServerStation());

				if(StringUtil.isNullOrEmpty(userDepartDTO.getId()))
				{
					orgUser.setId(StringUtil.buildUUID());
					orgUser.setCreateBy(dto.getAccountId());
					orgUserDao.insert(orgUser);
				}else{
					orgUser.setId(userDepartDTO.getId());
					orgUser.setUpdateBy(dto.getAccountId());
					//orgUserDao.updateById(orgUser);
					orgUserDao.updateById(orgUser);
					notDeleteIds+=orgUser.getId();
				}

                //查询部门，如果是一级部门，则进行加入群组
                this.handleDepartGroup(userDepartDTO.getDepartId(),cUser.getUserId());
			}
		}

		//删除原来有，现在没有的部门
		if(!StringUtil.isNullOrEmpty(notDeleteIds)){
			if(orgUserEntityList!=null){
				for(OrgUserEntity orgUserEntity:orgUserEntityList){
					if(notDeleteIds.indexOf(orgUserEntity.getId())<0){
                        //删除部门群用户
						this.handleDeleteGroupMemberFromOneDepartGroup(orgUserEntity.getOrgId(),cUser);
                        //从部门中删除人员
						orgUserDao.deleteById(orgUserEntity.getId());
					}
				}
			}
		}
	}
	//删除部门群用户
	private void handleDeleteGroupMemberFromOneDepartGroup(String orgId,CompanyUserEntity cUser) throws Exception{
		//查询部门
		DepartEntity departEntity = departDao.selectById(orgId);
		//存在
		if(null != departEntity){
			String oneDepartId = departEntity.getDepartPath();
			String[] oneDepartIds = oneDepartId.split("-");
			//查询出一级部门群
			departEntity = departDao.selectById(oneDepartIds[0]);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("orgId",departEntity.getId());
			map.put("groupType","1");
			//查询是否已经存在一级部门群
			List<ImGroupEntity> listDepartGroup = imGroupDao.getImGroupsByParam(map);
			if(listDepartGroup.size()>0){
				this.sendCompanyUserMessageFun(orgId,cUser.getUserId(),"4");
			}
		}
	}


	/**
     * 方法描述：处理部门群组
     * 作者：MaoSF
     * 日期：2016/11/29
     * @param:
     * @return:
     */
	private void handleDepartGroup(String departId,String userId) throws Exception{

		//查询部门
//		DepartEntity departEntity = departDao.selectById(departId);
//		//存在
//		if(null != departEntity){
//			String oneDepartId = departEntity.getDepartPath();
//			String[] oneDepartIds = oneDepartId.split("-");
//			//查询出一级部门群
//			departEntity = departDao.selectById(oneDepartIds[0]);
//			Map<String,Object> map = new HashMap<String,Object>();
//			map.put("orgId",departEntity.getId());
//			map.put("groupType","1");
//			//查询是否已经存在一级部门群
//			List<ImGroupEntity> listDepartGroup = imGroupDao.getImGroupsByParam(map);
//			if(listDepartGroup.size()==0){
//				//创建部门群组
//				this.createGroupIm(departEntity.getId(),userId,departEntity.getDepartName(),1,true);
//			}else{
//				//添加人到一级部门群组(String userId,String groupId,String departId)
//				addUserToGroup(userId,departEntity.getId(),departEntity.getId());
//			}
//		}
    }


	/**
	 * 方法描述：增加人员，部门处理
	 * 作者：MaoSF
	 * 日期：2016/9/20
	 * @param:
	 * @return:
	 */
	private void handleDepartForAdd(CompanyUserTableDTO dto,CompanyUserEntity cUser) throws Exception{
		//添加部门与成员之间的关系
		if(dto.getDepartList() !=null && dto.getDepartList().size()>0){
			Map<String,Object> param = new HashMap<String,Object>();
			//查询以前的部门
			param.put("companyId", dto.getCompanyId());
			param.put("cuId", cUser.getId());
			List<OrgUserEntity> orgUserEntityList = orgUserDao.selectByParam(param);
			//把OrgUserEntity放入map中，一遍更加orgId获取
			Map<String,OrgUserEntity> departOrgMap = new HashMap<String,OrgUserEntity>();
			if(orgUserEntityList!=null){
				for(OrgUserEntity orgUserEntity:orgUserEntityList){
					departOrgMap.put(orgUserEntity.getOrgId(),orgUserEntity);
				}
			}

			//循环传递过来的部门信息
			for(int j=0;j<dto.getDepartList().size();j++){
				UserDepartDTO userDepartDTO=dto.getDepartList().get(j);
				OrgUserEntity orgUser = new OrgUserEntity();
				orgUser.setCompanyId(cUser.getCompanyId());
				orgUser.setUserId(cUser.getUserId());
				orgUser.setCuId(cUser.getId());
				orgUser.setOrgId(userDepartDTO.getDepartId());
				orgUser.setServerStation(userDepartDTO.getServerStation());
				if(StringUtil.isNullOrEmpty(userDepartDTO.getId()))
				{
					if(departOrgMap.containsKey(userDepartDTO.getDepartId())){
						orgUser.setId(departOrgMap.get(userDepartDTO.getDepartId()).getId());
						orgUser.setUpdateBy(dto.getAccountId());
						orgUserDao.updateById(orgUser);
					}else {
						orgUser.setId(StringUtil.buildUUID());
						orgUser.setCreateBy(dto.getAccountId());
						orgUserDao.insert(orgUser);
					}
				}else{
					orgUser.setId(userDepartDTO.getId());
					orgUser.setUpdateBy(dto.getAccountId());
					orgUserDao.updateById(orgUser);
				}
				//处理部门群组
				this.handleDepartGroup(userDepartDTO.getDepartId(),cUser.getUserId());
			}
		}
	}


	/**
	 * 方法描述：添加修改人员.角色处理
	 * 作者：MaoSF
	 * 日期：2016/9/20
	 * @param:
	 * @return:
	 */
	private void handleRole(CompanyUserTableDTO dto,CompanyUserEntity cUser){
		//删除所有的角色
		Map<String,Object> param = new HashMap<String,Object>();
		param.clear();
		param.put("companyId", cUser.getCompanyId());
		param.put("userId", cUser.getUserId());
		int i = roleUserDao.deleteUserRole(param);

		//添加权限
		if(!StringUtil.isNullOrEmpty(dto.getRoleIds())){
			String[] roleIds = dto.getRoleIds().split(",");
			for(String roleId:roleIds){
				RoleUserEntity roleUser=new RoleUserEntity();
				roleUser.setId(StringUtil.buildUUID());
				roleUser.setCompanyId(cUser.getCompanyId());
				roleUser.setOrgId(cUser.getCompanyId());
				roleUser.setUserId(cUser.getUserId());
				roleUser.setRoleId(roleId);//普通员工角色    自己创建的公司，设置为总经理角色
				roleUser.setCreateBy(dto.getAccountId());
				roleUserDao.insert(roleUser);  //保存人员与权限之间的关系
			}
		}

		String key = cUser.getUserId()+"-"+cUser.getCompanyId();
		try {
			ListTranscoder<RoleEntity> listTranscoder = new ListTranscoder<>();
			List<RoleEntity> roleList =roleDao.getUserRolesByOrgId( cUser.getUserId(),cUser.getCompanyId());
			//存入redis中
			jedisUtils.setBytes(key.getBytes(), listTranscoder.serialize(roleList),0);
		}catch (Exception e){
			e.printStackTrace();
		}

	}



	/**
	 * 方法描述：添加人员（若没注册，先注册）【此方法不是接口】
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午5:20:59
	 * @param dto
	 * @return
	 */
	public AccountEntity register(CompanyUserTableDTO dto) throws Exception{
		AccountEntity account = this.accountService.createAccount(dto.getUserName(), SecretUtil.MD5("123456"),dto.getCellphone());
		if(!StringUtil.isNullOrEmpty(dto.getSex()))
		{
			if("男".equals(dto.getSex().trim()) || "女".equals(dto.getSex().trim()))
			{
				UserEntity user = new UserEntity();
				user.setId(account.getId());
				user.setSex(dto.getSex().trim());
				userDao.updateById(user);
			}
		}
		dto.setUserId(account.getId());
		return account;
	}

	/**
	 * 方法描述：设置用户权限【此方法不是接口】--员工角色已被删除，此方法暂无使用
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午8:24:18
	 * @param cUser
	 * @param accountId
	 */
	public void saveUserRole(CompanyUserEntity cUser,String accountId){
		/****************赋权限******************/
		RoleUserEntity roleUser=new RoleUserEntity();
		roleUser.setId(StringUtil.buildUUID());
		roleUser.setCompanyId(cUser.getCompanyId());
		roleUser.setOrgId(cUser.getCompanyId());
		roleUser.setUserId(cUser.getUserId());
		roleUser.setRoleId("3399429d3c514e09adaee86670c24f6e");//普通员工角色  
		roleUser.setCreateBy(accountId);
		roleUserDao.insert(roleUser);  //保存人员与权限之间的关系
		
	}
	
	/**
	 * 方法描述：保存公司成员【此方法不是接口】
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午5:37:42
	 * @param entity
	 * @return
	 */
	public CompanyUserEntity saveCompanyUser(CompanyUserEntity entity,String cellphone) throws Exception{
		//保存OrgEntity,组织基础表
		orgDao.deleteById(entity.getId());//线删除基础表中的数据（如果存在）
		OrgEntity orgUserEntity=new OrgEntity();
		orgUserEntity.setId(entity.getId());//基础表id和人员表id一致
		orgUserEntity.setOrgType("4");
		orgUserEntity.setOrgStatus("0");
		orgUserEntity.setCreateBy(entity.getCreateBy());
		orgDao.insert(orgUserEntity);
		companyUserDao.insert(entity);

		//添加了成员，查询companyUserAudit表中是否有该人的申请信息， 如果有，则删除
		companyUserAuditDao.deleteByCellphoneAndCompanyId(cellphone,entity.getCompanyId());
		/************添加到团队群*********/
		//imGroupService.addUserToGroup(entity.getUserId(), entity.getCompanyId());
		return entity;
	}
	
	/**
	 * 方法描述：发送短信【此方法不是接口】
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午8:05:44
	 * @param companyName
	 * @param cellphone
	 */
	public void sendMsg(boolean isNewUser,String companyName,String cellphone,String accountId,String companyId){
		String accountName ="";
		CompanyUserEntity companyUser = companyUserDao.getCompanyUserByUserIdAndCompanyId(accountId,companyId);
		if(companyUser!=null){
			accountName = companyUser.getUserName();
		}
		Sms sms=new Sms();
		sms.addMobile(cellphone);
		//暂时屏蔽短信
		if(isNewUser)
		{
			sms.setMsg(StringUtil.format(SystemParameters.ADD_COMPANY_USER_MSG_1, accountName,companyName,this.serverUrl,this.iosUrl,this.androidUrl));
		}else {
			sms.setMsg(StringUtil.format(SystemParameters.ADD_COMPANY_USER_MSG_2, accountName,companyName));
		}
		smsSender.send(sms);
	}
	
	@Override
	public Object addUserFile(List<ImportFileCompanyUserDTO> list) throws Exception{
		Map<String,List<ImportFileCompanyUserDTO>> retMap=new HashMap<String,List<ImportFileCompanyUserDTO>>();
		String companyId=list.get(0).getCompanyId();
		CompanyEntity c=companyDao.selectById(companyId);
		Map<String,DepartEntity> departMap=new HashMap<String,DepartEntity>();
		this.doSaveCompanyUser(list,retMap,c.getCompanyName(),departMap);
		//通知协同
		this.collaborationService.pushSyncCMD_CU(companyId);
		return retMap;
	}

	/**
	 * 批量导入（新增人员信息）
	 * 此方法不是userService接口）
	 * @param dto
	 * @param retMap
	 * @param departMap
	 * @throws Exception
     */
	public void saveNewCompanyUser(ImportFileCompanyUserDTO dto,Map<String,List<ImportFileCompanyUserDTO>> retMap,
								   Map<String,DepartEntity> departMap,String companyName,boolean isNewUser) throws Exception{
		/******************保存公司于员工信息*******************/
		dto.setId(StringUtil.buildUUID());
		dto.setAuditStatus("1");
		dto.setRelationType("2");//受邀请
		if(retMap.containsKey("add")){
			retMap.get("add").add(dto);
		}else{
			List<ImportFileCompanyUserDTO> addList=new ArrayList<ImportFileCompanyUserDTO>();
			addList.add(dto);
			retMap.put("add", addList);
		}

		//保存员工信息
		CompanyUserEntity entity = new CompanyUserEntity();
		BaseDTO.copyFields(dto, entity);
		entity.setCreateBy(dto.getAccountId());
		this.saveCompanyUser(entity,dto.getCellphone());

		//处理人员在群组的信息
		this.sendCompanyUserMessageFun(entity.getCompanyId(),entity.getUserId(),"1");
		/******************部门处理******************/
		this.saveDepart(dto,entity,departMap);
		/*********发送短信**********/
		this.sendMsg(isNewUser,companyName, dto.getCellphone(),dto.getAccountId(),dto.getCompanyId());
	}
	/**
	 * 方法描述：此方法，用于批量导入，数据过滤（过滤已经存在该组织的人员）
	 * 如果是离职状态，改变其状态
	 * （此方法不是userService接口）
	 * 作        者：MaoSF
	 * 日        期：2016年4月16日-上午11:26:04
	 * @param list
	 * @return
	 */
	public List<ImportFileCompanyUserDTO> doSaveCompanyUser(List<ImportFileCompanyUserDTO> list,Map<String,List<ImportFileCompanyUserDTO>> retMap,String companyName,Map<String,DepartEntity> departMap)throws Exception{
		List<ImportFileCompanyUserDTO> cuserList=new ArrayList<ImportFileCompanyUserDTO>();
		for(int i=0;i<list.size();i++){
			String cellphone=list.get(i).getCellphone();
			AccountEntity account=accountDao.getAccountByCellphoneOrEmail(cellphone);
			ImportFileCompanyUserDTO dto = list.get(i);
			/***************判断账号是否存在*****************/
			if(account !=null){ 
				CompanyUserEntity cUser = this.getCompanyUserByUserIdAndCompanyId(account.getId(), dto.getCompanyId());
				if(cUser !=null){
					String auditStatus = cUser.getAuditStatus();
					/*********************更新员工状态信息**************************/
					cUser.setAuditStatus("1");
					cUser.setUserName(dto.getUserName());
					cUser.setEmail(dto.getEmail());
					cUser.setPhone(dto.getPhone());
					cUser.setEntryTime(dto.getEntryTime());
					cUser.setRemark(dto.getRemark());
					companyUserDao.updateById(cUser);
					//imGroupService.addUserToGroup(cUser.getUserId(), cUser.getCompanyId());
					 /**********发送短信**********/
					 if(!"1".equals(auditStatus)){//如果不存在则发送短信
						 this.sendMsg(false,companyName, account.getCellphone(),dto.getAccountId(),dto.getCompanyId());
					 }

					if(retMap.containsKey("quit")){
						retMap.get("quit").add(dto);
					}else{
						List<ImportFileCompanyUserDTO> quitList=new ArrayList<ImportFileCompanyUserDTO>();
						quitList.add(dto);
						retMap.put("quit", quitList);
					}
					//设置部门
					this.saveDepart(dto,cUser,departMap);

				}else{
					dto.setUserId(account.getId());
					//新增人员信息
					saveNewCompanyUser(dto,retMap,departMap,companyName,false);
				}
			}else{
				/***************账号不存在，新建账号*****************/
				CompanyUserTableDTO companyUserTableDTO = new CompanyUserTableDTO();
				BaseDTO.copyFields(dto, companyUserTableDTO);
				account = register(companyUserTableDTO);
				dto.setUserId(account.getId());
				//新增人员信息
				saveNewCompanyUser(dto,retMap,departMap,companyName,true);
			}
			
		}
		
		return cuserList;
	}
	
	/**
	 * 方法描述：部门处理（如果部门存在）则返回部门id，如果不存在，新建部门，再返回部门id
	 * （此方法不是companyUserService接口）
	 * 作        者：MaoSF
	 * 日        期：2016年4月16日-上午11:47:47
	 * @param companyId
	 * @param pid
	 * @param departName
	 * @return
	 */
	public DepartEntity initDepart(String companyId,String pid,String departName,DepartEntity root,String accountId){
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("companyId", companyId);
		param.put("departName", departName);
		param.put("pid", pid);
		DepartEntity d=departDao.getByDepartNameAndPid(param);
		if(d==null){
			d=new DepartEntity();
			d.setId(StringUtil.buildUUID());
			d.setCompanyId(companyId);
			d.setPid(pid);
			d.setDepartName(departName);
			d.setCreateBy(accountId);
			if(root==null){
				d.setDepartLevel("1");
				d.setDepartPath(d.getId());
			}else if(root.getDepartPath()!=null && !"".equals(root.getDepartPath())){
				d.setDepartPath(root.getDepartPath()+"-"+d.getId());
				d.setDepartLevel((Integer.parseInt(root.getDepartLevel())+1)+"");
		    }else{
		        	d.setDepartPath(d.getId());
		        	d.setDepartLevel("1");
		     }
			departDao.insert(d);
			//保存OrgEntity,组织基础表
			OrgEntity org=new OrgEntity();
			org.setId(d.getId());//基础表的id和部门表的id一致
			org.setOrgType("1");
			org.setOrgStatus("0");
			org.setCreateBy(accountId);
			orgDao.insert(org);
			return d;
		}else{
			if(!"0".equals(d.getStatus()))
			{
				d.setStatus("0");
				departDao.updateById(d);
			}
			return d;
		}
	}
	
	/**
	 * 方法描述：设置部门（此方法不是companyUserService接口）
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午8:26:21
	 * @param entity
	 * @param departMap
	 * @return
	 */
	public Object saveDepart(ImportFileCompanyUserDTO dto,CompanyUserEntity entity,Map<String,DepartEntity> departMap) throws Exception{
		
		/******************部门处理******************/
		String companyId=entity.getCompanyId();
		String departName = dto.getDepartName();
		String pid=entity.getCompanyId();
		DepartEntity root=null;
		if(null !=departName && !"".equals(departName))
		{
			String[] departs=departName.split("/");
			for(int j=0;j<departs.length;j++){
				if(!departMap.containsKey(departs[j]+"-"+pid)){
					root=initDepart(companyId,pid,departs[j],root,dto.getAccountId());
					pid=root.getId();//最后得出的pid就是当前用户所在的departId;
					departMap.put(departs[j]+"-"+pid, root);
				}else{
					root=departMap.get(departs[j]+"-"+pid);
					pid=root.getId();//最后得出的pid就是当前用户所在的departId;
				}
			}
		}else{
			pid=entity.getCompanyId();//设置部门跟节点
		}
		/********************添加新的部门********************/
		if(pid!=null){
			/***************查找当前人是否存在该部门****************/
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("cuId", entity.getId());
			param.put("companyId", entity.getCompanyId());
			param.put("orgId",pid);
			List<OrgUserEntity> orgUserEntityList = orgUserDao.selectByParam(param);
			if(orgUserEntityList==null || orgUserEntityList.size()==0){
				OrgUserEntity orgUser=new OrgUserEntity();
				orgUser.setCompanyId(entity.getCompanyId());
				orgUser.setUserId(entity.getUserId());
				orgUser.setCuId(entity.getId());
				orgUser.setOrgId(pid);
				orgUser.setId(StringUtil.buildUUID());
				orgUser.setServerStation(dto.getServerStation());
				orgUser.setCreateBy(entity.getCreateBy());
				orgUserDao.insert(orgUser);
			}else {
				OrgUserEntity orgUser = orgUserEntityList.get(0);
				orgUser.setServerStation(dto.getServerStation());
				orgUser.setUpdateBy(dto.getAccountId());
				orgUserDao.updateById(orgUser);
			}

			//处理群组信息
			handleDepartGroup(pid,entity.getUserId());
		}
		return entity;
	}
	
	@Override
	public AjaxMessage quit(String id) throws Exception{

		//暂时屏蔽删除权限，过后添加
		//this.deleteRole(map1);//删除所在公司的权限
		CompanyUserEntity cuser = this.selectById(id);
		AccountEntity account=accountDao.selectById(cuser.getUserId());
		if(account.getDefaultCompanyId() !=null && cuser.getCompanyId().equals(account.getDefaultCompanyId())){
			//把默认组织设置为null
			account.setDefaultCompanyId(null);
			accountDao.updateById(account);
		}
		cuser.setAuditStatus("4");//离职状态
		int i=companyUserDao.updateById(cuser);
		if(i>0){
			//后期处理
			//删除所有部门信息
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("companyId", cuser.getCompanyId());
			param.put("cuId", cuser.getId());
			//退出部门群
			handleQuitOneLevelDepartGroup(param);

			orgUserDao.deleteOrgUserByParam(param);

			//删除所有的权限
			param.clear();
			param.put("userId",cuser.getUserId());
			param.put("companyId",cuser.getCompanyId());
			this.userPermissionDao.deleteByUserId(param);

			//退出公司群处理
			this.sendCompanyUserMessageFun(cuser.getCompanyId(),cuser.getUserId(),cuser.getAuditStatus());

			//通知协同
			this.collaborationService.pushSyncCMD_CU(cuser.getCompanyId());

			return new AjaxMessage().setCode("0").setInfo("操作成功");
		}
		return new AjaxMessage().setCode("1").setInfo("操作失败");
	}

	private void handleQuitOneLevelDepartGroup(Map<String,Object> param) throws Exception{
		//退出部门群
		List<OrgUserEntity> orgUserEntityList = orgUserDao.selectByParam(param);
		for(OrgUserEntity orgUserEntity : orgUserEntityList){
			//查询部门
			DepartEntity departEntity = departDao.selectById(orgUserEntity.getOrgId());
			//存在
			if(null != departEntity){
				String oneDepartId = departEntity.getDepartPath();
				String[] oneDepartIds = oneDepartId.split("-");
				//查询出一级部门群
				departEntity = departDao.selectById(oneDepartIds[0]);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("orgId",departEntity.getId());
				map.put("groupType","1");
				//查询是否已经存在一级部门群
				List<ImGroupEntity> listDepartGroup = imGroupDao.getImGroupsByParam(map);
				if(listDepartGroup.size()==1){
					//退出部门群处理
					this.sendCompanyUserMessageFun(departEntity.getId(),orgUserEntity.getUserId(),"4");
				}
			}
		}



	}

	/**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2016/8/5
     * @param dto1
     * @param dto2
     * @param orgId
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage orderCompanyUser(CompanyUserTableDTO dto1,CompanyUserTableDTO dto2,String orgId) throws Exception {

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("cuId",dto1.getId());
        map.put("orgId",orgId);
        List<OrgUserEntity> orgUserEntityList1 = orgUserDao.selectByParam(map);
        map.clear();
        map.put("cuId",dto2.getId());
        map.put("orgId",orgId);
        List<OrgUserEntity> orgUserEntityList2 = orgUserDao.selectByParam(map);
        if(orgUserEntityList1.isEmpty() || orgUserEntityList1.size()>1 || orgUserEntityList2.isEmpty() || orgUserEntityList2.size()>1){
            return new AjaxMessage().setCode("1").setInfo("操作失败");
        }
        OrgUserEntity orgUserEntity1 = orgUserEntityList1.get(0);
        OrgUserEntity orgUserEntity2 = orgUserEntityList2.get(0);
		int seq2= orgUserEntity2.getSeq();
		int seq1= orgUserEntity1.getSeq();
        orgUserEntity1.setSeq(seq2);
        orgUserEntity2.setSeq(seq1);
        orgUserDao.updateById(orgUserEntity1);
        orgUserDao.updateById(orgUserEntity2);
        return new AjaxMessage().setCode("0").setInfo("操作成功");
    }

	public String addOneLevelDepartGroup(String id,String departName,String userId) throws Exception{
		AccountEntity account = accountDao.selectById(userId);

		//创建环信账号
		this.createImUser(account.getId(),account.getUserName(),account.getPassword());

		this.createGroupIm(id,userId,departName,1,true);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId",id);
		map.put("groupType","1");
		List<ImGroupEntity> list =	imGroupDao.getImGroupsByParam(map);
		if(list.size()>0){
			return list.get(0).getGroupNo();
		}
		return  null;
	}


	public void addUserToGroup(String userId,String groupId,String departId) throws Exception{

		AccountEntity account = accountDao.selectById(userId);
		if(null != account){
			//创建环信账号
			this.createImUser(account.getId(),account.getUserName(),account.getPassword());

			this.sendCompanyUserMessageFun(departId,userId,"1");

		}
	}
	/**
	 * 方法描述：查询部门用户
	 * 作        者：MaoSF
	 * 日        期：2016年4月23日-下午6:02:15
	 * @param param
	 * @return
	 */
	@Override
	public List<CompanyUserEntity> getUserByDepartId(Map<String,Object> param){
		return companyUserDao.getUserByDepartId(param);
	}

	public String selectOneLevelDepartGroupId(String id, String departName, String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId",id);
		map.put("groupType","1");
		List<ImGroupEntity> list =	imGroupDao.getImGroupsByParam(map);
		if(list.size()>0){
			return list.get(0).getGroupNo();
		}
		return  null;
	}



	/**
	 * 方法描述：根据角色id查询公司人员
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 *
	 * @param param(companyId,roleId)
	 * @param:
	 * @return:
	 */
	@Override
	public List<CompanyUserTableDTO> getCompanyUserByRoleId(Map<String, Object> param) {
		if(null!=param.get("pageNumber")){
			int page=(Integer)param.get("pageNumber");
			int pageSize=(Integer) param.get("pageSize");
			param.put("startPage",page*pageSize);
			param.put("endPage",pageSize);
		}
		List<CompanyUserTableDTO> list = this.companyUserDao.getCompanyUserByRoleId(param);
		if(!CollectionUtils.isEmpty(list)){
			for(CompanyUserTableDTO c:list){
				List<UserDepartDTO> departList = companyUserDao.getCompanyUserDepartRole(c.getUserId(),c.getCompanyId());
				c.setDepartList(departList);
			}
		}
		return list;
	}

	/**
	 * 方法描述：根据权限id查询公司人员
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 *
	 * @param param(companyId,roleId)
	 * @param:
	 * @return:
	 */
	@Override
	public List<CompanyUserTableDTO> getCompanyUserByPermissionId(Map<String, Object> param) {
		if(null!=param.get("pageNumber")){
			int page=(Integer)param.get("pageNumber");
			int pageSize=(Integer) param.get("pageSize");
			param.put("startPage",page*pageSize);
			param.put("endPage",pageSize);
		}
		List<CompanyUserTableDTO> list = this.companyUserDao.getCompanyUserByPermissionId(param);
		return list;
	}

	@Override
	public  List<CompanyUserDTO>  selectCompanyUserId(Map<String,Object>pubMap) throws Exception{
		String messSource=pubMap.get("messSource").toString();
		String [] messSources=messSource.split("[;]");
		String messSourceType=pubMap.get("messSourceType").toString();
		String [] messSourceTypes=messSourceType.split("[;]");
		List<CompanyUserDTO>  companyUsers=new ArrayList<CompanyUserDTO>();
		Map<String,CompanyUserDTO> userMap = new HashMap<String,CompanyUserDTO>();
		for(int i=0;i<messSourceTypes.length;i++) {
			if ("company".equals(messSourceTypes[i])||"partner".equals(messSourceTypes[i])||"subCompany".equals(messSourceTypes[i])) {
				CompanyEntity company = companyDao.selectById(messSources[i]);
				List<CompanyEntity> companyEntityList = new ArrayList<CompanyEntity>();
				companyEntityList.addAll(companyService.getAllChildrenCompany(company.getId()));
				companyEntityList.add(company);
				for (CompanyEntity companyEntity : companyEntityList) {
					List<CompanyUserEntity> companyUserEntities = companyUserDao.getCompanyUserByCompanyId(companyEntity.getId());
					for (CompanyUserEntity companyUserEntity : companyUserEntities) {

						if(userMap.containsKey(companyUserEntity.getUserId())){//如果存在，则只添加orgId
							CompanyUserDTO userDTO2 =  userMap.get(companyUserEntity.getUserId());
							userDTO2.setOrgId(userDTO2.getOrgId()==null?companyEntity.getId():userDTO2.getOrgId()+","+companyEntity.getId());
						}else {//如果不存在则添加到map
							CompanyUserDTO userDTO = new CompanyUserDTO();
							BaseDTO.copyFields(companyUserEntity,userDTO);
							userDTO.setOrgId(companyEntity.getId());
							userMap.put(companyUserEntity.getUserId(),userDTO);
						}
					}
				}
			}
			if ("depart".equals(messSourceTypes[i])) {
				Map<String, Object> param = new HashMap<String, Object>();
				DepartEntity departEn = departDao.selectById(messSources[i]);
				List<DepartEntity> listChild = departDao.getDepartsByDepartPath(pubMap.get("messSource").toString() + "-");
				listChild.add(departEn);
				for (DepartEntity departEntity : listChild) {
					param.put("departId", departEntity.getId());
					List<CompanyUserEntity> companyUserEntities = companyUserDao.getUserByDepartId(param);
					for (CompanyUserEntity companyUserEntity : companyUserEntities) {
						if(userMap.containsKey(companyUserEntity.getUserId())){//如果存在，则只添加orgId
							CompanyUserDTO userDTO2 =  userMap.get(companyUserEntity.getUserId());
							userDTO2.setOrgId(userDTO2.getOrgId()==null?departEntity.getId():userDTO2.getOrgId()+","+departEntity.getId());
						}else {//如果不存在则添加到map
							CompanyUserDTO userDTO = new CompanyUserDTO();
							BaseDTO.copyFields(companyUserEntity,userDTO);
							userDTO.setOrgId(departEntity.getId());
							userMap.put(companyUserEntity.getUserId(),userDTO);
						}
					}
				}
			}
			if ("person".equals(messSourceTypes[i])) {
				CompanyUserEntity companyUserEntity=companyUserDao.selectById(messSources[i]);
				if(userMap.containsKey(companyUserEntity.getUserId())){//如果存在，则只添加orgId
					CompanyUserDTO userDTO2 =  userMap.get(companyUserEntity.getUserId());
					userDTO2.setOrgId(userDTO2.getOrgId()==null?companyUserEntity.getCompanyId():userDTO2.getOrgId()+","+companyUserEntity.getCompanyId());
				}else {//如果不存在则添加到map
					CompanyUserDTO userDTO = new CompanyUserDTO();
					BaseDTO.copyFields(companyUserEntity,userDTO);
					userDTO.setOrgId(companyUserEntity.getCompanyId());
					userMap.put(companyUserEntity.getUserId(),userDTO);
				}
			}
		}

		for(Map.Entry<String, CompanyUserDTO> entry:userMap.entrySet()){
			companyUsers.add(entry.getValue());
		}
		return  companyUsers;
	}

	/**
	 * 方法描述：批量导入处理添加成功员工，添加到群组当中
	 * 作者：Chenzhujie
	 * 日期：2016/11/21
	 * @param:
	 * @return:
	 */
	@Override
	public void handleCompanyIm(List<ImportFileCompanyUserDTO> cList)throws  Exception{
		//公司群
		for(ImportFileCompanyUserDTO userDTO : cList){
			AccountEntity account = accountDao.getAccountByCellphoneOrEmail(userDTO.getCellphone());
			if(account!=null){
				this.sendCompanyUserMessageFun(userDTO.getCompanyId(),account.getId(),"1");
			}
		}
	}

	/**
	 * 方法描述：创建环信账号
	 * 作者：MaoSF
	 * 日期：2016/11/29
	 * @param:
	 * @return:
	 */
	private void createImUser(String userId,String userName,String password) throws Exception{
		this.imService.createImAccount(userId,userName,password);
	}

	/**
	 * 方法描述：创建群组
	 * 作者：MaoSF
	 * 日期：2016/11/29
	 * @param:
	 * @return:
	 */
	private void createGroupIm(String id,String userId,String name,int type,boolean isSetDefaultImg) throws Exception{
		this.imService.createImGroup(id,userId,name,type);
	}

	//=====================================新接口2.0===================================================
	/**
	 * 方法描述：组织人员查询（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param@return
	 * @throws Exception
	 */
	@Override
	public List<CompanyUserTableDTO> getCompanyUserListByOrgIdOfAdmin(Map<String, Object> param) throws Exception {
		if(null!=param.get("pageIndex")){
			int page=(Integer)param.get("pageIndex");
			int pageSize=(Integer) param.get("pageSize");
			param.put("startPage",page*pageSize);
			param.put("endPage",pageSize);
		}
		List<CompanyUserTableDTO> list = companyUserDao.getCompanyUserByOrgIdOfAdmin(param);

		for(CompanyUserTableDTO c:list){
			List<UserDepartDTO> departList = companyUserDao.getCompanyUserDepartRole(c.getUserId(),c.getCompanyId());
			c.setDepartList(departList);
		}
		return list;
	}

	/**
	 * 方法描述：组织人员条数（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getCompanyUserListByOrgIdCountOfAdmin(Map<String, Object> param) throws Exception {
		return companyUserDao.getCompanyUserByOrgIdCountOfAdmin(param);
	}



	@Override
	public List<CompanyUserTableDTO> getComUserOfNotActive(
			Map<String, Object> param) throws Exception {

		if(null!=param.get("pageIndex")){
			int page=(Integer)param.get("pageIndex");
			int pageSize=(Integer) param.get("pageSize");
			param.put("startPage",page*pageSize);
			param.put("endPage",pageSize);
		}

		List<CompanyUserTableDTO> list = companyUserDao.getCompanyUserOfNotActive(param);
		for(CompanyUserTableDTO c:list){
			List<UserDepartDTO> departList = companyUserDao.getCompanyUserDepartRole(c.getUserId(),c.getCompanyId());
			c.setDepartList(departList);
		}

		return list;
	}

	@Override
	public int getComUserOfNotActiveCount(Map<String, Object> param)
			throws Exception {
		return companyUserDao.getCompanyUserOfNotActiveCount(param);
	}

	/**
	 * 方法描述：组织人员条数（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getComUserByOrgIdCountOfWork(Map<String, Object> param) throws Exception {
		return companyUserDao.getCompanyUserByOrgIdCountOfWork(param);
	}



	/**
	 * 方法描述：组织人员查询（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param@return
	 * @throws Exception
	 */
	@Override
	public List<CompanyUserTableDTO> getComUserByOrgIdOfWork(Map<String, Object> param) throws Exception {
		if(null!=param.get("pageIndex")){
			int page=(Integer)param.get("pageIndex");
			int pageSize=(Integer) param.get("pageSize");
			param.put("startPage",page*pageSize);
			param.put("endPage",pageSize);
		}
		List<CompanyUserTableDTO> list = companyUserDao.getCompanyUserByOrgIdOfWork(param);

		for(CompanyUserTableDTO c:list){
			List<UserDepartDTO> departList = companyUserDao.getCompanyUserDepartRole(c.getUserId(),c.getCompanyId());
			c.setDepartList(departList);
		}
		return list;
	}



	/**
	 * 方法描述：组织人员查询（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param@return
	 * @throws Exception
	 */
	@Override
	public List<CompanyUserTableDTO> getComUserByOrgIdOfAdmin(Map<String, Object> param) throws Exception {
		if(null!=param.get("pageIndex")){
			int page=(Integer)param.get("pageIndex");
			int pageSize=(Integer) param.get("pageSize");
			param.put("startPage",page*pageSize);
			param.put("endPage",pageSize);
		}
		List<CompanyUserTableDTO> list = companyUserDao.getCompanyUserByOrgIdOfAdmin(param);

		for(CompanyUserTableDTO c:list){
			List<UserDepartDTO> departList = companyUserDao.getCompanyUserDepartRole(c.getUserId(),c.getCompanyId());
			c.setDepartList(departList);
		}
		return list;
	}

	/**
	 * 方法描述：组织人员条数（分页查询）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getComUserByOrgIdCountOfAdmin(Map<String, Object> param) throws Exception {
		return companyUserDao.getCompanyUserByOrgIdCountOfAdmin(param);
	}

	/**
	 * 方法描述：根据关键字查询公司成员
	 * 作者：MaoSF
	 * 日期：2017/3/2
	 *
	 * @param param
	 * @param:
	 * @return:
	 */
	@Override
	public List<Map<String, String>> getUserByKeyWord(Map<String, Object> param) {
		return companyUserDao.getUserByKeyWord(param);
	}

	@Override
	public boolean isUserInOrg(Map<String, Object> param) {

		List<CompanyUserEntity> list = companyUserDao.getCompanyUserByParam(param);
		if(list!=null && list.size()>0){
			return true;
		}else {
			return false;
		}
	}

	@Override
	public List<CompanyUserTableDTO> getOperatorManager(String companyId) {
		Map<String, Object> map = new HashMap<>();
		map.put("permissionId", SystemParameters.OPERATOR_MANAGER_PERMISSION_ID);//经营总监权限id
		map.put("companyId", companyId);
		return this.companyUserDao.getCompanyUserByPermissionId(map);
	}

	@Override
	public List<CompanyUserTableDTO> getFinancialManager(String companyId) {
		Map<String, Object> map = new HashMap<>();
		map.put("permissionId", SystemParameters.FINANCIAL_PERMISSION_ID);//项目费用付款权限
		map.put("companyId", companyId);
		return this.companyUserDao.getCompanyUserByPermissionId(map);
	}

	@Override
	public List<CompanyUserTableDTO> getFinancialManagerForReceive(String companyId) {
		Map<String, Object> map = new HashMap<>();
		map.put("permissionId", SystemParameters.FINANCIAL_RECEIVE_PERMISSION_ID);//项目费用收款权限
		map.put("companyId", companyId);
		return this.companyUserDao.getCompanyUserByPermissionId(map);
	}

	@Override
	public List<CompanyUserTableDTO> getDesignManager(String companyId) {
		Map<String, Object> map = new HashMap<>();
		map.put("permissionId", SystemParameters.DESIGN_MANAGER_PERMISSION_ID);//设计总监权限id
		map.put("companyId", companyId);
		return this.companyUserDao.getCompanyUserByPermissionId(map);
	}

	@Override
	public CompanyUserTableDTO getOrgManager(String companyId) {
		Map<String, Object> map = new HashMap<>();
		map.put("permissionId", SystemParameters.ORG_MANAGER_PERMISSION_ID);//经营总监权限id
		map.put("companyId", companyId);
		List<CompanyUserTableDTO> list = this.companyUserDao.getCompanyUserByPermissionId(map);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}
}
