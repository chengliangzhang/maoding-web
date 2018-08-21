/**
 *
 * Created by veata on 2016/12/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_register",
        defaults = {
            cellphone: null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._cellphone = '';
        this._name = pluginName;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.getRegisterHtml(function () {
                that.bindClickFun();
                that.bindKeyDownEnter();
                that.registerOBox_validate();
            });
        },
        //敲打回车enter键提交请求
        bindKeyDownEnter: function () {
            var that = this;
            $(that.element).find('input').keydown(function () {
                if (event.keyCode == 13)
                    that.toRegister();
            });
        },
        getRegisterHtml: function (callback) {
            var that = this;
            var $data = {};
            $data.rootPath = window.rootPath;
            $data.cdnUrl = window.cdnUrl;
            var html = template('m_login/m_register', $data);
            $(that.element).html(html);
            if (callback) {
                return callback();
            }
        },
        bindClickFun: function () {
            var that = this;
            $(that.element).find('a[data-action]').on('click', function (event) {
                var action = $(this).attr('data-action');
                if ($(this).attr('disabled') == 'disabled') {
                    return false;
                }
                if (action == "getCode") {
                    if (that.receiveCode_validate()) {
                        $('#getCode').attr('disabled', 'disabled');
                        that.receiveCode();
                    }
                }
                if (action == "nextStep") {
                    that.step1();
                }
                if (action == "submitRegister") {
                    that.toRegister();
                }
                stopPropagation(event);
            })
        },
        //点击获取验证码
        receiveCode: function () {
            var that = this;
            var clock = 0;
            that.getCode(clock);
            var option = {};
            option.classId = 'registerOBox';
            option.url = restApi.url_homeRegister_securityCode;
            option.postData = {};
            option.postData.cellphone = $('#cellphone').val();
            m_ajax.postJson(option, function (response) {
                if (response.code == 0) {
                }
                else {
                    S_dialog.error(response.info);
                    clock = 0;
                    window.clearInterval(timer);
                    window.timer=null;
                    $('#getCode').removeClass('startCode').addClass('endCode');
                    $('#getCode').removeAttr('disabled');
                    $('#getCode').html('获取验证码');
                }
            });

        },
        //验证码
        getCode: function (c) {
            c = 60;
            $('#getCode').html(c);
            window.timer = setInterval(function () {
                if (c > 0) {
                    c = c - 1;
                    $('#getCode').removeClass('endCode').addClass('startCode').attr('disabled', 'disabled');

                    $('#getCode').html(c);
                } else {
                    if ("undefined" != typeof timer) {
                        window.clearInterval(timer);
                    }
                    $('#getCode').removeClass('endCode').addClass('startCode').removeAttr('disabled');
                    $('#getCode').removeClass('startCode').addClass('endCode');
                    $('#getCode').html('获取验证码');
                }
            }, 1000);
        },
        toRegister: function () {//点击完成注册
            var that = this;
            if ($(that.element).find("form.registerOBox").valid()) {
                var option = {};
                option.classId = 'registerOBox';
                option.url = restApi.url_homeRegister_register;
                option.postData = {};
                option.postData.cellphone = $('#cellphone').val();
                option.postData.code = $('#verifcationCode').val();
                option.postData.userName = $('#userName').val();
                option.postData.password = $('#password').val();
                m_ajax.postJson(option, function (response) {
                    if (response.code == 0) {
                        S_toastr.success('注册成功，欢迎进入卯丁！');
                        setTimeout(function () {
                            var url = '/iWork/home/workbench';//默认
                            /*if (response.data != null && response.data != '')
                                url = response.data;*/
                            window.location.href = rootPath + url;
                        }, 1500);
                    }
                    else {
                        S_dialog.error(response.info);
                    }
                });
            }
            return false;
        },
        //验证是否手机号填写格式正确
        receiveCode_validate: function () {//step1的表单验证
            var error = [];
            var cellphone = $('#cellphone').val();
            var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
            if (cellphone == '' || cellphone == null) {
                error.push('请输入手机号码');
            } else if (!mobile.test(cellphone)) {
                error.push('请正确填写您的手机号码');
            }
            if (error.length > 0) {
                var html = '<label id="cellphone-error" class="error" for="cellphone">';
                html += error[0];
                html += '</label>';
                if ($('#cellphone').parent().find('#cellphone-error').length < 1) {
                    $('#cellphone').after(html);
                }
                return false;
            } else {
                return true;
            }
        },
        registerOBox_validate: function () {//注册的表单验证
            var that = this;
            $(that.element).find("form.registerOBox").validate({
                rules: {
                    cellphone: {
                        required: true,
                        minlength: 11,
                        isMobile: true
                    },
                    verifcationCode: "required",
                    userName: "required",
                    password: {
                        required: true,
                        rangelength: [6, 32],
                        checkSpace: true
                    },
                    serviceTerm:{checkServiceTerm:true}
                },
                messages: {
                    cellphone: {
                        required: "请输入手机号码",
                        minlength: "确认手机不能小于11个字符",
                        isMobile: "请正确填写您的手机号码"
                    },
                    verifcationCode: "请输入验证码",
                    userName: "请输入姓名！",
                    password: {
                        required: '请输入卯丁账号密码！',
                        rangelength: "登录密码请设定六位数或六位数以上！",
                        checkSpace: "密码不应含有空格!"
                    },
                    serviceTerm:{checkServiceTerm:"请先同意服务条款"}

                },
                errorElement: 'label',
                errorPlacement: function (error, element) {
                    error.appendTo(element.closest('.form-group'));
                    error.addClass('input_error');
                }
            });
            // 手机号码验证
            jQuery.validator.addMethod("isMobile", function (value, element) {
                var length = value.length;
                var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
                return this.optional(element) || (length == 11 && mobile.test(value));
            }, "请正确填写您的手机号码");
            //密码验证
            $.validator.addMethod("checkSpace", function (value, element) {
                var pattern = /^\S+$/gi;
                return this.optional(element) || pattern.test(value);
            }, "密码不应含有空格!");
            //服务条款
            $.validator.addMethod("checkServiceTerm", function (value, element) {
               return $(element).prop('checked');
            }, "请先同意服务条款");
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
