package com.maoding.projectcost.service;

import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.invoice.dto.InvoiceEditDTO;
import com.maoding.projectcost.dto.*;
import com.maoding.projectcost.entity.ProjectCostEntity;
import com.maoding.task.entity.ProjectTaskEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostService
 * 类描述：费用service
 * 作    者：MaoSF
 * 日    期：2016年7月19日-下午5:28:54
 */
public interface ProjectCostService extends BaseService<ProjectCostEntity>{
    /**
     * 方法描述：设置合同总金额
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    AjaxMessage saveOrUpdateProjectCost(ProjectCostEditDTO projectCostDto)throws Exception;


    /**
     * 方法描述：添加修改回款
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    AjaxMessage saveOrUpdateProjectCostPoint(ProjectCostPointDTO projectCostPointDTO)throws Exception;



    /**
     * 方法描述：添加修改回款
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    AjaxMessage saveOtherProjectCostPoint(ProjectCostPointDTO projectCostPointDTO)throws Exception;

    /**
     * 方法描述：发起收款
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    AjaxMessage saveOrUpdateReturnMoneyDetail(ProjectCostPointDetailDTO projectCostPointDetailDTO)throws Exception;

    /**
     * 方法描述：发起收款
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    AjaxMessage saveOtherCostDetail(ProjectCostPointDetailDTO projectCostPointDetailDTO)throws Exception;

    /**
     * 方法描述：查询合同回款（projectId）
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    AjaxMessage getContractInfo(Map<String,Object> map)throws Exception;


    /**
     * 方法描述：查询技术审查费（projectId）
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    AjaxMessage getTechicalReviewFeeInfo(Map<String,Object> map)throws Exception;


    /**
     * 方法描述：合作设计费（projectId，companyId:当前公司id）
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    AjaxMessage getCooperativeDesignFeeInfo(Map<String,Object> map)throws Exception;

    /**
     * 方法描述：合作设计费（projectId，companyId:当前公司id,payType:1=收款，2=付款 ）
     * 作者：MaoSF
     * 日期：2018/8/8
     */
    List<Map<String, Object>> listProjectCost(Map<String,Object> map)throws Exception;


    /**
     * 方法描述：删除费用（目前界面上没有删除操作。用于删除签发的任务时候，如果不存在签发的记录，则合作设计费删除）
     * 作者：MaoSF
     * 日期：2017/3/2
     * @param:
     * @return:
     */
    AjaxMessage deleteProjectCost(String id,String accountId) throws Exception;

    /**
     * 方法描述：删除费用节点
     * 作者：MaoSF
     * 日期：2017/3/2
     * @param:
     * @return:
     */
    AjaxMessage deleteProjectCostPoint(String id,String accountId) throws Exception;

    /**
     * 方法描述：删除发起收款明细节点
     * 作者：MaoSF
     * 日期：2017/3/2
     * @param:
     * @return:
     */
    AjaxMessage deleteProjectCostPointDetail(String id,String companyUserId) throws Exception;
    AjaxMessage deleteProjectCostPointDetail(String id,String companyUserId,Boolean isAddDynamic) throws Exception;

    /**
     * 方法描述：删除收款明细节点
     * 作者：MaoSF
     * 日期：2017/4/27
     * @param:
     * @return:
     */
    AjaxMessage deleteProjectCostPaymentDetail(String id,String accountId) throws Exception;


    /**
     * 方法描述：合同乙方更改技术审查费
     * 作者：MaoSF
     * 日期：2017/3/2
     * @param:flag(1:重新添加，2.全部删除，3.先删除后添加）
     * @return:
     */
    AjaxMessage handPartBChange(String id,String accountId,int flag) throws Exception;

    /**
     * 方法描述：查询合同回款(map:projectId)map.put("type"="4"：付款，5：收款);
     * 作者：chenzhujie
     * 日期：2017/3/1
     */
    AjaxMessage getOtherFee(Map<String,Object> map)throws Exception;

    /***************************============技术审核==================***************/

    /**
     * 方法描述：验证合作设计费
     * 作者：MaoSF
     * 日期：2017/3/12
     * @param:
     * @return:
     */
    AjaxMessage validateTechnicalFee(ProjectCostPointDTO projectCostPointDTO)throws Exception;

    /**
     * 方法描述：保存付款或到款明细
     * 作者：wrb
     * 日期：2017/4/26
     */
    AjaxMessage saveCostPaymentDetail(ProjectCostPaymentDetailDTO dto)throws Exception;

    /**
     * 财务处理发票信息
     */
    AjaxMessage saveCostPointDetailForInvoice(InvoiceEditDTO dto)throws Exception;

    /**
     * 方法描述：修改付款或到款明细
     * 作者：wrb
     * 日期：2017/4/26
     */
    AjaxMessage updateCostPaymentDetail(ProjectCostPaymentDetailDTO dto)throws Exception;

    /**
     * 方法描述：合作设计费（技术审查费）详情
     * 作者：MaoSF
     * 日期：2017/3/9
     * @param:map(pointDetailId,taskType)
     */
    ProjectCostPointDataForMyTaskDTO getProjectCostPointDetailForMyTask(String paymentDetailId,String pointDetailId,int taskType,String companyId) throws Exception;


    void saveProjectCost(ProjectTaskEntity task, String currentCompanyId) throws Exception;

    /**
     * 描述     获取组织内各项目的收付款汇总列表
     * 日期     2018/8/10
     * @author  张成亮
     * @return  ProjectCostSummaryDTO列表
     * @param   query 查询条件
     *                startDate 起始日期
     *                endDate 终止日期
     **/
    List<ProjectCostSummaryDTO> listProjectCostSummary(ProjectCostSummaryQueryDTO query);

    /**
     * 描述     分页获取组织内各项目的收付款汇总列表
     * 日期     2018/8/10
     * @author  张成亮
     * @return  ProjectCostSummaryDTO列表
     * @param   query 查询条件
     *                startDate 起始日期
     *                endDate 终止日期
     **/
    CorePageDTO<ProjectCostSummaryDTO> listPageProjectCostSummary(ProjectCostSummaryQueryDTO query);
}
