/**
 * 工时汇总
 * Created by wrb on 2018/1/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_summary_workingHours",
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
        this._companyList = null;//组织列表
        this._companyListBySelect = null;//筛选组织
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

            var html = template('m_summary/m_summary_workingHours',{});
            $(that.element).html(html);
            that.renderDataList();

        }
        //渲染list
        ,renderDataList:function () {
            var that = this;

            var option = {};
            option.param = {};
            option.param.currentCompanyId=window.currentCompanyId;

            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-box',
                url: restApi.url_getProjectWorking,
                params: option.param
            }, function (response) {

                if (response.code == '0') {

                    var html = template('m_summary/m_summary_workingHours_list',{
                        dataList:response.data.data
                    });
                    $(that.element).find('.data-list-container').html(html);
                    that.bindActionClick();

                } else {
                    S_dialog.error(response.info);
                }
            });
        }

        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                var projectId = $this.attr('data-id');

                switch (dataAction){
                    case 'viewDetail'://查看详情
                        location.hash = '/workingHoursSummary/detail?projectId='+projectId+'&projectName='+encodeURI($this.text());
                        break;
                }
            });
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
