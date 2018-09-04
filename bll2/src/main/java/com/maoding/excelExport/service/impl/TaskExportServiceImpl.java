package com.maoding.excelExport.service.impl;

import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.excelExport.dto.ExcelDataDTO;
import com.maoding.project.dto.ProjectDesignUser;
import com.maoding.project.dto.ProjectTaskProcessNodeDTO;
import com.maoding.task.dto.ProductTaskInfoDTO;
import com.maoding.task.dto.ProjectDesignTaskShow;
import com.maoding.task.dto.QueryProjectTaskDTO;
import com.maoding.task.service.ProjectTaskService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 作者：fyt
 * 时间：2018/8/30
 * 描述：生产安排导出
 */

@Service("taskExportService")
public class TaskExportServiceImpl extends BaseExportServiceImpl<ProjectDesignTaskShow, ProjectDesignTaskShow, QueryProjectTaskDTO> {
    @Autowired
    private ProjectTaskService projectTaskService;

    @Override
    List<Map<String, ExcelDataDTO>> getExcelDataList(List<ProjectDesignTaskShow> dataList, List<CoreShowDTO> titleList) {
        DateUtils dateUtils = new DateUtils();


        List<Map<String, ExcelDataDTO>> excelDataList = new ArrayList<>();
        dataList.stream().forEach((ProjectDesignTaskShow d) -> {
            String st = "";
            String et = "";

            if(!StringUtil.isNullOrEmpty(d.getPlanStartTime()) &&!StringUtil.isNullOrEmpty(d.getPlanEndTime()) ){

                st = dateUtils.formatDate(d.getPlanStartTime());
                et = dateUtils.formatDate(d.getPlanEndTime());
            }

            Map<String, ExcelDataDTO> map = new HashMap<>();

            map.put("任务名称", new ExcelDataDTO(d.getTaskName(), 1));
            map.put("任务描述", new ExcelDataDTO(d.getTaskRemark(), 1));
            map.put("任务负责人", new ExcelDataDTO(d.getPersonInCharge(), 1));
            map.put("设计人员", new ExcelDataDTO(getUserNames(d.getDesignUser()), 1));
            map.put("校对人员", new ExcelDataDTO(getUserNames(d.getCheckUser()), 1));
            map.put("审核人员", new ExcelDataDTO(getUserNames(d.getExamineUser()), 1));
            map.put("计划进度", new ExcelDataDTO(d.getPlanStartTime() == null || d.getPlanEndTime() == null ? "" : (st + "~" + et + " |共" + d.getAllDay() + "天"), 1));
            map.put("进度提示", new ExcelDataDTO(d.getStatusText(), 1));
            map.put("完成时间", new ExcelDataDTO(d.getCompleteDate(), 1));
            map.put("完成情况", new ExcelDataDTO(d.getCompletion(), 1));
            map.put("任务状态", new ExcelDataDTO(d.getStateHtml(), 1));
            map.put("优先级", new ExcelDataDTO(d.getPriority(), 1));
            excelDataList.add(map);

        });
        return excelDataList;
    }

    private String getUserNames(ProjectTaskProcessNodeDTO taskUser) {
        List<String> nameList = new ArrayList<>();
        String names = "";
        for (ProjectDesignUser u : taskUser.getUserList()) {
            names += u.getUserName() + ",";
        }
        if (names.length() > 0) {
            names = names.substring(0, names.length() - 1);
        }
        return names;
    }

    @Override
    List<CoreShowDTO> getTitleList(QueryProjectTaskDTO queryDTO) {
        List<CoreShowDTO> titleList = Arrays.asList(new CoreShowDTO("1", "任务名称")
                , new CoreShowDTO("2", "任务描述")
                , new CoreShowDTO("3", "任务负责人")
                , new CoreShowDTO("4", "设计人员")
                , new CoreShowDTO("5", "校对人员")
                , new CoreShowDTO("6", "审核人员")
                , new CoreShowDTO("7", "计划进度")
                , new CoreShowDTO("8", "进度提示")
                , new CoreShowDTO("9", "完成时间")
                , new CoreShowDTO("10", "完成情况")
                , new CoreShowDTO("11", "任务状态")
                , new CoreShowDTO("12", "优先级")
        );
        return titleList;
    }

    @Override
    int exportTitle(List<CoreShowDTO> titleList, ProjectDesignTaskShow statisticSum, QueryProjectTaskDTO dto, Sheet sheet, Workbook wb) {
        int rowNum = 0;
        this.setExcelTitle(titleList, sheet, wb, rowNum);
        return rowNum;
    }

    @Override
    public AjaxMessage exportDownloadResource(HttpServletResponse response, QueryProjectTaskDTO query) throws Exception {

        //1.查找列表数据
        String currentCompanyUserId = query.getCurrentCompanyUserId();
        String companyId = query.getCompanyId();//此句必须在getSelectOrg后面。因为在getSelectOrg中可能改变了companyId
        List<ProjectDesignTaskShow> dataList = projectTaskService.getProjectDesignTaskList(companyId, query.getProjectId(), currentCompanyUserId, query.getIssueTaskId());
        return this.exportDownloadResource(response, dataList, null, query);
    }

}
