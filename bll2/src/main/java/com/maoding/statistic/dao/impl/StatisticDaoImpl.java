package com.maoding.statistic.dao.impl;

import com.maoding.statistic.dao.StatisticDao;
import com.maoding.statistic.dto.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Idccapp22 on 2016/10/17.
 */
@Service("statisticDao")
public class StatisticDaoImpl implements StatisticDao {

    @Autowired
    protected SqlSessionTemplate sqlSession;

    /**
     * 方法描述：公司人数统计
     * 作者：MaoSF
     * 日期：2016/10/17
     */
    public List<CompanyStatisticDTO> getCompanyStatisticList(Map<String, Object> param) {
        return sqlSession.selectList("GetCompanyStatisticMapper.getCompanyStatisticList", param);
    }

    /**
     * 方法描述：公司人数统计 总条数
     * 作者：MaoSF
     * 日期：2016/10/17
     */
    @Override
    public int getCompanyStatisticCount(Map<String, Object> param) {
        return sqlSession.selectOne("GetCompanyStatisticMapper.getCompanyStatisticCount");
    }

    /**
     * 方法描述：获取费用统计数据
     * 作者：ZCL
     * 日期：2017/4/27
     *
     * @param dto: 必填字段：companyId，type
     */
    @Override
    public List<ProjectCostStatisticDTO> selectStatisticByQuery(ProjectCostQueryDTO dto) {
        return sqlSession.selectList("CostDetailPaymentPointProjectMapper.selectStatisticByQuery", dto);
    }

    @Override
    public List<ProjectCostStatisticDTO> selectStatisticByQuery(ProjectCostQueryDTO dto, AtomicInteger total) {
        if (total != null) {
            dto.setIsCount(true);
        }
        List<ProjectCostStatisticDTO> result = selectStatisticByQuery(dto);
        if (total != null) {
            total.set(sqlSession.selectOne("CostDetailPaymentPointProjectMapper.getCount"));
        }
        return result;
    }

    @Override
    public CompanyCostStatisticDTO selectStatisticSummaryByQuery(ProjectCostQueryDTO dto) {
        return sqlSession.selectOne("CostDetailPaymentPointProjectMapper.selectStatisticSummaryByQuery", dto);
    }

    @Override
    public StatisticDetailSummaryDTO getStatisticDetailSummary(StatisticDetailQueryDTO dto, Boolean isIncludeAll) {
        StatisticDetailSummaryDTO result = sqlSession.selectOne("StatisticMapper.selectStatisticDetailSummary", dto);
        if ((isIncludeAll) && (result != null) && (result.getTotal() > 0)) {
            result.setDetailList(getStatisticDetailList(dto));
        }
        result.setSelectList(sqlSession.selectList("StatisticMapper.selectValidCompany", dto));
        return result;
    }

    @Override
    public List<StatisticDetailDTO> getStatisticDetailList(StatisticDetailQueryDTO dto) {
        if (dto == null) return null;
        return sqlSession.selectList("StatisticMapper.selectStatisticDetail", dto);
    }


    /**
     * 获取总收入
     */
    @Override
    public StatisticSummaryDTO getStatisticSummaryDTO(StatisticSummaryQueryDTO dto) {
        StatisticDetailQueryDTO query = new StatisticDetailQueryDTO();
        query.setCompanyIdList(dto.getCompanyIdList());
        query.setUnitType(dto.getUnitType());
        return sqlSession.selectOne("StatisticMapper.selectStatisticSummary", query);
    }

    /**
     * 方法描述：人员任务统计
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    @Override
    public List<UserTaskStaticDTO> listUserTaskStatistic(Map<String, Object> param) throws Exception {
        return this.sqlSession.selectList("TaskStatisticMapper.listUserTaskStatistic", param);
    }

    /**
     * 方法描述：获取 人员任务统计 总条数
     * 作者：MaoSF
     * 日期：2017/6/7
     *
     * @param param
     */
    @Override
    public int getUserTaskStatisticCount(Map<String, Object> param) throws Exception {
        return this.sqlSession.selectOne("TaskStatisticMapper.getUserTaskStatisticCount", param);
    }

    /**
     * 收支总览-台账
     */
    @Override
    public List<StatisticDetailDTO> getExpensesDetailLedger(StatisticDetailQueryDTO dto) {
        if ("".equals(dto.getProjectName())) {
            dto.setProjectName(null);
        }
        if ("".equals(dto.getAssociatedOrg())) {
            dto.setAssociatedOrg(null);
        }

      //  return this.sqlSession.selectList("StatisticMapper.getExpensesDetailLedger", dto);
        return this.sqlSession.selectList("StatisticCompanyBillMapper.getCompanyStandingBook", dto);
    }

    @Override
    public List<StatisticDetailDTO> getExpensesDetailLedgerSum(StatisticDetailQueryDTO param) {
        if ("".equals(param.getProjectName())) {
            param.setProjectName(null);
        }
        if ("".equals(param.getAssociatedOrg())) {
            param.setAssociatedOrg(null);
        }
        return this.sqlSession.selectList("StatisticMapper.getExpensesDetailLedgerSum", param);
    }

    @Override
    public StatisticDetailSummaryDTO getCompanyStandingBookSum(StatisticDetailQueryDTO param) {
        if ("".equals(param.getProjectName())) {
            param.setProjectName(null);
        }
        if ("".equals(param.getAssociatedOrg())) {
            param.setAssociatedOrg(null);
        }
        return this.sqlSession.selectOne("StatisticCompanyBillMapper.getCompanyStandingBookSum", param);
    }

    @Override
    public StatisticDetailSummaryDTO getBalanceSum(StatisticDetailQueryDTO param) {
        return this.sqlSession.selectOne("StatisticCompanyBillMapper.getBalanceSum", param);
    }

    public Integer getExpensesDetailLedgerCount(StatisticDetailQueryDTO param) {
       // return this.sqlSession.selectOne("StatisticMapper.getExpensesDetailLedgerCount", param);
        return this.sqlSession.selectOne("StatisticCompanyBillMapper.getCompanyStandingBookCount", param);
    }

    /**
     * 收支总览-应收
     */
    @Override
    public List<StatisticDetailDTO> getReceivable(StatisticDetailQueryDTO dto) {
        if ("".equals(dto.getProjectName())) {
            dto.setProjectName(null);
        }
        if ("".equals(dto.getAssociatedOrg())) {
            dto.setAssociatedOrg(null);
        }
        return this.sqlSession.selectList("StatisticMapper.getReceivable", dto);
    }

    @Override
    public Integer getReceivableCount(StatisticDetailQueryDTO param) {
        return this.sqlSession.selectOne("StatisticMapper.getReceivableCount", param);
    }

    @Override
    public BigDecimal getReceivableSum(StatisticDetailQueryDTO param) {
        return this.sqlSession.selectOne("StatisticMapper.getReceivableSum", param);
    }

    /**
     * 收支总览-应付
     */
    @Override
    public List<StatisticDetailDTO> getPayment(StatisticDetailQueryDTO dto) {
        if ("".equals(dto.getProjectName())) {
            dto.setProjectName(null);
        }
        if ("".equals(dto.getAssociatedOrg())) {
            dto.setAssociatedOrg(null);
        }
        return this.sqlSession.selectList("StatisticMapper.getPayment", dto);
    }

    @Override
    public Integer getPaymentCount(StatisticDetailQueryDTO param) {
        return this.sqlSession.selectOne("StatisticMapper.getPaymentCount", param);
    }

    @Override
    public BigDecimal getPaymentSum(StatisticDetailQueryDTO param) {
        return this.sqlSession.selectOne("StatisticMapper.getPaymentSum", param);
    }

    @Override
    public ReceivableDetailDTO getReceivableDetail(String projectId) {
        return this.sqlSession.selectOne("StatisticMapper.getReceivableDetail", projectId);
    }

    @Override
    public List<PaymentDetailDTO> getReceivableDetailList(String projectId) {
        return this.sqlSession.selectList("StatisticMapper.getReceivableDetailList", projectId);
    }

    @Override
    public List<PaymentDetailDTO> getPaymentDetailList(String projectId) {
        return this.sqlSession.selectList("StatisticMapper.getPaymentDetailList", projectId);
    }

    @Override
    public BigDecimal getPaymentAccountFee(String costPointId) {
        return this.sqlSession.selectOne("StatisticMapper.getPaymentAccountFee", costPointId);
    }

    @Override
    public BigDecimal getAccountFee(String costPointId) {
        return this.sqlSession.selectOne("StatisticMapper.getAccountFee", costPointId);
    }

    @Override
    public ReceivableDetailDTO getPaymentDetail(String projectId) {
        return this.sqlSession.selectOne("StatisticMapper.getPaymentDetail", projectId);
    }

    /**
     * 总收入
     */
    @Override
    public List<StatisticDetailDTO> getGeneralIncome(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticMapper.getGeneralIncome", dto);
    }

    /**
     * 总支出
     */
    @Override
    public List<StatisticDetailDTO> getTotalExpenditure(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticMapper.getTotalExpenditure", dto);
    }

    @Override
    public ExpensesStatisticsDTO getCompanyBillClassStatistics(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectOne("StatisticCompanyBillMapper.getCompanyBillClassStatistics", dto);
    }

    /**
     * 主营业务成本，直接项目成本中（合作设计分、技术审查费、其他支出）
     */
    @Override
    public List<StatisticDetailDTO> getProjectExpenditure(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticMapper.getProjectExpenditure", dto);
    }

    /**
     * 报销汇总
     */
    @Override
    public List<StatisticDetailDTO> getPaymentCollect(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticMapper.getPaymentCollect", dto);
    }

    /**
     * 曲线总收入
     */
    @Override
    public List<StatisticDetailDTO> getCurveIncomeData(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticMapper.getCurveIncomeData", dto);
    }

    /**
     * 曲线总支出
     */
    @Override
    public List<StatisticDetailDTO> getCurveExpenditureData1(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticMapper.getCurveExpenditureData1", dto);
    }

    /**
     * 曲线主营业务成本，直接项目成本中（合作设计分、技术审查费、其他支出）
     */
    @Override
    public List<StatisticDetailDTO> getCurveExpenditureData2(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticMapper.getCurveExpenditureData2", dto);
    }

    /**
     * 曲线报销汇总
     */
    @Override
    public List<StatisticDetailDTO> getCurveExpenditureData3(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticMapper.getCurveExpenditureData3", dto);
    }

    @Override
    public List<ClassifiedStatisticDataDTO> getCompanyBillForLineData(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticCompanyBillMapper.getCompanyBillForLineData", dto);
    }

    @Override
    public List<StatisticDetailDTO> getContractSumMoney(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticMapper.getContractSumMoney", dto);
    }

    @Override
    public List<CompanyBillDetailDTO> getCompanyBillDetailByYear(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticCompanyBillMapper.getCompanyBillDetailByYear", dto);
    }

    @Override
    public List<Map<String, String>> getRelationCompany(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticCompanyBillMapper.getRelationCompany", dto);
    }

    @Override
    public List<StatisticDetailSummaryDTO> getCompanyColumnarData(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticCompanyBillMapper.getCompanyColumnarData", dto);
    }

    @Override
    public List<StatisticClassicSummaryDTO> getColumnarDataForOrgGroup(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticCompanyBillMapper.getColumnarDataForOrgGroup", dto);
    }

    @Override
    public List<StatisticClassicSummaryDTO> getColumnarDataForTimeGroup(StatisticDetailQueryDTO dto) {
        return this.sqlSession.selectList("StatisticCompanyBillMapper.getColumnarDataForTimeGroup", dto);
    }

    /**
     * 描述     获取收支明细查询结果中的收支分类和收支分类子项信息
     * 日期     2018/8/15
     *
     * @param query 收支明细查询条件
     * @return 收支分类和收支分类子项信息
     * @author 张成亮
     **/
    @Override
    public List<CostTypeDTO> listFeeTypeFilter(StatisticDetailQueryDTO query) {
        return sqlSession.selectList("StatisticCompanyBillMapper.listFeeTypeFilter", query);
    }
}
