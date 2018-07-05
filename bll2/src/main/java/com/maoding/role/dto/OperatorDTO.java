package com.maoding.role.dto;

import java.io.Serializable;

public class OperatorDTO implements Serializable {

    //以下权限操作控制==1 可以操作

    //组织架构模块
    private int organizationModule;

    //组织模块
    private int companyModule ;
    private int orgInfoEdit;//编辑组织信息
    private int orgDisband;//解散组织
    private int bannerEdit;//轮播图编辑
    private int companyAuth;//组织审核
    private int companyView = 1;
    //权限模块
    private int roleModule;
    private int roleCreate;//权限创建
    private int roleEdit;//权限编辑
    private int roleDelete;//权限删除
    private int rolePermissionConfig;//权限权限配置
    private int roleOrgConfig;//权限成员配置

    private int backGroundManager;//后台管理

    private int departModule;//部门模块
    private int departCreate;//部门创建
    private int departEdit;//部门编辑
    private int departDelete;//部门删除

    private int companyUserModule;
    private int companyUserCreate;//组织成员创建
    private int companyUserEdit;//组织成员编辑
    private int companyUserDelete;//组织成员删除
    private int companyUserInvite;//邀请加入组织
    private int companyUserImport;//批量导入

    private int subOrgModule;
    private int subOrgCreate; //创建分支机构
    private int subOrgEdit;//分支机构编辑
    private int subOrgDelete;//分支机构删除
    private int subOrgInvite;//邀请加入组织

    private int partnerModule;
    private int partnerCreate;//创建事业合伙人
    private int partnerEdit;//事业合伙人编辑
    private int partnerDelete;//事业合伙人删除
    private int partnerInvite;//邀请加入组织

    private int projectDocModule;//项目文档库入口
    private int projectImport;//项目导入

    private int messageModule = 1;//消息
    private int projectCreate = 1;//项目立项
    private int myTaskModule = 1;//我的任务

    private int noticeModule = 1;//通知公告
    private int noticeCreate = 0;//通知公告发布
    private int noticeEdit = 0;//通知公告编辑
    private int noticeDelete = 0;//通知公告删除


    private int financeModule;// 财务管理
    private int paymentDetailModule;// 收支明细
    private int costDetailView;// 台账
    private int paidStatisticsView;// 应收
    private int payStatisticsView ;// 应付
    private int classicStatisticsView;// 分类明细
    private int profitStatisticsView;// 利润报表
    private int fixCostModule;// 费用录入模块
    private int fixCostEdit;// 费用录入
    private int financeTypeEdit; //报销/费用财务设置
    private int expCategorySet;//费用类别设置
    private int baseFinanceDataSet;//团队基础数据设置
    private int shareCostSet;//费用分摊项设置

    //审批管理
    private int auditModule;
    private int expStatisticsView;//报销统计
    private int costStatisticsView;//费用统计
    private int leaveStatisticsView;//请假统计
    private int onBusinessStatisticsView;//出差统计
    private int laborHourStatisticsView;//工时统计
    private int expAllocate; //报销拨款
    private int costAllocate; //费用拨款

    public int contractReceive;//合同回款到账确认
    public int technicalReceive;//技术审查费到账确认
    public int cooperationReceive;//合作设计费到账确认
    public int otherReceive;//其他到账付款确认
    public int technicalPay ;//技术审查费付款确认
    public int cooperationPay;//合作设计费付款确认
    public int otherPay;//其他收支付款确认

    public int getOrganizationModule() {
        if(companyUserModule==1 || departModule==1 || subOrgModule==1 || partnerModule==1){
            organizationModule = 1;
        }
        return organizationModule;
    }

    public void setOrganizationModule(int organizationModule) {
        this.organizationModule = organizationModule;
    }

    public int getOrgDisband() {
        return orgDisband;
    }
    public void setOrgDisband(int orgDisband) {
        this.orgDisband = orgDisband;
    }

    public int getRoleCreate() {
        return roleCreate;
    }

    public void setRoleCreate(int roleCreate) {
        this.roleCreate = roleCreate;
    }

    public int getRoleEdit() {
        return roleEdit;
    }

    public void setRoleEdit(int roleEdit) {
        this.roleEdit = roleEdit;
    }

    public int getRoleDelete() {
        return roleDelete;
    }

    public void setRoleDelete(int roleDelete) {
        this.roleDelete = roleDelete;
    }

    public int getBannerEdit() {
        return bannerEdit;
    }

    public void setBannerEdit(int bannerEdit) {
        this.bannerEdit = bannerEdit;
    }

    public int getRolePermissionConfig() {
        return rolePermissionConfig;
    }

    public void setRolePermissionConfig(int rolePermissionConfig) {
        this.rolePermissionConfig = rolePermissionConfig;
    }

    public int getRoleOrgConfig() {
        return roleOrgConfig;
    }

    public void setRoleOrgConfig(int roleOrgConfig) {
        this.roleOrgConfig = roleOrgConfig;
    }

    public int getDepartEdit() {
        return departEdit;
    }

    public void setDepartEdit(int departEdit) {
        this.departEdit = departEdit;
    }

    public int getCompanyUserEdit() {
        return companyUserEdit;
    }

    public void setCompanyUserEdit(int companyUserEdit) {
        this.companyUserEdit = companyUserEdit;
    }

    public int getSubOrgCreate() {
        return subOrgCreate;
    }

    public void setSubOrgCreate(int subOrgCreate) {
        this.subOrgCreate = subOrgCreate;
    }

    public int getSubOrgInvite() {
        return subOrgInvite;
    }

    public void setSubOrgInvite(int subOrgInvite) {
        this.subOrgInvite = subOrgInvite;
    }

    public int getPartnerInvite() {
        return partnerInvite;
    }

    public void setPartnerInvite(int partnerInvite) {
        this.partnerInvite = partnerInvite;
    }


    public int getOrgInfoEdit() {
        return orgInfoEdit;
    }

    public void setOrgInfoEdit(int orgInfoEdit) {
        this.orgInfoEdit = orgInfoEdit;
    }

    public int getBackGroundManager() {
        //后台管理包含了，企业认证，成员，部门，权限，下属组织，历史数据导入管理
        if(companyModule==1 || companyAuth==1 || companyUserModule==1  || departModule==1
                || subOrgModule == 1 || partnerModule ==1 || roleModule==1 || projectImport==1){
            backGroundManager = 1;
        }
        return backGroundManager;
    }

    public void setBackGroundManager(int backGroundManager) {
        this.backGroundManager = backGroundManager;
    }

    public int getCompanyModule() {
        //如果具有组织基本信息编辑或许解散组织的权限，则展示组织信息菜单
        if(orgInfoEdit==1 || orgDisband==1){
            companyModule = 1;
        }
        return companyModule;
    }

    public void setCompanyModule(int companyModule) {
        this.companyModule = companyModule;
    }

    public int getRoleModule() {
        //如果具有权限管理，角色成员分配，角色权限配置 的权限，则展示权限模块
        if(roleCreate==1 || roleEdit==1 || roleDelete==1 || roleOrgConfig==1 || rolePermissionConfig ==1){
            roleModule = 1;
        }
        return roleModule;
    }

    public void setRoleModule(int roleModule) {
        this.roleModule = roleModule;
    }

    public int getDepartModule() {
        if(departCreate==1 || departEdit==1 || departDelete==1){
            departModule = 1;
        }
        return departModule;
    }

    public void setDepartModule(int departModule) {
        this.departModule = departModule;
    }

    public int getDepartCreate() {
        return departCreate;
    }

    public void setDepartCreate(int departCreate) {
        this.departCreate = departCreate;
    }

    public int getDepartDelete() {
        return departDelete;
    }

    public void setDepartDelete(int departDelete) {
        this.departDelete = departDelete;
    }

    public int getCompanyUserModule() {
        if(companyUserCreate==1 || companyUserEdit==1 || companyUserDelete ==1
                || companyUserImport==1 || companyUserInvite==1){
            companyUserModule = 1;
        }
        return companyUserModule;
    }

    public void setCompanyUserModule(int companyUserModule) {
        this.companyUserModule = companyUserModule;
    }

    public int getCompanyUserCreate() {
        return companyUserCreate;
    }

    public void setCompanyUserCreate(int companyUserCreate) {
        this.companyUserCreate = companyUserCreate;
    }

    public int getCompanyUserDelete() {
        return companyUserDelete;
    }

    public void setCompanyUserDelete(int companyUserDelete) {
        this.companyUserDelete = companyUserDelete;
    }

    public int getCompanyUserInvite() {
        return companyUserInvite;
    }

    public void setCompanyUserInvite(int companyUserInvite) {
        this.companyUserInvite = companyUserInvite;
    }

    public int getCompanyUserImport() {
        return companyUserImport;
    }

    public void setCompanyUserImport(int companyUserImport) {
        this.companyUserImport = companyUserImport;
    }

    public int getSubOrgModule() {
        //分公司模块管理
        if (subOrgCreate==1 || subOrgEdit ==1 || subOrgDelete ==1 || subOrgInvite ==1){
            subOrgModule = 1;
        }
        return subOrgModule;
    }

    public void setSubOrgModule(int subOrgModule) {
        this.subOrgModule = subOrgModule;
    }

    public int getSubOrgEdit() {
        return subOrgEdit;
    }

    public void setSubOrgEdit(int subOrgEdit) {
        this.subOrgEdit = subOrgEdit;
    }

    public int getSubOrgDelete() {
        return subOrgDelete;
    }

    public void setSubOrgDelete(int subOrgDelete) {
        this.subOrgDelete = subOrgDelete;
    }

    public int getPartnerModule() {
        //事业合伙人模块管理
        if(partnerCreate ==1 || partnerEdit ==1 || partnerDelete==1 || partnerInvite ==1){
            partnerModule = 1;
        }
        return partnerModule;
    }

    public void setPartnerModule(int partnerModule) {
        this.partnerModule = partnerModule;
    }

    public int getPartnerCreate() {
        return partnerCreate;
    }

    public void setPartnerCreate(int partnerCreate) {
        this.partnerCreate = partnerCreate;
    }

    public int getPartnerEdit() {
        return partnerEdit;
    }

    public void setPartnerEdit(int partnerEdit) {
        this.partnerEdit = partnerEdit;
    }

    public int getPartnerDelete() {
        return partnerDelete;
    }

    public void setPartnerDelete(int partnerDelete) {
        this.partnerDelete = partnerDelete;
    }

    public int getProjectDocModule() {
        return projectDocModule;
    }

    public void setProjectDocModule(int projectDocModule) {
        this.projectDocModule = projectDocModule;
    }

    public int getMessageModule() {
        return messageModule;
    }

    public void setMessageModule(int messageModule) {
        this.messageModule = messageModule;
    }

    public int getProjectCreate() {
        return projectCreate;
    }

    public void setProjectCreate(int projectCreate) {
        this.projectCreate = projectCreate;
    }

    public int getMyTaskModule() {
        return myTaskModule;
    }

    public void setMyTaskModule(int myTaskModule) {
        this.myTaskModule = myTaskModule;
    }

    public int getNoticeModule() {
        return noticeModule;
    }

    public void setNoticeModule(int noticeModule) {
        this.noticeModule = noticeModule;
    }

    public int getNoticeCreate() {
        return noticeCreate;
    }

    public void setNoticeCreate(int noticeCreate) {
        this.noticeCreate = noticeCreate;
    }

    public int getNoticeEdit() {
        return noticeEdit;
    }

    public void setNoticeEdit(int noticeEdit) {
        this.noticeEdit = noticeEdit;
    }

    public int getNoticeDelete() {
        return noticeDelete;
    }

    public void setNoticeDelete(int noticeDelete) {
        this.noticeDelete = noticeDelete;
    }

    public int getFinanceModule() {
        //财务模块包含以下子模块，只要其中一项存在，则父模块就要显示
        if(paymentDetailModule==1 || classicStatisticsView==1 || profitStatisticsView==1
                || profitStatisticsView==1 || fixCostModule==1 || financeTypeEdit==1){
            financeModule = 1;
        }
        return financeModule;
    }

    public void setFinanceModule(int financeModule) {
        this.financeModule = financeModule;
    }

    public int getPaymentDetailModule() {
        //paymentDetailModule模块包含以下三个子模块
        if(costDetailView==1 || paidStatisticsView==1 || payStatisticsView==1){
            paymentDetailModule = 1;
        }
        return paymentDetailModule;
    }

    public void setPaymentDetailModule(int paymentDetailModule) {
        this.paymentDetailModule = paymentDetailModule;
    }

    public int getCostDetailView() {
        return costDetailView;
    }

    public void setCostDetailView(int costDetailView) {
        this.costDetailView = costDetailView;
    }

    public int getPaidStatisticsView() {
        return paidStatisticsView;
    }

    public void setPaidStatisticsView(int paidStatisticsView) {
        this.paidStatisticsView = paidStatisticsView;
    }

    public int getPayStatisticsView() {
        return payStatisticsView;
    }

    public void setPayStatisticsView(int payStatisticsView) {
        this.payStatisticsView = payStatisticsView;
    }

    public int getClassicStatisticsView() {
        return classicStatisticsView;
    }

    public void setClassicStatisticsView(int classicStatisticsView) {
        this.classicStatisticsView = classicStatisticsView;
    }

    public int getProfitStatisticsView() {
        return profitStatisticsView;
    }

    public void setProfitStatisticsView(int profitStatisticsView) {
        this.profitStatisticsView = profitStatisticsView;
    }

    public int getFixCostModule() {
        return fixCostModule;
    }

    public void setFixCostModule(int fixCostModule) {
        this.fixCostModule = fixCostModule;
    }

    public int getFixCostEdit() {
        return fixCostEdit;
    }

    public void setFixCostEdit(int fixCostEdit) {
        this.fixCostEdit = fixCostEdit;
    }

    public int getFinanceTypeEdit() {
        return financeTypeEdit;
    }

    public void setFinanceTypeEdit(int financeTypeEdit) {
        this.financeTypeEdit = financeTypeEdit;
    }

    public int getAuditModule() {
        //只要其中一个子项模块为1，父模块就要显示
        if(expStatisticsView==1 || costStatisticsView==1 || leaveStatisticsView==1
                || onBusinessStatisticsView==1 || laborHourStatisticsView ==1){
            auditModule = 1;
        }
        return auditModule;
    }

    public void setAuditModule(int auditModule) {
        this.auditModule = auditModule;
    }

    public int getExpStatisticsView() {
        return expStatisticsView;
    }

    public void setExpStatisticsView(int expStatisticsView) {
        this.expStatisticsView = expStatisticsView;
    }

    public int getCostStatisticsView() {
        return costStatisticsView;
    }

    public void setCostStatisticsView(int costStatisticsView) {
        this.costStatisticsView = costStatisticsView;
    }

    public int getLeaveStatisticsView() {
        return leaveStatisticsView;
    }

    public void setLeaveStatisticsView(int leaveStatisticsView) {
        this.leaveStatisticsView = leaveStatisticsView;
    }

    public int getOnBusinessStatisticsView() {
        return onBusinessStatisticsView;
    }

    public void setOnBusinessStatisticsView(int onBusinessStatisticsView) {
        this.onBusinessStatisticsView = onBusinessStatisticsView;
    }

    public int getLaborHourStatisticsView() {
        return laborHourStatisticsView;
    }

    public void setLaborHourStatisticsView(int laborHourStatisticsView) {
        this.laborHourStatisticsView = laborHourStatisticsView;
    }

    public int getExpAllocate() {
        return expAllocate;
    }

    public void setExpAllocate(int expAllocate) {
        this.expAllocate = expAllocate;
    }

    public int getCostAllocate() {
        return costAllocate;
    }

    public void setCostAllocate(int costAllocate) {
        this.costAllocate = costAllocate;
    }

    public int getCompanyAuth() {
        return companyAuth;
    }

    public void setCompanyAuth(int companyAuth) {
        this.companyAuth = companyAuth;
    }

    public int getCompanyView() {
        return companyView;
    }

    public void setCompanyView(int companyView) {
        this.companyView = companyView;
    }

    public int getProjectImport() {
        return projectImport;
    }

    public void setProjectImport(int projectImport) {
        this.projectImport = projectImport;
    }

    public int getContractReceive() {
        return contractReceive;
    }

    public void setContractReceive(int contractReceive) {
        this.contractReceive = contractReceive;
    }

    public int getTechnicalReceive() {
        return technicalReceive;
    }

    public void setTechnicalReceive(int technicalReceive) {
        this.technicalReceive = technicalReceive;
    }

    public int getCooperationReceive() {
        return cooperationReceive;
    }

    public void setCooperationReceive(int cooperationReceive) {
        this.cooperationReceive = cooperationReceive;
    }

    public int getOtherReceive() {
        return otherReceive;
    }

    public void setOtherReceive(int otherReceive) {
        this.otherReceive = otherReceive;
    }

    public int getTechnicalPay() {
        return technicalPay;
    }

    public void setTechnicalPay(int technicalPay) {
        this.technicalPay = technicalPay;
    }

    public int getCooperationPay() {
        return cooperationPay;
    }

    public void setCooperationPay(int cooperationPay) {
        this.cooperationPay = cooperationPay;
    }

    public int getOtherPay() {
        return otherPay;
    }

    public void setOtherPay(int otherPay) {
        this.otherPay = otherPay;
    }

    public int getExpCategorySet() {
        return expCategorySet;
    }

    public void setExpCategorySet(int expCategorySet) {
        this.expCategorySet = expCategorySet;
    }

    public int getBaseFinanceDataSet() {
        return baseFinanceDataSet;
    }

    public void setBaseFinanceDataSet(int baseFinanceDataSet) {
        this.baseFinanceDataSet = baseFinanceDataSet;
    }

    public int getShareCostSet() {
        return shareCostSet;
    }

    public void setShareCostSet(int shareCostSet) {
        this.shareCostSet = shareCostSet;
    }

    public void init(){
        getAuditModule();
        getPaymentDetailModule();
        getFinanceModule();
        getPartnerModule();
        getSubOrgModule();
        getCompanyUserModule();
        getDepartModule();
        getRoleModule();
        getCompanyModule();
        getOrganizationModule();
        getBackGroundManager();
    }
}
