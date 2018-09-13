/**
 * Created by Wuwq on 2017/3/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_notice",
        defaults = {
            $isFirstEnter:false//是否是第一次进来
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            var html = template('m_notice/m_notice', {});
            $(that.element).html(html);
            that.bindPage();
            that.bindActionClick();
            rolesControl();
        },
        renderHtml: function (data) {
            var that = this;
            var $container = $(that.element).find('.noticeList:first');
            $.each(data.noticeList, function (i, o) {
                var $ul = $container.find('div[data-createDate="' + dateSpecShortFormat(o.noticePublishdate) + '"]');
                if ($ul.length === 0) {
                    var group = template('m_notice/m_notice_group', {noticePublishdate: o.noticePublishdate});
                    $container.append(group);
                    $ul = $container.find('div[data-createDate="' + dateSpecShortFormat(o.noticePublishdate) + '"]');
                }
                var item = template('m_notice/m_notice_single', o);
                $ul.append(item);
            });
        }
        , renderEmptyHtml: function (data) {
            var that = this;

            var iHtml = template('m_notice/m_notice_empty', {rootPath: window.rootPath});
            $(that.element).find('.noticeList').html(iHtml);

        }
        //绑定事件
        ,bindActionClick:function () {
            var that = this;

            $(that.element).find('a[data-action]').on('click',function () {
                var $this = $(this);
                var action = $this.attr('data-action');
                switch(action){
                    case 'sendNotice'://发送公告
                        location.hash = '/announcement/send';
                        break;
                    case 'refreshNotice'://刷新公告
                        that.init();
                        break;
                    case 'attachAllRead':
                        that.attachAllRead($this);
                        break
                }
                return false;
            });
        }
        //为点击进入公告详情绑定事件
        ,bindToNoticeDetail:function(){
            var that = this;
            $(that.element).find('div[data-action="toNoticeDetail"]').on('click',function(event){
                var id = $(this).attr('data-notice-id');//data-notice-id
                location.hash = '/announcement/detail?id='+id;
                return false;

            });
        }
        //标记所有的消息为已读
        ,attachAllRead:function($obj){
            var that = this;
            $(that.element).find('input[name="noticeCk"]').prop('checked',true);
            var option = [];
            option.url = restApi.url_notice+'/'+id;
            option.postData={};
            if($obj.attr('checked')){
                option.postData.noticeStatus = '2';
            }else{
                option.postData.noticeStatus = '0';
            }
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    if(response.data.noticeStatus=='2'){
                        dialog.tips('已成功标记为已读');
                    }else{
                        dialog.tips('已成功标记为未读');
                    }
                }else {
                    S_layer.error(response.info);
                }

            });
        }
        //瀑布加载（分页原理）
        , bindPage: function () {

            var that = this;
            var postData = {};

            var $page = $(that.element).find('.m-page:first');
            $page.m_page({
                loadingId: '.noticeList',
                pageSize: 20,
                remote: {
                    url: restApi.url_getNotice,
                    params: postData,
                    success: function (response) {
                        if (response.code == '0') {

                            var data = {};
                            data.noticeList = response.data.data;
                            data.total = response.data.total;
                            data.pageIndex = $page.pagination('getPageIndex');
                            data.pageSize = $page.pagination('getPageSize');

                            if (!data.noticeList || data.noticeList.length < 1) {
                                //暂无数据时
                                that.renderEmptyHtml();
                            } else {

                                //渲染页面
                                that.renderHtml(data);

                                that.bindToNoticeDetail();
                                that.bindBtnPageNext(data);
                            }

                        } else {
                            S_layer.error(response.info);
                        }
                    }
                }
            });
        }

        //下一页
        , bindBtnPageNext: function (data) {
            var that = this;
            var $page = $(that.element).find('.m-page:first');

            var $btnPageNext = $(that.element).parent().find('.btnPageNext');
            if ((data.pageIndex + 1) * data.pageSize >= data.total) {
                $btnPageNext.hide();
            } else {
                $btnPageNext.blur().show().off('click.btnPageNext').on('click.btnPageNext', function () {
                    $page.pagination('setPageIndex', data.pageIndex + 1).pagination('remote');
                });
            }
        }
    });

    /*
     1.一般初始化（缓存单例）： $('#id').pluginName(initOptions);
     2.强制初始化（无视缓存）： $('#id').pluginName(initOptions,true);
     3.调用方法： $('#id').pluginName('methodName',args);
     */
    $.fn[pluginName] = function (options, args) {
        var instance;
        var funcResult;
        var jqObj = this.each(function () {

            //从缓存获取实例
            instance = $.data(this, "plugin_" + pluginName);

            if (options === undefined || options === null || typeof options === "object") {

                var opts = $.extend(true, {}, defaults, options);

                //options作为初始化参数，若args===true则强制重新初始化，否则根据缓存判断是否需要初始化
                if (args === true) {
                    instance = new Plugin(this, opts);
                } else {
                    if (instance === undefined || instance === null)
                        instance = new Plugin(this, opts);
                }

                //写入缓存
                $.data(this, "plugin_" + pluginName, instance);
            }
            else if (typeof options === "string" && typeof instance[options] === "function") {

                //options作为方法名，args则作为方法要调用的参数
                //如果方法没有返回值，funcReuslt为undefined
                funcResult = instance[options].call(instance, args);
            }
        });

        return funcResult === undefined ? jqObj : funcResult;
    };

})(jQuery, window, document);
