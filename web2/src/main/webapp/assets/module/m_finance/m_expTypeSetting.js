/**
 * 财务类别设置
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_expTypeSetting",
        defaults = {
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
            that.initCategoryList();
        }
        //获取数据并生成页面
        ,initCategoryList: function () {
            var that = this;
            var option = {};
            option.url = restApi.url_expCategory;
            m_ajax.get(option,function (response) {
                if(response.code=='0'){
                    var expTypeList = response.data.expTypeList;
                    var html = template('m_finance/m_expTypeSetting',{expTypeList:expTypeList});
                    $(that.element).html(html);
                    that.addActionClick(expTypeList);
                }else {
                    S_layer.error(response.info);
                }

            });
        }
        //添加点击事件
        ,addActionClick:function(expTypeList){
            var that=this;
            $('a.editExpButton').each(function(){
                var action = $(this).attr('data-action');
                $(this).bind('click',function(event){
                    var options = {};
                    var i = action.substr(action.length-1,1);
                    options.expTypeList = expTypeList;
                    options.expTypeData = expTypeList[i];
                    $(that.element).m_expTypeSetting_edit(options);
                    return false;
                });
            });

        }


    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            // if (!$.data(this, "plugin_" + pluginName)) {
                $.data(this, "plugin_" +
                    pluginName, new Plugin(this, options));
            // }
        });
    };

})(jQuery, window, document);
