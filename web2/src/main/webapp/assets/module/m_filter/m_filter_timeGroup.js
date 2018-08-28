/**
 * 时间组合筛选
 * Created by wrb on 2018/8/28.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_filter_timeGroup",
        defaults = {
            selectTimeCallBack:null//时间选择回调
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this._timeData = {
            startTime:'',
            endTime:''
        };//选择的时间
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.render();
        }
        ,render:function () {
            var that = this;
            var html = template('m_filter/m_filter_timeGroup',{});
            $(that.element).html(html);
            that.bindSetTime();
        }
        //快捷时间
        , bindSetTime: function () {
            var that = this;
            $(that.element).find('a[data-action="setTime"]').off('click').on('click',function () {
                var dataType = $(this).attr('data-type');
                var $start = $(that.element).find('input[name="startTime"]');
                var $end = $(that.element).find('input[name="endTime"]');

                $(this).addClass('btn-primary').removeClass('btn-default').siblings().addClass('btn-default').removeClass('btn-primary');

                $start.val('');
                $end.val('');

                switch (dataType){
                    case 'day':
                        $start.attr('placeholder','开始日期');
                        $end.attr('placeholder','结束日期');
                        break;
                    case 'month':
                        $start.attr('placeholder','开始月份');
                        $end.attr('placeholder','结束月份');
                        break;
                    case 'year':
                        $start.attr('placeholder','开始年份');
                        $end.attr('placeholder','结束年份');
                        break;
                }

            });
            $(that.element).find('input[name="startTime"]').off('click').on('click',function () {

                var dataType = $(that.element).find('a.btn-primary[data-action="setTime"]').attr('data-type');
                var formatStr = that.returnFormat(dataType);
                var endTime = $(that.element).find('input[name="endTime"]').val();
                var maxDate = endTime;
                if(isNullOrBlank(endTime))
                    maxDate = getNowDate();

                var onpicked =function(dp){

                    //$start.val(dp.cal.getNewDateStr());

                    if(endTime==''){//没有结束时间，弹出结束时间弹窗
                        $(that.element).find('input[name="endTime"]').click();
                    }else{
                        that.dateTimeCallBack();
                    }
                };
                WdatePicker({el:this,dateFmt:formatStr,maxDate:maxDate,onpicked:onpicked})
            });
            $(that.element).find('input[name="endTime"]').off('click').on('click',function () {

                var dataType = $(that.element).find('a.btn-primary[data-action="setTime"]').attr('data-type');
                var formatStr = that.returnFormat(dataType);

                var startTime = $(that.element).find('input[name="startTime"]').val();

                var maxDate = getNowDate();

                var onpicked =function(dp){

                    if(startTime==''){//没有开始时间，弹出开始时间弹窗
                        $(that.element).find('input[name="startTime"]').click();
                    }else{
                        that.dateTimeCallBack();
                    }
                };
                WdatePicker({el:this,dateFmt:formatStr,minDate:startTime,maxDate:maxDate,onpicked:onpicked})
            });

            $(that.element).find('span.input-group-addon').off('click').on('click',function () {
                $(this).closest('.input-group').find('input[class!="form-control input-sm hide"]').focus();
            });

        }
        //根据类型返回格式
        ,returnFormat:function (dataType) {
            var formatStr = 'yyyy-MM-dd';
            if(dataType=='month'){
                formatStr = 'yyyy-MM';
            }else if(dataType=='year'){
                formatStr = 'yyyy';
            }else{
                formatStr = 'yyyy-MM-dd';
            }
            return formatStr;
        }
        //时间回滚事件
        ,dateTimeCallBack:function () {
            var that = this;

            that._timeData.startTime = $(that.element).find('#ipt_startTime').val();
            that._timeData.endTime = $(that.element).find('#ipt_endTime').val();

            var dataType = $(that.element).find('a.btn-primary[data-action="setTime"]').attr('data-type');

            var lastDay = '';
            if(dataType=='month'){

                if(!isNullOrBlank(that._timeData.startTime))
                    that._timeData.startTime = that._timeData.startTime+'-01';

                if(!isNullOrBlank(that._timeData.endTime)){
                    var year = that._timeData.endTime.substring(0,4);
                    var month = that._timeData.endTime.substring(5,7);
                    lastDay = getLastDay(year-0,month-0);
                    that._timeData.endTime = that._timeData.endTime+'-'+lastDay;
                }

            }else if(dataType=='year'){

                if(!isNullOrBlank(that._timeData.startTime))
                    that._timeData.startTime = that._timeData.startTime+'-01-01';

                if(!isNullOrBlank(that._timeData.endTime)){
                    var year = that._timeData.endTime.substring(0,4);
                    lastDay = getLastDay(year-0,12);
                    that._timeData.endTime = that._timeData.endTime+'-12-'+lastDay;
                }
            }

            if(that.settings.selectTimeCallBack)
                that.settings.selectTimeCallBack(that._timeData);
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
