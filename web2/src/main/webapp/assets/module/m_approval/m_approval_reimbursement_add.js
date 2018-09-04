/**
 * 添加报销申请
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_reimbursement_add",
        defaults = {
            isDialog:true,
            buttonType: 0,
            reimburseObj: {}
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._uploadFileList = [];
        this._detailLen = 0;//报销条目的tr的target值，用于辨认tr
        this._uploadmgrContainer = null;
        this._maxExpNo = null;
        this._uuid = UUID.genV4().hexNoDelim;

        this._baseData = null;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            that.renderDialog(function () {
                var option = {};
                option.url = restApi.url_getExpBaseData;
                m_ajax.get(option, function (response) {
                    if (response.code == '0') {

                        that._baseData = response.data;
                        var html = template('m_approval/m_approval_reimbursement_add', {data: that._baseData});
                        $(that.element).html(html);
                        that.addItem();
                        that.fileUpload();
                        that.bindActionClick();

                    } else {
                        S_dialog.error(response.info);
                    }
                });
            });

        }
        //初始化数据,生成html
        ,renderDialog:function (callBack) {

            var that = this;
            if(that.settings.isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title||'报销申请',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '705',
                    tPadding: '0',
                    height:'650',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancel:function () {

                    },
                    ok:function () {
                        that.save();
                    }
                },function(d){//加载html后触发
                    that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                    if(callBack!=null)
                        callBack();

                });
            }else{//不以弹窗编辑
                if(callBack!=null)
                    callBack();
            }
        }

        //添加明细
        ,addItem:function () {
            var that = this;
            var panelBoxLen = $(that.element).find('.panel').length;
            that._baseData.itemIndex = panelBoxLen+1;
            var html = template('m_approval/m_approval_reimbursement_item', {data:that._baseData});
            $(that.element).find('button[data-action="addItem"]').parents('.form-group').before(html);

            var $ele = $(that.element).find('button[data-action="addItem"]').parents('.form-group').prev();
            $ele.find('select[name="expType"]').select2({
                tags:false,
                allowClear: false,
                minimumResultsForSearch: -1,
                language: "zh-CN"
            });
            $ele.find('select[name="projectName"]').select2({
                allowClear: true,
                //minimumResultsForSearch: -1,
                placeholder: "请选择关联项目!",
                language: "zh-CN"
            });
            $ele.find('a[data-action="delItem"]').on('click',function () {
                $(this).closest('.panel').remove();
                $(that.element).find('span[data-action="itemIndex"]').each(function (i) {
                    $(this).html(i+1);
                });
            });
        }
        ,fileUpload:function () {
            var that =this;
            var option = {};
            that._uploadmgrContainer = $(that.element).find('.uploadmgrContainer:eq(0)');
            option.server = restApi.url_attachment_uploadExpenseAttach;
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
                                S_dialog.error(res.msg);
                            }
                        });
                    };
                    ajaxDelete();

                    $(this).closest('span').remove();
                })
            });
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action]').off('click').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');

                switch (dataAction){
                    case 'addItem':
                        that.addItem();
                        return false;
                        break;

                }

            })
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
