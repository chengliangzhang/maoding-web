/**
 * 工作台菜单
 * Created by wrb on 2017/10/11.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_metismenu",
        defaults = {
            $contentEle:null,//
            $projectId:null,
            $projectName:null,
            $type:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._cookiesMark = 'cookiesData_metismenu_'+window._currentCompanyUserId;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;


            that.initHtmlTemplate();
        }
        , initHtmlTemplate:function () {
            var that = this;
            var isMiniNav = false;

            var html = template('m_home/m_metismenu',{});
            $(that.element).html(html);

            rolesControl();
            that.menuClickFun();
            that.menuHoverFun();
            that.initRoute();
            that.removeNoMenuUl();

            var cookiesData = Cookies.get(that._cookiesMark);
            if(cookiesData!=undefined){
                cookiesData = $.parseJSON(cookiesData);
                if(cookiesData.param.isMiniNav)
                    $('body').toggleClass('mini-navbar');
            }
            // Minimalize menu
            $('.navbar-minimalize').on('click', function () {

                $('body').toggleClass('mini-navbar');
                var $cookiesData = {};
                if($('body').hasClass('mini-navbar')){
                    $cookiesData.param = {isMiniNav:true};
                }else{
                    $cookiesData.param = {isMiniNav:false};
                }
                Cookies.set(that._cookiesMark, $cookiesData);
                that.smoothlyMenu();
            });
        }
        ,smoothlyMenu:function() {
            if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
                // Hide menu in order to smoothly turn on when maximize menu
                $('#side-menu').hide();
                // For smoothly turn on menu
                setTimeout(
                    function () {
                        $('#side-menu').fadeIn(400);
                    }, 200);
            } else if ($('body').hasClass('fixed-sidebar')) {
                $('#side-menu').hide();
                setTimeout(
                    function () {
                        $('#side-menu').fadeIn(400);
                    }, 100);
            } else {
                // Remove all inline style from jquery fadeIn function to reset menu state
                $('#side-menu').removeAttr('style');
            }
        }
        //路由
        , initRoute:function () {
            var that = this;
            Router.route('/', function() {//项目列表
                that.projectList();
            });
            Router.route('/addProject', function() {//项目立项
                that.addProject();
            });

            /**********************************我的任务--开始*****************************/
            Router.route('/myTask', function() {//我的任务
                that.myTask();
            });
            Router.route('/myTask/taskIssue', function(query) {//我的任务-任务签发
                that.taskIssueByMyTask(query);
            });
            Router.route('/myTask/production', function(query) {//我的任务-生产安排
                that.productionByMyTask(query);
            });
            Router.route('/myTask/designReview', function(query) {//我的任务-设、校、审
                that.designReviewByMyTask(query);
            });
            Router.route('/myTask/approved', function(query) {//我的任务-审批
                that.approvedByMyTask(query);
            });
            Router.route('/myTask/cost', function(query) {//我的任务-收支管理
                that.incomeExpenditureByMyTask(query);
            });

            /**********************************我的任务--结束*****************************/


            /**********************************项目详情--开始*****************************/
            Router.route('/projectDetails/basicInfo', function(query) {
                that.projectDetails(query,'basicInfo');
            });
            Router.route('/projectDetails/taskIssue', function(query) {
                that.projectDetails(query,'taskIssue');
            });
            Router.route('/projectDetails/productionArrangement', function(query) {
                that.projectDetails(query,'productionArrangement');
            });
            Router.route('/projectDetails/incomeExpenditure', function(query) {
                that.projectDetails(query,'incomeExpenditure');
            });
            Router.route('/projectDetails/projectDocumentLib', function(query) {
                that.projectDetails(query,'projectDocumentLib');
            });
            Router.route('/projectDetails/projectMember', function(query) {
                that.projectDetails(query,'projectMember');
            });
            Router.route('/projectDetails/externalCooperation', function(query) {
                that.projectDetails(query,'externalCooperation');
            });
            Router.route('/projectDetails/cost', function(query) {
                that.projectDetails(query,'cost');
            });

            /**********************************项目详情--结束*****************************/

            Router.route('/paymentsDetail', function() {//收支明细
                that.paymentsDetail();
            });

            Router.route('/paymentsStatistics', function() {//分类统计
                that.paymentsStatistics();
            });

            Router.route('/profitStatement', function() {//利润报表
                that.profitStatement();
            });

            Router.route('/financeSettings', function() {//财务类别设置
                that.financeSettings();
            });
            Router.route('/feeEntry', function() {//费用录入
                that.feeEntry();
            });

            Router.route('/projectCost', function() {//项目收支
                that.projectCost();
            });
            Router.route('/invoiceSummary', function() {//发票汇总
                that.invoiceSummary();
            });

            Router.route('/workingHoursSummary/detail', function(query) {//工时汇总详情
                that.workingHoursSummaryDetail(query);
            });
            Router.route('/orgInfomationShow', function() {//组织信息
                that.orgInfomationShow();
            });
            Router.route('/addressBook', function() {//通讯录
                that.addressBook();
            });
            Router.route('/projectArchiving', function() {//项目文档
                that.projectArchiving();
            });


            /**********************************审批管理--开始*****************************/
            Router.route('/initiateApproval', function() {//发起审批
                that.initiateApproval();
            });
            Router.route('/applied', function() {//我申请的
                that.approveDataList(1);
            });
            Router.route('/waitingMeApprove', function() {//待我审批
                that.approveDataList(2);
            });
            Router.route('/haveApproved', function() {//我已审批
                that.approveDataList(3);
            });
            Router.route('/ccMy', function() {//我已审批
                that.approveDataList(4);
            });

            Router.route('/approvalReport', function() {//审批报表
                that.approvalReport();
            });
            Router.route('/approvalReport/reimbursement', function() {//报销汇总
                that.approvalReport('reimbursement');
                that.menuShowOrHide(0);
            });
            Router.route('/approvalReport/cost', function() {//费用汇总
                that.approvalReport('cost');
                that.menuShowOrHide(0);
            });

            Router.route('/approvalReport/leave', function() {//请假汇总
                that.approvalReport('leave');
                that.menuShowOrHide(0);
            });

            Router.route('/approvalReport/business', function() {//出差汇总
                that.approvalReport('business');
                that.menuShowOrHide(0);
            });
            Router.route('/approvalReport/workingHours', function() {//工时汇总
                that.approvalReport('workingHours');
                that.menuShowOrHide(0);
            });
            Router.route('/approvalReport/workingHoursDetail', function(query) {//工时汇总详情
                that.approvalReport('workingHoursDetail',query);
                that.menuShowOrHide(0);
            });

            /**********************************审批管理--结束*****************************/


            /**********************************后台管理--开始*****************************/
            Router.route('/backstageMgt/orgInfo', function() {//组织信息
                that.backstageMgt('orgInfo');
                that.menuShowOrHide(0);
            });
            Router.route('/backstageMgt/organizational', function() {//组织架构
                that.backstageMgt('organizational');
                that.menuShowOrHide(0);
            });
            Router.route('/backstageMgt/permissionSettings', function() {//权限设置
                that.backstageMgt('permissionSettings');
                that.menuShowOrHide(0);
            });
            Router.route('/backstageMgt/enterpriseCertification', function() {//企业认证
                that.backstageMgt('enterpriseCertification');
                that.menuShowOrHide(0);
            });
            Router.route('/backstageMgt/historicalDataImport', function() {//历史数据导入
                that.backstageMgt('historicalDataImport');
                that.menuShowOrHide(0);
            });
            Router.route('/backstageMgt/financeSettingProcess', function(query) {//项目收支流程设置
                that.backstageMgt('financeSettingProcess',query);
                that.menuShowOrHide(0);
            });
            Router.route('/backstageMgt/approvalMgt', function() {//审批管理
                that.backstageMgt('approvalMgt');
                that.menuShowOrHide(0);
            });

            /**********************************后台管理--结束*****************************/

            /******************************* 头部操作路由-开始 *************************************/

            Router.route('/personalSettings', function() {//个人设置
                that.personalSettings();
                that.menuShowOrHide(0);
            });
            Router.route('/announcement', function() {//公告
                that.announcement();
                that.menuShowOrHide(0);
            });
            Router.route('/announcement/send', function() {//发送公告
                that.announcementSend();
                that.menuShowOrHide(0);
            });
            Router.route('/announcement/detail', function(query) {//发送公告
                that.announcementDetail(query);
                that.menuShowOrHide(0);
            });
            Router.route('/createOrg', function() {//创建组织
                that.createOrg();
                that.menuShowOrHide(1);
            });
            Router.route('/messageCenter', function() {//消息中心
                that.messageCenter();
                that.menuShowOrHide(0);
            });

            /******************************* 头部操作路由-结束 *************************************/

            Router.afterCallback(function () {
                console.log(Router.currentUrl);
                if(Router.currentUrl!='/createOrg'){
                   /* $('#page-wrapper').removeClass('menu-l-none');
                    $('#left-menu-box').show();*/
                }
                that.menuDealFun(Router.currentUrl);
            });

        }

        //菜单点击事件
        , menuClickFun:function () {
            var that = this;
            $(that.element).find('.metismenu li:not(.navbar-minimalize) a').on('click',function () {

                var $this = $(this);
                var id = $this.attr('id');

                if($this.parent().find('ul').length>0 && !$this.parent().hasClass('project-menu-box')){

                    $this.parent().toggleClass('selected');
                    $this.parent().find('ul').toggleClass('in');

                }else{

                    $this.parent().toggleClass('active').siblings().removeClass('active selected').find('li').removeClass('active selected');
                    $this.parent().siblings().find('ul').removeClass('in');

                    if($this.parents('.nav-second-level').length>0){
                        $this.parents('.selected').siblings().removeClass('active selected').find('li').removeClass('active selected');
                        $this.parents('.selected').siblings().find('ul').removeClass('in');
                    }

                    if($(that.element).find('.project-menu-box ul').length>0){
                        $(that.element).find('.project-menu-box ul').remove();//清除最近浏览的项目详情
                    }
                }
            });
        }
        //菜单hover事件
        , menuHoverFun:function () {
            var that = this;
            $(that.element).find('.metismenu li a').hover(function () {
                $(this).find('object').contents().find('svg text[fill],svg path[fill],svg polygon[fill]').attr('fill','#4765a1')
            },function () {
                $(this).find('object').contents().find('svg text[fill],svg path[fill],svg polygon[fill]').attr('fill','#666666')
            });
        }
        , menuDealFun:function (dataAction) {
            var that = this;
            var currentEle = $(that.element).find('.metismenu li a[href="#'+dataAction+'"]');
            var dataActionArr = dataAction.split('/');
            if(currentEle.length==0){
                var  newDataAction = '/'+dataActionArr[1];
                currentEle = $(that.element).find('.metismenu li a[href="#'+newDataAction+'"]');
            }
            if(currentEle.length>0){
                if(currentEle.parents('.nav-second-level').length>0){//定位在子菜单中
                    currentEle.parents('.nav-second-level').addClass('in').parent().addClass('selected');
                }
                currentEle.parent().addClass('active').siblings().removeClass('active');
            }
            if(!(dataAction.indexOf('/projectDetails/')>-1)){//当前不是项目详情

                //清除最近浏览的项目详情
                if($(that.element).find('.project-menu-box ul').length>0){
                    $(that.element).find('.project-menu-box').removeClass('selected').find('ul').remove();
                }
            }
        }
        //控制菜单显示与否
        , menuShowOrHide:function (t) {
            if(t==1){
                $('#page-wrapper').addClass('menu-l-none');

            }else{

                //添加z-index-102,侧栏效果自然点,滑动完删除class(z-index-102)
                $('#page-wrapper').addClass('z-index-102');
                $('#page-wrapper').removeClass('menu-l-none');
                var t = setTimeout(function () {
                    $('#page-wrapper').removeClass('z-index-102');
                    clearTimeout(t);
                },500);

                //折叠左菜单
                $('#left-menu-box').find('ul').removeClass('in');
                $('#left-menu-box').find('li').removeClass('active selected');
            }
        }
        //二级菜单没有就删除父菜单
        , removeNoMenuUl:function () {
            var that = this;
            $(that.element).find('ul.nav-second-level').each(function () {
                var len = $(this).find('li').length;
                if(len==0){
                    $(this).parents('li').remove();
                }
            })
        }
        //我的项目
        , projectList:function () {
            var options = {}, that = this;
            /*options.isAllProject = true;
            $(that.settings.$contentEle).m_projectList(options, true);*/
            $(that.settings.$contentEle).m_projectList_menu();
        }

        //通讯录
        , addressBook:function () {
            var that = this;
            $(that.settings.$contentEle).m_addressBook();
        }
        //组织信息(可查看)
        , orgInfomationShow: function () {
            var that = this;
            $(that.settings.$contentEle).m_orgInfomation({$type:0});
        }
        , addProject:function () {
            var that = this;
            $(that.settings.$contentEle).m_projectAdd({}, true);
        }
        //后台管理
        , backstageMgt: function (dataAction,query) {
            var that = this;
            $(that.settings.$contentEle).m_org_menu({
                $dataAction:dataAction,
                $query:query
            });
        }
        //项目详情
        , projectDetails:function (query,dataAction) {
            var that = this;
            var option = {};
            option.$projectId = query.id;
            option.$projectName = decodeURI(query.projectName);
            option.$projectId = query.id;
            option.$type = query.type;
            option.$dataAction = dataAction;
            $('.m_metismenu .project-menu-box').m_projectMenu(option);
        }
        //消息中心
        , messageCenter:function () {
            var that = this;
            var option = {};
            $(that.settings.$contentEle).m_message(option,true);
        }
        //个人设置
        , personalSettings:function () {
            //获取用户信息数据
            var option = {};
            option.url = restApi.url_userInfo;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {
                    $('#content-right').m_userInfo({userDto: response.data});
                } else {
                    S_layer.error(response.info);
                }
            });
        }
        //通知公告
        , announcement:function () {
            var that = this;
            var option = {};
            $(that.settings.$contentEle).m_notice(option,true)
        }
        , announcementSend:function () {
            var that = this;
            //$(that.settings.$contentEle).m_publishPublicNotice();
            $(that.settings.$contentEle).m_notice_publish();
        }
        , announcementDetail:function (query) {
            var options = {};
            options.noticeId = query.id;
            $('#content-right').m_showNoticeDetail(options);
        }
        , myTask:function () {
            var that = this;
            $(that.settings.$contentEle).m_myTask({}, true);
        }
        //财务类别设置
        , financeSettings:function () {
            var that = this;
            //$(that.settings.$contentEle).m_expTypeSetting();
            $(that.settings.$contentEle).m_finance_settings_menu();
        }

        //费用录入
        , feeEntry:function () {
            var that = this;
            $(that.settings.$contentEle).m_feeEntry();
        }
        //收支总览
        , paymentsDetail:function () {
            var that = this;
            $(that.settings.$contentEle).m_payments_detail_menu();
        }
        //分类统计
        , paymentsStatistics:function () {
            var that = this;
            $(that.settings.$contentEle).m_payments_statistics();
        }
        //利润报表
        , profitStatement:function () {
            var that = this;
            $(that.settings.$contentEle).m_payments_profitStatement();
        }
        //项目归档
        , projectArchiving:function () {
            var that = this;
            $(that.settings.$contentEle).m_projectArchiving({},true);
        }
        //项目总览
        , projectOverview:function () {
            var options = {}, that = this;
            options.isAllProject = true;
            $(that.settings.$contentEle).m_projectList(options, true);
        }
        //创建组织
        , createOrg:function () {
            var options = {};
            options.saveOrgCallback = function (data) {
                var url = '/iWork/home/workbench';
                window.location.href = window.rootPath + url;
            };
            $('#content-right').m_createOrg(options);
        }
        //我的任务-任务签发
        , taskIssueByMyTask:function (query) {
            var option = {};
            option.$projectId = query.projectId;
            option.$projectName = decodeURI(query.projectName);
            $('#content-right').m_myTask_taskIssue(option);
        }
        //我的任务-生产安排
        , productionByMyTask:function (query) {
            var option = {};
            option.$projectId = query.projectId;
            option.$projectName = decodeURI(query.projectName);
            $('#content-right').m_myTask_production(option);
        }
        //我的任务-设、校、审
        , designReviewByMyTask:function (query) {
            var option = {};
            option.$projectId = query.projectId;
            option.$projectName = decodeURI(query.projectName);
            option.$enterType = 'designer';
            $('#content-right').m_myTask_production(option);
        }
        //我的任务-审批
        , approvedByMyTask:function (query) {
            var option = {};
            option.$projectId = query.projectId;
            option.$projectName = decodeURI(query.projectName);
            option.$enterType = 'approved';
            $('#content-right').m_myTask_production(option);
        }
        //我的任务-收支管理
        , incomeExpenditureByMyTask:function (query) {
            var option = {};
            option.projectId = query.projectId;
            option.projectName = decodeURI(query.projectName);
            option.dataAction = query.dataType;
            option.myTaskId = query.myTaskId;
            $('#content-right').m_cost_menu(option,true);
        }

        //项目收支
        ,projectCost:function () {
            var options = {}, that = this;
            $(that.settings.$contentEle).m_payments_projectCost(options, true);
        }
        //发票汇总
        ,invoiceSummary:function () {
            var options = {}, that = this;
            $(that.settings.$contentEle).m_summary_invoice(options, true);
        }

        //发起审批
        ,initiateApproval:function () {
            var options = {}, that = this;
            $(that.settings.$contentEle).m_approval_initiate(options, true);
        }
        //审批报表
        ,approvalReport:function (dataAction,query) {
            var options = {}, that = this;
            options.dataAction=dataAction;
            options.query = query;
            $(that.settings.$contentEle).m_approvalReport_menu(options,true);
        }
        //1=我申请的,2=待我审批,3=我已审批,4=抄送我的
        ,approveDataList:function (type) {
            var options = {}, that = this;
            options.doType=type;
            $(that.settings.$contentEle).m_approval_data(options, true);
        }


    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {

            //if (!$.data(this, "plugin_" + pluginName)) {
            $.data(this, "plugin_" +
                pluginName, new Plugin(this, options));
            //}
        });
    };

})(jQuery, window, document);
