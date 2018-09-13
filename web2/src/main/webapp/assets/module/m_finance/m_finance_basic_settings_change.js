/**
 * 基础财务数据设置-余额变更
 * Created by wrb on 2018/9/10.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_finance_basic_settings_change",
        defaults = {
            isDialog:true,
            balanceId:null,//余额主表id
            companyId:null,//余额主表companyId
            saveCallBack:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._currentCompanyUserId = window.currentCompanyUserId;
        this._currentCompanyId = window.currentCompanyId;
        this._currentUserId = window.currentUserId;
        this._fastdfsUrl = window.fastdfsUrl;

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_finance/m_finance_basic_settings_change', {
                data: that._baseData,
                dataInfo:that.settings.dataInfo
            });
            that.renderDialog(html,function () {
                $(that.element).find('select[name="changeType"]').select2({
                    width: '100%',
                    allowClear: true,
                    language: 'zh-CN',
                    minimumResultsForSearch: Infinity,
                    placeholder: '请选择变更类型!'
                });
                that.save_validate();
            });
        }
        //初始化数据,生成html
        ,renderDialog:function (html,callBack) {

            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.title||'余额变更',
                    area : '750px',
                    content:html,
                    cancel:function () {
                    },
                    ok:function () {

                        var flag = $(that.element).find('form').valid();
                        if (!flag || that.save()) {
                            return false;
                        }
                    }

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;
                    if(callBack)
                        callBack();
                });

            }else{//不以弹窗编辑
                $(that.element).html(html);
                if(callBack)
                    callBack();
            }
        }

        //保存
        ,save:function () {
            var that = this;

            var $data = $(that.element).find("form.form-horizontal").serializeObject();

            var option = {};
            option.classId = that.element;
            option.url = restApi.url_saveCompanyBalanceChangeDetail;
            option.postData = $data;
            option.postData.balanceId = that.settings.balanceId;
            option.postData.companyId = that.settings.companyId;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('操作成功');
                    if(that.settings.saveCallBack)
                        that.settings.saveCallBack();
                } else {
                    S_layer.error(response.info);
                }
            });
        }

        //表单验证
        ,save_validate:function(){
            var that = this;
            $(that.element).find('form.form-horizontal').validate({
                ignore : [],
                rules: {
                    changeType: {
                        required: true
                    },
                    changeAmount:{
                        required: true,
                        number:true,
                        minNumber:true,
                        maxlength:25,//是否超过25位
                        pointTwo:true

                    },
                    changeReason:{
                        maxlength: 500
                    }
                },
                messages: {
                    changeType: {
                        required: '请选择变更类型！'
                    },
                    changeAmount:{
                        required:'请输入金额！',
                        number:'请输入有效数字',
                        minNumber:'请输入大于0的数字!',
                        maxlength:'对不起，您的操作超出了系统允许的范围。',
                        pointTwo:'请保留小数点后两位!'
                    },
                    changeReason:{
                        maxlength:'字数已超出限制！'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.appendTo(element.closest('.col-24-sm-18'));
                }
            });
            $.validator.addMethod('minNumber', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if( value<=0){
                    isOk = false;
                }
                return  isOk;
            }, '请输入大于0的数字!');
            $.validator.addMethod('pointTwo', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if(!regularExpressions.proportionnumber.test(value)){
                    isOk = false;
                }
                return  isOk;
            }, '请保留小数点后两位!');

        }

    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            // if (!$.data(this, "plugin_" + pluginName)) {
            $.data(this, "plugin_" +
                pluginName, new Plugin(this, options));
            // }
        });
    };

})(jQuery, window, document);
