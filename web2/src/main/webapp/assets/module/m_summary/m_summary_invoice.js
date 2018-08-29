/**
 * 项目－发票汇总
 * Created by wrb on 2018/8/8.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_summary_invoice",
        defaults = {
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
        this._dataList = [];//分页数据

        /*********** 筛选字段 ***********/
        this._filterData = {};
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_summary/m_summary_invoice',{});
            $(that.element).html(html);
            that.renderOrgTree();
            that.renderTimeSelectControl();
            that.bindActionClick();
        }

        //渲染组织树选择
        ,renderOrgTree:function () {
            var that = this;
            var option = {};
            option.$selectedCallBack = function (data) {
                that._filterData.companyId = data.id;
                that.renderListHeader();
            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);
        }
        //渲染时间筛选控件
        ,renderTimeSelectControl:function () {
            var that = this;
            var option = {};
            option.selectTimeCallBack = function (data) {

                that._filterData.startTime = data.startTime;
                that._filterData.endTime = data.endTime;
                that.renderListContent();
            };
            $(that.element).find('.time-combination').m_filter_timeGroup(option,true);
        }
        //渲染列表头部
        ,renderListHeader:function () {
            var that = this;
            var option = {};
            option.type = 2;
            option.renderCallBack = function (data) {
                that._headerList = data;
                that.renderListContent();
            };
            option.filterCallBack = function (data) {
                console.log(data);
                that._filterData = data;
                that.renderListContent();
            };
            $(that.element).find('.data-list-container table thead').m_field_list_header(option,true);
        }
        /**
         * 渲染list
         * @param t t==1刷新当前页
         */
        ,renderListContent:function (t) {
            var that = this;
            var option = {};
            option.url = restApi.url_listInvoice;
            option.headerList = that._headerList;
            option.param = that._filterData;

            var buttonMap = new Map();
            buttonMap.set('invoiceNo','确认开票_myTaskStatus');
            option.buttonMap = buttonMap;
            if(t==1)
                option.isRefreshCurrentPage = true;

            option.renderCallBack = function (data) {
                that._dataList = data.data;
                that.bindListActionClick();
            };
            $(that.element).find('.data-list-container table tbody').m_field_list_row(option,true);
        }
        //按钮事件绑定
        , bindListActionClick:function () {
            var that = this;
            $(that.element).find('.data-list-container button[data-action]').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                var dataId = $this.closest('tr').attr('data-id');//当前元素赋予的ID
                //获取节点数据
                var dataItem = getObjectInArray(that._dataList,dataId);
                switch (dataAction){

                    case 'invoiceNo_myTaskStatus'://确认开票
                        var option = {};
                        option.invoiceId = dataId;
                        option.taskId = dataItem.myTaskId;
                        option.projectId = dataItem.projectId;
                        option.dialogHeight = '100';
                        option.saveCallBack = function () {
                            that.renderListContent(1);
                        };
                        $('body').m_cost_confirmInvoice(option,true);
                        break;
                }
            });
        }
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('form button[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){

                    case 'refreshBtn'://刷新
                        that.init();
                        return false;
                        break;
                    case 'setField'://设置字段
                        var option = {};
                        option.type = 2;
                        option.dialogMinHeight = '250';
                        option.saveCallBack = function () {
                            that.renderListHeader();
                        };
                        $('body').m_field_settings(option,true);
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
