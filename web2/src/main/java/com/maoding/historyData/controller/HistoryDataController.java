package com.maoding.historyData.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.RoleConst;
import com.maoding.project.dto.*;
import com.maoding.project.service.ImportService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Created by Chengliang.zhang on 2017/7/19.
 */
@Controller
@RequestMapping("iWork/historyData")
public class HistoryDataController extends BaseController {

    @Autowired
    ImportService importService;

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }

    @RequestMapping("/entry")
    public String entry() {
        return "views/historyData/entry";
    }


    @RequestMapping(value = "/importProjects", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.IMPORT}, logical = Logical.OR)
    public AjaxMessage importProjects(@RequestPart("file") MultipartFile file, @RequestParam String creatorOrgId) throws Exception {
        InputStream in = file.getInputStream();
        ImportProjectDefaultParamDTO params = new ImportProjectDefaultParamDTO();
        params.setCompanyId(currentCompanyId);
        params.setUserId(currentUserId);
        params.setCreatorOrgId(creatorOrgId);
        ImportResultDTO result = importService.importProjects(in, params); //读入文件
        return ((result != null) && (result.getTotalCount() != null) && (result.getTotalCount() > 0)) ? AjaxMessage.succeed(result) : AjaxMessage.failed("未找到有效数据");
    }

    @RequestMapping(value = "/createProjects", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.IMPORT}, logical = Logical.OR)
    public AjaxMessage importProjects(@RequestBody ImportResultDTO dto) throws Exception {
        ImportProjectDefaultParamDTO params = new ImportProjectDefaultParamDTO();
        BeanUtils.copyProperties(dto, params);
        if (params.getCompanyId() == null) params.setCompanyId(currentCompanyId);
        if (params.getUserId() == null) params.setUserId(currentUserId);
        ImportResultDTO result = importService.importProjects(dto.getValidList(), params); //生成数据库记录
        return ((result != null) && (result.getValidCount() > 0)) ? AjaxMessage.succeed(result) : AjaxMessage.failed("导入失败");
    }

    /**
     * 方法描述：费用录入导入(excel-DTO)
     * 作   者：DongLiu
     * 日   期：2018/2/26 10:30
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/importExpFixeds", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.IMPORT}, logical = Logical.OR)
    public AjaxMessage importExpFixeds(@RequestPart("file") MultipartFile file, @RequestParam String creatorOrgId) throws Exception {
        InputStream in = file.getInputStream();
        ImportProjectDefaultParamDTO params = new ImportProjectDefaultParamDTO();
        params.setCompanyId(currentCompanyId);
        params.setUserId(currentUserId);
        params.setCreatorOrgId(creatorOrgId);
        ImportExpFixedDTO result = importService.importExpFixeds(in, params); //读入文件
        return ((result != null) && (result.getTotalCount() != null) && (result.getTotalCount() > 0)) ? AjaxMessage.succeed(result) : AjaxMessage.failed("未找到有效数据");
    }

    /**
     * 方法描述：正式保存费用录入导入（DTO-DB）
     * 作   者：DongLiu
     * 日   期：2018/2/26 15:10
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/saveExpFixeds", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.IMPORT}, logical = Logical.OR)
    public AjaxMessage saveExpFixeds(@RequestBody ImportExpFixedDTO dto) throws Exception {
        ImportProjectDefaultParamDTO params = new ImportProjectDefaultParamDTO();
        BeanUtils.copyProperties(dto, params);
        if (params.getCompanyId() == null) params.setCompanyId(currentCompanyId);
        if (params.getUserId() == null) params.setUserId(currentUserId);
        ImportExpFixedDTO result = importService.importExpFixedDTOs(dto.getValidList(), params); //生成数据库记录
        return ((result != null) && (result.getValidCount() > 0)) ? AjaxMessage.succeed(result) : AjaxMessage.failed("导入失败");
    }

    /**
     * 方法描述：报销费用导入(excel-DTO)
     * 作   者：DongLiu
     * 日   期：2018/2/26 11:38
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/importExpenses", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.IMPORT}, logical = Logical.OR)
    public AjaxMessage importExpenses(@RequestPart("file") MultipartFile file, @RequestParam String creatorOrgId) throws Exception {
        InputStream in = file.getInputStream();
        ImportProjectDefaultParamDTO params = new ImportProjectDefaultParamDTO();
        params.setCompanyId(currentCompanyId);
        params.setUserId(currentUserId);
        params.setCreatorOrgId(creatorOrgId);
        ImportExpenseDTO result = importService.ImportExpenses(in, params); //读入文件
        return ((result != null) && (result.getTotalCount() != null) && (result.getTotalCount() > 0)) ? AjaxMessage.succeed(result) : AjaxMessage.failed("未找到有效数据");
    }

    /**
     * 方法描述：项目收支导入（Excel-DTO）
     * 作   者：DongLiu
     * 日   期：2018/2/26 14:41
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/importExpenditures", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.IMPORT}, logical = Logical.OR)
    public AjaxMessage importExpenditures(@RequestPart("file") MultipartFile file, @RequestParam String creatorOrgId) throws Exception {
        InputStream in = file.getInputStream();
        ImportProjectDefaultParamDTO params = new ImportProjectDefaultParamDTO();
        params.setCompanyId(currentCompanyId);
        params.setUserId(currentUserId);
        params.setCreatorOrgId(creatorOrgId);
        ImportExpenditureDTO result = importService.ImportExpenditures(in, params); //读入文件
        return ((result != null) && (result.getTotalCount() != null) && (result.getTotalCount() > 0)) ? AjaxMessage.succeed(result) : AjaxMessage.failed("未找到有效数据");
    }
}
