/**
 * 财务设置－TAB菜单
 * Created by wrb on 2018/5/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_finance_settings_menu",
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

            var option = {};
            option.classId = '#content-box';
            option.url = restApi.url_getPermissionOperator;
            option.postData = {};
            m_ajax.postJson(option,function (response) {
               if(response.code=='0'){

                   var html = template('m_finance/m_finance_settings_menu',{role:response.data});
                   $(that.element).html(html);

                   var dataAction = $(that.element).find('ul.secondary-menu-ul li:eq(0)').attr('id');
                   $(that.element).find('ul.secondary-menu-ul li[id="'+dataAction+'"]').addClass('active').siblings().removeClass('active');
                   var menuName = $(that.element).find('ul.secondary-menu-ul li[id="'+dataAction+'"] a').text();
                   $(that.element).find('ol.breadcrumb li.active strong').html(menuName);
                   that.switchPage(dataAction);
                   that.menuClickFun();

               } else{
                   S_layer.error(response.info)
               }
            });
        }
        //菜单点击事件
        , menuClickFun:function () {
            var that = this;
            $(that.element).find('.secondary-menu-ul li').on('click',function () {
                var id = $(this).attr('id');
                $(this).addClass('active').siblings().removeClass('active');
                that.switchPage(id);
                var menuName = $(that.element).find('ul.secondary-menu-ul li[id="'+id+'"] a').text();
                $(that.element).find('.breadcrumb li.active strong').html(menuName);
            });
        }
        //切换页面
        , switchPage: function (dataAction) {
            var that = this;
            switch (dataAction) {
                case 'financeSettings'://财务类别设置
                    that.financeSettings();
                    break;
                case 'feeEntrySettings'://费用录入设置
                    that.feeEntrySettings();
                    break;
                case 'financeBasicSettings'://团队基础财务数据设置
                    that.financeBasicSettings();
                    break;
                case 'costSharingSettings'://费用均摊项设置
                    that.costSharingSettings();
                    break;
                default:
                    dataAction = 'financeSettings';
                    that.feeEntrySettings();
                    break;
            }
        }
        //财务类别设置
        , financeSettings:function () {
            var that = this;
            $(that.element).find('#content-box').m_expTypeSetting();
        }
        //费用录入设置
        , feeEntrySettings:function () {
            var that = this;
            $(that.element).find('#content-box').m_feeEntry_settings();
        }
        //团队基础财务数据设置
        , financeBasicSettings:function () {
            var that = this;
            $(that.element).find('#content-box').m_finance_basic_settings();
        }
        //团队基础财务数据设置
        , costSharingSettings:function () {
            var that = this;
            $(that.element).find('#content-box').m_costSharing_settings();
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
