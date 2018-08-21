/**
 * Created by veata on 2016/12/22.
 * it applies in showing data for showPublicNotice or showMessage!
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_showNoticeDetail",
        defaults = {
            noticeId:null,
            companyName:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._noticeInfo = null;
        this._name = pluginName;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.getPageData(function(){
                that.getHtml();
                that.sendNotice();
                that.backToLast();
                that.bindActionClick();
                rolesControl();
            });

        },
        //加载详情页面
        getHtml:function(){
            var that = this;
            var html = template('m_notice/m_showNoticeDetail',{notice:that._noticeInfo});
            $(that.element).html(html);
            $(that.element).find('.mail-box .mail-body').html(that._noticeInfo.noticeContent);
        },
        //返回后台，标记信息已读
        toAttachRead:function(callback){
            var that = this;
            var option = [];
            var newNoticeObj = {};
            newNoticeObj.id = that.settings.noticeId;
            newNoticeObj.noticeStatus = '2';
            option.url = restApi.url_saveNotice;
            option.postData = [];
            option.postData.push(newNoticeObj);
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    if(callback){
                        return callback.call(that);
                    }
                }else {
                    S_dialog.error(response.info);
                }

            });
        },
        //绑定发送公告按钮
        sendNotice:function(){
            var that = this;
            $(that.element).find('a[data-action="sendNotice"]').on('click',function(event){
                //$(that.element).m_publishPublicNotice();
                location.hash = '/announcement/send';
            });
        },
        //绑定返回按钮事件
        backToLast:function(){
            var that = this;
            $(that.element).find('a[data-action="announcement"]').on('click',function(event){
                //通过Cookie记住最近一次定位
                //$('#content-right').m_showPublicNoticeMainContent();
                location.hash = '/announcement';
            });
        },
        //获取详情数据
        getPageData:function(callback){
            var that = this;
            var option = [];
            //option.url = restApi.url_getNoticeByNoticeid+'/'+that.settings.noticeId;
            option.url = restApi.url_notice+'/'+that.settings.noticeId;
            m_ajax.get(option,function (response) {
                if(response.code=='0'){
                    that._noticeInfo = response.data;
                    that.toAttachRead();
                    if(callback){
                        return callback.call(that);
                    }
                }else {
                    S_dialog.error(response.info);
                }

            });
        }
        //事件绑定
        ,bindActionClick:function () {

            var that = this;
            $(that.element).find('a[data-action]').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');

                switch (dataAction){
                    case 'fileDownload'://文件下载
                        var url = restApi.url_downLoadFile+'/'+$this.attr('data-id');
                        window.open(url);
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
