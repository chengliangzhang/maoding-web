package com.maoding.notice.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.notice.dao.NoticeReadDao;
import com.maoding.notice.entity.NoticeReadEntity;
import org.springframework.stereotype.Service;

/**
 * Created by sandy on 2017/10/9.
 */
@Service("noticeReadDao")
public class NoticeReadDaoImpl extends GenericDao<NoticeReadEntity> implements NoticeReadDao {
}
