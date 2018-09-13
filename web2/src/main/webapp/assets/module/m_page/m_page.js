/**
 * Created by Wuwq on 2017/1/19.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_page",
        defaults = {
            loadingId: null,
            pageIndex: 0,
            pageSize: 10,
            total: 0,
            pageBtnCount: 5,
            showFirstLastBtn: false,
            firstBtnText: null,
            lastBtnText: null,
            /*prevBtnText: "&laquo;",
             nextBtnText: "&raquo;",*/
            prevBtnText: '上一页',
            nextBtnText: '下一页',
            loadFirstPage: true,
            remote: {
                url: null,
                params: null,
                pageParams: null,
                success: null,
                beforeSend: null,
                complete: null,
                pageIndexName: 'pageIndex',
                pageSizeName: 'pageSize',
                totalName: 'data.total',
                traditional: false,
                remoteWrongFormat: null
            },
            pageElementSort: ['$page', '$size', '$jump', '$info'],
            showInfo: false,
            infoFormat: '{start} ~ {end} of {total} entires',
            noInfoText: '0 entires',
            showJump: false,
            jumpBtnText: 'Go',
            showPageSizes: false,
            pageSizeItems: [5, 10, 15, 20],
            debug: false
        };

    function Plugin(element, options) {
        this.element = element;
        var remote = $.extend({}, defaults.remote, options.remote);
        this.settings = $.extend({}, defaults, options);
        this.settings.remote = remote;
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            if (that.settings.loadingId !== null && that.settings.remote.beforeSend === null) {
                that.settings.remote.beforeSend = function () {
                    S_loading.show(that.settings.loadingId, '正在加载中...');
                }
            }
            if (that.settings.loadingId !== null && that.settings.remote.complete === null) {
                that.settings.remote.complete = function () {
                    S_loading.close(that.settings.loadingId);
                }
            }

            if (that.settings.remote.remoteWrongFormat === null) {
                that.settings.remote.remoteWrongFormat = function (res) {
                    if (res && res.code === '500')
                        S_layer.error('很抱歉，请求发生异常');
                }
            }

            if ($(that.element).pagination())
                $(that.element).pagination('destroy');
            $(that.element).pagination(that.settings);
        }
    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            /*if ( !$.data( this, "plugin_" + pluginName ) ) {
             $.data( this, "plugin_" +
             pluginName, new Plugin( this, options ) );
             }*/
            new Plugin(this, options);
        });
    };

})(jQuery, window, document);