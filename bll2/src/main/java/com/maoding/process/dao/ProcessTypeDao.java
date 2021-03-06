package com.maoding.process.dao;


import com.maoding.activiti.dto.ProcessDefineDTO;
import com.maoding.activiti.dto.ProcessDefineGroupDTO;
import com.maoding.activiti.dto.ProcessDefineQueryDTO;
import com.maoding.core.base.dao.BaseDao;
import com.maoding.dynamicForm.dto.FormGroupDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.process.entity.ProcessTypeEntity;

import java.util.List;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
public interface ProcessTypeDao extends BaseDao<ProcessTypeEntity> {

    /**
     * 获取当前正在使用的流程类型
     */
    ProcessTypeEntity getCurrentProcessType(String companyId,String targetType);

    ProcessTypeEntity getCurrentProcessTypeByFormId(String companyId,String formId);

    /**
     * 描述       获取流程列表，分组返回列表
     * 日期       2018/9/13
     * @author   张成亮
     **/
    List<ProcessDefineGroupDTO> listProcessDefineWithGroup(ProcessDefineQueryDTO query);

    /**
     * 描述       获取流程列表
     * 日期       2018/9/13
     * @author   张成亮
     **/
    List<ProcessDefineDTO> listProcessDefine(ProcessDefineQueryDTO query);

    /**
     * 描述       获取单个流程
     * 日期       2018/9/20
     * @author   张成亮
     **/
    ProcessDefineDTO getProcessDefine(ProcessDefineQueryDTO query);

    /**
     * 作者：FYT
     * 日期：2018/9/17
     * 描述：审批表 启用/停用。根据dto中的id，和当前组织去查询ProcessTypeEntity（formId ,companyId = dto.currentCompanyId)
     */
    ProcessTypeEntity selectByTargetType (SaveDynamicFormDTO query);

    /**
     * 作者：FYT
     * 日期：2018/9/17
     * 描述：根据currentCompanyId查询最后的排序值seq
     */
    Integer selectMaxSeq (String currentCompanyId);

    //查询所有属于该分组的动态审批表
    List<ProcessTypeEntity> selectByCompanyIdFormType(FormGroupDTO formGroupDTO);

    int updateDynamicFormType(String oldFormType,String newFormType);

    //入参为“其他模板”的ID,formGroupDTO的ID，删除所有属于“其他模板”下的动态表单，批量将动态表单delete设置为1；
    int deleteBatchProcessType(FormGroupDTO formGroupDTO) throws Exception;

}
