package com.maoding.statistic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/5/8.
 */
@Alias("StatisticDetailQueryDTO")
public class StatisticDetailQueryDTO extends BaseDTO{
    /**
     * 要查询的公司ID列表
     */
    private List<String> companyIdList = new ArrayList<>();
    /**
     * 费用类型（收支类型）
     * 1:合同回款，2:技术审查费，3:合作设计费，4:其他费用
     * SystemParameters.FEE_TYPE_CONTRACT -- SystemParameters.FEE_TYPE_OTHER_DEBIT
     */
    private String feeType;

    /**
     * 1:收款，2：付款
     */
    private Integer payType;

    /**
     * 前端接收的参数
     */
    private List<String> feeTypeList = new ArrayList<>();

    /**
     * 分类统计中，柱状图 类型（参数整理）
     */
    private List<String> colFeeTypeList = new ArrayList<>();
    /**
     * 分类统计中，柱状图 月份类型（参数整理）
     */
    /** 收支分类 **/
    private List<String> feeTypeParentList;

    private List<String> colMonthList = new ArrayList<>();

    /**
     * 要查询的公司ID列表(并且带了每个公司应该查询的时间范围)
     */
    private List<StatisticCompanyDTO> statisticCompanyList =  new ArrayList<>();
    /**
     * 关联组织
     */
    private String combineCompanyId;
    /**
     * 是否合并报表
     */
    private Boolean typeIsUnion;
    /**
     * 起始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    /**
     * 终止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    /**
     * 起始日期
     */
    private String startDateStr;
    /**
     * 终止日期
     */
    private String endDateStr;

    /**
     * 显示第几页
     */
    private Integer pageIndex;
    /**
     * 显示每页行数
     */
    private Integer pageSize;
    /**
     * 返回的统计列表从符合条件的记录集中第几条记录开始
     */
    private Integer startLine;
    /**
     * 返回的统计列表最多为多少条
     */
    private Integer linesCount;

    /**
     * 按照项目名称搜索
     */
    private String projectNameMask;
    /**
     * 单位：1-万元，其他-元
     */
    Integer unitType;
    /**
     * 收支类型：0为正数，1为负数，2为项目收支，3为非项目收支
     */
    Integer profitType;
    /**
     * 排序字段，目前只能为profitDate
     */
    String orderField;
    /**
     * 排序方向：1-升序，其他-降序
     */
    Integer direction;

    /**
     * 应收组织ID
     */
    private String receivableId;
    /**
     * 应付组织ID
     */
    private String paymentId;

    private String associatedOrg;

    private String projectName;

    private String companyId;
    //利润报表查询条件
    private String date;

    private String pcode;

    private String year;

    private String groupByTime;

    private String fromCompanyName;

    private String toCompanyName;

    private String fromCompanyId;

    private String toCompanyId;

    /**
     * Excel模板文件名
     */
    String templateFileName;

    public List<String> getCompanyIdList() {
        return companyIdList;
    }

    public void setCompanyIdList(List<String> companyIdList) {
        this.companyIdList = companyIdList;
    }

    public String getFeeType() {
        if(StringUtil.isNullOrEmpty(feeType) && !CollectionUtils.isEmpty(this.feeTypeList)){
            feeType = StringUtils.join(feeTypeList, ",");
        }
        if(StringUtil.isNullOrEmpty(feeType)){
            feeType = null;
        }
        return feeType;
    }

    public List<String> getFeeTypeParentList() {
        return feeTypeParentList;
    }

    public void setFeeTypeParentList(List<String> feeTypeParentList) {
        this.feeTypeParentList = feeTypeParentList;
    }

    public List<String> getColFeeTypeList() {
        return colFeeTypeList;
    }

    public void setColFeeTypeList(List<String> colFeeTypeList) {
        this.colFeeTypeList = colFeeTypeList;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getCombineCompanyId() {
        return combineCompanyId;
    }

    public void setCombineCompanyId(String combineCompanyId) {
        this.combineCompanyId = combineCompanyId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if(startDate!=null){
            this.startDate = DateUtils.addDays(startDate, -1);
        }
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartLine() {
        if (startLine == null) {
            startLine = ((getPageIndex() != null) && (getPageSize() != null)) ? getPageIndex() * getPageSize() : null;
        }
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getLinesCount() {
        if (linesCount == null) {
            linesCount = getPageSize();
        }
        return linesCount;
    }

    public void setLinesCount(Integer linesCount) {
        this.linesCount = linesCount;
    }

    public Boolean getTypeIsUnion() {
        return typeIsUnion;
    }

    public void setTypeIsUnion(Boolean typeIsUnion) {
        this.typeIsUnion = typeIsUnion;
    }

    public String getProjectNameMask() {
        return projectNameMask;
    }

    public void setProjectNameMask(String projectNameMask) {
        this.projectNameMask = projectNameMask;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public Integer getProfitType() {
        return profitType;
    }

    public void setProfitType(Integer profitType) {
        this.profitType = profitType;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getReceivableId() {
        if(StringUtil.isNullOrEmpty(receivableId)){
            receivableId = null;
        }
        return receivableId;
    }

    public void setReceivableId(String receivableId) {
        this.receivableId = receivableId;
    }

    public String getPaymentId() {
        if(StringUtil.isNullOrEmpty(paymentId)){
            paymentId = null;
        }
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAssociatedOrg() {
        if(StringUtil.isNullOrEmpty(associatedOrg)){
            associatedOrg = null;
        }
        return associatedOrg;
    }

    public void setAssociatedOrg(String associatedOrg) {
        this.associatedOrg = associatedOrg;
    }

    public String getProjectName() {
        if(StringUtil.isNullOrEmpty(projectName)){
            projectName = null;
        }
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<String> getFeeTypeList() {
        return feeTypeList;
    }

    public void setFeeTypeList(List<String> feeTypeList) {
        this.feeTypeList = feeTypeList;
    }

    public String getGroupByTime() {
        return groupByTime;
    }

    public void setGroupByTime(String groupByTime) {
        this.groupByTime = groupByTime;
    }

    public List<String> getColMonthList() {
        return colMonthList;
    }

    public void setColMonthList(List<String> colMonthList) {
        this.colMonthList = colMonthList;
    }

    public List<StatisticCompanyDTO> getStatisticCompanyList() {
        return statisticCompanyList;
    }

    public void setStatisticCompanyList(List<StatisticCompanyDTO> statisticCompanyList) {
        this.statisticCompanyList = statisticCompanyList;
    }

    public String getFromCompanyName() {
        if(StringUtil.isNullOrEmpty(fromCompanyName)){
            fromCompanyName = null;
        }
        return fromCompanyName;
    }

    public void setFromCompanyName(String fromCompanyName) {

        this.fromCompanyName = fromCompanyName;
    }

    public String getToCompanyName() {
        if(StringUtil.isNullOrEmpty(toCompanyName)){
            toCompanyName = null;
        }
        return toCompanyName;
    }

    public void setToCompanyName(String toCompanyName) {
        this.toCompanyName = toCompanyName;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }

    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
    }

    public String getToCompanyId() {
        return toCompanyId;
    }

    public void setToCompanyId(String toCompanyId) {
        this.toCompanyId = toCompanyId;
    }
}
