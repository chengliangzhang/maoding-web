package com.maoding.process.service;

import com.maoding.process.dto.ProcessDTO;
import com.maoding.process.dto.ProcessEditDTO;
import com.maoding.process.dto.ProcessNodeDTO;
import com.maoding.process.dto.QueryProcessDTO;

import java.util.List;
import java.util.Map;

public interface BusinessProcessService {

    /**
     * 初始化流程
     */
    void initProcess(QueryProcessDTO query);

    /**
     * 查询组织的流程列表
     */
    Map<String,List<ProcessDTO>> getProcessByCompany(QueryProcessDTO query);

    /**
     * 保存流程（如果是 新增流程的化，流程的节点默认系统中同类型的节点信息）
     */
    int saveProcess(ProcessEditDTO dto) throws Exception;

    /**
     * 流程节点查询
     */
    List<ProcessNodeDTO> listProcessNode(QueryProcessDTO query);

    /**
     * （项目收支）删除流程
     */
    int deleteProcessForProjectPay(ProcessEditDTO dto) throws Exception;

    /**
     * 选择取消流程
     */
    int selectedProcessForProjectPay(ProcessEditDTO dto) throws Exception;

    int selectedProcessNodeStatus(ProcessEditDTO dto) throws Exception;
}
