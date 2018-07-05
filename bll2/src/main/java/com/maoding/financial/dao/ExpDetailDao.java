package com.maoding.financial.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.financial.dto.ExpDetailDTO;
import com.maoding.financial.entity.ExpDetailEntity;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpDetailDao
 * 描    述 : 报销明细Dao
 * 作    者 : LY
 * 日    期 : 2016/7/26 15:12
 */
public interface ExpDetailDao  extends BaseDao<ExpDetailEntity> {

    /**
     * 方法描述：根据报销主表Id删除明细
     * 作   者：LY
     * 日   期：2016/7/27 9:46
     * @param mainId(报销主表Id)
     * @return
     *
    */
    public int deleteByMainId(String mainId);

    /**
     * 方法描述：根据报销主表Id获取明细
     * 作   者：LY
     * 日   期：2016/7/27 9:46
     * @param mainId(报销主表Id)
     * @return
     *
     */
    public List<ExpDetailEntity> selectByMainId(String mainId);

    /**
     * 方法描述：根据报销主表Id获取明细包含报销类别名称和项目名称等
     * 作   者：LY
     * 日   期：2016/7/29 18:46
     * @param  mainId(报销主表Id)
     * @return
     *
    */
    public List<ExpDetailDTO> selectDetailDTOByMainId(String mainId);
}