/**
 * input输入筛选
 * Created by wrb on 2018/8/15.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_filter_input",
        defaults = {
            eleId:null,//元素ID
            align:null,//浮窗位置
            isClear:false,//是否显示清空按钮
            inputValue:null,//文本框值
            placeholder:null,
            oKCallBack:null//选择回调
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
            that.render();
        }
        , render: function () {
            var that = this;

            if(!isNullOrBlank(that.settings.inputValue)){
                $(that.element).find('i').addClass('fc-v1-blue');
            }



            $(that.element).on('click',function (e) {

                var iHtml = template('m_filter/m_filter_input',{
                    isClear:that.settings.isClear,
                    inputValue:that.settings.inputValue,
                    placeholder:that.settings.placeholder
                });
                $(that.element).m_floating_popover({
                    eleId:that.settings.eleId,
                    content:iHtml,
                    placement:'bottomRight',
                    renderedCallBack:function ($popover) {

                        that.bindBtnAction($popover);
                    }

                },true);

                e.stopPropagation();
                return false;
            });

        }

        //获取选中的数据
        ,bindBtnAction :function ($ele) {
            var that = this;

            $ele.find('button[data-action="sureFilter"]').off('click').on('click',function () {

                that.settings.inputValue = $ele.find('input[name="txtVal"]').val();

                $(that.element).m_floating_popover('closePopover');//关闭浮窗

                if(!isNullOrBlank(that.settings.inputValue))
                    $(that.element).find('i').addClass('fc-v1-blue');

                if(that.settings.oKCallBack)
                    that.settings.oKCallBack(that.settings.inputValue);
            });
            $ele.find('button[data-action="clearInput"]').off('click').on('click',function () {
                $ele.find('input[name="txtVal"]').val('');
                $(that.element).find('i').removeClass('fc-v1-blue');
                that.settings.inputValue = '';
                if(that.settings.oKCallBack)
                    that.settings.oKCallBack(that.settings.inputValue);
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
