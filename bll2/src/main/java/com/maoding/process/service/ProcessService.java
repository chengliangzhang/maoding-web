package com.maoding.process.service;

import com.maoding.process.dto.ProcessDTO;
import com.maoding.process.dto.ProcessEditDTO;
import com.maoding.process.dto.ProcessNodeDTO;
import com.maoding.process.dto.QueryProcessDTO;

import java.util.List;
import java.util.Map;

public interface ProcessService  {

    void initProcess(QueryProcessDTO query);

    Map<String,List<ProcessDTO>> getProcessByCompany(QueryProcessDTO query);

    int saveProcess(ProcessEditDTO dto) throws Exception;

    List<ProcessNodeDTO> listProcessNode(QueryProcessDTO query);
}
