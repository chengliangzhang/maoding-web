package com.maoding.financial.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.financial.dao.ExpCategoryDao;
import com.maoding.financial.dto.ExpCategoryDataDTO;
import com.maoding.financial.dto.ExpCountDTO;
import com.maoding.financial.dto.QueryExpCategoryDTO;
import com.maoding.financial.dto.SaveExpCategoryShowStatusDTO;
import com.maoding.financial.entity.ExpCategoryEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpCategoryDaoImpl
 * 描    述 : 报销类别DaoImpl
 * 作    者 : MaoSF
 * 日    期 : 2016/10/09-15:52
 */

@Service("expCategoryDao")
public class ExpCategoryDaoImpl extends GenericDao<ExpCategoryEntity> implements ExpCategoryDao {


    @Override
    public int insert(ExpCategoryEntity entity){
        entity.setStatus("0");
        if(entity.getSeq()==null || 0==entity.getSeq())
        {
            int seq = this.getMaxExpCategorySeq(entity.getCompanyId());
            entity.setSeq(seq);
        }
        return super.insert(entity);
    }

    /**
     * 方法描述：根据相关参数查找
     * 作        者：MaoSF
     * 日        期：2016年10月09日-下午2:46:28
     *
     * @param map
     * @return
     */
    @Override
    public List<ExpCategoryEntity> getDataByParemeter(Map<String, Object> map) {
        return this.sqlSession.selectList("ExpCategoryEntityMapper.selectByParemeter",map);
    }

    /**
     * 方法描述：根据父id和那么查询
     * 作者：MaoSF
     * 日期：2016/10/13
     *
     * @param pid
     * @param name
     * @param:
     * @return:
     */
    @Override
    public ExpCategoryEntity selectByName(String pid, String name) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("pid",pid);
        map.put("name",name);
        return this.sqlSession.selectOne("ExpCategoryEntityMapper.selectByName",map);
    }

    @Override
    public ExpCountDTO  getExpCategoryByCompanyId(String companyId) {
        return this.sqlSession.selectOne("ExpCategoryEntityMapper.getExpCategoryByCompanyId",companyId);
    }

    /**
     * 方法描述：获取最大的seq值
     * 作者：MaoSF
     * 日期：2016/10/9
     */
    @Override
    public int getMaxExpCategorySeq(String companyId) {
        return this.sqlSession.selectOne("ExpCategoryEntityMapper.getMaxExpCategorySeq",companyId);
    }

    /**
     * 方法描述：删除子类别
     * 作   者：LY
     * 日   期：2016/7/27 9:46
     */
    public int deleteByPId(Map<String, Object> map){
        return this.sqlSession.delete("ExpCategoryEntityMapper.deleteByPid", map);
    }

    @Override
    public List<ExpCategoryDataDTO> getExpCategoryList(QueryExpCategoryDTO query) {
        return this.sqlSession.selectList("ExpCategoryEntityMapper.getExpCategoryList",query);
    }

    @Override
    public List<ExpCategoryDataDTO> getExpTypeListForProfitReport(String companyId, String subCompanyId) {
        Map<String,Object> map = new HashMap<>();
        map.put("companyId",companyId);
        map.put("subCompanyId",subCompanyId);
        map.put("showStatus","1");
        return this.sqlSession.selectList("ExpCategoryEntityMapper.getExpTypeListForProfitReport",map);

    }

    @Override
    public List<ExpCategoryDataDTO> getExpTypeListForProfitReport(String companyId, List<String> subCompanyIdList) {
        Map<String,Object> map = new HashMap<>();
        map.put("companyId",companyId);
        map.put("companyIdList",subCompanyIdList);
        map.put("showStatus","1");
        return this.sqlSession.selectList("ExpCategoryEntityMapper.getExpTypeListForProfitReport",map);
    }

    @Override
    public List<ExpCategoryDataDTO> getExpCategoryListByType(QueryExpCategoryDTO query) {
        return this.sqlSession.selectList("ExpCategoryEntityMapper.getExpCategoryListByType",query);
    }


    @Override
    public int saveExpCategoryShowStatus(SaveExpCategoryShowStatusDTO dto) {
        return this.sqlSession.update("ExpCategoryEntityMapper.saveExpCategoryShowStatus",dto);
    }

    @Override
    public String getCategoryTypeRelationCompanyId(String companyId) {
        return this.sqlSession.selectOne("ExpCategoryEntityMapper.getCategoryTypeRelationCompanyId",companyId);
    }

}
