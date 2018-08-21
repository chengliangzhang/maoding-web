/**
 * Created by wrb on 2016/12/19.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_bulkImportListTips",
        defaults = {
            $userErrorList:[]
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
            this.initHtmlData();
        }
        //数据并加载模板
        ,initHtmlData:function () {
            var that = this;
            var $data = {};
            if(that.settings.$userErrorList!=null && that.settings.$userErrorList.length>0){
                $data.userErrorList =  that.settings.$userErrorList;
                var html = template('m_org/m_bulkImportListTips',$data);
                $(that.element).html(html);
            }

        }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;

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
