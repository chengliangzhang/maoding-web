/**
 *
 */
package com.maoding.core.constant;

import com.maoding.core.bean.MessageTemplate;

import java.util.*;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：SystemParameters
 * 类描述：
 * 作    者：MaoSF
 * 日    期：2015年7月15日-上午10:43:55
 */

public interface SystemParameters {

    int EXCEL_MAX_ROW = 65535;
    Locale DEFAULT_LOCALE = Locale.getDefault();
    String FILE_SEPARATOR = System.getProperty("file.separator");
    String FTP_FILE_SEPARATOR = "/";
    String UTF8 = "UTF-8";
    /**
     * 验证码时效（10分钟）
     */
    long SECURITY_CODE_10_MAX_LIVE_TIME = 600000;

    /******************系统返回状态码*****************/

    /**
     * 成功状态
     */
    String SUCCESS_CODE = "0";

    /**
     * session超时状态
     */
    String SESSION_TIMEOUT_CODE = "401";

    /**
     * 异常状态码
     */
    String EXCEPTION_CODE = "500";

    /**
     * 设计依据编码
     */
    String PRO_DESIGNBASIC = "designBasic";
    /**
     * 设计范围编码
     */
    String PRO_DESIGNRANGE = "designRange";
    /**
     * 设计阶段
     */
    String PRO_DESIGNCONTENT = "designContent";

    /**
     * 建筑功能
     */
    String PRO_CONSTRUCTFUNCTION = "zp-jzgn";
    /**
     * 项目类别
     */
    String PRO_Type = "project-type";

    /**
     * 专业
     */
    String USER_MAJOR = "zy";


    /**
     * 请假
     */
    String LEAVE = "leave";

    /**
     * 任务管理
     */
    String TASK_MANAGER = "taskManager";


    //在导入历史数据时，模板内没有项目类型，默认为建筑设计
    String DEFAULT_PROJECT_TYPE_NAME = "建筑设计";


    /**
     * 系统管理员角色id
     */
    String ADMIN_MANAGER_ROLE_ID = "2f84f20610314637a8d5113440c69bde";

    public final String ORG_MANAGER_ROLE_ID = "23297de920f34785b7ad7f9f6f5fe9d1";


    /**----------------路径---------------------**/
    /**
     * 富文本路径
     */
    String PATHPREFIX = "file/upload/";


    /**
     * 发送验证码短信模板
     */
    String SEND_CODE_MSG = "【卯 丁】你的短信验证码为：?，将于10分钟后失效。";

    /**
     * 添加团队人员（用户账号不存在）
     */
    String ADD_COMPANY_USER_MSG_1 = "【卯 丁】?邀请你加入\"?\"，请访问 ? 注册使用。";

    /**
     * 添加团队人员（用户账号已经存在）
     */
    String ADD_COMPANY_USER_MSG_2 = "【卯 丁】?邀请你加入\"?\"。";

    /**
     * 分享邀请，团队同意加入，短信提示,（用户账号不存在）
     */
    String SHARE_INVITE_MSG_1 = "【卯 丁】\"?\"已审核通过你的加入申请，请访问 ? 注册使用。";

    /**
     * 分享邀请，团队同意加入，短信提示,用户账号已经存在）
     */
    String SHARE_INVITE_MSG_2 = "【卯 丁】\"?\"已审核通过你的加入申请。";

    /**
     * 移交管理员，给新管理员发送短信模板
     */
    String TRANSFER_ADMIN_MSG = "【卯 丁】你已成为\"?\"的管理员。";

    /**
     * 移交、设置企业负责人，给新管理员发送短信模板
     */
    String TRANSFER_ORG_MSG = "【卯 丁】你已被取消“?”企业负责人相关权限。如有疑问请联系?。";

    /**
     * 创建分支机构，合作伙伴，管理员没有账号的短信信息
     */
    // String CREATE_SUB_COMPANY_MSG_1 = "【卯 丁】?邀请你成为\"?\"-\"?\"的管理员，卯丁帐号：?，密码：?，团队管理密码：?。请访问 ? 注册使用。";

    String CREATE_SUB_COMPANY_MSG_1 = "【卯 丁】?邀请你成为\"?\"-\"?\"的管理员，卯丁帐号：?，密码：?。请访问 ? 注册使用。";

    /**
     * 创建分支机构，合作伙伴，管理员有账号的短信信息
     */
    // String CREATE_SUB_COMPANY_MSG_2 = "【卯 丁】?邀请你成为\"?\"-\"?\"的管理员，团队管理密码：?。";

    String CREATE_SUB_COMPANY_MSG_2 = "【卯 丁】?邀请你成为\"?\"-\"?\"的管理员。";

    /**
     * 邀请分支机构，给邀请的手机号码发送短信
     */
    String INVITE_PARENT_MSG = "【卯 丁】?邀请你的组织成为“?”的分支机构，请点击 ? 进行操作。";
    /**
     * 邀请事业合伙人，给邀请的手机号码发送短信
     */
    String INVITE_PARENT_MSG2 = "【卯 丁】?邀请你的组织成为“?”的事业合伙人，请点击 ? 进行操作。";
    /**
     * 邀请外部合作设计组织，给邀请的手机号码发送短信
     */
    String INVITE_PARENT_MSG3 = "【卯 丁】?邀请你的组织成为“?”的外部合作设计组织，请点击 ? 进行操作。";

    String[] DEPART_DEFAULT_IMG = {"common/img/depart/financial.png", "common/img/depart/market.png", "common/img/depart/operatingmanagement.png",
            "common/img/depart/product.png", "common/img/depart/programcreation.png", "common/img/depart/publicadministration.png", "common/img/depart/technology.png"};


    /*****************任务类型******************/
    int ISSUE_TASK = 1;//1.签发
    int PRODUCT_TASK_DESIGN = 2;//2.生产安排（技术负责人）
    int PRODUCT_TASK = 12;//12.生产安排
    int PRODUCT_TASK_RESPONSE = 13;//任务负责人
    int PROCESS_DESIGN = 3;//3.设计，校对，审核
    int TECHNICAL_REVIEW_FEE_OPERATOR_MANAGER = 4;//4.技术审查费付款确认（经营负责人）
    int TECHNICAL_REVIEW_FEE_ORG_MANAGER = 5;//5.技术审查费付款确认（企业负责人）
    int COOPERATIVE_DESIGN_FEE_ORG_MANAGER = 6;//6.付款（合作设计费-付款确认（经营负责人））
    int COOPERATIVE_DESIGN_FEE_OPERATOR_MANAGER = 7;//7.合作设计费付款确认（企业负责人）
    int TECHNICAL_REVIEW_FEE_PAYMENT_CONFIRM = 8;//8.技术审查费到账确认
    int COOPERATIVE_DESIGN_FEE_PAYMENT_CONFIRM = 9;//9.合作设计费费到账确认
    int CONTRACT_FEE_PAYMENT_CONFIRM = 10;//10.合同回款财务到账确认
    int EXP_AUDIT = 11;//11.报销审核
    int ARRANGE_TASK_DESIGN = 14;//安排设计负责人
    int ARRANGE_TASK_RESPONSIBLE = 15;//安排任务负责人
    int TECHNICAL_REVIEW_FEE_FOR_PAY = 16;//技术审查费付款（财务）
    int TECHNICAL_REVIEW_FEE_FOR_PAID = 17;//技术审查费到账（财务）

    int COOPERATIVE_DESIGN_FEE_FOR_PAY = 18;//合作设计费付款（财务）
    int COOPERATIVE_DESIGN_FEE_FOR_PAID = 19;//合作设计费到账（财务）

    //修改后的
    int TECHNICAL_REVIEW_FEE_FOR_PAY_2 = 30;//技术审查费付款（财务）
    int TECHNICAL_REVIEW_FEE_FOR_PAID_2 = 31;//技术审查费到账（财务）

    int COOPERATIVE_DESIGN_FEE_FOR_PAY_2 = 32;//合作设计费付款（财务）
    int COOPERATIVE_DESIGN_FEE_FOR_PAID_2 = 33;//合作设计费到账（财务）
    int INVOICE_FINN_IN_FOR_PAID = 29;//（财务填写发票信息）

    int OTHER_FEE_FOR_PAY = 20;//其他费付款（财务）
    int OTHER_FEE_FOR_PAID = 21;//其他费到账（财务）
    int TASK_COMPLETE = 22;//生产根任务已完成，给设计负责人推送任务
    int COST_AUDIT = 23;//费用申请审核
    int LEAVE_AUDIT= 24;//请假申请审核
    int EVECTION_AUDIT= 25;//出差审核
    int CUSTOM_TASK= 100;//12.自定义的轻量型任务
    /** 确认交付文件上传完毕 */
    int DELIVER_CONFIRM_FINISH = 26;
    /** 进行交付文件上传 */
    int DELIVER_EXECUTE = 27;


    //任务签发权限
    String PROJECT_TASK_ISSUE = "project_task_issue";

    //根节点
    List<String> rootList = new ArrayList<String>() {{
        add("设计依据");
        add("设计文件");
        add("交付文件");
    }};


    //交付文件的子节点
    //  List<String> nodeList1=new ArrayList<String>(){{add("项目总归档");}};
    List<String> nodeList1 = new ArrayList<String>();
    //设计依据的子节点
    List<String> nodeList2 = new ArrayList<String>() {{
        add("基础资料");
        add("政府批文");
        add("设计任务书");
        add("往来函件");
        add("会议纪要");
    }};

    /*****************消息推送类型**********************/
    String NOTICE_TYPE = "notice";
    String ROLE_TYPE = "role";
    String PROJECT_TYPE = "project";
    String USER_MESSAGE = "userMessage";
    String APP_MESSAGE="maoDingSecretary";
    String ORG_TYPE = "org";

    /*****************团队动态类型******************/
    int DYNAMIC_PROJECT = 1;//1.立项动态
    int DYNAMIC_PARTYB = 2;//1.乙方动态
    int DYNAMIC_PARTNER = 3;//合作方动态
    int DYNAMIC_NOTICE = 4;//3.通知公告动态


    /*****************团队动态模板******************/
    String sperater = "。";
    Map<String, String> dynamic = new HashMap<String, String>() {
        {
            put("1", "? 创建了项目：?" + sperater +
                            "设计内容包括：?"
                    //	+sperater+"经营负责人是：?、设计负责人是：?"
            );
            put("2", "我们成为了 ? 项目的乙方，立项方为 ?" + sperater +
                            "设计内容包括：?"
                    //	"我们的经营负责人是：?、项目负责人是：?"
            );
            put("3", "我们成为了 ? 项目的合作设计方，合作方为 ?" + sperater +
                            "我们负责的部分是?。"
                    //	+	"经营负责人是：?、设计负责人是：?"
            );
            put("4", "?");
        }
    };

    /*****************消息模板******************/

    Map<String, String> messageForWeb = new HashMap<String, String>() {
        {
            /** 参数对照 ***
             * %sendUserName%:发出通告的用户名
             * %sendCompanyName%:发出通告的公司名
             * %projectName%:项目名
             * %pointName%:款项节点名
             * %feeDescription%:费用节点描述
             * %costFee%:费用金额，如100，200
             * %paymentDate%:修改前的到账，付款日期等
             * %url%：url地址
             * %startTime1%：变更前的开始时间
             * %endTime1%：变更前的结束时间
             * %startTime2%：变更后的开始时间
             * %endTime2%：变更后的结束时间
             * %nodeName% ：节点名称：设校审
             * %toNodeName%：节点名称：校审（***完成设计（nodeName），请审核（toNodeName））
             * remarks ：备注
             *
             */

            //设置乙方
            put("1", "你成为了 “%projectName%” 乙方经营负责人");//ok
            put("2", "你成为了 “%projectName%” 乙方设计负责人");//ok
            //立项
            /**********************经营负责人**********************/
            //ok //你成为了 “?” 经营负责人 -- hi,XXX完成了“卯丁科技大厦一期”的立项，请你担任该项目的经营负责人，请在任务中查看并进行［任务签发］，谢谢！
            //许佳迪   变更了“卯丁科技大厦一期”的经营负责人，请你担任该项目的经营负责人，请在任务中查看并进行［任务签发］及相关工作。
            put("3", "%sendUserName% 变更了“%projectName%”的经营负责人，请你担任该项目的经营负责人，请在任务中查看并进行<a href=\"%url%\">［任务签发］</a>及相关工作。");

            //立项方
            //许佳迪   创建了“卯丁科技大厦一期”的项目，请你担任该项目的经营负责人，请在任务中查看并进行［任务签发］。
            put(String.format("%d", MESSAGE_TYPE_301), "%sendUserName% 创建了%projectName%的项目，请你担任该项目的经营负责人，请在任务中查看并进行<a href=\"%url%\">［任务签发］</a>。");
            //许佳迪   增加了“卯丁科技大厦一期”的设计任务，请在任务中查看并进行［任务签发］。
            put(String.format("%d", MESSAGE_TYPE_302), "%sendUserName% 增加了“%projectName%”的设计任务，请在任务中查看并进行<a href=\"%url%\">［任务签发］</a>。");
            /**合作方**/
            //许佳迪   发布了“卯丁科技大厦一期”的设计任务，请在任务中查看并进行［任务签发］。
            put(String.format("%d", MESSAGE_TYPE_303), "%sendUserName% 发布了“%projectName%”的设计任务，请你担任该项目的经营负责人,请在任务中查看并进行<a href=\"%url%\">［任务签发］</a>。");//ok

            put(String.format("%d", MESSAGE_TYPE_306), "%sendUserName% 发布了“%projectName%”的设计任务，请在任务中查看并进行<a href=\"%url%\">［任务签发］</a>。");//ok

            //许佳迪，发起了“卯丁科技大厦：定金支付10%。”的合作设计费收款16.88万元，请你跟进并确认付款。
            put(String.format("%d", MESSAGE_TYPE_307), "%sendUserName% 发起了“%projectName%：%feeDescription%”的合作设计费收款%fee%万元，请你跟进并确认付款。");//ok

            //hi，“卯丁科技大厦一期-方案设计”的设计任务已完成，请你跟进相关项目收支的经营工作，谢谢！
            //许佳迪，提交了“卯丁科技大厦一期-方案设计”的设计任务，请你跟进相关项目收支的经营工作，谢谢！
            put(String.format("%d", MESSAGE_TYPE_304), "%sendUserName%，提交了“%projectName%-%taskName%”的设计任务，请你跟进相关项目收支的经营工作，谢谢！");
            //经营负责人指定了助理
            //许佳迪   指定了   你为“卯丁科技大厦一期”的经营助理，请在任务中查看并进行［任务签发］及相关工作。
            put(String.format("%d", MESSAGE_TYPE_305), "%sendUserName% 指定了你为 “%projectName%”的经营助理，请在任务中查看并进行<a href=\"%url%\">［任务签发］</a> 及相关工作");

            put("4", "你成为了 “%projectName%” 设计负责人");//ok
            /******************** 任务负责人************************/
            // hi,经营负责人XXX发布了“卯丁科技大厦一期”的设计任务，请你担任该项目的设计负责人，请在任务中查看并进行［生产安排］，谢谢！
            //许佳迪   发布了“卯丁科技大厦一期”的设计任务，请你担任该项目的设计负责人，请在任务中查看并进行［生产安排］。
            put(String.format("%d", MESSAGE_TYPE_401), "%sendUserName% 发布了“%projectName%”的设计任务，请你担任该项目的设计负责人，请在任务中查看并进行<a href=\"%url%\">［生产安排］</a>。");
            // hi,经营负责人XXX发布了“卯丁科技大厦一期”的设计任务，请在任务中查看并进行［生产安排］，谢谢！
            put(String.format("%d", MESSAGE_TYPE_402), "%sendUserName% 发布了“%projectName%”的设计任务，请在任务中查看并进行<a href=\"%url%\">［生产安排］</a>。");
            //hi，设计负责人XXX进行了“卯丁科技大厦一期-建筑设计”的生产安排，你被设定为任务负责人，详情点击［我的任务］查看，谢谢！
            //许佳迪，新增了“卯丁科技大厦一期-建筑设计”的设计任务，你被设定为任务负责人，详情点击［我的任务］查看。
            put(String.format("%d", MESSAGE_TYPE_403), "%sendUserName%，指定了 你为“%projectName%-%taskName%”任务负责人，详情点击<a href=\"%url%\">［我的任务］</a>查看。");
            //hi，任务负责人XXX进行了“卯丁科技大厦一期-建筑设计”的生产安排，你被设定为任务负责人，详情点击［我的任务］查看，谢谢！
            put(String.format("%d", MESSAGE_TYPE_404), "%sendUserName%，指定了 你为“%projectName%-%taskName%”任务负责人，详情点击<a href=\"%url%\">［我的任务］</a>查看。");
            //hi，“卯丁科技大厦一期-方案设计”的设计/校对/审核任务已完成，请你确认，谢谢！
            put(String.format("%d", MESSAGE_TYPE_405), "hi，“%projectName%-%taskName%”的%nodeName%任务已完成，请你确认，谢谢！");
            //hi，“卯丁科技大厦一期-方案设计”的设计任务已完成，请你确认，谢谢！(设计任务完成（任务负责人），每一条完成，都需要往上一级任务负责人推消息)
            //许佳迪，提交了“卯丁科技大厦一期-方案设计”的设计任务，请你确认。
            put(String.format("%d", MESSAGE_TYPE_406), "%sendUserName%，提交了“%projectName%-%taskName%”的设计任务，请你确认。");
            /*************************设计负责人*********/
            //所有的生产任务已经完成（仅自己生产的），给本组织的设计负责人推送消息：hi，“卯丁科技大厦一期-方案设计....”的设计任务已完成，请你确认，谢谢！
            //许佳迪，提交了“卯丁科技大厦一期-方案设计”的设计任务，现“卯丁科技大厦一期”整体工作已提交请你确认工作。
            put(String.format("%d", MESSAGE_TYPE_407), "%sendUserName%，提交了“%projectName%-%taskName%”的设计任务，请你跟进相关设计工作。");
            //本团队所有的生产任务已经完成（包含签发给其他组织的任务）：hi，“卯丁科技大厦一期-方案设计，初步设计....”所有生产任务已完成，请你确认，谢谢！
            put(String.format("%d", MESSAGE_TYPE_408), "%sendUserName%，提交了“%projectName%-%taskName%”，现“%projectName%”整体工作已提交请你确认工作。");//此处还应该推送任务，任务类型22
            //合作方 A 给 B的任务全部完成，给A组织的设计负责人推送消息
            put(String.format("%d", MESSAGE_TYPE_409), "%sendUserName%，“%projectName%-%taskName%”的设计任务已完成，请你跟进相关项目收支的经营工作，谢谢！");
            //许佳迪   指定了   你为“卯丁科技大厦一期”的经营助理，请在任务中查看并进行［任务签发］及相关工作。
            put(String.format("%d", MESSAGE_TYPE_410), "%sendUserName% 指定了你为 “%projectName%”的设计助理，请在任务中查看并进行<a href=\"%url%\">［生产安排］</a> 及相关工作");

            /****************时间变动消息推送**************/
            //hi，任务负责人/设计负责人XXX变更了“卯丁科技大厦一期-方案设计”的计划进度〖由2017-05-09至2017-09-09变更为2017-06-09至2017-10-09），请查看并作相应调整，谢谢！
            //许佳迪，变更了“卯丁科技大厦一期-方案设计”的计划进度〖由2017-05-09至2017-09-09变更为2017-06-09至2017-10-09），请查看并作相应调整。
            put(String.format("%d", MESSAGE_TYPE_601), "%sendUserName%，变更了“%projectName%-%taskName%”的计划进度【由%startTime1%至%endTime1%变更为%startTime2%至%endTime2%】，请查看并作相应调整。");
            put(String.format("%d", MESSAGE_TYPE_602), "%sendUserName%，变更了“%projectName%-%taskName%”的计划进度【由%startTime1%至%endTime1%变更为%startTime2%至%endTime2%】，请查看并作相应调整。");
            put(String.format("%d", MESSAGE_TYPE_603), "%sendUserName%，变更了“%projectName%-%taskName%”的计划进度【由%startTime1%至%endTime1%变更为%startTime2%至%endTime2%】，请查看并作相应调整。");
            put(String.format("%d", MESSAGE_TYPE_604), "%sendUserName%，变更了“%projectName%-%taskName%”的计划进度【由%startTime1%至%endTime1%变更为%startTime2%至%endTime2%】，请查看并作相应调整。");
            //任务签发
            put("5", "你成为了“%projectName%-%taskName%”的经营负责人");//ok

            /******************设校审人员*******************/
            //hi，设计负责人XXX进行了“卯丁科技大厦一期-建筑设计”的生产安排，你将参与该任务的（设计、校对、审核）xx工作，详情请点击［我的任务］查看，谢谢！
            //许佳迪，新增了“卯丁科技大厦一期-建筑设计”的生产安排，你将参与该任务的（设计、校对、审核）xx工作，详情请点击［我的任务］查看。
            put("501", "%sendUserName%，指定了 你为“%projectName%-%taskName%”的生产安排，你将参与该任务的%nodeName%工作，详情请点击<a href=\"%url%\">［我的任务］</a>查看。");//ok
            put("502", "%sendUserName%，指定了 你为“%projectName%-%taskName%”的生产安排，你将参与该任务的%nodeName%工作，详情请点击<a href=\"%url%\">［我的任务］</a>查看。");//ok
            //hi，XXX完成了“卯丁科技大厦一期-方案设计”的设计工作，请你校对，谢谢！
            //hi，XXX完成了“卯丁科技大厦一期-方案设计”的校对工作，请你审核，谢谢！
            put("503", "hi，%sendUserName%完成了“%projectName%-%taskName%”的%nodeName%工作，请你%toNodeName%，谢谢！");//ok


            /***************************************************/
            put("6", "你成为了“%projectName%-%taskName%”的设计负责人");//ok
            //设置任务负责人
            put("7", "你成为了“%projectName%-%taskName%”的任务负责人");//ok

            //“卯丁科技大厦 - 方案设计 - 方案C”设计任务的校对人是：许佳迪、小练，审核人是：XX，XXX
            put("8", "“%projectName%-%taskName%”设计任务的%nodeName%");//乙方项目负责人,当设置审核人员后，给乙方负责人发送消息 //ok

            put("9", "“%projectName%-%taskName%”设计任务已全部完成");//ok
            put("10", "你成为了 “%projectName%-%taskName%” 的%nodeName%人"); //ok

            put("21", "“%projectName%-%taskName%”所有子任务已全部完成");//ok
            /**************报销***************/
            put("19", "%sendUserName%申请报销“%expName%”，共计%expAmount%元");//ok
            //?共计?元的报销申请不予批准---hi，XXX拒绝了你申请的“办公用品”报销金额200.00元
            put("20", "hi，%sendUserName%拒绝了你申请的“%expName%”报销，金额%expAmount%元");//ok
            //你申请“?”共计?元已审批通过 -- hi，XXX同意你“办公用品”的报销金额为2000元，谢谢！
            put("22", "hi，%sendUserName%同意你“%expName%”的报销，金额为%expAmount%元，谢谢！");//报销单审批完成后发给报销人送消息
            //hi，XXX转交了许佳迪申请的“办公用品”报销金额200.00元，请你审批，谢谢！
            put("221", "hi，%sendUserName%转交了%expUserName%申请的“%expName%”报销，金额%expAmount%元，请你审批，谢谢！");

            /**************费用申请***************/
            put("222", "%sendUserName%申请费用“%expName%”，共计%expAmount%元");//ok
            //?共计?元的报销申请不予批准---hi，XXX拒绝了你申请的“办公用品”报销金额200.00元
            put("223", "hi，%sendUserName%拒绝了你申请的“%expName%”费用，金额%expAmount%元");//ok
            //你申请“?”共计?元已审批通过 -- hi，XXX同意你“办公用品”的报销金额为2000元，谢谢！
            put("224", "hi，%sendUserName%同意你“%expName%”的费用，金额为%expAmount%元，谢谢！");//报销单审批完成后发给报销人送消息
            put("225", "hi，%sendUserName%转交了%expUserName%申请的“%expName%”费用，金额%expAmount%元，请你审批，谢谢！");

            /***********请假部分***********/
            put("226","%sendUserName% 提交了请假申请，请假类型：%leaveTypeName%，请假时间：%startTime1% - %endTime1%，请您审批。");//ok
            put("227","%sendUserName% 拒绝了你的请假申请，请假类型：%leaveTypeName%，请假时间：%startTime1% - %endTime1%，退回原因：%reason%。");//ok
            //您提交的请假申请，请假类型：事假，请假时间：2017/12/26 09:00-2017/12/27 18:00，已完成审批。
            put("228","你提交的请假申请，请假类型：%leaveTypeName%，请假时间：%startTime1% - %endTime1%，已完成审批。");
            put("229","%sendUserName%同意并转交了%expUserName%的请假申请，请假类型：%leaveTypeName%，请假时间：%startTime1% - %endTime1%给你，请你审批。");
            /***********出差部分***********/
            put("230","%sendUserName% 提交了出差申请，出差地：%address%，出差时间：%startTime1% - %endTime1%，请您审批。");
            put("231","%sendUserName% 拒绝了你的出差申请，出差地：%address%，出差时间：%startTime1% - %endTime1%，退回原因：%reason%。");//ok
            put("232","你提交的出差申请，出差地：%address%，出差时间：%startTime1% - %endTime1%，已完成审批。");
            put("233","%sendUserName%同意并转交了%expUserName%的出差申请，出差地：%address%，出差时间：%startTime1% - %endTime1%，%sendUserName%给你，请你审批。");

            //许佳迪，提交的付款申请“卯丁科技大厦：定金支付10%。”的技术审查费付款16.88万元，请你及时审批
            put("243","%sendUserName% 提交的付款申请“%projectName%：%feeDescription%。”的%expName%付款%fee%万元，请你及时审批。");
            put("244","你提交的付款申请“%projectName%：%feeDescription%。”的%expName%付款%fee%万元，已完成审批。");
            put("245","%sendUserName% 拒绝了你的付款申请“%projectName%：%feeDescription%。”的%expName%付款%fee%万元，退回原因：%reason%。");//ok
            put("246","%sendUserName%同意了%expUserName%的付款申请“%projectName%：%feeDescription%。”的%expName%付款%fee%万元给你，请你审批。");

            //财务拨款（报销，费用）
            put("234","你申请的报销“%expName%”共计%expAmount%元，财务已拨款。");
            put("235","你申请的费用“%expName%”共计%expAmount%元，财务已拨款。");

            put("236","%expUserName% 提交的报销申请，“%expName%”，共计%expAmount%元，财务已拨款，请知晓。");//ok
            put("237","%expUserName% 提交的费用申请，“%expName%”，共计%expAmount%元，财务已拨款，请知晓。");//ok
            put("238","%expUserName% 提交的请假申请，请假类型：%leaveTypeName%，请假时间：%startTime1% - %endTime1%，已完成审批，请知晓");//ok
            put("239","%expUserName% 提交的出差申请，出差地：%address%，出差时间：%startTime1% - %endTime1%，已完成审批，请知晓");

            /***********自定义报销部分***********/
            put("249","%expUserName% 提交了“%formName%”的审批，请您审批。");// XXX 提交了，“XXX”的审批，请您审批。
            put("250","%sendUserName% 拒绝了你的“%formName%”的审批申请，退回原因：%reason%。");// XXX 拒绝了你的 XXX 的审批申请，退回原因：XXXX。
            put("251","你提交“%formName%”的审批，已完成审批。");//  你提交了 XXX 的审批，已完成审批。
            put("252","%sendUserName%同意并转交了%expUserName%的“%formName%”审批申请，请你审批。");//  XXX 同意并转交 X某的XXX审批申请，请你审批。
            put("253","%expUserName% 你申请的“%formName%”审批,共计%expAmount%元，财务已拨款，请知晓。");//  XXX 你申请的 XXX审批申请，共计XX元，财务已拨款，请知晓。"
            put("254","%expUserName% 你申请的“%formName%”审批,共计%expAmount%元，审批未通过，原因：%reason%。");//  XXX 你申请的 XXX审批申请，共计XX元，审批未通过，原因：XX"

            //财务审核不通过（报销，费用）
            put("247","%expUserName% 你申请的报销,“%expName%”,共计%expAmount%元，审批未通过，原因：%reason%。");
            put("248","%expUserName% 你申请的费用,“%expName%”,共计%expAmount%元，审批未通过，原因：%reason%。");
            /******************项目费用******************/
            //技术审查费
            //? - 技术审查费 - ?” 金额：?万，需要你确认付款 ----->hi，XXX发起了“卯丁科技大厦一期：技术审查费节点”的技术审查费30万元，请确认付款金额，谢谢！
            //许佳迪，发起了“卯丁科技大厦一期：技术审查费节点”的技术审查费30万元，请确认付款金额。
            put("11", "%sendUserName%，发起了“%projectName%：%feeDescription%”的技术审查费%fee%万元，请确认付款金额。");
            //许佳迪，发起了 “卯丁科技大厦一期：技术审查费节点”的技术审查费付款金额为30万元，请你跟进并确认实际付款日期。
            put("12", "“%sendUserName%，发起了“%projectName%：%feeDescription%” 的技术审查费付款金额为%fee%万，请你跟进并确认实际付款日期。"); //
            //许佳迪，确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费付款金额为30万元，请你跟进并确认实际到账日期。
            put("13", "“%sendUserName%，确认了“%projectName%：%feeDescription%” 的技术审查费付款金额为%fee%万，请你跟进并确认实际到账日期。");

            //合作设计费
            //? - 合作设计费 - ?” 金额：?万，需要你确认付款 -->hi，XXX发起了“卯丁科技大厦一期：合作设计费节点”的合作设计费30万元，请确认付款金额，谢谢！
            //许佳迪，发起了“卯丁科技大厦一期：合作设计费节点”的合作设计费30万元，请确认付款金额。
            put("14", "%sendUserName%，发起了“%projectName%：%feeDescription%”的合作设计费%fee%万元，请确认付款金额。");
            //许佳迪，确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费付款金额为30万元，请你跟进并确认实际付款日期。
            put("15", "%sendUserName%，确认了“%projectName%：%feeDescription%” 的合作设计费付款金额为%fee%万，请你跟进并确认实际付款日期。");
            //许佳迪，确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费付款金额为30万元，请你跟进并确认实际到账日期。
            put("16", "%sendUserName%，确认了“%projectName%：%feeDescription%” 的合作设计费付款金额为%fee%万，请你跟进并确认实际到账日期。");

            //财务发票确认
            put("100", "%sendUserName%，发起了“%projectName%：%feeDescription%”的合同回款%fee%万元，发票方为：%toCompanyName%，请你跟进确认发票号码及发票类型。");//财务发票确认
            put("101", "%sendUserName%，发起了“%projectName%：%feeDescription%”的技术审查费%fee%万元，发票方为：%toCompanyName%，请你跟进确认发票号码及发票类型。");//财务发票确认
            put("102", "%sendUserName%，发起了“%projectName%：%feeDescription%”的合作设计费%fee%万元，发票方为：%toCompanyName%，请你跟进确认发票号码及发票类型。");//财务发票确认
            put("103", "%sendUserName%，发起了“%projectName%：%feeDescription%”的其他收入%fee%万元，发票方为：%toCompanyName%，请你跟进确认发票号码及发票类型。");//财务发票确认
            //合同回款
            //? - 合同回款 - ?” 金额：?万，开始收款了 -->hi，XXX发起了“卯丁科技大厦一期：回款节点”的合同回款30万元，请你跟进并确认实际到帐金额和日期，谢谢！
            //许佳迪，发起了“卯丁科技大厦一期：方案完成后7日内支付10%”的合同回款30万元，请你跟进并确认实际到帐金额和日期。
            put("17", "%sendUserName%，发起了“%projectName%：%feeDescription%”的合同回款%fee%万元，请你跟进并确认实际到帐金额和日期。");//合同回款--通知财务收款

            //? - 合同回款 - ?” 金额：?万，已确认到账，到账日期为? --->hi，XXX确认了“卯丁科技大厦一期：回款节点”的实际到金额为30万元，到账日期为xxxx-xx-xx，谢谢！
            //许佳迪， 确认了“卯丁科技大厦一期：回款节点”的实际到金额为30万元，到账日期为xxxx-xx-xx。
            put("18", "%sendUserName%，确认了“%projectName%：%feeDescription%”的实际到金额为%fee%万元，到账日期为%paymentDate%。");//合同回款到账，通知经营负责人，企业负责人


            //? - 技术审查费 - ?” 金额：?万，已付款，请进行相关操作 -->hi，XXX确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费付款金额为30万元，请你跟进并确认实际付款日期，谢谢！
            //许佳迪，发起了 “卯丁科技大厦一期：技术审查费节点”的技术审查费付款金额为30万元，请你跟进并确认实际付款日期。
            put("23", "%sendUserName%，发起了“%projectName%：%feeDescription%”的技术审查费付款金额为%fee%万元，请你跟进并确认实际付款日期。");//技术审查费财务人员付款操作

            //? - 技术审查费 - ?” 金额：?万，已到账，请进行相关操作-->hi，XXX确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费付款金额为30万元，请你跟进并确认实际到账日期，谢谢！
            //许佳迪，确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费付款金额为30万元，请你跟进并确认实际到账日期。
            put("24", "%sendUserName%，确认了“%projectName%：%feeDescription%”的技术审查费付款金额为%fee%万元，请你跟进并确认实际到账日期。");//技术审查费财务人员到账操作

            //“? - 合作设计费 - ?” 金额：?万，已付款，请进行相关操作-->hi，XXX确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费付款金额为30万元，请你跟进并确认实际付款日期，谢谢！
            put("25", "%sendUserName%，发起了“%projectName%：%feeDescription%”的合作设计费付款金额为%fee%万元，请你跟进并确认实际付款日期，谢谢！");//合作设计费财务人员付款操作
            //? - 合作设计费 - ?” 金额：?万，已到账，请进行相关操作-->hi，XXX确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费付款金额为30万元，请你跟进并确认实际到账日期，谢谢！
            put("26", "%sendUserName%，确认了“%projectName%：%feeDescription%”的合作设计费付款金额为%fee%万元，请你跟进并确认实际到账日期，谢谢！");//合作设计费财务人员到账操作

            //技术审查费收款-财务
            put("110", "%sendUserName%，确认了“%projectName%：%feeDescription%”的技术审查费收款金额为%fee%万，请你跟进并确认实际到帐金额和日期。");
            //技术审查费付款-财务-无申请
            put("111", "%sendUserName%，确认了“%projectName%：%feeDescription%”的技术审查费付款金额为%fee%万，请你跟进并确认实际到帐金额和日期。");
            //技术审查费付款-财务-有申请
            put("112", "%sendUserName%，确认了“%projectName%：%feeDescription%”的技术审查费付款金额为%fee%万，%auditPersonName% 已同意拨款，请你跟进并确认实际付款金额和日期。");

            //合作设计费收款-财务
            put("113", "%sendUserName%，确认了“%projectName%：%feeDescription%”的合作设计费收款金额为%fee%万，请你跟进并确认实际到帐金额和日期。");
            //合作设计费付款-财务-无申请
            put("114", "%sendUserName%，确认了“%projectName%：%feeDescription%”的合作设计费付款金额为%fee%万，请你跟进并确认实际到帐金额和日期。");
            //合作设计费付款-财务-有申请
            put("115", "%sendUserName%，确认了“%projectName%：%feeDescription%”的合作设计费付款金额为%fee%万，%auditPersonName% 已同意拨款，请你跟进并确认实际付款金额和日期。");
            //其他支出-财务-有申请
            put("116", "%sendUserName%，确认了“%projectName%：%feeDescription%”的其他支出%fee%万，%auditPersonName% 已同意拨款，请你跟进并确认实际付款金额和日期。");

            //? - 技术审查费 - ?” 金额：?万，已确认付款，付款日期为?--->hi，XXX确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费付款金额为30万元，实际付款日期为xxxx-xx-xx，谢谢！
            //许佳迪，确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费付款金额为30万元，实际付款日期为xxxx-xx-xx。
            put("27", "%sendUserName%，发起了“%projectName%：%feeDescription%”的技术审查费付款金额为%fee%万元，实际付款日期为%paymentDate%。");//技术审查费付款，通知经营负责人，企业负责人
            //? - 技术审查费 - ?” 金额：?万，已确认到账，到账日期为?-->hi，XXX确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费到账金额为30万元，实际到账日期为xxxx-xx-xx，谢谢！
            //许佳迪，确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费到账金额为30万元，实际到账日期为xxxx-xx-xx。
            put("28", "%sendUserName%，确认了“%projectName%：%feeDescription%”的技术审查费到账金额为%fee%万元，实际到账日期为%paymentDate%。");//技术审查费到账，通知经营负责人，企业负责人

            //? - 合作设计费 - ?” 金额：?万，已确认付款，付款日期为? -->hi，XXX确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费付款金额为30万元，实际付款日期为xxxx-xx-xx，谢谢！
            //许佳迪，确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费付款金额为30万元，实际付款日期为xxxx-xx-xx。
            put("29", "%sendUserName%，发起了“%projectName%：%feeDescription%”的合作设计费付款金额为%fee%万元，实际付款日期为%paymentDate%。");//合作设计费付款，通知经营负责人，企业负责人
            //? - 合作设计费 - ?” 金额：?万，已确认到账，到账日期为?-->hi，XXX确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费到账金额为30万元，实际到账日期为xxxx-xx-xx，谢谢！
            //许佳迪，确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费到账金额为30万元，实际到账日期为xxxx-xx-xx。
            put("30", "%sendUserName%，确认了“%projectName%：%feeDescription%”的合作设计费到账金额为%fee%万元，实际到账日期为%paymentDate%。");//合作设计费到账，通知经营负责人，企业负责人

            //其他收支
            //“? - 其他收支 - ?” 金额：?万，开始付款了 --> hi，XXX发起了“卯丁科技大厦一期：节点描述”的其他支出30万元，请你跟进并确认实际付款金额和日期，谢谢！
            //许佳迪，发起了“卯丁科技大厦一期：节点描述”的其他支出30万元，请你跟进并确认实际付款金额和日期。
            put("31", "%sendUserName%，发起了“%projectName%：%feeDescription%”的其他支出%fee%万元，请你跟进并确认实际付款金额和日期。");//通知财务人员付款

            //? - 其他收支 - ?” 金额：?万，开始收款了 -->hi，XXX发起了“卯丁科技大厦一期：节点描述”的其他收入30万元，请你跟进并确认实际到帐金额和日期，谢谢！
            //许佳迪，发起了“卯丁科技大厦一期：节点描述”的其他收入30万元，请你跟进并确认实际到帐金额和日期。
            put("32", "%sendUserName%，发起了“%projectName%：%feeDescription%”的其他收入%fee%万元，请你跟进并确认实际到帐金额和日期。");//通知财务人员收款
            //“? - 其他收支 - ?” 金额：?万，已确认付款，付款日期为? -->
            //许佳迪，确认了“卯丁科技大厦一期：节点描述”的实际付款金额为30万元，付款日期为xxxx-xx-xx。
            put("33", "%sendUserName%，确认了“%projectName%：%feeDescription%”的实际付款金额为%fee%万元，付款日期为%paymentDate%。");//合其他收支付款，通知经营负责人，企业负责人
            //“? - 其他收支 - ?” 金额：?万，已确认收款，收款日期为? -->hi，XXX确认了“卯丁科技大厦一期：节点描述”的实际到账金额为30万元，到账日期为xxxx-xx-xx，谢谢！
            //许佳迪，确认了“卯丁科技大厦一期：节点描述”的实际到账金额为30万元，到账日期为xxxx-xx-xx。
            put("34", "%sendUserName%，确认了“%projectName%：%feeDescription%”的实际到账金额为%fee%万元，到账日期为%paymentDate%。");//合其他收支付款，通知经营负责人，企业负责人

            put("35", "“%projectName% - %taskName%”的所有设计任务已完成");//生产根任务已完成，给设计负责人推送消息
            put(String.format("%d", MESSAGE_TYPE_PHASE_TASK_CHANGE), "立项人%sendUserName%通知您， %taskName%的时间进行了调整，由%startTime1%变更为%endTime1%"); //设计内容时间更改
            put(String.format("%d", MESSAGE_TYPE_ISSUE_TASK_CHANGE), "?经营负责人%sendUserName%通知您， %taskName%的计划进度进行了调整，由%startTime1%变更为%endTime1%"); //签发任务时间更改
            put(String.format("%d", MESSAGE_TYPE_PRODUCT_TASK_CHANGE), "【生产安排】任务负责人%sendUserName%通知您， %taskName%的计划进度进行了调整，由%startTime1%变更为%endTime1%"); //生产任务时间更改
            put(String.format("%d", MESSAGE_TYPE_PRODUCT_TASK_FINISH), "%sendUserName%， %taskName%已完成，您看是否需要现在去处理相关项目费用的事宜？"); //生产任务完成
            //待处理
            put(String.format("%d",MESSAGE_TYPE_41),"%sendUserName%发起了“%projectName%：%feeDescription%”的实际到金额为%fee%万元，到账日期为%paymentDate%。");//财务发票确认

            //权限改动
            //许佳迪   设定你为“圆正测试设计院”系统管理员，相应权限请点击［个人设置］查看。
            put(String.format("%d", MESSAGE_TYPE_NEW_SYSTEM_MANAGER), "%sendUserName% 设定你为“%sendCompanyName%”系统管理员，相应权限请点击<a href=\"%url%\">［个人设置］</a>查看。"); //系统管理员移交，给新的管理员推送消息
            //许佳迪   修改了你的权限，请点击［个人中心］查看团队所属权限。
            put(String.format("%d", MESSAGE_TYPE_ROLE_CHANGE), "%sendUserName% 修改了你的权限，请点击<a href=\"%url%\">［个人设置］</a>查看。"); //个人权限变动，推送消息
            //许佳迪   设定你为“圆正测试设计院”企业负责人，相应权限请点击［个人设置］查看。
            put(String.format("%d", MESSAGE_TYPE_NEW_ORG_MANAGER), "%sendUserName% 设定你为“%sendCompanyName%”企业负责人，相应权限请点击<a href=\"%url%\">［个人设置］</a>查看。"); //企业负责人移交，给新的企业负责人推送消息

            //确定乙方:hi,“卯丁科技大厦一期”项目由“立项组织/XXX”完成立项，请你根据项目进度跟进相关工作，谢谢！
            put(String.format("%d", MESSAGE_TYPE_PART_B), "hi,“%projectName%”项目由“立项组织/%sendCompanyName%”完成立项，请你根据项目进度跟进相关工作，谢谢！"); //确定乙方
            //第一次发布任务给本团队，则给本团队的企业负责人推送消息
            //许佳迪   创建了“卯丁科技大厦一期”的项目，该项目的经营负责人是：AA，设计负责人是：BB，你可根据具体情况进行调整。
            put(String.format("%d", MESSAGE_TYPE_PUBLISH_TASK_ORG_MANAGER), "%sendUserName% 创建了“%projectName%”的项目，该项目的经营负责人是：%projectManagerName%，设计负责人是：%designerName%，你可根据具体情况进行调整。");

            //会议，日程消息模板
            put(String.format("%d", MESSAGE_TYPE_701), "%sendUserName% 已为你安排日程，日程内容“%scheduleContent%”，日程时间：%startTime1% - %endTime1%， 该日程提醒将会在开始前%reminderTime%分钟通过卯丁APP给您发送。详情请到“日程”中查看。");
            put(String.format("%d", MESSAGE_TYPE_702), "%sendUserName% 已修改安排日程，日程内容“%scheduleContent%”，日程时间：%startTime1% - %endTime1%， 该日程提醒将会在开始前%reminderTime%分钟通过卯丁APP给您发送。详情请到“日程”中查看。");
            put(String.format("%d", MESSAGE_TYPE_703), "%sendUserName% 已取消安排日程，日程内容“%scheduleContent%”，日程时间：%startTime1% - %endTime1%，请知晓。");
            put(String.format("%d", MESSAGE_TYPE_704), "%sendUserName% 已为你安排会议，会议内容“%scheduleContent%”，会议时间：%startTime1% - %endTime1%， 如同意参加，该会议提醒将会在开始前%reminderTime%分钟通过卯丁APP给您发送。点击详情回复是否参会。");
            put(String.format("%d", MESSAGE_TYPE_705), "%sendUserName% 已修改安排会议，会议内容“%scheduleContent%”，会议时间：%startTime1% - %endTime1%， 如同意参加，该会议提醒将会在开始前%reminderTime%分钟通过卯丁APP给您发送。点击详情回复是否参会。");
            put(String.format("%d", MESSAGE_TYPE_706), "%sendUserName% 已取消安排会议，会议内容“%scheduleContent%”，会议时间：%startTime1% - %endTime1%，请知晓。");
            put(String.format("%d", MESSAGE_TYPE_707), "%sendUserName% 已确定参加会议“%scheduleContent%”，点击查看详情。");
            put(String.format("%d", MESSAGE_TYPE_708), "%sendUserName% 已确定不参加会议“%scheduleContent%”，不参加原因“%reason%”，点击查看详情。");
            put(String.format("%d", MESSAGE_TYPE_709), "你的日程“%scheduleContent%”即将开始，点击查看详情。");
            put(String.format("%d", MESSAGE_TYPE_710), "你的会议“%scheduleContent%”即将开始，点击查看详情。");

            //归档通知
            put(String.format("%d", MESSAGE_TYPE_FILING_NOTICE), "请大家于%startTime1%日进行%projectName%系统的归档，归档人员名单“%toNodeName%”。备注：%remarks%");
            put(String.format("%d", MESSAGE_TYPE_DELIVER_CONFIRM),"%sendUserName% 发起了 %deliverName% 交付任务，并将你设置为任务负责人，截止时间：%endTime1%，请你确认。");
            put(String.format("%d", MESSAGE_TYPE_DELIVER_UPLOAD),"%sendUserName% 发起了 %deliverName% 交付任务，任务负责人为 %responseName% ; 截止时间为：%endTime1%，请您提交交付文件。");

            //轻量任务
            put(String.format("%d", MESSAGE_TYPE_901), "%sendUserName% 给你安排了“%taskName%”任务，内容：%scheduleContent%，开始时间：%startTime1%，截止时间：%endTime1%，点击查看详情。");

            put(String.format("%d", MESSAGE_TYPE_901), "%sendUserName% 给你安排了“%taskName%”任务，内容：%scheduleContent%，开始时间：%startTime1%，截止时间：%endTime1%，点击查看详情。");
            put(String.format("%d", MESSAGE_TYPE_902), "%sendUserName% 完成了你交付的“%taskName%”任务，内容：%scheduleContent%，点击查看详情。");

        }
    };


    /*****************消息模板******************/

    Map<String,MessageTemplate> messageForApp=new HashMap<String,MessageTemplate>(){
        {
            /** 参数对照 ***
             * %sendUserName%:发出通告的用户名
             * %sendCompanyName%:发出通告的公司名
             * %projectName%:项目名
             * %pointName%:款项节点名
             * %feeDescription%:费用节点描述
             * %costFee%:费用金额，如100，200
             * %paymentDate%:修改前的到账，付款日期等
             * %url%：url地址
             * %startTime1%：变更前的开始时间
             * %endTime1%：变更前的结束时间
             * %startTime2%：变更后的开始时间
             * %endTime2%：变更后的结束时间
             * %nodeName% ：节点名称：设校审
             * %toNodeName%：节点名称：校审（***完成设计（nodeName），请审核（toNodeName））
             *
             */

            //设置乙方
            put("1",new MessageTemplate("你成为了 “%projectName%” 乙方经营负责人",1) );//ok
            put("2",new MessageTemplate("你成为了 “%projectName%” 乙方设计负责人",1));//ok
            //立项
            /**********************经营负责人**********************/
            //ok //你成为了 “?” 经营负责人 -- hi,XXX完成了“卯丁科技大厦一期”的立项，请你担任该项目的经营负责人，请在任务中查看并进行［任务签发］，
            put("3",new MessageTemplate("%sendUserName% 变更了“%projectName%”的经营负责人，请你担任该项目的经营负责人，点击查看详情。",1) );
            //hi,XXX增加了“卯丁科技大厦一期”的设计任务，请在任务中查看并进行［任务签发］，
            put(String.format("%d", MESSAGE_TYPE_301),new MessageTemplate("%sendUserName% 创建了%projectName%的项目，请你担任该项目的经营负责人，点击查看详情。" ,1));
            /*******合作组织消息********2017-12-27 修改两条信息相同*/
            //hi,xxx组织，经营负责人XXX发布了“卯丁科技大厦一期”的设计任务，请你担任该项目的经营负责人，请在任务中查看并进行［任务签发］，
            put(String.format("%d", MESSAGE_TYPE_302),new MessageTemplate("%sendCompanyName%组织，%sendUserName% 增加了“%projectName%”的设计任务，点击查看详情。" ,1));
            //hi,xxx组织，经营负责人XXX发布了“卯丁科技大厦一期”的设计任务，请在任务中查看并进行［任务签发］，
            put(String.format("%d", MESSAGE_TYPE_303),new MessageTemplate("%sendCompanyName%组织，经营负责人%sendUserName%发布了“%projectName%”的设计任务，请你担任该项目的经营负责人,点击查看详情。" ,1));
            put(String.format("%d", MESSAGE_TYPE_306), new MessageTemplate("%sendCompanyName%组织，%sendUserName% 发布了“%projectName%”的设计任务，点击查看详情。",1));//ok
            //许佳迪，发起了“卯丁科技大厦：定金支付10%。”的合作设计费收款16.88万元，请你跟进并确认付款。
            put(String.format("%d", MESSAGE_TYPE_307), new MessageTemplate("%sendUserName% 发起了“%projectName%：%feeDescription%”的合作设计费收款%fee%万元，请你跟进并确认付款。",1));//ok

            /****************/
            //“卯丁科技大厦一期-方案设计”的设计任务已完成，请你跟进相关项目收支的经营工作，
            put(String.format("%d", MESSAGE_TYPE_304),new MessageTemplate("“%projectName%-%taskName%”的设计任务已完成，请你跟进相关项目收支的经营工作。" ,1));

            put(String.format("%d", MESSAGE_TYPE_305),new MessageTemplate("%sendUserName% 指定了你为 “%projectName%”的经营助理，点击查看详情。" ,1));

            put("4",new MessageTemplate("你成为了 “%projectName%” 设计负责人" ,1));//ok
            /******************** 任务负责人************************/
            // hi,经营负责人XXX发布了“卯丁科技大厦一期”的设计任务，请你担任该项目的设计负责人，请在任务中查看并进行［生产安排］，（第一次发布任务给本组织）
            put(String.format("%d", MESSAGE_TYPE_401),new MessageTemplate("%sendUserName% 发布了“%projectName%”的设计任务，请你担任该项目的设计负责人，点击查看详情。" ,1));
            // hi,经营负责人XXX发布了“卯丁科技大厦一期”的设计任务，请在任务中查看并进行［生产安排］，（第二次及后面发布任务给本组织）
            put(String.format("%d", MESSAGE_TYPE_402),new MessageTemplate("%sendUserName% 发布了“%projectName%”的设计任务，点击查看详情。" ,1));
            //设计负责人XXX进行了“卯丁科技大厦一期-建筑设计”的生产安排，你被设定为任务负责人，详情点击［我的任务］查看，
            put(String.format("%d", MESSAGE_TYPE_403),new MessageTemplate("%sendUserName% 指定了你为“%projectName%-%taskName%”的任务负责人，点击查看详情。" ,0));
            //任务负责人XXX进行了“卯丁科技大厦一期-建筑设计”的生产安排，你被设定为任务负责人，详情点击［我的任务］查看，
            put(String.format("%d", MESSAGE_TYPE_404),new MessageTemplate("%sendUserName% 指定了你为“%projectName%-%taskName%”的任务负责人，点击查看详情。" ,0));
            //“卯丁科技大厦一期-方案设计”的设计/校对/审核任务已完成，请你确认，
            put(String.format("%d", MESSAGE_TYPE_405),new MessageTemplate("“%projectName%-%taskName%”的%nodeName%任务已完成，请你确认，" ,1));
            //“卯丁科技大厦一期-方案设计”的设计任务已完成，请你确认，(设计任务完成（任务负责人），每一条完成，都需要往上一级任务负责人推消息)
            put(String.format("%d", MESSAGE_TYPE_406),new MessageTemplate("%sendUserName%  提交了“%projectName%-%taskName%”的设计任务，请你确认。" ,1));
            /*************************设计负责人*********/
            //所有的生产任务已经完成（仅自己生产的），给本组织的设计负责人推送消息：“卯丁科技大厦一期-方案设计....”的设计任务已完成，请你确认，
            put(String.format("%d", MESSAGE_TYPE_407),new MessageTemplate("%sendUserName%  提交了 “%projectName%-%taskName%”的设计任务已完成，现“%projectName%”整体工作已提交请你确认工作。" ,1));
            //本团队所有的生产任务已经完成（包含签发给其他组织的任务）：“卯丁科技大厦一期-方案设计，初步设计....”所有生产任务已完成，请你确认，
            //许佳迪，提交了“卯丁科技大厦一期-方案设计”的设计任务，现“卯丁科技大厦一期”整体工作已提交请你确认工作。
            put(String.format("%d", MESSAGE_TYPE_408),new MessageTemplate("%sendUserName% 提交了“%projectName%-%taskName%”的设计任务，现“%projectName%”整体工作已提交请你确认工作。" ,0));//此处还应该推送任务，任务类型22
            //合作方 A 给 B的任务全部完成，给A组织的设计负责人推送消息
            put(String.format("%d", MESSAGE_TYPE_409),new MessageTemplate("%sendUserName%  提交了“%projectName%-%taskName%”的设计任务已完成，请你跟进相关项目收支的经营工作。" ,1));
            put(String.format("%d", MESSAGE_TYPE_410),new MessageTemplate("%sendUserName% 指定了你为 “%projectName%”的设计助理，点击查看详情",1));

            /****************时间变动消息推送**************/
            //任务负责人/设计负责人XXX变更了“卯丁科技大厦一期-方案设计”的计划进度〖由2017-05-09至2017-09-09变更为2017-06-09至2017-10-09），请查看并作相应调整，
            //设计负责人更变时间
            put(String.format("%d", MESSAGE_TYPE_601),new MessageTemplate("%sendUserName%变更了“%projectName%-%taskName%”的计划进度【由%startTime1%至%endTime1%变更为%startTime2%至%endTime2%】，请查看并作相应调整。" ,1));
            //任务负责人更变时间
            put(String.format("%d", MESSAGE_TYPE_602),new MessageTemplate("%sendUserName%变更了“%projectName%-%taskName%”的计划进度【由%startTime1%至%endTime1%变更为%startTime2%至%endTime2%】，请查看并作相应调整。" ,1));
            //经营负责人更变时间
            put(String.format("%d", MESSAGE_TYPE_603),new MessageTemplate("%sendUserName%变更了“%projectName%-%taskName%”的计划进度【由%startTime1%至%endTime1%变更为%startTime2%至%endTime2%】，请查看并作相应调整。" ,1));
            //合作任务变更时间 %sendCompanyName%组织经营负责人
            put(String.format("%d", MESSAGE_TYPE_604),new MessageTemplate("%sendUserName%变更了“%projectName%-%taskName%”的计划进度【由%startTime1%至%endTime1%变更为%startTime2%至%endTime2%】，请查看并作相应调整。" ,1));
            //任务签发
            put("5",new MessageTemplate("你成为了“%projectName%-%taskName%”的经营负责人",1) );//ok

            /******************设校审人员*******************/
            //设计负责人XXX进行了“卯丁科技大厦一期-建筑设计”的生产安排，你将参与该任务的（设计、校对、审核）xx工作，详情请点击［我的任务］查看，
            put(String.format("%d", MESSAGE_TYPE_501),new MessageTemplate("%sendUserName% 指定了你为“%projectName%-%taskName%”的%nodeName%人员，点击查看详情。" ,1));//ok
            put(String.format("%d", MESSAGE_TYPE_502),new MessageTemplate("%sendUserName% 指定了你为“%projectName%-%taskName%”的%nodeName%人员，点击查看详情。" ,1));//ok
            //XXX完成了“卯丁科技大厦一期-方案设计”的设计工作，请你校对，
            //XXX完成了“卯丁科技大厦一期-方案设计”的校对工作，请你审核，
            put(String.format("%d", MESSAGE_TYPE_503),new MessageTemplate("%sendUserName% 完成了“%projectName%-%taskName%”的%nodeName%工作，请你%toNodeName%。" ,1));//ok


            /***************************************************/
            put("6",new MessageTemplate("你成为了“%projectName%-%taskName%”的设计负责人" ,1));//ok
            //设置任务负责人
            put("7",new MessageTemplate("你成为了“%projectName%-%taskName%”的任务负责人",0));//ok

            //“卯丁科技大厦 - 方案设计 - 方案C”设计任务的校对人是：许佳迪、小练，审核人是：XX，XXX
            put("8",new MessageTemplate("“%projectName%-%taskName%”设计任务的%nodeName%",1));//乙方项目负责人,当设置审核人员后，给乙方负责人发送消息 //ok

            put("9",new MessageTemplate("“%projectName%-%taskName%”设计任务已全部完成",1));//ok
            put("10",new MessageTemplate("你成为了 “%projectName%-%taskName%” 的%nodeName%人",0)); //ok

            put("21",new MessageTemplate("“%projectName%-%taskName%”所有子任务已全部完成",1));//ok
            /**************报销***************/
            put("19",new MessageTemplate("%sendUserName%申请报销“%expName%”，共计%expAmount%元，请你审批。",0));//ok
            //?共计?元的报销申请不予批准---XXX拒绝了你申请的“办公用品”报销金额200.00元
            put("20",new MessageTemplate("%sendUserName%拒绝了你申请的“%expName%”报销，金额%expAmount%元，退回原因：%reason%。",1));//ok
            //你申请“?”共计?元已审批通过 -- XXX同意你“办公用品”的报销金额为2000元，
            put("22",new MessageTemplate("%sendUserName%同意你“%expName%”的报销，金额为%expAmount%元。",1));
            //XXX转交了许佳迪申请的“办公用品”报销金额200.00元，请你审批，
            put("221",new MessageTemplate("%sendUserName%同意并转交了%expUserName%的报销申请“%expName%”，共计%expAmount%元给你，请你审批。",0));
            /***********费用部分***********/
            put("222",new MessageTemplate("%sendUserName%申请费用“%expName%”，共计%expAmount%元，请你审批。",0));//ok
            //?共计?元的报销申请不予批准---XXX拒绝了你申请的“办公用品”报销金额200.00元
            put("223",new MessageTemplate("%sendUserName%拒绝了你申请的“%expName%”费用，金额%expAmount%元，退回原因：%reason%。",1));//ok
            //你申请“?”共计?元已审批通过 -- XXX同意你“办公用品”的报销金额为2000元，
            put("224",new MessageTemplate("%sendUserName%同意你“%expName%”的费用，金额为%expAmount%元。",1));//报销单审批完成后发给报销人送消息
            //毛双凤同意并转交了郭志彬的费用申请“差旅费用”共计1200元，给您，请您审批。
            put("225",new MessageTemplate("%sendUserName%同意并转交了%expUserName%的费用申请“%expName%”，共计%expAmount%元给你，请你审批。",0));
            /***********请假部分***********/
            put("226",new MessageTemplate("%sendUserName% 提交了请假申请，请假类型：%leaveTypeName%，请假时间：%startTime1% - %endTime1%，请你审批。",0));//ok
            put("227",new MessageTemplate("%sendUserName% 拒绝了你的请假申请，请假类型“%leaveTypeName%”，请假时间：%startTime1% - %endTime1%，退回原因：%reason%。",1));//ok
            //您提交的请假申请，请假类型：事假，请假时间：2017/12/26 09:00-2017/12/27 18:00，已完成审批。
            put("228",new MessageTemplate("你提交的请假申请，请假类型：%leaveTypeName%，请假时间：%startTime1% - %endTime1%，已完成审批。",1));
            put("229",new MessageTemplate("%sendUserName%同意并转交了%expUserName%的请假申请，请假类型：%leaveTypeName%，请假时间：%startTime1% - %endTime1%给你，请你审批。",0));
            /***********出差部分***********/
            put("230",new MessageTemplate("%sendUserName% 提交了出差申请，出差地：%address%，出差时间：%startTime1% - %endTime1%，请你审批。",0));
            put("231",new MessageTemplate("%sendUserName% 拒绝了你的出差申请，出差地：%address%，出差时间：%startTime1% - %endTime1%，退回原因：%reason%。",1));//ok
            put("232",new MessageTemplate("你提交的出差申请，出差地：%address%，出差时间：%startTime1% - %endTime1%，已完成审批。",1));
            put("233",new MessageTemplate("%sendUserName%同意并转交了%expUserName%的出差申请，出差地：%address%，出差时间：%startTime1% - %endTime1%，%sendUserName%给你，请你审批。",0));

            put("236",new MessageTemplate("%expUserName% 提交的报销申请，“%expName%”，共计%expAmount%元，财务已拨款，请知晓。",1));//ok
            put("237",new MessageTemplate("%expUserName% 提交的费用申请，“%expName%”，共计%expAmount%元，财务已拨款，请知晓。",1));//ok
            put("238",new MessageTemplate("%expUserName% 提交的请假申请，请假类型：%leaveTypeName%，请假时间：%startTime1% - %endTime1%，已完成审批，请知晓",1));//ok
            put("239",new MessageTemplate("%expUserName% 提交的出差申请，出差地：%address%，出差时间：%startTime1% - %endTime1%，已完成审批，请知晓",1));
            /***********自定义审批***********/
            put("249",new MessageTemplate("%expUserName% 提交了“%formName%”的审批，请您审批。",0));
            put("250",new MessageTemplate("%sendUserName% 拒绝了你的“%formName%”的审批申请，退回原因：%reason%。",1));
            put("251",new MessageTemplate("你提交“%formName%”的审批，已完成审批。",1));
            put("252",new MessageTemplate("%sendUserName%同意并转交了%expUserName%的“%formName%”审批申请，请你审批。",0));
            put("253",new MessageTemplate("%expUserName% 你申请的“%formName%”审批,共计%expAmount%元，财务已拨款，请知晓。",1));
            put("254",new MessageTemplate("%expUserName% 你申请的“%formName%”审批,共计%expAmount%元，审批未通过，原因：%reason%。",1));
            put("255",new MessageTemplate("你申请的“%formName%”审批,共计%expAmount%元，财务已拨款。",1));


            //许佳迪，提交的付款申请“卯丁科技大厦：定金支付10%。”的技术审查费付款16.88万元，请你及时审批
            put("243",new MessageTemplate("%sendUserName% 提交的付款申请“%projectName%：%feeDescription%。”的%expName%付款%fee%万元，请你及时审批。",0));
            put("244",new MessageTemplate("你提交的付款申请“%projectName%：%feeDescription%。”的%expName%付款%fee%万元，已完成审批。",1));
            put("245",new MessageTemplate("%sendUserName% 拒绝了你的付款申请“%projectName%：%feeDescription%。”的%expName%付款%fee%万元，退回原因：%reason%。",1));//ok
            put("246",new MessageTemplate("%sendUserName%同意了%expUserName%的付款申请“%projectName%：%feeDescription%。”的%expName%付款%fee%万元给你，请你审批。",0));

            //财务拨款（报销，费用）
            put("234",new MessageTemplate("你申请的报销“%expName%”共计%expAmount%元，财务已拨款。",1));
            put("235",new MessageTemplate("你申请的费用“%expName%”共计%expAmount%元，财务已拨款。",1));
            //财务审核不通过（报销，费用）
            put("247",new MessageTemplate("你申请的报销“%expName%”共计%expAmount%元，审批未通过，原因：%reason%。",1));
            put("248",new MessageTemplate("你申请的费用“%expName%”共计%expAmount%元，审批未通过，原因：%reason%。",1));
            /******************项目费用******************/
            //技术审查费
            //? - 技术审查费 - ?” 金额：?万，需要你确认付款 ----->XXX发起了“卯丁科技大厦一期：技术审查费节点”的技术审查费30万元，请确认付款金额，
            put("11",new MessageTemplate("%sendUserName%发起了“%projectName%：%feeDescription%”的技术审查费%fee%万元，请确认付款金额。",0));
            //
            put("12",new MessageTemplate("“%projectName% - 技术审查费 - %feeDescription%” 金额：%fee%万，已确认付款",0)); //
            put("13",new MessageTemplate("“%projectName% - 技术审查费 - %feeDescription%” 金额：%fee%万，已确认到账",0));

            //合作设计费
            //? - 合作设计费 - ?” 金额：?万，需要你确认付款 -->XXX发起了“卯丁科技大厦一期：合作设计费节点”的合作设计费30万元，请确认付款金额，
            put("14",new MessageTemplate("%sendUserName%发起了“%projectName%：%feeDescription%”的合作设计费%fee%万元，请确认付款金额。",0));

            put("15",new MessageTemplate("“%projectName% - 合作设计费 - %feeDescription%” 金额：%fee%万，已确认付款",1));
            put("16",new MessageTemplate("“%projectName% - 合作设计费 - %feeDescription%” 金额：%fee%万，已确认到账",1));

            //财务发票确认
            put("100",new MessageTemplate( "%sendUserName%，发起了“%projectName%：%feeDescription%”的合同回款%fee%万元，发票方为：%toCompanyName%，请你跟进确认发票号码及发票类型。",0));//财务发票确认
            put("101",new MessageTemplate( "%sendUserName%，发起了“%projectName%：%feeDescription%”的技术审查费%fee%万元，发票方为：%toCompanyName%，请你跟进确认发票号码及发票类型。",0));//财务发票确认
            put("102",new MessageTemplate( "%sendUserName%，发起了“%projectName%：%feeDescription%”的合作设计费%fee%万元，发票方为：%toCompanyName%，请你跟进确认发票号码及发票类型。",0));//财务发票确认
            put("103",new MessageTemplate( "%sendUserName%，发起了“%projectName%：%feeDescription%”的其他收入%fee%万元，发票方为：%toCompanyName%，请你跟进确认发票号码及发票类型。",0));//财务发票确认


            //技术审查费收款-财务
            put("110", new MessageTemplate("%sendUserName%，确认了“%projectName%：%feeDescription%”的技术审查费收款金额为%fee%万，请你跟进并确认实际到帐金额和日期。",0));
            //技术审查费付款-财务-无申请
            put("111",new MessageTemplate( "%sendUserName%，确认了“%projectName%：%feeDescription%”的技术审查费付款金额为%fee%万，请你跟进并确认实际到帐金额和日期。",0));
            //技术审查费付款-财务-有申请
            put("112", new MessageTemplate("%sendUserName%，确认了“%projectName%：%feeDescription%”的技术审查费付款金额为%fee%万，%auditPersonName% 已同意拨款，请你跟进并确认实际付款金额和日期。",0));

            //合作设计费收款-财务
            put("113",new MessageTemplate( "%sendUserName%，确认了“%projectName%：%feeDescription%”的合作设计费收款金额为%fee%万，请你跟进并确认实际到帐金额和日期。",0));
            //合作设计费付款-财务-无申请
            put("114", new MessageTemplate("%sendUserName%，确认了“%projectName%：%feeDescription%”的合作设计费付款金额为%fee%万，请你跟进并确认实际到帐金额和日期。",0));
            //合作设计费付款-财务-有申请
            put("115", new MessageTemplate("%sendUserName%，确认了“%projectName%：%feeDescription%”的合作设计费付款金额为%fee%万，%auditPersonName% 已同意拨款，请你跟进并确认实际付款金额和日期。",0));
            //其他支出-财务-有申请
            put("116", new MessageTemplate("%sendUserName%，确认了“%projectName%：%feeDescription%”的其他支出%fee%万，%auditPersonName% 已同意拨款，请你跟进并确认实际付款金额和日期。",0));

            //合同回款
            //? - 合同回款 - ?” 金额：?万，开始收款了 -->XXX发起了“卯丁科技大厦一期：回款节点”的合同回款30万元，请你跟进并确认实际到帐金额和日期，
            put("17",new MessageTemplate("%sendUserName%发起了“%projectName%：%feeDescription%”的合同回款%fee%万元，请你跟进并确认实际到帐金额和日期。",0));//合同回款--通知财务收款
            //? - 合同回款 - ?” 金额：?万，已确认到账，到账日期为? --->XXX确认了“卯丁科技大厦一期：回款节点”的实际到金额为30万元，到账日期为xxxx-xx-xx，
            put("18",new MessageTemplate("%sendUserName%确认了“%projectName%：%feeDescription%”的实际到金额为%fee%万元，到账日期为%paymentDate%。",1));//合同回款到账，通知经营负责人，企业负责人


            //? - 技术审查费 - ?” 金额：?万，已付款，请进行相关操作 -->XXX确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费付款金额为30万元，请你跟进并确认实际付款日期，
            put("23",new MessageTemplate("%sendUserName%确认了“%projectName%：%feeDescription%”的技术审查费付款金额为%fee%万元，请你跟进并确认实际付款日期，",0));//技术审查费财务人员付款操作
            //? - 技术审查费 - ?” 金额：?万，已到账，请进行相关操作-->XXX确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费付款金额为30万元，请你跟进并确认实际到账日期，
            put("24",new MessageTemplate("%sendUserName%确认了“%projectName%：%feeDescription%”的技术审查费付款金额为%fee%万元，请你跟进并确认实际到账日期。",0));//技术审查费财务人员到账操作

            //“? - 合作设计费 - ?” 金额：?万，已付款，请进行相关操作-->XXX确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费付款金额为30万元，请你跟进并确认实际付款日期，
            put("25",new MessageTemplate("%sendUserName%确认了“%projectName%：%feeDescription%”的合作设计费付款金额为%fee%万元，请你跟进并确认实际付款日期。",0));//合作设计费财务人员付款操作
            //? - 合作设计费 - ?” 金额：?万，已到账，请进行相关操作-->XXX确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费付款金额为30万元，请你跟进并确认实际到账日期，
            put("26",new MessageTemplate("%sendUserName%确认了“%projectName%：%feeDescription%”的合作设计费付款金额为%fee%万元，请你跟进并确认实际到账日期。",0));//合作设计费财务人员到账操作

            //? - 技术审查费 - ?” 金额：?万，已确认付款，付款日期为?--->XXX确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费付款金额为30万元，实际付款日期为xxxx-xx-xx，
            put("27",new MessageTemplate("%sendUserName%确认了“%projectName%：%feeDescription%”的技术审查费付款金额为%fee%万元，实际付款日期为%paymentDate%。",1));//技术审查费付款，通知经营负责人，企业负责人
            //? - 技术审查费 - ?” 金额：?万，已确认到账，到账日期为?-->XXX确认了“卯丁科技大厦一期：技术审查费节点”的技术审查费到账金额为30万元，实际到账日期为xxxx-xx-xx，
            put("28",new MessageTemplate("%sendUserName%确认了“%projectName%：%feeDescription%”的技术审查费到账金额为%fee%万元，实际到账日期为%paymentDate%。",1));//技术审查费到账，通知经营负责人，企业负责人

            //? - 合作设计费 - ?” 金额：?万，已确认付款，付款日期为? -->XXX确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费付款金额为30万元，实际付款日期为xxxx-xx-xx，
            put("29",new MessageTemplate("%sendUserName%确认了“%projectName%：%feeDescription%”的合作设计费付款金额为%fee%万元，实际付款日期为%paymentDate%。",1));//合作设计费付款，通知经营负责人，企业负责人
            //? - 合作设计费 - ?” 金额：?万，已确认到账，到账日期为?-->XXX确认了“卯丁科技大厦一期：合作设计费节点”的合作设计费到账金额为30万元，实际到账日期为xxxx-xx-xx，
            put("30",new MessageTemplate("%sendUserName%确认了“%projectName%：%feeDescription%”的合作设计费到账金额为%fee%万元，实际到账日期为%paymentDate%。",1));//合作设计费到账，通知经营负责人，企业负责人

            //其他收支
            //“? - 其他收支 - ?” 金额：?万，开始付款了 --> XXX发起了“卯丁科技大厦一期：节点描述”的其他支出30万元，请你跟进并确认实际付款金额和日期，
            put("31",new MessageTemplate("%sendUserName%发起了“%projectName%：%feeDescription%”的其他支出%fee%万元，请你跟进并确认实际付款金额和日期。",0));//通知财务人员付款
            //? - 其他收支 - ?” 金额：?万，开始收款了 -->XXX发起了“卯丁科技大厦一期：节点描述”的其他收入30万元，请你跟进并确认实际到帐金额和日期，
            put("32",new MessageTemplate("%sendUserName%发起了“%projectName%：%feeDescription%”的其他收入%fee%万元，请你跟进并确认实际到帐金额和日期。",0));//通知财务人员收款
            //“? - 其他收支 - ?” 金额：?万，已确认付款，付款日期为? -->
            put("33",new MessageTemplate("%sendUserName%确认了“%projectName%：%feeDescription%”的实际付款金额为%fee%万元，付款日期为%paymentDate%。",1));//合其他收支付款，通知经营负责人，企业负责人
            //“? - 其他收支 - ?” 金额：?万，已确认收款，收款日期为? -->XXX确认了“卯丁科技大厦一期：节点描述”的实际到账金额为30万元，到账日期为xxxx-xx-xx，
            put("34",new MessageTemplate("%sendUserName%确认了“%projectName%：%feeDescription%”的实际到账金额为%fee%万元，到账日期为%paymentDate%。",1));//合其他收支付款，通知经营负责人，企业负责人

            put("35",new MessageTemplate("“%projectName% - %taskName%”的所有设计任务已完成",1));//生产根任务已完成，给设计负责人推送消息
            put(String.format("%d", MESSAGE_TYPE_PHASE_TASK_CHANGE),new MessageTemplate("立项人%sendUserName%通知您， %taskName%的时间进行了调整，由%startTime1%变更为%endTime1%",1)); //设计内容时间更改
            put(String.format("%d",MESSAGE_TYPE_ISSUE_TASK_CHANGE),new MessageTemplate("?经营负责人%sendUserName%通知您， %taskName%的计划进度进行了调整，由%startTime1%变更为%endTime1%",1)); //签发任务时间更改
            put(String.format("%d",MESSAGE_TYPE_PRODUCT_TASK_CHANGE),new MessageTemplate("任务负责人%sendUserName%通知您， %taskName%的计划进度进行了调整，由%startTime1%变更为%endTime1%",1)); //生产任务时间更改
            put(String.format("%d",MESSAGE_TYPE_PRODUCT_TASK_FINISH),new MessageTemplate("%sendUserName%， %taskName%已完成，您看是否需要现在去处理相关项目费用的事宜？",1)); //生产任务完成

            //待处理
            put(String.format("%d",MESSAGE_TYPE_41),new MessageTemplate("%sendUserName%发起了“%projectName%：%feeDescription%”的实际到金额为%fee%万元，到账日期为%paymentDate%。",1));//合同回款到账，通知经营负责人，企业负责人


            //权限改动
            //hi,你被设定为“圆正测试设计院”系统管理员，相应权限请点击［个人设置］查看。
            put(String.format("%d",MESSAGE_TYPE_NEW_SYSTEM_MANAGER),new MessageTemplate("%sendUserName% 设定你为“%sendCompanyName%”系统管理员，点击查看详情。",1)); //系统管理员移交，给新的管理员推送消息
            //系统管理员重新设置了你的权限，请点击［个人设置］查看。
            put(String.format("%d",MESSAGE_TYPE_ROLE_CHANGE),new MessageTemplate("%sendUserName% 修改了你的权限，请点击查看详情。",1)); //个人权限变动，推送消息
            //hi,你被设定为“圆正测试设计院”企业负责人，相应权限请点击［个人设置］查看。
            put(String.format("%d",MESSAGE_TYPE_NEW_ORG_MANAGER),new MessageTemplate("%sendUserName% 设定你为“%sendCompanyName%”企业负责人，点击查看详情。",1)); //企业负责人移交，给新的企业负责人推送消息

            //确定乙方:hi,“卯丁科技大厦一期”项目由“立项组织/XXX”完成立项，请你根据项目进度跟进相关工作，
            put(String.format("%d",MESSAGE_TYPE_PART_B),new MessageTemplate("%sendUserName% 创建了“%projectName%”项目由“立项组织/%sendCompanyName%”完成立项，请你根据项目进度跟进相关工作。",1)); //确定乙方
            //第一次发布任务给本团队，则给本团队的企业负责人推送消息
            put(String.format("%d",MESSAGE_TYPE_PUBLISH_TASK_ORG_MANAGER),new MessageTemplate("%sendUserName% 创建了“%projectName%”项目，该项目的经营负责人是：%projectManagerName%，设计负责人是：%designerName%，你可根据具体情况进行调整。",1));

            //会议，日程消息模板
            put(String.format("%d",MESSAGE_TYPE_701),new MessageTemplate("%sendUserName% 已为你安排日程，日程内容“%scheduleContent%”，日程时间：%startTime1% - %endTime1%，点击查看详情。",1));
            put(String.format("%d",MESSAGE_TYPE_702),new MessageTemplate("%sendUserName% 已修改安排日程，日程内容“%scheduleContent%”，日程时间：%startTime1% - %endTime1%，点击查看详情。",1));
            put(String.format("%d",MESSAGE_TYPE_703),new MessageTemplate("%sendUserName% 已取消安排日程，日程内容“%scheduleContent%”，日程时间：%startTime1% - %endTime1%，请知晓。",1));
            put(String.format("%d",MESSAGE_TYPE_704),new MessageTemplate("%sendUserName% 已为你安排会议，会议内容“%scheduleContent%”，会议时间：%startTime1% - %endTime1%， 如同意参加，点击详情回复是否参会。",1));
            put(String.format("%d",MESSAGE_TYPE_705),new MessageTemplate("%sendUserName% 已修改安排会议，会议内容“%scheduleContent%”，会议时间：%startTime1% - %endTime1%， 如同意参加，点击详情回复是否参会。",1));
            put(String.format("%d",MESSAGE_TYPE_706),new MessageTemplate("%sendUserName% 已取消安排会议，会议内容“%scheduleContent%”，会议时间：%startTime1% - %endTime1%，请知晓。",1));
            put(String.format("%d",MESSAGE_TYPE_707),new MessageTemplate("%sendUserName% 已确定参加会议“%scheduleContent%”，点击查看详情。",1));
            put(String.format("%d",MESSAGE_TYPE_708),new MessageTemplate("%sendUserName% 已确定不参加会议“%scheduleContent%”，不参加原因“%reason%”，点击查看详情。",1));
            put(String.format("%d",MESSAGE_TYPE_709),new MessageTemplate( "你的日程“%scheduleContent%”即将开始，点击查看详情。",1));
            put(String.format("%d",MESSAGE_TYPE_710),new MessageTemplate( "你的会议“%scheduleContent%”即将开始，点击查看详情。",1));

            put(String.format("%d", MESSAGE_TYPE_FILING_NOTICE),new MessageTemplate( "请大家于%startTime1%日进行%projectName%系统的归档，归档人员名单“%toNodeName%”。备注：%remarks%",1));
            put(String.format("%d", MESSAGE_TYPE_DELIVER_CONFIRM),new MessageTemplate( "%sendUserName% 发起了 %deliverName% 交付任务，并将你设置为任务负责人，截止时间：%endTime1%，请你确认。",1));
            put(String.format("%d", MESSAGE_TYPE_DELIVER_UPLOAD),new MessageTemplate( "%sendUserName% 发起了 %deliverName% 交付任务，任务负责人为 %responseName% ; 截止时间为：%endTime1%，请您提交交付文件。",1));

            put(String.format("%d", MESSAGE_TYPE_901),new MessageTemplate( "%sendUserName% 给你安排了“%taskName%”任务，内容：%scheduleContent%，开始时间：%startTime1%，截止时间：%endTime1%，点击查看详情。",0));
            put(String.format("%d", MESSAGE_TYPE_902),new MessageTemplate( "%sendUserName% 完成了你交付的“%taskName%”任务，内容：%scheduleContent%，点击查看详情。",1));

        }
    };


    /*****************消息类型******************/
    int MESSAGE_TYPE_1 = 1;//1.乙方经营负责人
    int MESSAGE_TYPE_2 = 2;//1.乙方经营任务负责人
    //经营负责人
    int MESSAGE_TYPE_3 = 3;//1.变更立项方经营负责人
    int MESSAGE_TYPE_301 = 301;//1.立项方经营负责人(增加设计内容，给经营负责人推送消息）
    int MESSAGE_TYPE_302 = 302;//1.(发布消息给外部团队，给其经营负责人推送消息，第一次）
    int MESSAGE_TYPE_303 = 303;//
    int MESSAGE_TYPE_304 = 304;//1.(设计负责人或任务负责人确认经营签发的一级和二级任务完成时（生产的根任务完成后，给本组织的经营负责人推）每一个都需要推送）
    int MESSAGE_TYPE_305 = 305; //经营负责人指定了助理
    int MESSAGE_TYPE_306 = 306; //(发布消息给外部团队，给其经营负责人推送消息）
    int MESSAGE_TYPE_307 = 307; //组织内部，如果是收款方发起收款，推送消息给付款方经营负责人，要求其付款申请
    int MESSAGE_TYPE_4 = 4;//
    //任务负责人
    int MESSAGE_TYPE_401 = 401;//1.立项方经营负责人
    int MESSAGE_TYPE_402 = 402;//1.立项方经营负责人
    int MESSAGE_TYPE_403 = 403;//1.立项方经营负责人
    int MESSAGE_TYPE_404 = 404;//1.立项方经营负责人
    int MESSAGE_TYPE_405 = 405;
    int MESSAGE_TYPE_406 = 406;
    int MESSAGE_TYPE_407 = 407;//所有的生产任务已经完成，给本组织的设计负责人推送消息：hi，“卯丁科技大厦一期-方案设计(此处是该团队自己生产的所有的生产的根任务)”的设计任务已完成，请你确认，谢谢！
    int MESSAGE_TYPE_408 = 408;//所有的生产任务已经完成（包含签发出去的任务），签发方组织的设计负责人推送消息：hi，“卯丁科技大厦一期-方案设计”的设计任务已完成，请你确认，谢谢！
    int MESSAGE_TYPE_409 = 409;////合作方 A 给 B的任务全部完成，给A组织的设计负责人推送消息:所有的生产任务已经完成，签发方组织的设计负责人推送消息：hi，“卯丁科技大厦一期-方案设计”的设计任务已完成，请你确认，谢谢！
    int MESSAGE_TYPE_410 = 410;//指定为设计助理

    int MESSAGE_TYPE_5 = 5;//1.合作方经营负责人
    int MESSAGE_TYPE_501 = 501;//1.立项方经营负责人
    int MESSAGE_TYPE_502 = 502;//1.立项方经营负责人
    int MESSAGE_TYPE_503 = 503;//1.立项方经营负责人

    //时间变动消息
    int MESSAGE_TYPE_601 = 601;
    int MESSAGE_TYPE_602 = 602;
    int MESSAGE_TYPE_603 = 603;
    int MESSAGE_TYPE_604 = 604;

    int MESSAGE_TYPE_6 = 6;//1.合作方经营负责人
    int MESSAGE_TYPE_7 = 7;//1.任务负责人
    int MESSAGE_TYPE_8 = 8;//1.乙方经营负责人
    int MESSAGE_TYPE_9 = 9;//1.乙方经营负责人
    int MESSAGE_TYPE_10 = 10;//1.乙方经营负责人
    int MESSAGE_TYPE_11 = 11;//经营负责人，技术审查费付款
    int MESSAGE_TYPE_12 = 12;//1.乙方经营负责人
    int MESSAGE_TYPE_13 = 13;//1.乙方经营负责人
    int MESSAGE_TYPE_14 = 14;//1.乙方经营负责人
    int MESSAGE_TYPE_15 = 15;//1.乙方经营负责人
    int MESSAGE_TYPE_16 = 16;//1.乙方经营负责人
    int MESSAGE_TYPE_17 = 17;//1.财务-合同回款到账确认
    int MESSAGE_TYPE_18 = 18;//1.合同回款财务人员到账操作
    int MESSAGE_TYPE_19 = 19;//1.乙方经营负责人
    int MESSAGE_TYPE_20 = 20;//1.乙方经营负责人

    int MESSAGE_TYPE_100 = 100;//财务发票确认- 合同回款
    int MESSAGE_TYPE_101 = 101;//财务发票确认- 技术审查费
    int MESSAGE_TYPE_102 = 102;//财务发票确认- 合作设计费
    int MESSAGE_TYPE_103 = 103;//财务发票确认- 其他费用

    int MESSAGE_TYPE_110 = 110;//技术审查费收款-财务
    int MESSAGE_TYPE_111 = 111;//技术审查费付款-财务 - 无申请
    int MESSAGE_TYPE_112 = 112;//技术审查费付款-财务 - 有申请
    int MESSAGE_TYPE_113 = 113;//合作设计费收款-财务
    int MESSAGE_TYPE_114 = 114;//合作设计费付款-财务 - 无申请
    int MESSAGE_TYPE_115 = 115;//合作设计费付款-财务 - 有申请
    int MESSAGE_TYPE_116 = 116;//其他费用付款  -财务 - 有申请

    int MESSAGE_TYPE_21 = 21;//流程审批完成，给负责人发送消息
    int MESSAGE_TYPE_22 = 22;//同意报销,给报销人推送消息
    int MESSAGE_TYPE_221 = 221;//1.报销单完全同意之后，给报销人推送消息
    int MESSAGE_TYPE_222 = 222;//费用申请
    int MESSAGE_TYPE_223 = 223;//费用申请（同意）
    int MESSAGE_TYPE_224 = 224;//费用申请（拒绝）
    int MESSAGE_TYPE_225 = 225;//费用申请（同意并转交）

    //请假
    int MESSAGE_TYPE_226 = 226;//1.报销单完全同意之后，给报销人推送消息
    int MESSAGE_TYPE_227 = 227;//费用申请
    int MESSAGE_TYPE_228 = 228;//费用申请（拒绝）
    int MESSAGE_TYPE_229 = 229;//费用申请（同意）
    //出差
    int MESSAGE_TYPE_230 = 230;
    int MESSAGE_TYPE_231 = 231;
    int MESSAGE_TYPE_232 = 232;
    int MESSAGE_TYPE_233 = 233;
    //许佳迪，提交的付款申请“卯丁科技大厦：定金支付10%。”的技术审查费付款16.88万元，请你及时审批
    int MESSAGE_TYPE_243 = 243;
    int MESSAGE_TYPE_244 = 244;
    int MESSAGE_TYPE_245 = 245;
    int MESSAGE_TYPE_246 = 246;


    //抄送
    int MESSAGE_TYPE_236 = 236;//报销
    int MESSAGE_TYPE_237 = 237;//费用
    int MESSAGE_TYPE_238 = 238;//请假
    int MESSAGE_TYPE_239 = 239;//出差

    //自定义报销
    int MESSAGE_TYPE_249 = 249;//审批提交
    int MESSAGE_TYPE_250 = 250;//审批拒绝
    int MESSAGE_TYPE_251 = 251;//审批完成
    int MESSAGE_TYPE_252 = 252;//审批转批
    int MESSAGE_TYPE_253 = 253;//审批财务同意(抄送)
    int MESSAGE_TYPE_254 = 254;//审批财务不同意
    int MESSAGE_TYPE_255 = 255;//财务拨款


    //财务拨款
    int MESSAGE_TYPE_234 = 234;//您申请的报销“办公费用”共计1200元，财务已拨款。
    int MESSAGE_TYPE_235 = 235;//您申请的报销“办公费用”共计1200元，财务已拨款
    //财务审核未通过
    int MESSAGE_TYPE_247 = 247;//您申请的报销“办公费用”共计1200元，审批未通过，原因：%reason%。
    int MESSAGE_TYPE_248 = 248;//您申请的费用“办公费用”共计1200元，审批未通过，原因：%reason%。

    //发送归档通知（已被交付确认和上传任务取代）
    int MESSAGE_TYPE_FILING_NOTICE = 240;

    int MESSAGE_TYPE_DELIVER_CONFIRM = 241;
    int MESSAGE_TYPE_DELIVER_UPLOAD = 242;

    int MESSAGE_TYPE_23 = 23;//技术审查费财务人员付款操作
    int MESSAGE_TYPE_24 = 24;//技术审查费财务人员到账操作
    int MESSAGE_TYPE_25 = 25;//合作设计费财务人员付款操作
    int MESSAGE_TYPE_26 = 26;//合作设计费财务人员到账操作
    int MESSAGE_TYPE_27 = 27;//技术审查费付款，通知经营负责人，企业负责人
    int MESSAGE_TYPE_28 = 28;//技术审查费到账，通知经营负责人，企业负责人
    int MESSAGE_TYPE_29 = 29;//合作设计费付款，通知经营负责人，企业负责人
    int MESSAGE_TYPE_30 = 30;//合作设计费到账，通知经营负责人，企业负责人
    int MESSAGE_TYPE_31 = 31;//其他费财务人员付款操作
    int MESSAGE_TYPE_32 = 32;//其他费财务人员到账操作
    int MESSAGE_TYPE_33 = 33;//其他费付款，通知经营负责人，企业负责人
    int MESSAGE_TYPE_34 = 34;//其他费到账，通知经营负责人，企业负责人
    int MESSAGE_TYPE_35 = 35;//生产根任务已完成，给设计负责人推送消息
    int MESSAGE_TYPE_PHASE_TASK_CHANGE = 36; //设计任务时间更改
    int MESSAGE_TYPE_ISSUE_TASK_CHANGE = 37; //签发任务时间更改
    int MESSAGE_TYPE_PRODUCT_TASK_CHANGE = 38; //生产任务时间更改
    int MESSAGE_TYPE_PRODUCT_TASK_FINISH = 39; //生产任务完成
    int MESSAGE_TYPE_DELIVER_FINISHED = 40; //文件归档任务完成
    int MESSAGE_TYPE_41 = 41;//财务发票确认

    int MESSAGE_TYPE_NEW_SYSTEM_MANAGER = 801; //系统管理员移交，给新的管理员推送消息
    int MESSAGE_TYPE_ROLE_CHANGE = 802; //权限发送改动，给相关人员推送消息
    int MESSAGE_TYPE_NEW_ORG_MANAGER = 803; //更改企业负责人，给新的企业负责人推送消息

    int MESSAGE_TYPE_PART_B = 44; //企业负责人、经营负责人、设计负责人（乙方组织）
    int MESSAGE_TYPE_PUBLISH_TASK_ORG_MANAGER = 45; //第一发布后本组织（给企业负责人推送消息）

    //会议.日程消息
    int MESSAGE_TYPE_701 = 701; //新增日程
    int MESSAGE_TYPE_702 = 702; //修改日程
    int MESSAGE_TYPE_703 = 703; //取消，删除日程
    int MESSAGE_TYPE_704 = 704;//新增会议
    int MESSAGE_TYPE_705 = 705; //修改会议
    int MESSAGE_TYPE_706 = 706; //取消，删除会议
    int MESSAGE_TYPE_707 = 707; //取消，删除会议
    int MESSAGE_TYPE_708 = 708; //取消，删除会议

    //轻量任务消息
    int MESSAGE_TYPE_901 = 901;
    int MESSAGE_TYPE_902 = 902;
    int MESSAGE_TYPE_709 = 709;
    int MESSAGE_TYPE_710 = 710;
    /*******任务类型************/
    int TASK_TYPE_PRODUCT = 0;//生产任务
    int TASK_TYPE_ISSUE = 2;//签发任务
    int TASK_TYPE_PHASE = 1;//项目阶段
    int TASK_TYPE_MODIFY = 3;//签发任务的修改版本
    int TASK_PRODUCT_TYPE_MODIFY = 4;//生产任务的修改版本
    int TASK_DESIGN_TYPE = 5;//设计分解任务

    String TASK_STATUS_VALID = "0";
    String TASK_STATUS_INVALID = "1";
    String TASK_STATUS_MODIFIED = "2";

    /*******任务计划时间类型*******/
    int PROCESS_TYPE_CONTACT = 1;//合同进度
    int PROCESS_TYPE_PLAN = 2;//计划进度
    int PROCESS_TYPE_NOT_PUBLISH = 3;//未发布

    /*************项目群成员加入的类型**************/
    int PROJECT_MANAGER = 1;//经营负责人
    int PROJECT_DESIGNER = 2;//任务负责人
    int TASK_RESPONSIBLE = 3;//任务负责人
    int PROCESS_USER = 4;//设校审人员
    int PROJECT_CREATOR = 5;//立项人

    /*************im 卯丁助手id *******************/
    String MAODING_ID = "123f9ef123c140fd9b6c7a5123c68e1a";
    /*************通知公告助手id *******************/
    String NOTICE_MESSAGE_ID = "75317ab0e0a011e782f8f8db88fcba36";
    /*************系统消息助手id *******************/
    String SYSTEM_MESSAGE_ID = "75380bfce0a011e782f8f8db88fcba36";
    /***************任务类型(是否是经营任务)************/
    int TASK_PRODUCT = 0;
    int TASK_OPERATOR = 1;

    /***************任务结束类型************/
    int STATUS_NOT_COMPLETE = 0;
    int STATUS_COMPLETE = 1;
    int STATUS_TERMINATION = 2;

    //企业负责人权限id
    String ORG_MANAGER_PERMISSION_ID = "11";

    String OPERATOR_MANAGER_PERMISSION_ID = "51";

    String DESIGN_MANAGER_PERMISSION_ID = "52";
    String PROJECT_DELETE_PERMISSION_ID = "54";

    String FINANCIAL_PERMISSION_ID = "49";

    String FINANCIAL_RECEIVE_PERMISSION_ID = "402";

    //todo 改为从数据库读取，目前为0＝进行中，1＝已暂停•未结清，2＝已完成•未结清 ，3 = 已终止•未结清， 4＝已完成•已结清，5＝已暂停•已结清 ，6 = 已终止•已结清
    Map<String, String> PROJECT_STATUS = new HashMap<String, String>() {
        {
            put("0", "进行中");
            put("1", "已暂停-未结清");
            put("2", "已完成-未结清");
            put("3", "已终止-未结清");
            put("4", "已完成-已结清");
            put("5", "已暂停-已结清");
            put("6", "已终止-已结清");
        }
    };

    //费用类型
    List<String> CATEGORY_ROOT = new ArrayList<String>() {{
        add("业务收入");
        add("主营业务成本");
        add("管理费用");
        add("费用分摊");
        add("财务费用");
        add("税务费用");
    }};

    List<String> CATEGORY_ROOT_NOT_SHARE = new ArrayList<String>() {{
        add("业务收入");
        add("主营业务成本");
        add("管理费用");
        add("财务费用");
        add("税务费用");
    }};

    Map<String,List<String>> CATEGORY_CHILD = new TreeMap<String,List<String>>(){
        {
            put("业务收入",Arrays.asList("主营业务收入","其他业务收入"));
            put("主营业务成本",Arrays.asList("直接项目成本","直接人工成本"));
            put("管理费用",Arrays.asList("管理人员工资","房屋物业费用","行政费用",
                    "经营费用","其他费用","软件、不动产购置","资产减值准备","房屋物业费用分摊"
            ,"资产购置费分摊","其他费用分摊"));
        }
    };

    Map<String,String> CATEGORY_RELATION = new TreeMap<String,String>(){
        {
            put("业务收入","业务收入");
            put("主营业务成本","主营业务成本");
            put("管理费用","管理费用");

            put("主营业务收入","业务收入");
            put("其他业务收入","业务收入");

            put("直接项目成本","主营业务成本");
            put("直接人工成本","主营业务成本");


            put("管理人员工资","管理费用");
            put("房屋物业费用","管理费用");
            put("行政费用","管理费用");
            put("经营费用","管理费用");
            put("其他费用","管理费用");
            put("软件、不动产购置","管理费用");
            put("资产减值准备","管理费用");

            put("房屋物业费用分摊","费用分摊");
            put("资产购置费分摊","费用分摊");
            put("其他费用分摊","费用分摊");

        }
    };

    //#FAAE2F，#2FB0ED，#D97053，#4E7EBE，#AF73C2，#93C888
    List<String> COLORS =  new ArrayList<String>() {{
        add("#FAAE2F");
        add("#2FB0ED");
        add("#D97053");
        add("#4E7EBE");
        add("#AF73C2");
        add("#93C888");
    }};
    
    /** 可选标题栏编号 **/
    // 项目基本信息
    int TITLE_TYPE_PROJECT_CREATE_DATE = 1;
    int TITLE_TYPE_PROJECT_NAME = 2;
    int TITLE_TYPE_PROJECT_STATUS = 3;
    int TITLE_TYPE_PROJECT_NO = 4;
    int TITLE_TYPE_PROJECT_PART_A = 5;
    int TITLE_TYPE_PROJECT_PART_B = 6;
    int TITLE_TYPE_PROJECT_SIGN_DATE = 7;
    int TITLE_TYPE_PROJECT_TYPE = 8;
    int TITLE_TYPE_PROJECT_CREATE_COMPANY = 9;
    int TITLE_TYPE_PROJECT_BUILD_TYPE = 10;
    int TITLE_TYPE_PROJECT_ADDRESS = 11;
    int TITLE_TYPE_PROJECT_RELATION_COMPANY = 12;
    // 项目成员信息
    int TITLE_TYPE_PROJECT_LEADER = 13;
    int TITLE_TYPE_PROJECT_ASSITANT = 14;
    int TITLE_TYPE_PROJECT_DESIGN = 15;
    int TITLE_TYPE_PROJECT_DESIGN_ASSISTANT = 16;
    int TITLE_TYPE_PROJECT_TASK_LEADER = 17;
    int TITLE_TYPE_PROJECT_TASK_DESIGNER = 18;
    int TITLE_TYPE_PROJECT_TASK_CHECKER = 19;
    int TITLE_TYPE_PROJECT_TASK_AUDITOR = 20;
    // 项目收入情况
    int TITLE_TYPE_PROJECT_CONTRACT = 21;
    int TITLE_TYPE_PROJECT_CONTRACT_REAL = 22;
    int TITLE_TYPE_PROJECT_TECHNICAL_GAIN = 23;
    int TITLE_TYPE_PROJECT_TECHNICAL_GAIN_REAL = 24;
    int TITLE_TYPE_PROJECT_COOPERATE_GAIN = 25;
    int TITLE_TYPE_PROJECT_COOPERATE_GAIN_REAL = 26;
    int TITLE_TYPE_PROJECT_OTHER_GAIN = 27;
    int TITLE_TYPE_PROJECT_OTHER_GAIN_REAL = 28;
    // 项目支出情况
    int TITLE_TYPE_PROJECT_TECHNICAL_PAY = 29;
    int TITLE_TYPE_PROJECT_TECHNICAL_PAY_REAL = 30;
    int TITLE_TYPE_PROJECT_COOPERATE_PAY = 31;
    int TITLE_TYPE_PROJECT_COOPERATE_PAY_REAL = 32;
    int TITLE_TYPE_PROJECT_OTHER_PAY = 33;
    int TITLE_TYPE_PROJECT_OTHER_PAY_REAL = 34;

    // 发票汇总标题栏
    int TITLE_TYPE_INVOICE_APPLY_DATE = 35;
    int TITLE_TYPE_INVOICE_APPLY_USER = 36;
    int TITLE_TYPE_INVOICE_FEE = 37;
    int TITLE_TYPE_INVOICE_INVOICE_TYPE = 38;
    int TITLE_TYPE_INVOICE_COST_TYPE = 39;
    int TITLE_TYPE_INVOICE_RELATION_COMPANY = 40;
    int TITLE_TYPE_INVOICE_TAX_ID = 41;
    int TITLE_TYPE_INVOICE_FEE_DESCRIPTION = 42;
    int TITLE_TYPE_INVOICE_PROJECT_NAME = 43;
    int TITLE_TYPE_INVOICE_NO = 44;

    /** 标题栏类别 **/
    int TITLE_TYPE_PROJECT = 0;
    int TITLE_TYPE_PROJECT_OVERVIEW = 1;
    int TITLE_TYPE_INVOICE = 2;

    /** 动态表单可以使用的控件类型 **/
    int DYNAMIC_FORM_FIELD_TYPE_SIMPLE_TEXT = 1;
    int DYNAMIC_FORM_FIELD_TYPE_MULTI_LINE_TEXT = 2;
    int DYNAMIC_FORM_FIELD_TYPE_DATE = 3;
    int DYNAMIC_FORM_FIELD_TYPE_START_END_DATE = 4;
    int DYNAMIC_FORM_FIELD_TYPE_NUMBER = 5;
    int DYNAMIC_FORM_FIELD_TYPE_NUMBER_WITH_UNIT = 6;
    int DYNAMIC_FORM_FIELD_TYPE_RADIO = 7;
    int DYNAMIC_FORM_FIELD_TYPE_CHECK_BOX = 8;
    int DYNAMIC_FORM_FIELD_TYPE_LIST = 9;
    int DYNAMIC_FORM_FIELD_TYPE_RICH_TEXT = 10;
    int DYNAMIC_FORM_FIELD_TYPE_STATIC = 11;
    int DYNAMIC_FORM_FIELD_TYPE_DETAIL = 12;
    int DYNAMIC_FORM_FIELD_TYPE_ATTACH = 13;
    int DYNAMIC_FORM_FIELD_TYPE_SPLIT = 14;
    int DYNAMIC_FORM_FIELD_TYPE_RELATION_PROJECT = 15;
    int DYNAMIC_FORM_FIELD_TYPE_RELATION_AUDIT = 16;

}
