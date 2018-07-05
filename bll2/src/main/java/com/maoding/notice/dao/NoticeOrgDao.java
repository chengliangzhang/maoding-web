package com.maoding.notice.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.notice.dto.NoticeOrgDTO;
import com.maoding.notice.entity.NoticeOrgEntity;

import java.util.List;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
public interface NoticeOrgDao extends BaseDao<NoticeOrgEntity> {

    /**
     * 方法描述：根据公告id删除关系
     * 作者：MaoSF
     * 日期：2016/11/30
     * @param:
     * @return:
     */
    public int deleteByNoticeId(String noticeId);

    /**
     * 方法描述：根据公告id查询发送的组织
     * 作者：MaoSF
     * 日期：2016/12/1
     * @param:
     * @return:
     */
    public List<NoticeOrgDTO> getByNoticeId(String noticeId);
    
    
}
