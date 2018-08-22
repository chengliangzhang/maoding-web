package com.maoding.statistic.controller;


import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.RoleConst;
import com.maoding.core.util.DateUtils;
import com.maoding.financial.service.ExpMainService;
import com.maoding.projectcost.dto.ProjectCostQueryDTO;
import com.maoding.projectcost.service.ProjectCostService;
import com.maoding.statistic.dto.*;
import com.maoding.statistic.service.StatisticService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("iWork/statistic")
public class StatisticController extends BaseController {

    @Autowired
    private StatisticService statisticService;
    @Autowired
    private ProjectCostService projectCostService;
    @Autowired
    private ExpMainService expMainService;

    @ModelAttribute
    public void before() throws Exception {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
    }

    /**
     * 统计
     */
    @RequestMapping("/toStat")
    public String toStat(ModelMap model) throws Exception {
        model.addAttribute("forwardType", model.getOrDefault("forwardType", null));
        model.addAttribute("timeNow", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return "views/statistic/stat";
    }

    /**
     * 统计
     *
     * @param forwordType{ 0=收支总览，1=合同回款,2=技术审查费（付款）,3=合作设计费（付款）,4=其他费用（付款）,5=技术审查费（收款），6=合作设计费（收款），7=//其他费用（收款）}
     */
    @RequestMapping("/toStat/{forwordType}")
    public String toStatBy(RedirectAttributes model, @PathVariable String forwordType) throws Exception {
        String dataAction;
        switch (forwordType) {
            case "0":
                dataAction = "summaryFee";//收支总览
                break;
            default:
                dataAction = "summaryFee";
                break;
            /*case "1":
                dataAction = "contractFee"; //合同回款
				break;
			case "2":
				dataAction = "techFee";//技术审查费（付款）
				break;
			case "3":
				dataAction = "corpFee";//合作设计费（付款）
				break;
			case "4":
				dataAction = "otherFee";//其他费用（付款）
				break;
			case "5":
				dataAction = "techFee2";//技术审查费（收款）
				break;
			case "6":
				dataAction = "corpFee2";//合作设计费（收款）
				break;
			case "7":
				dataAction = "otherFee2";//其他费用（收款）
				break;
			default:
				dataAction = "contractFee";
				break;*/
        }
        model.addFlashAttribute("forwardType", dataAction);
        return "redirect:/iWork/statistic/toStat";
    }

    /**
     * 统计项目费用
     */
    @RequestMapping("/getStatProjectCost")
    @ResponseBody
    public AjaxMessage getStatProjectCost(@RequestBody ProjectCostQueryDTO param) throws Exception {
        CompanyCostStatisticDTO data = statisticService.getStatProjectCost(param);
        return AjaxMessage.succeed(data);
    }

    /**
     * 收支总览（列表）
     */
    @RequestMapping("/getStatisticDetailSummary")
    @ResponseBody
    public AjaxMessage getStatisticDetailSummary(@RequestBody StatisticDetailQueryDTO param) throws Exception {
        if (param.getCompanyIdList() == null || param.getCompanyIdList().size() == 0)
            return AjaxMessage.failed(null).setInfo("没有指定组织");

        StatisticDetailSummaryDTO data = statisticService.getStatisticDetailSummary(param);
        return AjaxMessage.succeed(data);
    }

    /**
     * 收支总览（总计）
     */
    @RequestMapping("/getStatisticSummaryDTO")
    @ResponseBody
    public AjaxMessage getStatisticSummaryDTO(@RequestBody StatisticSummaryQueryDTO param) throws Exception {
        if (param.getCompanyIdList() == null || param.getCompanyIdList().size() == 0)
            return AjaxMessage.failed(null).setInfo("没有指定组织");

        StatisticSummaryDTO data = statisticService.getStatisticSummaryDTO(param);
        return AjaxMessage.succeed(data).setInfo("查询成功");
    }


    /**
     * 方法描述：统计当前系统中团队数数量，团队总人数，团队未激活人数
     * 作        者：MaoSF
     * 日        期：2016年10月17日-下午6:10:08
     */
    @RequestMapping(value = "getCompanyStatistic", method = RequestMethod.GET)
    @ResponseBody
    public Object getCompanyStatistic(@RequestBody Map<String, Object> param) throws Exception {
        List<CompanyStatisticDTO> data = statisticService.selectCompanyStatisticList(param);
        int totalNumber = statisticService.selectCompanyStatisticCount(param);
        //返回数据
        param.clear();
        param.put("data", data);
        param.put("total", totalNumber);
        return this.ajaxResponseSuccess().setData(param);
    }

    /**
     * 方法描述：统计合同回款界面数据
     * 作者：wrb
     * 日期：2017/5/11
     *
     * @param:{projectId,companyId}
     * @return:
     */
    @RequestMapping(value = {"/getContractInfo"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getContractInfo(@RequestBody Map<String, Object> param) throws Exception {

        return projectCostService.getContractInfo(param);
    }

    /**
     * 方法描述：统计技术审查费界面数据
     * 作者：wrb
     * 日期：2017/5/11
     *
     * @param:{projectId,companyId}
     * @return:
     */
    @RequestMapping(value = {"/getTechicalReviewFeeInfo"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getTechicalReviewFeeInfo(@RequestBody Map<String, Object> map) throws Exception {
        map.put("fromStatistic", "1");
        return projectCostService.getTechicalReviewFeeInfo(map);
    }

    /**
     * 方法描述：统计合作设计费界面数据
     * 作者：wrb
     * 日期：2017/5/11
     *
     * @param:projectId
     * @return:
     */
    @RequestMapping(value = {"/getCooperativeDesignFeeInfo"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getCooperativeDesignFeeInfo(@RequestBody Map<String, Object> map) throws Exception {

        return projectCostService.getCooperativeDesignFeeInfo(map);
    }

    /**
     * 方法描述：统计其他费用界面数据（project，type=4：其他费用付款，type=5:其他费用收款）
     * 作者：wrb
     * 日期：2017/5/11
     *
     * @param:projectId
     * @return:
     */
    @RequestMapping(value = {"/getOtherFeeInfo"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getOtherFee(@RequestBody Map<String, Object> map) throws Exception {
        return projectCostService.getOtherFee(map);
    }

    /**
     * 方法描述：人员任务统计
     * 作者：MaoSF
     * 日期：2017/5/11
     *
     * @param:projectId
     * @return:
     */
    @RequestMapping(value = {"/listUserTaskStatistic"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage listUserTaskStatistic(@RequestBody Map<String, Object> map) throws Exception {
        map.put("companyId", this.currentCompanyId);
        map.put("fastdfsUrl", this.fastdfsUrl);
        return statisticService.listUserTaskStatisticPage(map);
    }

    /**
     * 方法描述：收支总览-台账
     * 作   者：DongLiu
     * 日   期：2017/12/6 16:08
     */
    @RequestMapping(value = {"/getExpensesDetailLedger"}, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_REPORT}, logical = Logical.OR)
    public AjaxMessage getExpensesDetailLedger(@RequestBody StatisticDetailQueryDTO param) throws Exception {
        param.setCurrentCompanyId(this.currentCompanyId);
        if (null == param.getCombineCompanyId())
            return AjaxMessage.failed(null).setInfo("没有指定组织");
        Map<String, Object> para = new HashMap<>();
        if (null != param.getProfitType()) {
            param.setCompanyId(currentCompanyId);
        }
        List<StatisticDetailDTO> data = statisticService.getExpensesDetailLedger(param);
        Integer totalNumber = statisticService.getExpensesDetailLedgerCount(param);
        //关联组织查询条件
        para.put("data", data);
        para.put("total", totalNumber);
        para.put("organization",statisticService.getRelationCompany(param));
        setStatisticDetailSummary(param, para);
        para.put("startDateStr",param.getStartDateStr());
        if(param.getEndDate()==null){
            para.put("endDateStr",DateUtils.date2Str(DateUtils.date_sdf));
        }else {
            para.put("endDateStr",DateUtils.date2Str(param.getEndDate(),DateUtils.date_sdf));
        }
        return this.ajaxResponseSuccess().setData(para);
    }

    private void setStatisticDetailSummary(@RequestBody StatisticDetailQueryDTO param, Map<String, Object> para) throws Exception {
        StatisticDetailSummaryDTO statisticDetailSummary = statisticService.getStatisticDetailSummary(param);
        if(statisticDetailSummary!=null && statisticDetailSummary.getPay()!=null){
            statisticDetailSummary.setPay(new BigDecimal("0").subtract(statisticDetailSummary.getPay()));
        }
        para.put("StatisticDetailSummaryDTO", statisticDetailSummary);
    }


    /**
     * 方法描述：收支总览-应收
     * 作   者：DongLiu
     * 日   期：2017/12/7 16:08
     */
    @RequestMapping(value = {"/getReceivable"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getReceivable(@RequestBody StatisticDetailQueryDTO param) throws Exception {
        if (null == param.getReceivableId())
            return AjaxMessage.failed(null).setInfo("没有指定组织");
        Map<String, Object> para = new HashMap<String, Object>();
        //重新设置参数
        String companyId = param.getReceivableId();
        param.setCurrentCompanyId(this.currentCompanyId);
        List<StatisticDetailDTO> data = statisticService.getReceivable(param);
        for (StatisticDetailDTO dto : data) {
            //如果组织id与FromCompanyId相同并且类型为合作设计费或技术审查费的情况，金额为负数，关联组织显示toCompanyId
            if (1 == dto.getFeeType() || 4 == dto.getFeeType() || 5 == dto.getFeeType()) {
                dto.setFromCompanyName(null);
                dto.setToCompanyName(null);
            }
        }
        StatisticDetailQueryDTO queryDTO = new StatisticDetailQueryDTO();
        queryDTO.setReceivableId(companyId);
        queryDTO.setCurrentCompanyId(this.currentCompanyId);
        List<StatisticDetailDTO> data1 = statisticService.getReceivable(queryDTO);
        Map<String, String> mp = new HashMap<>();
        for (StatisticDetailDTO dto : data1) {
            if (null == mp.get(dto.getFromCompanyId()) && null != dto.getId() && null != dto.getFromCompanyName()) {
                mp.put(dto.getFromCompanyId(), dto.getFromCompanyName());
            }
        }
        Integer total = statisticService.getReceivableCount(param);
        //总金额
        BigDecimal receivaleSum = statisticService.getReceivableSum(param);
        para.put("data", data);
        para.put("total", total);
        para.put("receivaleSum", receivaleSum);
        para.put("organization", mp);

        return this.ajaxResponseSuccess().setData(para);
    }

    /**
     * 方法描述：收支总览-应付
     * 作   者：DongLiu
     * 日   期：2017/12/7 16:08
     */
    @RequestMapping(value = {"/getPayment"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getPayment(@RequestBody StatisticDetailQueryDTO param) throws Exception {
        if (null == param.getPaymentId())
            return AjaxMessage.failed(null).setInfo("没有指定组织");
        Map<String, Object> para = new HashMap<String, Object>();
        String companyId = param.getPaymentId();
        try {
            param.setCurrentCompanyId(this.currentCompanyId);
            List<StatisticDetailDTO> data = statisticService.getPayment(param);
            for (StatisticDetailDTO dto : data) {
                //如果组织id与FromCompanyId相同并且类型为合作设计费或技术审查费的情况，金额为负数，关联组织显示toCompanyId
//                if (1 == dto.getFeeType() || 4 == dto.getFeeType() || 5 == dto.getFeeType()) {
//                    dto.setFromCompanyName(null);
//                    dto.setToCompanyName(null);
//                }else if (companyId.equals(dto.getFromCompanyId())) {
//                    dto.setFromCompanyName(dto.getToCompanyName());
//                }
                dto.setFromCompanyName(dto.getToCompanyName());
            }
            StatisticDetailQueryDTO queryDTO = new StatisticDetailQueryDTO();
            queryDTO.setPaymentId(companyId);
            queryDTO.setCurrentCompanyId(this.currentCompanyId);
            List<StatisticDetailDTO> data1 = statisticService.getPayment(queryDTO);
            Map<String, String> mp = new HashMap<>();
            for (StatisticDetailDTO dto : data1) {
                if (null == mp.get(dto.getToCompanyId()) && null != dto.getId() && null != dto.getToCompanyName()) {
                    mp.put(dto.getToCompanyId(), dto.getToCompanyName());
                }
            }
            Integer total = statisticService.getPaymentCount(param);
            BigDecimal paymentSum = statisticService.getPaymentSum(param);
            para.put("data", data);
            para.put("total", total);
            para.put("paymentSum", paymentSum);
            para.put("organization", mp);
        } catch (Exception e) {
            log.error("getPayment" + e.getMessage());
            return this.ajaxResponseError("数据异常");
        }
        return this.ajaxResponseSuccess().setData(para);
    }

    /**
     * 方法描述：获取应收项目明细
     * 作   者：DongLiu
     * 日   期：2017/12/8 11:01
     */
    @RequestMapping(value = "/getReceivableDetail/{id}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getReceivableDetail(@PathVariable("id") String id) throws Exception {
        if (null == id)
            return AjaxMessage.failed(null).setInfo("没有指定组织");
        Map<String, Object> para = new HashMap<String, Object>();
        ReceivableDetailDTO data = new ReceivableDetailDTO();
        try {
            data = statisticService.getReceivableDetail(id);
            BigDecimal accountFee = statisticService.getAccountFee(id);
            data.setAccountFee(accountFee);
            if (null != accountFee) {
                data.setReceivableFee(data.getLaunchFee().subtract(accountFee));
            }else {
                data.setReceivableFee(data.getLaunchFee());
            }
            List<PaymentDetailDTO> paymentDetailDTOS = statisticService.getReceivableDetailList(id);
            data.setPaymentDetailDTOList(paymentDetailDTOS);
        } catch (Exception e) {
            log.error("getReceivableDetail" + e.getMessage());
            return this.ajaxResponseError("数据异常");
        }
        return AjaxMessage.succeed(data);
    }

    /**
     * 方法描述：获取应收项目明细
     * 作   者：DongLiu
     * 日   期：2017/12/8 11:01
     */
    @RequestMapping(value = "/getPaymentDetail/{id}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getPaymentDetail(@PathVariable("id") String id) throws Exception {
        if (null == id)
            return AjaxMessage.failed(null).setInfo("没有指定组织");
        Map<String, Object> para = new HashMap<String, Object>();
        ReceivableDetailDTO data = new ReceivableDetailDTO();
        try {
            data = statisticService.getPaymentDetail(id);
            BigDecimal accountFee = statisticService.getPaymentAccountFee(id);
            data.setAccountFee(accountFee);
            if (null != accountFee) {
                data.setReceivableFee(data.getLaunchFee().subtract(accountFee));
            }else{
                data.setReceivableFee(data.getLaunchFee());
            }
            List<PaymentDetailDTO> paymentDetailDTOS = statisticService.getPaymentDetailList(id);
            data.setPaymentDetailDTOList(paymentDetailDTOS);
        } catch (Exception e) {
            log.error("getPaymentDetail" + e.getMessage());
            return this.ajaxResponseError("数据异常");
        }
        return AjaxMessage.succeed(data);
    }


    /**
     * 方法描述：分类统计
     * 作   者：DongLiu
     * 日   期：2017/12/13 11:14
     */
    @RequestMapping(value = "/getExpensesStatistics", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_REPORT}, logical = Logical.OR)
    public AjaxMessage getExpensesStatistics(@RequestBody StatisticDetailQueryDTO param) throws Exception {
        if (null == param.getCombineCompanyId() && null == param.getCompanyIdList())
            return AjaxMessage.failed(null).setInfo("没有指定组织");
        Map<String, Object> para = new HashMap<String, Object>();
        //获取按收支类型统计数据
        try {
            ExpensesStatisticsDTO expensesStatisticsDTO = statisticService.setGeneralIncome(param);
            //封装收支类型统计数据
            BigDecimal oneData[] = {expensesStatisticsDTO.getOtherRevenue(), expensesStatisticsDTO.getContractRevenue(),
                    expensesStatisticsDTO.getTechnologyRevenue(), expensesStatisticsDTO.getCooperationRevenue()};
            BigDecimal twoData[] = {expensesStatisticsDTO.getMainBusiness(), expensesStatisticsDTO.getMainBusinessCost(),
                    expensesStatisticsDTO.getFinancialCost(), expensesStatisticsDTO.getIncomeTax(), expensesStatisticsDTO.getManagementFee()};
            //曲线图表
            Object allData[] = statisticService.getCompanyBillForLineData(param);
                    //getCurveAllData(param);
            para.put("oneData", oneData);
            para.put("twoData", twoData);
            para.put("threeData", allData);
            para.put("oneCount", expensesStatisticsDTO.getCountRevenue());
            para.put("twoCount", expensesStatisticsDTO.getCountExpenditure());
        } catch (Exception e) {
            log.error("getExpensesStatistics" + e.getMessage());
            return AjaxMessage.failed("操作失败");
        }
        return AjaxMessage.succeed(para);
    }



    private Object[] setCurveAllData(List<String> dateList, Map<String, BigDecimal> getCurveIncomeMap, Map<String, BigDecimal> getCurveExpenditureData, Map<String, BigDecimal> getContractSumMap) {
        BigDecimal data1[] = new BigDecimal[dateList.size()];
        BigDecimal data2[] = new BigDecimal[dateList.size()];
        BigDecimal data3[] = new BigDecimal[dateList.size()];
        for (int i = 0; i < dateList.size(); i++) {
            if (null == getCurveIncomeMap.get(dateList.get(i).toString())) {
                data1[i] = BigDecimal.valueOf(0);
            } else {
                data1[i] = getCurveIncomeMap.get(dateList.get(i).toString());
            }
        }
        for (int i = 0; i < dateList.size(); i++) {
            if (null == getCurveExpenditureData.get(dateList.get(i).toString())) {
                data2[i] = BigDecimal.valueOf(0);
            } else {
                data2[i] = getCurveExpenditureData.get(dateList.get(i).toString());
            }
        }
        for (int i = 0; i < dateList.size(); i++) {
            if (null == getContractSumMap.get(dateList.get(i).toString())) {
                data3[i] = BigDecimal.valueOf(0);
            } else {
                data3[i] = getContractSumMap.get(dateList.get(i).toString());
            }
        }
        Object allData[] = new Object[4];
        String[] timeData = (String[]) dateList.toArray(new String[dateList.size()]);
        allData[0] = timeData;//名称
        allData[1] = data1;//收入
        allData[2] = data2;//支出
        allData[3] = data3;
        return allData;
    }

    /**
     * 方法描述：测试获取曲线数据单独接口
     * 作   者：DongLiu
     * 日   期：2017/12/14 16:49
     */
    @RequestMapping(value = "/getCurveData", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getCurveData(@RequestBody StatisticDetailQueryDTO param) throws Exception {
        //获取当年的月份
        long oneDayTime = 1000 * 3600 * 24;
        List<String> dateList = new ArrayList<String>();
        List<String> timeList = new ArrayList<String>();
        //获取总收入
        Map<String, BigDecimal> getCurveIncomeMap = new HashMap<String, BigDecimal>();
        List<StatisticDetailDTO> getCurveIncomeData = statisticService.getCurveIncomeData(param);
        for (StatisticDetailDTO detailDTO : getCurveIncomeData) {
            getCurveIncomeMap.put(detailDTO.getCreateDate(), detailDTO.getProfitFee());
        }
        //获取总支出
        Map<String, BigDecimal> getCurveExpenditureData = statisticService.getCurveExpenditureData(param);
        List<StatisticDetailDTO> getCurveExpenditureList = new ArrayList<StatisticDetailDTO>();
        for (Map.Entry<String, BigDecimal> entry : getCurveExpenditureData.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            StatisticDetailDTO detailDTO = new StatisticDetailDTO();
            detailDTO.setCreateDate(entry.getKey());
            detailDTO.setProfitFee(entry.getValue());
            getCurveExpenditureList.add(detailDTO);
        }
        //合同总金额
        Map<String, BigDecimal> getContractSumMap = new HashMap<String, BigDecimal>();
        List<StatisticDetailDTO> getContractSumMoney = statisticService.getContractSumMoney(param);
        for (StatisticDetailDTO detailDTO : getContractSumMoney) {
            getContractSumMap.put(detailDTO.getCreateDate(), detailDTO.getProfitFee());
        }
        //时间判断，前台可能传时间为空的情况
        if (null != param.getStartDate() && null != param.getEndDate()) {
            Date nowTime = new Date(param.getStartDate().getTime() + oneDayTime);
            dateList = DateUtils.getMonthBetween(nowTime, param.getEndDate());
        } else {
            getCurveIncomeData.addAll(getContractSumMoney);
            getCurveIncomeData.addAll(getCurveExpenditureList);
            //对时间进行排序
            if (null != getCurveIncomeData) {
                SortClass sortClass = new SortClass();
                Collections.sort(getCurveIncomeData, sortClass);
            }
            if (getCurveIncomeData.size() > 0) {
                String startTime = getCurveIncomeData.get(0).getCreateDate();
                String endTime = "";
                if (getCurveIncomeData.size() >= 1) {
                    endTime = getCurveIncomeData.get(getCurveIncomeData.size() - 1).getCreateDate();
                } else {
                    endTime = getCurveIncomeData.get(0).getCreateDate();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                if (!"".equals(startTime) && !"".equals(endTime)) {
                    dateList = DateUtils.getMonthBetween(sdf.parse(startTime), sdf.parse(endTime));
                }
            }
        }
        //遍历月份所对应的数值
        Object[] allData = setCurveAllData(dateList, getCurveIncomeMap, getCurveExpenditureData, getContractSumMap);
        return AjaxMessage.succeed(allData);
    }

    /**
     * 方法描述：柱状图图表接口
     * 作   者：DongLiu
     * 日   期：2017/12/16 15:18
     */
    @RequestMapping(value = "/getColumnarData", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getColumnarData(@RequestBody StatisticDetailQueryDTO param) throws Exception {
        if (null == param.getCompanyIdList()) {
            return AjaxMessage.failed(null).setInfo("没有指定组织");
        }
        Object allData[] = new Object[4];
        try {
            //获取柱状图组织id，名称
            List<StatisticDetailSummaryDTO> getColumnarCompany = statisticService.getCompanyColumnarData(param);
            String data0[] = new String[getColumnarCompany.size()];//组织名称
            BigDecimal data2[] = new BigDecimal[getColumnarCompany.size()];
            BigDecimal data3[] = new BigDecimal[getColumnarCompany.size()];
            BigDecimal data4[] = new BigDecimal[getColumnarCompany.size()];
            for (int i = 0; i < getColumnarCompany.size(); i++) {
                data0[i] = getColumnarCompany.get(i).getCompanyName();
                data2[i] = getColumnarCompany.get(i).getGain();
                data3[i] = getColumnarCompany.get(i).getPay();
            }
            //合同总金额全部设置为0
            for (int i = 0; i < getColumnarCompany.size(); i++) {
                data4[i] = BigDecimal.valueOf(0);
            }
            allData[0] = data0;
            allData[1] = data2;
            allData[2] = data3;
            allData[3] = data4;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getColumnarData" + e.getMessage());
            return AjaxMessage.failed("数据异常");
        }
        return AjaxMessage.succeed(allData);
    }

    /**
     * 方法描述：利润报表
     * 作   者：DongLiu
     * 日   期：2017/12/11 15:21
     */
    @RequestMapping(value = "/getProfitDetail", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_REPORT}, logical = Logical.OR)
    public AjaxMessage getProfitDetail(@RequestBody StatisticDetailQueryDTO param) throws Exception {
        // 获取主营业务收入getMainBusinessIncome
        param.setCurrentCompanyId(this.currentCompanyId);
        List<ProfitStatementTableDTO> list = statisticService.getProfitStatement(param);
        return AjaxMessage.succeed(list);
    }

    /**
     * 描述     获取收支明细标题栏过滤条件列表
     * 日期     2018/8/15
     * @author  张成亮
     * @return  标题栏过滤条件
     * @param   query 收支明细查询条件
     **/
    @RequestMapping(value = "/getTitleFilter",method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getTitleFilter(@RequestBody StatisticDetailQueryDTO query) throws Exception{
        updateCurrentUserInfo(query);
        StatisticTitleFilterDTO result = statisticService.getTitleFilter(query);
        return AjaxMessage.succeed(result);
    }


    /**
     * 费用类型（收支明细-台账）
     */
    @RequestMapping(value = "/getCostType",method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    public AjaxMessage getData() throws Exception{
        StatisticDetailQueryDTO dto = new StatisticDetailQueryDTO();
        dto.setCombineCompanyId(this.currentCompanyId);
        dto.setCurrentCompanyId(this.currentCompanyId);
        return AjaxMessage.succeed(statisticService.getFeeTypeList(dto));
    }

    /**
     * 费用类型-分类统计
     */
    @RequestMapping(value = "/getCategoryTypeList",method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getCategoryTypeList(@RequestBody StatisticDetailQueryDTO dto) throws Exception{
//        if(StringUtil.isNullOrEmpty(dto.getCompanyId())){
//            dto.setCompanyId(this.currentCompanyId);
//        }
        dto.setCurrentCompanyId(this.currentCompanyId);
        return AjaxMessage.succeed(statisticService.getCategoryTypeList(dto));
    }

    /**
     * 费用类型-分类统计
     */
    @RequestMapping(value = "/getStatisticClassicData",method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getStatisticClassicData(@RequestBody StatisticDetailQueryDTO dto) throws Exception{
        dto.setCurrentCompanyId(this.currentCompanyId);
        return AjaxMessage.succeed(statisticService.getStatisticClassicData(dto));
    }


}
