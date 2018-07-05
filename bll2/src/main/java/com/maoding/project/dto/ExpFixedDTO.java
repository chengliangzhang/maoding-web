package com.maoding.project.dto;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/2/23 11:45
 * 描    述 :费用录入导入
 */
public class ExpFixedDTO {
    private String dateTime;
    private String gdfySalestaxTax;//主营业务税金及附加
    private String gdfyDirectcostsSalay;//投入在项目上的员工工资
    private String gdfyDirectcostsFund;//投入在项目上的五险一金
    private String gdfyDirectcostsBonus;//投入在项目上的员工奖金
    private String gdfyExecutivesalarySalay;//管理行政人员的工资
    private String gdfyExecutivesalaryFund;//管理行政人员的五险一金
    private String gdfyExecutivesalaryBonus;//管理行政人员的奖金
    private String gdfyFwsalaryBgcd;//办公场地房租
    private String gdfyFwsalaryWg;//物管费用
    private String gdfyFwsalarySd;//水电费用
    private String gdfyFwsalaryNet;//网络、电话
    private String gdfyFwsalaryGbwh;//办公场地维护费用
    private String gdfyAssetsamortizationBgzx;//办公室装修摊销
    private String gdfyAssetsamortizationBgsb;//办公室设备摊销
    private String gdfyAssetsamortizationRjtx;//软件摊销
    private String gdfyAssetsamortizationBdczj;//不动产折旧
    private String gdfyZcjzzbHzzb;//应收账款坏账准备
    private String gdfyCwfySxf;//银行手续费
    private String gdfyCwfyLx;//银行利息收入/支出
    private String gdfySdsfySds;//减：所得税费用
    /**
     * 导入错误原因
     */
    String errorReason;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getGdfySalestaxTax() {
        return gdfySalestaxTax;
    }

    public void setGdfySalestaxTax(String gdfySalestaxTax) {
        this.gdfySalestaxTax = gdfySalestaxTax;
    }

    public String getGdfyDirectcostsSalay() {
        return gdfyDirectcostsSalay;
    }

    public void setGdfyDirectcostsSalay(String gdfyDirectcostsSalay) {
        this.gdfyDirectcostsSalay = gdfyDirectcostsSalay;
    }

    public String getGdfyDirectcostsFund() {
        return gdfyDirectcostsFund;
    }

    public void setGdfyDirectcostsFund(String gdfyDirectcostsFund) {
        this.gdfyDirectcostsFund = gdfyDirectcostsFund;
    }

    public String getGdfyDirectcostsBonus() {
        return gdfyDirectcostsBonus;
    }

    public void setGdfyDirectcostsBonus(String gdfyDirectcostsBonus) {
        this.gdfyDirectcostsBonus = gdfyDirectcostsBonus;
    }

    public String getGdfyExecutivesalarySalay() {
        return gdfyExecutivesalarySalay;
    }

    public void setGdfyExecutivesalarySalay(String gdfyExecutivesalarySalay) {
        this.gdfyExecutivesalarySalay = gdfyExecutivesalarySalay;
    }

    public String getGdfyExecutivesalaryFund() {
        return gdfyExecutivesalaryFund;
    }

    public void setGdfyExecutivesalaryFund(String gdfyExecutivesalaryFund) {
        this.gdfyExecutivesalaryFund = gdfyExecutivesalaryFund;
    }

    public String getGdfyExecutivesalaryBonus() {
        return gdfyExecutivesalaryBonus;
    }

    public void setGdfyExecutivesalaryBonus(String gdfyExecutivesalaryBonus) {
        this.gdfyExecutivesalaryBonus = gdfyExecutivesalaryBonus;
    }

    public String getGdfyFwsalaryBgcd() {
        return gdfyFwsalaryBgcd;
    }

    public void setGdfyFwsalaryBgcd(String gdfyFwsalaryBgcd) {
        this.gdfyFwsalaryBgcd = gdfyFwsalaryBgcd;
    }

    public String getGdfyFwsalaryWg() {
        return gdfyFwsalaryWg;
    }

    public void setGdfyFwsalaryWg(String gdfyFwsalaryWg) {
        this.gdfyFwsalaryWg = gdfyFwsalaryWg;
    }

    public String getGdfyFwsalarySd() {
        return gdfyFwsalarySd;
    }

    public void setGdfyFwsalarySd(String gdfyFwsalarySd) {
        this.gdfyFwsalarySd = gdfyFwsalarySd;
    }

    public String getGdfyFwsalaryNet() {
        return gdfyFwsalaryNet;
    }

    public void setGdfyFwsalaryNet(String gdfyFwsalaryNet) {
        this.gdfyFwsalaryNet = gdfyFwsalaryNet;
    }

    public String getGdfyFwsalaryGbwh() {
        return gdfyFwsalaryGbwh;
    }

    public void setGdfyFwsalaryGbwh(String gdfyFwsalaryGbwh) {
        this.gdfyFwsalaryGbwh = gdfyFwsalaryGbwh;
    }

    public String getGdfyAssetsamortizationBgzx() {
        return gdfyAssetsamortizationBgzx;
    }

    public void setGdfyAssetsamortizationBgzx(String gdfyAssetsamortizationBgzx) {
        this.gdfyAssetsamortizationBgzx = gdfyAssetsamortizationBgzx;
    }

    public String getGdfyAssetsamortizationBgsb() {
        return gdfyAssetsamortizationBgsb;
    }

    public void setGdfyAssetsamortizationBgsb(String gdfyAssetsamortizationBgsb) {
        this.gdfyAssetsamortizationBgsb = gdfyAssetsamortizationBgsb;
    }

    public String getGdfyAssetsamortizationRjtx() {
        return gdfyAssetsamortizationRjtx;
    }

    public void setGdfyAssetsamortizationRjtx(String gdfyAssetsamortizationRjtx) {
        this.gdfyAssetsamortizationRjtx = gdfyAssetsamortizationRjtx;
    }

    public String getGdfyAssetsamortizationBdczj() {
        return gdfyAssetsamortizationBdczj;
    }

    public void setGdfyAssetsamortizationBdczj(String gdfyAssetsamortizationBdczj) {
        this.gdfyAssetsamortizationBdczj = gdfyAssetsamortizationBdczj;
    }

    public String getGdfyZcjzzbHzzb() {
        return gdfyZcjzzbHzzb;
    }

    public void setGdfyZcjzzbHzzb(String gdfyZcjzzbHzzb) {
        this.gdfyZcjzzbHzzb = gdfyZcjzzbHzzb;
    }

    public String getGdfyCwfySxf() {
        return gdfyCwfySxf;
    }

    public void setGdfyCwfySxf(String gdfyCwfySxf) {
        this.gdfyCwfySxf = gdfyCwfySxf;
    }

    public String getGdfyCwfyLx() {
        return gdfyCwfyLx;
    }

    public void setGdfyCwfyLx(String gdfyCwfyLx) {
        this.gdfyCwfyLx = gdfyCwfyLx;
    }

    public String getGdfySdsfySds() {
        return gdfySdsfySds;
    }

    public void setGdfySdsfySds(String gdfySdsfySds) {
        this.gdfySdsfySds = gdfySdsfySds;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
}
