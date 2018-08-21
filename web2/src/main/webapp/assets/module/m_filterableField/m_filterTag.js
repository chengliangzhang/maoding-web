/**
 * Created by Wuwq on 2017/1/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_filterTag",
        defaults = {
            filters: [],
            onRemoved: null
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._render();
        }
        , _render: function () {
            var that = this;
            var html = template('m_filterableField/m_filterTag', {filters: that.settings.filters});
            $(that.element).html(html);
            that._bindBtnRemove();
        }
        , _bindBtnRemove: function () {
            var that = this;
            $(that.element).find('.filter-tag-remove').each(function (i, o) {
                $(o).click(function () {
                    var field = $(this).closest('.filter-tag').attr('data-field');
                    var filters = _.reject(that.settings.filters, function (o) {
                        return field === o.field;
                    });
                    that.settings.filters = filters;
                    if (that.settings.onRemoved)
                        that.settings.onRemoved();
                    that._render();
                });
            });
        }
        , saveFilter: function (f) {

            var that = this;

            //判断过滤值是否为空，如果为空值则去掉过滤
            if (f.filterValue !== null && !_.isBlank(f.filterValue)) {
                var m = _.find(that.settings.filters, function (o) {
                    return f.field === o.field;
                });
                //判断是否已存在过滤标签
                if (m && m !== null) {
                    m.filterValue = f.filterValue;
                    m.filterValueDisplayName = f.filterValueDisplayName;
                } else {
                    that.settings.filters.push(f);
                }
            } else {
                var filters = _.reject(that.settings.filters, function (o) {
                    return f.field === o.field;
                });
                that.settings.filters = filters;
            }

            that._render();
        }
        , getFilters: function () {
            var that = this;
            return that.settings.filters;
        }
        , getFilter: function (field) {
            var that = this;
            var filter = _.find(that.settings.filters, function (f) {
                return f.field === field;
            });
            return filter || null;
        }
    });

    /*
     1.一般初始化（缓存单例）： $('#id').pluginName(initOptions);
     2.强制初始化（无视缓存）： $('#id').pluginName(initOptions,true);
     3.调用方法： $('#id').pluginName('methodName',args);
     */
    $.fn[pluginName] = function (options, args) {
        var instance;
        var funcResult;
        var jqObj = this.each(function () {

            //从缓存获取实例
            instance = $.data(this, "plugin_" + pluginName);

            if (options === undefined || options === null || typeof options === "object") {

                var opts = $.extend(true, {}, defaults, options);

                //options作为初始化参数，若args===true则强制重新初始化，否则根据缓存判断是否需要初始化
                if (args === true) {
                    instance = new Plugin(this, opts);
                } else {
                    if (instance === undefined || instance === null)
                        instance = new Plugin(this, opts);
                }

                //写入缓存
                $.data(this, "plugin_" + pluginName, instance);
            }
            else if (typeof options === "string" && typeof instance[options] === "function") {

                //options作为方法名，args则作为方法要调用的参数
                //如果方法没有返回值，funcReuslt为undefined
                funcResult = instance[options].call(instance, args);
            }
        });

        return funcResult === undefined ? jqObj : funcResult;
    };

})(jQuery, window, document);