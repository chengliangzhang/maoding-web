package com.maoding.excelExport.service;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.statistic.dto.StatisticDetailDTO;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BaseExportService<T,B,C extends BaseDTO> {

    /**
     * 导出excel的数据填充到excel中，供exportDownloadResource调用
     */
    Workbook getExportedResource(List<T> dataList, B statisticSum, C queryDTO)  throws Exception;

    /**
     * 导出excel通用接口
     */
    AjaxMessage exportDownloadResource(HttpServletRequest request, HttpServletResponse response, C queryDTO) throws Exception;
}
