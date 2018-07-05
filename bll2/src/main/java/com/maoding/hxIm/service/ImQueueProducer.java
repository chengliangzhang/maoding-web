package com.maoding.hxIm.service;

import com.maoding.hxIm.dto.ImAccountDTO;
import com.maoding.hxIm.dto.ImGroupDTO;
import com.maoding.hxIm.dto.ImQueueDTO;
import com.maoding.hxIm.dto.ImSendMessageDTO;

import java.util.List;

public interface ImQueueProducer {



    /** 创建环信账号 **/
    void account_create(ImAccountDTO dto) throws Exception;

    /** 批量创建环信账号 **/
    void account_createBatch(List<ImAccountDTO> list) throws Exception;

    /** 修改环信昵称 **/
    void account_modifyNickname(ImAccountDTO dto) throws Exception;

    /** 重置环信密码 **/
    void account_modifyPassword(ImAccountDTO dto) throws Exception;

    /** 删除环信账号 **/
    void account_delete(ImAccountDTO dto) throws Exception;

    /*************************************************************/

    /** 发送IM消息 **/
    void sendMessage(ImSendMessageDTO dto) throws Exception;

    /** 创建环信群组 **/
    void group_create(ImGroupDTO dto) throws Exception;

    /** 删除群 */
    void group_delete(ImGroupDTO dto) throws Exception;

    /** 修改环信群组名 **/
    void group_modifyGroupName(ImGroupDTO dto) throws Exception;

    /** 移交环信群主 **/
    void group_transferGroupOwner(ImGroupDTO dto) throws Exception;

    /** 增加群成员 **/
    void group_addMembers(ImGroupDTO dto) throws Exception;

    /** 移除群成员 **/
    void group_deleteMembers(ImGroupDTO dto) throws Exception;
}
