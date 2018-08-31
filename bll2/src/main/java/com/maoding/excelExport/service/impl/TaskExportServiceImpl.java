package com.maoding.excelExport.service.impl;

import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.bean.AjaxMessage;
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
public class TaskExportServiceImpl extends BaseExportServiceImpl<ProjectDesignTaskShow,ProjectDesignTaskShow,QueryProjectTaskDTO> {
    @Autowired
    private ProjectTaskService projectTaskService;

    @Override
    List<Map<String, ExcelDataDTO>> getExcelDataList(List<ProjectDesignTaskShow> dataList, List<CoreShowDTO> titleList) {

        List<Map<String,ExcelDataDTO>> excelDataList = new ArrayList<>();
        dataList.stream().forEach((ProjectDesignTaskShow d) ->{
            Map<String,ExcelDataDTO> map = new HashMap<>();
            map.put("任务名称",new ExcelDataDTO(d.getTaskName(),1));
            map.put("任务描述",new ExcelDataDTO(d.getTaskRemark(),1));
            map.put("任务负责人",new ExcelDataDTO(d.getPersonInCharge(),1));
            map.put("设计人员",new ExcelDataDTO(getUserNames(d.getDesignUser()),1));
            map.put("校对人员",new ExcelDataDTO(getUserNames(d.getCheckUser()),1));
            map.put("审核人员",new ExcelDataDTO(getUserNames(d.getExamineUser()),1));
            map.put("计划进度",new ExcelDataDTO(d.getPlanStartTime()==null || d.getPlanEndTime()==null?"":(formatDate(d.getPlanStartTime()+"")+"~"+formatDate(d.getPlanEndTime()+"")+" "+"|"+" 共"+transformDate(formatDate(d.getPlanStartTime()+""),formatDate(d.getPlanEndTime()+""))+"天"),1));
            map.put("进度提示",new ExcelDataDTO(d.getStatusText(),1));
            map.put("完成时间",new ExcelDataDTO(d.getCompleteDate(),1));
            map.put("完成情况",new ExcelDataDTO(d.getCompletion(),1));
            map.put("任务状态",new ExcelDataDTO(d.getStateHtml(),1));
            map.put("优先级",new ExcelDataDTO(d.getPriority(),1));
            excelDataList.add(map);

        });
        return excelDataList;
    }

    private String getUserNames(ProjectTaskProcessNodeDTO taskUser){
        List<String> nameList = new ArrayList<>();
        String names = "";
        for(ProjectDesignUser u:taskUser.getUserList()){
            names +=u.getUserName()+",";
        }
        if(names.length()>0){
            names = names.substring(0,names.length()-1);
        }
        return names;
    }

    //任务名称	任务描述	任务负责人	设计人员 	校对人员	审核人员	计划进度	进度提示	完成时间	完成情况	任务状态	优先级
    @Override
    List<CoreShowDTO> getTitleList(QueryProjectTaskDTO queryDTO) {
        List<CoreShowDTO> titleList = Arrays.asList(new CoreShowDTO("1","任务名称")
                ,new CoreShowDTO("2","任务描述")
                , new CoreShowDTO("3","任务负责人")
                , new CoreShowDTO("4","设计人员")
                , new CoreShowDTO("5","校对人员")
                , new CoreShowDTO("6","审核人员")
                , new CoreShowDTO("7","计划进度")
                , new CoreShowDTO("8","进度提示")
                , new CoreShowDTO("8","完成时间")
                , new CoreShowDTO("8","完成情况")
                , new CoreShowDTO("8","任务状态")
                , new CoreShowDTO("8","优先级")
        );
        return titleList;
    }

    @Override
    int exportTitle(List<CoreShowDTO> titleList, ProjectDesignTaskShow statisticSum, QueryProjectTaskDTO dto, Sheet sheet, Workbook wb) {
        int rowNum = 0;
        this.setExcelTitle(titleList,sheet,wb,rowNum);
        return rowNum;
    }

    @Override
    public AjaxMessage exportDownloadResource(HttpServletResponse response, QueryProjectTaskDTO query) throws Exception {

        //1.查找列表数据
        ProductTaskInfoDTO info = new ProductTaskInfoDTO();
        String currentCompanyUserId = query.getCurrentCompanyUserId();
        String accountId = query.getAccountId();
        String projectId = query.getProjectId();
//        info.setOrgList(this.getSelectOrg(query,projectEntity));
        String companyId = query.getCompanyId();//此句必须在getSelectOrg后面。因为在getSelectOrg中可能改变了companyId
        List<ProjectDesignTaskShow> dataList = projectTaskService.getProjectDesignTaskList(companyId, query.getProjectId(), currentCompanyUserId,query.getIssueTaskId());
        return this.exportDownloadResource(response,dataList,null,query);
    }

    /**
     * 标准化时间显示
     * 对页面：计划进度显示时间格式调整输出到Excel
     * yyyy-MM-dd HH:mm:ss
     * @param dateStr
     * @return
     */
    private String formatDate(String dateStr) {
        String[] aStrings = dateStr.split(" ");
        // 5
        if (aStrings[1].equals("Jan")) {
            aStrings[1] = "01";
        }
        if (aStrings[1].equals("Feb")) {
            aStrings[1] = "02";
        }
        if (aStrings[1].equals("Mar")) {
            aStrings[1] = "03";
        }
        if (aStrings[1].equals("Apr")) {
            aStrings[1] = "04";
        }
        if (aStrings[1].equals("May")) {
            aStrings[1] = "05";
        }
        if (aStrings[1].equals("Jun")) {
            aStrings[1] = "06";
        }
        if (aStrings[1].equals("Jul")) {
            aStrings[1] = "07";
        }
        if (aStrings[1].equals("Aug")) {
            aStrings[1] = "08";
        }
        if (aStrings[1].equals("Sep")) {
            aStrings[1] = "09";
        }
        if (aStrings[1].equals("Oct")) {
            aStrings[1] = "10";
        }
        if (aStrings[1].equals("Nov")) {
            aStrings[1] = "11";
        }
        if (aStrings[1].equals("Dec")) {
            aStrings[1] = "12";
        }
        return aStrings[5] + "-" + aStrings[1] + "-" + aStrings[2];
    }

    //两个日期相减获得（页面：计划进度  共*天）
    private String transformDate(String startTime,String endTime){
        String dateStart = startTime;
        String dateStop = endTime;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
            //毫秒ms
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;//秒
            long diffMinutes = diff / (60 * 1000) % 60;//分钟
            long diffHours = diff / (60 * 60 * 1000) % 24;//小时
            long diffDays = diff / (24 * 60 * 60 * 1000);//天

            //当天也算一天
        return (1+diffDays)+"";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
