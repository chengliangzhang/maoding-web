package com.maoding.financial.service;

import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.financial.dto.*;
import com.maoding.financial.entity.ExpMainEntity;
import com.maoding.org.dto.CompanyUserTableDTO;
import com.maoding.project.dto.ProjectDTO;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpMainService
 * 描    述 : 报销主表Service
 * 作    者 : LY
 * 日    期 : 2016/7/26-15:58
 */
public interface ExpMainService extends BaseService<ExpMainEntity>{

    /**
     * 方法描述：报销增加或者修改
     * 作   者：LY
     * 日   期：2016/7/26 17:35
    */
     AjaxMessage saveOrUpdateExpMainAndDetail(ExpMainDTO dto, String userId, String companyId) throws Exception;

    /**
     * 方法描述：项目费用申请
     * 作   者：MaoSF
     * 日   期：2016/7/26 17:35
     */
    AjaxMessage applyProjectCost(ApplyProjectCostDTO dto) throws Exception;


    /**
     * 方法描述：根据companyId查询所有有效项目(我要报销 选择项目下拉框 )app
     * 作   者：LY
     * 日   期：2016/7/27 17:39
     * @return
     *
     */
    List<ProjectDTO> getProjectListWS(String companyId,String userId);

    /**
     * 方法描述：得到当前公司和当前组织下面人员
     * 作   者：LY
     * 日   期：2016/8/3 17:17
     * @param  companyId 公司Id  orgId 组织Id
     */
     List<CompanyUserTableDTO> getUserList(String companyId, String orgId) throws Exception;

     int getExpMainPageCount(Map<String, Object> param);

    /**
     * 方法描述：撤回报销
     * 作   者：LY
     * 日   期：2016/7/29 11:01
     @param id--报销单id  type--状态(3撤回)
     *
     */
     int recallExpMain(String id,String versionNum, String type) throws Exception;


    /**
     * 方法描述：退回报销
     * 作   者：LY
     * 日   期：2016/7/29 11:01
     * @param  dto -- mainId--报销单id  approveStatus--状态(2.退回) auditMessage审批意见
     */
     int recallExpMain(ExpAuditDTO dto) throws Exception;

    /**
     * 方法描述：报销详情
     * 作   者：LY
     * 日   期：2016/7/29 10:53
     * @param id--报销单id
     *
     */
     ExpMainDTO getExpMainPageDetail(String id) throws Exception;

    /**
     * 方法描述：删除报销
     * 作   者：LY
     * 日   期：2016/7/29 10:53
     * @param id--报销单id
     */
     int deleteExpMain(String id,String versionNum) throws Exception;

    /**
     * 方法描述：同意报销
     * 作   者：LY
     * 日   期：2016/8/1 15:08
     * @param  id--报销单id
     */
     int agreeExpMain(String id,String versionNum,String currentCompanyId, String currentUserId) throws Exception;


    /**
     * 方法描述：同意报销并转移审批人
     * 作   者：LY
     * 日   期：2016/8/1 15:08
     * @param  id--报销单id auditPerson--新审批人  userId用户Id
     */
     int agreeAndTransAuditPerExpMain(String id, String userId,String auditPerson,String versionNum,String currentCompanyId) throws Exception;

    /**
     * 财务拨款
     */
    int financialAllocation(String id, String currentCompanyUserId,String currentCompanyId, String accountId, Date d) throws Exception;
    int financialAllocation(String id, String currentCompanyUserId,String currentCompanyId, String accountId) throws Exception;

    /**
     * 财务退回
     */
    int financialRecallExpMain(FinancialAllocationDTO dto) throws Exception;

    /**
     * 财务退回2
     */
    int financialRecallExpMain2(FinancialAllocationDTO dto) throws Exception;
    /**
     * 方法描述：报销详情与审批记录
     * 作   者：LY
     * 日   期：2016/8/2 14:13
     * @param  id--报销单id
     */
     Map<String, Object> getExpMainDetail(String id) throws Exception;


    /**
     * 方法描述：报销汇总数量
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     * @param  param 查询条件
     */
     int getExpMainPageForSummaryCount(Map<String, Object> param);


    //===============================2.0=================================

    /**
     * 方法描述：我的报销列表
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     * @param  param 查询条件
     * @return
     *
     */
     List<ExpMainDTO> getExpMainListPage(Map<String,Object> param);

     int getExpMainListPageCount(Map<String, Object> param);


    /**
     * 方法描述：报销汇总List
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     * @param  param 查询条件
     */
     List<ExpMainDTO> getExpMainListPageForSummary(Map<String, Object> param);

    /**
     * 方法描述：报销汇总数量
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     * @param  param 查询条件
     */
     int getExpMainListPageForSummaryCount(Map<String, Object> param);

    /**
     * 方法描述：报销汇总金额
     * 作   者：ZCL
     * 日   期：2016/7/28 16:34
     * @param  param 查询条件
     */
    ExpSummaryDTO getExpMainSummary(Map<String, Object> param);

    /**
     * 方法描述：获取最大组织expNo + 1
     * 作   者：ZhujieChen
     * 日   期：2016/12/22
     */
     Map<String, Object> getMaxExpNo(Map<String, Object> param) throws Exception;

     /**
      * 方法描述：项目收支明细-台账模块获取费用申请，报销申请
      * 作   者：DongLiu
      * 日   期：2017/12/6 18:36
      * @param
      * @return
      *
     */
     StatisticDetailSummaryDTO getExpenditureCount(StatisticDetailQueryDTO param);

    /**
     * 请假汇总
     */
    List<LeaveDetailDTO> getLeaveDetailList(LeaveDetailQueryDTO queryDTO);
    /**
     * 请假total
     * */
    Integer getLeaveDetailCount(LeaveDetailQueryDTO queryDTO);

    void saveCopy(List<String> ccCompanyUserList,String sendCompanyUserId,String targetId,String operateRecordId) throws Exception;
    /**
     * 请假任务详情
     * */
    Map<String, Object> getLeaveDetail(LeaveDetailQueryDTO queryDTO) throws Exception;


    /**
     * 根据付款申请的id获取审核信息
     * @param relationId(发起付款申请的id)
     */
    Map<String,Object> getAuditInfoByRelationId(String relationId,String companyUserId) throws Exception;

    /**
     * 发送消息
     */
    void sendMessageForAudit(String mainId, String companyId, String companyUserId,Integer type,String accountId,String auditId,String approveStatus) throws Exception;

    /**
     * 待审批的,我提交的(type=1:我提交的，type=2：待审核的,type=3:我已经审核的)
     */
    List<AuditDataDTO> getAuditDataDTO(QueryAuditDTO query) throws Exception;

    /**
     * 出差审核通过的（不包含退回的）
     */
    List<AuditDataDTO> getPassAuditData(QueryAuditDTO query) throws Exception;

    CorePageDTO<ExpMainDTO> getAuditDataForWeb(QueryAuditDTO dto)throws Exception;

    Map<String,Object> getAuditDetailForExp(ExpMainDTO expMainDTO) throws Exception;


    List<ExpMainDTO> getAuditDataDetail(QueryAuditDTO dto);
}
