package com.maoding.notice.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.notice.dao.NoticeOrgDao;
import com.maoding.notice.dto.NoticeOrgDTO;
import com.maoding.notice.entity.NoticeOrgEntity;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：NoticeOrgDaoImpl
 * 类描述：通知公告-组织DaoImpl
 * 作    者：MaoSF
 * 日    期：2016年11月30日-下午3:10:45
 */
@Service("noticeOrgDao")
public class NoticeOrgDaoImpl extends GenericDao<NoticeOrgEntity> implements NoticeOrgDao {


    /**
     * 方法描述：根据公告id删除关系
     * 作者：MaoSF
     * 日期：2016/11/30
     *
     * @param noticeId
     * @param:
     * @return:
     */
    @Override
    public int deleteByNoticeId(String noticeId) {
        return this.sqlSession.delete("NoticeOrgEntityMapper.deleteByNoticeId",noticeId);
    }

    @Override
    public List<NoticeOrgDTO> getByNoticeId(String noticeId) {
        return this.sqlSession.selectList("GetNoticeByParamMapper.getNoticeOrgByNoticeId",noticeId);
    }
}
