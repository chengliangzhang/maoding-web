package com.maoding.message.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.message.entity.NotifyEntity;

import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/4/14.
 */
public interface UpdateNotifyService extends BaseService<NotifyEntity> {
    AjaxMessage createNotify(Date toDate);
    AjaxMessage getNotify(String companyId,String userId);
    AjaxMessage completeNotify(String companyId);
}
