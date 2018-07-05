package com.maoding.task.service;

import com.maoding.core.bean.AjaxMessage;
import com.maoding.task.dto.ProjectDesignTaskShow;
import com.maoding.task.dto.ProjectTaskExportDTO;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/4/24.
 */
public interface ProjectTaskExportService{
    Workbook getExportedResource(List<ProjectDesignTaskShow> objects, ProjectTaskExportDTO dto)  throws Exception;
    AjaxMessage exportDownloadResource(HttpServletResponse response, ProjectTaskExportDTO dto) throws Exception;
}
