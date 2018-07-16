package com.maoding.core.constant;

public class PermissionConst {

    //10000101	解散组织
    //10000201	组织信息查询
    //10000202	组织信息编辑
    //10000501	企业认证
    public final static String COMPANY_EDIT = "10000202";
    public final static String COMPANY_DELETE = "10000101";
    public final static String COMPANY_VIEW = "10000201";
    public final static String COMPANY_AUTHENTICATION = "10000501";

    //10000301	成员信息查询
    //10000302	成员信息编辑
    //10000303	成员删除
    //10000304	成员新增
    //10000305	批量导入成员
    //组织成员
    public final static String COMPANY_USER_CREATE = "10000304";
    public final static String COMPANY_USER_EDIT = "10000302";
    public final static String COMPANY_USER_DELETE = "10000303";
    public final static String COMPANY_USER_INVITE = "org_partner";
    public final static String COMPANY_USER_VIEW = "10000301";
    public final static String COMPANY_USER_IMPORT = "10000305";

    // 10000306	部门查询
    //10000307	部门新增
    //10000308	部门编辑
    //10000309	部门删除
    //部门管理
    public final static String DEPART_CREATE = "10000307";
    public final static String DEPART_EDIT = "10000308";
    public final static String DEPART_DELETE = "10000309";
    public final static String DEPART_VIEW = "10000306";

    //10000401	角色新增
    //10000402	角色编辑
    //10000403	角色权限配置
    //10000404	角色成员配置
    //10000405	删除角色
    //10000406	查看角色
    public final static String ROLE_CREATE = "10000401";
    public final static String ROLE_EDIT = "10000402";
    public final static String ROLE_DELETE = "10000405";
    public final static String ROLE_VIEW = "10000406";
    public final static String ROLE_USER_CONFIG = "10000404";
    public final static String ROLE_PERMISSION_CONFIG = "10000403";

    //10000701	创建分公司
    //10000702	编辑分公司信息
    //10000703	分公司信息查询
    //10000704	解散分公司关系
    //10000705	邀请分公司
    //分公司
    public final static String SUB_COMPANY_CREATE = "10000701";
    public final static String SUB_COMPANY_EDIT = "10000702";
    public final static String SUB_COMPANY_DELETE = "10000704";
    public final static String SUB_COMPANY_INVITE = "10000705";
    public final static String SUB_COMPANY_VIEW = "10000703";

    //10000706	创建事业合伙人
    //10000707	查询事业合伙人
    //10000708	编辑事业合伙人
    //10000709	邀请事业合伙人
    //10000710	解除事业合伙人关系
    //事业合伙人
    public final static String ORG_PARTNER_CREATE = "10000706";
    public final static String ORG_PARTNER_EDIT = "10000708";
    public final static String ORG_PARTNER_DELETE = "10000710";
    public final static String ORG_PARTNER_INVITE = "10000709";
    public final static String ORG_PARTNER_VIEW = "10000707";

    //20000101	项目立项
    //20000201	删除项目
    //10000601	历史数据导入
    //20000601	编辑项目基本信息
    //20000501	邀请外部合作
    public final static String PROJECT_IMPORT = "10000601";
    public final static String PROJECT_CREATE = "20000101";
    public final static String PROJECT_EDIT = "20000601";
    public final static String PROJECT_DELETE = "20000201";
    public final static String PROJECT_INVITE_OUTER = "20000501";
    //20000701	查看项目总览
    public final static String PROJECT_ALL_VIEW = "20000701";
    //20000301	指定经营负责人
    //20000401	指定设计负责人
    public final static String PROJECT_OPERATOR = "20000301";
    public final static String PROJECT_DESIGNER = "20000401";
    //20000801	查看任务签发
    //20000901	查看任务签发总览
    //20001001	查看生产安排
    //20001101	查看生产安排总览
    //20001201	查看收支管理
    public final static String PROJECT_ISSUE_VIEW = "20000801";
    public final static String PROJECT_ISSUE_ALL_VIEW = "20000901";
    public final static String PROJECT_PRODUCT_VIEW = "20001001";
    public final static String PROJECT_PRODUCT_ALL_VIEW = "20001101";
    public final static String PROJECT_COST_VIEW = "20001201";

    //20001301	创建文件夹及文件
    //20001302	更新文件夹及文件
    //20001303	删除文件及文件夹
    //20001304	归档
    //20001401	查看项目文档

    public final static String PROJECT_DOC_CREATE = "20001301";
    public final static String PROJECT_DOC_EDIT = "20001302";
    public final static String PROJECT_DOC_DELETE = "20001303";
    public final static String PROJECT_DOC_PIGEONHOLE = "20001304";
    public final static String PROJECT_DOC_VIEW = "20001401";

    //30000101	查看报销统计
    //30000201	查看费用统计
    //30000301	查看请假统计
    //30000401	查看出差统计
    //30000501	查看工时统计
    public final static String EXP_STATISTICS_VIEW = "30000101";
    public final static String COST_STATISTICS_VIEW = "30000201";
    public final static String LEAVE_STATISTICS_VIEW = "30000301";
    public final static String ON_BUSINESS_STATISTICS_VIEW = "30000401";
    public final static String LABOR_HOUR_STATISTICS_VIEW = "30000501";


    //40000101	合同回款到账确认
    //40000201	技术审查费付款确认
    //40000301	技术审查费到账确认
    //40000401	合作设计费付款确认
    //40000501	合作设计费到账确认
    //40000601	其他收支付款确认
    //40000701	其他收支到账确认
    public final static String CONTRACT_RECEIVE = "40000101";
    public final static String TECHNICAL_RECEIVE = "40000301";
    public final static String COOPERATION_RECEIVE = "40000501";
    public final static String OTHER_RECEIVE = "40000701";

    public final static String TECHNICAL_PAY = "40000201";
    public final static String COOPERATION_PAY = "40000401";
    public final static String OTHER_PAY = "40000601";

    //40000801	报销拨款
    //40000901	费用拨款
    public final static String EXP_ALLOCATE = "40000801";
    public final static String COST_ALLOCATE = "40000901";
    //40001001	费用录入
    //40001101	查看费用录入
    public final static String FIX_COST_VIEW = "40001101";
    public final static String FIX_COST_EDIT = "40001001";
    //40001501  报销/费用财务设置
    public final static String FINANCE_TYPE_EDIT = "40001501";
    //40001201	查看台账
    //40001202	查看应收
    //40001203	查看应付
    //40001301	查看分类统计
    //40001401	查看利润报表
    public final static String COST_DETAIL_VIEW = "40001201";
    public final static String PAID_STATISTICS_VIEW = "40001202";
    public final static String PAY_STATISTICS_VIEW = "40001203";
    public final static String CLASSIC_STATISTICS_VIEW = "40001301";
    public final static String PROFIT_STATISTICS_VIEW = "40001401";
    //50000101	发布公告
    //50000102	编辑公告
    //50000103	删除公告
    //50000104	查看公告
    public final static String NOTICE_CREATE = "50000101";
    public final static String NOTICE_EDIT = "50000102";
    public final static String NOTICE_DELETE = "50000103";
    public final static String NOTICE_VIEW = "50000104";

    public final static String SYS_ROLE_PERMISSION = "10000403";


}
