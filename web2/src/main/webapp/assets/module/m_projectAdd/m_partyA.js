/**
 * 甲方
 * Created by wrb on 2018/2/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_partyA",
        defaults = {
            $eleId:null
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
            that.initHtml();
        },
        //初始化
        initHtml: function () {
            var that = this;
            var html = template('m_projectAdd/m_partyA', {});
            $(that.element).html(html);
            that.bindSearchPartyAClick();
        }

        //绑定事件
        , bindSearchPartyAClick:function () {
            var that = this;
            $(that.element).find('button[data-action="searchPartyA"]').off('click').on('click',function () {
                var $this = $(this);
                var partyAName = $('input[id="'+that.settings.$eleId+'"]').val();
                partyAName = $.trim(partyAName);
                if(partyAName==''){
                    return;
                }
                var option = {};
                option.url = restApi.url_enterpriseSearch;
                option.classId = '#partyAList';
                option.postData={
                    name:partyAName
                };
                m_ajax.postJson(option, function (response) {
                    if (response.code == '0') {

                        var html = template('m_projectAdd/m_partyA_list', {
                            partyAList:response.data.details
                        });
                        $(that.element).find('#partyAList').html(html);
                        that.bindSelectPartyA();

                    } else {
                        S_dialog.error(response.info);
                    }
                });


            });
        }

        //绑定选择事件
        , bindSelectPartyA:function () {
            var that = this;
            $(that.element).find('a[data-action="selectPartyA"]').off('click').on('click',function () {
                var $this = $(this);
                var dataId = $this.attr('data-id');
                var name = $this.text();

                $('input[id="'+that.settings.$eleId+'"]').val(name);
                $('input[id="'+that.settings.$eleId+'"]').attr('data-id',dataId);
                $(that.element).hide();

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


