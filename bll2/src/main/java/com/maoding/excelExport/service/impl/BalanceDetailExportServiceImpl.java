package com.maoding.excelExport.service.impl;

import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.ExcelUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.excelExport.dto.ExcelDataDTO;
import com.maoding.org.dao.CompanyDao;
import com.maoding.statistic.dto.StatisticDetailDTO;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;
import com.maoding.statistic.service.StatisticService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Service("balanceDetailExportService")
public class BalanceDetailExportServiceImpl  extends BaseExportServiceImpl<StatisticDetailDTO,StatisticDetailSummaryDTO,StatisticDetailQueryDTO> {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private CompanyDao companyDao;

    String amountStr = "金额（元）";

    @Override
    public AjaxMessage exportDownloadResource(HttpServletResponse response, StatisticDetailQueryDTO queryDTO) throws Exception {
        //1.查找列表数据
        List<StatisticDetailDTO> dataList = statisticService.getExpensesDetailLedger(queryDTO);
        //2.查找统计
        StatisticDetailSummaryDTO statisticSum = statisticService.getStatisticDetailSummary(queryDTO);
        if(statisticSum!=null && statisticSum.getPay()!=null){
            statisticSum.setPay(new BigDecimal("0").subtract(statisticSum.getPay()));
        }
       return this.exportDownloadResource(response,dataList,statisticSum,queryDTO);
    }

    int exportTitle(List<CoreShowDTO> titleList,StatisticDetailSummaryDTO statisticSum, StatisticDetailQueryDTO dto,Sheet sheet,Workbook wb){

        //创建合并单元格
        CellRangeAddress cel1 = new CellRangeAddress(0,0,0,1);
        CellRangeAddress cel2 = new CellRangeAddress(0,0,2,3);
        CellRangeAddress cel3 = new CellRangeAddress(0,0,4,5);

        CellRangeAddress cel4 = new CellRangeAddress(1,1,0,1);
        CellRangeAddress cel5 = new CellRangeAddress(1,1,2,3);
        CellRangeAddress cel6 = new CellRangeAddress(1,1,4,5);
        CellRangeAddress cel7 = new CellRangeAddress(1,1,6,7);

        sheet.addMergedRegion(cel1);
        sheet.addMergedRegion(cel2);
        sheet.addMergedRegion(cel3);
        sheet.addMergedRegion(cel4);
        sheet.addMergedRegion(cel5);
        sheet.addMergedRegion(cel6);
        sheet.addMergedRegion(cel7);
        //填充第一行的数据
        Row rowOne = getRow(sheet,0);
        Cell currentCompanyCell = rowOne.createCell(0);
        currentCompanyCell.setCellValue("当前组织："+ getCompanyName(dto));
        Cell timeCell = rowOne.createCell(2);
        String startDateStr = dto.getStartDateStr();
        if(startDateStr==null){
            startDateStr = "";
        }else {
            startDateStr = startDateStr.replaceAll("-","/");
        }
        String endDateStr = dto.getEndDateStr();
        if(endDateStr==null){
            endDateStr = DateUtils.date2Str(DateUtils.getDate(),DateUtils.date_sdf2);
        }
        timeCell.setCellValue("导出时间段："+ startDateStr +"~"+endDateStr);
        Cell operateTimeCell = rowOne.createCell(4);
        operateTimeCell.setCellValue("导出时间："+ DateUtils.date2Str(DateUtils.getDate(),DateUtils.date_sdf2));

        //填充第二行的数据
        Row rowTwo = getRow(sheet,1);
        Cell currentBalanceAmount = rowTwo.createCell(0);
        currentBalanceAmount.setCellValue("当前余额："+ statisticSum.getSumBalance());
        Cell totalAmount = rowTwo.createCell(2);
        totalAmount.setCellValue("合计金额："+ statisticSum.getAmount());
        Cell receiveAmount = rowTwo.createCell(4);
        receiveAmount.setCellValue("累计收入："+ statisticSum.getGain());
        Cell payAmount = rowTwo.createCell(6);
        payAmount.setCellValue("累计支出："+ statisticSum.getPay());

        int rowNum = 2;
        Row row = getRow(sheet,rowNum);
        for(int col=0;col<titleList.size();col++){
            Cell c = row.createCell(col);
            c.setCellValue(titleList.get(col).getName());
            this.setCellStyle(c,wb);
        }
        return rowNum;
    }

    private String getCompanyName(StatisticDetailQueryDTO dto){
        String companyFlag = dto.getCombineCompanyId();
        if(StringUtil.isNullOrEmpty(companyFlag)){
            return "";
        }
        String companyId = null;
        if(companyFlag!=null && companyFlag.length()!=32){//以下情况都是合并报表
            if("root".equals(companyFlag)){//查询所有
                companyId = dto.getCurrentCompanyId();
            }else {
                if( companyFlag.indexOf("subCompanyId")>-1 || companyFlag.indexOf("partnerId")>-1){
                    companyId = companyFlag.substring(0,32);
                }
            }
        }else {
            companyId = companyFlag;
        }
        return this.companyDao.getCompanyName(companyId);
    }

    public List<CoreShowDTO> getTitleList(StatisticDetailQueryDTO queryDTO){
        List<CoreShowDTO> titleList = Arrays.asList(new CoreShowDTO("1","日期")
                ,new CoreShowDTO("2",this.amountStr)
                , new CoreShowDTO("3","收支分类")
                , new CoreShowDTO("4","收支分类子项")
                , new CoreShowDTO("5","备注")
                , new CoreShowDTO("6","收款组织")
                , new CoreShowDTO("7","付款组织")
                , new CoreShowDTO("8","关联项目")
               );
        return titleList;
    }

    public List<Map<String,ExcelDataDTO>> getExcelDataList(List<StatisticDetailDTO> dataList,List<CoreShowDTO> titleList){
        List<Map<String,ExcelDataDTO>> excelDataList = new ArrayList<>();
        dataList.stream().forEach(d->{
            Map<String,ExcelDataDTO> map = new HashMap<>();
            map.put("日期",new ExcelDataDTO(d.getProfitDate().replaceAll("-","/"),1));
            if(d.getProfitFee()!=null && d.getProfitFee().doubleValue()<0){
                double amount = 0 - d.getProfitFee().doubleValue();
                map.put(amountStr,new ExcelDataDTO(amount,2));
            }else {
                map.put(amountStr,new ExcelDataDTO(d.getProfitFee().doubleValue(),3));
            }
            map.put("收支分类",new ExcelDataDTO(d.getFeeTypeParentName(),1));
            map.put("收支分类子项",new ExcelDataDTO(d.getFeeTypeName(),1));
            map.put("备注",new ExcelDataDTO("",1));
            map.put("收款组织",new ExcelDataDTO(d.getToCompanyName(),1));
            map.put("付款组织",new ExcelDataDTO(d.getFromCompanyName(),1));
            map.put("关联项目",new ExcelDataDTO(d.getProjectName(),1));
            excelDataList.add(map);
        });
        return excelDataList;
    }

}
