package com.maoding.task.dto;

/**
 * Created by Idccapp21 on 2017/1/4.
 */
public class ProjectManagerDataDTO {

    private String id;


    /**
     * 公司名称
     */
    private String companyName;

    private String realName;
    /**
     * 员工名称
     */
    private String companyUserName;

    /**
     * 经营负责人id
     */
    private String operatorPersonId;

    /**
     * 经营负责人
     */
    private String operatorPersonName;

    /**
     * 是否可以设置经营负责人（1：可以，0：不可以）
     */
    private int isUpdateOperator;

    /**
     *任务负责人id
     */
    private String designPersonId;


    /**
     *任务负责人
     */
    private String designPersonName;

    /**
     * 是否可以设置设计负责人（1：可以，0：不可以）
     */
    private int isUpdateDesign;

    /**
     * 数状id
     */
    private String treeId;

    /**
     *父id
     */
    private String pid;


    /**
     * 1:立项方，2.合作方
     */
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyUserName() {
        return companyUserName;
    }

    public void setCompanyUserName(String companyUserName) {
        this.companyUserName = companyUserName;
    }

    public String getOperatorPersonName() {
        return operatorPersonName;
    }

    public void setOperatorPersonName(String operatorPersonName) {
        this.operatorPersonName = operatorPersonName;
    }

    public String getDesignPersonName() {
        return designPersonName;
    }

    public void setDesignPersonName(String designPersonName) {
        this.designPersonName = designPersonName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOperatorPersonId() {
        return operatorPersonId;
    }

    public void setOperatorPersonId(String operatorPersonId) {
        this.operatorPersonId = operatorPersonId;
    }

    public String getDesignPersonId() {
        return designPersonId;
    }

    public void setDesignPersonId(String designPersonId) {
        this.designPersonId = designPersonId;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getIsUpdateOperator() {
        return isUpdateOperator;
    }

    public void setIsUpdateOperator(int isUpdateOperator) {
        this.isUpdateOperator = isUpdateOperator;
    }

    public int getIsUpdateDesign() {
        return isUpdateDesign;
    }

    public void setIsUpdateDesign(int isUpdateDesign) {
        this.isUpdateDesign = isUpdateDesign;
    }
}
