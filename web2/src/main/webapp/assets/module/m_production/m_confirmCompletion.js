/**
 * 生产安排-确认完成
 * Created by wrb on 2018/7/2.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_confirmCompletion",
        defaults = {
            $taskId:null,//任务ID
            $status:null,//任务状态
            $saveCallBack:null,
            $isDialog:true
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
            that.renderPage(function () {
                //that.bindFileUplad();
            });

        }
        ,renderPage:function (callBack) {
            var that = this;
            var currDate = getNowDate();
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||'确认完成',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '500',
                    height:'150',
                    tPadding:'0',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    ok:function(){

                        if ($('form.m_confirmCompletion').valid()) {

                            var completeDate = $('form.m_confirmCompletion input[name="completeDate"]').val();
                            var completion = $('form.m_confirmCompletion textarea[name="completion"]').val();
                            var option  = {};
                            option.url = restApi.url_completeTask;
                            option.postData = {
                                taskId:that.settings.$taskId,
                                status:that.settings.$status,
                                paidDate:completeDate,
                                completion:completion
                            };
                            m_ajax.postJson(option,function (response) {
                                if(response.code=='0'){
                                    S_toastr.success('操作成功');

                                    if(that.settings.$saveCallBack!=null){
                                        that.settings.$saveCallBack();
                                    }
                                }else {
                                    S_dialog.error(response.info);
                                }
                            });

                        } else {
                            return false;
                        }
                    },
                    cancelText:'取消',
                    cancel:function(){
                    }

                },function(d){//加载html后触发

                    that.element = $('div[id="content:'+d.id+'"] .dialogOBox');
                    var html = template('m_production/m_confirmCompletion',{currDate:currDate});
                    $(that.element).html(html);
                    that.confirmCompletion_validate();
                    if(callBack!=null)
                        callBack();
                });
            }else {//不以弹窗编辑
                var html = template('m_production/m_confirmCompletion', {currDate: currDate});
                $(that.element).html(html);
                if(callBack!=null)
                    callBack();
            }
        }
        //上传文件
        , bindFileUplad:function () {
            var that =this;
            var option = {};
            that._uploadmgrContainer = $(that.element).find('.uploadmgrContainer:eq(0)');
            option.server = restApi.url_attachment_uploadNoticeAttach;
            option.accept={
                title: '请选择文件',
                extensions: '*',
                mimeTypes: '*'
            };
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
                    that.bindAttachDelele();
                } else {
                    $uploadItem.find('.span_status:eq(0)').html('上传失败');
                }

            };
            that._uploadmgrContainer.m_uploadmgr(option, true);
        }
        //删除文件
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
        , bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').off('click').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'fileUpload'://上传文件
                        that.bindFileUplad();
                        break;
                }

            })
        }
        //时间验证
        , confirmCompletion_validate: function () {
            var that = this;
            $(that.element).find('form').validate({
                rules: {
                    completeDate: 'required'
                },
                messages: {
                    completeDate: '请选择时间！'
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.appendTo(element.closest('.col-md-8'));
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
