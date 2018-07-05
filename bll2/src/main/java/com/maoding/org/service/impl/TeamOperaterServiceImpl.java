package com.maoding.org.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.bean.Sms;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.StringUtil;
import com.maoding.message.entity.MessageEntity;
import com.maoding.message.service.MessageService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dao.TeamOperaterDao;
import com.maoding.org.dto.CompanyUserTableDTO;
import com.maoding.org.dto.TeamOperaterDTO;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.entity.TeamOperaterEntity;
import com.maoding.org.service.TeamOperaterService;
import com.maoding.role.dao.PermissionDao;
import com.maoding.role.dao.RolePermissionDao;
import com.maoding.role.dao.RoleUserDao;
import com.maoding.role.dao.UserPermissionDao;
import com.maoding.role.dto.SaveRoleUserDTO;
import com.maoding.role.entity.PermissionEntity;
import com.maoding.role.entity.RolePermissionEntity;
import com.maoding.role.entity.RoleUserEntity;
import com.maoding.role.entity.UserPermissionEntity;
import com.maoding.role.service.RoleUserService;
import com.maoding.role.service.UserPermissionService;
import com.maoding.user.dao.AccountDao;
import com.maoding.user.entity.AccountEntity;
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
@Service("teamOperaterService")
public class TeamOperaterServiceImpl extends GenericService<TeamOperaterEntity>  implements TeamOperaterService{

	@Autowired
	private TeamOperaterDao teamOperaterDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private SmsSender smsSender;

	@Autowired
	private RoleUserService roleUserService;

	@Autowired
	private RoleUserDao roleUserDao;

	@Autowired
	private PermissionDao permissionDao;

	@Autowired
	private UserPermissionDao userPermissionDao;

    @Autowired
    private CompanyUserDao companyUserDao;

	@Autowired
	private UserPermissionService userPermissionService;

	@Autowired
	private MessageService messageService;
	
	@Override
	public AjaxMessage transferSys(TeamOperaterDTO dto,String newPassword) throws Exception{
		TeamOperaterEntity entity = teamOperaterDao.getTeamOperaterByCompanyId(dto.getCompanyId(), null);
		if(entity==null){
			return AjaxMessage.failed("移交失败");
		}
		entity.setUserId(dto.getUserId());
		teamOperaterDao.updateById(entity);
		//添加系统管理员角色
//		SaveRoleUserDTO saveRoleUserDTO = new SaveRoleUserDTO();
//		saveRoleUserDTO.setRoleId(SystemParameters.ADMIN_MANAGER_ROLE_ID);
//		List<String> users = new ArrayList<String>();
//		users.add(dto.getUserId());
//		saveRoleUserDTO.setUserIds(users);
//		saveRoleUserDTO.setCurrentCompanyId(dto.getCompanyId());
//		this.roleUserService.saveOrgManager(saveRoleUserDTO);
		//-----------------end-----------------
		CompanyEntity companyEntity = companyDao.selectById(dto.getCompanyId());
		AccountEntity accountEntity = accountDao.selectById(dto.getUserId());
		if(companyEntity!=null && accountEntity!=null) {
			String msg = StringUtil.format(SystemParameters.TRANSFER_ADMIN_MSG, companyEntity.getCompanyName());
			this.sendMsg(accountEntity.getCellphone(),msg);
			//推送消息
			this.pushMessage(dto.getUserId(),dto.getCompanyId(),dto.getAccountId(),SystemParameters.MESSAGE_TYPE_NEW_SYSTEM_MANAGER);
		}

		return  new AjaxMessage().setCode("0").setInfo("移交成功");
	}

	/**
	 * 方法描述：移交企业负责人（统一在roleUserService中调用）
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午9:13:48
	 */
	@Override
	public AjaxMessage transferOrgManager(TeamOperaterDTO dto, String newPassword) throws Exception {
        CompanyUserTableDTO orgManager = null;
        if("2".equals(dto.getFlag())){//如果是设置
            //给原来企业负责人发送信息
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("permissionId", "11");//企业负责人
            map.put("companyId", dto.getCompanyId());
            List<CompanyUserTableDTO> companyUserList = this.companyUserDao.getCompanyUserByPermissionId(map);
            if(!CollectionUtils.isEmpty(companyUserList)){
                orgManager = companyUserList.get(0);
            }
        }
		//保存企业负责人权限
		SaveRoleUserDTO saveRoleUserDTO = new SaveRoleUserDTO();
		saveRoleUserDTO.setRoleId(SystemParameters.ORG_MANAGER_ROLE_ID);
		List<String> users = new ArrayList<String>();
		users.add(dto.getUserId());
		saveRoleUserDTO.setUserIds(users);
		saveRoleUserDTO.setCurrentCompanyId(dto.getCompanyId());
		this.roleUserService.saveOrgManager(saveRoleUserDTO);
        //发送短信通知
        if(orgManager!=null){
            CompanyEntity company = this.companyDao.selectById(dto.getCompanyId());
			if(company!=null ){
				String msg = StringUtil.format(SystemParameters.TRANSFER_ORG_MSG, company.getCompanyName(),orgManager.getUserName());
				this.sendMsg(orgManager.getCellphone(),msg);

            }
        }
		//推送消息
		this.pushMessage(dto.getUserId(),dto.getCompanyId(),dto.getAccountId(),SystemParameters.MESSAGE_TYPE_NEW_ORG_MANAGER);
		return AjaxMessage.succeed("设置成功");
	}

	/**
	 * 方法描述：保存系统管理员所以资料（创建企业的时候，给管理员设置系统管理员，并赋予所有权限）
	 * 作者：MaoSF
	 * 日期：2016/11/17
	 */
	@Override
	public AjaxMessage saveSystemManage(TeamOperaterEntity teamOperaterEntity) throws Exception {
		String companyId = teamOperaterEntity.getCompanyId();
		String userId = teamOperaterEntity.getUserId();
		String roleId = SystemParameters.ADMIN_MANAGER_ROLE_ID;
		//保存teamOperaterEntity
		teamOperaterEntity.setId(StringUtil.buildUUID());
		teamOperaterDao.insert(teamOperaterEntity);
		//添加系统管理员角色
//		SaveRoleUserDTO saveRoleUserDTO = new SaveRoleUserDTO();
//		saveRoleUserDTO.setRoleId(roleId);
//		List<String> users = new ArrayList<String>();
//		users.add(teamOperaterEntity.getUserId());
//		saveRoleUserDTO.setUserIds(users);
//		saveRoleUserDTO.setCurrentCompanyId(companyId);
//		this.roleUserService.saveOrUserRole(saveRoleUserDTO);

		//先删除权限
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("companyId",companyId);
		map.put("roleId",roleId);
		map.put("userId",userId);
		this.roleUserDao.deleteUserRole(map);

		//添加系统管理角色
		RoleUserEntity roleUserEntity = new RoleUserEntity();
		String id = StringUtil.buildUUID();
		roleUserEntity.setId(id);
		roleUserEntity.setOrgId(companyId);
		roleUserEntity.setCompanyId(companyId);
		roleUserEntity.setUserId(userId);
		roleUserEntity.setRoleId(roleId);
		this.roleUserDao.insert(roleUserEntity);

		//添加所有权限
		List<PermissionEntity> permissionList = permissionDao.getDefaultPermission();

		for(PermissionEntity rolePermission:permissionList){
			map.clear();
			//先删除已有数据
			List<String> permissionLists = new ArrayList<String>();
			permissionLists.add(rolePermission.getId());
			map.put("permissionList",permissionLists);
			map.put("companyId",companyId);
			map.put("userId",userId);
			userPermissionDao.deleteByUserIdAndPermission(map);

			//添加数据
			UserPermissionEntity userPermission =new UserPermissionEntity();
			userPermission.setPermissionId(rolePermission.getId());
			userPermission.setCompanyId(companyId);
			userPermission.setUserId(userId);
			userPermissionService.saveUserPermission(userPermission);
		}

		return  new AjaxMessage().setCode("0").setInfo("保存成功");
	}

	@Override
	public CompanyUserTableDTO getSystemManager(String companyId) {
		return teamOperaterDao.getSystemManager(companyId);
	}


	/**
	 * 方法描述：  根据（userId，companyId）查询
	 * 作        者：TangY
	 * 日        期：2016年7月12日-上午4:50:13
	 * @return
	 */
	@Override
	public TeamOperaterEntity getTeamOperaterByParam(String companyId,String userId)throws Exception{
		return teamOperaterDao.getTeamOperaterByCompanyId(companyId, userId);
	}

	/**
	 * 修改管理员密码
	 *
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@Override
	public AjaxMessage updateAdminPassword(TeamOperaterDTO dto) throws Exception {
		if(StringUtil.isNullOrEmpty(dto.getAdminPassword())){
			return new AjaxMessage().setCode("1").setInfo("密码不能为空");
		}
		TeamOperaterEntity teamOperaterEntity = this.getTeamOperaterByParam(dto.getCompanyId(),dto.getUserId());
		if(teamOperaterEntity == null){
			return new AjaxMessage().setCode("1").setInfo("操作失败");
		}
		teamOperaterEntity.setAdminPassword(dto.getAdminPassword());
		this.updateById(teamOperaterEntity);
		return new AjaxMessage().setCode("0").setInfo("修改成功");
	}

    /**
     * 方法描述：发送短信【此方法不是接口】
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午8:05:44
     * @param cellphone
     */
    public void sendMsg(String msg,String cellphone){
        Sms sms=new Sms();
        sms.addMobile(cellphone);
        sms.setMsg(msg);
        smsSender.send(sms);
    }

	/**
	 * 方法描述：推送消息【此方法不是接口】
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午8:05:44
	 */
	public void pushMessage(String userId,String companyId,String accountId,Integer messageType){
		//CompanyUserEntity u = companyUserDao.getCompanyUserByUserIdAndCompanyId(accountId,companyId);
		MessageEntity m = new MessageEntity();
		m.setCompanyId(companyId);
		m.setUserId(userId);
		m.setCreateBy(accountId);
		m.setSendCompanyId(companyId);
		m.setMessageType(messageType);
		messageService.sendMessage(m);
	}
}
