package com.maoding.message.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.DateUtils;
import com.maoding.message.dao.NotifyDao;
import com.maoding.message.entity.NotifyEntity;
import com.maoding.message.service.UpdateNotifyService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.TeamOperaterDao;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.TeamOperaterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/4/14.
 */
@Service("updateNotifyService")
public class UpdateNotifyServiceImpl extends GenericService<NotifyEntity> implements UpdateNotifyService {
    @Autowired
    NotifyDao notifyDao;

    @Autowired
    CompanyDao companyDao;
    @Autowired
    private TeamOperaterDao teamOperaterDao;

    @Override
    public AjaxMessage createNotify(Date toDate) {
        List<CompanyEntity> list = companyDao.selectAll();
        if (list == null) return AjaxMessage.failed("没有公司信息");
        for (CompanyEntity c : list){
            NotifyEntity e = new NotifyEntity();
            e.setCompanyId(c.getId());
            e.setToDate(new java.sql.Date(toDate.getTime()));
            notifyDao.insert(e);
        }
        return AjaxMessage.succeed("数据导入完毕");
    }

    @Override
    public AjaxMessage getNotify(String companyId,String userId) {
        NotifyEntity n = notifyDao.getNotifyByCompanyId(companyId);
        if (n == null) return AjaxMessage.succeed(new Integer(0));
        Date toDate = new Date(n.getToDate().getTime());
        Date curDate = new Date();
        if (DateUtils.compare_date(curDate,toDate) > 0) return AjaxMessage.succeed(new Integer(0));
        TeamOperaterEntity e = teamOperaterDao.getTeamOperaterByCompanyId(companyId,userId);
        if (e == null) return AjaxMessage.succeed(new Integer(0));
        return AjaxMessage.succeed(new Integer(1));
    }

    @Override
    public AjaxMessage completeNotify(String companyId) {
        notifyDao.deleteByCompanyId(companyId);
        return AjaxMessage.succeed("本公司不再提醒");
    }
}
