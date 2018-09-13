package com.maoding.process.dao;


import com.maoding.activiti.dto.ProcessDefineDTO;
import com.maoding.activiti.dto.ProcessDefineGroupDTO;
import com.maoding.activiti.dto.ProcessDefineQueryDTO;
import com.maoding.core.base.dao.BaseDao;
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
}
