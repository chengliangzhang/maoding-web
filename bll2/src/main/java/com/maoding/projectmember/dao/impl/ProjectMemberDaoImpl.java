package com.maoding.projectmember.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dto.ProjectTaskProcessNodeDTO;
import com.maoding.project.dto.ProjectWorkingHoursDTO;
import com.maoding.projectmember.dao.ProjectMemberDao;
import com.maoding.projectmember.dto.MemberQueryDTO;
import com.maoding.projectmember.dto.ProjectMemberDTO;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostDao
 * 类描述：项目费用Dao
 * 作    者：MaoSF
 * 日    期：2015年8月10日-下午4:28:32
 */
@Service("projectMemberDao")
public class ProjectMemberDaoImpl extends GenericDao<ProjectMemberEntity> implements ProjectMemberDao {


    /**
     * 方法描述：根据参数查询
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public List<ProjectMemberEntity> listProjectMember(MemberQueryDTO projectMember) {
        return this.sqlSession.selectList("ProjectMemberEntityMapper.listProjectMember", projectMember);
    }

    @Override
    public List<ProjectMemberEntity> listParentDesigner(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectMemberEntityMapper.listParentDesigner", map);
    }

    @Override
    public List<ProjectMemberDTO> listProjectMemberByParam(MemberQueryDTO projectMember) {
        return this.sqlSession.selectList("GetProjectMemberMapper.listProjectMemberByParam", projectMember);
    }

    /**
     * 方法描述：获取任务节点下所有的人员（用于任务copy）
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    @Override
    public List<ProjectMemberEntity> listProjectMemberByTaskId(String taskId) {
        return this.sqlSession.selectList("ProjectMemberEntityMapper.listProjectMemberByTaskId", taskId);
    }

    @Override
    public List<ProjectMemberEntity> listDesignerMemberByTaskPid(String taskId) {
        return this.sqlSession.selectList("ProjectMemberEntityMapper.listDesignerMemberByTaskPid", taskId);
    }


    /**
     * 方法描述：获取设计人员
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    @Override
    public List<ProjectTaskProcessNodeDTO> listDesignMember(String nodeId) {
        return this.sqlSession.selectList("GetProjectMemberMapper.listDesignMember", nodeId);
    }

    /**
     * 方法描述：更新状态（status:1，职责所承担的任务已完成，0，未完成）
     * 作者：MaoSF
     * 日期：2017/6/7
     *
     * @param projectMember
     */
    @Override
    public int updateProjectMemberStatus(ProjectMemberEntity projectMember) {
        return this.sqlSession.update("ProjectMemberEntityMapper.updateProjectMemberStatus", projectMember);
    }

    /**
     * 方法描述：逻辑删除
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    @Override
    public int deleteProjectMember(ProjectMemberEntity projectMember) {
        return this.sqlSession.update("ProjectMemberEntityMapper.deleteProjectMember", projectMember);
    }

    /**
     * 方法描述：获取设计人员
     * 作者：MaoSF
     * 日期：2017/6/24
     */
    @Override
    public String getDesignUserByTaskId(String taskId) {
        return this.sqlSession.selectOne("GetProjectMemberMapper.getDesignUser", taskId);
    }

    /**
     * 根据查询条件获取项目成员实体
     *
     * @param query
     */
    @Override
    public ProjectMemberEntity getProjectMember(MemberQueryDTO query) {
        return this.sqlSession.selectOne("ProjectMemberMapper.getProjectMember", query);
    }

    @Override
    public List<ProjectMemberDTO> listProjectAllMember(MemberQueryDTO query) {
        return this.sqlSession.selectList("GetProjectMemberMapper.listProjectAllMember", query);
    }

    /**
     * 获取工时
     */
    @Override
    public List<ProjectMemberDTO> getProjectWorkingHours(ProjectWorkingHoursDTO hoursDTO) throws Exception {
        return this.sqlSession.selectList("GetProjectMemberMapper.getProjectWorkingHours", hoursDTO);
    }

    @Override
    public Integer getProjectWorkingHoursCount(ProjectWorkingHoursDTO hoursDTO) throws Exception {
        return this.sqlSession.selectOne("GetProjectMemberMapper.getProjectWorkingHoursCount", hoursDTO);
    }

    @Override
    public String getProjectWorkingHoursSum(ProjectWorkingHoursDTO hoursDTO) throws Exception {
        return this.sqlSession.selectOne("GetProjectMemberMapper.getProjectWorkingHoursSum", hoursDTO);
    }

    @Override
    public List<String> getProjectMemberByUserIdAndTyep(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectMemberMapper.getProjectMemberByUserIdAndTyep", map);
    }

    /**
     * @param query 查询条件
     * @return 项目成员列表
     * @author 张成亮
     * @date 2018/7/19
     * @description 通用查找项目成员
     **/
    @Override
    public List<ProjectMemberEntity> listEntityByQuery(MemberQueryDTO query) {
        return sqlSession.selectList("ProjectMemberEntityMapper.listEntityByQuery",query);
    }
}
