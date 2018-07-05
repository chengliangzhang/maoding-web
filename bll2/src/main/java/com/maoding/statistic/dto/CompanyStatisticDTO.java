package com.maoding.statistic.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyStatisticDTO
 * 类描述：系统人员信息行为跟踪统计DTO
 * 作    者：MaoSF
 * 日    期：2016年7月6日-下午5:38:15
 */
public class CompanyStatisticDTO extends BaseDTO implements java.io.Serializable{

    /**
     * 公司名称
     */
    private String companyName;


    /**
     * 公司总人数
     */
    private int userCount;

    /**
     * 已激活人数
     */
    private int activeUserCount;

    /**
     * 未激活人数
     */
    private int notActiveUserCount;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getActiveUserCount() {
        return activeUserCount;
    }

    public void setActiveUserCount(int activeUserCount) {
        this.activeUserCount = activeUserCount;
    }

    public int getNotActiveUserCount() {
        return notActiveUserCount;
    }

    public void setNotActiveUserCount(int notActiveUserCount) {
        this.notActiveUserCount = notActiveUserCount;
    }
}