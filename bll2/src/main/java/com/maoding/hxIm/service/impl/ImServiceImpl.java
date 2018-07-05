package com.maoding.hxIm.service.impl;

import com.beust.jcommander.internal.Lists;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.StringUtil;
import com.maoding.hxIm.constDefine.ImAccountStatus;
import com.maoding.hxIm.constDefine.ImGroupStatus;
import com.maoding.hxIm.constDefine.ImGroupType;
import com.maoding.hxIm.dao.ImAccountDao;
import com.maoding.hxIm.dao.ImGroupDao;
import com.maoding.hxIm.dto.ImAccountDTO;
import com.maoding.hxIm.dto.ImGroupMemberDTO;
import com.maoding.hxIm.entity.ImAccountEntity;
import com.maoding.hxIm.entity.ImGroupEntity;
import com.maoding.hxIm.service.ImService;
import com.maoding.hxIm.service.ImQueueProducer;
import com.maoding.hxIm.dto.ImGroupDTO;
import com.maoding.message.dto.SendMessageDataDTO;
import com.maoding.notice.constDefine.NotifyDestination;
import com.maoding.notice.service.NoticeService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.entity.CompanyUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by sandy on 2017/8/7.
 */
@Service("imService")
public class ImServiceImpl extends NewBaseService implements ImService {

    @Autowired
    private ImQueueProducer imQueueProducer;

    @Autowired
    private ImAccountDao imAccountDao;

    @Autowired
    private ImGroupDao imGroupDao;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private CompanyUserDao companyUserDao;

    /**
     * 方法描述：创建群组
     * 作者：MaoSF
     * 日期：2016/11/29
     */
    public void createImGroup(String groupId, String admin, String companyName,Integer groupType) throws Exception{
        if(groupType==null || groupType== ImGroupType.DEPARTMENT){
            return;
        }
        ImGroupDTO dto = new ImGroupDTO();
        dto.setGroupOwner(admin);
        dto.setGroupName(companyName);
        dto.setOrgId(groupId);
        dto.setGroupType(groupType);
        dto.setGroupId(groupId);//由于orgId在im_group中是唯一的，所有用orgId设置为id，方便查找,
        insertImGroup(dto);
        imQueueProducer.group_create(dto);
        notify("你加入了"+companyDao.getCompanyName(groupId),Lists.newArrayList(admin));

    }

    /**
     * 方法描述：修改群组
     * 作者：MaoSF
     * 日期：2016/11/29
     */
    public void updateImGroup(String groupId, String companyName,Integer groupType) throws Exception{
        ImGroupDTO dto = new ImGroupDTO();
        dto.setGroupName(companyName);
        dto.setOrgId(groupId);
        dto.setGroupType(groupType);
        dto.setGroupId(groupId);//由于orgId在im_group中是唯一的，所有用orgId设置为id，方便查找,
        imQueueProducer.group_modifyGroupName(dto);
    }

    /**
     * 方法描述：删除群组
     * 作者：MaoSF
     * 日期：2016/11/29
     */
    public void deleteImGroup(String groupId,int groupType) throws Exception{
        ImGroupDTO dto = new ImGroupDTO();
        dto.setOrgId(groupId);
        dto.setGroupType(groupType);
        dto.setGroupId(groupId);//由于orgId在im_group中是唯一的，所有用orgId设置为id，方便查找,
        imQueueProducer.group_delete(dto);

        if(groupType==0){//解散组织
            List<CompanyUserEntity> userList = this.companyUserDao.getCompanyUserByCompanyId(groupId);
            List<String> userIds = new ArrayList<>();
            for(CompanyUserEntity u:userList){
                userIds.add(u.getUserId());
            }
            notify("你退出了"+companyDao.getCompanyName(groupId),userIds);
        }
    }

    @Override
    public void addMembers(String groupId, String userId) throws Exception {
        ImGroupDTO dto = new ImGroupDTO();
        dto.setOrgId(groupId);
        dto.setMembers(Arrays.asList(new ImGroupMemberDTO(userId,null)));
        dto.setGroupId(groupId);//由于orgId在im_group中是唯一的，所有用orgId设置为id，方便查找,
        imQueueProducer.group_addMembers(dto);

        //发送个推
        notify("你加入了"+companyDao.getCompanyName(groupId),Lists.newArrayList(userId));
    }

    @Override
    public void deleteMembers(String groupId, String userId) throws Exception {
        ImGroupDTO dto = new ImGroupDTO();
        dto.setOrgId(groupId);
        dto.setMembers(Arrays.asList(new ImGroupMemberDTO(userId,null)));
        dto.setGroupId(groupId);//由于orgId在im_group中是唯一的，所有用orgId设置为id，方便查找,
        imQueueProducer.group_deleteMembers(dto);
        //推送给web端
        notify("你退出了"+companyDao.getCompanyName(groupId),Lists.newArrayList(userId));
    }

    private void notify(String content,List<String> userIds){
        SendMessageDataDTO notifyMsg = new SendMessageDataDTO();
        notifyMsg.setMessageType(SystemParameters.ORG_TYPE);
        notifyMsg.setReceiverList(userIds);
        notifyMsg.setContent(content);
        this.noticeService.notify(NotifyDestination.APP, notifyMsg);
    }

    @Override
    public void createImAccount(String userId, String userName, String password) throws Exception {
        ImAccountDTO dto = new ImAccountDTO();
        dto.setAccountId(userId);
        dto.setAccountName(userName);
        dto.setPassword(password);
        insertImAccount(dto);
        imQueueProducer.account_create(dto);
    }

    @Override
    public void updateImAccount(String userId, String password) throws Exception {
//        ImAccountDTO dto = new ImAccountDTO();
//        dto.setAccountId(userId);
//        dto.setPassword(password);
//        imQueueProducer.account_modifyPassword(dto);
    }


    /**
     * 創建用戶，插入imAccount
     **/
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void insertImAccount(ImAccountDTO dto) throws Exception {
        if(imAccountDao.selectById(dto.getAccountId())!=null){
            return;
        }
        ImAccountEntity imAccount = new ImAccountEntity();
        BaseDTO.copyFields(dto, imAccount);
        imAccount.initEntity();
        imAccount.setId(dto.getAccountId());
        imAccount.setAccountStatus(ImAccountStatus.WAIT_CREATE);
        imAccount.setUpVersion(0L);
        imAccount.setDeleted(false);
        imAccount.setLastQueueNo(0L);
        imAccountDao.insert(imAccount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void insertImAccountBatch(List<ImAccountDTO> list) throws Exception {
        for(ImAccountDTO dto:list){
            this.insertImAccount(dto);
        }
    }

    /**
     * 插入imGroup
     **/
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void insertImGroup(ImGroupDTO dto) throws Exception {
        //先查询在插入
        if(imGroupDao.selectById(dto.getGroupId())!=null) {
            return;
        }
        ImGroupEntity imGroup = new ImGroupEntity();
        BaseDTO.copyFields(dto, imGroup);
        imGroup.initEntity();
        imGroup.setId(dto.getGroupId());
        if (StringUtil.isNullOrEmpty(dto.getGroupStatus())) {
            imGroup.setGroupStatus(ImGroupStatus.WAIT_CREATE);
        }
        imGroup.setUpVersion(0L);
        imGroup.setDeleted(false);
        imGroup.setLastQueueNo(0L);
        imGroup.setCreateDate(new Date());
        imGroupDao.insert(imGroup);
    }
}
