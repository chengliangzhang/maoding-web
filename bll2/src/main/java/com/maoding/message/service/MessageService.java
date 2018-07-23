package com.maoding.message.service;

import com.maoding.core.base.dto.BaseShowDTO;
import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.message.dto.QueryMessageDTO;
import com.maoding.message.dto.SendMessageDTO;
import com.maoding.message.entity.MessageEntity;
import com.maoding.project.dto.DeliverEditDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp21 on 2016/10/20.
 */
public interface MessageService extends BaseService<MessageEntity> {

    /**
     * 发送消息
     */
    AjaxMessage sendMessage(MessageEntity messageEntity);
    AjaxMessage sendMessage(List<MessageEntity> messageList);

    void sendMessage(SendMessageDTO messageDTO);
    void sendMessage(List<SendMessageDTO> messageList, Class<?> messageClass);

    /**
     * 设校审完成消息
     * @param messageDTO
     */
    void sendMessageForProcess(SendMessageDTO messageDTO);
    /**
     * 任务负责人，设计负责人
     */
    void sendMessageForDesigner(SendMessageDTO messageDTO);

    void sendMessageForProjectManager(SendMessageDTO messageDTO);

    void sendMessageForDesignManager(SendMessageDTO messageDTO);

    /**
     * 推送给抄送人
     */
    void sendMessageForCopy(SendMessageDTO messageDTO);

    /**
     * 通用的发送消息方法
     * @return: 发送失败原因，如果成功，返回null
     */
    <T> String sendMessage(T origin,T target,List<String> toUserIdList,String projectId,String companyId,String userId);

    /**
     * 方法描述：发送消息
     * 作者：MaoSF
     * 日期：2017/6/8
     */
    AjaxMessage sendMessage(String projectId,String companyId,String targetId,int messageType,String paramId,String userId,String accountId,String content);

    /**
     * 方法描述：获取消息
     * 作者：MaoSF
     * 日期：2017/3/17
     */
    AjaxMessage getMessage(Map<String,Object> map) throws Exception;

    /**
     * 方法描述：获取消息
     * 作者：MaoSF
     * 日期：2017/3/17
     */
    int getMessageCount(Map<String,Object> map);

    /**
     * 方法描述：根据关键字删除
     * 作者：MaoSF
     * 日期：2017/3/25
     */
    int deleteMessage(String field) throws Exception;

    List<MessageEntity> getMessageByParam(QueryMessageDTO dto);

    /**
     * @author  张成亮
     * @date    2018/7/17
     * @description     创建交付信息更改消息队列
     * @param   request 交付申请
     * @return  消息队列
     **/
    List<MessageEntity> createDeliverChangedMessageListFrom(DeliverEditDTO request, List<BaseShowDTO> receiverList, int messageType);


    void initOldData();

}
