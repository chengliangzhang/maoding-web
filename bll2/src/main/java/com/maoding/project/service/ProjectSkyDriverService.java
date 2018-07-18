package com.maoding.project.service;

import com.maoding.core.base.dto.BaseShowDTO;
import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.project.dto.DeliverEditDTO;
import com.maoding.project.dto.ProjectSkyDriveDTO;
import com.maoding.project.dto.ProjectSkyDriveRenameDTO;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.entity.ProjectSkyDriveEntity;
import com.maoding.task.dto.SaveProjectTaskDTO;
import com.maoding.task.entity.ProjectTaskEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectSkyDriverService
 * 类描述：项目文件磁盘
 * 作    者：MaoSF
 * 日    期：2016/12/18 16:50
 */
public interface ProjectSkyDriverService extends BaseService<ProjectSkyDriveEntity> {

    /**
     * 方法描述：保存文件夹
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param:
     * @return:
     */
    AjaxMessage saveOrUpdateFileMaster(ProjectSkyDriveDTO dto) throws Exception;


    /**
     * 方法描述：删除文件或文件夹（单个删除、若文件夹下有其他的文件夹或文件，不可删除）
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param:
     * @return:
     */
    AjaxMessage deleteSysDrive(String id, String accountId) throws Exception;

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
     * 方法描述：查询文件
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param map
     * @param:
     * @return:
     */
    AjaxMessage getSkyDriveByParam(Map<String, Object> map) throws Exception;

    AjaxMessage getSkyDriveByParamList(Map<String, Object> map) throws Exception;

    /**
     * 方法描述：获取我们的项目-项目文档
     * 作   者：DongLiu
     * 日   期：2018/1/11 18:23
     *
     * @param
     * @return
     */
    AjaxMessage getMyProjectSkyDriveByParam(Map<String, Object> map) throws Exception;


    /**
     * 方法描述：查询文件
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param map
     * @param:
     * @return:
     */
    List<ProjectSkyDriveEntity> getNetFileByParam(Map<String, Object> map) throws Exception;

    void initProjectFile();


    /**
     * 方法描述：创建项目的默认文件夹
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param projectEntity
     * @param:
     * @return:
     */
    void createProjectFile(ProjectEntity projectEntity);


    /**
     * 方法描述：文件信息保存
     * 作   者：LY
     * 日   期：2016/8/11 19:37
     */
    AjaxMessage saveFileMessage(ProjectSkyDriveDTO projectSkyDriveDTO) throws Exception;


    /**
     * 重命名
     */
    AjaxMessage rename(ProjectSkyDriveRenameDTO dto);

    /**
     * 方法描述：签发的时候，给该公司创建默认的文件（设计成果中的文件）
     * 作者：MaoSF
     * 日期：2017/4/12
     *
     * @param:
     * @return:
     */
    AjaxMessage createFileMasterForIssueTask(String projectId, String companyId);

    /**
     * 方法描述：签发的时候，给该公司创建默认的文件（设计成果中的文件）
     * 作者：MaoSF
     * 日期：2017/4/12
     *
     * @param:
     * @return:
     */
    void createFileMasterForTask(ProjectTaskEntity taskEntity);

    /**
     * 签发的时候，给该公司创建默认的文件（归档文件）
     */
    void createFileMasterForArchivedFile(ProjectTaskEntity taskEntity);

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
     * 方法描述：
     * 作者：MaoSF
     * 日期：2017/4/21
     *
     * @param:
     * @return:
     */
    AjaxMessage updateSkyDriveStatus(String taskId, String accountId) throws Exception;

    /**
     * 方法描述：数据迁移使用
     * 作者：MaoSF
     * 日期：2017/5/1
     *
     * @param:
     * @return:
     */
    AjaxMessage initProjectSkyDriver() throws Exception;

    /********************************公司附件**********************************/
    /**
     * 方法描述：获取组织logo地址(完整的url地址)
     * 作者：MaoSF
     * 日期：2017/6/2
     *
     * @param:
     * @return:
     */
    String getCompanyLogo(String companyId) throws Exception;

    /**
     * 方法描述：获取组织logo地址（不包含轮播图，不带fastdfsUrl）
     * 作者：MaoSF
     * 日期：2017/6/2
     *
     * @param:
     * @return:
     */
    String getCompanyFileByType(String companyId, Integer type) throws Exception;

    /**
     * 方法描述：生成公司二维码
     * 作者：MaoSF
     * 日期：2016/11/25
     *
     * @param:
     * @return:
     */
    String createCompanyQrcode(String companyId) throws Exception;

    /********************************项目合同附件***************************************/
    /**
     * 方法描述：获取项目合同附件
     * 作者：MaoSF
     * 日期：2017/6/2
     *
     * @param:
     * @return:
     */
    ProjectSkyDriveEntity getProjectContractAttach(String projectId) throws Exception;

    /**
     * 方法描述：获取项目合同附件列表
     * 作者：ZCL
     * 日期：2017/9/12
     *
     * @param:
     * @return:
     */
    List<ProjectSkyDriveEntity> listProjectContractAttach(String projectId) throws Exception;

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
     * 生产安排，给该公司创建默认的文件（归档文件）
     */
    void createFileMasterForProductionFile(ProjectTaskEntity taskEntity);

    /**
     * 发送归档通知文件夹
     */
    String createSendarchivedFileNotifier(ProjectTaskEntity taskEntity);

    /**
     * @author  张成亮
     * @date    2018/7/16
     * @description 在任务对应的文档目录下创建或更改交付文件夹
     * @param task 任务
     *        request 交付申请
     * @return 创建或更改的交付文件夹
     */
    String createDeliverDir(ProjectTaskEntity task, DeliverEditDTO request);

    /**
     * 查询归档通知下的所有文件夹和文件
     */
    List<ProjectSkyDriveEntity> getProjectFileFirst(Map<String, Object> map);

    /**
     * 查询归档通知下的文件夹所有文件夹和文件
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

    /**
     *
     */
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
     * @date    2018/7/17
     * @description      在交付文件目录下创建参与者用户目录
     * @param   request  更改任务申请
     * @param   taskList 关联任务
     * @param   designerList 用户列表
     **/

    void createDesignerDir(SaveProjectTaskDTO request, List<ProjectTaskEntity> taskList, List<BaseShowDTO> designerList);
}
