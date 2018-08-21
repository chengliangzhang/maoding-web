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
            that.initHtmlData();
        },
        //初始化数据
        initHtmlData:function (callBack) {
            var that = this;
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||'详情',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '800',
                    minHeight:'125',
                    height:'600',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancelText:'关闭',
                    cancel:function () {
                    }

                },function(d){//加载html后触发

                    that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                    that.initHtmlTemplate();

                });
            }else{//不以弹窗编辑
                that.initHtmlTemplate();
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
                    $(that.element).html(html);
                }else {
                    S_dialog.error(response.info);
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


