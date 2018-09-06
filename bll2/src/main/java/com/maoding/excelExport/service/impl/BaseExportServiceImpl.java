package com.maoding.excelExport.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.ExcelUtils;
import com.maoding.excelExport.dto.ExcelDataDTO;
import com.maoding.excelExport.service.BaseExportService;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <T>:导出list列表的数据的类型
 * @param <B>:导出excel列表中，散列数据的类型，比如excel的头部数据
 * @param <C>：导出excel列表数据，需要请求参数的类型
 */
public abstract class BaseExportServiceImpl<T,B,C extends BaseDTO>  extends NewBaseService implements BaseExportService<T,B,C> {

    /**
     * 把要导出的list列表，重新封装成 Map<String,ExcelDataDTO>对象。T：导出list列表的数据的类型。
     * 每个excel导出的数据类型不一样，所有需要重写，把数据都转化成一样的格式
     */
    abstract List<Map<String,ExcelDataDTO>> getExcelDataList(List<T> dataList,List<CoreShowDTO> titleList);

    /**
     * 获取excel列表的标题（每个excel的标题不一样，所有需要重写该方法）
     */
    abstract List<CoreShowDTO> getTitleList(C queryDTO);

    /**
     * 处理excel的散列数据和标题栏目
     */
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

    public AjaxMessage exportDownloadResource(HttpServletRequest request, HttpServletResponse response, List<T> dataList, B statisticSum, C queryDTO) throws Exception {
        //填充excel数据
        Workbook wb = this.getExportedResource(dataList,statisticSum,queryDTO);
        ServletOutputStream out = null;
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        //2.设置文件头：最后一个参数是设置下载文件名，需转换为UTF-8
        try {
            setFileName(request,response,queryDTO.getExcelFileName());
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

    private void setFileName(HttpServletRequest request, HttpServletResponse response,String fileName) throws Exception{
        String  browserDetails = request.getHeader("User-Agent").toLowerCase();
        if(browserDetails != null && browserDetails.indexOf("firefox") > 0){
            fileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
        } else{
            fileName =  java.net.URLEncoder.encode(fileName, "UTF-8");
        }
        fileName += ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    }

    /**
     * 数据填充到excel中
     */
    public void exportToSheet(List<T> dataList, B statisticSum, C dto,Workbook wb) throws Exception{
        Sheet sheet = wb.getSheetAt(0);
        //获取标题
        List<CoreShowDTO> titleList = this.getTitleList(dto);
        //处理标题和统计数据（或许其他的散列数据）
        int firstRow = this.exportTitle(titleList,statisticSum,dto, sheet,wb);
        firstRow ++;
        //获取列表数
        List<Map<String,ExcelDataDTO>> excelDataList = this.getExcelDataList(dataList,titleList);
        //填充列表数据到excel中
        for(int r=0;r<excelDataList.size();r++,firstRow++){
            Map<String,ExcelDataDTO> data = excelDataList.get(r);
            Row row = this.getRow(sheet,firstRow);
            for(int col=0;col<titleList.size();col++){
                Cell c = row.createCell(col);
                String key = titleList.get(col).getName();
                ExcelDataDTO e = data.get(key);
                c.setCellValue(getCelText(e,wb));
            }
        }
    }

    /**
     * 处理每个单元格的样式
     */
    void setCellStyle(Cell cell,Workbook wb){
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        Font font = wb.createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 获取每一行，并且设置高度为500
     */
    public Row getRow(Sheet sheet,int i){
        Row r = sheet.createRow(i);
        r.setHeight((short) 500);
        return r;
    }

    /**
     * 设置单元格数据和字体颜色
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

    /**
     * 设置标题栏目的数据，填充到excel中
     */
    public void setExcelTitle(List<CoreShowDTO> titleList,Sheet sheet,Workbook wb,int rowNum){
        Row row = getRow(sheet,rowNum);
        for(int col=0;col<titleList.size();col++){
            Cell c = row.createCell(col);
            c.setCellValue(titleList.get(col).getName());
            this.setCellStyle(c,wb);
        }
    }
}
