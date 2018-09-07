/**
 * 报销申请详情
 * Created by wrb on 2018/9/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_leave_details",
        defaults = {
            isDialog:true,
            id:null,
            doType: 1
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._uploadmgrContainer = null;
        this._uuid = UUID.genV4().hexNoDelim;//targetId

        this._baseData = null;

        this._title = this.settings.doType==1?'报销':'费用';

        this._currentCompanyUserId = window.currentCompanyUserId;
        this._dialogHeight = 550;

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderDialog(function () {
                that.renderContent();
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
                    height:that._dialogHeight+'',
                    url: rootPath+'/assets/module/m_common/m_dialog.html'
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
        ,renderContent:function () {
            var that = this;
            var option = {};
            option.url = restApi.url_getLeaveDetailForWeb;
            option.postData = {
                id:that.settings.id
            };
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    that._baseData = response.data;
                    that._baseData.doType = that.settings.doType;
                    that._baseData.title = that._title;
                    that._baseData.currentCompanyUserId = that._currentCompanyUserId;
                    that._baseData.dialogHeight = 'height:'+(that._dialogHeight-55)+'px';

                    var html = template('m_approval/m_approval_leave_details', that._baseData);
                    $(that.element).html(html);
                    that.bindActionClick();

                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action]').off('click').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');

                switch(dataAction){
                    case 'cancel'://取消
                        S_dialog.close($(that.element));
                        break;
                    case 'agree'://同意
                        var option = {};
                        option.dataInfo = {
                            id:that.settings.id,
                            processFlag:that._baseData.processFlag
                        };
                        option.doType = 1;
                        option.saveCallBack = function () {
                            that.renderContent();
                        };
                        console.log(option);
                        $('body').m_approval_operational_comments(option,true);

                        break;
                    case 'returnBack'://退回

                        var option = {};
                        option.dataInfo = {
                            id:that.settings.id,
                            processFlag:that._baseData.processFlag
                        };
                        option.doType = 2;
                        option.saveCallBack = function () {
                            that.renderContent();
                        };
                        $('body').m_approval_operational_comments(option,true);

                        break;
                    case 'cancellation'://撤销

                        var option = {};
                        option.dataInfo = {
                            id:that.settings.id,
                            processFlag:that._baseData.processFlag
                        };
                        option.doType = 3;
                        option.saveCallBack = function () {
                            that.renderContent();
                        };
                        $('body').m_approval_operational_comments(option,true);

                        break;
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
