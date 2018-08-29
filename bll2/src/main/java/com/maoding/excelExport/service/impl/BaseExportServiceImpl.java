package com.maoding.excelExport.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.ExcelUtils;
import com.maoding.excelExport.dto.ExcelDataDTO;
import com.maoding.excelExport.service.BaseExportService;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public abstract class BaseExportServiceImpl<T,B,C extends BaseDTO>  extends NewBaseService implements BaseExportService<T,B,C> {

    abstract List<Map<String,ExcelDataDTO>> getExcelDataList(List<T> dataList,List<CoreShowDTO> titleList);

    abstract List<CoreShowDTO> getTitleList(C queryDTO);

    abstract int exportTitle(List<CoreShowDTO> titleList,B statisticSum, C dto,Sheet sheet,Workbook wb);

    @Override
    public Workbook getExportedResource(List<T> dataList, B statisticSum, C queryDTO) throws Exception {
        Workbook wb = ExcelUtils.getWorkbook();
        wb.createSheet();
        Sheet sht = wb.getSheetAt(0);
        if (sht != null) {
            exportToSheet(dataList,statisticSum,queryDTO,wb);
        }
        return wb;
    }

    public AjaxMessage exportDownloadResource(HttpServletResponse response,  List<T> dataList,B statisticSum,C queryDTO) throws Exception {
        //填充excel数据
        Workbook wb = this.getExportedResource(dataList,statisticSum,queryDTO);
        ServletOutputStream out = null;
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        //2.设置文件头：最后一个参数是设置下载文件名，需转换为UTF-8
        try {
            String fn = java.net.URLEncoder.encode(queryDTO.getExcelFileName(), "UTF-8");
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

    public void exportToSheet(List<T> dataList, B statisticSum, C dto,Workbook wb) throws Exception{
        Sheet sheet = wb.getSheetAt(0);
        //处理参数
        List<CoreShowDTO> titleList = this.getTitleList(dto);
        int firstRow = this.exportTitle(titleList,statisticSum,dto, sheet,wb);
        firstRow ++;
        List<Map<String,ExcelDataDTO>> excelDataList = this.getExcelDataList(dataList,titleList);
        for(int r=0;r<excelDataList.size();r++,firstRow++){
            Map<String,ExcelDataDTO> data = excelDataList.get(r);
            Row row = this.getRow(sheet,firstRow);
            for(int col=0;col<titleList.size();col++){
                Cell c = row.createCell(col);
                String key = titleList.get(col).getName();
                c.setCellValue(getCelText(data.get(key),wb));
            }
        }
    }

    void setCellStyle(Cell cell,Workbook wb){
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        Font font = wb.createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    public Row getRow(Sheet sheet,int i){
        Row r = sheet.createRow(i);
        r.setHeight((short) 500);
        return r;
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

    public void setExcelTitle(List<CoreShowDTO> titleList,Sheet sheet,Workbook wb,int rowNum){
        Row row = getRow(sheet,rowNum);
        for(int col=0;col<titleList.size();col++){
            Cell c = row.createCell(col);
            c.setCellValue(titleList.get(col).getName());
            this.setCellStyle(c,wb);
        }
    }
}
