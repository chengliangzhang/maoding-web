package com.maoding.hxIm.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.util.JsonUtils;
import com.maoding.core.util.RedissonUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.hxIm.constDefine.ImDestination;
import com.maoding.hxIm.constDefine.ImOperation;
import com.maoding.hxIm.dao.ImQueueDao;
import com.maoding.hxIm.dto.ImAccountDTO;
import com.maoding.hxIm.dto.ImGroupDTO;
import com.maoding.hxIm.dto.ImQueueDTO;
import com.maoding.hxIm.dto.ImSendMessageDTO;
import com.maoding.hxIm.entity.ImQueueEntity;
import com.maoding.hxIm.service.ImQueueProducer;
import org.redisson.api.RAtomicLong;
import org.redisson.client.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("imQueueProducer")
public class ImQueueProducerImpl extends NewBaseService implements ImQueueProducer {

    private final static Logger logger = LoggerFactory.getLogger(ImQueueProducerImpl.class);

    @Autowired
    @Qualifier("imJmsTemplate")
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("redissonUtils_session")
    private RedissonUtils redissonClient;

    @Autowired
    private ImQueueDao imQueueDao;



    /**
     * 生成队列号
     */
    private long generateQueueNo() {
        long id = -1L;
        RAtomicLong queueNo = null;
        for (int i = 1; i <= 3; i++) {
            try {
                queueNo = redissonClient.getAtomicLong("IM_QUEUE_NO");
                id = queueNo.incrementAndGet();
            } catch (Exception ex) {
                logger.error("获取 IM_QUEUE_NO 失败", ex);
            }

            if (Long.compare(id, -1L) == 0) {
                if (i == 3) {
                    throw new RedisException("获取 IM_QUEUE_NO 失败");
                } else {
                    try {
                        TimeUnit.MILLISECONDS.sleep(300 * i);
                        logger.info("获取 IM_QUEUE_NO 失败，开始重试第 {} 次", i);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                }
            } else {
                i = 99;
            }
        }

        return id;
    }

    private ImQueueDTO getQueueByAccount(ImAccountDTO dto) throws Exception {
        ImQueueDTO queueDTO = new ImQueueDTO();
        queueDTO.setTargetId(dto.getAccountId());
        queueDTO.setQueueNo(generateQueueNo());
        queueDTO.setRetry(0);
        queueDTO.setContent(JsonUtils.obj2json(dto));
        return queueDTO;
    }

    private ImQueueDTO getQueueByGroup(ImGroupDTO dto) throws Exception {
        ImQueueDTO queueDTO = new ImQueueDTO();
        queueDTO.setTargetId(dto.getGroupId());
        queueDTO.setQueueNo(generateQueueNo());
        queueDTO.setRetry(0);
        queueDTO.setContent(JsonUtils.obj2json(dto));
        return queueDTO;
    }

    /**
     * 消息队列异常，插入到队列表中
     **/
    private void insertErrorQueue(ImQueueDTO queueDTO) throws Exception{
        ImQueueEntity queue = new ImQueueEntity();
        BaseDTO.copyFields(queueDTO,queue);
        queue.initEntity();
        queue.setQueueStatus(0);
        queue.setDeleted(false);
        if(StringUtil.isNullOrEmpty(queue.getUpVersion())){
            queue.setUpVersion(0L);
        }
        imQueueDao.insert(queue);
    }

    private ImQueueDTO getQueueBySendMessage(ImSendMessageDTO dto) throws Exception {
        ImQueueDTO queueDTO = new ImQueueDTO();
        queueDTO.setTargetId(null);
        queueDTO.setQueueNo(generateQueueNo());
        queueDTO.setRetry(0);
        queueDTO.setContent(JsonUtils.obj2json(dto));
        return queueDTO;
    }


    /**
     * 创建环信账号
     **/
    @Override
    public void account_create(ImAccountDTO dto) throws Exception {
        ImQueueDTO queueDTO = getQueueByAccount(dto);
        queueDTO.setOperation(ImOperation.ACCOUNT_CREATE);
        try {
            jmsTemplate.convertAndSend(ImDestination.ACCOUNT, queueDTO);
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

    /**
     * 批量创建环信账号
     **/
    @Override
    public void account_createBatch(List<ImAccountDTO> list) throws Exception {
        ImQueueDTO queueDTO = new ImQueueDTO();
        queueDTO.setTargetId(null);
        queueDTO.setQueueNo(generateQueueNo());
        queueDTO.setRetry(0);
        queueDTO.setContent(JsonUtils.obj2json(list));
        queueDTO.setOperation(ImOperation.ACCOUNT_CREATE_BATCH);
        try {
            jmsTemplate.convertAndSend(ImDestination.ACCOUNT, queueDTO);
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

    /**
     * 修改环信昵称
     **/
    @Override
    public void account_modifyNickname(ImAccountDTO dto) throws Exception {
        ImQueueDTO queueDTO = getQueueByAccount(dto);
        queueDTO.setOperation(ImOperation.ACCOUNT_MODIFY_NICKNAME);
        try {
            jmsTemplate.convertAndSend(ImDestination.ACCOUNT, queueDTO);
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

    /**
     * 重置环信密码
     **/
    @Override
    public void account_modifyPassword(ImAccountDTO dto) throws Exception {
        ImQueueDTO queueDTO = getQueueByAccount(dto);
        queueDTO.setOperation(ImOperation.ACCOUNT_MODIFY_PASSWORD);
        try {
            jmsTemplate.convertAndSend(ImDestination.ACCOUNT, queueDTO);
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

    /**
     * 删除环信账号
     **/
    @Override
    public void account_delete(ImAccountDTO dto) throws Exception {
        ImQueueDTO queueDTO = getQueueByAccount(dto);
        queueDTO.setOperation(ImOperation.ACCOUNT_DELETE);
        try {
            jmsTemplate.convertAndSend(ImDestination.ACCOUNT, queueDTO);
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

    /********************************************************************************************/

    /**
     * 发送IM消息
     **/
    @Override
    public void sendMessage(ImSendMessageDTO dto) throws Exception {
        ImQueueDTO queueDTO = getQueueBySendMessage(dto);
        queueDTO.setOperation(ImOperation.SEND_MESSAGE);
        try {
            jmsTemplate.convertAndSend(ImDestination.SEND_MESSAGE,queueDTO); // JsonUtils.obj2json(queueDTO)
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

    /**
     * 创建环信群组
     **/
    @Override
    public void group_create(ImGroupDTO dto) throws Exception {
        ImQueueDTO queueDTO = getQueueByGroup(dto);
        queueDTO.setOperation(ImOperation.GROUP_CREATE);
        try {
            jmsTemplate.convertAndSend(ImDestination.GROUP, queueDTO);
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

    /**
     * 删除群
     */
    @Override
    public void group_delete(ImGroupDTO dto) throws Exception {
        ImQueueDTO queueDTO = getQueueByGroup(dto);
        queueDTO.setOperation(ImOperation.GROUP_DELETE);
        try {
            jmsTemplate.convertAndSend(ImDestination.GROUP, queueDTO);
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

    /**
     * 修改环信群组名
     **/
    @Override
    public void group_modifyGroupName(ImGroupDTO dto) throws Exception {
        ImQueueDTO queueDTO = getQueueByGroup(dto);
        queueDTO.setOperation(ImOperation.GROUP_MODIFY_GROUP_NAME);
        try {
            jmsTemplate.convertAndSend(ImDestination.GROUP, queueDTO);
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

    /**
     * 移交环信群主
     **/
    @Override
    public void group_transferGroupOwner(ImGroupDTO dto) throws Exception {
        ImQueueDTO queueDTO = getQueueByGroup(dto);
        queueDTO.setOperation(ImOperation.GROUP_TRANSFER_GROUP_OWNER);
        try {
            jmsTemplate.convertAndSend(ImDestination.GROUP, queueDTO);
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

    /**
     * 增加群成员
     **/
    @Override
    public void group_addMembers(ImGroupDTO dto) throws Exception {
        ImQueueDTO queueDTO = getQueueByGroup(dto);
        queueDTO.setOperation(ImOperation.GROUP_MEMBER_ADD);
        try {
            jmsTemplate.convertAndSend(ImDestination.GROUP_MEMBER, queueDTO);
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

    /**
     * 移除群成员
     **/
    @Override
    public void group_deleteMembers(ImGroupDTO dto) throws Exception {
        ImQueueDTO queueDTO = getQueueByGroup(dto);
        queueDTO.setOperation(ImOperation.GROUP_MEMBER_DELETE);
        try {
            jmsTemplate.convertAndSend(ImDestination.GROUP_MEMBER, queueDTO);
        } catch (JmsException ex) {
            insertErrorQueue(queueDTO);
        }
    }

}
