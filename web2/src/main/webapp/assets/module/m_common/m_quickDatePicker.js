/**
 * 快速选择时间，以在位编辑的方式打开
 * Created by veata on 2016/12/21.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_quickDatePicker",
        defaults = {
            $title:null,
            $placement:true,//浮窗展开的位置，共四个值‘top’,‘bottom’,‘left’,‘right’,
            $okCallBack:null,
            $isClear:true,//展示清空按钮
            $eleId:null//当前元素浮动窗ID
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData(function (data) {
                WdatePicker({eCont:'quickDatePicker',onpicked:function(dp){that.saveDate(dp)}});
                $('#quickDatePicker iframe').css('height','232px');//firefox有时不出来修改
            });
        },
        //初始化数据
        initHtmlData:function (callBack) {
            var that = this;
            var title = '变更合同签订日期';
            if(that.settings.$title!=null){
                title = that.settings.$title;
            }
            $(that.settings.$eleId).m_popover({
                placement: that.settings.$placement==null?'top':that.settings.$placement,
                titleHtml : '<div i="header" class="ui-dialog-header"><button i="close" class="ui-dialog-close">&#215;</button><div i="title" class="ui-dialog-title">'+title+'</div></div>',
                content: template('m_common/m_quickDatePicker', {isClear:that.settings.$isClear}),
                contentStyle: 'height:257px;',
                onShown: function ($popover) {
                    if(callBack!=null){
                        callBack();
                    }
                    if($popover.prev().text()=='未签订'){
                        $popover.find('.ui-dialog-header').remove();
                    }
                    $popover.find('.ui-dialog-close').on('click',function () {
                        $(that.settings.$eleId).next().remove();
                    });
                    that.bindClearDate($popover);
                },
                onSave: function ($popover) {
                    return that.saveDate();
                }
            }, true);
        }
        //保存编辑
        ,saveDate:function (dp) {
            var that = this;
            var $data = dp.cal.getDateStr();

            if(that.settings.$okCallBack!=null){
                return that.settings.$okCallBack($data);
            }
        }
        ,bindClearDate:function ($popover) {
            var that = this;
            $popover.find('button.m-popover-clear').on('click',function () {
                if(that.settings.$okCallBack!=null){
                    return that.settings.$okCallBack(null);
                }
            });
        }

    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {

            //if (!$.data(this, "plugin_" + pluginName)) {
            $.data(this, "plugin_" +
                pluginName, new Plugin(this, options));
            //}
        });
    };

})(jQuery, window, document);


