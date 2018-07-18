package com.maoding.mytask.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.mytask.dto.MyTaskDTO;
import com.maoding.mytask.dto.MyTaskList2DTO;
import com.maoding.mytask.dto.MyTaskListDTO;
import com.maoding.mytask.dto.MyTaskQueryDTO;
import com.maoding.mytask.entity.MyTaskEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
public interface MyTaskDao extends BaseDao<MyTaskEntity> {

     /**
      * 方法描述：根据参数查询我的任务（companyId,companyUserId)
      * 作者：MaoSF
      * 日期：2016/12/1
      */
      List<MyTaskEntity> getMyTaskByParam(Map<String, Object> param);

     /**
      * 方法描述：根据参数查询我的任务（companyId,companyUserId)
      * 作者：MaoSF
      * 日期：2016/12/1
      */

      List<MyTaskDTO> getMyTaskListByParam(Map<String, Object> param);

     /**
      * 方法描述：根据参数查询我的任务个数（companyId,companyUserId)
      * 作者：MaoSF
      * 日期：2016/12/1
      */
      int getMyTaskByParamCount(Map<String, Object> param);
 
    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2017/6/26
     * @param:
     * @return:
     */
      int getMyTaskCount();

     /**
      * 根据tagetId修改任务状态
      * @param paraMap
      * @return
      */
      int updateStatesByTargetId(Map<String,Object> paraMap);

     /**
      * 方法描述：更改任务的状态
      * 作者：MaoSF
      * 日期：2017/1/6
      */
      int updateStatesByTargetId(MyTaskEntity entity);

     /**
      * 方法描述：更改param4的值（用于删除项目）
      * 作者：MaoSF
      * 日期：2017/3/29
      */
     int deleteProjectTask(String targetId);

    /**
     * 方法描述：更改param4的值（忽略任务使用）
     * 作者：MaoSF
     * 日期：2017/3/29
     */
    int deleteMyTask(Map<String,Object> param);

    /**
     * 作用：根据查询条件获取个人任务
     * 作者：ZCL
     * 日期：2017-8-30
     */
    MyTaskDTO getMyTask(MyTaskQueryDTO query) throws Exception;

    /**
     * 作用：根据任务ID获取个人任务
     * 作者：ZCL
     * 日期：2017-8-30
     */
    MyTaskDTO getMyTaskByTaskId(String taskId) throws Exception;

     /**
      * 方法描述：任务列表
      * 作者：MaoSF
      * 日期：2017/5/4
      */
     List<MyTaskListDTO> getMyTaskList(Map<String, Object> param);

    /**
     * 方法描述：任务列表
     * 作者：MaoSF
     * 日期：2017/5/4
     */
    List<MyTaskListDTO> getMyTaskList2(Map<String, Object> param);

    /**
     * 方法描述：根据项目id任务列表
     * 作者：MaoSF
     * 日期：2017/5/4
     */
    List<MyTaskDTO> getMyTaskByProjectId(Map<String, Object> param);

    List<MyTaskList2DTO> getMyTaskList4(Map<String, Object> param);

    /**
     * 方法描述：获取一个项目内设计负责人的个人任务
     * 作者：ZhangChengliang
     * 日期：2017/6/30
     */
    MyTaskEntity getMyTaskForDesignManagerByCompanyIdAndProjectId(@Param("companyId") String companyId, @Param("projectId") String projectId);
    MyTaskEntity getMyTaskForDesignManagerByCompanyIdAndProjectId(Map<String,String> query);


    /**
     * 生产列表，单独确认按钮获取maoding_web_my_task中id
     *
     * */
    String getCompleteTaskId(Map<String,Object> param);

    /** 根据taskId设置mytask及相应的子mytask为完成状态 */
    int finishMyTaskByTaskIdWithoutId(@Param("taskId") String taskId, @Param("ignoreId") String ignoreId);

    /**
     * @author  张成亮
     * @date    2018/7/18
     * @description     更新个人任务
     * @param   myTask 要修改的字段，如果为null则不修改
     * @param   query 要修改的条件
     **/
    void updateByQuery(@Param("myTask") MyTaskEntity myTask, @Param("query") MyTaskQueryDTO query);

    /**
     * @author  张成亮
     * @date    2018/7/18
     * @description     查询个人任务
     * @param   query 查询的条件
     * @return  个人任务列表
     **/
    List<MyTaskEntity> listByQuery(MyTaskQueryDTO query);
}
