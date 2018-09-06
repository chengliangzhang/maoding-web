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
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/iWork/leave")
public class LeaveController extends BaseController{

    @Autowired
    private DataDictionaryService dataDictionaryService;


    @Autowired
    private LeaveService leaveService;

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }


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
    @RequestMapping(value = "/getLeaveDetailForWeb", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getLeaveDetailForWeb(@RequestBody SaveLeaveDTO dto) throws Exception {
        updateCurrentUserInfo(dto);
        return AjaxMessage.succeed(leaveService.getLeaveDetail(dto.getId()));
//        return ResponseBean.responseSuccess().addData("leaveDetail",leaveService.getLeaveDetail(dto.getId()));
    }

}
