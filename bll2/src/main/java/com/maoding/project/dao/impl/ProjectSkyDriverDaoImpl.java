package com.maoding.project.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dao.ProjectSkyDriverDao;
import com.maoding.project.dto.NetFileDTO;
import com.maoding.project.dto.ProjectSkyDriveRenameDTO;
import com.maoding.project.dto.ProjectSkyDriverQueryDTO;
import com.maoding.project.dto.SkyDriveUpdateDTO;
import com.maoding.project.entity.ProjectSkyDriveEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectSkyDriverDaoImpl
 * 类描述：项目文件磁盘
 * 作    者：MaoSF
 * 日    期：2016/7/22 16:50
 */
@Service("projectSkyDriverDao")
public class ProjectSkyDriverDaoImpl extends GenericDao<ProjectSkyDriveEntity> implements ProjectSkyDriverDao {
    /**
     * @param request 更改申请
     * @description
     * @author 张成亮
     * @date 2018/7/2 20:43
     **/
    @Override
    public void updateSkyDrive(SkyDriveUpdateDTO request) {
        sqlSession.update("ProjectSkyDriveEntityMapper.updateSkyDrive", request);
    }


    @Override
    public ProjectSkyDriveEntity getSkyDriveByPidAndFileName(String pid, String fileName, String projectId, String companyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pid", pid);
        map.put("fileName", fileName);
        map.put("projectId", projectId);
        map.put("companyId", companyId);
        List<ProjectSkyDriveEntity> list = sqlSession.selectList("ProjectSkyDriveEntityMapper.getSkyDriveByPidAndFileName", map);
        return (!ObjectUtils.isEmpty(list)) ? list.get(0) : null;
    }

    @Override
    public ProjectSkyDriveEntity getSkyDriveByFileAndProIdAndComId(Map<String, Object> map) {
        return this.sqlSession.selectOne("ProjectSkyDriveEntityMapper.getSkyDriveByFileAndProIdAndComId", map);
    }

    /**
     * 方法描述：根据项目id，父级id，文件名查询
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param taskId
     * @param:
     * @return:
     */
    @Override
    public ProjectSkyDriveEntity getSkyDriveByTaskId(String taskId) {
        return this.sqlSession.selectOne("ProjectSkyDriveEntityMapper.getSkyDriveByTaskId", taskId);
    }

    /**
     * 方法描述：查询文件id为pid的所有文件及文件夹
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param pid
     * @param projectId
     * @param:
     * @return:
     */
    @Override
    public List<ProjectSkyDriveEntity> getSkyDriveByPid(String pid, String projectId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pid", pid);
        map.put("projectId", projectId);
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getSkyDriveByPid", map);
    }

    /**
     * 方法描述：查询文件
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param map
     * @param:
     * @return:
     */
    @Override
    public List<ProjectSkyDriveEntity> getSkyDriveByParam(Map<String, Object> map) {

        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getSkyDriveByParam", map);
    }
    @Override
    public List<ProjectSkyDriveEntity> getSkyDriveByParamList(Map<String, Object> map) {

        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getSkyDriveByParamList", map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getSkyDriveByParamPid(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getSkyDriveByParamPid", map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getSkyDriveByParamId(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getSkyDriveByParamId", map);
    }

    @Override
    public int rename(ProjectSkyDriveRenameDTO dto) {
        return this.sqlSession.update("ProjectSkyDriveEntityMapper.renameById", dto);
    }

    /**
     * 方法描述：查询公司是否存在“设计成果”中的文件
     * 作者：MaoSF
     * 日期：2017/4/12
     *
     * @param projectId
     * @param companyId
     * @param:
     * @return:
     */
    @Override
    public List<ProjectSkyDriveEntity> getProjectSkyByCompanyId(String projectId, String companyId) {
        Map<String, Object> map = new HashMap<>();
        map.put("projectId", projectId);
        map.put("companyId", companyId);
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getProjectSkyByCompanyId", map);
    }

    /**
     * 方法描述：更改状态（用于删除）
     * 作者：MaoSF
     * 日期：2017/4/21
     *
     * @param map
     * @param:
     * @return:
     */
    @Override
    public int updateSkyDriveStatus(Map<String, Object> map) {
        return this.sqlSession.update("ProjectSkyDriveEntityMapper.updateSkyDriveStatus", map);
    }

    /**
     * 方法描述：获取远程文件
     * 作者：MaoSF
     * 日期：2017/6/1
     *
     * @param map
     * @param:
     * @return:
     */
    @Override
    public List<NetFileDTO> getNetFileByParam(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getNetFileByParam", map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getProjectSkyDriveEntityById(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getProjectSkyDriveEntityById", map);
    }

    @Override
    public ProjectSkyDriveEntity getSkyDriveByProjectIdAndFileNameAndTaskId(Map<String, Object> map) {
        return this.sqlSession.selectOne("ProjectSkyDriveEntityMapper.getSkyDriveByProjectIdAndFileNameAndTaskId", map);
    }

    @Override
    public int updateSkyDriveForStatus(Map<String, Object> map) {
        return this.sqlSession.update("ProjectSkyDriveEntityMapper.updateSkyDriveForStatus", map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getProjectSkyDriveEntityByProjectIdAndFileName(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getProjectSkyDriveEntityByProjectIdAndFileName", map);
    }

    /**
     * 方法描述：生产安排创建文件夹
     * 作   者：DongLiu
     * 日   期：2018/1/9 18:05
     *
     * @param
     * @return
     */
    @Override
    public ProjectSkyDriveEntity getProjectSkyDriveEntityByProductionFile(Map<String, Object> map) {
        return this.sqlSession.selectOne("ProjectSkyDriveEntityMapper.getProjectSkyDriveEntityByProductionFile", map);
    }

    @Override
    public Integer updateSkyDriveForFileName(Map<String, Object> map) {
        return this.sqlSession.update("ProjectSkyDriveEntityMapper.updateSkyDriveForFileName", map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getProjectSkyDriveEntityByProjectIdAndCompanyId(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getProjectSkyDriveEntityByProjectIdAndCompanyId", map);
    }

    @Override
    public int deleteSysDriveByIds(List<String> ids) throws Exception {
        return this.sqlSession.delete("ProjectSkyDriveEntityMapper.deleteSysDriveByIds", ids);
    }

    @Override
    public List<ProjectSkyDriveEntity> getProjectFileFirst(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getProjectFileFirst", map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getProjectFileSecond(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getProjectFileSecond", map);
    }

    @Override
    public ProjectSkyDriveEntity getResultsFolder(Map<String, Object> map) {
        return this.sqlSession.selectOne("ProjectSkyDriveEntityMapper.getResultsFolder", map);
    }

    @Override
    public ProjectSkyDriveEntity getOwnerProject(Map<String, Object> map) {
        return this.sqlSession.selectOne("ProjectSkyDriveEntityMapper.getOwnerProject", map);
    }

    @Override
    public ProjectSkyDriveEntity getSendResults(Map<String, Object> map) {
        return this.sqlSession.selectOne("ProjectSkyDriveEntityMapper.getSendResults", map);
    }

    @Override
    public ProjectSkyDriveEntity getOwnerProjectFile(Map<String, Object> map) {
        return this.sqlSession.selectOne("ProjectSkyDriveEntityMapper.getOwnerProjectFile", map);
    }

    @Override
    public ProjectSkyDriveEntity getOwnerProjectFileByPid(Map<String, Object> map) {
        return this.sqlSession.selectOne("ProjectSkyDriveEntityMapper.getOwnerProjectFileByPid", map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getOwnerProjectFileFirst(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getOwnerProjectFileFirst", map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getDirectoryDTOList(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getDirectoryDTOList", map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getProjectFileByFileName(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectSkyDriveEntityMapper.getProjectFileByFileName", map);
    }

    @Override
    public Integer getProjectFileTotil(Map<String, Object> map) {
        return this.sqlSession.selectOne("ProjectSkyDriveEntityMapper.getProjectFileTotil", map);
    }

    /**
     * @param query 查询条件
     * @return 文件列表
     * @author 张成亮
     * @date 2018/7/19
     * @description 通用查询文件方法
     **/
    @Override
    public List<ProjectSkyDriveEntity> listEntityByQuery(ProjectSkyDriverQueryDTO query) {
        return sqlSession.selectList("ProjectSkyDriveEntityMapper.listEntityByQuery", query);
    }
}
