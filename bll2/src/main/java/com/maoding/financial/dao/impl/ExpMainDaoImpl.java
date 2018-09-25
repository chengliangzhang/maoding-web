package com.maoding.financial.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.financial.dao.ExpMainDao;
import com.maoding.financial.dto.*;
import com.maoding.financial.entity.ExpMainEntity;
import com.maoding.invoice.dto.InvoiceDTO;
import com.maoding.org.dto.CompanyRelationDTO;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpMainDaoImpl
 * 描    述 : 报销DaoImpl
 * 作    者 : LY
 * 日    期 : 2016/7/26-15:52
 */

@Service("expMainDao")
public class ExpMainDaoImpl extends GenericDao<ExpMainEntity> implements ExpMainDao {


    /**
     * 方法描述：我的报销列表
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     *
     * @param param 查询条件
     * @return
     */
    public List<ExpMainDTO> getExpMainPage(Map<String, Object> param) {
        return this.sqlSession.selectList("GetExpMainPageMapper.getExpMainPage", param);
    }

    public int getExpMainPageCount(Map<String, Object> param) {
        return this.sqlSession.selectOne("GetExpMainPageMapper.getExpMainPageCount", param);
    }


    /**
     * 方法描述：查询报销主表记录并关联账号表
     * 作   者：LY
     * 日   期：2016/8/2 15:10
     *
     * @param id 报销主表id
     */
    public ExpMainDTO selectByIdWithUserName(String id) {
        return this.sqlSession.selectOne("ExpMainEntityMapper.selectByIdWithUserName", id);
    }

    /**
     * 方法描述：查询报销主表记录并关联账号表
     * 作   者：LY
     * 日   期：2016/8/2 15:10
     */
    public ExpMainDataDTO selectByIdWithUserNameMap(Map<String, Object> param){
        return this.sqlSession.selectOne("ExpMainEntityMapper.selectByIdWithUserNameByParam", param);
    }

    /**
     * 方法描述：报销汇总list
     * 作   者：LY
     * 日   期：2016/7/28 16:34
     *
     * @param param 查询条件
     */
    public List<ExpMainDTO> getExpMainPageForSummary(Map<String, Object> param) {
        return this.sqlSession.selectList("GetExpMainPageMapper.getExpMainPageForSummary", param);
    }


    /**
     * 方法描述：报销汇总列表数量
     * 作   者：LY
     * 日   期：2016/8/2 17:42
     */
    public int getExpMainPageForSummaryCount(Map<String, Object> param) {
        return this.sqlSession.selectOne("GetExpMainPageMapper.getExpMainPageForSummaryCount", param);
    }

    /**
     * 方法描述：费用总额查询
     * 作   者：ZCL
     * 日   期：2018/3/23 17:42
     */
    @Override
    public ExpSummaryDTO getExpMainSummary(Map<String, Object> param) {
        return this.sqlSession.selectOne("GetExpMainPageMapper.summaryExpMain", param);
    }

    /**
     * 方法描述：获取过滤组织列表
     * 作   者：ZCL
     * 日   期：2018/4/2
     */
    @Override
    public List<CompanyRelationDTO> listExpFilterCompany(Map<String, Object> param) {
        return this.sqlSession.selectList("GetExpMainPageMapper.listExpFilterCompany", param);
    }

    /**
     * 方法描述：报销详细器查询
     * 作   者：LY
     * 日   期：2016/8/2 17:42
     */
    public ExpMainDTO getExpMainDetail(String mainId) {
        return this.sqlSession.selectOne("GetExpMainPageMapper.selectExpMainDetail", mainId);
    }

    /**
     * 方法描述：查询报销单（用于我的任务中）
     * 作者：MaoSF
     * 日期：2016/12/22
     *
     * @param mainId
     * @param:
     * @return:
     */
    @Override
    public ExpMainDTO getByMainIdForMyTask(String mainId) {
        return this.sqlSession.selectOne("GetExpMainPageMapper.getByMainIdForMyTask", mainId);
    }

    /**
     * 方法描述：获取某组织中最大值
     * 作者：MaoSF
     * 日期：2016/8/5
     *
     * @param:
     * @return:
     */
    @Override
    public String getMaxExpNo(Map<String, Object> param) {
        return this.sqlSession.selectOne("GetExpMainPageMapper.getMaxExpNo", param);
    }

    /**
     * 方法描述：查询
     * 作   者：ChenZhuJie
     * 日   期：2016/12/27
     */
    public List<ExpMainEntity> selectByParam(Map<String, Object> param) {
        return this.sqlSession.selectList("ExpMainEntityMapper.selectByParam", param);
    }

    @Override
    public StatisticDetailSummaryDTO getExpenditureCount(StatisticDetailQueryDTO param) {
        StatisticDetailSummaryDTO result = this.sqlSession.selectOne("ExpMainEntityMapper.getExpenditureCount", param);
        return result;
    }

    @Override
    public List<AuditDataDTO> getAuditData(QueryAuditDTO query) {
        return this.sqlSession.selectList("GetExpMainPageMapper.getAuditData",query);
    }

    @Override
    public List<LeaveDetailDTO> getLeaveDetailList(LeaveDetailQueryDTO queryDTO) {
        return this.sqlSession.selectList("ExpMainEntityMapper.getLeaveDetailList", queryDTO);
    }

    @Override
    public Integer getLeaveDetailCount(LeaveDetailQueryDTO queryDTO) {
        return this.sqlSession.selectOne("ExpMainEntityMapper.getLeaveDetailCount", queryDTO);
    }

    @Override
    public String getTotalAmountByMainId(String mainId) {
        return this.sqlSession.selectOne("GetExpMainPageMapper.getTotalAmountByMainId", mainId);
    }

    @Override
    public ExpMainDTO getExpMainByRelationId(String relationId) {
        return this.sqlSession.selectOne("GetExpMainPageMapper.getExpMainByRelationId", relationId);
    }

    @Override
    public ExpMainDataDTO selectAllocationUser(Map<String, Object> param) {
        return this.sqlSession.selectOne("ExpMainEntityMapper.selectAllocationUser", param);
    }

    @Override
    public List<AuditCommonDTO> getAuditDataForWeb(QueryAuditDTO dto) {
        return this.sqlSession.selectList("GetExpMainPageMapper.getAuditDataForWeb",dto);
    }

    @Override
    public List<CoreShowDTO> listAuditTypeName(QueryAuditDTO query) {
        return this.sqlSession.selectList("GetExpMainPageMapper.listAuditTypeName",query);
    }

    @Override
    public int getAuditDataForWebCount(QueryAuditDTO dto) {
        return this.sqlSession.selectOne("GetExpMainPageMapper.getAuditDataForWebCount",dto);
    }

    @Override
    public List<ExpMainDTO> getAuditDataDetail(QueryAuditDTO dto) {
        return this.sqlSession.selectList("GetExpMainPageMapper.getAuditDataDetail",dto);
    }
}
