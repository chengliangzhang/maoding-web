package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.constant.ProcessTypeConst;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/13
 * 类名: com.maoding.activiti.dto.ProcessDefineDTO
 * 作者: 张成亮
 * 描述: 
 **/
public class ProcessDefineDTO extends CoreShowDTO {
    /** id: 流程编号 */

    /** 流程说明 */
    private String documentation;

    /** 流程分类关键字，定义见ProcessTypeConst.PROCESS_TYPE_xxx */
    private String key;

    /** 流程类型，定义见ProcessTypeConst.TYPE_xxx */
    private Integer type;

    /** 分条件流程变量名称 **/
    private String varName;

    /** 分条件流程变量单位 **/
    private String varUnit;

    /** 是否启用 **/
    private Integer isActive;

    /** 是否设置了抄送人 **/
    private Integer isCCSet;

    /** 是否系统表单 **/
    private Integer isSystem;

    public Integer getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
    }

    public Integer getIsCCSet() {
        return isCCSet;
    }

    public void setIsCCSet(Integer isCCSet) {
        this.isCCSet = isCCSet;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVarUnit() {
        return varUnit;
    }

    public void setVarUnit(String varUnit) {
        this.varUnit = varUnit;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ProcessDefineDTO(){}
    public ProcessDefineDTO(String name,String documentation,String key,Integer type,String companyId){
        setName(name);
        setDocumentation(documentation);
        setKey(key);
        setType(type);
        setId(ProcessTypeConst.ID_PREFIX_PROCESS
                + companyId + ProcessTypeConst.ID_SPLIT
                + key + ProcessTypeConst.ID_SPLIT
                + type);
    }
}
