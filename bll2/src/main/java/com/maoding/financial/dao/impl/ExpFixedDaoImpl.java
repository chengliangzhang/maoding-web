package com.maoding.financial.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.financial.dao.ExpFixedDao;
import com.maoding.financial.dto.ExpFixedAmountDTO;
import com.maoding.financial.dto.ExpFixedDataDTO;
import com.maoding.financial.dto.ExpFixedMainDTO;
import com.maoding.financial.entity.ExpFixedEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpFixedDaoImpl
 * 描    述 : 固定支出DaoImpl
 * 作    者 : LY
 * 日    期 : 2016/8/4-11:02
 */

@Service("expFixedDao")
public class ExpFixedDaoImpl extends GenericDao<ExpFixedEntity> implements ExpFixedDao {

    /**
     * 方法描述：查询选择月的固定支出
     * 作   者：LY
     * 日   期：2016/8/4 15:25
     * @param  expDate 月份
     */
    public List<ExpFixedDataDTO> getExpFixedByExpDate(String companyId, String expDate){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("companyId", companyId);
        map.put("expDate", expDate);
        return this.sqlSession.selectList("ExpFixedEntityMapper.getExpFixedByExpDate", map);
    }

    /**
     * 方法描述：报销汇总列表数量
     * 作   者：LY
     * 日   期：2016/8/2 17:42
     */
    public int getExpDateCount(Map<String, Object> param){
        return this.sqlSession.selectOne("ExpFixedEntityMapper.getExpDateCount", param);
    }

    @Override
    public ExpFixedMainDTO getExpFixed(String companyId, String rootCompanyId, String expDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("companyId", companyId);
        map.put("rootCompanyId", rootCompanyId);
        map.put("expDate", expDate);
        List<ExpFixedMainDTO> list = this.sqlSession.selectList("GetExpFixedMapper.getExpFixed", map);
        if(!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<ExpFixedAmountDTO> getExpFixedByMonth(String companyId, String[] months) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("companyId", companyId);
        map.put("months", months);
        return this.sqlSession.selectList("GetExpFixedMapper.getExpFixedByMonth", map);
    }

    @Override
    public ExpFixedMainDTO getExpFixedDefault(String companyId,String rootCompanyId,String expDate) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("companyId", companyId);
        map.put("rootCompanyId", rootCompanyId);
        map.put("expDate", expDate);
        List<ExpFixedMainDTO> list = this.sqlSession.selectList("GetExpFixedMapper.getExpFixedDefault", map);
        if(!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public int updateAmount(ExpFixedEntity entity) {
        return this.sqlSession.update("ExpFixedEntityMapper.updateAmount",entity);
    }
}
