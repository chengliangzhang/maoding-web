/**
 * 收支管理-菜单
 * Created by wrb on 2018/8/8.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_cost_menu",
        defaults = {
            projectId:null,
            projectName:null,
            myTaskId:null,//任务ID
            dataAction:null//记录页面的key
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this._currentCompanyUserId = window.currentCompanyUserId;//当前员工ID
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

            var html = template('m_cost/m_cost_menu',{
                id:that.settings.projectId,
                projectName:that.settings.projectName,
                projectNameCode:encodeURI(that.settings.projectName)
            });
            $(that.element).html(html);
            var dataAction = $(that.element).find('li:eq(0) a').attr('id');
            if(that.settings.dataAction!=null){
                dataAction = that.settings.dataAction;
            }

            that.switchPage(dataAction);
        }
        //菜单点击事件
        , menuClickFun:function () {
            var that = this;
            $(that.element).find('.secondary-menu-ul li a').on('click',function () {
                var id = $(this).parent().attr('id');
                $(this).parent().addClass('active').siblings().removeClass('active');
            });
        }
        //切换页面
        , switchPage: function (dataAction) {
            var that = this;
            switch (dataAction) {
                case 'costDetails'://收支明细
                    that.costDetails();
                    break;
                case 'collectionPlan'://收款计划
                    that.collectionPlan();
                    break;
                case 'paymentPlan'://付款计划
                    that.paymentPlan();
                    break;
                default:
                    dataAction = 'collectionPlan';
                    that.collectionPlan();
                    break;
            }

            $(that.element).find('ul.secondary-menu-ul li a[id="'+dataAction+'"]').parent().addClass('active').siblings().removeClass('active');
            var name = $(that.element).find('ul.secondary-menu-ul li.active a[id="'+dataAction+'"]').text();
            $(that.element).find('.breadcrumb li.active strong').text(name);

        }
        //收支明细
        ,costDetails:function () {
            var that = this;
            var options = {};
            $(that.element).find('#content-box').m_cost_details(options, true);
        }
        //收款计划
        ,collectionPlan:function () {
            var that = this;
            var options = {};
            options.projectId = that.settings.projectId;
            options.myTaskId = that.settings.myTaskId;
            $(that.element).find('#content-box').m_cost_collectionPlan(options, true);
        }
        //付款计划
        ,paymentPlan:function () {
            var that = this;
            var options = {};
            options.projectId = that.settings.projectId;
            options.myTaskId = that.settings.myTaskId;
            $(that.element).find('#content-box').m_cost_paymentPlan(options, true);
        }



    });

    /*
    1.一般初始化（缓存单例）： $('#id').pluginName(initOptions);
    2.强制初始化（无视缓存）： $('#id').pluginName(initOptions,true);
    3.调用方法： $('#id').pluginName('methodName',args);
    */
    $.fn[pluginName] = function (options, args) {
        var instance;
        var funcResult;
        var jqObj = this.each(function () {

            //从缓存获取实例
            instance = $.data(this, "plugin_" + pluginName);

            if (options === undefined || options === null || typeof options === "object") {

                var opts = $.extend(true, {}, defaults, options);

                //options作为初始化参数，若args===true则强制重新初始化，否则根据缓存判断是否需要初始化
                if (args === true) {
                    instance = new Plugin(this, opts);
                } else {
                    if (instance === undefined || instance === null)
                        instance = new Plugin(this, opts);
                }

                //写入缓存
                $.data(this, "plugin_" + pluginName, instance);
            }
            else if (typeof options === "string" && typeof instance[options] === "function") {

                //options作为方法名，args则作为方法要调用的参数
                //如果方法没有返回值，funcReuslt为undefined
                funcResult = instance[options].call(instance, args);
            }
        });

        return funcResult === undefined ? jqObj : funcResult;
    };

})(jQuery, window, document);
