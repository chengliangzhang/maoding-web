/**
 * 收支总览-收支明细-菜单
 * Created by wrb on 2017/11/30.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_detail_menu",
        defaults = {
            $contentEle:null,
            $isFirstEnter:false//是否是第一次進來
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
            that.initHtmlData();
        }
        //初始化数据并加载模板
        ,initHtmlData:function () {
            var that = this;

            var html = template('m_payments/m_payments_detail_menu',{});
            $(that.element).html(html);

            var dataAction = $(that.element).find('li:eq(0)').attr('id');
            $(that.element).find('ul.secondary-menu-ul li[id="'+dataAction+'"]').addClass('active').siblings().removeClass('active');
            that.switchPage(dataAction);
            that.menuClickFun();

        }
        //菜单点击事件
        , menuClickFun:function () {
            var that = this;
            $(that.element).find('.secondary-menu-ul li a').on('click',function () {
                var id = $(this).parent().attr('id');
                $(this).parent().addClass('active').siblings().removeClass('active');
                that.switchPage(id);
            });
        }
        //切换页面
        , switchPage: function (dataAction) {
            var that = this;
            switch (dataAction) {
                case 'ledger'://台账
                    that.ledger();
                    break;
                case 'receivable'://应收
                    that.receivable();
                    break;
                case 'payable'://应付
                    that.payable();
                    break;
                default:
                    dataAction = 'ledger';
                    that.ledger();
                    break;
            }
        }
        //台账
        , ledger: function () {
            var that = this;
            $(that.element).find('#content-box').m_payments_ledger();
        }
        //应收
        , receivable:function () {
            var that = this;
            $(that.element).find('#content-box').m_payments_receivable();
        }
        //应付
        , payable:function () {
            var that = this;
            $(that.element).find('#content-box').m_payments_payable();
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
