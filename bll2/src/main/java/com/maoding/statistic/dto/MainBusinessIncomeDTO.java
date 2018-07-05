package com.maoding.statistic.dto;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/16 16:45
 * 描    述 : 主营业务收入
 */
public class MainBusinessIncomeDTO {
    String total[]; //合计
    String contract[];//合同回款
    String technology[];//技术审查费
    String cooperation[];//合作设计费
    String other[];//其他收入

    public String[] getTotal() {
        return total;
    }

    public void setTotal(String[] total) {
        this.total = total;
    }

    public String[] getContract() {
        return contract;
    }

    public void setContract(String[] contract) {
        this.contract = contract;
    }

    public String[] getTechnology() {
        return technology;
    }

    public void setTechnology(String[] technology) {
        this.technology = technology;
    }

    public String[] getCooperation() {
        return cooperation;
    }

    public void setCooperation(String[] cooperation) {
        this.cooperation = cooperation;
    }

    public String[] getOther() {
        return other;
    }

    public void setOther(String[] other) {
        this.other = other;
    }
}
