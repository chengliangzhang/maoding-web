/**
 * 项目收支流程-技术审查费设置
 * Created by wrb on 2018/7/20.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_process_finance_setting_technicalReviewFee",
        defaults = {
            $processType:null,//流程类型
            $processId:null//流程ID
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._selectedOrg = null;//当前选中组织
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            var option  = {};
            option.classId= '#content-box';
            option.url = restApi.url_listProcessNode;
            option.postData = {
                processId:that.settings.$processId
            };
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){

                    var html = template('m_process/m_process_finance_setting_technicalReviewFee',{listProcessNode:response.data});
                    $(that.element).html(html);
                    that.bindActionClick();
                    that.initICheck();

                }else {
                    S_layer.error(response.info);
                }
            });


        }
        //初始化iCheck
        ,initICheck:function () {
            var that = this;
            var ifChecked = function (e) {
                var param = {};
                param.status = 1;
                param.nodeId = $(this).closest('tr').attr('data-id');
                param.processId = $(this).closest('tr').attr('data-process-id');
                param.statusType = $(this).attr('data-type');
                that.saveChangeSelectionStatus(param);
            };
            var ifUnchecked = function (e) {
                var param = {};
                param.status = 0;
                param.nodeId = $(this).closest('tr').attr('data-id');
                param.processId = $(this).closest('tr').attr('data-process-id');
                param.statusType = $(this).attr('data-type');
                that.saveChangeSelectionStatus(param);
            };
            $(that.element).find('input[name="iCheck"]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);
        }
        //更改选择状态
        ,saveChangeSelectionStatus:function (param) {
            var that = this;
            var option = {};
            option.classId = '#content-right';
            option.url = restApi.url_selectedProcessNodeStatus;
            option.postData = param;
            m_ajax.postJson(option, function (response) {
                if(response.code=='0'){
                    S_toastr.success('操作成功！');
                }else {
                    S_layer.error(response.info);
                }
            });
        }
        //事件绑定
        , bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action],button[data-action]').off('click').on('click',function (e) {
                var $this = $(this),dataAction = $this.attr('data-action');
                var dataActionType = $this.attr('data-action-type');
                var dataId = $this.attr('data-id');
                switch (dataAction){
                    case 'back'://返回项目收支流程

                        window.history.go(-1);
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
