/**
 * 项目信息－任务签发总览表
 * Created by wrb on 2017/5/13.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_taskIssue_overview",
        defaults = {
            $projectId: null,
            $getCallBack:null//请求渲染html后回滚事件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this._taskIssueList = [];//当前任务列表
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtml();

        },
        //初始化数据,生成html
        initHtml: function () {
            var that = this;
            var html = template('m_taskIssue/m_taskIssue_overview',{});
            $(that.element).html(html);

            //that.renderHeaderInfo();

            var option = {};
            option.classId = '#taskIssueList';
            option.url = restApi.url_getIssueTaskOverview+'/'+that.settings.$projectId;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {

                    that._taskIssueList = response.data;
                    var listHtml = template('m_taskIssue/m_taskIssue_overview_list',{taskIssueList:response.data});
                    $(that.element).find('#taskIssueOverviewList').html(listHtml);
                    $(that.element).find('.tree').treegrid({
                        expanderExpandedClass: 'icon iconfont icon-iconfontttpodicon2',
                        expanderCollapsedClass: 'icon iconfont icon-shouqi',
                        treeColumn: 0
                    });
                    $(that.element).find('a[data-toggle="tooltip"],i[data-toggle="tooltip"]').tooltip();
                    that.bindActionClick();
                    if(that.settings.$getCallBack!=null){
                        that.settings.$getCallBack();
                    }

                } else {
                    S_dialog.error(response.info);
                }
            })
        }
        //渲染头部展示信息
        ,renderHeaderInfo:function () {
            var that=this,option = {};
            option.url = restApi.url_getProjectInfoForTask+'/'+that.settings.$projectId;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {

                    var html = template('m_taskIssue/m_taskIssue_overview_header',response.data);
                    $(that.element).find('#taskIssueOverviewHeader').html(html);

                } else {
                    S_dialog.error(response.info);
                }
            })
        }

        //事件绑定
        , bindActionClick: function () {
            var that = this;
            $(that.element).find('a[data-action]').on('click', function (e) {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                var taskId = $this.closest('tr').attr('data-id'),i=$this.closest('tr').attr('data-i');
                switch (dataAction) {
                    case 'viewPlanTime'://与自己有关的约定时间
                        S_dialog.dialog({
                            title: '约定时间',
                            contentEle: 'dialogOBox',
                            ele:'viewPlanTime'+taskId,
                            lock: 2,
                            quickClose:true,
                            noTriangle:true,
                            width: '420',
                            minHeight:'110',
                            tPadding: '0px',
                            url: rootPath+'/assets/module/m_common/m_dialog.html'

                        },function(d){//加载html后触发

                            var tipsEle = 'div[id="content:'+d.id+'"] .dialogOBox';
                            var currentTaskObj = that._taskIssueList[i];
                            $(tipsEle).html(template('m_taskIssue/m_taskIssue_overview_list_relatedTime',{
                                issuePlanList:currentTaskObj.issuePlanList
                            }));
                            $(tipsEle).parents('.ui-popup').css('z-index','29891016');//layer弹窗下，需要改下z-index
                            $('.ui-popup-backdrop').css('z-index','29891016');//layer弹窗下，需要改下z-index
                        });
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
