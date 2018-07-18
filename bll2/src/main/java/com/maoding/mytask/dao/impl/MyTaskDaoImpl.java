package com.maoding.mytask.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.mytask.dao.MyTaskDao;
import com.maoding.mytask.dto.MyTaskDTO;
import com.maoding.mytask.dto.MyTaskList2DTO;
import com.maoding.mytask.dto.MyTaskListDTO;
import com.maoding.mytask.dto.MyTaskQueryDTO;
import com.maoding.mytask.entity.MyTaskEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：MyTaskDaoImpl
 * 类描述：我的任务DaoImpl
 * 作    者：MaoSF
 * 日    期：2016年11月30日-下午3:10:45
 */
@Service("myTaskDao")
public class MyTaskDaoImpl extends GenericDao<MyTaskEntity> implements MyTaskDao {

    MyTaskDao dao = null; //调用xml内定义的SQL语句的对象

    /**
     * 方法描述：根据参数查询我的任务（companyId,companyUserId)
     * 作者：MaoSF
     * 日期：2016/12/1
     *
     * @param param
     * @param:
     * @return:
     */
    @Override
    public List<MyTaskEntity> getMyTaskByParam(Map<String, Object> param) {
        return this.sqlSession.selectList("MyTaskEntityMapper.getMyTaskByParam",param);
    }



    /**
     * 方法描述：根据参数查询我的任务（companyId,companyUserId)
     * 作者：MaoSF
     * 日期：2016/12/1
     *
     * @param param
     * @param:
     * @return:
     */
    @Override
    public List<MyTaskDTO> getMyTaskListByParam(Map<String, Object> param) {
        return this.sqlSession.selectList("GetMyTaskByPage.getMyTaskByParam",param);
    }
    /**
     * 方法描述：根据参数查询我的任务个数（companyId,companyUserId)
     * 作者：MaoSF
     * 日期：2016/12/1
     *
     * @param param
     * @param:
     * @return:
     */
    @Override
    public int getMyTaskByParamCount(Map<String, Object> param) {
        return this.sqlSession.selectOne("GetMyTaskByPage.getMyTaskByParamCount",param);
    }

    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2017/6/26
     */
    @Override
    public int getMyTaskCount() {
        return this.sqlSession.selectOne("MyTaskEntityMapper.getMyTaskCount");
    }

    /**
     * 方法描述：根据targetId修改状态
     * 作者：MaoSF
     * 日期：2016/12/1
     */
    @Override
    public int updateStatesByTargetId(Map<String,Object> paraMap){
        return this.sqlSession.update("MyTaskEntityMapper.updateByTargetId",paraMap);
    }

    /**
     * 方法描述：更改任务的状态
     * 作者：MaoSF
     * 日期：2017/1/6
     */
    @Override
    public int updateStatesByTargetId(MyTaskEntity entity) {
        return this.sqlSession.update("MyTaskEntityMapper.updateByTargetId2",entity);
    }


    /**
     * 方法描述：更改param4的值（用于删除项目）
     * 作者：MaoSF
     * 日期：2017/3/29
     */
    @Override
    public int deleteProjectTask(String targetId) {
        return this.sqlSession.update("MyTaskEntityMapper.deleteProjectTask",targetId);
    }

    /**
     * 方法描述：更改param4的值（忽略任务使用）
     * 作者：MaoSF
     * 日期：2017/3/29
     */
    @Override
    public int deleteMyTask(Map<String, Object> param) {
        return this.sqlSession.update("MyTaskEntityMapper.deleteMyTask",param);
    }

    /**
     * 作用：根据任务ID获取个人任务
     * 作者：ZCL
     * 日期：2017-5-20
     */
    @Override
    public MyTaskDTO getMyTask(MyTaskQueryDTO query) throws Exception {
        assert (query != null) : "getMyTask 参数错误";

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("handlerId",query.getUserId());
        queryMap.put("companyId",query.getCompanyId());
        queryMap.put("targetId",query.getTaskId());
        queryMap.put("status",query.getStatus());
        queryMap.put("taskType",13);
        List<MyTaskDTO> list = sqlSession.selectList("GetMyTaskByPage.getMyTaskByParam",queryMap);
        if (list.size() > 0){
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public MyTaskDTO getMyTaskByTaskId(String taskId) throws Exception {
        assert (taskId != null) : "getMyTaskByTaskId 参数错误";

        MyTaskQueryDTO query = new MyTaskQueryDTO();
        query.setTaskId(taskId);
        return getMyTask(query);
    }

    /**
     * 方法描述：任务列表
     * 作者：MaoSF
     * 日期：2017/5/4
     */
    @Override
    public List<MyTaskListDTO> getMyTaskList(Map<String, Object> param) {
        return this.sqlSession.selectList("GetMyTaskByPage.getMyTaskList",param);
    }

    /**
     * 方法描述：任务列表
     * 作者：MaoSF
     * 日期：2017/5/4
     *
     * @param param
     */
    @Override
    public List<MyTaskListDTO> getMyTaskList2(Map<String, Object> param) {
        return this.sqlSession.selectList("GetMyTaskByPage.getMyTaskList2",param);
    }

    /**
     * 方法描述：根据项目id任务列表
     * 作者：MaoSF
     * 日期：2017/5/4
     */
    @Override
    public List<MyTaskDTO> getMyTaskByProjectId(Map<String, Object> param) {
        return this.sqlSession.selectList("GetMyTaskByPage.getMyTaskByProjectId",param);
    }

    @Override
    public List<MyTaskList2DTO> getMyTaskList4(Map<String, Object> param) {
        return this.sqlSession.selectList("GetMyTaskByPage.getMyTaskList4",param);
    }

    /**
     * 方法描述：获取一个项目内设计负责人的个人任务
     * 作者：ZhangChengliang
     * 日期：2017/6/30
     *
     * @param companyId 公司编号
     * @param projectId 项目编号
     */
    @Override
    public MyTaskEntity getMyTaskForDesignManagerByCompanyIdAndProjectId(@Param("companyId") String companyId, @Param("projectId") String projectId) {
        Map<String,String> query = new HashMap<>();
        query.put("companyId",companyId);
        query.put("projectId",projectId);
        return getMyTaskForDesignManagerByCompanyIdAndProjectId(query);
    }

    @Override
    public MyTaskEntity getMyTaskForDesignManagerByCompanyIdAndProjectId(Map<String, String> query) {
        return dao.getMyTaskForDesignManagerByCompanyIdAndProjectId(query);
    }


    @Override
    public String getCompleteTaskId(Map<String, Object> param) {
        return this.sqlSession.selectOne("GetMyTaskByPage.getCompleteTaskId",param);
    }

    /**
     * 根据taskId设置mytask及相应的子mytask为完成状态
     *
     * @param taskId
     */
    @Override
    public int finishMyTaskByTaskIdWithoutId(@Param("taskId") String taskId, @Param("ignoreId") String ignoreId) {
        Map<String,Object> param = new HashMap<>();
        param.put("taskId",taskId);
        if (ignoreId != null) param.put("ignoreId",ignoreId);
        return this.sqlSession.update("MyTaskEntityMapper.finishMyTaskByTaskId",param);
    }

    @PostConstruct
    public void init(){
        dao = sqlSession.getMapper(MyTaskDao.class);
    }

}
