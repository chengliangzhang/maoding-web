/**
 * 我的任务－设校审
 * Created by wrb on 2017/11/10.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_myTask_incomeExpenditure",
        defaults = {
            $projectId:null,
            $myTaskId:null,
            $projectName:null,
            $type:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtml();
        },
        //初始化数据,生成html
        initHtml:function () {

            var that = this;
            var html = template('m_myTask/m_myTask_incomeExpenditure',{
                projectName:that.settings.$projectName
            });
            $(that.element).html(html);
            that.renderPage();
        }
        //加载收支界面
        ,renderPage:function () {

            var that = this;
            var type = that.settings.$type;
            switch (type){

                case '10':
                    var option = {};
                    option.$projectId = that.settings.$projectId;
                    option.$myTaskId =  that.settings.$myTaskId;
                    $(that.element).find('#incomeExpenditureBox').m_contractPayment(option,true);
                    break;
                case '4':
                case '5':
                case '8':
                case '16':
                case '17':
                    var option = {};
                    option.$projectId = that.settings.$projectId;
                    option.$myTaskId =  that.settings.$myTaskId;
                    $(that.element).find('#incomeExpenditureBox').m_technicalReviewFee(option,true);
                    break;
                case '6':
                case '7':
                case '9':
                case '18':
                case '19':
                    var option = {};
                    option.$projectId = that.settings.$projectId;
                    option.$myTaskId =  that.settings.$myTaskId;
                    $(that.element).find('#incomeExpenditureBox').m_cooperativeDesignFee(option,true);
                    break;
                case '20':
                case '21':
                    var option = {};
                    option.$projectId = that.settings.$projectId;
                    option.$myTaskId =  that.settings.$myTaskId;
                    $(that.element).find('#incomeExpenditureBox').m_otherFee(option,true);
                    break;
            }

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
