/**
 * 添加报销申请
 * Created by wrb on 2018/9/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_cost_add",
        defaults = {
            isDialog:true,
            doType: 1
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._uploadmgrContainer = null;
        this._maxExpNo = null;//报销编号
        this._uuid = UUID.genV4().hexNoDelim;//targetId

        this._baseData = null;
        this._passAuditData = null;//关联审批

        this._title = this.settings.doType==1?'报销':'费用';
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.getMaxExpNo();
            that.renderDialog(function () {
                var option = {};
                option.url = restApi.url_getExpBaseData;
                m_ajax.get(option, function (response) {
                    if (response.code == '0') {

                        that._baseData = response.data;
                        that._baseData.doType = that.settings.doType;
                        that._baseData.title = that._title;
                        var html = template('m_approval/m_approval_cost_add', {data: that._baseData});
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
                    title: that.settings.title||that._title+'申请',
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
        //保存
        ,save:function () {
            var that = this;
            var $data = {};
            $data.detailList = [];
            $(that.element).find('.panel.panel-default').each(function () {
                var $this = $(this), expItem = {};
                expItem.expAmount = $this.find('input[name="expAmount"]').val();
                expItem.expUse = $this.find('textarea[name="expUse"]').val();
                expItem.projectId = $this.find('select[name="projectName"]').val();
                expItem.expType =  $this.find('select[name="expType"] option:selected').text();
                expItem.expParentType = $this.find('select[name="expType"] option:selected').parent().attr('label');
                $data.detailList.push(expItem);
            });
            $data.type = 1;
            $data.remark = $("form.tessExpenseBox input#remark").val();
            $data.userId = window.currentCompanyUserId;
            $data.expNo = that._maxExpNo;
            $data.targetId = that._uuid;

            var option = {};
            option.url = restApi.url_saveOrUpdateExpMainAndDetail;
            option.postData = $data;

            var errors = [];
            $('tr[target]').each(function () {
                if ($(this).find('input#expAmount').val() == '') {
                    errors.push('请输入报销金额');
                    return;
                }
                if ($(this).find('input#collectExpType').val() == '') {
                    errors.push('请选择报销类别');
                    return;
                }
                if ($(this).find('input#expUse').val() == '') {
                    errors.push('请输入用途说明');
                    return;
                }
            });
            if (errors.length > 0) {
                S_toastr.warning(errors[0]);
                return;
            }
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('操作成功');
                    that.refreshMyExpense();
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //添加明细
        ,addItem:function () {
            var that = this;
            var panelBoxLen = $(that.element).find('.panel').length;
            that._baseData.itemIndex = panelBoxLen+1;
            var html = template('m_approval/m_approval_cost_add_item', {data:that._baseData});
            $(that.element).find('button[data-action="addItem"]').parents('.form-group').before(html);

            var $ele = $(that.element).find('button[data-action="addItem"]').parents('.form-group').prev();
            $ele.find('select[name="expType"]').select2({
                tags:false,
                allowClear: false,
                minimumResultsForSearch: -1,
                width:'100%',
                language: "zh-CN"
            });
            $ele.find('select[name="projectName"]').select2({
                allowClear: true,
                //minimumResultsForSearch: -1,
                placeholder: "请选择关联项目!",
                width:'100%',
                language: "zh-CN"
            });
            $ele.find('a[data-action="delItem"]').on('click',function () {
                $(this).closest('.panel').remove();
                $(that.element).find('span[data-action="itemIndex"]').each(function (i) {
                    $(this).html(i+1);
                });
            });
            that.getPassAuditData($ele);
        }
        //关联审批
        ,getPassAuditData:function ($ele) {
            var that = this;
            var option = {};
            option.url = restApi.url_getPassAuditData;
            m_ajax.post(option, function (response) {
                if (response.code == '0') {

                    var data = [];
                    if(response.data!=null && response.data.length>0){
                        $.each(response.data, function (i, o) {
                            data.push({id: o.id, text: o.companyName});
                        });
                    }
                    $ele.find('select[name="linkageApproval"]').select2({
                        width: '100%',
                        allowClear: true,
                        language: "zh-CN",
                        minimumResultsForSearch: Infinity,
                        placeholder: "请选择关联审批!",
                        data: data
                    });


                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //进入页面获取报销编号
        , getMaxExpNo: function (callback) {
            var that = this;
            var options = {};
            options.postData = {};
            options.url = restApi.url_getMaxExpNo;
            options.postData.companyId = window.currentCompanyId;
            m_ajax.postJson(options, function (response) {
                if (response.code == "0") {
                    that._maxExpNo = response.data.maxExpNo;
                    if (callback) return callback(response.data.maxExpNo);
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //上传附件
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
        ,bindAttachDelele: function () {

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
