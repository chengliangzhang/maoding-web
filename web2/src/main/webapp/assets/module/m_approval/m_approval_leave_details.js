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
            closeCallBack:null,
            doType: 3// 报销=1=expense,费用=2=costApply,请假=3=leave,出差=4=onBusiness,付款申请=5=projectPayApply
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

        this._title = this.settings.doType==3?'请假':'出差';

        this._currentCompanyUserId = window.currentCompanyUserId;
        this._dialogHeight = 550;

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderContent();

        }
        //渲染弹窗
        ,renderDialog:function (html,callBack) {
            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.title||that._title+'申请',
                    area : '750px',
                    btn : false,
                    content:html

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;
                    if(callBack!=null)
                        callBack();
                });

            }else{//不以弹窗编辑
                $(that.element).html(html);
                if(callBack!=null)
                    callBack();
            }
        }
        //渲染内容
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
                    that.renderDialog(html,function () {
                        that.bindActionClick();
                    });

                } else {
                    S_layer.error(response.info);
                }
            });
        }
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action],a[data-action]').off('click').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');

                switch(dataAction){
                    case 'cancel'://取消
                        S_layer.close($(that.element));
                        if(that.settings.closeCallBack)
                            that.settings.closeCallBack();
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
