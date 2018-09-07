/**
 * 1=我申请的,2=待我审批,3=我已审批,4=抄送我的
 * Created by wrb on 2018/9/3.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_data",
        defaults = {
            doType: 1//1=我申请的,2=待我审批,3=我已审批,4=抄送我的
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._title = '';
        if(this.settings.doType==1){
            this._title = '我申请的';
        }else if(this.settings.doType==2){
            this._title = '待我审批';
        }else if(this.settings.doType==3){
            this._title = '我已审批';
        }else if(this.settings.doType==4){
            this._title = '抄送我的';
        }
        this._filterData = {};
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_approval/m_approval_data', {title:that._title});
            $(that.element).html(html);
            that.renderDataList();
        }

        //加载数据
        , renderDataList: function () {
            var that = this;

            var html = template('m_approval/m_approval_data_list', {});
            $(that.element).find('.data-list-container').html(html);

            var option = {};
            option.param = {};
            that._filterData.type=that.settings.doType;
            option.param = filterParam(that._filterData);
            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-container',
                url: restApi.url_getAuditData,
                params: option.param
            }, function (response) {
                // data为ajax返回数据
                if (response.code == '0') {

                    var html = template('m_approval/m_approval_data_list', {dataList:response.data.data});
                    $(that.element).find('.data-list-container').html(html);
                    that.bindTrClick();
                } else {
                    S_dialog.error(response.info);
                }
            });

        }
        ,bindTrClick:function () {
            var that = this;
            $(that.element).find('tr').off('click').on('click',function () {
                var $this = $(this);
                var type = $this.attr('data-type');
                var dataId = $this.attr('data-id');

                if(type==1 || type==2 ){

                    var option = {};
                    option.doType = type;
                    option.id = dataId;
                    $('body').m_approval_cost_details(option,true);

                }else if(type==3 || type==4){

                    var option = {};
                    option.doType = type;
                    option.id = dataId;
                    $('body').m_approval_leave_details(option,true);

                }else if(type==5){
                    var option = {};
                    option.doType = type;
                    option.id = dataId;
                    $('body').m_approval_payment_details(option,true);
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
