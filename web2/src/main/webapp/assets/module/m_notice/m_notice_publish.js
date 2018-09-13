/**
 * 发布公告
 * Created by wrb on 2018/1/25.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_notice_publish",
        defaults = {
            noticeId:null,
            companyName:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._noticeInfo = null;
        this._name = pluginName;
        this._uuid = UUID.genV4().hexNoDelim;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderPage();

        }
        ,renderPage:function () {
            var that = this;
            var html = template('m_notice/m_notice_publish', {});
            $(that.element).html(html);
            $('.summernote').summernote({
                height: 200,
                tabsize: 2,
                lang: 'zh-CN',
                toolbar: [
                    // [groupName, [list of button]]
                    ['style', ['bold', 'italic', 'underline', 'clear']],
                    ['font', ['strikethrough', 'superscript', 'subscript']],
                    ['fontsize', ['fontsize']],
                    ['color', ['color']],
                    ['para', ['ul', 'ol', 'paragraph']],
                    ['height', ['height']],
                    ['view', ['fullscreen', 'codeview']]
                ],
                callbacks: {
                    onFocus: function() {
                        //删除错误提示
                        var $summernoteError = $(that.element).find('#summernote-error');
                        if($summernoteError.length> 0){
                            $summernoteError.remove();
                        }
                    }
                }
            });
            that.bindClickFun();
            that.bindFileUplad();
        }
        , bindFileUplad:function () {
            var that =this;
            var option = {};
            that._uploadmgrContainer = $(that.element).find('.uploadmgrContainer:eq(0)');
            option.server = restApi.url_attachment_uploadNoticeAttach;
            option.accept={
                title: '上传附件',
                extensions: '*',
                mimeTypes: '*'
            };
            option.btnPickText = '<i class="fa fa-upload"></i>&nbsp;上传附件';
            option.ifCloseItemFinished = true;
            option.boxClass = 'no-borders';
            option.isShowBtnClose = false;
            option.uploadBeforeSend = function (object, data, headers) {
                data.companyId = window.currentCompanyId;
                data.accountId = window.currentUserId;
                data.targetId = that._uuid;
            };
            option.uploadSuccessCallback = function (file, response) {
                console.log(response);
                var fileData = {
                    netFileId: response.data.netFileId,
                    fileName: response.data.fileName,
                    fullPath: window.fastdfsUrl + response.data.fastdfsGroup + '/' + response.data.fastdfsPath
                };
                var $uploadItem = that._uploadmgrContainer.find('.uploadItem_' + file.id + ':eq(0)');
                if (!isNullOrBlank(fileData.netFileId)) {
                    $uploadItem.find('.span_status:eq(0)').html('上传成功');
                    var html = template('m_common/m_attach', fileData);
                    $('#showFileLoading').append(html);
                    var obj = 'a[data-net-file-id="' + fileData.netFileId + '"]';
                    that.bindAttachDelele();
                } else {
                    $uploadItem.find('.span_status:eq(0)').html('上传失败');
                }

            };
            that._uploadmgrContainer.m_uploadmgr(option, true);
        }
        , bindAttachDelele: function () {
            $.each($('#showFileLoading').find('a[data-action="deleteAttach"]'), function (i, o) {
                $(o).off('click.deleteAttach').on('click.deleteAttach', function () {
                    var netFileId = $(this).attr('data-net-file-id');

                    var ajaxDelete = function () {
                        var ajaxOption = {};
                        ajaxOption.classId = '.file-list:eq(0)';
                        ajaxOption.url = restApi.url_attachment_delete;
                        ajaxOption.postData = {
                            id: netFileId,
                            accountId: window.currentUserId
                        };
                        m_ajax.postJson(ajaxOption, function (res) {
                            if (res.code === '0') {
                                S_toastr.success("删除成功");
                            } else if (res.code === '1') {
                                S_layer.error(res.msg);
                            }
                        });
                    };
                    ajaxDelete();

                    $(this).closest('span').remove();
                })
            });
        }
        //给选择发送范围绑定触发事件
        , bindClickFun: function () {
            var that = this;
            var ids = '';
            $('.publishPublicNoticeOBox input[name="noticeTitle"]').bind('focus', function (event) {

                //删除错误提示
                var $noticeTitleError = $(that.element).find('#noticeTitle-error');
                if($noticeTitleError.length> 0){
                    $noticeTitleError.remove();
                }
                stopPropagation(event);
            });
            $('.publishPublicNoticeOBox input[data-action="choseDepartment"]').bind('click', function (event) {
                var options = {};
                var $this = $(this);
                options.title = '选择发送范围';
                options.isExcludeOrgChoice = 1;
                options.ids = ids;
                options.callBack = function (data) {
                    ids = data;
                    if (data && data.length > 0 && $("#choseDepartment-error").length > 0) {
                        $("#choseDepartment-error").remove();
                    } else if (!(data && data.length > 0) && $("#choseDepartment-error").length < 1) {
                        var iHtml = '<label id="choseDepartment-error" for="choseDepartment">请选择发送范围！</label>';
                        $this.after(iHtml);
                    }
                };
                $(that.element).m_onlyGetTeamByTree(options);

                //删除错误提示
                var $choseDepartmentError = $(that.element).find('#choseDepartment-error');
                if($choseDepartmentError.length> 0){
                    $choseDepartmentError.remove();
                }

                stopPropagation(event);
            });
            $(that.element).find('a[data-action]').bind('click', function (event) {

                var action = $(this).attr('data-action');
                switch (action){
                    case 'savePublish'://点击发送按钮
                        var orgList = [];
                        if (ids.indexOf('root' + ',') > -1) {
                            ids = ids.replace('root' + ',', '');
                        }
                        if (ids.lastIndexOf(',') != ids.length - 1) {
                            ids += ',';
                        }
                        if (ids.indexOf(',') > -1) {
                            ids = ids.substr(0, ids.length - 1);
                            orgList = ids.split(',');

                        }
                        var data = $('form.publishPublicNoticeOBox').serializeObject();
                        data.isSendMsg = 0;
                        if ($('input[name="ensureSendMessage"]').prop('checked')) {
                            data.isSendMsg = 1;
                        }
                        data.noticeContent = $('.summernote').summernote('code');
                        data.orgList = orgList;
                        data.companyId = window.currentCompanyId;
                        data.targetId = that._uuid;
                        var options = {};
                        options.postData = data;

                        if (that.savePublish_validate(ids)) {
                            options.url = restApi.url_notice;
                            m_ajax.postJson(options, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success('发布成功！');
                                    $('.summernote').summernote('destroy');
                                    location.hash = '/announcement';
                                } else {
                                    S_layer.error(response.info);
                                }

                            });
                        }
                        break;
                    case 'announcement'://返回公告列表
                        $('.summernote').summernote('destroy');
                        location.hash = '/announcement';
                        break;

                }
                stopPropagation(event);
            });
        }
        , savePublish_validate: function (ids) {
            var that = this;

            //验证标题　
            var $noticeTitle = $(that.element).find('input[name="noticeTitle"]');
            var noticeTitle = $noticeTitle.val();
            noticeTitle = $.trim(noticeTitle);
            if(noticeTitle=='' && $('#noticeTitle-error').length==0){
                $noticeTitle.after('<label id="noticeTitle-error" class="error" for="noticeTitle">请输入公告标题!</label>');
                return false;
            }

            //验证发送范围
            var $choseDepartment = $(that.element).find('form.publishPublicNoticeOBox input[name="choseDepartment"]');
            var range = $choseDepartment.val();

            if (!(range != "点击设置" && ids && ids.length > 0)) {
                if (!$("#choseDepartment-error").length > 0) {
                    $choseDepartment.after('<label id="choseDepartment-error" class="error" for="choseDepartment">请选择发送范围！</label>');
                }
                return false;
            }

            //发送内容，暂时只验证文字
            if ($(that.element).find('.note-editable').text() == "" && $(that.element).find('#summernote-error').length == 0) {

                $(that.element).find('.note-editor').after('<label id="summernote-error" class="error">公告内容不能为空！</label>');
                return false;
            }

            return true;
        }

        //清除公告内容验证
        , clearThisValidate: function (timer) {
            var that = this;
            $(that.element).find('label#summernote-error').remove();
            clearInterval(timer);
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
