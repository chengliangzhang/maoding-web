package com.maoding.excelExport.service.impl;

import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.excelExport.service.BalanceDetailExportService;
import com.maoding.statistic.dto.StatisticDetailDTO;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;
import com.maoding.statistic.service.StatisticService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

@Service("balanceDetailExportService")
public class BalanceDetailExportServiceImpl  extends NewBaseService implements BalanceDetailExportService {

    @Autowired
    private StatisticService statisticService;

    @Override
    public Workbook getExportedResource(List<StatisticDetailDTO> dataList, StatisticDetailSummaryDTO statisticSum, StatisticDetailQueryDTO queryDTO) throws Exception {
        return null;
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

        return null;
    }
}
