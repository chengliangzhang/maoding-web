package com.maoding.task.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.task.dao.ProjectProcessTimeDao;
import com.maoding.task.dto.ProjectProcessTimeDTO;
import com.maoding.task.entity.ProjectProcessTimeEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskRelationDaoImpl
 * 类描述：项目任务（任务签发组织之间的关系）
 * 作    者：MaoSF
 * 日    期：2016年12月28日-上午10:13:28
 */
@Service("projectProcessTimeDao")
public  class ProjectProcessTimeDaoImpl extends GenericDao<ProjectProcessTimeEntity> implements ProjectProcessTimeDao {

    /**
     * @param param
     * @return
     * @description 查询更改历史字符串
     * @author 张成亮
     * @date 2018/6/28 11:09
     **/
    @Override
    public List<ProjectProcessTimeDTO> listTimeHistory(Map<String, Object> param) {
        return this.sqlSession.selectList("ProjectProcessTimeEntityMapper.listTimeHistory",param);
    }

    @Override
    public List<ProjectProcessTimeEntity> getProjectProcessTime(Map<String,Object> param){
         return this.sqlSession.selectList("ProjectProcessTimeEntityMapper.selectByParam",param);
    }


    @Override
    public int  deleteByTargetId (Map<String,Object> param)throws Exception{
        return  this.sqlSession.delete("ProjectProcessTimeEntityMapper.deleteByTargetId",param);
    }

    /**
     * 方法描述：根据任务id删除
     * 作者：MaoSF
     * 日期：2017/3/3
     *
     * @param taskId
     * @param:
     * @return:
     */
    @Override
    public int deleteByTaskId(String taskId) {
        return  this.sqlSession.delete("ProjectProcessTimeEntityMapper.deleteByTaskId",taskId);
    }

    @Override
    public ProjectProcessTimeEntity getProjectProcessTimeByTargetId(Map<String,Object> param){
        return this.sqlSession.selectOne("ProjectProcessTimeEntityMapper.selectByTargetId",param);
    }
}