package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.org.dto.DepartRoleDTO;
import com.maoding.system.dto.DataDictionaryDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectShowDTO
 * 类描述：项目立项-界面展示数据DTO
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:49:50
 */
public class ProjectShowDTO extends BaseDTO {

    /********************数据字典常量信息*********************/

    private String fastdfsUrl;
    /**
     * 设计范围列表(数据字典)
     */
    private List<DataDictionaryDTO> designRangeList = new ArrayList<DataDictionaryDTO>();

    /**
     * 设计依据列表(数据字典)
     */
    private List<DataDictionaryDTO> designBasicList  = new ArrayList<DataDictionaryDTO>();

    /**
     * 设计阶段列表(数据字典)
     */
    private List<DataDictionaryDTO> designContentList = new ArrayList<DataDictionaryDTO>();


    /**
     * 任务管理名称列表（数据字典）
     */
    List<DataDictionaryDTO> taskManageList = new ArrayList<DataDictionaryDTO>();

    /**
     * 建筑功能列表（数据字典）
     */
    private List<DataDictionaryDTO> constructionCateList = new ArrayList<DataDictionaryDTO>();

    /**
     * 项目类型列表（数据字典）
     */
    private List<DataDictionaryDTO> projectTypeList = new ArrayList<DataDictionaryDTO>();



    /*********************与当前公司相关信息**************************/

    /**
     * 甲方
     */
    private List<ProjectConstructDTO> partyAList = new ArrayList<ProjectConstructDTO>();

    /**
     * 乙方
     */
    private List<CompanyDTO> partyBList = new ArrayList<CompanyDTO>();

    /**
     * 当前人在当前组织下所有的角色--组织（部门）
     */
    private List<DepartRoleDTO> departRoleList = new ArrayList<DepartRoleDTO>();


    /*******************************项目信息*****************************************/

    private ProjectDTO projectDTO;

    public String getFastdfsUrl() {
        return fastdfsUrl;
    }

    public void setFastdfsUrl(String fastdfsUrl) {
        this.fastdfsUrl = fastdfsUrl;
    }

    public List<DataDictionaryDTO> getDesignRangeList() {
        return designRangeList;
    }

    public void setDesignRangeList(List<DataDictionaryDTO> designRangeList) {
        this.designRangeList = designRangeList;
    }

    public List<DataDictionaryDTO> getDesignBasicList() {
        return designBasicList;
    }

    public void setDesignBasicList(List<DataDictionaryDTO> designBasicList) {
        this.designBasicList = designBasicList;
    }

    public List<DataDictionaryDTO> getDesignContentList() {
        return designContentList;
    }

    public void setDesignContentList(List<DataDictionaryDTO> designContentList) {
        this.designContentList = designContentList;
    }

    public List<DataDictionaryDTO> getTaskManageList() {
        return taskManageList;
    }

    public void setTaskManageList(List<DataDictionaryDTO> taskManageList) {
        this.taskManageList = taskManageList;
    }

    public List<DataDictionaryDTO> getConstructionCateList() {
        return constructionCateList;
    }

    public void setConstructionCateList(List<DataDictionaryDTO> constructionCateList) {
        this.constructionCateList = constructionCateList;
    }

    public List<DataDictionaryDTO> getProjectTypeList() {
        return projectTypeList;
    }

    public void setProjectTypeList(List<DataDictionaryDTO> projectTypeList) {
        this.projectTypeList = projectTypeList;
    }

    public List<ProjectConstructDTO> getPartyAList() {
        return partyAList;
    }

    public void setPartyAList(List<ProjectConstructDTO> partyAList) {
        this.partyAList = partyAList;
    }

    public List<CompanyDTO> getPartyBList() {
        return partyBList;
    }

    public void setPartyBList(List<CompanyDTO> partyBList) {
        this.partyBList = partyBList;
    }

    public ProjectDTO getProjectDTO() {
        return projectDTO;
    }

    public void setProjectDTO(ProjectDTO projectDTO) {
        this.projectDTO = projectDTO;
    }

    public List<DepartRoleDTO> getDepartRoleList() {
        return departRoleList;
    }

    public void setDepartRoleList(List<DepartRoleDTO> departRoleList) {
        this.departRoleList = departRoleList;
    }
}
