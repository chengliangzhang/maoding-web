;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_popover_filter",
        defaults = {
            titleHtml: null,
            popoverStyle: '',
            content: null,
            placement: null,//浮窗是在哪个位置展开：‘left’,‘right’,‘top’,‘bottom’,空值则默认为top
            onShown: null,
            onSave: null,
            onClear: null,
            template: 'm_filterableField/m_popover_filter'
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            //清掉其他列的Popover
            var $table = $(that.element).closest('.table');
            if ($table.length > 0) {
                $table.find('.popover-filter').each(function (i, o) {
                    $(o).remove();
                });
            }

            var html = template(that.settings.template, {
                titleHtml: that.settings.titleHtml,
                popoverStyle: that.settings.popoverStyle
            });
            $(html).insertAfter(that.element);
            var $popover = $(that.element).next('.popover-filter');
            $popover.find('.popover-content').html(that.settings.content);

            //回车键
            $popover.find('.popover-content input').keydown(function (e) {
                if (event.keyCode == 13) {
                    stopPropagation(e);
                    preventDefault(e);
                    return false;
                }
            });

            //防止冒泡
            $popover.off('click.popover-filter').on('click.popover-filter', function (e) {
                stopPropagation(e);
                return false;
            });

            if (that.settings.onShown && that.settings.onShown !== null)
                that.settings.onShown($popover);

            setTimeout(function () {
                that.setPosition();
                that.bindBtnClick();
                that.bindPopoverClickedOut();
            }, 50);
        },
        //当鼠标点击的焦点不在浮窗内时，关闭浮窗
        bindPopoverClickedOut: function () {
            var that = this;
            $(document).on('click.popover-filter', function (e) {
                //console.log('document.clicked');
                var flag = $(e.target).parents('.select2-container').length > 0 || $(e.target).is('.select2-container');
                //防止select2搜索框点击触发关闭
                if ($(e.target).closest('.select2-search__field').length > 0 || flag)
                    return false;

                that.closeFilter();
            });
        },
        setPosition: function () {
            var that = this;
            var $popover = $(that.element).next('.popover-filter');
            if ($popover.length > 0) {
                var p_p = that.settings.placement ? that.settings.placement : 'top';//浮窗的展示位置
                var a_ptop = $(that.element).position().top;//a标签的top值
                var a_width = $(that.element).width();//a标签的width值
                var a_height = $(that.element).height();//a标签的height值
                var a_pleft = $(that.element).position().left;//a标签的left值
                var p_width = $popover.width();//浮窗的宽度
                var p_height = $popover.height();//浮窗的高度
                var p_top = 0;//浮窗的top值
                var p_left = 0;//浮窗的left值
                switch (p_p) {
                    case 'top':
                        p_top = (a_ptop - p_height);
                        p_left = a_pleft + a_width / 2 - p_width / 2;
                        break;
                    case 'bottom':
                        p_top = (a_ptop + a_height);
                        p_left = a_pleft + a_width / 2 - p_width / 2;
                        break;
                    case 'left':
                        p_top = (a_ptop - p_height / 2 + 5);
                        p_left = a_pleft - p_width - 10;
                        break;
                    case 'right':
                        p_top = (a_ptop - p_height / 2 + 7);
                        p_left = a_pleft + a_width;
                        break;
                }

                $popover.removeClass('top').addClass(p_p);
                if (p_p.indexOf('left') > -1 || p_p.indexOf('right') > -1) {
                    $popover.find('.arrow').css({'top': '50%', 'left': ''});
                }

                $popover.css({
                    display: 'inline-block',
                    position: 'absolute',
                    top: p_top,
                    left: p_left
                });


                //自动调整左右超出浏览器窗口的情况
                var left = $popover.offset().left;
                var winWidth = $(window).width();
                var pWidth = $popover.width();
                if (left < 0) {
                    $popover.offset({left: 0});
                }
                else if (left + pWidth > winWidth) {
                    var left = winWidth - pWidth;
                    if (left > 0)
                        $popover.offset({left: left});
                }
            }
        },
        bindBtnClick: function () {
            var that = this;
            var $popover = $(that.element).next('.popover-filter');
            if ($popover.length > 0) {

                var $btnSubmit = $popover.find('.filter-submit');
                if ($btnSubmit.length > 0) {
                    $btnSubmit.click(function (e) {

                        if (that.settings.onSave && that.settings.onSave !== null)
                            that.settings.onSave($popover);

                        that.closeFilter();

                        stopPropagation(e);
                        return false;
                    });
                }

                var $btnClear = $popover.find('.filter-clear');
                if ($btnClear.length > 0) {
                    $btnClear.click(function (e) {

                        //如果没有自定义清除函数，则使用默认
                        if (that.settings.onClear && that.settings.onClear !== null)
                            that.settings.onClear($popover);
                        else {
                            //查找第一个input清空
                            var $input = $(this).closest('form').find('input:first');
                            if ($input.length > 0)
                                $input.val('');
                        }

                        stopPropagation(e);
                        return false;
                    });
                }

                var $btnClose = $popover.find('.filter-close');
                if ($btnClose.length > 0) {
                    $btnClose.click(function (e) {

                        that.closeFilter();

                        stopPropagation(e);
                        return false;
                    });
                }
            }
        },
        closeFilter: function () {
            var that = this;
            $(that.element).siblings('.popover-filter').each(function (i, o) {
                $(o).remove();
            });
            $(document).off('click.popover-filter');
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


