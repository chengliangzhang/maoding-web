package com.maoding.task.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.task.dto.ProjectProcessTimeDTO;
import com.maoding.task.entity.ProjectProcessTimeEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskDao
 * 类描述：项目任务
 * 作    者：MaoSF
 * 日    期：2016年12月28日-上午10:13:28
 */
public interface ProjectProcessTimeDao extends BaseDao<ProjectProcessTimeEntity> {

    /**
     * @description 查询更改历史字符串
     * @author  张成亮
     * @date    2018/6/28 11:09
     * @param
     * @return
     **/
    public List<ProjectProcessTimeDTO> listTimeHistory(Map<String,Object> param);

    public List<ProjectProcessTimeEntity> getProjectProcessTime(Map<String,Object> param);


    public int   deleteByTargetId (Map<String,Object> param)throws Exception;

    /**
     * 方法描述：根据任务id删除
     * 作者：MaoSF
     * 日期：2017/3/3
     * @param:
     * @return:
     */
    int deleteByTaskId(String taskId);


    public ProjectProcessTimeEntity getProjectProcessTimeByTargetId(Map<String,Object> param)throws Exception;
}