/**
 * Created by wrb on 2016/12/15.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_bindEmail",
        defaults = {
            sendEmailCallBack:null
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
            var html = template('m_personal/m_bindEmail',{});
            $(this.element).html(html);
            this.sendValidationEemail_validate();
            this.bindActionClick();
        }
        //发送邮箱
        ,sendValidationEemail:function () {

            var that = this;

            if ($('form.bindEmailBox').valid()) {
                var option  = {};
                option.url = restApi.url_sendBindMail;
                option.postData = {toMail:$('form.bindEmailBox input[name="bindEmailDtoEmail"]').val()};
                m_ajax.postJson(option,function (response) {
                    if(response.code=='0'){
                        S_toastr.success('发送成功！');
                        if(that.settings.sendEmailCallBack!=null){
                            that.settings.sendEmailCallBack();
                        }
                    }else {
                        S_layer.error(response.info);
                    }

                })
            }
        }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;
            $('form.bindEmailBox button[data-action]').on('click',function () {
                var dataAction = $(this).attr('data-action');
                if(dataAction=="sendValidationEemail"){//发送邮件
                    that.sendValidationEemail();
                }
            });
        }
        //发送邮箱表单验证
        ,sendValidationEemail_validate:function(){
            $('form.bindEmailBox').validate({
                rules: {
                    bindEmailDtoEmail: {
                        required: true,
                        email: true
                    }
                },
                messages: {
                    bindEmailDtoEmail: {
                        required: '请输入邮箱!',
                        email: "请输入有效的电子邮件地址",
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    if ($(element).attr('name')=='bindEmailDtoEmail') {
                        error.appendTo(element.closest('.col-md-12'));
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
