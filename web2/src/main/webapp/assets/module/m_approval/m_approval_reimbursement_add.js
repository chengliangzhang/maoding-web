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

                        var html = template('m_approval/m_approval_reimbursement_add', {data: response.data});
                        $(that.element).html(html);

                    } else {
                        S_dialog.error(response.info);
                    }
                });
                $('body').bind('click', function (event) {
                    that.closeExpTypeSquare(event);
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
                    minHeight: that.settings.dialogMinHeight || '460',
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
        //加载基本数据
        , getData: function () {
            var that = this;
            var expTypeData = {};
            var html = template('m_approval/m_approval_reimbursement_add', {reimburseObj: that.settings.reimburseObj});
            $(that.element).html(html);

            /************首次打开，添加一条报销明细************/
            var options = {};
            options.tableId = '#reimburseTable';
            options.detailLen = that._detailLen;
            options.callBack1 = function (expTypeData) {
                var $obj = $('#reimburseTable tbody tr[target="' + expTypeData.i + '"]');
                $obj.attr('expName', expTypeData.expName);
                $obj.attr('expPName', expTypeData.expPName);
                $obj.attr('expAllName', expTypeData.expAllName);
                $obj.attr('expType', expTypeData.expType);
                $obj.attr('expParentType', expTypeData.expParentType);
            };
            $('#reimburseTable').m_addExpDetailTr(options);

            /************是否添加返回，保存按钮************/
            /*if (that.settings.buttonType == 0) {
                var bHtml = '<a type="button" class="btn btn-white getClickFun rounded m-r-xs" data-action="backToLast" >返回</a>';
                bHtml += '<a type="button" class="btn btn-primary getClickFun rounded" data-action="saveApplyExp" >保存</a>';
                $('#toExpApplication').find('div.footTools').prepend(bHtml).css('text-align', 'right');
            }*/
            that.addActionClick(expTypeData);
            that.updateAmount();
        }
        //收起报销类型选择框
        , closeExpTypeSquare: function (event) {
            if ($(event).parents('.typeNameArea').length == 0) {
                $('.typeNameArea').each(function () {
                    if (!($(this).is('.hide'))) {
                        $(this).addClass('hide');
                    }
                });
            }
        }
        //添加点击事件
        , addActionClick: function () {
            var that = this;
            $('#toExpApplication .getClickFun').each(function () {
                var action = $(this).attr('data-action');
                $(this).bind('click', function (event) {
                    if (action == 'auditPerson') {//点击选择审核人
                        var options = {};
                        var $this = $(this);
                        options.treeUrl = restApi.url_getOrgTreeForSearch;
                        var userId = $(this).attr('userId');
                        if (userId != null && userId != '') {
                            options.selectedUserList = [{
                                id: userId,
                                userName: $(this).val()
                            }];
                        }
                        options.selectUserCallback = function (data, event) {
                            S_dialog.close($(event));
                            $('#toExpApplication').find('#auditPerson').attr('userId', data.companyUserId);
                            $('#toExpApplication').find('#auditPerson').val(data.userName);
                        };
                        options.saveCallback = function (data) {
                            if (data == null || data.selectedUserList == null || data.selectedUserList.length == 0) {
                                $this.attr('userId', '');
                                $this.val('');
                            }
                        };
                        $(that.element).m_orgByTree(options);

                    } else if (action == "fileUpload") {
                        var option = {};
                        that._uploadmgrContainer = $(that.element).find('.uploadmgrContainer:eq(0)');
                        option.server = restApi.url_attachment_uploadExpenseAttach;
                        option.accept={
                            title: '请选择图片',
                            extensions: 'jpg,jpeg,png,bmp',
                            mimeTypes: 'image/jpg,image/jpeg,image/png,image/bmp'
                        };
                        option.closeIfFinished = true;
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
                                that.bindAttachAction();
                            } else {
                                $uploadItem.find('.span_status:eq(0)').html('上传失败');
                            }

                        };
                        that._uploadmgrContainer.m_uploadmgr(option, true);
                    }
                    else if (action == 'addExpItem') {//点击添加报销明细
                        that._detailLen++;
                        var len = $('#reimburseTable tbody tr').length - 2;
                        if (len >= 4) {
                            S_toastr.warning('报销明细不能超过4条！');
                            return;
                        }
                        var i = len;
                        var options = {};
                        options.tableId = '#reimburseTable';
                        options.detailLen = that._detailLen;
                        options.callBack1 = function (expTypeData) {
                            var $obj = $('#reimburseTable tbody tr[target="' + expTypeData.i + '"]');
                            $obj.attr('expName', expTypeData.expName);
                            $obj.attr('expPName', expTypeData.expPName);
                            $obj.attr('expAllName', expTypeData.expAllName);
                            $obj.attr('expType', expTypeData.expType);
                            $obj.attr('expParentType', expTypeData.expParentType);
                        };
                        $('#reimburseTable').m_addExpDetailTr(options);
                        that.closeExpTypeSquare(event);
                    } else if (action == 'backToLast') {

                    }
                    else if (action == 'saveApplyExp') {
                        var $data = {};
                        $data.detailList = [];
                        $('table#reimburseTable tbody tr[target]').each(function () {
                            var $this = $(this), expItem = {};
                            expItem.expAmount = $this.find('input#expAmount').val();
                            expItem.expUse = $this.find('input#expUse').val();
                            expItem.projectId = $this.find('select#projectId').val();
                            expItem.expName = $this.attr('expName') ? $this.attr('expName') : '';
                            expItem.expPName = $this.attr('expPName') ? $this.attr('expPName') : '';
                            expItem.expAllName = $this.attr('expAllName') ? $this.attr('expAllName') : '';
                            expItem.expType = $this.attr('expType') ? $this.attr('expType') : '';
                            expItem.expParentType = $this.attr('expParentType') ? $this.attr('expParentType') : '';
                            $data.detailList.push(expItem);
                        });
                        $data.type = $(that.element).find('#receiptType').val();
                        $data.auditPersonName = $('form.tessExpenseBox input[data-action="auditPerson"]').val();
                        $data.auditPerson = $('form.tessExpenseBox input[data-action="auditPerson"]').attr('userid');
                        $data.remark = $("form.tessExpenseBox input#remark").val();
                        $data.userId = window.currentCompanyUserId;
                        $data.expNo = that._maxExpNo;
                        $data.targetId = that._uuid;

                        var option = {};
                        option.url = restApi.url_saveOrUpdateExpMainAndDetail;
                        option.postData = $data;
                        if ($data.auditPersonName == '' || $data.auditPersonName == null) {
                            S_toastr.warning('请选择审批人');
                            return;
                        }
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
                    event.stopPropagation();
                });
            });
        }
        , bindAttachAction: function () {
            var that = this;
            that.bindAttachPreview();
            that.bindAttachDelele();
        }
        , bindAttachPreview: function () {
            $.each($('#showFileLoading').find('a[data-action="preview"]'), function (i, o) {
                $(o).off('click.preview').on('click.preview', function () {
                    var $a = $(this);
                    var pic = [];
                    pic.push({
                        alt: $a.attr('data-name'),
                        pid: $a.attr('data-net-file-id'),
                        src: $a.attr('data-src')
                    });
                    $.each($('#showFileLoading').find('a[data-action="preview"]'), function (j, p) {
                        var $p = $(p);
                        if ($p.attr('data-net-file-id') !== $a.attr('data-net-file-id')) {
                            pic.push({
                                alt: $p.attr('data-name'),
                                pid: $p.attr('data-net-file-id'),
                                src: $p.attr('data-src')
                            })
                        }
                    });
                    var photos={
                            title: '报销附件',
                            id: 1,
                            start: 0,
                            data: pic
                        }
                    ;
                    layer.photos({
                        photos: photos,
                        shift: 5
                    });
                })
            });
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
        //刷新总计数据
        , updateAmount: function () {
            var time = setInterval(function () {
                var amount = 0;
                $('input#expAmount').each(function () {
                    amount += parseFloat($(this).val());
                });
                amount = amount ? amount.toFixed(2) : '';
                amount = expNumberFilter(amount);
                $('span#expAmount').html(amount);
                $('div.footTools a[data-action="saveApplyExp"]').bind('click', function () {
                    clearInterval(time);
                });
            }, 100);
        }
        //返回报销列表
        ,refreshMyExpense:function () {

            $('ul.nav-second-level li a[id="myExpense"]').click();
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
