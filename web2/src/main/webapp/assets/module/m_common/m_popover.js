;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_popover",
        defaults = {
            clearOnInit: true,//初始化是否清掉其他的Popover
            titleHtml: null,
            hideArrow: false,
            popoverStyle: '',//父容器popover的追加样式，如弹窗宽度
            contentStyle: 'padding: 10px 14px 10px;',//popover-content的追加样式
            content: null,//自定义内容，可以用模板
            placement: null,//浮窗是在哪个位置展开：‘left’,‘right’,‘top’,‘bottom’,空值则默认为top
            onShown: null,//浮窗显示后的事件，可以用来重新绑定值
            onSave: null,//提交事件
            onClear: null,//清除事件
            onClose: null,//关闭事件
            template: 'm_common/m_popover',//主框架,
            type:null,//默认浮窗,inline=内置
            hideElement:false,//隐藏该标签
            closeOnDocumentClicked: null//自定义样式类处理关闭editable
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this._popoverTop = null;//初始化页面时，保存浮窗的top值
        this._popoverHeight = null;//初始化页面时，保存浮窗的height值
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            if (that.settings.clearOnInit === true) {
                //清掉其他的Popover
                $(document).find('.m-popover').each(function (i, o) {
                    $(o).remove();
                });

                $(document).off('click.m-popover');
            }

            var html = template(that.settings.template, {
                titleHtml: that.settings.titleHtml,
                popoverStyle: that.settings.popoverStyle,
                contentStyle: that.settings.contentStyle,
                content: that.settings.content
            });

            $(html).insertAfter($(that.element));
            var $popover = $(that.element).next('.m-popover');

            if (that.settings.hideArrow === true)
                $popover.find('.arrow').eq(0).hide();

            if (that.settings.onShown && that.settings.onShown !== null)
                that.settings.onShown($popover);

            if(that.settings.hideElement === true){
                $(that.element).hide();
            }

            //防止冒泡
            $popover.off('click.m-popover').on('click.m-popover', function (e) {
                stopPropagation(e);
                return false;
            });

            setTimeout(function () {
                if(that.settings.type==null || that.settings.type!='inline'){
                    that.setPosition();
                }
                that.bindBtnClick();
                that.bindPopoverClickedOut();
            }, 50);

            //绑定回车事件
            $popover.find('input[type="text"]').keydown(function () {
                if (event.keyCode == '13') {//keyCode=13是回车键
                    var $btnSubmit = $popover.find('.m-popover-submit');
                    if ($btnSubmit && $btnSubmit.length > 0)
                        $btnSubmit.click();
                }
            });

        },
        //当鼠标点击的焦点不在浮窗内时，关闭浮窗
        bindPopoverClickedOut: function () {
            var that = this;
            $(document).on('click.m-popover', function (e) {
                //console.log('document.clicked');
                var flag = $(e.target).parents('.select2-container').length > 0 || $(e.target).is('.select2-container');
                //防止select2搜索框点击触发关闭
                if ($(e.target).closest('.select2-search__field').length > 0 || flag)
                    return false;

                if (typeof that.settings.closeOnDocumentClicked === 'function') {
                    if (that.settings.closeOnDocumentClicked(e) === false)
                        return false;
                }

                if (that.settings.onClose && that.settings.onClose !== null) {
                    //返回false则不关闭
                    if (that.settings.onClose($(e.target)) !== false)
                        that.closeFilter();
                }
                else {
                    that.closeFilter();
                }
            });
        },
        setPosition: function () {
            var that = this;
            var $popover = $(that.element).next('.m-popover');
            if ($popover.length > 0) {
                var p_p = that.settings.placement ? that.settings.placement : 'top';//浮窗的展示位置
                var a_ptop = $(that.element).position().top;//a标签的top值
                var a_width = $(that.element).outerWidth();//a标签的width值
                var a_height = $(that.element).outerHeight();//a标签的height值
                var a_pleft = $(that.element).position().left;//a标签的left值
                var p_width = $popover.width();//浮窗的宽度
                var p_height = $popover.outerHeight();//浮窗的高度
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


                };
                that._popoverTop = p_top;
                that._popoverHeight = p_height;
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
            }
        },
        bindBtnClick: function () {
            var that = this;
            var $popover = $(that.element).next('.m-popover');
            if ($popover.length > 0) {

                //查找【提交按钮】并绑定事件
                var $btnSubmit = $popover.find('.m-popover-submit');
                if ($btnSubmit.length > 0) {
                    $btnSubmit.click(function (e) {

                        if (that.settings.onSave && that.settings.onSave !== null) {
                            if (that.settings.onSave($popover) !== false)
                                that.closeFilter();
                        }
                        else
                            that.closeFilter();

                        stopPropagation(e);
                        return false;
                    });
                }

                //查找【清除按钮】并绑定事件
                var $btnClear = $popover.find('.m-popover-clear');
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

                //查找【关闭按钮】并绑定事件
                var $btnClose = $popover.find('.m-popover-close');
                if ($btnClose.length > 0) {
                    $btnClose.click(function (e) {

                        if (that.settings.onClose && that.settings.onClose !== null) {
                            //返回false则不关闭
                            if (that.settings.onClose($popover) !== false)
                                that.closeFilter();
                        }
                        else {
                            that.closeFilter();
                        }

                        stopPropagation(e);
                        return false;
                    });
                }
                /*var $btnSubmit = $popover.find('.m-popover-submit');
                 //点击submit按钮或浮窗其他地方，出现验证信息时，相应改变popover的top值
                 $popover.find('.popover-content,button,input').off('click.changePosition').on('click.changePosition',function(e){
                 setTimeout(function(){
                 that.changePosition($popover);
                 },20);
                 });
                 //点击input表单，出现验证信息时，相应改变popover的top值
                 $popover.find('input[type="text"]').off('keyup.changePosition').on('keyup.changePosition',function(e){
                 setTimeout(function(){
                 console.log(2222)
                 that.changePosition($popover);
                 },20);
                 });*/
                $popover.resize(function () {
                    setTimeout(function () {
                        that.changePosition($popover);
                    }, 20);
                });
            }
        },
        //通过改变弹窗的top值来改变弹窗的位置
        changePosition: function ($popover) {
            var that = this;
            var errTag = $popover.find('label.error').length;
            var errTagH = errTag * ($popover.find('label.error').outerHeight() - 0);
            var popH = $popover.height();
            var popT = $popover.position().top;
            var h1 = popH - that._popoverHeight;

            if (that.settings.placement != null && that.settings.placement == 'top') {//当为top，popoverTop需要设置
                if (h1 == -4) {
                    $popover.css('top', that._popoverTop + 'px');
                } else {
                    var newTop = that._popoverTop - h1 - 4;
                    $popover.css('top', newTop);
                }

            } else if (that.settings.placement != null && (that.settings.placement == 'left' || that.settings.placement == 'right')) {

                $popover.find('.arrow').css('top', (that._popoverHeight) / 2 + 'px');
            }
        },
        closeFilter: function () {
            var that = this;
            $(that.element).siblings('.m-popover').each(function (i, o) {
                $(o).remove();
            });
            $(document).off('click.m-popover');
            if(that.settings.hideElement === true){
                $(that.element).show();
            }
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


