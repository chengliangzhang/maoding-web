/**
 * Created by wrb on 2016/12/6.
 */

var home_shareRegister={
     _cellphone : '',
    init:function(){
        var that=this;
        that.shareRegisterOBox_validate();
        that.bindClickFun();
        var filePath = $('#companyInfoFile').val();
        if(filePath!=undefined && filePath!=''){
            $('.filePath').attr('src',window.fastdfsUrl+filePath);
        }

    },
    bindClickFun: function () {
        var that = this;
        $('.shareRegisterOBox').find('a[data-action]').on('click', function (event) {
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
                that.joinFun();
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
                var $_screenW = $(window).width();
                if($_screenW<768){
                    S_toastr.success(response.info);
                }else{
                    S_dialog.error(response.info);
                }
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
    joinFun:function () {//组织注册提交
        var that = this;
        if ($(".shareRegisterOBox form").valid()) {
            var option = {};
            option.classId='.shareRegisterOBox';
            option.url=restApi.url_shareInvateRegister;
            option.postData={};
            that._cellphone = $('#cellphone').val();
            option.postData.companyId = $('#companyInfoId').val();//邀请公司ID
            option.postData.userId = $('#userInfoId').val();//邀请用户ID
            option.postData.cellphone=$('#cellphone').val();
            option.postData.userName=$('#userName').val();
            option.postData.code=$('#verifcationCode').val();

            m_ajax.postJson(option,function (response) {
                if (response.code == 0) {
                    S_toastr.success('加入成功，请等待审核！');
                    if($(window).width()>992){
                        window.setTimeout(function () {
                            window.location.href = window.rootPath;
                        },2000);
                    }else{
                        $('#showViewType2').removeClass('hide');
                        $('#showViewType1').addClass('hide');
                    }
                }else {
                    var $_screenW = $(window).width();
                    if($_screenW<768){
                        S_toastr.success(response.info);
                    }else{
                        S_dialog.error(response.info);
                    }

                }
            })
        }
    },
    //点击调到下载页面
    toDownload : function(){
        $('#iframeDown').attr('src',window.cdnUrl+'/download');
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
    shareRegisterOBox_validate: function () {//注册的表单验证
        var that = this;
        $(".shareRegisterOBox form").validate({
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
                    rangelength: [6, 12],
                    checkSpace: true
                }

            },
            messages: {
                cellphone: {
                    required: "请输入手机号码",
                    minlength: "请输入11位有效手机号码",
                    isMobile: "请输入11位有效手机号码"
                },
                verifcationCode: "请输入验证码",
                userName: "姓名不能为空！",
                password: {
                    required: '请设置登录密码！',
                    rangelength: "密码为6-12位！",
                    checkSpace: "密码不应含有空格!"
                }

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
        }, "请输入11位有效手机号码");
    }

};