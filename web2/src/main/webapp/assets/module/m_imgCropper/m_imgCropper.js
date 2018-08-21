/**
 * 图片裁剪
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_imgCropper",
        defaults = {
            title: "设置头像",
            showDialog: true,
            croppedCallback: null,
            zoomWidth: 0,//剪切后的缩放宽度
            zoomHeight: 0,//剪切后的缩放高度
            originalFileGroup: null,//
            originalFilePath: null,//
            originalFileName: null,//
            cut_deletePath:null,//剪切后的要删除的（一般指上一个头像之类）
            cropper:{
                options:{
                    aspectRatio: 1 / 1,
                    preview: '.img-preview',
                    zoomable: false,
                    minCropBoxWidth: 50,
                    minCropBoxHeight: 50
                }
            }
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._x = 0;
        this._y = 0;
        this._width = 0;
        this._height = 0;

        this._originalFileGroup = '';
        this._originalFilePath = '';
        this._originalFileName = '';

        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderHtml();
        },
        //渲染界面
        renderHtml: function () {
            var that = this;
            if (that.settings.showDialog) {//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title,
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '600',
                    minHeight: '300',
                    tPadding: '0px',
                    url: window.rootPath + '/assets/module/m_common/m_dialog.html',
                    cancel: function () {

                    },
                    okText: '保存',
                    ok: function () {
                        var imgSrc = $('.setArea .thumbnail img.img-responsive').attr('src');
                        if(imgSrc && imgSrc!=null && imgSrc!=''){
                            that._saveCroppedImage();
                        }else{
                            S_toastr.warning('上传图片不能为空！');
                            return false;
                        }

                    }
                }, function (d) {//加载html后触发
                    that.element = 'div[id="content:' + d.id+'"] .dialogOBox';
                    that._renderTemplate();
                    that.renderPic();
                });
            } else {//不以弹窗编辑
                that._renderTemplate();
                that.renderPic();
            }
        },
        _renderTemplate: function () {
            var that = this;
            var html = template('m_imgCropper/m_imgCropper', {});
            $(that.element).html(html);
            // that._bindUploadOrinalImage();
        }
        //渲染图片
        ,renderPic:function(){
            var that = this;
            var originalFileGroup = that.settings.originalFileGroup;
            var originalFilePath = that.settings.originalFilePath;
            $('.m_imgCropper .img-container').attr('src', window.fastdfsUrl + originalFileGroup + '/' + originalFilePath);

            $(that.element).find('.title:eq(0)').addClass('hide');
            $(that.element).find('.setArea:eq(0)').removeClass('hide');

            setTimeout(function () {
                that._setImage();
            }, 500);
        }
        //上传原图
        , _bindUploadOrinalImage: function () {
            var that = this;
            $('.m_imgCropper .btnFilePicker').m_imgUploader({
                server: window.fileCenterUrl + "/fastUploadImage",
                formData: {
                    cut_deleteGroup:that.settings.cut_deleteGroup,
                    cut_deletePath:that.settings.cut_deletePath
                },
                innerHTML: '<i class="fa fa-upload fa-fixed"></i>',
                uploadSuccessCallback: function (file, response) {
                    that._originalFileGroup = response.data.fastdfsGroup;
                    that._originalFilePath = response.data.fastdfsPath;
                    that._originalFileName = response.data.fileName;

                    //渲染图片
                    $('.m_imgCropper .img-container').attr('src', window.fastdfsUrl + that._originalFileGroup + '/' + that._originalFilePath);

                    $(that.element).find('.title:eq(0)').addClass('hide');
                    $(that.element).find('.setArea:eq(0)').removeClass('hide');

                    setTimeout(function () {
                        that._setImage();
                    }, 500);

                    S_toastr.success(response.info);
                }
            },true);
        }
        //保存裁切过的图片并替换Fastdfs上原图
        , _saveCroppedImage: function () {
            var that = this;
            var cutPostData = {
                x: that._x,
                y: that._y,
                width: that._width,
                height: that._height,
                zoomWidth: that.settings.zoomWidth,
                zoomHeight: that.settings.zoomHeight,
                group: that.settings.originalFileGroup,
                path: that.settings.originalFilePath,
                fileName: that.settings.originalFileName
            };
            var option = {};
            option.url = window.fileCenterUrl + "/fastCutImage";
            option.postData = cutPostData;
            m_ajax.postJson(option, function (response) {
                if (response.code === '0') {
                    if (that.settings.croppedCallback !== null) {
                        return that.settings.croppedCallback(response.data);
                    }
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //初始化cropping插件
        , _setImage: function () {
            var that = this;
            var $image = $('.m_imgCropper .img-container');
            /*var options = {
                aspectRatio: 1 / 1,
                preview: '.img-preview',
                zoomable: false,
                minCropBoxWidth: 50,
                minCropBoxHeight: 50,
                crop: function (e) {
                    that._x = e.x;
                    that._y = e.y;
                    that._width = e.width;
                    that._height = e.height;
                }
            };*/

            that.settings.cropper.options.crop=function(e){
                that._x = e.x;
                that._y = e.y;
                that._width = e.width;
                that._height = e.height;
            };

            $image.on({
                'build.cropper': function (e) {

                },
                'built.cropper': function (e) {

                },
                'cropstart.cropper': function (e) {

                },
                'cropmove.cropper': function (e) {

                },
                'cropend.cropper': function (e) {

                },
                'crop.cropper': function (e) {
                    that._x = e.x;
                    that._y = e.y;
                    that._width = e.width;
                    that._height = e.height;
                },
                'zoom.cropper': function (e) {

                }
            }).cropper(that.settings.cropper.options);
        }

    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            //if (!$.data(this, "plugin_" + pluginName)) {
            //$.data(this, "plugin_" +
                //pluginName, new Plugin(this, options));
            //}
            new Plugin(this, options);
        });
    };

})(jQuery, window, document);
