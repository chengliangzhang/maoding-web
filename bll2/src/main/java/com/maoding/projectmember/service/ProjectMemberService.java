package com.maoding.projectmember.service;

import com.maoding.project.dto.ProjectDesignUserList;
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

public interface ProjectMemberService {

    ProjectMemberEntity getProjectMemberById(String id);

    /**
     * 方法描述：保存项目成员(不推送消息，不发送任务)
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    ProjectMemberEntity insertProjectMember(String projectId, String companyId, String companyUserId, String accountId, Integer memberType, String targetId, String nodeId, Integer seq, String createBy) throws Exception;

    //  ProjectMemberEntity saveProjectMember(SaveProjectMemberDTO dto) throws Exception;

    /**
     * 方法描述：保存项目成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    ProjectMemberEntity saveProjectMember(String projectId, String companyId, String companyUserId, String accountId, Integer memberType, String targetId, String nodeId, Integer seq, String createBy, boolean isSendMessage, String currentCompanyId) throws Exception;

    /**
     * 方法描述：保存项目成员（未发布版本的数据：status=2）
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    ProjectMemberEntity saveProjectMember(String projectId, String companyId, String companyUserId, String targetId, Integer memberType, Integer seq, String createBy, String currentCompanyId) throws Exception;

    /**
     * 方法描述：保存项目成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    ProjectMemberEntity saveProjectMember(String projectId, String companyId, String companyUserId, String accountId, Integer memberType, String targetId, String createBy, boolean isSendMessage, String currentCompanyId) throws Exception;

    /**
     * 方法描述：保存项目成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    ProjectMemberEntity saveProjectMember(String projectId, String companyId, String companyUserId, String accountId, Integer memberType, String createBy, boolean isSendMessage, String currentCompanyId) throws Exception;

    /**
     * 方法描述：保存项目成员(只推送消息，不发送任务)
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    ProjectMemberEntity saveProjectMember(String projectId, String companyId, String companyUserId, Integer memberType, String createBy, boolean isSendMessage, String currentCompanyId) throws Exception;


    /**
     * 方法描述：更新成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    void updateProjectMember(ProjectMemberEntity projectMember, String companyUserId, String accountId, String updateBy,String currentCompanyId, boolean isSendMessage) throws Exception;

    /**
     * 方法描述：更新成员状态
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    void updateProjectMember(String projectId, String companyId, String companyUserId, Integer memberType, String targetId) throws Exception;

    void updateProjectMember(ProjectMemberEntity projectMember);

    /**
     * 方法描述：删除成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    void deleteProjectMember(String projectId, String companyId, Integer memberType, String targetId) throws Exception;

    /**
     * 方法描述：删除成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    void deleteProjectMember(Integer memberType, String targetId) throws Exception;

    /**
     * 方法描述：删除成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    void deleteProjectMember(String id) throws Exception;

    /**
     * 方法描述：获取成员信息
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    List<ProjectMemberEntity> listProjectMember(String projectId, String companyId, Integer memberType, String targetId) ;


    /**
     * 方法描述：是否是父级所有任务负责人
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    boolean isParentDesigner(String projectId,String taskPath, String companyId, String companyUserId) ;

    /**
     * 方法描述：获取成员信息
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    ProjectMemberEntity getProjectMember(String projectId, String companyId, Integer memberType, String targetId) ;

    /**
     * 方法描述：获取成员信息(社校审)
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    ProjectMemberEntity getProjectMember(String companyUserId, Integer memberType, String targetId);

    /**
     * 设计负责人+设计助理
     */
    List<ProjectMemberEntity> listDesignManagerAndAssist(String projectId, String companyId);


    /**
     * 方法描述：获取成员信息(带有个人资料信息)
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    List<ProjectMemberDTO> listProjectMemberByParam(String projectId, String companyId, Integer memberType, String targetId);

    List<ProjectMemberDTO> listProjectMemberByParam(String projectId, String companyId);

    /**
     * 方法描述：获取成员信息(带有个人资料信息)
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    ProjectMemberDTO getProjectMemberByParam(String projectId, String companyId, Integer memberType, String targetId);

    /**
     * 方法描述：项目成员
     * 作者：MaoSF
     * 日期：2017/6/9
     */
    List<ProjectMemberDTO> listProjectMember(String projectId, String currentCompanyId, String accountId);

    /**
     * 方法描述：经营负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    ProjectMemberEntity getOperatorManager(String projectId, String companyId) ;

    /**
     * 方法描述：经营助理
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    ProjectMemberEntity getOperatorAssistant(String projectId, String companyId) ;

    /**
     * 方法描述：设计负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    ProjectMemberEntity getDesignManager(String projectId, String companyId) ;

    /**
     * 方法描述：设计助理
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    ProjectMemberEntity getDesignManagerAssistant(String projectId, String companyId);

    /**
     * 方法描述：任务负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    ProjectMemberEntity getTaskDesigner(String taskId) ;

    /**
     * 方法描述：经营负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    ProjectMemberDTO getOperatorManagerDTO(String projectId, String companyId) ;

    /**
     * 方法描述：设计负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    ProjectMemberDTO getDesignManagerDTO(String projectId, String companyId) ;

    /**
     * 方法描述：任务负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    ProjectMemberDTO getTaskDesignerDTO(String taskId) ;

    /**
     * 方法描述：立项人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    ProjectMemberDTO getProjectCreatorDTO(String projectId) ;

    /**
     * 方法描述：经营负责人+设计负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    List<ProjectMemberDTO> listProjectManager(String projectId, String companyId) ;

    /**
     * 方法描述：copy 任务下所有的人员
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    void copyProjectMember(String taskId, String newTaskId) ;

    /**
     * 方法描述：发布任务，处理当前任务下所有的人员
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    void publishProjectMember(String projectId, String companyId, String taskId, String modifyTaskId, String accountId) throws Exception;

    /**
     * 方法描述：获取设计人员
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    List<ProjectTaskProcessNodeDTO> listDesignMember(String taskId);

    ProjectDesignUserList listDesignMemberList(String taskId);

    /**
     * 方法描述：获取设计人员
     * 作者：MaoSF
     * 日期：2017/6/24
     */
    String getDesignUserByTaskId(String taskId);

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

    /**
     * 删除助理
     */
    void deleteAssistMember(ProjectMemberEntity entity,String accountId) throws Exception;
    /**
     * 判断查询条件是否包含经营负责人、经营负责人助理、设计负责人、设计负责人助理
     * */
    List<String> getProjectMemberByUserIdAndTyep(Map<String, Object> map);

    /**
     * @author  张成亮
     * @date    2018/7/19
     * @description     查询项目成员
     * @param   query 查询条件
     * @return  项目成员列表
     **/
    List<ProjectMemberDTO> listByQuery(MemberQueryDTO query);
}
