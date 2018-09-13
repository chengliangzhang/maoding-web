/**
 * Created by wrb on 2016/12/14.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_userInfo",
        defaults = {
            doType: 'view',//操作类型{view=查看,edit=编辑}
            userDto: null//用户信息数据(当从外传入，则不需要重新请求)
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._originalFileGroup = '';
        this._originalFilePath = '';
        this._originalFileName = '';

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            this.getUserInfo();
        },
        //渲染页面
        renderUserInfo: function () {
            var that = this;

            that.getCompanyDepartAndPermission(function (data) {
                var userDto = that.settings.userDto;

                userDto.orgpermissionList = data;
                if (userDto.headImg !== void 0 && userDto.headImg != null)
                    userDto.headImgUrl = window.fastdfsUrl + userDto.headImg;
                else
                    userDto.headImgUrl=window.rootPath+'/assets/img/default/default_headPic.png';
                if (that.settings.doType == 'edit')
                    var html = template('m_personal/m_userInfo_edit', {userDto: userDto});
                else
                    var html = template('m_personal/m_userInfo', {userDto: userDto});

                $(that.element).html(html);
                that.editableBind();
                that._bindUploadOrinalImage();
                that.bindActionClick();
                that.editableBind();
            });

        },
        //获取用户信息
        getUserInfo: function () {
            var that = this;
            var userDto = that.settings.userDto;
            if (userDto === void 0 || userDto === null) {
                var option = {};
                option.url = restApi.url_userInfo;
                m_ajax.get(option, function (response) {
                    if (response.code === '0') {
                        that.settings.userDto=response.data;
                        that.renderUserInfo();

                    } else {
                        S_layer.error(response.info);
                    }
                });
            } else {
                that.renderUserInfo();

            }
        }

        //获取数据字典专业列表
        ,getMajor:function (callBack) {
            var option = {};
            option.url = restApi.url_getMajor;
            m_ajax.post(option, function (response) {
                if (response.code === '0') {
                   if(callBack!=null){
                       return callBack(response.data);
                   }
                } else {
                    S_layer.error(response.info);
                }
            });
        }
        //获取人员组织权限信息
        ,getCompanyDepartAndPermission:function (callBack) {
            var option = {};
            option.url = restApi.url_getCompanyDepartAndPermission;
            option.postData = {};
            option.postData.accountId = window.currentUserId;
            option.postData.orgId = window.currentCompanyId;
            m_ajax.postJson(option, function (response) {
                if (response.code === '0') {
                    if(callBack!=null){
                        return callBack(response.data);
                    }
                } else {
                    S_layer.error(response.info);
                }
            });
        }
        //在位编辑事件绑定
        ,editableBind:function(){
            var that = this;
            $(that.element).find('.editableInfo a[data-action="text_userName"]').editable({
                type : 'text'
                ,title : '编辑'
                ,placeholder : '请输入姓名'
                ,placement:'right'
                ,value:$.trim($(that.element).find('.editableInfo a[data-action="text_userName"]').text())
                ,mode: "popup"
                ,success:function(response,newValue){
                    that.saveUserInfo(newValue,function(){
                        $(this).text(newValue).css({'color':'#4765a0','font-weight':'normal'});
                        $('nav>div[id="navbar"]>ul>li>div').find('a[data-type="textUserName"]').text(newValue);
                    });
                }
                ,validate: function (value) {
                if ($.trim(value) == '') {
                    return "姓名不能为空！";
                }
            }
            });

            that.dealEmptyText();
        }
        //当在为编辑的文本显示为empty时，改为“请设置”
        ,dealEmptyText:function(){
            var that = this;
            $(that.element).find('.editableInfo a[data-action]').each(function(){
                var text = $(this).text();
                if(text=='Empty'){
                    $(this).text(text=='Empty'?'未设置':text).css({'color':'#ccc','font-style':'normal'});
                }
            });
        }
        //保存用户信息
        , saveUserInfo: function (value,callback) {
            var that = this;
            var option = {};
            option.url = restApi.url_userInfo;
            // var $data = $("form.userInfoForm").serializeObject();
            option.postData = {};
            option.postData.id = that.settings.userDto.id;
            option.postData.userName = value;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('保存成功！');
                    // $(that.element).m_userInfo(option.postData);
                    if(callback) return callback();
                } else {
                    S_layer.error(response.info);
                }
            })
        }
        //按钮事件绑定
        , bindActionClick: function () {
            var that = this;
            $('.headImg a[data-action]').off('click');
            // $('.headImg a[data-action="changeHeadPic"]').on('click', function () {
            //         that._bindUploadOrinalImage();
            //     return false;
            // });

            $(that.element).find('a[data-action]').on('click',function () {

                var $this = this,dataAction = $(this).attr('data-action');

                if(dataAction=='bindPhone'){//绑定手机

                    $('body').m_bindPhone();

                }else if(dataAction=='changePwd'){//修改密码

                    $('body').m_uptPassword();//打开修改密码弹窗
                }else if(dataAction=='edit_designRange'){//修改专业

                    that.getMajor(function (data) {
                        $($this).m_popover({
                            placement: 'right',
                            content: template('m_personal/m_userInfo_major_edit', {majorList:data,majorId:that.settings.userDto.major}),
                            titleHtml: '<h3 class="popover-title">编辑</h3>',
                            onShown: function ($popover) {

                                $popover.find('.userInfo_profession_edit input[name="major"]').on('click',function (e) {

                                    stopPropagation(e);
                                })
                                $popover.find('.userInfo_profession_edit input[name="major"]').next().on('click',function (e) {
                                    $(this).prev().click();
                                })
                                that._saveUserMajor_validate();
                            },
                            onSave: function ($popover) {

                                if($(that.element).find('form.userInfo_profession_edit').valid()){
                                    var option = {};
                                    option.url = restApi.url_userInfo;
                                    option.postData = {};
                                    option.postData.id = that.settings.userDto.id;
                                    option.postData.major = $popover.find('.userInfo_profession_edit input[name="major"]:checked').val();
                                    m_ajax.postJson(option, function (response) {
                                        if (response.code == '0') {
                                            S_toastr.success('保存成功！');
                                            that.refresh();
                                        } else {
                                            S_layer.error(response.info);
                                        }
                                    })
                                }else{
                                    return false;
                                }


                            }
                        }, true);
                        return false;
                    });

                }
                return false;
            });
        }
        //上传原图
        , _bindUploadOrinalImage: function () {
            var that = this;
            var userDto = that.settings.userDto;
            var headImg=null;
            var text=$('.imgContent .btnFilePicker').html();
            if (userDto.headImg !== void 0 && userDto.headImg != null)
                headImg=userDto.headImg;
            $('.imgContent .btnFilePicker').m_imgUploader({

                server: window.fileCenterUrl + "/fastUploadImage",
                formData: {
                    cut_deleteGroup:that.settings.cut_deleteGroup,
                    cut_deletePath:headImg
                },
                innerHTML: text,
                loadingId: 'body',
                uploadSuccessCallback: function (file, response) {
                    that._originalFileGroup = response.data.fastdfsGroup;
                    that._originalFilePath = response.data.fastdfsPath;
                    that._originalFileName = response.data.fileName;

                    that.toCutDialog();
                    //渲染图片
                    $('.m_imgCropper .img-container').attr('src', window.fastdfsUrl + that._originalFileGroup + '/' + that._originalFilePath);



                    $(that.element).find('.title:eq(0)').addClass('hide');
                    $(that.element).find('.setArea:eq(0)').removeClass('hide');



                    //S_toastr.success(response.info);
                }
            },true);
        }
        //调到裁剪窗口
        ,toCutDialog:function(){
            var that = this;
            var userDto = that.settings.userDto;
            var headImg=null;
            if (userDto.headImg !== void 0 && userDto.headImg != null)
                headImg=userDto.headImg;
            $('body').m_imgCropper({
                showDialog: true,
                zoomWidth: 180,
                zoomHeight: 180,
                originalFileGroup: that._originalFileGroup,
                originalFilePath: that._originalFilePath,
                originalFileName: that._originalFileName,
                cut_deletePath:headImg,
                croppedCallback: function (data) {
                    var ajaxOption={};
                    ajaxOption.url=restApi.url_saveOrUpdateUserAttach;
                    ajaxOption.postData={
                        fileGroup:data.fastdfsGroup,
                        filePath:data.fastdfsPath,
                        fileName:data.fileName,
                        userId:window.currentUserId
                    };
                    m_ajax.postJson(ajaxOption,function(response){
                        if(response.code==='0'){
                            var path = window.fastdfsUrl + data.fastdfsGroup + '/' + data.fastdfsPath;
                            $(that.element).find('#headImage').attr('src', path);
                            //右上角个人头像
                            $('#navbar ul.navbar-right li .img-circle').attr('src', path);
                            S_toastr.success("保存成功");
                        } else {
                            S_layer.error(response.info);
                        }
                    });
                }
            });
        }
        //刷新当前组件
        ,refresh:function () {
            var that = this;
            var option = {};
            option.url = restApi.url_userInfo;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {
                    that.userDto = response.data;
                    $('#box_detail').m_userInfo({userDto: that.userDto});
                } else {
                    S_layer.error(response.info);
                }
            });
        }
        //金额比例验证
        ,_saveUserMajor_validate:function(){
            var that = this;
            $(that.element).find('form.userInfo_profession_edit').validate({
                rules: {
                    major:{
                        required:true
                    }
                },
                messages: {
                    major:{
                        required:'请选择专业!'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.appendTo(element.closest('.row').find('.col-md-12'));
                }
            });
        }
    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            new Plugin(this, options);
        });
    };

})(jQuery, window, document);
