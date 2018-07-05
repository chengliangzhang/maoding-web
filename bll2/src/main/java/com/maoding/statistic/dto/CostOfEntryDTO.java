package com.maoding.statistic.dto;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/18 9:56
 * 描    述 :
 */
public enum CostOfEntryDTO {
    GDFY_CWFY_SXF("银行手续费", "gdfy_cwfy_sxf"), GDFY_CWFY_LX("银行利息收入／支出", "gdfy_cwfy_lx"),
    GDFY_SDSFY_SDS("减：所得税费用", "gdfy_sdsfy_sds"), GDFY_SALESTAX_TAX("主营业务税金及附加", "gdfy_salestax_tax"),
    GDFY_DIRECTCOSTS_SALAY("投入在项目上的员工工资", "gdfy_directcosts_salay"), GDFY_DIRECTCOSTS_FUND("投入在项目上的五险一金", "gdfy_directcosts_fund"),
    GDFY_DIRECTCOSTS_BONUS("投入在项目上的员工奖金", "gdfy_directcosts_bonus"), GDFY_EXECUTIVESALARY_SALAY("管理行政人员的工资", "gdfy_executivesalary_salay"),
    GDFY_EXECUTIVESALARY_FUND("管理行政人员的五险一金", "gdfy_executivesalary_fund"), GDFY_EXECUTIVESALARY_BONUS("管理行政人员的奖金", "gdfy_executivesalary_bonus"),
    GDFY_FWSALARY_BGCD("办公场地房租", "gdfy_fwsalary_bgcd"), GDFY_FWSALARY_WG("物管费用", "gdfy_fwsalary_wg"), GDFY_FWSALARY_SD("水电费用", "gdfy_fwsalary_sd"),
    GDFY_FWSALARY_NET("网络、电话", "gdfy_fwsalary_net"), GDFY_FWSALARY_GBWH("办公场地维护费用", "gdfy_fwsalary_gbwh"), GDFY_ASSETSAMORTIZATION_BGZX("办公室装修摊销", "gdfy_assetsamortization_bgzx"),
    GDFY_ASSETSAMORTIZATION_BGSB("办公室设备摊销", "gdfy_assetsamortization_bgsb"), GDFY_ASSETSAMORTIZATION_RJTX("软件摊销", "gdfy_assetsamortization_rjtx"),
    GDFY_ASSETSAMORTIZATION_BDCZJ("不动产折旧", "gdfy_assetsamortization_bdczj"), GDFY_ZCJZZB_HZZB("应收账款坏账准备", "gdfy_zcjzzb_hzzb"), BX_YWFY("直接项目成本", "bx_ywfy"),
    BX_XZFY("行政费用", "bx_xzfy"), BX_JYFY("经营费用", "bx_jyfy"), BX_QTFY("其他费用", "bx_qtfy"), GDFY_EXECUTIVESALARY("管理人员工资", "gdfy_executivesalary"),
    GDFY_FWSALARY("房屋物业费用", "gdfy_fwsalary"), GDFY_ASSETSAMORTIZATION("资产摊销", "gdfy_assetsamortization"), GDFY_ZCJZZB("资产减值准备", "gdfy_zcjzzb"), GDFY_CWFY("财务费用", "gdfy_cwfy"),
    GDFY_SDSFY("所得税费用", "gdfy_sdsfy"), GDFY_DIRECTCOSTS("直接人工成本", "gdfy_directcosts"), GDFY_SALESTAX("主营业务税金及附加主code", "gdfy_salestax"),GDZC_QTFY("其他费用","gdzc_qtfy");

    private String name;
    private String code;

    CostOfEntryDTO(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

//    public static void main(String[] args) {
//        System.out.println(CostOfEntryDTO.GDFY_CWFY_LX.getCode()+CostOfEntryDTO.GDFY_CWFY_LX.getName());
//    }
}
