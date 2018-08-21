/**
 * 项目左边菜单导航
 * Created by wrb on 2016/12/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_projectMenu",
        defaults = {
            $projectId:null,//项目ID
            $projectName:null,//项目名称
            $isFirstEnter:null,//是否是第一次进来
            $dataAction:null,
            $type:null//标签页标识
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._contractPaymentFlag = null;
        this._technicalReviewFlag = null;
        this._cooperativeDesignFlag = null;
        this._otherFeeFlag = null;
        this._projectDeleteFlag = null;
        this._projectEditFlag = null;
        this._managerFlag = null;

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            that.getRoleFun(function () {

                var html = template('m_project/m_projectMenu',{
                    id:that.settings.$projectId,
                    projectName:encodeURI(that.settings.$projectName),
                    contractPaymentFlag:that._contractPaymentFlag,
                    technicalReviewFlag:that._technicalReviewFlag,
                    cooperativeDesignFlag:that._cooperativeDesignFlag,
                    otherFeeFlag:that._otherFeeFlag
                });
                //rolesControl();
                $(that.element).find('ul.nav-second-level').remove();
                $(that.element).append(html);
                $(that.element).addClass('chosed');//添加选中标识

                if(that.settings.$dataAction!=null){
                    that.switchPage(that.settings.$dataAction);
                    var currentEle = $('.m_metismenu li a[id="'+that.settings.$dataAction+'"]');
                    if(currentEle.length>0){
                        $('.m_metismenu li').removeClass('active');
                        currentEle.parent().addClass('active');
                    }
                }else{
                    $(that.element).find('.m_metismenu li:first a').click();
                }
                that.menuClickFun();

            })
        }
        ,getRoleFun:function (callBack) {
            var that = this;
            var option={};
            option.url=restApi.url_getProjectNavigationRole+'/'+that.settings.$projectId;
            m_ajax.get(option,function (response) {
                if(response.code=='0'){
                    that._contractPaymentFlag = response.data.flag1;
                    that._technicalReviewFlag = response.data.flag2;
                    that._cooperativeDesignFlag = response.data.flag3;
                    that._otherFeeFlag = response.data.flag4;
                    that._projectDeleteFlag = response.data.deleteFlag;
                    that._projectEditFlag = response.data.editFlag;
                    that._managerFlag = response.data.managerFlag;
                    if(response.data.projectName!=null && response.data.projectName!=''){
                        that.settings.$projectName = response.data.projectName;
                    }
                    if(callBack!=null){
                        callBack();
                    }

                }else {
                    S_dialog.error(response.info);
                }
            })
        }

        //菜单点击事件
        , menuClickFun:function () {
            var that = this;
            $(that.element).find('.nav-second-level li a').off('click').on('click',function (e) {
                var id = $(this).attr('id');
                $('.m_metismenu li').removeClass('active');
                $(this).parent().addClass('active');
                stopPropagation(e);
            });
        }
        //切换页面
        , switchPage: function (dataAction) {
            var that = this;
            switch (dataAction) {
                case 'basicInfo':
                    that.projectBasicInfo();
                    break;
                case 'taskIssue':
                    that.taskIssue();
                    break;
                case 'productionArrangement':
                    that.productionArrangement();
                    break;
                case 'contractPayment':
                    that.contractPayment();
                    break;
                case 'technicalReviewFee':
                    that.technicalReviewFee();
                    break;
                case 'cooperativeDesignFee':
                    that.cooperativeDesignFee();
                    break;
                case 'otherFee':
                    that.otherFee();
                    break;
                case 'projectDocumentLib':
                    that.projectDocumentLib();
                    break;
                case 'projectMember':
                    that.projectMember();
                    break;
                case 'projectDynamic':
                    that.projectDynamic();
                    break;
                case 'externalCooperation':
                    that.externalCooperation();
                    break;
                case 'taskIssueOverview':
                    that.taskIssueOverview();
                    break;
                case 'productionArrangementOverview':
                    that.productionArrangementOverview();
                    break;
                case 'incomeExpenditure':
                    that.incomeExpenditure();
                    break;
                case 'cost':
                    that.cost();
                    break;
                default:
                    dataAction = 'basicInfo';
                    that.projectBasicInfo();
                    break;
            }
        }
        //项目基本信息
        , projectBasicInfo: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            option.$editFlag = that._projectEditFlag;
            option.$deleteFlag = that._projectDeleteFlag;
            option.$renderCallBack = function () {
                if(that._projectDeleteFlag!=1){//不存在删除项目的权限，删除此按钮
                    $('#content-right a[data-action="deleteProject"]').remove();
                }
            };
            $('#content-right').m_projectBasicInfo(option,true);
        }
        //任务签发
        , taskIssue: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_taskIssue(option,true);
        }
        //生产安排
        , productionArrangement: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_production(option,true);
        }
        //合同回款
        , contractPayment: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_contractPayment(option,true);
        }
        //技术审查费
        , technicalReviewFee: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_technicalReviewFee(option,true);
        }
        //合作设计费
        , cooperativeDesignFee: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_cooperativeDesignFee(option,true);
        }
        //其他费用
        , otherFee: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_otherFee(option,true);
        }
        //项目文档
        , projectDocumentLib: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_docmgr(option,true);
        }
        //项目成员
        , projectMember: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_projectMember(option,true);
        }
        //项目动态
        , projectDynamic: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_projectDynamic(option,true);
        }
        //外部合作
        , externalCooperation:function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            option.$isManager = that._managerFlag;
            $('#content-right').m_projectExternalCooperation(option,true);
        }
        //签发总览
        , taskIssueOverview:function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_taskIssue_overview(option,true);
        }
        //生产安排总览
        , productionArrangementOverview:function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_production_overview(option,true);
        }
        //收支管理
        , incomeExpenditure:function () {

            var option = {}, that = this;
            that.getRoleFun(function () {

                option.$projectId = that.settings.$projectId;
                option.$projectName = that.settings.$projectName;
                option.$roleFlag = {
                    contractPaymentFlag : that._contractPaymentFlag,
                    technicalReviewFlag : that._technicalReviewFlag,
                    cooperativeDesignFlag : that._cooperativeDesignFlag,
                    otherFeeFlag : that._otherFeeFlag
                };
                option.$type = that.settings.$type;
                $('#content-right').m_incomeExpenditure(option);
            });

            return false;
        }
        //收支管理
        ,cost:function () {
            var option = {}, that = this;
            option.projectId = that.settings.$projectId;
            option.projectName = that.settings.$projectName;
            option.dataAction = that.settings.$type;
            $('#content-right').m_cost_menu(option,true);
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


