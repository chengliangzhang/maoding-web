/**
 * 项目信息－付款详情
 * Created by wrb on 2018/8/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_cost_paymentDetails",
        defaults = {
            isDialog:true,
            projectId:null,
            pointDetailId:null,//记录ID
            saveCallBack:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderDialog(function () {

                that.renderPage();
            });
        },
        //初始化数据,生成html
        renderDialog:function (callBack) {

            var that = this;
            if(that.settings.isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title||'付款详情',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '700',
                    minHeight: that.settings.dialogHeight||'300',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancelText:'关闭',
                    cancel:function () {

                    }
                },function(d){//加载html后触发
                    that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                    if(callBack!=null)
                        callBack();

                });
            }else{//不以弹窗编辑
                if(callBack!=null)
                    callBack();
            }
        }
        /**
         * 渲染界面
         */
        ,renderPage:function () {
            var that = this;
            var option  = {};
            option.classId = that.element;
            option.url = restApi.url_getProjectCostPaymentDetailByPointDetailIdForPay;
            option.postData = {
                pointDetailId : that.settings.pointDetailId
            };

            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){

                    var html = template('m_cost/m_cost_paymentDetails',response.data);
                    $(that.element).html(html);

                }else {
                    S_dialog.error(response.info);
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
