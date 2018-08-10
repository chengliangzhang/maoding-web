package com.maoding.projectcost.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.projectcost.dto.*;
import com.maoding.projectcost.service.ProjectCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostController
 * 类描述：项目费用
 * 作    者：MaoSF
 * 日    期：2016年7月8日-下午3:12:45
 */
@Controller
@RequestMapping("iWork/projectcost")
public class ProjectCostController extends BaseController {

    @Autowired
    private ProjectCostService projectCostService;

    @ModelAttribute
    public void before(){
        this.currentUserId = this.getFromSession("userId",String.class);
        this.currentCompanyId = this.getFromSession("companyId",String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId",String.class);
    }

    /**
     * 方法描述：合同回款界面数据
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId
     * @return:
     */
    @RequestMapping(value ={"/contractInfo"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getContractInfo(@RequestBody Map<String,Object> map)throws Exception{
        map.put("companyUserId",this.currentCompanyUserId);
        map.put("accountId",this.currentUserId);
        map.put("currentCompanyId",this.currentCompanyId);
        map.put("companyId",this.currentCompanyId);
        return projectCostService.getContractInfo(map);
    }

    /**
     * 方法描述：技术审查费界面数据
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId
     * @return:
     */
    @RequestMapping(value ={"/techicalReviewFeeInfo"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getTechicalReviewFeeInfo(@RequestBody Map<String,Object> map)throws Exception{
        map.put("companyUserId",this.currentCompanyUserId);
        map.put("currentCompanyId",this.currentCompanyId);
        map.put("companyId",this.currentCompanyId);
        map.put("accountId",this.currentUserId);
        return projectCostService.getTechicalReviewFeeInfo(map);
    }

    /**
     * 方法描述：合作设计费界面数据
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId
     * @return:
     */
    @RequestMapping(value ={"/cooperativeDesignFeeInfo"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getCooperativeDesignFeeInfo(@RequestBody Map<String,Object> map)throws Exception{
        map.put("companyId",this.currentCompanyId);
        map.put("currentCompanyId",this.currentCompanyId);
        map.put("companyUserId",this.currentCompanyUserId);
        map.put("accountId",this.currentUserId);
        return projectCostService.getCooperativeDesignFeeInfo(map);
    }

    /**
     * 方法描述：合作设计费界面数据
     * 作者：MaoSF
     * 日期：2017/3/7
     */
    @RequestMapping(value ={"/listProjectCost"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage listProjectCost(@RequestBody Map<String,Object> map)throws Exception{
        map.put("companyId",this.currentCompanyId);
        map.put("currentCompanyId",this.currentCompanyId);
        map.put("companyUserId",this.currentCompanyUserId);
        map.put("accountId",this.currentUserId);
        return AjaxMessage.succeed(projectCostService.listProjectCost(map)) ;
    }



    /**
     * 方法描述：其他费用界面数据（project，type=4：其他费用付款，type=5:其他费用收款）
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId
     * @return:
     */
    @RequestMapping(value ={"/getOtherFee"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getOtherFee(@RequestBody Map<String,Object> map)throws Exception{
        map.put("companyId",this.currentCompanyId);
        map.put("currentCompanyId",this.currentCompanyId);
        map.put("companyUserId",this.currentCompanyUserId);
        map.put("accountId",this.currentUserId);
        return projectCostService.getOtherFee(map);
    }


    /**
     * 方法描述：保存费用总金额（type=1:合同总金额,type=2:技术审查费，type=3：合作设计费（合作设计只有更新操作））
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId，type
     * @return:
     */
    @RequestMapping(value ={"/saveProjectCost"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveProjectCost(@RequestBody ProjectCostEditDTO dto) throws Exception{
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        return this.projectCostService.saveOrUpdateProjectCost(dto);
    }

    /**
     * 方法描述：新增/更新 费用节点（如果是合作设计费，必须传递costId（合作设计费），projectId，type）
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId，type（type=1:合同总金额,type=2:技术审查费，type=3：合作设计费，type=4：其他费用（付款），type=5:其他费用收款）
     * @return:
     */
    @RequestMapping(value ={"/saveProjectCostPoint"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveProjectCostPoint(@RequestBody ProjectCostPointDTO dto) throws Exception{
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        if("4".equals(dto.getType()) || "5".equals(dto.getType()) ){
            return this.projectCostService.saveOtherProjectCostPoint(dto);
        }
        return this.projectCostService.saveOrUpdateProjectCostPoint(dto);
    }

    /**
     * 方法描述：发起收款（包含：合同回款，技术审查费，合作设计费，其他费用），其中，pointId必传（对应节点的id）
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId，type
     * @return:
     */
    @RequestMapping(value ={"/saveOrUpdateReturnMoneyDetail"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrUpdateReturnMoneyDetail(@RequestBody ProjectCostPointDetailDTO dto) throws Exception{
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        return this.projectCostService.saveOrUpdateReturnMoneyDetail(dto);
    }

    /**
     * 方法描述：其他费用收款付款确认
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId，type
     * @return:
     */
    @RequestMapping(value ={"/saveOtherCostDetail"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOtherCostDetail(@RequestBody ProjectCostPointDetailDTO dto) throws Exception{
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        return this.projectCostService.saveOtherCostDetail(dto);
    }


    /**
     * 方法描述：删除费用节点
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId，type
     * @return:
     */
    @RequestMapping(value ={"/deleteProjectCostPoint/{id}"} , method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage deleteProjectCostPoint(@PathVariable("id") String id) throws Exception{
        return this.projectCostService.deleteProjectCostPoint(id,currentCompanyUserId);
    }


    /**
     * 方法描述：删除费用详情
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId，type
     * @return:
     */
    @RequestMapping(value ={"/deleteProjectCostPointDetail/{id}"} , method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage deleteProjectCostPointDetail(@PathVariable("id") String id) throws Exception{
        return this.projectCostService.deleteProjectCostPointDetail(id,currentCompanyUserId);
    }


    /**
     * 方法描述：删除收款费用详情
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId，type
     * @return:
     */
    @RequestMapping(value ={"/deleteProjectCostPaymentDetail/{id}"} , method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage deleteProjectCostPaymentDetail(@PathVariable("id") String id) throws Exception{
        return this.projectCostService.deleteProjectCostPaymentDetail(id,currentCompanyUserId);
    }

    /**
     * 方法描述：验证合作设计费
     * 作者：MaoSF
     * 日期：2017/3/7
     * @param:projectId，type
     * @return:
     */
    @RequestMapping(value ={"/validateTechnicalFee"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage validateTechnicalFee(@RequestBody ProjectCostPointDTO dto) throws Exception{
        return this.projectCostService.validateTechnicalFee(dto);
    }

    /**
     * 方法描述：修改付款或到款明细
     * 作者：wrb
     * 日期：2017/4/26
     * @param:
     * @return:
     */
    @RequestMapping(value ={"/saveCostPaymentDetail"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveCostPaymentDetail(@RequestBody ProjectCostPaymentDetailDTO dto) throws Exception{
        dto.setAccountId(this.currentUserId);
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setCurrentCompanyUserId(this.currentCompanyUserId);
        return this.projectCostService.updateCostPaymentDetail(dto);
    }
    
    /**
     * 描述     获取组织内各项目的收付款汇总列表
     * 日期     2018/8/10
     * @author  张成亮
     * @return  ProjectCostSummaryDTO列表
     * @param   query 查询条件
     *                startDate 起始日期
     *                endDate 终止日期
     **/
    @RequestMapping(value ={"/listProjectCostSummary"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage listProjectCostSummary(@RequestBody ProjectCostSummaryQueryDTO query) throws Exception{
        updateCurrentUserInfo(query);
        List<ProjectCostSummaryDTO> list = projectCostService.listProjectCostSummary(query);
        return AjaxMessage.succeed(list);
    }

}
