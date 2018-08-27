/**
 * 项目－发票汇总
 * Created by wrb on 2018/8/8.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_summary_invoice",
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
        this._dataList = [];//分页数据

        /*********** 筛选字段 ***********/
        this._filterData = {
            companyId:null,
            startTime:null,
            endTime:null
        };
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
            var html = template('m_summary/m_summary_invoice',{});
            $(that.element).html(html);

            var option = {};
            option.$selectedCallBack = function (data) {
                that._filterData.companyId = data.id;
                that.renderList();
            };
            option.$renderCallBack = function () {

            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);

            var timeOption = {};
            timeOption.selectTimeCallBack = function (data) {
                console.log(data);
                that._filterData.startTime = data.startTime;
                that._filterData.endTime = data.endTime;
                that.renderList();
            };
            $(that.element).find('.time-combination').m_filter_timeCombination(timeOption,true);

        }

        ,renderList:function () {
            var that = this;
            var option = {};
            option.param = {};
            option.param = filterParam(that._filterData);
            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-box',
                url: restApi.url_listInvoice,
                params: option.param
            }, function (response) {

                if (response.code == '0') {
                    that._dataList = response.data.data;
                    var html = template('m_summary/m_summary_invoice_list',{
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
                var dataId = $this.closest('tr').attr('data-id');//当前元素赋予的ID
                //获取节点数据
                var dataItem = getObjectInArray(that._dataList,dataId);
                switch (dataAction){

                    case 'refreshBtn':
                        that.renderPage();
                        return false;
                        break;
                    case 'confirmInvoice'://确认开票
                        var option = {};
                        option.invoiceId = dataId;
                        option.taskId = dataItem.myTaskId;
                        option.projectId = dataItem.projectId;
                        option.dialogHeight = '100';
                        option.saveCallBack = function () {
                            that.renderList();
                        };
                        $('body').m_cost_confirmInvoice(option,true);
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
