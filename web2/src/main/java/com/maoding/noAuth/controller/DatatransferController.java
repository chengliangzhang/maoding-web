package com.maoding.noAuth.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.MapUtil;
import com.maoding.core.util.StringUtil;
import com.maoding.message.service.MessageService;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.service.CompanyService;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.task.dto.SaveProjectTaskDTO;
import com.maoding.task.entity.ProjectManagerEntity;
import com.maoding.task.service.ProjectTaskService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp22 on 2017/4/11.
 */
@Controller
@RequestMapping("/na/datatransfer")
public class DatatransferController extends BaseController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MyTaskService myTaskService;


    @ModelAttribute
    public void before(){
        this.currentUserId = this.getFromSession("userId",String.class);
        this.currentCompanyId =this.getFromSession("companyId",String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }
    /**
     * 方法描述：初始化权限
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:
     * @return:
     */
    @RequestMapping(value = "/initCompanyRole",method = RequestMethod.POST)
    @ResponseBody
    public void getCompanyByInviteUrl() throws Exception{
        companyService.initCompanyRole();
        companyService.initRolePermission();
    }

    /**
     * 方法描述：初始话项目设计阶段+任务+经营负责人
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:
     * @return:
     */
    @RequestMapping(value = "/initProject",method = RequestMethod.POST)
    @ResponseBody
    public void initProject() throws Exception{

        List<ProjectEntity> list = projectDao.selectAll();
        for(ProjectEntity project:list){
            //1.创建文件夹
            projectSkyDriverService.createProjectFile(project);//
            //2.保存经营负责人
           // saveProjectManager(project);//
            //3.把设计阶段管理到任务中
           // saveProjectTask(project);
        }
    }



    /**
     * 方法描述：初始话项目设计阶段+任务+经营负责人
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:
     * @return:
     */
    @RequestMapping(value = "/initCompanyUser",method = RequestMethod.POST)
    @ResponseBody
    public void initCompanyUser() throws Exception{
        List<CompanyUserEntity> list = companyUserDao.getAllCompanyUser();
        for(CompanyUserEntity entity:list){
            companyUserDao.updateById(entity);
        }
    }

    @RequestMapping(value = "/dynamicDownExcel",method = RequestMethod.POST)
    @ResponseBody
    public void dynamicDownExcel(HttpServletResponse  response) throws Exception{

//创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
//建立新的sheet对象（excel的表单）
        HSSFSheet sheet=wb.createSheet("成绩表");
//在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        HSSFRow row1=sheet.createRow(0);
//创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        HSSFCell cell=row1.createCell(0);
        //设置单元格内容
        cell.setCellValue("学员考试成绩一览表");
//合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
//在sheet里创建第二行
        HSSFRow row2=sheet.createRow(1);
        //创建单元格并设置单元格内容
        row2.createCell(0).setCellValue("姓名");
        row2.createCell(1).setCellValue("班级");
        row2.createCell(2).setCellValue("笔试成绩");
        row2.createCell(3).setCellValue("机试成绩");
        //在sheet里创建第三行
        HSSFRow row3=sheet.createRow(2);
        row3.createCell(0).setCellValue("李明");
        row3.createCell(1).setCellValue("As178");
        row3.createCell(2).setCellValue(87);
        row3.createCell(3).setCellValue(78);
        //.....省略部分代码


//输出Excel文件
        OutputStream output=response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=details.xls");
        response.setContentType("application/msexcel");
        wb.write(output);
        output.close();
    }


    /**
     * 方法描述：初始化文档库
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:
     * @return:
     */
    @RequestMapping(value = "/initProjectSkyDriver",method = RequestMethod.POST)
    @ResponseBody
    public void initProjectSkyDriver() throws Exception{

        projectSkyDriverService.initProjectSkyDriver();

    }


    /**
     * 方法描述：测试加密
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:
     * @return:
     */
    @RequestMapping(value = "/test",method = RequestMethod.POST)
    @ResponseBody
    public Map test(@RequestBody Map<String,Object> map) throws Exception{
        System.out.println(map);
       return map;
    }

    @RequestMapping(value = "/saveTask", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveTaskIssuing(@RequestBody SaveProjectTaskDTO dto) throws Exception {
        dto.setAccountId(this.currentUserId);
        dto.setCurrentCompanyId(this.currentCompanyId);
        return  projectTaskService.saveProjectTask2(dto);
    }


    /**
     * 方法描述：发布签发任务
     *           复制任务到正式签发任务，同时保存相应的日期更改历史、项目经营人等信息，并把状态改为已发布
     * 作   者： ZCL
     * 日   期：2017/5/15
     * param  dto  要更改的任务信息，包括任务起止时间
     */
    @RequestMapping(value = "publishTest",method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage publishIssueTask(@RequestBody Map<String,Object> map) throws Exception{
        return projectTaskService.publishIssueTask((List<String>)map.get("idList"),currentUserId,currentCompanyId);
    }

    @RequestMapping(value = "initOldDataMessage",method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage initOldDataMessage()throws Exception{
        messageService.initOldData();
        return AjaxMessage.succeed(null);
    }

    @RequestMapping(value = "getMyTaskList4",method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMyTaskList4()throws Exception{
        Map<String,Object> param = new HashMap<>();
        param.put("companyId", "bbdcfb2bfa9649b2baa72e6f57e1e9b0");
        param.put("handlerId", "6811a42d1771455e99d542271effefcc");
        param.put("status","0");
        return AjaxMessage.succeed( myTaskService.getMyTaskList4(param));
    }


}
