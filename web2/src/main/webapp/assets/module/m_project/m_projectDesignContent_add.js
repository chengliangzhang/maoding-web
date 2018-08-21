/**
 * 任务签发－添加设计内容
 * Created by wrb on 2017/5/15.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_projectDesignContent_add",
        defaults = {
            $title:'添加设计任务',
            designContentNameList:null,
            onShown:null,
            callback:null
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
            that._initHtml();
        }
        //初始化数据,生成html
        ,_initHtml:function () {
            var that = this;
            var options = {};

            /*var left = $(that.element).position().left;
            console.log(left)
            if(parseInt(left)>600){
                //options.placement = 'right';
            }*/
            options.placement = 'top';
            options.content = template('m_project/m_projectDesignContent_add');
            options.titleHtml = '<h3 class="popover-title">'+that.settings.$title+'</h3>';
            options.onShown = function ($popover) {


                var option = {};
                option.$isDialog = false;
                option.$isHaveMemo = false;
                $popover.find('#time-box').m_inputProcessTime(option);
                that._saveDesignContentName_validate($popover);
                //that._saveDesignContentTime_validate($popover);


            };
            options.onSave = function ($popover) {

                var flag1 = $($popover).find('form.designContentNameOBox').valid();
                //var flag2 = $($popover).find('form.inputTimeOBox').valid();
                if (!flag1) {
                    return false;
                }else {
                    var data = {};
                    data.contentName = $popover.find('input[name="designContentName"]').val();
                    var startTime = $popover.find('input[name="startTime"]').val();
                    var endTime = $popover.find('input[name="endTime"]').val();
                    if(startTime!=undefined && startTime!=''){
                        data.startTime = startTime;
                    }
                    if(endTime!=undefined && endTime!=''){
                        data.endTime = endTime;
                    }
                    if(that.settings.callBack){
                        return that.settings.callBack(data);
                    }
                }
            };

            $(that.element).m_popover(options,true);
            if(that.settings.onShown){
                return that.settings.onShown();
            }

        }
        //验证
        ,_saveDesignContentName_validate:function($popover){
            var that = this;
            $popover.find('form.designContentNameOBox').validate({
                rules: {
                    designContentName:{
                        required:true,
                        notConflict:true
                    }
                },
                messages: {
                    designContentName:{
                        required:'设计内容不能为空!',
                        notConflict:'设计内容不能重复!'
                    }
                }
            });
            $.validator.addMethod('notConflict', function(value, element) {
                var list = that.settings.designContentNameList;
                var error = true;
                if(list&&list.length>0){
                    for(var i=0;i<list.length;i++){
                        if(list[i]==value){
                            error = false;
                            break;
                        }
                    }
                }
                return error;
            }, '阶段名称不能重复!');
        }
        ,_saveDesignContentTime_validate:function($popover){
            var that = this;
            $popover.find('form.inputTimeOBox').validate({
                rules: {
                    startTime:{
                        required:true
                    },
                    endTime:{
                        required:true
                    }

                },
                messages: {
                    startTime:{
                        required:'请设置开始日期!'
                    },
                    endTime:{
                        required:'请设置结束日期!'
                    }
                }
                ,errorPlacement: function (error, element) { //指定错误信息位置
                    if (element.hasClass('timeInput')) {
                        error.appendTo(element.closest('.form-group'));
                    } else {
                        error.insertAfter(element);
                    }
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
