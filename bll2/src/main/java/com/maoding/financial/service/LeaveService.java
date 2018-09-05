package com.maoding.financial.service;

import com.maoding.core.bean.AjaxMessage;
import com.maoding.financial.dto.LeaveDTO;
import com.maoding.financial.dto.SaveLeaveDTO;

public interface LeaveService {

    /**
     * 保存请假信息
     */
    AjaxMessage saveLeave(SaveLeaveDTO dto) throws Exception;

    LeaveDTO getLeaveDetail(String id) throws Exception;

}
