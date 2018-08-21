/**
 * Created by Wuwq on 2017/3/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_message",
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

            var html = template('m_message/m_message', {});
            $(that.element).html(html);
            that._bindPage();
            that._bindActionClick();
        },
        _renderHtml: function (data) {
            var that = this;
            var $container = $(that.element).find('.messageList:first');
            $.each(data.messageList, function (i, o) {
                var $ul = $container.find('div[data-createDate="' + dateSpecShortFormat(o.createDate) + '"]');
                if ($ul.length === 0) {
                    var group = template('m_message/m_message_group', {createDate: o.createDate});
                    $container.append(group);
                    $ul = $container.find('div[data-createDate="' + dateSpecShortFormat(o.createDate) + '"]');
                }
                var item = template('m_message/m_message_single', o);
                $ul.append(item);
            });
        }
        , _renderEmptyHtml: function (data) {
            var that = this;

            var iHtml = template('m_message/m_message_empty', {rootPath: window.rootPath});
            $(that.element).find('.messageList').html(iHtml);

        }
        //绑定事件
        ,_bindActionClick:function () {
            var that = this;

            $(that.element).find('a[data-action]').on('click',function () {
                var $this = $(this);
                var action = $this.attr('data-action');
                switch(action){
                    case 'refreshMsg'://刷新
                        that.init();
                        break;
                }
                return false;
            });
        }
        //瀑布加载（分页原理）
        , _bindPage: function () {

            var that = this;
            var postData = {};

            var $page = $(that.element).find('.m-page:first');
            $page.m_page({
                loadingId: '.messageList',
                pageSize: 20,
                remote: {
                    url: restApi.url_getMessage,
                    params: postData,
                    success: function (response) {
                        if (response.code == '0') {
                            var data = {};

                            data.messageList = response.data.data;
                            data.total = response.data.total;
                            data.pageIndex = $page.pagination('getPageIndex');
                            data.pageSize = $page.pagination('getPageSize');

                            if (!data.messageList || data.messageList.length < 1) {
                                //暂无数据时
                                that._renderEmptyHtml();
                            } else {

                                //渲染页面
                                that._renderHtml(data);

                                that._bindBtnHandle();
                                that._bindBtnPageNext(data);
                            }

                        } else {
                            S_dialog.error(response.info);
                        }
                    }
                }
            });
        }
        , _bindBtnHandle: function () {
            var that = this;
            $(that.element).find('.msg-content a').click(function () {
                var $this = $(this);
                var messageType = $this.closest('.feed-element').attr('data-messageType');
                var companyId = $this.closest('.feed-element').attr('data-companyId');
                var projectId = $this.closest('.feed-element').attr('data-projectId');
                var targetId = $this.closest('.feed-element').attr('data-targetId');
                var expNo = $this.closest('.feed-element').attr('data-expNo');
                var projectName = $this.closest('.feed-element').attr('data-project-name');
                var status = $this.closest('.feed-element').attr('data-status');

                //跳转页面
                var gotoPage = function (url) {

                    if(status!=undefined && status=='1'){
                        S_dialog.alert('当前项目已不存在！');
                        return false;
                    }

                    var option  = {};
                    option.url = restApi.url_isUserInOrg+'/'+window.currentUserId+'/'+companyId;
                    m_ajax.get(option,function (response) {
                        if(response.code=='0'){

                            if(response.data!=null && response.data.isUserInOrg){
                                if (companyId !== window.currentCompanyId) {
                                    S_dialog.confirm('该消息不属于当前团队，查看会自动切换团队，仍要继续吗？',function () {

                                        var option = {};
                                        option.url = restApi.url_switchCompany + '/' + companyId;
                                        m_ajax.getJson(option, function (response) {
                                            if (response.code == '0') {
                                                window.location.reload();
                                                location.href = url;
                                            } else {
                                                S_dialog.error(response.info);
                                            }
                                        })
                                    },function () {
                                    });
                                }
                                else {
                                    window.location.href = url;
                                }
                            }else{
                                S_toastr.warning('不好意思，你不在此组织，暂不能查看！');
                            }


                        }else {
                            S_dialog.error(response.info);
                        }
                    });


                };

                var url;
                switch (messageType) {
                    case '3':
                    case '301':
                    case '302':
                    case '303':
                    case '305':
                        //url = _.sprintf('%s/iWork/home/workbench#/projectDetails/basicInfo?id=%s&projectName=%s', window.rootPath,projectId,encodeURI(projectName));
                        url = _.sprintf('%s/iWork/home/workbench#/projectDetails/taskIssue?id=%s&projectName=%s', window.rootPath,projectId,encodeURI(projectName));
                        gotoPage(url);
                        break;
                    case '401':
                    case '402':
                        url = _.sprintf('%s/iWork/home/workbench#/projectDetails/productionArrangement?id=%s&projectName=%s', window.rootPath,projectId,encodeURI(projectName));
                        gotoPage(url);
                        break;
                    case '403':
                    case '404':
                    case '501':
                    case '502':
                        url = _.sprintf('%s/iWork/home/workbench#/myTask/production?projectId=%s&projectName=%s', window.rootPath,projectId,encodeURI(projectName));
                        gotoPage(url);
                        break;
                    case '801':
                    case '802':
                    case '803':
                        url = _.sprintf('%s/iWork/home/workbench#/personalSettings', window.rootPath);
                        gotoPage(url);
                        break;
                }

            });
        }
        //下一页
        , _bindBtnPageNext: function (data) {
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
