package com.maoding.message.entity;

import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.StringUtil;

import java.sql.Date;

/**
 * Created by Chengliang.zhang on 2017/4/14.
 */
public class NotifyEntity extends BaseEntity{
    private String companyId;
    private Date toDate;

    @Override
    public String getId() {
        if (super.getId() == null){
            super.setId(StringUtil.buildUUID());
        }
        return super.getId();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
