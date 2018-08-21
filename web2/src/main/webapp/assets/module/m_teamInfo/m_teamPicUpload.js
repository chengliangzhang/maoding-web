/**
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_teamPicUpload",
        defaults = {
            filePath: null,
            isEdit: 'no',//判断是展示或是编辑状态
            fastUrl: null,
            isHaveFile: false//判断是否存在已上传过logo
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
            var that = this;
            that._renderHtml();
            that._bindBtnUpload();
        }
        //加载主要页面
        , _renderHtml: function () {
            var that = this;
            var html = template('m_teamInfo/m_teamPicUpload', {
                filePath: that.settings.filePath,
                isEdit: that.settings.isEdit
            });
            $(that.element).html(html);
        },
        _bindBtnUpload: function () {
            var that = this;
            var title = '上传LOGO';
            if (that.settings.isHaveFile != null && that.settings.isHaveFile != '') {
                title = '替换LOGO';
            }
            var headImg = null;
            $('#btnUploadImg').m_imgUploader({
                innerHTML: title,
                server: window.fileCenterUrl + "/fastUploadImage",
                formData: {
                    cut_deleteGroup: that.settings.cut_deleteGroup,
                    cut_deletePath: headImg
                },
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
            }, true);
        }
        //调到裁剪窗口
        , toCutDialog: function () {
            var that = this;
            var headImg = null;
            var title = '上传LOGO';
            if (that.settings.isHaveFile != null && that.settings.isHaveFile != '') {
                title = '替换LOGO';
            }
            $('body').m_imgCropper({
                showDialog: true,
                title:title,
                zoomWidth: 200,
                zoomHeight: 200,
                originalFileGroup: that._originalFileGroup,
                originalFilePath: that._originalFilePath,
                originalFileName: that._originalFileName,
                cut_deletePath: headImg,
                cropper: {
                    options: {
                        aspectRatio: 200 / 200,
                        preview: '.img-preview',
                        zoomable: false,
                        minCropBoxWidth: 200,
                        minCropBoxHeight: 200
                    }
                },
                croppedCallback: function (data) {
                    var ajaxOption = {};
                    ajaxOption.url = restApi.url_attachment_saveCompanyLogo;
                    ajaxOption.postData = $.extend({}, data, {
                        accountId: window.currentUserId,
                        companyId: window.currentCompanyId
                    });
                    m_ajax.postJson(ajaxOption, function (response) {
                        if (response.code === '0') {
                            var path = window.fastdfsUrl + data.fastdfsGroup + '/' + data.fastdfsPath;
                            $(that.element).find('.img-logo').attr('src', path);
                            S_toastr.success("保存成功");
                            $('.navbar-header .navbar-brand img').attr('src', path);
                        } else {
                            S_dialog.error(response.info);
                        }
                    });
                }
            });
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
