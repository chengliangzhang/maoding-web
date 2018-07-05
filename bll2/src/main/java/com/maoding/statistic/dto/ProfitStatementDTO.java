package com.maoding.statistic.dto;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/19 18:29
 * 描    述 :利润报表
 */
public class ProfitStatementDTO {
    private MainBusinessIncomeDTO mainBusinessIncomeDTO;//主营业务收入
    private MainBusinessCostDTO mainBusinessCostDTO;//主营业务税金、主营业务成本、主营业务利润
    private AdministrativeCostDTO administrativeCostDTO;//管理费用
    private FinancialCostDTO financialCostDTO;//财务费用

    public MainBusinessIncomeDTO getMainBusinessIncomeDTO() {
        return mainBusinessIncomeDTO;
    }

    public void setMainBusinessIncomeDTO(MainBusinessIncomeDTO mainBusinessIncomeDTO) {
        this.mainBusinessIncomeDTO = mainBusinessIncomeDTO;
    }

    public MainBusinessCostDTO getMainBusinessCostDTO() {
        return mainBusinessCostDTO;
    }

    public void setMainBusinessCostDTO(MainBusinessCostDTO mainBusinessCostDTO) {
        this.mainBusinessCostDTO = mainBusinessCostDTO;
    }

    public AdministrativeCostDTO getAdministrativeCostDTO() {
        return administrativeCostDTO;
    }

    public void setAdministrativeCostDTO(AdministrativeCostDTO administrativeCostDTO) {
        this.administrativeCostDTO = administrativeCostDTO;
    }

    public FinancialCostDTO getFinancialCostDTO() {
        return financialCostDTO;
    }

    public void setFinancialCostDTO(FinancialCostDTO financialCostDTO) {
        this.financialCostDTO = financialCostDTO;
    }
}
