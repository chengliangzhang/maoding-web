package com.maoding.project.service.impl;


import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.*;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.ExcelUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.StringUtils;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dto.CompanyDataDTO;
import com.maoding.org.dto.CompanyUserDTO;
import com.maoding.org.dto.CompanyUserTableDTO;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.TeamOperaterService;
import com.maoding.project.dao.ProjectAuditDao;
import com.maoding.project.dao.ProjectConstructDao;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.dao.ProjectDesignContentDao;
import com.maoding.project.dto.*;
import com.maoding.project.entity.ProjectAuditEntity;
import com.maoding.project.entity.ProjectDesignContentEntity;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.service.ImportService;
import com.maoding.project.service.ProjectService;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.projectcost.service.ProjectCostService;
import com.maoding.projectmember.dao.ProjectMemberDao;
import com.maoding.projectmember.dto.MemberQueryDTO;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.task.dao.ProjectProcessTimeDao;
import com.maoding.task.dao.ProjectTaskRelationDao;
import com.maoding.task.entity.ProjectProcessTimeEntity;
import com.maoding.task.entity.ProjectTaskEntity;
import com.maoding.task.entity.ProjectTaskRelationEntity;
import com.maoding.task.service.ProjectTaskService;
import com.maoding.user.dao.AccountDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chengliang.zhang on 2017/7/19.
 * 导入文件服务
 */
@Service("importService")
public class ImportServiceImpl extends NewBaseService implements ImportService {
    /**
     * 日志对象
     */
    private static final Logger log = LoggerFactory.getLogger(ImportServiceImpl.class);

    @Autowired
    CompanyDao companyDAO;

    @Autowired
    ProjectDao projectDAO;

    @Autowired
    ProjectMemberDao projectMemberDAO;

    @Autowired
    ProjectAuditDao projectAuditDAO;

    @Autowired
    ProjectConstructDao projectConstructDao;

    @Autowired
    AccountDao userDao;

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ProjectProcessTimeDao projectProcessTimeDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectDesignContentDao projectDesignContentDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private ProjectTaskRelationDao projectTaskRelationDao;

    @Autowired
    private ProjectCostService projectCostService;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private TeamOperaterService teamOperaterService;

    /**
     * 导入项目数据
     *
     * @param in     输入流
     * @param params 操作用户标识
     * @return 导入结果
     */
    @Override
    public ImportResultDTO importProjects(InputStream in, ImportProjectDefaultParamDTO params) {
        if (in == null) throw new IllegalArgumentException("importProjects 参数错误");

        final Integer DEFAULT_SHEET_INDEX = -1;
        final Integer DEFAULT_TITLE_ROW = 3;
        final Integer DEFAULT_START_ROW = -1;
        final Short DEFAULT_START_COLUMN = 1;
        final Short DEFAULT_END_COLUMN = -1;
        final String DEMO_PROJECT_NO_MASK = "md";
        final String DEMO_PROJECT_NAME_MASK = "深圳市卯丁科技大厦";

        List<Map<String, Object>> dataList = ExcelUtils.readFrom(ExcelUtils.getWorkbook(in), DEFAULT_SHEET_INDEX, DEFAULT_TITLE_ROW, DEFAULT_START_COLUMN);
        if ((dataList == null) || (dataList.isEmpty())) throw new IllegalArgumentException("importProjects 找不到有效数据");

        ImportResultDTO result = new ImportResultDTO();
        for (Map<String, Object> data : dataList) {
            ImportProjectDTO dto = createImportProjectDTO(data);
            //忽略示范行
            if ((dto == null)
                    || (StringUtils.startsWith(dto.getProjectNo(), DEMO_PROJECT_NO_MASK)
                    && StringUtils.startsWith(dto.getProjectName(), DEMO_PROJECT_NAME_MASK))) {
                continue;
            }
            result.addTotalCount();
            //检查是否存在必填项为空字段
            if (dto.getErrorReason() != null) {
                result.addInvalid(dto);
                continue;
            }

            //检查数据有效性
            ProjectEntity project = createProjectDOFrom(dto, params);
            if (project == null) {
                result.addInvalid(dto);
                continue;
            }
            result.addValid(dto);
        }
        return result;
    }

    @Override
    public ImportResultDTO importProjects(List<ImportProjectDTO> dataList, ImportProjectDefaultParamDTO params) {
        if (dataList == null) throw new IllegalArgumentException("importProjects 参数错误");

        ImportResultDTO result = new ImportResultDTO();
        for (ImportProjectDTO data : dataList) {
            result.addTotalCount();

            //检查数据有效性
            ProjectEntity project = createProjectDOFrom(data, params);
            if (project == null) {
                result.addInvalid(data);
                continue;
            }

            //存储项目数据
            try {

                insertProject(project);
                //save乙方经营负责人
                //判断是否发送给乙方

                List<CompanyDataDTO> companyList = companyService.getCompanyForSelect(project.getCompanyId(), project.getId());
                //添加项目设计内容
                insertDesignContent(project, data.getDesignContentList(), params, companyList);
            } catch (Exception e) {
                log.error("添加数据时产生错误", e);
                data.setErrorReason("添加数据时产生错误");
                result.addInvalid(data);
                continue;
            }

            result.addValid(data);
        }
        return result;
    }

    /**
     * 方法描述：费用录入导入
     * 作   者：DongLiu
     * 日   期：2018/2/26 10:39
     */
    @Override
    public ImportExpFixedDTO importExpFixeds(InputStream in, ImportProjectDefaultParamDTO params) {
        if (in == null) throw new IllegalArgumentException("importProjects 参数错误");
        final Integer DEFAULT_SHEET_INDEX = -1;
        final Integer DEFAULT_TITLE_ROW = 3;
        final Integer DEFAULT_START_ROW = -1;
        final Short DEFAULT_START_COLUMN = 1;
        final Short DEFAULT_END_COLUMN = -1;
        final String DEMO_PROJECT_NO_MASK = "md";
        final String DEMO_PROJECT_NAME_MASK = "深圳市卯丁科技大厦";
        List<Map<String, Object>> dataList = ExcelUtils.readFrom(ExcelUtils.getWorkbook(in), DEFAULT_SHEET_INDEX, DEFAULT_TITLE_ROW, DEFAULT_START_COLUMN);
        if ((dataList == null) || (dataList.isEmpty())) throw new IllegalArgumentException("importProjects 找不到有效数据");
        ImportExpFixedDTO importExpFixedDTO = new ImportExpFixedDTO();
        for (Map<String, Object> data : dataList) {
            ExpFixedDTO dto = createImportExpFixedDTO(data);
            //忽略示范行
//            if ((dto == null)
//                    || (StringUtils.startsWith(dto.getProjectNo(), DEMO_PROJECT_NO_MASK)
//                    && StringUtils.startsWith(dto.getProjectName(), DEMO_PROJECT_NAME_MASK))) {
//                continue;
//            }
            importExpFixedDTO.addTotalCount();
            //检查是否存在必填项为空字段
            if (dto.getErrorReason() != null) {
                importExpFixedDTO.addInvalid(dto);
                continue;
            } else {
                importExpFixedDTO.addValid(dto);
            }
        }
        return importExpFixedDTO;
    }

    @Override
    public ImportExpFixedDTO importExpFixedDTOs(List<ExpFixedDTO> dataList, ImportProjectDefaultParamDTO params) {

        return null;
    }

    /**
     * 方法描述：费用数据导入，拼装数据（excel数据保存到DTO中）
     * 作   者：DongLiu
     * 日   期：2018/2/26 11:18
     *
     * @param
     * @return
     */
    private ExpFixedDTO createImportExpFixedDTO(Map<String, Object> data) {
        ExpFixedDTO dto = new ExpFixedDTO();
        try {
            dto.setDateTime(data.get(ExpCategoryConst.DATE_TIME).toString());
            if (dto.getDateTime() == null) {
                dto.setErrorReason(ExpCategoryConst.DATE_TIME + "不能为空");
                return dto;
            }
            dto.setGdfySalestaxTax(data.get(ExpCategoryConst.EXP_SALESTAX_TAX).toString());
            dto.setGdfyDirectcostsSalay(data.get(ExpCategoryConst.EXP_DIRECTCOSTS_SALAY).toString());
            dto.setGdfyDirectcostsFund(data.get(ExpCategoryConst.EXP_DIRECTCOSTS_FUND).toString());
            dto.setGdfyDirectcostsBonus(data.get(ExpCategoryConst.EXP_DIRECTCOSTS_BONUS).toString());
            dto.setGdfyExecutivesalarySalay(data.get(ExpCategoryConst.EXP_EXECUTIVESALARY_SALAY).toString());
            dto.setGdfyExecutivesalaryFund(data.get(ExpCategoryConst.EXP_EXECUTIVESALARY_FUND).toString());
            dto.setGdfyExecutivesalaryBonus(data.get(ExpCategoryConst.EXP_EXECUTIVESALARY_BONUS).toString());
            dto.setGdfyFwsalaryBgcd(data.get(ExpCategoryConst.EXP_FWSALARY_BGCD).toString());
            dto.setGdfyFwsalaryWg(data.get(ExpCategoryConst.EXP_FWSALARY_WG).toString());
            dto.setGdfyFwsalarySd(data.get(ExpCategoryConst.EXP_FWSALARY_SD).toString());
            dto.setGdfyFwsalaryNet(data.get(ExpCategoryConst.EXP_FWSALARY_NET).toString());
            dto.setGdfyFwsalaryGbwh(data.get(ExpCategoryConst.EXP_FWSALARY_GBWH).toString());
            dto.setGdfyAssetsamortizationBgzx(data.get(ExpCategoryConst.EXP_ASSETSAMORTIZATION_BGZX).toString());
            dto.setGdfyAssetsamortizationBgsb(data.get(ExpCategoryConst.EXP_ASSETSAMORTIZATION_BGSB).toString());
            dto.setGdfyAssetsamortizationRjtx(data.get(ExpCategoryConst.EXP_ASSETSAMORTIZATION_RJTX).toString());
            dto.setGdfyAssetsamortizationBdczj(data.get(ExpCategoryConst.EXP_ASSETSAMORTIZATION_BDCZJ).toString());
            dto.setGdfyZcjzzbHzzb(data.get(ExpCategoryConst.EXP_ZCJZZB_HZZB).toString());
            dto.setGdfyCwfySxf(data.get(ExpCategoryConst.EXP_CWFY_SXF).toString());
            dto.setGdfyCwfyLx(data.get(ExpCategoryConst.EXP_CWFY_LX).toString());
            dto.setGdfySdsfySds(data.get(ExpCategoryConst.EXP_SDSFY_SDS).toString());
        } catch (Exception e) {
            log.error("createImportExpFixedDTO is fail:" + e.getMessage());
        }
        return dto;
    }

    /**
     * 方法描述：报销费用导入
     * 作   者：DongLiu
     * 日   期：2018/2/26 11:43
     *
     * @param
     * @return
     */
    @Override
    public ImportExpenseDTO ImportExpenses(InputStream in, ImportProjectDefaultParamDTO params) {
        if (in == null) throw new IllegalArgumentException("ImportExpenses 参数错误");
        final Integer DEFAULT_SHEET_INDEX = -1;
        final Integer DEFAULT_TITLE_ROW = 3;
        final Integer DEFAULT_START_ROW = -1;
        final Short DEFAULT_START_COLUMN = 1;
        final Short DEFAULT_END_COLUMN = -1;
        final String DEMO_PROJECT_NO_MASK = "md";
        final String DEMO_PROJECT_NAME_MASK = "深圳市卯丁科技大厦";
        List<Map<String, Object>> dataList = ExcelUtils.readFrom(ExcelUtils.getWorkbook(in), DEFAULT_SHEET_INDEX, DEFAULT_TITLE_ROW, DEFAULT_START_COLUMN);
        if ((dataList == null) || (dataList.isEmpty())) throw new IllegalArgumentException("ImportExpenses 找不到有效数据");
        ImportExpenseDTO importExpenseDTO = new ImportExpenseDTO();
        for (Map<String, Object> data : dataList) {
            ExpenseDTO dto = createExpenseDTO(data);
            //忽略示范行
//            if ((dto == null)
//                    || (StringUtils.startsWith(dto.getProjectNo(), DEMO_PROJECT_NO_MASK)
//                    && StringUtils.startsWith(dto.getProjectName(), DEMO_PROJECT_NAME_MASK))) {
//                continue;
//            }
            importExpenseDTO.addTotalCount();
            //检查是否存在必填项为空字段
            if (dto.getErrorReason() != null) {
                importExpenseDTO.addInvalid(dto);
                continue;
            } else {
                importExpenseDTO.addValid(dto);
            }
        }
        return importExpenseDTO;
    }

    /**
     * 方法描述：报销费用导入，拼装数据（excel数据保存到DTO中）
     * 作   者：DongLiu
     * 日   期：2018/2/26 11:49
     *
     * @param
     * @return
     */
    private ExpenseDTO createExpenseDTO(Map<String, Object> data) {
        ExpenseDTO dto = new ExpenseDTO();
        try {
            dto.setExpDate((Date) data.get(ExpenseConst.EXP_DATE));
            dto.setExpAmount((BigDecimal) data.get(ExpenseConst.EXP_AMOUNT));
            dto.setExpTypeStr(data.get(ExpenseConst.EXP_TYPE).toString());
            dto.setExpUser(data.get(ExpenseConst.EXP_USER_ID).toString());
            dto.setAuditPerson(data.get(ExpenseConst.AUDIT_PERSON).toString());
            dto.setExpAllname(data.get(ExpenseConst.EXP_ALLNAME).toString());
            dto.setExpUse(data.get(ExpenseConst.EXP_USE).toString());
            dto.setAllocationUser(data.get(ExpenseConst.ALLOCATION_USER_ID).toString());
            dto.setProjectName(data.get(ExpenseConst.PROJECT_ID).toString());
        } catch (Exception e) {
            log.error("createExpenseDTO is fail:" + e.getMessage());
        }
        return dto;
    }

    @Override
    public ImportExpenseDTO ImportExpenseDTOs(List<ExpenseDTO> dataList, ImportProjectDefaultParamDTO params) {
        return null;
    }

    /**
     * 方法描述：项目收支导入
     * 作   者：DongLiu
     * 日   期：2018/2/26 14:48
     *
     * @param
     * @return
     */
    @Override
    public ImportExpenditureDTO ImportExpenditures(InputStream in, ImportProjectDefaultParamDTO params) {
        ImportExpenditureDTO importExpenditureDTO = new ImportExpenditureDTO();
        if (in == null) throw new IllegalArgumentException("ImportExpenses 参数错误");
        final Integer DEFAULT_SHEET_INDEX = -1;
        final Integer DEFAULT_TITLE_ROW = 3;
        final Integer DEFAULT_START_ROW = -1;
        final Short DEFAULT_START_COLUMN = 1;
        final Short DEFAULT_END_COLUMN = -1;
        final String DEMO_PROJECT_NO_MASK = "md";
        final String DEMO_PROJECT_NAME_MASK = "深圳市卯丁科技大厦";
        List<Map<String, Object>> dataList = ExcelUtils.readFrom(ExcelUtils.getWorkbook(in), DEFAULT_SHEET_INDEX, DEFAULT_TITLE_ROW, DEFAULT_START_COLUMN);
        if ((dataList == null) || (dataList.isEmpty())) throw new IllegalArgumentException("ImportExpenses 找不到有效数据");
        for (Map<String, Object> data : dataList) {
            ExpenditureDTO dto = createExpenditureDTO(data);
            //忽略示范行
//            if ((dto == null)
//                    || (StringUtils.startsWith(dto.getProjectNo(), DEMO_PROJECT_NO_MASK)
//                    && StringUtils.startsWith(dto.getProjectName(), DEMO_PROJECT_NAME_MASK))) {
//                continue;
//            }
            importExpenditureDTO.addTotalCount();
            //检查是否存在必填项为空字段
            if (dto.getErrorReason() != null) {
                importExpenditureDTO.addInvalid(dto);
                continue;
            } else {
                importExpenditureDTO.addValid(dto);
            }
        }
        return importExpenditureDTO;
    }

    /**
     * 方法描述：项目收支导入，拼装数据（excel数据保存到DTO中）
     * 作   者：DongLiu
     * 日   期：2018/2/26 11:49
     *
     * @param
     * @return
     */
    private ExpenditureDTO createExpenditureDTO(Map<String, Object> data) {
        ExpenditureDTO dto = new ExpenditureDTO();
        try {
            dto.setProjectName(data.get(ExpenditureConst.PROJECT_NAME).toString());
            dto.setProjectTypeStr(data.get(ExpenditureConst.PROJECT_TYPE).toString());
            dto.setNode(data.get(ExpenditureConst.NODE).toString());
            dto.setCostAmount((BigDecimal) data.get(ExpenditureConst.COST_AMOUNT));
            dto.setCostDate((Date) data.get(ExpenditureConst.COST_DATE));
            dto.setCompanyInName(data.get(ExpenditureConst.COMPANY_IN).toString());
            dto.setCompanyOutName(data.get(ExpenditureConst.COMPANY_OUT).toString());
        } catch (Exception e) {
            log.error("createExpenditureDTO is fail:" + e.getMessage());
        }
        return dto;
    }

    @Override
    public ImportExpenditureDTO ImportExpenditureDTOs(List<ExpenditureDTO> dataList, ImportProjectDefaultParamDTO params) {
        return null;
    }

    /**
     * 转换MAP到DTO
     */
    private ImportProjectDTO createImportProjectDTO(Map<String, Object> data) {
        ImportProjectDTO dto = new ImportProjectDTO();
        //项目编号
        dto.setProjectNo((String) data.get(ProjectConst.PROJECT_NO));
        if (dto.getProjectNo() == null) return dto.setErrorReason(ProjectConst.PROJECT_NO + "不能为空");
        //项目名称
        dto.setProjectName((String) data.get(ProjectConst.PROJECT_NAME));
        if (dto.getProjectName() == null) return dto.setErrorReason(ProjectConst.PROJECT_NAME + "不能为空");

        dto.setCreatorOrgName((String) data.get(ProjectConst.PROJECT_COMPANY_NAME));
        dto.setCreatorUserName((String) data.get(ProjectConst.PROJECT_CREATOR_NAME));
        try {
            dto.setProjectCreateDate((Date) data.get(ProjectConst.PROJECT_CREATE_DATE));
        } catch (ClassCastException e) {
            log.error(e.getMessage());
            return dto.setErrorReason(ProjectConst.PROJECT_CREATE_DATE + "格式错误");
        }

        try {
            dto.setContractDate((Date) data.get(ProjectConst.PROJECT_CONTRACT_DATE));
            if (dto.getContractDate() == null) return dto.setErrorReason(ProjectConst.PROJECT_CONTRACT_DATE + "不能为空");
        } catch (ClassCastException e) {
            log.error(e.getMessage());
            return dto.setErrorReason(ProjectConst.PROJECT_CONTRACT_DATE + "格式错误");
        }

        dto.setProvince((String) data.get(ProjectConst.PROJECT_PROVINCE));
        if (dto.getProvince() == null) return dto.setErrorReason(ProjectConst.PROJECT_PROVINCE + "不能为空");

        dto.setCity((String) data.get(ProjectConst.PROJECT_CITY));
        if (dto.getCity() == null) return dto.setErrorReason(ProjectConst.PROJECT_CITY + "不能为空");

        dto.setCounty((String) data.get(ProjectConst.PROJECT_COUNTY));

        dto.setDetailAddress((String) data.get(ProjectConst.PROJECT_DETAIL_ADDRESS));
        if (dto.getDetailAddress() == null) return dto.setErrorReason(ProjectConst.PROJECT_DETAIL_ADDRESS + "不能为空");

        dto.setProjectStatus((String) data.get(ProjectConst.PROJECT_STATUS));
        if (dto.getProjectStatus() == null) return dto.setErrorReason(ProjectConst.PROJECT_STATUS + "不能为空");

        dto.setaCompanyName((String) data.get(ProjectConst.PROJECT_A_NAME));
        if (dto.getaCompanyName() == null) return dto.setErrorReason(ProjectConst.PROJECT_A_NAME + "不能为空");

        dto.setbCompanyName((String) data.get(ProjectConst.PROJECT_B_NAME));

        if (data.get(ProjectConst.PROJECT_DESIGN_CONTENT_1) != null) {
            String errorMsg = validateDate(ProjectConst.PROJECT_START_DATE_1, data);
            if (errorMsg != null) return dto.setErrorReason(errorMsg);
            errorMsg = validateDate(ProjectConst.PROJECT_END_DATE_1, data);
            if (errorMsg != null) return dto.setErrorReason(errorMsg);
            dto.addDesignContent((String) data.get(ProjectConst.PROJECT_DESIGN_CONTENT_1),
                    (Date) data.get(ProjectConst.PROJECT_START_DATE_1),
                    (Date) data.get(ProjectConst.PROJECT_END_DATE_1),
                    (Date) data.get(ProjectConst.PROJECT_FINISH_DATE_1),
                    (String) data.get(ProjectConst.PROJECT_DESIGN_ORGANIZATION_1));
        }
        if (dto.getDesignContentList() == null)
            return dto.setErrorReason(ProjectConst.PROJECT_DESIGN_CONTENT_1 + "不能为空");

        if (data.get(ProjectConst.PROJECT_DESIGN_CONTENT_2) != null) {
            String errorMsg = validateDate(ProjectConst.PROJECT_START_DATE_2, data);
            if (errorMsg != null) return dto.setErrorReason(errorMsg);
            errorMsg = validateDate(ProjectConst.PROJECT_END_DATE_2, data);
            if (errorMsg != null) return dto.setErrorReason(errorMsg);
            dto.addDesignContent((String) data.get(ProjectConst.PROJECT_DESIGN_CONTENT_2),
                    (Date) data.get(ProjectConst.PROJECT_START_DATE_2),
                    (Date) data.get(ProjectConst.PROJECT_END_DATE_2),
                    (Date) data.get(ProjectConst.PROJECT_FINISH_DATE_2),
                    (String) data.get(ProjectConst.PROJECT_DESIGN_ORGANIZATION_2));
        }
        if (data.get(ProjectConst.PROJECT_DESIGN_CONTENT_3) != null) {
            String errorMsg = validateDate(ProjectConst.PROJECT_START_DATE_3, data);
            if (errorMsg != null) return dto.setErrorReason(errorMsg);
            errorMsg = validateDate(ProjectConst.PROJECT_END_DATE_3, data);
            if (errorMsg != null) return dto.setErrorReason(errorMsg);
            dto.addDesignContent((String) data.get(ProjectConst.PROJECT_DESIGN_CONTENT_3),
                    (Date) data.get(ProjectConst.PROJECT_START_DATE_3),
                    (Date) data.get(ProjectConst.PROJECT_END_DATE_3),
                    (Date) data.get(ProjectConst.PROJECT_FINISH_DATE_3),
                    (String) data.get(ProjectConst.PROJECT_DESIGN_ORGANIZATION_3));
        }
        if (data.get(ProjectConst.PROJECT_DESIGN_CONTENT_4) != null) {
            String errorMsg = validateDate(ProjectConst.PROJECT_START_DATE_4, data);
            if (errorMsg != null) return dto.setErrorReason(errorMsg);
            errorMsg = validateDate(ProjectConst.PROJECT_END_DATE_4, data);
            if (errorMsg != null) return dto.setErrorReason(errorMsg);
            dto.addDesignContent((String) data.get(ProjectConst.PROJECT_DESIGN_CONTENT_4),
                    (Date) data.get(ProjectConst.PROJECT_START_DATE_4),
                    (Date) data.get(ProjectConst.PROJECT_END_DATE_4),
                    (Date) data.get(ProjectConst.PROJECT_FINISH_DATE_4),
                    (String) data.get(ProjectConst.PROJECT_DESIGN_ORGANIZATION_4));
        }
        if (data.get(ProjectConst.PROJECT_DESIGN_CONTENT_5) != null) {
            String errorMsg = validateDate(ProjectConst.PROJECT_START_DATE_5, data);
            if (errorMsg != null) return dto.setErrorReason(errorMsg);
            errorMsg = validateDate(ProjectConst.PROJECT_END_DATE_5, data);
            if (errorMsg != null) return dto.setErrorReason(errorMsg);
            dto.addDesignContent((String) data.get(ProjectConst.PROJECT_DESIGN_CONTENT_5),
                    (Date) data.get(ProjectConst.PROJECT_START_DATE_5),
                    (Date) data.get(ProjectConst.PROJECT_END_DATE_5),
                    (Date) data.get(ProjectConst.PROJECT_FINISH_DATE_5),
                    (String) data.get(ProjectConst.PROJECT_DESIGN_ORGANIZATION_5));
        }

        //如果没有立项时间字段，用合同时间作为项目立项时间
        if (dto.getProjectCreateDate() == null) dto.setProjectCreateDate(dto.getContractDate());

        return dto;
    }

    private String validateDate(String title, Map date) {
        if (date.get(title) != null) {
            if (date.get(title) instanceof Date) {
                return null;
            } else {
                return title + ":" + date.get(title) + "格式不对";
            }
        }
        return null;
    }

    /**
     * 添加项目设计内容
     */
    private void insertDesignContent(ProjectEntity project, List<ImportDesignContentDTO> designContentList, ImportProjectDefaultParamDTO params, List<CompanyDataDTO> companyList) throws Exception {
        if ((project == null) || (project.getId() == null) || (designContentList == null)) return;
        Integer n = 1;
        for (ImportDesignContentDTO dto : designContentList) {
            //在maoding_web_project_design_content内存放设计内容
            insertDesignContent(project, dto, n);

            String toCompanyId = getToCompany(companyList, dto.getDesignOrganization());
            String id = insertProjectTask(project, dto, 1, null, n, toCompanyId);//taskId


            /* 在分离设计内容和设计任务时，设计内容放在maoding_web_project_design_content中，不再存放于maoding_web_project_task
            // 以下代码是为把设计内容仍存放在maoding_web_project_task内而编写，在此设计中不再需要
            insertProjectTask(project,dto,1,null,n);
            insertProjectTask(project,dto,2,null,n);
            //*/
            n++;
        }
    }

    String getToCompany(List<CompanyDataDTO> companyList, String designOrganization) {
        for (CompanyDataDTO data : companyList) {
            if (data.getCompanyName().equals(designOrganization)) {
                return data.getId();
            }
        }
        return null;
    }

    private void saveProjectMemberEntity(ProjectTaskEntity task, Integer memberType) {
        MemberQueryDTO query = new MemberQueryDTO(task.getCompanyId(), task.getProjectId(), memberType);
        List<CompanyUserTableDTO> companyUserList = null;
        ProjectMemberEntity projectMember = projectMemberDAO.getProjectMember(query);
        if (null == projectMember) {
            Map<String, Object> map = new HashMap<>();
            map.put("permissionId", memberType == 1 ? "51" : "52");//相应权限id
            map.put("companyId", task.getCompanyId());
            companyUserList = this.companyUserDao.getCompanyUserByPermissionId(map);
            if (!CollectionUtils.isEmpty(companyUserList)) {
                CompanyUserTableDTO params = companyUserList.get(0);
                ProjectMemberEntity member = new ProjectMemberEntity();
                member.resetId();
                member.setProjectId(task.getProjectId());
                member.setCompanyId(task.getCompanyId());
                member.setAccountId(params.getUserId());
                member.setCompanyUserId(params.getId());
                member.setMemberType(memberType);
                member.setStatus(0);
                member.setDeleted(0);
                member.setSeq(0);
                member.resetCreateDate();
                member.setCreateBy(params.getUserId());
                projectMemberDAO.insert(member);
                projectMember = member;
            }
        }

        if (memberType == 2 && projectMember != null) {
            projectMember.initEntity();
            projectMember.setTargetId(task.getId());
            projectMember.setMemberType(ProjectMemberType.PROJECT_TASK_RESPONSIBLE);
            projectMemberDAO.insert(projectMember);
        }
    }

    /**
     * 添加任务
     */
    private String insertProjectTask(ProjectEntity project, ImportDesignContentDTO dto, Integer taskType, String beModifyId, Integer seq, String toCompanyId) throws Exception {
        //添加设计内容记录
        ProjectTaskEntity content = new ProjectTaskEntity();
        content.resetId();
        content.setFromCompanyId(project.getCompanyId());
        content.setCompanyId(toCompanyId != null ? toCompanyId : project.getCompanyId());
        content.setProjectId(project.getId());
        content.setSeq(seq);
        content.setTaskLevel(1);
        content.setTaskName(dto.getContentName());
        content.setTaskStatus("0");
        content.setIsOperaterTask(0); //批量导入的直接为生产的根任务
        content.setTaskPath(content.getId());
        content.setTaskType(taskType);
        content.setStartTime(dto.getStartDate());
        content.setEndTime(dto.getEndDate());
        content.setCreateDate(project.getCreateDate());
        content.setCreateBy(project.getCreateBy());
        content.setBeModifyId(beModifyId);
        content.setCompleteDate(dto.getFinishDate());
        content.setEndStatus(0);//已完成
        if (dto.getFinishDate() != null) {
            content.setEndStatus(1);//已完成
        }
        projectTaskService.insert(content);
        //添加时间修改历史记录
        if ((content.getStartTime() != null) && (content.getEndTime() != null)) {
            ProjectProcessTimeEntity time = new ProjectProcessTimeEntity();
            time.resetId();
            time.setCompanyId(content.getCompanyId());
            time.setStartTime(content.getStartTime());
            time.setEndTime(content.getEndTime());
            time.setType(2);
            time.setMemo("");
            time.setTargetId(content.getId());
            time.setCreateBy(content.getCreateBy());
            time.setCreateDate(content.getCreateDate());
            projectProcessTimeDao.insert(time);
        }

        //处理taskRelation，和合作设计费
        if (!content.getCompanyId().equals(content.getFromCompanyId())) {
            ProjectTaskRelationEntity relation = new ProjectTaskRelationEntity();
            relation.initEntity();
            relation.setTaskId(content.getId());
            relation.setFromCompanyId(content.getFromCompanyId());
            relation.setToCompanyId(content.getCompanyId());
            relation.setRelationStatus(0);
            relation.setProjectId(project.getId());
            projectTaskRelationDao.insert(relation);
            //保存费用
            this.projectCostService.saveProjectCost(content, content.getFromCompanyId());//保存管理的费用
        }

        //添加经营负责人和设计负责人
        saveProjectMemberEntity(content, ProjectMemberType.PROJECT_OPERATOR_MANAGER);
        saveProjectMemberEntity(content, ProjectMemberType.PROJECT_DESIGNER_MANAGER);

        return content.getId();
    }

    /**
     * 添加设计内容
     */
    private String insertDesignContent(ProjectEntity project, ImportDesignContentDTO dto, Integer seq) {
        //添加设计内容记录
        ProjectDesignContentEntity content = new ProjectDesignContentEntity();
        content.resetId();
        content.setCompanyId(project.getCompanyId());
        content.setProjectId(project.getId());
        content.setSeq(seq.toString());
        content.setContentName(dto.getContentName());
        content.setCreateDate(project.getCreateDate());
        content.setCreateBy(project.getCreateBy());
        projectDesignContentDao.insert(content);
        //添加时间修改历史记录
        if ((dto.getStartDate() != null) && (dto.getEndDate() != null)) {
            ProjectProcessTimeEntity time = new ProjectProcessTimeEntity();
            time.resetId();
            time.setCompanyId(project.getCompanyId());
            time.setStartTime(dto.getStartDate());
            time.setEndTime(dto.getEndDate());
            time.setType(1);
            time.setMemo("");
            time.setTargetId(content.getId());
            time.setCreateBy(content.getCreateBy());
            time.setCreateDate(content.getCreateDate());
            projectProcessTimeDao.insert(time);
        }
        return content.getId();
    }

    /**
     * 添加项目
     */
    private void insertProject(ProjectEntity project) {
        if (project == null) return;
        //补充缺失字段
        if (project.getId() == null) project.resetId();
        if (project.getPstatus() == null) project.setPstatus("0");
        //因项目列表内仍使用createTime作为立项时间，用对createTime做设置作为临时处理方案
        if (project.getCreateDate() == null)
            project.setCreateDate(project.getProjectCreateDate()); //if (project.getCreateDate() == null) project.resetCreateDate();
        if (project.getIsHistory() == null) project.setIsHistory(1);

        //添加立项人和项目负责人
        if (!StringUtils.isEmpty(project.getCompanyId())) {
            insertProjectMember(project.getCompanyId(), project.getId(), ProjectConst.MEMBER_TYPE_CREATOR, project.getCreateBy());
            insertProjectMember(project.getCompanyId(), project.getId(), ProjectConst.MEMBER_TYPE_MANAGER, project.getCreateBy());
            insertProjectMember(project.getCompanyId(), project.getId(), ProjectConst.MEMBER_TYPE_DESIGN, project.getCreateBy());
        }
        //添加乙方项目负责人
        if (!StringUtils.isEmpty(project.getCompanyBid())) {
            insertProjectMember(project.getCompanyBid(), project.getId(), ProjectConst.MEMBER_TYPE_MANAGER, project.getCreateBy());
            insertProjectMember(project.getCompanyBid(), project.getId(), ProjectConst.MEMBER_TYPE_DESIGN, project.getCreateBy());
        }
        //保存合同签订日期
        ProjectAuditEntity audit = new ProjectAuditEntity();
        audit.resetId();
        audit.setProjectId(project.getId());
        audit.setAuditDate(DateUtils.date2Str(project.getContractDate(), DateUtils.date_sdf2));
        audit.setAuditType("2");
        audit.setCreateBy(project.getCreateBy());
        audit.setCreateDate(project.getCreateDate());
        projectAuditDAO.insert(audit);
        //建立项目自定义属性
        projectService.createDefaultProjectProperty(project);

        //添加项目数据
        projectDAO.insert(project);

        //创建默认磁盘
        this.projectSkyDriverService.createProjectFile(project);
    }

    /**
     * 添加项目成员
     */
    private void insertProjectMember(String companyId, String projectId, Integer memberType, String userId) {
        if ((companyId == null) || (projectId == null) || (memberType == null)) return;
        MemberQueryDTO query = new MemberQueryDTO(companyId, projectId, memberType);
        if (projectMemberDAO.getProjectMember(query) == null) {
            ProjectMemberEntity member = new ProjectMemberEntity();
            member.resetId();
            member.setProjectId(projectId);
            member.setCompanyId(companyId);
            member.setAccountId(userId);
            //在当前用户无权限时，更改为默认的项目负责人
            String permissionId = ProjectConst.PERMISSION_MAPPER.get(memberType);
            if (permissionId != null) {
                List<String> list = companyDAO.listUserIdByCompanyIdAndPermissionId(companyId, permissionId);
                if ((userId == null) || ((list != null) && !(list.contains(userId)))) {
                    member.setAccountId(list.get(0));
                }
            }
            if (!StringUtils.isEmpty(member.getCompanyId()) && (!StringUtils.isEmpty(member.getAccountId()))) {
                member.setCompanyUserId(companyDAO.getCompanyUserIdByCompanyIdAndUserId(member.getCompanyId(), member.getAccountId()));
            }
            member.setMemberType(memberType);
            member.setStatus(0);
            member.setDeleted(0);
            member.setSeq(0);
            member.resetCreateDate();
            member.setCreateBy(userId);
            projectMemberDAO.insert(member);
        }
    }

    /**
     * 转换数据为实体对象
     */
    private ProjectEntity createProjectDOFrom(ImportProjectDTO data, ImportProjectDefaultParamDTO params) {
        if (data == null) return null;

        ProjectEntity project = new ProjectEntity();

        //项目编号
        project.setProjectNo(data.getProjectNo());

        //项目名称
        if (StringUtil.isEmpty(data.getProjectName())) return createProjectDOError(data, "项目名称不能为空");
        project.setProjectName(data.getProjectName());

        //立项组织和立项人
        String creatorCompanyName = data.getCreatorOrgName();
        String creatorUserName = data.getCreatorUserName();

        CompanyUserDTO creator;
        if (StringUtils.isEmpty(creatorCompanyName) || StringUtils.isEmpty(creatorUserName)) {
            creator = new CompanyUserDTO();
            if (params != null) {
                //设置立项组织
                creator.setCompanyId(!StringUtil.isEmpty(params.getCreatorOrgId()) ? params.getCreatorOrgId() : params.getCompanyId());
                //设置立项人
                if (!StringUtil.isEmpty(params.getCompanyId()) && !StringUtil.isSame(params.getCompanyId(), params.getCreatorOrgId())) {
//                    final String ADMIN_OF_COMPANY = "50";
//                    List<String> adminList = companyDAO.listUserIdByCompanyIdAndPermissionId(creator.getCompanyId(), ADMIN_OF_COMPANY);
//                    if ((adminList != null) && (adminList.size() > 0)) {
//                        creator.setUserId(adminList.get(0));
//                    }
                    CompanyUserTableDTO systemManager = teamOperaterService.getSystemManager(creator.getCompanyId());
                    if(systemManager!=null){
                        creator.setUserId(systemManager.getUserId());
                    }
                } else {
                    creator.setUserId(params.getUserId());
                }
                //保存立项人和立项组织名称
                data.setCreatorOrgName(companyDAO.getCompanyName(creator.getCompanyId()));
                data.setCreatorUserName(companyUserDao.getUserNameByCompanyIdAndUserId(creator.getCompanyId(),creator.getUserId()));
            }
        } else {
            creator = companyDAO.getCompanyUserByCompanyNameAndUserName(creatorCompanyName, creatorUserName);
        }
        if ((creator != null) && ((creator.getCompanyId() != null) || (creator.getUserId() != null))) {
            project.setCompanyId(creator.getCompanyId());
            project.setCreateBy(creator.getUserId());
        }

        //合同签订日期
        project.setContractDate(data.getContractDate());

        //立项日期
        project.setProjectCreateDate(data.getProjectCreateDate());

        //项目地点-省/直辖市
        project.setProvince(data.getProvince());

        //项目地点-市/直辖市区
        project.setCity(data.getCity());

        //项目地点-区/县
        project.setCounty(data.getCounty());

        //项目地点-详细地址
        project.setDetailAddress(data.getDetailAddress());

        //项目状态
        String status = data.getProjectStatus();
        if ((status != null) && ProjectConst.STATUS_MAPPER.containsKey(status)) {
            project.setStatus(ProjectConst.STATUS_MAPPER.get(status));
        } else {
            project.setStatus(ProjectConst.PROJECT_STATUS_FINISHED);
        }

        //甲方
        String aCompanyName = data.getaCompanyName();
        if (!StringUtils.isEmpty(aCompanyName)) {
            String aCompanyId = null;
            try {
                aCompanyId = projectService.handleProjectConstructName(aCompanyName, creator.getCompanyId());
            } catch (Exception e) {
                //甲方未设置成功
            }
            project.setConstructCompany((!"".equals(aCompanyId) && null != aCompanyId) ? aCompanyId : null);
        }

        //乙方
        String bCompanyName = data.getbCompanyName();
        if (StringUtils.isEmpty(bCompanyName)) {
            String bCompanyId = companyDAO.getRootCompanyId(creator.getCompanyId());
            project.setCompanyBid(bCompanyId);
            //保存找到的乙方公司名
            bCompanyName = companyDAO.getCompanyName(bCompanyId);
            data.setbCompanyName(bCompanyName);
        } else {
            String bCompanyId = companyDAO.getCompanyIdByCompanyNameForB(bCompanyName, creator.getCompanyId());
            project.setCompanyBid(bCompanyId);
            if (bCompanyId == null) {
                data.setbCompanyName("");
            }
        }

        //协助立项人
        String helperCompanyUserId = null;
        if ((params.getCompanyId() != null) && (params.getUserId() != null) && !StringUtil.isSame(params.getCompanyId(), creator.getCompanyId())) {
            helperCompanyUserId = companyDAO.getCompanyUserIdByCompanyIdAndUserId(params.getCompanyId(), params.getUserId());
        }
        project.setHelperCompanyUserId(helperCompanyUserId);

        //目前模板内没有项目类型，默认为建筑设计
        project.setProjectType(SystemParameters.DEFAULT_PROJECT_TYPE_NAME);

        ProjectQueryDTO query = new ProjectQueryDTO(project.getCompanyId(), project.getProjectNo(), project.getProjectName(), project.getProjectCreateDate());
        if (projectDAO.getProject(query) != null) return createProjectDOError(data, "已存在项目");

        return project;
    }

    private ProjectEntity createProjectDOError(ImportProjectDTO data, String reason) {
        if (StringUtil.isEmpty(reason)) reason = "未知原因";
        data.setErrorReason(reason);
        return null;
    }
}
