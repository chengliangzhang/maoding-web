/**
 * 我的项目-菜单
 * Created by wrb on 2018/1/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_projectList_menu",
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

            var html = template('m_projectList/m_projectList_menu',{});
            $(that.element).html(html);

            var dataAction = $(that.element).find('li:eq(0)').attr('id');

            var cookiesMark = 'cookiesData_'+dataAction+'_'+window.currentCompanyUserId;

            var cookiesData = Cookies.get(cookiesMark);
            if(cookiesData!=undefined){
                cookiesData = $.parseJSON(cookiesData);
                dataAction = cookiesData.dataAction;
            }
            $(that.element).find('ul.secondary-menu-ul li[id="'+dataAction+'"]').addClass('active').siblings().removeClass('active');
            that.switchPage(dataAction);
            that.menuClickFun();
        }
        //菜单点击事件
        , menuClickFun:function () {
            var that = this;
            $(that.element).find('.secondary-menu-ul li').on('click',function () {
                var id = $(this).attr('id');
                Cookies.remove('projectList_cookiesData');
                $(this).addClass('active').siblings().removeClass('active');
                that.switchPage(id);
            });
        }
        //切换页面
        , switchPage: function (dataAction) {
            var that = this;
            switch (dataAction) {
                case 'myProjectList'://我的项目
                    that.myProjectList();
                    break;
                case 'projectOverview'://项目总览
                    that.projectOverview();
                    break;
                default:
                    dataAction = 'myProjectList';
                    that.myProjectList();
                    break;
            }
        }
        //我的项目
        , myProjectList: function () {
            var options = {}, that = this;
            options.dataAction = 'myProjectList';
            options.renderCallback = function (data) {

                if(data.flag!=null && data.flag==1){
                    $(that.element).find('.secondary-menu-ul').show();
                }else{
                    $(that.element).find('.secondary-menu-ul').remove();
                }
            };
            $(that.element).find('#content-box').m_projectList(options, true);
            $(that.element).find('.breadcrumb-box li.active strong').html('我的项目');
        }
        //项目总览
        , projectOverview:function () {
            var options = {}, that = this;
            options.dataAction = 'projectOverview';
            options.renderCallback = function (data) {

                if(data.flag!=null && data.flag==1){
                    $(that.element).find('.secondary-menu-ul').show();
                }else{
                    $(that.element).find('.secondary-menu-ul').remove();
                }
            };
            $(that.element).find('#content-box').m_projectList(options, true);
            $(that.element).find('.breadcrumb-box li.active strong').html('项目总览');
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
