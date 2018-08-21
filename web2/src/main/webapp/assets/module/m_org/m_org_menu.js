/**
 * 操作－左菜单
 * Created by wrb on 2016/12/16.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_org_menu",
        defaults = {
            $contentEle:null,
            $dataAction:null,
            $query:null,//URL参数
            $isFirstEnter:false//是否是第一次進來
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData();
        }
        //初始化数据并加载模板
        ,initHtmlData:function () {
            var that = this;

            var html = template('m_org/m_org_menu',{adminFlag:window.adminFlag});
            $(that.element).html(html);
            rolesControl();
            that.menuClickFun();
            if(that.settings.$dataAction!=null){
                that.switchPage(that.settings.$dataAction);
                $(that.element).find('ul.secondary-menu-ul li[id="'+that.settings.$dataAction+'"]').addClass('active').siblings().removeClass('active');
            }else{
                $(that.element).find('ul.secondary-menu-ul li:first a').click();
            }
        }
        //菜单点击事件
        , menuClickFun:function () {
            var that = this;
            $(that.element).find('.secondary-menu-ul li a').on('click',function () {
                var id = $(this).parent().attr('id');
                $(this).parent().addClass('active').siblings().removeClass('active');
            });
        }
        //tab页切换
        , switchPage: function (dataAction) {

            var that = this;
            switch (dataAction) {
                case 'organizational'://组织架构
                    that.organizational();
                    break;
                case 'orgInfo'://组织信息编辑
                    that.orgInfomationEdit();
                    break;
                case 'addressBook'://通讯录
                    that.addressBook();
                    break;
                case 'orgInfomationShow'://组织信息
                    that.orgInfomationShow();
                    break;
                case 'permissionSettings'://权限设置
                    that.permissionSettings();
                    break;
                case 'enterpriseCertification'://企业认证
                    that.enterpriseCertification();
                    break;
                case 'historicalDataImport'://历史数据导入
                    that.historicalDataImport();
                    break;
                case 'financeSettingProcess'://项目收支流程设置
                    that.financeSettingProcess();
                    break;
                case 'approvalMgt'://审批管理
                    that.approvalMgt();
                    break;
                default:
                    dataAction = 'organizational';
                    that.organizational();
                    break;
            }
        }
        //组织架构
        , organizational: function () {
            var that = this;
            $(that.element).find('#content-box').m_organizational();
        }
        //组织信息(可编辑)
        , orgInfomationEdit: function () {
            var that = this;
            $(that.element).find('#content-box').m_orgInfomation({$type:1});
        }
        //组织信息(可查看)
        , orgInfomationShow: function () {
            var that = this;
            $(that.element).find('#content-box').m_orgInfomation({$type:0});
        }
        //通讯录
        , addressBook: function () {
            var that = this;
            $(that.element).find('#content-box').m_addressBook();
        }
        //权限设置
        ,permissionSettings:function () {
            var that = this;
            var option = {};
            option.isAddUser = 1;
            $(that.element).find('#content-box').m_roleList(option);
        }
        //企业认证
        ,enterpriseCertification:function () {
            var that = this;
            var option = {};
            $(that.element).find('#content-box').m_enterpriseCertification(option);
        }
        //历史数据导入
        ,historicalDataImport:function () {
            var that = this;
            $(that.element).find('#content-box').m_historyData({}, true);
        }
        //项目收支流程设置
        ,financeSettingProcess:function () {
            var that = this;
            var query  = that.settings.$query;
            if(query!=null && query.processType=='2'){//项目收支流程-合同回款设置

                $(that.element).find('#content-box').m_process_finance_setting_contractPayment({
                    $processType:query.processType,
                    $processId:query.processId
                }, true);

            }else if(query!=null && (query.processType=='3' || query.processType=='4')){//项目收支流程-技术审查费设置

                $(that.element).find('#content-box').m_process_finance_setting_technicalReviewFee({
                    $processType:query.processType,
                    $processId:query.processId
                }, true);

            }else{
                $(that.element).find('#content-box').m_process_finance_setting({}, true);
            }

        }
        //审批管理
        ,approvalMgt:function () {
            var that = this;
            $(that.element).find('#content-box').m_approval_mgt({}, true);
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
