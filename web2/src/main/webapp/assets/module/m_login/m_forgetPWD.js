/**
 * Created by veata on 2016/12/28
 * it applies in changing password when users forgets it
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_forgetPWD",
        defaults = {
            companyName:null,
            nextStepUrl:null,
            toSuccessUrl:null,
            companyId:null,
            userId:null,
            getCodeUrl:null,
            saveUrl:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._cellphone = null;
        this._name = pluginName;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that  = this;
            that._initHtmlData();
        }
        //初始化数据
        ,_initHtmlData:function (callBack) {
            var that = this;
            var $data = {};
            $data.title = that.settings.title||'重置用户密码';
            $data.rootPath = window.rootPath;
            var html = template('m_login/m_forgetPWDStep1',$data);
            $(that.element).html(html);
            that.forgetStep1_validate();
            that.bindStep1ClickAction();
            that.bindKeyDownEnter();

        },
        bindKeyDownEnter: function () {
            var that = this;
            $('body').keydown(function () {
                if (event.keyCode == 13)
                    that.forgetStep2();
            });
        },
        bindStep1ClickAction:function(){//给按钮添加绑定事件
            var that = this;
            $(that.element).find('form.forgetStep1OBox').find('a[data-action],input[data-action]').bind('click',function(){
                var action = $(this).attr('data-action');
                if($(this).attr('disabled')=='disabled'){
                    return false;
                }
                if(action == "getCode"){
                    if (that.receiveCode_validate()) {
                        $('#getCode').attr('disabled', 'disabled');
                        that.receiveCode();
                    }
                }
                if(action == "nextStep"){
                    that.forgetStep1();
                }
            });
        },
        bindStep2ClickAction:function(){//给按钮添加绑定事件
            var that = this;
            $(that.element).find('form.forgetStep2OBox').find('a[data-action],input[data-action]').bind('click',function(){
                var action = $(this).attr('data-action');
                 if(action == 'completeChange'){//点击完成
                    that.forgetStep2();
                }
            });

            $('#password').keydown(function () {
                if (event.keyCode == 13){
                    that.forgetStep2();
                    return false;
                }
            });
        },
        receiveCode:function(){//点击获取验证码
            var clock = 0;
            var that = this;
            that.getCode(clock);
            var option = {};
                option.classId='forgetOBox';
                var userId = that.settings.userId;
                if(userId && userId!==void 0 && userId!='null'){
                    option.url=that.settings.getCodeUrl+'/'+$('#userId').val()+'/'+$('#cellphone').val();
                    option.postData={};
                }else{
                    option.url=that.settings.getCodeUrl;
                    option.postData={};
                    option.postData.cellphone = $('#cellphone').val();
                }
                /* option.postData.userId=$('#userId').val();
                 option.postData.cellphone=$('#cellphone').val();*/
                m_ajax.postJson(option,function (response) {
                    if (response.code == 0) {
                    }
                    else {
                        S_layer.error(response.info);
                        clock = 0;
                        window.clearInterval(timer);
                        window.timer=null;
                        $('#getCode').removeClass('startCode').addClass('endCode');
                        $('#getCode').removeAttr('disabled');
                        $('#getCode').html('获取验证码');
                    }
                });
        },
        getCode:function(c,type){
            c = 60;
            $('#getCode').html(c);
            window.timer=setInterval(function() {
                if(c>0)
                {
                    c = c-1;
                    $('#getCode').removeClass('endCode').addClass('startCode').attr('disabled','disabled');

                    $('#getCode').html(c);
                }else{
                    if("undefined" != typeof timer){
                        window.clearInterval(timer);
                    }
                    $('#getCode').removeClass('endCode').addClass('startCode').removeAttr('disabled');
                    $('#getCode').removeClass('startCode').addClass('endCode');
                    $('#getCode').html('获取验证码');
                }
            }, 1000);
        },
        receiveCode_validate:function() {//step1的表单验证
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
                html+=error[0];
                html+='</label>';
                if($('#cellphone').parent().find('#cellphone-error').length<1){
                    $('#cellphone').after(html);
                }
                return false;
            }else{
                return true;
            }
        },
        forgetStep1:function(){//点击进入下一步
            var that = this;
            if ($(that.element).find("form.forgetStep1OBox").valid()) {
                var that = this;
                var option = {};
                option.classId='forgetOBox';
                option.url=that.settings.nextStepUrl;
                option.postData={};
                that._cellphone=$('#cellphone').val();
                option.postData.cellphone=$('#cellphone').val();
                option.postData.code=$('input[name="verifcationCode"]').val();
                m_ajax.postJson(option,function (response) {
                    if (response.code == 0) {
                        var cellphone = $('#forgetStepOBox form.forgetStep1OBox').find('#cellphone').val();
                        $('#forgetStepOBox').html('');
                        var $data = {};
                        $data.title = that.settings.title||'重置用户密码';
                        $data.cellphone = cellphone;
                        var html = template('m_login/m_forgetPWDStep2',$data);
                        $('#forgetStepOBox').html(html);
                        that.forgetStep2_validate();
                        that.bindStep2ClickAction();
                    }
                    else {
                        S_layer.error(response.info);
                    }
                })
            }
            return false;
        },
        forgetStep2:function(){//点击完成重置密码
            var that = this;
            if ($(that.element).find("form.forgetStep2OBox").valid()) {
                var option = {};
                option.url = that.settings.saveUrl;
                option.async = false;
                option.postData = {};
                if(that.settings.userId && that.settings.companyId) {
                    option.postData.userId = that.settings.userId;
                    option.postData.companyId = that.settings.companyId;
                    option.postData.cellphone = that._cellphone;
                    option.postData.adminPassword = $('#password').val();
                }else{
                    option.postData.cellphone = that._cellphone;
                    option.postData.password = $('#password').val();
                }

                m_ajax.postJson(option, function (response) {
                    if (response.code == 0) {

                        S_layer.success('修改成功！确定跳转请重新登录。','提示',function(){

                            var url = that.settings.toSuccessUrl||'/iWork/sys/login';
                            if (response.data != null && response.data != '')
                                url = response.data;
                            window.location.href = window.rootPath + url;
                            return false;
                        })
                    }
                    else {
                        S_layer.error(response.info);
                    }
                })
            }
            return false;
        },
        forgetStep1_validate:function(){//step1的表单验证
            var that = this;
            $(that.element).find("form.forgetStep1OBox").validate({
                rules: {
                    cellphone: {
                        required: true,
                        minlength: true,
                        isMobile: true
                    },
                    verifcationCode: "required"
                },
                messages: {
                    cellphone: {
                        required: "请输入手机号码",
                        minlength: "确认手机不能小于6个字符",
                        isMobile: "请正确填写您的手机号码"
                    },
                    verifcationCode: "请输入验证码"
                },

                errorElement:'label',
                errorPlacement:function(error,element){
                    error.appendTo(element.closest('.form-group'));
                    error.addClass('input_error');
                }
            });
            // 手机号码验证
            jQuery.validator.addMethod("isMobile", function(value, element) {
                var length = value.length;
                var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
                return this.optional(element) || (length == 11 && mobile.test(value));
            }, "请正确填写您的手机号码");
        },
        forgetStep2_validate:function(){//step1的表单验证
            var that = this;
            $(that.element).find("form.forgetStep2OBox").validate({
                rules: {
                    userName: "required",
                    password: {
                        required: true,
                        rangelength:[6,12],
                        checkSpace:true
                    }
                },
                messages: {
                    userName: "请输入姓名！",
                    password: {
                        required:'请输入密码！',
                        rangelength: "密码为6-12位！",
                        checkSpace: "密码不应含有空格!"
                    }

                },
                errorElement:'label',
                errorPlacement:function(error,element){
                    error.appendTo(element.closest('.form-group'));
                    error.addClass('input_error');
                }
            });
            $.validator.addMethod("checkSpace", function(value, element) {
                var pattern=/^\S+$/gi;
                return this.optional(element) || pattern.test( value ) ;
            }, "密码不应含有空格!");
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
