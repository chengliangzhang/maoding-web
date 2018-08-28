package com.maoding.excelExport.service.impl;

import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.ExcelUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.excelExport.dto.ExcelDataDTO;
import com.maoding.excelExport.service.BalanceDetailExportService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.statistic.dto.StatisticDetailDTO;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;
import com.maoding.statistic.service.StatisticService;
import com.maoding.task.dto.ProjectDesignTaskShow;
import com.maoding.task.dto.ProjectProductTaskDTO;
import com.maoding.task.dto.ProjectTaskExportDTO;
import com.maoding.task.dto.QueryProjectTaskDTO;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

import static com.maoding.core.util.DateUtils.date_sdf2;

@Service("balanceDetailExportService")
public class BalanceDetailExportServiceImpl  extends NewBaseService implements BalanceDetailExportService {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private CompanyDao companyDao;

    String amountStr = "金额（元）";

    @Override
    public Workbook getExportedResource(List<StatisticDetailDTO> dataList, StatisticDetailSummaryDTO statisticSum, StatisticDetailQueryDTO queryDTO) throws Exception {
        Workbook wb = ExcelUtils.getWorkbook();
        wb.createSheet();
        Sheet sht = wb.getSheetAt(0);
        if (sht != null) {
            exportToSheet(dataList,statisticSum,queryDTO,wb);
        }
        return wb;
    }

    @Override
    public AjaxMessage exportDownloadResource(HttpServletResponse response, StatisticDetailQueryDTO queryDTO) throws Exception {
        //1.查找列表数据
        List<StatisticDetailDTO> dataList = statisticService.getExpensesDetailLedger(queryDTO);
        //2.查找统计
        StatisticDetailSummaryDTO statisticSum = statisticService.getStatisticDetailSummary(queryDTO);
        if(statisticSum!=null && statisticSum.getPay()!=null){
            statisticSum.setPay(new BigDecimal("0").subtract(statisticSum.getPay()));
        }
        //填充excel数据
        Workbook wb = this.getExportedResource(dataList,statisticSum,queryDTO);
        ServletOutputStream out = null;
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        //2.设置文件头：最后一个参数是设置下载文件名，需转换为UTF-8
        try {
            String fn = java.net.URLEncoder.encode("台账数据.xls", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fn);
            out = response.getOutputStream();
            if (wb != null) {
                wb.write(out);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            AjaxMessage.failed("文件名转换失败");
        }finally {
            try {
                if (wb != null)
                    wb.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return AjaxMessage.succeed("获取成功");
    }

    private void exportToSheet(List<StatisticDetailDTO> dataList, StatisticDetailSummaryDTO statisticSum, StatisticDetailQueryDTO dto,Workbook wb) throws Exception{
        Sheet sheet = wb.getSheetAt(0);
        //处理参数
        List<String> titleList = this.getTitleList();
        int firstRow = exportTitle(titleList,statisticSum,dto, sheet);
        firstRow ++;
        List<Map<String,ExcelDataDTO>> excelDataList = this.getExcelDataList(dataList);
        for(int r=0;r<excelDataList.size();r++,firstRow++){
            Map<String,ExcelDataDTO> data = excelDataList.get(r);
            Row row = sheet.createRow(firstRow);
            for(int col=0;col<titleList.size();col++){
                Cell c = row.createCell(col);
                c.setCellValue(getCelText(data.get(titleList.get(col)),wb));
            }
        }
    }

    /**
     * 设置单元格数据
     */
    RichTextString getCelText(ExcelDataDTO data,Workbook wb){
        CreationHelper helper = wb.getCreationHelper();
        if(data.getData()==null){
            data.setData("");
        }
        RichTextString str = helper.createRichTextString(data.getData().toString());
        Font font = wb.createFont();
        if(data.getColor()==2){
            font.setColor(HSSFColor.RED.index);
        }
        if(data.getColor()==3){
            font.setColor(HSSFColor.GREEN.index);
        }
        str.applyFont(font);
        return str;
    }

    int exportTitle(List<String> titleList,StatisticDetailSummaryDTO statisticSum, StatisticDetailQueryDTO dto,Sheet sheet){

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
        Row rowOne = sheet.createRow(0);
        Cell currentCompanyCell = rowOne.createCell(0);
        currentCompanyCell.setCellValue("当前组织："+ getCompanyName(dto));
        Cell timeCell = rowOne.createCell(1);
        timeCell.setCellValue("导出时间段："+ dto.getStartDateStr() +"~"+dto.getEndDateStr());
        Cell operateTimeCell = rowOne.createCell(2);
        operateTimeCell.setCellValue("导出时间："+ DateUtils.date2Str(DateUtils.getDate(),DateUtils.date_sdf2));

        //填充第二行的数据
        Row rowTwo = sheet.createRow(1);
        Cell currentBalanceAmount = rowTwo.createCell(0);
        currentBalanceAmount.setCellValue("当前余额："+ statisticSum.getBalance());
        Cell totalAmount = rowTwo.createCell(1);
        totalAmount.setCellValue("合计金额："+ statisticSum.getAmount());
        Cell receiveAmount = rowTwo.createCell(2);
        receiveAmount.setCellValue("累计收入："+ statisticSum.getGain());
        Cell payAmount = rowTwo.createCell(2);
        payAmount.setCellValue("累计支出："+ statisticSum.getPay());

        int rowNum = 2;
        Row row = sheet.createRow(rowNum);
        for(int col=0;col<titleList.size();col++){
            Cell c = row.createCell(col);
            c.setCellValue(titleList.get(col));
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

    List<String> getTitleList(){
        List<String> titleList = Arrays.asList("日期",amountStr,"收支分类","收支分类子项","备注","收款组织","付款组织","关联项目");
        return titleList;
    }

    List<Map<String,ExcelDataDTO>> getExcelDataList(List<StatisticDetailDTO> dataList){
        List<Map<String,ExcelDataDTO>> excelDataList = new ArrayList<>();
        dataList.stream().forEach(d->{
            Map<String,ExcelDataDTO> map = new HashMap<>();
            map.put("日期",new ExcelDataDTO(d.getCreateDate(),1));
            if(d.getProfitFee()!=null && d.getProfitFee().doubleValue()<0){
                map.put(amountStr,new ExcelDataDTO(d.getProfitFee().doubleValue(),2));
            }else {
                map.put(amountStr,new ExcelDataDTO(d.getProfitFee().doubleValue(),3));
            }
            map.put("收支分类",new ExcelDataDTO(d.getFeeTypeParentName(),1));
            map.put("收支分类子项",new ExcelDataDTO(d.getFeeTypeName(),1));
            map.put("备注",new ExcelDataDTO("",1));
            map.put("收款组织",new ExcelDataDTO(d.getToCompanyId(),1));
            map.put("付款组织",new ExcelDataDTO(d.getFromCompanyName(),1));
            map.put("关联项目",new ExcelDataDTO(d.getProjectName(),1));
            excelDataList.add(map);
        });
        return excelDataList;
    }
}
