package com.maoding.mytask.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.mytask.dto.DeliverDTO;
import com.maoding.mytask.dto.HandleMyTaskDTO;
import com.maoding.mytask.dto.MyTaskQueryDTO;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.system.service.SystemService;
import org.apache.shiro.authc.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp22 on 2017/3/6.
 */
@Controller
@RequestMapping("iWork/myTask")
public class MyTaskController extends BaseController {

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private SystemService systemService;

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }


    @RequestMapping(value = "/taskList", method = RequestMethod.GET)
    public String index(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        model.addAttribute("viewType", 0);
        return "views/task/myTasks";
    }

    @RequestMapping(value = "/taskList/{viewType}", method = RequestMethod.GET)
    public String index(ModelMap model, @PathVariable int viewType) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        model.addAttribute("viewType", viewType);
        return "views/task/myTasks";
    }

    @RequestMapping(value = "/taskList/{companyId}/{viewType}", method = RequestMethod.GET)
    public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model, @PathVariable String companyId, @PathVariable int viewType) throws Exception {
        String userId = this.currentUserId;
        AjaxMessage ajaxMessage = systemService.switchCompany(request, response, companyId, userId);
        if ("1".equals(ajaxMessage.getCode())) {
            throw new AccountException("切换团队失败");
        }
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        model.addAttribute("viewType", viewType);
        return "views/task/myTasks";
    }


    /**
     * 方法描述：查询我的任务
     * 作者：MaoSF
     * 日期：2017/3/6
     *
     * @param:分页参数（pageIndex，pageSize）任务类型（taskType），完成的情况（status:1，已完成。未完成，无需传递）
     * @return:
     */

    @RequestMapping(value = {"/getMyTask"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMyTask(@RequestBody Map<String, Object> map) throws Exception {
        map.put("companyId", this.currentCompanyId);
        map.put("handlerId", this.currentCompanyUserId);
        Map result = myTaskService.getMyTaskByParam(map);
        return AjaxMessage.succeed("查询成功").setData(result);
    }

    /**
     * 方法描述：查询我的任务
     * 作者：MaoSF
     * 日期：2017/3/6
     *
     * @param:分页参数（pageIndex，pageSize）任务类型（taskType），完成的情况（status:1，已完成。未完成，无需传递）
     * @return:
     */

    @RequestMapping(value = {"/getMyTaskList"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMyTaskList(@RequestBody Map<String, Object> map) throws Exception {
        map.put("companyId", this.currentCompanyId);
        map.put("handlerId", this.currentCompanyUserId);
        return AjaxMessage.succeed("查询成功").setData(myTaskService.getMyTaskList(map));
    }

    /**
     * 方法描述：查询我的任务
     * 作者：MaoSF
     * 日期：2017/3/6
     *
     * @param:分页参数（pageIndex，pageSize）任务类型（taskType），完成的情况（status:1，已完成。未完成，无需传递）
     * @return:
     */

    @RequestMapping(value = {"/getMyTaskList2"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMyTaskList2(@RequestBody Map<String, Object> map) throws Exception {
        map.put("companyId", this.currentCompanyId);
        map.put("handlerId", this.currentCompanyUserId);
        map.put("accountId",this.currentUserId);
        return AjaxMessage.succeed("查询成功").setData(myTaskService.getMyTaskList2(map));
    }


    /**
     * 方法描述：查询我的任务(新版本)
     * 作者：MaoSF
     * 日期：2017/3/6
     * @param:分页参数（pageIndex，pageSize）完成的情况（status:1，已完成。未完成，无需传递）
     */
    @RequestMapping(value = "getMyTaskList4",method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMyTaskList4(@RequestBody Map<String, Object> map)throws Exception{
        map.put("companyId", this.currentCompanyId);
        map.put("handlerId", this.currentCompanyUserId);
        map.put("accountId", this.currentUserId);
        return AjaxMessage.succeed( myTaskService.getMyTaskList4(map));
    }

    /**
     * 方法描述：查询我的任务(根据projectId查询，如果projectId为null。则查询报销数据)
     * 作者：MaoSF
     * 日期：2017/3/6
     * @param:分页参数（pageIndex，pageSize）任务类型（taskType）
     * @return:
     */

    @RequestMapping(value = {"/getMyTaskByProjectId"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMyTaskByProjectId(@RequestBody Map<String, Object> map) throws Exception {
        map.put("companyId", this.currentCompanyId);
        map.put("handlerId", this.currentCompanyUserId);
        map.put("accountId", this.currentUserId);
        return myTaskService.getMyTaskByProjectId(map);
    }

    /**
     * 方法描述：处理任务
     * 作者：MaoSF
     * 日期：2017/1/11
     */
    @RequestMapping("/handleMyTask")
    @ResponseBody
    public AjaxMessage handleMyTask(@RequestBody HandleMyTaskDTO dto) throws Exception {
        dto.setAccountId(this.currentUserId);
        return   this.myTaskService.handleMyTask(dto);
    }

    /**
     * @author  张成亮
     * @date    2018/7/18
     * @description     查询交付任务
     **/
    @RequestMapping("/listDeliver")
    @ResponseBody
    public AjaxMessage listDeliver(@RequestBody MyTaskQueryDTO query) throws Exception {
        List<DeliverDTO> list = myTaskService.listDeliver(query);
        return AjaxMessage.succeed(list);
    }
}
