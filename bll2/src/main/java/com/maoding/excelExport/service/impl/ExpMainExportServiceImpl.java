package com.maoding.excelExport.service.impl;


import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.excelExport.dto.ExcelDataDTO;
import com.maoding.financial.dto.ExpMainDTO;
import com.maoding.financial.service.ExpMainService;
import com.maoding.task.dto.ProductTaskInfoDTO;
import com.maoding.task.dto.ProjectDesignTaskShow;
import com.maoding.task.dto.QueryProjectTaskDTO;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 作者：fyt
 * 时间：2018/8/31
 * 未测试
 * 描述：审批管理→报销/费用Excel导出功能
 */
@Service("expMainExportService")
public class ExpMainExportServiceImpl extends BaseExportServiceImpl<ExpMainDTO, ExpMainDTO, ExpMainDTO> {

    @Autowired
    private ExpMainService expMainService;

    @Override
    List<Map<String, ExcelDataDTO>> getExcelDataList(List<ExpMainDTO> dataList, List<CoreShowDTO> titleList) {
        List<Map<String, ExcelDataDTO>> excelDataList = new ArrayList<>();
        dataList.stream().forEach((ExpMainDTO d) -> {
            Map<String, ExcelDataDTO> map = new HashMap<>();
            map.put("序号", new ExcelDataDTO(d.getTargetId(), 1));
            map.put("编号", new ExcelDataDTO(d.getExpNo(), 1));
            map.put("申请时间", new ExcelDataDTO(d.getExpDate(), 1));
            map.put("申请人", new ExcelDataDTO(d.getUserName(), 1));
            map.put("所在组织", new ExcelDataDTO(d.getCompanyName(), 1));
            map.put("用途说明", new ExcelDataDTO(d.getExpUse(), 1));
            map.put("申请金额（元）", new ExcelDataDTO(d.getExpSumAmount(), 1));
            map.put("审批人", new ExcelDataDTO(d.getAuditPersonName(), 1));
            map.put("审批时间", new ExcelDataDTO(d.getApproveDate(), 1));
            map.put("拨款时间", new ExcelDataDTO(d.getAllocationDate(), 1));

            excelDataList.add(map);

        });
        return excelDataList;
    }

    @Override
    List<CoreShowDTO> getTitleList(ExpMainDTO queryDTO) {
        List<CoreShowDTO> titleList = Arrays.asList(
                new CoreShowDTO("1", "序号")
                , new CoreShowDTO("2", "编号")
                , new CoreShowDTO("3", "申请时间")
                , new CoreShowDTO("4", "申请人")
                , new CoreShowDTO("5", "所在组织")
                , new CoreShowDTO("6", "用途说明")
                , new CoreShowDTO("7", "申请金额（元）")
                , new CoreShowDTO("8", "审批人")
                , new CoreShowDTO("9", "审批时间")
                , new CoreShowDTO("10", "拨款时间")
        );

        return titleList;
    }

    @Override
    int exportTitle(List<CoreShowDTO> titleList, ExpMainDTO statisticSum, ExpMainDTO dto, Sheet sheet, Workbook wb) {
        int rowNum = 0;
        this.setExcelTitle(titleList, sheet, wb, rowNum);
        return rowNum;
    }

    @Override
    public AjaxMessage exportDownloadResource(HttpServletRequest request, HttpServletResponse response, ExpMainDTO query) throws Exception {
        //1.查找列表数据
        Map<String, Object> map = new HashMap<>();
        map.put("type", query.getType());
        map.put("company_id", query.getCompanyId());
        map.put("startPage", 0);
        map.put("endPage", 10);

        List<ExpMainDTO> dataList = expMainService.getExpMainListPageForSummary(map);

        return this.exportDownloadResource(request,response, dataList, null, query);
    }
}
