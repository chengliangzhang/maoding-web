/**
 * 基础财务数据设置-余额变更记录
 * Created by wrb on 2018/9/10.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_finance_basic_settings_change_record",
        defaults = {
            isDialog:true,
            id:null,//余额主表Id
            companyId:null,//余额主表companyId
            saveCallBack:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._currentCompanyUserId = window.currentCompanyUserId;
        this._currentCompanyId = window.currentCompanyId;
        this._currentUserId = window.currentUserId;
        this._fastdfsUrl = window.fastdfsUrl;

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderDialog(function () {

                var option = {};
                option.classId = that.element;
                option.url = restApi.url_listCompanyBalanceChangeDetail;
                option.postData = {
                    id:that.settings.id,
                    companyId:that.settings.companyId
                };

                m_ajax.postJson(option, function (response) {
                    if (response.code == '0') {
                        var html = template('m_finance/m_finance_basic_settings_change_record', {
                            dataList: response.data
                        });
                        $(that.element).html(html);
                    } else {
                        S_dialog.error(response.info);
                    }
                });


            });

        }
        //初始化数据,生成html
        ,renderDialog:function (callBack) {

            var that = this;
            if(that.settings.isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title||'余额变更记录',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '700',
                    tPadding: '0',
                    height: '310',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancel:function () {

                    },
                    cancelText:'关闭'
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
