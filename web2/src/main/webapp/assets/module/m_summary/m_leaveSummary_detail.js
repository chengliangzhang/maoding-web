/**
 * 请假详情
 * Created by wrb on 2017/12/26.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_leaveSummary_detail",
        defaults = {
            $title:null,//弹窗标题
            $url:null,//请求地址
            $type:3,//默认3=请假,4=出差
            $detailData:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._typeStr = this.settings.$type==3?'请假':'出差';
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.getData();
        }
        //加载弹窗
        ,getData: function () {
            var that = this;
            S_dialog.dialog({
                title: that.settings.$title||'请假申请明细',
                contentEle: 'TConsentOBox',
                lock: 3,
                width: '800',
                minHeight: '450',
                tPadding: '0px',
                url: rootPath+'/assets/module/m_common/m_dialog.html',
                cancelText:'关闭',
                cancel:function () {

                }
            },function(d){//加载html后触发
                that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                that.getExpData();
            });
        }
        //加载
        ,getExpData:function(){
            var that = this;
            var option = {};
            option.url=that.settings.$url;
            option.postData={};
            option.postData.id=that.settings.$detailData.id;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    var $data={};
                    $data.detailData = response.data;
                    $data.fastdfsUrl = window.fastdfsUrl;
                    $data.type = that.settings.$type;
                    $data.typeStr = that._typeStr;
                    var html = template('m_summary/m_leaveSummary_detail',$data);
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
