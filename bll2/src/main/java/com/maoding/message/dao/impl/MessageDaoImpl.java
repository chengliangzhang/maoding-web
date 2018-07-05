package com.maoding.message.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.message.dao.MessageDao;
import com.maoding.message.dto.MessageDTO;
import com.maoding.message.dto.QueryMessageDTO;
import com.maoding.message.entity.MessageEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
@Service("messageDao")
public class MessageDaoImpl extends GenericDao<MessageEntity> implements MessageDao {

    /**
     * 方法描述：获取消息
     * 作者：MaoSF
     * 日期：2017/3/17
     *
     * @param map
     * @param:
     * @return:
     */
    @Override
    public List<MessageDTO> getMessage(Map<String, Object> map) {
        return this.sqlSession.selectList("GetMessageMapper.getMessage",map);
    }

    @Override
    public List<MessageEntity> selectByParam(QueryMessageDTO query) {
        return  this.sqlSession.selectList("MessageEntityMapper.selectByParam",query);
    }

    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2017/3/18
     *
     * @param map
     * @param:
     * @return:
     */
    @Override
    public int getMessageCount(Map<String, Object> map) {
        return this.sqlSession.selectOne("GetMessageMapper.getMessageCount",map);
    }

    @Override
    public int updateRead(String userId) {
        return this.sqlSession.update("MessageEntityMapper.updateRead",userId);
    }

    /**
     * 方法描述：根据关键字删除（逻辑删除）
     * 作者：MaoSF
     * 日期：2017/3/25
     *
     * @param field
     * @param:
     * @return:
     */
    @Override
    public int deleteMessage(String field) {
        //此处使用update逻辑删除
        return this.sqlSession.update("MessageEntityMapper.deleteMessage",field);
    }
}
