package com.maoding.notice.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.notice.dto.NoticeDTO;
import com.maoding.notice.entity.NoticeEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
public interface NoticeDao extends BaseDao<NoticeEntity> {

    /**
     * 方法描述：根据参数查询公告（companyId,userId) 可以分页查询
     * 作者：MaoSF
     * 日期：2016/12/1
     *
     * @param:
     * @return:
     */
    public List<NoticeDTO> getNoticeByParam(Map<String, Object> param);


    /**
     * 方法描述：根据参数查询公告条目数（companyId,userId) 分页查询使用
     * 作者：MaoSF
     * 日期：2016/12/1
     *
     * @param:
     * @return:
     */
    public int getNoticeCountByParam(Map<String, Object> param);

    /**
     * 方法描述：根据companyId获取公告
     * 作者：MaoSF
     * 日期：2016/12/1
     *
     * @param:
     * @return:
     */
    public List<NoticeDTO> getNoticeByCompanyId(Map<String, Object> param) throws Exception;

    /**
     * 方法描述：根据参数查询公告条目数（companyId,userId) 分页查询使用
     * 作者：MaoSF
     * 日期：2016/12/1
     *
     * @param:
     * @return:
     */
    public int getNoticeCountByCompanyId(Map<String, Object> param);

    /**
     * 获取未读公告
     */
    List<NoticeDTO> getNotReadNotice(Map<String, Object> param);

    NoticeDTO getNoticeById(String id);
}
