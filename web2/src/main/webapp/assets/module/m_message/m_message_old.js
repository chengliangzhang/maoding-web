/**
 * Created by Wuwq on 2017/3/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_message_old",
        defaults = {};

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
        },
        _renderHtml: function (data) {
            var that = this;
            var $container = $(that.element).find('.messageList:first');
            $.each(data.messageList, function (i, o) {
                var $ul = $container.find('ul[data-createDate="' + dateSpecShortFormat(o.createDate) + '"]');
                if ($ul.length === 0) {
                    var group = template('m_message/m_message_group', {createDate: o.createDate});
                    $container.append(group);
                    $ul = $container.find('ul[data-createDate="' + dateSpecShortFormat(o.createDate) + '"]');
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

                                //that._bindBtnHandle();
                                that._bindBtnPageNext(data);
                            }

                        } else {
                            S_layer.error(response.info);
                        }
                    }
                }
            });
        }
        , _bindBtnHandle: function () {
            var that = this;
            $('.btn-handle-msg').click(function () {
                var $btn = $(this);
                var messageType = $btn.attr('data-messageType');
                var companyId = $btn.attr('data-companyId');
                var projectId = $btn.attr('data-projectId');
                var targetId = $btn.attr('data-targetId');
                var expNo = $btn.attr('data-expNo');

                //跳转页面
                var gotoPage = function (url, url_sc) {

                    var option  = {};
                    option.url = restApi.url_isUserInOrg+'/'+window.currentUserId+'/'+companyId;
                    m_ajax.get(option,function (response) {
                        if(response.code=='0'){

                            if(response.data!=null && response.data.isUserInOrg){
                                if (companyId !== window.currentCompanyId) {
                                    $btn.m_popover({
                                        placement: 'top',
                                        content: template('m_common/m_popover_confirm', {confirmMsg: '该消息不属于当前团队，查看会自动切换团队，仍要继续吗？'}),
                                        onSave: function ($popover) {
                                            window.location.href = url_sc;
                                        }
                                    }, true);
                                }
                                else {
                                    window.location.href = url;
                                }
                            }else{
                                S_toastr.warning('不好意思，你不在此组织，暂不能查看！');
                            }


                        }else {
                            S_layer.error(response.info);
                        }
                    });


                };

                var url;
                var url_sc;
                switch (messageType) {
                    case '1'://被设置为乙方经营负责人
                    case '2'://被设置为乙方项目负责人
                        url = _.sprintf(restApi.url_projectInformation + '/%s/1', projectId);
                        url_sc = _.sprintf(restApi.url_companyProjectInformation + '/%s/%s/1', companyId, projectId);
                        gotoPage(url, url_sc);
                        break;
                    case '3'://被设置为经营负责人
                    case '5'://被设置为合作方的经营负责人
                        url = _.sprintf(restApi.url_projectInformation + '/%s/2', projectId);
                        url_sc = _.sprintf(restApi.url_companyProjectInformation + '/%s/%s/2', companyId, projectId);
                        gotoPage(url, url_sc);
                        break;
                    case '4'://被设置为设计负责人
                    case '6'://被设置为合作方的设计负责人
                    case '7'://被设置为任务负责人
                    case '8'://通知乙方项目负责人，xxx被设置为设、校、审
                    case '9'://通知父节点任务负责人，任务已经完成
                    case '10'://被设置为设、校、审
                        url = _.sprintf(restApi.url_projectInformation + '/%s/3', projectId);
                        url_sc = _.sprintf(restApi.url_companyProjectInformation + '/%s/%s/3', companyId, projectId);
                        gotoPage(url, url_sc);
                        break;
                    case '11'://技术审查费-确认付款任务
                        url = _.sprintf(restApi.url_taskList + '/4', projectId);
                        url_sc = _.sprintf(restApi.url_taskList + '/%s/4', companyId);
                        gotoPage(url, url_sc);
                        break;
                    case '12'://技术审查费
                    case '13'://技术审查费
                    case '23'://技术审查费
                    case '27'://技术审查费
                        url = _.sprintf(restApi.url_projectInformation + '/%s/5', projectId);
                        url_sc = _.sprintf(restApi.url_companyProjectInformation + '/%s/%s/5', companyId, projectId);
                        gotoPage(url, url_sc);
                        break;
                    case '24'://技术审查费
                        url = _.sprintf(restApi.url_taskList + '/5', projectId);
                        url_sc = _.sprintf(restApi.url_taskList + '/%s/5', companyId);
                        gotoPage(url, url_sc);
                        break;
                    case '25'://合作设计费
                        url = _.sprintf(restApi.url_taskList + '/4', projectId);
                        url_sc = _.sprintf(restApi.url_taskList + '/%s/4', companyId);
                        gotoPage(url, url_sc);
                        break;
                    case '14'://合作设计费-确认付款任务
                        url = _.sprintf(restApi.url_taskList + '/4', projectId);
                        url_sc = _.sprintf(restApi.url_taskList + '/%s/4', companyId);
                        gotoPage(url, url_sc);
                        break;
                    case '15'://合作设计费
                    case '16'://合作设计费
                    case '26'://合作设计费
                    case '30'://合作设计费
                        url = _.sprintf(restApi.url_projectInformation + '/%s/6', projectId);
                        url_sc = _.sprintf(restApi.url_companyProjectInformation + '/%s/%s/6', companyId, projectId);
                        gotoPage(url, url_sc);
                        break;
                    case '17'://合同回款-确认到账任务
                        url = _.sprintf(restApi.url_taskList + '/5', projectId);
                        url_sc = _.sprintf(restApi.url_taskList + '/%s/5', companyId);
                        gotoPage(url, url_sc);
                        break;
                    case '18'://合同回款
                        url = _.sprintf(restApi.url_projectInformation + '/%s/4', projectId);
                        url_sc = _.sprintf(restApi.url_companyProjectInformation + '/%s/%s/4', companyId, projectId);
                        gotoPage(url, url_sc);
                        break;
                    case '19': //审批报销
                        url = _.sprintf(restApi.url_toMyChecking + '/%s', expNo);
                        url_sc = _.sprintf(restApi.url_toCompanyMyChecking + '/%s/%s', companyId, expNo);
                        gotoPage(url, url_sc);
                        break;
                    case '20': //我的报销
                        url = _.sprintf(restApi.url_toMyExpense + '/%s', expNo);
                        url_sc = _.sprintf(restApi.url_toCompanyMyExpense + '/%s/%s', companyId, expNo);
                        gotoPage(url, url_sc);
                        break;
                    case '21': //所有子任务已经完成，跳生产安排
                        url = _.sprintf(restApi.url_projectInformation + '/%s/3', projectId);
                        url_sc = _.sprintf(restApi.url_companyProjectInformation + '/%s/%s/3', companyId, projectId);
                        gotoPage(url, url_sc);
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
