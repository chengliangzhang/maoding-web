/**
 * 添加一条变更记录
 * Created by wrb on 2016/12/21.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_addTimeChangeRecord",
        defaults = {
            $data : null
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
            that.initHtmlTemplate(that.settings.$data);
        }
        //生成html
        ,initHtmlTemplate:function (data) {
            var that = this;
            var html = template('m_project/m_addTimeChangeRecord',data);
            $(that.element).append(html);
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
