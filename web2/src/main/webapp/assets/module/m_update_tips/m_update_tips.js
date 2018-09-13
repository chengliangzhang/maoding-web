/**
 * Created by Wuwq on 2017/4/11.
 */
/**
 * Created by Wuwq on 2017/1/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_update_tips",
        defaults = {
            pageName:null,//组织加载的静态页面名称
            pageIndex:null,//页码
            pageUrl:null,//是否跳转的URL
            isAction:false//是否请求删除引导记录
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._render();
        }
        , _render: function () {
            var that = this;
            var page = that.settings.pageName!=null?that.settings.pageName:'m_update_tips';

            var html = template('m_update_tips/'+page, {});

            var layerIndex=layer.open({
                type: 1,
                title: false,
                closeBtn:false,
                area: ['1000px', '550px'],
                shade: 0.7,
                content: html,
                end:function () {
                    if(that.settings.isAction){
                        var option = {};
                        option.url = restApi.url_sys_complete_notify;
                        m_ajax.getJson(option, function (response) {
                            if (response.code == '0') {
                                if(that.settings.pageUrl!=null){
                                    //window.location.href = restApi.url_permissionSettings;
                                    window.location.href = that.settings.pageUrl;
                                }
                            } else {
                                S_layer.error(response.info);
                            }
                            layer.close(layerIndex);
                        })
                    }
                }
            });

            $('a[data-action="jumpOutWizard"],a[data-action="complete"]').click(function(){
                layer.close(layerIndex);
            });
            $('a[data-action="nextPage"]').click(function(){

                layer.close(layerIndex);
                var i = that.settings.pageIndex;
                var ni = i-0+1;
                that.settings.pageName = 'm_update_tips'+ni;
                that.settings.pageIndex = ni;
                that._render();

            });
            $('a[data-action="prevPage"]').click(function(){

                layer.close(layerIndex);
                var i = that.settings.pageIndex;
                var ni = i-0-1;
                that.settings.pageName = 'm_update_tips'+ni;
                that.settings.pageIndex = ni;
                that._render();
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