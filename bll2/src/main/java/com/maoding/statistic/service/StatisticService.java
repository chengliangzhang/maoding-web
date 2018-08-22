package com.maoding.statistic.service;

import com.maoding.core.bean.AjaxMessage;
import com.maoding.projectcost.dto.ProjectCostQueryDTO;
import com.maoding.statistic.dto.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp22 on 2016/10/17.
 */
public interface StatisticService {

    /**
     * 方法描述：公司人数统计
     * 作者：MaoSF
     * 日期：2016/10/17
     *
     * @param:
     * @return:
     */
    public List<CompanyStatisticDTO> getCompanyStatisticList(Map<String, Object> param);

    /**
     * 方法描述：公司人数统计 总条数
     * 作者：MaoSF
     * 日期：2016/10/17
     *
     * @param:
     * @return:
     */
    public int getCompanyStatisticCount(Map<String, Object> param);

    //===============================新接口2.0==========================================

    /**
     * 方法描述：公司人数统计
     * 作者：MaoSF
     * 日期：2016/10/17
     *
     * @param:
     * @return:
     */
    public List<CompanyStatisticDTO> selectCompanyStatisticList(Map<String, Object> param);

    /**
     * 方法描述：公司人数统计 总条数
     * 作者：MaoSF
     * 日期：2016/10/17
     *
     * @param:
     * @return:
     */
    public int selectCompanyStatisticCount(Map<String, Object> param);

    /**
     * 统计项目费用
     */
    CompanyCostStatisticDTO getStatProjectCost(ProjectCostQueryDTO param);

    /**
     * 统计总览（列表）
     */
    StatisticDetailSummaryDTO getStatisticDetailSummary(StatisticDetailQueryDTO param);

    StatisticDetailSummaryDTO getStatisticDetailSummaryByCompanyId(String companyId);

    /**
     * 余额
     */
    BigDecimal getBalanceSum(StatisticDetailQueryDTO param);

    /**
     * 收支总览（总计）
     */
    StatisticSummaryDTO getStatisticSummaryDTO(StatisticSummaryQueryDTO param);

    /**
     * 方法描述：人员任务统计
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    List<UserTaskStaticDTO> listUserTaskStatistic(Map<String, Object> param) throws Exception;


    /**
     * 方法描述：人员任务统计
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    AjaxMessage listUserTaskStatisticPage(Map<String, Object> param) throws Exception;

    /**
     * 收支总览-台账
     */
    List<StatisticDetailDTO> getExpensesDetailLedger(StatisticDetailQueryDTO param) throws Exception;

    /**
     * 计算总收入、总支出
     */
    List<StatisticDetailDTO> getExpensesDetailLedgerSum(StatisticDetailQueryDTO param);
    StatisticDetailSummaryDTO getCompanyStandingBookSum(StatisticDetailQueryDTO param);

    Integer getExpensesDetailLedgerCount(StatisticDetailQueryDTO param);

    /**
     * 应收
     */
    List<StatisticDetailDTO> getReceivable(StatisticDetailQueryDTO dto) throws Exception;

    Integer getReceivableCount(StatisticDetailQueryDTO param);

    /**
     * 应收总额
     */
    BigDecimal getReceivableSum(StatisticDetailQueryDTO param);

    /**
     * 应付
     */
    List<StatisticDetailDTO> getPayment(StatisticDetailQueryDTO dto) throws Exception;

    Integer getPaymentCount(StatisticDetailQueryDTO param);

    /**
     * 应付总额
     */
    BigDecimal getPaymentSum(StatisticDetailQueryDTO param);

    /**
     * 应收明细
     */
    ReceivableDetailDTO getReceivableDetail(String projectId);

    /**
     * 到账列表
     */
    List<PaymentDetailDTO> getReceivableDetailList(String projectId);

    /**
     * 台账--》关联组织
     */
    Map<String,List<Map<String, String>>> getRelationCompany(StatisticDetailQueryDTO param) ;

    /**
     * 获取到账金额
     */
    BigDecimal getAccountFee(String costPointId);

    /**
     * 应付明细
     */
    ReceivableDetailDTO getPaymentDetail(String projectId);

    /**
     * 应付到账列表
     */
    List<PaymentDetailDTO> getPaymentDetailList(String projectId);

    /**
     * 获取到账金额
     */
    BigDecimal getPaymentAccountFee(String costPointId);

    /**
     * 总收入
     */
    List<StatisticDetailDTO> getGeneralIncome(StatisticDetailQueryDTO dto);

    /**
     * 总支出
     */
    List<StatisticDetailDTO> getTotalExpenditure(StatisticDetailQueryDTO dto);

    /**
     * 封装总收入
     */
    ExpensesStatisticsDTO setGeneralIncome(StatisticDetailQueryDTO dto);

    /**
     * 主营业务成本，直接项目成本中（合作设计分、技术审查费、其他支出）
     */
    List<StatisticDetailDTO> getProjectExpenditure(StatisticDetailQueryDTO dto);

    /**
     * 报销汇总
     */
    List<StatisticDetailDTO> getPaymentCollect(StatisticDetailQueryDTO dto);

    /**
     * 曲线总收入
     */
    List<StatisticDetailDTO> getCurveIncomeData(StatisticDetailQueryDTO dto);

    /**
     * 曲线总支出
     */
    List<StatisticDetailDTO> getCurveExpenditureData1(StatisticDetailQueryDTO dto);

    /**
     * 曲线主营业务成本，直接项目成本中（合作设计分、技术审查费、其他支出）
     */
    List<StatisticDetailDTO> getCurveExpenditureData2(StatisticDetailQueryDTO dto);

    /**
     * 曲线报销汇总
     */
    List<StatisticDetailDTO> getCurveExpenditureData3(StatisticDetailQueryDTO dto);

    /**
     * 获取曲线总支出
     */
    Map<String, BigDecimal> getCurveExpenditureData(StatisticDetailQueryDTO dto);

    /**
     * 获取曲线图数据
     */
    Object[] getCompanyBillForLineData(StatisticDetailQueryDTO dto);

    /***
     * 获取合同总金额
     * */
    List<StatisticDetailDTO> getContractSumMoney(StatisticDetailQueryDTO dto);

    /**
     * 利润报表
     */
    List<ProfitStatementTableDTO> getProfitStatement(StatisticDetailQueryDTO dto)  throws Exception;

    List<StatisticDetailSummaryDTO> getCompanyColumnarData(StatisticDetailQueryDTO dto);

    /**
     * 按组织分组的柱状图数据
     */
    List<StatisticClassicSummaryDTO> getColumnarDataForOrgGroup(StatisticDetailQueryDTO dto);

    /**
     * 按时间分组的柱状图数据
     */
    List<StatisticClassicSummaryDTO> getColumnarDataForTimeGroup(StatisticDetailQueryDTO dto);

    Map<String,Object> getStatisticClassicData(StatisticDetailQueryDTO dto)  throws Exception;

    /**
     * 台账费用分类列表
     */
    List<CostTypeDTO> getFeeTypeList(StatisticDetailQueryDTO dto) throws Exception;

    /**
     * 分类统计分类列表
     */
    List<List<CostTypeDTO>> getCategoryTypeList(StatisticDetailQueryDTO dto) throws Exception;

    /**
     * 描述     获取收支明细标题栏过滤条件列表
     * 日期     2018/8/15
     * @author  张成亮
     * @return  标题栏过滤条件
     * @param   query 收支明细查询条件
     **/
    StatisticTitleFilterDTO getTitleFilter(StatisticDetailQueryDTO query);
}
