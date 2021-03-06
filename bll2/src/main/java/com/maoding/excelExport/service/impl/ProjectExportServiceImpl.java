package com.maoding.excelExport.service.impl;

import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.BeanUtilsEx;
import com.maoding.core.util.DateUtils;
import com.maoding.excelExport.dto.ExcelDataDTO;
import com.maoding.project.dto.ProjectQueryDTO;
import com.maoding.project.dto.ProjectVariableDTO;
import com.maoding.project.dto.TitleColumnDTO;
import com.maoding.project.dto.TitleQueryDTO;
import com.maoding.project.service.ProjectConditionService;
import com.maoding.project.service.ProjectService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.h2.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service("projectExportService")
public class ProjectExportServiceImpl extends BaseExportServiceImpl<ProjectVariableDTO,ProjectVariableDTO,ProjectQueryDTO>  {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectConditionService projectConditionService;

    @Override
    List<Map<String, ExcelDataDTO>> getExcelDataList(List<ProjectVariableDTO> dataList,List<CoreShowDTO> titleList) {
        DateUtils dateUtils = new DateUtils();

        List<Map<String,ExcelDataDTO>> excelDataList = new ArrayList<>();
        dataList.stream().forEach(d->{
            Map<String,ExcelDataDTO> map = new HashMap<>();
            titleList.stream().forEach(t->{
                if("立项时间".equals(t.getName())){
//                    String s = ""+(BeanUtilsEx.getProperty(d,t.getId()));
                    String date = dateUtils.date2Str((Date)BeanUtilsEx.getProperty(d,t.getId()),DateUtils.date_sdf2);
                    map.put(t.getName(),new ExcelDataDTO(date,1));
                }else{
                    map.put(t.getName(),new ExcelDataDTO(BeanUtilsEx.getProperty(d,t.getId()),1));
                }
            });
            excelDataList.add(map);
        });
        return excelDataList;
    }

    @Override
    List<CoreShowDTO> getTitleList(ProjectQueryDTO queryDTO) {
        TitleQueryDTO query = new TitleQueryDTO();
        BeanUtils.copyProperties(queryDTO,query);
        List<TitleColumnDTO> list = projectConditionService.listTitle(query);
        List<CoreShowDTO> titleList = new ArrayList<>();
        list.stream().forEach(t->{
            titleList.add(new CoreShowDTO(t.getCode(),t.getName()));
        });
        return titleList;
    }

    @Override
    int exportTitle(List<CoreShowDTO> titleList, ProjectVariableDTO statisticSum, ProjectQueryDTO dto, Sheet sheet, Workbook wb) {
        int rowNum = 0;
        this.setExcelTitle(titleList,sheet,wb,rowNum);
        return rowNum;
    }


    @Override
    public AjaxMessage exportDownloadResource(HttpServletRequest request, HttpServletResponse response, ProjectQueryDTO queryDTO) throws Exception {
        //1.查找列表数据
        List<ProjectVariableDTO> dataList = projectService.listProject(queryDTO);
        return this.exportDownloadResource(request,response,dataList,null,queryDTO);
    }
}
