package com.maoding.role.service.impl;

import com.beust.jcommander.internal.Lists;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.StringUtil;
import com.maoding.message.dto.SendMessageDataDTO;
import com.maoding.message.entity.MessageEntity;
import com.maoding.message.service.MessageService;
import com.maoding.notice.constDefine.NotifyDestination;
import com.maoding.notice.service.NoticeService;
import com.maoding.role.dao.RoleUserDao;
import com.maoding.role.dao.UserPermissionDao;
import com.maoding.role.dto.SaveUserPermission2DTO;
import com.maoding.role.dto.SaveUserPermissionDTO;
import com.maoding.role.entity.UserPermissionEntity;
import com.maoding.role.service.UserPermissionService;
import com.maoding.shiro.realm.UserRealm;
import com.maoding.user.dto.AccountDTO;
import com.maoding.user.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：UserPermissionService
 * 类描述：前台角色权限表（dao）
 * 作    者：wrb
 * 日    期：2016年11月2日-上午11:38:47
 */
@Service("userPermissionService")
public class UserPermissionServiceImpl extends GenericService<UserPermissionEntity> implements UserPermissionService {
    protected final Logger log= LoggerFactory.getLogger(getClass());

    @Autowired
    private UserPermissionDao userPermissionDao;

    @Autowired
    private RoleUserDao roleUserDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserRealm userRealm;

    @Autowired
    private AccountService accountService;

    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2017/6/13
     */
    @Override
    public void saveUserPermission(UserPermissionEntity entity) {
        entity.setSeq(userPermissionDao.getMaxSeq(entity.getCompanyId(),entity.getPermissionId()));
        entity.setId(StringUtil.buildUUID());
        userPermissionDao.insert(entity);
    }

    /**
     * 方法描述：保存用户权限关联数据
     * 作者：MaoSF
     * 日期：2016/11/2
     */
    @Override
    public AjaxMessage saveUserPermission(SaveUserPermissionDTO dto) throws Exception {

        String companyId=dto.getCurrentCompanyId();
        String createBy=dto.getAccountId();

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("userId", dto.getUserId());
        map.put("companyId", companyId);
        this.userPermissionDao.deleteByUserId(map);//先删除权限与用户之间的关系

        for(int i=0;i<dto.getPermissionIds().size();i++){
            UserPermissionEntity userPermission =new UserPermissionEntity();
            userPermission.setPermissionId(dto.getPermissionIds().get(i));
            userPermission.setCompanyId(companyId);
            userPermission.setUserId(dto.getUserId());
            userPermission.setCreateBy(createBy);
            this.saveUserPermission(userPermission);
        }

        return new AjaxMessage().setCode("0").setInfo("保存成功");
    }

    /**
     * 方法描述：保存用户权限关联数据（从权限中选择人员）
     * 作者：MaoSF
     * 日期：2016/11/2
     */
    @Override
    public AjaxMessage saveUserPermission2(SaveUserPermission2DTO dto) throws Exception {
        String companyId=dto.getCurrentCompanyId();
        String createBy=dto.getAccountId();
        List<String> listStr = dto.getDeleteUserIds();

        for(String str : listStr){
            Map<String,Object> map = new HashMap<>();
            map.put("permissionId",dto.getPermissionId());
            map.put("companyId",companyId);
            map.put("userId",str);
            this.userPermissionDao.deleteByPermissionId(map);
        }
        for(int i=0;i<dto.getUserIds().size();i++){
            UserPermissionEntity userPermission =new UserPermissionEntity();
            userPermission.setPermissionId(dto.getPermissionId());
            userPermission.setCompanyId(companyId);
            userPermission.setUserId(dto.getUserIds().get(i));
            userPermission.setCreateBy(createBy);
            saveUserPermission(userPermission);
        }


        this.sendMessageForRole(dto.getUserIds(),companyId,createBy);
        //并且更新缓存里的权限

        return new AjaxMessage().setCode("0").setInfo("保存成功");
    }

    /**
     * 方法描述：删除用户权限关联数据（从权限中删除人员）
     * 作者：MaoSF
     * 日期：2016/11/2
     */
    @Override
    public AjaxMessage deleteUserPermission2(Map<String,Object> map) throws Exception {
        this.userPermissionDao.deleteByUserIdAndPermission(map);
        //推送消息
        List<String> userIds = Lists.newArrayList();
        userIds.add((String)map.get("userId"));
        this.sendMessageForRole(userIds,(String)map.get("companyId"),(String)map.get("accountId"));
        return new AjaxMessage().setCode("0").setInfo("删除成功");
    }

    /**
     * 方法描述：批量删除用户权限关联数据
     * 作者：MaoSF
     * 日期：2016/11/2
     */
    @Override
    public AjaxMessage deleteUserPermissionOfBatch(SaveUserPermissionDTO dto) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        if(!CollectionUtils.isEmpty(dto.getPermissionIds())){
            map.put("userId",dto.getUserId());
            map.put("companyId",dto.getCurrentCompanyId());
            map.put("permissionList",dto.getPermissionIds());
            this.userPermissionDao.deleteByUserIdAndPermission(map);

            if (!CollectionUtils.isEmpty(dto.getRoleIds())){
                map.put("roleIds",dto.getRoleIds());
                this.roleUserDao.deleteUserRole(map);
            }
            return new AjaxMessage().setCode("0").setInfo("删除成功");
        }
        return new AjaxMessage().setCode("1").setInfo("删除失败");
    }


    /**
     * 方法描述：发送消息
     * 作者：MaoSF
     * 日期：2016/12/8
     */
    public void sendMessageForRole(List<String> userIdList, String companyId,String accountId) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("msg", "Operator");
//        map.put("receiverList", userIdList);
//        map.put("messageType", SystemParameters.ROLE_TYPE);
//        map.put("noticeTitle","Permission");
//        noticeService.notify(NotifyDestination.APP, map);//推送给app
        SendMessageDataDTO notifyMsg = new SendMessageDataDTO();
        notifyMsg.setMessageType(SystemParameters.ROLE_TYPE);
        notifyMsg.setReceiverList(userIdList);
        notifyMsg.setContent("Operator");
        noticeService.notify(NotifyDestination.APP, notifyMsg);//推送给app

        //推送消息
        pushMessage(userIdList, companyId, accountId);

        //强制更新权限缓存
        refreshShiroRole(userIdList);
    }

    public void refreshShiroRole(List<String> userIds) {
        try{
            for(String userId:userIds) {
                AccountDTO dto = accountService.getAccountById(userId);
                if(dto!=null){
                    userRealm.resetRole(dto);
                }
            }
        }catch (Exception e){
            log.error("refreshShiroRole 更新个人权限缓存失败");
        }

    }

    /**
     * 方法描述：推送消息【此方法不是接口】
     * 作        者：MaoSF
     * 日        期：2016年7月11日-下午8:05:44
     */
    public void pushMessage(List<String> userIds,String companyId,String accountId){
        for(String userId:userIds){
            MessageEntity m = new MessageEntity();
            m.setCompanyId(companyId);
            m.setUserId(userId);
            m.setCreateBy(accountId);
            m.setSendCompanyId(companyId);
            m.setMessageType(SystemParameters.MESSAGE_TYPE_ROLE_CHANGE);
            messageService.sendMessage(m);
        }
    }
}