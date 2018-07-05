package com.maoding.financial.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.financial.dao.ExpDetailDao;
import com.maoding.financial.dto.ExpDetailDTO;
import com.maoding.financial.entity.ExpDetailEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpDetailDaoImpl
 * 描    述 : 报销明细DaoImpl
 * 作    者 : LY
 * 日    期 : 2016/7/26-15:51
 */
@Service("expDetailDao")
public class ExpDetailDaoImpl extends GenericDao<ExpDetailEntity> implements ExpDetailDao{

    /**
     * 方法描述：根据报销主表Id删除明细
     * 作   者：LY
     * 日   期：2016/7/27 9:46
     * @param mainId(报销主表Id)
     * @return
     *
     */
    public int deleteByMainId(String mainId){
        return this.sqlSession.delete("ExpDetailEntityMapper.deleteByMainId", mainId);
    }

    @Override
    public List<ExpDetailEntity> selectByMainId(String mainId) {
        return this.sqlSession.selectList("ExpDetailEntityMapper.selectByMainId", mainId);
    }

    /**
     * 方法描述：根据报销主表Id获取明细包含报销类别名称和项目名称等
     * 作   者：LY
     * 日   期：2016/7/29 18:46
     * @param  mainId(报销主表Id)
     * @return
     *
     */
    public List<ExpDetailDTO> selectDetailDTOByMainId(String mainId){
        return this.sqlSession.selectList("ExpDetailEntityMapper.selectDetailDTOByMainId", mainId);
    }

}
