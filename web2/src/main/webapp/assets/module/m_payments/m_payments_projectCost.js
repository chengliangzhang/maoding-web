/**
 * 项目－收支明细
 * Created by wrb on 2018/8/8.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_projectCost",
        defaults = {
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
        this._selectedOrg = null;//当前组织筛选-选中组织对象
        this._filterTimeData = {};//时间筛选
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
            var html = template('m_payments/m_payments_projectCost',{});
            $(that.element).html(html);
            var option = {};
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                that.renderList();
            };
            option.$renderCallBack = function () {

            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);

            var timeOption = {};
            timeOption.selectTimeCallBack = function (data) {
                console.log(data);
                that._filterTimeData = data;
                that.renderList();
            };
            $(that.element).find('.time-combination').m_filter_timeCombination(timeOption,true);
        }

        ,renderList:function () {
            var that = this;
            var option = {};
            option.param = {};
            option.param.startDate = that._filterTimeData.startTime;
            option.param.endDate = that._filterTimeData.endTime;
            option.param.companyId = that._selectedOrg.id;
            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-box',
                url: restApi.url_listProjectCostSummary,
                params: option.param
            }, function (response) {

                if (response.code == '0') {

                    var html = template('m_payments/m_payments_projectCost_list',{
                        dataList:response.data.data
                    });
                    $(that.element).find('.data-list-container').html(html);
                    that.bindActionClick();
                } else {
                    S_dialog.error(response.info);
                }
            });

        }
        //按钮事件绑定
        , bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action]').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){

                    case 'refreshBtn':
                        that.renderPage();
                        return false;
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
