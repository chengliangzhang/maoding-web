/**
 * 付款申请详情
 * Created by wrb on 2018/9/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_payment_details",
        defaults = {
            isDialog:true,
            id:null,
            doType: 5// 报销=1=expense,费用=2=costApply,请假=3=leave,出差=4=onBusiness,付款申请=5=projectPayApply
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._baseData = null;
        this._currentCompanyUserId = window.currentCompanyUserId;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderContent();

        }
        //渲染弹窗
        ,renderDialog:function (html) {

            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.title||'付款申请',
                    area : '750px',
                    btn : false,
                    content:html
                    
                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;

                });

            }else{//不以弹窗编辑
                $(that.element).html(html);
            }
        }
        //渲染内容
        ,renderContent:function () {
            var that = this;
            var option = {};
            option.url = restApi.url_getProjectCostPaymentDetailByMainIdForPay;
            option.postData = {
                id:that.settings.id
            };
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    that._baseData = response.data;
                    that._baseData.doType = that.settings.doType;
                    that._baseData.title = that._title;
                    that._baseData.currentCompanyUserId = that._currentCompanyUserId;
                    var html = template('m_approval/m_approval_payment_details', that._baseData);
                    that.renderDialog(html);
                    that.bindActionClick();
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
