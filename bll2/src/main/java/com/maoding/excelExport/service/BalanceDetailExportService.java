package com.maoding.excelExport.service;

import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.statistic.dto.StatisticDetailDTO;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;
import com.maoding.task.dto.ProjectDesignTaskShow;
import com.maoding.task.dto.ProjectTaskExportDTO;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BalanceDetailExportService {

    Workbook getExportedResource(List<StatisticDetailDTO> dataList, StatisticDetailSummaryDTO statisticSum,StatisticDetailQueryDTO queryDTO)  throws Exception;

    AjaxMessage exportDownloadResource(HttpServletResponse response, StatisticDetailQueryDTO queryDTO) throws Exception;
}
