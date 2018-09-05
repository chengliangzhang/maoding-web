package com.maoding.financial.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.financial.dto.LeaveDTO;
import com.maoding.financial.dto.QueryLeaveDTO;
import com.maoding.financial.entity.LeaveDetailEntity;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpDetailDao
 * 描    述 : 请假明细Dao
 * 作    者 : MaoSF
 * 日    期 : 2016/7/26 15:12
 */
public interface LeaveDetailDao extends BaseDao<LeaveDetailEntity> {


    /**
     * 获取明细
     */
    LeaveDetailEntity getLeaveDetailByMainId(String mainId);

    /**
     * 获取详情信息
     * @param id(mainId)
     */
    LeaveDTO getLeaveById(String id);

    /**
     * 查询请假出差列表
     */
    List<LeaveDTO> listLeave(QueryLeaveDTO query);
}