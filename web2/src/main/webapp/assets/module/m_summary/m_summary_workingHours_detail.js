/**
 * 工时汇总-详情
 * Created by wrb on 2018/1/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_summary_workingHours_detail",
        defaults = {
            $projectId:null,
            $projectName:null
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

            var html = template('m_summary/m_summary_workingHours_detail',{
                projectName:that.settings.$projectName
            });
            $(that.element).html(html);
            that.renderDataList();

        }
        //渲染list
        ,renderDataList:function () {
            var that = this;

            var option = {};
            option.param = {};
            option.param.currentCompanyId=window.currentCompanyId;
            option.param.projectId = that.settings.$projectId;
            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-box',
                url: restApi.url_getProjectWorkingHours,
                params: option.param
            }, function (response) {

                if (response.code == '0') {

                    var html = template('m_summary/m_summary_workingHours_detail_list',{
                        dataList:response.data.data,
                        sum:response.data.sum
                    });
                    $(that.element).find('.data-list-container').html(html);

                } else {
                    S_dialog.error(response.info);
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
