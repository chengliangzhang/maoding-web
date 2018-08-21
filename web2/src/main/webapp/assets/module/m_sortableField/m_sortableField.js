/**
 * Created by Wuwq on 2017/3/6.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_sortableField",
        defaults = {
            swapId: null, /*交换区标签Id，用于临时存储当前状态*/
            field: null,
            sortType: '', /* '':没排序 asc：升序 desc：降序*/
            onChanged: null  /*function (sortField,sortType) {}*/
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;

        this.settings.field = this.settings.field || $(this.element).attr('name');

        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._render();
            that._bindClick();
        }
        , _render: function () {
            var that = this;
            var $el = $(that.element);

            $el.addClass('field-sortable');
            $el.append('<span class="field-sort-indicator"></span>');

            //从交换区恢复或初始化状态
            if (that.settings.swapId && that.settings.swapId !== null) {
                var $swap = $(that.settings.swapId);
                if ($swap.length > 0) {
                    if ($swap.attr('data-sortField') === that.settings.field) {

                        var sortType = $swap.attr('data-sortType');
                        that.settings.sortType=sortType;
                        if (sortType === 'asc')
                            $el.addClass('field-sorted-asc');
                        else if (sortType === 'desc')
                            $el.addClass('field-sorted-desc');

                        //去掉其他列的排序状态
                        $el.closest('th').siblings().each(function (i, o) {
                            var $o = $(o);
                            $o.removeClass('field-sorted-asc');
                            $o.removeClass('field-sorted-desc');
                        });
                    }
                }
            }
        }
        , _bindClick: function () {
            var that = this;
            var $el = $(that.element);

            $el.find('.field-sort-indicator').click(function () {

                $el.removeClass('field-sorted-asc');
                $el.removeClass('field-sorted-desc');

                //去掉其他列的排序状态
                $el.closest('th').siblings().each(function (i, o) {
                    var $o = $(o);
                    $o.removeClass('field-sorted-asc');
                    $o.removeClass('field-sorted-desc');
                });

                /* '':没排序 asc：升序 desc：降序*/
                switch (that.settings.sortType) {
                    case '':
                        that.settings.sortType = 'asc';
                        $(that.element).addClass('field-sorted-asc');
                        break;
                    case 'asc':
                        that.settings.sortType = 'desc';
                        $(that.element).addClass('field-sorted-desc');
                        break;
                    case 'desc':
                        that.settings.sortType = '';
                        break;
                }

                //交换区更新状态
                if (that.settings.swapId && that.settings.swapId !== null) {
                    var $swap = $(that.settings.swapId);
                    if ($swap.length > 0) {
                        if(isNullOrBlank(that.settings.sortType))
                        {
                            $swap.removeAttr('data-sortField');
                            $swap.removeAttr('data-sortType');
                        } else{
                            $swap.attr('data-sortField', that.settings.field);
                            $swap.attr('data-sortType', that.settings.sortType);
                        }
                    }
                }

                if (that.settings.onChanged && that.settings.onChanged !== null)
                    that.settings.onChanged(that.settings.field, that.settings.sortType);
            });
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
