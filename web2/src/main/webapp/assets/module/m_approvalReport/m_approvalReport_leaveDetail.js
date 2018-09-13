/**
 * 请假详情
 * Created by wrb on 2017/12/26.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approvalReport_leaveDetail",
        defaults = {
            $title:null,//弹窗标题
            $url:null,//请求地址
            $type:3,//默认3=请假,4=出差
            $detailData:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._typeStr = this.settings.$type==3?'请假':'出差';
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.getExpData();
        }
        //加载弹窗
        ,renderDialog: function (html) {
            var that = this;
            S_layer.dialog({
                title: that.settings.$title||'请假申请明细',
                area : '750px',
                maxHeight:600,
                content:html,
                cancelText:'关闭',
                cancel:function () {

                }

            },function(layero,index,$dialogEle){//加载html后触发
            });
        }
        //加载
        ,getExpData:function(){
            var that = this;
            var option = {};
            option.url=that.settings.$url;
            option.postData={};
            option.postData.id=that.settings.$detailData.id;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    var $data={};
                    $data.detailData = response.data;
                    $data.fastdfsUrl = window.fastdfsUrl;
                    $data.type = that.settings.$type;
                    $data.typeStr = that._typeStr;
                    var html = template('m_approvalReport/m_approvalReport_leaveDetail',$data);
                    that.renderDialog(html);

                }else {
                    S_layer.error(response.info);
                }
            })
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
