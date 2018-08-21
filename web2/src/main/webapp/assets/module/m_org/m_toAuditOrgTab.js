/**
 * 待审核组织－tab页
 * Created by wrb on 2016/12/18.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_toAuditOrgTab",
        defaults = {
            $isDialog:true,
            $dataUrl:null,
            $auditOrgCallBack:null
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
            this.initHtmlData(function () {
                that.getOrgList(2);
                that.bindActionClick();
            });
        }
        //数据并加载模板
        ,initHtmlData:function (callBack) {

            var that = this;
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title||'待审核组织',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '800',
                    maxHeight:'700',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancelText:'关闭',
                    cancel:function () {

                    }
                },function(d){//加载html后触发

                    var html = template('m_org/m_toAuditOrgTab',{});
                    $('div[id="content:'+d.id+'"] .dialogOBox').html(html);

                    if(callBack!=null){
                        callBack();
                    }

                });
            }else{//不以弹窗编辑

                var html = template('m_org/m_toAuditOrgTab',{userAuditList:data});
                $(that.element).html(html);
                if(callBack!=null){
                    callBack();
                }
            }


        }
        ,getOrgList:function (t) {
            var that = this;
            var options = {};
            options.$type = t;
            options.$auditOrgCallBack = that.settings.$auditOrgCallBack;
            if(t==2){
                $('.toAuditOrgTabBox #branch section').m_toAuditOrgList(options);
            }else{
                $('.toAuditOrgTabBox #businessPartner section').m_toAuditOrgList(options);
            }
            return false;

        }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;
            $('.toAuditOrgTabBox a[data-action]').on('click',function () {
                var dataAction = $(this).attr('data-action');
                if(dataAction=='getCompanyToAudit2'){
                    that.getOrgList(2);
                    //return false;
                }else if(dataAction=='getCompanyToAudit3'){
                    that.getOrgList(3);
                    //return false;
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
