package com.maoding.project.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dao.ProjectConstructDao;
import com.maoding.project.dto.ProjectConstructDTO;
import com.maoding.project.entity.ProjectConstructEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectConstructDaoImpl
 * 类描述：建设单位DaoImpl
 * 作    者：LY
 * 日    期：2016年7月7日-下午3:44:06
 */

@Service("projectConstructDao")
public class ProjectConstructDaoImpl extends GenericDao<ProjectConstructEntity> implements ProjectConstructDao {

    @Override
    public List<ProjectConstructDTO> getConstructByCompanyId(String companyId){
        List<ProjectConstructDTO> list = this.sqlSession.selectList("ProjectConstructEntityMapper.selectByCompanyId",companyId);
        return list;
    }



    /**
     * 方法描述：根据当前组织ID查找
     * 作        者：wangrb
     * 日        期：2015年11月26日-下午5:01:44
     * @param paramMap
     * @return
     */
    public ProjectConstructDTO getConstructByName(Map<String,Object> paramMap){
        ProjectConstructDTO projectConstructDTO= this.sqlSession.selectOne("ProjectConstructEntityMapper.selectByCompanyName",paramMap);
        return projectConstructDTO;
    }
    /**
     * 方法描述：根据当前组织ID查找
     * 作        者：wangrb
     * 日        期：2015年11月26日-下午5:01:44
     *
     * @param param
     * @return
     */
    @Override
    public List<ProjectConstructDTO> getConstructByParam(Map<String, Object> param) {
        List<ProjectConstructDTO> list = this.sqlSession.selectList("ProjectConstructEntityMapper.getConstructByParam",param);
        return list;
    }

    /**
     * 方法描述：获取5个常用的建设单位
     * 作者：MaoSF
     * 日期：2016/8/26
     *
     * @param companyId
     * @param:
     * @return:
     */
    @Override
    public List<ProjectConstructEntity> selectUsedConstructByCompanyId(String companyId) {
        return this.sqlSession.selectList("ProjectConstructEntityMapper.selectUsedConstructByCompanyId",companyId);
    }
}
