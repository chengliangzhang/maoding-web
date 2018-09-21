package com.maoding.financial.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.financial.dto.*;
import com.maoding.financial.entity.ExpMainEntity;
import com.maoding.invoice.dto.InvoiceDTO;
import com.maoding.org.dto.CompanyRelationDTO;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpMainDao
 * 描    述 : 报销Dao
 * 作    者 : LY
 * 日    期 : 2016/7/26 15:13
 */
public interface ExpMainDao extends BaseDao<ExpMainEntity> {

    /**
     * 方法描述：我的报销列表
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     *
     * @param param 查询条件
     */
    public List<ExpMainDTO> getExpMainPage(Map<String, Object> param);


    public int getExpMainPageCount(Map<String, Object> param);

    /**
     * 方法描述：查询报销主表记录并关联账号表
     * 作   者：LY
     * 日   期：2016/8/2 15:10
     *
     * @param id 报销主表id
     */
    public ExpMainDTO selectByIdWithUserName(String id);

    /**
     * 方法描述：查询报销主表记录并关联账号表
     * 作   者：LY
     * 日   期：2016/8/2 15:10
     * @param param 报销主表id
     */
    public ExpMainDataDTO selectByIdWithUserNameMap(Map<String, Object> param);

    /**
     * 方法描述：报销汇总list
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     *
     * @param param 查询条件
     */
    public List<ExpMainDTO> getExpMainPageForSummary(Map<String, Object> param);


    /**
     * 方法描述：报销汇总列表数量
     * 作   者：LY
     * 日   期：2016/8/2 17:42
     */
    public int getExpMainPageForSummaryCount(Map<String, Object> param);

    /**
     * 方法描述：获取报销列表总金额
     * 作   者：ZCL
     * 日   期：2018/3/23 17:42
     */
    public ExpSummaryDTO getExpMainSummary(Map<String, Object> param);

    /**
     * 方法描述：获取过滤组织列表
     * 作   者：ZCL
     * 日   期：2018/4/2
     */
    public List<CompanyRelationDTO> listExpFilterCompany(Map<String, Object> param);

    /**
     * 方法描述：报销详细器查询
     * 作   者：LY
     * 日   期：2016/8/2 17:42
     */
    public ExpMainDTO getExpMainDetail(String mainId);

    /**
     * 方法描述：查询报销单（用于我的任务中）
     * 作者：MaoSF
     * 日期：2016/12/22
     *
     * @param:
     * @return:
     */
    public ExpMainDTO getByMainIdForMyTask(String mainId);


    /**
     * 方法描述：获取某组织中最大值
     * 作者：MaoSF
     * 日期：2016/8/5
     *
     * @param:
     * @return:
     */
    public String getMaxExpNo(Map<String, Object> param);

    /**
     * 方法描述：查询
     * 作   者：ChenZhuJie
     * 日   期：2016/12/27
     */
    public List<ExpMainEntity> selectByParam(Map<String, Object> param);

    /**
     * 获取审核信息
     */
    List<AuditDataDTO> getAuditData(QueryAuditDTO query);
	
    /**
     * 方法描述：项目收支明细-台账模块获取费用申请，报销申请
     * 作   者：DongLiu
     * 日   期：2017/12/6 18:36
     *
     * @param
     * @return
     */
    public StatisticDetailSummaryDTO getExpenditureCount(StatisticDetailQueryDTO param);

    /**
     * 请假汇总
     */
    List<LeaveDetailDTO> getLeaveDetailList(LeaveDetailQueryDTO queryDTO);
    /**
     * 请假total
     * */
    Integer getLeaveDetailCount(LeaveDetailQueryDTO queryDTO);

    String getTotalAmountByMainId(String mainId);

    /**
     * 根据关联项获取审批单
     */
    ExpMainDTO getExpMainByRelationId(String relationId);


    /**
     * 财务拨款信息
     */
    ExpMainDataDTO selectAllocationUser(Map<String, Object> param);

    /**
     * 我申请的报销/费用
     * @param dto
     * @return
     */
    List<AuditCommonDTO> getAuditDataForWeb(QueryAuditDTO dto);

    /**
     * 我申请的报销/费用   的分页
     * @param dto
     * @return
     */
    int getAuditDataForWebCount(QueryAuditDTO dto);

    /**
     * 我申请的   详情
     * @param dto
     * @return
     */
    List<ExpMainDTO> getAuditDataDetail(QueryAuditDTO dto);

}