package com.maoding.project.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.dto.NetFileDTO;
import com.maoding.project.dto.ProjectSkyDriveRenameDTO;
import com.maoding.project.dto.ProjectSkyDriverQueryDTO;
import com.maoding.project.dto.SkyDriveUpdateDTO;
import com.maoding.project.entity.ProjectSkyDriveEntity;

import java.util.List;
import java.util.Map;


/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ProjectAttachDao
 * 描    述 : 项目附件（dao）
 * 作    者 : LY
 * 日    期 : 2016/7/22 16:50
 */
public interface ProjectSkyDriverDao extends BaseDao<ProjectSkyDriveEntity> {

    /**
     * @description
     * @author  张成亮
     * @date    2018/7/2 20:43
     * @param   request 更改申请
     **/
    void updateSkyDrive(SkyDriveUpdateDTO request);

    /**
     * 方法描述：根据项目id，父级id，文件名查询
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param:
     * @return:
     */
    ProjectSkyDriveEntity getSkyDriveByPidAndFileName(String pid, String fileName, String projectId, String companyId);

    ProjectSkyDriveEntity getSkyDriveByFileAndProIdAndComId(Map<String, Object> map);


    /**
     * 方法描述：根据项目id，父级id，文件名查询
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param:
     * @return:
     */
    ProjectSkyDriveEntity getSkyDriveByTaskId(String taskId);


    /**
     * 方法描述：查询文件id为pid的所有文件及文件夹
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param:
     * @return:
     */
    List<ProjectSkyDriveEntity> getSkyDriveByPid(String pid, String projectId);


    /**
     * 方法描述：查询文件
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param map
     * @param:
     * @return:
     */
    List<ProjectSkyDriveEntity> getSkyDriveByParam(Map<String, Object> map);

    List<ProjectSkyDriveEntity> getSkyDriveByParamList(Map<String, Object> map);

    List<ProjectSkyDriveEntity> getSkyDriveByParamPid(Map<String, Object> map);

    List<ProjectSkyDriveEntity> getSkyDriveByParamId(Map<String, Object> map);

    /**
     * 重命名
     */
    int rename(ProjectSkyDriveRenameDTO dto);

    /**
     * 方法描述：查询公司是否存在“设计成果”中的文件
     * 作者：MaoSF
     * 日期：2017/4/12
     *
     * @param:
     * @return:
     */
    List<ProjectSkyDriveEntity> getProjectSkyByCompanyId(String projectId, String companyId);

    /**
     * 方法描述：更改状态（用于删除）
     * 作者：MaoSF
     * 日期：2017/4/21
     *
     * @param:map(skyDrivePath)
     * @return:
     */
    int updateSkyDriveStatus(Map<String, Object> map);

    /**
     * 方法描述：获取远程文件
     * 作者：MaoSF
     * 日期：2017/6/1
     *
     * @param:
     * @return:
     */
    List<NetFileDTO> getNetFileByParam(Map<String, Object> map);

    /**
     * 方法描述：获取项目文档，第二层目录例如（设计依据，归档文件），根据组织id和项目id获取
     * 作   者：DongLiu
     * 日   期：2018/1/6 16:52
     *
     * @param :组织id，项目id
     * @return:
     */
    List<ProjectSkyDriveEntity> getProjectSkyDriveEntityById(Map<String, Object> map);

    /**
     * 方法描述：根据projectId，fileName，task_id查询
     * 作   者：DongLiu
     * 日   期：2018/1/9 10:05
     *
     * @param
     * @return
     */
    ProjectSkyDriveEntity getSkyDriveByProjectIdAndFileNameAndTaskId(Map<String, Object> map);

    /**
     * 方法描述：更新文件夹状态
     * 作   者：DongLiu
     * 日   期：2018/1/9 14:46
     *
     * @param
     * @return
     */
    int updateSkyDriveForStatus(Map<String, Object> map);

    /**
     * 方法描述：查询归档文件夹是否已发布
     * 作   者：DongLiu
     * 日   期：2018/1/9 15:46
     *
     * @param
     * @return
     */
    List<ProjectSkyDriveEntity> getProjectSkyDriveEntityByProjectIdAndFileName(Map<String, Object> map);

    /**
     * 方法描述：生产安排创建文件夹
     * 作   者：DongLiu
     * 日   期：2018/1/9 18:05
     *
     * @param
     * @return
     */
    ProjectSkyDriveEntity getProjectSkyDriveEntityByProductionFile(Map<String, Object> map);

    /**
     * 方法描述：更新文件夹名称
     * 作   者：DongLiu
     * 日   期：2018/1/10 14:19
     *
     * @param
     * @return
     */
    Integer updateSkyDriveForFileName(Map<String, Object> map);

    List<ProjectSkyDriveEntity> getProjectSkyDriveEntityByProjectIdAndCompanyId(Map<String, Object> map);

    /**
     * 方法描述：批量删除文件夹
     * 作   者：DongLiu
     * 日   期：2018/1/16 18:27
     *
     * @param
     * @return
     */
    int deleteSysDriveByIds(List<String> ids) throws Exception;

    /**
     * 查询归档通知下的所有文件夹和文件
     */
    List<ProjectSkyDriveEntity> getProjectFileFirst(Map<String, Object> map);

    /**
     * 查询归档通知下的所有文件夹和文件
     */
    List<ProjectSkyDriveEntity> getProjectFileSecond(Map<String, Object> map);

    /**
     * 获取对应的成果文件夹
     */
    ProjectSkyDriveEntity getResultsFolder(Map<String, Object> map);

    /**
     * 获取成果文件发给甲方
     */
    ProjectSkyDriveEntity getOwnerProject(Map<String, Object> map);

    ProjectSkyDriveEntity getSendResults(Map<String, Object> map);
    /**
     * 获取成果文件发给甲方文件夹
     */
    ProjectSkyDriveEntity getOwnerProjectFile(Map<String, Object> map);
    ProjectSkyDriveEntity getOwnerProjectFileByPid(Map<String, Object> map);

    List<ProjectSkyDriveEntity> getOwnerProjectFileFirst(Map<String, Object> map);
    /**
     * 查询文件夹顺序
     */
    List<ProjectSkyDriveEntity> getDirectoryDTOList(Map<String, Object> map);

    /**
     * 项目文档全文搜索
     * */
    List<ProjectSkyDriveEntity> getProjectFileByFileName(Map<String, Object> map);

    Integer getProjectFileTotil(Map<String, Object> map);

    /**
     * @author  张成亮
     * @date    2018/7/19
     * @description     通用查询文件方法
     * @param   query 查询条件
     * @return  文件列表
     **/
    List<ProjectSkyDriveEntity> listEntityByQuery(ProjectSkyDriverQueryDTO query);
}
