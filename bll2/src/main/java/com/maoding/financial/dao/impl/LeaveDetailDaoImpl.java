package com.maoding.financial.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.financial.dto.LeaveDTO;
import com.maoding.financial.dto.QueryLeaveDTO;
import com.maoding.financial.entity.LeaveDetailEntity;
import com.maoding.financial.dao.LeaveDetailDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("leaveDetailDao")
public class LeaveDetailDaoImpl extends GenericDao<LeaveDetailEntity> implements LeaveDetailDao {
    @Override
    public LeaveDetailEntity getLeaveDetailByMainId(String mainId) {
        return this.sqlSession.selectOne("LeaveDetailEntityMapper.getLeaveDetailByMainId",mainId);
    }

    @Override
    public LeaveDTO getLeaveById(String id) {
        return this.sqlSession.selectOne("GetLeaveMapper.getLeaveById",id);
    }

    @Override
    public List<LeaveDTO> listLeave(QueryLeaveDTO query) {
        return this.sqlSession.selectOne("GetLeaveMapper.listLeave",query);
    }
}
