/**
 *
 * Created by veata on 2016/12/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_login",
        defaults = {

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
        init:function(){
            var that=this;
            that.initHtmlData(function(){
                that.login_validate();
                that.bindKeyDownEnter();
                that.bindOkLogin()
            });

        },
        initHtmlData:function(callback){
            var that = this;
            var rootPath = window.rootPath;
            var html = template('m_login/m_login',{rootPath:rootPath});
            $(that.element).html(html);
            if(callback) return callback();
        },
        bindKeyDownEnter: function () {
            var that = this;
            $('#password').keydown(function () {
                if (event.keyCode == 13)
                    that.login();
            });
        },
        bindOkLogin: function(){
            var that = this;
            $(that.element).find('#btnLogin').on('click',function(event){
                that.login();
            });
        },
        login: function () {
            if ($("#loginForm").valid()) {
                var option = {};
                option.classId = '.login-form';
                option.url = restApi.url_homeLogin;
                option.postData = {};
                option.postData.cellphone = $('#cellphone').val();
                option.postData.password = $('#password').val();
                m_ajax.postJson(option, function (response) {
                    if (response.code == 0) {

                        removeCookiesOnLoginOut();

                        var url = '/iWork/home/workbench/1';
                        window.location.href = window.rootPath + url;
                    }
                    else {
                        S_layer.error(response.info);
                    }
                });
            }
            return false;
        },
        login_validate: function () {//点击登录时进行密码验证
            $("#loginForm").validate({
                rules: {
                    cellphone: "required",
                    password: "required"
                },
                messages: {
                    cellphone: "请输入手机号码",
                    password: "请输入账号密码"
                },
                highlight: function (element, errorClass, validClass) {
                    $(element).removeClass("valid-success").addClass("valid-error");
                },
                unhighlight: function (element, errorClass, validClass) {
                    $(element).tooltip('destroy');
                    $(element).removeClass("valid-error");
                    //$(element).removeClass("valid-error").addClass("valid-success");
                },
                success: function (element, errorClass, validClass) {
                    $(element).tooltip('destroy');
                    $(element).removeClass("valid-error");
                    //$(element).removeClass("valid-error").addClass("valid-success");
                },
                /*errorElement:"span",*/
                errorPlacement: function (label, element) {
                    /* $(element).appendTo(r.is(":radio")||r.is(":checkbox")?r.parent().parent().parent():r.parent());*/
                    $(element).attr('title', $(label).text()).tooltip({placement: 'left'}).tooltip('show');
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
