/**
 * 报销申请详情
 * Created by wrb on 2018/9/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_cost_details",
        defaults = {
            isDialog:true,
            id:null,
            closeCallBack:null,
            doType: 1// 报销=1=expense,费用=2=costApply,请假=3=leave,出差=4=onBusiness,付款申请=5=projectPayApply
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;


        this._baseData = null;

        this._title = this.settings.doType==1?'报销':'费用';
        this._currentCompanyUserId = window.currentCompanyUserId;
        this._dialogHeight = 650;
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
        //渲染弹窗
        ,renderDialog:function (callBack) {

            var that = this;
            if(that.settings.isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title||that._title+'申请',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '750',
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
        //渲染内容
        ,renderContent:function () {
            var that = this;
            var option = {};
            option.url = restApi.url_getAuditDetailForExp;
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
                    var html = template('m_approval/m_approval_cost_details', that._baseData);
                    $(that.element).html(html);
                    that.bindActionClick();

                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action],a[data-action]').off('click').on('click',function () {
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
                            if(that.settings.closeCallBack)
                                that.settings.closeCallBack();
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
                            if(that.settings.closeCallBack)
                                that.settings.closeCallBack();
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
                            if(that.settings.closeCallBack)
                                that.settings.closeCallBack();
                        };
                        $('body').m_approval_operational_comments(option,true);

                        break;
                    case 'preview'://查看文件

                        window.open($this.attr('data-src'));

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
