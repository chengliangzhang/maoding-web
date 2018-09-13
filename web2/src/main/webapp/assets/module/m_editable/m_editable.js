/**
 *
 * Created by wrb on 2018/9/13.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_editable",
        defaults = {
            type:1//type=1=input
            ,inline:false
            ,placement:'bottom'
            ,ok:null
            ,cancel:null
            ,hideElement:false

        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;

        this._placeholder = $(this.element).attr('placeholder');
        if(this._placeholder==undefined)
            this._placeholder = '请输入';

        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.render();
        }
        , render: function () {
            var that = this;
            var content = that.renderEditTypeHtml();
            var iHtml = template('m_editable/m_editable',{content:content});

            if(isNullOrBlank(that.settings.value))
                $(that.element).html('<span class="fc-v1-grey">未设置</span>');


            $(that.element).off('click').on('click',function (e) {

                if(that.settings.hideElement)
                    $(that.element).hide();

                $(that.element).m_floating_popover({
                    content:iHtml,
                    placement:that.settings.placement,
                    type:3,
                    scrollClose:false,
                    //quickClose:false,
                    popoverClass:'editable-inline',
                    hideElement:true,
                    closed:function () {
                        $(that.element).show();
                    },
                    renderedCallBack:function ($popover) {

                        that.bindActionClick($popover);
                        $popover.find('input[name="editable_input"]').off('focus keyup').on('focus keyup',function () {
                            if($.trim($(this).val())!=''){
                                $popover.find('span.m-editable-clear').show();
                            }else{
                                $popover.find('span.m-editable-clear').hide();
                            }
                        });
                        $popover.find('input[name="editable_input"]').focus();
                        $popover.find('span.m-editable-clear').off('click').on('click',function () {
                            $popover.find('input[name="editable_input"]').val('');
                            $popover.find('span.m-editable-clear').hide();
                        });
                    }

                },true);
                e.stopPropagation();
            })

        }
        ,renderEditTypeHtml:function () {
            var that = this,html = '';
            switch (that.settings.type){
                case 1:
                    html = template('m_editable/m_editable_input',{placeholder:that._placeholder,value:that.settings.value});
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
            return html;
        }
        ,bindActionClick:function ($popover) {
            var that = this;
            $popover.find('button.m-editable-submit').off('click').on('click',function () {

                var flag = true;
                if(that.settings.ok)
                    flag = that.settings.ok();

                if(flag!==false)
                    $(that.element).m_floating_popover('closePopover');

            });
            $popover.find('button.m-editable-cancel').off('click').on('click',function () {
                $(that.element).m_floating_popover('closePopover');
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
