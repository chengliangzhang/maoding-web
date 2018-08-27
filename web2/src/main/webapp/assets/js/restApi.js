/**
 * Created by Wuwq on 2017/2/27.
 */
var restApi = {
    /** 基础 **/
    url_homeLogin: window.rootPath + '/iWork/sys/login'
    , url_homeLogout: window.rootPath + '/iWork/sys/logout'
    , url_homeRegister_securityCode: window.rootPath + '/iWork/sys/securityCode'
    , url_homeRegister_validateCode: window.rootPath + '/iWork/sys/validateCode'
    , url_homeRegister_register: window.rootPath + '/iWork/sys/register'
    , url_homeForget_securityCode: window.rootPath + '/iWork/sys/sendSecurityCodeAndValidateCellphone'
    , url_homeForget_changePwd: window.rootPath + '/iWork/sys/forgotPassword'
    , url_shareInvateRegister: window.rootPath + '/iWork/sys/shareInvateRegister'
    , url_listUpdateHistory: window.rootPath + '/listUpdateHistory'

    /** 组织 **/
    , url_registerCompany: window.rootPath + '/iWork/home/registerCompany'
    , url_getOrgTreeForSearch: window.rootPath + '/iWork/org/getOrgTreeForSearch'
    , url_getOrgTree: window.rootPath + '/iWork/org/getOrgTree'
    , url_getOrgTreeForNotice: window.rootPath + '/iWork/org/getOrgTreeForNotice'
    , url_switchCompany: window.rootPath + '/iWork/home/switchCompany'
    , url_getOrgStructureTree: window.rootPath + '/iWork/org/getOrgStructureTree'
    , url_teamInfo: window.rootPath + '/iWork/org/teamInfo'
    , url_getOrgUserNoPage: window.rootPath + '/iWork/org/getOrgUserNoPage'
    , url_orderCompanyUser: window.rootPath + '/iWork/org/orderCompanyUser'
    , url_bulkImport: window.rootPath + '/iWork/org/bulkImport'
    , url_saveOrUpdateDepart: window.rootPath + '/iWork/org/saveOrUpdateDepart'
    , url_getOrgTreeSimple: window.rootPath + '/iWork/org/getOrgTreeSimple'
    , url_depart: window.rootPath + '/iWork/org/depart'
    , url_getDepartByCompanyId: window.rootPath + '/iWork/org/getDepartByCompanyId'
    , url_saveCompanyUser: window.rootPath + '/iWork/org/saveCompanyUser'
    , url_subCompany: window.rootPath + '/iWork/org/subCompany'
    , url_businessPartner: window.rootPath + '/iWork/org/businessPartner'
    , url_getOrgUser: window.rootPath + '/iWork/org/getOrgUserList'
    , url_audiOrgUser: window.rootPath + '/iWork/org/audiOrgUser'
    , url_uploadUserFile: window.rootPath + '/iWork/org/uploadUserFile'
    , url_selectInvitedPartner: window.rootPath + '/iWork/org/selectInvitedPartner'
    , url_auditOrgRelation: window.rootPath + '/iWork/org/processingApplicationOrInvitation'
    , url_getPendingAudiOrgUser: window.rootPath + '/iWork/org/getPendingAudiOrgUser'
    , url_saveOrUpdateCompany: window.rootPath + '/iWork/org/saveOrUpdateCompany'
    , url_saveOrUpdateCompanyAttach: window.rootPath + '/iWork/org/saveOrUpdateCompanyAttach'
    , url_notice: window.rootPath + '/iWork/notice/notice'
    , url_saveNotice: window.rootPath + '/iWork/notice/saveNotice'
    , url_getNoticeByNoticeid: window.rootPath + '/iWork/notice/getNoticeByNoticeid'
    , url_disbandCompany: window.rootPath + '/iWork/org/disbandCompany'
    , url_getUsedPartB: window.rootPath + '/iWork/org/getUsedPartB'
    , url_getUserByKeyWord: window.rootPath + '/iWork/org/getUserByKeyWord'
    , url_getToAuditCompanyCount: window.rootPath + '/iWork/org/getToAuditCompanyCount'
    , url_validateDisbandCompany: window.rootPath + '/iWork/org/validateDisbandCompany'
    , url_applyAuthentication: window.rootPath + '/iWork/org/applyAuthentication'
    , url_getAuthenticationById: window.rootPath + '/iWork/org/getAuthenticationById'
    , url_listCompanyAndChildren: window.rootPath + '/iWork/org/listCompanyAndChildren'
    , url_getExpAmountCompanyAndChildren: window.rootPath + '/iWork/org/getExpAmountCompanyAndChildren'
    , url_listCompany: window.rootPath + '/iWork/org/listCompany'


    /** 分支机构、事业合伙人、外部合作 **/
    , url_inviteBPartner: window.rootPath + '/iWork/org/inviteParent'
    , url_verifyIdentityForBPartner: window.rootPath + '/na/bPartner/verifyIdentityForParent'
    , url_getCompanyByInviteUrl: window.rootPath + '/na/bPartner/getCompanyByInviteUrl'
    , url_getCompanyPrincipal: window.rootPath + '/na/bPartner/getCompanyPrincipal'
    , url_applayBusinessPartner: window.rootPath + '/na/bPartner/applayBusinessPartner'
    , url_createBusinessPartner: window.rootPath + '/na/bPartner/createBusinessPartner'
    , url_setBusinessPartnerNickName: window.rootPath + '/iWork/org/setBusinessPartnerNickName'
    , url_getProjectPartnerList: window.rootPath + '/iWork/cooperation/getProjectPartnerList'
    , url_relieveRelationship: window.rootPath + '/iWork/cooperation/relieveRelationship'
    , url_resendSMS: window.rootPath + '/iWork/cooperation/resendSMS'

    /** 个人 **/
    , url_getCurrUserOfWork: window.rootPath + '/iWork/sys/getCurrUserOfWork'
    , url_userInfo: window.rootPath + '/iWork/personal/userInfo'
    , url_saveOrUpdateUserAttach: window.rootPath + '/iWork/personal/saveOrUpdateUserAttach'
    , url_changeCellphone: window.rootPath + '/iWork/personal/changeCellphone'
    , url_securityCode: window.rootPath + '/iWork/sys/securityCode'
    , url_changePassword: window.rootPath + '/iWork/personal/changePassword'
    , url_getMajor: window.rootPath + '/iWork/personal/getMajor'
    , url_getCompanyDepartAndPermission: window.rootPath + '/iWork/role/getCompanyDepartAndPermission'

    /** 项目 **/
    , url_projectInformation: window.rootPath + '/iWork/project/projectInformation'
    , url_companyProjectInformation: window.rootPath + '/iWork/project/companyProjectInformation'
    , url_getProjectBulidType: window.rootPath + '/iWork/project/getProjectBulidType'
    , url_getMyProjectList: window.rootPath + '/iWork/project/getProjects'
    , url_getProjectConditions: window.rootPath + '/iWork/project/getProjectConditions'
    , url_projectType: window.rootPath + '/iWork/project/projectType'
    , url_addProjectBasicData: window.rootPath + '/iWork/project/addProjectBasicData'
    , url_project: window.rootPath + '/iWork/project/project'
    , url_saveOrUpdateProjectProcessTime: window.rootPath + '/iWork/project/saveOrUpdateProjectProcessTime'
    , url_getProjectDetails: window.rootPath + '/iWork/project/getProjectDetails'
    , url_getDesignContentList: window.rootPath + '/iWork/project/getDesignContentList'
    , url_getDesignRangeList: window.rootPath + '/iWork/project/getDesignRangeList'
    , url_attention: window.rootPath + '/iWork/attention/attention'
    , url_constructList: window.rootPath + '/iWork/project/constructList'
    , url_deleteProject: window.rootPath + '/iWork/project/deleteProject'
    , url_getProjectParticipants: window.rootPath + '/iWork/project/getProjectParticipants'
    , url_getProjectNavigationRole: window.rootPath + '/iWork/project/getProjectNavigationRole'
    , url_saveOrUpdateProjectDesign: window.rootPath + '/iWork/project/saveOrUpdateProjectDesign'
    , url_getCostRoleByCompanyId: window.rootPath + '/iWork/project/getCostRoleByCompanyId'

    , url_loadProjectDetails: window.rootPath + '/iWork/project/loadProjectDetails'
    , url_loadProjectCustomFields: window.rootPath + '/iWork/project/loadProjectCustomFields'
    , url_saveProjectCustomFields: window.rootPath + '/iWork/project/saveProjectCustomFields'
    , url_saveProjectField: window.rootPath + '/iWork/project/saveProjectField'

    , url_enterpriseSearch: window.enterpriseUrl + '/enterpriseSearch/queryAutoComplete'
    , url_deleteProjectDesign: window.rootPath + '/iWork/project/deleteProjectDesign'

    , url_insertProCondition: window.rootPath + '/iWork/project/insertProCondition'
    , url_inviteProjectParent: window.rootPath + '/iWork/org/inviteProjectParent'


    /**设置字段**/
    , url_listOptionalTitle: window.rootPath + '/iWork/project/listOptionalTitle'
    , url_listTitle: window.rootPath + '/iWork/project/listTitle'
    , url_changeTitle: window.rootPath + '/iWork/project/changeTitle'



    /** 项目动态 **/
    , url_getProjectDynamicList: window.rootPath + '/iWork/project/getProjectDynamicList'

    /**项目－签发、生产*/
    , url_getOperatorList: window.rootPath + '/iWork/projectTask/getOperatorList'
    , url_saveTaskIssuing: window.rootPath + '/iWork/projectTask/saveTaskIssuing'
    , url_getIssueTaskCompany: window.rootPath + '/iWork/projectTask/getIssueTaskCompany'
    , url_validateIssueTaskCompany: window.rootPath + '/iWork/projectTask/validateIssueTaskCompany'
    , url_updateTaskInfo: window.rootPath + '/iWork/projectTask/updateTaskInfo'
    , url_saveProjectProcessTime: window.rootPath + '/iWork/projectTask/saveProjectProcessTime'
    , url_deleteProjectTask: window.rootPath + '/iWork/projectTask/deleteProjectTask'
    , url_getChangeTimeList: window.rootPath + '/iWork/projectTask/getChangeTimeList'
    , url_updateProjectManager: window.rootPath + '/iWork/projectTask/updateProjectManager'
    , url_getDesignTaskList: window.rootPath + '/iWork/projectTask/getDesignTaskList'
    , url_getProductTaskOverview: window.rootPath + '/iWork/projectTask/getProductTaskOverview'
    , url_getProcessesByTask: window.rootPath + '/iWork/projectProcess/getProcessesByTask'
    , url_saveOrUpdateProcess: window.rootPath + '/iWork/projectProcess/saveOrUpdateProcess'
    , url_transferTaskResponse: window.rootPath + '/iWork/projectTask/transferTaskResponse'
    , url_getProjectTaskCoopateCompany: window.rootPath + '/iWork/projectTask/getProjectTaskCoopateCompany'
    //, url_deleteProjectTask: window.rootPath + '/iWork/projectTask/deleteProjectTask'
    , url_getProjectTaskForChangeDesigner: window.rootPath + '/iWork/projectTask/getProjectTaskForChangeDesigner'
    , url_transferTaskDesigner: window.rootPath + '/iWork/projectTask/transferTaskDesigner'
    , url_exportTaskList: window.rootPath + '/iWork/projectTask/exportTaskList'
    , url_publishProductTask: window.rootPath + '/iWork/projectTask/publishProductTask'
    , url_updateProjectAssistant: window.rootPath + '/iWork/projectTask/updateProjectAssistant'

    , url_listOperatorManager: window.rootPath + '/iWork/org/listOperatorManager'
    , url_listDesignManager: window.rootPath + '/iWork/org/listDesignManager'

    , url_getTaskGroupInfo: window.rootPath + '/iWork/projectTask/getTaskGroupInfo'

    , url_getIssueInfo: window.rootPath + '/iWork/projectTask/getIssueInfo'
    , url_publishIssueTask: window.rootPath + '/iWork/projectTask/publishIssueTask'
    , url_getIssueTaskOverview: window.rootPath + '/iWork/projectTask/getIssueTaskOverview'
    , url_getProjectInfoForTask: window.rootPath + '/iWork/projectTask/getProjectInfoForTask'
    , url_exchangeTask: window.rootPath + '/iWork/projectTask/exchangeTask'
    , url_completeTask: window.rootPath + '/iWork/projectTask/completeTask'
    , url_updateCompleteTask: window.rootPath + '/iWork/projectTask/updateCompleteTask'
    , url_activeProjectTask: window.rootPath + '/iWork/projectTask/activeProjectTask'
    , url_getDesignManagerInfo: window.rootPath + '/iWork/projectTask/getDesignManagerInfo'

    , url_listDeliver: window.rootPath + '/iWork/myTask/listDeliver'
    , url_changeDeliver: window.rootPath + '/iWork/myTask/changeDeliver'
    , url_deleteDeliver: window.rootPath + '/iWork/myTask/deleteDeliver'


    /** 费用 **/
    , url_saveProjectCost: window.rootPath + '/iWork/projectcost/saveProjectCost'
    //, url_saveCostPaymentDetail: window.rootPath + '/iWork/projectcost/saveCostPaymentDetail'
    , url_contractInfo: window.rootPath + '/iWork/projectcost/contractInfo'
    , url_saveProjectCostPoint: window.rootPath + '/iWork/projectcost/saveProjectCostPoint'
    , url_saveReturnMoneyDetail: window.rootPath + '/iWork/projectcost/saveOrUpdateReturnMoneyDetail'
    , url_techicalReviewFeeInfo: window.rootPath + '/iWork/projectcost/techicalReviewFeeInfo'
    , url_getOtherFee: window.rootPath + '/iWork/projectcost/getOtherFee'
    , url_cooperativeDesignFeeInfo: window.rootPath + '/iWork/projectcost/cooperativeDesignFeeInfo'
    , url_deleteProjectCostPoint: window.rootPath + '/iWork/projectcost/deleteProjectCostPoint'
    , url_deleteProjectCostPointDetail: window.rootPath + '/iWork/projectcost/deleteProjectCostPointDetail'
    , url_saveCostPaymentDetail: window.rootPath + '/iWork/projectcost/saveCostPaymentDetail'
    , url_saveOtherCostDetail: window.rootPath + '/iWork/projectcost/saveOtherCostDetail'
    , url_deleteProjectCostPaymentDetail: window.rootPath + '/iWork/projectcost/deleteProjectCostPaymentDetail'
    , url_listProjectCost: window.rootPath + '/iWork/projectcost/listProjectCost'
    , url_listProjectCostSummary: window.rootPath + '/iWork/projectcost/listProjectCostSummary'
    , url_applyProjectCostPayFee: window.rootPath + '/iWork/projectcost/applyProjectCostPayFee'
    , url_getProjectCostPaymentDetailByPointDetailIdForPay: window.rootPath + '/iWork/projectcost/getProjectCostPaymentDetailByPointDetailIdForPay'

    /**发票**/
    , url_listInvoice: window.rootPath + '/iWork/invoice/listInvoice'
    , url_getInvoice: window.rootPath + '/iWork/invoice/getInvoice'


    /** 报销 **/
    , url_getExpMainPage: window.rootPath + '/iWork/finance/getExpMainPage'
    , url_getExpMainPageForAudit: window.rootPath + '/iWork/finance/getExpMainPageForAudit'
    , url_getExpTypeList: window.rootPath + '/iWork/finance/getExpTypeList'
    , url_agreeExpMain: window.rootPath + '/iWork/finance/agreeExpMain'
    , url_getExpMainPageForSummary: window.rootPath + '/iWork/finance/getExpMainPageForSummary'
    , url_deleteExpMain: window.rootPath + '/iWork/finance/deleteExpMain'
    , url_getExpBaseData: window.rootPath + '/iWork/finance/getExpBaseData'
    , url_getMaxExpNo: window.rootPath + '/iWork/finance/getMaxExpNo'
    , url_expCategory: window.rootPath + '/iWork/finance/expCategory'
    , url_getExpMainDetail: window.rootPath + '/iWork/finance/getExpMainDetail'
    , url_recallExpMain: window.rootPath + '/iWork/finance/recallExpMain'
    , url_saveOrUpdateExpMainAndDetail: window.rootPath + '/iWork/finance/saveOrUpdateExpMainAndDetail'
    , url_agreeAndTransAuditPerExpMain: window.rootPath + '/iWork/finance/agreeAndTransAuditPerExpMain'
    , url_toMyChecking: window.rootPath + '/iWork/finance/toMyChecking'
    , url_toCompanyMyChecking: window.rootPath + '/iWork/finance/toCompanyMyChecking'
    , url_toMyExpense: window.rootPath + '/iWork/finance/toMyExpense'
    , url_toCompanyMyExpense: window.rootPath + '/iWork/finance/toCompanyMyExpense'

    , url_getExpFixedByExpDate: window.rootPath + '/iWork/finance/getExpFixedByExpDate'
    , url_getExpAmountByYear: window.rootPath + '/iWork/finance/getExpAmountByYear'
    , url_saveExpFixedByExpDate: window.rootPath + '/iWork/finance/saveExpFixedByExpDate'
    , url_financialAllocation: window.rootPath + '/iWork/finance/financialAllocation'

    , url_getLeaveDetailList: window.rootPath + '/iWork/finance/getLeaveDetailList'
    , url_getLeaveDetail: window.rootPath + '/iWork/finance/getLeaveDetail'
    , url_financialRecallExpMain: window.rootPath + '/iWork/finance/financialRecallExpMain'
    , url_saveCompanyBalance: window.rootPath + '/iWork/companyBill/saveCompanyBalance'
    , url_getCompanyBalance: window.rootPath + '/iWork/companyBill/getCompanyBalance'
    , url_getExpFixTypeList: window.rootPath + '/iWork/finance/getExpFixTypeList'
    , url_saveExpFixCategory: window.rootPath + '/iWork/finance/saveExpFixCategory'
    , url_deleteExpCategory: window.rootPath + '/iWork/finance/deleteExpCategory'
    , url_saveExpTypeShowStatus: window.rootPath + '/iWork/finance/saveExpTypeShowStatus'
    , url_getExpShareTypeList: window.rootPath + '/iWork/finance/getExpShareTypeList'
    , url_saveExpShareTypeShowStatus: window.rootPath + '/iWork/finance/saveExpShareTypeShowStatus'
    , url_getRelationTypeIsThree: window.rootPath + '/iWork/org/getRelationTypeIsThree'
    , url_getPermissionOperator: window.rootPath + '/iWork/role/getPermissionOperator'

    /**财务流程设置**/
    , url_getProcessByCompany: window.rootPath + '/iWork/process/getProcessByCompany'
    , url_saveProcess: window.rootPath + '/iWork/process/saveProcess'
    , url_listProcessNode: window.rootPath + '/iWork/process/listProcessNode'
    , url_deleteProcessForProjectPay: window.rootPath + '/iWork/process/deleteProcessForProjectPay'
    , url_selectedProcessForProjectPay: window.rootPath + '/iWork/process/selectedProcessForProjectPay'
    , url_selectedProcessNodeStatus: window.rootPath + '/iWork/process/selectedProcessNodeStatus'

    /**审批管理**/
    , url_listProcessDefine: window.rootPath + '/iWork/workflow/listProcessDefine'
    , url_prepareProcessDefine: window.rootPath + '/iWork/workflow/prepareProcessDefine'
    , url_changeProcessDefine: window.rootPath + '/iWork/workflow/changeProcessDefine'

    /**收支总览**/
    , url_getExpensesDetailLedger: window.rootPath + '/iWork/statistic/getExpensesDetailLedger'
    , url_getReceivable: window.rootPath + '/iWork/statistic/getReceivable'
    , url_getPayment: window.rootPath + '/iWork/statistic/getPayment'
    , url_getReceivableDetail: window.rootPath + '/iWork/statistic/getReceivableDetail'
    , url_getPaymentDetail: window.rootPath + '/iWork/statistic/getPaymentDetail'
    , url_getExpensesStatistics: window.rootPath + '/iWork/statistic/getExpensesStatistics'
    , url_getColumnarData: window.rootPath + '/iWork/statistic/getColumnarData'
    , url_getProfitDetail: window.rootPath + '/iWork/statistic/getProfitDetail'

    , url_getCostType: window.rootPath + '/iWork/statistic/getCostType'
    , url_getStaticCompanyForPaymentDetail: window.rootPath + '/iWork/org/getStaticCompanyForPaymentDetail'
    , url_getStaticCompanyForFinance: window.rootPath + '/iWork/org/getStaticCompanyForFinance'
    , url_getStatisticClassicData: window.rootPath + '/iWork/statistic/getStatisticClassicData'
    , url_getTitleFilter: window.rootPath + '/iWork/statistic/getTitleFilter'

    /**审批汇总**/
    , url_getProjectWorking: window.rootPath + '/iWork/project/getProjectWorking'
    , url_getProjectWorkingHours: window.rootPath + '/iWork/project/getProjectWorkingHours'

    /** 系统通知 **/
    , url_sys_notify: window.rootPath + '/iWork/sys/notify'
    , url_sys_complete_notify: window.rootPath + '/iWork/sys/completeNotify'

    /** 通知 **/
    , url_getNotice: window.rootPath + '/iWork/notice/getNotice'

    /** 消息 **/
    , url_getMessage: window.rootPath + '/iWork/message/getMessage'
    , url_getMessageUnRead: window.rootPath + '/iWork/message/getMessageUnRead'
    , url_isUserInOrg: window.rootPath + '/iWork/org/isUserInOrg'
    , url_getNotReadNotice: window.rootPath + '/iWork/notice/getNotReadNotice'

    /** 任务 */
    , url_taskList: window.rootPath + '/iWork/myTask/taskList'
    , url_getMyTask: window.rootPath + '/iWork/myTask/getMyTask'
    , url_handleMyTask: window.rootPath + '/iWork/myTask/handleMyTask'
    , url_activeTask: window.rootPath + '/iWork/projectTask/activeTask'
    , url_getMyTaskList: window.rootPath + '/iWork/myTask/getMyTaskList'
    , url_getMyTaskList2: window.rootPath + '/iWork/myTask/getMyTaskList2'
    , url_getMyTaskByProjectId: window.rootPath + '/iWork/myTask/getMyTaskByProjectId'

    ,url_getMyTaskList4:window.rootPath + '/iWork/myTask/getMyTaskList4'

    /** 动态 */
    , url_getCompanyDynamics: window.rootPath + '/iWork/dynamic/getCompanyDynamics'

    /** 网盘 **/
    , url_getCompanyDiskInfo: window.fileCenterUrl + '/companyDisk/getCompanyDiskInfo'

    /** 文档库 */
    , url_getSkyDriverByProject: window.rootPath + '/iWork/projectSkyDriver/getSkyDriverByProject'
    , url_netFile_createDirectory: window.fileCenterUrl + '/netFile/createDirectory'
    , url_netFile_uploadFile: window.fileCenterUrl + '/netFile/uploadFile'
    , url_netFile_rename: window.fileCenterUrl + '/netFile/rename'
    , url_netFile_delete: window.fileCenterUrl + '/netFile/delete'

    , url_getProjectsDocuments: window.rootPath + '/iWork/projectSkyDriver/getProjectsDocuments'
    , url_getMyProjectsDocuments: window.rootPath + '/iWork/projectSkyDriver/getMyProjectsDocuments'
    , url_getMyProjectSkyDriveByParam: window.rootPath + '/iWork/projectSkyDriver/getMyProjectSkyDriveByParam'
    , url_getArchivedFileNotifier: window.rootPath + '/iWork/projectSkyDriver/getArchivedFileNotifier'
    , url_sendArchivedFileNotifier: window.rootPath + '/iWork/projectSkyDriver/sendarchivedFileNotifier'
    , url_notarizeArchivedFileNotifier: window.rootPath + '/iWork/projectSkyDriver/notarizeArchivedFileNotifier'
    , url_sendOwner: window.rootPath + '/iWork/projectSkyDriver/sendOwner'
    , url_sendOwnerProjectFile: window.rootPath + '/iWork/projectSkyDriver/sendOwnerProjectFile'
    , url_getSkyDriverByProjectList: window.rootPath + '/iWork/projectSkyDriver/getSkyDriverByProjectList'
    , url_getProjectFileByFileName: window.rootPath + '/iWork/projectSkyDriver/getProjectFileByFileName'
    , url_getProjectContract: window.rootPath + '/iWork/projectSkyDriver/getProjectContract'


    /** 附件 **/
    , url_attachment_delete: window.fileCenterUrl + '/attachment/delete'
    , url_attachment_uploadProjectContract: window.fileCenterUrl + '/attachment/uploadProjectContract'
    , url_attachment_uploadExpenseAttach: window.fileCenterUrl + '/attachment/uploadExpenseAttach'
    , url_attachment_saveCompanyLogo: window.fileCenterUrl + '/attachment/saveCompanyLogo'
    , url_attachment_uploadOrgAuthenticationAttach: window.fileCenterUrl + '/attachment/uploadOrgAuthenticationAttach'
    , url_downLoadFile: window.fileCenterUrl + '/downLoadFile'
    , url_attachment_uploadNoticeAttach: window.fileCenterUrl + '/attachment/uploadNoticeAttach'
    , url_attachment_uploadCostPlanAttach: window.fileCenterUrl + '/attachment/uploadCostPlanAttach'

    /** 后台管理*/
    , url_permissionSettings: window.rootPath + '/iWork/role/permissionSettings'
    , url_getRoleUserPermission: window.rootPath + '/iWork/role/getRoleUserPermission'
    , url_userPermission: window.rootPath + '/iWork/role/userPermission'
    , url_getRolePermissionByUser: window.rootPath + '/iWork/role/getRolePermissionByUser'
    , url_transferSys: window.rootPath + '/iWork/org/transferSys'
    , url_deleteUserPermission: window.rootPath + '/iWork/role/deleteUserPermission'
    , url_saveUserPermission: window.rootPath + '/iWork/role/saveUserPermission'
    , url_roleUser: window.rootPath + '/iWork/role/roleUser'
    , url_getRolePermissionByType: window.rootPath + '/iWork/role/getRolePermissionByType'



    /** 统计 **/
    , url_getStatProjectCost: window.rootPath + '/iWork/statistic/getStatProjectCost'
    , url_getStatContractFee: window.rootPath + '/iWork/statistic/getStatContractFee'
    , url_getStatTechFee: window.rootPath + '/iWork/statistic/getStatTechFee'
    , url_getStatCorpFee: window.rootPath + '/iWork/statistic/getStatCorpFee'
    , url_getStatOtherFee: window.rootPath + '/iWork/statistic/getStatOtherFee'
    , url_getStatisticSummaryDTO: window.rootPath + '/iWork/statistic/getStatisticSummaryDTO'
    , url_getStatisticDetailSummary: window.rootPath + '/iWork/statistic/getStatisticDetailSummary'
    , url_getStaticCompany: window.rootPath + '/iWork/org/getStaticCompany'
    , url_getContractInfo: window.rootPath + '/iWork/statistic/getContractInfo'
    , url_getTechicalReviewFeeInfo: window.rootPath + '/iWork/statistic/getTechicalReviewFeeInfo'
    , url_getCooperativeDesignFeeInfo: window.rootPath + '/iWork/statistic/getCooperativeDesignFeeInfo'
    , url_getOtherFeeInfo: window.rootPath + '/iWork/statistic/getOtherFeeInfo'
    , url_listProjectByProjectName: window.rootPath + '/iWork/project/listProjectByProjectName'
    , url_listUserTaskStatistic: window.rootPath + '/iWork/statistic/listUserTaskStatistic'
    , url_getCategoryTypeList: window.rootPath + '/iWork/statistic/getCategoryTypeList'

    /** 历史数据 **/
    , url_historyData_importProjects: window.rootPath + '/iWork/historyData/importProjects'
    , url_historyData_createProjects:window.rootPath+'/iWork/historyData/createProjects'
};