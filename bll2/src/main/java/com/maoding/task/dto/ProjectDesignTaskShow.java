package com.maoding.task.dto;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.project.dto.ProjectDesignUser;
import com.maoding.project.dto.ProjectTaskProcessNodeDTO;
import com.maoding.projectmember.dto.ProjectMemberDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp21 on 2017/2/25.
 */
public class ProjectDesignTaskShow  extends TaskBaseDTO{

    private String id;

    private String projectId;

    /**
     * 排序序号
     */
    private Integer seq;

    /**
     * 任务的父Id
     */
    private String taskPid;

    /**
     * 任务的名称
     */
    private String taskName;

    /**
     * 备注
     */
    private String taskRemark;

    /**
     * 任务类型（签发设计阶段或服务内容，默认1=设计阶段 2:经营方分解的任务，0：生产方分解的任务）
     */
    private int taskType;

    private int beModifyTaskType;//正式记录的taskType

    private String designOrg;

    private String personInCharge;

    /**
     * 任务负责人员工id（companyUserId）
     */

    private String personInChargeId;

    private String designOrgId;

    /**
     * 任务负责人id（任务负责人记录id）
     */
    private String setPersonInChargeId;

    private String departId;
    private String departName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date contractStartTime;

    private int isHasChild;

    private int issueCount;

    private String taskPath;

    /**
     * 是否和我相关的任务
     */
    private int isMyTask;

    /**
     * 是否显示约定时间
     */
    private int isShowAppointmentTime;

    /**
     * 共多少天
     */
    private int allDay;

    /**
     * 未完成的任务数
     */
    private int notCompleteCount;

    private int isRootTask;//是否是根任务（签发给其他组织的任务）

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date contractEndTime;

    private int isOperaterTask;

    /**
     * 是否存在经营任务（0：否：1：是）
     */
    private int isHasOperatorTask;

    /**
     * 生产任务的层级
     */
    private int produceTaskLevel;

    /**
     * 生产任务控制分级标识
     */
    private Integer taskLevelStatus;

    /**
     * 生产是否可分级标识
     * 状态值:=====0：可分级；1：不可分级
     */
    private Integer gradeStatus;

    /**
     * 完成情况
     */
    private String completion;

    /**
     * 任务层级，对应数据库内的task_level
     */
    private Integer taskLevel;

    private String beModifyId;
    private String beModifyTaskPid;
    private String taskStatus;

    /***任务负责人，如果是其他公司的，则是设计负责人***/
    private ProjectMemberDTO designer;

    /**
     * 设计人员列表
     */
    private List<ProjectTaskProcessNodeDTO> designersList = Lists.newArrayList();

    /**
     * 设计人
     */
    private ProjectTaskProcessNodeDTO designUser = new ProjectTaskProcessNodeDTO("设计人", ProjectMemberType.PROJECT_DESIGNER);

    private ProjectDesignUser projectDesignUser=new ProjectDesignUser();

    /**
     * 校对人
     */
    private ProjectTaskProcessNodeDTO checkUser = new ProjectTaskProcessNodeDTO("校对人", ProjectMemberType.PROJECT_PROOFREADER);
    private ProjectDesignUser projectCheckUser=new ProjectDesignUser();

    /**
     * 审核人
     */
    private ProjectTaskProcessNodeDTO examineUser = new ProjectTaskProcessNodeDTO("审核人", ProjectMemberType.PROJECT_AUDITOR);
    private ProjectDesignUser projectExamineUser=new ProjectDesignUser();

    /**
     * 设计人(列表外面的人名:如果当前人包含在设计人中，就展示当前人，如果不包含，则展示第一个人)
     */
    private String designUserName;

    private String designUserId;

    /**
     * 校对人(列表外面的人名:如果当前人包含在设计人中，就展示当前人，如果不包含，则展示第一个人)
     */
    private String checkUserName;

    private String checkUserId;

    /**
     * 审核人(列表外面的人名:如果当前人包含在设计人中，就展示当前人，如果不包含，则展示第一个人)
     */
    private String examineUserName;

    private String examineUserId;


    /**
     * 权限标示(flag1：任务签发，flag2：任务负责人，flag3：设计人员，flag4：计划进度，flag5：编辑任务，flag6：删除任务,flag7:分解设计任务)
     */
    private Map<String, String> roleFlag = new HashMap<String, String>();
    /**
     * 是否是第一个
     */
    private boolean isFirst;

    /**
     * 是否是最后一个
     */
    private boolean isLast;

    /**
     * 添加权限判断值
     * 0:可以添加，1：不可以添加
     * */
    private Integer saveExtent;

    /**
     * 判断是否有历史时间
     * */
    private Boolean changeTime;

    /**
     * 优先级
     */
    private Short priority;


    public Short getPriority() {
        return priority;
    }

    public void setPriority(Short priority) {
        this.priority = priority;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSaveExtent() {
        return saveExtent;
    }

    public void setSaveExtent(Integer saveExtent) {
        this.saveExtent = saveExtent;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Date getContractStartTime() {
        return contractStartTime;
    }

    public void setContractStartTime(Date contractStartTime) {
        this.contractStartTime = contractStartTime;
    }

    public Date getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(Date contractEndTime) {
        this.contractEndTime = contractEndTime;
    }

    public int getIsHasOperatorTask() {
        return isHasOperatorTask;
    }

    public void setIsHasOperatorTask(int isHasOperatorTask) {
        this.isHasOperatorTask = isHasOperatorTask;
    }


    public String getTaskPid() {
        return taskPid;
    }

    public void setTaskPid(String taskPid) {
        this.taskPid = taskPid;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDesignOrg() {
        return designOrg;
    }

    public void setDesignOrg(String designOrg) {
        this.designOrg = designOrg;
    }

    public String getPersonInCharge() {
        if (designer != null) {
            personInCharge = designer.getCompanyUserName();
        }
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    public List<ProjectTaskProcessNodeDTO> getDesignersList() {
        return designersList;
    }

    public void setDesignersList(List<ProjectTaskProcessNodeDTO> designersList) {
        this.designersList = designersList;
    }

    public int getIsOperaterTask() {
        return isOperaterTask;
    }

    public void setIsOperaterTask(int isOperaterTask) {
        this.isOperaterTask = isOperaterTask;
    }

    public String getDesignOrgId() {
        return designOrgId;
    }

    public void setDesignOrgId(String designOrgId) {
        this.designOrgId = designOrgId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getSetPersonInChargeId() {
        if (designer != null) {
            personInChargeId = designer.getId();
        }
        return setPersonInChargeId;
    }

    public void setSetPersonInChargeId(String setPersonInChargeId) {
        this.setPersonInChargeId = setPersonInChargeId;
    }

    public int getIsHasChild() {
        return isHasChild;
    }

    public void setIsHasChild(int isHasChild) {
        this.isHasChild = isHasChild;
    }

    public Map<String, String> getRoleFlag() {
        return roleFlag;
    }

    public void setRoleFlag(Map<String, String> roleFlag) {
        this.roleFlag = roleFlag;
    }

    public int getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }

    public String getTaskPath() {
        return taskPath;
    }

    public void setTaskPath(String taskPath) {
        this.taskPath = taskPath;
    }


    public int getIsShowAppointmentTime() {
        return isShowAppointmentTime;
    }

    public void setIsShowAppointmentTime(int isShowAppointmentTime) {
        this.isShowAppointmentTime = isShowAppointmentTime;
    }

    public int getIsMyTask() {
        return isMyTask;
    }

    public void setIsMyTask(int isMyTask) {
        this.isMyTask = isMyTask;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }


    public int getAllDay() {
        if (!StringUtil.isNullOrEmpty(this.getPlanStartTime()) && !StringUtil.isNullOrEmpty(this.getPlanEndTime())) {
            return DateUtils.daysOfTwo(DateUtils.date_sdf.format(getPlanEndTime()), DateUtils.date_sdf.format(getPlanStartTime())) + 1;
        }
        return allDay;
    }

    public void setAllDay(int allDay) {
        this.allDay = allDay;
    }

    public String getPersonInChargeId() {
        if (designer != null) {
            personInChargeId = designer.getCompanyUserId();
        }
        return personInChargeId;
    }

    public void setPersonInChargeId(String personInChargeId) {
        this.personInChargeId = personInChargeId;
    }

    public int getNotCompleteCount() {
        return notCompleteCount;
    }

    public void setNotCompleteCount(int notCompleteCount) {
        this.notCompleteCount = notCompleteCount;
    }

    public int getIsRootTask() {
        return isRootTask;
    }

    public void setIsRootTask(int isRootTask) {
        this.isRootTask = isRootTask;
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }

    public int getProduceTaskLevel() {
        return produceTaskLevel;
    }

    public void setProduceTaskLevel(int produceTaskLevel) {
        this.produceTaskLevel = produceTaskLevel;
    }

    public String getBeModifyId() {
        return beModifyId;
    }

    public void setBeModifyId(String beModifyId) {
        this.beModifyId = beModifyId;
    }

    public String getBeModifyTaskPid() {
        return beModifyTaskPid;
    }

    public void setBeModifyTaskPid(String beModifyTaskPid) {
        this.beModifyTaskPid = beModifyTaskPid;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getBeModifyTaskType() {
        return beModifyTaskType;
    }

    public void setBeModifyTaskType(int beModifyTaskType) {
        this.beModifyTaskType = beModifyTaskType;
    }

    public ProjectMemberDTO getDesigner() {
        return designer;
    }

    public void setDesigner(ProjectMemberDTO designer) {
        this.designer = designer;
    }

    public ProjectTaskProcessNodeDTO getDesignUser() {
        return designUser;
    }

    public void setDesignUser(ProjectTaskProcessNodeDTO designUser) {
        this.designUser = designUser;
    }

    public ProjectTaskProcessNodeDTO getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(ProjectTaskProcessNodeDTO checkUser) {
        this.checkUser = checkUser;
    }

    public ProjectTaskProcessNodeDTO getExamineUser() {
        return examineUser;
    }

    public void setExamineUser(ProjectTaskProcessNodeDTO examineUser) {
        this.examineUser = examineUser;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getDesignUserName() {
        return designUserName;
    }

    public void setDesignUserName(String designUserName) {
        this.designUserName = designUserName;
    }

    public String getCheckUserName() {
        return checkUserName;
    }

    public void setCheckUserName(String checkUserName) {
        this.checkUserName = checkUserName;
    }

    public String getExamineUserName() {
        return examineUserName;
    }

    public void setExamineUserName(String examineUserName) {
        this.examineUserName = examineUserName;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public Integer getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(Integer taskLevel) {
        this.taskLevel = taskLevel;
    }

    public Integer getTaskLevelStatus() {
        return taskLevelStatus;
    }

    public void setTaskLevelStatus(Integer taskLevelStatus) {
        this.taskLevelStatus = taskLevelStatus;
    }

    public Integer getGradeStatus() {
        if (null != taskLevel && null != taskLevelStatus) {
            if ((1 == taskLevelStatus && 4 == taskLevel) || (2 == taskLevelStatus && 5 == taskLevel) || (3 == taskLevelStatus && 6 == taskLevel)) {
                gradeStatus = 1;
            }
        }
        return gradeStatus;
    }

    public void setGradeStatus(Integer gradeStatus) {
        this.gradeStatus = gradeStatus;
    }

    public String getDesignUserId() {
        return designUserId;
    }

    public void setDesignUserId(String designUserId) {
        this.designUserId = designUserId;
    }

    public String getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(String checkUserId) {
        this.checkUserId = checkUserId;
    }

    public String getExamineUserId() {
        return examineUserId;
    }

    public void setExamineUserId(String examineUserId) {
        this.examineUserId = examineUserId;
    }

    public ProjectDesignUser getProjectDesignUser() {
        return projectDesignUser;
    }

    public void setProjectDesignUser(ProjectDesignUser projectDesignUser) {
        this.projectDesignUser = projectDesignUser;
    }

    public ProjectDesignUser getProjectCheckUser() {
        return projectCheckUser;
    }

    public void setProjectCheckUser(ProjectDesignUser projectCheckUser) {
        this.projectCheckUser = projectCheckUser;
    }

    public ProjectDesignUser getProjectExamineUser() {
        return projectExamineUser;
    }

    public void setProjectExamineUser(ProjectDesignUser projectExamineUser) {
        this.projectExamineUser = projectExamineUser;
    }

    public Boolean getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Boolean changeTime) {
        this.changeTime = changeTime;
    }

    public String getCompletion() {
        return completion;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }
}
