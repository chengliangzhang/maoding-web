package com.maoding.financial.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.financial.dto.AuditDTO;
import com.maoding.financial.dto.ExpAuditDTO;
import com.maoding.financial.dto.ExpMainDTO;
import com.maoding.financial.entity.ExpAuditEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpAuditDao
 * 描    述 : 报销审核Dao
 * 作    者 : LY
 * 日    期 : 2016/7/26 15:11
 */
public interface ExpAuditDao extends BaseDao<ExpAuditEntity> {

    /**
     * 方法描述：根据报销主表Id把历史数据最新审核更新为"N"
     * 作   者：LY
     * 日   期：2016/7/27 10:25
     *
     * @param mainId(报销主表Id)
     * @return
     */
    public int updateIsNewByMainId(String mainId);

    /**
     * 方法描述：根据报销主表Id查询最新审核为"Y"
     * 作   者：LY
     * 日   期：2016/7/27 10:37
     *
     * @param mainId(报销主表Id)
     * @return
     */
    public List<ExpAuditEntity> selectByMainId(String mainId);

    ExpAuditEntity getLastAuditByMainId(String mainId);

    /**
     * 方法描述：根据报销主表Id更新审批状态
     * 作   者：LY
     * 日   期：2016/7/29 11:49
     *
     * @param
     * @return
     */
    public int updateByMainId(ExpAuditEntity auditEntity);

    /**
     * 方法描述：查询审批人
     *
     * @param id
     * @return
     */
    public ExpAuditDTO selectAuditPersonByMainId(String id);

    /**
     * 方法描述：转移审批人
     * 作   者：LY
     * 日   期：2016/8/1 15:29
     *
     * @param
     */
    public int transAuditPer(ExpAuditEntity auditEntity);

    /**
     * 方法描述：根据报销主表id查询审批记录
     * 作   者：LY
     * 日   期：2016/8/2 15:35
     *
     * @param id 报销主表id
     */
    public List<ExpMainDTO> selectAuditDetailByMainId(String id);

    /**
     * 查询审批记录（和 selectAuditDetailByMainId 一样 ）
     */
    List<AuditDTO> selectAuditByMainId(Map<String, Object> map);

    public List<ExpAuditEntity> selectDetailByMainId(String mainId);

    ExpAuditEntity selectLastRecallAudit(String mainId);
    /**
     * 获取最新的审批记录
     */
    ExpAuditEntity selectLastAudit(String mainId);
}