package com.maoding.excelExport.service;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.statistic.dto.StatisticDetailDTO;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BaseExportService<T,B,C extends BaseDTO> {

    Workbook getExportedResource(List<T> dataList, B statisticSum, C queryDTO)  throws Exception;

    AjaxMessage exportDownloadResource(HttpServletResponse response, C queryDTO) throws Exception;
}
