/**
 * Created by Wuwq on 2017/07/05.
 */
var home_index1 = {
    init: function () {
        var that=this;

        /*判断浏览器版本是否是ie10以下 开始*/
        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
        var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
        var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器
        if (isIE) {
            var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
            reIE.test(userAgent);
            var fIEVersion = parseFloat(RegExp["$1"]);
            //当浏览器为小于ie10版本时，去掉data-ease属性
            if (fIEVersion < 10) {
                $('#slider-wrapper .master-slider').find('img').removeAttr('data-ease');
                $('#slider-wrapper .ms-slide').find('h1').removeAttr('data-ease');
            }


        }
        /*判断浏览器版本是否是ie10以下 结束*/

        _.mixin(s.exports());
        App.init();
        MasterSliderMainShowcase.initMasterSliderMainShowcase();
        MasterSliderProductsShowcase.initMasterSliderProductsShowcase();

        that.bindBtnShowLoginForm();
    },
    bindBtnShowLoginForm: function () {
        var that = this;
        $('#btnShowLoginForm').click(function () {
            $(this).m_popover({
                hideArrow: true,
                placement: 'bottom',
                content: template('m_login/m_popover_login', {}),
                onShown: function ($popover) {
                    that.login_validate();

                    $popover.find('#btnForgetPwd').off('click.forgetPwd').on('click.forgetPwd', function (e) {
                        var $btn = $(this);
                        var url = $btn.attr('data-url');
                        window.location.href = url;
                    });

                    $popover.find('#password').off('keydown.pwd').on('keydown.login',function (e) {
                        if (event.keyCode == 13)
                            that.login();
                    });
                },
                onSave: function ($popover) {
                    that.login();
                    return false;
                }
            }, true);
        });

    },
    login: function () {
        if ($("#loginForm").valid()) {
            var option = {};
            option.url = restApi.url_homeLogin;
            option.postData = {};
            option.postData.cellphone = $('#cellphone').val();
            option.postData.password = $('#password').val();
            m_ajax.postJson(option, function (response) {
                if (response.code == 0) {
                    var url = '/iWork/home/workbench/1';
                    if (response.data != null && response.data != '') {
                        url = response.data;
                        if (response.data.indexOf("iAdmin") > -1) {
                            url = '/iWork/sys/login';
                        }
                    }
                    window.location.href = window.rootPath + url;
                }
                else {
                    S_dialog.error(response.info);
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
};