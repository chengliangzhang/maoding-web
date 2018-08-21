/**
 * 项目－收款计划
 * Created by wrb on 2018/8/8.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_cost_collectionPlan",
        defaults = {
            myTaskId:null,//任务ID
            projectId:null//项目ID
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this._currentCompanyUserId = window.currentCompanyUserId;//当前员工ID
        this._collectionPlan = null;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderPage();
        }
        //初始化数据,生成html
        ,renderPage:function () {

            var that = this;
            var option  = {};
            option.classId = that.element;
            option.url = restApi.url_listProjectCost;
            option.postData = {};
            option.postData.payType = '1';
            option.postData.projectId = that.settings.projectId;
            if(that.settings.myTaskId!=null)
                option.postData.myTaskId = that.settings.myTaskId;

            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    that._collectionPlan = response.data.costList;
                    var html = template('m_cost/m_cost_collectionPlan',{
                        dataList:that._collectionPlan,
                        isFinancial:response.data.isFinancial,
                        isManager:response.data.isManager
                    });
                    $(that.element).html(html);
                    that.bindActionClick();

                    if(that._collectionPlan!=null && that._collectionPlan.length>0){
                        $.each(that._collectionPlan,function (i,item) {

                            var option = {};
                            option.projectCost = item;
                            option.projectId = that.settings.projectId;
                            option.isAppend = true;
                            option.myTaskId = that.settings.myTaskId;
                            $(that.element).find('#collectionPlanBox').m_cost_collectionPlan_item(option,true);
                        });
                    }

                }else {
                    S_dialog.error(response.info);
                }
            })
        }

        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'addCollectionPlan'://添加收款计划

                        var option = {};
                        option.projectId = that.settings.projectId;
                        option.oKCallBack = function () {
                            that.renderPage();
                        };
                        $('body').m_cost_collectionPlan_add(option,true);

                        break;
                }
            });
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
