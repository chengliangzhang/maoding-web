/**
 * Created by Wuwq on 2017/1/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_popConfirm",
        defaults = {
            title: undefined,
            content: "您确定要执行该操作吗？",
            placement: "top",
            container: "body",
            btnYesText: "确定",
            btnNoText: "取消",
            hasClick: null,
            afterCallBack: null,
            onYes: undefined,
            onNo: undefined
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._last = null;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var popConfirmId = _.uniqueId('popConfirm_');

            var template = null;
            if (that.settings.title)
                template = '<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>';
            else
                template = '<div class="popover" role="tooltip"><div class="arrow"></div><div class="popover-content"></div></div>';

            var hide = function (popConfirmId) {
                $('.popover').each(function (i, o) {
                    if ($(o).find('#' + popConfirmId).length > 0) {
                        $(o).popover('hide');
                    }
                });
            };
            var popoverFunc = function (popConfirmId,template) {
                $('.popover').each(function (i, o) {
                    if ($(o).find('#' + popConfirmId).length == 0)
                        $(o).popover('hide');
                });
                $(that.element).popover({
                    trigger: 'manual',
                    title: that.settings.title,
                    html: true,
                    placement: that.settings.placement,
                    container: that.settings.container,
                    template: template,
                    content: '<div id="' + popConfirmId + '"><p style="font-size:13px;">' + that.settings.content + '</p>' + '<p style="margin-top: 10px; text-align: right;"><button type="button" class="popover-btn-no btn-u btn-u-default btn-u-xs rounded" style="margin-right: 4px !important;">' + that.settings.btnNoText + '</button><button type="button" class="popover-btn-yes btn-u btn-u-red btn-u-xs rounded">' + that.settings.btnYesText + '</button></p></div>'
                }).on('shown.bs.popover.' + popConfirmId, function () {

                    var $popConfirmId = $('#' + popConfirmId);
                    $popConfirmId.find('.popover-btn-yes:eq(0)').one('click', function () {
                        hide(popConfirmId);
                        if (that.settings.onYes)
                            that.settings.onYes(that.element);
                    });

                    $popConfirmId.find('.popover-btn-no:eq(0)').one('click', function () {
                        hide(popConfirmId);
                        if (that.settings.onNo)
                            that.settings.onNo(that.element);
                    });

                    $(document).on('click.' + popConfirmId, function (e) {
                        hide(popConfirmId);
                    });
                    if(that.settings.afterCallBack) return that.settings.afterCallBack();

                }).on('hidden.bs.popover.' + popConfirmId, function () {

                    $(document).off('click.' + popConfirmId);

                }).popover('show');
            };
            if(that.settings.hasClick){
                popoverFunc(popConfirmId,template);
            }else{
                $(that.element).click(function () {
                    popoverFunc(popConfirmId,template);
                });
            }

        }
    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            new Plugin(this, options);
        });
    };

})(jQuery, window, document);
