/**
 * Created by Wuwq on 2017/3/6.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_filterableField",
        defaults = {
            filterTagContainer: null, /*存储过滤标签的元素*/
            field: null,
            fieldDisplayName: null,
            filterType: null,
            customProcess: function (popoverOptions, m_filterableField) {
            },
            selectLocalData:null,
            onChanged: null
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
            that._bindClick();
        }
        , _render: function () {
            var that = this;
            var $el = $(that.element);
            $el.addClass('field-filterable');
            $el.append('<span class="field-filter-indicator"></span>');
        }
        , _bindClick: function () {
            var that = this;
            var $el = $(that.element);

            $el.find('.field-filter-indicator').click(function (e) {
                var popoverOptions = {};
                switch (that.settings.filterType) {
                    case 'custom':
                        that.settings.customProcess(popoverOptions, that);
                        break;
                    case 'contain':
                        popoverOptions.placement = 'left';
                        popoverOptions.content = template('m_filterableField/m_filterType_contain');
                        popoverOptions.onShown = function ($popover) {
                            //恢复浮动窗口里的值
                            var filter = $(that.settings.filterTagContainer).m_filterTag('getFilter', that.settings.field);
                            if (filter && filter !== null) {
                                $popover.find('input:first').val(filter.filterValue)
                            }
                        };
                        popoverOptions.onSave = function ($popover) {
                            var filter = {
                                field: that.settings.field,
                                fieldDisplayName: that.settings.fieldDisplayName,
                                filterType: that.settings.filterType,
                                filterValue: $popover.find('input:first').val()
                            };
                            $(that.settings.filterTagContainer).m_filterTag('saveFilter', filter);

                            if (that.settings.onChanged && that.settings.onChanged !== null)
                                that.settings.onChanged(filter);
                        };
                        break;
                    case 'select_local_data':
                        var selectLocalData = that.settings.selectLocalData;
                        popoverOptions.popoverStyle = 'width:296px;min-width:296px';
                        popoverOptions.placement = 'left';
                        popoverOptions.content = template('m_filterableField/m_filterType_select_local_data');
                        popoverOptions.onShown = function ($popover) {
                            var $select = $popover.find('select[name="filter_select"]:first').select2({
                                allowClear: false,
                                language: "zh-CN",
                                minimumResultsForSearch: Infinity,
                                data: selectLocalData
                            });

                            //恢复浮动窗口里的值
                            var filter = $(that.settings.filterTagContainer).m_filterTag('getFilter', that.settings.field);
                            if (filter && filter !== null) {
                                $popover.find('select[name="filter_select"]:first').val(filter.filterValue);
                                $select.trigger('change');
                            }
                        };
                        popoverOptions.onSave = function ($popover) {
                            var filterValue = $popover.find('select[name="filter_select"]:first').find('option:selected').val();
                            var filterValueDisplayName = _.find(selectLocalData, function (o) {
                                return o.id == filterValue
                            }).text;
                            var filter = {
                                field: that.settings.field,
                                fieldDisplayName: that.settings.fieldDisplayName,
                                filterType: that.settings.filterType,
                                filterValue: filterValue,
                                filterValueDisplayName: filterValueDisplayName
                            };
                            $(that.settings.filterTagContainer).m_filterTag('saveFilter', filter);

                            if (that.settings.onChanged && that.settings.onChanged !== null)
                                that.settings.onChanged(filter);
                        };
                        break;
                    case 'select':
                        popoverOptions.placement = 'left';
                        popoverOptions.content = template('m_filterableField/m_filterType_select');
                        popoverOptions.onShown = function ($popover) {
                            //恢复浮动窗口里的值
                            var filter = $(that.settings.filterTagContainer).m_filterTag('getFilter', that.settings.field);
                            if (filter && filter !== null) {

                            }
                        };
                        popoverOptions.onSave = function ($popover) {
                            var filter = {
                                field: that.settings.field,
                                fieldDisplayName: that.settings.fieldDisplayName,
                                filterType: that.settings.filterType,
                                filterValue: null
                            };
                            $(that.settings.filterTagContainer).m_filterTag('saveFilter', filter);

                            if (that.settings.onChanged && that.settings.onChanged !== null)
                                that.settings.onChanged(filter);
                        };
                        break;
                    case 'dateRange':
                        popoverOptions.popoverStyle = 'width:335px;min-width:335px';
                        popoverOptions.placement = 'left';
                        popoverOptions.content = template('m_filterableField/m_filterType_dateRange');
                        popoverOptions.onClear = function ($popover) {
                            var $input = $popover.find('form input');
                            if ($input.length > 0)
                                $input.val('');
                        };
                        popoverOptions.onShown = function ($popover) {
                            //恢复浮动窗口里的值
                            var filter = $(that.settings.filterTagContainer).m_filterTag('getFilter', that.settings.field);

                            $popover.find('.input-group-addon').on('click',function(e){
                                $(this).prev('input').focus();
                                stopPropagation(e);
                            });
                            if (filter && filter !== null) {
                                var split = filter.filterValue.split(',');
                                $popover.find('input[name="filter_startDate"]').val(split[0]);
                                $popover.find('input[name="filter_endDate"]').val(split[1]);
                            }else{ return false};
                        };
                        popoverOptions.onSave = function ($popover) {
                            var startDate = $popover.find('input[name="filter_startDate"]').val();
                            var endDate = $popover.find('input[name="filter_endDate"]').val();
                            var filterValue = startDate + ',' + endDate;
                            if ($.trim(filterValue) == ',') {
                                filterValue = '';
                            }
                            var filter = {
                                field: that.settings.field,
                                fieldDisplayName: that.settings.fieldDisplayName,
                                filterType: that.settings.filterType,
                                filterValue: filterValue
                            };
                            $(that.settings.filterTagContainer).m_filterTag('saveFilter', filter);

                            if (that.settings.onChanged && that.settings.onChanged !== null){
                                that.settings.onChanged(filter);
                            }else{
                                return false
                            }

                        };
                        break;
                }

                $(this).m_popover_filter(popoverOptions, true);

                stopPropagation(e);
                return false;
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
