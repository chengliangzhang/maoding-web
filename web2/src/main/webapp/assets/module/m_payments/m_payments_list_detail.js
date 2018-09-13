/**
 * 应收或应付详情
 * Created by wrb on 2017/12/8.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_list_detail",
        defaults = {
            $title:null,
            $isDialog:true,
            $type:1,//默认是1=应收,2=应付
            $id:null//报销ID
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlTemplate();
        },
        //渲染弹窗
        renderDialog:function (html,callBack) {
            var that = this;
            if(that.settings.$isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.$title||'详情',
                    area : '750px',
                    content:html,
                    cancelText:'关闭',
                    cancel:function () {
                    }

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.$isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;
                    if(callBack)
                        callBack();
                });

            }else{//不以弹窗编辑
                $(that.element).html(html);
                if(callBack)
                    callBack();
            }
        }
        //生成html
        ,initHtmlTemplate:function () {
            var that = this;
            var options={};
            if(that.settings.$type==2){
                options.url=restApi.url_getPaymentDetail+'/'+that.settings.$id;
            }else{
                options.url=restApi.url_getReceivableDetail+'/'+that.settings.$id;
            }
            m_ajax.post(options,function (response) {
                if(response.code=='0'){
                    var html = template('m_payments/m_payments_list_detail',{
                        detailData:response.data,
                        type:that.settings.$type
                    });
                    that.renderDialog(html);
                }else {
                    S_layer.error(response.info);
                }
            })

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


