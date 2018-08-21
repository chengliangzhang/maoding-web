/**
 * 上传文件
 * Created by wrb on 2018/1/11.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_docmgr_fileUpload",
        defaults = {
            $title:null,
            $isDialog:true,
            $selectedNodeObj:null,//当前选中对象
            $saveCallBack:null//保存后事件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._uploadmgrContainer = null;//上传容器
        this._btnNewDir = null;
        this._btnUpload = null;
        this._fileListItemsContainer = null;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData(function () {
                that.bindBtnUpload();
            });
        }
        //初始化数据并加载模板
        ,initHtmlData:function (callBack) {
            var that = this;
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||'上传文件',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '800',
                    height: '370',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancelText:'关闭',
                    cancel:function () {

                    }
                },function(d){//加载html后触发

                    that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                    var html = template('m_docmgr/m_docmgr_fileUpload',{});
                    $(that.element).html(html);
                    if(callBack!=null){
                        callBack();
                    }
                });
            }else{//不以弹窗编辑
                var html = template('m_docmgr/m_docmgr_fileUpload',{
                });
                $(that.element).html(html);
                if(callBack!=null){
                    callBack();
                }
            }
        }
        //上传文件
        , bindBtnUpload: function () {
            var that = this;
            that._btnUpload = $(that.element).find('.btnUpload:eq(0)');
            that._uploadmgrContainer = $(that.element).find('.uploadmgrContainer:eq(0)');

            //防止重复渲染
            if (that._uploadmgrContainer.find('.uploadmgr').length > 0)
                return false;


            that._uploadmgrContainer.m_uploadmgr({
                server: restApi.url_netFile_uploadFile,
                btnPickText : '<i class="fa fa-upload"></i>&nbsp;上传附件',
                ifCloseItemFinished : false,
                boxClass : 'no-borders',
                isShowBtnClose : false,
                auto:true,
                beforeFileQueued: function (file, m_uploadmgr) {

                    return true;
                },
                uploadBeforeSend: function (object, data, headers) {
                    data.companyId = window.currentCompanyId;
                    data.accountId = window.currentUserId;
                    data.projectId = that.settings.$selectedNodeObj.projectId;
                    data.pid = that.settings.$selectedNodeObj.id;
                },
                uploadSuccessCallback: function (file, res) {
                    if (res.code === '0') {
                        if(that.settings.$saveCallBack!=null){
                            that.settings.$saveCallBack();
                        }
                    }
                }
            }, true);

            return false;
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
