package com.maoding.notice.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.notice.dao.NoticeDao;
import com.maoding.notice.dto.NoticeDTO;
import com.maoding.notice.entity.NoticeEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：NoticeDaoImpl
 * 类描述：通知公告DaoImpl
 * 作    者：MaoSF
 * 日    期：2016年11月30日-下午3:10:45
 */
@Service("noticeDao")
public class NoticeDaoImpl extends GenericDao<NoticeEntity> implements NoticeDao {

    @Override
    public List<NoticeDTO> getNoticeByParam(Map<String, Object> param) {
        return this.sqlSession.selectList("GetNoticeByParamMapper.selectByParam", param);
    }


    @Override
    public int getNoticeCountByParam(Map<String, Object> param) {
        return this.sqlSession.selectOne("GetNoticeByParamMapper.selectByParamCount", param);
    }

    /**
     * 方法描述：根据companyId获取公告
     * 作者：MaoSF
     * 日期：2016/12/1
     *
     * @param param
     * @param:
     * @return:
     */
    @Override
    public List<NoticeDTO> getNoticeByCompanyId(Map<String, Object> param) throws Exception {
        return this.sqlSession.selectList("GetNoticeByParamMapper.getNoticeByCompanyId", param);
    }

    /**
     * 方法描述：根据参数查询公告条目数（companyId,userId) 分页查询使用
     * 作者：MaoSF
     * 日期：2016/12/1
     *
     * @param param
     * @param:
     * @return:
     */
    @Override
    public int getNoticeCountByCompanyId(Map<String, Object> param) {
        return this.sqlSession.selectOne("GetNoticeByParamMapper.getNoticeCountByCompanyId", param);
    }

    @Override
    public List<NoticeDTO> getNotReadNotice(Map<String, Object> param) {
        return this.sqlSession.selectList("GetNoticeByParamMapper.getNotReadNotice", param);
    }

    @Override
    public NoticeDTO getNoticeById(String id) {
        return this.sqlSession.selectOne("GetNoticeByParamMapper.getNoticeById", id);
    }


}
