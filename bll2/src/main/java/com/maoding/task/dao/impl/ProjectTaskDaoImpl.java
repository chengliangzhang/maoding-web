package com.maoding.task.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.task.dao.ProjectTaskDao;
import com.maoding.task.dto.*;
import com.maoding.task.entity.ProjectTaskEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskDaoImpl
 * 类描述：项目任务
 * 作    者：MaoSF
 * 日    期：2016年12月28日-上午10:13:28
 */
@Service("projectTaskDao")
public class ProjectTaskDaoImpl extends GenericDao<ProjectTaskEntity> implements ProjectTaskDao {
    /**
     * 方法描述：完成设计任务
     * 作者：ZhangChengliang
     * 日期：2018/3/29
     *
     * @param taskId
     */
    @Override
    public void finishSubDesignTask(String taskId) {
        this.sqlSession.update("ProjectTaskEntityMapper.finishSubDesignTask",taskId) ;
    }

    /**
     * 方法描述：获取任务完整信息
     * 作者：ZhangChengliang
     * 日期：2017/7/6
     *
     * @param id 任务编号，可以是设计内容、签发任务和生产任务
     * @return 任务的完整信息
     */
    @Override
    public TaskWithFullNameDTO getTaskByTaskId(String id) {
        if (id == null){
            return null;
        }
        return dao.getTaskByTaskId(id);
    }

    /**
     * 方法描述：根据taskPath设定节点的状态
     * 作者：MaoSF
     * 日期：2016/12/30
     */
    @Override
    public int updateProjectTaskStatus(ProjectTaskEntity taskEntity) {
        return this.sqlSession.update("ProjectTaskEntityMapper.updateProjectTaskStatus",taskEntity) ;
    }

    @Override
    public List<ProjectTaskDTO> selectProjectTaskList(Map<String,Object> param){
        return this.sqlSession.selectList("GetProjectTaskMapper.selectByParam",param);
    }


    @Override
    public int updateTaskStatusByParam(Map<String,Object> param){
        return this.sqlSession.update("ProjectTaskEntityMapper.updateByParam",param) ;
    }

    /**
     * 方法描述：获取任务的父级的名称
     * 作者：MaoSF
     * 日期：2017/1/4
     */
    @Override
    public String getTaskParentName(String id) {
        return this.sqlSession.selectOne("GetProjectTaskMapper.getTaskParentName",id) ;
    }


    @Override
    public String getProductRootTaskName(String projectId, String companyId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("companyId",companyId);
        map.put("projectId",projectId);
        return this.sqlSession.selectOne("GetProjectTaskMapper.getProductRootTaskName",map) ;
    }


    /**
     * 方法描述：查询子节点
     * 作者：MaoSF
     * 日期：2017/3/12
     */
    @Override
    public List<ProjectTaskEntity> getProjectTaskByPid(String taskPid) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("taskPid",taskPid);
        return  this.selectByParam(map) ;
    }

    @Override
    public List<ProjectTaskEntity> listTaskByPid(String taskPid) {
        return this.sqlSession.selectList("ProjectTaskEntityMapper.listTaskByPid",taskPid);
    }

    /**
     * 方法描述：根据项目id和公司id查询任务（用于经营分解时，判断是否存在已经签发给该公司的记录）
     * 作者：MaoSF
     * 日期：2017/2/27
     */
    @Override
    public List<ProjectTaskEntity> getProjectTaskByCompanyIdOfOperater(String projectId, String companyId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("projectId",projectId);
        map.put("companyId",companyId);
        return  this.selectByParam(map) ;
    }

    /**
     * 方法描述：根据taskPath查询所有的子任务(删除任务使用)
     * 作者：MaoSF
     * 日期：2017/1/4
     */
    @Override
    public List<ProjectTaskEntity> getProjectTaskByTaskPath(String taskPath) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("taskPath",taskPath);
        map.put("taskStatusFlag","1");
        return  this.selectByParam(map);
    }

    /***************计算任务状态******************/
    /**
     * 方法描述：根据id查询
     * 作者：MaoSF
     * 日期：2017/1/4
     */
    @Override
    public ProjectTaskDataDTO getProjectTaskById(String id, String companyId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        map.put("companyId",companyId);
        return this.sqlSession.selectOne("GetProjectTask2Mapper.getProjectTaskById",map) ;
    }


    @Override
    public int  updateByTaskId(ProjectTaskEntity projectTaskEntity){
        return this.sqlSession.update("ProjectTaskEntityMapper.updateByTaskId",projectTaskEntity);
    }

    @Override
    public int updateByIdOrModifyId(ProjectTaskEntity projectTaskEntity) {
        return this.sqlSession.update("ProjectTaskEntityMapper.updateByIdOrModifyId",projectTaskEntity);
    }

    @Override
    public int updateById(ProjectTaskEntity projectTaskEntity) {
        return this.sqlSession.update("ProjectTaskEntityMapper.updateById",projectTaskEntity);
    }

    /**
     * 方法描述：把部门字段设置为null,用于更换任务的组织
     * 作者：MaoSF
     * 日期：2017/3/20
     */
    @Override
    public int updateTaskOrgId(String id) {
        return this.sqlSession.update("ProjectTaskEntityMapper.updateTaskOrgId",id);
    }

    /**
     * 方法描述：获取我是任务负责人的任务
     * 作者：MaoSF
     * 日期：2017/3/22
     * @param: map(projectId,targetId:companyUserId)

     */
    @Override
    public List<ProejctTaskForDesignerDTO> getMyProjectTask(Map<String, Object> map) {
        return this.sqlSession.selectList("GetProjectTaskMapper.getMyProjectTask",map) ;
    }

    @Override
    public String getParentTaskCompanyId(Map<String,Object> map)  {
        return this.sqlSession.selectOne("GetProjectTaskMapper.getParentTaskCompanyId",map) ;
    }

    /**
     * 方法描述：获取某项目内某承包方的任务
     * 作者：张成亮
     * 日期：2017/4/11
     */
    @Override
    public List<String> getProjectTaskOfToCompany(String projectId, String companyId) {
        Map<String,Object> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("companyId",companyId);
        return this.sqlSession.selectList("GetProjectTaskMapper.getProjectTaskOfToCompany",map) ;
    }

    /**
     * 方法描述：根据taskPid,taskName获取（用于重名处理）
     * 作者：MaoSF
     * 日期：2017/4/21
     */
    @Override
    public ProjectTaskEntity getProjectTaskByPidAndTaskName(String projectId, String taskPid, String taskName) {
        Map<String,Object> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("taskPid",taskPid);
        map.put("taskName",taskName);
        List<ProjectTaskEntity> taskList = this.sqlSession.selectList("ProjectTaskEntityMapper.getProjectTaskByPidAndTaskName",map) ;
        if(!CollectionUtils.isEmpty(taskList)){
            return taskList.get(0);
        }
        return null;
    }

    @Override
    public ProjectTaskEntity getAllTaskNameByToCompanyId(Map<String, Object> map) {
        return this.sqlSession.selectOne("ProjectTaskEntityMapper.getAllTaskNameByToCompanyId",map) ;
    }

    /**
     * 方法描述：经营板块的数据
     * 作者：MaoSF
     * 日期：2017/5/15
     *
     * @param dto {companyId ,projectId}
     */
    @Override
    public List<ProjectIssueTaskDTO> getOperatorTaskList(QueryProjectTaskDTO dto) {
       // return this.sqlSession.selectList("GetProjectTaskForOperatorMapper.getOperatorTaskList",dto) ;
        return this.sqlSession.selectList("GetProjectIssueTaskMapper.getOperatorTaskList",dto) ;
    }

    /**
     * 方法描述：根据ID列表获取任务数据
     * 作者：ZCL
     * 日期：2017/5/16
     *
     * @param idList 任务的id列表
     */
    @Override
    public List<ProjectTaskEntity> getTaskByIdList(List<String> idList) {
        Map<String,Object> map = new HashMap<>();
        map.put("idList",idList);
        return sqlSession.selectList("ProjectTaskEntityMapper.getTaskByIdList",map);
    }

    /**
     * 方法描述：根据taskPid获取任务ID及未发布的信息记录
     * 作者：MaoSF
     * 日期：2017/5/16
     */
    @Override
    public List<ProjectIssueTaskDTO> getTaskByTaskPidForChangeProcessTime(String taskPid) {
       return sqlSession.selectList("GetProjectTaskForOperatorMapper.getTaskByTaskPidForChangeProcessTime",taskPid);
    }

    @Override
    public List<ProjectProductTaskDTO> getProductTaskOverview(QueryProjectTaskDTO dto) {
        return this.sqlSession.selectList("GetProjectProductTaskOverviewMapper.getProductTaskOverview",dto) ;
    }
    /**
     * 新增获取成产安排列表
     * */
    @Override
    public List<ProjectProducttaskViewDTO> getProductTaskOverviewNew(QueryProjectTaskDTO dto) {
        return this.sqlSession.selectList("GetProjectProductTaskOverviewMapper.getProductTaskOverviewNew",dto);
    }

    @Override
    public List<ProjectTaskDTO> getMyProjectProductTask(Map<String,Object> map) {
        return this.sqlSession.selectList("GetProjectTaskMapper.getMyProjectProductTask",map) ;
    }

    @Override
    public List<ProjectDesignTaskShow> getProductTaskList(QueryProjectTaskDTO dto) {
        return this.sqlSession.selectList("GetProjectIssueTaskMapper.getProductTaskList",dto) ;
    }

    /**
     * 方法描述：查找签发任务数据
     * 作者：ZCL
     * 日期：2017/5/17
     *
     */
    @Override
    public List<ProjectIssueTaskOverviewDTO> getProjectTaskIssueOverview(String projectId) {
        long t1 = System.currentTimeMillis(),t2=t1;
        List<ProjectIssueTaskOverviewDTO> list = sqlSession.selectList
                ("GetProjectIssueTaskOverviewMapper.getProjectTaskIssueOverview2",projectId);
        for (ProjectIssueTaskOverviewDTO phase:list){
            for (ProjectIssueTaskOverviewDTO design:phase.getChildrenList()){
                design.setStateHtml(getStateText(design.getTaskState(),design.getPlanStartTime(),
                        design.getPlanEndTime(),design.getLastDate()));
                for (ProjectIssueTaskOverviewDTO task:design.getChildrenList()){
                    task.setStateHtml(getStateText(task.getTaskState(),task.getPlanStartTime(),
                            task.getPlanEndTime(),task.getLastDate()));
                }
            }
        }
        t2 = System.currentTimeMillis();System.out.println("查询时间：" + (t2-t1) + "ms");t1 = t2;
        return list;
    }

    /**
     * 方法描述：查找签发任务数据(版本2.0）
     * 作者：ZCL
     * 日期：2017/8/28
     */
    @Override
    public List<ProjectIssueTaskOverviewDTO> listProjectTaskIssueOverview(String projectId,String companyId) {
        Map<String,Object> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("companyId",companyId);
        long t1 = System.currentTimeMillis(),t2=t1;
        List<ProjectIssueTaskOverviewDTO> list = sqlSession.selectList
                ("GetProjectIssueTaskOverviewMapper.listProjectTaskIssueOverview",map);
        t2 = System.currentTimeMillis();System.out.println("listProjectTaskIssueOverview查询时间：" + (t2-t1) + "ms");t1 = t2;
        return list;
    }

    /**
     * 方法描述：根据参数查询
     * 作者：MaoSF
     * 日期：2017/5/16
     */
    @Override
    public List<ProjectTaskEntity> selectByParam(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectTaskEntityMapper.selectByParam",map);
    }

    /**
     * 作用：复位已完成状态
     * 作者：ZCL
     * 日期：2017-5-20
     */
    @Override
    public void resetTaskCompleteStatus(String taskId) throws Exception {
        sqlSession.selectList("ProjectTaskEntityMapper.resetTaskCompleteStatus",taskId);
    }
    @Override
    public void resetProcessNodeCompleteStatus(String processNodeId) throws Exception {
        sqlSession.selectList("ProjectTaskEntityMapper.resetProcessNodeCompleteStatus",processNodeId);
    }

    /**
     * 作用：获取任务状态
     * 作者：ZCL
     * 日期：2017-5-24
     *
     * @param taskId 任务编号
     */
    @Override
    public Integer getTaskState(String taskId) {
        Integer taskState = sqlSession.selectOne("ProjectTaskEntityMapper.getTaskState",taskId);
        return taskState==null?0:taskState;
    }

    public Map<String,Object> getTaskStateMap(Map<String,Object> map){
        return  sqlSession.selectOne("GetTaskStateMapper.getTaskState3",map);
    }

    @Override
    public String getStateText(Integer taskState,Date startTime,Date endTime,Date completeDate){
        return getStateText(taskState,DateUtils.date2Str(startTime,DateUtils.date_sdf),
                DateUtils.date2Str(endTime,DateUtils.date_sdf),DateUtils.date2Str(completeDate,DateUtils.date_sdf));
    }

    @Override
    public String getStateText(Integer taskState,String startTime,String endTime,String completeDate){
        if ((taskState==null)) {
            return null;
        }
        String stateStr = "--";
        switch (taskState) {
            case 1:
                if(!StringUtil.isNullOrEmpty(endTime)){
                    stateStr = "剩余" + (DateUtils.daysOfTwo(endTime,DateUtils.date2Str(DateUtils.date_sdf))+1) + "天";
                }else {
                  //  stateStr = "进行中";
                    if(!StringUtil.isNullOrEmpty(startTime)){
                        stateStr = "用时" + (DateUtils.daysOfTwo(DateUtils.date2Str(DateUtils.date_sdf),startTime)) + "天";
                    }
                }
                break;
            case 2:
                stateStr = "超时" + (DateUtils.daysOfTwo(DateUtils.date2Str(DateUtils.date_sdf),endTime)) + "天";
                break;
            case 3:
                if ((completeDate!=null) && (startTime!=null)){
                    int days = DateUtils.daysOfTwo(completeDate,startTime);
                    if(days<0){
                        stateStr="提前完成";
                    }else {
                        stateStr = "用时" + (days+1) + "天完成";
                    }
                } else {
                    stateStr = "已完成";
                }
                break;
            case 4:
                if (completeDate!=null){
                    stateStr = "超时" + (DateUtils.daysOfTwo(completeDate,endTime)) + "天完成";
                }else {
                    stateStr = "超时完成";
                }
                break;
            case 5:
              //  stateStr = "未开始";
                stateStr = "--";
                break;
            case 6:
                stateStr = "剩余" + (DateUtils.daysOfTwo(endTime,DateUtils.date2Str(DateUtils.date_sdf))+1) + "天";
                //stateStr = "进行中";
                break;
            case 7:
                stateStr = "--";
               // stateStr = "未发布";
                break;
            default:
                stateStr = "--";
        }
        return stateStr;
    }

    /**
     * 方法描述：获取最大的seq
     * 作者：MaoSF
     * 日期：2017/6/9
     */
    @Override
    public int getProjectTaskMaxSeq(String projectId, String taskPid) {
        Map<String,Object> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("taskPid",taskPid);
        return this.sqlSession.selectOne("ProjectTaskEntityMapper.getProjectTaskMaxSeq",map);
    }

    /**
     * 方法描述：查找未完成的子任务
     * 作者：ZhangChengliang
     * 日期：2017/6/22
     *
     * @param taskPid 任务ID
     */
    @Override
    public List<ProjectTaskEntity> listUnCompletedTask(String taskPid) {
        return sqlSession.selectList("ProjectTaskEntityMapper.listUnCompletedTask",taskPid);
    }

    @Override
    public List<ProjectTaskEntity> listUnCompletedTaskByCompany(String projectId,String fromCompanyId,String toCompanyId) {
        Map<String,Object> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("fromCompanyId",fromCompanyId);
        map.put("toCompanyId",toCompanyId);
        return sqlSession.selectList("ProjectTaskEntityMapper.listUnCompletedTaskByCompany",map);
    }

    @Override
    public List<ProjectTaskEntity> getTaskByBeModifyId(String beModifyId, String fromCompanyId) {
        Map<String,Object> map = new HashMap<>();
        map.put("fromCompanyId",fromCompanyId);
        map.put("beModifyId",beModifyId);
        return sqlSession.selectList("ProjectTaskEntityMapper.getTaskByBeModifyId",map);
    }

    /**
     * 方法描述：发布当前记录后，修改该任务下的子集的taskPid，taskPath(重新设置为正式记录的taskPid)
     * param：publishId:被发布记录的ID，taskPid：新的taskPid,新的taskPath=parentPath+id
     * 作者：MaoSF
     * 日期：2017/6/23
     */
    @Override
    public int updateModifyTaskPid(String publishId, String taskPid, String parentPath) {
        Map<String,Object> map = new HashMap<>();
        map.put("publishId",publishId);
        map.put("taskPid",taskPid);
        map.put("parentPath",parentPath);
        return this.sqlSession.update("ProjectTaskEntityMapper.updateModifyTaskPid",map);
    }

    /**
     * 方法描述：获取设计内容（立项方（type=1）：设计阶段，合作方（type=2 and companyId），签发的数据）
     * 作者：MaoSF
     * 日期：2017/6/28
     */
    @Override
    public String getIssueTaskName(String projectId, String companyId, int type,String fromCompanyId) {
        Map<String,Object> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("companyId",companyId);
        map.put("fromCompanyId",fromCompanyId);
        map.put("type",type);
        return this.sqlSession.selectOne("GetProjectTaskMapper.getIssueTaskName",map);
    }

    /**
     * 方法描述：获取我负责的任务
     * 作者：MaoSF
     * 日期：2017/6/30
     * @param:notComplete可以为null，其他不可以为null
     */
    @Override
    public List<ProjectTaskEntity> getMyResponsibleTask(String projectId, String companyId, String companyUserId, String notComplete) {
        Map<String,Object> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("companyId",companyId);
        map.put("companyUserId",companyUserId);
        map.put("notComplete",notComplete);
        return sqlSession.selectList("ProjectTaskEntityMapper.getMyResponsibleTask",map);
    }

    /**
     * 方法描述：获取任务下的所有生产子任务个数
     * 作者：ZhangChengliang
     * 日期：2017/6/30
     *
     * @param taskId 任务ID
     */
    @Override
    public Integer getSubProductTaskCountByTaskId(String taskId) {
        return dao.getSubProductTaskCountByTaskId(taskId);
    }

    @Override
    public void changeCompany(TaskChangeCompanyDTO dto) {
        sqlSession.update("ProjectTaskEntityMapper.changeCompany",dto);
        sqlSession.update("ProjectTaskEntityMapper.changeFromCompany",dto);
    }

    /**
     * @param id 生产安排的任务编号
     * @return 签发任务
     * @author 张成亮
     * @date 2018/7/17
     * @description 查找生产安排所属的签发任务
     **/
    @Override
    public List<ProjectTaskEntity> listIssueParentByTaskId(String id) {
        return sqlSession.selectList("ProjectTaskEntityMapper.listIssueParentByTaskId",id);
    }

    ProjectTaskDao dao = null; //调用xml内定义的SQL语句的对象
    @PostConstruct
    public void init(){
        dao = sqlSession.getMapper(ProjectTaskDao.class);
    }
}