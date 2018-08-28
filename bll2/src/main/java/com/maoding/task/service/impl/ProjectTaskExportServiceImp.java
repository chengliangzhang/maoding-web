package com.maoding.task.service.impl;

import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.ExcelUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.service.ProjectService;
import com.maoding.task.dao.ProjectTaskDao;
import com.maoding.task.dto.ProjectDesignTaskShow;
import com.maoding.task.dto.ProjectProductTaskDTO;
import com.maoding.task.dto.ProjectTaskExportDTO;
import com.maoding.task.dto.QueryProjectTaskDTO;
import com.maoding.task.service.ProjectTaskExportService;
import com.maoding.task.service.ProjectTaskService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.maoding.core.util.DateUtils.date_sdf2;

/**
 * Created by Chengliang.zhang on 2017/4/24.
 */
@Service("projectTaskExportService")
public class ProjectTaskExportServiceImp implements ProjectTaskExportService {

    @Autowired
    private ProjectTaskDao projectTaskDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ProjectService projectService;

    final private static int PROJECT_INFO_ROW = 0;
    final private static int PHASE_DEMO_ROW = 3;
    final private static int TASK_DEMO_ROW = 4;
    final private static int FIRST_NEW_ROW = TASK_DEMO_ROW + 1;
    final private static int LAST_COL = 7;
    final private static String PHASE_STYLE = "phase";
    final private static String TASK_STYLE = "task";


    public AjaxMessage exportDownloadResource(HttpServletResponse response, ProjectTaskExportDTO dto) throws Exception {
        if (response == null) AjaxMessage.failed("申请参数错误");
        if ((dto.getProjectId() == null) || (dto.getDestFileName() == null)) AjaxMessage.failed("参数错误");

        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        //2.设置文件头：最后一个参数是设置下载文件名，需转换为UTF-8
        try {
            String fn = java.net.URLEncoder.encode(dto.getDestFileName(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fn);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            AjaxMessage.failed("文件名转换失败");
        }

        AjaxMessage msg = (new AjaxMessage()).setCode("0").setInfo("操作成功");
        Workbook wb = null;
        ServletOutputStream out = null;
        try {
            List<ProjectDesignTaskShow> list = projectTaskService.getProjectDesignTaskList(dto.getCompanyId(),dto.getProjectId(),dto.getCompanyUserId());
//            if(dto.getType()==1){
//                list = projectTaskService.getProjectDesignTaskShowList(
//                        dto.getCompanyId(),dto.getProjectId(),dto.getCompanyUserId());
//            } else{
//                QueryProjectTaskDTO query = new QueryProjectTaskDTO();
//                query.setProjectId(dto.getProjectId());
//                List<ProjectProductTaskDTO> productList = projectTaskService.getProductTaskOverview(query);
//                list = this.convertProjectProductList(productList);
//            }
            out = response.getOutputStream();
            wb = getExportedResource(list,dto);
            if (wb != null) {
                wb.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
            msg.setCode("1").setInfo("输出异常");
        } finally {
            try {
                if (wb != null)
                    wb.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    @Override
    public Workbook getExportedResource(List<ProjectDesignTaskShow> objects, ProjectTaskExportDTO dto) throws Exception{
        if (dto.getProjectId() == null) return null;
        Workbook wb = ExcelUtils.getWorkbook(dto.getTemplateFileName());
        if (wb == null) {
            wb = ExcelUtils.getWorkbook();
            if (wb == null) return null;
            createTitle(wb.createSheet());
        }

        Sheet sht = wb.getSheetAt(0);
        if (sht != null) {
            exportToSheet(objects, sht, dto, getStyles(sht));
        }
        return wb;
    }

    private void exportToSheet(List<ProjectDesignTaskShow> objects, Sheet sheet, ProjectTaskExportDTO dto, Map<String,CellStyle> styles) throws Exception{
        int r = exportTitle(dto, sheet, styles);
        for (ProjectDesignTaskShow task : objects){
            if (StringUtil.isNullOrEmpty(task.getTaskPid())){
                exportPhaseRow(task,((r>=FIRST_NEW_ROW)?sheet.createRow(r++):sheet.getRow(r++)),styles);
            } else if (!StringUtil.isNullOrEmpty(task.getPersonInCharge()) &&
                    ((dto.getType() != 1) || (dto.getCompanyId().equals(task.getDesignOrgId())))){
                exportTaskRow(task,((r>=FIRST_NEW_ROW)?sheet.createRow(r++):sheet.getRow(r++)),styles);
            }
        }
    }

    private void createTitle(Sheet sheet){
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,LAST_COL));
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,LAST_COL));
        int r = 0;
        Row row = sheet.createRow(r++);
        Cell c = row.createCell(0);
        c.setCellValue("工作安排表（个人组织）");
        row = sheet.createRow(r++);
        c = row.createCell(0);
        c.setCellValue("项目名称：       经营负责人：      设计负责人：    导出时间：");
        row = sheet.createRow(r++);
        row.getSheet().addMergedRegion(new CellRangeAddress(row.getRowNum(),row.getRowNum(),0,1));
        int col = 0;
        c = row.createCell(col++);
        c.setCellValue("任务名称");
        col++;
        c = row.createCell(col++);
        c.setCellValue("设计组织");
        c = row.createCell(col++);
        c.setCellValue("任务负责人");
        c = row.createCell(col++);
        c.setCellValue("进度计划");
        c = row.createCell(col++);
        c.setCellValue("状态");
        c = row.createCell(col++);
        c.setCellValue("任务描述");
        row = sheet.createRow(r++);
        row.getSheet().addMergedRegion(new CellRangeAddress(row.getRowNum(),row.getRowNum(),0,1));
        c = row.createCell(0);
        c.setCellValue("阶段");
        row = sheet.createRow(r++);
        c = row.createCell(1);
        c.setCellValue("任务");
    }

    private Map<String,CellStyle> getStyles(Sheet sheet){
        if (sheet == null) return null;
        Map<String,CellStyle> styles = new HashMap<>();
        getRowStyle(sheet.getRow(PHASE_DEMO_ROW),styles,PHASE_STYLE);
        getRowStyle(sheet.getRow(TASK_DEMO_ROW),styles,TASK_STYLE);
        return styles;
    }

    private int exportTitle(ProjectTaskExportDTO dto, Sheet sheet, Map<String,CellStyle> styles) throws Exception{
        if (dto.getType() == 2){
            Row row = sheet.getRow(PROJECT_INFO_ROW);
            Cell c = row.getCell(0);
            c.setCellValue(c.getStringCellValue().replace("个人","所有"));
        }
        Row row = sheet.getRow(PROJECT_INFO_ROW+1);
        Cell c = row.getCell(0);
        ProjectEntity entity = projectDao.selectById(dto.getProjectId());
        c.setCellValue("项目名称：" + getProjectName(entity) + " 经营负责人：" + getManagerName(entity,dto.getCompanyId()) +
                " 设计负责人：" + getDesignerName(entity,dto.getCompanyId()) + " 导出时间：" + getCurrentDateString());
        return PHASE_DEMO_ROW;
    }

    private String getProjectName(ProjectEntity entity){
        if (entity == null) return "";
        return entity.getProjectName();
    }
    private String getManagerName(ProjectEntity entity, String companyId) throws Exception{
        if ((entity == null) || (companyId == null)) return "";
        return projectService.getManagerName(entity.getId(),companyId);
    }
    private String getDesignerName(ProjectEntity entity, String companyId) throws Exception{
        if ((entity == null) || (companyId == null)) return "";
        return projectService.getDesignerName(entity.getId(),companyId);
    }
    private String getCurrentDateString(){
        return DateUtils.getDataString(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
    }

    private void exportPhaseRow(ProjectDesignTaskShow data, Row row, Map<String,CellStyle> styles){
        int col = 0;
        if (row.getRowNum()>PHASE_DEMO_ROW)
            row.getSheet().addMergedRegion(new CellRangeAddress(row.getRowNum(),row.getRowNum(),col,1));
        Cell c = (row.getRowNum()>=TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        c.setCellValue(getTaskTreeName(data.getId())); //任务名
        col++;
        c = (row.getRowNum()>TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        c.setCellValue(StringUtil.isNullOrEmpty(data.getDepartName()) ? data.getDesignOrg() :
                data.getDesignOrg() + "(" + data.getDepartName() + ")");  //组织部门
        c = (row.getRowNum()>TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        c.setCellValue(data.getPersonInCharge()); //负责人
        c = (row.getRowNum()>TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        if ((data.getPlanStartTime() != null) || (data.getPlanEndTime() != null)) {
            StringBuffer t = new StringBuffer();
            if (data.getPlanStartTime() != null){
                t.append(DateUtils.date2Str(data.getPlanStartTime(),date_sdf2));
            }
            t.append("～");
            if (data.getPlanEndTime() != null){
                t.append(DateUtils.date2Str(data.getPlanEndTime(),date_sdf2));
            }
            c.setCellValue(t.toString()); //计划时间
        }
        c = (row.getRowNum()>TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        c.setCellValue(data.getStateHtml()); //状态
        c = (row.getRowNum()>TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        c.setCellValue(data.getTaskRemark()); //任务描述
        setRowStyle(row, styles, TASK_STYLE);
        setRowStyle(row,styles,PHASE_STYLE);
    }

    private void exportTaskRow(ProjectDesignTaskShow data, Row row, Map<String,CellStyle> styles){
        int col = 1;
        Cell c = (row.getRowNum()>TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        c.setCellValue(getTaskTreeName(data.getId())); //任务名
        c = (row.getRowNum()>TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        c.setCellValue(StringUtil.isNullOrEmpty(data.getDepartName()) ? data.getDesignOrg() :
                data.getDesignOrg() + "(" + data.getDepartName() + ")");  //组织部门
        c = (row.getRowNum()>TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        c.setCellValue(data.getPersonInCharge()); //负责人
        c = (row.getRowNum()>TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        if ((data.getPlanStartTime() != null) || (data.getPlanEndTime() != null)) {
            StringBuffer t = new StringBuffer();
            if (data.getPlanStartTime() != null){
                t.append(DateUtils.date2Str(data.getPlanStartTime(),date_sdf2));
            }
            t.append("～");
            if (data.getPlanEndTime() != null){
                t.append(DateUtils.date2Str(data.getPlanEndTime(),date_sdf2));
            }
            c.setCellValue(t.toString()); //计划时间
        }
        c = (row.getRowNum()>TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        c.setCellValue(data.getStateHtml()); //状态
        c = (row.getRowNum()>TASK_DEMO_ROW) ? row.createCell(col++) : row.getCell(col++);
        c.setCellValue(data.getTaskRemark()); //任务描述
        setRowStyle(row, styles, TASK_STYLE);
    }

    private void getRowStyle(Row row, Map<String,CellStyle> styles, String styleType){
        if (row != null) {
            for (int i=0; i<LAST_COL; i++) {
                Cell c = row.getCell(i);
                if ((c != null) && (c.getCellStyle() != null)) {
                    CellStyle style = row.getSheet().getWorkbook().createCellStyle();
                    style.cloneStyleFrom(c.getCellStyle());
                    styles.put(styleType + i, style);
                }
            }
        }
    }

    private void setRowStyle(Row row, Map<String,CellStyle> styles, String styleType){
        for (int i=0; i<LAST_COL; i++){
            if (styles.containsKey(styleType + i)){
                Cell c = row.getCell(i);
                if (c == null){
                    c = row.createCell(i);
                }
                if (c != null){
                    c.setCellStyle(styles.get(styleType + i));
                }
            }
        }
    }

    private String getTaskTreeName(String id){
        return projectTaskDao.getTaskParentName(id);
    }

    /**
     * 方法描述：生产总览的数据转换
     * 作者：MaoSF
     * 日期：2017/6/1
     * @param:
     * @return:
     */
    private List<ProjectDesignTaskShow> convertProjectProductList(List<ProjectProductTaskDTO> productList) throws Exception{
        List<ProjectDesignTaskShow> list = new ArrayList<>();
        for(ProjectProductTaskDTO dto: productList){
            list.add(convertProjectProduct(dto));
            for(ProjectProductTaskDTO dto1:dto.getChildList()){
                list.add(convertProjectProduct(dto1));
            }
        }

        return list;
    }

    private ProjectDesignTaskShow convertProjectProduct(ProjectProductTaskDTO dto){
        ProjectDesignTaskShow task = new ProjectDesignTaskShow();
        task.setId(dto.getId());
        task.setTaskPid(dto.getTaskPid());
        task.setTaskPath(dto.getTaskPath());
        task.setDesignOrg(dto.getCompanyName());
        task.setDepartName(dto.getDepartName());
        task.setTaskName(dto.getTaskName());
        task.setPersonInCharge(dto.getPersonInChargeName());
        task.setTaskState(dto.getTaskState());
        task.setStateHtml(dto.getStateHtml());
        task.setTaskRemark(dto.getTaskRemark());
        task.setPlanEndTime(dto.getPlanEndTime());
        task.setPlanStartTime(dto.getPlanStartTime());
        return task;
    }
}
