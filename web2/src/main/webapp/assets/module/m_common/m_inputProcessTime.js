/**
 * Created by wrb on 2016/12/21.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_inputProcessTime",
        defaults = {
            $title: null,
            $isDialog: true,
            $isHaveMemo: true,
            $timeInfo: null,
            $okCallBack: null,
            $isOkSave: false,//默认false
            $okText: null,//按钮文字
            $saveDataUrl: null,//直接保存url
            $minHeight: null,//弹窗的最小高度
            $saveData: null,//保存格外的参数
            $currentAppointmentDate: null,//当前任务的合同进度时间
            $saveTimeKeyVal: null,//保存到库所对应的时间字段 key 值
            $validate:null//验证{默认第一套验证}
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
            that.initHtmlTemplate();
        },

        //初始化数据
        renderDialog: function (html,callBack) {
            var that = this;
            if (that.settings.$isDialog) {//以弹窗编辑
                var options = {};
                options.title = that.settings.$title || '设计依据';
                options.area = ['500px'];
                options.content = html;
                options.cancel = function () {
                };
                if (that.settings.$isOkSave) {//直接保存
                    options.ok = function () {
                        if ($('form.inputTimeOBox').valid()) {
                            var $data = $("form.inputTimeOBox").serializeObject();
                            var timeObj = {};
                            if (that.settings.$saveTimeKeyVal != null) {//处理保存到库所对应的时间字段 key 值,此处传入的key是有序的
                                for (var key in $data) {
                                    if (key == 'startTime') {
                                        timeObj[that.settings.$saveTimeKeyVal[0]] = $data[key];
                                        continue;
                                    }
                                    if (key == 'endTime') {
                                        timeObj[that.settings.$saveTimeKeyVal[1]] = $data[key];
                                        continue;
                                    }
                                    if (key == 'memo') {
                                        timeObj[that.settings.$saveTimeKeyVal[2]] = $data[key];
                                        timeObj[that.settings.$saveTimeKeyVal[2]] = $.trim(timeObj[that.settings.$saveTimeKeyVal[2]]);
                                        continue;
                                    }
                                }
                            }
                            $.extend(timeObj, that.settings.$saveData);
                            var option = {};
                            if($('#page-wrapper #content-right').length>0){
                                option.classId = '#page-wrapper #content-right';
                            }
                            option.url = that.settings.$saveDataUrl;
                            option.postData = timeObj;
                            m_ajax.postJson(option, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success("保存成功!");
                                    if (that.settings.$okCallBack != null) {
                                        return that.settings.$okCallBack(response.data);
                                    }
                                } else {
                                    S_layer.error(response.info);
                                }
                            })
                        } else {
                            return false;
                        }
                    }
                } else {//返回上级
                    options.ok = function () {
                        if ($('form.inputTimeOBox').valid()) {
                            var $data = $("form.inputTimeOBox").serializeObject();
                            if (that.settings.$okCallBack != null) {
                                return that.settings.$okCallBack($data);
                            }
                        } else {
                            return false;
                        }
                    };
                }

                S_layer.dialog(options,function(layero,index,$dialogEle){//加载html后触发

                    if(callBack)
                        callBack();
                });

            } else {//不以弹窗编辑

                $(that.element).html(html);
                if(callBack)
                    callBack();
            }
        }
        //计算时间差的方法
        , countTimeDiff: function (startTime, endTime) {
            return moment(endTime).diff(moment(startTime), 'days') + 1;
        }
        //生成html
        , initHtmlTemplate: function () {
            var that = this;

            var $data = {};
            $data.$isHaveMemo = that.settings.$isHaveMemo;
            $data.$timeInfo = {};
            if (that.settings.$timeInfo != null) {
                $data.$timeInfo = that.settings.$timeInfo;
            }

            if (that.settings.$currentAppointmentDate != null) {
                $data.appointmentStartTime = that.settings.$currentAppointmentDate.startTime;
                $data.appointmentEndTime = that.settings.$currentAppointmentDate.endTime;
            }
            var html = template('m_common/m_inputProcessTime', $data);
            that.renderDialog(html,function () {

                that.initSpinner();
                if(that.settings.$validate!=null && that.settings.$validate==1){
                    that.addInputTime_validate1();
                }else if(that.settings.$validate!=null && that.settings.$validate==2){

                }else{
                    that.addInputTime_validate();
                }

                m_inputProcessTime_onpicked();

                $(that.element).find('.fa-calendar').click(function () {
                    $(this).closest('.input-group').find('input:first').focus();
                });
            });

        }
        //初始化Spinner
        , initSpinner: function () {
            $('form.inputTimeOBox').find('.dayCountSpinner:eq(0)')
                .spinner({
                    delay: 200,
                    changed: function (e, newVal, oldVal) {
                        var startTime = $('form.inputTimeOBox').find('.startTime:eq(0)').val();
                        if (startTime === null || _.isBlank(startTime))
                            return;

                        if (newVal == 0) {
                            $('form.inputTimeOBox').find('.endTime:eq(0)').val('');
                            return;
                        }

                        var endTime = moment(startTime).add(newVal - 1, 'days').format('YYYY-MM-DD');
                        $('form.inputTimeOBox').find('.endTime:eq(0)').val(endTime);
                    }
                });
        }
        //step1的表单验证
        , addInputTime_validate: function () {
            var that = this;
            $('form.inputTimeOBox').validate({
                rules: {
                    startTime: {
                        required: true
                    }
                    , endTime: {
                        required: true
                    }
                    , memo: {
                        required: true,
                        maxlength: 250,
                        isBlank:true
                    }
                },
                messages: {
                    startTime: {
                        required: '请设置开始日期！'
                    }
                    , endTime: {
                        required: '请设置结束日期！'
                    }
                    , memo: {
                        required: '请输入进度变更原因！',
                        maxlength: '输入不能超过250个字符！',
                        isBlank:'请输入进度变更原因!'
                    }
                },
                errorElement: "label",  //用来创建错误提示信息标签
                errorPlacement: function (error, element) {  //重新编辑error信息的显示位置
                    error.appendTo(element.closest('.form-group'));
                }
            });
            $.validator.addMethod('isBlank', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if( value!=null && $.trim(value)==''){
                    isOk = false;
                }
                return  isOk;
            }, '请输入进度变更原因!');

        }
        , addInputTime_validate1: function () {
            var that = this;
            $('form.inputTimeOBox').validate({
                rules: {
                    startTime: {
                        ckTime: true
                    }
                },
                messages: {
                    startTime: {
                        ckTime: '请输入日期!'
                    }
                },
                errorElement: "label",  //用来创建错误提示信息标签
                errorPlacement: function (error, element) {  //重新编辑error信息的显示位置
                    error.appendTo(element.closest('.form-group'));
                }
            });
            $.validator.addMethod('ckTime', function(value, element) {
                var isOk = true;
                var startTime = $('.inputTimeOBox input[name="startTime"]').val();
                var endTime = $('.inputTimeOBox input[name="endTime"]').val();
                if(startTime=='' && endTime==''){
                    isOk = false;
                }
                return  isOk;
            }, '请输入日期!');

            $('form.inputTimeOBox').find('input,textarea').on('click',function () {
                $('form.inputTimeOBox label.error').hide();
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
