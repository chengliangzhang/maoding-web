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
            doType: 1
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._uploadmgrContainer = null;
        this._maxExpNo = null;//报销编号
        this._uuid = UUID.genV4().hexNoDelim;//targetId

        this._baseData = null;
        this._passAuditData = null;//关联审批

        this._title = this.settings.doType==1?'报销':'费用';
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderDialog(function () {
                var option = {};
                option.url = restApi.url_getAuditDetailForExp;
                option.postData = {
                    id:that.settings.id
                }
                m_ajax.postJson(option, function (response) {
                    if (response.code == '0') {

                        that._baseData = response.data;
                        that._baseData.doType = that.settings.doType;
                        that._baseData.title = that._title;
                        var html = template('m_approval/m_approval_cost_details', {data: that._baseData});
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
                    title: that.settings.title||that._title+'申请',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '705',
                    tPadding: '0',
                    height:'650',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancel:function () {

                    },
                    ok:function () {
                        that.save();
                    }
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
