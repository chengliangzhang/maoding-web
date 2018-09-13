/**
 * 企业认证
 * Created by wrb on 2017/7/10.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_enterpriseCertification",
        defaults = {
            $projectId:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this.businessLicensePhoto = null;//营业执照
        this.legalRepresentativePhoto = null;//法人身份证
        this.operatorPhoto = null;//经办人身份证
        this.sealPhoto = null;//授权证书
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._initHtml();
        }
        //初始化数据,生成html
        ,_initHtml:function () {

            var that = this,option = {};
            option.url = restApi.url_getAuthenticationById+'/'+this._currentCompanyId;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {

                    var status = response.data.authenticationStatus;
                    var html = template('m_org/m_enterpriseCertification',{authentication:response.data});
                    $(that.element).html(html);


                    that._bindActionClick();

                    if(status!=1&&status!=2){//未认证或认证失败可以再次编辑提交

                        that._uploadFile('businessLicensePhotoUpload',8);
                        that._uploadFile('legalRepresentativePhotoUpload',9);
                        that._uploadFile('operatorPhotoUpload',10);
                        that._uploadFile('sealPhotoUpload',5);
                        that._bindRadioCheck();
                        that._applyAuthentication_validate();

                    }else {

                        $(that.element).find('a.file-upload').attr('disabled','disabled').addClass('failure');
                        $(that.element).find('input').attr('disabled','disabled');
                        $(that.element).find('a[data-action="submitReview"]').attr('disabled','disabled').off('click');
                        $(that.element).find('ins.iCheck-helper').css('cursor','not-allowed');
                    }
                    that._bindAttachPreview();
                    if(status==null || status==0){//未认证，组织名称不带过去
                        $(that.element).find('input[name="orgName"]').val('');
                    }

                } else {
                    S_layer.error(response.info);
                }
            })

        }
        //上传营业执照
        ,_uploadFile: function (id,type) {
            var that = this;
            $('#'+id).m_fileUploader({
                server: restApi.url_attachment_uploadOrgAuthenticationAttach,
                accept:{
                    title: '请选择图片',
                    extensions: 'jpg,jpeg,png,bmp',
                    mimeTypes: 'image/jpg,image/jpeg,image/png,image/bmp'
                },
                fileSingleSizeLimit:20*1024*1024,
                formData: {},
                loadingId: '#box_detail',
                innerHTML: '选择文件',
                uploadBeforeSend: function (object, data, headers) {
                    data.companyId = window.currentCompanyId;
                    data.accountId = window.currentUserId;
                    data.type = type;
                },
                uploadSuccessCallback: function (file, response) {
                    var path = window.fastdfsUrl+response.data.fastdfsGroup+'/'+response.data.fastdfsPath;
                    if(type==8){
                        $('#businessLicensePhoto').attr('src',path);
                        $('#businessLicensePhoto-error').hide();
                    }else if(type==9){
                        $('#legalRepresentativePhoto').attr('src',path);
                        $('#legalRepresentativePhoto-error').hide();
                    }else if(type==10){
                        $('#operatorPhoto').attr('src',path);
                        $('#operatorPhoto-error').hide();
                    }else if(type==5){
                        $('#sealPhoto').attr('src',path);
                        $('#sealPhoto-error').hide();
                    }

                }
            },true);
        }
        //营业执照选择事件
        ,_bindRadioCheck:function () {
            var that = this;
            $(that.element).find('input[name="businessLicenseType"]').on('click',function () {
                var v = $(this).val();
                var $imgSrc = $('#businessLicensePhoto').attr('src');
                if(v==1){
                    $(that.element).find('.number-label-0').addClass('hide');
                    $(that.element).find('.number-label-1').removeClass('hide');
                    $(that.element).find('input[name="businessLicenseNumber"]').attr('placeholder','请输入18位统一社会信用代码');
                    if($imgSrc.indexOf('/assets/img/org')>-1){
                        $('#businessLicensePhoto').attr('src',window.rootPath+'/assets/img/org/businessLicenseTemp2.jpg');
                    }
                }else{
                    $(that.element).find('.number-label-1').addClass('hide');
                    $(that.element).find('.number-label-0').removeClass('hide');
                    $(that.element).find('input[name="businessLicenseNumber"]').attr('placeholder','请输入15位工商注册号');
                    if($imgSrc.indexOf('/assets/img/org')>-1){
                        $('#businessLicensePhoto').attr('src',window.rootPath+'/assets/img/org/businessLicenseTemp1.jpg');
                    }
                }
                that._applyAuthentication_validate_change();
            });
        }
        //提交审核
        ,_submitReview:function () {
            var that = this,option = {};

            var flag = $(that.element).find('form').valid();
            var photoError = false;
            var businessLicensePhoto = $('#businessLicensePhoto').attr('src');
            var legalRepresentativePhoto = $('#legalRepresentativePhoto').attr('src');
            var operatorPhoto = $('#operatorPhoto').attr('src');
            var sealPhoto = $('#sealPhoto').attr('src');

            if(!(businessLicensePhoto.indexOf('group')>-1)){
                $('#businessLicensePhoto-error').html('请上传营业执照!');
                photoError = true;
            }
            if(!(legalRepresentativePhoto.indexOf('group')>-1)){
                $('#legalRepresentativePhoto-error').html('请法人身份证照!');
                photoError = true;
            }
            if(!(operatorPhoto.indexOf('group')>-1)){
                $('#operatorPhoto-error').html('请经办人身份证照!');
                photoError = true;
            }
            if(!(sealPhoto.indexOf('group')>-1)){
                $('#sealPhoto-error').html('请上传认证授权书!');
                photoError = true;
            }
            if(!flag || photoError){
                $(that.element).find('label.error').show();
                return false;
            }

            option.url = restApi.url_applyAuthentication;
            option.postData = {};
            option.postData.id = that._currentCompanyId;
            option.postData.businessLicenseType = $(that.element).find('input[name="businessLicenseType"]:checked').val();
            option.postData.businessLicenseNumber = $(that.element).find('input[name="businessLicenseNumber"]').val();
            option.postData.orgName = $(that.element).find('input[name="orgName"]').val();
            option.postData.legalRepresentative = $(that.element).find('input[name="legalRepresentative"]').val();
            option.postData.operatorName = $(that.element).find('input[name="operatorName"]').val();
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('提交审核成功！');
                    that._initHtml();
                } else {
                    S_layer.error(response.info);
                }
            })
        }
        //绑定图片预览事件
        ,_bindAttachPreview: function () {
            var that = this;
            $.each($(that.element).find('img[data-action="preview"]'), function (i, o) {
                $(o).off('click.preview').on('click.preview', function () {
                    var $a = $(this);
                    var $imgSrc = $a.attr('src');
                    /*if($imgSrc.indexOf('/assets/img/org')>-1){
                        return false;
                    }*/
                    var photos={
                            title: '',
                            id: 1,
                            start: 0,
                            data: [{
                                alt: '',
                                pid: '',
                                src: $a.attr('src')
                            }]
                        }
                    ;
                    layer.photos({
                        photos: photos,
                        shift: 5
                    });
                })
            });
        }
        //事件绑定
        ,_bindActionClick:function () {
            var that = this;

            $(that.element).find('a[data-action]').on('click',function () {
                var _this = this;
                var dataAction = $(_this).attr('data-action'
                );
                switch (dataAction) {
                    case 'submitReview'://提交审核
                        that._submitReview();
                        break;
                    case 'viewBusinessLicensePhotoTemp':
                        console.log(1)
                        var $imgSrc = window.rootPath+'/assets/img/org/businessLicenseTemp1.jpg';
                        var photos={
                                title: '',
                                id: 1,
                                start: 0,
                                data: [{
                                    alt: '',
                                    pid: '',
                                    src: $imgSrc
                                }]
                            }
                        ;
                        layer.photos({
                            photos: photos,
                            shift: 5
                        });
                        break;

                    case 'sealPhotoDownLoad':

                        window.open(window.rootPath+'/assets/template/org/ddingCertificationApplication.doc');
                        break;

                    case 'maodingConvention':

                        window.open(window.rootPath+'/assets/template/org/maodingConvention.pdf');
                        break;

                    case 'maodingCertificationSpecification':

                        window.open(window.rootPath+'/assets/template/org/maodingCertificationSpecification.pdf');
                        break;
                }

            });
        }
        //营业执照切换后重置验证
        ,_applyAuthentication_validate_change:function () {
            var that = this;
            var businessLicenseType = $(that.element).find('input[name="businessLicenseType"]:checked').val();
            var eleObj = $(that.element).find('input[name="businessLicenseNumber"]');
            if(businessLicenseType==1){
                eleObj.rules("remove");
                eleObj.rules("add",{
                    required:true,
                    maxlength:18,
                    minlength:18,
                    messages:{
                        required:'请输入18位统一社会信用代码!',
                        maxlength:"社会信用代码为18位!",
                        minlength:'社会信用代码为18位!'
                    }});
            }else{
                eleObj.rules("remove");
                eleObj.rules("add",{
                    required:true,
                    maxlength:15,
                    minlength:15,
                    messages:{
                        required:'请输入15位工商注册号!',
                        maxlength:"工商注册号为15位!",
                        minlength:'工商注册号为15位!'
                    }});
            }
            $(that.element).find('#businessLicenseNumber-error').html('');

        }
        //提交审核表单验证
        ,_applyAuthentication_validate:function(){
            var that = this;
            var businessLicenseType = $(that.element).find('input[name="businessLicenseType"]:checked').val();
            var len = 15,tip = '工商注册号为15位!',requiredTip = '请输入15位工商注册号!';
            if(businessLicenseType==1){
                len = 18;
                tip = '社会信用代码为18位!';
                requiredTip = '请输入18位统一社会信用代码!';
            }
            $(that.element).find('form').validate({
                rules: {
                    businessLicenseNumber: {
                        required: true,
                        maxlength: len,
                        minlength: len
                    },
                    orgName:{
                        required: true
                    },
                    legalRepresentative:{
                        required: true
                    },
                    operatorName:{
                        required: true
                    },
                    convention:{
                        required: true
                    },
                    specification:{
                        required: true
                    }
                },
                messages: {
                    businessLicenseNumber: {
                        required: requiredTip,
                        maxlength: tip,
                        minlength: tip
                    },
                    orgName:{
                        required:'请输入营业执照上的名称!'
                    },
                    legalRepresentative:{
                        required:'请输入法人姓名!'
                    },
                    operatorName:{
                        required: '请输入经办人姓名!'
                    },
                    convention:{
                        required: '请同意《卯丁公约》!'
                    },
                    specification:{
                        required: '请同意《卯丁认证规范》!'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置

                    if ($(element).parents('.i-checks').length>0) {
                        error.appendTo(element.closest('.check-div'));
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
