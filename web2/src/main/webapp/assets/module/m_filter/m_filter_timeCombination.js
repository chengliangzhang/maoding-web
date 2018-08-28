/**
 * 时间组合筛选
 * Created by wrb on 2018/8/14.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_filter_timeCombination",
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
            var html = template('m_filter/m_filter_timeCombination',{});
            $(that.element).html(html);
            that.bindSetTime();
            that.bindChoseTime();
        }
        //快捷时间
        , bindSetTime: function () {
            var that = this;
            $(that.element).find('a[data-action="setTime"]').click(function () {
                var days = $(this).attr('data-days');
                var endTime = getNowDate();
                var startTime = '';//moment(endTime).subtract(days, 'days').format('YYYY-MM-DD');

                if (endTime != null && endTime.indexOf('-') > -1) {

                    var month = endTime.substring(5, 7) - 0;//当前月份

                    if (days == 30) {//一个月

                        startTime = endTime.substring(0, 8) + '01';

                    } else if (days == 90) {//一季度

                        if (month >= 1 && month <= 3) {//第一季度
                            startTime = endTime.substring(0, 5) + '01-01';
                        } else if (month >= 4 && month <= 6) {//第二季度
                            startTime = endTime.substring(0, 5) + '04-01';
                        } else if (month >= 7 && month <= 9) {//第三季度
                            startTime = endTime.substring(0, 5) + '07-01';
                        } else if (month >= 10 && month <= 12) {//第四季度
                            startTime = endTime.substring(0, 5) + '10-01';
                        }

                    } else if (days == 180) {//半年

                        if (month >= 1 && month <= 6) {//前半年
                            startTime = endTime.substring(0, 5) + '01-01';
                        } else if (month >= 7 && month <= 12) {//后半年
                            startTime = endTime.substring(0, 5) + '07-01';
                        }

                    } else if (days == 360) {//一年

                        startTime = endTime.substring(0, 5) + '01-01';
                    }
                }
                that.getDateTime(startTime,endTime);

                $(this).blur();
            });

        }
        //时间绑定事件
        , bindChoseTime:function () {
            var that = this;
            var currDate = getNowDate();
            $(that.element).find('input[name="startTime"]').off('click').on('click',function () {

                var endTime = $(that.element).find('input[name="endTime"]').val();
                var onpicked =function(dp){

                    that._timeData.startTime = dp.cal.getNewDateStr();
                    that._timeData.endTime = endTime;
                    if(that.settings.selectTimeCallBack)
                        that.settings.selectTimeCallBack(that._timeData);

                };
                WdatePicker({el:this,maxDate:endTime,onpicked:onpicked});
            });
            $(that.element).find('input[name="endTime"]').off('click').on('click',function () {

                var startTime = $(that.element).find('input[name="startTime"]').val();
                var onpicked =function(dp){

                    that._timeData.startTime = startTime;
                    that._timeData.endTime = dp.cal.getNewDateStr();
                    if(that.settings.selectTimeCallBack)
                        that.settings.selectTimeCallBack(that._timeData);

                };
                WdatePicker({el:this,minDate:startTime,onpicked:onpicked});
            });
            $(that.element).find('i.fa-calendar').off('click').on('click',function () {
                $(this).closest('.input-group').find('input').click();
            });

            $(that.element).find('input[name="ipt_year"]').click(function () {

                var onpicked =function(dp){

                    var year = dp.cal.getNewDateStr();
                    year  = year.substring(0,4);

                    var endTime = year+'-12-31';
                    var startTime = year+'-01-01';

                    var currYear = new Date().getFullYear();
                    if(year==currYear){//今年
                        endTime  = getNowDate();
                    }

                    that.initMonthSelect();

                    that.getDateTime(startTime,endTime);
                };
                WdatePicker({el:this,dateFmt:'yyyy年',maxDate:currDate,onpicked:onpicked});
            });

        }
        //月份选择
        ,initMonthSelect:function () {
            var that = this;
            var currYear = new Date().getFullYear();
            var year = $(that.element).find('input[name="ipt_year"]').val();
            year  = year.substring(0,4);
            var month = new Date().getMonth();
            var monthLen = 12;
            if(year==currYear){//今年
                monthLen = month+1;
            }
            var staffArr = [{id:'',text:'请选择月份'}];
            for(var i=0;i<monthLen;i++){

                var m = i+1;
                if(m.length<2){
                    m = '0' + m;
                }

                staffArr.push({id:m,text:(i+1)+'月'});
            }
            $(that.element).find('select[name="ipt_month"]').select2({
                tags:false,
                allowClear: false,
                placeholder: "请选择月份",
                containerCssClass:'select-sm',
                minimumResultsForSearch: -1,
                language: "zh-CN",
                data: staffArr
            });
            $(that.element).find('select[name="ipt_month"]').on("change", function (e) {

                var currYear = new Date().getFullYear();
                var year = $(that.element).find('input[name="ipt_year"]').val();
                year  = year.substring(0,4);
                var month = $(that.element).find('select[name="ipt_month"]').val();
                var lastDay = (new Date((new Date(year,month,1)).getTime()-1000*60*60*24)).getDate();
                var endTime = year+'-'+month+'-'+lastDay;
                var startTime = year+'-'+month+'-01';
                if(month==''){
                    endTime  =  year+'-12-31';
                    startTime = year+'-01-01';
                }
                if(year==currYear && month==new Date().getMonth()+1){//今年
                    endTime  = getNowDate();
                }
                that.getDateTime(startTime,endTime);
            })
        }

        ,getDateTime:function (startTime,endTime) {
            var that = this;

            $(that.element).find('#ipt_startTime').val(startTime);
            $(that.element).find('#ipt_endTime').val(endTime);

            that._timeData.startTime = startTime;
            that._timeData.endTime = endTime;

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
