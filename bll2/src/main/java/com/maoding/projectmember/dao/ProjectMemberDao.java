package com.maoding.projectmember.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.dto.ProjectTaskProcessNodeDTO;
import com.maoding.project.dto.ProjectWorkingHoursDTO;
import com.maoding.projectmember.dto.MemberQueryDTO;
import com.maoding.projectmember.dto.ProjectMemberDTO;
import com.maoding.projectmember.entity.ProjectMemberEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp22 on 2017/6/6.
 */
public interface ProjectMemberDao extends BaseDao<ProjectMemberEntity> {

    /**
     * 方法描述：根据参数查询
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    List<ProjectMemberEntity> listProjectMember(MemberQueryDTO projectMember);

    /**
     * 方法描述：根据参数查询
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    List<ProjectMemberEntity> listParentDesigner(Map<String, Object> map);

    /**
     * 方法描述：根据参数查询(包含个人信息)
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    List<ProjectMemberDTO> listProjectMemberByParam(MemberQueryDTO projectMember);

    /**
     * 方法描述：获取任务节点下所有的人员（用于任务copy）
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    List<ProjectMemberEntity> listProjectMemberByTaskId(String taskId);

    /**
     * 方法描述：获取子级的任务负责人
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    List<ProjectMemberEntity> listDesignerMemberByTaskPid(String taskId);


    /**
     * 方法描述：获取设计人员
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    List<ProjectTaskProcessNodeDTO> listDesignMember(String nodeId);

    /**
     * 方法描述：更新状态（status:1，职责所承担的任务已完成，0，未完成）
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    int updateProjectMemberStatus(ProjectMemberEntity projectMember);

    /**
     * 方法描述：逻辑删除
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    int deleteProjectMember(ProjectMemberEntity projectMember);

    /**
     * 方法描述：获取设计人员
     * 作者：MaoSF
     * 日期：2017/6/24
     */
    String getDesignUserByTaskId(String taskId);

    /**
     * 根据查询条件获取项目成员实体
     */
    ProjectMemberEntity getProjectMember(MemberQueryDTO query);

    /**
     * 获取项目成员列表
     */
    List<ProjectMemberDTO> listProjectAllMember(MemberQueryDTO query);

    /**
     * 获取工时
     */
    List<ProjectMemberDTO> getProjectWorkingHours(ProjectWorkingHoursDTO hoursDTO) throws Exception;

    /**
     * 获取工时总页数
     */
    Integer getProjectWorkingHoursCount(ProjectWorkingHoursDTO hoursDTO) throws Exception;
    /**
     * 获取工时总数
     */
    String getProjectWorkingHoursSum(ProjectWorkingHoursDTO hoursDTO) throws Exception;

    List<String> getProjectMemberByUserIdAndTyep(Map<String, Object> map);
}
