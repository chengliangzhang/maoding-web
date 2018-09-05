package com.maoding.leave.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.bean.ResponseBean;
import com.maoding.core.constant.SystemParameters;
import com.maoding.financial.dto.SaveLeaveDTO;
import com.maoding.financial.service.LeaveService;
import com.maoding.system.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/iWork/leave")
public class LeaveController extends BaseController{

    @Autowired
    private DataDictionaryService dataDictionaryService;


    @Autowired
    private LeaveService leaveService;


    /**
     * 方法描述：获取请假类型
     * 作    者 : MaoSF
     * 日    期 : 2017/05/23
     */
    @RequestMapping("/getLeaveType")
    @ResponseBody
    public AjaxMessage getLeaveType() throws Exception {
        return AjaxMessage.succeed(this.dataDictionaryService.getSubDataByCode(SystemParameters.LEAVE));
    }

    /**
     * 方法描述：保存请假信息
     * 作    者 : MaoSF
     * 日    期 : 2017/05/23
     */
    @RequestMapping("/saveLeave")
    @ResponseBody
    public AjaxMessage saveLeave(@RequestBody SaveLeaveDTO dto) throws Exception {
        updateCurrentUserInfo(dto);
        return leaveService.saveLeave(dto);
    }

    /**
     * 方法描述：获取请假信息
     * 作    者 : MaoSF
     * 日    期 : 2017/05/23
     */
    @RequestMapping("/getLeaveDetail")
    @ResponseBody
    public ResponseBean getLeaveDetail(@RequestBody SaveLeaveDTO dto) throws Exception {
        updateCurrentUserInfo(dto);
        return ResponseBean.responseSuccess().addData("leaveDetail",leaveService.getLeaveDetail(dto.getId()));
    }

}
